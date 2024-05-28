package de.svws_nrw.data.schueler;

import de.svws_nrw.core.data.schueler.SchuelerVermerke;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;

import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
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
	 * Lambda-Ausdruck zum Umwandeln des Vermerkes in eines Datenbank-DTOs {@link DTOSchuelerVermerke}
	 * in einen Core-DTO {@link SchuelerVermerke}.
	 */
	public static final Function<DTOSchuelerVermerke, SchuelerVermerke> dtoMapper = (final DTOSchuelerVermerke e) -> {
		final SchuelerVermerke vermerk = new SchuelerVermerke();

		vermerk.id = e.ID;
		vermerk.schueler_id = e.Schueler_ID;
		vermerk.VermerkArt_ID = e.VermerkArt_ID;
		vermerk.Datum = e.Datum;
		vermerk.Bemerkung = e.Bemerkung;
		vermerk.AngelegtVon = e.AngelegtVon;
		vermerk.GeaendertVon = e.GeaendertVon;

		return vermerk;
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
		return daten.reversed().stream().map(dtoMapper).toList();
	}

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
				}
				case "VermerkArt_ID" -> {
					final Long vermerkArt_ID = JSONMapper.convertToLong(value, true);
					if (vermerkArt_ID == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Es muss eine ID für die Vermerkart angegeben werden.");
					final DTOVermerkArt betrieb = conn.queryByKey(DTOVermerkArt.class, vermerkArt_ID);
					if (betrieb == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Vermerkart mit der ID " + vermerkArt_ID + " wurde nicht gefunden.");
					vermerk.VermerkArt_ID = vermerkArt_ID;
				}
				case "Bemerkung" -> vermerk.Bemerkung = JSONMapper.convertToString(value, true, true, null);

				default -> throw new ApiOperationException(Status.BAD_REQUEST);
			}
		}

		// Aktualisier für die Bearbeitung (Patch) den Autor und das Datum
		vermerk.Datum = String.valueOf(Date.valueOf(LocalDate.now()));
		vermerk.GeaendertVon = this.conn.getUser().getUsername();

		conn.transactionPersist(vermerk);

		return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt einen neuen Schülervermerk
	 *
	 * @param schueler_id 		ID des Schülers, für den ein Vermerk erstellt wird.
	 *
	 * @return Eine Response mit dem neuen Schülerbetrieb
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response create(final long schueler_id) throws ApiOperationException {
		DTOSchuelerVermerke s_vermerk = null;
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Schüler mit der ID " + schueler_id + " wurde nicht gefunden.");

		// Bestimme die ID des neuen Vermerks
		final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "SchuelerVermerke");
		final long id = lastID == null ? 1 : lastID.MaxID + 1;

		// Vermerk anlegen
		s_vermerk = new DTOSchuelerVermerke(id, schueler_id);
		s_vermerk.VermerkArt_ID = 1L; // Neuer Vermerkart ist vom Typ 'Allgemeiner Vermerk'
		s_vermerk.AngelegtVon = this.conn.getUser().getUsername(); // Erhalte den aktuellen Autor für den neuen Vermerk
		s_vermerk.Bemerkung = "";
		s_vermerk.Datum = String.valueOf(Date.valueOf(LocalDate.now())); // Erhalte die aktuelle Zeit für den neuen Vermerk

		conn.transactionPersist(s_vermerk);

		final SchuelerVermerke daten = dtoMapper.apply(s_vermerk);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Löscht einen Vermerk
	 *
	 * @param idVermerk   die ID des Vermerks
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long idVermerk) throws ApiOperationException {
		// Erhalte Delete Kandidaten mit der übermittelten ID
		final DTOSchuelerVermerke deleteCandidate = conn.queryByKey(DTOSchuelerVermerke.class, idVermerk);
		final SchuelerVermerke daten = dtoMapper.apply(deleteCandidate);
		// Entferne den Vermerk
		conn.transactionRemove(deleteCandidate);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}

