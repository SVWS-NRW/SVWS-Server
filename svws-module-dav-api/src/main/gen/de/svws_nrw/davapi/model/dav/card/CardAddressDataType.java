package de.svws_nrw.davapi.model.dav.card;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * </p>
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * </p>
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "contentType", "version" })
@XmlRootElement(name = "address-data-type", namespace = "urn:ietf:params:xml:ns:carddav")
public class CardAddressDataType {

	@XmlAttribute(name = "content-type")
	private String contentType;

	@XmlAttribute(name = "version")
	private String version;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CardAddressDataType() {
		// leer
	}

	/**
	 * Gibt den Inhaltstyp zurück.
	 *
	 * @return der Inhaltstyp als String.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Setzt den Inhaltstyp.
	 *
	 * @param contentType   der zu setzende Inhaltstyp.
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/**
	* Gibt die Version zurück.
	*
	* @return die Version als String.
	*/
	public String getVersion() {
		return version;
	}

	/**
	* Setzt die Version.
	*
	* @param version   die zu setzende Version.
	*/
	public void setVersion(final String version) {
		this.version = version;
	}

}
