package de.svws_nrw.davapi.util.icalendar.recurrence;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;

/**
 * Diese Klasse kapselt die verschiedenen Properties eines iCalendars, welche
 * für wiederkehrende Ereignisse notwendig sind. Darüber hinaus bietet sie
 * Methoden zur Bestimmung des letzten Termins einer begrenzt wiederkehrenden
 * Regel
 */
public class RecurrenceSet {

	/** Regeln,welche wiederkehrende Ereignisse definieren. */
	private RRule rrule;

	/** Eine Liste von Datumsangaben, an denen ein Ereignis auftritt. */
	private DateListProperty rDates;

	/** Eine Liste von Ausnahemn für die Regeln und die Daten. */
	private DateListProperty exDates;

	/**
	 * Leerer Konstruktor für das RecurrenceSets
	 */
	public RecurrenceSet() {
		// leerer default konstruktor
	}

	/**
	 * Liefert das {@link RRule}-Objekt.
	 *
	 * @return das {@link RRule}-Objekt.
	 */
	public RRule getRrule() {
		return rrule;
	}

	/**
	 * Setzt das {@link RRule}-Objekt.
	 *
	 * @param rrule   das zu setzende {@link RRule}-Objekt.
	 */
	public void setRrule(final RRule rrule) {
		this.rrule = rrule;
	}

	/**
	 * Liefert das {@link DateListProperty}-Objekt.
	 *
	 * @return das {@link DateListProperty}-Objekt.
	 */
	public DateListProperty getrDates() {
		return this.rDates;
	}

	/**
	 * Liefert das {@link DateListProperty}-Objekt.
	 *
	 * @return das {@link DateListProperty}-Objekt.
	 */
	public DateListProperty getExDates() {
		return this.exDates;
	}

	/**
	 * Berechnet aus dem gegebenen Werten Startzeitpunkt sowie den Regeln
	 * und Daten dieses RecurrenceSets den maximalen Endzeitpunkt eines Ereignis
	 *
	 * @param dtStart  der Startzeitpunkt des Ereignisses
	 * @param tzid     die Zeitzone
	 * @return das letzte Auftreten des Ereignisses gemäß dieses RecurrenceSets oder
	 *         {@link Instant#MAX}, wenn das Ereignis unendlich oft auftritt
	 */
	public Instant getEndOfLastOccurence(final Instant dtStart, final String tzid) {
		if (this.rrule != null) {
			return rrule.getMaxInstant(dtStart);
		}
		return Collections.max(this.rDates.getDateList()).atStartOfDay(ZoneId.of(tzid)).toInstant();
	}
}
