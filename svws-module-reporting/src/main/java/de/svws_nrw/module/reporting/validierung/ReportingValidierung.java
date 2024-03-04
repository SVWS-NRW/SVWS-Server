package de.svws_nrw.module.reporting.validierung;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DBUtilsGostAbitur;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.WebApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingValidierung {

	/**
	 * Validiert von der API übergebene Daten für Schüler. Bei fehlenden oder unstimmigen Daten wird eine WebApplicationException geworfen.
	 * Über den Parameter cacheDaten kann gesteuert werden, ob bereits abgerufene Daten aus der DB im Repository zwischengespeichert werden soll.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 * @param idsSchueler   		Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 * @param mitGostDaten 			Legt fest, ob der Daten zur gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 * @param mitAbiturDaten 		Legt fest, ob die Daten zum Abitur in der gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 * @param cacheDaten 			Legt fest, ob die zur Validierung geladenen Daten im Repository gespeichert werden sollen.
	 */
	public void validiereSchuelerDaten(final ReportingRepository reportingRepository, final List<Long> idsSchueler, final boolean mitGostDaten, final boolean mitAbiturDaten, final boolean cacheDaten) throws WebApplicationException {

		// Grunddaten prüfen.
		final DBEntityManager conn = reportingRepository.conn();

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Keine Datenbankverbindung übergeben.");

		if (idsSchueler == null || idsSchueler.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Schueler-IDs übergeben.");

		// Prüfe die Schüler-IDs. Erzeuge Maps, damit auch später leicht auf die Schülerdaten zugegriffen werden kann.
		final Map<Long, SchuelerStammdaten> mapSchueler = DataSchuelerStammdaten.getListStammdaten(conn, idsSchueler).stream().collect(Collectors.toMap(s -> s.id, s -> s));
		for (final Long sID : idsSchueler)
			if (mapSchueler.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden ungültige Schüler-IDs übergeben.");


		// Nur, wenn Daten zur gymnasialen Oberstufe mit angefordert werden.
		if (mitGostDaten) {
			// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
			try {
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
			} catch (WebApplicationException ex) {
				throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
			}

			final Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten = new HashMap<>(new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn).getMapFromIDs(idsSchueler));

			for (final Long sID : idsSchueler)
				if (mapGostBeratungsdaten.get(sID) == null)
					throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");

			reportingRepository.mapGostBeratungsdaten().putAll(mapGostBeratungsdaten);
		}

		// Nur, wenn Daten zum Abitur in der gymnasialen Oberstufe mit angefordert werden.
		if (mitAbiturDaten) {
			// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
			try {
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
			} catch (WebApplicationException ex) {
				throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
			}

			final Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten = new HashMap<>();

			for (final Long sID : idsSchueler) {
				try {
					mapGostSchuelerAbiturdaten.put(sID, DBUtilsGostAbitur.getAbiturdaten(conn, sID));
				} catch (WebApplicationException ex) {
					throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.");
				}
			}

			reportingRepository.mapGostSchuelerAbiturdaten().putAll(mapGostSchuelerAbiturdaten);
		}

		// Daten sind valide, speichere diese nun gemäß Parameter im Repository.
		if (cacheDaten) {
			reportingRepository.mapSchuelerStammdaten().putAll(mapSchueler);
			reportingRepository.mapAktuelleLernabschnittsdaten().putAll(new DataSchuelerLernabschnittsdaten(conn).getListFromSchuelerIDsUndSchuljahresabschnittID(idsSchueler, reportingRepository.aktuellerSchuljahresabschnitt().id, false).stream().collect(Collectors.toMap(l -> l.schuelerID, l -> l)));
		}
	}

}
