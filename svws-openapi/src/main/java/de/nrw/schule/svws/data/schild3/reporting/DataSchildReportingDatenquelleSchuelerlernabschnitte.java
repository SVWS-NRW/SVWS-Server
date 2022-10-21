package de.nrw.schule.svws.data.schild3.reporting;

import java.io.InputStream;

import de.nrw.schule.svws.core.data.schild3.SchildReportingSchuelerlernabschnitt;
import de.nrw.schule.svws.core.types.schild3.SchildReportingAttributTyp;
import de.nrw.schule.svws.db.DBEntityManager;
import jakarta.ws.rs.core.Response;

/**
 * Die Definition der Schild-Reporting-Datenquelle "Schuelerlernabschnitte" 
 */
public class DataSchildReportingDatenquelleSchuelerlernabschnitte extends DataSchildReportingDatenquelle {

    
    DataSchildReportingDatenquelleSchuelerlernabschnitte() {
        super("Schuelerlernabschnitte", "/schueler/lernabschnitte", "Die Lernabschnitte eines Schülers bzw. von mehreren Schülern");
        this.setMaster("Schueler", "id", SchildReportingAttributTyp.INT);
        this.addAttribute(SchildReportingSchuelerlernabschnitt.class);
    }


    @Override
    Response getDaten(DBEntityManager conn, InputStream is) {
        // TODO interpretiere den InputStream, falls ein master gesetzt ist: eine ID oder Liste?
        // TODO führe die Datenbank-Abfrage aus und erzeuge das Core-DTO ...
        // TODO Erstelle die Response
        return null;
    }

}
