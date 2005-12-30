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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.file;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlOptions;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IStatusBar;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.IntPair;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.WoPeDUndoManager;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.pnml.ArcType;
import org.woped.pnml.NetType;
import org.woped.pnml.OrganizationUnitType;
import org.woped.pnml.PlaceType;
import org.woped.pnml.PnmlDocument;
import org.woped.pnml.PnmlType;
import org.woped.pnml.ResourceMappingType;
import org.woped.pnml.ResourceType;
import org.woped.pnml.RoleType;
import org.woped.pnml.TransitionType;

// TODO: BUG in import. When import toolspec mit splitjoin. import ONLY one arc
// !!!
/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The Import Class parses an <code>PetriNet </code> defined in PNML Format. To
 * get more Information about PNML look at <br>
 * <a
 * href="http://www.informatik.hu-berlin.de/top/pnml">http://www.informatik.hu-berlin.de/top/pnml
 * </a> <br>
 * <br>
 * In Order to parse the extended Notation (WF-Nets from W.v.d.Aalst) its
 * necessary to read toolspecific elements additionaly. <br>
 * @see org.woped.editor.core.model.PetriNetModelProcessor <br>
 *      <br>
 *      Created on 29.04.2003 <br>
 *      Last change 05.12.2004 (S.Landes) <br>
 */
public class PNMLImport
{
    private EditorVC[]   editor     = null;

    private PnmlDocument pnmlDoc    = null;

    private XmlOptions   opt        = new XmlOptions();

    private Vector       warnings   = new Vector();

    private IStatusBar[] statusBars = null;

    ApplicationMediator  mediator   = null;

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param statusBar
     */
    public PNMLImport(ApplicationMediator am, IStatusBar[] statusBars)
    {
        opt.setUseDefaultNamespace();
        mediator = am;
        Map map = new HashMap();
        map.put("", "pnml.woped.org");
        this.statusBars = statusBars;
        opt.setLoadSubstituteNamespaces(map);
        if (true)
        {
            opt.setCompileNoUpaRule();
            opt.setCompileNoPvrRule();
            opt.setCompileNoValidation();
        }
    }

    /**
     * Load an XML document using the generated PNMLFactory class
     * 
     * @param filename
     *            An existing XML file name
     */
    public boolean run(String absolutePath)
    {
        InputStream is;
        try
        {
            is = new FileInputStream(absolutePath);
            return run(is);
        } catch (FileNotFoundException e)
        {
            LoggerManager.warn(Constants.FILE_LOGGER, "File does not exists. " + absolutePath);
            return false;
        }

    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param is
     * @return
     */
    public boolean run(InputStream is)
    {
        LoggerManager.debug(Constants.FILE_LOGGER, "##### START PNML Version (1.3.2) IMPORT #####");

        long begin = System.currentTimeMillis();
        try
        {
            pnmlDoc = PnmlDocument.Factory.parse(is, opt);
            createEditorFromBeans();
            if (!warnings.isEmpty())
            {
                LoggerManager.warn(Constants.FILE_LOGGER, "Imported a not valid PNML.");
                StringBuffer warningStrings = new StringBuffer();
                for (Iterator iter = warnings.iterator(); iter.hasNext();)
                {
                    warningStrings.append(iter.next());
                }
                JOptionPane.showMessageDialog(null, "XML-File is not a proper PNML. Imported Petrinet with lost of information:\n".concat(warningStrings.toString()), "Import Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            LoggerManager.warn(Constants.FILE_LOGGER, "   ... Could parse PNML file. Perhaps OLD PNML file-format. When saving, new pnml file-format will be created.");
            // e.printStackTrace();
            /*
             * PNMLImport oldPnml = new PNMLImport(); if
             * (oldPnml.loadExistingInstance(is)) { PetriNet petriNet =
             * oldPnml.getPetriNet(); // TODO build editor return loadSuccess =
             * true; } else { return loadSuccess = false; }
             */
            // return
            return false;
        } finally
        {
            LoggerManager.debug(Constants.FILE_LOGGER, "##### END PNML IMPORT Version (1.3.2) ##### (" + (System.currentTimeMillis() - begin) + " ms)");
        }

    }

    private void createEditorFromBeans() throws Exception
    {
        importNets(pnmlDoc.getPnml());
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @return
     */
    public EditorVC[] getEditor()
    {
        return editor;
    }

    private void importNets(PnmlType pnml) throws Exception
    {

        editor = new EditorVC[pnml.getNetArray().length];
        NetType currentNet;
        Dimension dim;
        Point location;
        PetriNetModelProcessor currentPetrinet;

        for (int i = 0; i < pnml.getNetArray().length; i++)
        {
            currentNet = pnml.getNetArray(i);
            editor[i] = mediator.createEditorVC(AbstractModelProcessor.MODEL_PROCESSOR_PETRINET, true);
            if (((WoPeDUndoManager) editor[i].getGraph().getUndoManager()) != null)
            {
                ((WoPeDUndoManager) editor[i].getGraph().getUndoManager()).setEnabled(false);
            }
            currentPetrinet = ((PetriNetModelProcessor) editor[i].getModelProcessor());
            // attr. id
            currentPetrinet.setId(currentNet.getId());
            // attr. type
            // Type no other types
            // name
            if (currentNet.isSetName())
            {
                currentPetrinet.setName(currentNet.getName().getText());
            }
            if (ConfigurationManager.getConfiguration().isImportToolspecific())
            {
                // toolspecific
                for (int j = 0; j < currentNet.getToolspecificArray().length; j++)
                {
                    if (currentNet.getToolspecificArray(j).getTool().equals("WoPeD"))
                    {
                        if (currentNet.getToolspecificArray(j).isSetBounds())
                        {
                            dim = new Dimension(currentNet.getToolspecificArray(j).getBounds().getDimension().getX().intValue(), currentNet.getToolspecificArray(j).getBounds().getDimension().getY()
                                    .intValue());
                            editor[i].setSavedSize(dim);
                            location = new Point(currentNet.getToolspecificArray(j).getBounds().getPosition().getX().intValue(), currentNet.getToolspecificArray(j).getBounds().getPosition().getY()
                                    .intValue());
                            editor[i].setSavedLocation(location);
                        }
                        if (currentNet.getToolspecificArray(j).isSetResources())
                        {
                            // ResourceMapType resourceMap =
                            // currentNet.getToolspecificArray(j).getResources().getResourceMap();
                            ResourceMappingType[] resourceMaps = currentNet.getToolspecificArray(j).getResources().getResourceMappingArray();

                            RoleType[] roles = currentNet.getToolspecificArray(j).getResources().getRoleArray();
                            ResourceClassModel roleModelTemp;
                            for (int k = 0; k < roles.length; k++)
                            {
                                roleModelTemp = new ResourceClassModel(roles[k].getName(), ResourceClassModel.TYPE_ROLE);
                                currentPetrinet.addRole(roleModelTemp);
                            }

                            OrganizationUnitType[] units = currentNet.getToolspecificArray(j).getResources().getOrganizationUnitArray();
                            ResourceClassModel orgUnitTemp;
                            for (int l = 0; l < units.length; l++)
                            {
                                orgUnitTemp = new ResourceClassModel(units[l].getName(), ResourceClassModel.TYPE_ORGUNIT);
                                currentPetrinet.addOrgUnit(orgUnitTemp);
                            }

                            ResourceType[] resources = currentNet.getToolspecificArray(j).getResources().getResourceArray();
                            ResourceModel resourceModelTemp;
                            for (int m = 0; m < resources.length; m++)
                            {
                                resourceModelTemp = new ResourceModel(resources[m].getName());
                                currentPetrinet.addResource(resourceModelTemp);
                            }

                            for (int n = 0; n < resourceMaps.length; n++)
                            {
                                currentPetrinet.addResourceMapping(resourceMaps[n].getResourceClass(), resourceMaps[n].getResourceID());
                            }
                        }
                    } else
                    {
                        currentPetrinet.addUnknownToolSpecs(currentNet.getToolspecificArray(j));
                    }
                }

            }
            // if (bar != null)
            // {
            // int elements = currentNet.getPlaceArray().length +
            // currentNet.getTransitionArray().length +
            // currentNet.getArcArray().length;
            // setTaskLength(elements);
            // bar.setMaximum(elements);
            // }
            for (int f = 0; f < statusBars.length; f++)
            {
                statusBars[f].startProgress("Loading from File", currentNet.getPlaceArray().length + currentNet.getArcArray().length + currentNet.getTransitionArray().length);
            }
            importPlaces(currentNet.getPlaceArray(), i);
            getEditor()[i].updateNet();
            importTransitions(currentNet.getTransitionArray(), i);
            // important... import arcs in the end
            getEditor()[i].updateNet();
            importArcs(currentNet.getArcArray(), i);
            getEditor()[i].updateNet();
            getEditor()[i].getGraph().clearSelection();
            if (editor[i].getGraph().getUndoManager() != null)
            {
                ((WoPeDUndoManager) editor[i].getGraph().getUndoManager()).setEnabled(true);
            }
        }
    }

    private void importPlaces(PlaceType[] places, int editorIndex) throws Exception
    {
        int tokens;
        CreationMap map;
        int x;
        int y;
        boolean doNOTcreate = false;
        for (int i = 0; i < places.length; i++)
        {
            for (int f = 0; f < statusBars.length; f++)
            {
                statusBars[f].nextStep();
            }
            map = CreationMap.createMap();
            map.setEditOnCreation(false);
            map.setType(PetriNetModelElement.PLACE_TYPE);
            try
            {
                map.setId(places[i].getId());
                map.setPosition(places[i].getGraphics().getPosition().getX().intValue(), places[i].getGraphics().getPosition().getY().intValue());
                if (places[i].getGraphics().isSetDimension()) map.setSize(new IntPair(new Dimension(places[i].getGraphics().getDimension().getX().intValue(), places[i].getGraphics().getDimension()
                        .getY().intValue())));
                map.setName(places[i].getName().getText());
                try
                {
                    /*
                     * TODO Offset if
                     */
                    if (places[i].getName().isSetGraphics() && places[i].getName().getGraphics().getOffsetArray() != null && places[i].getName().getGraphics().getOffsetArray().length > 0)
                    {
                        x = places[i].getName().getGraphics().getOffsetArray(0).getX().intValue();
                        y = places[i].getName().getGraphics().getOffsetArray(0).getY().intValue();
                        map.setNamePosition(x, y);
                    }
                    // if
                    // (places[i].getName().getGraphics().getOffsetArray().length
                    // >
                    // 1)
                    // {
                    // tempWidth =
                    // places[i].getName().getGraphics().getOffsetArray(1).getX().intValue()
                    // -
                    // places[i].getName().getGraphics().getOffsetArray(0).getX().intValue();
                    // tempHeight =
                    // places[i].getName().getGraphics().getOffsetArray(1).getX().intValue()
                    // -
                    // places[i].getName().getGraphics().getOffsetArray(0).getX().intValue();
                    // map.setNameSize(new Dimension(tempWidth, tempHeight));
                    // }

                    if (places[i].isSetInitialMarking() && (tokens = Integer.parseInt(places[i].getInitialMarking().getText())) > 0)
                    {
                        map.setTokens(tokens);
                    }
                    // toolspecific
                    if (ConfigurationManager.getConfiguration().isImportToolspecific())
                    {
                        for (int j = 0; j < places[i].getToolspecificArray().length; j++)
                        {
                            if (places[i].getToolspecificArray(j).getTool().equals("WoPeD"))
                            {
                                if (places[i].getToolspecificArray(j).getOperator().getType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE) doNOTcreate = true;
                            } else
                            {
                                map.addUnknownToolSpec(places[i].getToolspecificArray(j));
                            }
                        }
                    }
                } catch (Exception e)
                {
                    warnings.add("- PLACE LOST INFORMATION (" + places[i].getId() + ") Exception while importing lesser important information.\n");
                }
                if (!doNOTcreate) getEditor()[editorIndex].createElement(map);
                doNOTcreate = false;
                LoggerManager.debug(Constants.FILE_LOGGER, "   ... Place (ID:" + places[i].getId() + ") imported");
            } catch (Exception e)
            {
                warnings.add("- SKIP PLACE: Exception while importing important information.\n");
            }
            // increaseCurrent();

        }
    }

    private void importTransitions(TransitionType[] transitions, int editorIndex) throws Exception
    {
        CreationMap map;
        int x;
        int y;

        for (int i = 0; i < transitions.length; i++)
        {
            for (int f = 0; f < statusBars.length; f++)
            {
                statusBars[f].nextStep();
            }
            map = CreationMap.createMap();
            map.setEditOnCreation(false);
            map.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
            try
            {
                map.setId(transitions[i].getId());
                map.setPosition(transitions[i].getGraphics().getPosition().getX().intValue(), transitions[i].getGraphics().getPosition().getY().intValue());
                if (transitions[i].getGraphics().isSetDimension()) map.setSize(new IntPair(transitions[i].getGraphics().getDimension().getX().intValue(), transitions[i].getGraphics().getDimension()
                        .getY().intValue()));
                map.setName(transitions[i].getName().getText());
                try
                {
                    if (transitions[i].getName().isSetGraphics() && transitions[i].getName().getGraphics().getOffsetArray() != null
                            && transitions[i].getName().getGraphics().getOffsetArray().length > 0)
                    {
                        x = transitions[i].getName().getGraphics().getOffsetArray(0).getX().intValue();
                        y = transitions[i].getName().getGraphics().getOffsetArray(0).getY().intValue();
                        map.setNamePosition(x, y);
                    }
                    if (ConfigurationManager.getConfiguration().isImportToolspecific())
                    {
                        for (int j = 0; j < transitions[i].getToolspecificArray().length; j++)
                        {
                            if (transitions[i].getToolspecificArray(j).getTool().equals("WoPeD"))
                            {
                                if (transitions[i].getToolspecificArray(j).isSetOperator())
                                {
                                    map.setOperatorType(transitions[i].getToolspecificArray(j).getOperator().getType());
                                    map.setType(TransitionModel.TRANS_OPERATOR_TYPE);
                                    map.setId(transitions[i].getToolspecificArray(j).getOperator().getId());
                                } else if (transitions[i].getToolspecificArray(j).isSetSubprocess())
                                {
                                    map.setType(PetriNetModelElement.SUBP_TYPE);
                                }
                                if (transitions[i].getToolspecificArray(j).isSetTrigger())
                                {
                                    map.setTriggerType(transitions[i].getToolspecificArray(j).getTrigger().getType());
                                    // int test
                                    // =transitions[i].getToolspecificArray(j).getTrigger().getType();
                                    // System.out.println("TriggerType:" +test);
                                    map.setTriggerPosition(transitions[i].getToolspecificArray(j).getTrigger().getGraphics().getPosition().getX().intValue(), transitions[i].getToolspecificArray(j)
                                            .getTrigger().getGraphics().getPosition().getY().intValue());
                                }
                                if (transitions[i].getToolspecificArray(j).isSetTransitionResource())
                                {
                                    // &&
                                    // transitions[i].getToolspecificArray(j).getTrigger().getType()
                                    // == 200
                                    map.setResourceOrgUnit(transitions[i].getToolspecificArray(j).getTransitionResource().getOrganizationalUnitName());
                                    map.setResourceRole(transitions[i].getToolspecificArray(j).getTransitionResource().getRoleName());
                                    map.setResourcePosition(transitions[i].getToolspecificArray(j).getTransitionResource().getGraphics().getPosition().getX().intValue(), transitions[i]
                                            .getToolspecificArray(j).getTransitionResource().getGraphics().getPosition().getY().intValue());
                                }

                            } else
                            {
                                map.addUnknownToolSpec(transitions[i].getToolspecificArray(j));
                            }
                        }
                    }
                } catch (Exception e)
                {
                    warnings.add("- TRANSITION LOST INFORMATION (" + transitions[i].getId() + "): Exception while importing lesser important information.");
                }

                if (!getEditor()[editorIndex].getModelProcessor().getElementContainer().containsElement(map.getId()))
                {
                    getEditor()[editorIndex].createElement(map);
                    LoggerManager.debug(Constants.FILE_LOGGER, " ... Transition (ID:" + map.getId() + ")imported");
                    // increaseCurrent();
                }
            } catch (Exception e)
            {
                warnings.add("- SKIP TRANSITION: Exception while importing important information.");
            }
        }
    }

    private void importArcs(ArcType[] arcs, int editorIndex) throws Exception
    {
        PetriNetModelElement currentSourceModel = null;
        PetriNetModelElement currentTargetModel = null;
        ArcModel arc = null;
        for (int i = 0; i < arcs.length; i++)
        {
            try
            {
                for (int f = 0; f < statusBars.length; f++)
                {
                    statusBars[f].nextStep();
                }
                currentSourceModel = (PetriNetModelElement) getEditor()[editorIndex].getModelProcessor().getElementContainer().getElementById(arcs[i].getSource());
                currentTargetModel = (PetriNetModelElement) getEditor()[editorIndex].getModelProcessor().getElementContainer().getElementById(arcs[i].getTarget());
                String tempID;

                if (ConfigurationManager.getConfiguration().isImportToolspecific())
                {
                    try
                    {
                        if (currentTargetModel == null && currentSourceModel != null)
                        {
                            if (arcs[i].getTarget().indexOf(OperatorTransitionModel.INNERID_SEPERATOR) != 0)
                            {
                                tempID = arcs[i].getTarget().substring(0, arcs[i].getTarget().indexOf(OperatorTransitionModel.INNERID_SEPERATOR));
                            } else
                            {
                                tempID = arcs[i].getTarget().substring(0, arcs[i].getTarget().indexOf(OperatorTransitionModel.INNERID_SEPERATOR_OLD));
                            }
                            if (isOperator(getEditor()[editorIndex].getModelProcessor(), tempID))
                            {
                                arc = getEditor()[editorIndex].createArc(arcs[i].getSource(), tempID);
                            }
                        }
                        if (currentSourceModel == null && currentTargetModel != null)
                        {
                            if (arcs[i].getSource().indexOf(OperatorTransitionModel.INNERID_SEPERATOR) != 0)
                            {
                                tempID = arcs[i].getSource().substring(0, arcs[i].getSource().indexOf(OperatorTransitionModel.INNERID_SEPERATOR));
                            } else
                            {
                                tempID = arcs[i].getSource().substring(0, arcs[i].getSource().indexOf(OperatorTransitionModel.INNERID_SEPERATOR_OLD));
                            }

                            if (isOperator(getEditor()[editorIndex].getModelProcessor(), tempID))
                            {
                                arc = getEditor()[editorIndex].createArc(tempID, arcs[i].getTarget());
                            }
                        }
                        if (currentTargetModel != null && currentSourceModel != null)
                        {
                            arc = getEditor()[editorIndex].createArc(arcs[i].getSource(), arcs[i].getTarget());
                        }
                        // toolspecific
                        for (int j = 0; j < arcs[i].getToolspecificArray().length; j++)
                        {
                            if (arcs[i].getToolspecificArray(j).getTool().equals("WoPeD"))
                            {
                                if (arcs[i].getToolspecificArray(j).isSetRoute() && arcs[i].getToolspecificArray(j).getRoute()) arc.setRoute(true);
                            } else
                            {
                                arc.addUnknownToolSpecs(arcs[i].getToolspecificArray(j));
                            }
                        }
                    } catch (Exception e)
                    {
                        warnings.add("- ARC LOST INFORMATION (" + arcs[i].getId() + "): Exception while importing lesser important information.");
                    }
                } else
                {
                    arc = getEditor()[editorIndex].createArc(arcs[i].getSource(), arcs[i].getTarget());
                }
                if (arcs[i].isSetGraphics() && arc != null)
                {
                    for (int j = 0; j < arcs[i].getGraphics().getPositionArray().length; j++)
                    {
                        arc.addPoint(new Point2D.Double(arcs[i].getGraphics().getPositionArray(j).getX().doubleValue(), arcs[i].getGraphics().getPositionArray(j).getY().doubleValue()), j + 1);
                    }
                }
                LoggerManager.debug(Constants.FILE_LOGGER, " ... Arc (ID:" + arcs[i].getId() + "( " + arcs[i].getSource() + " -> " + arcs[i].getTarget() + ") created");
                // increaseCurrent();
            } catch (Exception e)
            {
                warnings.add("- SKIP ARC: Exception while importing important information.");
            }
        }
    }

    private boolean isOperator(AbstractModelProcessor net, String elementId) throws Exception
    {
        if (elementId != null && net.getElementContainer().getElementById(elementId) != null
                && net.getElementContainer().getElementById(elementId).getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
        {
            return true;
        } else
        {
            return false;
        }
    }

}