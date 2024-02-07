package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerLeistungsdaten}.
 */
public final class DataSchuelerLeistungsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerLeistungsdaten}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLeistungsdaten(final DBEntityManager conn) {
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
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuelerLeistungsdaten} in einen Core-DTO {@link SchuelerLeistungsdaten}.
	 */
	private final Function<DTOSchuelerLeistungsdaten, SchuelerLeistungsdaten> dtoMapper = (final DTOSchuelerLeistungsdaten dto) -> {
		final SchuelerLeistungsdaten daten = new SchuelerLeistungsdaten();
		daten.id = dto.ID;
		daten.lernabschnittID = dto.Abschnitt_ID;
		daten.fachID = dto.Fach_ID;
		daten.kursID = dto.Kurs_ID;
		daten.kursart = dto.Kursart == null ? ZulaessigeKursart.PUK.daten.kuerzel : dto.Kursart;
		try {
			daten.abifach = dto.AbiFach == null ? null : Integer.parseInt(dto.AbiFach);
		} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
			daten.abifach = null;
		}
		daten.istZP10oderZK10 = dto.Prf10Fach != null && dto.Prf10Fach;
		daten.koopSchule = dto.SchulNr;
		daten.lehrerID = dto.Fachlehrer_ID;
		daten.wochenstunden = dto.Wochenstunden == null ? 0 : dto.Wochenstunden;
		daten.zusatzkraftID = dto.Zusatzkraft_ID;
		daten.zusatzkraftWochenstunden = dto.WochenstdZusatzkraft == null ? 0 : dto.WochenstdZusatzkraft;
		daten.aufZeugnis = dto.AufZeugnis == null || dto.AufZeugnis;
		daten.note = dto.NotenKrz == null ? Note.KEINE.kuerzel : dto.NotenKrz.kuerzel;
		daten.noteQuartal = dto.NotenKrzQuartal == null ? Note.KEINE.kuerzel : dto.NotenKrzQuartal.kuerzel;
		daten.istGemahnt = dto.Warnung != null && dto.Warnung; // TODO bestimme ggf. aus Halbjahr zuvor
		daten.mahndatum = dto.Warndatum;
		daten.istEpochal = dto.VorherAbgeschl != null && dto.VorherAbgeschl;
		daten.geholtJahrgangAbgeschlossen = dto.AbschlussJahrgang;
		daten.gewichtungAllgemeinbildend = dto.Gewichtung == null ? 1 : dto.Gewichtung;
		daten.noteBerufsabschluss = dto.NoteAbschlussBA;
		daten.textFachbezogeneLernentwicklung = dto.Lernentw == null ? "" : dto.Lernentw;
		daten.umfangLernstandsbericht = dto.Umfang == null ? "" : dto.Umfang;
		daten.fehlstundenGesamt = dto.FehlStd == null ? 0 : dto.FehlStd;
		daten.fehlstundenUnentschuldigt = dto.uFehlStd == null ? 0 : dto.uFehlStd;
		return daten;
	};


	/**
	 * Ermittelt die Leistungsdaten für den angegebenen Lernabschnitt und fügt diese in die übergebene Liste ein.
	 *
	 * @param abschnittID   die ID des Lernabschnitts
	 * @param list          die Liste, in die eingefügt wird
	 *
	 * @return eine Liste mit der Leistungsdaten des Lernabschnitts
	 */
	public boolean getByLernabschnitt(final Long abschnittID, final List<SchuelerLeistungsdaten> list) {
    	// Bestimme die Leistungsdaten des Lernabschnitts
    	final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", abschnittID, DTOSchuelerLeistungsdaten.class);
    	if (leistungsdaten == null)
    		return false;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (final DTOSchuelerLeistungsdaten l : leistungsdaten)
    		list.add(dtoMapper.apply(l));
    	return true;
	}


	@Override
	public Response get(final Long id) {
		// Prüfe, ob die Leistungsdaten mit der ID existieren
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOSchuelerLeistungsdaten dto = conn.queryByKey(DTOSchuelerLeistungsdaten.class, id);
    	if (dto == null)
    		return OperationError.NOT_FOUND.getResponse("Die Leistungsdaten mit der ID %d wurden in der Datenbank nicht gefunden".formatted(id));
		final SchuelerLeistungsdaten daten = dtoMapper.apply(dto);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Map<String, DataBasicMapper<DTOSchuelerLeistungsdaten>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("lernabschnittID", (conn, dto, value, map) -> {
			final long idAbschnitt = JSONMapper.convertToLong(value, false);
			if (conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, idAbschnitt) == null)
				throw OperationError.CONFLICT.exception();
			dto.Abschnitt_ID = idAbschnitt;
		}),
		Map.entry("fachID", (conn, dto, value, map) -> {
			final long idFach = JSONMapper.convertToLong(value, false);
			final DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
			if (fach == null)
				throw OperationError.CONFLICT.exception();
			dto.Fach_ID = idFach;
			dto.Sortierung = fach.SortierungAllg;
			dto.Kurs_ID = null;
		}),
		Map.entry("kursID", (conn, dto, value, map) -> {
			final Long idKurs = JSONMapper.convertToLong(value, true);
			if (idKurs != null)	{
				/// Prüfe, ob der Kurs existiert und passe ggf. Fachlehrer und Kursart an.
				final DTOKurs kurs = conn.queryByKey(DTOKurs.class, idKurs);
				if (kurs == null)
					throw OperationError.CONFLICT.exception();
				// Setze Fachlehrer
				dto.Fachlehrer_ID = kurs.Lehrer_ID;
				// Passe ggf. die Kursart an, wenn sie sich geändert hat
				if ((kurs.KursartAllg != null) && (!kurs.KursartAllg.equals(dto.KursartAllg))) {
					final @NotNull List<@NotNull ZulaessigeKursart> kursarten = ZulaessigeKursart.getByAllgemeinerKursart(kurs.KursartAllg);
					dto.KursartAllg = kurs.KursartAllg;
					if (kurs.KursartAllg.equals(ZulaessigeKursart.E.daten.kuerzel)) { // Speziallfall Gesamtschule E-Kurs
						dto.Kursart = ZulaessigeKursart.E.daten.kuerzel;
					} else if (kurs.KursartAllg.equals(ZulaessigeKursart.G.daten.kuerzel)) { // Speziallfall Gesamtschule G-Kurs
						dto.Kursart = ZulaessigeKursart.G.daten.kuerzel;
					} else if (kurs.KursartAllg.equals(ZulaessigeKursart.E.daten.kuerzelAllg)) { // Spezialfall Gesamtschule DK-Kurs -> nehme G als Default
						dto.Kursart = ZulaessigeKursart.G.daten.kuerzel;
					} else if (kurs.KursartAllg.equals(ZulaessigeKursart.GKM.daten.kuerzelAllg)) { // Spezialfall Gymnasiale Oberstufe GK -> Berücksichtige Abiturfach, Default GKM
						ZulaessigeKursart kursart = ZulaessigeKursart.GKM;
						if ("1".equals(dto.AbiFach) || "2".equals(dto.AbiFach))
							dto.AbiFach = null;
						if ("3".equals(dto.AbiFach))
							kursart = ZulaessigeKursart.AB3;
						else if ("4".equals(dto.AbiFach))
							kursart = ZulaessigeKursart.AB4;
						dto.Kursart = kursart.daten.kuerzel;
					} else if (kurs.KursartAllg.equals(ZulaessigeKursart.LK1.daten.kuerzelAllg)) { // Spezialfall Gymnasiale Oberstufe LK -> Berücksichtige Abiturfach, Default LK1
						dto.Kursart = ZulaessigeKursart.LK1.daten.kuerzel;
						if ("2".equals(dto.AbiFach))
							dto.Kursart = ZulaessigeKursart.LK2.daten.kuerzel;
						if (dto.AbiFach == null)
							dto.AbiFach = "1";
					} else {
						dto.Kursart = kursarten.isEmpty() ? null : kursarten.get(0).daten.kuerzel;
					}
				}
			}
			dto.Kurs_ID = idKurs;
		}),
		Map.entry("kursart", (conn, dto, value, map) -> {
			final String strKursart = JSONMapper.convertToString(value, true, false, null);
			final ZulaessigeKursart kursart = (strKursart == null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.getByASDKursart(strKursart);
			if (kursart == null)
				throw OperationError.CONFLICT.exception();
			dto.Kursart = kursart.daten.kuerzel;
			dto.KursartAllg = kursart.daten.kuerzelAllg;
		}),
		Map.entry("abifach", (conn, dto, value, map) -> {
			final Integer abiFach = JSONMapper.convertToInteger(value, true);
			if ((abiFach != null) && (abiFach != 1) && (abiFach != 2) && (abiFach != 3) && (abiFach != 4))
				throw OperationError.CONFLICT.exception();
			dto.AbiFach = (abiFach == null) ? null : "" + abiFach;
		}),
		Map.entry("istZP10oderZK10", (conn, dto, value, map) -> dto.Prf10Fach = JSONMapper.convertToBoolean(value, false)),
		Map.entry("koopSchule", (conn, dto, value, map) -> dto.SchulNr = JSONMapper.convertToIntegerInRange(value, true, 100000, 1000000)),
		Map.entry("lehrerID", (conn, dto, value, map) -> {
			final Long idLehrer = JSONMapper.convertToLong(value, true);
			if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
				throw OperationError.CONFLICT.exception();
			dto.Fachlehrer_ID = idLehrer;
		}),
		Map.entry("wochenstunden", (conn, dto, value, map) -> dto.Wochenstunden = JSONMapper.convertToIntegerInRange(value, true, 0, 1000)),
		Map.entry("zusatzkraftID", (conn, dto, value, map) -> {
			final Long idLehrer = JSONMapper.convertToLong(value, true);
			if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
				throw OperationError.CONFLICT.exception();
			dto.Zusatzkraft_ID = idLehrer;
		}),
		Map.entry("zusatzkraftWochenstunden", (conn, dto, value, map) -> dto.WochenstdZusatzkraft = JSONMapper.convertToIntegerInRange(value, true, 0, 1000)),
		Map.entry("aufZeugnis", (conn, dto, value, map) -> dto.AufZeugnis = JSONMapper.convertToBoolean(value, false)),
		Map.entry("note", (conn, dto, value, map) -> dto.NotenKrz = Note.fromKuerzel(JSONMapper.convertToString(value, true, true, null))),
		Map.entry("noteQuartal", (conn, dto, value, map) -> dto.NotenKrzQuartal = Note.fromKuerzel(JSONMapper.convertToString(value, true, true, null))),
		Map.entry("istGemahnt", (conn, dto, value, map) -> dto.Warnung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("mahndatum", (conn, dto, value, map) -> {
			final String strMahndatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.Warndatum = strMahndatum;
		}),
		Map.entry("istEpochal", (conn, dto, value, map) -> dto.VorherAbgeschl = JSONMapper.convertToBoolean(value, false)),
		Map.entry("geholtJahrgangAbgeschlossen", (conn, dto, value, map) -> {
			final String strJahrgang = JSONMapper.convertToString(value, true, false, null);
			// TODO Jahrgang validieren
			dto.AbschlussJahrgang = strJahrgang;
		}),
		Map.entry("gewichtungAllgemeinbildend", (conn, dto, value, map) -> dto.Gewichtung = JSONMapper.convertToIntegerInRange(value, true, 0, 10)),
		// noteBerufsabschluss -> dto.NoteAbschlussBA
		Map.entry("textFachbezogeneLernentwicklung", (conn, dto, value, map) -> dto.Lernentw = JSONMapper.convertToString(value, true, true, null)),
		Map.entry("umfangLernstandsbericht", (conn, dto, value, map) -> {
			final String strUmfang = JSONMapper.convertToString(value, true, true, 1);
			if ((strUmfang != null) && (!strUmfang.isBlank()) && (!strUmfang.equals("V")) && (!strUmfang.equals("R")))
				throw OperationError.CONFLICT.exception();
			dto.Umfang = (strUmfang == null) || strUmfang.isBlank() ? null : strUmfang;
		}),
		Map.entry("fehlstundenGesamt", (conn, dto, value, map) -> dto.FehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000)),
		Map.entry("fehlstundenUnentschuldigt", (conn, dto, value, map) -> dto.uFehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000))
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOSchuelerLeistungsdaten.class, patchMappings);
	}


	private static final Set<String> requiredCreateAttributes = Set.of("lernabschnittID", "fachID");

	private final ObjLongConsumer<DTOSchuelerLeistungsdaten> initDTO = (dto, id) -> dto.ID = id;

	/**
	 * Fügt die Leistungsdaten mit den übergebenen JSON-Daten der Datenbank hinzu und gibt das zugehörige CoreDTO
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response add(final InputStream is) {
		return super.addBasic(is, DTOSchuelerLeistungsdaten.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Fügt mehrere Leistungsdaten mit den übergebenen JSON-Daten der Datenbank hinzu und gibt die zugehörigen CoreDTOs
	 * zurück. Falls ein Fehler auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is   der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 */
	public Response addMultiple(final InputStream is) {
		return super.addBasicMultiple(is, DTOSchuelerLeistungsdaten.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht die Leistungsdaten mit der angegebenen ID
	 *
	 * @param id   die ID der Leistungsdaten
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOSchuelerLeistungsdaten.class, dtoMapper);
	}


	/**
	 * Löscht die Leistungsdaten mit den angegebenen IDs
	 *
	 * @param ids   die IDs der Leistungsdaten
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteMultiple(final List<Long> ids) {
		return super.deleteBasicMultiple(ids, DTOSchuelerLeistungsdaten.class, dtoMapper);
	}

}
