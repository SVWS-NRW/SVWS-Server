package de.svws_nrw.module.reporting.types.schueler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.schule.Religion;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbiturdaten;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ReportingSchuelerGostKursplanungKursbelegung;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schueler.</p>
 * <p>Sie enthält die Stammdaten eines Schülers, so wie alle weiteren abhängigen Daten (Lernabschnitte, Plaungsdaten usw.).</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchueler {

	/** Daten des aktuellen Lernabschnitts. */
	private ReportingSchuelerLernabschnitt aktuellerLernabschnitt = null;

	/** Das Anmeldedatum des Schülers. */
	private String anmeldedatum;

	/** Das Aufnahmedatum des Schülers. */
	private String aufnahmedatum;

	/** Textfeld mit Bemerkungen zum Schülerdatensatz. */
	private String bemerkungen;

	/** Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll. */
	private boolean druckeKonfessionAufZeugnisse;

	/** Die private Email-Adresse des Schülers. */
	private String emailPrivat;

	/** Die schulische E-Mail-Adresse des Schülers. */
	private String emailSchule;

	/** Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht. */
	private boolean erhaeltMeisterBAFOEG;

	/** Gibt an, ob der Schüler BAFÖG erhält oder nicht. */
	private boolean erhaeltSchuelerBAFOEG;

	/** Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist. */
	private String externeSchulNr;

	/** Die ID der Art des Fahr des Schülers. */
	private Long fahrschuelerArtID;

	/** Das Foto (in Base64 kodiert) des Schülers. */
	private String foto;

	/** Das Geburtsdatum des Schülers. */
	private String geburtsdatum;

	/** Das Geburtsland des Schülers. */
	private String geburtsland;

	/** Das Geburtsland der Mutter des Schülers. */
	private String geburtslandMutter;

	/** Das Geburtsland des Vaters des Schülers. */
	private String geburtslandVater;

	/** Der Geburtsname des Schülers. */
	private String geburtsname;

	/** Der Geburtsort des Schülers. */
	private String geburtsort;

	/** Das Geschlecht des Schülers */
	private Geschlecht geschlecht;

	/** Daten der Abiturdaten der GOSt. */
	private ReportingSchuelerGostAbiturdaten gostAbiturdaten = null;

	/** Daten der GOSt-Laufbahnplanung. */
	private ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung = null;

	/** Die Kursbelegungen des Schülers in einer GOSt-Kursplanung. Sie werden beim Initialisieren eines Blockungsergebnisses initialisiert. */
	private List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen;

	/** Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers. */
	private Long haltestelleID;

	/** Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat. */
	private boolean hatMasernimpfnachweis;

	/** Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist. */
	private boolean hatMigrationshintergrund;

	/** Ggf. die Hausnummer zur Straße im Wohnort des Schülers. */
	private String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers. */
	private String hausnummerZusatz;

	/** Die ID des Schülers. */
	private long id;

	/** Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht. */
	private Boolean istBerufsschulpflichtErfuellt;

	/** Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht. */
	private boolean istDuplikat;

	/** Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht. */
	private Boolean istSchulpflichtErfuellt;

	/** Gibt an, ob der Schüler volljährig ist oder nicht. */
	private Boolean istVolljaehrig;

	/** Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte. */
	private boolean keineAuskunftAnDritte;

	/** Daten aller Lernabschnitte. */
	private List<ReportingSchuelerLernabschnitt> lernabschnitte = new ArrayList<>();

	/** Der Nachname des Schülers. */
	private String nachname;

	/** Das Datum der Religionsabmeldung des Schülers. */
	private String religionabmeldung;

	/** Das Datum der Religionsanmeldung des Schülers. */
	private String religionanmeldung;

	/** Die Religion des Schülers. */
	private Religion religion;

	/** Die erste Staatsangehörigkeit des Schülers. */
	private Nationalitaeten staatsangehoerigkeit1;

	/** Die zweite Staatsangehörigkeit des Schülers. */
	private Nationalitaeten staatsangehoerigkeit2;

	/** Der Status des Schülers. */
	private SchuelerStatus status;

	/** Ggf. der Straßenname im Wohnort des Schülers. */
	private String strassenname;

	/** Die Telefonnummer des Schülers. */
	private String telefon;

	/** Die Mobilnummer des Schülers. */
	private String telefonMobil;

	/** Die Verkehrssprache der Familie des Schülers. */
	private String verkehrspracheFamilie;

	/** Der Vorname des Schülers. */
	private String vorname;

	/** Alle Vornamen, sofern es mehrere gibt, des Schülers. */
	private String vornamen;

	/** Der Wohnort des Schülers. */
	private OrtKatalogEintrag wohnort;

	/** Der Name des Wohnorts des Schülers. */
	private String wohnortname;

	/** Der Ortsteil des Wohnorts des Schülers. */
	private OrtsteilKatalogEintrag wohnortsteil;

	/** Der Name des Ortsteils des Wohnorts des Schülers. */
	private String wohnortsteilname;

	/** Das Zuzugsjahr des Schülers. */
	private Integer zuzugsjahr;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param aktuellerLernabschnitt Daten des aktuellen Lernabschnitts.
	 * @param anmeldedatum Das Anmeldedatum des Schülers.
	 * @param aufnahmedatum Das Aufnahmedatum des Schülers.
	 * @param bemerkungen Textfeld mit Bemerkungen zum Schülerdatensatz.
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
	 * @param gostAbiturdaten Daten der Abiturdaten der GOSt.
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
	public ReportingSchueler(final ReportingSchuelerLernabschnitt aktuellerLernabschnitt, final String anmeldedatum, final String aufnahmedatum, final String bemerkungen, final boolean druckeKonfessionAufZeugnisse, final String emailPrivat, final String emailSchule, final boolean erhaeltMeisterBAFOEG, final boolean erhaeltSchuelerBAFOEG, final String externeSchulNr, final Long fahrschuelerArtID, final String foto, final String geburtsdatum, final String geburtsland, final String geburtslandMutter, final String geburtslandVater, final String geburtsname, final String geburtsort, final Geschlecht geschlecht, final ReportingSchuelerGostAbiturdaten gostAbiturdaten, final List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen, final ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung, final Long haltestelleID, final boolean hatMasernimpfnachweis, final boolean hatMigrationshintergrund, final String hausnummer, final String hausnummerZusatz, final long id, final Boolean istBerufsschulpflichtErfuellt, final boolean istDuplikat, final Boolean istSchulpflichtErfuellt, final Boolean istVolljaehrig, final boolean keineAuskunftAnDritte, final List<ReportingSchuelerLernabschnitt> lernabschnitte, final String nachname, final String religionabmeldung, final String religionanmeldung, final Religion religion, final Nationalitaeten staatsangehoerigkeit1, final Nationalitaeten staatsangehoerigkeit2, final SchuelerStatus status, final String strassenname, final String telefon, final String telefonMobil, final String verkehrspracheFamilie, final String vorname, final String vornamen, final OrtKatalogEintrag wohnort, final String wohnortname, final OrtsteilKatalogEintrag wohnortsteil, final String wohnortsteilname, final Integer zuzugsjahr) {
		this.aktuellerLernabschnitt = aktuellerLernabschnitt;
		this.anmeldedatum = anmeldedatum;
		this.aufnahmedatum = aufnahmedatum;
		this.bemerkungen = bemerkungen;
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
		this.gostAbiturdaten = gostAbiturdaten;
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



	// ##### Getter und Setter #####

	/**
	 * Daten des aktuellen Lernabschnitts.
	 * @return Inhalt des Feldes aktuellerLernabschnitt
	 */
	public ReportingSchuelerLernabschnitt aktuellerLernabschnitt() {
		return aktuellerLernabschnitt;
	}

	/**
	 * Daten des aktuellen Lernabschnitts wird gesetzt.
	 * @param aktuellerLernabschnitt Neuer Wert für das Feld aktuellerLernabschnitt
	 */
	public void setAktuellerLernabschnitt(final ReportingSchuelerLernabschnitt aktuellerLernabschnitt) {
		this.aktuellerLernabschnitt = aktuellerLernabschnitt;
	}

	/**
	 * Das Anmeldedatum des Schülers.
	 * @return Inhalt des Feldes anmeldedatum
	 */
	public String anmeldedatum() {
		return anmeldedatum;
	}

	/**
	 * Das Anmeldedatum des Schülers wird gesetzt.
	 * @param anmeldedatum Neuer Wert für das Feld anmeldedatum
	 */
	public void setAnmeldedatum(final String anmeldedatum) {
		this.anmeldedatum = anmeldedatum;
	}

	/**
	 * Das Aufnahmedatum des Schülers.
	 * @return Inhalt des Feldes aufnahmedatum
	 */
	public String aufnahmedatum() {
		return aufnahmedatum;
	}

	/**
	 * Das Aufnahmedatum des Schülers wird gesetzt.
	 * @param aufnahmedatum Neuer Wert für das Feld aufnahmedatum
	 */
	public void setAufnahmedatum(final String aufnahmedatum) {
		this.aufnahmedatum = aufnahmedatum;
	}

	/**
	 * Textfeld mit Bemerkungen zum Schülerdatensatz.
	 * @return Inhalt des Feldes bemerkungen
	 */
	public String bemerkungen() {
		return bemerkungen;
	}

	/**
	 * Textfeld mit Bemerkungen zum Schülerdatensatz wird gesetzt.
	 * @param bemerkungen Neuer Wert für das Feld bemerkungen
	 */
	public void setBemerkungen(final String bemerkungen) {
		this.bemerkungen = bemerkungen;
	}

	/**
	 * Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 * @return Inhalt des Feldes druckeKonfessionAufZeugnisse
	 */
	public boolean druckeKonfessionAufZeugnisse() {
		return druckeKonfessionAufZeugnisse;
	}

	/**
	 * Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll wird gesetzt.
	 * @param druckeKonfessionAufZeugnisse Neuer Wert für das Feld druckeKonfessionAufZeugnisse
	 */
	public void setDruckeKonfessionAufZeugnisse(final boolean druckeKonfessionAufZeugnisse) {
		this.druckeKonfessionAufZeugnisse = druckeKonfessionAufZeugnisse;
	}

	/**
	 * Die private E-Mail-Adresse des Schülers.
	 * @return Inhalt des Feldes emailPrivat
	 */
	public String emailPrivat() {
		return emailPrivat;
	}

	/**
	 * Die private E-Mail-Adresse des Schülers wird gesetzt.
	 * @param emailPrivat Neuer Wert für das Feld emailPrivat
	 */
	public void setEmailPrivat(final String emailPrivat) {
		this.emailPrivat = emailPrivat;
	}

	/**
	 * Die schulische E-Mail-Adresse des Schülers.
	 * @return Inhalt des Feldes emailSchule
	 */
	public String emailSchule() {
		return emailSchule;
	}

	/**
	 * Die schulische E-Mail-Adresse des Schülers wird gesetzt.
	 * @param emailSchule Neuer Wert für das Feld emailSchule
	 */
	public void setEmailSchule(final String emailSchule) {
		this.emailSchule = emailSchule;
	}

	/**
	 * Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 * @return Inhalt des Feldes erhaeltMeisterBAFOEG
	 */
	public boolean erhaeltMeisterBAFOEG() {
		return erhaeltMeisterBAFOEG;
	}

	/**
	 * Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht wird gesetzt.
	 * @param erhaeltMeisterBAFOEG Neuer Wert für das Feld erhaeltMeisterBAFOEG
	 */
	public void setErhaeltMeisterBAFOEG(final boolean erhaeltMeisterBAFOEG) {
		this.erhaeltMeisterBAFOEG = erhaeltMeisterBAFOEG;
	}

	/**
	 * Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 * @return Inhalt des Feldes erhaeltSchuelerBAFOEG
	 */
	public boolean erhaeltSchuelerBAFOEG() {
		return erhaeltSchuelerBAFOEG;
	}

	/**
	 * Gibt an, ob der Schüler BAFÖG erhält oder nicht wird gesetzt.
	 * @param erhaeltSchuelerBAFOEG Neuer Wert für das Feld erhaeltSchuelerBAFOEG
	 */
	public void setErhaeltSchuelerBAFOEG(final boolean erhaeltSchuelerBAFOEG) {
		this.erhaeltSchuelerBAFOEG = erhaeltSchuelerBAFOEG;
	}

	/**
	 * Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist.
	 * @return Inhalt des Feldes externeSchulNr
	 */
	public String externeSchulNr() {
		return externeSchulNr;
	}

	/**
	 * Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist, wird gesetzt.
	 * @param externeSchulNr Neuer Wert für das Feld externeSchulNr
	 */
	public void setExterneSchulNr(final String externeSchulNr) {
		this.externeSchulNr = externeSchulNr;
	}

	/**
	 * Die ID der Art des Fahr des Schülers.
	 * @return Inhalt des Feldes fahrschuelerArtID
	 */
	public Long fahrschuelerArtID() {
		return fahrschuelerArtID;
	}

	/**
	 * Die ID der Art des Fahr des Schülers wird gesetzt.
	 * @param fahrschuelerArtID Neuer Wert für das Feld fahrschuelerArtID
	 */
	public void setFahrschuelerArtID(final Long fahrschuelerArtID) {
		this.fahrschuelerArtID = fahrschuelerArtID;
	}

	/**
	 * Das Foto (in Base64 kodiert) des Schülers.
	 * @return Inhalt des Feldes foto
	 */
	public String foto() {
		return foto;
	}

	/**
	 * Das Foto (in Base64 kodiert) des Schülers wird gesetzt.
	 * @param foto Neuer Wert für das Feld foto
	 */
	public void setFoto(final String foto) {
		this.foto = foto;
	}

	/**
	 * Das Geburtsdatum des Schülers.
	 * @return Inhalt des Feldes geburtsdatum
	 */
	public String geburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * Das Geburtsdatum des Schülers wird gesetzt.
	 * @param geburtsdatum Neuer Wert für das Feld geburtsdatum
	 */
	public void setGeburtsdatum(final String geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	/**
	 * Das Geburtsland des Schülers.
	 * @return Inhalt des Feldes geburtsland
	 */
	public String geburtsland() {
		return geburtsland;
	}

	/**
	 * Das Geburtsland des Schülers wird gesetzt.
	 * @param geburtsland Neuer Wert für das Feld geburtsland
	 */
	public void setGeburtsland(final String geburtsland) {
		this.geburtsland = geburtsland;
	}

	/**
	 * Das Geburtsland der Mutter des Schülers.
	 * @return Inhalt des Feldes geburtslandMutter
	 */
	public String geburtslandMutter() {
		return geburtslandMutter;
	}

	/**
	 * Das Geburtsland der Mutter des Schülers wird gesetzt.
	 * @param geburtslandMutter Neuer Wert für das Feld geburtslandMutter
	 */
	public void setGeburtslandMutter(final String geburtslandMutter) {
		this.geburtslandMutter = geburtslandMutter;
	}

	/**
	 * Das Geburtsland des Vaters des Schülers.
	 * @return Inhalt des Feldes geburtslandVater
	 */
	public String geburtslandVater() {
		return geburtslandVater;
	}

	/**
	 * Das Geburtsland des Vaters des Schülers wird gesetzt.
	 * @param geburtslandVater Neuer Wert für das Feld geburtslandVater
	 */
	public void setGeburtslandVater(final String geburtslandVater) {
		this.geburtslandVater = geburtslandVater;
	}

	/**
	 * Der Geburtsname des Schülers.
	 * @return Inhalt des Feldes geburtsname
	 */
	public String geburtsname() {
		return geburtsname;
	}

	/**
	 * Der Geburtsname des Schülers wird gesetzt.
	 * @param geburtsname Neuer Wert für das Feld geburtsname
	 */
	public void setGeburtsname(final String geburtsname) {
		this.geburtsname = geburtsname;
	}

	/**
	 * Der Geburtsort des Schülers.
	 * @return Inhalt des Feldes geburtsort
	 */
	public String geburtsort() {
		return geburtsort;
	}

	/**
	 * Der Geburtsort des Schülers wird gesetzt.
	 * @param geburtsort Neuer Wert für das Feld geburtsort
	 */
	public void setGeburtsort(final String geburtsort) {
		this.geburtsort = geburtsort;
	}

	/**
	 * Das Geschlecht des Schülers
	 * @return Inhalt des Feldes geschlecht
	 */
	public Geschlecht geschlecht() {
		return geschlecht;
	}

	/**
	 * Das Geschlecht des Schülers wird gesetzt.
	 * @param geschlecht Neuer Wert für das Feld geschlecht
	 */
	public void setGeschlecht(final Geschlecht geschlecht) {
		this.geschlecht = geschlecht;
	}

	/**
	 * Daten der Abiturdaten der GOSt.
	 * @return Inhalt des Feldes gostAbiturdaten
	 */
	public ReportingSchuelerGostAbiturdaten gostAbiturdaten() {
		return gostAbiturdaten;
	}

	/**
	 * Daten der Abiturdaten der GOSt wird gesetzt.
	 * @param gostAbiturdaten Neuer Wert für das Feld gostAbiturdaten
	 */
	public void setGostAbiturdaten(final ReportingSchuelerGostAbiturdaten gostAbiturdaten) {
		this.gostAbiturdaten = gostAbiturdaten;
	}

	/**
	 * Daten der GOSt-Laufbahnplanung.
	 * @return Inhalt des Feldes gostLaufbahnplanung
	 */
	public ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung() {
		return gostLaufbahnplanung;
	}

	/**
	 * Daten der GOSt-Laufbahnplanung wird gesetzt.
	 * @param gostLaufbahnplanung Neuer Wert für das Feld gostLaufbahnplanung
	 */
	public void setGostLaufbahnplanung(final ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung) {
		this.gostLaufbahnplanung = gostLaufbahnplanung;
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
	 * Die Kursbelegungen des Schülers in einer GOSt-Kursplanung werden gesetzt. Sie werden beim Initialisieren eines Blockungsergebnisses initialisiert.
	 * @param gostKursplanungKursbelegungen Neuer Wert für das Feld gostKursplanung
	 */
	public void setGostKursplanung(final List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen) {
		this.gostKursplanungKursbelegungen = gostKursplanungKursbelegungen;
	}

	/**
	 * Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers.
	 * @return Inhalt des Feldes haltestelleID
	 */
	public Long haltestelleID() {
		return haltestelleID;
	}

	/**
	 * Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers wird gesetzt.
	 * @param haltestelleID Neuer Wert für das Feld haltestelleID
	 */
	public void setHaltestelleID(final Long haltestelleID) {
		this.haltestelleID = haltestelleID;
	}

	/**
	 * Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat.
	 * @return Inhalt des Feldes hatMasernimpfnachweis
	 */
	public boolean hatMasernimpfnachweis() {
		return hatMasernimpfnachweis;
	}

	/**
	 * Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat, wird gesetzt.
	 * @param hatMasernimpfnachweis Neuer Wert für das Feld hatMasernimpfnachweis
	 */
	public void setHatMasernimpfnachweis(final boolean hatMasernimpfnachweis) {
		this.hatMasernimpfnachweis = hatMasernimpfnachweis;
	}

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist.
	 * @return Inhalt des Feldes hatMigrationshintergrund
	 */
	public boolean hatMigrationshintergrund() {
		return hatMigrationshintergrund;
	}

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist, wird gesetzt.
	 * @param hatMigrationshintergrund Neuer Wert für das Feld hatMigrationshintergrund
	 */
	public void setHatMigrationshintergrund(final boolean hatMigrationshintergrund) {
		this.hatMigrationshintergrund = hatMigrationshintergrund;
	}

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Schülers.
	 * @return Inhalt des Feldes hausnummer
	 */
	public String hausnummer() {
		return hausnummer;
	}

	/**
	 * Ggf die Hausnummer zur Straße im Wohnort des Schülers wird gesetzt.
	 * @param hausnummer Neuer Wert für das Feld hausnummer
	 */
	public void setHausnummer(final String hausnummer) {
		this.hausnummer = hausnummer;
	}

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers.
	 * @return Inhalt des Feldes hausnummerZusatz
	 */
	public String hausnummerZusatz() {
		return hausnummerZusatz;
	}

	/**
	 * Ggf der Hausnummerzusatz zur Straße im Wohnort des Schülers wird gesetzt.
	 * @param hausnummerZusatz Neuer Wert für das Feld hausnummerZusatz
	 */
	public void setHausnummerZusatz(final String hausnummerZusatz) {
		this.hausnummerZusatz = hausnummerZusatz;
	}

	/**
	 * Die ID des Schülers.
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Die ID des Schülers wird gesetzt.
	 * @param id Neuer Wert für das Feld id
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht.
	 * @return Inhalt des Feldes istBerufsschulpflichtErfuellt
	 */
	public Boolean istBerufsschulpflichtErfuellt() {
		return istBerufsschulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht wird gesetzt.
	 * @param istBerufsschulpflichtErfuellt Neuer Wert für das Feld istBerufsschulpflichtErfuellt
	 */
	public void setIstBerufsschulpflichtErfuellt(final Boolean istBerufsschulpflichtErfuellt) {
		this.istBerufsschulpflichtErfuellt = istBerufsschulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht.
	 * @return Inhalt des Feldes istDuplikat
	 */
	public boolean istDuplikat() {
		return istDuplikat;
	}

	/**
	 * Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht wird gesetzt.
	 * @param istDuplikat Neuer Wert für das Feld istDuplikat
	 */
	public void setIstDuplikat(final boolean istDuplikat) {
		this.istDuplikat = istDuplikat;
	}

	/**
	 * Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht.
	 * @return Inhalt des Feldes istSchulpflichtErfuellt
	 */
	public Boolean istSchulpflichtErfuellt() {
		return istSchulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht wird gesetzt.
	 * @param istSchulpflichtErfuellt Neuer Wert für das Feld istSchulpflichtErfuellt
	 */
	public void setIstSchulpflichtErfuellt(final Boolean istSchulpflichtErfuellt) {
		this.istSchulpflichtErfuellt = istSchulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob der Schüler volljährig ist oder nicht.
	 * @return Inhalt des Feldes istVolljaehrig
	 */
	public Boolean istVolljaehrig() {
		return istVolljaehrig;
	}

	/**
	 * Gibt an, ob der Schüler volljährig ist oder nicht wird gesetzt.
	 * @param istVolljaehrig Neuer Wert für das Feld istVolljaehrig
	 */
	public void setIstVolljaehrig(final Boolean istVolljaehrig) {
		this.istVolljaehrig = istVolljaehrig;
	}

	/**
	 * Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.
	 * @return Inhalt des Feldes keineAuskunftAnDritte
	 */
	public boolean keineAuskunftAnDritte() {
		return keineAuskunftAnDritte;
	}

	/**
	 * Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte wird gesetzt.
	 * @param keineAuskunftAnDritte Neuer Wert für das Feld keineAuskunftAnDritte
	 */
	public void setKeineAuskunftAnDritte(final boolean keineAuskunftAnDritte) {
		this.keineAuskunftAnDritte = keineAuskunftAnDritte;
	}

	/**
	 * Daten aller Lernabschnitte.
	 * @return Inhalt des Feldes lernabschnitte
	 */
	public List<ReportingSchuelerLernabschnitt> lernabschnitte() {
		return lernabschnitte;
	}

	/**
	 * Daten aller Lernabschnitte wird gesetzt.
	 * @param lernabschnitte Neuer Wert für das Feld lernabschnitte
	 */
	public void setLernabschnitte(final List<ReportingSchuelerLernabschnitt> lernabschnitte) {
		this.lernabschnitte = lernabschnitte;
	}

	/**
	 * Der Nachname des Schülers.
	 * @return Inhalt des Feldes nachname
	 */
	public String nachname() {
		return nachname;
	}

	/**
	 * Der Nachname des Schülers wird gesetzt.
	 * @param nachname Neuer Wert für das Feld nachname
	 */
	public void setNachname(final String nachname) {
		this.nachname = nachname;
	}

	/**
	 * Das Datum der Religionsabmeldung des Schülers.
	 * @return Inhalt des Feldes religionabmeldung
	 */
	public String religionabmeldung() {
		return religionabmeldung;
	}

	/**
	 * Das Datum der Religionsabmeldung des Schülers wird gesetzt.
	 * @param religionabmeldung Neuer Wert für das Feld religionabmeldung
	 */
	public void setReligionabmeldung(final String religionabmeldung) {
		this.religionabmeldung = religionabmeldung;
	}

	/**
	 * Das Datum der Religionsanmeldung des Schülers.
	 * @return Inhalt des Feldes religionanmeldung
	 */
	public String religionanmeldung() {
		return religionanmeldung;
	}

	/**
	 * Das Datum der Religionsanmeldung des Schülers wird gesetzt.
	 * @param religionanmeldung Neuer Wert für das Feld religionanmeldung
	 */
	public void setReligionanmeldung(final String religionanmeldung) {
		this.religionanmeldung = religionanmeldung;
	}

	/**
	 * Die Religion des Schülers.
	 * @return Inhalt des Feldes religion
	 */
	public Religion religion() {
		return religion;
	}

	/**
	 * Die Religion des Schülers wird gesetzt.
	 * @param religion Neuer Wert für das Feld religion
	 */
	public void setReligion(final Religion religion) {
		this.religion = religion;
	}

	/**
	 * Die erste Staatsangehörigkeit des Schülers.
	 * @return Inhalt des Feldes staatsangehoerigkeit1
	 */
	public Nationalitaeten staatsangehoerigkeit1() {
		return staatsangehoerigkeit1;
	}

	/**
	 * Die erste Staatsangehörigkeit des Schülers wird gesetzt.
	 * @param staatsangehoerigkeit1 Neuer Wert für das Feld staatsangehoerigkeit1
	 */
	public void setStaatsangehoerigkeit1(final Nationalitaeten staatsangehoerigkeit1) {
		this.staatsangehoerigkeit1 = staatsangehoerigkeit1;
	}

	/**
	 * Die zweite Staatsangehörigkeit des Schülers.
	 * @return Inhalt des Feldes staatsangehoerigkeit2
	 */
	public Nationalitaeten staatsangehoerigkeit2() {
		return staatsangehoerigkeit2;
	}

	/**
	 * Die zweite Staatsangehörigkeit des Schülers wird gesetzt.
	 * @param staatsangehoerigkeit2 Neuer Wert für das Feld staatsangehoerigkeit2
	 */
	public void setStaatsangehoerigkeit2(final Nationalitaeten staatsangehoerigkeit2) {
		this.staatsangehoerigkeit2 = staatsangehoerigkeit2;
	}

	/**
	 * Der Status des Schülers.
	 * @return Inhalt des Feldes status
	 */
	public SchuelerStatus status() {
		return status;
	}

	/**
	 * Der Status des Schülers wird gesetzt.
	 * @param status Neuer Wert für das Feld status
	 */
	public void setStatus(final SchuelerStatus status) {
		this.status = status;
	}

	/**
	 * Ggf. der Straßenname im Wohnort des Schülers.
	 * @return Inhalt des Feldes strassenname
	 */
	public String strassenname() {
		return strassenname;
	}

	/**
	 * Ggf der Straßenname im Wohnort des Schülers wird gesetzt.
	 * @param strassenname Neuer Wert für das Feld strassenname
	 */
	public void setStrassenname(final String strassenname) {
		this.strassenname = strassenname;
	}

	/**
	 * Die Telefonnummer des Schülers.
	 * @return Inhalt des Feldes telefon
	 */
	public String telefon() {
		return telefon;
	}

	/**
	 * Die Telefonnummer des Schülers wird gesetzt.
	 * @param telefon Neuer Wert für das Feld telefon
	 */
	public void setTelefon(final String telefon) {
		this.telefon = telefon;
	}

	/**
	 * Die Mobilnummer des Schülers.
	 * @return Inhalt des Feldes telefonMobil
	 */
	public String telefonMobil() {
		return telefonMobil;
	}

	/**
	 * Die Mobilnummer des Schülers wird gesetzt.
	 * @param telefonMobil Neuer Wert für das Feld telefonMobil
	 */
	public void setTelefonMobil(final String telefonMobil) {
		this.telefonMobil = telefonMobil;
	}

	/**
	 * Die Verkehrssprache der Familie des Schülers.
	 * @return Inhalt des Feldes verkehrspracheFamilie
	 */
	public String verkehrspracheFamilie() {
		return verkehrspracheFamilie;
	}

	/**
	 * Die Verkehrssprache der Familie des Schülers wird gesetzt.
	 * @param verkehrspracheFamilie Neuer Wert für das Feld verkehrspracheFamilie
	 */
	public void setVerkehrspracheFamilie(final String verkehrspracheFamilie) {
		this.verkehrspracheFamilie = verkehrspracheFamilie;
	}

	/**
	 * Der Vorname des Schülers.
	 * @return Inhalt des Feldes vorname
	 */
	public String vorname() {
		return vorname;
	}

	/**
	 * Der Vorname des Schülers wird gesetzt.
	 * @param vorname Neuer Wert für das Feld vorname
	 */
	public void setVorname(final String vorname) {
		this.vorname = vorname;
	}

	/**
	 * Alle Vornamen, sofern es mehrere gibt, des Schülers.
	 * @return Inhalt des Feldes vornamen
	 */
	public String vornamen() {
		return vornamen;
	}

	/**
	 * Alle Vornamen, sofern es mehrere gibt, des Schülers wird gesetzt.
	 * @param vornamen Neuer Wert für das Feld vornamen
	 */
	public void setVornamen(final String vornamen) {
		this.vornamen = vornamen;
	}

	/**
	 * Der Wohnort des Schülers.
	 * @return Inhalt des Feldes wohnort
	 */
	public OrtKatalogEintrag wohnort() {
		return wohnort;
	}

	/**
	 * Der Wohnort des Schülers wird gesetzt.
	 * @param wohnort Neuer Wert für das Feld wohnort
	 */
	public void setWohnort(final OrtKatalogEintrag wohnort) {
		this.wohnort = wohnort;
	}

	/**
	 * Der Name des Wohnorts des Schülers.
	 * @return Inhalt des Feldes wohnortname
	 */
	public String wohnortname() {
		return wohnortname;
	}

	/**
	 * Der Name des Wohnorts des Schülers wird gesetzt.
	 * @param wohnortname Neuer Wert für das Feld wohnortname
	 */
	public void setWohnortname(final String wohnortname) {
		this.wohnortname = wohnortname;
	}

	/**
	 * Der Ortsteil des Wohnorts des Schülers.
	 * @return Inhalt des Feldes wohnortsteil
	 */
	public OrtsteilKatalogEintrag wohnortsteil() {
		return wohnortsteil;
	}

	/**
	 * Der Ortsteil des Wohnorts des Schülers wird gesetzt.
	 * @param wohnortsteil Neuer Wert für das Feld wohnortsteil
	 */
	public void setWohnortsteil(final OrtsteilKatalogEintrag wohnortsteil) {
		this.wohnortsteil = wohnortsteil;
	}

	/**
	 * Der Name des Ortsteils des Wohnorts des Schülers.
	 * @return Inhalt des Feldes wohnortsteilname
	 */
	public String wohnortsteilname() {
		return wohnortsteilname;
	}

	/**
	 * Der Name des Ortsteils des Wohnorts des Schülers wird gesetzt.
	 * @param wohnortsteilname Neuer Wert für das Feld wohnortsteilname
	 */
	public void setWohnortsteilname(final String wohnortsteilname) {
		this.wohnortsteilname = wohnortsteilname;
	}

	/**
	 * Das Zuzugsjahr des Schülers.
	 * @return Inhalt des Feldes zuzugsjahr
	 */
	public Integer zuzugsjahr() {
		return zuzugsjahr;
	}

	/**
	 * Das Zuzugsjahr des Schülers wird gesetzt.
	 * @param zuzugsjahr Neuer Wert für das Feld zuzugsjahr
	 */
	public void setZuzugsjahr(final Integer zuzugsjahr) {
		this.zuzugsjahr = zuzugsjahr;
	}
}
