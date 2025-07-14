package de.svws_nrw.module.reporting.types.schueler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.person.ReportingPerson;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArtGruppe;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ReportingSchuelerGostKursplanungKursbelegung;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ReportingSchuelerSchulbesuch;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ReportingSchuelerSprachbelegung;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schüler.</p>
 */
public class ReportingSchueler extends ReportingPerson {

	/** Daten aller Lernabschnitte. */
	private List<ReportingSchuelerLernabschnitt> lernabschnitte;

	/** Eine Map zum schnellen Zugriff auf die Lernabschnitte nach Schuljahresabschnitt, Wechselnummer und LernabschnittID. */
	private ListMap3DLongKeys<ReportingSchuelerLernabschnitt> mapLernabschnitte = new ListMap3DLongKeys<>();


	/** Daten des aktuellen Lernabschnitts. */
	protected ReportingSchuelerLernabschnitt aktuellerLernabschnitt;

	/** Das Anmeldedatum des Schülers. */
	protected String anmeldedatum;

	/** Das Aufnahmedatum des Schülers. */
	protected String aufnahmedatum;

	/** Daten des ausgewählten Lernabschnitts. */
	protected ReportingSchuelerLernabschnitt auswahlLernabschnitt;

	/** Status-Flags zu bereits aus der Datenbank geladenen Daten. */
	protected EnumSet<ReportingSchuelerDatenstatus> datenstatus;

	/** Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll. */
	protected boolean druckeKonfessionAufZeugnisse;

	/** Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht. */
	protected boolean erhaeltMeisterBAFOEG;

	/** Gibt an, ob der Schüler BAFÖG erhält oder nicht. */
	protected boolean erhaeltSchuelerBAFOEG;

	/** Die Liste der Erzieher des Schülers. */
	protected List<ReportingErzieher> erzieher;

	/** Die Liste der Erzieher gruppiert nach Erzieher-Art in Listen von Erziehern. */
	protected List<ReportingErzieherArtGruppe> erzieherArtGruppen;

	/** Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist. */
	protected String externeSchulNr;

	/** Die ID der Art des Fahr des Schülers. */
	protected Long fahrschuelerArtID;

	/** Das Foto (in Base64 kodiert) des Schülers. */
	protected String foto;

	/** Das Geburtsland der Mutter des Schülers. */
	protected String geburtslandMutter;

	/** Das Geburtsland des Vaters des Schülers. */
	protected String geburtslandVater;

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

	/** Das Datum der Religionsabmeldung des Schülers. */
	protected String religionabmeldung;

	/** Das Datum der Religionsanmeldung des Schülers. */
	protected String religionanmeldung;

	/** Die Religion des Schülers. */
	protected ReligionEintrag religion;

	/** Daten zum bisherigen und zukünftigen Schulbesuch. */
	protected ReportingSchuelerSchulbesuch schulbesuch;

	/** Daten aller Sprachbelegungen. */
	protected List<ReportingSchuelerSprachbelegung> sprachbelegungen;

	/** Der Status des Schülers. */
	protected SchuelerStatus status;

	/** Die Verkehrssprache der Familie des Schülers. */
	protected String verkehrspracheFamilie;

	/** Das Zuzugsjahr des Schülers. */
	protected Integer zuzugsjahr;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param aktuellerLernabschnitt Daten des aktuellen Lernabschnitts.
	 * @param anmeldedatum Das Anmeldedatum des Schülers.
	 * @param anrede Die Anrede des Schülers.
	 * @param aufnahmedatum Das Aufnahmedatum des Schülers.
	 * @param auswahlLernabschnitt Daten des ausgewählten Lernabschnitts.
	 * @param datenstatus Status-Flags zu bereits aus der Datenbank geladenen Date
	 * @param druckeKonfessionAufZeugnisse Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 * @param emailPrivat Die private Email-Adresse des Schülers.
	 * @param emailSchule Die schulische E-Mail-Adresse des Schülers.
	 * @param erhaeltMeisterBAFOEG Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 * @param erhaeltSchuelerBAFOEG Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 * @param erzieher Die Liste der Erzieher des Schülers.
	 * @param erzieherArtGruppen Die Liste der Erzieher gruppiert nach Erzieher-Art in Listen von Erziehern.
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
	 * @param schulbesuch Daten zum bisherigen und zukünftigen Schulbesuch.
	 * @param sprachbelegungen Daten aller Sprachbelegungen.
	 * @param staatsangehoerigkeit Die erste Staatsangehörigkeit des Schülers.
	 * @param staatsangehoerigkeit2 Die zweite Staatsangehörigkeit des Schülers.
	 * @param status Der Status des Schülers.
	 * @param strassenname Ggf. der Straßenname im Wohnort des Schülers.
	 * @param telefonPrivat Die private Telefonnummer des Schülers.
	 * @param telefonPrivatMobil Die private Mobilfunk-Telefonnummer des Schülers.
	 * @param titel Der Titel des Schülers.
	 * @param verkehrspracheFamilie Die Verkehrssprache der Familie des Schülers.
	 * @param vorname Der Vorname des Schülers.
	 * @param vornamen Alle Vornamen, sofern es mehrere gibt, des Schülers.
	 * @param wohnort Der Wohnort des Schülers.
	 * @param wohnortsteil Der Ortsteil des Wohnorts des Schülers.
	 * @param zuzugsjahr Das Zuzugsjahr des Schülers.
	 */
	public ReportingSchueler(final ReportingSchuelerLernabschnitt aktuellerLernabschnitt, final String anmeldedatum,
			final String anrede, final String aufnahmedatum, final ReportingSchuelerLernabschnitt auswahlLernabschnitt,
			final EnumSet<ReportingSchuelerDatenstatus> datenstatus, final boolean druckeKonfessionAufZeugnisse,
			final String emailPrivat, final String emailSchule, final boolean erhaeltMeisterBAFOEG, final boolean erhaeltSchuelerBAFOEG,
			final List<ReportingErzieher> erzieher,
			final List<ReportingErzieherArtGruppe> erzieherArtGruppen, final String externeSchulNr, final Long fahrschuelerArtID, final String foto,
			final String geburtsdatum, final String geburtsland, final String geburtslandMutter, final String geburtslandVater, final String geburtsname,
			final String geburtsort, final Geschlecht geschlecht, final ReportingSchuelerGostAbitur gostAbitur,
			final List<ReportingGostKlausurplanungSchuelerklausur> gostKlausurplanungSchuelerklausuren,
			final List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen,
			final ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung, final Long haltestelleID, final boolean hatMasernimpfnachweis,
			final boolean hatMigrationshintergrund, final String hausnummer, final String hausnummerZusatz, final long id,
			final Boolean istBerufsschulpflichtErfuellt, final boolean istDuplikat, final Boolean istSchulpflichtErfuellt, final Boolean istVolljaehrig,
			final boolean keineAuskunftAnDritte, final List<ReportingSchuelerLernabschnitt> lernabschnitte, final String nachname,
			final String religionabmeldung, final String religionanmeldung, final ReligionEintrag religion, final ReportingSchuelerSchulbesuch schulbesuch,
			final List<ReportingSchuelerSprachbelegung> sprachbelegungen, final Nationalitaeten staatsangehoerigkeit,
			final Nationalitaeten staatsangehoerigkeit2, final SchuelerStatus status, final String strassenname, final String telefonPrivat,
			final String telefonPrivatMobil, final String titel, final String verkehrspracheFamilie, final String vorname, final String vornamen,
			final OrtKatalogEintrag wohnort,
			final OrtsteilKatalogEintrag wohnortsteil, final Integer zuzugsjahr) {
		super(anrede, emailPrivat, emailSchule, "", geburtsdatum, geburtsland, geburtsname, geburtsort, geschlecht, hausnummer, hausnummerZusatz, nachname,
				staatsangehoerigkeit, staatsangehoerigkeit2, strassenname, telefonPrivat, telefonPrivatMobil, "", "", titel, vorname, vornamen, wohnort,
				wohnortsteil);
		this.aktuellerLernabschnitt = aktuellerLernabschnitt;
		this.anmeldedatum = anmeldedatum;
		this.aufnahmedatum = aufnahmedatum;
		this.auswahlLernabschnitt = auswahlLernabschnitt;
		this.datenstatus = datenstatus;
		this.druckeKonfessionAufZeugnisse = druckeKonfessionAufZeugnisse;
		this.erhaeltMeisterBAFOEG = erhaeltMeisterBAFOEG;
		this.erhaeltSchuelerBAFOEG = erhaeltSchuelerBAFOEG;
		this.erzieher = erzieher;
		this.erzieherArtGruppen = erzieherArtGruppen;
		this.externeSchulNr = externeSchulNr;
		this.fahrschuelerArtID = fahrschuelerArtID;
		this.foto = foto;
		this.geburtslandMutter = geburtslandMutter;
		this.geburtslandVater = geburtslandVater;
		this.gostAbitur = gostAbitur;
		this.gostKlausurplanungSchuelerklausuren = gostKlausurplanungSchuelerklausuren;
		this.gostKursplanungKursbelegungen = gostKursplanungKursbelegungen;
		this.gostLaufbahnplanung = gostLaufbahnplanung;
		this.haltestelleID = haltestelleID;
		this.hatMasernimpfnachweis = hatMasernimpfnachweis;
		this.hatMigrationshintergrund = hatMigrationshintergrund;
		this.id = id;
		this.istBerufsschulpflichtErfuellt = istBerufsschulpflichtErfuellt;
		this.istDuplikat = istDuplikat;
		this.istSchulpflichtErfuellt = istSchulpflichtErfuellt;
		this.istVolljaehrig = istVolljaehrig;
		this.keineAuskunftAnDritte = keineAuskunftAnDritte;
		this.religionabmeldung = religionabmeldung;
		this.religionanmeldung = religionanmeldung;
		this.religion = religion;
		this.schulbesuch = schulbesuch;
		this.sprachbelegungen = sprachbelegungen;
		this.status = status;
		this.verkehrspracheFamilie = verkehrspracheFamilie;
		this.zuzugsjahr = zuzugsjahr;

		if (lernabschnitte != null)
			this.setLernabschnitte(lernabschnitte);
	}

	/**
	 * Erzeugt das geschlechtsspezifische Wort für "Schüler/Schülerin".
	 *
	 * @return Geschlechtsspezifisches Wort für "Schüler/Schülerin"
	 */
	public String schuelerIn() {
		switch (geschlecht) {
			case Geschlecht.W -> {
				return "Schülerin";
			}
			case Geschlecht.M -> {
				return "Schüler";
			}
			case null, default -> {
				return "Kind";
			}
		}
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingSchueler other))
			return false;
		return (id == other.id);
	}


	// ##### Getter #####

	/**
	 * Zum gegebenen Schuljahresabschnitt wird der darin aktive Lernabschnitt (WechselNr. 0) ermittelt
	 *
	 * @param schuljahresabschnitt  Der Schuljahresabschnitt, dessen Lernabschnitt ermittelt werden soll.
	 *
	 * @return Der Lernabschnitt zum SchuljahresAbschnitt oder null, wenn kein solcher Abschnitt existiert.
	 */
	public ReportingSchuelerLernabschnitt aktiverLernabschnittInSchuljahresabschnitt(final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		if ((this.lernabschnitte() == null) || this.lernabschnitte().isEmpty())
			return null;
		return this.mapLernabschnitte().getSingle12OrNull(schuljahresabschnitt.id(), 0);
	}

	/**
	 * Daten des aktuellen Lernabschnitts.
	 *
	 * @return Inhalt des Feldes aktuellerLernabschnitt
	 */
	public ReportingSchuelerLernabschnitt aktuellerLernabschnitt() {
		return aktuellerLernabschnitt;
	}

	/**
	 * Das Anmeldedatum des Schülers.
	 *
	 * @return Inhalt des Feldes anmeldedatum
	 */
	public String anmeldedatum() {
		return anmeldedatum;
	}

	/**
	 * Das Aufnahmedatum des Schülers.
	 *
	 * @return Inhalt des Feldes aufnahmedatum
	 */
	public String aufnahmedatum() {
		return aufnahmedatum;
	}

	/**
	 * Daten des ausgewählten Lernabschnitts.
	 *
	 * @return Inhalt des Feldes auswahlLernabschnitt
	 */
	public ReportingSchuelerLernabschnitt auswahlLernabschnitt() {
		return auswahlLernabschnitt;
	}

	/**
	 * Status-Flags zu bereits aus der Datenbank geladenen Daten.
	 *
	 * @return Inhalt des Feldes datenstatus
	 */
	public Set<ReportingSchuelerDatenstatus> datenstatus() {
		return datenstatus;
	}

	/**
	 * Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 *
	 * @return Inhalt des Feldes druckeKonfessionAufZeugnisse
	 */
	public boolean druckeKonfessionAufZeugnisse() {
		return druckeKonfessionAufZeugnisse;
	}

	/**
	 * Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 *
	 * @return Inhalt des Feldes erhaeltMeisterBAFOEG
	 */
	public boolean erhaeltMeisterBAFOEG() {
		return erhaeltMeisterBAFOEG;
	}

	/**
	 * Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 *
	 * @return Inhalt des Feldes erhaeltSchuelerBAFOEG
	 */
	public boolean erhaeltSchuelerBAFOEG() {
		return erhaeltSchuelerBAFOEG;
	}

	/**
	 * Die Liste der Erzieher des Schülers.
	 *
	 * @return Inhalt des Feldes erzieher
	 */
	public List<ReportingErzieher> erzieher() {
		return erzieher;
	}

	/**
	 * Die Liste der Erzieher gruppiert nach Erzieher-Art in Listen von Erziehern.
	 *
	 * @return Inhalt des Feldes erzieherArtGruppen
	 */
	public List<ReportingErzieherArtGruppe> erzieherArtGruppen() {
		return erzieherArtGruppen;
	}

	/**
	 * Die Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist.
	 *
	 * @return Inhalt des Feldes externeSchulNr
	 */
	public String externeSchulNr() {
		return externeSchulNr;
	}

	/**
	 * Die ID der Art des Fahr des Schülers.
	 *
	 * @return Inhalt des Feldes fahrschuelerArtID
	 */
	public Long fahrschuelerArtID() {
		return fahrschuelerArtID;
	}

	/**
	 * Das Foto (in Base64 kodiert) des Schülers.
	 *
	 * @return Inhalt des Feldes foto
	 */
	public String foto() {
		return foto;
	}

	/**
	 * Das Geburtsland der Mutter des Schülers.
	 *
	 * @return Inhalt des Feldes geburtslandMutter
	 */
	public String geburtslandMutter() {
		return geburtslandMutter;
	}

	/**
	 * Das Geburtsland des Vaters des Schülers.
	 *
	 * @return Inhalt des Feldes geburtslandVater
	 */
	public String geburtslandVater() {
		return geburtslandVater;
	}

	/**
	 * Die Abiturdaten der GOSt.
	 *
	 * @return Inhalt des Feldes gostAbitur
	 */
	public ReportingSchuelerGostAbitur gostAbitur() {
		return gostAbitur;
	}

	/**
	 * Daten der GOSt-Laufbahnplanung.
	 *
	 * @return Inhalt des Feldes gostLaufbahnplanung
	 */
	public ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung() {
		return gostLaufbahnplanung;
	}

	/**
	 * Die Klausuren des Schülers in einer GOSt-Klausurplanung. Sie werden beim Initialisieren eines Klausurplans initialisiert.
	 *
	 * @return Inhalt des Feldes gostKlausurplanungSchuelerklausuren
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> gostKlausurplanungSchuelerklausuren() {
		return gostKlausurplanungSchuelerklausuren;
	}

	/**
	 * Gibt die Kursbelegung des Schülers aus der Kursplanung der gymnasialen Oberstufe zurück, die zur angegebenen ID des Kurses gehört oder null.
	 * @param idKurs	Die ID des Kurses, dessen Kursbelegung gesucht ist.
	 *
	 * @return			Die Kursbelegung des Kurses.
	 */
	@JsonIgnore
	public ReportingSchuelerGostKursplanungKursbelegung getGostKursplanungKursbelegungById(final long idKurs) {
		return this.gostKursplanungKursbelegungen().stream().filter(b -> b.kurs().id() == idKurs).findAny().orElse(null);
	}

	/**
	 * Die Kursbelegungen des Schülers in einer GOSt-Kursplanung. Sie werden beim Initialisieren eines Blockungsergebnisses initialisiert.
	 *
	 * @return Inhalt des Feldes gostKursplanungKursbelegungen
	 */
	public List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen() {
		return gostKursplanungKursbelegungen;
	}

	/**
	 * Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers.
	 *
	 * @return Inhalt des Feldes haltestelleID
	 */
	public Long haltestelleID() {
		return haltestelleID;
	}

	/**
	 * Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat.
	 *
	 * @return Inhalt des Feldes hatMasernimpfnachweis
	 */
	public boolean hatMasernimpfnachweis() {
		return hatMasernimpfnachweis;
	}

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist.
	 *
	 * @return Inhalt des Feldes hatMigrationshintergrund
	 */
	public boolean hatMigrationshintergrund() {
		return hatMigrationshintergrund;
	}

	/**
	 * Die ID des Schülers.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht.
	 *
	 * @return Inhalt des Feldes istBerufsschulpflichtErfuellt
	 */
	public Boolean istBerufsschulpflichtErfuellt() {
		return istBerufsschulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht.
	 *
	 * @return Inhalt des Feldes istDuplikat
	 */
	public boolean istDuplikat() {
		return istDuplikat;
	}

	/**
	 * Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht.
	 *
	 * @return Inhalt des Feldes istSchulpflichtErfuellt
	 */
	public Boolean istSchulpflichtErfuellt() {
		return istSchulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob der Schüler volljährig ist oder nicht.
	 *
	 * @return Inhalt des Feldes istVolljaehrig
	 */
	public Boolean istVolljaehrig() {
		return istVolljaehrig;
	}

	/**
	 * Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.
	 *
	 * @return Inhalt des Feldes keineAuskunftAnDritte
	 */
	public boolean keineAuskunftAnDritte() {
		return keineAuskunftAnDritte;
	}

	/**
	 * Daten aller Lernabschnitte.
	 *
	 * @return Inhalt des Feldes lernabschnitte
	 */
	public List<ReportingSchuelerLernabschnitt> lernabschnitte() {
		return lernabschnitte;
	}

	/**
	 * Gibt eine Map mit den Lernabschnitten des Schülers nach Schuljahresabschnitt, WechselNr und LernabschnittsID zurück
	 *
	 * @return Map der Lernabschnitte oder eine leere Liste.
	 */
	public ListMap3DLongKeys<ReportingSchuelerLernabschnitt> mapLernabschnitte() {
		return mapLernabschnitte;
	}

	/**
	 * Das Datum der Religionsabmeldung des Schülers.
	 *
	 * @return Inhalt des Feldes religionabmeldung
	 */
	public String religionabmeldung() {
		return religionabmeldung;
	}

	/**
	 * Das Datum der Religionsanmeldung des Schülers.
	 *
	 * @return Inhalt des Feldes religionanmeldung
	 */
	public String religionanmeldung() {
		return religionanmeldung;
	}

	/**
	 * Die Religion des Schülers.
	 *
	 * @return Inhalt des Feldes religion
	 */
	public ReligionEintrag religion() {
		return religion;
	}

	/**
	 * Daten zum bisherigen und zukünftigen Schulbesuch.
	 *
	 * @return Inhalt des Feldes schulbesuch
	 */
	public ReportingSchuelerSchulbesuch schulbesuch() {
		return schulbesuch;
	}


	/**
	 * Daten aller Sprachbelegungen.
	 *
	 * @return Inhalt des Feldes sprachbelegungen
	 */
	public List<ReportingSchuelerSprachbelegung> sprachbelegungen() {
		return sprachbelegungen;
	}

	/**
	 * Gibt an, ob das kleine Latinum erreicht wurde.
	 *
	 * @return Wahr, wenn das kleine Latinum erreicht wurde, sonst false.
	 */
	public boolean hatKleinesLatinum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : sprachbelegungen) {
			if (sprachbelegung.hatKleinesLatinum())
				return true;
		}
		return false;
	}

	/**
	 * Gibt an, ob das Latinum erreicht wurde.
	 *
	 * @return Wahr, wenn das Latinum erreicht wurde, sonst false.
	 */
	public boolean hatLatinum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : sprachbelegungen) {
			if (sprachbelegung.hatLatinum())
				return true;
		}
		return false;
	}

	/**
	 * Gibt an, ob das Graecum erreicht wurde.
	 *
	 * @return Wahr, wenn das Graecum erreicht wurde, sonst false.
	 */
	public boolean hatGraecum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : sprachbelegungen) {
			if (sprachbelegung.hatGraecum())
				return true;
		}
		return false;
	}

	/**
	 * Gibt an, ob das Hebraicum erreicht wurde.
	 *
	 * @return Wahr, wenn das Hebraicum erreicht wurde, sonst false.
	 */
	public boolean hatHebraicum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : sprachbelegungen) {
			if (sprachbelegung.hatHebraicum())
				return true;
		}
		return false;
	}

	/**
	 * Die erreichten Sprachqualifikationen in den antiken Sprachen (Latinum, Graecum und Hebraicum).
	 * Das kleine Latinum wird hier nicht berücksichtigt.
	 *
	 * @return Ein Text mit den erreichten Qualifikationen, andernfalls ein leerer String.
	 */
	public String alteSprachenQualifikationen() {
		int hatErreicht = 0;
		for (final ReportingSchuelerSprachbelegung sprachbelegung : sprachbelegungen) {
			hatErreicht = hatErreicht + (sprachbelegung.hatLatinum() ? 1 : 0) + (sprachbelegung.hatGraecum() ? 2 : 0) + (sprachbelegung.hatHebraicum() ? 4 : 0);
		}
		return switch (hatErreicht) {
			case 1 -> "Latinum";
			case 2 -> "Graecum";
			case 3 -> "Latinum und Graecum";
			case 4 -> "Hebraicum";
			case 5 -> "Latinum und Hebraicum";
			case 6 -> "Graecum und Hebraicum";
			case 7 -> "Latinum, Graecum und Hebraicum";
			default -> "";
		};
	}

	/**
	 * Der Status des Schülers.
	 *
	 * @return Inhalt des Feldes status
	 */
	public SchuelerStatus status() {
		return status;
	}

	/**
	 * Die Verkehrssprache der Familie des Schülers.
	 *
	 * @return Inhalt des Feldes verkehrspracheFamilie
	 */
	public String verkehrspracheFamilie() {
		return verkehrspracheFamilie;
	}

	/**
	 * Das Zuzugsjahr des Schülers.
	 *
	 * @return Inhalt des Feldes zuzugsjahr
	 */
	public Integer zuzugsjahr() {
		return zuzugsjahr;
	}


	// ##### Setter #####

	/**
	 * Setzt die Liste der Lernabschnitte und aktualisiert die zugehörigen internen Datenstrukturen.
	 *
	 * @param lernabschnitte Eine Liste von Lernabschnitten vom Typ ReportingSchuelerLernabschnitt,
	 *                       die gesetzt werden soll.
	 */
	public void setLernabschnitte(final List<ReportingSchuelerLernabschnitt> lernabschnitte) {
		this.lernabschnitte = new ArrayList<>();
		this.mapLernabschnitte = new ListMap3DLongKeys<>();
		addLernabschnitte(lernabschnitte);
	}

	/**
	 * Fügt eine Liste von Lernabschnitten zur bestehenden Sammlung hinzu. Nullwerte in der Eingabeliste
	 * werden ignoriert. Die gültigen Lernabschnitte werden außerdem einer Mapping-Struktur hinzugefügt.
	 *
	 * @param lernabschnitte eine Liste von {@link ReportingSchuelerLernabschnitt}, die hinzugefügt werden sollen.
	 *                        Nullwerte innerhalb der Liste werden ignoriert.
	 */
	public void addLernabschnitte(final List<ReportingSchuelerLernabschnitt> lernabschnitte) {
		if (lernabschnitte == null)
			return;

		final List<ReportingSchuelerLernabschnitt> lernabschnitteNonNull = new ArrayList<>(lernabschnitte.stream().filter(Objects::nonNull).toList());
		this.lernabschnitte.addAll(lernabschnitteNonNull);

		lernabschnitteNonNull.forEach(la -> mapLernabschnitte.add(la.schuljahresabschnitt().id(), la.wechselNr(), la.id(), la));
	}

}
