package de.svws_nrw.davapi.model.dav.card;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.davapi.model.dav.Prop;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{DAV:}sync-token"/&gt;
 *         &lt;element ref="{DAV:}sync-level"/&gt;
 *         &lt;element ref="{DAV:}limit" minOccurs="0"/&gt;
 *         &lt;element ref="{DAV:}prop"/&gt;
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
		"prop",
		"href"
})
@XmlRootElement(name = "addressbook-multiget", namespace = "urn:ietf:params:xml:ns:carddav")
public class AddressbookMultiget {

	/**
	 * Das Prop-Objekt, das die Eigenschaften für die Multiget-Anfrage enthält.
	 */
	@XmlElement(required = true, namespace = "DAV:")
	protected Prop prop;

	/**
	 * Liste von Href-Strings, die die Ressourcen für die Multiget-Anfrage identifizieren.
	 */
	@XmlElement(required = false, namespace = "DAV:")
	protected List<String> href;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public AddressbookMultiget() {
		// leer
	}

	/**
	 * Gibt das Prop-Objekt zurück.
	 *
	 * @return Das Prop-Objekt mit den Eigenschaften für die Multiget-Anfrage.
	 */
	public Prop getProp() {
		return prop;
	}

	/**
	 * Setzt das Prop-Objekt.
	 *
	 * @param value Das zu setzende Prop-Objekt.
	 */
	public void setProp(final Prop value) {
		this.prop = value;
	}

	/**
	 * Gibt die Liste der Href-Strings zurück.
	 * Wenn die Liste noch nicht existiert, wird eine neue erstellt.
	 *
	 * @return Die Liste der Href-Strings.
	 */
	public List<String> getHref() {
		if (href == null) {
			href = new ArrayList<>();
		}
		return this.href;
	}

}
