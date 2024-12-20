//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2015.09.04 at 07:26:18 PM PDT
//


package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{DAV:}response" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{DAV:}responsedescription" minOccurs="0"/&gt;
 *         &lt;element ref="{DAV:}sync-token" minOccurs="0"/&gt;
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
		"response",
		"responsedescription",
		"syncToken"
})
@XmlRootElement(name = "multistatus")
public class Multistatus {

	/**
	 * Liste von Response-Objekten.
	 */
	@XmlElement(required = true)
	protected List<Response> response;

	/**
	 * Beschreibung der Antwort.
	 */
	protected String responsedescription;

	/**
	 * Das Synchronisations-Token.
	 */
	@XmlElement(name = "sync-token")
	@XmlSchemaType(name = "anyURI")
	protected String syncToken;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Multistatus() {
		// leer
	}

	/**
	* Gibt die Liste der Response-Inhalte zurück.
	*
	* <p>
	* Diese Zugriffsmetode gibt eine Referenz auf die live-Liste zurück,
	* nicht auf eine Kopie. Daher werden alle Änderungen, die Sie an der
	* zurückgegebenen Liste vornehmen, im JAXB-Objekt sichtbar sein.
	* Aus diesem Grund gibt es keine <CODE>set</CODE>-Methode.
	* </p>
	*
	* <p>
	* Um beispielsweise ein neues Element hinzuzufügen, tun Sie Folgendes:
	* </p>
	* <pre>
	*    getResponse().add(neuesElement);
	* </pre>
	*
	* <p>
	* Objekte des folgenden Typs sind in der Liste erlaubt:
	* {@link Response }
	* </p>
	*
	* @return eine modifizierbare Liste von Response-Inhalten. Die Liste ist niemals null.
	*/
	public List<Response> getResponse() {
		if (response == null) {
			response = new ArrayList<>();
		}
		return this.response;
	}

	/**
	 * Liefert den Wert von {@link #responsedescription}.
	 *
	 * @return den Wert von {@link #responsedescription}.
	 */
	public String getResponsedescription() {
		return responsedescription;
	}

	/**
	 * Setzt den Wert von {@link #responsedescription}.
	 *
	 * @param value   der zu setzende Wert von {@link #responsedescription}.
	 */
	public void setResponsedescription(final String value) {
		this.responsedescription = value;
	}

	/**
	 * Liefert den Wert von {@link #syncToken}.
	 *
	 * @return den Wert von {@link #syncToken}.
	 */
	public String getSyncToken() {
		return syncToken;
	}

	/**
	 * Setzt den Wert von {@link #syncToken}.
	 *
	 * @param value   der zu setzende Wert von {@link #syncToken}.
	 */
	public void setSyncToken(final String value) {
		this.syncToken = value;
	}

}
