//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.18 at 08:57:29 AM CEST 
//


package org.woped.file.yawlinterface.yawlmodel;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourcingNonHumanType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResourcingNonHumanType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="nonHumanResource"/>
 *     &lt;enumeration value="nonHumanCategory"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResourcingNonHumanType")
@XmlEnum
public enum ResourcingNonHumanType {

    @XmlEnumValue("nonHumanResource")
    NON_HUMAN_RESOURCE("nonHumanResource"),
    @XmlEnumValue("nonHumanCategory")
    NON_HUMAN_CATEGORY("nonHumanCategory");
    private final String value;

    ResourcingNonHumanType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResourcingNonHumanType fromValue(String v) {
        for (ResourcingNonHumanType c: ResourcingNonHumanType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
