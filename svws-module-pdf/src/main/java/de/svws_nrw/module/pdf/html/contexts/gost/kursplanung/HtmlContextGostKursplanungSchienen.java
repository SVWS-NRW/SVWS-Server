package de.svws_nrw.module.pdf.html.contexts.gost.kursplanung;

import de.svws_nrw.core.data.gost.GostBlockungSchiene;
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
import de.svws_nrw.module.pdf.reptypes.gost.kursplanung.RepGostKursplanungSchiene;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKursplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKursplanungSchienen extends HtmlContext {

	/**
	 * Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht.
	 */
	private ArrayList<RepGostKursplanungSchiene> kursplanungsschienen = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus der Context erstellt werden soll.
	 */
	public HtmlContextGostKursplanungSchienen(final DBEntityManager conn, final Long blockungsergebnisID) {
		erzeugeContext(conn, blockungsergebnisID);
	}

	/**
	 * Gibt die interne Liste des Contexts mit den Daten zurück.
	 * @return	Liste mit den Daten
	 */
	public List<RepGostKursplanungSchiene> getGostKursplanungSchienen() {
		return kursplanungsschienen;
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem der Context erstellt werden soll.
	 */
	private void erzeugeContext(final DBEntityManager conn, final Long blockungsergebnisID) throws WebApplicationException {

		// ####### Daten validieren. Wirft eine Exception bei Fehlern, andernfalls werden die Manager für die Blockung erzeugt. ###############################

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Datenbankverbindung ungültig.");

		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

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
		final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);


		// ####### Daten sind valide. Erzeuge nun Datenstruktur und daraus den Daten-Context. #################################################################

		// Liste für die Ausgabe der Schienen initialisieren.
		kursplanungsschienen = new ArrayList<>();

		// Liste der Schienen aus der Blockung einlesen.
		final List<GostBlockungSchiene> schienen = datenManager.schieneGetListe();

		// Daten der Schienen und der in ihnen enthaltenen Kurse sammeln.
		for (final GostBlockungSchiene schiene : schienen) {
			if (!ergebnisManager.getSchieneE(schiene.id).kurse.isEmpty()) {
				// Liste der Kurse der Schiene ermitteln
				final List<RepGostKursplanungKurs> schieneKurse = new ArrayList<>();
				ergebnisManager.kursSetSortierungFachKursartNummer();
				List<Long> schieneKursIDs = new ArrayList<>(ergebnisManager.getOfSchieneKursmengeSortiert(schiene.id).stream().map(k -> k.id).toList());
				for (final Long kursID : schieneKursIDs) {
					schieneKurse.add(HtmlContextUtils.getDruckGostKursplanungKurs(conn, datenManager, ergebnisManager, kursID));
				}

				kursplanungsschienen.add(
					new RepGostKursplanungSchiene(
						schiene.id,
						schiene.bezeichnung,
						ergebnisManager.getOfSchieneAnzahlSchueler(schiene.id),
						ergebnisManager.getOfSchieneAnzahlSchuelerExterne(schiene.id),
						ergebnisManager.getOfSchieneAnzahlSchuelerDummy(schiene.id),
						ergebnisManager.getOfSchieneHatKollision(schiene.id),
						schieneKurse,
						ergebnisManager.getOfSchieneKursmengeMitKollisionen(schiene.id).stream().map(k -> k.id).toList(),
						ergebnisManager.getOfSchieneSchuelermengeMitKollisionen(schiene.id).stream().toList()
					)
				);
			}
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Schienen", kursplanungsschienen);

		super.setContext(context);
	}
}
