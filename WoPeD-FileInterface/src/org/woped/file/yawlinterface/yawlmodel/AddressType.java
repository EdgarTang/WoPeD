//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.18 at 08:57:29 AM CEST 
//


package org.woped.file.yawlinterface.yawlmodel;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="numAndStreet">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="number">
 *                       &lt;simpleType>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                           &lt;pattern value="\d*(/|-)*\d+"/>
 *                         &lt;/restriction>
 *                       &lt;/simpleType>
 *                     &lt;/element>
 *                     &lt;element name="street">
 *                       &lt;simpleType>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                           &lt;pattern value="\D+\s+[S|s|R|r|A|a|C|c|B|b|o](treet|oad|venue|t|d|ve|rescent|lvd|ther)"/>
 *                         &lt;/restriction>
 *                       &lt;/simpleType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="GPOBox" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;/choice>
 *         &lt;element name="suburbOrTown" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postOrZipCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger">
 *               &lt;minInclusive value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = {
    "numAndStreet",
    "gpoBox",
    "suburbOrTown",
    "postOrZipCode",
    "state",
    "country"
})
public class AddressType {

    protected AddressType.NumAndStreet numAndStreet;
    @XmlElement(name = "GPOBox")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger gpoBox;
    protected String suburbOrTown;
    @XmlElement(required = true)
    protected BigInteger postOrZipCode;
    protected String state;
    @XmlElement(required = true)
    protected String country;

    /**
     * Gets the value of the numAndStreet property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType.NumAndStreet }
     *     
     */
    public AddressType.NumAndStreet getNumAndStreet() {
        return numAndStreet;
    }

    /**
     * Sets the value of the numAndStreet property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType.NumAndStreet }
     *     
     */
    public void setNumAndStreet(AddressType.NumAndStreet value) {
        this.numAndStreet = value;
    }

    /**
     * Gets the value of the gpoBox property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGPOBox() {
        return gpoBox;
    }

    /**
     * Sets the value of the gpoBox property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGPOBox(BigInteger value) {
        this.gpoBox = value;
    }

    /**
     * Gets the value of the suburbOrTown property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuburbOrTown() {
        return suburbOrTown;
    }

    /**
     * Sets the value of the suburbOrTown property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuburbOrTown(String value) {
        this.suburbOrTown = value;
    }

    /**
     * Gets the value of the postOrZipCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPostOrZipCode() {
        return postOrZipCode;
    }

    /**
     * Sets the value of the postOrZipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPostOrZipCode(BigInteger value) {
        this.postOrZipCode = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="number">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;pattern value="\d*(/|-)*\d+"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="street">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;pattern value="\D+\s+[S|s|R|r|A|a|C|c|B|b|o](treet|oad|venue|t|d|ve|rescent|lvd|ther)"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "number",
        "street"
    })
    public static class NumAndStreet {

        @XmlElement(required = true)
        protected String number;
        @XmlElement(required = true)
        protected String street;

        /**
         * Gets the value of the number property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumber() {
            return number;
        }

        /**
         * Sets the value of the number property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumber(String value) {
            this.number = value;
        }

        /**
         * Gets the value of the street property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStreet() {
            return street;
        }

        /**
         * Sets the value of the street property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStreet(String value) {
            this.street = value;
        }

    }

}
