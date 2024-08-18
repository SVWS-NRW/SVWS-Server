package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerLeistungsdaten}.
 */
public final class DataSchuelerLeistungsdaten extends DataManagerRevised<Long, DTOSchuelerLeistungsdaten, SchuelerLeistungsdaten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerLeistungsdaten}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLeistungsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("lernabschnittID", "fachID");
	}

	@Override
	protected void initDTO(final DTOSchuelerLeistungsdaten dto, final Long newId) throws ApiOperationException {
		dto.ID = newId;
	}


	@Override
	public SchuelerLeistungsdaten map(final DTOSchuelerLeistungsdaten dto) {
		final SchuelerLeistungsdaten daten = new SchuelerLeistungsdaten();
		daten.id = dto.ID;
		daten.lernabschnittID = dto.Abschnitt_ID;
		daten.fachID = dto.Fach_ID;
		daten.kursID = dto.Kurs_ID;
		daten.kursart = (dto.Kursart == null) ? ZulaessigeKursart.PUK.daten.kuerzel : dto.Kursart;
		try {
			daten.abifach = (dto.AbiFach == null) ? null : Integer.parseInt(dto.AbiFach);
		} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
			daten.abifach = null;
		}
		daten.istZP10oderZK10 = (dto.Prf10Fach != null) && dto.Prf10Fach;
		daten.koopSchule = dto.SchulNr;
		daten.lehrerID = dto.Fachlehrer_ID;
		daten.wochenstunden = (dto.Wochenstunden == null) ? 0 : dto.Wochenstunden;
		daten.zusatzkraftID = dto.Zusatzkraft_ID;
		daten.zusatzkraftWochenstunden = (dto.WochenstdZusatzkraft == null) ? 0 : dto.WochenstdZusatzkraft;
		daten.aufZeugnis = (dto.AufZeugnis == null) || dto.AufZeugnis;
		daten.note = (dto.NotenKrz == null) ? Note.KEINE.kuerzel : dto.NotenKrz.kuerzel;
		daten.noteQuartal = (dto.NotenKrzQuartal == null) ? Note.KEINE.kuerzel : dto.NotenKrzQuartal.kuerzel;
		daten.istGemahnt = (dto.Warnung != null) && dto.Warnung; // TODO bestimme ggf. aus Halbjahr zuvor
		daten.mahndatum = dto.Warndatum;
		daten.istEpochal = (dto.VorherAbgeschl != null) && dto.VorherAbgeschl;
		daten.geholtJahrgangAbgeschlossen = dto.AbschlussJahrgang;
		daten.gewichtungAllgemeinbildend = (dto.Gewichtung == null) ? 1 : dto.Gewichtung;
		daten.noteBerufsabschluss = dto.NoteAbschlussBA;
		daten.textFachbezogeneLernentwicklung = (dto.Lernentw == null) ? "" : dto.Lernentw;
		daten.umfangLernstandsbericht = (dto.Umfang == null) ? "" : dto.Umfang;
		daten.fehlstundenGesamt = (dto.FehlStd == null) ? 0 : dto.FehlStd;
		daten.fehlstundenUnentschuldigt = (dto.uFehlStd == null) ? 0 : dto.uFehlStd;
		return daten;
	}


	private void mapKursID(final DTOSchuelerLeistungsdaten dto, final Long idKurs) throws ApiOperationException {
		if (idKurs != null) {
			/// Prüfe, ob der Kurs existiert und passe ggf. Fachlehrer und Kursart an.
			final DTOKurs kurs = conn.queryByKey(DTOKurs.class, idKurs);
			if (kurs == null)
				throw new ApiOperationException(Status.CONFLICT);
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
	}


	@Override
	protected void mapAttribute(final DTOSchuelerLeistungsdaten dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}
			case "lernabschnittID" -> {
				final long idAbschnitt = JSONMapper.convertToLong(value, false);
				if (conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, idAbschnitt) == null)
					throw new ApiOperationException(Status.CONFLICT);
				dto.Abschnitt_ID = idAbschnitt;
			}
			case "fachID" -> {
				final long idFach = JSONMapper.convertToLong(value, false);
				final DTOFach fach = conn.queryByKey(DTOFach.class, idFach);
				if (fach == null)
					throw new ApiOperationException(Status.CONFLICT);
				dto.Fach_ID = idFach;
				dto.Sortierung = fach.SortierungAllg;
				dto.Kurs_ID = null;
			}
			case "kursID" -> mapKursID(dto, JSONMapper.convertToLong(value, true));
			case "kursart" -> {
				final String strKursart = JSONMapper.convertToString(value, true, false, null);
				final ZulaessigeKursart kursart = (strKursart == null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.getByASDKursart(strKursart);
				if (kursart == null)
					throw new ApiOperationException(Status.CONFLICT);
				dto.Kursart = kursart.daten.kuerzel;
				dto.KursartAllg = kursart.daten.kuerzelAllg;
			}
			case "abifach" -> {
				final Integer abiFach = JSONMapper.convertToInteger(value, true);
				if ((abiFach != null) && (abiFach != 1) && (abiFach != 2) && (abiFach != 3) && (abiFach != 4))
					throw new ApiOperationException(Status.CONFLICT);
				dto.AbiFach = (abiFach == null) ? null : ("" + abiFach);
			}
			case "istZP10oderZK10" -> dto.Prf10Fach = JSONMapper.convertToBoolean(value, false);
			case "koopSchule" -> dto.SchulNr = JSONMapper.convertToIntegerInRange(value, true, 100000, 1000000);
			case "lehrerID" -> {
				final Long idLehrer = JSONMapper.convertToLong(value, true);
				if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Fachlehrer_ID = idLehrer;
			}
			case "wochenstunden" -> dto.Wochenstunden = JSONMapper.convertToIntegerInRange(value, true, 0, 1000);
			case "zusatzkraftID" -> {
				final Long idLehrer = JSONMapper.convertToLong(value, true);
				if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Zusatzkraft_ID = idLehrer;
			}
			case "zusatzkraftWochenstunden" -> dto.WochenstdZusatzkraft = JSONMapper.convertToIntegerInRange(value, true, 0, 1000);
			case "aufZeugnis" -> dto.AufZeugnis = JSONMapper.convertToBoolean(value, false);
			case "note" -> dto.NotenKrz = Note.fromKuerzel(JSONMapper.convertToString(value, true, true, null));
			case "noteQuartal" -> dto.NotenKrzQuartal = Note.fromKuerzel(JSONMapper.convertToString(value, true, true, null));
			case "istGemahnt" -> dto.Warnung = JSONMapper.convertToBoolean(value, false);
			case "mahndatum" -> {
				final String strMahndatum = JSONMapper.convertToString(value, true, false, null);
				// TODO Datum validieren
				dto.Warndatum = strMahndatum;
			}
			case "istEpochal" -> dto.VorherAbgeschl = JSONMapper.convertToBoolean(value, false);
			case "geholtJahrgangAbgeschlossen" -> {
				final String strJahrgang = JSONMapper.convertToString(value, true, false, null);
				// TODO Jahrgang validieren
				dto.AbschlussJahrgang = strJahrgang;
			}
			case "gewichtungAllgemeinbildend" -> dto.Gewichtung = JSONMapper.convertToIntegerInRange(value, true, 0, 10);
			// noteBerufsabschluss -> dto.NoteAbschlussBA
			case "textFachbezogeneLernentwicklung" -> dto.Lernentw = JSONMapper.convertToString(value, true, true, null);
			case "umfangLernstandsbericht" -> {
				final String strUmfang = JSONMapper.convertToString(value, true, true, 1);
				if ((strUmfang != null) && (!strUmfang.isBlank()) && (!strUmfang.equals("V")) && (!strUmfang.equals("R")))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Umfang = ((strUmfang == null) || strUmfang.isBlank()) ? null : strUmfang;
			}
			case "fehlstundenGesamt" -> dto.FehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			case "fehlstundenUnentschuldigt" -> dto.uFehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}


	/**
	 * Prüft, ob der Benutzer mit einer funktionsbezogenen Kompetenz auf den Lernabschnitt zugreift und wenn ja, ob dieser dann
	 * die Kompetenz auf den Klassen für die übergebenen Lernabschnitte hat. Hat er diese Kompetenz nicht, so wird eine
	 * Exception geschmissen.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 *
	 * @throws ApiOperationException   im Fehlerfall, wenn der Benutzer nicht alle Rechte zum Zugriff auf die übergebene Lernabschnitte hat (503 - FORBIDDEN)
	 */
	private void checkFunktionsbezogeneKompetenzAufLernabschnitt(final List<Long> idsLernabschnitte) throws ApiOperationException {
		if (checkBenutzerFunktionsbezogeneKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN,
				Set.of(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))) {
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryByKeyList(DTOSchuelerLernabschnittsdaten.class, idsLernabschnitte);
			for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte)
				checkBenutzerFunktionsbezogeneKompetenzKlasse(lernabschnitt.Klassen_ID);
		}
	}


	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Leistungsdaten in dem Lernabschnitt zu erstellen
		final long idLernabschnitt = JSONMapper.convertToLong(initAttributes.get("lernabschnittID"), false);
		checkFunktionsbezogeneKompetenzAufLernabschnitt(List.of(idLernabschnitt));
	}


	@Override
	public void checkBeforePatch(final DTOSchuelerLeistungsdaten dto, final Map<String, Object> patchAttributes) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Leistungsdaten in dem Lernabschnitt zu verändern
		if (patchAttributes.get("lernabschnittID") != null) {
			final long idLernabschnitt = JSONMapper.convertToLong(patchAttributes.get("lernabschnittID"), false);
			checkFunktionsbezogeneKompetenzAufLernabschnitt(List.of(idLernabschnitt));
		}
		checkFunktionsbezogeneKompetenzAufLernabschnitt(List.of(dto.Abschnitt_ID));
	}


	@Override
	public void checkBeforeDeletion(final List<DTOSchuelerLeistungsdaten> dtos) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Leistungsdaten in dem Lernabschnitt zu löschen
		checkFunktionsbezogeneKompetenzAufLernabschnitt(dtos.stream().map(l -> l.Abschnitt_ID).toList());
	}


	@Override
	public SchuelerLeistungsdaten getById(final Long id) throws ApiOperationException {
		// Prüfe, ob die Leistungsdaten mit der ID existieren
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOSchuelerLeistungsdaten dto = conn.queryByKey(DTOSchuelerLeistungsdaten.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Leistungsdaten mit der ID %d wurden in der Datenbank nicht gefunden".formatted(id));
		return map(dto);
	}


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
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_BY_ABSCHNITT_ID,
				DTOSchuelerLeistungsdaten.class, abschnittID);
		if (leistungsdaten == null)
			return false;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOSchuelerLeistungsdaten l : leistungsdaten)
			list.add(map(l));
		return true;
	}

}
