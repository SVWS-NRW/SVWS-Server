package de.svws_nrw.davapi.model.dav;

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
@XmlRootElement(name = "current-user-principal", namespace = "DAV:")
public class CurrentUserPrincipal {

	/**
	 * Die Liste der Href-Strings.
	 */
	@XmlElement(required = true)
	protected List<String> href;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CurrentUserPrincipal() {
		// leer
	}

	/**
	 * Liefert eine Liste der href-Strings.
	 *
	 * <p>
	 * Diese Zugriffsmethode gibt eine Referenz auf die Live-Liste zurück,
	 * also keine Kopie. Daher werden alle Änderungen, die Sie an der
	 * zurückgegebenen Liste vornehmen, im JAXB-Objekt präsent sein.
	 * Aus diesem Grund gibt es keine <CODE>set</CODE>-Methode.
	 * </p>
	 *
	 * <p>
	 * Um beispielsweise ein neues Element hinzuzufügen, gehen Sie wie folgt vor:
	 * </p>
	 * <pre>
	 *    getHref().add(neuesElement);
	 * </pre>
	 *
	 * <p>
	 * Objekte der folgenden Typ(en) sind in der Liste erlaubt:
	 * {@link String}
	 * </p>
	 *
	 * @return eine Liste der href-Strings.
	 */
	public List<String> getHref() {
		if (href == null) {
			href = new ArrayList<>();
		}
		return this.href;
	}

}
