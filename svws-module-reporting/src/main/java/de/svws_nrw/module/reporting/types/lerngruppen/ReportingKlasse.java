
package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Klasse.
 */
public class ReportingKlasse extends ReportingSchuelergruppe {

	/** Gibt am WBK an, ob die Klasse im Sommersemester angefangen hat. */
	protected boolean beginnSommersemester;

	/** Eine zusätzliche Beschreibung zu der Klasse */
	protected String beschreibung;

	/** Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist. */
	protected ReportingKlasse folgeklasse;

	/** Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich */
	protected Long idAllgemeinbildendOrganisationsform;

	/** Die ID für die Organisationsform der Klasse im berufsbildenden Bereich */
	protected Long idBerufsbildendOrganisationsform;

	/** Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt, oder null */
	protected Long idFachklasse;

	/** Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null */
	protected Long idFolgeklasse;

	/** Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist */
	protected Long idJahrgang;

	/** Die ID für Klassenart */
	protected long idKlassenart;

	/** Die Liste der IDs der Schüler der Klasse. */
	protected List<Long> idsSchueler;

	/** Die ID für die Schulgliederung der Klasse */
	protected long idSchulgliederung;

	/** Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null */
	protected Long idVorgaengerklasse;

	/** Die ID für die Organisationsform der Klasse im Weiterbildungsbereich */
	protected Long idWeiterbildungOrganisationsform;

	/** Der Jahrgang, dem die Klasse zugeordnet ist. */
	protected ReportingJahrgang jahrgang;

	/** Das Kürzel der Folgeklasse. */
	protected String kuerzelFolgeklasse;

	/** Das Kürzel der Vorgängerklasse. */
	protected String kuerzelVorgaengerklasse;

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	protected String parallelitaet;

	/** Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird. */
	protected String pruefungsordnung;

	/** Adressmerkmal des Teilstandorts für die Klasse */
	protected String teilstandort;

	/** Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden. */
	protected boolean verwendungAnkreuzkompetenzen;

	/** Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse. */
	protected ReportingKlasse vorgaengerklasse;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param id Die ID der Klasse.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt der Klasse.
	 * @param kuerzel Das Kürzel der Klasse.
	 * @param schueler Die Liste der Schüler der Klasse.
	 * @param sortierung Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 * @param beginnSommersemester Gibt am WBK an, ob die Klasse im Sommersemester angefangen hat.
	 * @param beschreibung Eine zusätzliche Beschreibung zu der Klasse
	 * @param folgeklasse Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist.
	 * @param idAllgemeinbildendOrganisationsform Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich
	 * @param idBerufsbildendOrganisationsform Die ID für die Organisationsform der Klasse im berufsbildenden Bereich
	 * @param idFachklasse Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt, oder null
	 * @param idFolgeklasse Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null
	 * @param idJahrgang Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist
	 * @param idKlassenart Die ID für Klassenart
	 * @param idsSchueler Die Liste der IDs der Schüler der Klasse.
	 * @param idSchulgliederung Die ID für die Schulgliederung der Klasse
	 * @param idVorgaengerklasse Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null
	 * @param idWeiterbildungOrganisationsform Die ID für die Organisationsform der Klasse im Weiterbildungsbereich
	 * @param jahrgang Der Jahrgang, dem die Klasse zugeordnet ist.
	 * @param klassenleitungen Die Lehrer, die die Klasse leiten.
	 * @param kuerzelFolgeklasse Das Kürzel der Folgeklasse.
	 * @param kuerzelVorgaengerklasse Das Kürzel der Vorgängerklasse.
	 * @param parallelitaet Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 * @param pruefungsordnung Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird.
	 * @param teilstandort Adressmerkmal des Teilstandorts für die Klasse
	 * @param verwendungAnkreuzkompetenzen Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden.
	 * @param vorgaengerklasse Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse.
	 */
	public ReportingKlasse(final long id, final ReportingSchuljahresabschnitt schuljahresabschnitt, final String kuerzel,
			final List<ReportingSchueler> schueler, final int sortierung, final boolean beginnSommersemester, final String beschreibung,
			final ReportingKlasse folgeklasse, final Long idAllgemeinbildendOrganisationsform, final Long idBerufsbildendOrganisationsform,
			final Long idFachklasse, final Long idFolgeklasse, final Long idJahrgang, final long idKlassenart, final List<ReportingLehrer> klassenleitungen,
			final List<Long> idsSchueler, final long idSchulgliederung, final Long idVorgaengerklasse, final Long idWeiterbildungOrganisationsform,
			final ReportingJahrgang jahrgang, final String kuerzelFolgeklasse, final String kuerzelVorgaengerklasse, final String parallelitaet,
			final String pruefungsordnung, final String teilstandort, final boolean verwendungAnkreuzkompetenzen, final ReportingKlasse vorgaengerklasse) {
		super(id, schuljahresabschnitt, kuerzel, klassenleitungen, schueler, sortierung);
		this.beginnSommersemester = beginnSommersemester;
		this.beschreibung = beschreibung;
		this.folgeklasse = folgeklasse;
		this.idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;
		this.idBerufsbildendOrganisationsform = idBerufsbildendOrganisationsform;
		this.idFachklasse = idFachklasse;
		this.idFolgeklasse = idFolgeklasse;
		this.idJahrgang = idJahrgang;
		this.idKlassenart = idKlassenart;
		this.idsSchueler = idsSchueler;
		this.idSchulgliederung = idSchulgliederung;
		this.idVorgaengerklasse = idVorgaengerklasse;
		this.idWeiterbildungOrganisationsform = idWeiterbildungOrganisationsform;
		this.jahrgang = jahrgang;
		this.kuerzelFolgeklasse = kuerzelFolgeklasse;
		this.kuerzelVorgaengerklasse = kuerzelVorgaengerklasse;
		this.parallelitaet = parallelitaet;
		this.pruefungsordnung = pruefungsordnung;
		this.teilstandort = teilstandort;
		this.verwendungAnkreuzkompetenzen = verwendungAnkreuzkompetenzen;
		this.vorgaengerklasse = vorgaengerklasse;
	}


	// ##### Implementierung der abstrakten Methoden #####

	/**
	 * Gibt den Jahrgang der Klasse zurück.
	 *
	 * @return Liste mit dem Jahrgang der Klasse.
	 */
	@Override
	public List<ReportingJahrgang> jahrgaenge() {
		return (jahrgang == null) ? new ArrayList<>() : List.of(jahrgang);
	}


	// ##### Getter #####

	/**
	 * Gibt am WBK an, ob die Klasse im Sommersemester angefangen hat.
	 *
	 * @return Inhalt des Feldes beginnSommersemester
	 */
	public boolean beginnSommersemester() {
		return beginnSommersemester;
	}

	/**
	 * Eine zusätzliche Beschreibung zu der Klasse
	 *
	 * @return Inhalt des Feldes Beschreibung
	 */
	public String beschreibung() {
		return beschreibung;
	}

	/**
	 * Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist.
	 *
	 * @return Inhalt des Feldes folgeklasse
	 */
	public ReportingKlasse folgeklasse() {
		return folgeklasse;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich
	 *
	 * @return Inhalt des Feldes idAllgemeinbildendOrganisationsform
	 */
	public Long idAllgemeinbildendOrganisationsform() {
		return idAllgemeinbildendOrganisationsform;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im berufsbildenden Bereich
	 *
	 * @return Inhalt des Feldes idBerufsbildendOrganisationsform
	 */
	public Long idBerufsbildendOrganisationsform() {
		return idBerufsbildendOrganisationsform;
	}

	/**
	 * Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt, oder null
	 *
	 * @return Inhalt des Feldes idFachklasse
	 */
	public Long idFachklasse() {
		return idFachklasse;
	}

	/**
	 * Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null
	 *
	 * @return Inhalt des Feldes idFolgeklasse
	 */
	public Long idFolgeklasse() {
		return idFolgeklasse;
	}

	/**
	 * Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist
	 *
	 * @return Inhalt des Feldes idJahrgang
	 */
	public Long idJahrgang() {
		return idJahrgang;
	}

	/**
	 * Die ID für Klassenart
	 *
	 * @return Inhalt des Feldes idKlassenart
	 */
	public long idKlassenart() {
		return idKlassenart;
	}

	/**
	 * Die Liste der IDs der Schüler der Klasse.
	 *
	 * @return Inhalt des Feldes idsSchueler
	 */
	public List<Long> idsSchueler() {
		return idsSchueler;
	}

	/**
	 * Die ID für die Schulgliederung der Klasse
	 *
	 * @return Inhalt des Feldes idSchulgliederung
	 */
	public long idSchulgliederung() {
		return idSchulgliederung;
	}

	/**
	 * Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null
	 *
	 * @return Inhalt des Feldes idVorgaengerklasse
	 */
	public Long idVorgaengerklasse() {
		return idVorgaengerklasse;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im Weiterbildungsbereich
	 *
	 * @return Inhalt des Feldes idWeiterbildungOrganisationsform
	 */
	public Long idWeiterbildungOrganisationsform() {
		return idWeiterbildungOrganisationsform;
	}

	/**
	 * Der Jahrgang, dem die Klasse zugeordnet ist.
	 *
	 * @return Inhalt des Feldes Jahrgang
	 */
	public ReportingJahrgang jahrgang() {
		return jahrgang;
	}

	/**
	 * Die Liste aller Klassenlehrer der Klasse.
	 *
	 * @return Alle Klassenlehrer der Klasse.
	 */
	public List<ReportingLehrer> klassenlehrer() {
		return super.lehrer();
	}

	/**
	 * Die Lehrkraft, die als erster Lehrer in der Liste der Klassenlehrer aufgeführt wird.
	 *
	 * @return Der erste Lehrer der Klassenlehrerliste.
	 */
	public ReportingLehrer klassenleitung() {
		return leitenderLehrer();
	}

	/**
	 * Weitere Lehrer der Klassenlehrerliste.
	 *
	 * @return Weitere Lehrer der Klassenlehrerliste.
	 */
	public List<ReportingLehrer> zusatzlicheKlassenlehrer() {
		return zusatzLehrer();
	}

	/**
	 * Auflistung der Kürzel der Klassenleitungen als kommaseparierte Liste der Kürzel.
	 *
	 * @return Kommaseparierte Liste der Kürzel der Klassenleitungen.
	 */
	public String auflistungKlassenlehrerkuerzel() {
		return super.auflistungLehrerkuerzel();
	}

	/**
	 * Das Kürzel der Folgeklasse.
	 *
	 * @return Inhalt des Feldes kuerzelFolgeklasse
	 */
	public String kuerzelFolgeklasse() {
		return kuerzelFolgeklasse;
	}

	/**
	 * Das Kürzel der Vorgängerklasse.
	 *
	 * @return Inhalt des Feldes kuerzelVorgaengerklasse
	 */
	public String kuerzelVorgaengerklasse() {
		return kuerzelVorgaengerklasse;
	}

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 *
	 * @return Inhalt des Feldes parallelitaet
	 */
	public String parallelitaet() {
		return parallelitaet;
	}

	/**
	 * Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird.
	 *
	 * @return Inhalt des Feldes pruefungsordnung
	 */
	public String pruefungsordnung() {
		return pruefungsordnung;
	}

	/**
	 * Adressmerkmal des Teilstandorts für die Klasse
	 *
	 * @return Inhalt des Feldes teilstandort
	 */
	public String teilstandort() {
		return teilstandort;
	}

	/**
	 * Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden.
	 *
	 * @return Inhalt des Feldes verwendungAnkreuzkompetenzen
	 */
	public boolean verwendungAnkreuzkompetenzen() {
		return verwendungAnkreuzkompetenzen;
	}

	/**
	 * Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse.
	 *
	 * @return Inhalt des Feldes vorgängerklasse
	 */
	public ReportingKlasse vorgaengerklasse() {
		return vorgaengerklasse;
	}

}
