package de.svws_nrw.module.pdf.html.contexts.gost.kursplanung;

import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.html.base.HtmlContext;
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
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus der Context erstellt werden soll.
	 */
	public HtmlContextGostKursplanungBlockungsergebnis(final DBEntityManager conn, final Long blockungsergebnisID) {
		erzeugeContext(conn, blockungsergebnisID);
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
		int anzahlSchienen = 0;

		// Liste der Schienen aus der Blockung einlesen.
		final List<GostBlockungSchiene> schienen = datenManager.schieneGetListe();

		// Daten der Schienen und der in ihnen enthaltenen Kurse sammeln.
		for (final GostBlockungSchiene schiene : schienen) {
			if (!ergebnisManager.getSchieneE(schiene.id).kurse.isEmpty()) {
				anzahlSchienen++;
			}
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Blockungsbezeichnung", datenManager.daten().name);
		context.setVariable("BlockungsergebnisId", blockungsergebnisID);
		context.setVariable("Abiturjahr", datenManager.daten().abijahrgang);
		context.setVariable("GostHalbjahr", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
		context.setVariable("AnzahlSchienen", anzahlSchienen);
		context.setVariable("MaxKurseProSchiene", ergebnisManager.getOfSchieneMaxKursanzahl());
		context.setVariable("AnzahlSchueler", datenManager.schuelerGetAnzahl());
		context.setVariable("AnzahlExterne", ergebnisManager.getAnzahlSchuelerExterne());
		context.setVariable("AnzahlDummy", ergebnisManager.getAnzahlSchuelerDummy());

		super.setContext(context);
	}
}
