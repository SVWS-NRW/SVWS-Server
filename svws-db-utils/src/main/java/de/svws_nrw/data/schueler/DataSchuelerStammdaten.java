package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.schule.Verkehrssprache;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerStammdaten}.
 */
public final class DataSchuelerStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerStammdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchueler} in einen Core-DTO {@link SchuelerStammdaten}.
	 */
	private static final Function<DTOSchueler, SchuelerStammdaten> dtoMapper = (final DTOSchueler schueler) -> {
		final SchuelerStammdaten daten = new SchuelerStammdaten();
		// Basisdaten
		daten.id = schueler.ID;
		daten.foto = "";
		daten.nachname = (schueler.Nachname == null) ? "" : schueler.Nachname;
		daten.vorname = (schueler.Vorname == null) ? "" : schueler.Vorname;
		daten.alleVornamen = (schueler.AlleVornamen == null) ? "" : schueler.AlleVornamen;
		daten.geschlecht = schueler.Geschlecht.id;
		daten.geburtsdatum = schueler.Geburtsdatum;
		daten.geburtsort = schueler.Geburtsort;
		daten.geburtsname = schueler.Geburtsname;
		// Wohnort und Kontaktdaten
		daten.strassenname = schueler.Strassenname;
		daten.hausnummer = schueler.HausNr;
		daten.hausnummerZusatz = schueler.HausNrZusatz;
		daten.wohnortID = schueler.Ort_ID;
		daten.ortsteilID = schueler.Ortsteil_ID;
		daten.telefon = schueler.Telefon;
		daten.telefonMobil = schueler.Fax;
		daten.emailPrivat = schueler.Email;
		daten.emailSchule = schueler.SchulEmail;
		// Daten zur Staatsangehörigkeit und zur Religion
		daten.staatsangehoerigkeitID = (schueler.StaatKrz == null) ? null : schueler.StaatKrz.daten.iso3;
		daten.staatsangehoerigkeit2ID = (schueler.StaatKrz2 == null) ? null : schueler.StaatKrz2.daten.iso3;
		daten.religionID = schueler.Religion_ID;
		daten.druckeKonfessionAufZeugnisse = schueler.KonfDruck;
		daten.religionabmeldung = schueler.Religionsabmeldung;
		daten.religionanmeldung = schueler.Religionsanmeldung;
		// Daten zum Migrationshintergrund
		// TODO DB-Converter für boolean statt Boolean beim Migrationshintergrund
		daten.hatMigrationshintergrund = (schueler.Migrationshintergrund != null) && schueler.Migrationshintergrund;
		daten.zuzugsjahr = (schueler.JahrZuzug == null) ? null : schueler.JahrZuzug;
		daten.geburtsland = (schueler.GeburtslandSchueler == null) ? null : schueler.GeburtslandSchueler.daten.iso3;
		daten.verkehrspracheFamilie = (schueler.VerkehrsspracheFamilie == null) ? null : schueler.VerkehrsspracheFamilie.daten.kuerzel;
		daten.geburtslandVater = (schueler.GeburtslandVater == null) ? null : schueler.GeburtslandVater.daten.iso3;
		daten.geburtslandMutter = (schueler.GeburtslandMutter == null) ? null : schueler.GeburtslandMutter.daten.iso3;
		// Statusdaten
		daten.status = schueler.idStatus;
		daten.istDuplikat = schueler.Duplikat;
		daten.externeSchulNr = schueler.ExterneSchulNr;
		daten.fahrschuelerArtID = schueler.Fahrschueler_ID;
		daten.haltestelleID = schueler.Haltestelle_ID;
		daten.anmeldedatum = schueler.AnmeldeDatum;
		daten.aufnahmedatum = schueler.Aufnahmedatum;
		daten.istVolljaehrig = (schueler.Volljaehrig != null) && schueler.Volljaehrig; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.keineAuskunftAnDritte = schueler.KeineAuskunft;
		daten.istSchulpflichtErfuellt = (schueler.SchulpflichtErf != null) && schueler.SchulpflichtErf; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.istBerufsschulpflichtErfuellt = (schueler.BerufsschulpflErf != null) && schueler.BerufsschulpflErf; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.hatMasernimpfnachweis = schueler.MasernImpfnachweis;
		daten.erhaeltSchuelerBAFOEG = schueler.Bafoeg;
		daten.erhaeltMeisterBAFOEG = schueler.MeisterBafoeg;
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gibt die Liste der Schülerstammdaten für die übergebenen IDs zurück.
	 *
	 * @param conn	die Datenbank-Verbindung
	 * @param ids   die Liste der Schüler-IDs
	 *
	 * @return die Liste der Schülerstammdaten
	 */
	public static List<SchuelerStammdaten> getListStammdaten(final DBEntityManager conn, final List<Long> ids) {
		if (ids == null)
			throw new DeveloperNotificationException("Die Liste darf nicht null sein.");
		final var result = new ArrayList<SchuelerStammdaten>();
		if (ids.isEmpty())
			return result;
		final List<DTOSchueler> schueler = conn.queryByKeyList(DTOSchueler.class, ids);
		if ((schueler == null) || schueler.isEmpty())
			return result;
		final Map<Long, DTOSchuelerFoto> mapFotos = conn.queryByKeyList(DTOSchuelerFoto.class, ids)
				.stream().collect(Collectors.toMap(sf -> sf.Schueler_ID, sf -> sf));
		for (final DTOSchueler s : schueler) {
			final var tmp = dtoMapper.apply(s);
			final var tmpFoto = mapFotos.get(tmp.id);
			tmp.foto = (tmpFoto == null) ? null : tmpFoto.FotoBase64;
			result.add(tmp);
		}
		return result;
	}

	/**
	 * Gibt die Liste der Schülerstammdaten für die übergebene ID zurück.
	 *
	 * @param conn	die Datenbank-Verbindung
	 * @param id    die Schüler-ID
	 *
	 * @return die Liste der Schülerstammdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public SchuelerStammdaten getStammdaten(final DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final SchuelerStammdaten daten = dtoMapper.apply(schueler);
		final DTOSchuelerFoto schuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, id);
		if (schuelerFoto != null)
			daten.foto = schuelerFoto.FotoBase64;
		return daten;
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final SchuelerStammdaten daten = getStammdaten(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOSchueler>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, schueler, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != schueler.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("foto", (conn, schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				DTOSchuelerFoto schuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, schueler.ID);
				if (schuelerFoto == null)
					schuelerFoto = new DTOSchuelerFoto(schueler.ID);
				final String oldFoto = schuelerFoto.FotoBase64;
				if (((strData == null) && (oldFoto == null)) || ((strData != null) && (strData.equals(oldFoto))))
					return;
				schuelerFoto.FotoBase64 = strData;
				conn.transactionPersist(schuelerFoto);
			}),
			Map.entry("nachname",
					(conn, schueler, value, map) -> schueler.Nachname =
							JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Name.datenlaenge())),
			Map.entry("vorname",
					(conn, schueler, value, map) -> schueler.Vorname =
							JSONMapper.convertToString(value, false, false, Schema.tab_Schueler.col_Vorname.datenlaenge())),
			Map.entry("alleVornamen",
					(conn, schueler, value, map) -> schueler.AlleVornamen =
							JSONMapper.convertToString(value, false, true, Schema.tab_Schueler.col_Zusatz.datenlaenge())),
			Map.entry("geschlecht", (conn, schueler, value, map) -> {
				final Geschlecht geschlecht = Geschlecht.fromValue(JSONMapper.convertToInteger(value, false));
				if (geschlecht == null)
					throw new ApiOperationException(Status.CONFLICT);
				schueler.Geschlecht = geschlecht;
			}),
			Map.entry("geburtsdatum", (conn, schueler, value, map) -> schueler.Geburtsdatum = JSONMapper.convertToString(value, false, false, null)),
			Map.entry("geburtsort", (conn, schueler, value, map) -> schueler.Geburtsort =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Geburtsort.datenlaenge())),
			Map.entry("geburtsname", (conn, schueler, value, map) -> schueler.Geburtsname =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Geburtsname.datenlaenge())),

			// Wohnort und Kontaktdaten
			Map.entry("strassenname", (conn, schueler, value, map) -> schueler.Strassenname =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Strassenname.datenlaenge())),
			Map.entry("hausnummer", (conn, schueler, value, map) -> schueler.HausNr =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_HausNr.datenlaenge())),
			Map.entry("hausnummerZusatz", (conn, schueler, value, map) -> schueler.HausNrZusatz =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_HausNrZusatz.datenlaenge())),
			Map.entry("wohnortID", (conn, schueler, value, map) -> setWohnort(schueler, JSONMapper.convertToLong(value, true),
					(map.get("ortsteilID") == null) ? schueler.Ortsteil_ID : ((Long) map.get("ortsteilID")))),
			Map.entry("ortsteilID", (conn, schueler, value, map) -> setWohnort(schueler, (map.get("wohnortID") == null) ? schueler.Ort_ID
					: ((Long) map.get("wohnortID")), JSONMapper.convertToLong(value, true))),
			Map.entry("telefon", (conn, schueler, value, map) -> schueler.Telefon =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Telefon.datenlaenge())),
			Map.entry("telefonMobil", (conn, schueler, value, map) -> schueler.Fax =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Fax.datenlaenge())),
			Map.entry("emailPrivat", (conn, schueler, value, map) -> schueler.Email =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_Email.datenlaenge())),
			Map.entry("emailSchule", (conn, schueler, value, map) -> schueler.SchulEmail =
					JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_SchulEmail.datenlaenge())),

			// Daten zur Staatsangehörigkeit und zur Religion
			Map.entry("staatsangehoerigkeitID", (conn, schueler, value, map) -> {
				final String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true, null);
				if ((staatsangehoerigkeitID == null) || ("".equals(staatsangehoerigkeitID))) {
					schueler.StaatKrz = null;
				} else {
					final Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
					if (nat == null)
						throw new ApiOperationException(Status.NOT_FOUND);
					schueler.StaatKrz = nat;
				}
			}),
			Map.entry("staatsangehoerigkeit2ID", (conn, schueler, value, map) -> {
				final String staatsangehoerigkeit2ID = JSONMapper.convertToString(value, true, true, null);
				if ((staatsangehoerigkeit2ID == null) || ("".equals(staatsangehoerigkeit2ID))) {
					schueler.StaatKrz2 = null;
				} else {
					final Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeit2ID);
					if (nat == null)
						throw new ApiOperationException(Status.NOT_FOUND);
					schueler.StaatKrz2 = nat;
				}
			}),
			Map.entry("religionID", (conn, schueler, value, map) -> {
				final Long religionID = JSONMapper.convertToLong(value, true);
				if (religionID != null) {
					if (religionID < 0)
						throw new ApiOperationException(Status.CONFLICT);
					final DTOKonfession rel = conn.queryByKey(DTOKonfession.class, religionID);
					if (rel == null)
						throw new ApiOperationException(Status.NOT_FOUND);
				}
				schueler.Religion_ID = religionID;
			}),
			Map.entry("druckeKonfessionAufZeugnisse", (conn, schueler, value, map) -> schueler.KonfDruck = JSONMapper.convertToBoolean(value, false)),
			Map.entry("religionabmeldung", (conn, schueler, value, map) -> schueler.Religionsabmeldung = JSONMapper.convertToString(value, true, true, null)),
			Map.entry("religionanmeldung", (conn, schueler, value, map) -> schueler.Religionsanmeldung = JSONMapper.convertToString(value, true, true, null)),

			// Daten zum Migrationshintergrund
			Map.entry("hatMigrationshintergrund", (conn, schueler, value, map) -> schueler.Migrationshintergrund = JSONMapper.convertToBoolean(value, false)),
			Map.entry("zuzugsjahr", (conn, schueler, value, map) -> {
				// TODO Bestimme das aktuelle Jahr für die obere Grenze des Bereichs
				schueler.JahrZuzug = JSONMapper.convertToIntegerInRange(value, true, 1900, 3000);
			}),
			Map.entry("geburtsland", (conn, schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
				if (nat == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				schueler.GeburtslandSchueler = nat;
			}),
			Map.entry("verkehrspracheFamilie", (conn, schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Verkehrssprache vs = Verkehrssprache.getByKuerzelAuto(strData);
				if (vs == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				schueler.VerkehrsspracheFamilie = vs;
			}),
			Map.entry("geburtslandVater", (conn, schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
				if (nat == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				schueler.GeburtslandVater = nat;
			}),
			Map.entry("geburtslandMutter", (conn, schueler, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				final Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
				if (nat == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				schueler.GeburtslandMutter = nat;
			}),

			// Statusdaten
			Map.entry("status", (conn, schueler, value, map) -> {
				final int status = JSONMapper.convertToInteger(value, false);
				final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(schueler.Schuljahresabschnitts_ID);
				final SchuelerStatus s = SchuelerStatus.data().getWertBySchluessel("" + status);
				if (s == null)
					throw new ApiOperationException(Status.BAD_REQUEST);
				final SchuelerStatusKatalogEintrag ske = s.daten(abschnitt.schuljahr);
				if (ske == null)
					throw new ApiOperationException(Status.BAD_REQUEST);
				schueler.idStatus = status;
			}),
			Map.entry("externeSchulNr", (conn, schueler, value, map) -> {
				final String externeSchulNr = JSONMapper.convertToString(value, true, true, 6);
				if ((externeSchulNr == null) || externeSchulNr.isBlank()) {
					schueler.ExterneSchulNr = null;
				} else {
					if (externeSchulNr.length() != 6)
						throw new ApiOperationException(Status.BAD_REQUEST, "Die Anzahl der Ziffern einer Schulnummer aus NRW muss 6 betragen.");
					schueler.ExterneSchulNr = externeSchulNr;
				}
			}),
			Map.entry("fahrschuelerArtID", (conn, schueler, value, map) -> {
				final Long fid = JSONMapper.convertToLong(value, true);
				if ((fid != null) && (fid < 0))
					throw new ApiOperationException(Status.CONFLICT);
				if (fid == null) {
					schueler.Fahrschueler_ID = null;
				} else {
					final DTOFahrschuelerart f = conn.queryByKey(DTOFahrschuelerart.class, fid);
					if (f == null)
						throw new ApiOperationException(Status.NOT_FOUND);
					schueler.Fahrschueler_ID = fid;
				}
			}),
			Map.entry("haltestelleID", (conn, schueler, value, map) -> {
				final Long hid = JSONMapper.convertToLong(value, true);
				if ((hid != null) && (hid < 0))
					throw new ApiOperationException(Status.CONFLICT);
				if (hid == null) {
					schueler.Haltestelle_ID = null;
				} else {
					final DTOHaltestellen h = conn.queryByKey(DTOHaltestellen.class, hid);
					if (h == null)
						throw new ApiOperationException(Status.NOT_FOUND);
					schueler.Haltestelle_ID = hid;
				}
			}),
			Map.entry("anmeldedatum", (conn, schueler, value, map) -> schueler.AnmeldeDatum = JSONMapper.convertToString(value, true, false, null)),
			Map.entry("aufnahmedatum", (conn, schueler, value, map) -> {
				final String aufnahmedatum = JSONMapper.convertToString(value, true, true, null);
				schueler.Aufnahmedatum = "".equals(aufnahmedatum) ? null : aufnahmedatum;
			}),
			Map.entry("istVolljaehrig", (conn, schueler, value, map) -> schueler.Volljaehrig = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istSchulpflichtErfuellt", (conn, schueler, value, map) -> schueler.SchulpflichtErf = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istBerufsschulpflichtErfuellt", (conn, schueler, value, map) -> schueler.BerufsschulpflErf = JSONMapper.convertToBoolean(value, false)),
			Map.entry("hatMasernimpfnachweis", (conn, schueler, value, map) -> schueler.MasernImpfnachweis = JSONMapper.convertToBoolean(value, false)),
			Map.entry("keineAuskunftAnDritte", (conn, schueler, value, map) -> schueler.KeineAuskunft = JSONMapper.convertToBoolean(value, false)),
			Map.entry("erhaeltSchuelerBAFOEG", (conn, schueler, value, map) -> schueler.Bafoeg = JSONMapper.convertToBoolean(value, false)),
			Map.entry("erhaeltMeisterBAFOEG", (conn, schueler, value, map) -> schueler.MeisterBafoeg = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istDuplikat", (conn, schueler, value, map) -> schueler.Duplikat = JSONMapper.convertToBoolean(value, false)));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOSchueler.class, patchMappings);
	}


	/**
	 * Setzt den Wohnort bei den Schülerdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
	 * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
	 *
	 * @param schueler     das Schüler-DTO der Datenbank
	 * @param wohnortID    die zu setzende Wohnort-ID
	 * @param ortsteilID   die zu setzende O	eil-ID
	 *
	 * @throws ApiOperationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
	 */
	private void setWohnort(final DTOSchueler schueler, final Long wohnortID, final Long ortsteilID) throws ApiOperationException {
		if ((wohnortID != null) && (wohnortID < 0))
			throw new ApiOperationException(Status.CONFLICT);
		if ((ortsteilID != null) && (ortsteilID < 0))
			throw new ApiOperationException(Status.CONFLICT);
		schueler.Ort_ID = wohnortID;
		// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		Long ortsteilIDNeu = ortsteilID;
		if (ortsteilIDNeu != null) {
			final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilIDNeu);
			if ((ortsteil == null) || ((ortsteil.Ort_ID != null) && (!ortsteil.Ort_ID.equals(wohnortID)))) {
				ortsteilIDNeu = null;
			}
		}
		schueler.Ortsteil_ID = ortsteilIDNeu;
	}


}
