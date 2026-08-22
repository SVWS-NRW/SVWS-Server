package de.svws_nrw.module.reporting.types.schueler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungQrDaten;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
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
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingImageUtils;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schüler.</p>
 */
public class ReportingSchueler extends ReportingPerson {

	/** Die Sortierkonfiguration für {@link ReportingSchueler}. */
	public static final ReportingSortierung<ReportingSchueler> SORTIERUNG = ReportingSchuelerSortierung.SORTIERUNG;

	/** Die Filterkonfiguration für {@link ReportingSchueler}. */
	public static final ReportingFilterung<ReportingSchueler> FILTER = ReportingSchuelerFilter.FILTER;

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

	/** Die Schulnummer bei einem externen Schüler oder ein leerer String, wenn der Schüler kein externer Schüler ist. */
	protected String externeSchulNr;

	/** Das Kürzel der externen Schule bei einem externen Schüler, sofern dieses im Schulkatalog hinterlegt ist. Andernfalls ein leerer String, wenn der
	 * Schüler kein externer Schüler ist bzw. kein Kürzel hinterlegt ist. */
	protected String externesSchulKuerzel;

	/** Die ID der Art des Fahr des Schülers. */
	protected Long fahrschuelerArtID;

	/** Das Foto (in Base64 kodiert) des Schülers. */
	protected String foto;

	/** Eine HTML-kompatible Bildquelle (Data-URI) für das Foto des Schülers. Der MIME-Type wird, falls nicht explizit bekannt, aus den Base64-Daten ermittelt. */
	protected String fotoHtmlSource;

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

	/** Die gerenderten QR-Codes der signierten Schulbescheinigung; werden lazy über das Repository befüllt. */
	protected SchulbescheinigungQrDaten schulbescheinigungQrDaten;

	/** Daten aller Sprachbelegungen. */
	protected List<ReportingSchuelerSprachbelegung> sprachbelegungen;

	/** Der Status des Schülers. */
	protected SchuelerStatus status;

	/** Eine Liste der telefonischen Kontakte zum Schüler. */
	protected List<ReportingSchuelerTelefonkontakt> telefonKontakte;

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
	 * @param druckeKonfessionAufZeugnisse Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 * @param emailPrivat Die private Email-Adresse des Schülers.
	 * @param emailSchule Die schulische E-Mail-Adresse des Schülers.
	 * @param erhaeltMeisterBAFOEG Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 * @param erhaeltSchuelerBAFOEG Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 * @param erzieher Die Liste der Erzieher des Schülers.
	 * @param erzieherArtGruppen Die Liste der Erzieher gruppiert nach Erzieher-Art in Listen von Erziehern.
	 * @param externeSchulNr Die Schulnummer bei einem externen Schüler oder ein leerer String, wenn der Schüler kein externer Schüler ist.
	 * @param externesSchulKuerzel Das Kürzel der externen Schule bei einem externen Schüler, sofern dieses im Schulkatalog hinterlegt ist. Andernfalls ein leerer String, wenn der Schüler kein externer Schüler ist bzw. kein Kürzel hinterlegt ist.
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
	 * @param telefonKontakte Eine Liste der telefonischen Kontakte zum Schüler.
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
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingSchueler(final ReportingSchuelerLernabschnitt aktuellerLernabschnitt, final String anmeldedatum,
			final String anrede, final String aufnahmedatum, final ReportingSchuelerLernabschnitt auswahlLernabschnitt,
			final boolean druckeKonfessionAufZeugnisse, final String emailPrivat, final String emailSchule, final boolean erhaeltMeisterBAFOEG,
			final boolean erhaeltSchuelerBAFOEG, final List<ReportingErzieher> erzieher, final List<ReportingErzieherArtGruppe> erzieherArtGruppen,
			final String externeSchulNr, final String externesSchulKuerzel, final Long fahrschuelerArtID, final String foto, final String geburtsdatum,
			final String geburtsland,
			final String geburtslandMutter, final String geburtslandVater, final String geburtsname, final String geburtsort, final Geschlecht geschlecht,
			final ReportingSchuelerGostAbitur gostAbitur, final List<ReportingGostKlausurplanungSchuelerklausur> gostKlausurplanungSchuelerklausuren,
			final List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen,
			final ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung, final Long haltestelleID, final boolean hatMasernimpfnachweis,
			final boolean hatMigrationshintergrund, final String hausnummer, final String hausnummerZusatz, final long id,
			final Boolean istBerufsschulpflichtErfuellt, final boolean istDuplikat, final Boolean istSchulpflichtErfuellt, final Boolean istVolljaehrig,
			final boolean keineAuskunftAnDritte, final List<ReportingSchuelerLernabschnitt> lernabschnitte, final String nachname,
			final String religionabmeldung, final String religionanmeldung, final ReligionEintrag religion, final ReportingSchuelerSchulbesuch schulbesuch,
			final List<ReportingSchuelerSprachbelegung> sprachbelegungen, final Nationalitaeten staatsangehoerigkeit,
			final Nationalitaeten staatsangehoerigkeit2, final SchuelerStatus status, final String strassenname,
			final List<ReportingSchuelerTelefonkontakt> telefonKontakte, final String telefonPrivat, final String telefonPrivatMobil, final String titel,
			final String verkehrspracheFamilie, final String vorname, final String vornamen, final OrtKatalogEintrag wohnort,
			final OrtsteilKatalogEintrag wohnortsteil, final Integer zuzugsjahr) {
		super(anrede, emailPrivat, emailSchule, "", geburtsdatum, geburtsland, geburtsname, geburtsort, geschlecht, hausnummer, hausnummerZusatz, nachname,
				staatsangehoerigkeit, staatsangehoerigkeit2, strassenname, telefonPrivat, telefonPrivatMobil, "", "", titel, vorname, vornamen, wohnort,
				wohnortsteil);
		this.aktuellerLernabschnitt = aktuellerLernabschnitt;
		this.anmeldedatum = ersetzeNullBlankTrim(anmeldedatum);
		this.aufnahmedatum = ersetzeNullBlankTrim(aufnahmedatum);
		this.auswahlLernabschnitt = auswahlLernabschnitt;
		this.druckeKonfessionAufZeugnisse = druckeKonfessionAufZeugnisse;
		this.erhaeltMeisterBAFOEG = erhaeltMeisterBAFOEG;
		this.erhaeltSchuelerBAFOEG = erhaeltSchuelerBAFOEG;
		this.erzieher = (erzieher != null) ? new ArrayList<>(erzieher.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.erzieherArtGruppen =
				(erzieherArtGruppen != null) ? new ArrayList<>(erzieherArtGruppen.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.externeSchulNr = ersetzeNullBlankTrim(externeSchulNr);
		this.externesSchulKuerzel = ersetzeNullBlankTrim(externesSchulKuerzel);
		this.fahrschuelerArtID = fahrschuelerArtID;
		this.foto = ersetzeNullBlankTrim(foto);
		this.fotoHtmlSource = ReportingImageUtils.base64ImageToHtmlImageSource(this.foto, null, null);
		this.geburtslandMutter = ersetzeNullBlankTrim(geburtslandMutter);
		this.geburtslandVater = ersetzeNullBlankTrim(geburtslandVater);
		this.gostAbitur = gostAbitur;
		this.gostKlausurplanungSchuelerklausuren =
				(gostKlausurplanungSchuelerklausuren != null)
						? new ArrayList<>(gostKlausurplanungSchuelerklausuren.stream().filter(Objects::nonNull).toList())
						: new ArrayList<>();
		this.gostKursplanungKursbelegungen =
				(gostKursplanungKursbelegungen != null)
						? new ArrayList<>(gostKursplanungKursbelegungen.stream().filter(Objects::nonNull).toList())
						: new ArrayList<>();
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
		this.religionabmeldung = ersetzeNullBlankTrim(religionabmeldung);
		this.religionanmeldung = ersetzeNullBlankTrim(religionanmeldung);
		this.religion = religion;
		this.schulbesuch = schulbesuch;
		this.sprachbelegungen = (sprachbelegungen != null) ? new ArrayList<>(sprachbelegungen.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.status = status;
		this.telefonKontakte = (telefonKontakte != null) ? new ArrayList<>(telefonKontakte.stream().filter(Objects::nonNull).toList()) : new ArrayList<>();
		this.verkehrspracheFamilie = ersetzeNullBlankTrim(verkehrspracheFamilie);
		this.zuzugsjahr = zuzugsjahr;

		if (lernabschnitte != null) {
			setLernabschnitte(lernabschnitte);
		}
	}

	/**
	 * Erzeugt das geschlechtsspezifische Wort für "Schüler/Schülerin".
	 *
	 * @return Geschlechtsspezifisches Wort für "Schüler/Schülerin"
	 */
	public String schuelerIn() {
		switch (this.geschlecht) {
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
	@Override
	public int hashCode() {
		return 31 + Long.hashCode(this.id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof final ReportingSchueler other)) {
			return false;
		}
		return (this.id == other.id);
	}


	// ##### Getter #####

	/**
	 * Zum gegebenen Schuljahresabschnitt wird der darin aktive Lernabschnitt (WechselNr. 0) ermittelt.
	 * <p>Die Vorlagen rufen diese Methode auch mit {@code null} auf - etwa mit dem Abitur-Schuljahresabschnitt eines Jahrgangs, dessen Abschnitt die Schule
	 * nicht angelegt hat. Ohne den Guard bräche die Druckausgabe dort mit einer NullPointerException ab.</p>
	 *
	 * @param schuljahresabschnitt  Der Schuljahresabschnitt, dessen Lernabschnitt ermittelt werden soll, oder {@code null}.
	 *
	 * @return Der Lernabschnitt zum SchuljahresAbschnitt oder null, wenn kein Abschnitt übergeben wurde oder kein solcher Lernabschnitt existiert.
	 */
	public ReportingSchuelerLernabschnitt aktiverLernabschnittInSchuljahresabschnitt(final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		if ((schuljahresabschnitt == null) || (lernabschnitte() == null) || lernabschnitte().isEmpty()) {
			return null;
		}
		return this.mapLernabschnitte.getSingle12OrNull(schuljahresabschnitt.id(), 0);
	}

	/**
	 * Daten des aktuellen Lernabschnitts.
	 *
	 * @return Inhalt des Feldes aktuellerLernabschnitt
	 */
	public ReportingSchuelerLernabschnitt aktuellerLernabschnitt() {
		return this.aktuellerLernabschnitt;
	}

	/**
	 * Das Anmeldedatum des Schülers.
	 *
	 * @return Inhalt des Feldes anmeldedatum; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String anmeldedatum() {
		return this.anmeldedatum;
	}

	/**
	 * Das Aufnahmedatum des Schülers.
	 *
	 * @return Inhalt des Feldes aufnahmedatum; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String aufnahmedatum() {
		return this.aufnahmedatum;
	}

	/**
	 * Daten des ausgewählten Lernabschnitts.
	 *
	 * @return Inhalt des Feldes auswahlLernabschnitt
	 */
	public ReportingSchuelerLernabschnitt auswahlLernabschnitt() {
		return this.auswahlLernabschnitt;
	}

	/**
	 * Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 *
	 * @return Inhalt des Feldes druckeKonfessionAufZeugnisse
	 */
	public boolean druckeKonfessionAufZeugnisse() {
		return this.druckeKonfessionAufZeugnisse;
	}

	/**
	 * Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 *
	 * @return Inhalt des Feldes erhaeltMeisterBAFOEG
	 */
	public boolean erhaeltMeisterBAFOEG() {
		return this.erhaeltMeisterBAFOEG;
	}

	/**
	 * Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 *
	 * @return Inhalt des Feldes erhaeltSchuelerBAFOEG
	 */
	public boolean erhaeltSchuelerBAFOEG() {
		return this.erhaeltSchuelerBAFOEG;
	}

	/**
	 * Die Liste der Erzieher des Schülers.
	 *
	 * @return Inhalt des Feldes erzieher; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingErzieher> erzieher() {
		return this.erzieher;
	}

	/**
	 * Die Liste der Erzieher gruppiert nach Erzieher-Art in Listen von Erziehern.
	 *
	 * @return Inhalt des Feldes erzieherArtGruppen; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingErzieherArtGruppe> erzieherArtGruppen() {
		return this.erzieherArtGruppen;
	}

	/**
	 * Die Liste der Erzieher des Schülers ohne einen evtl. volljährigen Schüler selbst.
	 *
	 * @return Inhalt des Feldes erzieher ohne evtl. volljähren Schüler.
	 */
	public List<ReportingErzieher> erzieherOhneVolljaehrigenSchueler() {
		return this.erzieher.stream().filter(e -> !e.istVolljaehrigerSchueler()).toList();
	}

	/**
	 * Die Liste der Erzieher gruppiert nach Erzieher-Art in Listen von Erziehern ohne einen evtl. volljährigen Schüler selbst.
	 *
	 * @return Inhalt des Feldes erzieherArtGruppen ohne evtl. volljähren Schüler.
	 */
	public List<ReportingErzieherArtGruppe> erzieherArtGruppenOhneVolljaehrigenSchueler() {
		return erzieherArtGruppen().stream().filter(e -> !e.istVolljaehrigerSchueler()).toList();
	}

	/**
	 * Die Schulnummer bei einem externen Schüler oder ein leerer String, wenn der Schüler kein externer Schüler ist.
	 *
	 * @return Inhalt des Feldes externeSchulNr; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String externeSchulNr() {
		return this.externeSchulNr;
	}

	/**
	 * Das Kürzel der externen Schule bei einem externen Schüler, sofern dieses im Schulkatalog hinterlegt ist.
	 *
	 * @return Inhalt des Feldes externesSchulKuerzel; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String externesSchulKuerzel() {
		return this.externesSchulKuerzel;
	}

	/**
	 * Die ID der Fahrschülerart des Schülers.
	 *
	 * @return Inhalt des Feldes fahrschuelerArtID
	 */
	public Long fahrschuelerArtID() {
		return this.fahrschuelerArtID;
	}

	/**
	 * Das Foto (in Base64 kodiert) des Schülers.
	 *
	 * @return Inhalt des Feldes foto; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String foto() {
		return this.foto;
	}

	/**
	 * Liefert eine HTML-kompatible Bildquelle (Data-URI) für das Foto des Schülers.
	 * Der MIME-Type wird, falls nicht explizit bekannt, aus den Base64-Daten ermittelt.
	 *
	 * @return Data-URI für das Foto oder ein leerer String, wenn kein Foto vorhanden ist.
	 */
	public String fotoHtmlSource() {
		return this.fotoHtmlSource;
	}

	/**
	 * Das Geburtsland der Mutter des Schülers.
	 *
	 * @return Inhalt des Feldes geburtslandMutter; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String geburtslandMutter() {
		return this.geburtslandMutter;
	}

	/**
	 * Das Geburtsland des Vaters des Schülers.
	 *
	 * @return Inhalt des Feldes geburtslandVater; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String geburtslandVater() {
		return this.geburtslandVater;
	}

	/**
	 * Die Abiturdaten der GOSt.
	 *
	 * @return Inhalt des Feldes gostAbitur; kann {@code null} sein, wenn zum Schüler keine Abiturdaten vorliegen.
	 */
	public ReportingSchuelerGostAbitur gostAbitur() {
		return this.gostAbitur;
	}

	/**
	 * Daten der GOSt-Laufbahnplanung.
	 *
	 * @return Inhalt des Feldes gostLaufbahnplanung
	 */
	public ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung() {
		return this.gostLaufbahnplanung;
	}

	/**
	 * Die Klausuren des Schülers in einer GOSt-Klausurplanung. Sie werden beim Initialisieren eines Klausurplans initialisiert.
	 *
	 * @return Inhalt des Feldes gostKlausurplanungSchuelerklausuren; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> gostKlausurplanungSchuelerklausuren() {
		return this.gostKlausurplanungSchuelerklausuren;
	}

	/**
	 * Gibt die Kursbelegung des Schülers aus der Kursplanung der gymnasialen Oberstufe zurück, die zur angegebenen ID des Kurses gehört, oder null.
	 * @param idKurs	Die ID des Kurses, dessen Kursbelegung gesucht ist.
	 *
	 * @return			Die Kursbelegung des Kurses.
	 */
	@JsonIgnore
	public ReportingSchuelerGostKursplanungKursbelegung getGostKursplanungKursbelegungById(final long idKurs) {
		return gostKursplanungKursbelegungen().stream().filter(b -> (b.kurs() != null) && (b.kurs().id() == idKurs)).findAny().orElse(null);
	}

	/**
	 * Die Kursbelegungen des Schülers in einer GOSt-Kursplanung. Sie werden beim Initialisieren eines Blockungsergebnisses initialisiert.
	 *
	 * @return Inhalt des Feldes gostKursplanungKursbelegungen; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingSchuelerGostKursplanungKursbelegung> gostKursplanungKursbelegungen() {
		return this.gostKursplanungKursbelegungen;
	}

	/**
	 * Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülers.
	 *
	 * @return Inhalt des Feldes haltestelleID
	 */
	public Long haltestelleID() {
		return this.haltestelleID;
	}

	/**
	 * Gibt an, ob der Schüler einen Nachweis über die Masernimpfpflicht erbracht hat.
	 *
	 * @return Inhalt des Feldes hatMasernimpfnachweis
	 */
	public boolean hatMasernimpfnachweis() {
		return this.hatMasernimpfnachweis;
	}

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist.
	 *
	 * @return Inhalt des Feldes hatMigrationshintergrund
	 */
	public boolean hatMigrationshintergrund() {
		return this.hatMigrationshintergrund;
	}

	/**
	 * Die ID des Schülers.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return this.id;
	}

	/**
	 * Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht.
	 *
	 * @return Inhalt des Feldes istBerufsschulpflichtErfuellt
	 */
	public Boolean istBerufsschulpflichtErfuellt() {
		return this.istBerufsschulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht.
	 *
	 * @return Inhalt des Feldes istDuplikat
	 */
	public boolean istDuplikat() {
		return this.istDuplikat;
	}

	/**
	 * Überprüft, ob ein Schüler in der Primarstufe ist, basierend auf einem gegebenen Schuljahresabschnitt.
	 *
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, für den überprüft werden soll, ob der Schüler in der Primarstufe ist.
	 *
	 * @return true, wenn der Schüler in der Primarstufe ist, sonst false.
	 */
	public boolean istSchuelerInPrimarstufe(final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		final ReportingSchuelerLernabschnitt lernabschnitt = aktiverLernabschnittInSchuljahresabschnitt(schuljahresabschnitt);
		return (lernabschnitt != null) && (lernabschnitt.jahrgang() != null) && lernabschnitt.jahrgang().istJahrgangImBereichPrimarstufe();
	}

	/**
	 * Prüft, ob ein Schüler im angegebenen Schuljahresabschnitt zur Sekundarstufe I gehört.
	 *
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, für den überprüft werden soll,
	 *                             ob der Schüler in der Sekundarstufe I ist.
	 *
	 * @return true, wenn der Schüler im angegebenen Schuljahresabschnitt zur Sekundarstufe I gehört,
	 *         ansonsten false.
	 */
	public boolean istSchuelerInSek1(final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		final ReportingSchuelerLernabschnitt lernabschnitt = aktiverLernabschnittInSchuljahresabschnitt(schuljahresabschnitt);
		return (lernabschnitt != null) && (lernabschnitt.jahrgang() != null) && lernabschnitt.jahrgang().istJahrgangImBereichSek1();
	}

	/**
	 * Prüft, ob ein Schüler im angegebenen Schuljahresabschnitt in der Sekundarstufe II
	 * oder in einer Weiterbildung ist.
	 *
	 * @param schuljahresabschnitt Der Schuljahresabschnitt, in dem geprüft werden soll.
	 *
	 * @return true, wenn der Schüler in der Sekundarstufe II oder in einer Weiterbildung ist,
	 *         ansonsten false.
	 */
	public boolean istSchuelerInSek2OderWB(final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		final ReportingSchuelerLernabschnitt lernabschnitt = aktiverLernabschnittInSchuljahresabschnitt(schuljahresabschnitt);
		return (lernabschnitt != null) && (lernabschnitt.jahrgang() != null)
				&& lernabschnitt.jahrgang().istJahrgangImBereichSek2OderWeiterbildung();
	}

	/**
	 * Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht.
	 *
	 * @return Inhalt des Feldes istSchulpflichtErfuellt
	 */
	public Boolean istSchulpflichtErfuellt() {
		return this.istSchulpflichtErfuellt;
	}

	/**
	 * Gibt an, ob der Schüler volljährig ist oder nicht.
	 *
	 * @return Inhalt des Feldes istVolljaehrig
	 */
	public Boolean istVolljaehrig() {
		return this.istVolljaehrig;
	}

	/**
	 * Gibt an, ob über den Schüler eine Auskunft an Dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.
	 *
	 * @return Inhalt des Feldes keineAuskunftAnDritte
	 */
	public boolean keineAuskunftAnDritte() {
		return this.keineAuskunftAnDritte;
	}

	/**
	 * Daten des Lernabschnitts zur übergebenen ID des Lernabschnitts.
	 *
	 * @param id Die ID des Lernabschnitts.
	 *
	 * @return Der Lernabschnitt zur ID.
	 */
	public ReportingSchuelerLernabschnitt lernabschnittById(final long id) {
		if ((lernabschnitte() == null) || lernabschnitte().isEmpty()) {
			return null;
		}
		return this.mapLernabschnitte.getSingle3OrNull(id);
	}

	/**
	 * Daten aller Lernabschnitte.
	 *
	 * @return Inhalt des Feldes lernabschnitte
	 */
	public List<ReportingSchuelerLernabschnitt> lernabschnitte() {
		return this.lernabschnitte;
	}

	/**
	 * Daten der Lernabschnitte zur übergebenen ID des Schuljahresabschnitts.
	 *
	 * @param id Die ID des Schuljahresabschnitts, dessen Lernabschnitte gesucht werden sollen.
	 *
	 * @return Die Lernabschnitte zur ID.
	 */
	public List<ReportingSchuelerLernabschnitt> lernabschnittBySchuljahresabschnittsId(final long id) {
		if ((lernabschnitte() == null) || lernabschnitte().isEmpty()) {
			return new ArrayList<>();
		}
		return this.mapLernabschnitte.get1(id);
	}

	/**
	 * Das Datum der Religionsabmeldung des Schülers.
	 *
	 * @return Inhalt des Feldes religionabmeldung; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String religionabmeldung() {
		return this.religionabmeldung;
	}

	/**
	 * Das Datum der Religionsanmeldung des Schülers.
	 *
	 * @return Inhalt des Feldes religionanmeldung; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String religionanmeldung() {
		return this.religionanmeldung;
	}

	/**
	 * Die Religion des Schülers.
	 *
	 * @return Inhalt des Feldes religion
	 */
	public ReligionEintrag religion() {
		return this.religion;
	}

	/**
	 * Daten zum bisherigen und zukünftigen Schulbesuch.
	 *
	 * @return Inhalt des Feldes schulbesuch
	 */
	public ReportingSchuelerSchulbesuch schulbesuch() {
		return this.schulbesuch;
	}

	/**
	 * Die gerenderten QR-Codes der signierten Schulbescheinigung des Schülers.
	 *
	 * @return Inhalt des Feldes schulbescheinigungQrDaten
	 */
	public SchulbescheinigungQrDaten schulbescheinigungQrDaten() {
		return schulbescheinigungQrDaten;
	}


	/**
	 * Daten aller Sprachbelegungen.
	 *
	 * @return Inhalt des Feldes sprachbelegungen; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingSchuelerSprachbelegung> sprachbelegungen() {
		return this.sprachbelegungen;
	}

	/**
	 * Gibt an, ob das kleine Latinum erreicht wurde.
	 *
	 * @return Wahr, wenn das kleine Latinum erreicht wurde, sonst false.
	 */
	public boolean hatKleinesLatinum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : this.sprachbelegungen) {
			if (sprachbelegung.hatKleinesLatinum()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gibt an, ob das Latinum erreicht wurde.
	 *
	 * @return Wahr, wenn das Latinum erreicht wurde, sonst false.
	 */
	public boolean hatLatinum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : this.sprachbelegungen) {
			if (sprachbelegung.hatLatinum()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gibt an, ob das Graecum erreicht wurde.
	 *
	 * @return Wahr, wenn das Graecum erreicht wurde, sonst false.
	 */
	public boolean hatGraecum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : this.sprachbelegungen) {
			if (sprachbelegung.hatGraecum()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gibt an, ob das Hebraicum erreicht wurde.
	 *
	 * @return Wahr, wenn das Hebraicum erreicht wurde, sonst false.
	 */
	public boolean hatHebraicum() {
		for (final ReportingSchuelerSprachbelegung sprachbelegung : this.sprachbelegungen) {
			if (sprachbelegung.hatHebraicum()) {
				return true;
			}
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
		for (final ReportingSchuelerSprachbelegung sprachbelegung : this.sprachbelegungen) {
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
		return this.status;
	}

	/**
	 * Eine Liste der telefonischen Kontakte zum Schüler.
	 *
	 * @return Inhalt des Feldes telefonKontakte; nie {@code null}, bei fehlender Zuordnung eine leere Liste.
	 */
	public List<ReportingSchuelerTelefonkontakt> telefonKontakte() {
		return this.telefonKontakte;
	}

	/**
	 * Die Verkehrssprache der Familie des Schülers.
	 *
	 * @return Inhalt des Feldes verkehrspracheFamilie; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String verkehrspracheFamilie() {
		return this.verkehrspracheFamilie;
	}

	/**
	 * Das Zuzugsjahr des Schülers.
	 *
	 * @return Inhalt des Feldes zuzugsjahr
	 */
	public Integer zuzugsjahr() {
		return this.zuzugsjahr;
	}


	// ##### Setter #####

	/**
	 * Setzt die Liste der Lernabschnitte und aktualisiert die zugehörigen internen Datenstrukturen.
	 *
	 * @param lernabschnitte Eine Liste von Lernabschnitten vom Typ ReportingSchuelerLernabschnitt, die gesetzt werden sollen.
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
		if (lernabschnitte == null) {
			return;
		}

		final List<ReportingSchuelerLernabschnitt> lernabschnitteNonNull = new ArrayList<>(lernabschnitte.stream().filter(Objects::nonNull).toList());
		this.lernabschnitte.addAll(lernabschnitteNonNull);

		lernabschnitteNonNull.stream().filter(la -> la.schuljahresabschnitt() != null)
				.forEach(la -> this.mapLernabschnitte.add(la.schuljahresabschnitt().id(), la.wechselNr(), la.id(), la));
	}

}
