package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Diese Klasse repräsentiert ein XML-Element namens "inherited".
 * Sie enthält eine Eigenschaft, die eine URL oder einen Verweis (href) darstellt.
 *
 * <p>Die Klasse ist für die Verwendung mit JAXB (Java Architecture for XML Binding)
 * annotiert, um die Konvertierung zwischen Java-Objekten und XML-Daten zu ermöglichen.</p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"href"
})
@XmlRootElement(name = "inherited")
public class Inherited {

	private String href;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Inherited() {
		// leer
	}

	/**
	 * Liefert den Wert der href-Eigenschaft.
	 *
	 * @return den Wert der href-Eigenschaft als String.
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Setzt den Wert der href-Eigenschaft.
	 *
	 * @param href   der neue Wert der href-Eigenschaft als String.
	 */
	public void setHref(final String href) {
		this.href = href;
	}

}
