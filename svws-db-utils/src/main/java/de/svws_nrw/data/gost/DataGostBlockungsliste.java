package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungListeneintrag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungListeneintrag}.
 */
public final class DataGostBlockungsliste extends DataManager<Integer> {

  private final int abijahrgang;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungListeneintrag}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang
	 */
	public DataGostBlockungsliste(final DBEntityManager conn, final Integer abijahrgang) {
		super(conn);
		if (abijahrgang == null)
			throw new NullPointerException();
		this.abijahrgang = abijahrgang;
	}


	/**
	 * Erstellt einen Blockungslisten-Eintrag mit den übergebenen Daten.
	 *
	 * @param blockung   das DB-DTO der Blockung
	 * @param mapErgebnisse   eine Map mit den Listen von Ergebnissen zugeordnet zu ihren Blockungs-IDs
	 *
	 * @return der Blockungs-Listeneintrag
	 */
	private static GostBlockungListeneintrag dtoMapper(final DTOGostBlockung blockung, final Map<Long, List<DTOGostBlockungZwischenergebnis>> mapErgebnisse) {
		final List<DTOGostBlockungZwischenergebnis> ergebnisse = mapErgebnisse.computeIfAbsent(blockung.ID, k -> new ArrayList<>());
		final GostBlockungListeneintrag daten = new GostBlockungListeneintrag();
		daten.id = blockung.ID;
		daten.name = blockung.Name;
		daten.gostHalbjahr = blockung.Halbjahr.id;
		daten.istAktiv = blockung.IstAktiv;
		daten.anzahlErgebnisse = ergebnisse.size();
		return daten;
	}


	@Override
	public Response getAll() throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1", DTOGostBlockung.class, abijahrgang);
		if (blockungen == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<Long> blockungsIDs = blockungen.stream().map(b -> b.ID).toList();
		final Map<Long, List<DTOGostBlockungZwischenergebnis>> mapErgebnisse = conn.queryNamed("DTOGostBlockungZwischenergebnis.blockung_id.multiple", blockungsIDs, DTOGostBlockungZwischenergebnis.class)
				.stream().collect(Collectors.groupingBy(e -> e.Blockung_ID));
    	final var daten = blockungen.stream().map(b -> dtoMapper(b, mapErgebnisse)).toList();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
	}

	@Override
	public Response get(final Integer id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final GostHalbjahr halbjahr = GostHalbjahr.fromID(id);
		if (halbjahr == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1 and e.Halbjahr = ?2", DTOGostBlockung.class, abijahrgang, halbjahr);
		if (blockungen == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		if (blockungen.isEmpty())
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new ArrayList<>()).build();
		final List<Long> blockungsIDs = blockungen.stream().map(b -> b.ID).toList();
		final Map<Long, List<DTOGostBlockungZwischenergebnis>> mapErgebnisse = conn.queryNamed("DTOGostBlockungZwischenergebnis.blockung_id.multiple", blockungsIDs, DTOGostBlockungZwischenergebnis.class)
				.stream().collect(Collectors.groupingBy(e -> e.Blockung_ID));
    	final var daten = blockungen.stream().map(b -> dtoMapper(b, mapErgebnisse)).toList();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Integer id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Ermittelt für das angegeben Abiturjahr die Anzahl der angelegten Blockungen in der Kursplanung
	 * für die einzelnen Halbjahre der Gymnasialen Oberstufe.
	 *
	 * @param conn         die Datenbankverbindung
	 * @param abiturjahr   das Abiturjahr
	 *
	 * @return die Anzahl der Blockungen für die einzelnen Halbjahre (Index 0 = EF.1, 1=EF.2, ...)
	 */
	public static long[] getAnzahlBlockungen(final DBEntityManager conn, final int abiturjahr) {
		final long[] result = new long[6];
		final List<Object[]> tmpAnzahlBlockungen = conn.queryNative("SELECT Halbjahr, count(*) AS Anzahl FROM Gost_Blockung WHERE Abi_Jahrgang=%d GROUP BY Halbjahr".formatted(abiturjahr));
		for (final Object[] anzahlBlockungen : tmpAnzahlBlockungen) {
			final int halbjahr = GostHalbjahr.fromID((Integer) anzahlBlockungen[0]).id;
			result[halbjahr] = (Long) anzahlBlockungen[1];
		}
		return result;
	}

}
