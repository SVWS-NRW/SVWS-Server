package de.svws_nrw.module.reporting.html.contexts.gost.kursplanung;

import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.proxytypes.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.util.List;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKursplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKursplanungBlockungsergebnis extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param idBlockungsergebnis	ID des Blockungsergebnisses, aus der Context erstellt werden soll.
	 * @param filterSchueler 		Legt fest, ob im Blockungsergebnis eine zusätzliche gefilterte Liste der Schüler erzeugt werden soll.
	 * @param filterIdsSchueler 	Liste von Schüler-IDs zur Filterung.
	 * @param filterKurse 			Legt fest, ob im Blockungsergebnis eine zusätzliche gefilterte Liste der Kurse erzeugt werden soll.
	 * @param filterIdsKurse	 	Liste von Kurs-IDs zur Filterung.
	 */
	public HtmlContextGostKursplanungBlockungsergebnis(final DBEntityManager conn, final Long idBlockungsergebnis, final boolean filterSchueler, final List<Long> filterIdsSchueler, final boolean filterKurse, final List<Long> filterIdsKurse) {
		erzeugeContext(conn, idBlockungsergebnis, filterSchueler, filterIdsSchueler, filterKurse, filterIdsKurse);
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param idBlockungsergebnis	ID des Blockungsergebnisses, aus dem der Context erstellt werden soll.
	 * @param filterSchueler 		Legt fest, ob im Blockungsergebnis eine zusätzliche gefilterte Liste der Schüler erzeugt werden soll.
	 * @param filterIdsSchueler 	Liste von Schüler-IDs zur Filterung.
	 * @param filterKurse 			Legt fest, ob im Blockungsergebnis eine zusätzliche gefilterte Liste der Kurse erzeugt werden soll.
	 * @param filterIdsKurse	 	Liste von Kurs-IDs zur Filterung.
	 */
	private void erzeugeContext(final DBEntityManager conn, final Long idBlockungsergebnis, final boolean filterSchueler, final List<Long> filterIdsSchueler, final boolean filterKurse, final List<Long> filterIdsKurse) throws WebApplicationException {

		// ####### Daten validieren. Wirft eine Exception bei Fehlern, andernfalls werden die Manager für die Blockung erzeugt. ###############################

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Datenbankverbindung ungültig.");

		if (idBlockungsergebnis == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (WebApplicationException ex) {
			throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
		}

		final ReportingRepository reportingRepository = new ReportingRepository(conn);
		final ProxyReportingGostKursplanungBlockungsergebnis blockungsergebnis = new ProxyReportingGostKursplanungBlockungsergebnis(reportingRepository, idBlockungsergebnis);
		blockungsergebnis.setFilterIdsSchueler(filterIdsSchueler);
		blockungsergebnis.setFilterSchueler(filterSchueler);
		blockungsergebnis.setFilterIdsKurse(filterIdsKurse);
		blockungsergebnis.setFilterKurse(filterKurse);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Blockungsergebnis", blockungsergebnis);

		super.setContext(context);
	}
}
