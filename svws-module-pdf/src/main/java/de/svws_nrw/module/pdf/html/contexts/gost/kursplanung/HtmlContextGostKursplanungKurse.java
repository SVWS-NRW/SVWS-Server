package de.svws_nrw.module.pdf.html.contexts.gost.kursplanung;

import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.html.base.HtmlContext;
import de.svws_nrw.module.pdf.html.utils.HtmlContextUtils;
import de.svws_nrw.module.pdf.reptypes.gost.kursplanung.RepGostKursplanungKurs;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKursplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKursplanungKurse extends HtmlContext {

	/**
	 * Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht.
	 */
	private ArrayList<RepGostKursplanungKurs> kursplanungKurse = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus der Context erstellt werden soll.
	 * @param kursIDs           	Liste der IDs der Kurse, die berücksichtigt werden sollen.
	 */
	public HtmlContextGostKursplanungKurse(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> kursIDs) {
		erzeugeContext(conn, blockungsergebnisID, kursIDs);
	}

	/**
	 * Gibt die interne Liste des Contexts mit den Daten zurück.
	 * @return	Liste mit den Daten
	 */
	public List<RepGostKursplanungKurs> getGostKursplanungKurse() {
		return kursplanungKurse;
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem der Context erstellt werden soll.
	 * @param kursIDs           	Liste der IDs der Kurse, die berücksichtigt werden sollen.
	 */
	private void erzeugeContext(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> kursIDs) throws WebApplicationException {

		// ####### Daten validieren. Wirft eine Exception bei Fehlern, andernfalls werden die Manager für die Blockung erzeugt. ###############################

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Datenbankverbindung ungültig.");

		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		if (kursIDs == null)
			throw OperationError.NOT_FOUND.exception("Keine Kurs-IDs übergeben.");

		// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (WebApplicationException ex) {
			throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
		}

		// Hole das Blockungsergebnis über die ID aus der DB.
		final GostBlockungsergebnis blockungsergebnis = (new DataGostBlockungsergebnisse(conn)).getErgebnisFromID(blockungsergebnisID);

		// Im Ergebnis ist auch die ID der Blockung enthalten. Blockungsdatenmanager auf Basis der Blockung-ID des Ergebnisses erstellen.
		final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(blockungsergebnis.blockungID);

		// Kurs-IDs auf Validität prüfen, d. h. ob diese Kurse zum Blockungsergebnis gehören.
		for (final Long kursID : kursIDs) {
			if (datenManager.kursGetListeSortiertNachFachKursartNummer().stream().noneMatch(k -> (k.id == kursID)))
				throw OperationError.NOT_FOUND.exception("Ungültige Kurs-ID in Bezug auf die angegebene Blockung vorhanden.");
		}

		final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);


		// ####### Daten sind valide. Erzeuge nun Datenstruktur und daraus den Daten-Context. #################################################################

		// Bei leerer Liste von Kurs-IDs sollen alle Kurse der Blockung verwendet werden.
		final List<Long> kursplanungKursIds = !kursIDs.isEmpty() ? new ArrayList<>(kursIDs) : new ArrayList<>(datenManager.kursGetListeSortiertNachFachKursartNummer().stream().map(k -> k.id).toList());

		// Liste der Kurse aus dem Blockungsergebnis
		kursplanungKurse = new ArrayList<>();

		for (final Long kursID : kursplanungKursIds) {
			kursplanungKurse.add(
				HtmlContextUtils.getDruckGostKursplanungKurs(conn, datenManager, ergebnisManager, kursID)
			);
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("KursplanungKurse", kursplanungKurse);

		super.setContext(context);
	}
}
