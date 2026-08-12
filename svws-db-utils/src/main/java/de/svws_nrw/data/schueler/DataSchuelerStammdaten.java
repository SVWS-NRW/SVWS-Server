package de.svws_nrw.data.schueler;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import de.svws_nrw.service.schueler.foto.SchuelerFoto;
import de.svws_nrw.service.schueler.foto.SchuelerFotoServiceFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.annotation.Nonnull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerStammdaten}.
 */
public final class DataSchuelerStammdaten extends DataManagerRevised<Long, DTOSchueler, SchuelerStammdaten> {

	private static final String ID_SCHULJAHRESABSCHNITT = "idSchuljahresabschnitt";
	private static final String WOHNORT_ID = "wohnortID";
	private static final String ORTSTEIL_ID = "ortsteilID";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerStammdaten(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public SchuelerStammdaten getById(final Long id) {
		final DTOSchueler dto = getDTO(id);
		final SchuelerStammdaten schuelerStammdaten = map(dto);
		schuelerStammdaten.foto = SchuelerFotoServiceFactory.getNewInstance()
				.getSchuelerFotoService()
				.findByIdSchueler(id)
				.map(SchuelerFoto::fotoBase64)
				.orElse(null);
		return schuelerStammdaten;
	}

	@Override
	protected void initDTO(final DTOSchueler dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
		dto.GU_ID = String.format("{%s}", UUID.randomUUID());
		dto.Schuljahresabschnitts_ID = JSONMapper.convertToLong(initAttributes.get(ID_SCHULJAHRESABSCHNITT), false, ID_SCHULJAHRESABSCHNITT);
		dto.Migrationshintergrund = false;
		dto.KonfDruck = false;
		dto.Duplikat = false;
		dto.Volljaehrig = false;
		dto.KeineAuskunft = false;
		dto.SchulpflichtErf = false;
		dto.BerufsschulpflErf = false;
		dto.MasernImpfnachweis = false;
		dto.Bafoeg = false;
		dto.MeisterBafoeg = false;
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
	public Response getListByIdsAsResponse(final List<Long> ids) {
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
	public List<SchuelerStammdaten> getListByIds(final List<Long> ids) {
		final List<DTOSchueler> schuelerDtos = getDTOList(ids);
		final Map<Long, SchuelerFoto> fotoDtosBySchuelerId = SchuelerFotoServiceFactory.getNewInstance()
				.getSchuelerFotoService()
				.getBySchuelerIds(ids)
				.stream()
				.collect(Collectors.toMap(SchuelerFoto::idSchueler, sf -> sf));

		return schuelerDtos.stream()
				.map(schueler -> {
					final var schuelerStammdaten = map(schueler);
					schuelerStammdaten.foto = Optional.of(fotoDtosBySchuelerId)
							.map(sf -> sf.get(schuelerStammdaten.id))
							.map(SchuelerFoto::fotoBase64)
							.orElse(null);
					return schuelerStammdaten;
				})
				.toList();
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
	public DTOSchueler getDTO(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schüler darf nicht null sein.");
		}
		final DTOSchueler dto = this.conn.queryByKey(DTOSchueler.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Schüler zur ID " + id + " gefunden.");
		}
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
	public List<DTOSchueler> getDTOList(final List<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Liste der IDs für die Schüler darf nicht null sein.");
		}
		return this.conn.queryByKeyList(DTOSchueler.class, CollectionUtils.emptyIfNull(ids));
	}


	@Override
	protected long getLongId(final DTOSchueler dto) {
		return dto.ID;
	}

	@Override
	protected Long getID(final Map<String, Object> attributes) {
		return JSONMapper.convertToLong(attributes.get("id"), false);
	}

	@Override
	protected SchuelerStammdaten map(final DTOSchueler dto) {
		final SchuelerStammdaten daten = new SchuelerStammdaten();
		// Basisdaten
		daten.id = dto.ID;
		daten.nachname = StringUtils.defaultString(dto.Nachname);
		daten.vorname = StringUtils.defaultString(dto.Vorname);
		daten.alleVornamen = StringUtils.defaultString(dto.AlleVornamen);
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
		daten.idStaatsangehoerigkeit = (dto.StaatKrz == null) ? null : dto.StaatKrz.historie().getLast().id;
		daten.idStaatsangehoerigkeit2 = (dto.StaatKrz2 == null) ? null : dto.StaatKrz2.historie().getLast().id;
		daten.religionID = dto.Religion_ID;
		daten.druckeKonfessionAufZeugnisse = dto.KonfDruck;
		daten.religionabmeldung = dto.Religionsabmeldung;
		daten.religionanmeldung = dto.Religionsanmeldung;
		// Daten zum Migrationshintergrund
		// TODO DB-Converter für boolean statt Boolean beim Migrationshintergrund
		daten.hatMigrationshintergrund = Boolean.TRUE.equals(dto.Migrationshintergrund);
		daten.zuzugsjahr = dto.JahrZuzug;
		daten.idGeburtsland = (dto.GeburtslandSchueler == null) ? null : dto.GeburtslandSchueler.historie().getLast().id;
		daten.idVerkehrspracheFamilie = (dto.VerkehrsspracheFamilie == null) ? null : dto.VerkehrsspracheFamilie.historie().getLast().id;
		daten.idGeburtslandVater = (dto.GeburtslandVater == null) ? null : dto.GeburtslandVater.historie().getLast().id;
		daten.idGeburtslandMutter = (dto.GeburtslandMutter == null) ? null : dto.GeburtslandMutter.historie().getLast().id;
		// Statusdaten
		daten.status = dto.idStatus;
		daten.istDuplikat = dto.Duplikat;
		daten.externeSchulNr = dto.ExterneSchulNr;
		daten.idSchuelerausweis = dto.Ausweisnummer;
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
		daten.dauerBildungsgang = dto.DauerBildungsgang; // Schulform BK und SB
		daten.beruf = dto.Beruf; // Schulform BK/SB/WB
		return daten;
	}


	@Override
	@SuppressWarnings("java:S1479")
	protected void mapAttribute(final DTOSchueler dto, final String name, final Object value, final Map<String, Object> map) {
		switch (name) {
			// Persönliche Daten
			case "id" -> mapID(dto, value);
			case "foto" -> mapSchuelerFoto(dto, value);
			case "nachname" -> dto.Nachname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Name.datenlaenge(), "nachname");
			case "vorname" -> dto.Vorname = JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Vorname.datenlaenge(), "vorname");
			case "alleVornamen" -> dto.AlleVornamen = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Zusatz.datenlaenge(),
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
			case WOHNORT_ID -> mapWohnort(dto, value, map);
			case ORTSTEIL_ID -> mapOrtsteil(dto, value, map);
			case "telefon" -> dto.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Telefon.datenlaenge(), "telefon");
			case "telefonMobil" -> dto.Fax = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Fax.datenlaenge(), "telefonMobil");
			case "emailPrivat" -> dto.Email = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Email.datenlaenge(), "emailPrivat");
			case "emailSchule" -> dto.SchulEmail = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_SchulEmail.datenlaenge(),
					"emailSchule");

			// Daten zur Staatsangehörigkeit und zur Religion
			case "idStaatsangehoerigkeit" -> mapStaatsangehoerigkeit(dto, value);
			case "idStaatsangehoerigkeit2" -> mapStaatsangehoerigkeit2(dto, value);
			case "religionID" -> mapReligionID(dto, value);
			case "druckeKonfessionAufZeugnisse" -> dto.KonfDruck = JSONMapper.convertToBoolean(value, false, "druckeKonfessionAufZeugnisse");
			case "religionabmeldung" -> dto.Religionsabmeldung = JSONMapper.convertToString(value, true, true, null, "religionabmeldung");
			case "religionanmeldung" -> dto.Religionsanmeldung = JSONMapper.convertToString(value, true, true, null, "religionanmeldung");

			// Daten zum Migrationshintergrund
			case "hatMigrationshintergrund" -> dto.Migrationshintergrund = JSONMapper.convertToBoolean(value, false, "hatMigrationshintergrund");
			case "zuzugsjahr" -> dto.JahrZuzug = JSONMapper.convertToIntegerInRange(value, true, 1900, Year.now().plusYears(1).getValue(), "zuzugsjahr");
			case "idVerkehrspracheFamilie" -> mapVerkehrsspracheFamilie(dto, value);
			case "idGeburtsland" -> mapGeburtsland(dto, value);
			case "idGeburtslandVater" -> mapGeburtslandVater(dto, value);
			case "idGeburtslandMutter" -> mapGeburtslandMutter(dto, value);

			// Statusdaten
			case "status" -> mapStatus(dto, value);
			case "externeSchulNr" -> mapExterneSchulNr(dto, value);
			case "idSchuelerausweis" -> dto.Ausweisnummer = JSONMapper.convertToString(value, true, true,
					Schema.tab_Schueler.col_Ausweisnummer.datenlaenge(), "idSchuelerausweis");
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
			case "beginnBildungsgang" -> dto.BeginnBildungsgang = JSONMapper.convertToString(value, true, false,
					Schema.tab_Schueler.col_BeginnBildungsgang.datenlaenge(), "beginnBildungsgang");
			case "dauerBildungsgang" -> dto.DauerBildungsgang = JSONMapper.convertToInteger(value, true, "dauerBildungsgang");
			case "beruf" -> dto.Beruf = JSONMapper.convertToString(value, true, true, 100, name);
			case ID_SCHULJAHRESABSCHNITT -> {
				//do nothing
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s ist nicht implementiert.".formatted(name));
		}
	}

	private static void mapID(final DTOSchueler dto, final Object value) {
		final Long id = JSONMapper.convertToLong(value, true, "id");
		if ((id == null) || (id != dto.ID)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID darf nicht verändert werden.");
		}
	}

	private static void mapGeschlecht(final DTOSchueler dto, final Object value) {
		final Integer geschlechtId = JSONMapper.convertToInteger(value, false, "geschlecht");
		final Geschlecht geschlecht = Geschlecht.fromValue(geschlechtId);
		if (geschlecht == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Geschlecht darf nicht null sein.");
		}
		dto.Geschlecht = geschlecht;
	}

	private void mapOrtsteil(final DTOSchueler dto, final Object value, final Map<String, Object> map) {
		final boolean patchContainsWohnortId = map.containsKey(WOHNORT_ID);
		if (!patchContainsWohnortId) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Patch enthält keine wohnortID. Ort und Ortsteil können nur zusammen geändert werden.");
		}

		final Long wohnortId = JSONMapper.convertToLong(map.get(WOHNORT_ID), true, WOHNORT_ID);
		final Long ortsteilId = JSONMapper.convertToLong(value, true, ORTSTEIL_ID);
		updateWohnortAndOrtsteil(dto, wohnortId, ortsteilId);
	}

	private void mapWohnort(final DTOSchueler dto, final Object value, final Map<String, Object> map) {
		final boolean patchContainsOrtsteilId = map.containsKey(ORTSTEIL_ID);
		if (!patchContainsOrtsteilId) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Patch enthält keine ortsteilID. Ort und Ortsteil können nur zusammen geändert werden.");
		}

		final Long wohnortId = JSONMapper.convertToLong(value, true, WOHNORT_ID);
		final Long ortsteilId = JSONMapper.convertToLong(map.get(ORTSTEIL_ID), true, ORTSTEIL_ID);
		updateWohnortAndOrtsteil(dto, wohnortId, ortsteilId);
	}

	private static void mapStaatsangehoerigkeit(final DTOSchueler dto, final Object value) {
		final Long idStaatsangehoerigkeit = JSONMapper.convertToLong(value, true, "idStaatsangehoerigkeit");
		if (idStaatsangehoerigkeit == null) {
			dto.StaatKrz = null;
		} else {
			dto.StaatKrz = getNationalitaetByID(idStaatsangehoerigkeit);
		}
	}

	private static void mapStaatsangehoerigkeit2(final DTOSchueler dto, final Object value) {
		final Long idStaatsangehoerigkeit2 = JSONMapper.convertToLong(value, true, "idStaatsangehoerigkeit2");
		if (idStaatsangehoerigkeit2 == null) {
			dto.StaatKrz2 = null;
		} else {
			dto.StaatKrz2 = getNationalitaetByID(idStaatsangehoerigkeit2);
		}
	}

	private void mapReligionID(final DTOSchueler dto, final Object value) {
		final Long religionID = JSONMapper.convertToLongInRange(value, true, 0L, null, "religionID");
		if (religionID != null) {
			checkReligionExists(religionID);
		}
		dto.Religion_ID = religionID;
	}

	private void checkReligionExists(final Long religionID) {
		final DTOKonfession religionDto = this.conn.queryByKey(DTOKonfession.class, religionID);
		if (religionDto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Konfession zur ID %d gefunden.".formatted(religionID));
		}
	}

	private static void mapGeburtsland(final DTOSchueler dto, final Object value) {
		final Long idGeburtsland = JSONMapper.convertToLong(value, true, "idGeburtsland");
		if (idGeburtsland == null) {
			dto.GeburtslandSchueler = null;
		} else {
			dto.GeburtslandSchueler = getNationalitaetByID(idGeburtsland);
		}
	}

	private static void mapVerkehrsspracheFamilie(final DTOSchueler dto, final Object value) {
		final Long idVerkehrsspracheFamilie = JSONMapper.convertToLong(value, true, "idVerkehrsspracheFamilie");
		if (idVerkehrsspracheFamilie == null) {
			dto.VerkehrsspracheFamilie = null;
		} else {
			dto.VerkehrsspracheFamilie = getVerkehrsspracheByID(idVerkehrsspracheFamilie);
		}
	}

	@Nonnull
	private static Verkehrssprache getVerkehrsspracheByID(final Long idVerkehrsspracheFamilie) {
		final Verkehrssprache verkehrssprache = Verkehrssprache.data().getWertByIDOrNull(idVerkehrsspracheFamilie);
		if (verkehrssprache == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Verkehrssprache zum Wert %s gefunden.".formatted(idVerkehrsspracheFamilie));
		}
		return verkehrssprache;
	}

	private static void mapGeburtslandVater(final DTOSchueler dto, final Object value) {
		final Long idGeburtslandVater = JSONMapper.convertToLong(value, true, "idGeburtslandVater");
		if (idGeburtslandVater == null) {
			dto.GeburtslandVater = null;
		} else {
			dto.GeburtslandVater = getNationalitaetByID(idGeburtslandVater);
		}
	}

	private static void mapGeburtslandMutter(final DTOSchueler dto, final Object value) {
		final Long idGeburtslandMutter = JSONMapper.convertToLong(value, true, "idGeburtslandMutter");
		if (idGeburtslandMutter == null) {
			dto.GeburtslandMutter = null;
		} else {
			dto.GeburtslandMutter = getNationalitaetByID(idGeburtslandMutter);
		}
	}

	private void mapStatus(final DTOSchueler dto, final Object value) {
		final int status = JSONMapper.convertToInteger(value, false, "status");
		checkSchuelerStatusExists(dto, status);
		dto.idStatus = status;
	}

	private void checkSchuelerStatusExists(final DTOSchueler schuelerDto, final int status) {
		final SchuelerStatus schuelerStatus = SchuelerStatus.data().getWertBySchluessel(String.valueOf(status));
		if (schuelerStatus == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein SchuelerStatus zum Wert %d gefunden.".formatted(status));
		}

		final Schuljahresabschnitt abschnitt = this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schuelerDto.Schuljahresabschnitts_ID);
		final SchuelerStatusKatalogEintrag schuelerStatusEintrag = schuelerStatus.daten(abschnitt.schuljahr);
		if (schuelerStatusEintrag == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurde kein SchuelerStatus zum Wert %d und Schuljahr %d gefunden.".formatted(status, abschnitt.schuljahr));
		}
	}

	private static void mapExterneSchulNr(final DTOSchueler dto, final Object value) {
		final String externeSchulNr = JSONMapper.convertToString(value, true, true, 6, "externeSchulNr");
		if ((externeSchulNr != null) && (externeSchulNr.length() != 6)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Anzahl der Ziffern einer Schulnummer aus NRW muss 6 betragen.");
		}
		dto.ExterneSchulNr = StringUtils.defaultIfBlank(externeSchulNr, null);
	}

	private void mapFahrschuelerArtID(final DTOSchueler dto, final Object value) {
		final Long fahrschuelerArtId = JSONMapper.convertToLongInRange(value, true, 0L, null, "fahrschuelerArtID");
		if (fahrschuelerArtId != null) {
			checkFahrschuelerArtExists(fahrschuelerArtId);
		}
		dto.Fahrschueler_ID = fahrschuelerArtId;
	}

	private void checkFahrschuelerArtExists(final Long fahrschuelerArtId) {
		final DTOFahrschuelerart fahrschuelerArtDto = this.conn.queryByKey(DTOFahrschuelerart.class, fahrschuelerArtId);
		if (fahrschuelerArtDto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Fahrschülerart zur ID %d gefunden.".formatted(fahrschuelerArtId));
		}
	}

	private void mapHaltestelleID(final DTOSchueler dto, final Object value) {
		final Long haltestelleId = JSONMapper.convertToLongInRange(value, true, 0L, null, "haltestelleID");
		if (haltestelleId != null) {
			checkHaltestelleExists(haltestelleId);
		}
		dto.Haltestelle_ID = haltestelleId;
	}

	private void checkHaltestelleExists(final Long haltestelleId) {
		final DTOHaltestellen haltestellenDto = this.conn.queryByKey(DTOHaltestellen.class, haltestelleId);
		if (haltestellenDto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Haltestelle zur ID %d gefunden.".formatted(haltestelleId));
		}
	}

	/**
	 * - Das bestehende Schüler-Foto wird gelöscht, wenn der übergebene Parameter {@code value} gleich {@code null} ist.<br>
	 * - Das bestehende Schüler-Foto wird geändert, wenn der übergebene Parameter {@code value} ungleich dem aktuellen Wert ist.<br>
	 * - Es wird ein neues Schüler-Foto erstellt, wenn kein Schüler-Foto existiert und der Parameter {@code value} nicht {@code null} ist.
	 *
	 * @param schuelerDto das DB-DTO des Schülers
	 * @param value       das Schüler-Foto in Base64-Kodierung
	 */
	void mapSchuelerFoto(final DTOSchueler schuelerDto, final Object value) {
		final String newSchuelerFotoBase64 = JSONMapper.convertToString(value, true, true, null);
		SchuelerFotoServiceFactory.getNewInstance()
				.getSchuelerFotoService()
				.upsertOrDelete(schuelerDto.ID, newSchuelerFotoBase64);
	}

	private void updateWohnortAndOrtsteil(final DTOSchueler schueler, final Long wohnortID, final Long ortsteilID) {
		if (ortAndOrtsteilAreNull(wohnortID, ortsteilID) || (ortExists(wohnortID) && ortsteilIsNullOrMatchesToOrt(ortsteilID, wohnortID))) {
			schueler.Ort_ID = wohnortID;
			schueler.Ortsteil_ID = ortsteilID;
		} else {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Kombination von Ort und Ortsteil ist nicht zulässig. Der Ortsteil ist dem Ort nicht zugeordnet.");
		}
	}

	private static boolean ortAndOrtsteilAreNull(final Long wohnortID, final Long ortsteilID) {
		return (wohnortID == null) && (ortsteilID == null);
	}

	private boolean ortsteilIsNullOrMatchesToOrt(final Long ortsteilID, final Long wohnortID) {
		if (ortsteilID == null) {
			return true;
		}
		final DTOOrtsteil ortsteilDto = this.conn.queryByKey(DTOOrtsteil.class, ortsteilID);
		return (ortsteilDto != null) && Objects.equals(ortsteilDto.Ort_ID, wohnortID);
	}

	private boolean ortExists(final Long ortId) {
		return this.conn.existsBy(DTOOrt.QUERY_BY_ID, DTOOrt.class, ortId);
	}

	private static Nationalitaeten getNationalitaetByID(final Long id) {
		final Nationalitaeten nationalitaet = Nationalitaeten.data().getWertByIDOrNull(id);
		if (nationalitaet == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Nationalität zum Wert %s gefunden.".formatted(id));
		}
		return nationalitaet;
	}

}
