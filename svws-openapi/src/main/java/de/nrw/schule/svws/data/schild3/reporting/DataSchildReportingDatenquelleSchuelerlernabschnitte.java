package de.nrw.schule.svws.data.schild3.reporting;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.schild3.SchildReportingSchuelerlernabschnitt;
import de.nrw.schule.svws.core.types.schild3.SchildReportingAttributTyp;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Die Definition der Schild-Reporting-Datenquelle "Schuelerlernabschnitte" 
 */
public class DataSchildReportingDatenquelleSchuelerlernabschnitte extends DataSchildReportingDatenquelle {

    /**
     * Erstelle eine die Datenquelle Schuelerlernabschnitt
     */
    DataSchildReportingDatenquelleSchuelerlernabschnitte() {
        super(SchildReportingSchuelerlernabschnitt.class);
        this.setMaster("Schueler", "id", SchildReportingAttributTyp.INT);
    }


    @Override
    List<? extends Object> getDatenInteger(DBEntityManager conn, List<Long> params) {
        // Prüfe, ob die Schüler in der DB vorhanden sind
        Map<Long, DTOSchueler> schueler = conn
                .queryNamed("DTOSchueler.id.multiple", params, DTOSchueler.class)
                .stream().collect(Collectors.toMap(s -> s.ID, s -> s));
        for (Long schuelerID : params)
            if (schueler.get(schuelerID) == null)
                throw OperationError.CONFLICT.exception("Parater der Abfrage ungültig: Ein Schüler mit der ID " + schuelerID + " existiert nicht."); 
        // Aggregiere die benötigten Daten aus der Datenbank
        List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id.multiple", params, DTOSchuelerLernabschnittsdaten.class);
        if (lernabschnittsdaten == null)
            return null;
        List<Long> idSchuljahresabschnitte = lernabschnittsdaten.stream().map(l -> l.Schuljahresabschnitts_ID).toList();
        Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = conn
                .queryNamed("DTOSchuljahresabschnitte.id.multiple", idSchuljahresabschnitte, DTOSchuljahresabschnitte.class)
                .stream().collect(Collectors.toMap(j -> j.ID, j -> j));
        List<Long> idKlassen = lernabschnittsdaten.stream().map(l -> l.Klassen_ID).toList();
        Map<Long, DTOKlassen> mapKlassen = conn
                .queryNamed("DTOKlassen.id.multiple", idKlassen, DTOKlassen.class)
                .stream().collect(Collectors.toMap(k -> k.ID, k -> k));
        List<Long> idJahrgaenge = lernabschnittsdaten.stream().map(l -> l.Jahrgang_ID).toList();
        Map<Long, DTOJahrgang> mapJahrgaenge = conn
                .queryNamed("DTOJahrgang.id.multiple", idJahrgaenge, DTOJahrgang.class)
                .stream().collect(Collectors.toMap(j -> j.ID, j -> j));
        // Erzeuge die Core-DTOs für das Ergebnis der Datenquelle
        Vector<SchildReportingSchuelerlernabschnitt> result = new Vector<>();
        for (DTOSchuelerLernabschnittsdaten dto : lernabschnittsdaten) {
            DTOSchuljahresabschnitte dtoSJA = mapSchuljahresabschnitte.get(dto.Schuljahresabschnitts_ID);
            if (dtoSJA == null)
                throw OperationError.INTERNAL_SERVER_ERROR.exception("Daten inkonsistend: Schuljahresabschnitt mit der ID " + dto.Schuljahresabschnitts_ID + " konnte nicht für die Lernabschnittsdaten mit der ID " + dto.ID + " gefunden werden."); 
            DTOKlassen dtoKlasse = mapKlassen.get(dto.Klassen_ID);
            if (dtoKlasse == null)
                throw OperationError.INTERNAL_SERVER_ERROR.exception("Daten inkonsistend: Klasse mit der ID " + dto.Klassen_ID + " konnte nicht für die Lernabschnittsdaten mit der ID " + dto.ID + " gefunden werden."); 
            DTOJahrgang dtoJahrgang = mapJahrgaenge.get(dto.Jahrgang_ID);
            if (dtoJahrgang == null)
                throw OperationError.INTERNAL_SERVER_ERROR.exception("Daten inkonsistend: Jahrgang mit der ID " + dto.Jahrgang_ID + " konnte nicht für die Lernabschnittsdaten mit der ID " + dto.ID + " gefunden werden."); 
            SchildReportingSchuelerlernabschnitt data = new SchildReportingSchuelerlernabschnitt();
            data.id = dto.ID;
            data.schuelerID = dto.Schueler_ID;
            data.schuljahr = dtoSJA.Jahr;
            data.abschnitt = dtoSJA.Abschnitt;
            data.wechselNr = dto.WechselNr;
            data.istGewertet = dto.SemesterWertung == null ? false : dto.SemesterWertung;
            data.istWiederholung = dto.Wiederholung == null ? false : dto.Wiederholung;
            data.pruefungsOrdnung = dto.PruefOrdnung;
            data.klasse = dtoKlasse.Klasse;
            data.klasseStatistik = dtoKlasse.ASDKlasse;
            data.jahrgang = dtoJahrgang.InternKrz;
            data.jahrgangStatistik = dtoJahrgang.ASDJahrgang;
            result.add(data);
        }
        // Geben die Ergebnis-Liste mit den Core-DTOs zurück
        return result;
    }

}
