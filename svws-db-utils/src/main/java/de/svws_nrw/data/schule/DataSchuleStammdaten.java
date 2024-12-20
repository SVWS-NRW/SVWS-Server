package de.svws_nrw.data.schule;

import de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag;
import de.svws_nrw.asd.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.data.kataloge.KatalogEintragOrte;
import de.svws_nrw.core.data.schule.SchulenKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.types.schule.Herkunftsschulnummern;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AdressenUtils;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOErzieherart;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAdressart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKursarten;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEinschulungsart;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSportbefreiung;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchuleLogo;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulformen;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuleStammdaten}.
 */
public final class DataSchuleStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuleStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuleStammdaten(final DBEntityManager conn) {
		super(conn);
	}


	private static SchuleStammdaten map(final DTOEigeneSchule schule) {
		final SchuleStammdaten daten = new SchuleStammdaten();
		daten.schulNr = schule.SchulNr;
		daten.schulform = schule.SchulformKuerzel;
		daten.bezeichnung1 = schule.Bezeichnung1;
		daten.bezeichnung2 = schule.Bezeichnung2;
		daten.bezeichnung3 = schule.Bezeichnung3;
		daten.strassenname = schule.Strassenname;
		daten.hausnummer = schule.HausNr;
		daten.hausnummerZusatz = schule.HausNrZusatz;
		daten.plz = schule.PLZ;
		daten.ort = schule.Ort;
		daten.telefon = schule.Telefon;
		daten.fax = schule.Fax;
		daten.email = schule.Email;
		daten.webAdresse = schule.WebAdresse;
		daten.idSchuljahresabschnitt = schule.Schuljahresabschnitts_ID;
		daten.anzJGS_Jahr = (schule.AnzJGS_Jahr == null) ? 1 : schule.AnzJGS_Jahr;
		daten.schuleAbschnitte.anzahlAbschnitte = schule.AnzahlAbschnitte;
		daten.schuleAbschnitte.abschnittBez = schule.AbschnittBez;
		daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt1);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 2)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt2);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 3)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt3);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 4)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt4);
		daten.dauerUnterrichtseinheit = (schule.DauerUnterrichtseinheit == null) ? 45 : schule.DauerUnterrichtseinheit;
		return daten;
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die Schulstammdaten zu der angegebenen ID aus der DB.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die Stammdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static SchuleStammdaten getStammdaten(final DBEntityManager conn) throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schuldaten für die Schule vorhanden.");
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Schule hat keinen gültigen aktuellen Schuljahresabschnitt.");
		final SchuleStammdaten daten = map(schule);
		daten.abschnitte.addAll((new DataSchuljahresabschnitte(conn)).getAbschnitte());
		return daten;
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final SchuleStammdaten daten = getStammdaten(conn);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Bestimmt die Schulnummer der Schule
	 *
	 * @return die Schulnummer oder null im Fehlerfall
	 */
	public Integer getSchulnummer() {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			return null;
		return schule.SchulNr;
	}


	/**
	 * Bestimmt die Schulnummer der Schule
	 *
	 * @return Die HTTP-Response (NOT_FOUND im Fehlerfall)
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response getSchulnummerResponse() throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(schule.SchulNr).build();
	}


	/**
	 * Gibt die Anzahl der Abschnitte pro Schuljahr
	 *
	 * @param conn   die Datenbankverbindung, welche bei der Abfrage genutzt wird
	 *
	 * @return die Anzahl der Abschnitte pro Schuljahr
	 */
	public static int getAnzahlAbschnitte(final DBEntityManager conn) {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if ((schule == null) || (schule.AnzahlAbschnitte == null))
			return 2;   // Default-Wert
		return schule.AnzahlAbschnitte;
	}


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (schule == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "schulNr" -> throw new ApiOperationException(Status.BAD_REQUEST);
					case "schulform" -> throw new ApiOperationException(Status.BAD_REQUEST);
					case "bezeichnung1" ->
						schule.Bezeichnung1 = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Bezeichnung1.datenlaenge());
					case "bezeichnung2" ->
						schule.Bezeichnung2 = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Bezeichnung2.datenlaenge());
					case "bezeichnung3" ->
						schule.Bezeichnung3 = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Bezeichnung3.datenlaenge());
					case "strassenname" ->
						schule.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Strassenname.datenlaenge());
					case "hausnummer" -> schule.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_HausNr.datenlaenge());
					case "hausnummerZusatz" ->
						schule.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_HausNrZusatz.datenlaenge());

					case "plz" -> schule.PLZ = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_PLZ.datenlaenge()); // TODO Schema anpassen: Stakue-Ortskatalog nutzen -> Orts-ID
					case "ort" -> schule.Ort = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Ort.datenlaenge()); // TODO Schema anpassen: Stakue-Ortskatalog nutzen -> Orts-ID

					case "telefon" -> schule.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Telefon.datenlaenge());
					case "fax" -> schule.Fax = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Fax.datenlaenge());
					case "email" -> schule.Email = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_Email.datenlaenge());
					case "webAdresse" ->
						schule.WebAdresse = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule.col_WebAdresse.datenlaenge());

					case "idSchuljahresabschnitt" -> schule.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false); // TODO ID des Schuljahresabschnittes überprüfen
					case "textSchuljahr" -> throw new ApiOperationException(Status.BAD_REQUEST);
					case "textSchuljahresabschnitt" -> throw new ApiOperationException(Status.BAD_REQUEST);
					case "anzJGS_Jahr" -> schule.AnzJGS_Jahr = JSONMapper.convertToInteger(value, false); // TODO Abschnitt überprüfen

					case "schuleAbschnitte" -> {
						@SuppressWarnings("unchecked") // TODO check conversion
						final Map<String, Object> mapAbschnitte = (Map<String, Object>) value;
						if (mapAbschnitte.containsKey("anzahlAbschnitte")) {
							final Integer anzahlAbschnitte = JSONMapper.convertToInteger(mapAbschnitte.get("anzahlAbschnitte"), false);
							if ((anzahlAbschnitte < 1) || (anzahlAbschnitte > 4))
								throw new ApiOperationException(Status.CONFLICT);
							schule.AnzahlAbschnitte = anzahlAbschnitte;
						}
						if (mapAbschnitte.containsKey("abschnittBez"))
							schule.AbschnittBez = JSONMapper.convertToString(mapAbschnitte.get("abschnittBez"), true, true,
									Schema.tab_EigeneSchule.col_BezAbschnitt1.datenlaenge());
						if (mapAbschnitte.containsKey("bezAbschnitte")) {
							final List<?> bezAbschnitte = (List<?>) mapAbschnitte.get("bezAbschnitte");
							if (bezAbschnitte.size() != schule.AnzahlAbschnitte)
								throw new ApiOperationException(Status.CONFLICT);
							for (int i = 0; i < bezAbschnitte.size(); i++) {
								final Object objBezeichnung = bezAbschnitte.get(i);
								if (!(objBezeichnung instanceof String))
									throw new ApiOperationException(Status.BAD_REQUEST);
								switch (i) {
									case 0 -> schule.BezAbschnitt1 = (String) objBezeichnung;
									case 1 -> schule.BezAbschnitt2 = (String) objBezeichnung;
									case 2 -> schule.BezAbschnitt3 = (String) objBezeichnung;
									case 3 -> schule.BezAbschnitt4 = (String) objBezeichnung;
									default -> throw new ApiOperationException(Status.BAD_REQUEST);
								}
							}
						}
					}

					case "dauerUnterrichtseinheit" -> schule.DauerUnterrichtseinheit = JSONMapper.convertToInteger(value, false); // TODO Dauer in Minuten prüfen, evtl. einschränken
					case "abschnitte" -> throw new ApiOperationException(Status.BAD_REQUEST);

					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(schule);
		}
		return Response.status(Status.OK).build();
	}


	/**
	 * Ermittelt das Schullogo.
	 *
	 * @return Die HTTP-Response der Get-Operation
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response getSchullogo() throws ApiOperationException {
		final DTOEigeneSchuleLogo logo = conn.querySingle(DTOEigeneSchuleLogo.class);
		if (logo == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final String daten = "\"" + logo.LogoBase64 + "\"";
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Ermittelt das Schullogo im Base64-Format.
	 *
	 * @return Schullogo im Base64-Format
	 */
	public String getSchullogoBase64() {
		final DTOEigeneSchuleLogo logo = conn.querySingle(DTOEigeneSchuleLogo.class);
		if (logo == null)
			return "";
		return logo.LogoBase64;
	}

	/**
	 * Ersetzt das Schullogo.
	 *
	 * @param is            der {@link InputStream} mit dem JSON-Patch für das Logo
	 *
	 * @return Die HTTP-Response der Patch-Operation
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response putSchullogo(final InputStream is) throws ApiOperationException {
		final DTOEigeneSchuleLogo logo = conn.querySingle(DTOEigeneSchuleLogo.class);
		if (logo == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		logo.LogoBase64 = JSONMapper.toString(is);
		conn.transactionPersist(logo);
		return Response.ok().build();
	}


	/**
	 * Initialisiert das Schema mit der angebenen Schulnummer
	 *
	 * @param schulnummer   die Schulnummer der anzulegenden Schule
	 *
	 * @return die HTTP-Response mit den Stammdaten der Schule
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response init(final int schulnummer) throws ApiOperationException {
		// Prüfe, ob bereits ein Eintrag in der Tabelle EigeneSchule vorliegt...
		DTOEigeneSchule eigeneSchule = conn.querySingle(DTOEigeneSchule.class);
		if (eigeneSchule != null)
			throw new ApiOperationException(Status.CONFLICT, "Das Datenbank-Schema kann nicht mit einer Schule initialisiert werden, da es bereits einen"
					+ " Schuleintrag enthält.");
		// Prüfe, ob die Schulnummer im Katalog der Schulen vorkommt.
		final List<SchulenKatalogEintrag> katalogSchulen = DataKatalogSchulen.getKatalog();
		SchulenKatalogEintrag schulEintrag = null;
		for (final SchulenKatalogEintrag eintrag : katalogSchulen) {
			if (eintrag.SchulNr.equals("" + schulnummer)) {
				schulEintrag = eintrag;
				break;
			}
		}
		if (schulEintrag == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schule mit der Schulnummer " + schulnummer + " im Katalog der Schulen gefunden.");
		// Bestimme das aktuelle Datum
		final LocalDate date = LocalDate.now();
		final int month = date.getMonthValue();
		final int year = date.getYear();
		// Lege den ersten Schuljahresabschnitt an
		final int schuljahr = (month > 7) ? year : (year - 1);
		final int abschnitt = ((month > 2) && (month < 8)) ? 2 : 1;
		final DTOSchuljahresabschnitte schuljahresabschnitt = new DTOSchuljahresabschnitte(1L, schuljahr, abschnitt);
		conn.transactionPersist(schuljahresabschnitt);
		conn.transactionFlush();
		// Initialisiere die Daten in der Tabelle EigeneSchule
		eigeneSchule = new DTOEigeneSchule(1L);
		final Schulform schulform = switch (schulEintrag.SF) {
			case "81" -> Schulform.S;
			default -> Schulform.data().getWertBySchluessel(schulEintrag.SF);
		};
		if (schulform == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schulform mit der Nummer " + schulEintrag.SF + " bei den Schulformen gefunden.");
		final SchulformKatalogEintrag ske = schulform.daten(schuljahr);
		if (ske == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulform mit der Nummer %s ist im Schuljahr %d nicht gültig."
					.formatted(schulEintrag.SF, schuljahr));
		eigeneSchule.SchulformKuerzel = ske.kuerzel;
		eigeneSchule.SchulformNr = ske.schluessel;
		eigeneSchule.SchulformBez = ske.text;
		eigeneSchule.SchultraegerArt = schulEintrag.ArtDerTraegerschaft;
		eigeneSchule.SchultraegerNr = schulEintrag.SchultraegerNr;
		eigeneSchule.SchulNr = Integer.parseInt(schulEintrag.SchulNr);
		eigeneSchule.Bezeichnung1 = schulEintrag.ABez1;
		eigeneSchule.Bezeichnung2 = schulEintrag.ABez2;
		eigeneSchule.Bezeichnung3 = schulEintrag.ABez3;
		final String[] strasse = AdressenUtils.splitStrasse(schulEintrag.Strasse);
		eigeneSchule.Strassenname = strasse[0];
		eigeneSchule.HausNr = strasse[1];
		eigeneSchule.HausNrZusatz = strasse[2];
		eigeneSchule.PLZ = schulEintrag.PLZ;
		eigeneSchule.Ort = schulEintrag.Ort;
		eigeneSchule.Telefon = schulEintrag.TelVorw + " " + schulEintrag.Telefon;
		eigeneSchule.Fax = schulEintrag.FaxVorw + " " + schulEintrag.Fax;
		eigeneSchule.Email = schulEintrag.Email;
		eigeneSchule.Ganztags = Integer.parseInt(schulEintrag.Ganztagsbetrieb) > 0;
		eigeneSchule.Schuljahresabschnitts_ID = schuljahresabschnitt.ID;
		eigeneSchule.AnzahlAbschnitte = 2;
		eigeneSchule.Fremdsprachen = null;
		eigeneSchule.JVAZeigen = null;
		eigeneSchule.RefPaedagogikZeigen = null;
		eigeneSchule.AnzJGS_Jahr = null;
		eigeneSchule.AbschnittBez = "Halbjahr";
		eigeneSchule.BezAbschnitt1 = "1. Halbjahr";
		eigeneSchule.BezAbschnitt2 = "2. Halbjahr";
		eigeneSchule.BezAbschnitt3 = null;
		eigeneSchule.BezAbschnitt4 = null;
		eigeneSchule.IstHauptsitz = true;
		conn.transactionPersist(eigeneSchule);
		conn.transactionFlush();

		// Der Hauptstandort einrichten
		final DTOTeilstandorte teilstandort = new DTOTeilstandorte("A");
		teilstandort.PLZ = schulEintrag.PLZ;
		teilstandort.Ort = schulEintrag.Ort;
		teilstandort.Strassenname = strasse[0];
		teilstandort.HausNr = strasse[1];
		teilstandort.HausNrZusatz = strasse[2];
		teilstandort.Bemerkung = "Hauptstandort";
		teilstandort.Kuerzel = "";
		conn.transactionPersist(teilstandort);
		conn.transactionFlush();

		// Einrichten der Schulgliederung in EigeneSchule_Schulformen - je nach Schulform
		final DTOSchulformen schulgliederung = new DTOSchulformen(1L);
		final Schulgliederung sgl = switch (schulform) {
			case BK, SB -> Schulgliederung.A01;
			case SG, SR, V, PS, S, KS, SK, R, H, GE, G, FW, HI, WF -> Schulgliederung.DEFAULT;
			case GM -> Schulgliederung.GRH;
			case GY -> Schulgliederung.GY9;
			case WB -> Schulgliederung.G02;
		};
		schulgliederung.SGL = sgl.daten(schuljahr).kuerzel;
		schulgliederung.SF_SGL = schulform.daten(schuljahr).kuerzel + "" + sgl.daten(schuljahr).kuerzel;
		schulgliederung.Schulform = sgl.daten(schuljahr).text;
		schulgliederung.Sortierung = 1;
		schulgliederung.BKIndex = sgl.daten(schuljahr).bkIndex;
		schulgliederung.Schulform2 = sgl.daten(schuljahr).kuerzel + ": " + sgl.daten(schuljahr).text;
		conn.transactionPersist(schulgliederung);
		conn.transactionFlush();

		// Grundlegende Fächer - je nach Schulform - einrichten
		final List<Fach> faecher = switch (schulform) {
			case BK, SB -> Arrays.asList(Fach.D, Fach.E, Fach.M);
			case G -> Arrays.asList(Fach.KR, Fach.ER, Fach.D, Fach.D, Fach.D,
					Fach.E, Fach.M, Fach.SU, Fach.SP, Fach.MU, Fach.KU);
			case GE, GY -> Arrays.asList(Fach.D, Fach.E, Fach.F, Fach.L, Fach.S,
					Fach.KU, Fach.MU, Fach.LI, Fach.EK, Fach.GE,
					Fach.SW, Fach.PL, Fach.PA, Fach.ER, Fach.KR,
					Fach.M, Fach.BI, Fach.CH, Fach.PH, Fach.IF, Fach.SP);
			default -> Arrays.asList(Fach.D, Fach.E, Fach.M);
		};
		final ArrayList<DTOFach> dtoFaecher = new ArrayList<>();
		int iDeutsch = 1;
		for (int i = 0; i < faecher.size(); i++) {
			final Fach fach = faecher.get(i);
			final DTOFach dto = new DTOFach(i + 1L, true);
			String kuerzel = fach.daten(schuljahr).kuerzel;
			String bezeichnung = fach.daten(schuljahr).text;
			if ((schulform == Schulform.G) && (fach == Fach.D)) {
				if (iDeutsch == 1) {
					kuerzel = "SG";
					bezeichnung = "Sprachgebrauch";
				} else if (iDeutsch == 2) {
					kuerzel = "LE";
					bezeichnung = "Lesen";
				} else if (iDeutsch == 3) {
					kuerzel = "RS";
					bezeichnung = "Rechtschreibung";
				}
				iDeutsch++;
			}
			dto.Kuerzel = kuerzel;
			dto.Bezeichnung = bezeichnung;
			dto.BezeichnungZeugnis = bezeichnung;
			dto.BezeichnungUeberweisungsZeugnis = bezeichnung;
			final Fachgruppe gruppe = fach.getFachgruppe(schuljahr);
			final FachgruppeKatalogEintrag fgke = (gruppe == null) ? null : gruppe.daten(schuljahr);
			dto.Zeugnisdatenquelle_ID = ((fgke == null) || (fgke.schluessel == null)) ? null : Long.valueOf(fgke.schluessel);
			dto.StatistikKuerzel = fach.daten(schuljahr).schluessel;
			dto.IstFremdsprache = fach.daten(schuljahr).istFremdsprache;
			dto.SortierungAllg = i * 100;
			dto.SortierungSekII = i * 100;
			dto.IstNachpruefungErlaubt = (fach == Fach.D) || (fach == Fach.M) || (fach == Fach.E);
			dto.Sichtbar = true;
			dto.Aenderbar = true;
			dto.Gewichtung = 1;
			dto.Unterrichtssprache = "D";
			dto.IstSchriftlichZK = (fach == Fach.D) || (fach == Fach.M) || (fach == Fach.E);
			dto.IstSchriftlichBA = false;
			dto.AufZeugnis = true;
			dto.Lernfelder = null;
			// nur für GymOb
			final boolean istOberstufenfach = schulform.daten(schuljahr).hatGymOb;
			dto.IstOberstufenFach = istOberstufenfach;
			dto.IstMoeglichAbiLK = istOberstufenfach;
			dto.IstMoeglichAbiGK = istOberstufenfach;
			dto.IstMoeglichEF1 = istOberstufenfach;
			dto.IstMoeglichEF2 = istOberstufenfach;
			dto.IstMoeglichQ11 = istOberstufenfach;
			dto.IstMoeglichQ12 = istOberstufenfach;
			dto.IstMoeglichQ21 = istOberstufenfach;
			dto.IstMoeglichQ22 = istOberstufenfach;
			dto.IstMoeglichAlsNeueFremdspracheInSekII = false;
			dto.ProjektKursLeitfach1_ID = null;
			dto.ProjektKursLeitfach2_ID = null;
			// nur für WBK
			dto.WochenstundenEF1 = (schulform == Schulform.WB) ? 3 : null;
			dto.WochenstundenEF2 = (schulform == Schulform.WB) ? 3 : null;
			dto.WochenstundenQualifikationsphase = (schulform == Schulform.WB) ? 3 : null;
			dto.MussSchriftlichEF1 = (schulform == Schulform.WB);
			dto.MussSchriftlichEF2 = (schulform == Schulform.WB);
			dto.MussMuendlich = false;
			// ...
			dto.Aufgabenfeld = (fach.daten(schuljahr).aufgabenfeld == null) ? null : ("" + fach.daten(schuljahr).aufgabenfeld);
			dto.AbgeschlFaecherHolen = true;
			dto.GewichtungFHR = 1;
			dto.MaxBemZeichen = null;
			dtoFaecher.add(dto);
		}
		conn.transactionPersistAll(dtoFaecher);
		conn.transactionFlush();

		// Kursarten - je nach Schulform - einrichten
		final ArrayList<DTOKursarten> dtoKursarten = new ArrayList<>();
		final List<ZulaessigeKursart> kursarten = ZulaessigeKursart.getListBySchuljahrAndSchulform(schuljahr, schulform);
		for (int i = 0; i < kursarten.size(); i++) {
			final ZulaessigeKursart kursart = kursarten.get(i);
			final DTOKursarten dto = new DTOKursarten((long) i + 1);
			dto.Bezeichnung = kursart.daten(schuljahr).text;
			dto.InternBez = kursart.daten(schuljahr).kuerzel;
			dto.Kursart = kursart.daten(schuljahr).kuerzel;
			dto.KursartAllg = kursart.daten(schuljahr).kuerzelAllg;
			dto.Sortierung = i * 100;
			dto.Sichtbar = true;
			dto.Aenderbar = true;
			dtoKursarten.add(dto);
		}
		conn.transactionPersistAll(dtoKursarten);
		conn.transactionFlush();

		// Einrichten der Jahrgänge - je nach Schulform
		final ArrayList<DTOJahrgang> dtoJahrgaenge = new ArrayList<>();
		final List<Jahrgaenge> jahrgaenge = Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr, schulform);
		for (int i = 0; i < jahrgaenge.size(); i++) {
			final Jahrgaenge jg = jahrgaenge.get(i);
			final DTOJahrgang dto = new DTOJahrgang((long) i + 1);
			dto.InternKrz = jg.daten(schuljahr).kuerzel;
			dto.GueltigVon = null;
			dto.GueltigBis = null;
			dto.ASDJahrgang = jg.daten(schuljahr).kuerzel;
			dto.ASDBezeichnung = jg.daten(schuljahr).text;
			dto.Sichtbar = true;
			dto.Sortierung = i + 1;
			dto.IstChronologisch = true;
			dto.Kurzbezeichnung = jg.daten(schuljahr).kuerzel;
			dto.GliederungKuerzel = null;
			dto.Sekundarstufe = null;
			dto.AnzahlRestabschnitte = null;
			dto.Folgejahrgang_ID = null;
			dtoJahrgaenge.add(dto);
		}
		conn.transactionPersistAll(dtoJahrgaenge);
		conn.transactionFlush();

		// K_Adressart mit Betrieb füllen
		final DTOKatalogAdressart addressart = new DTOKatalogAdressart(1L, "Betrieb");
		addressart.Sortierung = 1;
		conn.transactionPersist(addressart);
		conn.transactionFlush();

		// K_Beschaeftigungsart mit Ausbildung und Praktikum füllen
		final ArrayList<DTOBeschaeftigungsart> beschaeftigungsart = new ArrayList<>();
		beschaeftigungsart.add(new DTOBeschaeftigungsart(1L, "Ausbildung"));
		beschaeftigungsart.add(new DTOBeschaeftigungsart(2L, "Praktikum"));
		for (int i = 0; i < beschaeftigungsart.size(); i++)
			beschaeftigungsart.get(i).Sortierung = i + 1;
		conn.transactionPersistAll(beschaeftigungsart);
		conn.transactionFlush();

		// K_Datenschutz mit Verwendung Foto
		final DTOKatalogEinwilligungsart foto = new DTOKatalogEinwilligungsart(1L, "Verwendung Foto", true, 32000);
		foto.Schluessel = "FOTO";
		foto.personTyp = PersonTyp.SCHUELER;
		conn.transactionPersist(foto);
		conn.transactionFlush();


		// K_EinschulgungsArt normal, vorzeitig und zurückgestellt
		final ArrayList<DTOEinschulungsart> einschulungsart = new ArrayList<>();
		einschulungsart.add(new DTOEinschulungsart(1L, "normal"));
		einschulungsart.add(new DTOEinschulungsart(2L, "vorzeitig"));
		einschulungsart.add(new DTOEinschulungsart(3L, "zurückgestellt"));
		for (int i = 0; i < einschulungsart.size(); i++)
			einschulungsart.get(i).Sortierung = i + 1;
		conn.transactionPersistAll(einschulungsart);
		conn.transactionFlush();

		// K_Entlassgrund mit "Schulpflicht endet", "Normaler Abschluss", "Ohne Angabe" und "Wechsel zu anderer Schule"
		final ArrayList<DTOEntlassarten> entlassart = new ArrayList<>();
		entlassart.add(new DTOEntlassarten(1L, "Schulpflicht endet"));
		entlassart.add(new DTOEntlassarten(2L, "Normaler Abschluss"));
		entlassart.add(new DTOEntlassarten(3L, "Ohne Angabe"));
		entlassart.add(new DTOEntlassarten(4L, "Wechsel zu anderer Schule"));
		for (int i = 0; i < entlassart.size(); i++)
			entlassart.get(i).Sortierung = i + 1;
		conn.transactionPersistAll(entlassart);
		conn.transactionFlush();

		// K_Erzieherart mit den Vorgaben von Schild-NRW befüllen
		final ArrayList<DTOErzieherart> erzieherarten = new ArrayList<>();
		erzieherarten.add(new DTOErzieherart(1L, "Vater"));
		erzieherarten.add(new DTOErzieherart(2L, "Mutter"));
		erzieherarten.add(new DTOErzieherart(3L, "Schüler ist volljährig"));
		erzieherarten.add(new DTOErzieherart(4L, "Schülerin ist volljährig"));
		erzieherarten.add(new DTOErzieherart(5L, "Eltern"));
		erzieherarten.add(new DTOErzieherart(6L, "Sonstige"));
		for (int i = 0; i < erzieherarten.size(); i++)
			erzieherarten.get(i).Sortierung = i + 1;
		conn.transactionPersistAll(erzieherarten);
		conn.transactionFlush();

		// K-Ort aus der Default-Daten-Tabelle befüllen
		final ArrayList<DTOOrt> dtoOrt = new ArrayList<>();
		final List<KatalogEintragOrte> katalog = CsvReader.fromResource("daten/csv/Orte.csv", KatalogEintragOrte.class);
		for (int i = 0; i < katalog.size(); i++) {
			final KatalogEintragOrte ort = katalog.get(i);
			final DTOOrt dto = new DTOOrt((long) i + 1, ort.PLZ, ort.Ort);
			dtoOrt.add(dto);
		}
		conn.transactionPersistAll(dtoOrt);
		conn.transactionFlush();

		// K_Religion aus dem Core-Type befüllen
		final ArrayList<DTOKonfession> dtoKonfession = new ArrayList<>();
		int i = 1;
		for (final Religion kon : Religion.values()) {
			final ReligionKatalogEintrag eintrag = kon.daten(schuljahr);
			if (eintrag == null)
				continue;
			final DTOKonfession dto = new DTOKonfession(i, eintrag.text);
			dto.StatistikKrz = eintrag.kuerzel;
			dto.Sortierung = i;
			dtoKonfession.add(dto);
			i++;
		}
		conn.transactionPersistAll(dtoKonfession);
		conn.transactionFlush();

		// K_Schule mit Schulen aus dem sonstigen Ausland, den Bundesländern und Nachbarländern, Keine Schul und der eigenen Schule befüllen (Core-Type)
		final ArrayList<DTOSchuleNRW> dtoSchulen = new ArrayList<>();
		i = 1;
		for (final Herkunftsschulnummern snr : Herkunftsschulnummern.values()) {
			final DTOSchuleNRW dto = new DTOSchuleNRW(i, "" + (200000 + i + 1));
			dto.SchulNr_SIM = "" + snr.daten.schulnummer;
			dto.Name = snr.daten.bezeichnung;
			dto.Sortierung = i;
			dto.Sichtbar = true;
			dto.Aenderbar = true;
			dto.KurzBez = snr.daten.bezeichnung;
			dtoSchulen.add(dto);
			i++;
		}
		conn.transactionPersistAll(dtoSchulen);
		conn.transactionFlush();

		// K_Schwerpunkte befüllen
		final ArrayList<DTOSchwerpunkt> schwerpunkte = new ArrayList<>();
		schwerpunkte.add(new DTOSchwerpunkt(1L, "naturwissenschaftlich-technisch"));
		schwerpunkte.add(new DTOSchwerpunkt(2L, "sozialwissenschaftlich"));
		schwerpunkte.add(new DTOSchwerpunkt(3L, "musisch-künstlerisch"));
		schwerpunkte.add(new DTOSchwerpunkt(4L, "fremdsprachlich"));
		for (i = 0; i < schwerpunkte.size(); i++)
			schwerpunkte.get(i).Sortierung = i + 1;
		conn.transactionPersistAll(schwerpunkte);
		conn.transactionFlush();

		//K_Sportbefreiung mit einem Beispiel befüllen
		final DTOSportbefreiung sportbefreiung = new DTOSportbefreiung(1L, "temporär - Schwimmen");
		sportbefreiung.Sortierung = 1;
		conn.transactionPersist(sportbefreiung);
		conn.transactionFlush();

		// K_Telefonart mit den Schild-NRW-Vorgaben befüllen
		final ArrayList<DTOTelefonArt> telefonArten = new ArrayList<>();
		telefonArten.add(new DTOTelefonArt(1L, "Eltern"));
		telefonArten.add(new DTOTelefonArt(2L, "Mutter"));
		telefonArten.add(new DTOTelefonArt(3L, "Vater"));
		telefonArten.add(new DTOTelefonArt(4L, "Schüler/-in"));
		telefonArten.add(new DTOTelefonArt(5L, "(sonst.) gesetzl. Vertreter"));
		telefonArten.add(new DTOTelefonArt(6L, "Notfallnummer"));
		telefonArten.add(new DTOTelefonArt(7L, "Festnetznummer"));
		telefonArten.add(new DTOTelefonArt(8L, "Mobilnummer"));
		telefonArten.add(new DTOTelefonArt(9L, "Fax-Nummer"));
		for (i = 0; i < telefonArten.size(); i++)
			telefonArten.get(i).Sortierung = i + 1;
		conn.transactionPersistAll(telefonArten);
		conn.transactionFlush();

		// Liefere die Schul-Stammdaten der neu angelegten Schule zurück.
		return this.get(null);
	}

}
