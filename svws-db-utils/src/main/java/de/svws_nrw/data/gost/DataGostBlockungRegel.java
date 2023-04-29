package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
import de.svws_nrw.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungRegel}.
 */
public final class DataGostBlockungRegel extends DataManager<Long> {

	/** Ein Mapper von den DB-DTOs der Blockungsregeln ({@link DTOGostBlockungRegel}, {@link DTOGostBlockungRegelParameter}) zu dem Core-Type {@link GostBlockungRegel}. */
	public static BiFunction<DTOGostBlockungRegel, List<DTOGostBlockungRegelParameter>, GostBlockungRegel> dtoMapper = (regel, params) -> {
		final GostBlockungRegel daten = new GostBlockungRegel();
		daten.id = regel.ID;
		daten.typ = regel.Typ.typ;
		if ((params != null) && (!params.isEmpty()))
			daten.parameter.addAll(params.stream().sorted((a, b) -> Integer.compare(a.Nummer, b.Nummer)).map(r -> r.Parameter).toList());
		return daten;
	};


	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungRegel}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungRegel(final DBEntityManager conn) {
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


	@Override
	public Response get(final Long id) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Regel der Blockung
		final DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
		if (regel == null)
			return OperationError.NOT_FOUND.getResponse();
		final List<DTOGostBlockungRegelParameter> params = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id", regel.ID, DTOGostBlockungRegelParameter.class);
		final GostBlockungRegel daten = dtoMapper.apply(regel, params);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() <= 0)
	    	return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Bestimme die Regel der Blockung
			final DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
			if (regel == null)
				return OperationError.NOT_FOUND.getResponse();
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, regel.Blockung_ID);
	        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Die Regel kann nicht angepasst werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
	    	for (final Entry<String, Object> entry : map.entrySet()) {
	    		final String key = entry.getKey();
	    		final Object value = entry.getValue();
	    		switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
	    			case "typ" -> {
						final Integer patch_typ = JSONMapper.convertToInteger(value, true);
						if ((patch_typ == null) || (patch_typ != regel.Typ.typ))
							throw OperationError.BAD_REQUEST.exception();
	    			}
	    			case "parameter" -> {
	    				if (!(value instanceof List))
	    					throw OperationError.BAD_REQUEST.exception();
	    				@SuppressWarnings("unchecked")
						final
						List<? extends Number> params = (List<? extends Number>) value;
	    				// Überprüfe zunächst die Anzahl der Parameter
	    				final int pcount = regel.Typ.getParamCount();
	    				if (pcount != params.size())
	    					throw OperationError.BAD_REQUEST.exception();
	    				// Aktualisiere die Parameter
	    				for (int i = 0; i < pcount; i++) {
	    					// Bestimme Typ und Wert
	    					final GostKursblockungRegelParameterTyp ptype = regel.Typ.getParamType(i);
	    					final long pvalue = params.get(i).longValue();
	    					// Überprüfe den Parameter-Wert, ob dieser einen gültigen Wert für die Blockung enthält
	    					switch (ptype) {
								case KURSART -> {
									final GostKursart kursart = GostKursart.fromIDorNull((int) pvalue);
									if (kursart == null)
										throw OperationError.BAD_REQUEST.exception();
								}
								case KURS_ID -> {
									final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, pvalue);
									if ((kurs == null) || (kurs.Blockung_ID != regel.Blockung_ID))
										throw OperationError.BAD_REQUEST.exception();
								}
								case SCHIENEN_NR -> {
									final List<DTOGostBlockungSchiene> dtos = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", regel.Blockung_ID, DTOGostBlockungSchiene.class);
									if ((dtos == null) || (dtos.isEmpty()))
										throw OperationError.BAD_REQUEST.exception();
									final Set<Integer> schienen = dtos.stream().map(s -> s.Nummer).collect(Collectors.toSet());
									if (!schienen.contains((int) pvalue))
										throw OperationError.BAD_REQUEST.exception();
								}
								case SCHUELER_ID -> {
									final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, pvalue);
									if (schueler == null)
										throw OperationError.BAD_REQUEST.exception();
								}
								case BOOLEAN -> {
									if ((pvalue < 0) || (pvalue > 1))
										throw OperationError.BAD_REQUEST.exception();
								}
								default -> throw OperationError.BAD_REQUEST.exception();
	    					}
	    					// Aktualisiere den Parameter-Wert in der Datenbank, sofern er sich geändert hat
	    					final DTOGostBlockungRegelParameter param = conn.queryByKey(DTOGostBlockungRegelParameter.class, id, i);
							if (param.Parameter != pvalue) {
								param.Parameter = pvalue;
								conn.transactionPersist(param);
							}
	    				}
	    			}
	    			default -> throw OperationError.BAD_REQUEST.exception();
	    		}
	    	}
	    	conn.transactionCommit();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
    	return Response.status(Status.OK).build();
	}


	/**
     * Fügt eine Regel mit Default-Werten zu einer Blockung der Gymnasialen Oberstufe hinzu.
	 *
     * @param idBlockung       die ID der Blockung
	 * @param idRegelTyp       die ID des Typs der Blockungsregel (siehe {@link GostKursblockungRegelTyp})
	 * @param regelParameter   die Parameter der Regel oder null, falls Default-Paramater verwendet werden sollen
	 *
	 * @return Eine Response mit der neuen Regel
	 */
	public Response addRegel(final long idBlockung, final int idRegelTyp, final List<Long> regelParameter) {
		try {
			conn.transactionBegin();
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
	        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Die Regel kann nicht hinzugefügt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
			// Prüfe ob die ID des Typs korrekt ist
			final GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(idRegelTyp);
			if (regelTyp == GostKursblockungRegelTyp.UNDEFINIERT)
				throw OperationError.CONFLICT.exception();
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			final DTODBAutoInkremente dbRegelID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Blockung_Regeln");
			final long idRegel = dbRegelID == null ? 1 : dbRegelID.MaxID + 1;
			// Füge die Regel hinzu
	    	final DTOGostBlockungRegel regel = new DTOGostBlockungRegel(idRegel, idBlockung, regelTyp);
	    	conn.transactionPersist(regel);
	    	final GostBlockungRegel daten = new GostBlockungRegel();
	    	daten.id = idRegel;
	    	daten.typ = regelTyp.typ;
	    	if ((regelParameter != null) && (regelTyp.getParamCount() != regelParameter.size()))
	    		throw OperationError.CONFLICT.exception();
	    	// Füge Default-Parameter zu der Regel hinzu.
	    	for (int i = 0; i < regelTyp.getParamCount(); i++) {
	    		final GostKursblockungRegelParameterTyp paramType = regelTyp.getParamType(i);
	    		final long paramValue;
	    		if (regelParameter == null) {
		    		paramValue = switch (paramType) {
						case KURSART -> GostKursart.LK.id;
						case KURS_ID -> {
					    	final List<DTOGostBlockungKurs> kurse = conn.queryNamed("DTOGostBlockungKurs.blockung_id", idBlockung, DTOGostBlockungKurs.class);
							if ((kurse == null) || (kurse.isEmpty()))
								throw OperationError.NOT_FOUND.exception();
							yield kurse.get(0).ID;
						}
						case SCHIENEN_NR -> {
							final Optional<Integer> minSchiene = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", idBlockung, DTOGostBlockungSchiene.class).stream().map(s -> s.Nummer).min(Integer::compare);
							if (minSchiene.isEmpty())
								throw OperationError.NOT_FOUND.exception();
							yield minSchiene.get();
						}
						case SCHUELER_ID -> {
							final List<DTOViewGostSchuelerAbiturjahrgang> schueler = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", blockung.Abi_Jahrgang, DTOViewGostSchuelerAbiturjahrgang.class);
							if ((schueler == null) || (schueler.isEmpty()))
								throw OperationError.NOT_FOUND.exception();
							yield schueler.get(0).ID;
						}
						case BOOLEAN -> 0L;
		    		};
	    		} else {
					final Long tmp = regelParameter.get(i);
					paramValue = tmp;
		    		switch (paramType) {
						case KURSART -> {
							if (GostKursart.fromIDorNull(tmp.intValue()) == null)
								throw OperationError.NOT_FOUND.exception();
						}
						case KURS_ID -> {
							final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, tmp);
							if (kurs == null)
								throw OperationError.NOT_FOUND.exception();
						}
						case SCHIENEN_NR -> {
							final List<DTOGostBlockungSchiene> schienen = conn.queryList("SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID = ?1 AND e.Nummer = ?2",
									DTOGostBlockungSchiene.class, idBlockung, tmp);
							if (schienen.isEmpty())
								throw OperationError.NOT_FOUND.exception();
						}
						case SCHUELER_ID -> {
							final List<DTOViewGostSchuelerAbiturjahrgang> schueler = conn.queryList("SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Abiturjahr = ?1 AND e.ID = ?2",
									DTOViewGostSchuelerAbiturjahrgang.class, blockung.Abi_Jahrgang, tmp);
							if ((schueler == null) || (schueler.isEmpty()))
								throw OperationError.NOT_FOUND.exception();
						}
						case BOOLEAN -> {
							if ((paramValue < 0) || (paramValue > 1))
								throw OperationError.NOT_FOUND.exception();
						}
		    		}
	    		}
	    		final DTOGostBlockungRegelParameter param = new DTOGostBlockungRegelParameter(idRegel, i, paramValue);
	    		conn.transactionPersist(param);
	    		daten.parameter.add(paramValue);
	    	}
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof final IllegalArgumentException e)
				throw OperationError.NOT_FOUND.exception(e);
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}


	/**
	 * Löscht eine Regel einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param id   die ID der Regel
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		try {
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Bestimme die Regel
			final DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
			if (regel == null)
	    		throw OperationError.NOT_FOUND.exception();
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, regel.Blockung_ID);
	        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Die Regel kann nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
			// Bestimme die Regel-Parameter (diese werden beim Entfernen der Regel automatisch mit entfernt.
			final GostBlockungRegel daten = new GostBlockungRegel();
			daten.id = id;
	    	final List<DTOGostBlockungRegelParameter> params = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id", id, DTOGostBlockungRegelParameter.class);
	    	if (params == null)
	    		throw OperationError.NOT_FOUND.exception();
	    	params.sort((a, b) -> Integer.compare(a.Nummer, b.Nummer));
			for (final DTOGostBlockungRegelParameter param : params)
				daten.parameter.add(param.Parameter);
			daten.typ = regel.Typ.typ;
			// Entferne die Regel
			conn.transactionRemove(regel);
			conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}


	/**
	 * Erstellt eine Liste von Core-Type-Blockungsregeln zu den angegebenen Datenbank-DTOs
	 *
	 * @param regeln         eine Liste der Regeln (Datenbank-DTOs)
	 * @param parameter   eine Liste mit allen Parametern der Regeln
	 *
	 * @return die Liste der Blockungsregeln
	 */
	public static List<GostBlockungRegel> getBlockungsregeln(final List<DTOGostBlockungRegel> regeln, final List<DTOGostBlockungRegelParameter> parameter) {
		if ((regeln == null) || (parameter == null) || (regeln.isEmpty()))
			return Collections.emptyList();
		final Map<Long, List<DTOGostBlockungRegelParameter>> mapParameter = parameter.stream().collect(Collectors.groupingBy(r -> r.Regel_ID));
		// Erzeuge die Liste der Core-Types
		final List<GostBlockungRegel> result = new ArrayList<>();
		for (final DTOGostBlockungRegel regel : regeln)
			result.add(dtoMapper.apply(regel, mapParameter.get(regel.ID)));
        return result;
	}


	/**
	 * Erstellt eine Liste von Blockungsregeln zu der angegebenen Blockungs-ID
	 *
	 * @param idBlockung   die ID der Blockung
	 *
	 * @return die Liste der Blockungsregeln
	 */
	public List<GostBlockungRegel> getBlockungsregeln(final long idBlockung) {
		// Bestimme alle Regeln der Blockung
		final List<DTOGostBlockungRegel> regeln = conn.queryNamed("DTOGostBlockungRegel.blockung_id", idBlockung, DTOGostBlockungRegel.class);
		// Bestimme die IDs dieser Regeln
		final List<Long> regelIDs = regeln.stream().map(r -> r.ID).toList();
		// Bestimme die RegelParameter dieser Regeln
		final List<DTOGostBlockungRegelParameter> parameter = conn.queryNamed("DTOGostBlockungRegelParameter.regel_id.multiple", regelIDs, DTOGostBlockungRegelParameter.class);
		return getBlockungsregeln(regeln, parameter);
	}

}
