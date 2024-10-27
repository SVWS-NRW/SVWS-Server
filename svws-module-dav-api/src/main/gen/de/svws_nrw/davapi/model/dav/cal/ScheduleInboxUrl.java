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
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"content"
})
@XmlRootElement(name = "schedule-inbox-URL", namespace = "urn:ietf:params:xml:ns:caldav")
public class ScheduleInboxUrl {

	@XmlMixed
	private List<String> content;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ScheduleInboxUrl() {
		// leer
	}

 	/**
	* Gibt die Liste der String-Inhalte zurück.
	*
	* <p>
	* Diese Methode gibt eine Referenz auf die live-Liste zurück,
	* nicht auf eine Kopie. Änderungen an dieser Liste wirken sich direkt
	* auf das JAXB-Objekt aus. Daher gibt es keine <CODE>set</CODE>-Methode.
	* </p>
	*
	* <p>
	* Um ein neues Element hinzuzufügen, verwenden Sie bitte folgenden Code:
	* </p>
	* <pre>
	*    getContent().add(neuesElement);
	* </pre>
	*
	* <p>
	* In dieser Liste sind Objekte des folgenden Typs erlaubt:
	* {@link String}
	* </p>
	*
	* @return eine modifizierbare Liste von String-Inhalten. Die Liste ist niemals null.
	*/
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

}
