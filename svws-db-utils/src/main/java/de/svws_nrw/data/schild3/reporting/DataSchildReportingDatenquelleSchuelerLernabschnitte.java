package de.svws_nrw.data.schild3.reporting;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerLernabschnitt;
import de.svws_nrw.core.types.schild3.SchildReportingAttributTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;

/**
 * Die Definition der Schild-Reporting-Datenquelle "Schuelerlernabschnitte"
 */
public final class DataSchildReportingDatenquelleSchuelerLernabschnitte extends DataSchildReportingDatenquelle<SchildReportingSchuelerLernabschnitt, Long> {

    /**
     * Erstelle eine die Datenquelle Schuelerlernabschnitt
     */
    DataSchildReportingDatenquelleSchuelerLernabschnitte() {
        super(SchildReportingSchuelerLernabschnitt.class);
        this.setMaster("schuelerID", "Schueler", "id", SchildReportingAttributTyp.INT, Long.class);
        // Beispiel für die Einschränkung auf Schulformen: this.restrictTo(Schulform.GY, Schulform.GE)
    }

	@Override
    List<SchildReportingSchuelerLernabschnitt> getDaten(final DBEntityManager conn, final List<Long> params) {
        // Prüfe, ob die Schüler in der DB vorhanden sind
        final Map<Long, DTOSchueler> schueler = conn
                .queryNamed("DTOSchueler.id.multiple", params, DTOSchueler.class)
                .stream().collect(Collectors.toMap(s -> s.ID, s -> s));
        for (final Long schuelerID : params)
            if (schueler.get(schuelerID) == null)
                throw OperationError.NOT_FOUND.exception("Parameter der Abfrage ungültig: Ein Schüler mit der ID " + schuelerID + " existiert nicht.");

		// Erzeuge die Core-DTOs für das Ergebnis der Datenquelle
		final ArrayList<SchildReportingSchuelerLernabschnitt> result = new ArrayList<>();

        // Aggregiere die benötigten Daten aus der Datenbank
		final List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id.multiple", params, DTOSchuelerLernabschnittsdaten.class);
        if (lernabschnittsdaten == null || lernabschnittsdaten.isEmpty())
            return result;
        final List<Long> idSchuljahresabschnitte = lernabschnittsdaten.stream().map(l -> l.Schuljahresabschnitts_ID).toList();
        final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = conn
                .queryNamed("DTOSchuljahresabschnitte.id.multiple", idSchuljahresabschnitte, DTOSchuljahresabschnitte.class)
                .stream().collect(Collectors.toMap(j -> j.ID, j -> j));
        final List<Long> idKlassen = lernabschnittsdaten.stream().map(l -> l.Klassen_ID).toList();
        final Map<Long, DTOKlassen> mapKlassen = conn
                .queryNamed("DTOKlassen.id.multiple", idKlassen, DTOKlassen.class)
                .stream().collect(Collectors.toMap(k -> k.ID, k -> k));
        final List<Long> idJahrgaenge = lernabschnittsdaten.stream().map(l -> l.Jahrgang_ID).toList();
        final Map<Long, DTOJahrgang> mapJahrgaenge = conn
                .queryNamed("DTOJahrgang.id.multiple", idJahrgaenge, DTOJahrgang.class)
                .stream().collect(Collectors.toMap(j -> j.ID, j -> j));

		final String meldungsvorlageDatenInkonsistent = "Daten inkonsistent: %s mit der ID %d konnte nicht für die Lernabschnittsdaten mit der ID %d gefunden werden.";

        for (final DTOSchuelerLernabschnittsdaten dto : lernabschnittsdaten) {
			final DTOSchuljahresabschnitte dtoSJA = mapSchuljahresabschnitte.get(dto.Schuljahresabschnitts_ID);

            if (dtoSJA == null)
                throw OperationError.INTERNAL_SERVER_ERROR.exception(String.format(meldungsvorlageDatenInkonsistent, "Schuljahresabschnitt",  dto.Schuljahresabschnitts_ID, dto.ID));
            final DTOKlassen dtoKlasse = mapKlassen.get(dto.Klassen_ID);
            if (dtoKlasse == null)
                throw OperationError.INTERNAL_SERVER_ERROR.exception(String.format(meldungsvorlageDatenInkonsistent, "Klasse", dto.Klassen_ID, dto.ID));
            final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(dto.Jahrgang_ID);
            if (dtoJahrgang == null)
                throw OperationError.INTERNAL_SERVER_ERROR.exception(String.format(meldungsvorlageDatenInkonsistent, "Jahrgang", dto.Jahrgang_ID, dto.ID));
            final SchildReportingSchuelerLernabschnitt data = new SchildReportingSchuelerLernabschnitt();
            data.id = dto.ID;
            data.schuelerID = dto.Schueler_ID;
            data.schuljahr = dtoSJA.Jahr;
            data.abschnitt = dtoSJA.Abschnitt;
            data.wechselNr = dto.WechselNr;
            data.istGewertet = dto.SemesterWertung != null && dto.SemesterWertung;
            data.istWiederholung = dto.Wiederholung != null && dto.Wiederholung;
            data.pruefungsOrdnung = dto.PruefOrdnung;
            data.klasse = dtoKlasse.Klasse;
            data.klasseStatistik = dtoKlasse.ASDKlasse;
            data.jahrgang = dtoJahrgang.InternKrz;
            data.jahrgangStatistik = dtoJahrgang.ASDJahrgang;
            data.datumZeugniskonferenz = dto.Konferenzdatum;
            data.datumZeugnis = dto.ZeugnisDatum;
            data.logPruefungsalgorithmus = dto.PruefAlgoErgebnis;
            result.add(data);
        }
		result.sort(comparatorLernabschnitte);

		// Geben die Ergebnis-Liste mit den Core-DTOs zurück
        return result;
    }

	private final Comparator<SchildReportingSchuelerLernabschnitt> comparatorLernabschnitte = (la1, la2) -> {
		if (la1.schuljahr != la2.schuljahr)
			return Integer.compare(la1.schuljahr, la2.schuljahr);
		if (la1.abschnitt != la2.abschnitt) {
			return Integer.compare(la1.abschnitt, la2.abschnitt);
		}
		if ((la1.wechselNr != 0 && la2.wechselNr != 0) || (la1.wechselNr == 0 && la2.wechselNr == 0)) {
			if ((la1.wechselNr != 0) && (la1.wechselNr != la2.wechselNr)) {
				return Integer.compare(la1.wechselNr, la2.wechselNr);
			}
			// Dieser Fall darf bei korrekten Daten nicht auftreten, weil es dann zwei identische Lernabschnitte geben würde.
			// Prüfe aber dennoch zusätzlich die Wertung und Wiederholung.
			int checkA1 = 1;
			if (!la1.istGewertet)
				checkA1 -= 1;
			if (la1.istWiederholung)
				checkA1 += 2;
			int checkA2 = 1;
			if (!la2.istGewertet)
				checkA2 -= 1;
			if (la2.istWiederholung)
				checkA2 += 2;
			return Integer.compare(checkA1, checkA2);
		} else if (la1.wechselNr == 0) {
			return -1;
		} else {
			return 1;
		}
	};

}
