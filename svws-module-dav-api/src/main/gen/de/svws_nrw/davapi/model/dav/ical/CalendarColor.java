package de.svws_nrw.davapi.model.dav.ical;

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
@XmlRootElement(name = "calendar-color", namespace = "http://apple.com/ns/ical")
public class CalendarColor {

	@XmlMixed
	private List<String> content;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CalendarColor() {
		// leer
	}

	/**
	 * Gibt die Liste des Inhalts zurück.
	 *
	 * @return Eine Live-Referenz auf die content-Liste. Wenn die Liste noch nicht
	 *         existiert, wird sie erstellt.
	 *
	 * Hinweis: Änderungen an der zurückgegebenen Liste wirken sich direkt auf das
	 * Objekt aus. Es wird keine Kopie erstellt.
	 *
	 * Beispiel zur Verwendung:
	 * <pre>
	 *    calendarColor.getContent().add(neuesElement);
	 * </pre>
	 */
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

}
