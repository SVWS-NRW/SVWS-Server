package de.svws_nrw.module.pdf.types.klasse;

import de.svws_nrw.module.pdf.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.pdf.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.pdf.types.schueler.ReportingSchueler;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Klasse.</p>
 * <p>Sie enthält die Grunddaten der Klasse mit Klassenleitungen und den zugeordneten Schülern.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingKlasse {

	/** Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat. */
	public boolean beginnSommersemester;

	/** Eine zusätzliche Beschreibung zu der Klasse */
	public String beschreibung;

	/** Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist. */
	public ReportingKlasse folgeklasse;

	/** Die ID der Klasse. */
	public long id;

	/** Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich */
	public Long idAllgemeinbildendOrganisationsform;

	/** Die ID für die Organisationsform der Klasse im berufsbildenden Bereich */
	public Long idBerufsbildendOrganisationsform;

	/** Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null */
	public Long idFachklasse;

	/** Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null */
	public Long idFolgeklasse;

	/** Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist */
	public Long idJahrgang;

	/** Die ID für Klassenart */
	public long idKlassenart;

	/** Die Liste der IDs der Klassenleitungen der Klasse. */
	private List<Long> idsKlassenleitungen = new ArrayList<>();

	/** Die Liste der IDs der Schüler der Klasse. */
	private List<Long> idsSchueler = new ArrayList<>();

	/** Die ID für die Schulgliederung der Klasse */
	public long idSchulgliederung;

	/** Die ID des Schuljahresabschnittes der Klasse. */
	public long idSchuljahresabschnitt;

	/** Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null */
	public Long idVorgaengerklasse;

	/** Die ID für die Organisationsform der Klasse im Weiterbildungsbereich */
	public Long idWeiterbildungOrganisationsform;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	public boolean istSichtbar;

	/** Der Jahrgang, dem die Klasse zugeordnet ist. */
	private ReportingJahrgang jahrgang;

	/** Die Liste der Klassenleitungen der Klasse. */
	private List<ReportingLehrer> klassenleitungen = new ArrayList<>();

	/** Das Kürzel der Klasse. */
	public String kuerzel;

	/** Das Kürzel der Folgeklasse. */
	public String kuerzelFolgeklasse;

	/** Das Kürzel der Vorgängerklasse. */
	public String kuerzelVorgaengerklasse;

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	public String parallelitaet;

	/** Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird. */
	public String pruefungsordnung;

	/** Die Liste der Schüler der Klasse. */
	private List<ReportingSchueler> schueler = new ArrayList<>();

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	public int sortierung;

	/** Adressmerkmal des Teilstandorts für die Klasse */
	public String teilstandort;

	/** Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden. */
	public boolean verwendungAnkreuzkompetenzen;

	/** Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse. */
	public ReportingKlasse vorgaengerklasse;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
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
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnittes der Klasse.
	 * @param idVorgaengerklasse Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null
	 * @param idWeiterbildungOrganisationsform Die ID für die Organisationsform der Klasse im Weiterbildungsbereich
	 * @param istSichtbar Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @param jahrgang Der Jahrgang, dem die Klasse zugeordnet ist.
	 * @param klassenleitungen Die Liste der Klassenleitungen der Klasse.
	 * @param kuerzel Das Kürzel der Klasse.
	 * @param kuerzelFolgeklasse Das Kürzel der Folgeklasse.
	 * @param kuerzelVorgaengerklasse Das Kürzel der Vorgängerklasse.
	 * @param parallelitaet Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 * @param pruefungsordnung Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird.
	 * @param schueler Die Liste der Schüler der Klasse.
	 * @param sortierung Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 * @param teilstandort Adressmerkmal des Teilstandorts für die Klasse
	 * @param verwendungAnkreuzkompetenzen Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden.
	 * @param vorgaengerklasse Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse.
	 */
	public ReportingKlasse(final boolean beginnSommersemester, final String beschreibung, final ReportingKlasse folgeklasse, final long id, final Long idAllgemeinbildendOrganisationsform, final Long idBerufsbildendOrganisationsform, final Long idFachklasse, final Long idFolgeklasse, final Long idJahrgang, final long idKlassenart, final List<Long> idsKlassenleitungen, final List<Long> idsSchueler, final long idSchulgliederung, final long idSchuljahresabschnitt, final Long idVorgaengerklasse, final Long idWeiterbildungOrganisationsform, final boolean istSichtbar, final ReportingJahrgang jahrgang, final List<ReportingLehrer> klassenleitungen, final String kuerzel, final String kuerzelFolgeklasse, final String kuerzelVorgaengerklasse, final String parallelitaet, final String pruefungsordnung, final List<ReportingSchueler> schueler, final int sortierung, final String teilstandort, final boolean verwendungAnkreuzkompetenzen, final ReportingKlasse vorgaengerklasse) {
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
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.idVorgaengerklasse = idVorgaengerklasse;
		this.idWeiterbildungOrganisationsform = idWeiterbildungOrganisationsform;
		this.istSichtbar = istSichtbar;
		this.jahrgang = jahrgang;
		this.klassenleitungen = klassenleitungen;
		this.kuerzel = kuerzel;
		this.kuerzelFolgeklasse = kuerzelFolgeklasse;
		this.kuerzelVorgaengerklasse = kuerzelVorgaengerklasse;
		this.parallelitaet = parallelitaet;
		this.pruefungsordnung = pruefungsordnung;
		this.schueler = schueler;
		this.sortierung = sortierung;
		this.teilstandort = teilstandort;
		this.verwendungAnkreuzkompetenzen = verwendungAnkreuzkompetenzen;
		this.vorgaengerklasse = vorgaengerklasse;
	}



	// ##### Getter und Setter #####

	/**
	 * Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat.
	 * @return Inhalt des Feldes beginnSommersemester
	 */
	public boolean beginnSommersemester() {
		return beginnSommersemester;
	}

	/**
	 * Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat, wird gesetzt.
	 * @param beginnSommersemester Neuer Wert für das Feld beginnSommersemester
	 */
	public void setBeginnSommersemester(final boolean beginnSommersemester) {
		this.beginnSommersemester = beginnSommersemester;
	}

	/**
	 * Eine zusätzliche Beschreibung zu der Klasse
	 * @return Inhalt des Feldes beschreibung
	 */
	public String beschreibung() {
		return beschreibung;
	}

	/**
	 * Eine zusätzliche Beschreibung zu der Klasse wird gesetzt.
	 * @param beschreibung Neuer Wert für das Feld beschreibung
	 */
	public void setBeschreibung(final String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/**
	 * Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist.
	 * @return Inhalt des Feldes folgeklasse
	 */
	public ReportingKlasse folgeklasse() {
		return folgeklasse;
	}

	/**
	 * Die Folgeklasse dieser Klasse zur idFolgeklasse, sofern diese bereits vorhanden ist, wird gesetzt.
	 * @param folgeklasse Wert für das Feld folgeklasse
	 */
	public void setFolgeklasse(final ReportingKlasse folgeklasse) {
		this.folgeklasse = folgeklasse;
	}

	/**
	 * Die ID der Klasse.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID der Klasse wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich
	 * @return Inhalt des Feldes idAllgemeinbildendOrganisationsform
	 */
	public Long idAllgemeinbildendOrganisationsform() {
		return idAllgemeinbildendOrganisationsform;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich wird gesetzt.
	 * @param idAllgemeinbildendOrganisationsform Neuer Wert für das Feld idAllgemeinbildendOrganisationsform
	 */
	public void setIdAllgemeinbildendOrganisationsform(final Long idAllgemeinbildendOrganisationsform) {
		this.idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im berufsbildenden Bereich
	 * @return Inhalt des Feldes idBerufsbildendOrganisationsform
	 */
	public Long idBerufsbildendOrganisationsform() {
		return idBerufsbildendOrganisationsform;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im berufsbildenden Bereich wird gesetzt.
	 * @param idBerufsbildendOrganisationsform Neuer Wert für das Feld idBerufsbildendOrganisationsform
	 */
	public void setIdBerufsbildendOrganisationsform(final Long idBerufsbildendOrganisationsform) {
		this.idBerufsbildendOrganisationsform = idBerufsbildendOrganisationsform;
	}

	/**
	 * Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null
	 * @return Inhalt des Feldes idFachklasse
	 */
	public Long idFachklasse() {
		return idFachklasse;
	}

	/**
	 * Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null wird gesetzt.
	 * @param idFachklasse Neuer Wert für das Feld idFachklasse
	 */
	public void setIdFachklasse(final Long idFachklasse) {
		this.idFachklasse = idFachklasse;
	}

	/**
	 * Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null
	 * @return Inhalt des Feldes idFolgeklasse
	 */
	public Long idFolgeklasse() {
		return idFolgeklasse;
	}

	/**
	 * Die ID der Folgeklasse, sofern im Folgeabschnitt definiert - ansonsten null wird gesetzt.
	 * @param idFolgeklasse Neuer Wert für das Feld idFolgeklasse
	 */
	public void setIdFolgeklasse(final Long idFolgeklasse) {
		this.idFolgeklasse = idFolgeklasse;
	}

	/**
	 * Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist
	 * @return Inhalt des Feldes idJahrgang
	 */
	public Long idJahrgang() {
		return idJahrgang;
	}

	/**
	 * Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist, wird gesetzt.
	 * @param idJahrgang Neuer Wert für das Feld idJahrgang
	 */
	public void setIdJahrgang(final Long idJahrgang) {
		this.idJahrgang = idJahrgang;
	}

	/**
	 * Die ID für Klassenart
	 * @return Inhalt des Feldes idKlassenart
	 */
	public long idKlassenart() {
		return idKlassenart;
	}

	/**
	 * Die ID für Klassenart wird gesetzt.
	 * @param idKlassenart Neuer Wert für das Feld idKlassenart
	 */
	public void setIdKlassenart(final long idKlassenart) {
		this.idKlassenart = idKlassenart;
	}

	/**
	 * Die Liste der IDs der Klassenleitungen der Klasse.
	 * @return Inhalt des Feldes idsKlassenleitungen
	 */
	public  List<Long> idsKlassenleitungen() {
		return idsKlassenleitungen;
	}

	/**
	 * Die Liste der IDs der Klassenleitungen der Klasse wird gesetzt.
	 * @param idsKlassenleitungen Neuer Wert für das Feld idsKlassenleitungen
	 */
	public void setIdsKlassenleitungen(final  List<Long> idsKlassenleitungen) {
		this.idsKlassenleitungen = idsKlassenleitungen;
	}

	/**
	 * Die Liste der IDs der Schüler der Klasse.
	 * @return Inhalt des Feldes idsSchueler
	 */
	public  List<Long> idsSchueler() {
		return idsSchueler;
	}

	/**
	 * Die Liste der IDs der Schüler der Klasse wird gesetzt.
	 * @param idsSchueler Neuer Wert für das Feld idsSchueler
	 */
	public void setIdsSchueler(final  List<Long> idsSchueler) {
		this.idsSchueler = idsSchueler;
	}

	/**
	 * Die ID für die Schulgliederung der Klasse
	 * @return Inhalt des Feldes idSchulgliederung
	 */
	public long idSchulgliederung() {
		return idSchulgliederung;
	}

	/**
	 * Die ID für die Schulgliederung der Klasse wird gesetzt.
	 * @param idSchulgliederung Neuer Wert für das Feld idSchulgliederung
	 */
	public void setIdSchulgliederung(final long idSchulgliederung) {
		this.idSchulgliederung = idSchulgliederung;
	}

	/**
	 * Die ID des Schuljahresabschnittes der Klasse.
	 * @return Inhalt des Feldes idSchuljahresabschnitt
	 */
	public long idSchuljahresabschnitt() {
		return idSchuljahresabschnitt;
	}

	/**
	 * Die ID des Schuljahresabschnittes der Klasse wird gesetzt.
	 * @param idSchuljahresabschnitt Neuer Wert für das Feld idSchuljahresabschnitt
	 */
	public void setIdSchuljahresabschnitt(final long idSchuljahresabschnitt) {
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
	}

	/**
	 * Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null
	 * @return Inhalt des Feldes idVorgaengerklasse
	 */
	public Long idVorgaengerklasse() {
		return idVorgaengerklasse;
	}

	/**
	 * Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null wird gesetzt.
	 * @param idVorgaengerklasse Neuer Wert für das Feld idVorgaengerklasse
	 */
	public void setIdVorgaengerklasse(final Long idVorgaengerklasse) {
		this.idVorgaengerklasse = idVorgaengerklasse;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im Weiterbildungsbereich
	 * @return Inhalt des Feldes idWeiterbildungOrganisationsform
	 */
	public Long idWeiterbildungOrganisationsform() {
		return idWeiterbildungOrganisationsform;
	}

	/**
	 * Die ID für die Organisationsform der Klasse im Weiterbildungsbereich wird gesetzt.
	 * @param idWeiterbildungOrganisationsform Neuer Wert für das Feld idWeiterbildungOrganisationsform
	 */
	public void setIdWeiterbildungOrganisationsform(final Long idWeiterbildungOrganisationsform) {
		this.idWeiterbildungOrganisationsform = idWeiterbildungOrganisationsform;
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
	 * Der Jahrgang, dem die Klasse zugeordnet ist.
	 * @return Inhalt des Feldes jahrgang
	 */
	public  ReportingJahrgang jahrgang() {
		return jahrgang;
	}

	/**
	 * Der Jahrgang, dem die Klasse zugeordnet ist, wird gesetzt.
	 * @param jahrgang Neuer Wert für das Feld jahrgang
	 */
	public void setJahrgang(final  ReportingJahrgang jahrgang) {
		this.jahrgang = jahrgang;
	}

	/**
	 * Die Liste der Klassenleitungen der Klasse.
	 * @return Inhalt des Feldes klassenleitungen
	 */
	public  List<ReportingLehrer> klassenleitungen() {
		return klassenleitungen;
	}

	/**
	 * Die Liste der Klassenleitungen der Klasse wird gesetzt.
	 * @param klassenleitungen Neuer Wert für das Feld klassenleitungen
	 */
	public void setKlassenleitungen(final  List<ReportingLehrer> klassenleitungen) {
		this.klassenleitungen = klassenleitungen;
	}

	/**
	 * Das Kürzel der Klasse.
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Das Kürzel der Klasse wird gesetzt.
	 * @param kuerzel Neuer Wert für das Feld kuerzel
	 */
	public void setKuerzel(final String kuerzel) {
		this.kuerzel = kuerzel;
	}

	/**
	 * Das Kürzel der Folgeklasse.
	 * @return Inhalt des Feldes kuerzelFolgeklasse
	 */
	public String kuerzelFolgeklasse() {
		return kuerzelFolgeklasse;
	}

	/**
	 * Das Kürzel der Folgeklasse wird gesetzt.
	 * @param kuerzelFolgeklasse Neuer Wert für das Feld kuerzelFolgeklasse
	 */
	public void setKuerzelFolgeklasse(final String kuerzelFolgeklasse) {
		this.kuerzelFolgeklasse = kuerzelFolgeklasse;
	}

	/**
	 * Das Kürzel der Vorgängerklasse.
	 * @return Inhalt des Feldes kuerzelVorgaengerklasse
	 */
	public String kuerzelVorgaengerklasse() {
		return kuerzelVorgaengerklasse;
	}

	/**
	 * Das Kürzel der Vorgängerklasse wird gesetzt.
	 * @param kuerzelVorgaengerklasse Neuer Wert für das Feld kuerzelVorgaengerklasse
	 */
	public void setKuerzelVorgaengerklasse(final String kuerzelVorgaengerklasse) {
		this.kuerzelVorgaengerklasse = kuerzelVorgaengerklasse;
	}

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 * @return Inhalt des Feldes parallelitaet
	 */
	public String parallelitaet() {
		return parallelitaet;
	}

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z) wird gesetzt.
	 * @param parallelitaet Neuer Wert für das Feld parallelitaet
	 */
	public void setParallelitaet(final String parallelitaet) {
		this.parallelitaet = parallelitaet;
	}

	/**
	 * Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird.
	 * @return Inhalt des Feldes pruefungsordnung
	 */
	public String pruefungsordnung() {
		return pruefungsordnung;
	}

	/**
	 * Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird, wird gesetzt.
	 * @param pruefungsordnung Neuer Wert für das Feld pruefungsordnung
	 */
	public void setPruefungsordnung(final String pruefungsordnung) {
		this.pruefungsordnung = pruefungsordnung;
	}

	/**
	 * Die Liste der Schüler der Klasse.
	 * @return Inhalt des Feldes schueler
	 */
	public  List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Die Liste der Schüler der Klasse wird gesetzt.
	 * @param schueler Neuer Wert für das Feld schueler
	 */
	public void setSchueler(final  List<ReportingSchueler> schueler) {
		this.schueler = schueler;
	}

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags wird gesetzt.
	 * @param sortierung Neuer Wert für das Feld sortierung
	 */
	public void setSortierung(final int sortierung) {
		this.sortierung = sortierung;
	}

	/**
	 * Adressmerkmal des Teilstandorts für die Klasse
	 * @return Inhalt des Feldes teilstandort
	 */
	public String teilstandort() {
		return teilstandort;
	}

	/**
	 * Adressmerkmal des Teilstandorts für die Klasse wird gesetzt.
	 * @param teilstandort Neuer Wert für das Feld teilstandort
	 */
	public void setTeilstandort(final String teilstandort) {
		this.teilstandort = teilstandort;
	}

	/**
	 * Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden.
	 * @return Inhalt des Feldes verwendungAnkreuzkompetenzen
	 */
	public boolean verwendungAnkreuzkompetenzen() {
		return verwendungAnkreuzkompetenzen;
	}

	/**
	 * Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden wird gesetzt.
	 * @param verwendungAnkreuzkompetenzen Neuer Wert für das Feld verwendungAnkreuzkompetenzen
	 */
	public void setVerwendungAnkreuzkompetenzen(final boolean verwendungAnkreuzkompetenzen) {
		this.verwendungAnkreuzkompetenzen = verwendungAnkreuzkompetenzen;
	}

	/**
	 * Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse.
	 * @return Inhalt des Feldes vorgaengerklasse
	 */
	public ReportingKlasse vorgaengerklasse() {
		return vorgaengerklasse;
	}

	/**
	 * Die Vorgängerklasse dieser Klasse zur idVorgaengerklasse wird gesetzt.
	 * @param vorgaengerklasse Wert für das Feld vorgaengerklasse
	 */
	public void setVorgaengerklasse(final ReportingKlasse vorgaengerklasse) {
		this.vorgaengerklasse = vorgaengerklasse;
	}
}
