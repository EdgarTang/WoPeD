//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.18 at 08:57:29 AM CEST 
//


package org.woped.file.yawlinterface.yawlmodel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExternalNetElementFactsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExternalNetElementFactsType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.yawlfoundation.org/yawlschema}ExternalNetElementType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.yawlfoundation.org/yawlschema}LabelType" minOccurs="0"/>
 *         &lt;element name="documentation" type="{http://www.yawlfoundation.org/yawlschema}DocumentationType" minOccurs="0"/>
 *         &lt;element name="flowsInto" type="{http://www.yawlfoundation.org/yawlschema}FlowsIntoType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExternalNetElementFactsType", propOrder = {
    "name",
    "documentation",
    "flowsInto"
})
@XmlSeeAlso({
    ExternalConditionFactsType.class,
    ExternalTaskFactsType.class
})
public class ExternalNetElementFactsType
    extends ExternalNetElementType
{

    protected String name;
    protected String documentation;
    @XmlElement(required = true)
    protected List<FlowsIntoType> flowsInto;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the documentation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * Sets the value of the documentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentation(String value) {
        this.documentation = value;
    }

    /**
     * Gets the value of the flowsInto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flowsInto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlowsInto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlowsIntoType }
     * 
     * 
     */
    public List<FlowsIntoType> getFlowsInto() {
        if (flowsInto == null) {
            flowsInto = new ArrayList<FlowsIntoType>();
        }
        return this.flowsInto;
    }

}
