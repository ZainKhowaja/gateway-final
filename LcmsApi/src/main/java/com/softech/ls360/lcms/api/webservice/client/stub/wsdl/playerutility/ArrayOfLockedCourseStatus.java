//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.17 at 07:44:26 PM PKT 
//


package com.softech.ls360.lcms.api.webservice.client.stub.wsdl.playerutility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfLockedCourseStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfLockedCourseStatus"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LockedCourseStatus" type="{http://tempuri.org/}LockedCourseStatus" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfLockedCourseStatus", propOrder = {
    "lockedCourseStatus"
})
public class ArrayOfLockedCourseStatus
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "LockedCourseStatus", nillable = true)
    protected List<LockedCourseStatus> lockedCourseStatus;

    /**
     * Gets the value of the lockedCourseStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lockedCourseStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLockedCourseStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LockedCourseStatus }
     * 
     * 
     */
    public List<LockedCourseStatus> getLockedCourseStatus() {
        if (lockedCourseStatus == null) {
            lockedCourseStatus = new ArrayList<LockedCourseStatus>();
        }
        return this.lockedCourseStatus;
    }

}
