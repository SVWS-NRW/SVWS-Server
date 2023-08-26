package de.svws_nrw.data.schild3.reporting;

import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerSprachpruefungen;
import de.svws_nrw.core.types.schild3.SchildReportingAttributTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.db.utils.OperationError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Die Definition einer Schild-Reporting-Datenquelle für die Fehler der Laufbahnplanung in der gymnasialen Oberstufe
 */
public final class DataSchildReportingDatenquelleSchuelerSprachpruefungen extends DataSchildReportingDatenquelle<SchildReportingSchuelerSprachpruefungen, Long> {

    /**
     * Erstelle die Datenquelle SchildReportingSchuelerSprachpruefungen
     */
    DataSchildReportingDatenquelleSchuelerSprachpruefungen() {
        super(SchildReportingSchuelerSprachpruefungen.class);
        this.setMaster("schuelerID", "Schueler", "id", SchildReportingAttributTyp.INT, Long.class);
        // Beispiel für die Einschränkung auf Schulformen: this.restrictTo(Schulform.GY, Schulform.GE)
    }

	@Override
    List<SchildReportingSchuelerSprachpruefungen> getDaten(final DBEntityManager conn, final List<Long> params) {

		// Prüfe, ob die Schüler in der DB vorhanden sind
        final Map<Long, DTOSchueler> schueler = conn
                .queryNamed("DTOSchueler.id.multiple", params, DTOSchueler.class)
                .stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long schuelerID : params) {
			if (schueler.get(schuelerID) == null)
				throw OperationError.NOT_FOUND.exception("Parameter der Abfrage ungültig: Ein Schüler mit der ID " + schuelerID.toString() + " existiert nicht.");
		}

		// Aggregiere die benötigten Daten aus der Datenbank, wenn alle Schüler-IDs existieren
		final ArrayList<SchildReportingSchuelerSprachpruefungen> result = new ArrayList<>();
		for (final Long schuelerID : params) {
			// Die Sprachprüfungsdaten der Schüler sammeln.
			final List<DTOSchuelerSprachpruefungen> schuelerSprachpruefungen = conn.queryNamed("DTOSchuelerSprachpruefungen.schueler_id", schuelerID, DTOSchuelerSprachpruefungen.class);

			if ((schuelerSprachpruefungen != null) && (!schuelerSprachpruefungen.isEmpty())) {
				// Nur wenn zum Schüler Sprachprüfungen vorhanden sind, werden diese zurückgegeben. Andernfalls wird ein leerer Vektor zurückgegeben.
				// Alternativ wäre der vollständige Abbruch im Fehlerfall: throw OperationError.INTERNAL_SERVER_ERROR.exception("Parameter der Abfrage ungültig: Die GOSt-Daten oder Abiturdaten des Schülers mit der ID " + schuelerID.toString() + " konnten nicht ermittelt werden.")
				try {
					for (final DTOSchuelerSprachpruefungen dtoSP : schuelerSprachpruefungen) {
						final SchildReportingSchuelerSprachpruefungen schuelerSprachpruefung = new SchildReportingSchuelerSprachpruefungen();
						schuelerSprachpruefung.schuelerID = schuelerID;
						schuelerSprachpruefung.sprache = dtoSP.Sprache;
						schuelerSprachpruefung.jahrgang = dtoSP.ASDJahrgang;
						schuelerSprachpruefung.pruefungsdatum = dtoSP.Pruefungsdatum;
						schuelerSprachpruefung.anspruchsniveau = dtoSP.Anspruchsniveau.daten.kuerzel;
						schuelerSprachpruefung.ersetzteSprache = dtoSP.ErsetzteSprache;
						schuelerSprachpruefung.istHSUPruefung = dtoSP.IstHSUPruefung;
						schuelerSprachpruefung.istFeststellungspruefung = dtoSP.IstFeststellungspruefung;
						schuelerSprachpruefung.kannErstePflichtfremdspracheErsetzen = dtoSP.KannErstePflichtfremdspracheErsetzen;
						schuelerSprachpruefung.kannZweitePflichtfremdspracheErsetzen = dtoSP.KannZweitePflichtfremdspracheErsetzen;
						schuelerSprachpruefung.kannWahlpflichtfremdspracheErsetzen = dtoSP.KannWahlpflichtfremdspracheErsetzen;
						schuelerSprachpruefung.kannBelegungAlsFortgefuehrteSpracheErlauben = dtoSP.KannBelegungAlsFortgefuehrteSpracheErlauben;
						schuelerSprachpruefung.referenzniveau = dtoSP.Referenzniveau.daten.kuerzel;
						schuelerSprachpruefung.note = dtoSP.NotePruefung.kuerzel;
						result.add(schuelerSprachpruefung);
					}
				} catch (final Exception ex) {
					throw OperationError.INTERNAL_SERVER_ERROR.exception("Die Daten zu den Sprachprüfungen des Schülers mit der ID " + schuelerID + " sind vermutlich inkonsistent. Folgender Fehler ist aufgetreten: " + ex.getMessage());
				}
			}
		}
		// Geben die Ergebnis-Liste mit den Core-DTOs zurück
        return result;
    }

}
