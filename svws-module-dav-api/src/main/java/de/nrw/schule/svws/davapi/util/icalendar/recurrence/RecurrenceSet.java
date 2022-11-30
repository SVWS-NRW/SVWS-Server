package de.nrw.schule.svws.davapi.util.icalendar.recurrence;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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
	private List<Instant> rDates = new Vector<>();
	/** eine Liste von Ausnahemn für die Regeln und die Daten */
	private List<Instant> exDates = new Vector<>();

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
	public List<Instant> getrDates() {
		return rDates;
	}

	/**
	 * @return the exDates
	 */
	public List<Instant> getExDates() {
		return exDates;
	}

	/**
	 * Berechnet aus dem gegebenen Werten Startzeitpunkt und Dauer sowie den Regeln
	 * und Daten dieses RecurrenceSets den maximalen Endzeitpunkt eines Ereignis
	 * 
	 * @param dtStart  der Startzeitpunkt des Ereignisses
	 * @param duration die Dauer des Ereignisses
	 * @return das letzte Auftreten des Ereignisses gemäß dieses RecurrenceSets oder
	 *         {@link Instant#MAX}, wenn das Ereignis unendlich oft auftritt
	 */
	public Instant getEndOfLastOccurence(Instant dtStart, Duration duration) {
		Instant maxDate = dtStart;
		if (this.rrule != null && this.rrule.getLimit() == null) {
			return Instant.MAX;
		}
		List<Instant> validRDates = this.rDates.stream().filter(d -> !this.exDates.contains(d)).toList();
		if (!validRDates.isEmpty()) {
			maxDate = Collections.max(validRDates);
		}
		if (this.rrule != null && this.rrule.getLimit() != null) {
			maxDate = this.rrule.getMaxInstant(dtStart);
		}
		return maxDate.plus(duration);
	}
}
