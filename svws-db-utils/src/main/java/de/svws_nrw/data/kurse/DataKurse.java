package de.svws_nrw.data.kurse;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.kurse.KursMapper;
import de.svws_nrw.service.kurse.KursService;
import de.svws_nrw.service.kurse.KursServiceFactory;
import jakarta.validation.constraints.NotNull;
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
		return getKursdaten(id);
	}

	@Override
	protected long getLongId(final DTOKurs kurs) {
		return kurs.ID;
	}

	@Override
	protected void initDTO(final DTOKurs dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
		dto.WochenStd = 3;
		dto.Fortschreibungsart = KursFortschreibungsart.KEINE;
		dto.EpochU = false;
	}

	@Override
	protected KursDaten map(final DTOKurs dto) {
		final var daten = KursMapper.INSTANCE.toKursDaten(dto);
		daten.weitereLehrer.addAll(new DataKursLehrer(conn, dto.ID).getList());
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOKurs dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idSchuljahresabschnitt" -> dto.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false, name);
			case "idFach" -> mapIdFach(dto, name, value);
			case "idLehrer" -> mapLehrer(dto, name, value);
			case "kuerzel" -> dto.KurzBez =
					JSONMapper.convertToString(value, false, false, Schema.tab_Kurse.col_KurzBez.datenlaenge(), name);
			case "kursartAllg" -> // TODO Prüfe Kursart
				dto.KursartAllg = JSONMapper.convertToString(value, false, true, Schema.tab_Kurse.col_KursartAllg.datenlaenge(), name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "wochenstunden" -> dto.WochenStd = JSONMapper.convertToIntegerInRange(value, false, 0, 40, name);
			case "wochenstundenLehrer" -> dto.WochenstdKL = Optional.ofNullable(JSONMapper.convertToDouble(value, true, name)).orElse(0.0);
			case "idKursFortschreibungsart" -> dto.Fortschreibungsart =
					KursFortschreibungsart.fromID(JSONMapper.convertToIntegerInRange(value, false, 0, 4, name));
			case "schulnummer" -> mapSchulnummer(dto, name, value);
			case "istEpochalunterricht" -> dto.EpochU = JSONMapper.convertToBoolean(value, false, name);
			case "bezeichnungZeugnis" -> dto.ZeugnisBez =
					JSONMapper.convertToString(value, true, true, Schema.tab_Kurse.col_ZeugnisBez.datenlaenge(), name);
			case "schienen" -> mapSchienen(dto, name, value);
			case "idJahrgaenge" -> mapIdJahrgaenge(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	private void mapIdJahrgaenge(final DTOKurs dto, final String name, final Object value) throws ApiOperationException {
		final List<Long> neu = JSONMapper.convertToListOfLong(value, false, name);
		final List<Long> vorher = KursMapper.INSTANCE.mapIdJahrgaenge(dto);
		boolean changed = (neu.size() != vorher.size());
		for (final long n : neu) {
			if (!vorher.contains(n)) {
				changed = true;
				break;
			}
		}
		if (!changed) {
			return;
		}
		if (neu.isEmpty()) {
			dto.ASDJahrgang = null;
			dto.Jahrgang_ID = null;
			dto.Jahrgaenge = null;
		} else {
			final List<DTOJahrgang> dtoJahrgaenge = conn.queryByKeyList(DTOJahrgang.class, neu);
			if (dtoJahrgaenge.size() != neu.size()) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Mindestens einer der angegebenen Jahrgang-IDs existiert nicht in der SVWS-Datenbank");
			}
			if (neu.size() > 1) {
				dto.ASDJahrgang = null;
				dto.Jahrgang_ID = null;
				dto.Jahrgaenge = neu.stream().map(Object::toString).collect(Collectors.joining(","));
			} else {
				dto.Jahrgang_ID = neu.getFirst();
				dto.ASDJahrgang = dtoJahrgaenge.getFirst().ASDJahrgang;
				dto.Jahrgaenge = null;
			}
		}
	}

	private void mapSchienen(final DTOKurs dto, final String name, final Object value) throws ApiOperationException {
		final List<Integer> neu = JSONMapper.convertToListOfInteger(value, false, name);
		final List<Integer> vorher = KursMapper.INSTANCE.mapSchienen(dto.Schienen);
		boolean changed = (neu.size() != vorher.size());
		for (final int n : neu) {
			if (n < 0) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Eine Schienen-Nummer kleiner als 0 ist nicht zulässig.");
			}
			if (!vorher.contains(n)) {
				changed = true;
			}
		}
		if (changed) {
			dto.Schienen = neu.stream().map(Object::toString).collect(Collectors.joining(","));
		}
	}

	private void mapSchulnummer(final DTOKurs dto, final String name, final Object value) throws ApiOperationException {
		final Integer schulnummer = JSONMapper.convertToIntegerInRange(value, true, 100000, 999999, name);
		if (schulnummer == null) {
			dto.SchulNr = null;
			return;
		}
		final List<DTOSchuleNRW> schulen = this.conn.queryList(DTOSchuleNRW.QUERY_BY_SCHULNR, DTOSchuleNRW.class, schulnummer);
		if (schulen.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Für die Schulnummer %d konnte keine passende Schule gefunden werden.".formatted(schulnummer));
		}

		dto.SchulNr = schulnummer;
	}

	private void mapLehrer(final DTOKurs dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, name);
		if (id == null) {
			dto.Lehrer_ID = null;
		} else {
			final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
			if (lehrer == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Lehrer mit der angegebenen ID %d gefunden werden.".formatted(id));
			}
			dto.Lehrer_ID = id;
		}

		// Übertrage die Änderungen auch auf die Leistungsdaten der Schüler in dem Kurs
		final List<DTOSchuelerLeistungsdaten> listLeistungsdaten = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_BY_KURS_ID, DTOSchuelerLeistungsdaten.class, dto.ID);
		if (listLeistungsdaten.isEmpty()) {
			return;
		}
		for (final DTOSchuelerLeistungsdaten leistungsdaten : listLeistungsdaten) {
			if (!Objects.equals(leistungsdaten.Fachlehrer_ID, dto.Lehrer_ID)) {
				leistungsdaten.Fachlehrer_ID = dto.Lehrer_ID;
				conn.transactionPersist(leistungsdaten);
			}
		}
	}

	private void mapIdFach(final DTOKurs dto, final String name, final Object value) throws ApiOperationException {
		final Long idFach = JSONMapper.convertToLong(value, true, name);
		if (idFach == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Faches darf nicht null sein.");
		}

		final DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
		if (fach == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Fach mit der angegebenen ID gefunden werden.");
		}

		dto.Fach_ID = idFach;
	}


	/**
	 * Ermittelt die Daten zu dem Kurs mit der angegebenen ID.
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die Daten des Kurses
	 */
	public static KursDaten getKursdaten(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Kurses darf nicht null sein.");
		}
		return getKursService().getById(id);
	}

	/**
	 * Ermittelt die Kurse zu dem Schuljahresabschnitt mit der angegebenen ID.
	 *
	 * @param idSchuljahresabschnitt	die ID des Schuljahresabschnitts
	 * @param attachSchueler			gibt an, ob die Schüler zu den Kursen mit geladen werden sollen
	 *
	 * @return eine Liste mit den Kursen des Schuljahresabschnitts
	 */
	public List<KursDaten> getListBySchuljahresabschnittID(final Long idSchuljahresabschnitt, final boolean attachSchueler) {
		return getKursService().getListBySchuljahresabschnitt(idSchuljahresabschnitt, attachSchueler);
	}

	/**
	 * Ermittelt die Kurse zu den übergebenen Kurs-IDs.
	 *
	 * @param ids				die IDs der Kurse
	 * @param attachSchueler	gibt an, ob die Schüler zu den Kursen mit geladen werden sollen
	 *
	 * @return eine Liste mit den Kursen zu den übergebenen IDs
	 */
	public List<KursDaten> getListByIDs(final List<Long> ids, final boolean attachSchueler) {
		return getKursService().getListByIds(ids, attachSchueler);
	}

	/**
	 * Bestimmt die Liste der Kurse für den angegeben Abschnitt. Ist dieser Abschnitt null, so werden die Kurse
	 * aller Abschnitte zurückgegeben. Dabei kann gewählt werden, ob die Schülerlisten zu den Kursen mitbestimmt werden
	 * sollen oder nicht.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param mitSchuelerListe         gibt an, ob die Kurslisten-Einträge die Information zu Schülern beinhalten soll
	 *
	 * @return die Liste der Kurse
	 */
	public static @NotNull List<KursDaten> getKursListenFuerAbschnitt(final Long idSchuljahresabschnitt, final boolean mitSchuelerListe) {
		return getKursService().getListBySchuljahresabschnitt(idSchuljahresabschnitt, mitSchuelerListe);
	}

	private static KursService getKursService() {
		return KursServiceFactory.getNewInstance().getKursService();
	}

}
