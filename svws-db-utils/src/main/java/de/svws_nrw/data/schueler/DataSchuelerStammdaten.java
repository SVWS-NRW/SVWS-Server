package de.svws_nrw.data.schueler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.schueler.Einschulungsart;
import de.svws_nrw.core.types.schule.Verkehrssprache;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerStammdaten}.
 */
public final class DataSchuelerStammdaten extends DataManagerRevised<Long, DTOSchueler, SchuelerStammdaten> {

	private final Long idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerStammdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		this.idSchuljahresabschnitt = 0L;
	}

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerStammdaten}.
	 *
	 * @param conn                     die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public DataSchuelerStammdaten(final DBEntityManager conn, final Long idSchuljahresabschnitt) {
		super(conn);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
	}

	@Override
	public SchuelerStammdaten getById(final Long id) throws ApiOperationException {
		final DTOSchueler dto = getDTO(id);
		final DTOSchuelerFoto dtoSchuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, id);
		final SchuelerStammdaten schuelerStammdaten = map(dto);
		schuelerStammdaten.foto = Optional.ofNullable(dtoSchuelerFoto).map(sf -> sf.FotoBase64).orElse(null);
		return schuelerStammdaten;
	}


	/**
	 * Liefert eine Response mit einer Liste mit {@link SchuelerStammdaten} Objekten zu den übergebenen IDs.
	 *
	 * @param ids   IDs der Schüler
	 *
	 * @return die Response mit der Liste von {@link SchuelerStammdaten} Objekten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getListByIdsAsResponse(final List<Long> ids) throws ApiOperationException {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getListByIds(ids)).build();
	}


	/**
	 * Liefert eine Liste mit {@link SchuelerStammdaten} Objekten zu den übergebenen IDs.
	 *
	 * @param ids   die IDs der Schüler
	 *
	 * @return die Liste mit den {@link SchuelerStammdaten} Objekten
	 *
	 * @throws ApiOperationException - Im Fehlerfall
	 */
	public List<SchuelerStammdaten> getListByIds(final List<Long> ids) throws ApiOperationException {
		final List<DTOSchueler> dtos = getDTOList(ids);
		final Map<Long, DTOSchuelerFoto> fotoDtosById = conn.queryByKeyList(DTOSchuelerFoto.class, ids).stream()
				.collect(Collectors.toMap(sf -> sf.Schueler_ID, sf -> sf));
		final List<SchuelerStammdaten> schuelerStammdatenListe = new ArrayList<>();
		for (final DTOSchueler dto : dtos) {
			final SchuelerStammdaten schuelerStammdaten = map(dto);
			schuelerStammdaten.foto = Optional.ofNullable(fotoDtosById)
					.map(sf -> sf.get(schuelerStammdaten.id))
					.map(foto -> foto.FotoBase64)
					.orElse(null);
			schuelerStammdatenListe.add(schuelerStammdaten);
		}
		return schuelerStammdatenListe;
	}


	/**
	 * Die Methode ermittelt das entsprechende {@link DTOSchueler} Objekt zur übergebenen ID.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return das zugehörige {@link DTOSchueler} Objekt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DTOSchueler getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schüler darf nicht null sein.");
		final DTOSchueler dto = conn.queryByKey(DTOSchueler.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Schüler zur ID " + id + " gefunden.");
		return dto;
	}


	/**
	 * Die Methode ermittelt die entsprechenden {@link DTOSchueler} Objekte zu den übergebenen IDs.
	 *
	 * @param ids   die IDs der Schüler
	 *
	 * @return die Liste mit den {@link DTOSchueler} Objekten.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<DTOSchueler> getDTOList(final List<Long> ids) throws ApiOperationException {
		if (ids == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Liste der IDs für die Schüler darf nicht null sein.");
		return (ids.isEmpty()) ? new ArrayList<>() : conn.queryByKeyList(DTOSchueler.class, ids);
	}


	@Override
	protected long getLongId(final DTOSchueler dto) throws ApiOperationException {
		return dto.ID;
	}

	@Override
	protected void initDTO(final DTOSchueler dto, final Long id, final Map<String, Object> initAttributes) {
		// Basisdaten
		dto.ID = id;
		dto.GU_ID = String.format("{%s}", UUID.randomUUID());
		dto.Schuljahresabschnitts_ID = idSchuljahresabschnitt;
		dto.Nachname = "";
		dto.Vorname = "";
		dto.AlleVornamen = "";
		dto.Geschlecht = Geschlecht.M;
		dto.Geburtsdatum = "";
		dto.Geburtsort = "";
		dto.Geburtsname = "";
		// Wohnort und Kontaktdaten
		dto.Strassenname = "";
		dto.HausNr = "";
		dto.HausNrZusatz = "";
		dto.Ort_ID = null;
		dto.Ortsteil_ID = null;
		dto.Telefon = "";
		dto.Fax = "";
		dto.Email = "";
		dto.SchulEmail = "";
		// Daten zur Staatsangehörigkeit und zur Religion
		dto.StaatKrz = null;
		dto.StaatKrz2 = null;
		dto.Religion_ID = null;
		dto.KonfDruck = false;
		dto.Religionsabmeldung = "";
		dto.Religionsanmeldung = "";
		// Daten zum Migrationshintergrund
		dto.Migrationshintergrund = false;
		dto.JahrZuzug = null;
		dto.GeburtslandSchueler = null;
		dto.VerkehrsspracheFamilie = null;
		dto.GeburtslandVater = null;
		dto.GeburtslandMutter = null;
		// Statusdaten
		dto.idStatus = null;
		dto.Duplikat = null;
		dto.ExterneSchulNr = null;
		dto.Fahrschueler_ID = null;
		dto.Haltestelle_ID = null;
		dto.AnmeldeDatum = null;
		dto.Aufnahmedatum = null;
		dto.Volljaehrig = false;
		dto.KeineAuskunft = false;
		dto.SchulpflichtErf = false;
		dto.BerufsschulpflErf = false;
		dto.MasernImpfnachweis = false;
		dto.Bafoeg = false;
		dto.MeisterBafoeg = false;

		dto.BeginnBildungsgang = null;
		dto.EinschulungsartASD = null;
		dto.DauerKindergartenbesuch = null;
		dto.Kindergarten_ID = null;
		dto.VerpflichtungSprachfoerderkurs = null;
		dto.TeilnahmeSprachfoerderkurs = null;
	}

	@Override
	protected SchuelerStammdaten map(final DTOSchueler dto) throws ApiOperationException {
		final SchuelerStammdaten daten = new SchuelerStammdaten();
		// Basisdaten
		daten.id = dto.ID;
		daten.nachname = (dto.Nachname == null) ? "" : dto.Nachname;
		daten.vorname = (dto.Vorname == null) ? "" : dto.Vorname;
		daten.alleVornamen = (dto.AlleVornamen == null) ? "" : dto.AlleVornamen;
		daten.geschlecht = dto.Geschlecht.id;
		daten.geburtsdatum = dto.Geburtsdatum;
		daten.geburtsort = dto.Geburtsort;
		daten.geburtsname = dto.Geburtsname;
		// Wohnort und Kontaktdaten
		daten.strassenname = dto.Strassenname;
		daten.hausnummer = dto.HausNr;
		daten.hausnummerZusatz = dto.HausNrZusatz;
		daten.wohnortID = dto.Ort_ID;
		daten.ortsteilID = dto.Ortsteil_ID;
		daten.telefon = dto.Telefon;
		daten.telefonMobil = dto.Fax;
		daten.emailPrivat = dto.Email;
		daten.emailSchule = dto.SchulEmail;
		// Daten zur Staatsangehörigkeit und zur Religion
		daten.staatsangehoerigkeitID = (dto.StaatKrz == null) ? null : dto.StaatKrz.historie().getLast().iso3;
		daten.staatsangehoerigkeit2ID = (dto.StaatKrz2 == null) ? null : dto.StaatKrz2.historie().getLast().iso3;
		daten.religionID = dto.Religion_ID;
		daten.druckeKonfessionAufZeugnisse = dto.KonfDruck;
		daten.religionabmeldung = dto.Religionsabmeldung;
		daten.religionanmeldung = dto.Religionsanmeldung;
		// Daten zum Migrationshintergrund
		// TODO DB-Converter für boolean statt Boolean beim Migrationshintergrund
		daten.hatMigrationshintergrund = Boolean.TRUE.equals(dto.Migrationshintergrund);
		daten.zuzugsjahr = dto.JahrZuzug;
		daten.geburtsland = (dto.GeburtslandSchueler == null) ? null : dto.GeburtslandSchueler.historie().getLast().iso3;
		daten.verkehrspracheFamilie = (dto.VerkehrsspracheFamilie == null) ? null : dto.VerkehrsspracheFamilie.daten.kuerzel;
		daten.geburtslandVater = (dto.GeburtslandVater == null) ? null : dto.GeburtslandVater.historie().getLast().iso3;
		daten.geburtslandMutter = (dto.GeburtslandMutter == null) ? null : dto.GeburtslandMutter.historie().getLast().iso3;
		// Statusdaten
		daten.status = dto.idStatus;
		daten.istDuplikat = dto.Duplikat;
		daten.externeSchulNr = dto.ExterneSchulNr;
		daten.fahrschuelerArtID = dto.Fahrschueler_ID;
		daten.haltestelleID = dto.Haltestelle_ID;
		daten.anmeldedatum = dto.AnmeldeDatum;
		daten.aufnahmedatum = dto.Aufnahmedatum;
		daten.istVolljaehrig = Boolean.TRUE.equals(dto.Volljaehrig); // TODO ermittle die Information aus den anderen Schülerdaten
		daten.keineAuskunftAnDritte = dto.KeineAuskunft;
		daten.istSchulpflichtErfuellt = Boolean.TRUE.equals(dto.SchulpflichtErf); // TODO ermittle die Information aus den anderen Schülerdaten
		daten.istBerufsschulpflichtErfuellt = Boolean.TRUE.equals(dto.BerufsschulpflErf); // TODO ermittle die Information aus den anderen Schülerdaten
		daten.hatMasernimpfnachweis = dto.MasernImpfnachweis;
		daten.erhaeltSchuelerBAFOEG = dto.Bafoeg;
		daten.erhaeltMeisterBAFOEG = dto.MeisterBafoeg;
		daten.beginnBildungsgang = dto.BeginnBildungsgang; // Schulform BK und SB
		// TODO DauerBildungsgang // Schulform BK und SB

		// TODO Die nachfolgenden Daten gehören in SchuelerSchulbesuchsdaten und nicht in SchuelerStammdaten
		final Einschulungsart einschulungsart = Einschulungsart.getBySchluessel(dto.EinschulungsartASD);
		daten.einschulungsartID = (einschulungsart == null) ? null : einschulungsart.daten.id;
		daten.dauerKindergartenbesuch = dto.DauerKindergartenbesuch;
		daten.kindergartenID = dto.Kindergarten_ID;

		// TODO klären, ob dies zu SchuelerSchulbesuchsdaten oder SchuelerStammdaten gehört
		daten.verpflichtungSprachfoerderkurs = (dto.VerpflichtungSprachfoerderkurs != null) && dto.VerpflichtungSprachfoerderkurs;
		daten.teilnahmeSprachfoerderkurs = (dto.TeilnahmeSprachfoerderkurs != null) && dto.TeilnahmeSprachfoerderkurs;
		return daten;
	}


	@Override
	protected void mapAttribute(final DTOSchueler dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			// Persönliche Daten
			case "id" -> mapID(dto, value);
			case "foto" -> mapSchuelerFoto(dto, value);
			case "nachname" -> dto.Nachname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Name.datenlaenge(), "nachname");
			case "vorname" -> dto.Vorname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Vorname.datenlaenge(), "vorname");
			case "alleVornamen" -> dto.AlleVornamen = JSONMapper.convertToString(value, false, true, Schema.tab_Schueler.col_Zusatz.datenlaenge(),
					"alleVornamen");
			case "geschlecht" -> mapGeschlecht(dto, value);
			case "geburtsdatum" -> dto.Geburtsdatum = JSONMapper.convertToString(value, false, false, null, "geburtsdatum");
			case "geburtsort" -> dto.Geburtsort = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Geburtsort.datenlaenge(), "geburtsort");
			case "geburtsname" -> dto.Geburtsname = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Geburtsname.datenlaenge(),
					"geburtsname");

			// Wohnort und Kontaktdaten
			case "strassenname" -> dto.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Strassenname.datenlaenge(),
					"strassenname");
			case "hausnummer" -> dto.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_HausNr.datenlaenge(), "hausnummer");
			case "hausnummerZusatz" -> dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_HausNrZusatz.datenlaenge(),
					"hausnummerZusatz");
			case "wohnortID" -> setWohnort(dto, JSONMapper.convertToLong(value, true, "wohnortID"),
					Optional.ofNullable(JSONMapper.convertToLong(map.get("ortsteilID"), true, "ortsteilID")).orElse(dto.Ortsteil_ID));
			case "ortsteilID" -> setWohnort(dto, Optional.ofNullable(JSONMapper.convertToLong(map.get("wohnortID"), true, "wohnortID")).orElse(dto.Ort_ID),
					JSONMapper.convertToLong(value, true, "ortsteilID"));
			case "telefon" -> dto.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Telefon.datenlaenge(), "telefon");
			case "telefonMobil" -> dto.Fax = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Fax.datenlaenge(), "telefonMobil");
			case "emailPrivat" -> dto.Email = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Email.datenlaenge(), "emailPrivat");
			case "emailSchule" -> dto.SchulEmail = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_SchulEmail.datenlaenge(),
					"emailSchule");

			// Daten zur Staatsangehörigkeit und zur Religion
			case "staatsangehoerigkeitID" -> mapStaatsangehoerigkeitID(dto, value);
			case "staatsangehoerigkeit2ID" -> mapStaatsangehoerigkeit2ID(dto, value);
			case "religionID" -> mapReligionID(dto, value);
			case "druckeKonfessionAufZeugnisse" -> dto.KonfDruck = JSONMapper.convertToBoolean(value, false, "druckeKonfessionAufZeugnisse");
			case "religionabmeldung" -> dto.Religionsabmeldung = JSONMapper.convertToString(value, true, true, null, "religionabmeldung");
			case "religionanmeldung" -> dto.Religionsanmeldung = JSONMapper.convertToString(value, true, true, null, "religionanmeldung");

			// Daten zum Migrationshintergrund
			case "hatMigrationshintergrund" -> dto.Migrationshintergrund = JSONMapper.convertToBoolean(value, false, "hatMigrationshintergrund");
			// TODO Bestimme das aktuelle Jahr für die obere Grenze des Bereichs
			case "zuzugsjahr" -> dto.JahrZuzug = JSONMapper.convertToIntegerInRange(value, true, 1900, 3000, "zuzugsjahr");
			case "verkehrspracheFamilie" -> mapVerkehrspracheFamilie(dto, value);
			case "geburtsland" -> mapGeburtsland(dto, value);
			case "geburtslandVater" -> mapGeburtslandVater(dto, value);
			case "geburtslandMutter" -> mapGeburtslandMutter(dto, value);

			// Statusdaten
			case "status" -> mapStatus(dto, value);
			case "externeSchulNr" -> mapExterneSchulNr(dto, value);
			case "fahrschuelerArtID" -> mapFahrschuelerArtID(dto, value);
			case "haltestelleID" -> mapHaltestelleID(dto, value);
			case "anmeldedatum" -> dto.AnmeldeDatum = JSONMapper.convertToString(value, true, false, null, "anmeldedatum");
			case "aufnahmedatum" -> dto.Aufnahmedatum = JSONMapper.convertToString(value, true, false, null, "aufnahmedatum");
			case "istVolljaehrig" -> dto.Volljaehrig = JSONMapper.convertToBoolean(value, false, "istVolljaehrig");
			case "istSchulpflichtErfuellt" -> dto.SchulpflichtErf = JSONMapper.convertToBoolean(value, false, "istSchulpflichtErfuellt");
			case "istBerufsschulpflichtErfuellt" -> dto.BerufsschulpflErf = JSONMapper.convertToBoolean(value, false, "istBerufsschulpflichtErfuellt");
			case "hatMasernimpfnachweis" -> dto.MasernImpfnachweis = JSONMapper.convertToBoolean(value, false, "hatMasernimpfnachweis");
			case "keineAuskunftAnDritte" -> dto.KeineAuskunft = JSONMapper.convertToBoolean(value, false, "keineAuskunftAnDritte");
			case "erhaeltSchuelerBAFOEG" -> dto.Bafoeg = JSONMapper.convertToBoolean(value, false, "erhaeltSchuelerBAFOEG");
			case "erhaeltMeisterBAFOEG" -> dto.MeisterBafoeg = JSONMapper.convertToBoolean(value, false, "erhaeltMeisterBAFOEG");
			case "istDuplikat" -> dto.Duplikat = JSONMapper.convertToBoolean(value, false, "istDuplikat");

			case "beginnBildungsgang" -> dto.BeginnBildungsgang = JSONMapper.convertToString(value, true, false, Schema.tab_Schueler.col_BeginnBildungsgang.datenlaenge(),
					"beginnBildungsgang");
			case "einschulungsartID" -> mapEinschulungsartID(dto, value);
			case "dauerKindergartenbesuch" -> dto.DauerKindergartenbesuch = JSONMapper.convertToString(value, true, false,
					Schema.tab_Schueler.col_DauerKindergartenbesuch.datenlaenge(), "dauerKindergartenbesuch");
			case "kindergartenID" -> mapKindergartenID(dto, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s ist nicht implementiert.".formatted(name));
		}
	}

	private static void mapID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true);
		if ((id == null) || (id != dto.ID))
			throw new ApiOperationException(Status.BAD_REQUEST);
	}


	private static void mapGeschlecht(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Integer geschlechtId = JSONMapper.convertToInteger(value, false, "geschlecht");
		final Geschlecht geschlecht = Geschlecht.fromValue(geschlechtId);
		if (geschlecht == null)
			throw new ApiOperationException(Status.CONFLICT);
		dto.Geschlecht = geschlecht;
	}


	private static void mapStaatsangehoerigkeitID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true, null, "staatsangehoerigkeitID");
		if ((staatsangehoerigkeitID == null) || staatsangehoerigkeitID.isEmpty()) {
			dto.StaatKrz = null;
		} else {
			final Nationalitaeten nationalitaet = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
			if (nationalitaet == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			dto.StaatKrz = nationalitaet;
		}
	}


	private static void mapStaatsangehoerigkeit2ID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String staatsangehoerigkeit2ID = JSONMapper.convertToString(value, true, true, null, "staatsangehoerigkeit2ID");
		if ((staatsangehoerigkeit2ID == null) || staatsangehoerigkeit2ID.isEmpty()) {
			dto.StaatKrz2 = null;
		} else {
			final Nationalitaeten nationalitaet = Nationalitaeten.getByISO3(staatsangehoerigkeit2ID);
			if (nationalitaet == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			dto.StaatKrz2 = nationalitaet;
		}
	}


	private void mapReligionID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Long religionID = JSONMapper.convertToLongInRange(value, true, 0L, null, "religionID");
		if (religionID != null) {
			final DTOKonfession religionDto = conn.queryByKey(DTOKonfession.class, religionID);
			if (religionDto == null)
				throw new ApiOperationException(Status.NOT_FOUND);
		}
		dto.Religion_ID = religionID;
	}


	private static void mapGeburtsland(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String geburtsland = JSONMapper.convertToString(value, true, true, null, "geburtsland");
		if ((geburtsland == null) || geburtsland.isBlank()) {
			dto.GeburtslandSchueler = null;
			return;
		}
		final Nationalitaeten nationalitaet = Nationalitaeten.getByISO3(geburtsland);
		if (nationalitaet == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		dto.GeburtslandSchueler = nationalitaet;
	}


	private static void mapVerkehrspracheFamilie(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String verkehrspracheFamilie = JSONMapper.convertToString(value, true, true, null, "verkehrspracheFamilie");
		if ((verkehrspracheFamilie == null) || verkehrspracheFamilie.isBlank()) {
			dto.VerkehrsspracheFamilie = null;
			return;
		}
		final Verkehrssprache verkehrsprache = Verkehrssprache.getByKuerzelAuto(verkehrspracheFamilie);
		if (verkehrsprache == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		dto.VerkehrsspracheFamilie = verkehrsprache;
	}


	private static void mapGeburtslandVater(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String geburtslandVater = JSONMapper.convertToString(value, true, true, null, "geburtslandVater");
		if ((geburtslandVater == null) || geburtslandVater.isBlank()) {
			dto.GeburtslandVater = null;
			return;
		}
		final Nationalitaeten nationalitaet = Nationalitaeten.getByISO3(geburtslandVater);
		if (nationalitaet == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		dto.GeburtslandVater = nationalitaet;
	}


	private static void mapGeburtslandMutter(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String geburtslandMutter = JSONMapper.convertToString(value, true, true, null, "geburtslandMutter");
		if ((geburtslandMutter == null) || geburtslandMutter.isBlank()) {
			dto.GeburtslandMutter = null;
			return;
		}
		final Nationalitaeten nationalitaet = Nationalitaeten.getByISO3(geburtslandMutter);
		if (nationalitaet == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		dto.GeburtslandMutter = nationalitaet;
	}


	private void mapStatus(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final int status = JSONMapper.convertToInteger(value, false, "status");
		final SchuelerStatus schuelerStatus = SchuelerStatus.data().getWertBySchluessel(String.valueOf(status));
		if (schuelerStatus == null)
			throw new ApiOperationException(Status.BAD_REQUEST);
		final long schuljahresabschnittsID = (dto.Schuljahresabschnitts_ID != null) ? dto.Schuljahresabschnitts_ID : idSchuljahresabschnitt;
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schuljahresabschnittsID);
		final SchuelerStatusKatalogEintrag schuelerStatusEintrag = schuelerStatus.daten(abschnitt.schuljahr);
		if (schuelerStatusEintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST);
		dto.idStatus = status;
	}


	private static void mapExterneSchulNr(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String externeSchulNr = JSONMapper.convertToString(value, true, true, 6, "externeSchulNr");
		if ((externeSchulNr != null) && (externeSchulNr.length() != 6))
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Anzahl der Ziffern einer Schulnummer aus NRW muss 6 betragen.");
		dto.ExterneSchulNr = ((externeSchulNr == null) || externeSchulNr.isBlank()) ? null : externeSchulNr;
	}


	private void mapFahrschuelerArtID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Long fahrschuelerArtId = JSONMapper.convertToLongInRange(value, true, 0L, null, "fahrschuelerArtID");
		if (fahrschuelerArtId != null) {
			final DTOFahrschuelerart fahrschuelerArtDto = conn.queryByKey(DTOFahrschuelerart.class, fahrschuelerArtId);
			if (fahrschuelerArtDto == null)
				throw new ApiOperationException(Status.NOT_FOUND);
		}
		dto.Fahrschueler_ID = fahrschuelerArtId;
	}


	private void mapHaltestelleID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Long haltestelleId = JSONMapper.convertToLongInRange(value, true, 0L, null, "haltestelleID");
		if (haltestelleId != null) {
			final DTOHaltestellen haltestellenDto = conn.queryByKey(DTOHaltestellen.class, haltestelleId);
			if (haltestellenDto == null)
				throw new ApiOperationException(Status.NOT_FOUND);
		}
		dto.Haltestelle_ID = haltestelleId;
	}

	// TODO -> verschieben nach DataSchuelerSchulbesuchsdaten bzw. SchuelerSchulbesuchdaten
	private static void mapEinschulungsartID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Long idEinschulungsart = JSONMapper.convertToLongInRange(value, true, 0L, null, "einschulungsartID");
		final Einschulungsart einschulungsart = Einschulungsart.getByID(idEinschulungsart);
		if (einschulungsart == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID %d der Einschulungsart ist ungültig.".formatted(idEinschulungsart));
		dto.EinschulungsartASD = einschulungsart.daten.kuerzel;
	}

	// TODO -> verschieben nach DataSchuelerSchulbesuchsdaten bzw. SchuelerSchulbesuchdaten
	private void mapKindergartenID(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final Long kindergartenId = JSONMapper.convertToLongInRange(value, true, 0L, null, "kindergartenID");
		if (kindergartenId != null) {
			final DTOKindergarten kindergartenDTO = conn.queryByKey(DTOKindergarten.class, kindergartenId);
			if (kindergartenDTO == null)
				throw new ApiOperationException(Status.NOT_FOUND);
		}
		dto.Kindergarten_ID = kindergartenId;
	}

	/**
	 * Setzt das Schüler-Foto für den übergebenen Schüler.
	 *
	 * @param dto     das DB-DTO des Schülers
	 * @param value   das Schüler-Foto in Base64-Kodierung
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	void mapSchuelerFoto(final DTOSchueler dto, final Object value) throws ApiOperationException {
		final String schuelerFotoNeu = JSONMapper.convertToString(value, true, true, null);
		DTOSchuelerFoto schuelerFotoOldDto = conn.queryByKey(DTOSchuelerFoto.class, dto.ID);
		if (schuelerFotoOldDto == null)
			schuelerFotoOldDto = new DTOSchuelerFoto(dto.ID);
		if ((schuelerFotoNeu != null) && !schuelerFotoNeu.equals(schuelerFotoOldDto.FotoBase64)) {
			schuelerFotoOldDto.FotoBase64 = schuelerFotoNeu;
			conn.transactionPersist(schuelerFotoOldDto);
		}
	}


	/**
	 * Setzt den Wohnort bei den Schülerdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
	 * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
	 *
	 * @param dto das Schüler-DTO der Datenbank
	 * @param wohnortID die zu setzende Wohnort-ID
	 * @param ortsteilID die zu setzende O	eil-ID
	 *
	 * @throws ApiOperationException eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
	 */
	void setWohnort(final DTOSchueler dto, final Long wohnortID, final Long ortsteilID) throws ApiOperationException {
		if ((wohnortID != null) && (wohnortID < 0))
			throw new ApiOperationException(Status.CONFLICT);
		if ((ortsteilID != null) && (ortsteilID < 0))
			throw new ApiOperationException(Status.CONFLICT);

		// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		dto.Ortsteil_ID = isOrtsteilGueltig(ortsteilID, wohnortID) ? ortsteilID : null;
		dto.Ort_ID = wohnortID;
	}


	/**
	 * Prüft, ob der Ortsteil mit der übergebenen ID für den Wohnort mit der übergebenen ID gültig ist.
	 *
	 * @param ortsteilID   die ID des Ortsteils
	 * @param wohnortID    die ID des Wohnortes
	 *
	 * @return true, falls der Ortsteil für den Wohnort gültig ist, und ansonsten false
	 */
	boolean isOrtsteilGueltig(final Long ortsteilID, final Long wohnortID) {
		if (ortsteilID == null)
			return false;
		final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilID);
		return (ortsteil != null) && Objects.equals(ortsteil.Ort_ID, wohnortID);
	}

}
