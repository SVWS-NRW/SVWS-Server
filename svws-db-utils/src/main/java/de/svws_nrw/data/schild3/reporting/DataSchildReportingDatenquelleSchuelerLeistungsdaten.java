package de.svws_nrw.data.schild3.reporting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerLeistungsdaten;
import de.svws_nrw.core.types.schild3.SchildReportingAttributTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Definition der Schild-Reporting-Datenquelle "Schuelerleistungsdaten"
 */
public final class DataSchildReportingDatenquelleSchuelerLeistungsdaten extends DataSchildReportingDatenquelle<SchildReportingSchuelerLeistungsdaten, Long> {

	/**
	 * Erstelle eine die Datenquelle Schuelerleistungsdaten
	 */
	DataSchildReportingDatenquelleSchuelerLeistungsdaten() {
		super(SchildReportingSchuelerLeistungsdaten.class);
		this.setMaster("abschnittID", "Schuelerlernabschnitte", "id", SchildReportingAttributTyp.INT, Long.class);
		// Beispiel für die Einschränkung auf Schulformen: this.restrictTo(Schulform.GY, Schulform.GE)
	}


	@Override
	List<SchildReportingSchuelerLeistungsdaten> getDaten(final DBEntityManager conn, final List<Long> params) throws ApiOperationException {
		// Prüfe, ob die Lernabschnittsdaten in der DB vorhanden sind
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapAbschnitte = conn.queryByKeyList(DTOSchuelerLernabschnittsdaten.class, params)
				.stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		for (final Long abschnittID : params)
			if (mapAbschnitte.get(abschnittID) == null)
				throw new ApiOperationException(Status.NOT_FOUND,
						"Parameter der Abfrage ungültig: Ein Schülerlernabschnitt mit der ID " + abschnittID + " existiert nicht.");

		// Erzeuge die Core-DTOs für das Ergebnis der Datenquelle
		final ArrayList<SchildReportingSchuelerLeistungsdaten> result = new ArrayList<>();

		// Aggregiere die benötigten Daten aus der Datenbank
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryList(DTOSchuelerLeistungsdaten.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOSchuelerLeistungsdaten.class, params);
		if ((leistungsdaten == null) || leistungsdaten.isEmpty())
			return result;
		final List<Long> idFaecher = leistungsdaten.stream().map(l -> l.Fach_ID).distinct().toList();
		final Map<Long, DTOFach> mapFaecher = (idFaecher.isEmpty()) ? Collections.emptyMap()
				: conn.queryByKeyList(DTOFach.class, idFaecher).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		final List<Long> idLehrer = leistungsdaten.stream().filter(l -> l.Fachlehrer_ID != null).map(l -> l.Fachlehrer_ID).distinct().toList();
		final Map<Long, DTOLehrer> mapLehrer = (idLehrer.isEmpty()) ? Collections.emptyMap()
				: conn.queryByKeyList(DTOLehrer.class, idLehrer).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final List<Long> idKurse = leistungsdaten.stream().filter(l -> l.Kurs_ID != null).map(l -> l.Kurs_ID).distinct().toList();
		final Map<Long, DTOKurs> mapKurse = (idKurse.isEmpty()) ? Collections.emptyMap()
				: conn.queryByKeyList(DTOKurs.class, idKurse).stream().collect(Collectors.toMap(k -> k.ID, k -> k));

		final String meldungsvorlageDatenInkonsistent =
				"Daten inkonsistent: %s mit der ID %d konnte nicht für die Leistungsdaten mit der ID %d gefunden werden.";

		for (final DTOSchuelerLeistungsdaten dto : leistungsdaten) {
			final DTOFach dtoFach = mapFaecher.get(dto.Fach_ID);
			if (dtoFach == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, String.format(meldungsvorlageDatenInkonsistent, "Fach", dto.Fach_ID, dto.ID));
			String lehrerKuerzel = null;
			if (dto.Fachlehrer_ID != null) {
				final DTOLehrer dtoLehrer = mapLehrer.get(dto.Fachlehrer_ID);
				if (dtoLehrer == null)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
							String.format(meldungsvorlageDatenInkonsistent, "Fachlehrer", dto.Fachlehrer_ID, dto.ID));
				lehrerKuerzel = dtoLehrer.Kuerzel;
			}
			final DTOSchuelerLernabschnittsdaten dtoLernabschnitt = mapAbschnitte.get(dto.Abschnitt_ID);
			final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoLernabschnitt.Schuljahresabschnitts_ID);
			final DTOKurs dtoKurs = mapKurse.get(dto.Kurs_ID);
			final SchildReportingSchuelerLeistungsdaten data = new SchildReportingSchuelerLeistungsdaten();
			data.id = dto.ID;
			data.abschnittID = dto.Abschnitt_ID;
			data.fachID = dto.Fach_ID;
			data.fachKuerzel = dtoFach.Kuerzel;
			data.fach = dtoFach.Bezeichnung;
			data.lehrerID = dto.Fachlehrer_ID;
			data.lehrerKuerzel = lehrerKuerzel;
			data.kursID = dto.Kurs_ID;
			data.kurs = (dtoKurs == null) ? "" : dtoKurs.KurzBez;
			data.kursart = dto.Kursart;
			data.kursartAllg = (dtoKurs == null) ? dto.KursartAllg : dtoKurs.KursartAllg;
			final Note note = Note.data().getWertByKuerzel(dto.NotenKrz);
			final NoteKatalogEintrag noteEintrag = note.daten(abschnitt.schuljahr);
			data.note = noteEintrag.text;
			data.noteKuerzel = noteEintrag.kuerzel;
			data.notePunkte = noteEintrag.notenpunkte;
			data.sortierungAllg = dtoFach.SortierungAllg;
			data.sortierungSekII = dtoFach.SortierungSekII;
			result.add(data);
		}
		result.sort(Comparator.comparing(ld -> ld.sortierungAllg));

		// Geben die Ergebnis-Liste mit den Core-DTOs zurück
		return result;
	}

}
