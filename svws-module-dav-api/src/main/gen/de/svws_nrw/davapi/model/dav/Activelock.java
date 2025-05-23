//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.4-10/27/2009 06:09 PM(mockbuild)-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2009.12.23 at 06:27:19 PM PST
//


package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.</p>
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{DAV:}lockscope"/&gt;
 *         &lt;element ref="{DAV:}locktype"/&gt;
 *         &lt;element ref="{DAV:}depth"/&gt;
 *         &lt;element ref="{DAV:}owner" minOccurs="0"/&gt;
 *         &lt;element ref="{DAV:}timeout" minOccurs="0"/&gt;
 *         &lt;element ref="{DAV:}locktoken" minOccurs="0"/&gt;
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
    "lockscope",
    "locktype",
    "depth",
    "owner",
    "timeout",
    "locktoken"
})
@XmlRootElement(name = "activelock")
public class Activelock {
	@XmlElement(required = true)
    private Lockscope lockscope;
    @XmlElement(required = true)
    private Locktype locktype;
    @XmlElement(required = true)
    private String depth;
    private Owner owner;
    private String timeout;
    private Locktoken locktoken;

    /**
	 * Leerer Standardkonstruktor.
	 */
	public Activelock() {
		// leer
	}

	/**
     * Gets the value of the lockscope property.
     *
     * @return
     *     possible object is
     *     {@link Lockscope }
     *
     */
    public Lockscope getLockscope() {
        return lockscope;
    }

    /**
     * Sets the value of the lockscope property.
     *
     * @param value
     *     allowed object is
     *     {@link Lockscope }
     *
     */
    public void setLockscope(final Lockscope value) {
        this.lockscope = value;
    }

    /**
     * Gets the value of the locktype property.
     *
     * @return
     *     possible object is
     *     {@link Locktype }
     *
     */
    public Locktype getLocktype() {
        return locktype;
    }

    /**
     * Sets the value of the locktype property.
     *
     * @param value
     *     allowed object is
     *     {@link Locktype }
     *
     */
    public void setLocktype(final Locktype value) {
        this.locktype = value;
    }

    /**
     * Gets the value of the depth property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDepth() {
        return depth;
    }

    /**
     * Sets the value of the depth property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDepth(final String value) {
        this.depth = value;
    }

    /**
     * Gets the value of the owner property.
     *
     * @return
     *     possible object is
     *     {@link Owner }
     *
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     *
     * @param value
     *     allowed object is
     *     {@link Owner }
     *
     */
    public void setOwner(final Owner value) {
        this.owner = value;
    }

    /**
     * Gets the value of the timeout property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTimeout(final String value) {
        this.timeout = value;
    }

    /**
     * Gets the value of the locktoken property.
     *
     * @return
     *     possible object is
     *     {@link Locktoken }
     *
     */
    public Locktoken getLocktoken() {
        return locktoken;
    }

    /**
     * Sets the value of the locktoken property.
     *
     * @param value
     *     allowed object is
     *     {@link Locktoken }
     *
     */
    public void setLocktoken(final Locktoken value) {
        this.locktoken = value;
    }

}
