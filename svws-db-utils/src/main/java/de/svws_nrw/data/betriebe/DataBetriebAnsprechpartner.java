package de.svws_nrw.data.betriebe;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.betrieb.BetriebAnsprechpartner;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.schild.katalog.DTOAnsprechpartnerAllgemeineAdresse;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BetriebAnsprechpartner}.
 */

public final class DataBetriebAnsprechpartner extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BetriebAnsprechpartner}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebAnsprechpartner(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOAnsprechpartnerAllgemeineAdresse}
	 */
	private final Function<DTOAnsprechpartnerAllgemeineAdresse, BetriebAnsprechpartner> dtoMapper  = (final DTOAnsprechpartnerAllgemeineAdresse k) -> {
		final BetriebAnsprechpartner eintrag = new BetriebAnsprechpartner();
		eintrag.id = k.ID;
		eintrag.betrieb_id = k.Adresse_ID;
		eintrag.titel = k.Titel;
		eintrag.anrede = k.Anrede;
		eintrag.name = k.Name;
		eintrag.vorname = k.Vorname;
		eintrag.email = k.Email;
		eintrag.telefon = k.Telefon;
		eintrag.abteilung = k.Abteilung;
		eintrag.GU_ID = k.GU_ID;
		return eintrag;
	};

	@Override
	public Response getAll() throws ApiOperationException {
		final List<DTOAnsprechpartnerAllgemeineAdresse> katalog = conn.queryAll(DTOAnsprechpartnerAllgemeineAdresse.class);
		if (katalog == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<BetriebAnsprechpartner> daten = katalog.stream().map(dtoMapper).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
	}

	/**
	 * Liefert die Ansprechparnter des Betriebs mit der betrieb_id zurück
	 *
	 * @param  betrieb_id  ID des Betriebs
	 *
	 * @return Eine Response mit der Ansprechpartnerliste
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getBetriebansprechpartner(final Long betrieb_id) throws ApiOperationException {
		final List<DTOAnsprechpartnerAllgemeineAdresse> liste = conn.queryList(DTOAnsprechpartnerAllgemeineAdresse.QUERY_BY_ADRESSE_ID, DTOAnsprechpartnerAllgemeineAdresse.class, betrieb_id);
		if (liste == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<BetriebAnsprechpartner> daten = liste.stream().map(dtoMapper).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Methode erwartet einen Wert für id. Sie ist nicht übergeben!!");
		final DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, id);
		final BetriebAnsprechpartner daten = dtoMapper.apply(ansprechpartner);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			final DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, id);
			if (ansprechpartner == null)
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
					case "betrieb_id" -> {
						final Long betrieb_id = JSONMapper.convertToLong(value, true);
						if (betrieb_id == null)
							throw new ApiOperationException(Status.BAD_REQUEST);
						final DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
						if (betrieb == null)
							throw new ApiOperationException(Status.NOT_FOUND);
						ansprechpartner.Adresse_ID = betrieb.ID;
					}
					case "titel" -> ansprechpartner.Titel = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Titel.datenlaenge());
					case "anrede" -> ansprechpartner.Anrede = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Anrede.datenlaenge());
					case "name" -> ansprechpartner.Name = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Name.datenlaenge());
					case "vorname" -> ansprechpartner.Vorname = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Vorname.datenlaenge());
					case "email" -> ansprechpartner.Email = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Email.datenlaenge());
					case "telefon" -> ansprechpartner.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Telefon.datenlaenge());
					case "abteilung" -> ansprechpartner.Abteilung = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Abteilung.datenlaenge());
					case "GU_ID" -> ansprechpartner.GU_ID = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_GU_ID.datenlaenge());
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(ansprechpartner);
		}
		return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt einen neuen Ansprechpartner
	 *
	 * @param  betrieb_id  ID des Betriebs
	 * @param  is					JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Ansprechpartner
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response create(final Long betrieb_id, final InputStream is) throws ApiOperationException {
		DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = null;
		if (betrieb_id == null)
		    throw new ApiOperationException(Status.NOT_FOUND, "Parameter betrieb_id darf nicht leer sein.");
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			// Bestimme die ID des neuen Ansprechpartners
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "AllgAdrAnsprechpartner");
			final Long id = (lastID == null) ? 1 : lastID.MaxID + 1;
			final DTOKatalogAllgemeineAdresse betrieb = conn.queryByKey(DTOKatalogAllgemeineAdresse.class, betrieb_id);
			if (betrieb == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Ein Betrieb mit der ID " + betrieb_id + " existiert in der Datenbank nicht.");
			// Ansprechpartner anlegen
			ansprechpartner = new DTOAnsprechpartnerAllgemeineAdresse(id, betrieb_id);
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						// ignoriere die Angabe einer ID
					}
					case "betrieb_id" -> {
						final Long bid = JSONMapper.convertToLong(value, true);
						if (bid == null)
							throw new ApiOperationException(Status.BAD_REQUEST, "Betireb_ID darf nicht fehlen.");
						if (bid.longValue() != betrieb_id.longValue())
							throw new ApiOperationException(Status.BAD_REQUEST, "Betrieb_ID aus dem JSON-Objekt stimmt mit dem übergebenen Argument nicht überein.");
					}
					case "titel" -> ansprechpartner.Titel = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Titel.datenlaenge());
					case "anrede" -> ansprechpartner.Anrede = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Anrede.datenlaenge());
					case "name" -> ansprechpartner.Name = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Name.datenlaenge());
					case "vorname" -> ansprechpartner.Vorname = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Vorname.datenlaenge());
					case "email" -> ansprechpartner.Email = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Email.datenlaenge());
					case "telefon" -> ansprechpartner.Telefon = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Telefon.datenlaenge());
					case "abteilung" -> ansprechpartner.Abteilung = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_Abteilung.datenlaenge());
					case "GU_ID" -> ansprechpartner.GU_ID = JSONMapper.convertToString(value, true, true, Schema.tab_AllgAdrAnsprechpartner.col_GU_ID.datenlaenge());
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			conn.transactionPersist(ansprechpartner);
		}
		final BetriebAnsprechpartner daten = dtoMapper.apply(ansprechpartner);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	 /**
	 * Löscht die Betriebansprechpartner mit den IDs
	 *
	 * @param bids die IDs der Benutzer
	 *
	 * @return bei Erfolg eine HTTP-Response 200
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response remove(final List<Long> bids) throws ApiOperationException {
		final String strErrorAnsprechpartnerIDFehlt = "Der zu löschende Datensatz in DTOAnsprechpartnerAllgemeineAdresse mit der ID %d existiert nicht.";
		for (final Long id : bids) {
			final DTOAnsprechpartnerAllgemeineAdresse ansprechpartner = conn.queryByKey(DTOAnsprechpartnerAllgemeineAdresse.class, id);
			if (ansprechpartner == null)
				throw new ApiOperationException(Status.NOT_FOUND, strErrorAnsprechpartnerIDFehlt.formatted(id));
			conn.transactionRemove(ansprechpartner);
		}
		return Response.status(Status.OK).build();
	}

}
