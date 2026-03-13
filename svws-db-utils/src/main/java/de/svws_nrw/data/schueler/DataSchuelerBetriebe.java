package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.asd.data.schueler.SchuelerBetriebe;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebeAnsprechpartner;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetrieb;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerBetrieb;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerBetriebe}.
 */
public final class DataSchuelerBetriebe extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerBetriebe}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerBetriebe(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerBetrieb}
	 * in einen Core-DTO {@link SchuelerBetriebe}.
	 */
	private final Function<DTOSchuelerBetrieb, SchuelerBetriebe> dtoMapper = (final DTOSchuelerBetrieb e) -> {
		final SchuelerBetriebe eintrag = new SchuelerBetriebe();
		eintrag.id = e.id;
		eintrag.idSchueler = e.idSchueler;
		eintrag.idBetrieb = e.idBetrieb;
		eintrag.idBeschaeftigungsart = e.idBeschaeftigungsart;
		eintrag.vertragsbeginn = e.vertragsbeginn;
		eintrag.vertragsende = e.vertragende;
		eintrag.nameAusbilder = e.nameAusbilder;
		eintrag.erhaeltAnschreiben = e.erhaeltAnschreiben;
		eintrag.istPraktikum = e.istPraktikum;
		eintrag.sortierung = e.sortierung;
		eintrag.idAnsprechpartner = e.idAnsprechpartner;
		eintrag.idBetreuungslehrer = e.idBetreuungslehrer;
		return eintrag;
	};

	@Override
	public Response getAll() throws ApiOperationException {
		final List<DTOSchuelerBetrieb> betriebe = conn.queryAll(DTOSchuelerBetrieb.class);
		if (betriebe == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<SchuelerBetriebe> daten = betriebe.stream().map(dtoMapper).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die erwartete ID zur Anfrage ist nicht vorhanden.");
		final DTOSchuelerBetrieb schuelerbetrieb = conn.queryByKey(DTOSchuelerBetrieb.class, id);
		if (schuelerbetrieb == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert mit der ID kein Schülerbetrieb.");
		final SchuelerBetriebe daten = dtoMapper.apply(schuelerbetrieb);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOSchuelerBetrieb s_betrieb = conn.queryByKey(DTOSchuelerBetrieb.class, id);
			if (s_betrieb == null)
				throw new ApiOperationException(Status.NOT_FOUND);
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.intValue() != id.intValue()))
							throw new ApiOperationException(Status.BAD_REQUEST);
					}
					case "idSchueler" -> {
						final Long schueler_id = JSONMapper.convertToLong(value, true);
						if (schueler_id == null)	//TODO Darf eine Beschäftigung ohne Betrieb angeleget werden?
							throw new ApiOperationException(Status.BAD_REQUEST, "SchülerID darf nicht fehlen.");
						final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
						if (schueler == null)
							throw new ApiOperationException(Status.NOT_FOUND, "Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");
						s_betrieb.idSchueler = schueler_id;
					}
					case "idBetrieb" -> {

						final Long betrieb_id = JSONMapper.convertToLong(value, true);
						if (betrieb_id == null)
							throw new ApiOperationException(Status.BAD_REQUEST, "Es muss eine ID für den Betrieb angegeben werden.");
						final DTOBetrieb betrieb = conn.queryByKey(DTOBetrieb.class, betrieb_id);
						if (betrieb == null)
							throw new ApiOperationException(Status.NOT_FOUND, "Betrieb mit der ID " + betrieb_id + " wurde nicht gefunden.");
						s_betrieb.idBetrieb = betrieb_id;
					}
					case "idBeschaeftigungsart" -> {
						final Long art_id = JSONMapper.convertToLong(value, true);
						if (art_id == null) {	//TODO Darf eine Beschäftigung ohne Art angeleget werden?
							s_betrieb.idBeschaeftigungsart = null;
						} else {
							final DTOBeschaeftigungsart b_art = conn.queryByKey(DTOBeschaeftigungsart.class, art_id);
							if (b_art == null)
								throw new ApiOperationException(Status.NOT_FOUND, "Beschäftigungsart mit der ID " + art_id + " wurde nicht gefunden.");
							s_betrieb.idBeschaeftigungsart = art_id;
						}
					}
					case "vertragsbeginn" -> s_betrieb.vertragsbeginn = JSONMapper.convertToString(value, true, true, null);
					case "vertragsende" -> s_betrieb.vertragende = JSONMapper.convertToString(value, true, true, null);
					case "nameAusbilder" -> s_betrieb.nameAusbilder = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler_AllgAdr.col_Ausbilder
							.datenlaenge());
					case "erhaeltAnschreiben" -> s_betrieb.erhaeltAnschreiben = JSONMapper.convertToBoolean(value, true);
					case "istPraktikum" -> s_betrieb.istPraktikum = JSONMapper.convertToBoolean(value, true);
					case "sortierung" -> s_betrieb.sortierung = JSONMapper.convertToInteger(value, true);
					case "idAnsprechpartner" -> {
						final Long a_id = JSONMapper.convertToLong(value, true);
						if (a_id == null) {	//TODO Darf eine Beschäftigung ohne Ansprechpartner angeleget werden?
							s_betrieb.idAnsprechpartner = null;
						} else {
							final DTOBetriebeAnsprechpartner ansprechpartner = conn.queryByKey(DTOBetriebeAnsprechpartner.class, a_id);
							if (ansprechpartner == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.idAnsprechpartner = a_id;
						}
					}
					case "idBetreuungslehrer" -> {
						final Long lehrer_id = JSONMapper.convertToLong(value, true);
						if (lehrer_id == null) {	//TODO Darf eine Beschäftigung ohne Betreuungslehrer angeleget werden?
							s_betrieb.idBetreuungslehrer = null;
						} else {
							final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrer_id);
							if (lehrer == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.idBetreuungslehrer = lehrer_id;
						}
					}
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(s_betrieb);
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Ermittelt eine Liste der {@link SchuelerBetriebe} für den Schüler mit der angegebenen ID.
	 *
	 * @param schuelerID   die ID des Schülers, dessen {@link SchuelerBetriebe    } ermittelt werden sollen
	 *
	 * @return eine Liste mit den {@link SchuelerBetriebe} für den Schüler mit der angegebenen ID
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response getListFromSchueler(final long schuelerID) throws ApiOperationException {
		final List<DTOSchuelerBetrieb> betriebe =
				conn.queryList(DTOSchuelerBetrieb.QUERY_BY_IDSCHUELER, DTOSchuelerBetrieb.class, schuelerID);
		if (betriebe == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<SchuelerBetriebe> daten = betriebe.stream().map(dtoMapper).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Erstellt einen neuen Schülerbetrieb
	 *
	 * @param schueler_id 	ID des Schülers, für den ein Schülerbetrieb erstellt wird.
	 * @param betrieb_id	ID des Betriebs
	 * @param is			das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Schülerbetrieb
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response create(final long schueler_id, final long betrieb_id, final InputStream is) throws ApiOperationException {
		DTOSchuelerBetrieb s_betrieb = null;
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
			if (schueler == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");

			final DTOBetrieb betrieb = conn.queryByKey(DTOBetrieb.class, betrieb_id);
			if (betrieb == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Betrieb mit der ID " + betrieb_id + " wurde nicht gefunden.");

			// Bestimme die ID des neuen Ansprechpartners
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Schueler_AllgAdr");
			final Long id = (lastID == null) ? 1 : (lastID.MaxID + 1);

			// Schülerbetrieb anlegen
			s_betrieb = new DTOSchuelerBetrieb(id, schueler_id, betrieb_id);

			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						// ignoriere eine angegebene ID
					}
					case "idSchueler" -> {
						final Long sid = JSONMapper.convertToLong(value, true);
						if (sid == null)
							throw new ApiOperationException(Status.BAD_REQUEST, "SchülerID darf nicht fehlen.");
						if (sid != schueler_id)
							throw new ApiOperationException(Status.BAD_REQUEST,
									"SchülerID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
					}
					case "idBetrieb" -> {
						final Long bid = JSONMapper.convertToLong(value, true);
						if ((bid == null) || (bid != betrieb_id))
							throw new ApiOperationException(Status.BAD_REQUEST,
									"Betrieb-ID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
					}
					case "idBeschaeftigungsart" -> {
						final Long art_id = JSONMapper.convertToLong(value, true);
						if (art_id == null) {	//TODO Darf eine Beschäftigung ohne Art angeleget werden?
							s_betrieb.idBeschaeftigungsart = null;
						} else {
							final DTOBeschaeftigungsart b_art = conn.queryByKey(DTOBeschaeftigungsart.class, art_id);
							if (b_art == null)
								throw new ApiOperationException(Status.NOT_FOUND, "Beschäftigungsart mit der ID " + art_id + " wurde nicht gefunden.");
							s_betrieb.idBeschaeftigungsart = art_id;
						}
					}
					case "vertragsbeginn" -> s_betrieb.vertragsbeginn = JSONMapper.convertToString(value, true, true, null);
					case "vertragsende" -> s_betrieb.vertragende = JSONMapper.convertToString(value, true, true, null);
					case "nameAusbilder" ->
						s_betrieb.nameAusbilder = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler_AllgAdr.col_Ausbilder.datenlaenge());
					case "erhaeltAnschreiben" -> s_betrieb.erhaeltAnschreiben = JSONMapper.convertToBoolean(value, true);
					case "istPraktikum" -> s_betrieb.istPraktikum = JSONMapper.convertToBoolean(value, true);
					case "sortierung" -> s_betrieb.sortierung = JSONMapper.convertToInteger(value, true);
					case "idAnsprechpartner" -> {
						final Long a_id = JSONMapper.convertToLong(value, true);
						if (a_id == null) {	//TODO Darf eine Beschäftigung ohne Ansprechpartner angeleget werden?
							s_betrieb.idAnsprechpartner = null;
						} else {
							final DTOBetriebeAnsprechpartner ansprechpartner = conn.queryByKey(DTOBetriebeAnsprechpartner.class, a_id);
							if (ansprechpartner == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.idAnsprechpartner = a_id;
						}
					}
					case "idBetreuungslehrer" -> {
						final Long lehrer_id = JSONMapper.convertToLong(value, true);
						if (lehrer_id == null) {	//TODO Darf eine Beschäftigung ohne Betreuungslehrer angeleget werden?
							s_betrieb.idBetreuungslehrer = null;
						} else {
							final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrer_id);
							if (lehrer == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.idBetreuungslehrer = lehrer_id;
						}
					}
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(s_betrieb);
		}
		final SchuelerBetriebe daten = dtoMapper.apply(s_betrieb);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}

