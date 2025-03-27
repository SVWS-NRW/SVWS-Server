package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.Objects;

import de.svws_nrw.module.reporting.types.ReportingBaseType;


/**
 * Basis-Raum im Rahmen des Reportings für Daten vom Typ Schüler-Stundenplan.
 */
public class ReportingStundenplanungRaumStundenplan extends ReportingBaseType {

	/** Der Raum des Stundenplans. */
	protected ReportingStundenplanungRaum raum;

	/** Der gesamte Stundenplan der Schule. */
	protected ReportingStundenplanungStundenplan stundenplan;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Raum.
	 *
	 * @param raum        Der Raum des Stundenplans.
	 * @param stundenplan Der gesamte Stundenplan der Schule.
	 */
	public ReportingStundenplanungRaumStundenplan(final ReportingStundenplanungRaum raum, final ReportingStundenplanungStundenplan stundenplan) {
		this.raum = raum;
		this.stundenplan = stundenplan;
	}

	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Raum
	 * @return Hashcode der Raum
	 */
	public int hashCode() {
		return 31 + Long.hashCode(((long) Long.hashCode(raum.id())) + ((long) Long.hashCode(stundenplan.id())));
	}

	/**
	 * Equals der Raum
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (this.raum == null) || (this.stundenplan == null))
			return false;
		if (!(obj instanceof final ReportingStundenplanungRaumStundenplan other))
			return false;
		if (((other.raum == null) || (other.stundenplan == null)))
			return false;
		return (Objects.equals(raum.id(), other.raum.id()) && (Objects.equals(stundenplan.id(), other.stundenplan.id())));
	}


	// ##### Getter #####

	/**
	 * Gibt den Raum zu diesem Stundenplan zurück.
	 *
	 * @return den Raum des Stundenplans
	 */
	public ReportingStundenplanungRaum raum() {
		return raum;
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
