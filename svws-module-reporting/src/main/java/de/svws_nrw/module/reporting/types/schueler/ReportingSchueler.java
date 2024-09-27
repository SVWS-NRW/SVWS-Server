package de.svws_nrw.module.reporting.types.schueler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ReportingSchuelerGostKursplanungKursbelegung;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ReportingSchuelerSprachbelegung;

import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schüler.</p>
 */
public class ReportingSchueler {

	/** Daten des aktuellen Lernabschnitts. */
	protected ReportingSchuelerLernabschnitt aktuellerLernabschnitt;

	/** Das Anmeldedatum des Schülers. */
	protected String anmeldedatum;

	/** Das Aufnahmedatum des Schülers. */
	protected String aufnahmedatum;

	/** Daten des ausgewählten Lernabschnitts. */
	protected ReportingSchuelerLernabschnitt auswahlLernabschnitt;

	/** Textfeld mit Bemerkungen zum Schülerdatensatz. */
	protected String bemerkungen;

	/** Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll. */
	protected boolean druckeKonfessionAufZeugnisse;

	/** Die protected Email-Adresse des Schülers. */
	protected String emailPrivat;

	/** Die schulische E-Mail-Adresse des Schülers. */
	protected String emailSchule;

	/** Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht. */
	protected boolean erhaeltMeisterBAFOEG;

	/** Gibt an, ob der Schüler BAFÖG erhält oder nicht. */
	protected boolean erhaeltSchuelerBAFOEG;

	/** Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist. */
	protected String externeSchulNr;

	/** Die ID der Art des Fahr des Schülers. */
	protected Long fahrschuelerArtID;

	/** Das Foto (in Base64 kodiert) des Schülers. */
	protected String foto;

	/** Das Geburtsdatum des Schülers. */
	protected String geburtsdatum;

	/** Das Geburtsland des Schülers. */
	protected String geburtsland;

	/** Das Geburtsland der Mutter des Schülers. */
	protected String geburtslandMutter;

	/** Das Geburtsland des Vaters des Schülers. */
	protected String geburtslandVater;

	/** Der Geburtsname des Schülers. */
	protected String geburtsname;

	/** Der Geburtsort des Schülers. */
	protected String geburtsort;

	/** Das Geschlecht des Schülers */
	protected Geschlecht geschlecht;

	/** Daten der Abiturdaten der GOSt. */
	protected ReportingSchuelerGostAbitur gostAbitur;

	/** Die Klausuren des Schülers in einer GOSt-Klausurplanung. Sie werden beim Initialisieren eines Klausurplans initialisiert. */
	protected List<ReportingGostKlausurplanungSchuelerklausur> gostKlausurplanungSchuelerklausuren;

	/** Die Kursbelegungen des Schülers in einer GOSt-Kursplanung. Sie werden beim Initialisieren eines Blockungsergebnisses initialisiert. */
	protected List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen;

	/** Daten der GOSt-Laufbahnplanung. */
	protected ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung;

	/** Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers. */
	protected Long haltestelleID;

	/** Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat. */
	protected boolean hatMasernimpfnachweis;

	/** Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist. */
	protected boolean hatMigrationshintergrund;

	/** Ggf. die Hausnummer zur Straße im Wohnort des Schülers. */
	protected String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers. */
	protected String hausnummerZusatz;

	/** Die ID des Schülers. */
	protected long id;

	/** Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht. */
	protected Boolean istBerufsschulpflichtErfuellt;

	/** Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht. */
	protected boolean istDuplikat;

	/** Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht. */
	protected Boolean istSchulpflichtErfuellt;

	/** Gibt an, ob der Schüler volljährig ist oder nicht. */
	protected Boolean istVolljaehrig;

	/** Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte. */
	protected boolean keineAuskunftAnDritte;

	/** Daten aller Lernabschnitte. */
	protected List<ReportingSchuelerLernabschnitt> lernabschnitte;

	/** Der Nachname des Schülers. */
	protected String nachname;

	/** Das Datum der Religionsabmeldung des Schülers. */
	protected String religionabmeldung;

	/** Das Datum der Religionsanmeldung des Schülers. */
	protected String religionanmeldung;

	/** Die Religion des Schülers. */
	protected ReligionEintrag religion;

	/** Daten aller Sprachbelegungen. */
	protected List<ReportingSchuelerSprachbelegung> sprachbelegungen;

	/** Die erste Staatsangehörigkeit des Schülers. */
	protected Nationalitaeten staatsangehoerigkeit1;

	/** Die zweite Staatsangehörigkeit des Schülers. */
	protected Nationalitaeten staatsangehoerigkeit2;

	/** Der Status des Schülers. */
	protected SchuelerStatus status;

	/** Ggf. der Straßenname im Wohnort des Schülers. */
	protected String strassenname;

	/** Die Telefonnummer des Schülers. */
	protected String telefon;

	/** Die Mobilnummer des Schülers. */
	protected String telefonMobil;

	/** Die Verkehrssprache der Familie des Schülers. */
	protected String verkehrspracheFamilie;

	/** Der Vorname des Schülers. */
	protected String vorname;

	/** Alle Vornamen, sofern es mehrere gibt, des Schülers. */
	protected String vornamen;

	/** Der Wohnort des Schülers. */
	protected OrtKatalogEintrag wohnort;

	/** Der Name des Wohnorts des Schülers. */
	protected String wohnortname;

	/** Der Ortsteil des Wohnorts des Schülers. */
	protected OrtsteilKatalogEintrag wohnortsteil;

	/** Der Name des Ortsteils des Wohnorts des Schülers. */
	protected String wohnortsteilname;

	/** Das Zuzugsjahr des Schülers. */
	protected Integer zuzugsjahr;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param aktuellerLernabschnitt Daten des aktuellen Lernabschnitts.
	 * @param anmeldedatum Das Anmeldedatum des Schülers.
	 * @param aufnahmedatum Das Aufnahmedatum des Schülers.
	 * @param auswahlLernabschnitt Daten des ausgewählten Lernabschnitts.
	 * @param druckeKonfessionAufZeugnisse Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 * @param emailPrivat Die private Email-Adresse des Schülers.
	 * @param emailSchule Die schulische E-Mail-Adresse des Schülers.
	 * @param erhaeltMeisterBAFOEG Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 * @param erhaeltSchuelerBAFOEG Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 * @param externeSchulNr Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist.
	 * @param fahrschuelerArtID Die ID der Art des Fahr des Schülers.
	 * @param foto Das Foto (in Base64 kodiert) des Schülers.
	 * @param geburtsdatum Das Geburtsdatum des Schülers.
	 * @param geburtsland Das Geburtsland des Schülers.
	 * @param geburtslandMutter Das Geburtsland der Mutter des Schülers.
	 * @param geburtslandVater Das Geburtsland des Vaters des Schülers.
	 * @param geburtsname Der Geburtsname des Schülers.
	 * @param geburtsort Der Geburtsort des Schülers.
	 * @param geschlecht Das Geschlecht des Schülers.
	 * @param gostAbitur Daten der Abiturdaten der GOSt.
	 * @param gostKlausurplanungSchuelerklausuren Die Klausuren des Schülers in einer GOSt-Klausurplanung. Sie werden beim Initialisieren eines Klausurplans initialisiert.
	 * @param gostKursplanungKursbelegungen Die Kursbelegungen des Schülers in einer GOSt-Kursplanung. Sie werden beim Initialisieren eines Blockungsergebnisses initialisiert.
	 * @param gostLaufbahnplanung Daten der GOSt-Laufbahnplanung.
	 * @param haltestelleID Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers.
	 * @param hatMasernimpfnachweis Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat.
	 * @param hatMigrationshintergrund Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist.
	 * @param hausnummer Ggf. die Hausnummer zur Straße im Wohnort des Schülers.
	 * @param hausnummerZusatz Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers.
	 * @param id Die ID des Schülers.
	 * @param istBerufsschulpflichtErfuellt Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht.
	 * @param istDuplikat Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht.
	 * @param istSchulpflichtErfuellt Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht.
	 * @param istVolljaehrig Gibt an, ob der Schüler volljährig ist oder nicht.
	 * @param keineAuskunftAnDritte Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.
	 * @param lernabschnitte Daten aller Lernabschnitte.
	 * @param nachname Der Nachname des Schülers.
	 * @param religionabmeldung Das Datum der Religionsabmeldung des Schülers.
	 * @param religionanmeldung Das Datum der Religionsanmeldung des Schülers.
	 * @param religion Die Religion des Schülers.
	 * @param sprachbelegungen Daten aller Sprachbelegungen.
	 * @param staatsangehoerigkeit1 Die erste Staatsangehörigkeit des Schülers.
	 * @param staatsangehoerigkeit2 Die zweite Staatsangehörigkeit des Schülers.
	 * @param status Der Status des Schülers.
	 * @param strassenname Ggf. der Straßenname im Wohnort des Schülers.
	 * @param telefon Die Telefonnummer des Schülers.
	 * @param telefonMobil Die Mobilnummer des Schülers.
	 * @param verkehrspracheFamilie Die Verkehrssprache der Familie des Schülers.
	 * @param vorname Der Vorname des Schülers.
	 * @param vornamen Alle Vornamen, sofern es mehrere gibt, des Schülers.
	 * @param wohnort Der Wohnort des Schülers.
	 * @param wohnortname Der Name des Wohnorts des Schülers.
	 * @param wohnortsteil Der Ortsteil des Wohnorts des Schülers.
	 * @param wohnortsteilname Der Name des Ortsteils des Wohnorts des Schülers.
	 * @param zuzugsjahr Das Zuzugsjahr des Schülers.
	 */
	public ReportingSchueler(final ReportingSchuelerLernabschnitt aktuellerLernabschnitt, final String anmeldedatum, final String aufnahmedatum,
			final ReportingSchuelerLernabschnitt auswahlLernabschnitt, final boolean druckeKonfessionAufZeugnisse, final String emailPrivat,
			final String emailSchule, final boolean erhaeltMeisterBAFOEG, final boolean erhaeltSchuelerBAFOEG, final String externeSchulNr,
			final Long fahrschuelerArtID, final String foto, final String geburtsdatum, final String geburtsland, final String geburtslandMutter,
			final String geburtslandVater, final String geburtsname, final String geburtsort, final Geschlecht geschlecht,
			final ReportingSchuelerGostAbitur gostAbitur, final List<ReportingGostKlausurplanungSchuelerklausur> gostKlausurplanungSchuelerklausuren,
			final List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen,
			final ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung, final Long haltestelleID, final boolean hatMasernimpfnachweis,
			final boolean hatMigrationshintergrund, final String hausnummer, final String hausnummerZusatz, final long id,
			final Boolean istBerufsschulpflichtErfuellt, final boolean istDuplikat, final Boolean istSchulpflichtErfuellt, final Boolean istVolljaehrig,
			final boolean keineAuskunftAnDritte, final List<ReportingSchuelerLernabschnitt> lernabschnitte, final String nachname,
			final String religionabmeldung, final String religionanmeldung, final ReligionEintrag religion,
			final List<ReportingSchuelerSprachbelegung> sprachbelegungen, final Nationalitaeten staatsangehoerigkeit1,
			final Nationalitaeten staatsangehoerigkeit2, final SchuelerStatus status, final String strassenname, final String telefon,
			final String telefonMobil, final String verkehrspracheFamilie, final String vorname, final String vornamen, final OrtKatalogEintrag wohnort,
			final String wohnortname, final OrtsteilKatalogEintrag wohnortsteil, final String wohnortsteilname, final Integer zuzugsjahr) {
		this.aktuellerLernabschnitt = aktuellerLernabschnitt;
		this.anmeldedatum = anmeldedatum;
		this.aufnahmedatum = aufnahmedatum;
		this.auswahlLernabschnitt = auswahlLernabschnitt;
		this.druckeKonfessionAufZeugnisse = druckeKonfessionAufZeugnisse;
		this.emailPrivat = emailPrivat;
		this.emailSchule = emailSchule;
		this.erhaeltMeisterBAFOEG = erhaeltMeisterBAFOEG;
		this.erhaeltSchuelerBAFOEG = erhaeltSchuelerBAFOEG;
		this.externeSchulNr = externeSchulNr;
		this.fahrschuelerArtID = fahrschuelerArtID;
		this.foto = foto;
		this.geburtsdatum = geburtsdatum;
		this.geburtsland = geburtsland;
		this.geburtslandMutter = geburtslandMutter;
		this.geburtslandVater = geburtslandVater;
		this.geburtsname = geburtsname;
		this.geburtsort = geburtsort;
		this.geschlecht = geschlecht;
		this.gostAbitur = gostAbitur;
		this.gostKlausurplanungSchuelerklausuren = gostKlausurplanungSchuelerklausuren;
		this.gostKursplanungKursbelegungen = gostKursplanungKursbelegungen;
		this.gostLaufbahnplanung = gostLaufbahnplanung;
		this.haltestelleID = haltestelleID;
		this.hatMasernimpfnachweis = hatMasernimpfnachweis;
		this.hatMigrationshintergrund = hatMigrationshintergrund;
		this.hausnummer = hausnummer;
		this.hausnummerZusatz = hausnummerZusatz;
		this.id = id;
		this.istBerufsschulpflichtErfuellt = istBerufsschulpflichtErfuellt;
		this.istDuplikat = istDuplikat;
		this.istSchulpflichtErfuellt = istSchulpflichtErfuellt;
		this.istVolljaehrig = istVolljaehrig;
		this.keineAuskunftAnDritte = keineAuskunftAnDritte;
		this.lernabschnitte = lernabschnitte;
		this.nachname = nachname;
		this.religionabmeldung = religionabmeldung;
		this.religionanmeldung = religionanmeldung;
		this.religion = religion;
		this.sprachbelegungen = sprachbelegungen;
		this.staatsangehoerigkeit1 = staatsangehoerigkeit1;
		this.staatsangehoerigkeit2 = staatsangehoerigkeit2;
		this.status = status;
		this.strassenname = strassenname;
		this.telefon = telefon;
		this.telefonMobil = telefonMobil;
		this.verkehrspracheFamilie = verkehrspracheFamilie;
		this.vorname = vorname;
		this.vornamen = vornamen;
		this.wohnort = wohnort;
		this.wohnortname = wohnortname;
		this.wohnortsteil = wohnortsteil;
		this.wohnortsteilname = wohnortsteilname;
		this.zuzugsjahr = zuzugsjahr;
	}



	// ##### Getter #####

	/**
	 * Daten des aktuellen Lernabschnitts.
	 * @return Inhalt des Feldes aktuellerLernabschnitt
	 */
	public ReportingSchuelerLernabschnitt aktuellerLernabschnitt() {
		return aktuellerLernabschnitt;
	}

	/**
	 * Das Anmeldedatum des Schülers.
	 * @return Inhalt des Feldes anmeldedatum
	 */
	public String anmeldedatum() {
		return anmeldedatum;
	}

	/**
	 * Das Aufnahmedatum des Schülers.
	 * @return Inhalt des Feldes aufnahmedatum
	 */
	public String aufnahmedatum() {
		return aufnahmedatum;
	}

	/**
	 * Daten des ausgewählten Lernabschnitts.
	 * @return Inhalt des Feldes auswahlLernabschnitt
	 */
	public ReportingSchuelerLernabschnitt auswahlLernabschnitt() {
		return auswahlLernabschnitt;
	}

	/**
	 * Textfeld mit Bemerkungen zum Schülerdatensatz.
	 * @return Inhalt des Feldes bemerkungen
	 */
	public String bemerkungen() {
		return bemerkungen;
	}

	/**
	 * Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 * @return Inhalt des Feldes druckeKonfessionAufZeugnisse
	 */
	public boolean druckeKonfessionAufZeugnisse() {
		return druckeKonfessionAufZeugnisse;
	}

	/**
	 * Die private E-Mail-Adresse des Schülers.
	 * @return Inhalt des Feldes emailPrivat
	 */
	public String emailPrivat() {
		return emailPrivat;
	}

	/**
	 * Die schulische E-Mail-Adresse des Schülers.
	 * @return Inhalt des Feldes emailSchule
	 */
	public String emailSchule() {
		return emailSchule;
	}

	/**
	 * Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 * @return Inhalt des Feldes erhaeltMeisterBAFOEG
	 */
	public boolean erhaeltMeisterBAFOEG() {
		return erhaeltMeisterBAFOEG;
	}

	/**
	 * Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 * @return Inhalt des Feldes erhaeltSchuelerBAFOEG
	 */
	public boolean erhaeltSchuelerBAFOEG() {
		return erhaeltSchuelerBAFOEG;
	}

	/**
	 * Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist.
	 * @return Inhalt des Feldes externeSchulNr
	 */
	public String externeSchulNr() {
		return externeSchulNr;
	}

	/**
	 * Die ID der Art des Fahr des Schülers.
	 * @return Inhalt des Feldes fahrschuelerArtID
	 */
	public Long fahrschuelerArtID() {
		return fahrschuelerArtID;
	}

	/**
	 * Das Foto (in Base64 kodiert) des Schülers.
	 * @return Inhalt des Feldes foto
	 */
	public String foto() {
		return foto;
	}

	/**
	 * Das Geburtsdatum des Schülers.
	 * @return Inhalt des Feldes geburtsdatum
	 */
	public String geburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * Das Geburtsland des Schülers.
	 * @return Inhalt des Feldes geburtsland
	 */
	public String geburtsland() {
		return geburtsland;
	}

	/**
	 * Das Geburtsland der Mutter des Schülers.
	 * @return Inhalt des Feldes geburtslandMutter
	 */
	public String geburtslandMutter() {
		return geburtslandMutter;
	}

	/**
	 * Das Geburtsland des Vaters des Schülers.
	 * @return Inhalt des Feldes geburtslandVater
	 */
	public String geburtslandVater() {
		return geburtslandVater;
	}

	/**
	 * Der Geburtsname des Schülers.
	 * @return Inhalt des Feldes geburtsname
	 */
	public String geburtsname() {
		return geburtsname;
	}

	/**
	 * Der Geburtsort des Schülers.
	 * @return Inhalt des Feldes geburtsort
	 */
	public String geburtsort() {
		return geburtsort;
	}

	/**
	 * Das Geschlecht des Schülers
	 * @return Inhalt des Feldes geschlecht
	 */
	public Geschlecht geschlecht() {
		return geschlecht;
	}

	/**
	 * Die Abiturdaten der GOSt.
	 * @return Inhalt des Feldes gostAbitur
	 */
	public ReportingSchuelerGostAbitur gostAbitur() {
		return gostAbitur;
	}

	/**
	 * Daten der GOSt-Laufbahnplanung.
	 * @return Inhalt des Feldes gostLaufbahnplanung
	 */
	public ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung() {
		return gostLaufbahnplanung;
	}

	/**
	 * Die Klausuren des Schülers in einer GOSt-Klausurplanung. Sie werden beim Initialisieren eines Klausurplans initialisiert.
	 * @return Inhalt des Feldes gostKlausurplanungSchuelerklausuren
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> gostKlausurplanungSchuelerklausuren() {
		return gostKlausurplanungSchuelerklausuren;
	}

	/**
	 * Gibt die Kursbelegung des Schülers aus der Kursplanung der gymnasialen Oberstufe zurück, die zur angegebenen ID des Kurses gehört oder null.
	 * @param idKurs	Die ID des Kurses, dessen Kursbelegung gesucht ist.
	 * @return			Die Kursbelegung des Kurses.
	 */
	@JsonIgnore
	public ReportingSchuelerGostKursplanungKursbelegung getGostKursplanungKursbelegungById(final long idKurs) {
		return this.gostKursplanungKursbelegungen().stream().filter(b -> b.kurs().id() == idKurs).findAny().orElse(null);
	}

	/**
	 * Die Kursbelegungen des Schülers in einer GOSt-Kursplanung. Sie werden beim Initialisieren eines Blockungsergebnisses initialisiert.
	 * @return Inhalt des Feldes gostKursplanungKursbelegungen
	 */
	public List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen() {
		return gostKursplanungKursbelegungen;
	}

	/**
	 * Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers.
	 * @return Inhalt des Feldes haltestelleID
	 */
	public Long haltestelleID() {
		return haltestelleID;
	}

	/**
	 * Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat.
	 * @return Inhalt des Feldes hatMasernimpfnachweis
	 */
	public boolean hatMasernimpfnachweis() {
		return hatMasernimpfnachweis;
	}

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist.
	 * @return Inhalt des Feldes hatMigrationshintergrund
	 */
	public boolean hatMigrationshintergrund() {
		return hatMigrationshintergrund;
	}

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Schülers.
	 * @return Inhalt des Feldes hausnummer
	 */
	public String hausnummer() {
		return hausnummer;
	}

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers.
	 * @return Inhalt des Feldes hausnummerZusatz
	 */
	public String hausnummerZusatz() {
		return hausnummerZusatz;
	}

	/**
	 * Die ID des Schülers.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht.
	 * @return Inhalt des Feldes istBerufsschulpflichtErfuellt
	 */
	public Boolean istBerufsschulpflichtErfuellt() {
		return istBerufsschulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht.
	 * @return Inhalt des Feldes istDuplikat
	 */
	public boolean istDuplikat() {
		return istDuplikat;
	}

	/**
	 * Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht.
	 * @return Inhalt des Feldes istSchulpflichtErfuellt
	 */
	public Boolean istSchulpflichtErfuellt() {
		return istSchulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob der Schüler volljährig ist oder nicht.
	 * @return Inhalt des Feldes istVolljaehrig
	 */
	public Boolean istVolljaehrig() {
		return istVolljaehrig;
	}

	/**
	 * Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.
	 * @return Inhalt des Feldes keineAuskunftAnDritte
	 */
	public boolean keineAuskunftAnDritte() {
		return keineAuskunftAnDritte;
	}

	/**
	 * Daten aller Lernabschnitte.
	 * @return Inhalt des Feldes lernabschnitte
	 */
	public List<ReportingSchuelerLernabschnitt> lernabschnitte() {
		return lernabschnitte;
	}

	/**
	 * Der Nachname des Schülers.
	 * @return Inhalt des Feldes nachname
	 */
	public String nachname() {
		return nachname;
	}

	/**
	 * Das Datum der Religionsabmeldung des Schülers.
	 * @return Inhalt des Feldes religionabmeldung
	 */
	public String religionabmeldung() {
		return religionabmeldung;
	}

	/**
	 * Das Datum der Religionsanmeldung des Schülers.
	 * @return Inhalt des Feldes religionanmeldung
	 */
	public String religionanmeldung() {
		return religionanmeldung;
	}

	/**
	 * Die Religion des Schülers.
	 * @return Inhalt des Feldes religion
	 */
	public ReligionEintrag religion() {
		return religion;
	}

	/**
	 * Daten aller Sprachbelegungen.
	 * @return Inhalt des Feldes sprachbelegungen
	 */
	public List<ReportingSchuelerSprachbelegung> sprachbelegungen() {
		return sprachbelegungen;
	}

	/**
	 * Die erste Staatsangehörigkeit des Schülers.
	 * @return Inhalt des Feldes staatsangehoerigkeit1
	 */
	public Nationalitaeten staatsangehoerigkeit1() {
		return staatsangehoerigkeit1;
	}

	/**
	 * Die zweite Staatsangehörigkeit des Schülers.
	 * @return Inhalt des Feldes staatsangehoerigkeit2
	 */
	public Nationalitaeten staatsangehoerigkeit2() {
		return staatsangehoerigkeit2;
	}

	/**
	 * Der Status des Schülers.
	 * @return Inhalt des Feldes status
	 */
	public SchuelerStatus status() {
		return status;
	}

	/**
	 * Ggf. der Straßenname im Wohnort des Schülers.
	 * @return Inhalt des Feldes strassenname
	 */
	public String strassenname() {
		return strassenname;
	}

	/**
	 * Die Telefonnummer des Schülers.
	 * @return Inhalt des Feldes telefon
	 */
	public String telefon() {
		return telefon;
	}

	/**
	 * Die Mobilnummer des Schülers.
	 * @return Inhalt des Feldes telefonMobil
	 */
	public String telefonMobil() {
		return telefonMobil;
	}

	/**
	 * Die Verkehrssprache der Familie des Schülers.
	 * @return Inhalt des Feldes verkehrspracheFamilie
	 */
	public String verkehrspracheFamilie() {
		return verkehrspracheFamilie;
	}

	/**
	 * Der Vorname des Schülers.
	 * @return Inhalt des Feldes vorname
	 */
	public String vorname() {
		return vorname;
	}

	/**
	 * Alle Vornamen, sofern es mehrere gibt, des Schülers.
	 * @return Inhalt des Feldes vornamen
	 */
	public String vornamen() {
		return vornamen;
	}

	/**
	 * Der Wohnort des Schülers.
	 * @return Inhalt des Feldes wohnort
	 */
	public OrtKatalogEintrag wohnort() {
		return wohnort;
	}

	/**
	 * Der Name des Wohnorts des Schülers.
	 * @return Inhalt des Feldes wohnortname
	 */
	public String wohnortname() {
		return wohnortname;
	}

	/**
	 * Der Ortsteil des Wohnorts des Schülers.
	 * @return Inhalt des Feldes wohnortsteil
	 */
	public OrtsteilKatalogEintrag wohnortsteil() {
		return wohnortsteil;
	}

	/**
	 * Der Name des Ortsteils des Wohnorts des Schülers.
	 * @return Inhalt des Feldes wohnortsteilname
	 */
	public String wohnortsteilname() {
		return wohnortsteilname;
	}

	/**
	 * Das Zuzugsjahr des Schülers.
	 * @return Inhalt des Feldes zuzugsjahr
	 */
	public Integer zuzugsjahr() {
		return zuzugsjahr;
	}

}
