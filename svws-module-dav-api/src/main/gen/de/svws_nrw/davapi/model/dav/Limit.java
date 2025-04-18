//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2015.09.22 at 01:09:41 PM PDT
//


package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.*;
import java.math.BigInteger;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{DAV:}nresults"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nresults"
})
@XmlRootElement(name = "limit")
public class Limit {

	/**
	 * Das Resultat als {@link BigInteger}-Objekt.
	 */
    @XmlElement(required = true)
    protected BigInteger nresults;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Limit() {
		// leer
	}

    /**
     * Gets the value of the nresults property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getNresults() {
        return nresults;
    }

    /**
     * Sets the value of the nresults property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setNresults(final BigInteger value) {
        this.nresults = value;
    }

}
