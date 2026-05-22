package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ReportingAnkreuzkompetenz;

/**
 * Basis-Klasse im Rahmen des Reportings für eine Schüler-Ankreuzkompetenz (Belegung einer Ankreuzkompetenz für einen Schüler in einem Lernabschnitt).
 */
public class ReportingSchuelerAnkreuzkompetenz extends ReportingBaseType {

	/** Die Ankreuzkompetenz (Katalog-Eintrag/Floskel), auf die sich die Belegung bezieht. */
	protected ReportingAnkreuzkompetenz ankreuzkompetenz;

	/** Die ID des Datensatzes in der Datenbank. */
	protected long id;

	/** Der Lernabschnitt, dem die Belegung zugeordnet ist. */
	protected ReportingSchuelerLernabschnitt lernabschnitt;

	/** Gibt an, ob die Stufe 1 angekreuzt ist. */
	protected Boolean stufe1;

	/** Gibt an, ob die Stufe 2 angekreuzt ist. */
	protected Boolean stufe2;

	/** Gibt an, ob die Stufe 3 angekreuzt ist. */
	protected Boolean stufe3;

	/** Gibt an, ob die Stufe 4 angekreuzt ist. */
	protected Boolean stufe4;

	/** Gibt an, ob die Stufe 5 angekreuzt ist. */
	protected Boolean stufe5;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param ankreuzkompetenz Die Ankreuzkompetenz (Katalog-Eintrag/Floskel), auf die sich die Belegung bezieht.
	 * @param id Die ID des Datensatzes in der Datenbank.
	 * @param lernabschnitt Der Lernabschnitt, dem die Belegung zugeordnet ist.
	 * @param stufe1 Gibt an, ob die Stufe 1 angekreuzt ist.
	 * @param stufe2 Gibt an, ob die Stufe 2 angekreuzt ist.
	 * @param stufe3 Gibt an, ob die Stufe 3 angekreuzt ist.
	 * @param stufe4 Gibt an, ob die Stufe 4 angekreuzt ist.
	 * @param stufe5 Gibt an, ob die Stufe 5 angekreuzt ist.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingSchuelerAnkreuzkompetenz(final ReportingAnkreuzkompetenz ankreuzkompetenz, final long id, final ReportingSchuelerLernabschnitt lernabschnitt,
			final Boolean stufe1, final Boolean stufe2, final Boolean stufe3, final Boolean stufe4, final Boolean stufe5) {
		this.ankreuzkompetenz = ankreuzkompetenz;
		this.id = id;
		this.lernabschnitt = lernabschnitt;
		this.stufe1 = stufe1;
		this.stufe2 = stufe2;
		this.stufe3 = stufe3;
		this.stufe4 = stufe4;
		this.stufe5 = stufe5;
	}


	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof final ReportingSchuelerAnkreuzkompetenz other)) {
			return false;
		}
		return (id == other.id);
	}


	// ##### Getter #####

	/**
	 * Die Ankreuzkompetenz (Katalog-Eintrag/Floskel), auf die sich die Belegung bezieht.
	 *
	 * @return Inhalt des Feldes ankreuzkompetenz
	 */
	public ReportingAnkreuzkompetenz ankreuzkompetenz() {
		return ankreuzkompetenz;
	}

	/**
	 * Die ID des Datensatzes in der Datenbank.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Der Lernabschnitt, dem die Belegung zugeordnet ist.
	 *
	 * @return Inhalt des Feldes lernabschnitt
	 */
	public ReportingSchuelerLernabschnitt lernabschnitt() {
		return lernabschnitt;
	}

	/**
	 * Gibt an, ob die Stufe 1 angekreuzt ist.
	 *
	 * @return Inhalt des Feldes stufe1
	 */
	public Boolean stufe1() {
		return stufe1;
	}

	/**
	 * Gibt an, ob die Stufe 2 angekreuzt ist.
	 *
	 * @return Inhalt des Feldes stufe2
	 */
	public Boolean stufe2() {
		return stufe2;
	}

	/**
	 * Gibt an, ob die Stufe 3 angekreuzt ist.
	 *
	 * @return Inhalt des Feldes stufe3
	 */
	public Boolean stufe3() {
		return stufe3;
	}

	/**
	 * Gibt an, ob die Stufe 4 angekreuzt ist.
	 *
	 * @return Inhalt des Feldes stufe4
	 */
	public Boolean stufe4() {
		return stufe4;
	}

	/**
	 * Gibt an, ob die Stufe 5 angekreuzt ist.
	 *
	 * @return Inhalt des Feldes stufe5
	 */
	public Boolean stufe5() {
		return stufe5;
	}

}
