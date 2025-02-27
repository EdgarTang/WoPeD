/*
 *
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.qualanalysis.p2t;

import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;
import javax.swing.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.bpel.Partnerlink;
import org.woped.core.model.petrinet.*;
import org.woped.gui.translations.Messages;
import org.woped.pnml.*;
import org.woped.pnml.NetType.Page;
import org.woped.pnml.TextType.Phrase;

// TODO: Nur kopiert aus FileInterface und geringfügig angepasst

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *     <br>
 *     <p>Created on: 13.01.2005 Last Change on: 13.01.2005
 */
public class PNMLExport {
  private static final String comment =
      "\n"
          + "PLEASE DO NOT EDIT THIS FILE\n"
          + "Created with Workflow PetriNet Designer Version 3.2.0 (woped.org)\n";
  private PnmlDocument pnmlDoc = null;

  /**
   * Method saveToWebFile. Saves a PetriNet Object to a Bytestream.
   *
   * @param editor the editor
   * @param os the output stream to write
   */
  public boolean saveToStream(IEditor editor, ByteArrayOutputStream os) {
    long begin = System.currentTimeMillis();
    try {
      createJavaBeansInstances(editor);
      XmlOptions opt = new XmlOptions();
      opt.setUseDefaultNamespace();
      opt.setSavePrettyPrint();
      opt.setSavePrettyPrintIndent(2);
      Map<String, String> map = new HashMap<String, String>();
      map.put("", "pnml.woped.org");
      opt.setSaveImplicitNamespaces(map);

      pnmlDoc.save(os, opt);

      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private void createJavaBeansInstances(IEditor editor) {
    ModelElementContainer elementContainer = editor.getModelProcessor().getElementContainer();
    PetriNetModelProcessor petrinetModel = editor.getModelProcessor();
    pnmlDoc = PnmlDocument.Factory.newInstance();
    PnmlType iPnml = pnmlDoc.addNewPnml();

    // TODO (blackfox) - extend statusbars for simulations

    // ------------------------------
    // pnmlDoc.documentProperties();
    XmlCursor cursor = iPnml.newCursor();
    cursor.insertComment(comment);
    /* ##### NET ##### */
    NetType iNet = iPnml.addNewNet();
    // attr type
    iNet.setType(petrinetModel.getType());
    // attr id
    iNet.setId(petrinetModel.getId());
    // name

    if (petrinetModel.getName() != null) {
      iNet.addNewName().setText(petrinetModel.getName());
    }
    if (ConfigurationManager.getConfiguration().isExportToolspecific()) {
      NetToolspecificType iNetToolSpec = iNet.addNewToolspecific();

      iNetToolSpec.setTool("WoPeD");
      iNetToolSpec.setVersion("1.0"); // TODO Version aus properties �bernehmen!?

      // get PartnerLinks
      TPartnerLinks iPLs = iNetToolSpec.addNewPartnerLinks();
      Iterator<Partnerlink> plist =
          petrinetModel.getElementContainer().getPartnerlinkList().getPartnerlinkList().iterator();
      while (plist.hasNext()) {
        Partnerlink link = plist.next();
        TPartnerLink iPL = iPLs.addNewPartnerLink();
        iPL.setName(link.getName());
        iPL.setPartnerLinkType(link.getPartnerlinkTypeByQName());
        iPL.setPartnerRole(link.getPartnerlinkRole());
        iPL.setMyRole(link.getMyRole());
        iPL.setWSDL(link.getWsdlUrl());
      }

      // get Variables
      TVariables iVs = iNetToolSpec.addNewVariables();
      int VarCounter = 0;
      while (petrinetModel
              .getElementContainer()
              .getVariableList()
              .getBpelCode()
              .sizeOfVariableArray()
          > VarCounter) {
        TVariable iVar = iVs.addNewVariable();
        iVar.setName(
            petrinetModel
                .getElementContainer()
                .getTVariablesList()
                .getVariableArray(VarCounter)
                .getName());
        iVar.setType(
            petrinetModel
                .getElementContainer()
                .getTVariablesList()
                .getVariableArray(VarCounter)
                .getType());
        VarCounter++;
      }

      // scale
      iNetToolSpec.setScale((int) (editor.getGraph().getScale() * 100));

      // graphics
      GraphicsSimpleType iGraphicsNet = iNetToolSpec.addNewBounds();
      // verticalLayout
      iNetToolSpec.setVerticalLayout(editor.isRotateSelected());
      // resources
      ResourcesType iNetResources = iNetToolSpec.addNewResources();
      // Rescources
      ResourceType iResourceType;
      ResourceModel rModelTemp;

      for (Iterator<ResourceModel> iter = petrinetModel.getResources().iterator();
          iter.hasNext(); ) {
        rModelTemp = iter.next();
        iResourceType = iNetResources.addNewResource();
        iResourceType.setName(rModelTemp.getName());
      }
      // Roles

      RoleType iRoleType;
      ResourceClassModel roleModelTemp;
      for (Iterator<ResourceClassModel> iter = petrinetModel.getRoles().iterator();
          iter.hasNext(); ) {
        roleModelTemp = iter.next();
        iRoleType = iNetResources.addNewRole();
        iRoleType.setName(roleModelTemp.getName());
        if (roleModelTemp.getSuperModels() != null) {
          for (Iterator<ResourceClassModel> i = roleModelTemp.getSuperModels(); i.hasNext(); ) {
            ResourceClassModel superMe = i.next();
            SuperModelType newSuper = iRoleType.addNewSuperModel();
            newSuper.setName(superMe.getName());
          }
        }
      }
      // Orga Units
      OrganizationUnitType iOrganizationUnitType;
      ResourceClassModel orgunitModelTemp;
      for (Iterator<ResourceClassModel> iter = petrinetModel.getOrganizationUnits().iterator();
          iter.hasNext(); ) {
        orgunitModelTemp = iter.next();
        iOrganizationUnitType = iNetResources.addNewOrganizationUnit();
        iOrganizationUnitType.setName(orgunitModelTemp.getName());
        if (orgunitModelTemp.getSuperModels() != null) {
          for (Iterator<ResourceClassModel> i = orgunitModelTemp.getSuperModels(); i.hasNext(); ) {
            ResourceClassModel superMe = i.next();
            SuperModelType newSuper = iOrganizationUnitType.addNewSuperModel();
            newSuper.setName(superMe.getName());
          }
        }
      }
      // ResourceMap
      ResourceMappingType iNetResourceMap;
      for (Iterator<String> iter = petrinetModel.getResourceMapping().keySet().iterator();
          iter.hasNext(); ) {
        String tempResourceClass = iter.next();
        Vector<String> values = petrinetModel.getResourceMapping().get(tempResourceClass);
        // TODO check if mapping exists NullPointerExeption bei
        // speicherung ge�nderter orgUnit die keine zugeordnete Resource
        // hat!
        for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
          iNetResourceMap = iNetResources.addNewResourceMapping();
          iNetResourceMap.setResourceClass(tempResourceClass);
          iNetResourceMap.setResourceID(iterator.next().toString());
        }
      }

      // Simulations
      SimulationsType iNetSimulations = iNetToolSpec.addNewSimulations();
      SimulationType iSimulation;
      TransitionsequenceType iTransitionsequence;
      OccuredtransitionType iOccuredTransition;
      for (Iterator<SimulationModel> iter = petrinetModel.getSimulations().iterator();
          iter.hasNext(); ) {
        SimulationModel currSimulation = iter.next();

        // check if current fingerprint of the net equals the imported one
        // if not ask the user if he want's to keep the simulation
        //
        // this check is performed as well on:
        // - fileixport
        // - loading a simulation
        // when you change it here please do at those locations as well
        int answer = 0;
        Date simulationCreationDate = currSimulation.getSavedDate();
        if (!petrinetModel.isLogicalFingerprintEqual(currSimulation.getFingerprint())) {
          Object[] options = {
            Messages.getString("Tokengame.ChangedNetDialog.ButtonKeep"),
            Messages.getString("Tokengame.ChangedNetDialog.ButtonDelete")
          };
          // get the localized message text
          String message = Messages.getString("Tokengame.ChangedNetDialog.Export.Message");
          // fill the message text dynamically with the simulationname and simulationdate
          message = message.replaceAll("##SIMULATIONNAME##", currSimulation.getName());
          message =
              message.replaceAll(
                  "##SIMULATIONDATE##",
                  DateFormat.getDateInstance().format(currSimulation.getSavedDate()));
          answer =
              JOptionPane.showOptionDialog(
                  null,
                  message,
                  Messages.getString("Tokengame.ChangedNetDialog.Title"),
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE,
                  null,
                  options,
                  options[0]);
          // if the user didn't choose one of the buttons but closed the OptionDialog don't drop the
          // simulation
          if (answer == -1) {
            answer = 0;
          }
        }
        if (answer == 0) {
          iSimulation = iNetSimulations.addNewSimulation();
          iSimulation.setId(currSimulation.getId());
          iSimulation.setSimulationname(currSimulation.getName());
          Calendar cal = Calendar.getInstance();
          cal.setTime(simulationCreationDate);
          iSimulation.setSimulationdate(cal);
          iTransitionsequence = iSimulation.addNewTransitionsequence();
          for (Iterator<TransitionModel> iterator =
                  currSimulation.getOccuredTransitions().iterator();
              iterator.hasNext(); ) {
            iOccuredTransition = iTransitionsequence.addNewOccuredtransition();
            iOccuredTransition.setTransitionID((iterator.next()).getId());
          }
          iSimulation.setNetFingerprint(currSimulation.getFingerprint());
        }
      }

      // toolspecific
      for (short i = 0; i < petrinetModel.getUnknownToolSpecs().size(); i++) {
        iNet.addNewToolspecific();
        if (petrinetModel.getUnknownToolSpecs().get(i) instanceof ToolspecificType) {
          iNet.setToolspecificArray(
              iNet.getToolspecificArray().length - 1,
              (NetToolspecificType) petrinetModel.getUnknownToolSpecs().get(i));
        }
      }
    }
    // Now save the root model element container into the
    // NetType XMLBean holding our net
    saveModelElementContainer(iNet, elementContainer);
  }

  /**
   * Dump the specified ModelElementContainer into the specified XMLBeans bean responsible for the
   * net layout This method may be called multiple times: It is called once for the main model (the
   * root net) and recursively for all sub-process ModelElementContainer instances found
   *
   * @param iNet specifies the XMLBeans object representing the PNML section that will store the
   *     specified net
   * @param elementContainer specifies the ModelElementContainer to be stored in the specified
   *     XMLBean
   */
  private void saveModelElementContainer(NetType iNet, ModelElementContainer elementContainer) {
    Iterator<AbstractPetriNetElementModel> root2Iter =
        elementContainer.getRootElements().iterator();
    while (root2Iter.hasNext()) {
      AbstractPetriNetElementModel currentModel = root2Iter.next();
      /* ##### PLACES ##### */
      if (currentModel.getType() == AbstractPetriNetElementModel.PLACE_TYPE) {
        initPlace(iNet.addNewPlace(), (PlaceModel) currentModel);
      } else if (currentModel.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
      /* ##### TRANSITION ##### */ {
        initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);

      } else if (currentModel.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
        // A sub-process is a reference transition with an associated page
        // First, generate the transition itself
        initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);
        // Create the page and add the sub-net to it
        // by calling ourselves recursively
        Page newPage = iNet.addNewPage();
        // Associate the new page with the ID of the sub-process model
        // so it can be assigned back later on when importing the net
        newPage.setId(currentModel.getId());
        // Create a new XMLBean representing the sub-net
        NetType newNet = newPage.addNewNet();

        ModelElementContainer subProcessContainer =
            ((SubProcessModel) currentModel).getSimpleTransContainer();

        EditorLayoutInfo subProcessLayout = subProcessContainer.getEditorLayoutInfo();
        if (subProcessLayout != null) {
          // This sub-process model stores some information about
          // the layout of the subprocessor editor
          // Convert it to XMLBeans information and store it
          NetToolspecificType subPToolSpec = newNet.addNewToolspecific();

          subPToolSpec.setTool("WoPeD");
          subPToolSpec.setVersion("1.0");
          // graphics
          GraphicsSimpleType iGraphicsNet = subPToolSpec.addNewBounds();
          if (subProcessLayout.getSavedSize() != null) {
            DimensionType dim = iGraphicsNet.addNewDimension();
            dim.setX(new BigDecimal(subProcessLayout.getSavedSize().getWidth()));
            dim.setY(new BigDecimal(subProcessLayout.getSavedSize().getHeight()));
          }
          if (subProcessLayout.getSavedLocation() != null) {
            PositionType location = iGraphicsNet.addNewPosition();
            location.setX(new BigDecimal(subProcessLayout.getSavedLocation().getX()));
            location.setY(new BigDecimal(subProcessLayout.getSavedLocation().getY()));
          }
          // Store the width of the tree view
          subPToolSpec.setTreeWidthRight(subProcessLayout.getTreeViewWidthRight());
          subPToolSpec.setOverviewPanelVisible(subProcessLayout.getOverviewPanelVisible());
          subPToolSpec.setTreeHeightOverview(subProcessLayout.getTreeHeightOverview());
          subPToolSpec.setTreePanelVisible(subProcessLayout.getTreePanelVisible());
        }

        // Call ourselves recursively to store the sub-process net
        saveModelElementContainer(newNet, subProcessContainer);
      } else if (currentModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
        // Special handling code for operators:
        // Instead of the operator itself, the inner transitions and places
        // will be written to the PNML file
        // Their location (screen coordinates) are those of the original operator
        // (and also have to be because the operator screen location is not stored separately
        // but restored from its replacement elements)

        OperatorTransitionModel operatorModel = (OperatorTransitionModel) currentModel;
        Iterator<AbstractPetriNetElementModel> simpleTransIter =
            operatorModel
                .getSimpleTransContainer()
                .getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
                .values()
                .iterator();
        while (simpleTransIter.hasNext()) {
          AbstractPetriNetElementModel simpleTransModel = simpleTransIter.next();
          if (simpleTransModel != null // Sometimes the iterator
              // returns null...
              && operatorModel
                      .getSimpleTransContainer()
                      .getElementById(simpleTransModel.getId())
                      .getType()
                  == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE) {
            initTransition(
                iNet.addNewTransition(),
                (TransitionModel)
                    operatorModel
                        .getSimpleTransContainer()
                        .getElementById(simpleTransModel.getId()),
                operatorModel);
          }
        }
        if (operatorModel.getCenterPlace() != null) {
          PlaceType iCenterPlace = initPlace(iNet.addNewPlace(), operatorModel.getCenterPlace());
          initToolspecific(
              iCenterPlace.addNewToolspecific(),
              operatorModel.getCenterPlace(),
              operatorModel.getId(),
              operatorModel.getOperatorType());
        }
      }
    }

    /* ##### ARCS ##### */
    exportArcs(iNet, elementContainer);

    /* ##### Textual description ##### */
    saveTextualDescription(iNet, elementContainer);
  }

  /**
   * Exports the arc of the container to the provided output bean.
   *
   * @param netBean the bean to save the arcs to.
   * @param container the ElementContainer which contains the arcs
   */
  void exportArcs(NetType netBean, ModelElementContainer container) {

    // When iterating through our arcs, we remember all
    // transitions that are either source or destination of
    // any arc we encounter
    // Instead of serializing the arc itself, we serialize
    // the "inner arcs" of all such transitions
    // To sort out duplicates, we create a set
    Set<AbstractPetriNetElementModel> connectedTransitions = new HashSet<>();

    for (ArcModel arc : container.getArcMap().values()) {
      AbstractPetriNetElementModel source = container.getElementById(arc.getSourceId());
      AbstractPetriNetElementModel target = container.getElementById(arc.getTargetId());

      // Remember either source or target if it is a transition
      // Please note that one special condition of petri nets is that
      // a transition is never directly connected to another transition
      // so either source or target may be a transition, never both
      if (target.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
        connectedTransitions.add(target);
      } else if (source.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
        connectedTransitions.add(source);
      } else {
        // The current arc is not connected to any transition
        // We do not need to take care of any inner arcs
        // and instead store the currentArc itself
        initArc(netBean.addNewArc(), arc, null);
      }
    }
    // A transition can be a very complex construct consisting
    // of a lot more than just one primitive petri-net transition (e.g.
    // XOR Split, XOR Join, ...
    // When dumping the PNML structure we must create primitive petri-net
    // objects for applications that cannot read our tool specific
    // complex transitions
    // This is why all transitions store a map of primitive transitions
    // with (ID, Object-Reference) entries.
    // For all transitions connected to at least one arc we will
    // dump the internal arcs now instead of the (previously ignored) visible arcs

    for (AbstractPetriNetElementModel operator : connectedTransitions) {
      exportInnerArcs(netBean, container, (OperatorTransitionModel) operator);
    }
  }

  /**
   * Exports all inner arcs of the given operator transition.
   *
   * <p>The method first checks for each inner arc if an corresponding outer arc exists. If so, it
   * exports the attributes of the outer arc together with the inner arc, such as way points, arc
   * weight, and so on.
   *
   * <p>Not all arcs have an corresponding outer arc. For example, all arcs connected to the center
   * place of an {@link XORJoinSplitOperatorTransitionModel}.
   *
   * @param netBean the output bean for the petrinet
   * @param operator the operator
   */
  private void exportInnerArcs(
      NetType netBean, ModelElementContainer container, OperatorTransitionModel operator) {

    for (ArcModel innerArc : operator.getSimpleTransContainer().getArcMap().values()) {
      ArcModel outerArc = getOuterArc(container, operator.getId(), innerArc);
      initArc(netBean.addNewArc(), (outerArc != null) ? outerArc : innerArc, innerArc);
    }
  }

  /**
   * Gets the corresponding outer arc to the given inner arc.
   *
   * <p>The arc is pointing to or from an inner element, which does not exists in the outer
   * container. The other element can exist in the outer container. If either the source or the
   * target of the arc exists in the outer container, there should also exist a corresponding outer
   * arc. If not, the arc is only a connection between 2 inner elements.
   *
   * @param container the container which could contain the outer arc
   * @param operatorId the id of the operator which contains the inner arc.
   * @param innerArc the inner arc to get the outer arc for.
   * @return the corresponding outer arc or {@code null}, if no such arc exists.
   */
  private ArcModel getOuterArc(
      ModelElementContainer container, String operatorId, ArcModel innerArc) {
    ArcModel outerArc = null;

    if (container.containsElement(innerArc.getSourceId())) {
      outerArc = container.findArc(innerArc.getSourceId(), operatorId);
    }
    if (container.containsElement(innerArc.getTargetId())) {
      outerArc = container.findArc(operatorId, innerArc.getTargetId());
    }
    return outerArc;
  }

  private PlaceType initPlace(PlaceType iPlace, PlaceModel currentModel) {
    // Name
    initNodeName(iPlace.addNewName(), currentModel.getNameModel());
    // initNodeName
    initElementGraphics(iPlace.addNewGraphics(), currentModel);
    // initalMarkings
    if (currentModel.getTokenCount() > 0) {
      iPlace.addNewInitialMarking().setText(String.valueOf(currentModel.getTokenCount()));
    }
    // toolspecific
    if (ConfigurationManager.getConfiguration().isExportToolspecific()) {
      for (short i = 0; i < currentModel.getUnknownToolSpecs().size(); i++) {
        iPlace.addNewToolspecific();
        if (currentModel.getUnknownToolSpecs().get(i) instanceof ToolspecificType) {
          iPlace.setToolspecificArray(
              iPlace.getToolspecificArray().length - 1,
              (PlaceToolspecificType) currentModel.getUnknownToolSpecs().get(i));
        }
        // TODO 1 Hochz�hlen
      }
    }
    // attr. id
    iPlace.setId(currentModel.getId());

    return iPlace;
  }

  private TransitionType initTransition(
      TransitionType iTransition,
      TransitionModel currentModel,
      OperatorTransitionModel operatorModel) {
    TransitionModel takenModel = operatorModel == null ? currentModel : operatorModel;
    // name
    initNodeName(iTransition.addNewName(), takenModel.getNameModel());
    // graphics
    initElementGraphics(iTransition.addNewGraphics(), takenModel);
    if (ConfigurationManager.getConfiguration().isExportToolspecific()) {
      // toolspecific
      for (short i = 0; i < takenModel.getUnknownToolSpecs().size(); i++) {
        iTransition.addNewToolspecific();
        if (takenModel.getUnknownToolSpecs().get(i) instanceof ToolspecificType) {
          iTransition.setToolspecificArray(
              iTransition.getToolspecificArray().length - 1,
              (TransitionToolspecificType) takenModel.getUnknownToolSpecs().get(i));
        }
        // TODO 1 Hochz�hlen
      }
      initToolspecific(iTransition.addNewToolspecific(), takenModel);
    }
    // attr. id
    iTransition.setId(currentModel.getId());

    return iTransition;
  }

  private NodeNameType initNodeName(NodeNameType nodeName, NameModel element) {
    // name
    nodeName.setText(element.getNameValue());
    /*
     * graphics
     *
     * An annotation's graphics part requires an offset element describing
     * the offset the lower left point of the surrounding text box has to
     * the reference point of the net object on which the annotation occurs.
     * TOD O:
     */
    AnnotationGraphisType iGraphics = nodeName.addNewGraphics();
    PositionType pos = iGraphics.addNewOffset();
    pos.setX(BigDecimal.valueOf(element.getX()));
    pos.setY(BigDecimal.valueOf(element.getY()));

    return nodeName;
  }

  private GraphicsNodeType initElementGraphics(
      GraphicsNodeType iGraphics, AbstractPetriNetElementModel element) {
    DimensionType dim = iGraphics.addNewDimension();
    dim.setX(BigDecimal.valueOf(element.getWidth()));
    dim.setY(BigDecimal.valueOf(element.getHeight()));
    PositionType pos = iGraphics.addNewPosition();
    pos.setX(BigDecimal.valueOf(element.getX()));
    pos.setY(BigDecimal.valueOf(element.getY()));

    return iGraphics;
  }

  private PlaceToolspecificType initToolspecific(
      PlaceToolspecificType iToolspecific,
      PlaceModel currentModel,
      String operatorId,
      int operatorType) {
    iToolspecific.setTool("WoPeD");
    iToolspecific.setVersion("1.0");
    initOperator(iToolspecific.addNewOperator(), operatorId, operatorType);

    return iToolspecific;
  }

  private TransitionToolspecificType initToolspecific(
      TransitionToolspecificType iToolspecific, TransitionModel currentModel) {
    iToolspecific.setTool("WoPeD");
    iToolspecific.setVersion("1.0");
    /*if (org.woped.bpel.gui.transitionproperties.Empty.class.isInstance(currentModel.getBpelData())){
        org.woped.pnml.TEmpty iEmpty = iToolspecific.addNewEmpty();
    	iEmpty.set((XmlObject)((BaseActivity)currentModel.getBpelData()).getActivity());
    }*/
    if (currentModel.getToolSpecific().getOperatorId() != null) {
      initOperator(
          iToolspecific.addNewOperator(),
          currentModel.getToolSpecific().getOperatorId(),
          currentModel.getToolSpecific().getOperatorType());
    }
    if (currentModel.getToolSpecific().getTrigger() != null) {
      initTrigger(iToolspecific.addNewTrigger(), currentModel.getToolSpecific().getTrigger());
    }
    if (currentModel.getToolSpecific().isSubprocess()) {
      iToolspecific.setSubprocess(true);
    }
    if (currentModel.getToolSpecific().getTransResource() != null) {
      initTransResource(
          iToolspecific.addNewTransitionResource(),
          currentModel.getToolSpecific().getTransResource());
    }
    // Store the timing of this transition
    iToolspecific.setTime(currentModel.getToolSpecific().getTime());
    iToolspecific.setTimeUnit(currentModel.getToolSpecific().getTimeUnit());

    // Store the OperatorOrientation
    iToolspecific.setOrientation(currentModel.getToolSpecific().getOperatorPosition().ordinal());
    return iToolspecific;
  }

  private OperatorType initOperator(OperatorType iOperator, String id, int type) {
    // attr. id
    iOperator.setId(id);
    // attr. type
    iOperator.setType(type);
    return iOperator;
  }

  private TriggerType initTrigger(TriggerType iTrigger, TriggerModel trigger) {
    // attr. id
    iTrigger.setId(trigger.getId());
    // attr. type
    iTrigger.setType(trigger.getTriggertype());
    // graphics
    GraphicsSimpleType iGraphics = iTrigger.addNewGraphics();
    DimensionType dim = iGraphics.addNewDimension();
    dim.setX(BigDecimal.valueOf(trigger.getWidth()));
    dim.setY(BigDecimal.valueOf(trigger.getHeight()));
    PositionType pos = iGraphics.addNewPosition();
    pos.setX(BigDecimal.valueOf(trigger.getX()));
    pos.setY(BigDecimal.valueOf(trigger.getY()));
    return iTrigger;
  }

  private TransitionResourceType initTransResource(
      TransitionResourceType iTransResource, TransitionResourceModel transResource) {
    // set Role & orgUnit
    iTransResource.setOrganizationalUnitName(transResource.getTransOrgUnitName());
    iTransResource.setRoleName(transResource.getTransRoleName());

    // graphics
    GraphicsSimpleType iGraphics = iTransResource.addNewGraphics();
    DimensionType dim = iGraphics.addNewDimension();
    dim.setX(BigDecimal.valueOf(transResource.getWidth()));
    dim.setY(BigDecimal.valueOf(transResource.getHeight()));
    PositionType pos = iGraphics.addNewPosition();
    pos.setX(BigDecimal.valueOf(transResource.getX()));
    pos.setY(BigDecimal.valueOf(transResource.getY()));
    return iTransResource;
  }

  /**
   * Initialize arc dump to XML beans.
   *
   * <p>The access of the method needs to be package local to enable testing.
   *
   * @param outerArc specifies the outer arc to be dumped. The outerArc argument may not be null. It
   *     is the element whose graphics information (way-points) will be dumped
   * @param innerArc specifies the (optional) inner arc to be dumped If !=null, this arc will be
   *     dumped to PNML, together with the graphics information of the specified outerArc
   */
  ArcType initArc(ArcType arcBean, ArcModel outerArc, ArcModel innerArc) {
    ArcModel useArc = innerArc == null ? outerArc : innerArc;

    // inscription
    initNodeName(arcBean.addNewInscription(), outerArc);

    // graphics
    initArcGraphics(arcBean.addNewGraphics(), outerArc);

    // attr. id
    arcBean.setId(outerArc.getId());

    // attr. source
    arcBean.setSource(useArc.getSourceId());

    // attr. target
    arcBean.setTarget(useArc.getTargetId());

    // tool specific
    if (ConfigurationManager.getConfiguration().isExportToolspecific()) {
      ArcToolspecificType iArcTool = arcBean.addNewToolspecific();
      iArcTool.setTool("WoPeD");
      iArcTool.setVersion("1.0");

      if (outerArc.isRoute()) iArcTool.setRoute(true);

      iArcTool.setProbability(outerArc.getProbability());
      iArcTool.setDisplayProbabilityOn(outerArc.displayProbability());

      PositionType probPos = iArcTool.addNewDisplayProbabilityPosition();
      Point2D probPosPoint = outerArc.getProbabilityLabelPosition();
      probPos.setX(BigDecimal.valueOf(probPosPoint.getX()));
      probPos.setY(BigDecimal.valueOf(probPosPoint.getY()));

      iArcTool.setDisplayProbabilityPosition(probPos);

      // unknown parameters
      for (short i = 0; i < outerArc.getUnknownToolSpecs().size(); i++) {
        arcBean.addNewToolspecific();
        if (outerArc.getUnknownToolSpecs().get(i) instanceof ToolspecificType) {
          arcBean.setToolspecificArray(
              arcBean.getToolspecificArray().length - 1,
              (ArcToolspecificType) outerArc.getUnknownToolSpecs().get(i));
        }
      }
    }

    return arcBean;
  }

  /**
   * Sets the arc weight of the exported arc.
   *
   * <p>The access of the method needs to be package local to enable testing.
   *
   * @param nodeName the name element of the export object for the arc.
   * @param element the arc to export
   * @return the initialized export object
   */
  ArcNameType initNodeName(ArcNameType nodeName, ArcModel element) {
    // name
    nodeName.setText(element.getInscriptionValue());

    // label position
    Point2D weightLabelPosition = element.getWeightLabelPosition();
    AnnotationGraphisType iGraphics = nodeName.addNewGraphics();
    PositionType pos = iGraphics.addNewOffset();
    pos.setX(BigDecimal.valueOf(weightLabelPosition.getX()));
    pos.setY(BigDecimal.valueOf(weightLabelPosition.getY()));

    return nodeName;
  }

  private GraphicsArcType initArcGraphics(GraphicsArcType iGraphics, ArcModel arc) {
    // position
    if (arc.getPoints().length > 2) {
      PositionType pos;
      for (int i = 1; i < arc.getPoints().length - 1; i++) {
        pos = iGraphics.addNewPosition();
        pos.setX(BigDecimal.valueOf((int) arc.getPoints()[i].getX()));
        pos.setY(BigDecimal.valueOf((int) arc.getPoints()[i].getY()));
      }
    }
    // line
    // ...none

    return iGraphics;
  }

  private void saveTextualDescription(NetType iNet, ModelElementContainer elementContainer) {

    TextType textType = null;
    if (iNet.isSetText()) {
      iNet.unsetText();
    }

    // table has values
    int tableSize = elementContainer.getParaphrasingModel().getTableSize();
    if (tableSize > 0) {

      textType = iNet.addNewText();

      // write every row of the table to the file
      for (int i = 0; i < tableSize; i++) {
        String[] row = elementContainer.getParaphrasingModel().getElementByRow(i);
        Phrase phrase = textType.addNewPhrase();
        phrase.setIds(row[0].trim());
        phrase.setStringValue(row[1].trim());
      }
    }
  }
}
