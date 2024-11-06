package de.svws_nrw.davapi.model.dav.cs;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlMixed;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


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
@XmlRootElement(name = "getctag", namespace = "http://calendarserver.org/ns/")
public class Getctag {

	@XmlMixed
	private List<String> content;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Getctag() {
		// leer
	}

	/**
	* Gibt die Liste der Inhalte zurück.
	*
	* <p>
	* Diese Methode gibt eine Referenz auf die live-Liste zurück,
	* nicht auf eine Kopie. Änderungen an dieser Liste wirken sich direkt
	* auf das JAXB-Objekt aus. Daher gibt es keine <CODE>set</CODE>-Methode
	* für die Eigenschaft content.
	*
	* <p>
	* Um ein neues Element hinzuzufügen, verwenden Sie bitte folgenden Code:
	* <pre>
	*    getContent().add(neuesElement);
	* </pre>
	*
	* <p>
	* In dieser Liste sind Objekte des folgenden Typs erlaubt:
	* {@link String }
	*
	* @return eine modifizierbare Liste von Inhalten.
	*/
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

}
