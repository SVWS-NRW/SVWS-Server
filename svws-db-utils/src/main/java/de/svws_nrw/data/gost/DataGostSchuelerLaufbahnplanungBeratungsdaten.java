package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostLaufbahnplanungBeratungsdaten}.
 */
public final class DataGostSchuelerLaufbahnplanungBeratungsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostLaufbahnplanungBeratungsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostSchuelerLaufbahnplanungBeratungsdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostSchueler} in einen Core-DTO {@link StundenplanLehrer}.
	 */
	private static final Function<DTOGostSchueler, GostLaufbahnplanungBeratungsdaten> dtoMapper = (final DTOGostSchueler dto) -> {
		final GostLaufbahnplanungBeratungsdaten daten = new GostLaufbahnplanungBeratungsdaten();
		daten.beratungslehrerID = dto.Beratungslehrer_ID;
		daten.beratungsdatum = dto.DatumBeratung;
		daten.kommentar = dto.Kommentar;
		daten.ruecklaufdatum = dto.DatumRuecklauf;
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
	public Response get(final Long schueler_id) throws ApiOperationException {
		final GostLaufbahnplanungBeratungsdaten daten = getFromID(schueler_id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Gibt die Beratungsdaten eines Schülers zur GostLaufbahnplanung zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die GostLaufbahnplanungBeratungsdaten des Schülers.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostLaufbahnplanungBeratungsdaten getFromID(final Long idSchueler) throws ApiOperationException {
		if (idSchueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Schüler-ID übergeben.");
		}
		DBUtilsGost.pruefeSchuleMitGOSt(conn);

		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Schüler mit der ID %d gefunden.".formatted(idSchueler));
		}

		DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, idSchueler);
		if (gostSchueler == null) {
			gostSchueler = new DTOGostSchueler(idSchueler, false);
			conn.transactionPersist(gostSchueler);
		}
		return dtoMapper.apply(gostSchueler);
	}

	/**
	 * Gibt die Beratungsdaten zu mehreren Schülern zur GostLaufbahnplanung zurück.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die GostLaufbahnplanungBeratungsdaten der Schüler.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> getMapFromIDs(final List<Long> idsSchueler) throws ApiOperationException {
		if (idsSchueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Schüler-IDs übergeben.");
		}
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Set<Long> tmp = idsSchueler.stream().filter(Objects::nonNull).collect(Collectors.toSet());
		final List<DTOSchueler> schueler = conn.queryByKeyList(DTOSchueler.class, tmp);
		if (schueler.size() != tmp.size()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden Schüler-IDs übergeben, die nicht gültig sind.");
		}

		final Map<Long, DTOGostSchueler> mapGostSchueler = conn.queryList(DTOGostSchueler.QUERY_LIST_BY_SCHUELER_ID, DTOGostSchueler.class, idsSchueler)
				.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));
		if (mapGostSchueler.size() != tmp.size()) {
			tmp.removeAll(mapGostSchueler.keySet());
			for (final Long idSchueler : tmp) {
				final DTOGostSchueler gostSchueler = new DTOGostSchueler(idSchueler, false);
				conn.transactionPersist(gostSchueler);
				mapGostSchueler.put(idSchueler, gostSchueler);
			}
		}
		conn.transactionFlush();

		final Map<Long, GostLaufbahnplanungBeratungsdaten> result = new HashMap<>();
		for (final Long sID : idsSchueler) {
			final var gostSchueler = mapGostSchueler.get(sID);
			if (gostSchueler == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
			}
			result.put(sID, dtoMapper.apply(gostSchueler));
		}
		return result;
	}



	@Override
	public Response patch(final Long schueler_id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (!map.isEmpty()) {
			final DTOGostSchueler gostSchueler = conn.queryByKey(DTOGostSchueler.class, schueler_id);
			if (gostSchueler == null) {
				throw new ApiOperationException(Status.NOT_FOUND);
			}
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "beratungslehrerID" -> {
						final Long beratungslehrerID = JSONMapper.convertToLong(value, true);
						if (beratungslehrerID != null) {
							final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, beratungslehrerID);
							if (lehrer == null) {
								throw new ApiOperationException(Status.CONFLICT);
							}
						}
						gostSchueler.Beratungslehrer_ID = beratungslehrerID;
					}
					case "beratungsdatum" -> gostSchueler.DatumBeratung = JSONMapper.convertToString(value, true, false, null);
					case "kommentar" ->
						gostSchueler.Kommentar = JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Schueler.col_Kommentar.datenlaenge());
					case "ruecklaufdatum" -> gostSchueler.DatumRuecklauf = JSONMapper.convertToString(value, true, false, null);
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(gostSchueler);
		}
		return Response.status(Status.OK).build();
	}


}
