package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.Objects;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer-Stundenplan.
 */
public class ReportingStundenplanungLehrerStundenplan extends ReportingBaseType {

	/** Der Lehrer des Stundenplans. */
	protected ReportingLehrer lehrer;

	/** Der gesamte Stundenplan der Schule. */
	protected ReportingStundenplanungStundenplan stundenplan;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param lehrer      Der Lehrer des Stundenplans.
	 * @param stundenplan Der gesamte Stundenplan der Schule.
	 */
	public ReportingStundenplanungLehrerStundenplan(final ReportingLehrer lehrer, final ReportingStundenplanungStundenplan stundenplan) {
		this.lehrer = lehrer;
		this.stundenplan = stundenplan;
	}

	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(((long) Long.hashCode(lehrer.id())) + ((long) Long.hashCode(stundenplan.id())));
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (this.lehrer == null) || (this.stundenplan == null))
			return false;
		if (!(obj instanceof final ReportingStundenplanungLehrerStundenplan other))
			return false;
		if (((other.lehrer == null) || (other.stundenplan == null)))
			return false;
		return (Objects.equals(lehrer.id(), other.lehrer.id()) && (Objects.equals(stundenplan.id(), other.stundenplan.id())));
	}


	// ##### Getter #####

	/**
	 * Gibt den Lehrer zu diesem Stundenplan zurück.
	 *
	 * @return der Lehrer des Stundenplans
	 */
	public ReportingLehrer lehrer() {
		return lehrer;
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
