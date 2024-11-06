package de.svws_nrw.davapi.model.dav.cal;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse repräsentiert die unterstützten Kalenderkomponenten
 * im CalDAV-Protokoll. Sie enthält eine Liste von
 * {@link CalendarComponent}, die die verschiedenen Typen
 * von Kalenderkomponenten beschreibt, die unterstützt werden.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "supported-calendar-component-set", namespace = "urn:ietf:params:xml:ns:caldav")
public class SupportedCalendarComponentSet {

	/** Eine Liste von unterstützten Kalenderkomponenten. */
	@XmlElement(required = true)
	protected List<CalendarComponent> calendarComponents;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SupportedCalendarComponentSet() {
		// leer
	}

	/**
	 * Gibt die Liste der unterstützten Kalenderkomponenten zurück.
	 * Wenn die Liste noch nicht initialisiert wurde, wird sie automatisch erstellt.
	 *
	 * @return Eine Liste von {@link CalendarComponent},
	 *         die die unterstützten Kalenderkomponenten enthält.
	 */
	public List<CalendarComponent> getCalendarComponents() {
		if (calendarComponents == null) {
			calendarComponents = new ArrayList<>();
		}
		return this.calendarComponents;
	}
}
