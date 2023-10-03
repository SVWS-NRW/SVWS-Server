package de.svws_nrw.module.pdf.gost;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.PDFCreator;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer Übersicht der Kurse-Schienen-Zuordnung
 * eines Blockungsergebnisses, entweder für das gesamte Ergebnis oder für einzelne Schüler.
 */
public final class PDFGostKurseSchienenZuordnung extends PDFCreator {

	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostKurseSchienenZuordnung.html.txt");
	private static final String css = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostKurseSchienenZuordnung.css.txt");

	/** Der Dateiname für die PDF-Datei */
	private final String filename;


	/**
	 * Erstellt die Kurse-Schienen-Matrix für eine Jahrgangsstufe oder für einen Schüler der Stufe auf Basis der HTML-Vorlage
	 *
	 * @param dateiname        	Dateiname der finalen PDF-Datei.
	 * @param schulnummer      	Schulnummer der Schule, deren Blockungsergebnis verwendet wird.
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurse-Schienen-Zuordnung ausgegeben werden soll.
	 * @param zeigeExterneDummy Legt fest, ob die Anzahlen zu Externen und Dummy in der Übersicht angezeigt werden sollen.
	 * @param schuelerID 		ID des Schülers, dessen Kurse-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 * @param lfdNr 			Laufende Nummer der Kurse-Schienen-Zuordnung bei der Ausgabe für mehrere SuS.
	 */
	private PDFGostKurseSchienenZuordnung(final String dateiname,
										  final String schulnummer,
										  final GostBlockungsdatenManager datenManager,
										  final GostBlockungsergebnisManager ergebnisManager,
										  final boolean zeigeExterneDummy,
										  final Long schuelerID,
										  final Integer lfdNr) {

		super("Kurse-Schienen-Zuordnung", html, css);

		this.querformat = true;
		this.filename = dateiname;

		// Definiert, ob die PDF-Datei für einen Schüler (oder einen Abiturjahrgang) erstellt wird.
		final boolean istSchuelerPDF = (schuelerID != null);

		// Ersetze die Felder des Templates mit Daten
		if (istSchuelerPDF) {
			bodyData.put("INHALT", "%s, %s".formatted(datenManager.schuelerGet(schuelerID).nachname, datenManager.schuelerGet(schuelerID).vorname));
			bodyData.put("SCHUELERSTATISTIK", "");
		} else {
			bodyData.put("INHALT", "Abitur %d".formatted(datenManager.daten().abijahrgang));
			if (zeigeExterneDummy)
				bodyData.put("SCHUELERSTATISTIK", "(Gesamt: %d SuS, davon %d/%d Externe/Platzhalter)".formatted(datenManager.schuelerGetAnzahl(), ergebnisManager.getAnzahlSchuelerExterne(), ergebnisManager.getAnzahlSchuelerDummy()));
			else
				bodyData.put("SCHUELERSTATISTIK", "(Gesamt: %d SuS)".formatted(datenManager.schuelerGetAnzahl()));
		}

		bodyData.put("AKTUELLESHALBJAHR", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
		bodyData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		bodyData.put("SCHULNUMMER", schulnummer);
		if (lfdNr == null) {
			bodyData.put("INFORMATIONEN", "%s (Erg-ID %d) - Angaben zu SuS in den Kursen: Gesamt (Schriftlich, Abiturfach)%s".formatted(datenManager.daten().name, ergebnisManager.getErgebnis().id, zeigeExterneDummy ? "(Externe, Platzhalter)" : ""));

		} else
			bodyData.put("INFORMATIONEN", "%s (Erg-ID %d) - Ausdruck lfd. Nr: %03d".formatted(datenManager.daten().name, ergebnisManager.getErgebnis().id, lfdNr));

		// Grundwerte für die Kurse-Schienen-Matrix ermitteln
		final List<GostBlockungSchiene> schienen = datenManager.schieneGetListe();
		final int maxKurseInSchienen = ergebnisManager.getOfSchieneMaxKursanzahl();
		// Zähle die Schienen manuel, da leere Schienen später nicht ausgegeben werden.
		int anzahlSchienen = 0;
		for (final GostBlockungSchiene schiene : schienen) {
			if (!ergebnisManager.getSchieneE(schiene.id).kurse.isEmpty()) {
				anzahlSchienen++;
			}
		}

		// Die Spalten der Matrix werden gemäß maximale Kurszahl fix gewählt und der evtl. Rest als Spalte zum Füllen angehängt.
		// Zudem wird die Schriftgröße der Matrix angepasst, wiederum abhängig von der maximalen Kurszahl in einer Schiene.
		int anzahlSpaltenAnsichtOptimierung;
		String spaltenbreite;
		String spaltenbreiteRest;
		String schriftgroesse;

		if (maxKurseInSchienen <= 8 && anzahlSchienen <= 14) {
			anzahlSpaltenAnsichtOptimierung = 8;
			spaltenbreite = "11%";
			spaltenbreiteRest = "1%";
			schriftgroesse = "11px";
		} else if (maxKurseInSchienen <= 12  && anzahlSchienen <= 16) {
			anzahlSpaltenAnsichtOptimierung = 12;
			spaltenbreite = "7.5%";
			spaltenbreiteRest = "2.5%";
			schriftgroesse = "10px";
		} else {
			anzahlSpaltenAnsichtOptimierung = 16;
			spaltenbreite = "5.8%";
			spaltenbreiteRest = "1.4%";
			schriftgroesse = "9px";
		}

		// Erste Zeile der Kurse-Schienen-Matrix für die Spaltenbreiten erzeugen. Die Spalte ist unsichtbar und dient nur der Formatierung.
		final StringBuilder zeileSpaltenbreiten = new StringBuilder();
		for (int i = 0; i <= anzahlSpaltenAnsichtOptimierung; i++) {
			zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreite));
		}
		zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreiteRest));
		bodyData.put("SPALTENDEFINITION", zeileSpaltenbreiten.toString());
		bodyData.put("SCHRIFTGROESSE", schriftgroesse);

		// Hier wird die eigentliche Kurse-Schienen-Matrix erstellt.
		bodyData.put("SCHIENENUNDKURSE", getSchienenUndKurseZeilen(datenManager, ergebnisManager, zeigeExterneDummy, schuelerID, schienen, istSchuelerPDF));
	}


	/**
	 * Erstellt der HTML-COde für die Kurse-Schienen-Matrix.
	 *
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurse-Schienen-Zuordnung ausgegeben werden soll.
	 * @param zeigeExterneDummy Legt fest, ob die Anzahlen zu Externen und Dummy in der Übersicht angezeigt werden sollen.
	 * @param schuelerID		ID des Schülers, dessen Kurse-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 * @param schienen			Schienen des Blockungsergebnisses
	 * @param istSchuelerPDF	Gibt an, ob das PDF für Schüler erstellt wird (true) oder für das Ergebnis insgesamt (false)
	 * @return					Der gesamte HTML-Code für die Zeilen der Kurse-Schienen-Matrix
	 */
	private static String getSchienenUndKurseZeilen(final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final boolean zeigeExterneDummy, final Long schuelerID, final List<GostBlockungSchiene> schienen, final boolean istSchuelerPDF) {
		// Nun für jede Schiene eine Zeile ind er Matrix erzeugen.
		final StringBuilder zeileMatrix = new StringBuilder();
		for (final GostBlockungSchiene schiene : schienen) {
			if (!ergebnisManager.getSchieneE(schiene.id).kurse.isEmpty()) {
				zeileMatrix.append("<tr>");

				// Zeilenkopf für Schiene mit zugehörigen Informationen erstellen.
				zeileMatrix.append(("<td style=\"text-align: left;%s\"><b>%s</b>").formatted(((ergebnisManager.getOfSchieneHatKollision(schiene.id) && !istSchuelerPDF) ? " background-color: rgb(255,0,0);" : ""), schiene.bezeichnung));
				if (istSchuelerPDF)
					zeileMatrix.append("</td>");
				else {
					zeileMatrix.append(("<p class=\"tinyfont\">"
						+ "<b>%d</b> K. mit <b>%d</b> S.")
						.formatted(ergebnisManager.getSchieneE(schiene.id).kurse.size(), ergebnisManager.getOfSchieneAnzahlSchueler(schiene.id)));

					if (zeigeExterneDummy)
						zeileMatrix.append(("<br/>(%d/%d Ext./P.h.)</p></td>").formatted(ergebnisManager.getOfSchieneAnzahlSchuelerExterne(schiene.id), ergebnisManager.getOfSchieneAnzahlSchuelerDummy(schiene.id)));
					else
						zeileMatrix.append("</p></td>");
				}

				zeileMatrix.append(getKurseInZeile(datenManager, ergebnisManager, zeigeExterneDummy, schuelerID, schiene, istSchuelerPDF));
				zeileMatrix.append("</tr>");
			}
		}
		return zeileMatrix.toString();
	}


	/**
	 * Erstellt den HTML-Code für die Kurseinträge einer Schiene.
	 *
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurse-Schienen-Zuordnung ausgegeben werden soll.
	 * @param zeigeExterneDummy Legt fest, ob die Anzahlen zu Externen und Dummy in der Übersicht angezeigt werden sollen.
	 * @param schuelerID		ID des Schülers, dessen Kurse-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 * @param schiene			Schiene des Blockungsergebnisses, für die Kurseinträge erstellt werden sollen
	 * @param istSchuelerPDF		Gibt an, ob das PDF für Schüler erstellt wird (true) oder für das Ergebnis insgesamt (false)
	 * @return					Der HTML-Code für die Kurse einer Schiene in der Kurse-Schienen-Matrix
	 */
	private static String getKurseInZeile(final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final boolean zeigeExterneDummy, final Long schuelerID, final GostBlockungSchiene schiene, final boolean istSchuelerPDF) {
		final StringBuilder zeileKurse = new StringBuilder();

		// Kurse mit deren Informationen in der aktuellen Zeile der Schiene ergänzen.
		for (final GostBlockungsergebnisKurs kurs : ergebnisManager.getSchieneE(schiene.id).kurse) {

			// Prüfe, ob der Kurs vom Schüler belegt ist. wenn ja, ermittle die Schriftlichkeit und evtl. welches Abiturfach es ist.
			final boolean istSchuelerKursbelegung = (kurs.schueler.contains(schuelerID));
			String eintragSchriMuendAbiFach = "";
			if (istSchuelerKursbelegung) {
				eintragSchriMuendAbiFach = ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).istSchriftlich ? "s" : "m";
				if (ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).abiturfach != null)
					eintragSchriMuendAbiFach += " (Abifach " + ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).abiturfach + ")";
			}

			// Lehrkräfte des Kurses als kommaspeparierte Liste darstellen
			final String eintragLehrkraefte = datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty() ? "----" : datenManager.kursGetLehrkraefteSortiert(kurs.id).stream().map(l -> l.kuerzel).collect(Collectors.joining(","));

			// Farbe des Hintergrundes gemäß Fachfarbe aus dem Client festlegen, wenn allgemeine Darstellung erfolgt oder Schüler den Kurs belegt.
			final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(kurs.fachID).kuerzel);
			if (fach != null && (!istSchuelerPDF || istSchuelerKursbelegung))
				zeileKurse.append("<td style=\"background-color: %s;\"><b>%s</b>".formatted(fach.getHMTLFarbeRGB().replace("rgba", "rgb"), datenManager.kursGetName(kurs.id)));
			else
				zeileKurse.append("<td><b>%s</b>".formatted(datenManager.kursGetName(kurs.id)));

			// Ab hier werden die Detailinfomationen zum Kurs ergänzt.
			zeileKurse.append("<p class=\"tinyfont\">");

			if (istSchuelerPDF) {
				if (istSchuelerKursbelegung) {
					zeileKurse.append(("%s-%s<br/>").formatted(ergebnisManager.getOfSchuelerOfFachKursart(schuelerID, kurs.fachID).kuerzel, eintragSchriMuendAbiFach));
				} else {
					zeileKurse.append("<br/>");
				}
				zeileKurse.append(("%s</p></td>").formatted(eintragLehrkraefte));
			} else {
				final int anzahlAbitur = ergebnisManager.getofKursAnzahlSchuelerAbiturLK(kurs.id) + ergebnisManager.getofKursAnzahlSchuelerAbitur3(kurs.id) + ergebnisManager.getofKursAnzahlSchuelerAbitur4(kurs.id);
				zeileKurse.append(("%s<br/>"
								   + "<b>%d</b> (%d,%d)")
								   .formatted(eintragLehrkraefte, ergebnisManager.getOfKursAnzahlSchueler(kurs.id), ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kurs.id), anzahlAbitur));
				if (zeigeExterneDummy)
					zeileKurse.append(("(%d,%d)</p></td>").formatted(ergebnisManager.getOfKursAnzahlSchuelerExterne(kurs.id), ergebnisManager.getOfKursAnzahlSchuelerDummy(kurs.id)));
				else
					zeileKurse.append("</p></td>");
			}
		}
		return zeileKurse.toString();
	}


	/**
	 * Erstellt ein neues Objekt des Typs PDFGostKurseSchienenZuordnung für die Kurse-Schienen-Zuordnung
	 * des gewählten Blockungsergebnisses.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	die ID der GostBlockungsergebnis, auf denen der GostBlockungsergebnisManager basiert.
	 * @param schuelerIDs 			Liste der ID der Schüler, deren Kurse-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 *
	 * @return 						das Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFGostKurseSchienenZuordnung getPDFmitKurseSchienenZuordnung(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) throws WebApplicationException {

		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		// Schuldaten sammeln, pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		DTOEigeneSchule schule;
		try {
			schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (WebApplicationException ex) {
			throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
		}
		final String schulnummer = schule.SchulNr.toString();

		// Hole das Blockungsergebnis über die ID aus der DB.
		final DTOGostBlockungZwischenergebnis dtoErgebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, blockungsergebnisID);
		if (dtoErgebnis == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		// Im Ergebnis ist auch die ID der Blockung enthalten. Blockungsdatenmanager auf Basis der BlockungsID des Ergebnisses erstellen.
		final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(dtoErgebnis.Blockung_ID);
		// Bestimme die Daten des Blockungsergebnisses und erstelle damit den Ergebnismanager
		final GostBlockungsergebnis blockungsergebnis = (new DataGostBlockungsergebnisse(conn)).getErgebnis(dtoErgebnis, datenManager);
		final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);

		// Variable für den späteren Dateinamen, abhängig davon, ob für ein Blockungsergebnis oder für einen Schüler daraus die PDF-Datei erstellt wird.
		String dateiname;

		// Um die Übersicht leserlicher zu halten, zeige Externe und Dummy nur an, wenn diese auch im Ergebnis vorkommen.
		final boolean zeigeExterneDummy = (ergebnisManager.getAnzahlSchuelerExterne() > 0 || ergebnisManager.getAnzahlSchuelerDummy() > 0);

		if (schuelerIDs == null || schuelerIDs.isEmpty()) {
			// Allgemeine Kurse-Schienen-Zuordnung eines Blockungsergebnisses
			dateiname = "Kurse-Schienen-Zuordnung_%d-%s_(Erg-ID%d).pdf".formatted(datenManager.daten().abijahrgang, GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"), blockungsergebnisID);
			return new PDFGostKurseSchienenZuordnung(dateiname, schulnummer, datenManager, ergebnisManager, zeigeExterneDummy, null, null);
		}

		// Schüler in der Blockung ermitteln
		final List<Schueler> schuelerListe = datenManager.schuelerGetListe();

		// Zu Beginn Schüler-IDs auf Validität prüfen, d. h. ob diese Schüler zum Blockungsergebnis gehören
		for (final Long schuelerID : schuelerIDs) {
			if (schuelerListe.stream().noneMatch(s -> (s.id == schuelerID)))
				throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht Teil der Blockungsdaten sind.");
		}

		// Die Schüler-IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe mehrerer Kurse-Schienen-Zuordnungen sollten
		// sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schüler-IDs, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		final Map<Long, Schueler> mapSchueler = schuelerListe.stream().filter(s -> schuelerIDs.contains(s.id)).collect(Collectors.toMap(s -> s.id, s -> s));

		final List<Long> sortedSchuelerIDs = mapSchueler.values().stream()
			.sorted(Comparator.comparing((final Schueler s) -> s.nachname, colGerman)
				.thenComparing((final Schueler s) -> s.vorname, colGerman)
				.thenComparing((final Schueler s) -> s.id))
			.map(s -> s.id)
			.toList();

		if (sortedSchuelerIDs.size() == 1) {
			final Long schuelerID = sortedSchuelerIDs.get(0);
			dateiname = "Kurse-Schienen-Zuordnung_%s-%s-%s_(Erg-ID%d)-(S-ID%d).pdf"
						.formatted(datenManager.schuelerGet(schuelerID).nachname.replace(" ", "_").replace(".", "_"),
								   datenManager.schuelerGet(schuelerID).vorname.replace(" ", "_").replace(".", "_"),
								   GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"),
								   blockungsergebnisID,
								   schuelerID);
		} else {
			dateiname = "Kurse-Schienen-Zuordnung_SuS-%d-%s_(Erg-ID%d).pdf"
						.formatted(datenManager.daten().abijahrgang,
								   GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"),
							 	   blockungsergebnisID);
		}

		// Erstelle die PDF-Datei, die entweder die Kurse-Schienen-Zuordnung eines Schülers beinhaltet oder die aller Schüler eines Abiturjahrgangs.
		int lfdNr = 0;
		PDFGostKurseSchienenZuordnung pdf = null;
		PDFGostKurseSchienenZuordnung pdfAktuelleSeite = null;
		for (final Long sID : sortedSchuelerIDs) {
			lfdNr++;
			final PDFGostKurseSchienenZuordnung pdfNeueSeite = new PDFGostKurseSchienenZuordnung(dateiname, schulnummer, datenManager, ergebnisManager, zeigeExterneDummy, sID, lfdNr);
			if (pdfAktuelleSeite == null) {
				pdf = pdfNeueSeite;
			} else {
				pdfAktuelleSeite.setNext(pdfNeueSeite);
			}
			pdfAktuelleSeite = pdfNeueSeite;
		}

		if (pdf != null)
			return pdf;
		else
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler bei der Generierung der PDF-Datei.");
	}


	/**
	 * Erstellt das PDF-Dokument für die Kurse-Schienen-Zuordnung
	 * zum gewählten Ergebnis der gewählten Blockung.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, dessen Kurse-Schienen-Zuordnung ausgegeben werden soll.
	 * @param schuelerIDs           Liste der IDs der SuS, deren Kurse-Schienen-Zuordnung erstellt werden soll. Werden keine IDs übergeben, so wird die Matrix allgemein für das Blockungsergebnis erstellt.
	 *
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) {

		try {
			final PDFGostKurseSchienenZuordnung pdf = getPDFmitKurseSchienenZuordnung(conn, blockungsergebnisID, (schuelerIDs == null || schuelerIDs.isEmpty()) ? null : schuelerIDs);

			final byte[] data = pdf.toByteArray();
			if (data == null)
				return OperationError.INTERNAL_SERVER_ERROR.getResponse("Fehler bei der Generierung der PDF-Datei.");

			final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(pdf.filename, StandardCharsets.UTF_8);

			return Response.ok(data, "application/pdf")
						   .header("Content-Disposition", "attachment; " + encodedFilename)
						   .build();

		} catch (WebApplicationException ex) {
			return ex.getResponse();
		}
	}

}
