package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.Objects;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schüler-Stundenplan.
 */
public class ReportingStundenplanungSchuelerStundenplan extends ReportingBaseType {

	/** Der Schüler des Stundenplans. */
	protected ReportingSchueler schueler;

	/** Der gesamte Stundenplan der Schule. */
	protected ReportingStundenplanungStundenplan stundenplan;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieses Schülers.
	 *
	 * @param schueler      Der Schüler des Stundenplans.
	 * @param stundenplan Der gesamte Stundenplan der Schule.
	 */
	public ReportingStundenplanungSchuelerStundenplan(final ReportingSchueler schueler, final ReportingStundenplanungStundenplan stundenplan) {
		this.schueler = schueler;
		this.stundenplan = stundenplan;
	}

	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(((long) Long.hashCode(schueler.id())) + ((long) Long.hashCode(stundenplan.id())));
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (this.schueler == null) || (this.stundenplan == null))
			return false;
		if (!(obj instanceof final ReportingStundenplanungSchuelerStundenplan other))
			return false;
		if (((other.schueler == null) || (other.stundenplan == null)))
			return false;
		return (Objects.equals(schueler.id(), other.schueler.id()) && (Objects.equals(stundenplan.id(), other.stundenplan.id())));
	}


	// ##### Getter #####

	/**
	 * Gibt den Schüler zu diesem Stundenplan zurück.
	 *
	 * @return der Schüler des Stundenplans
	 */
	public ReportingSchueler schueler() {
		return schueler;
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
