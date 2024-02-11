package de.svws_nrw.data.schule;

import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.core.types.schule.Religion;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ReligionEintrag}.
 */
public final class DataReligionen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ReligionEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataReligionen(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStatkueNationalitaeten} in einen Core-DTO {@link ReligionEintrag}.
	 */
	private static final Function<DTOKonfession, ReligionEintrag> dtoMapper = (final DTOKonfession k) -> {
		final ReligionEintrag daten = new ReligionEintrag();
		daten.id = k.ID;
		daten.text = k.Bezeichnung;
		daten.textZeugnis = k.ZeugnisBezeichnung;
		daten.kuerzel = k.StatistikKrz;
		daten.sortierung = k.Sortierung;
		daten.istSichtbar = k.Sichtbar;
		daten.istAenderbar = k.Aenderbar;
		return daten;
	};

	@Override
	public Response getAll() {
    	final var daten = getReligionen(conn);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die Liste der Religionen zurück.
	 * @return die Liste der Religionen
	 */
	public List<ReligionEintrag> getListReligionen() {
		if (this.conn != null)
			return getReligionen(this.conn);
		else
			return new ArrayList<>();
	}

	/**
	 * Gibt die Liste der Religionen zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 *
	 * @return die Liste der Religionen
	 */
	public static List<ReligionEintrag> getReligionen(final @NotNull DBEntityManager conn) {
    	final List<DTOKonfession> katalog = conn.queryAll(DTOKonfession.class);
    	if (katalog == null)
    		throw OperationError.NOT_FOUND.exception("Keine Religion gefunden.");
		final ArrayList<ReligionEintrag> daten = new ArrayList<>();
		for (final DTOKonfession r : katalog)
			daten.add(dtoMapper.apply(r));
		return daten;
	}


	@Override
	public Response getList() {
		return this.getAll();
	}


	/**
	 * Bestimmt die Religion anhand der angegeben ID.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param id     die ID des Religion-Katalog-Eintrags
	 *
	 * @return der Eintrag der Religion
	 */
	public static ReligionEintrag getReligion(final @NotNull DBEntityManager conn, final long id) {
		final DTOKonfession reli = conn.queryByKey(DTOKonfession.class, id);
		if (reli == null)
			throw OperationError.NOT_FOUND.exception();
		return dtoMapper.apply(reli);
	}


	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einer Religion mit der ID null ist unzulässig.");
		final ReligionEintrag daten = getReligion(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOKonfession>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("kuerzel", (conn, dto, value, map) -> {
			dto.StatistikKrz = JSONMapper.convertToString(value, true, true, Schema.tab_K_Religion.col_StatistikKrz.datenlaenge());
			if (dto.StatistikKrz != null) {
				final ReligionKatalogEintrag rke = Religion.getByKuerzel(dto.StatistikKrz).daten;
				if (rke == null)
					throw OperationError.NOT_FOUND.exception("Eine Religion mit dem  Kürzel " + dto.StatistikKrz + " existiert in der amtlichen Schulstatistik nicht.");
			}
		}),
		Map.entry("text", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Religion.col_Bezeichnung.datenlaenge())),
		Map.entry("textZeugnis", (conn, dto, value, map) -> dto.ZeugnisBezeichnung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Religion.col_ZeugnisBezeichnung.datenlaenge())),
		Map.entry("istSichtbar", (conn, dto, value, map) -> dto.Sichtbar = JSONMapper.convertToBoolean(value, true)),
		Map.entry("istAenderbar", (conn, dto, value, map) -> dto.Aenderbar = JSONMapper.convertToBoolean(value, true)),
		Map.entry("sortierung", (conn, dto, value, map) -> dto.Sortierung = JSONMapper.convertToInteger(value, true))
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOKonfession.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("kuerzel");

	private final ObjLongConsumer<DTOKonfession> initDTO = (dto, id) -> {
		dto.ID = id;
	};


	/**
	 * Erstellt eine neue Religion
	 *
	 * @param  is					JSON-Objekt mit den Daten
	 * @return Eine Response mit der neuen Religion
	 */
	public Response add(final InputStream is) {
		return super.addBasic(is, DTOKonfession.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Religion aus dem Katalog
	 *
	 * @param id   die ID des Religion-Katalog-Eintrags
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOKonfession.class, dtoMapper);
	}


	/**
	 * Löscht mehrere Religion-Katalog-Einträge
	 *
	 * @param ids   die IDs der Religion-Katalog-Einträge
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		return super.deleteBasicMultiple(ids, DTOKonfession.class, dtoMapper);
	}

}
