package de.svws_nrw.module.reporting.types.jahrgang;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Jahrgang.</p>
 * <p>Sie enthält die Daten zum Jahrgang so wie dessen Schüler.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingJahrgang {

	/** Der Name bzw. die Bezeichnung des Jahrgangs. */
	protected String bezeichnung;

	/** Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen */
	protected Long gueltigBis;

	/** Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an */
	protected Long gueltigVon;

	/** Die Daten des Folgejahrgangs. */
	protected ReportingJahrgang folgejahrgang = null;

	/** Die ID des Jahrgangs. */
	protected long id;

	/** Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null */
	protected Long idFolgejahrgang;

	/** Liste der Klassen des Jahrgangs. */
	protected List<ReportingKlasse> klassen = new ArrayList<>();

	/** Das Kürzel des Jahrgangs. */
	protected String kuerzel;

	/** Das Kürzel der Schulgliederung, der der Eintrag zugeordnet ist. */
	protected String kuerzelSchulgliederung;

	/** Das dem Jahrgang zugeordnete Statistik-Kürzel. */
	protected String kuerzelStatistik;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	protected boolean istSichtbar;

	/** Liste der Schüler des Jahrgangs. */
	protected List<ReportingSchueler> schueler = new ArrayList<>();

	/** Der Schuljahresabschnitt zu diesem Jahrgang. */
	protected ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Die Sortierreihenfolge des Jahrgangs in der Liste der Jahrgänge. */
	protected int sortierung;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param bezeichnung Der Name bzw. die Bezeichnung des Jahrgangs.
	 * @param gueltigBis Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen
	 * @param gueltigVon Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an
	 * @param folgejahrgang Die Daten des Folgejahrgangs.
	 * @param id Die ID des Jahrgangs.
	 * @param idFolgejahrgang Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null
	 * @param klassen Liste der Klassen des Jahrgangs.
	 * @param kuerzel Das Kürzel des Jahrgangs.
	 * @param kuerzelSchulgliederung Das Kürzel der Schulgliederung, der der Eintrag zugeordnet ist.
	 * @param kuerzelStatistik Das dem Jahrgang zugeordnete Statistik-Kürzel.
	 * @param istSichtbar Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @param schueler Liste der Schüler des Jahrgangs.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt zu diesem Jahrgang.
	 * @param sortierung Die Sortierreihenfolge des Jahrgangs in der Liste der Jahrgänge.
	 */
	public ReportingJahrgang(final String bezeichnung, final Long gueltigBis, final Long gueltigVon, final ReportingJahrgang folgejahrgang, final long id,
			final Long idFolgejahrgang, final List<ReportingKlasse> klassen, final String kuerzel, final String kuerzelSchulgliederung,
			final String kuerzelStatistik, final boolean istSichtbar, final List<ReportingSchueler> schueler,
			final ReportingSchuljahresabschnitt schuljahresabschnitt, final int sortierung) {
		this.bezeichnung = bezeichnung;
		this.gueltigBis = gueltigBis;
		this.gueltigVon = gueltigVon;
		this.folgejahrgang = folgejahrgang;
		this.id = id;
		this.idFolgejahrgang = idFolgejahrgang;
		this.klassen = klassen;
		this.kuerzel = kuerzel;
		this.kuerzelSchulgliederung = kuerzelSchulgliederung;
		this.kuerzelStatistik = kuerzelStatistik;
		this.istSichtbar = istSichtbar;
		this.schueler = schueler;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.sortierung = sortierung;
	}



	// ##### Getter #####

	/**
	 * Der Name bzw. die Bezeichnung des Jahrgangs.
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen
	 * @return Inhalt des Feldes gueltigBis
	 */
	public Long gueltigBis() {
		return gueltigBis;
	}

	/**
	 * Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an
	 * @return Inhalt des Feldes gueltigVon
	 */
	public Long gueltigVon() {
		return gueltigVon;
	}

	/**
	 * Die Daten des Folgejahrgangs.
	 * @return Inhalt des Feldes folgejahrgang
	 */
	public ReportingJahrgang folgejahrgang() {
		return folgejahrgang;
	}

	/**
	 * Die ID des Jahrgangs.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null
	 * @return Inhalt des Feldes idFolgejahrgang
	 */
	public Long idFolgejahrgang() {
		return idFolgejahrgang;
	}

	/**
	 * Liste der Klassen des Jahrgangs.
	 * @return Inhalt des Feldes klassen
	 */
	public List<ReportingKlasse> klassen() {
		return klassen;
	}

	/**
	 * Das Kürzel des Jahrgangs.
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Das Kürzel der Schulgliederung, der der Eintrag zugeordnet ist.
	 * @return Inhalt des Feldes kuerzelSchulgliederung
	 */
	public String kuerzelSchulgliederung() {
		return kuerzelSchulgliederung;
	}

	/**
	 * Das dem Jahrgang zugeordnete Statistik-Kürzel.
	 * @return Inhalt des Feldes kuerzelStatistik
	 */
	public String kuerzelStatistik() {
		return kuerzelStatistik;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @return Inhalt des Feldes istSichtbar
	 */
	public boolean istSichtbar() {
		return istSichtbar;
	}

	/**
	 * Liste der Schüler des Jahrgangs.
	 * @return Inhalt des Feldes schueler
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Der Schuljahresabschnitt zu diesem Jahrgang.
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Die Sortierreihenfolge des Jahrgangs in der Liste der Jahrgänge.
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

}
