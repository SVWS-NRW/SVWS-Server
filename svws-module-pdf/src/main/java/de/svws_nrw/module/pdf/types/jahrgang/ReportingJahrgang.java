package de.svws_nrw.module.pdf.types.jahrgang;

import de.svws_nrw.module.pdf.types.klasse.ReportingKlasse;
import de.svws_nrw.module.pdf.types.schueler.ReportingSchueler;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Jahrgang.</p>
 * <p>Sie enthält die Daten zum Jahrgang so wie dessen Schüler.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingJahrgang {

	/** Der Name bzw. die Bezeichnung des Jahrgangs. */
	private String bezeichnung;

	/** Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen */
	private Long gueltigBis;

	/** Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an */
	private Long gueltigVon;

	/** Die Daten des Folgejahrgangs. */
	private ReportingJahrgang folgejahrgang = null;

	/** Die ID des Jahrgangs. */
	private long id;

	/** Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null */
	private Long idFolgejahrgang;

	/** Liste der Klassen des Jahrgangs. */
	private List<ReportingKlasse> klassen = new ArrayList<>();

	/** Das Kürzel des Jahrgangs. */
	private String kuerzel;

	/** Das Kürzel der Schulgliederung, der der Eintrag zugeordnet ist. */
	private String kuerzelSchulgliederung;

	/** Das dem Jahrgang zugeordnete Statistik-Kürzel. */
	private String kuerzelStatistik;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	private boolean istSichtbar;

	/** Liste der Schüler des Jahrgangs. */
	private List<ReportingSchueler> schueler = new ArrayList<>();

	/** Die Sortierreihenfolge des Jahrgangs in der Liste der Jahrgänge. */
	private int sortierung;

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
	 * @param sortierung Die Sortierreihenfolge des Jahrgangs in der Liste der Jahrgänge.
	 */
	public ReportingJahrgang(final String bezeichnung, final Long gueltigBis, final Long gueltigVon, final ReportingJahrgang folgejahrgang, final long id, final Long idFolgejahrgang, final List<ReportingKlasse> klassen, final String kuerzel, final String kuerzelSchulgliederung, final String kuerzelStatistik, final boolean istSichtbar, final List<ReportingSchueler> schueler, final int sortierung) {
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
		this.sortierung = sortierung;
	}



	// ##### Getter und Setter #####

	/**
	 * Der Name bzw. die Bezeichnung des Jahrgangs.
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Der Name bzw die Bezeichnung des Jahrgangs wird gesetzt.
	 * @param bezeichnung Neuer Wert für das Feld bezeichnung
	 */
	public void setBezeichnung(final String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen
	 * @return Inhalt des Feldes gueltigBis
	 */
	public Long gueltigBis() {
		return gueltigBis;
	}

	/**
	 * Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen wird gesetzt.
	 * @param gueltigBis Neuer Wert für das Feld gueltigBis
	 */
	public void setGueltigBis(final Long gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	/**
	 * Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an
	 * @return Inhalt des Feldes gueltigVon
	 */
	public Long gueltigVon() {
		return gueltigVon;
	}

	/**
	 * Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an wird gesetzt.
	 * @param gueltigVon Neuer Wert für das Feld gueltigVon
	 */
	public void setGueltigVon(final Long gueltigVon) {
		this.gueltigVon = gueltigVon;
	}

	/**
	 * Die Daten des Folgejahrgangs.
	 * @return Inhalt des Feldes folgejahrgang
	 */
	public ReportingJahrgang folgejahrgang() {
		return folgejahrgang;
	}

	/**
	 * Die Daten des Folgejahrgangs wird gesetzt.
	 * @param folgejahrgang Neuer Wert für das Feld folgejahrgang
	 */
	public void setFolgejahrgang(final ReportingJahrgang folgejahrgang) {
		this.folgejahrgang = folgejahrgang;
	}

	/**
	 * Die ID des Jahrgangs.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Jahrgangs wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null
	 * @return Inhalt des Feldes idFolgejahrgang
	 */
	public Long idFolgejahrgang() {
		return idFolgejahrgang;
	}

	/**
	 * Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null wird gesetzt.
	 * @param idFolgejahrgang Neuer Wert für das Feld idFolgejahrgang
	 */
	public void setIdFolgejahrgang(final Long idFolgejahrgang) {
		this.idFolgejahrgang = idFolgejahrgang;
	}

	/**
	 * Liste der Klassen des Jahrgangs.
	 * @return Inhalt des Feldes klassen
	 */
	public List<ReportingKlasse> klassen() {
		return klassen;
	}

	/**
	 * Liste der Klassen des Jahrgangs wird gesetzt.
	 * @param klassen Neuer Wert für das Feld klassen
	 */
	public void setKlassen(final List<ReportingKlasse> klassen) {
		this.klassen = klassen;
	}

	/**
	 * Das Kürzel des Jahrgangs.
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Das Kürzel des Jahrgangs wird gesetzt.
	 * @param kuerzel Neuer Wert für das Feld kuerzel
	 */
	public void setKuerzel(final String kuerzel) {
		this.kuerzel = kuerzel;
	}

	/**
	 * Das Kürzel der Schulgliederung, der der Eintrag zugeordnet ist.
	 * @return Inhalt des Feldes kuerzelSchulgliederung
	 */
	public String kuerzelSchulgliederung() {
		return kuerzelSchulgliederung;
	}

	/**
	 * Das Kürzel der Schulgliederung, der der Eintrag zugeordnet ist, wird gesetzt.
	 * @param kuerzelSchulgliederung Neuer Wert für das Feld kuerzelSchulgliederung
	 */
	public void setKuerzelSchulgliederung(final String kuerzelSchulgliederung) {
		this.kuerzelSchulgliederung = kuerzelSchulgliederung;
	}

	/**
	 * Das dem Jahrgang zugeordnete Statistik-Kürzel.
	 * @return Inhalt des Feldes kuerzelStatistik
	 */
	public String kuerzelStatistik() {
		return kuerzelStatistik;
	}

	/**
	 * Das dem Jahrgang zugeordnete Statistik-Kürzel wird gesetzt.
	 * @param kuerzelStatistik Neuer Wert für das Feld kuerzelStatistik
	 */
	public void setKuerzelStatistik(final String kuerzelStatistik) {
		this.kuerzelStatistik = kuerzelStatistik;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @return Inhalt des Feldes istSichtbar
	 */
	public boolean istSichtbar() {
		return istSichtbar;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht wird gesetzt.
	 * @param istSichtbar Neuer Wert für das Feld istSichtbar
	 */
	public void setIstSichtbar(final boolean istSichtbar) {
		this.istSichtbar = istSichtbar;
	}

	/**
	 * Liste der Schüler des Jahrgangs.
	 * @return Inhalt des Feldes schueler
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Liste der Schüler des Jahrgangs wird gesetzt.
	 * @param schueler Neuer Wert für das Feld schueler
	 */
	public void setSchueler(final List<ReportingSchueler> schueler) {
		this.schueler = schueler;
	}

	/**
	 * Die Sortierreihenfolge des Jahrgangs in der Liste der Jahrgänge.
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

	/**
	 * Die Sortierreihenfolge des Jahrgangs in der Liste der Jahrgänge wird gesetzt.
	 * @param sortierung Neuer Wert für das Feld sortierung
	 */
	public void setSortierung(final int sortierung) {
		this.sortierung = sortierung;
	}
}
