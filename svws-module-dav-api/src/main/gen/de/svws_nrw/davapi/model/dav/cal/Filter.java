package de.svws_nrw.davapi.model.dav.cal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Diese Klasse repräsentiert einen Filter im CalDAV-Protokoll.
 * Ein Filter kann ein {@link CompFilter}-Objekt enthalten, das zur
 * Spezifizierung von Filterkriterien für Kalenderkomponenten dient.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "filter", namespace = "urn:ietf:params:xml:ns:caldav")
public class Filter {

	/**
	 * Das {@link CompFilter}-Objekt, das in diesem Filter enthalten ist.
	 */
	@XmlElement(name = "comp-filter")
	protected CompFilter compFilter;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Filter() {
		// leer
	}

	/**
	 * Gibt das aktuell enthaltene {@link CompFilter}-Objekt zurück.
	 *
	 * @return das aktuelle {@link CompFilter}-Objekt oder {@code null},
	 *         wenn kein Filter gesetzt ist.
	 */
	public CompFilter getCompFilter() {
		return compFilter;
	}

	/**
	 * Setzt das {@link CompFilter}-Objekt für diesen Filter.
	 *
	 * @param compFilter das {@link CompFilter}-Objekt, das gesetzt werden soll.
	 */
	public void setCompFilter(final CompFilter compFilter) {
		this.compFilter = compFilter;
	}

}
