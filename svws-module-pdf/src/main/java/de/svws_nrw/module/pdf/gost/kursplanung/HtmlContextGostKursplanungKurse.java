package de.svws_nrw.module.pdf.gost.kursplanung;

import de.svws_nrw.core.data.druck.DruckGostKursplanungKurs;
import de.svws_nrw.core.data.druck.DruckGostKursplanungKursSchueler;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.HtmlContext;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus der Context erstellt werden soll.
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
		final DTOGostBlockungZwischenergebnis dtoErgebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, blockungsergebnisID);
		if (dtoErgebnis == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		// Im Ergebnis ist auch die ID der Blockung enthalten. Blockungsdatenmanager auf Basis der Blockung-ID des Ergebnisses erstellen.
		final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(dtoErgebnis.Blockung_ID);

		// Kurs-IDs auf Validität prüfen, d. h. ob diese Kurse zum Blockungsergebnis gehören.
		for (final Long kursID : kursIDs) {
			if (datenManager.kursGetListeSortiertNachFachKursartNummer().stream().noneMatch(k -> (k.id == kursID)))
				throw OperationError.NOT_FOUND.exception("Ungültige Kurs-ID in Bezug auf die angegebene Blockung vorhanden.");
		}

		final GostBlockungsergebnis blockungsergebnis = (new DataGostBlockungsergebnisse(conn)).getErgebnis(dtoErgebnis, datenManager);
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
			final List<DTOSchueler> sortiertKursschueler = conn.queryNamed("DTOSchueler.id.multiple", ergebnisManager.getOfKursSchuelermenge(kursID).stream().map(ks -> ks.id).toList(), DTOSchueler.class).stream()
					.sorted(Comparator.comparing((final DTOSchueler s) -> s.Nachname, colGerman)
							.thenComparing((final DTOSchueler s) -> s.Vorname, colGerman)
							.thenComparing((final DTOSchueler s) -> s.ID))
					.toList();

			final List<DruckGostKursplanungKursSchueler> listeKursschueler = sortiertKursschueler.stream().map(soks ->
					{
						final var dto =  new DruckGostKursplanungKursSchueler();
						dto.Id = soks.ID;
						dto.Nachname = soks.Nachname;
						dto.Vorname = soks.Vorname;
						dto.Geschlecht = soks.Geschlecht.kuerzel;
						dto.Geburtsdatum = LocalDate.parse(soks.Geburtsdatum).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
						dto.ExterneSchulnummer = soks.ExterneSchulNr;
						dto.Belegung = (ergebnisManager.getOfSchuelerOfKursFachwahl(soks.ID, kursID).istSchriftlich ? "s" : "m");
						dto.Abiturfach = (ergebnisManager.getOfSchuelerOfKursFachwahl(soks.ID, kursID).abiturfach != null) ? ergebnisManager.getOfSchuelerOfKursFachwahl(soks.ID, kursID).abiturfach.toString() : "";
						return dto;
					}
			).toList();

			final DruckGostKursplanungKurs kurs =  new DruckGostKursplanungKurs();

			kurs.Id = kursID;
			kurs.GostHalbjahr =	GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel;
			kurs.Bezeichnung = datenManager.kursGetName(kursID);
			kurs.Lehrkraefte = datenManager.kursGetLehrkraefteSortiert(kursID).isEmpty() ? "" : datenManager.kursGetLehrkraefteSortiert(kursID).stream().map(l -> l.kuerzel).collect(Collectors.joining(","));
			kurs.Kursart = GostKursart.fromID(ergebnisManager.getKursE(kursID).kursart).kuerzel;
			kurs.AnzahlTeilnehmer = ergebnisManager.getOfKursAnzahlSchueler(kursID);
			kurs.AnzahlExterneTeilnehmer = ergebnisManager.getOfKursAnzahlSchuelerExterne(kursID);
			kurs.AnzahlKlausurteilnehmer = ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kursID);
			kurs.AnzahlAB12 = ergebnisManager.getOfKursAnzahlSchuelerAbiturLK(kursID);
			kurs.AnzahlAB3 = ergebnisManager.getOfKursAnzahlSchuelerAbitur3(kursID);
			kurs.AnzahlAB4 = ergebnisManager.getOfKursAnzahlSchuelerAbitur4(kursID);
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
