package de.svws_nrw.data.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataSchulleitung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerStammdaten}.
 */
public final class DataLehrerStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerStammdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrer} in einen Core-DTO {@link LehrerStammdaten}.
	 */
	public final DTOMapper<DTOLehrer, LehrerStammdaten> dtoMapper = (final DTOLehrer lehrer) -> {
		final LehrerStammdaten daten = new LehrerStammdaten();
		daten.id = lehrer.ID;
		daten.kuerzel = lehrer.Kuerzel;
		daten.personalTyp = lehrer.PersonTyp.kuerzel;
		daten.anrede = (lehrer.Anrede == null) ? "" : lehrer.Anrede;
		daten.titel = (lehrer.Titel == null) ? "" : lehrer.Titel;
		daten.amtsbezeichnung = (lehrer.Amtsbezeichnung == null) ? "" : lehrer.Amtsbezeichnung;
		daten.nachname = (lehrer.Nachname == null) ? "" : lehrer.Nachname;
		daten.vorname = (lehrer.Vorname == null) ? "" : lehrer.Vorname;
		daten.geschlecht = lehrer.Geschlecht.id;
		daten.geburtsdatum = lehrer.Geburtsdatum;
		daten.staatsangehoerigkeitID = (lehrer.staatsangehoerigkeit == null) ? null : lehrer.staatsangehoerigkeit.daten.iso3;
		daten.strassenname = lehrer.Strassenname;
		daten.hausnummer = lehrer.HausNr;
		daten.hausnummerZusatz = lehrer.HausNrZusatz;
		daten.wohnortID = lehrer.Ort_ID;
		daten.ortsteilID = lehrer.Ortsteil_ID;
		daten.telefon = lehrer.telefon;
		daten.telefonMobil = lehrer.telefonMobil;
		daten.emailDienstlich = lehrer.eMailDienstlich;
		daten.emailPrivat = lehrer.eMailPrivat;
		daten.istSichtbar = (lehrer.Sichtbar == null) || lehrer.Sichtbar;
		daten.istRelevantFuerStatistik = (lehrer.statistikRelevant == null) || lehrer.statistikRelevant;
		daten.foto = "";
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

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final LehrerStammdaten daten = getFromID(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Gibt die Lehrerstammdaten zur ID der Lehrkraft zurück.
	 *
	 * @param id	Die ID der Lehrkraft.
	 *
	 * @return		Die Lehrerstammdaten zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public LehrerStammdaten getFromID(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine ID für die Lehrkraft übergeben");
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lehrkraft zur ID " + id + " gefunden.");
		final LehrerStammdaten daten = dtoMapper.apply(lehrer);
		final DTOLehrerFoto lehrerFoto = conn.queryByKey(DTOLehrerFoto.class, id);
		if (lehrerFoto != null)
			daten.foto = lehrerFoto.FotoBase64;
		daten.leitungsfunktionen.addAll(DataSchulleitung.getSchulleitungsfunktionen(conn, id));
		return daten;
	}

	/**
	 * Gibt die Liste der Stammdaten aller Lehrer zurück, die in der angegebenen Liste enthalten sind.
	 *
	 * @param conn	die Datenbank-Verbindung
	 * @param idsLehrer die Liste der IDs der gewünschten Lehrer
	 *
	 * @return Liste der Stammdaten der Lehrer zu den IDs, bei einer leeren ID-Liste werden alle Lehrer zurückgegeben.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<LehrerStammdaten> getFromIDs(final DBEntityManager conn, final List<Long> idsLehrer) throws ApiOperationException {
		final var result = new ArrayList<LehrerStammdaten>();
		if (idsLehrer == null)
			return result;

		final List<DTOLehrer> dtoListLehrer = new ArrayList<>();
		if (idsLehrer.isEmpty())
			dtoListLehrer.addAll(conn.queryList(DTOLehrer.QUERY_ALL, DTOLehrer.class));
		else
			dtoListLehrer.addAll(conn.queryByKeyList(DTOLehrer.class, idsLehrer));
		if (dtoListLehrer.isEmpty())
			return result;

		final List<DTOLehrer> lehrer = dtoListLehrer.stream().filter(l -> idsLehrer.contains(l.ID)).toList();

		final Map<Long, DTOLehrerFoto> mapFotos = conn.queryByKeyList(DTOLehrerFoto.class, dtoListLehrer.stream().map(l -> l.ID).toList())
				.stream().collect(Collectors.toMap(lf -> lf.Lehrer_ID, lf -> lf));
		for (final DTOLehrer l : lehrer) {
			final LehrerStammdaten daten = dtoMapper.apply(l);
			final var tmpFoto = mapFotos.get(daten.id);
			daten.foto = (tmpFoto == null) ? null : tmpFoto.FotoBase64;
			daten.leitungsfunktionen.addAll(DataSchulleitung.getSchulleitungsfunktionen(conn, daten.id));
			result.add(daten);
		}
		return result;
	}

	/**
	 * Gibt die Liste der Stammdaten der Lehrer zurück, die sichtbar sind.
	 *
	 * @param conn	die Datenbank-Verbindung
	 *
	 * @return Liste der Stammdaten der sichtbaren Lehrer
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<LehrerStammdaten> getSichtbareLehrerStammdaten(final DBEntityManager conn) throws ApiOperationException {
		final var result = new ArrayList<LehrerStammdaten>();
		final List<DTOLehrer> lehrer = conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true);
		if ((lehrer == null) || lehrer.isEmpty())
			return result;
		final Map<Long, DTOLehrerFoto> mapFotos = conn.queryByKeyList(DTOLehrerFoto.class, lehrer.stream().map(l -> l.ID).toList())
				.stream().collect(Collectors.toMap(lf -> lf.Lehrer_ID, lf -> lf));
		for (final DTOLehrer l : lehrer) {
			final LehrerStammdaten daten = dtoMapper.apply(l);
			final var tmpFoto = mapFotos.get(daten.id);
			daten.foto = (tmpFoto == null) ? null : tmpFoto.FotoBase64;
			daten.leitungsfunktionen.addAll(DataSchulleitung.getSchulleitungsfunktionen(conn, daten.id));
			result.add(daten);
		}
		return result;
	}

	private final Map<String, DataBasicMapper<DTOLehrer>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, lehrer, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != lehrer.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("foto", (conn, lehrer, value, map) -> {
				final String strData = JSONMapper.convertToString(value, true, true, null);
				DTOLehrerFoto lehrerFoto = conn.queryByKey(DTOLehrerFoto.class, lehrer.ID);
				if (lehrerFoto == null)
					lehrerFoto = new DTOLehrerFoto(lehrer.ID);
				final String oldFoto = lehrerFoto.FotoBase64;
				if (((strData == null) && (oldFoto == null)) || ((strData != null) && (strData.equals(oldFoto))))
					return;
				lehrerFoto.FotoBase64 = strData;
				conn.transactionPersist(lehrerFoto);
			}),
			Map.entry("kuerzel",
					(conn, lehrer, value, map) -> lehrer.Kuerzel =
							JSONMapper.convertToString(value, false, false, Schema.tab_K_Lehrer.col_Kuerzel.datenlaenge())),
			Map.entry("personalTyp", (conn, lehrer, value, map) -> {
				final PersonalTyp p = PersonalTyp.fromKuerzel(JSONMapper.convertToString(value, false, false, null));
				if (p == null)
					throw new ApiOperationException(Status.CONFLICT);
				lehrer.PersonTyp = p;
			}),
			Map.entry("anrede",
					(conn, lehrer, value, map) -> lehrer.Kuerzel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Anrede.datenlaenge())),
			Map.entry("titel",
					(conn, lehrer, value, map) -> lehrer.Titel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Titel.datenlaenge())),
			Map.entry("amtsbezeichnung",
					(conn, lehrer, value, map) -> lehrer.Amtsbezeichnung =
							JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Amtsbezeichnung.datenlaenge())),
			Map.entry("nachname",
					(conn, lehrer, value, map) -> lehrer.Nachname =
							JSONMapper.convertToString(value, false, false, Schema.tab_K_Lehrer.col_Nachname.datenlaenge())),
			Map.entry("vorname",
					(conn, lehrer, value, map) -> lehrer.Vorname =
							JSONMapper.convertToString(value, false, false, Schema.tab_K_Lehrer.col_Vorname.datenlaenge())),
			Map.entry("geschlecht", (conn, lehrer, value, map) -> {
				final Geschlecht geschlecht = Geschlecht.fromValue(JSONMapper.convertToInteger(value, false));
				if (geschlecht == null)
					throw new ApiOperationException(Status.CONFLICT);
				lehrer.Geschlecht = geschlecht;
			}),
			Map.entry("geburtsdatum", (conn, lehrer, value, map) -> lehrer.Geburtsdatum = JSONMapper.convertToString(value, false, false, null)),  // TODO convertToDate im JSONMapper
			Map.entry("staatsangehoerigkeitID", (conn, lehrer, value, map) -> {
				final String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true, null);
				if ((staatsangehoerigkeitID == null) || (staatsangehoerigkeitID.isBlank())) {
					lehrer.staatsangehoerigkeit = null;
				} else {
					final Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
					if (nat == null)
						throw new ApiOperationException(Status.NOT_FOUND);
					lehrer.staatsangehoerigkeit = nat;
				}
			}),

			// Wohnort und Kontaktdaten
			Map.entry("strassenname", (conn, lehrer, value, map) -> lehrer.Strassenname =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Strassenname.datenlaenge())),
			Map.entry("hausnummer", (conn, lehrer, value, map) -> lehrer.HausNr =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_HausNr.datenlaenge())),
			Map.entry("hausnummerZusatz", (conn, lehrer, value, map) -> lehrer.HausNrZusatz =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_HausNrZusatz.datenlaenge())),
			Map.entry("wohnortID", (conn, lehrer, value, map) -> setWohnort(conn, lehrer, JSONMapper.convertToLong(value, true),
					(map.get("ortsteilID") == null) ? lehrer.Ortsteil_ID : ((Long) map.get("ortsteilID")))),
			Map.entry("ortsteilID", (conn, lehrer, value, map) -> setWohnort(conn, lehrer, (map.get("wohnortID") == null) ? lehrer.Ort_ID
					: ((Long) map.get("wohnortID")), JSONMapper.convertToLong(value, true))),
			Map.entry("telefon", (conn, lehrer, value, map) -> lehrer.telefon =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Tel.datenlaenge())),
			Map.entry("telefonMobil", (conn, lehrer, value, map) -> lehrer.telefonMobil =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Handy.datenlaenge())),
			Map.entry("emailDienstlich", (conn, lehrer, value, map) -> lehrer.eMailDienstlich =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_EmailDienstlich.datenlaenge())),
			Map.entry("emailPrivat", (conn, lehrer, value, map) -> lehrer.eMailPrivat =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_Email.datenlaenge())),

			// Sichtbarkeit und Statistik-Relevanz
			Map.entry("istSichtbar", (conn, lehrer, value, map) -> lehrer.Sichtbar = JSONMapper.convertToBoolean(value, false)),
			Map.entry("istRelevantFuerStatistik", (conn, lehrer, value, map) -> lehrer.statistikRelevant = JSONMapper.convertToBoolean(value, false)));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOLehrer.class, patchMappings);
	}


	/**
	 * Setzt den Wohnort bei den Lehrerdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
	 * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
	 *
	 * @param conn         die aktuelle Datenbankverbindung
	 * @param lehrer       das Lehrer-DTO der Datenbank
	 * @param wohnortID    die zu setzende Wohnort-ID
	 * @param ortsteilID   die zu setzende Ortsteil-ID
	 *
	 * @throws ApiOperationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
	 */
	private static void setWohnort(final DBEntityManager conn, final DTOLehrer lehrer, final Long wohnortID, final Long ortsteilID)
			throws ApiOperationException {
		if ((wohnortID != null) && (wohnortID < 0))
			throw new ApiOperationException(Status.CONFLICT);
		if ((ortsteilID != null) && (ortsteilID < 0))
			throw new ApiOperationException(Status.CONFLICT);
		lehrer.Ort_ID = wohnortID;
		// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		Long ortsteilIDNeu = ortsteilID;
		if (ortsteilIDNeu != null) {
			final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilIDNeu);
			if ((ortsteil == null) || ((ortsteil.Ort_ID != null) && (!ortsteil.Ort_ID.equals(wohnortID)))) {
				ortsteilIDNeu = null;
			}
		}
		lehrer.Ortsteil_ID = ortsteilIDNeu;
	}

}
