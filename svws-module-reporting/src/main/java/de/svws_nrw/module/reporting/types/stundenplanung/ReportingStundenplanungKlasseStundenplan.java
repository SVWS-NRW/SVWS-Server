package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.Objects;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Klassen-Stundenplan.
 */
public class ReportingStundenplanungKlasseStundenplan extends ReportingBaseType {

	/** Die Klasse des Stundenplans. */
	protected ReportingKlasse klasse;

	/** Der gesamte Stundenplan der Schule. */
	protected ReportingStundenplanungStundenplan stundenplan;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param klasse      Die Klasse des Stundenplans.
	 * @param stundenplan Der gesamte Stundenplan der Schule.
	 */
	public ReportingStundenplanungKlasseStundenplan(final ReportingKlasse klasse, final ReportingStundenplanungStundenplan stundenplan) {
		this.klasse = klasse;
		this.stundenplan = stundenplan;
	}

	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(((long) Long.hashCode(klasse.id())) + ((long) Long.hashCode(stundenplan.id())));
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (this.klasse == null) || (this.stundenplan == null))
			return false;
		if (!(obj instanceof final ReportingStundenplanungKlasseStundenplan other))
			return false;
		if (((other.klasse == null) || (other.stundenplan == null)))
			return false;
		return (Objects.equals(klasse.id(), other.klasse.id()) && (Objects.equals(stundenplan.id(), other.stundenplan.id())));
	}


	// ##### Getter #####

	/**
	 * Gibt die Klasse zu diesem Stundenplan zurück.
	 *
	 * @return die Klasse des Stundenplans
	 */
	public ReportingKlasse klasse() {
		return klasse;
	}

	/**
	 * Gibt den gesamten Stundenplan der Schule zurück.
	 *
	 * @return der gesamte Stundenplan der Schule
	 */
	public ReportingStundenplanungStundenplan stundenplan() {
		return stundenplan;
	}
}
