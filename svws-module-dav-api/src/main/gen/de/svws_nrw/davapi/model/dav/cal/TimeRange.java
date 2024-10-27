package de.svws_nrw.davapi.model.dav.cal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Die Klasse {@code TimeRange} repräsentiert einen Zeitbereich mit einem
 * Start- und Endzeitpunkt. Sie wird im Rahmen der CalDAV-Protokolls verwendet,
 * um Zeitintervalle zu definieren.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "time-range", namespace = "urn:ietf:params:xml:ns:caldav")
public class TimeRange {

	@XmlAttribute(name = "start")
	private String start;

	@XmlAttribute(name = "end")
	private String end;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public TimeRange() {
		// leer
	}

	/**
	 * Liefert den Wert von {@link #start}.
	 *
	 * @return den Wert von {@link #start}.
	 */
	public String getStart() {
		return start;
	}

	/**
	 * Setzt den Wert von {@link #start}.
	 *
	 * @param start   der zu setzende Wert von {@link #start}.
	 */
	public void setStart(final String start) {
		this.start = start;
	}

	/**
	 * Liefert den Wert von {@link #end}.
	 *
	 * @return den Wert von {@link #end}.
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * Setzt den Wert von {@link #end}.
	 *
	 * @param end   der zu setzende Wert von {@link #end}.
	 */
	public void setEnd(final String end) {
		this.end = end;
	}

}
