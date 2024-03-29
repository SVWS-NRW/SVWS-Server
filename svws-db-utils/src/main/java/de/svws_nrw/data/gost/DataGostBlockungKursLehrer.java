package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungKursLehrer}.
 */
public final class DataGostBlockungKursLehrer extends DataManager<Long> {

	private final long idKurs;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungKursLehrer}.
	 *
	 * @param conn     die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idKurs   die ID des Kurses
	 */
	public DataGostBlockungKursLehrer(final DBEntityManager conn, final long idKurs) {
		super(conn);
		this.idKurs = idKurs;
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	private static GostBlockungKursLehrer dtoMapper(final DTOEigeneSchule schule, final DTOGostBlockungKurslehrer dto, final DTOLehrer lehrer, final DTOLehrerAbschnittsdaten abschnitt) {
		final GostBlockungKursLehrer daten = new GostBlockungKursLehrer();
		daten.id = dto.Lehrer_ID;
		daten.kuerzel = lehrer.Kuerzel;
		daten.vorname = lehrer.Vorname;
		daten.nachname = lehrer.Nachname;
		daten.reihenfolge = dto.Reihenfolge;
		daten.wochenstunden = dto.Wochenstunden;
		daten.istExtern = (abschnitt != null) && ((abschnitt.StammschulNr != null) || (!abschnitt.StammschulNr.equals("" + schule.SchulNr)));
		return daten;
	}


	private DTOGostBlockungKurslehrer getKurslehrer(final DTOGostBlockungKurs kurs, final Long idLehrer) throws ApiOperationException {
		if (idLehrer == null) {
			final List<DTOGostBlockungKurslehrer> tmp = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id", idKurs, DTOGostBlockungKurslehrer.class);
			if (tmp.isEmpty())
				throw new ApiOperationException(Status.NOT_FOUND);
			if (tmp.size() > 1)
				throw new ApiOperationException(Status.CONFLICT);
			return tmp.get(0);
		}
		final DTOGostBlockungKurslehrer kurslehrer = conn.queryByKey(DTOGostBlockungKurslehrer.class, idKurs, idLehrer);
		if (kurslehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return kurslehrer;
	}


	@Override
	public Response get(final Long idLehrer) throws ApiOperationException {
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchuljahresabschnitte dtoSchuleSchuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (dtoSchuleSchuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Der Schuljahresabschnitt für die Schule konnte nicht aus der Datenbank bestimmt werden.");
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs.Blockung_ID);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Bestimme den Schuljahresabschnitt (oder null) für die Blockung, sofern der Schuljahresabschnitt schon angelegt ist.
		Schuljahresabschnitt lehrerSchuljahresabschnitt = DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, blockung.Halbjahr.getSchuljahrFromAbiturjahr(blockung.Abi_Jahrgang), blockung.Halbjahr.halbjahr);
		if (lehrerSchuljahresabschnitt == null)
			lehrerSchuljahresabschnitt = DataSchuljahresabschnitte.dtoMapper.apply(dtoSchuleSchuljahresabschnitt);
		final DTOGostBlockungKurslehrer kurslehrer = getKurslehrer(kurs, idLehrer);
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, kurslehrer.Lehrer_ID);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<DTOLehrerAbschnittsdaten> abschnitte = conn.queryList("SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOLehrerAbschnittsdaten.class, lehrer.ID, lehrerSchuljahresabschnitt.id);
		final DTOLehrerAbschnittsdaten abschnitt = (abschnitte.size() != 1) ? null : abschnitte.get(0);
		final GostBlockungKursLehrer daten = dtoMapper(schule, kurslehrer, lehrer, abschnitt);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	@Override
	public Response patch(final Long idLehrer, final InputStream is) throws ApiOperationException {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0)
	    	return Response.status(Status.OK).build();
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOGostBlockungKurslehrer kurslehrer = getKurslehrer(kurs, idLehrer);
    	for (final Entry<String, Object> entry : map.entrySet()) {
    		final String key = entry.getKey();
    		final Object value = entry.getValue();
    		switch (key) {
				case "id" -> {
					final Long patch_id = JSONMapper.convertToLong(value, true);
					if ((patch_id == null) || (patch_id.longValue() != kurslehrer.Lehrer_ID))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
    			case "kuerzel" -> throw new ApiOperationException(Status.BAD_REQUEST);
    			case "vorname" -> throw new ApiOperationException(Status.BAD_REQUEST);
    			case "nachname" -> throw new ApiOperationException(Status.BAD_REQUEST);
    			case "reihenfolge" -> {
    				final int tmp = JSONMapper.convertToInteger(value, false);
    				final Set<Integer> reihenfolgen = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id", idKurs, DTOGostBlockungKurslehrer.class).stream()
    					.map(kl -> kl.Reihenfolge).filter(Objects::nonNull).collect(Collectors.toSet());
    				if (reihenfolgen.contains(tmp))
    					throw new ApiOperationException(Status.CONFLICT);
    				kurslehrer.Reihenfolge = tmp;
    			}
    			case "wochenstunden" -> {
    				kurslehrer.Wochenstunden = JSONMapper.convertToInteger(value, false);
    				if ((kurslehrer.Wochenstunden < 1) || (kurslehrer.Wochenstunden > 40))
    					throw new ApiOperationException(Status.BAD_REQUEST);
    			}
    			case "istExtern" -> throw new ApiOperationException(Status.BAD_REQUEST);
    			default -> throw new ApiOperationException(Status.BAD_REQUEST);
    		}
    	}
    	conn.transactionPersist(kurslehrer);
    	return Response.status(Status.NO_CONTENT).build();
	}


	/**
     * Fügt einen weiteren lehrer zu dem Kurs einer Blockung der Gymnasialen Oberstufe hinzu.
	 *
     * @param idLehrer   die ID des Lehrers
	 *
	 * @return Eine Response mit dem neuen {@link GostBlockungKursLehrer}-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addKurslehrer(final long idLehrer) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchuljahresabschnitte dtoSchuleSchuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (dtoSchuleSchuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Der Schuljahresabschnitt für die Schule konnte nicht aus der Datenbank bestimmt werden.");
		// Prüfe, ob ein Kurs mit der ID für eine Blockung existiert
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, kurs.Blockung_ID);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Bestimme den Schuljahresabschnitt (oder null) für die Blockung, sofern der Schuljahresabschnitt schon angelegt ist.
		Schuljahresabschnitt lehrerSchuljahresabschnitt = DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, blockung.Halbjahr.getSchuljahrFromAbiturjahr(blockung.Abi_Jahrgang), blockung.Halbjahr.halbjahr);
		if (lehrerSchuljahresabschnitt == null)
			lehrerSchuljahresabschnitt = DataSchuljahresabschnitte.dtoMapper.apply(dtoSchuleSchuljahresabschnitt);
		// Prüfe, ob ein Lehrer mit der ID existiert
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, idLehrer);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<DTOLehrerAbschnittsdaten> abschnitte = conn.queryList("SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOLehrerAbschnittsdaten.class, lehrer.ID, lehrerSchuljahresabschnitt.id);
		final DTOLehrerAbschnittsdaten abschnitt = (abschnitte.size() != 1) ? null : abschnitte.get(0);
		// Prüfe, ob der Lehrer bereits dem Kurs zugeordnet ist, das darf nicht der Fall sein
		DTOGostBlockungKurslehrer kurslehrer = conn.queryByKey(DTOGostBlockungKurslehrer.class, idKurs, idLehrer);
		if (kurslehrer != null)
			throw new ApiOperationException(Status.CONFLICT);
		// Bestimme den niedrigsten Wert für Reihenfolge, der noch nicht genutzt ist und setze diese als Default
		final Set<Integer> reihenfolgen = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id", idKurs, DTOGostBlockungKurslehrer.class).stream()
			.map(kl -> kl.Reihenfolge).filter(Objects::nonNull).collect(Collectors.toSet());
		int min;
		for (min = 1; true; min++)
			if (!reihenfolgen.contains(min))
				break;
		kurslehrer = new DTOGostBlockungKurslehrer(idKurs, idLehrer, min, kurs.Wochenstunden);
		conn.transactionPersist(kurslehrer);
		final GostBlockungKursLehrer daten = dtoMapper(schule, kurslehrer, lehrer, abschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
     * Entfernt den Lehrer als Kurslehrer bei dem Kurs einer Blockung der
     * Gymnasialen Oberstufe.
	 *
     * @param idLehrer   die ID des Lehrers
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteKurslehrer(final long idLehrer) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe, ob ein Kurs mit der ID für eine Blockung existiert
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob der Lehrer überhaupt dem Kurs zugeordnet ist, das muss hier der Fall sein
		final DTOGostBlockungKurslehrer kurslehrer = conn.queryByKey(DTOGostBlockungKurslehrer.class, idKurs, idLehrer);
		if (kurslehrer == null)
			throw new ApiOperationException(Status.BAD_REQUEST);
		if (!conn.transactionRemove(kurslehrer))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}

}
