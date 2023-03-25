package de.svws_nrw.davapi.util.icalendar.recurrence;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;

/**
 * Diese Klasse kapselt die verschiedenen Properties eines iCalendars, welche
 * für wiederkehrende Ereignisse notwendig sind. Darüber hinaus bietet sie
 * Methoden zur Bestimmung des letzten Termins einer begrenzt wiederkehrenden
 * Regel
 * 
 *
 */
public class RecurrenceSet {

	/**
	 * Regeln,welche wiederkehrende Ereignisse definieren.
	 */
	private RRule rrule;
	/** eine Liste von Datumsangaben, an denen ein Ereignis auftritt */
	private DateListProperty rDates;
	/** eine Liste von Ausnahemn für die Regeln und die Daten */
	private DateListProperty exDates;

	/**
	 * Leerer Konstruktor für das RecurrenceSets
	 */
	public RecurrenceSet() {
		// leerer default konstruktor
	}

	/**
	 * @return the rrule
	 */
	public RRule getRrule() {
		return rrule;
	}

	/**
	 * @param rrule the rrule to set
	 */
	public void setRrule(RRule rrule) {
		this.rrule = rrule;
	}

	/**
	 * @return the rDates
	 */
	public DateListProperty getrDates() {
		return this.rDates;
	}

	/**
	 * @return the exDates
	 */
	public DateListProperty getExDates() {
		return this.exDates;
	}

	/**
	 * Berechnet aus dem gegebenen Werten Startzeitpunkt sowie den Regeln
	 * und Daten dieses RecurrenceSets den maximalen Endzeitpunkt eines Ereignis
	 * 
	 * @param dtStart der Startzeitpunkt des Ereignisses
	 * @param tzid    die Zeitzone
	 * @return das letzte Auftreten des Ereignisses gemäß dieses RecurrenceSets oder
	 *         {@link Instant#MAX}, wenn das Ereignis unendlich oft auftritt
	 */
	public Instant getEndOfLastOccurence(Instant dtStart, String tzid) {
		if (this.rrule != null) {
			return rrule.getMaxInstant(dtStart);
		}
		return Collections.max(this.rDates.getDateList()).atStartOfDay(ZoneId.of(tzid)).toInstant();
	}
}
