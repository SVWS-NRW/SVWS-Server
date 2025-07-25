package de.svws_nrw.data.gost.klausurplan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaumstunden;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link GostKlausurraumstunde}.
 */
public final class DataGostKlausurenRaumstunde extends DataManagerRevised<Long, DTOGostKlausurenRaumstunden, GostKlausurraumstunde> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link GostKlausurraumstunde}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenRaumstunde(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idRaum", "idZeitraster");
		super.setAttributesRequiredOnCreation("idRaum", "idZeitraster");
	}

	/**
	 * Gibt die Daten einer {@link GostKlausurraumstunde} zu deren ID zurück.
	 *
	 * @param id   Die ID der {@link GostKlausurraumstunde}.
	 *
	 * @return die Daten der {@link GostKlausurraumstunde} zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostKlausurraumstunde getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenRaumstunden klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOGostKlausurenRaumstunden} Objekt zur angegebenen ID.
	 *
	 * @param id ID der {@link DTOGostKlausurenRaumstunden}
	 *
	 * @return Ein {@link DTOGostKlausurenRaumstunden} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenRaumstunden getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die GostKlausurraumstunde darf nicht null sein.");

		final DTOGostKlausurenRaumstunden klasseDto = conn.queryByKey(DTOGostKlausurenRaumstunden.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine GostKlausurraumstunde zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected void initDTO(final DTOGostKlausurenRaumstunden dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected GostKlausurraumstunde map(final DTOGostKlausurenRaumstunden dto) throws ApiOperationException {
		final GostKlausurraumstunde daten = new GostKlausurraumstunde();
		daten.id = dto.ID;
		daten.idRaum = dto.Klausurraum_ID;
		daten.idZeitraster = dto.Zeitraster_ID;
		return daten;
	}

	/**
	 * Gibt die Liste der Klausurraumstunden zu einem Klausurtermin zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurraumstunde> getKlausurraumstundenZuTermin(final Long idTermin) throws ApiOperationException {
		final List<GostKlausurraum> listRaeume = new DataGostKlausurenRaum(conn).getKlausurraeumeZuTerminIDs(ListUtils.create1(idTermin));
		if (listRaeume.isEmpty())
			return new ArrayList<>();
		return getKlausurraumstundenZuRaeumen(listRaeume);
	}

	/**
	 * Gibt die Liste der Klausurraumstunden zu einer Liste von Räumen zurück.
	 *
	 * @param listRaeume die Liste der Klausurräume
	 *
	 * @return die Liste der Klausurraumstunden
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostKlausurraumstunde> getKlausurraumstundenZuRaeumen(final Collection<GostKlausurraum> listRaeume) throws ApiOperationException {
		if (listRaeume.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenRaumstunden> stunden = conn.queryList(DTOGostKlausurenRaumstunden.QUERY_LIST_BY_KLAUSURRAUM_ID,
				DTOGostKlausurenRaumstunden.class, listRaeume.stream().map(s -> s.id).toList());
		return mapList(stunden);
	}

	/**
	 * Gibt die Liste der Klausurraumstunden zu einer Raumid zurück.
	 *
	 * @param idRaum	 die ID des Raums
	 *
	 * @return die Liste der Klausurraumstunden
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostKlausurraumstunde> getKlausurraumstundenZuRaumid(final long idRaum) throws ApiOperationException {
		final List<DTOGostKlausurenRaumstunden> listKlausurraumstunden = conn.queryList(DTOGostKlausurenRaumstunden.QUERY_BY_KLAUSURRAUM_ID,
				DTOGostKlausurenRaumstunden.class, idRaum);
		return mapList(listKlausurraumstunden);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminraumstunden die Klausurraumstunden
	 *
	 * @param listSktrs die Liste der GostSchuelerklausurterminraumstunden
	 *
	 * @return die Liste der zugehörigen Klausurraumstunden-Objekte
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<GostKlausurraumstunde> getKlausurraumstundenZuSchuelerklausurterminraumstunden(final Collection<GostSchuelerklausurterminraumstunde> listSktrs) throws ApiOperationException {
		if (listSktrs.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenRaumstunden> sks = conn.queryByKeyList(DTOGostKlausurenRaumstunden.class,
				listSktrs.stream().map(sktrs -> sktrs.idRaumstunde).toList());
		return mapList(sks);
	}

	/**
	 * Ermittelt und löscht die nicht mehr benötigten Raumstunden (Aufräumen der
	 * Tabelle Gost_Klausuren_Raeume_Stunden)
	 *
	 * @return Liste der gelöschten Raumstunden
	 * @throws ApiOperationException im Fehlerfall
	 */
	List<GostKlausurraumstunde> removeRaumStundenInDb() throws ApiOperationException {
		final List<DTOGostKlausurenRaumstunden> stundenAlt = conn.queryList(
				"SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID NOT IN (SELECT w.Raumstunde_ID FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden w)",
				DTOGostKlausurenRaumstunden.class);
		conn.transactionRemoveAll(stundenAlt);
		return mapList(stundenAlt);
	}

}
