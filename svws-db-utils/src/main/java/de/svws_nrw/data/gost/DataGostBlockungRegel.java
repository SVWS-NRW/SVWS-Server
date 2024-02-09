package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
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
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungRegel}.
 */
public final class DataGostBlockungRegel extends DataManager<Long> {

	/** Ein Mapper von den DB-DTOs der Blockungsregeln ({@link DTOGostBlockungRegel}, {@link DTOGostBlockungRegelParameter}) zu dem Core-Type {@link GostBlockungRegel}. */
	public static final BiFunction<DTOGostBlockungRegel, List<DTOGostBlockungRegelParameter>, GostBlockungRegel> dtoMapper = (regel, params) -> {
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
							case GANZZAHL -> {
								break; // immer gültig
							}
							case FACH_ID -> {
								break; // TODO BACHRAN
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
    	return Response.status(Status.OK).build();
	}


	/**
     * Fügt eine Regel mit Default-Werten zu einer Blockung der Gymnasialen Oberstufe hinzu.
	 *
     * @param idBlockung       die ID der Blockung
	 * @param idRegelTyp       die ID des Typs der Blockungsregel (siehe {@link GostKursblockungRegelTyp})
	 * @param regelParameter   die Parameter der Regel oder null, falls Default-Parameter verwendet werden sollen
	 *
	 * @return Eine Response mit der neuen Regel
	 */
	public Response addRegel(final long idBlockung, final int idRegelTyp, final List<Long> regelParameter) {
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
		// Prüfe, ob die Anzahl der Parameter korrekt ist
    	if ((regelParameter != null) && (regelTyp.getParamCount() != regelParameter.size()))
    		throw OperationError.CONFLICT.exception();
		// Prüfe ggf., ob bereits eine identische Regel von dem Typ existiert und führe ggf. weitere Prüfungen durch
		final List<DTOGostBlockungRegel> regeln = conn.queryList("SELECT e FROM DTOGostBlockungRegel e WHERE e.Blockung_ID = ?1 AND e.Typ = ?2", DTOGostBlockungRegel.class, idBlockung, regelTyp);
		final List<Long> regelIDs = regeln.stream().map(r -> r.ID).toList();
		if (!regeln.isEmpty()) {
			switch (regelTyp) {
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> {
					if (regelParameter == null)
						throw OperationError.CONFLICT.exception();
					final List<DTOGostBlockungRegelParameter> duplicates = conn.queryList("SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID IN ?1 AND e.Nummer = 0 AND e.Parameter = ?2", DTOGostBlockungRegelParameter.class, regelIDs, regelParameter.get(0));
					if (!duplicates.isEmpty())
						throw OperationError.CONFLICT.exception("Es existiert bereits eine Regel zum Auffüllen des Kurses mit der ID %d.".formatted(regelParameter.get(0)));
					final long anzahl = regelParameter.get(1);
					if ((anzahl < 1) || (anzahl > 99))
						throw OperationError.BAD_REQUEST.exception("Die Anzahl der Schüler muss mindestens 1 sein und darf 99 nicht überschreiten.");
				}
				default -> { /* TODO weitere Regeltypen prüfen */ }
			}
		}
		// Bestimme die ID, für welche der Datensatz eingefügt wird
		final long idRegel = conn.transactionGetNextID(DTOGostBlockungRegel.class);
		// Füge die Regel hinzu
    	final DTOGostBlockungRegel regel = new DTOGostBlockungRegel(idRegel, idBlockung, regelTyp);
    	conn.transactionPersist(regel);
    	conn.transactionFlush();
    	final GostBlockungRegel daten = new GostBlockungRegel();
    	daten.id = idRegel;
    	daten.typ = regelTyp.typ;
    	// Füge Default-Parameter zu der Regel hinzu.
    	final List<DTOSchueler> schueler = DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, blockung.Abi_Jahrgang);
    	final Set<Long> setSchuelerIDs = schueler.stream().map(s -> s.ID).collect(Collectors.toSet());
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
						if (schueler.isEmpty())
							throw OperationError.NOT_FOUND.exception();
						yield schueler.get(0).ID;
					}
					case BOOLEAN -> 0L;
					case GANZZAHL -> 0L;
					case FACH_ID -> 0L; // TODO BACHRAN
	    		};
    		} else {
				paramValue = regelParameter.get(i);
				validateRegelParameter(blockung.Abi_Jahrgang, idBlockung, setSchuelerIDs, paramType, paramValue);
    		}
    		final DTOGostBlockungRegelParameter param = new DTOGostBlockungRegelParameter(idRegel, i, paramValue);
    		conn.transactionPersist(param);
    		daten.parameter.add(paramValue);
    	}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private void validateRegelParameter(final int abijahrgang, final long idBlockung, final Set<Long> setSchuelerIDs, final GostKursblockungRegelParameterTyp paramType, final long paramValue) {
		switch (paramType) {
			case KURSART -> {
				if (GostKursart.fromIDorNull((int) paramValue) == null)
					throw OperationError.NOT_FOUND.exception();
			}
			case KURS_ID -> {
				final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, paramValue);
				if (kurs == null)
					throw OperationError.NOT_FOUND.exception();
			}
			case SCHIENEN_NR -> {
				final List<DTOGostBlockungSchiene> schienen = conn.queryList("SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID = ?1 AND e.Nummer = ?2",
						DTOGostBlockungSchiene.class, idBlockung, paramValue);
				if (schienen.isEmpty())
					throw OperationError.NOT_FOUND.exception();
			}
			case SCHUELER_ID -> {
				if (!setSchuelerIDs.contains(paramValue))
					throw OperationError.NOT_FOUND.exception();
			}
			case BOOLEAN -> {
				if ((paramValue < 0) || (paramValue > 1))
					throw OperationError.NOT_FOUND.exception();
			}
			case GANZZAHL -> {
				break; // immer gültig
			}
			case FACH_ID -> {
				break; // TODO BACHRAN
			}
		}
	}


	private void validateRegel(final int abijahrgang, final long idBlockung, final Set<Long> setSchuelerIDs, final GostBlockungRegel regel, final Map<Integer, List<DTOGostBlockungRegel>> mapVorhanden) {
		// Prüfe ob der Regel-Typ korrekt ist
		final GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		if (regelTyp == GostKursblockungRegelTyp.UNDEFINIERT)
			throw OperationError.CONFLICT.exception("Der Typ der Regel ist unbekannt.");
		// Prüfe, ob die Anzahl der Parameter korrekt ist
    	if (regelTyp.getParamCount() != regel.parameter.size())
    		throw OperationError.CONFLICT.exception();
    	// Prüfe ggf., ob bereits eine identische Regel von dem Typ existiert und führe ggf. weitere Prüfungen durch
        final List<DTOGostBlockungRegel> regelnVorhanden = mapVorhanden.get(regelTyp.typ);
		if ((regelnVorhanden != null) && (!regelnVorhanden.isEmpty())) {
			switch (regelTyp) {
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> {
					final List<Long> regelIDs = regelnVorhanden.stream().map(r -> r.ID).toList();
					final List<DTOGostBlockungRegelParameter> duplicates = conn.queryList("SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID IN ?1 AND e.Nummer = 0 AND e.Parameter = ?2", DTOGostBlockungRegelParameter.class, regelIDs, regel.parameter.get(0));
					if (!duplicates.isEmpty())
						throw OperationError.CONFLICT.exception("Es existiert bereits eine Regel zum Auffüllen des Kurses mit der ID %d.".formatted(regel.parameter.get(0)));
					final long anzahl = regel.parameter.get(1);
					if ((anzahl < 1) || (anzahl > 99))
						throw OperationError.BAD_REQUEST.exception("Die Anzahl der Schüler muss mindestens 1 sein und darf 99 nicht überschreiten.");
				}
				default -> { /* TODO weitere Regeltypen prüfen */ }
			}
		}
    	// Prüfe die Parameter der Regel.
    	for (int i = 0; i < regelTyp.getParamCount(); i++) {
    		final GostKursblockungRegelParameterTyp paramType = regelTyp.getParamType(i);
			final long paramValue = regel.parameter.get(i);
    		validateRegelParameter(abijahrgang, idBlockung, setSchuelerIDs, paramType, paramValue);
    	}
	}


	/**
     * Fügt mehrere Regeln zu einer Blockung der Gymnasialen Oberstufe hinzu. Diese müssen zunächst eine
     * Pseudo-ID von -1 beinhalten.
	 *
     * @param idBlockung  die ID der Blockung
	 * @param regeln      die hinzuzufügenden Regeln
	 *
	 * @return Die HTTP-response
	 */
	public Response addRegeln(final long idBlockung, final List<GostBlockungRegel> regeln) {
		try {
			// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
	        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
	        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
	        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
	        if (vorlage == null)
	        	throw OperationError.BAD_REQUEST.exception("Die Regeln können nicht hinzugefügt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
	        // Bestimme schon vorhandene Regeln der Blockung
	        final Map<Integer, List<DTOGostBlockungRegel>> mapVorhanden = conn.queryNamed("DTOGostBlockungRegel.blockung_id", idBlockung, DTOGostBlockungRegel.class)
        		.stream().collect(Collectors.groupingBy(r -> r.Typ.typ));
			// Bestimme die ID, ab welcher die Datensätze eingefügt werden
			long idRegel = conn.transactionGetNextID(DTOGostBlockungRegel.class);
			// Bestimme die Schüler des Abiturjahrgangs, falls Regeln einen Schüler-Bezug haben
	    	final List<DTOSchueler> schueler = DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, blockung.Abi_Jahrgang);
	    	final Set<Long> setSchuelerIDs = schueler.stream().map(s -> s.ID).collect(Collectors.toSet());
	        // Durchwandere die Regeln und füge sie nacheinander hinzu
			for (final GostBlockungRegel regel : regeln) {
				// validiere die Regel
				validateRegel(blockung.Abi_Jahrgang, idBlockung, setSchuelerIDs, regel, mapVorhanden);
				final GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
				// Füge die Regel hinzu
				regel.id = idRegel++;
				final DTOGostBlockungRegel dtoRegel = new DTOGostBlockungRegel(regel.id, idBlockung, regelTyp);
		    	conn.transactionPersist(dtoRegel);
		    	conn.transactionFlush();
		    	// Füge die Parameter zu der Regel hinzu.
		    	for (int i = 0; i < regel.parameter.size(); i++) {
		    		final DTOGostBlockungRegelParameter param = new DTOGostBlockungRegelParameter(regel.id, i, regel.parameter.get(i));
		    		conn.transactionPersist(param);
		    	}
		    	conn.transactionFlush();
		    	mapVorhanden.computeIfAbsent(regel.typ, r -> new ArrayList<>()).add(dtoRegel);
	    	}
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(regeln).build();
		} catch (final Exception exception) {
			if (exception instanceof final IllegalArgumentException e)
				throw OperationError.NOT_FOUND.exception(e);
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
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
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
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Löscht mehrere Regeln einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param regelIDs   die IDs der Regeln
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteMultiple(final List<Long> regelIDs) {
		if (regelIDs.isEmpty())
			throw OperationError.BAD_REQUEST.exception("Es wurden keine IDs für Regeln angegeben.");
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Regeln
		final List<DTOGostBlockungRegel> regeln = conn.queryNamed("DTOGostBlockungRegel.id.multiple", regelIDs, DTOGostBlockungRegel.class);
		if (regeln.size() != regelIDs.size())
    		throw OperationError.NOT_FOUND.exception("Mindestens eine Regel wurde für die angegebenen IDs nicht gefunden.");
		// Prüfe, ob alle eingelesenen Regeln die gleiche Blockungs-ID haben
		final long idBlockung = regeln.get(0).Blockung_ID;
		for (final DTOGostBlockungRegel regel : regeln)
			if (regel.Blockung_ID != idBlockung)
				throw OperationError.BAD_REQUEST.exception("Alle zu löschenden Regeln müssen zur gleichen Blockung gehören.");
        // Prüfe, ob die Blockung nur das Vorlage-Ergebnis hat
        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
        final DTOGostBlockungZwischenergebnis vorlage = DataGostBlockungsdaten.pruefeNurVorlageErgebnis(conn, blockung);
        if (vorlage == null)
        	throw OperationError.BAD_REQUEST.exception("Die Regeln können nicht entfernt werden, da bei der Blockungsdefinition schon berechnete Ergebnisse existieren.");
		// Entferne die Regeln
        if (!conn.transactionRemoveAll(regeln))
        	throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler bei der Datenbank-Transaktion.");
		return Response.status(Status.NO_CONTENT).build();
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
