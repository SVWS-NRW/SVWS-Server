//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.15 at 12:28:12 PM CEST 
//


package digital.xschule.def.xschule._1_1.xsd;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Diese Klasse ermöglicht die Übermittlung von Anlagen. XSchule legt sich nicht abschließend darauf fest, wie Anlagen übermittelt werden sollen. Wird die Anlage nicht in die XML-Datei eingebettet, wird davon ausgegangen, dass sie über den Dateinamen und das verwendete Übertragungs- bzw. Auslieferungsverfahren eindeutig identifiziert werden kann.
 * 
 * <p>Java class for Anlage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Anlage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dateiname" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="bezeichnung" type="{urn:xoev-de:kosit:xoev:datentyp:din-91379_2022-08}datatypeC" minOccurs="0"/&gt;
 *         &lt;element name="mediaType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="binaerdarstellung" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Anlage", propOrder = {
    "dateiname",
    "bezeichnung",
    "mediaType",
    "binaerdarstellung"
})
public class Anlage {

    @XmlElement(required = true)
    protected String dateiname;
    protected String bezeichnung;
    protected String mediaType;
    protected byte[] binaerdarstellung;

    /**
     * Gets the value of the dateiname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateiname() {
        return dateiname;
    }

    /**
     * Sets the value of the dateiname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateiname(String value) {
        this.dateiname = value;
    }

    /**
     * Gets the value of the bezeichnung property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /**
     * Sets the value of the bezeichnung property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBezeichnung(String value) {
        this.bezeichnung = value;
    }

    /**
     * Gets the value of the mediaType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Sets the value of the mediaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaType(String value) {
        this.mediaType = value;
    }

    /**
     * Gets the value of the binaerdarstellung property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBinaerdarstellung() {
        return binaerdarstellung;
    }

    /**
     * Sets the value of the binaerdarstellung property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBinaerdarstellung(byte[] value) {
        this.binaerdarstellung = value;
    }

}
