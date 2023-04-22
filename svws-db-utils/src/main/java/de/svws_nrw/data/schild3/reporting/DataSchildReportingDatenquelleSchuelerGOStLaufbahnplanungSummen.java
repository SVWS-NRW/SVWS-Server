package de.svws_nrw.data.schild3.reporting;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.schild3.SchildReportingSchuelerGOStLaufbahnplanungSummen;
import de.svws_nrw.core.types.schild3.SchildReportingAttributTyp;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.db.utils.gost.FaecherGost;
import de.svws_nrw.db.utils.gost.GostSchuelerLaufbahn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Die Definition einer Schild-Reporting-Datenquelle für die Kurs- und Wochenstundensummen der Laufbahnplanung in der gymnasialen Oberstufe
 */
public final class DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen extends DataSchildReportingDatenquelle {

    /**
     * Erstelle die Datenquelle SchuelerGOStLaufbahnplanungSummen
     */
    DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen() {
        super(SchildReportingSchuelerGOStLaufbahnplanungSummen.class);
        this.setMaster("schuelerID", "Schueler", "id", SchildReportingAttributTyp.INT);
        // Beispiel für die Einschränkung auf Schulformen: this.restrictTo(Schulform.GY, Schulform.GE)
    }

	@Override
    List<? extends Object> getDatenInteger(final DBEntityManager conn, final List<Long> params) {

		// Prüfe, ob die Schüler in der DB vorhanden sind
        final Map<Long, DTOSchueler> schueler = conn
                .queryNamed("DTOSchueler.id.multiple", params, DTOSchueler.class)
                .stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long schuelerID : params) {
			if (schueler.get(schuelerID) == null)
				throw OperationError.NOT_FOUND.exception("Parameter der Abfrage ungültig: Ein Schüler mit der ID " + schuelerID.toString() + " existiert nicht.");
		}

		// Aggregiere die benötigten Daten aus der Datenbank, wenn alle Schüler-IDs existieren
		final ArrayList<SchildReportingSchuelerGOStLaufbahnplanungSummen> result = new ArrayList<>();
		for (final Long schuelerID : params) {
			final SchildReportingSchuelerGOStLaufbahnplanungSummen laufbahnplanungSummen = new SchildReportingSchuelerGOStLaufbahnplanungSummen();
			laufbahnplanungSummen.schuelerID = schuelerID;

			// Abiturdaten und Abiturjahrgangsdaten zur Schueler_ID ermitteln
			final Abiturdaten abidaten = GostSchuelerLaufbahn.get(conn, schuelerID);
			final DTOGostJahrgangsdaten jahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abidaten.abiturjahr);

			if ((abidaten.abiturjahr <= 0) || (jahrgangsdaten == null)) {
				// Zum Schüler wurden keine Abiturdaten gefunden oder die Jahrgangsdaten zum Abiturjahrgang existieren nicht.
				// Gebe daher leere Daten zurück. So wird die Datenquelle auch gefüllt, wenn bei Anfragen zu mehreren Schülern die Daten von nur einem Schüler nicht existiert.
				// Alternativ wäre der vollständige Abbruch: throw OperationError.INTERNAL_SERVER_ERROR.exception("Parameter der Abfrage ungültig: Der Abiturjahrgang des Schülers mit der ID " + schuelerID.toString() + " ist ungültig.")

				laufbahnplanungSummen.kursanzahlEF1 = 0;
				laufbahnplanungSummen.kursanzahlEF2 = 0;
				laufbahnplanungSummen.kursanzahlQ11 = 0;
				laufbahnplanungSummen.kursanzahlQ12 = 0;
				laufbahnplanungSummen.kursanzahlQ21 = 0;
				laufbahnplanungSummen.kursanzahlQ22 = 0;

				laufbahnplanungSummen.kursanzahlQPh = 0;

				laufbahnplanungSummen.wochenstundenEF1 = 0;
				laufbahnplanungSummen.wochenstundenEF2 = 0;
				laufbahnplanungSummen.wochenstundenQ11 = 0;
				laufbahnplanungSummen.wochenstundenQ12 = 0;
				laufbahnplanungSummen.wochenstundenQ21 = 0;
				laufbahnplanungSummen.wochenstundenQ22 = 0;

				laufbahnplanungSummen.wochenstundenDurchschnittEF = 0;
				laufbahnplanungSummen.wochenstundenDurchschnittQPh = 0;
				laufbahnplanungSummen.wochenstundenGesamt = 0;
			} else {
				// Abiturdatenmanager für weitere Angaben erzeugen.
				// Da unter Umständen durch Migration und Importe alter Daten aus Schild und LuPO die GOSt-Fächer nicht mit den Fachwahlen übereinstimmen könnten,
				// kann beim Erzeugen der Manager ein Fehler auftreten. Dieser wird hier abgefangen, das Füllen der Datenquelle beendet und eine Exception geworfen.
				try {
					final GostFaecherManager gostFaecher = FaecherGost.getFaecherListeGost(conn, abidaten.abiturjahr);
					final AbiturdatenManager abiManager = new AbiturdatenManager(abidaten, gostFaecher.toList(), GostBelegpruefungsArt.GESAMT);

					final int[] kurse = abiManager.getAnrechenbareKurse();
					final int[] wstd = abiManager.getWochenstunden();

					laufbahnplanungSummen.kursanzahlEF1 = kurse[0];
					laufbahnplanungSummen.kursanzahlEF2 = kurse[1];
					laufbahnplanungSummen.kursanzahlQ11 = kurse[2];
					laufbahnplanungSummen.kursanzahlQ12 = kurse[3];
					laufbahnplanungSummen.kursanzahlQ21 = kurse[4];
					laufbahnplanungSummen.kursanzahlQ22 = kurse[5];

					laufbahnplanungSummen.kursanzahlQPh = laufbahnplanungSummen.kursanzahlQ11 + laufbahnplanungSummen.kursanzahlQ12 + laufbahnplanungSummen.kursanzahlQ21 + laufbahnplanungSummen.kursanzahlQ22;

					laufbahnplanungSummen.wochenstundenEF1 = wstd[0];
					laufbahnplanungSummen.wochenstundenEF2 = wstd[1];
					laufbahnplanungSummen.wochenstundenQ11 = wstd[2];
					laufbahnplanungSummen.wochenstundenQ12 = wstd[3];
					laufbahnplanungSummen.wochenstundenQ21 = wstd[4];
					laufbahnplanungSummen.wochenstundenQ22 = wstd[5];

					laufbahnplanungSummen.wochenstundenDurchschnittEF = (laufbahnplanungSummen.wochenstundenEF1 + laufbahnplanungSummen.wochenstundenEF2) / 2.0;
					laufbahnplanungSummen.wochenstundenDurchschnittQPh = (laufbahnplanungSummen.wochenstundenQ11 + laufbahnplanungSummen.wochenstundenQ12 + laufbahnplanungSummen.wochenstundenQ21 + laufbahnplanungSummen.wochenstundenQ22) / 4.00;
					laufbahnplanungSummen.wochenstundenGesamt = (laufbahnplanungSummen.wochenstundenEF1 + laufbahnplanungSummen.wochenstundenEF2 + laufbahnplanungSummen.wochenstundenQ11 + laufbahnplanungSummen.wochenstundenQ12 + laufbahnplanungSummen.wochenstundenQ21 + laufbahnplanungSummen.wochenstundenQ22) / 2.0;
				} catch (final Exception ex) {
					throw OperationError.INTERNAL_SERVER_ERROR.exception("Die Daten zur Laufbahn und zum Abitur des Schülers mit der ID " + schuelerID + " und die Einstellungen zu den Fächern der Oberstufe des Abiturjahrgangs " + abidaten.abiturjahr + " sind vermutlich inkonsistent. Folgender Fehler ist aufgetreten: " + ex.getMessage());
				}
			}

			result.add(laufbahnplanungSummen);
		}

		// Geben die Ergebnis-Liste mit den Core-DTOs zurück
        return result;
    }


}
