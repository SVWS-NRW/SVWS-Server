package de.svws_nrw.data.schueler;

import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.data.schueler.SchuelerVermerke;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;

import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerVermerke}.
 */
public final class DataSchuelerVermerke extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerVermerke}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerVermerke(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerVermerke}
	 * in einen Core-DTO {@link SchuelerVermerke}.
	 */
	private final Function<DTOSchuelerVermerke, SchuelerVermerke> dtoMapper = (final DTOSchuelerVermerke e) -> {
		final SchuelerVermerke eintrag = new SchuelerVermerke();

		eintrag.id = e.ID;
		eintrag.schueler_id = e.Schueler_ID;
		eintrag.VermerkArt_ID = e.VermerkArt_ID;
		eintrag.Datum = e.Datum;
		eintrag.Bemerkung = e.Bemerkung;
		eintrag.AngelegtVon = e.AngelegtVon;
		eintrag.GeaendertVon = e.GeaendertVon;

		return eintrag;
	};

	@Override
	public Response getAll() throws ApiOperationException {
		final List<DTOSchuelerVermerke> vermerke = conn.queryAll(DTOSchuelerVermerke.class);
		if (vermerke == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<SchuelerVermerke> daten = vermerke.stream().map(dtoMapper).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt getBySchuelerID als Response zurück
	 *
	 * @param aLong SchuelerID
	 *
	 * @return getBySchuelerID als Response
	 */
	public Response getBySchuelerIDasResponse(final Long aLong) {

		try {
			List<SchuelerVermerke> daten = this.getBySchuelerID(aLong);
			if (daten == null || daten.isEmpty())
				daten = new ArrayList<>();
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (@SuppressWarnings("unused") final Exception e) {
			System.out.println(e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * Gibt alle Vermerke zum Schüler zurück
	 *
	 * @param aLong SchuelerID
	 *
	 * @return Liste von Vermerke passend zum Schüler
	 */
	public List<SchuelerVermerke> getBySchuelerID(final Long aLong) {

		System.out.println(DTOSchuelerVermerke.class);
		final List<DTOSchuelerVermerke> daten = conn.queryNamed("DTOSchuelerVermerke.schueler_id", aLong, DTOSchuelerVermerke.class);
		return daten.stream().map(mapSchuelerVermerkeDaten).toList();
	}

	public static final Function<DTOSchuelerVermerke, SchuelerVermerke> mapSchuelerVermerkeDaten = (final DTOSchuelerVermerke schuelerVermerkeDaten) -> {
		final SchuelerVermerke result = new SchuelerVermerke();
		result.id = schuelerVermerkeDaten.ID;
		result.schueler_id = schuelerVermerkeDaten.Schueler_ID;

		result.VermerkArt_ID = schuelerVermerkeDaten.VermerkArt_ID;

		result.Datum = schuelerVermerkeDaten.Datum;
		result.AngelegtVon = schuelerVermerkeDaten.AngelegtVon;
		result.GeaendertVon = schuelerVermerkeDaten.GeaendertVon;
		result.Bemerkung = schuelerVermerkeDaten.Bemerkung;
		return result;
	};



	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die erwartete ID zur Anfrage ist nicht vorhanden.");
		final DTOSchuelerVermerke schuelerVermerk = conn.queryByKey(DTOSchuelerVermerke.class, id);
		if (schuelerVermerk == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert mit der ID kein Vermerk.");
		final SchuelerVermerke daten = dtoMapper.apply(schuelerVermerk);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {

		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty()) {
			return Response.status(Status.OK).build();
		}

		final DTOSchuelerVermerke vermerk = conn.queryByKey(DTOSchuelerVermerke.class, id);
		if (vermerk == null)
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
					if (schueler_id == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "SchülerID darf nicht fehlen.");
					final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
					if (schueler == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");
					vermerk.Schueler_ID = schueler_id;
				}
				case "VermerkArt_ID" -> {

					final Long VermerkArt_ID = JSONMapper.convertToLong(value, true);
					if (VermerkArt_ID == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Es muss eine ID für den Betrieb angegeben werden.");
					final DTOVermerkArt betrieb = conn.queryByKey(DTOVermerkArt.class, VermerkArt_ID);
					if (betrieb == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Betrieb mit der ID " + VermerkArt_ID + " wurde nicht gefunden.");
					vermerk.VermerkArt_ID = VermerkArt_ID;
				}
				case "Bemerkung" -> vermerk.Bemerkung = JSONMapper.convertToString(value, true, true, null);
				case "AngelegtVon" -> vermerk.AngelegtVon = JSONMapper.convertToString(value, true, true, null);
				case "GeaendertVon" -> vermerk.GeaendertVon = JSONMapper.convertToString(value, true, true, null);

				default -> throw new ApiOperationException(Status.BAD_REQUEST);
			}
		}
		conn.transactionPersist(vermerk);

		return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt einen neuen Schülerbetrieb
	 *
	 * @param schueler_id 		ID des Schülers, für den ein Schülerbetrieb erstellt wird.
	 * @param vermerkArt_id		ID des Vermerkes
	 * @param is				das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Schülerbetrieb
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response create(final long schueler_id, final long vermerkArt_id, final InputStream is) throws ApiOperationException {
		DTOSchuelerVermerke s_vermerk = null;
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
			if (schueler == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");

			final DTOVermerkArt betrieb = conn.queryByKey(DTOVermerkArt.class, vermerkArt_id);
			if (betrieb == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Vermerkart mit der ID " + vermerkArt_id + " wurde nicht gefunden.");

			// Bestimme die ID des neuen Ansprechpartners
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "SchuelerVermerke");
			final Long id = lastID == null ? 1 : lastID.MaxID + 1;

			// Schülerbetrieb anlegen
			s_vermerk = new DTOSchuelerVermerke(id, schueler_id);

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
							throw new ApiOperationException(Status.BAD_REQUEST, "SchülerID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
					}
					case "vermerkArt_id" -> {
						final Long bid = JSONMapper.convertToLong(value, true);
						if ((bid == null) || (bid != vermerkArt_id))
							throw new ApiOperationException(Status.BAD_REQUEST, "VermerkArt_id aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
					}
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(s_vermerk);
		}
		final SchuelerVermerke daten = dtoMapper.apply(s_vermerk);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}

