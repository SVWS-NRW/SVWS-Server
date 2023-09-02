package de.svws_nrw.data.betriebe;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.betrieb.BetriebStammdaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAdressart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BetriebStammdaten}.
 */
public final class DataBetriebsStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BetriebStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebsStammdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogAllgemeineAdresse} in einen Core-DTO {@link BetriebStammdaten}.
	 */
	private final Function<DTOKatalogAllgemeineAdresse, BetriebStammdaten> dtoMapper = (final DTOKatalogAllgemeineAdresse betrieb) -> {
		final BetriebStammdaten daten = new BetriebStammdaten();
		daten.id = betrieb.ID;
		daten.adressArt = betrieb.adressArt;
		daten.name1 = betrieb.name1;
		daten.name2 = betrieb.name2;
		daten.strassenname = betrieb.strassenname;
		daten.hausnr = betrieb.hausnr;
		daten.hausnrzusatz = betrieb.hausnrzusatz;
		daten.ort_id = betrieb.ort_id;
		daten.telefon1 = betrieb.telefon1;
		daten.telefon2 = betrieb.telefon2;
		daten.fax = betrieb.fax;
		daten.email = betrieb.email;
		daten.bemerkungen = betrieb.bemerkungen;
		daten.sortierung = betrieb.sortierung;
		daten.ausbildungsbetrieb = betrieb.ausbildungsbetrieb;
		daten.bietetPraktika = betrieb.bietetPraktika;
		daten.branche = betrieb.branche;
		daten.zusatz1 = betrieb.zusatz1;
		daten.zusatz2 = betrieb.zusatz2;
		daten.Sichtbar = betrieb.Sichtbar;
		daten.Aenderbar = betrieb.Aenderbar;
		daten.BelehrungISG = betrieb.BelehrungISG;
		daten.Massnahmentraeger = betrieb.Massnahmentraeger;
		daten.GU_ID = betrieb.GU_ID;
		daten.ErwFuehrungszeugnis = betrieb.ErwFuehrungszeugnis;
		daten.ExtID = betrieb.ExtID;
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
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, id);
		if (betrieb == null)
			return OperationError.NOT_FOUND.getResponse();
		final BetriebStammdaten daten = dtoMapper.apply(betrieb);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Estellt einen neuen Betrieb
	 *
	 * @param is             das JSON-Objekt
	 *
	 * @return  die HTTP-Antwort mit der neuen Betriebsart
	 */
	public Response create(final InputStream is) {
		DTOKatalogAllgemeineAdresse betrieb = null;
		// Bestimme die ID des neuen Betriebs
		final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "K_AllgAdresse");
		final Long id = lastID == null ? 1 : lastID.MaxID + 1;

		// Betrieb anlegen
		betrieb = new DTOKatalogAllgemeineAdresse(id);
		return persistDTO(is, betrieb, null);
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		final DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, id);
		return persistDTO(is, betrieb, id);
	}

	/**
	 * Löscht die Betriebe mit den IDs
	 *
	 * @param bids die IDs der Betrieber
	 *
	 * @return bei Erfolg eine HTTP-Response 200
	 */
	public Response remove(final List<Long> bids) {
		final String strErrorBetriebIDFehlt = "Der zu löschende Datensatz in DTOKatalogAllgemeineAdresse mit der ID %d existiert nicht.";
		try {
			conn.transactionBegin();
			for (final Long id : bids) {
				final DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, id);
				if (betrieb == null)
					throw OperationError.NOT_FOUND.exception(strErrorBetriebIDFehlt.formatted(id));
				conn.transactionRemove(betrieb);
			}
			conn.transactionCommit();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webApplicationException)
				return webApplicationException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Liefert eine Liste der Stammdaten aller Betriebe, die einem Schüler zugeordnet sind. Bei dem
	 * Stammdaten wird ggf. ein ausgewählter Ansprechpartner des Schülers angegeben.
	 *
	 * @param schueler_id   die ID des Schülers
	 *
	 * @return die HTTP-Antwort mit den Stammdaten aller Betriebe, die dem Schüler zugeordnet sind.
	 */
	public Response getSchuelerBetriebe(final Long schueler_id) {
		if (schueler_id == null)
			return OperationError.NOT_FOUND.getResponse("Die Id des Schülers darf nicht leer sein.");

		final List<DTOKatalogAllgemeineAdresse> betriebe = conn.queryList("SELECT dtoa FROM DTOKatalogAllgemeineAdresse dtoa, DTOSchuelerAllgemeineAdresse dtos WHERE dtoa.ID=dtos.Adresse_ID and dtos.Schueler_ID = ?1 ", DTOKatalogAllgemeineAdresse.class, schueler_id);

		if (betriebe == null || betriebe.isEmpty())
			return OperationError.NOT_FOUND.getResponse("Schüler mit der ID" + schueler_id + " hat keine Betriebe");
		final List<BetriebStammdaten> daten = betriebe.stream().map(dtoMapper).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Erstellet eine DTO-Objekt aus dem JSON-Objekt und persistiert es in der Datanbenk.
	 *
	 * @param is            das JSON-Objekt
	 * @param betrieb   das neue oder bereits vorhandene DTO-Objekt
	 * @param id            die ID des DTO-Objekts bei einem Patch, null bei create
	 *
	 * @return die HTTP-Antwort mit dem neuen bzw. angepassten Betrieb.
	 */

	public Response persistDTO(final InputStream is, final DTOKatalogAllgemeineAdresse betrieb, final Long id) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			if (betrieb == null)
				throw OperationError.NOT_FOUND.exception();
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						if (id != null) {
							final Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
					}
					case "adressArt" -> {
						final Long adressartID = JSONMapper.convertToLong(value, true);
						if (adressartID == null) {
							betrieb.adressArt = null;
						} else {
							final DTOKatalogAdressart adressart = conn.queryByKey(DTOKatalogAdressart.class, adressartID);
							if (adressart == null)
								throw OperationError.NOT_FOUND.exception();
							betrieb.adressArt = adressartID;
						}
					}
					case "name1" -> betrieb.name1 = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrName1.datenlaenge());
					case "name2" -> betrieb.name2 = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrName2.datenlaenge());
					case "strassenname" -> betrieb.strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrStrassenname.datenlaenge());
					case "hausnr" -> betrieb.hausnr = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrHausNr.datenlaenge());
					case "hausnrzusatz" -> betrieb.hausnrzusatz = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrHausNrZusatz.datenlaenge());
					case "ort_id" -> setOrt(betrieb, JSONMapper.convertToLong(value, true), map.get("ortsteil_id") == null ? betrieb.ortsteil_id : ((Long) map.get("ortsteil_id")));
					case "ortsteil_id" -> setOrt(betrieb, map.get("ort_id") == null ? betrieb.ort_id : ((Long) map.get("ort_id")), JSONMapper.convertToLong(value, true));
					case "ansprechpartner" ->  System.out.println("TODO");  // TODO Ansprechpartner
					case "telefon1" -> betrieb.telefon1 = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrTelefon1.datenlaenge());
					case "telefon2" -> betrieb.telefon2 = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrTelefon2.datenlaenge());
					case "fax" -> betrieb.fax = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrFax.datenlaenge());
					case "email" -> betrieb.email = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrEmail.datenlaenge());
					case "bemerkungen" -> betrieb.bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrBemerkungen.datenlaenge());
					case "sortierung" -> betrieb.sortierung = JSONMapper.convertToInteger(value, true);
					case "ausbildungsbetrieb" -> betrieb.ausbildungsbetrieb = JSONMapper.convertToBoolean(value, true);
					case "bietetPraktika" -> betrieb.bietetPraktika = JSONMapper.convertToBoolean(value, true);
					case "branche" -> betrieb.branche = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrBranche.datenlaenge());
					case "zusatz1" -> betrieb.zusatz1 = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrZusatz1.datenlaenge());
					case "zusatz2" -> betrieb.zusatz2 = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_AllgAdrZusatz2.datenlaenge());
					case "Sichtbar" -> betrieb.Sichtbar = JSONMapper.convertToBoolean(value, true);
					case "Aenderbar" -> betrieb.Aenderbar = JSONMapper.convertToBoolean(value, true);
					case "Massnahmentraeger" -> betrieb.Massnahmentraeger = JSONMapper.convertToBoolean(value, true);
					case "BelehrungISG" -> betrieb.BelehrungISG = JSONMapper.convertToBoolean(value, true);
					case "GU_ID" -> betrieb.GU_ID = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_GU_ID.datenlaenge());
					case "ErwFuehrungszeugnis" -> betrieb.ErwFuehrungszeugnis = JSONMapper.convertToBoolean(value, true);
					case "ExtID" -> betrieb.ExtID = JSONMapper.convertToString(value, true, true, Schema.tab_K_AllgAdresse.col_ExtID.datenlaenge());

					default -> throw OperationError.BAD_REQUEST.exception();
				}
			}
			conn.transactionPersist(betrieb);
		}
		if (id != null)
			return Response.status(Status.OK).build();
		final BetriebStammdaten daten = dtoMapper.apply(betrieb);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Setzt den Ort bei dem Betrieb und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
	 * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
	 *
	 * @param betrieb      das Betriebs-DTO der Datenbank
	 * @param ortID    die zu setzende Ort-ID
	 * @param ortsteilID   die zu setzende Ortsteil-ID
	 *
	 * @throws WebApplicationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
	 */
	private void setOrt(final DTOKatalogAllgemeineAdresse betrieb, final Long ortID, final Long ortsteilID) throws WebApplicationException {
		if ((ortID != null) && (ortID < 0))
			throw OperationError.CONFLICT.exception();
		if ((ortsteilID != null) && (ortsteilID < 0))
			throw OperationError.CONFLICT.exception();
		betrieb.ort_id = ortID;
		// Prüfe, ob die Ortsteil ID in Bezug auf die ortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		Long ortsteilIDNeu = ortsteilID;
		if (ortsteilIDNeu != null) {
			final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilIDNeu);
			if ((ortsteil == null) || ((ortsteil.Ort_ID != null) && (!ortsteil.Ort_ID.equals(ortID)))) {
				ortsteilIDNeu = null;
			}
		}
		betrieb.ortsteil_id = ortsteilIDNeu;
	}

}
