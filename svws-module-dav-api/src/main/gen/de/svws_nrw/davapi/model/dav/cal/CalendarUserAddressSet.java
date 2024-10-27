package de.svws_nrw.davapi.model.dav.cal;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
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
@XmlRootElement(name = "calendar-user-address-set", namespace = "urn:ietf:params:xml:ns:caldav")
public class CalendarUserAddressSet {

	@XmlMixed
	private List<String> content;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CalendarUserAddressSet() {
		// leer
	}

	/**
	* Gibt den Wert der content-Eigenschaft zurück.
	*
	* <p>Diese Zugriffsmethode gibt eine Referenz auf die live-Liste zurück,
	* also keine Kopie. Daher werden alle Änderungen, die Sie an der
	* zurückgegebenen Liste vornehmen, im JAXB-Objekt präsent sein.
	* Aus diesem Grund gibt es keine <CODE>set</CODE>-Methode für die content-Eigenschaft.</p>
	*
	* <p>Um beispielsweise ein neues Element hinzuzufügen, gehen Sie wie folgt vor:</p>
	* <pre>
	*    getContent().add(newItem);
	* </pre>
	*
	* <p>Objekte der folgenden Typ(en) sind in der Liste erlaubt: {@link String}</p>
	*
	* @return eine Liste von Strings, die die Inhalte repräsentieren
	*/
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

}
