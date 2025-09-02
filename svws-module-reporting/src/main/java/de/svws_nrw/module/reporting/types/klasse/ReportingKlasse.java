package de.svws_nrw.module.reporting.types.klasse;

import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Klasse.
 */
public class ReportingKlasse extends ReportingBaseType {

	/** Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat. */
	protected boolean beginnSommersemester;

	/** Eine zusätzliche Beschreibung zu der Klasse */
	protected String beschreibung;

	/** Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist. */
	protected ReportingKlasse folgeklasse;

	/** Die ID der Klasse. */
	protected long id;

	/** Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich */
	protected Long idAllgemeinbildendOrganisationsform;

	/** Die ID für die Organisationsform der Klasse im berufsbildenden Bereich */
	protected Long idBerufsbildendOrganisationsform;

	/** Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null */
	protected Long idFachklasse;

	/** Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null */
	protected Long idFolgeklasse;

	/** Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist */
	protected Long idJahrgang;

	/** Die ID für Klassenart */
	protected long idKlassenart;

	/** Die Liste der IDs der Klassenleitungen der Klasse. */
	protected List<Long> idsKlassenleitungen;

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

	/** Die Liste der Klassenleitungen der Klasse. */
	protected List<ReportingLehrer> klassenleitungen;

	/** Das Kürzel der Klasse. */
	protected String kuerzel;

	/** Das Kürzel der Folgeklasse. */
	protected String kuerzelFolgeklasse;

	/** Das Kürzel der Vorgängerklasse. */
	protected String kuerzelVorgaengerklasse;

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	protected String parallelitaet;

	/** Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird. */
	protected String pruefungsordnung;

	/** Die Liste der Schüler der Klasse. */
	protected List<ReportingSchueler> schueler;

	/** Der Schuljahresabschnitt der Klasse. */
	protected ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	protected int sortierung;

	/** Adressmerkmal des Teilstandorts für die Klasse */
	protected String teilstandort;

	/** Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden. */
	protected boolean verwendungAnkreuzkompetenzen;

	/** Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse. */
	protected ReportingKlasse vorgaengerklasse;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param beginnSommersemester Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat.
	 * @param beschreibung Eine zusätzliche Beschreibung zu der Klasse
	 * @param folgeklasse Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist.
	 * @param id Die ID der Klasse.
	 * @param idAllgemeinbildendOrganisationsform Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich
	 * @param idBerufsbildendOrganisationsform Die ID für die Organisationsform der Klasse im berufsbildenden Bereich
	 * @param idFachklasse Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null
	 * @param idFolgeklasse Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null
	 * @param idJahrgang Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist
	 * @param idKlassenart Die ID für Klassenart
	 * @param idsKlassenleitungen Die Liste der IDs der Klassenleitungen der Klasse.
	 * @param idsSchueler Die Liste der IDs der Schüler der Klasse.
	 * @param idSchulgliederung Die ID für die Schulgliederung der Klasse
	 * @param idVorgaengerklasse Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null
	 * @param idWeiterbildungOrganisationsform Die ID für die Organisationsform der Klasse im Weiterbildungsbereich
	 * @param jahrgang Der Jahrgang, dem die Klasse zugeordnet ist.
	 * @param klassenleitungen Die Liste der Klassenleitungen der Klasse.
	 * @param kuerzel Das Kürzel der Klasse.
	 * @param kuerzelFolgeklasse Das Kürzel der Folgeklasse.
	 * @param kuerzelVorgaengerklasse Das Kürzel der Vorgängerklasse.
	 * @param parallelitaet Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 * @param pruefungsordnung Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird.
	 * @param schueler Die Liste der Schüler der Klasse.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt der Klasse.
	 * @param sortierung Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 * @param teilstandort Adressmerkmal des Teilstandorts für die Klasse
	 * @param verwendungAnkreuzkompetenzen Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden.
	 * @param vorgaengerklasse Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse.
	 */
	public ReportingKlasse(final boolean beginnSommersemester, final String beschreibung, final ReportingKlasse folgeklasse, final long id,
			final Long idAllgemeinbildendOrganisationsform, final Long idBerufsbildendOrganisationsform, final Long idFachklasse, final Long idFolgeklasse,
			final Long idJahrgang, final long idKlassenart, final List<Long> idsKlassenleitungen, final List<Long> idsSchueler, final long idSchulgliederung,
			final Long idVorgaengerklasse, final Long idWeiterbildungOrganisationsform, final ReportingJahrgang jahrgang,
			final List<ReportingLehrer> klassenleitungen, final String kuerzel, final String kuerzelFolgeklasse, final String kuerzelVorgaengerklasse,
			final String parallelitaet, final String pruefungsordnung, final List<ReportingSchueler> schueler,
			final ReportingSchuljahresabschnitt schuljahresabschnitt, final int sortierung, final String teilstandort,
			final boolean verwendungAnkreuzkompetenzen, final ReportingKlasse vorgaengerklasse) {
		this.beginnSommersemester = beginnSommersemester;
		this.beschreibung = beschreibung;
		this.folgeklasse = folgeklasse;
		this.id = id;
		this.idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;
		this.idBerufsbildendOrganisationsform = idBerufsbildendOrganisationsform;
		this.idFachklasse = idFachklasse;
		this.idFolgeklasse = idFolgeklasse;
		this.idJahrgang = idJahrgang;
		this.idKlassenart = idKlassenart;
		this.idsKlassenleitungen = idsKlassenleitungen;
		this.idsSchueler = idsSchueler;
		this.idSchulgliederung = idSchulgliederung;
		this.idVorgaengerklasse = idVorgaengerklasse;
		this.idWeiterbildungOrganisationsform = idWeiterbildungOrganisationsform;
		this.jahrgang = jahrgang;
		this.klassenleitungen = klassenleitungen;
		this.kuerzel = kuerzel;
		this.kuerzelFolgeklasse = kuerzelFolgeklasse;
		this.kuerzelVorgaengerklasse = kuerzelVorgaengerklasse;
		this.parallelitaet = parallelitaet;
		this.pruefungsordnung = pruefungsordnung;
		this.schueler = schueler;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.sortierung = sortierung;
		this.teilstandort = teilstandort;
		this.verwendungAnkreuzkompetenzen = verwendungAnkreuzkompetenzen;
		this.vorgaengerklasse = vorgaengerklasse;
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
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingKlasse other))
			return false;
		return (id == other.id);
	}


	// ##### Berechnete Methoden #####

	/**
	 * Auflistung der Klassenleitungen als kommaseparierte Liste der Kürzel.
	 *
	 * @return		Kommaseparierte Liste der Klassenleitungen.
	 */
	public String auflistungKlassenleitung() {
		if ((this.klassenleitungen() == null) || this.klassenleitungen().isEmpty())
			return "";
		return this.klassenleitungen().stream().map(ReportingLehrer::kuerzel).collect(Collectors.joining(","));
	}

	/**
	 * Erstellt aus den Geschlechtern der Schüler eine Statistik in der Form (m/w/d) für diese Klasse.
	 *
	 * @return		Statistik in der Form (m/w/d).
	 */
	public String statistikGeschlechter() {
		if ((this.schueler() == null) || this.schueler().isEmpty())
			return "(0/0/0)";
		final long anzahlM = this.schueler().stream().filter(s -> "m".equalsIgnoreCase(s.geschlecht().kuerzel)).count();
		final long anzahlW = this.schueler().stream().filter(s -> "w".equalsIgnoreCase(s.geschlecht().kuerzel)).count();
		final long anzahlD = this.schueler().stream().filter(s -> "d".equalsIgnoreCase(s.geschlecht().kuerzel)).count();
		return String.format("(%d/%d/%d)", anzahlM, anzahlW, anzahlD);
	}


	// ##### Getter #####

	/**
	 * Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat.
	 *
	 * @return Inhalt des Feldes beginnSommersemester
	 */
	public boolean beginnSommersemester() {
		return beginnSommersemester;
	}

	/**
	 * Eine zusätzliche Beschreibung zu der Klasse
	 *
	 * @return Inhalt des Feldes beschreibung
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
	 * Die ID der Klasse.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
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
	 * Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null
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
	 * Die Liste der IDs der Klassenleitungen der Klasse.
	 *
	 * @return Inhalt des Feldes idsKlassenleitungen
	 */
	public List<Long> idsKlassenleitungen() {
		return idsKlassenleitungen;
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
	 * @return Inhalt des Feldes jahrgang
	 */
	public ReportingJahrgang jahrgang() {
		return jahrgang;
	}

	/**
	 * Die Liste der Klassenleitungen der Klasse.
	 *
	 * @return Inhalt des Feldes klassenleitungen
	 */
	public List<ReportingLehrer> klassenleitungen() {
		return klassenleitungen;
	}

	/**
	 * Das Kürzel der Klasse.
	 *
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
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
	 * Die Liste der Schüler der Klasse.
	 *
	 * @return Inhalt des Feldes schueler
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Der Schuljahresabschnitt der Klasse.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 *
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
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
	 * @return Inhalt des Feldes vorgaengerklasse
	 */
	public ReportingKlasse vorgaengerklasse() {
		return vorgaengerklasse;
	}

}
