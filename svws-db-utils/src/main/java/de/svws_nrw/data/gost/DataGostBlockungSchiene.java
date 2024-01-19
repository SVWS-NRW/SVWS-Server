package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungKurs}.
 */
public final class DataGostBlockungSchiene extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungKurs}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungSchiene(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockungSchiene} in einen Core-DTO {@link GostBlockungSchiene}.
	 */
	public static final Function<DTOGostBlockungSchiene, GostBlockungSchiene> dtoMapper = (final DTOGostBlockungSchiene schiene) -> {
		final GostBlockungSchiene daten = new GostBlockungSchiene();
		daten.id = schiene.ID;
		daten.nummer = schiene.Nummer;
		daten.bezeichnung = schiene.Bezeichnung;
		daten.wochenstunden = schiene.Wochenstunden;
		return daten;
	};


	@Override
	public Response get(final Long id) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Schiene der Blockung
		final DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, id);
		if (schiene == null)
			return OperationError.NOT_FOUND.getResponse();
		final GostBlockungSchiene daten = dtoMapper.apply(schiene);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0)
	    	return Response.status(Status.OK).build();
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Schiene der Blockung
		final DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, id);
		if (schiene == null)
			return OperationError.NOT_FOUND.getResponse();
        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, schiene.Blockung_ID);
        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
        if (vorlage == null)
        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht angepasst werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
    	for (final Entry<String, Object> entry : map.entrySet()) {
    		final String key = entry.getKey();
    		final Object value = entry.getValue();
    		switch (key) {
				case "id" -> {
					final Long patch_id = JSONMapper.convertToLong(value, true);
					if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
						throw OperationError.BAD_REQUEST.exception();
				}
    			case "bezeichnung" -> schiene.Bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_Gost_Blockung_Schienen.col_Bezeichnung.datenlaenge());
    			case "wochenstunden" -> {
    				schiene.Wochenstunden = JSONMapper.convertToInteger(value, false);
    				if ((schiene.Wochenstunden < 1) || (schiene.Wochenstunden > 40))
    					throw OperationError.BAD_REQUEST.exception();
    			}
    			case "nummer" -> {
					final Integer patch_nummer = JSONMapper.convertToInteger(value, true);
    				if ((patch_nummer == null) || (!patch_nummer.equals(schiene.Nummer)))
    					throw OperationError.BAD_REQUEST.exception();
    			}
    			default -> throw OperationError.BAD_REQUEST.exception();
    		}
    	}
    	conn.transactionPersist(schiene);
    	return Response.status(Status.OK).build();
	}


	/**
	 * Löscht eine Schiene einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param schiene   die zu entfernende Schiene (DB-DTO)
	 *
	 * @return die entfernte Schiene (Core-DTO).
	 */
	private GostBlockungSchiene _delete(final DTOGostBlockungSchiene schiene) {
		if (schiene == null)
			throw OperationError.NOT_FOUND.exception();

        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, schiene.Blockung_ID);
        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
        if (vorlage == null)
        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");

        // Prüfe, ob die Schiene im aktuelle Vorlage-Ergebnis Kurse hat: Dann ist das entfernen nicht erlaubt...
    	final List<DTOGostBlockungZwischenergebnisKursSchiene> kurse = conn.queryList(
			"SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID = ?1 AND e.Schienen_ID = ?2",
			DTOGostBlockungZwischenergebnisKursSchiene.class,
			vorlage.ID, schiene.ID
    	);
        if (!kurse.isEmpty())
        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht entfernt werden, da der Schiene bereits Kurse zugeordnet sind. Diese müssen zuerst entfernt werden.");
        final GostBlockungSchiene daten = dtoMapper.apply(schiene);

        // Passt die Schienen-Nummern bei den Regeln an.
		final List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", schiene.Blockung_ID, DTOGostBlockungSchiene.class);
		for (final DTOGostBlockungSchiene tmp : schienen) {
		    if (daten.id == tmp.ID) {
	            conn.transactionRemove(tmp); // Entferne die Schiene
		    } else if (tmp.Nummer > daten.nummer) {
		        tmp.Nummer--; // Reduziere die Nummer dieser Schiene aufgrund der entfernten Schiene
		        conn.transactionPersist(tmp);
		    }
		}

		// Passe alle Regeln einem Parametern Schienenanzahl an.
		// Bestimme alle Regeln der Blockung
		final List<DTOGostBlockungRegel> dtoRegeln = conn.queryNamed("DTOGostBlockungRegel.blockung_id", schiene.Blockung_ID, DTOGostBlockungRegel.class);
		final Map<Long, DTOGostBlockungRegel> mapDTORegeln = dtoRegeln.stream().collect(Collectors.toMap(r -> r.ID, r -> r));
		if (!dtoRegeln.isEmpty()) {
			final List<Long> regelIDs = dtoRegeln.stream().map(r -> r.ID).toList();
			// Bestimme die RegelParameter dieser Regeln
			final List<DTOGostBlockungRegelParameter> dtoRegelParameter = conn
					.queryNamed("DTOGostBlockungRegelParameter.regel_id.multiple", regelIDs, DTOGostBlockungRegelParameter.class);
			final Map<Long, List<DTOGostBlockungRegelParameter>> mapParameter = dtoRegelParameter.stream().collect(Collectors.groupingBy(r -> r.Regel_ID));
			// Erstelle die Core-Types und prüfe auf neue Parameter-Werte (verwende hierbei den gleichen Algorithmus, wie im zugehörigen Daten-Manager..
			final List<GostBlockungRegel> regeln = DataGostBlockungRegel.getBlockungsregeln(dtoRegeln, dtoRegelParameter);
			// Übertrage die Anpassungen in die Datenbank
			for (final GostBlockungRegel regel : regeln) {
				if (regel.parameter.isEmpty())
					continue;
				final List<DTOGostBlockungRegelParameter> dtoParams = mapParameter.get(regel.id);
				if ((dtoParams == null) || (dtoParams.isEmpty()))
					throw OperationError.INTERNAL_SERVER_ERROR.exception();
				final Map<Integer, DTOGostBlockungRegelParameter> mapParam = dtoParams.stream().collect(Collectors.toMap(p -> p.Nummer, p -> p));
				final long[] newParams = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(regel, daten.nummer);
				if (newParams == null) { // Lösche die Regel in der DB und gehe zur nächsten über
					conn.transactionRemove(mapDTORegeln.get(regel.id));
					continue;
				}
				// Prüfe, ob die Parameter der Regel verändert wurden und aktualisiere diese ggf. in der DB
				for (int i = 0; i < newParams.length; i++) {
					if (newParams[i] != regel.parameter.get(i)) {
						final DTOGostBlockungRegelParameter dtoParam = mapParam.get(i);
						dtoParam.Parameter = newParams[i];
						conn.transactionPersist(dtoParam);
					}
				}
			}
		}
		return daten;
	}


	/**
	 * Löscht eine Schiene einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param id   die ID der Schiene
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
	    if (id == null)
	        return OperationError.CONFLICT.getResponse();
		// Bestimme die Schiene der Blockung
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
        final GostBlockungSchiene daten = _delete(conn.queryByKey(DTOGostBlockungSchiene.class, id));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
     * Fügt eine weitere Schiene zu einer Blockung der Gymnasialen Oberstufe hinzu
	 *
     * @param idBlockung   die ID der Blockung
	 *
	 * @return Eine Response mit der ID der neuen Schiene
	 */
	public Response addSchiene(final long idBlockung) {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe, ob die Blockung mit der ID existiert
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception();
        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
        if (vorlage == null)
        	throw OperationError.BAD_REQUEST.exception("Die Schiene kann nicht hinzugefügt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
		// Bestimme die ID, für welche der Datensatz eingefügt wird
		final DTOSchemaAutoInkremente dbSchienenID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Blockung_Schienen");
		final long idSchiene = dbSchienenID == null ? 1 : dbSchienenID.MaxID + 1;
		// Ermittle, ob bereits Schienen existieren
		final List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class);
    	int schienennummer = 1;
    	if ((schienen != null) && (!schienen.isEmpty())) { // Bestimme die erste freie Schienennummer
    		final Set<Integer> schienenIDs = schienen.stream().map(e -> e.Nummer).collect(Collectors.toSet());
    		while (schienenIDs.contains(schienennummer))
    			schienennummer++;
    	}
    	final DTOGostBlockungSchiene schiene = new DTOGostBlockungSchiene(idSchiene, idBlockung, schienennummer, "Schiene " + schienennummer, 3);
    	conn.transactionPersist(schiene);
		final GostBlockungSchiene daten = dtoMapper.apply(schiene);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
     * Entfernt eine Schiene bei einer Blockung der Gymnasialen Oberstufe.
	 *
     * @param idBlockung   die ID der Blockung
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteSchiene(final long idBlockung) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Schienen der Blockung und löschen die Schiene mit der höchsten Nummer
    	final List<DTOGostBlockungSchiene> schienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class);
    	if ((schienen == null) || (schienen.isEmpty()))
    		throw OperationError.NOT_FOUND.exception();
    	final Optional<DTOGostBlockungSchiene> optSchiene = schienen.stream().max((a, b) -> Integer.compare(a.Nummer, b.Nummer));
    	if (optSchiene.isEmpty())
    		throw OperationError.NOT_FOUND.exception();
    	final DTOGostBlockungSchiene schiene = optSchiene.get();
        final GostBlockungSchiene daten = _delete(schiene);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
