package de.svws_nrw.davapi.model.dav.card;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlMixed;
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
@XmlType(name = "", propOrder = { "content" })
@XmlRootElement(name = "address-data", namespace = "urn:ietf:params:xml:ns:carddav")
public class CardAddressData {

	@XmlMixed
	private List<String> content;

//	@XmlElement
//	@XmlAttribute(name = "content-type")
	@XmlAttribute(name = "Content-Type")
	private String contentType;

	@XmlAttribute(name = "version")
	private String version;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CardAddressData() {
		// leer
	}

	/**
	 * Gibt den Inhaltstyp der CardAddressData zurück.
	 *
	 * @return der Inhaltstyp als String.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Setzt den Inhaltstyp der CardAddressData.
	 *
	 * @param contentType   der zu setzende Inhaltstyp.
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gibt die Version der CardAddressData zurück.
	 *
	 * @return die Version als String.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Setzt die Version der CardAddressData.
	 *
	 * @param version   die zu setzende Version.
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * Gibt den Inhalt der CardAddressData zurück.
	 *
	 * <p>Diese Methode gibt eine Referenz auf die aktuelle Liste zurück, nicht eine Kopie.
	 * Daher werden alle Änderungen an der zurückgegebenen Liste auch im JAXB-Objekt reflektiert.
	 * Aus diesem Grund gibt es keine <CODE>set</CODE>-Methode für die content-Eigenschaft.</p>
	 *
	 * <p>Um ein neues Element hinzuzufügen, verwenden Sie folgendes Beispiel:</p>
	 *
	 * <pre>
	 * getContent().add(neuesElement);
	 * </pre>
	 *
	 * <p>Die Liste kann Objekte vom Typ {@link String} enthalten.</p>
	 *
	 * @return eine Liste von Strings, die den Inhalt repräsentieren. Wenn die Liste noch nicht existiert, wird eine neue erstellt.
	 */
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

}
