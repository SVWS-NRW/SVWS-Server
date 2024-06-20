package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.schueler.SchuelerBetriebsdaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOAnsprechpartnerAllgemeineAdresse;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAllgemeineAdresse;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerBetriebsdaten}.
 */
public final class DataSchuelerBetriebsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerBetriebsdaten}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerBetriebsdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerAllgemeineAdresse}
	 * in einen Core-DTO {@link SchuelerBetriebsdaten}.
	 */
	private final Function<DTOSchuelerAllgemeineAdresse, SchuelerBetriebsdaten> dtoMapper = (final DTOSchuelerAllgemeineAdresse e) -> {
		final SchuelerBetriebsdaten eintrag = new SchuelerBetriebsdaten();
		eintrag.id = e.ID;
		eintrag.schueler_id = e.Schueler_ID;
		eintrag.betrieb_id = e.Adresse_ID;
		eintrag.beschaeftigungsart_id = e.Vertragsart_ID;
		eintrag.vertragsbeginn = e.Vertragsbeginn;
		eintrag.vertragsende = e.Vertragsende;
		eintrag.ausbilder = e.Ausbilder;
		eintrag.allgadranschreiben = e.AllgAdrAnschreiben;
		eintrag.praktikum = e.Praktikum;
		eintrag.sortierung = e.Sortierung;
		eintrag.ansprechpartner_id = e.Ansprechpartner_ID;
		eintrag.betreuungslehrer_id = e.Betreuungslehrer_ID;
		return eintrag;
	};

	@Override
	public Response getAll() throws ApiOperationException {
		final List<DTOSchuelerAllgemeineAdresse> betriebe = conn.queryAll(DTOSchuelerAllgemeineAdresse.class);
		if (betriebe == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<SchuelerBetriebsdaten> daten = betriebe.stream().map(dtoMapper).toList();
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
		final DTOSchuelerAllgemeineAdresse schuelerbetrieb = conn.queryByKey(DTOSchuelerAllgemeineAdresse.class, id);
		if (schuelerbetrieb == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert mit der ID kein Schülerbetrieb.");
		final SchuelerBetriebsdaten daten = dtoMapper.apply(schuelerbetrieb);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOSchuelerAllgemeineAdresse s_betrieb = conn.queryByKey(DTOSchuelerAllgemeineAdresse.class, id);
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
					case "schueler_id" -> {
						final Long schueler_id = JSONMapper.convertToLong(value, true);
						if (schueler_id == null)	//TODO Darf eine Beschäftigung ohne Betrieb angeleget werden?
							throw new ApiOperationException(Status.BAD_REQUEST, "SchülerID darf nicht fehlen.");
						final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
						if (schueler == null)
							throw new ApiOperationException(Status.NOT_FOUND, "Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");
						s_betrieb.Schueler_ID = schueler_id;
					}
					case "betrieb_id" -> {

						final Long betrieb_id = JSONMapper.convertToLong(value, true);
						if (betrieb_id == null)
							throw new ApiOperationException(Status.BAD_REQUEST, "Es muss eine ID für den Betrieb angegeben werden.");
						final DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
						if (betrieb == null)
							throw new ApiOperationException(Status.NOT_FOUND, "Betrieb mit der ID " + betrieb_id + " wurde nicht gefunden.");
						s_betrieb.Adresse_ID = betrieb_id;
					}
					case "beschaeftigungsart_id" -> {
						final Long art_id = JSONMapper.convertToLong(value, true);
						if (art_id == null) {	//TODO Darf eine Beschäftigung ohne Art angeleget werden?
							s_betrieb.Vertragsart_ID = null;
						} else {
							final DTOBeschaeftigungsart b_art = conn.queryByKey(DTOBeschaeftigungsart.class, art_id);
							if (b_art == null)
								throw new ApiOperationException(Status.NOT_FOUND, "Beschäftigungsart mit der ID " + art_id + " wurde nicht gefunden.");
							s_betrieb.Vertragsart_ID = art_id;
						}
					}
					case "vertragsbeginn" -> s_betrieb.Vertragsbeginn = JSONMapper.convertToString(value, true, true, null);
					case "vertragsende" -> s_betrieb.Vertragsende = JSONMapper.convertToString(value, true, true, null);
					case "ausbilder" -> s_betrieb.Ausbilder = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler_AllgAdr.col_Ausbilder
							.datenlaenge());
					case "allgadranschreiben" -> s_betrieb.AllgAdrAnschreiben = JSONMapper.convertToBoolean(value, true);
					case "praktikum" -> s_betrieb.Praktikum = JSONMapper.convertToBoolean(value, true);
					case "sortierung" -> s_betrieb.Sortierung = JSONMapper.convertToInteger(value, true);
					case "ansprechpartner_id" -> {
						final Long a_id = JSONMapper.convertToLong(value, true);
						if (a_id == null) {	//TODO Darf eine Beschäftigung ohne Ansprechpartner angeleget werden?
							s_betrieb.Ansprechpartner_ID = null;
						} else {
							final DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, a_id);
							if (ansprechpartner == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.Ansprechpartner_ID = a_id;
						}
					}
					case "betreuungslehrer_id" -> {
						final Long lehrer_id = JSONMapper.convertToLong(value, true);
						if (lehrer_id == null) {	//TODO Darf eine Beschäftigung ohne Betreuungslehrer angeleget werden?
							s_betrieb.Betreuungslehrer_ID = null;
						} else {
							final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrer_id);
							if (lehrer == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.Betreuungslehrer_ID = lehrer_id;
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
	 * Ermittelt eine Liste der {@link SchuelerBetriebsdaten} für den Schüler mit der angegebenen ID.
	 *
	 * @param schuelerID   die ID des Schülers, dessen {@link SchuelerBetriebsdaten	} ermittelt werden sollen
	 *
	 * @return eine Liste mit den {@link SchuelerBetriebsdaten} für den Schüler mit der angegebenen ID
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response getListFromSchueler(final long schuelerID) throws ApiOperationException {
		final List<DTOSchuelerAllgemeineAdresse> betriebe =
				conn.queryList(DTOSchuelerAllgemeineAdresse.QUERY_BY_SCHUELER_ID, DTOSchuelerAllgemeineAdresse.class, schuelerID);
		if (betriebe == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<SchuelerBetriebsdaten> daten = betriebe.stream().map(dtoMapper).toList();
		/*
		// TODO Isak später bitte entfernen
		// Bestimme die Namen aller Betriebe, die bei den Schüler-Betriebsdaten vorkommen
		List<Long> betriebIDs = daten.stream().filter(sbd -> sbd.betrieb_id != null).map(sbd -> sbd.betrieb_id).toList();
		Map<Long, String> mapBetriebeIDName = (betriebIDs.size() <= 0) ? new HashMap<>() :
			conn.queryNamed("DTOKatalogAllgemeineAdresse.id.multiple", betriebIDs, DTOKatalogAllgemeineAdresse.class).stream()
				.collect(Collectors.toMap(b -> b.ID, b -> b.name1));
		// Bestimme die Namen aller Ansprechpartner, die bei den Schüler-Betriebsdaten vorkommen
		List<Long> ansprechpartnerIDs = daten.stream().filter(sbd -> sbd.ansprechpartner_id != null).map(sbd -> sbd.ansprechpartner_id).toList();
		Map<Long, String> mapAnsprechpartnerIDName = (ansprechpartnerIDs.size() <= 0) ? new HashMap<>() :
			conn.queryNamed("DTOAnsprechpartnerAllgemeineAdresse.id.multiple", ansprechpartnerIDs, DTOAnsprechpartnerAllgemeineAdresse.class).stream()
				.collect(Collectors.toMap(b -> b.ID, b -> b.Name));
		// Bestimme die Namen aller Betreuungslehrer, die bei den Schüler-Betriebsdaten vorkommen
		List<Long> betreuungslehrerIDs = daten.stream().filter(sbd -> sbd.betreuungslehrer_id != null).map(sbd -> sbd.betreuungslehrer_id).toList();
		Map<Long, String> mapBetreuungslehrerIDName = (betreuungslehrerIDs.size() <= 0) ? new HashMap<>() :
			conn.queryNamed("DTOLehrer.id.multiple", betreuungslehrerIDs, DTOLehrer.class).stream()
				.collect(Collectors.toMap(b -> b.ID, b -> b.Nachname));
		// Setze nun alle Namen, welche für die obigen IDs bestimmt wurden
		for (SchuelerBetriebsdaten sbd : daten) {
			if (sbd.id != null)
				sbd.betrieb_name = mapBetriebeIDName.get(sbd.betrieb_id);
			if (sbd.ansprechpartner_id != null)
				sbd.ansprechpartner_name = mapAnsprechpartnerIDName.get(sbd.ansprechpartner_id);
			if (sbd.betreuungslehrer_id != null)
				sbd.betreuungslehrer_name = mapBetreuungslehrerIDName.get(sbd.betreuungslehrer_id);
		}

		*/
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
		DTOSchuelerAllgemeineAdresse s_betrieb = null;
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
			if (schueler == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");

			final DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
			if (betrieb == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Betrieb mit der ID " + betrieb_id + " wurde nicht gefunden.");

			// Bestimme die ID des neuen Ansprechpartners
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Schueler_AllgAdr");
			final Long id = (lastID == null) ? 1 : (lastID.MaxID + 1);

			// Schülerbetrieb anlegen
			s_betrieb = new DTOSchuelerAllgemeineAdresse(id, schueler_id, betrieb_id);

			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						// ignoriere eine angegebene ID
					}
					case "schueler_id" -> {
						final Long sid = JSONMapper.convertToLong(value, true);
						if (sid == null)
							throw new ApiOperationException(Status.BAD_REQUEST, "SchülerID darf nicht fehlen.");
						if (sid != schueler_id)
							throw new ApiOperationException(Status.BAD_REQUEST,
									"SchülerID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
					}
					case "betrieb_id" -> {
						final Long bid = JSONMapper.convertToLong(value, true);
						if ((bid == null) || (bid != betrieb_id))
							throw new ApiOperationException(Status.BAD_REQUEST,
									"Betrieb-ID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
					}
					case "beschaeftigungsart_id" -> {
						final Long art_id = JSONMapper.convertToLong(value, true);
						if (art_id == null) {	//TODO Darf eine Beschäftigung ohne Art angeleget werden?
							s_betrieb.Vertragsart_ID = null;
						} else {
							final DTOBeschaeftigungsart b_art = conn.queryByKey(DTOBeschaeftigungsart.class, art_id);
							if (b_art == null)
								throw new ApiOperationException(Status.NOT_FOUND, "Beschäftigungsart mit der ID " + art_id + " wurde nicht gefunden.");
							s_betrieb.Vertragsart_ID = art_id;
						}
					}
					case "vertragsbeginn" -> s_betrieb.Vertragsbeginn = JSONMapper.convertToString(value, true, true, null);
					case "vertragsende" -> s_betrieb.Vertragsende = JSONMapper.convertToString(value, true, true, null);
					case "ausbilder" ->
						s_betrieb.Ausbilder = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler_AllgAdr.col_Ausbilder.datenlaenge());
					case "allgadranschreiben" -> s_betrieb.AllgAdrAnschreiben = JSONMapper.convertToBoolean(value, true);
					case "praktikum" -> s_betrieb.Praktikum = JSONMapper.convertToBoolean(value, true);
					case "sortierung" -> s_betrieb.Sortierung = JSONMapper.convertToInteger(value, true);
					case "ansprechpartner_id" -> {
						final Long a_id = JSONMapper.convertToLong(value, true);
						if (a_id == null) {	//TODO Darf eine Beschäftigung ohne Ansprechpartner angeleget werden?
							s_betrieb.Ansprechpartner_ID = null;
						} else {
							final DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, a_id);
							if (ansprechpartner == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.Ansprechpartner_ID = a_id;
						}
					}
					case "betreuungslehrer_id" -> {
						final Long lehrer_id = JSONMapper.convertToLong(value, true);
						if (lehrer_id == null) {	//TODO Darf eine Beschäftigung ohne Betreuungslehrer angeleget werden?
							s_betrieb.Betreuungslehrer_ID = null;
						} else {
							final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrer_id);
							if (lehrer == null)
								throw new ApiOperationException(Status.NOT_FOUND);
							s_betrieb.Betreuungslehrer_ID = lehrer_id;
						}
					}
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(s_betrieb);
		}
		final SchuelerBetriebsdaten daten = dtoMapper.apply(s_betrieb);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}

