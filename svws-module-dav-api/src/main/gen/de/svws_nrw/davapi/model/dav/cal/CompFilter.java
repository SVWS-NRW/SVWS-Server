package de.svws_nrw.davapi.model.dav.cal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Diese Klasse repräsentiert einen Filter für Komponenten im CalDAV-Protokoll.
 * Sie ermöglicht das Filtern von Kalenderkomponenten basierend auf ihrem Namen
 * und einem Zeitbereich. Ein Filter kann auch untergeordnete Filter enthalten.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "comp-filter", namespace = "urn:ietf:params:xml:ns:caldav")
public class CompFilter {

	@XmlAttribute(name = "name")
	private String name;

	@XmlElement(name = "time-range")
	private TimeRange timeRange;

	@XmlElement(name = "comp-filter")
	private CompFilter compFilterRef;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CompFilter() {
		// leer
	}

	/**
	 * Gibt den Namen des Filters zurück.
	 * @return der Name des Filters
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Namen des Filters.
	 * @param name   der zu setzende Name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	* Gibt den Zeitbereich des Filters zurück.
	* @return der Zeitbereich des Filters
	*/
	public TimeRange getTimeRange() {
		return timeRange;
	}

	/**
	 * Setzt den Zeitbereich des Filters.
	 * @param timeRange   der zu setzende Zeitbereich
	 */
	public void setTimeRange(final TimeRange timeRange) {
		this.timeRange = timeRange;
	}

	/**
	 * Gibt die Referenz auf den untergeordneten CompFilter zurück.
	 * @return die Referenz auf den untergeordneten CompFilter
	 */
	public CompFilter getCompFilter() {
		return compFilterRef;
	}

	/**
	* Setzt die Referenz auf den untergeordneten CompFilter.
	* @param compFilter   die zu setzende Referenz auf den untergeordneten CompFilter
	*/
	public void setCompFilter(final CompFilter compFilter) {
		this.compFilterRef = compFilter;
	}

}
