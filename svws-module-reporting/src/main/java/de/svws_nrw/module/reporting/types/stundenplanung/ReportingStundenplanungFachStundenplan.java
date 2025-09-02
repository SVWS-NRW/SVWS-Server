package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.Objects;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;


/**
 * Basis-Fach im Rahmen des Reportings für Daten vom Typ Fach-Stundenplan.
 */
public class ReportingStundenplanungFachStundenplan extends ReportingBaseType {

	/** Das Fach des Stundenplans. */
	protected ReportingFach fach;

	/** Der gesamte Stundenplan der Schule. */
	protected ReportingStundenplanungStundenplan stundenplan;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieses Faches.
	 *
	 * @param fach        Das Fach des Stundenplans.
	 * @param stundenplan Der gesamte Stundenplan der Schule.
	 */
	public ReportingStundenplanungFachStundenplan(final ReportingFach fach, final ReportingStundenplanungStundenplan stundenplan) {
		this.fach = fach;
		this.stundenplan = stundenplan;
	}

	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode des Faches
	 * @return Hashcode des Faches
	 */
	public int hashCode() {
		return 31 + Long.hashCode(((long) Long.hashCode(fach.id())) + ((long) Long.hashCode(stundenplan.id())));
	}

	/**
	 * Equals des Faches
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (this.fach == null) || (this.stundenplan == null))
			return false;
		if (!(obj instanceof final ReportingStundenplanungFachStundenplan other))
			return false;
		if (((other.fach == null) || (other.stundenplan == null)))
			return false;
		return (Objects.equals(fach.id(), other.fach.id()) && (Objects.equals(stundenplan.id(), other.stundenplan.id())));
	}


	// ##### Getter #####

	/**
	 * Gibt das Fach zu diesem Stundenplan zurück.
	 *
	 * @return das Fach des Stundenplans
	 */
	public ReportingFach fach() {
		return fach;
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
