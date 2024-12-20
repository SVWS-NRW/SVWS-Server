//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.4-10/27/2009 06:09 PM(mockbuild)-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2009.12.23 at 06:27:19 PM PST
//


package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;any/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"href", "property", "all", "authenticated", "unauthenticated", "self"
})
@XmlRootElement(name = "principal")
public class Principal {

	private String href;
	private Property property;
	private All all;
	private Authenticated authenticated;
	private Unauthenticated unauthenticated;
	private Self self;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Principal() {
		// leer
	}

	/**
	 * Liefert den Wert der href-Eigenschaft.
	 *
	 * @return den Wert der href-Eigenschaft als String.
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Setzt den Wert der href-Eigenschaft.
	 *
	 * @param href   der neue Wert der href-Eigenschaft als String.
	 */
	public void setHref(final String href) {
		this.href = href;
	}

	/**
	 * Liefert das {@link Property}-Objekt.
	 *
	 * @return das {@link Property}-Objekt.
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * Setzt das {@link Property}-Objekt.
	 *
	 * @param property   das zu setzende  {@link Property}-Objekt.
	 */
	public void setProperty(final Property property) {
		this.property = property;
	}

	/**
	 * Liefert das {@link All}-Objekt.
	 *
	 * @return das {@link All}-Objekt.
	 */
	public All getAll() {
		return all;
	}

	/**
	 * Setzt das {@link All}-Objekt.
	 *
	 * @param all   das zu setzende  {@link All}-Objekt.
	 */
	public void setAll(final All all) {
		this.all = all;
	}

	/**
	 * Liefert das {@link Authenticated}-Objekt.
	 *
	 * @return das {@link Authenticated}-Objekt.
	 */
	public Authenticated getAuthenticated() {
		return authenticated;
	}

	/**
	 * Setzt das {@link Authenticated}-Objekt.
	 *
	 * @param authenticated   das zu setzende  {@link Authenticated}-Objekt.
	 */
	public void setAuthenticated(final Authenticated authenticated) {
		this.authenticated = authenticated;
	}

	/**
	 * Liefert das {@link Unauthenticated}-Objekt.
	 *
	 * @return das {@link Unauthenticated}-Objekt.
	 */
	public Unauthenticated getUnauthenticated() {
		return unauthenticated;
	}

	/**
	 * Setzt das {@link Unauthenticated}-Objekt.
	 *
	 * @param unauthenticated   das zu setzende  {@link Unauthenticated}-Objekt.
	 */
	public void setUnauthenticated(final Unauthenticated unauthenticated) {
		this.unauthenticated = unauthenticated;
	}

	/**
	 * Liefert das {@link Self}-Objekt.
	 *
	 * @return das {@link Self}-Objekt.
	 */
	public Self getSelf() {
		return self;
	}

	/**
	 * Setzt das {@link Self}-Objekt.
	 *
	 * @param self   das zu setzende  {@link Self}-Objekt.
	 */
	public void setSelf(final Self self) {
		this.self = self;
	}

}
