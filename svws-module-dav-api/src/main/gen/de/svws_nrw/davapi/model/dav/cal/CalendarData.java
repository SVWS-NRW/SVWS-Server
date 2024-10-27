package de.svws_nrw.davapi.model.dav.cal;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlMixed;
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
		"content"
})
@XmlRootElement(name = "calendar-data", namespace = "urn:ietf:params:xml:ns:caldav")
public class CalendarData {

	@XmlMixed
	private List<String> content;

	@XmlAttribute(name = "content-type")
	private String contentType;

	@XmlAttribute(name = "version")
	private String version;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CalendarData() {
		// leer
	}

	/**
	 * Gibt den Inhaltstyp des Kalenders zurück.
	 *
	 * @return der Inhaltstyp des Kalenders
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Setzt den Inhaltstyp des Kalenders.
	 *
	 * @param contentType   der zu setzende Inhaltstyp
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gibt die Version des Kalenders zurück.
	 *
	 * @return die Version des Kalenders
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Setzt die Version des Kalenders.
	 *
	 * @param version   die zu setzende Version
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * Gibt die Liste der Kalenderinhalte zurück.
	 *
	 * <p>Diese Zugriffsmethode gibt eine Referenz auf die aktuelle Liste zurück. Es handelt sich also nicht im eine Kopie.
	 * Daher werden alle Änderungen, die Sie an der zurückgegebenen Liste vornehmen, im JAXB-Objekt präsent sein.
	 * Dies ist der Grund, warum es keine <CODE>set</CODE>-Methode für die content-Eigenschaft gibt.</p>
	 *
	 * <p>Um beispielsweise ein neues Element hinzuzufügen, gehen Sie wie folgt vor:</p>
	 *
	 * <pre>
	 * getContent().add(newItem);
	 * </pre>
	 *
	 * <p>Objekte der folgenden Typ(en) sind in der Liste erlaubt: {@link String}</p>
	 *
	 * @return eine Liste von Strings, die den Inhalt des Kalenders repräsentieren
	 */
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}
}
