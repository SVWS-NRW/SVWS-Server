package de.svws_nrw.module.pdf.htmlcontexts;

import de.svws_nrw.module.pdf.drucktypes.DruckGostKursplanungKurs;
import de.svws_nrw.module.pdf.drucktypes.DruckGostKursplanungKursSchueler;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.HtmlContext;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKursplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKursplanungKurse extends HtmlContext {

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
		List<Long> datenKursIDs = !kursIDs.isEmpty() ? new ArrayList<>(kursIDs) : new ArrayList<>(datenManager.kursGetListeSortiertNachFachKursartNummer().stream().map(k -> k.id).toList());

		// Collator für die alphabetische Sortierung der Schüler
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);

		// Liste der Kurse aus dem Blockungsergebnis
		final List<DruckGostKursplanungKurs> kursplanungskurse = new ArrayList<>();

		for (final Long kursID : datenKursIDs) {

			// Die Kursschüler können in einer beliebigen Reihenfolge sein. Für die Ausgabe der Liste sollten
			// sie aber in alphabetischer Reihenfolge der Schüler sein.
			// Erzeuge daher eine Liste mit Schülern, die in der alphabetischen Reihenfolge der Schüler sortiert ist
			final List<SchuelerStammdaten> sortiertKursschueler = DataSchuelerStammdaten.getListStammdaten(conn, ergebnisManager.getOfKursSchuelermenge(kursID).stream().map(ks -> ks.id).toList()).stream()
					.sorted(Comparator.comparing((final SchuelerStammdaten s) -> s.nachname, colGerman)
							.thenComparing((final SchuelerStammdaten s) -> s.vorname, colGerman)
							.thenComparing((final SchuelerStammdaten s) -> s.id))
					.toList();

			final List<DruckGostKursplanungKursSchueler> listeKursschueler = sortiertKursschueler.stream().map(ks -> {
				final DruckGostKursplanungKursSchueler kursSchueler = new DruckGostKursplanungKursSchueler(ks);
				kursSchueler.belegung = (ergebnisManager.getOfSchuelerOfKursFachwahl(ks.id, kursID).istSchriftlich ? "s" : "m");
				kursSchueler.abiturfach = (ergebnisManager.getOfSchuelerOfKursFachwahl(ks.id, kursID).abiturfach != null) ? ergebnisManager.getOfSchuelerOfKursFachwahl(ks.id, kursID).abiturfach.toString() : "";
				return kursSchueler;
			}).toList();

			final DruckGostKursplanungKurs kurs =  new DruckGostKursplanungKurs();

			kurs.id = kursID;
			kurs.gostHalbjahr =	GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel;
			kurs.Bezeichnung = datenManager.kursGetName(kursID);
			kurs.lehrkraefte = datenManager.kursGetLehrkraefteSortiert(kursID).isEmpty() ? "" : datenManager.kursGetLehrkraefteSortiert(kursID).stream().map(l -> l.kuerzel).collect(Collectors.joining(","));
			kurs.kursart = GostKursart.fromID(ergebnisManager.getKursE(kursID).kursart).kuerzel;
			kurs.anzahlTeilnehmer = ergebnisManager.getOfKursAnzahlSchueler(kursID);
			kurs.anzahlExterneTeilnehmer = ergebnisManager.getOfKursAnzahlSchuelerExterne(kursID);
			kurs.anzahlKlausurteilnehmer = ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kursID);
			kurs.anzahlAB12 = ergebnisManager.getOfKursAnzahlSchuelerAbiturLK(kursID);
			kurs.anzahlAB3 = ergebnisManager.getOfKursAnzahlSchuelerAbitur3(kursID);
			kurs.anzahlAB4 = ergebnisManager.getOfKursAnzahlSchuelerAbitur4(kursID);
			kurs.Kursschueler = listeKursschueler;

			kursplanungskurse.add(kurs);
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Kursplanungskurse", kursplanungskurse);
		context.setVariable("BlockungsergebnisId", blockungsergebnisID);
		context.setVariable("Abiturjahr", datenManager.daten().abijahrgang);
		context.setVariable("GostHalbjahr", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);

		super.setContext(context);
	}
}
