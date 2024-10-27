package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Diese Klasse repräsentiert ein geschütztes Element im DAV-Modell.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "protected")
public class Protected {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Protected() {
		// leer
	}

}
