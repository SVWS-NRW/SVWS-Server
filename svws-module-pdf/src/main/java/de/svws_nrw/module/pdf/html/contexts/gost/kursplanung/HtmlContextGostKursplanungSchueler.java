package de.svws_nrw.module.pdf.html.contexts.gost.kursplanung;

import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.html.base.HtmlContext;
import de.svws_nrw.module.pdf.html.utils.HtmlContextUtils;
import de.svws_nrw.module.pdf.reptypes.gost.kursplanung.RepGostKursplanungSchueler;
import de.svws_nrw.module.pdf.reptypes.gost.kursplanung.RepGostKursplanungSchuelerKurs;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKursplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKursplanungSchueler extends HtmlContext {

	/**
	 * Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht.
	 */
	private ArrayList<RepGostKursplanungSchueler> kursplanungSchueler = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus der Context erstellt werden soll.
	 * @param schuelerIDs           	Liste der IDs der Kurse, die berücksichtigt werden sollen.
	 */
	public HtmlContextGostKursplanungSchueler(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) {
		erzeugeContext(conn, blockungsergebnisID, schuelerIDs);
	}

	/**
	 * Gibt die interne Liste des Contexts mit den Daten zurück.
	 * @return	Liste mit den Daten
	 */
	public List<RepGostKursplanungSchueler> getGostKursplanungSchueler() {
		return kursplanungSchueler;
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param conn         			Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem der Context erstellt werden soll.
	 * @param schuelerIDs           Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 */
	private void erzeugeContext(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) throws WebApplicationException {

		// ####### Daten validieren. Wirft eine Exception bei Fehlern, andernfalls werden die Manager für die Blockung erzeugt. ###############################

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Datenbankverbindung ungültig.");

		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		if (schuelerIDs == null)
			throw OperationError.NOT_FOUND.exception("Keine Schüler-IDs übergeben.");

		// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (final WebApplicationException ex) {
			throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
		}

		// Hole das Blockungsergebnis über die ID aus der DB.
		final GostBlockungsergebnis blockungsergebnis = (new DataGostBlockungsergebnisse(conn)).getErgebnisFromID(blockungsergebnisID);

		// Im Ergebnis ist auch die ID der Blockung enthalten. Blockungsdatenmanager auf Basis der Blockung-ID des Ergebnisses erstellen.
		final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(blockungsergebnis.blockungID);

		// Schüler-IDs auf Validität prüfen, d. h. ob diese Schüler zum Blockungsergebnis gehören.
		for (final Long schuelerID : schuelerIDs) {
			if (datenManager.schuelerGetListe().stream().noneMatch(s -> (s.id == schuelerID)))
				throw OperationError.NOT_FOUND.exception("Ungültige Schüler-ID in Bezug auf die angegebene Blockung vorhanden.");
		}

		final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);


		// ####### Daten sind valide. Erzeuge nun Datenstruktur und daraus den Daten-Context. #################################################################

		// Collator für die alphabetische Sortierung der Schüler
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);

		// Die Schüler der übergebenen IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe der Liste sollten
		// sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schülern, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final List<SchuelerStammdaten> sortiertSchueler = DataSchuelerStammdaten.getListStammdaten(conn, schuelerIDs).stream()
			.sorted(Comparator.comparing((final SchuelerStammdaten s) -> s.nachname, colGerman)
				.thenComparing((final SchuelerStammdaten s) -> s.vorname, colGerman)
				.thenComparing((final SchuelerStammdaten s) -> s.id))
			.toList();


		// Liste der Kursplanungsschüler, die auf Basis der Schüler-IDs und deren belegter Kurse erstellt wird.
		kursplanungSchueler = new ArrayList<>();

		ergebnisManager.kursSetSortierungKursartFachNummer();
		for (final SchuelerStammdaten schueler : sortiertSchueler) {
			// Liste der Kurse des aktuellen Schülers initialisieren.
			final List<RepGostKursplanungSchuelerKurs> schuelerKurse = new ArrayList<>();

			// Sortiere und bestimme die IDs aller Kurse des Schülers
			final List<Long> schuelerKursIDs = ergebnisManager.getOfSchuelerKursmenge(schueler.id).stream().map(k -> k.id).toList();

			// Ermittle die Daten der Kurse des Schülers
			for (final Long kursID : schuelerKursIDs) {
				schuelerKurse.add(
					new RepGostKursplanungSchuelerKurs(
						HtmlContextUtils.getDruckGostKursplanungKurs(conn, datenManager, ergebnisManager, kursID),
						ergebnisManager.getOfSchuelerOfKursFachwahl(schueler.id, kursID).istSchriftlich,
						(ergebnisManager.getOfSchuelerOfKursFachwahl(schueler.id, kursID).abiturfach != null) ? ergebnisManager.getOfSchuelerOfKursFachwahl(schueler.id, kursID).abiturfach.toString() : "")
				);
			}
			// Füge den neuen Schüler mit seinen Kursen hinzu.
			kursplanungSchueler.add(new RepGostKursplanungSchueler(schueler, schuelerKurse));
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("KursplanungSchueler", kursplanungSchueler);

		super.setContext(context);
	}
}
