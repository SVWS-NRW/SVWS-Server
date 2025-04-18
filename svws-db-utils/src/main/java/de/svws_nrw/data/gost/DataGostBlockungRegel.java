package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungRegelUpdate;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegel;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungRegelParameter;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
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
	public Response get(final Long id) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Regel der Blockung
		final DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
		if (regel == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<DTOGostBlockungRegelParameter> params =
				conn.queryList(DTOGostBlockungRegelParameter.QUERY_BY_REGEL_ID, DTOGostBlockungRegelParameter.class, regel.ID);
		final GostBlockungRegel daten = DataGostBlockungRegel.dtoMapper.apply(regel, params);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() <= 0)
			return Response.status(Status.OK).build();
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Regel der Blockung
		final DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
		if (regel == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob die Blockung vorhanden ist
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, regel.Blockung_ID);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d kann für die Regel nicht gefunden werden."
				.formatted(regel.Blockung_ID));
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				case "id" -> {
					final Long patch_id = JSONMapper.convertToLong(value, true);
					if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				case "typ" -> {
					final Integer patch_typ = JSONMapper.convertToInteger(value, true);
					if ((patch_typ == null) || (patch_typ != regel.Typ.typ))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				case "parameter" -> {
					if (!(value instanceof List))
						throw new ApiOperationException(Status.BAD_REQUEST);
					@SuppressWarnings("unchecked") final List<? extends Number> params = (List<? extends Number>) value;
					// Überprüfe zunächst die Anzahl der Parameter
					final int pcount = regel.Typ.getParamCount();
					if (pcount != params.size())
						throw new ApiOperationException(Status.BAD_REQUEST);
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
									throw new ApiOperationException(Status.BAD_REQUEST);
							}
							case KURS_ID -> {
								final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, pvalue);
								if ((kurs == null) || (kurs.Blockung_ID != regel.Blockung_ID))
									throw new ApiOperationException(Status.BAD_REQUEST);
							}
							case SCHIENEN_NR -> {
								final List<DTOGostBlockungSchiene> dtos =
										conn.queryList(DTOGostBlockungSchiene.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungSchiene.class, regel.Blockung_ID);
								if ((dtos == null) || (dtos.isEmpty()))
									throw new ApiOperationException(Status.BAD_REQUEST);
								final Set<Integer> schienen = dtos.stream().map(s -> s.Nummer).collect(Collectors.toSet());
								if (!schienen.contains((int) pvalue))
									throw new ApiOperationException(Status.BAD_REQUEST);
							}
							case SCHUELER_ID -> {
								final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, pvalue);
								if (schueler == null)
									throw new ApiOperationException(Status.BAD_REQUEST);
							}
							case FACH_ID -> {
								final DTOFach fach = conn.queryByKey(DTOFach.class, pvalue);
								if ((fach == null) || ((fach.IstOberstufenFach != null) && (!fach.IstOberstufenFach)))
									throw new ApiOperationException(Status.BAD_REQUEST);
							}
							case BOOLEAN -> {
								if ((pvalue < 0) || (pvalue > 1))
									throw new ApiOperationException(Status.BAD_REQUEST);
							}
							case GANZZAHL -> {
								break; // immer gültig
							}
							default -> throw new ApiOperationException(Status.BAD_REQUEST);
						}
						// Aktualisiere den Parameter-Wert in der Datenbank, sofern er sich geändert hat
						final DTOGostBlockungRegelParameter param = conn.queryByKey(DTOGostBlockungRegelParameter.class, id, i);
						if (param.Parameter != pvalue) {
							param.Parameter = pvalue;
							conn.transactionPersist(param);
						}
					}
				}
				default -> throw new ApiOperationException(Status.BAD_REQUEST);
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addRegel(final long idBlockung, final int idRegelTyp, final List<Long> regelParameter) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe, ob die Blockung vorhanden ist
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d kann für die Regel nicht gefunden werden."
				.formatted(idBlockung));
		// Prüfe ob die ID des Typs korrekt ist
		final GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(idRegelTyp);
		if (regelTyp == GostKursblockungRegelTyp.UNDEFINIERT)
			throw new ApiOperationException(Status.CONFLICT);
		// Prüfe, ob die Anzahl der Parameter korrekt ist
		if ((regelParameter != null) && (regelTyp.getParamCount() != regelParameter.size()))
			throw new ApiOperationException(Status.CONFLICT);
		// Prüfe ggf., ob bereits eine identische Regel von dem Typ existiert und führe ggf. weitere Prüfungen durch
		final List<DTOGostBlockungRegel> regeln = conn.queryList("SELECT e FROM DTOGostBlockungRegel e WHERE e.Blockung_ID = ?1 AND e.Typ = ?2",
				DTOGostBlockungRegel.class, idBlockung, regelTyp);
		final List<Long> regelIDs = regeln.stream().map(r -> r.ID).toList();
		if (!regeln.isEmpty()) {
			switch (regelTyp) {
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> {
					if (regelParameter == null)
						throw new ApiOperationException(Status.CONFLICT);
					final List<DTOGostBlockungRegelParameter> duplicates =
							conn.queryList("SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID IN ?1 AND e.Nummer = 0 AND e.Parameter = ?2",
									DTOGostBlockungRegelParameter.class, regelIDs, regelParameter.get(0));
					if (!duplicates.isEmpty())
						throw new ApiOperationException(Status.CONFLICT,
								"Es existiert bereits eine Regel zum Auffüllen des Kurses mit der ID %d.".formatted(regelParameter.get(0)));
					final long anzahl = regelParameter.get(1);
					if ((anzahl < 1) || (anzahl > 99))
						throw new ApiOperationException(Status.BAD_REQUEST, "Die Anzahl der Schüler muss mindestens 1 sein und darf 99 nicht überschreiten.");
				}
				default -> {
					/* TODO weitere Regeltypen prüfen */ }
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
		final GostFaecherManager faecher = DataGostFaecher.getFaecherManager(conn, blockung.Abi_Jahrgang);
		final Set<Long> setSchuelerIDs = schueler.stream().map(s -> s.ID).collect(Collectors.toSet());
		for (int i = 0; i < regelTyp.getParamCount(); i++) {
			final GostKursblockungRegelParameterTyp paramType = regelTyp.getParamType(i);
			final long paramValue;
			if (regelParameter == null) {
				paramValue = switch (paramType) {
					case KURSART -> GostKursart.LK.id;
					case KURS_ID -> {
						final List<DTOGostBlockungKurs> kurse = conn.queryList(DTOGostBlockungKurs.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungKurs.class, idBlockung);
						if ((kurse == null) || (kurse.isEmpty()))
							throw new ApiOperationException(Status.NOT_FOUND);
						yield kurse.get(0).ID;
					}
					case SCHIENEN_NR -> {
						final Optional<Integer> minSchiene =
								conn.queryList(DTOGostBlockungSchiene.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungSchiene.class, idBlockung).stream()
										.map(s -> s.Nummer).min(Integer::compare);
						if (minSchiene.isEmpty())
							throw new ApiOperationException(Status.NOT_FOUND);
						yield minSchiene.get();
					}
					case SCHUELER_ID -> {
						if (schueler.isEmpty())
							throw new ApiOperationException(Status.NOT_FOUND);
						yield schueler.get(0).ID;
					}
					case FACH_ID -> {
						if (faecher.faecher().isEmpty())
							throw new ApiOperationException(Status.NOT_FOUND);
						yield faecher.faecher().get(0).id;
					}
					case BOOLEAN -> 0L;
					case GANZZAHL -> 0L;
				};
			} else {
				paramValue = regelParameter.get(i);
				DataGostBlockungRegel.validateRegelParameter(conn, blockung.Abi_Jahrgang, idBlockung, setSchuelerIDs, faecher, paramType, paramValue);
			}
			final DTOGostBlockungRegelParameter param = new DTOGostBlockungRegelParameter(idRegel, i, paramValue);
			conn.transactionPersist(param);
			daten.parameter.add(paramValue);
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static void validateRegelParameter(final @NotNull DBEntityManager conn, final int abijahrgang, final long idBlockung,
			final Set<Long> setSchuelerIDs, final GostFaecherManager faecher, final GostKursblockungRegelParameterTyp paramType, final long paramValue)
			throws ApiOperationException {
		switch (paramType) {
			case KURSART -> {
				if (GostKursart.fromIDorNull((int) paramValue) == null)
					throw new ApiOperationException(Status.NOT_FOUND);
			}
			case KURS_ID -> {
				final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, paramValue);
				if (kurs == null)
					throw new ApiOperationException(Status.NOT_FOUND);
			}
			case SCHIENEN_NR -> {
				final List<DTOGostBlockungSchiene> schienen =
						conn.queryList("SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID = ?1 AND e.Nummer = ?2",
								DTOGostBlockungSchiene.class, idBlockung, paramValue);
				if (schienen.isEmpty())
					throw new ApiOperationException(Status.NOT_FOUND);
			}
			case SCHUELER_ID -> {
				if (!setSchuelerIDs.contains(paramValue))
					throw new ApiOperationException(Status.NOT_FOUND);
			}
			case FACH_ID -> {
				if (faecher.get(paramValue) == null)
					throw new ApiOperationException(Status.NOT_FOUND);
			}
			case BOOLEAN -> {
				if ((paramValue < 0) || (paramValue > 1))
					throw new ApiOperationException(Status.NOT_FOUND);
			}
			case GANZZAHL -> {
				break; // immer gültig
			}
		}
	}


	private static void validateRegel(final @NotNull DBEntityManager conn, final int abijahrgang, final long idBlockung, final Set<Long> setSchuelerIDs,
			final GostFaecherManager faecher, final GostBlockungRegel regel, final Map<Integer, List<DTOGostBlockungRegel>> mapVorhanden)
			throws ApiOperationException {
		// Prüfe ob der Regel-Typ korrekt ist
		final GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		if (regelTyp == GostKursblockungRegelTyp.UNDEFINIERT)
			throw new ApiOperationException(Status.CONFLICT, "Der Typ der Regel ist unbekannt.");
		// Prüfe, ob die Anzahl der Parameter korrekt ist
		if (regelTyp.getParamCount() != regel.parameter.size())
			throw new ApiOperationException(Status.CONFLICT);
		// Prüfe ggf., ob bereits eine identische Regel von dem Typ existiert und führe ggf. weitere Prüfungen durch
		final List<DTOGostBlockungRegel> regelnVorhanden = mapVorhanden.get(regelTyp.typ);
		if ((regelnVorhanden != null) && (!regelnVorhanden.isEmpty())) {
			switch (regelTyp) {
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> {
					final List<Long> regelIDs = regelnVorhanden.stream().map(r -> r.ID).toList();
					final List<DTOGostBlockungRegelParameter> duplicates =
							conn.queryList("SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID IN ?1 AND e.Nummer = 0 AND e.Parameter = ?2",
									DTOGostBlockungRegelParameter.class, regelIDs, regel.parameter.get(0));
					if (!duplicates.isEmpty())
						throw new ApiOperationException(Status.CONFLICT,
								"Es existiert bereits eine Regel zum Auffüllen des Kurses mit der ID %d.".formatted(regel.parameter.get(0)));
					final long anzahl = regel.parameter.get(1);
					if ((anzahl < 1) || (anzahl > 99))
						throw new ApiOperationException(Status.BAD_REQUEST, "Die Anzahl der Schüler muss mindestens 1 sein und darf 99 nicht überschreiten.");
				}
				default -> {
					/* TODO weitere Regeltypen prüfen */ }
			}
		}
		// Prüfe die Parameter der Regel.
		for (int i = 0; i < regelTyp.getParamCount(); i++) {
			final GostKursblockungRegelParameterTyp paramType = regelTyp.getParamType(i);
			final long paramValue = regel.parameter.get(i);
			DataGostBlockungRegel.validateRegelParameter(conn, abijahrgang, idBlockung, setSchuelerIDs, faecher, paramType, paramValue);
		}
	}


	private static List<GostBlockungRegel> addRegelnInternal(final @NotNull DBEntityManager conn, final DTOGostBlockung blockung,
			final List<GostBlockungRegel> regeln) throws ApiOperationException {
		if (regeln.isEmpty())
			return new ArrayList<>();
		try {
			// Bestimme schon vorhandene Regeln der Blockung
			final Map<Integer, List<DTOGostBlockungRegel>> mapVorhanden = conn.queryList(DTOGostBlockungRegel.QUERY_BY_BLOCKUNG_ID,
					DTOGostBlockungRegel.class, blockung.ID).stream().collect(Collectors.groupingBy(r -> r.Typ.typ));
			// Bestimme die ID, ab welcher die Datensätze eingefügt werden
			long idRegel = conn.transactionGetNextID(DTOGostBlockungRegel.class);
			// Bestimme die Schüler des Abiturjahrgangs, falls Regeln einen Schüler-Bezug haben
			final List<DTOSchueler> schueler = DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, blockung.Abi_Jahrgang);
			final Set<Long> setSchuelerIDs = schueler.stream().map(s -> s.ID).collect(Collectors.toSet());
			final GostFaecherManager faecher = DataGostFaecher.getFaecherManager(conn, blockung.Abi_Jahrgang);
			// Durchwandere die Regeln und füge sie nacheinander hinzu
			for (final GostBlockungRegel regel : regeln) {
				// validiere die Regel
				DataGostBlockungRegel.validateRegel(conn, blockung.Abi_Jahrgang, blockung.ID, setSchuelerIDs, faecher, regel, mapVorhanden);
				final GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
				// Füge die Regel hinzu
				regel.id = idRegel++;
				final DTOGostBlockungRegel dtoRegel = new DTOGostBlockungRegel(regel.id, blockung.ID, regelTyp);
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
			return regeln;
		} catch (final Exception exception) {
			if (exception instanceof final IllegalArgumentException e)
				throw new ApiOperationException(Status.NOT_FOUND, e);
			throw exception;
		}
	}


	/**
	 * Fügt mehrere Regeln zu einer Blockung der Gymnasialen Oberstufe hinzu. Diese müssen zunächst eine
	 * Pseudo-ID von -1 beinhalten.
	 *
	 * @param idBlockung  die ID der Blockung
	 * @param regeln      die hinzuzufügenden Regeln
	 *
	 * @return Die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response addRegeln(final long idBlockung, final List<GostBlockungRegel> regeln) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe, ob die Blockung vorhanden
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d kann für die Regel nicht gefunden werden."
				.formatted(idBlockung));
		final List<GostBlockungRegel> daten = DataGostBlockungRegel.addRegelnInternal(conn, blockung, regeln);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Löscht eine Regel einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param id   die ID der Regel
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Regel
		final DTOGostBlockungRegel regel = conn.queryByKey(DTOGostBlockungRegel.class, id);
		if (regel == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Prüfe, ob die Blockung vorhanden ist
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, regel.Blockung_ID);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d kann für die Regel nicht gefunden werden."
				.formatted(regel.Blockung_ID));
		// Bestimme die Regel-Parameter (diese werden beim Entfernen der Regel automatisch mit entfernt.
		final GostBlockungRegel daten = new GostBlockungRegel();
		daten.id = id;
		final List<DTOGostBlockungRegelParameter> params =
				conn.queryList(DTOGostBlockungRegelParameter.QUERY_BY_REGEL_ID, DTOGostBlockungRegelParameter.class, id);
		if (params == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		params.sort((a, b) -> Integer.compare(a.Nummer, b.Nummer));
		for (final DTOGostBlockungRegelParameter param : params)
			daten.parameter.add(param.Parameter);
		daten.typ = regel.Typ.typ;
		// Entferne die Regel
		conn.transactionRemove(regel);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static void deleteMultipleInternal(final @NotNull DBEntityManager conn, final List<Long> regelIDs) throws ApiOperationException {
		if (regelIDs.isEmpty())
			return;
		// Bestimme die Regeln
		final List<DTOGostBlockungRegel> regeln = conn.queryByKeyList(DTOGostBlockungRegel.class, regelIDs);
		if (regeln.size() != regelIDs.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Mindestens eine Regel wurde für die angegebenen IDs nicht gefunden.");
		// Prüfe, ob alle eingelesenen Regeln die gleiche Blockungs-ID haben
		final long idBlockung = regeln.get(0).Blockung_ID;
		for (final DTOGostBlockungRegel regel : regeln)
			if (regel.Blockung_ID != idBlockung)
				throw new ApiOperationException(Status.BAD_REQUEST, "Alle zu löschenden Regeln müssen zur gleichen Blockung gehören.");
		// Prüfe, ob die Blockung vorhanden ist
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d kann für die Regel nicht gefunden werden."
				.formatted(idBlockung));
		// Entferne die Regeln
		if (!conn.transactionRemoveAll(regeln))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler bei der Datenbank-Transaktion.");
	}

	/**
	 * Löscht mehrere Regeln einer Blockung der Gymnasialen Oberstufe
	 *
	 * @param regelIDs   die IDs der Regeln
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> regelIDs) throws ApiOperationException {
		if (regelIDs.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurden keine IDs für Regeln angegeben.");
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		DataGostBlockungRegel.deleteMultipleInternal(conn, regelIDs);
		return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Entfernt alle zum Entfernen angegebenen Blockungsregeln und fügt anschließend alle
	 * zum Hinzufügen angegebenen Blockungsregeln hinzu.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param blockung   die Blockung, bei der die Regelanpassungen vorgenommen werden sollen
	 * @param update     die zu entfernenden Regeln und die hinzuzufügenden Regeln
	 *
	 * @return die hinzugefügten Regeln mit den neuen IDs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static @NotNull List<@NotNull GostBlockungRegel> updateBlockungsregeln(final @NotNull DBEntityManager conn, final @NotNull DTOGostBlockung blockung,
			final @NotNull GostBlockungRegelUpdate update) throws ApiOperationException {
		// Entferne die Regeln
		if (!update.listEntfernen.isEmpty()) {
			final @NotNull List<@NotNull Long> listEntfernenIDs = update.listEntfernen.stream().map(r -> r.id).toList();
			DataGostBlockungRegel.deleteMultipleInternal(conn, listEntfernenIDs);
			conn.transactionFlush();
		}
		// Füge die Kurs-Schüler-Zuordnungen hinzu
		return DataGostBlockungRegel.addRegelnInternal(conn, blockung, update.listHinzuzufuegen);
	}


	/**
	 * Entfernt alle zum Entfernen angegebenen Blockungsregeln und fügt anschließend alle
	 * zum Hinzufügen angegebenen Blockungsregeln hinzu.
	 *
	 * @param idBlockung   die ID der Blockung, bei der die Regelanpassungen vorgenommen werden sollen
	 * @param update       die zu entfernenden Regeln und die hinzuzufügenden Regeln
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response updateRegeln(final Long idBlockung, final @NotNull GostBlockungRegelUpdate update) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		if (update.listEntfernen.isEmpty() && update.listHinzuzufuegen.isEmpty())
			return Response.status(Status.NO_CONTENT).build();
		// Bestimme die zugehörige Blockung
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d wurde nicht gefunden.".formatted(idBlockung));
		// Füge die Kurs-Schüler-Zuordnungen hinzu
		final List<GostBlockungRegel> daten = DataGostBlockungRegel.updateBlockungsregeln(conn, blockung, update);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
			result.add(DataGostBlockungRegel.dtoMapper.apply(regel, mapParameter.get(regel.ID)));
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
		final List<DTOGostBlockungRegel> regeln = conn.queryList(DTOGostBlockungRegel.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungRegel.class, idBlockung);
		// Bestimme die IDs dieser Regeln
		final List<Long> regelIDs = regeln.stream().map(r -> r.ID).toList();
		// Bestimme die RegelParameter dieser Regeln
		final List<DTOGostBlockungRegelParameter> parameter =
				conn.queryList(DTOGostBlockungRegelParameter.QUERY_LIST_BY_REGEL_ID, DTOGostBlockungRegelParameter.class, regelIDs);
		return DataGostBlockungRegel.getBlockungsregeln(regeln, parameter);
	}


	/**
	 * Entfernt alle Kurs-Regeln, die sich auf den Parameter kursDelete beziehen. Ist der Parameter kursTo
	 * nicht null, so werden einige dieser Regeln auf diesen Kurs übertragen. Dies kommt zum Beispiel beim Zusammenlegen
	 * von Kursen vor, wo Dummy-Schüler übertragen werden.
	 * Dabei wird nicht geprüft, ob es sich um eine Blockung mit nur einem Ergebnis handelt. Dies muss von der
	 * aufrufenden Methode sichergestellt werden!
	 *
	 * @param conn         die Datenbankverbindung
	 * @param kursDelete   der Kurs der entfernt wurde und dessen Regeln angepasst werden müssen.
	 * @param kursTo       der Kurs, aud den Regeln übertragen wurden
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void updateKursRegelnOnDelete(final DBEntityManager conn, final @NotNull DTOGostBlockungKurs kursDelete, final DTOGostBlockungKurs kursTo)
			throws ApiOperationException {
		if ((kursTo != null) && (kursTo.Blockung_ID != kursDelete.Blockung_ID))
			throw new ApiOperationException(Status.BAD_REQUEST, "Die beiden Kurse, die zusammengeführt werden sollen, gehören nicht zur gleichen Blockung.");
		final List<DTOGostBlockungRegel> regeln = conn.queryList("SELECT e FROM DTOGostBlockungRegel e WHERE e.Blockung_ID = ?1 AND e.Typ IN ?2",
				DTOGostBlockungRegel.class, kursDelete.Blockung_ID, GostKursblockungRegelTyp.getKursRegelTypen());
		if (regeln.isEmpty())
			return;
		final List<Long> regelIDs = regeln.stream().map(r -> r.ID).toList();
		final List<DTOGostBlockungRegelParameter> regelParameter =
				conn.queryList(DTOGostBlockungRegelParameter.QUERY_LIST_BY_REGEL_ID, DTOGostBlockungRegelParameter.class, regelIDs);
		final Map<Long, List<DTOGostBlockungRegelParameter>> mapRegelParameter = regelParameter.stream().collect(
				Collectors.groupingBy(p -> p.Regel_ID, Collectors.collectingAndThen(Collectors.toCollection(ArrayList::new), l -> {
					l.sort((final DTOGostBlockungRegelParameter a, final DTOGostBlockungRegelParameter b) -> Integer.compare(a.Nummer, b.Nummer));
					return l;
				})));
		final Map<Long, DTOGostBlockungRegel> mapRegelDummySuS = new HashMap<>();
		// Entferne betroffene Regeln aus der DB
		for (final DTOGostBlockungRegel regel : regeln) {
			final List<DTOGostBlockungRegelParameter> params = mapRegelParameter.get(regel.ID);
			if (params == null)
				continue;
			// Prüfe, ob die Regel sich auf den Kurs bezieht und entferne sie ggf.
			switch (regel.Typ) {
				case KURS_FIXIERE_IN_SCHIENE, KURS_SPERRE_IN_SCHIENE, KURS_MAXIMALE_SCHUELERANZAHL, KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN -> {
					final DTOGostBlockungRegelParameter param = params.get(0);
					if (param.Nummer != 0)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Bei Regel %d kann der Regel-Parameter nicht fehlerfrei bestimmt werden.".formatted(regel.ID));
					if (kursDelete.ID == param.Parameter)
						conn.transactionRemove(regel);
				}
				case SCHUELER_FIXIEREN_IN_KURS, SCHUELER_VERBIETEN_IN_KURS -> {
					final DTOGostBlockungRegelParameter param = params.get(1);
					if (param.Nummer != 1)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Bei Regel %d kann der Regel-Parameter nicht fehlerfrei bestimmt werden.".formatted(regel.ID));
					if (kursDelete.ID == param.Parameter)
						conn.transactionRemove(regel);
				}
				case KURS_VERBIETEN_MIT_KURS, KURS_ZUSAMMEN_MIT_KURS -> {
					final DTOGostBlockungRegelParameter param1 = params.get(0);
					final DTOGostBlockungRegelParameter param2 = params.get(1);
					if ((param1.Nummer != 0) || (param2.Nummer != 1))
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Bei Regel %d kann der Regel-Parameter nicht fehlerfrei bestimmt werden.".formatted(regel.ID));
					if ((kursDelete.ID == param1.Parameter) || (kursDelete.ID == param2.Parameter))
						conn.transactionRemove(regel);
				}
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN -> {
					final DTOGostBlockungRegelParameter param = params.get(0);
					if (param.Nummer != 0)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Bei Regel %d kann der Regel-Parameter nicht fehlerfrei bestimmt werden.".formatted(regel.ID));
					if (kursTo == null) {
						conn.transactionRemove(regel);
					} else {
						if (kursDelete.ID == param.Parameter)
							mapRegelDummySuS.put(kursDelete.ID, regel);
						if (kursTo.ID == param.Parameter)
							mapRegelDummySuS.put(kursTo.ID, regel);
					}
				}
				default -> throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Der Regel-Typ wird noch nicht beim Entfernen der Regeln zu einem Kurs unterstützt, obwohl dieser sich (auch) auf Kurse bezieht.");
			}
		}
		conn.transactionFlush();
		// Füge ggf. Regeln mit Dummy SuS in kursTo ein
		if ((kursTo != null) && (mapRegelDummySuS.containsKey(kursDelete.ID))) {
			final DTOGostBlockungRegel regelEntfernt = mapRegelDummySuS.get(kursDelete.ID);
			if (mapRegelDummySuS.containsKey(kursTo.ID)) {
				final DTOGostBlockungRegel regelVorhanden = mapRegelDummySuS.get(kursTo.ID);
				final List<DTOGostBlockungRegelParameter> paramsVorhanden = mapRegelParameter.get(regelVorhanden.ID);
				final List<DTOGostBlockungRegelParameter> paramsEntfernt = mapRegelParameter.get(regelEntfernt.ID);
				if ((paramsVorhanden != null) && (paramsEntfernt != null)) {
					final DTOGostBlockungRegelParameter paramVorhanden = paramsVorhanden.get(1);
					final DTOGostBlockungRegelParameter paramEntfernt = paramsEntfernt.get(1);
					if ((paramVorhanden.Nummer != 1) && (paramEntfernt.Nummer != 1))
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Bei Regel %d kann der Regel-Parameter nicht fehlerfrei bestimmt werden.".formatted(regelVorhanden.ID));
					paramVorhanden.Parameter += paramEntfernt.Parameter;
					conn.transactionPersist(paramVorhanden);
				}
				conn.transactionRemove(regelEntfernt);
			} else {
				final List<DTOGostBlockungRegelParameter> paramsEntfernt = mapRegelParameter.get(regelEntfernt.ID);
				if (paramsEntfernt != null) {
					final DTOGostBlockungRegelParameter paramEntfernt = paramsEntfernt.get(0);
					if (paramEntfernt.Nummer != 0)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Bei Regel %d kann der Regel-Parameter nicht fehlerfrei bestimmt werden.".formatted(regelEntfernt.ID));
					paramEntfernt.Parameter = kursTo.ID;
					conn.transactionPersist(paramEntfernt);
					conn.transactionFlush();
				}
			}
		}
	}

}
