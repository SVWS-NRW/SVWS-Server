package de.svws_nrw.davapi.model.dav.card;

import jakarta.xml.bind.annotation.*;
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
		"href"
})
@XmlRootElement(name = "addressbook-home-set", namespace = "urn:ietf:params:xml:ns:carddav")
public class AddressbookHomeSet {

	/**
	 * Eine Liste von href-Strings, die die Links darstellen.
	 */
	@XmlElement(required = true, namespace = "DAV:")
	protected List<String> href;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public AddressbookHomeSet() {
		// leer
	}

	/**
	 * Gibt die Liste der href-Inhalte zurück.
	 *
	 * <p>
	 * Diese Zugriffsmethode gibt eine Referenz auf die live-Liste zurück,
	 * nicht auf eine Kopie. Daher sind alle Änderungen, die Sie an der
	 * zurückgegebenen Liste vornehmen, im JAXB-Objekt sichtbar.
	 * Aus diesem Grund gibt es keine <CODE>set</CODE>-Methode.
	 *
	 * <p>
	 * Um beispielsweise ein neues Element hinzuzufügen, gehen Sie wie folgt vor:
	 * <pre>
	 *    getHref().add(neuesElement);
	 * </pre>
	 *
	 * <p>
	 * Objekte des folgenden Typs sind in der Liste erlaubt:
	 * {@link String }
	 *
	 * @return eine modifizierbare Liste von href-Strings. Die Liste ist nie null.
	 */
	public List<String> getHref() {
		if (href == null) {
			href = new ArrayList<>();
		}
		return this.href;
	}

}
