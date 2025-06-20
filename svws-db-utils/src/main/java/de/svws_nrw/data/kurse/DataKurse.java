package de.svws_nrw.data.kurse;

import de.svws_nrw.db.schema.Schema;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Data-Manager für Kurse {@link KursDaten}.
 */
public final class DataKurse extends DataManagerRevised<Long, DTOKurs, KursDaten> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KursDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKurse(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("idSchuljahresabschnitt", "kuerzel", "idFach", "kursartAllg");
		setAttributesNotPatchable("id", "idSchuljahresabschnitt");
	}


	/**
	 * Gibt die Daten eines Kurses zu dessen ID zurück.
	 *
	 * @param id   die ID des Kurses.
	 *
	 * @return die Daten des Kurses zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public KursDaten getById(final Long id) throws ApiOperationException {
		return getKursdaten(conn, id);
	}

	@Override
	protected long getLongId(final DTOKurs kurs) throws ApiOperationException {
		return kurs.ID;
	}

	@Override
	protected KursDaten map(final DTOKurs dto) throws ApiOperationException {
		return mapInternal(dto);
	}


	/**
	 * Führt das Mapping von einem Datenbank-DTO auf ein Core-DTO aus, um dieses zu
	 * initialisieren. Weitere Angaben, wie z.B. die Schülerliste müssen anschließend noch
	 * ergänzt werden.
	 *
	 * @param dto   das Datenbank-DTO
	 *
	 * @return das initialisierte Core-DTO
	 */
	private static KursDaten mapInternal(final DTOKurs dto) {
		final KursDaten daten = new KursDaten();
		daten.id = dto.ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.kuerzel = dto.KurzBez;
		daten.idJahrgaenge.addAll(convertJahrgaenge(dto));
		daten.idFach = dto.Fach_ID;
		daten.lehrer = dto.Lehrer_ID;
		daten.kursartAllg = (dto.KursartAllg == null) ? "" : dto.KursartAllg;
		daten.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		daten.istSichtbar = dto.Sichtbar;
		daten.schienen.addAll(convertSchienenStrToList(dto.Schienen));
		daten.wochenstunden = (dto.WochenStd == null) ? 0 : dto.WochenStd;
		daten.wochenstundenLehrer = (dto.WochenstdKL == null) ? daten.wochenstunden : dto.WochenstdKL;
		daten.idKursFortschreibungsart = dto.Fortschreibungsart.id;
		daten.schulnummer = dto.SchulNr;
		daten.istEpochalunterricht = dto.EpochU;
		daten.bezeichnungZeugnis = dto.ZeugnisBez;
		if ((daten.bezeichnungZeugnis != null) && daten.bezeichnungZeugnis.isBlank())
			daten.bezeichnungZeugnis = null;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOKurs dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idSchuljahresabschnitt" -> dto.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false, "idSchuljahresabschnitt");
			case "idFach" -> {
				final Long idFach = JSONMapper.convertToLong(value, true, "idFach");
				if (idFach == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Faches darf nicht null sein.");
				final DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
				if (fach == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Fach mit der angegebenen ID gefunden werden.");
				dto.Fach_ID = idFach;
			}
			case "lehrer" -> {
				dto.Lehrer_ID = JSONMapper.convertToLong(value, true, "lehrer");
				if (dto.Lehrer_ID != null) {
					final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, dto.Lehrer_ID);
					if (lehrer == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Lehrer mit der angegebenen ID gefunden werden.");
				}
			}
			case "kuerzel" -> dto.KurzBez =
					JSONMapper.convertToString(value, false, false, Schema.tab_Kurse.col_KurzBez.datenlaenge(), "kuerzel");
			case "kursartAllg" -> {
				dto.KursartAllg = JSONMapper.convertToString(value, false, true, Schema.tab_Kurse.col_KursartAllg.datenlaenge(), "kursartAllg");
				// TODO Prüfe Kursart
			}
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE, "sortierung");
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, "istSichtbar");
			case "wochenstunden" -> dto.WochenStd = JSONMapper.convertToIntegerInRange(value, false, 0, 40, "wochenstunden");
			case "wochenstundenLehrer" -> {
				dto.WochenstdKL = JSONMapper.convertToDouble(value, true, "wochenstundenLehrer");
				if (dto.WochenstdKL == null)
					dto.WochenstdKL = 0.0;
			}
			case "idKursFortschreibungsart" -> dto.Fortschreibungsart =
					KursFortschreibungsart.fromID(JSONMapper.convertToIntegerInRange(value, false, 0, 4, "idKursFortschreibungsart"));
			case "schulnummer" -> {
				dto.SchulNr = JSONMapper.convertToIntegerInRange(value, true, 100000, 999999, "schulnummer");
				// TODO Prüfe die Schulnummer anhand des Katalogs
			}
			case "istEpochalunterricht" -> dto.EpochU = JSONMapper.convertToBoolean(value, false, "istEpochalunterricht");
			case "bezeichnungZeugnis" -> dto.ZeugnisBez =
					JSONMapper.convertToString(value, true, true, Schema.tab_Kurse.col_ZeugnisBez.datenlaenge(), "bezeichnungZeugnis");
			case "schienen" -> {
				final List<Integer> neu = JSONMapper.convertToListOfInteger(value, false);
				final List<Integer> vorher = convertSchienenStrToList(dto.Schienen);
				boolean changed = (neu.size() != vorher.size());
				for (final int n : neu) {
					if (n < 0)
						throw new ApiOperationException(Status.BAD_REQUEST, "Eine Schienen-Nummer kleiner als 0 ist nicht zulässig.");
					if (!vorher.contains(n))
						changed = true;
				}
				if (changed) {
					dto.Schienen = neu.stream().map(Object::toString).collect(Collectors.joining(","));
				}
			}
			case "idJahrgaenge" -> {
				final List<Long> neu = JSONMapper.convertToListOfLong(value, false);
				final List<Long> vorher = convertJahrgaenge(dto);
				boolean changed = (neu.size() != vorher.size());
				for (final long n : neu) {
					if (!vorher.contains(n)) {
						changed = true;
						break;
					}
				}
				if (!changed)
					return;
				if (neu.isEmpty()) {
					dto.ASDJahrgang = null;
					dto.Jahrgang_ID = null;
					dto.Jahrgaenge = null;
				} else {
					final List<DTOJahrgang> dtoJahrgaenge = conn.queryByKeyList(DTOJahrgang.class, neu);
					if (dtoJahrgaenge.size() != neu.size())
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Mindestens einer der angegebenen Jahrgang-IDs existiert nicht in der SVWS-Datenbank");
					if (neu.size() > 1) {
						dto.ASDJahrgang = null;
						dto.Jahrgang_ID = null;
						dto.Jahrgaenge = neu.stream().map(Object::toString).collect(Collectors.joining(","));
					} else {
						dto.Jahrgang_ID = neu.get(0);
						dto.ASDJahrgang = dtoJahrgaenge.get(0).ASDJahrgang;
						dto.Jahrgaenge = null;
					}
				}
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}


	@Override
	protected void initDTO(final DTOKurs dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newId;
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
		dto.WochenStd = 3;
		dto.Fortschreibungsart = KursFortschreibungsart.KEINE;
		dto.EpochU = false;
	}


	/**
	 * Ermittelt für die angegebenen Kurse alle Kurs-Schüler und gibt die Listen für die einzelnen Kurse
	 * in einer Map zugeordnet zu ihrer Kurs-ID zurück. Bei leeren Kursen wird eine leere Liste zugeordnet.
	 *
	 * @param conn       die Datenbank-Verbindung
	 * @param idsKurse   die IDs der Kurse
	 *
	 * @return die Map mit der Kurs-Schüler-Zuordnung
	 */
	private static @NotNull Map<Long, List<Schueler>> getMapKursSchuelerByKursID(final DBEntityManager conn, final @NotNull List<Long> idsKurse) {
		final @NotNull Map<Long, List<Schueler>> result = idsKurse.stream().collect(Collectors.toMap(id -> id, id -> new ArrayList<>()));
		if (idsKurse.isEmpty())
			return result;
		final List<DTOKursSchueler> listKursSchueler =
				conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, idsKurse);
		final List<Long> schuelerIDs = listKursSchueler.stream().map(ks -> ks.Schueler_ID).toList();
		final List<DTOSchueler> listSchueler =
				((schuelerIDs == null) || (schuelerIDs.isEmpty())) ? new ArrayList<>() : conn.queryByKeyList(DTOSchueler.class, schuelerIDs);
		final Map<Long, Schueler> mapSchueler = listSchueler.stream()
				.filter(dto -> Boolean.FALSE.equals(dto.Geloescht))
				.map(dto -> DataSchuelerliste.mapToSchueler(dto, null))
				.collect(Collectors.toMap(s -> s.id, s -> s));
		for (final @NotNull DTOKursSchueler kursSchueler : listKursSchueler)
			result.get(kursSchueler.Kurs_ID).add(mapSchueler.get(kursSchueler.Schueler_ID));
		return result;
	}


	/**
	 * Ermittelt für die angegebenen Kurse alle zusätzlichen Kurslehrer und gibt die Listen für die einzelnen Kurse
	 * in einer Map zugeordnet zu ihrer Kurs-ID zurück. Existieren keine zusätzlichen Kurslehrer, so wird eine
	 * leere Liste zugeordnet.
	 *
	 * @param conn       die Datenbank-Verbindung
	 * @param idsKurse   die IDs der Kurse
	 *
	 * @return die Map mit der Zuordnung weiterer Kurslehrer
	 */
	private static @NotNull Map<Long, List<KursLehrer>> getMapKursLehrerByKursID(final DBEntityManager conn, final @NotNull List<Long> idsKurse) {
		final @NotNull Map<Long, List<KursLehrer>> result = idsKurse.stream().collect(Collectors.toMap(id -> id, id -> new ArrayList<>()));
		if (idsKurse.isEmpty())
			return result;
		final List<DTOKursLehrer> listKursLehrer =
				conn.queryList("SELECT e FROM DTOKursLehrer e WHERE e.Kurs_ID IN ?1", DTOKursLehrer.class, idsKurse);
		for (final @NotNull DTOKursLehrer dto : listKursLehrer) {
			final KursLehrer lehrer = new KursLehrer();
			lehrer.idLehrer = dto.Lehrer_ID;
			lehrer.wochenstundenLehrer = dto.Anteil;
			result.get(dto.Kurs_ID).add(lehrer);
		}
		return result;
	}


	/**
	 * Ermittelt die Daten zu dem Kurs mit der angegebenen ID.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id     die ID des Kurses
	 *
	 * @return die Daten des Kurses
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static KursDaten getKursdaten(final DBEntityManager conn, final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOKurs kurs = conn.queryByKey(DTOKurs.class, id);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final KursDaten daten = mapInternal(kurs);
		// Bestimme die Schüler des Kurses
		daten.schueler.addAll(getMapKursSchuelerByKursID(conn, List.of(daten.id)).get(daten.id));
		// Bestimme die Zusatzkräfte des Kurses
		daten.weitereLehrer.addAll(getMapKursLehrerByKursID(conn, List.of(daten.id)).get(daten.id));
		return daten;
	}


	/**
	 * Wandelt den übergebenen String aus komma-separierten Schienenlisten in eine Liste der
	 * Schienen-Nummern um.
	 *
	 * @param strSchienen   der String mit der Schienenliste
	 *
	 * @return die Liste der Schienennummern
	 */
	public static List<Integer> convertSchienenStrToList(final String strSchienen) {
		final List<Integer> result = new ArrayList<>();
		if ((strSchienen != null) && (!strSchienen.isBlank())) {
			for (final String strSchiene : strSchienen.split(",")) {
				if ("".equals(strSchiene.trim()))
					continue;
				try {
					result.add(Integer.parseInt(strSchiene.trim()));
				} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
					// ignore exception
				}
			}
		}
		return result;
	}


	private static List<Long> convertJahrgaenge(final DTOKurs kurs) {
		final List<Long> result = new ArrayList<>();
		if (kurs.Jahrgang_ID != null)
			result.add(kurs.Jahrgang_ID);
		if (kurs.Jahrgaenge != null)
			for (final String jahrgang : kurs.Jahrgaenge.split(","))
				if (jahrgang.matches("^\\d+$"))
					result.add(Long.parseLong(jahrgang));
		return result;
	}


	/**
	 * Ermittelt die Kurse zu dem Schuljahresabschnitt mit der angegebenen ID.
	 *
	 * @param idSchuljahresabschnitt	die ID des Schuljahresabschnitts
	 * @param attachSchueler			gibt an, ob die Schüler zu den Kursen mit geladen werden sollen
	 *
	 * @return eine Liste mit den Kursen des Schuljahresabschnitts
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<KursDaten> getListBySchuljahresabschnittID(final Long idSchuljahresabschnitt, final boolean attachSchueler) throws ApiOperationException {
		if (idSchuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<DTOKurs> kurse = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt);
		return getKurseDaten(kurse, attachSchueler);
	}

	/**
	 * Ermittelt die Kurse zu den übergebenen Kurs-IDs.
	 *
	 * @param ids				die ID des Schuljahresabschnitts
	 * @param attachSchueler	gibt an, ob die Schüler zu den Kursen mit geladen werden sollen
	 *
	 * @return eine Liste mit den Kursen zu den übergebenen IDs
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<KursDaten> getListByIDs(final List<Long> ids, final boolean attachSchueler) throws ApiOperationException {
		if (ids == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<DTOKurs> kurse = conn.queryByKeyList(DTOKurs.class, ids);
		return getKurseDaten(kurse, attachSchueler);
	}

	/**
	 * Ermittelt die Kursdaten der übergebenen Kurs-DTOs
	 *
	 * @param dtoKurse			die DTOs der Kurse
	 * @param attachSchueler	gibt an, ob die Schüler zu den Kursen mit geladen werden sollen
	 *
	 * @return eine Liste der Kursdaten zu den DTOs der Kurse
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private List<KursDaten> getKurseDaten(final List<DTOKurs> dtoKurse, final boolean attachSchueler) throws ApiOperationException {
		if ((dtoKurse == null) || dtoKurse.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);

		// Bestimme die IDs der Kurse
		final List<Long> idsKurse = dtoKurse.stream().map(k -> k.ID).toList();
		final Map<Long, List<Schueler>> mapKursSchueler = attachSchueler ? getMapKursSchuelerByKursID(conn, idsKurse) : new HashMap<>();
		final Map<Long, List<KursLehrer>> mapKursLehrer = getMapKursLehrerByKursID(conn, idsKurse);
		final List<KursDaten> kurseDaten = new ArrayList<>();
		for (final DTOKurs kurs : dtoKurse) {
			final KursDaten kursdaten = map(kurs);
			// Bestimme die Schüler der Kurse, wenn gewünscht.
			if (attachSchueler)
				kursdaten.schueler.addAll(mapKursSchueler.get(kurs.ID));
			// Bestimme die Zusatzkräfte des Kurses
			kursdaten.weitereLehrer.addAll(mapKursLehrer.get(kurs.ID));
			kurseDaten.add(kursdaten);
		}
		return kurseDaten;
	}


	/**
	 * Bestimmt die Liste der Kurse für den angegeben Abschnitt. Ist dieser Abschnitt null, so werden die Kurse
	 * aller Abschnitte zurückgegeben. Dabei kann gewählt werden, ob die Schülerlisten zu den Kursen mitbestimmt werden
	 * sollen oder nicht.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param mitSchuelerListe         gibt an, ob die Kurslisten-Einträge die Information zu Schülern beinhalten soll
	 *
	 * @return die Liste der Kurse
	 */
	public static @NotNull List<KursDaten> getKursListenFuerAbschnitt(final DBEntityManager conn,
			final Long idSchuljahresabschnitt, final boolean mitSchuelerListe) {
		final @NotNull List<DTOKurs> kurse = (idSchuljahresabschnitt == null)
				? conn.queryAll(DTOKurs.class)
				: conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt);
		if (kurse.isEmpty())
			return new ArrayList<>();
		// Erstelle die Liste der Kurse
		final @NotNull List<KursDaten> daten = new ArrayList<>();
		for (final @NotNull DTOKurs dtoKurs : kurse)
			daten.add(mapInternal(dtoKurs));
		daten.sort((a, b) -> Long.compare(a.sortierung, b.sortierung));
		if (!mitSchuelerListe)
			return daten;
		// Ergänze die Liste der Schüler in den Kursen
		final List<Long> kursIDs = daten.stream().map(k -> k.id).toList();
		final List<DTOKursSchueler> listKursSchueler = conn.queryList(
				"SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, kursIDs);
		final List<Long> schuelerIDs = listKursSchueler.stream().map(ks -> ks.Schueler_ID).toList();
		final Map<Long, DTOSchueler> mapSchueler = conn.queryByKeyList(DTOSchueler.class, schuelerIDs).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		final HashMap<Long, List<Schueler>> mapKursSchueler = new HashMap<>();
		for (final DTOKursSchueler ks : listKursSchueler) {
			final DTOSchueler dtoSchueler = mapSchueler.get(ks.Schueler_ID);
			if ((dtoSchueler == null) || (Boolean.TRUE.equals(dtoSchueler.Geloescht)))
				continue;
			List<Schueler> listSchueler = mapKursSchueler.get(ks.Kurs_ID);
			if (listSchueler == null) {
				listSchueler = new ArrayList<>();
				mapKursSchueler.put(ks.Kurs_ID, listSchueler);
			}
			listSchueler.add(DataSchuelerliste.mapToSchueler(dtoSchueler, null));   // TODO Abschlussjahrgang bestimmen
		}
		for (final KursDaten eintrag : daten) {
			final List<Schueler> listSchueler = mapKursSchueler.get(eintrag.id);
			if (listSchueler != null)
				eintrag.schueler.addAll(listSchueler);
		}
		return daten;
	}


	/**
	 * Bestimmt die Liste der Kurse für den angegeben Abschnitt. Ist dieser Abschnitt null, so werden die Kurse
	 * aller Abschnitte zurückgegeben. Dabei kann gewählt werden, ob die Schülerlisten zu den Kursen mitbestimmt werden
	 * sollen oder nicht.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param mitSchuelerListe         gibt an, ob die Kurslisten-Einträge die Information zu Schülern beinhalten soll
	 *
	 * @return die Liste der Kurse
	 */
	public static Response getKurslistenFuerAbschnittAsResponse(final DBEntityManager conn, final Long idSchuljahresabschnitt, final boolean mitSchuelerListe) {
		final @NotNull List<de.svws_nrw.asd.data.kurse.KursDaten> daten = DataKurse.getKursListenFuerAbschnitt(conn, idSchuljahresabschnitt, mitSchuelerListe);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
