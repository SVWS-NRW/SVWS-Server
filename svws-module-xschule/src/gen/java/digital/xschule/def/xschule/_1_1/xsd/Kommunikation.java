//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.15 at 12:28:12 PM CEST 
//


package digital.xschule.def.xschule._1_1.xsd;

import de.xbildung.def.xbildung._1_1.xsd.CodeErreichbarkeit;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Diese Klasse fasst Angaben zur Erreichbarkeit über elektronische Kommunikationskanäle (z.B. Telefon, Fax, E-Mail) zusammen.
 * 
 * <p>Java class for Kommunikation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Kommunikation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="kanal" type="{http://xbildung.de/def/xbildung/1.1/xsd}Code.Erreichbarkeit"/&gt;
 *         &lt;element name="kennung" type="{urn:xoev-de:kosit:xoev:datentyp:din-91379_2022-08}datatypeC"/&gt;
 *         &lt;element name="istDienstlich" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="zusatz" type="{urn:xoev-de:kosit:xoev:datentyp:din-91379_2022-08}datatypeC" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Kommunikation", propOrder = {
    "kanal",
    "kennung",
    "istDienstlich",
    "zusatz"
})
public class Kommunikation {

    @XmlElement(required = true)
    protected CodeErreichbarkeit kanal;
    @XmlElement(required = true)
    protected String kennung;
    protected Boolean istDienstlich;
    protected String zusatz;

    /**
     * Gets the value of the kanal property.
     * 
     * @return
     *     possible object is
     *     {@link CodeErreichbarkeit }
     *     
     */
    public CodeErreichbarkeit getKanal() {
        return kanal;
    }

    /**
     * Sets the value of the kanal property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeErreichbarkeit }
     *     
     */
    public void setKanal(CodeErreichbarkeit value) {
        this.kanal = value;
    }

    /**
     * Gets the value of the kennung property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKennung() {
        return kennung;
    }

    /**
     * Sets the value of the kennung property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKennung(String value) {
        this.kennung = value;
    }

    /**
     * Gets the value of the istDienstlich property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIstDienstlich() {
        return istDienstlich;
    }

    /**
     * Sets the value of the istDienstlich property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIstDienstlich(Boolean value) {
        this.istDienstlich = value;
    }

    /**
     * Gets the value of the zusatz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZusatz() {
        return zusatz;
    }

    /**
     * Sets the value of the zusatz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZusatz(String value) {
        this.zusatz = value;
    }

}
