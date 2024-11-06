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
@XmlRootElement(name = "calendar", namespace = "urn:ietf:params:xml:ns:caldav")
public class CalCalendar {

	@XmlMixed
	private List<String> content;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CalCalendar() {
		// leer
	}

	/**
	 * Gibt die Liste des Kalenderinhalts zurück.
	 *
	 * @return Eine Live-Referenz auf die Inhaltsliste. Änderungen an dieser Liste
	 *         werden direkt im JAXB-Objekt reflektiert.
	 *
	 * Hinweis:
	 * - Wenn die Liste null ist, wird eine neue ArrayList erstellt.
	 * - Es gibt keine separate set-Methode für diese Eigenschaft.
	 * - Um neue Elemente hinzuzufügen, verwenden Sie: getContent().add(neuesElement);
	 *
	 * Die Liste kann Objekte vom Typ String enthalten.
	 */
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

}
