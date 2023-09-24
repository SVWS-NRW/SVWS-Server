package de.svws_nrw.module.pdf.gost;

import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung eines Wahlbogens
 * für die Laufbahnplanung der gymnasialen Oberstufe.
 */
public final class PDFGostKursSchienenZuordnung extends PDFCreator {

	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostKursSchienenZuordnung.html.txt");
	private static final String css = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostKursSchienenZuordnung.css.txt");

	/** Der Dateiname für die PDF-Datei */
	private final String filename;


	/**
	 * Erstellt die Kurs-Schienen-Matrix für eine Jahrgangsstufe oder für einen Schüler der Stufe auf Basis der HTML-Vorlage
	 *
	 * @param dateiname        	Dateiname der finalen PDF-Datei. Ist dieser leer, so wird aus den übergebenen Daten des Schülers ein eindeutiger Dateiname erzeugt.
	 * @param schulnummer      	Schulnummer der Schule, deren Blockungsergebnis verwendet wird.
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurs-Schienen-Zuordnung ausgegeben werden soll.
	 * @param schuelerID 		ID des Schülers, dessen Kurs-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 * @param lfdNr 			Laufende Nummer der Kurs-Schienen-Zuordnung bei der Ausgabe für mehrere SuS.
	 */
	private PDFGostKursSchienenZuordnung(final String dateiname,
										 final String schulnummer,
										 final GostBlockungsdatenManager datenManager,
										 final GostBlockungsergebnisManager ergebnisManager,
										 final Long schuelerID,
										 final Integer lfdNr) {

		super("Kurs-Schienen-Zuordnung", html, css);

		this.querformat = true;
		this.filename = dateiname;

		// Definiert, ob die PDF-Datei für einen Schüler (oder einen Abiturjahrgang) erstellt wird.
		final boolean schuelerPDF = (schuelerID != null);

		// Ersetze die Felder des Templates mit Daten
		if (schuelerPDF) {
			bodyData.put("INHALT", "%s, %s".formatted(datenManager.schuelerGet(schuelerID).nachname, datenManager.schuelerGet(schuelerID).vorname));
			bodyData.put("SCHUELERSTATISTIK", "");
		} else {
			bodyData.put("INHALT", "Abitur %d".formatted(datenManager.daten().abijahrgang));
			bodyData.put("SCHUELERSTATISTIK", "(Gesamt: %d SuS, davon %d Externe)".formatted(datenManager.schuelerGetAnzahl(), -1));
			// TODO: Bug in schuelerGetAnzahl nach Beseitigung prüfen
			// TODO: Neue Methode notwendig, um alle Externen zu erhalten.
		}

		bodyData.put("AKTUELLESHALBJAHR", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
		bodyData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		bodyData.put("SCHULNUMMER", schulnummer);
		if (lfdNr == null)
			// TODO: Ergebnismanager braucht Methode um die ErgebnisID abzurufen.
			bodyData.put("INFORMATIONEN", "Blockungsergebnis-ID: -1 - Angaben zu SuS in den Kursen: Gesamt (Schriftlich, Externe, Dummy)");
		else
			bodyData.put("INFORMATIONEN", "Blockungsergebnis-ID: -1 - Ausdruck lfd. Nr: %03d".formatted(lfdNr));

		// Grundwerte für die Kurs-Schienenmatrix ermitteln
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

		// Erste Zeile der Kurs-Schienen-Matrix für die Spaltenbreiten erzeugen. Die Spalte ist unsichtbar und dient nur der Formatierung.
		final StringBuilder zeileSpaltenbreiten = new StringBuilder();
		for (int i = 0; i <= anzahlSpaltenAnsichtOptimierung; i++) {
			zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreite));
		}
		zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreiteRest));
		bodyData.put("SPALTENDEFINITION", zeileSpaltenbreiten.toString());
		bodyData.put("SCHRIFTGROESSE", schriftgroesse);

		// Hier wird die eigentliche Kurs-Schienen-Matrix erstellt.
		bodyData.put("SCHIENENUNDKURSE", getSchienenUndKurseZeilen(datenManager, ergebnisManager, schuelerID, schienen, schuelerPDF));
	}


	/**
	 * Erstellt der HTML-COde für die Kurs-Schienen-Matrix.
	 *
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurs-Schienen-Zuordnung ausgegeben werden soll.
	 * @param schuelerID		ID des Schülers, dessen Kurs-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 * @param schienen			Schienen des Blockungsergebnisses
	 * @param schuelerPDF		Gibt an, ob das PDF für Schüler erstellt wird (true) oder für das Ergebnis insgesamt (false)
	 * @return					Der gesamte HTML-Code für die Zeilen der Kurs-Schienen-Matrix
	 */
	private static String getSchienenUndKurseZeilen(final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final Long schuelerID, final List<GostBlockungSchiene> schienen, final boolean schuelerPDF) {
		// Nun für jede Schiene eine Zeile ind er Matrix erzeugen.
		final StringBuilder zeileMatrix = new StringBuilder();
		for (final GostBlockungSchiene schiene : schienen) {
			if (!ergebnisManager.getSchieneE(schiene.id).kurse.isEmpty()) {
				zeileMatrix.append("<tr>");

				// Zeilenkopf für Schiene mit zugehörigen Informationen erstellen.
				zeileMatrix.append(("<td style=\"text-align: left;%s\">"
									+ "<b>%s</b>")
									.formatted(((ergebnisManager.getOfSchieneHatKollision(schiene.id) && !schuelerPDF) ? " background-color: rgb(255,0,0);" : ""),
												 schiene.bezeichnung));
				if (!schuelerPDF)
					zeileMatrix.append(("<p class=\"tinyfont\">"
										+ "<b>%d</b> K. mit "
										+ "<b>%d</b> S.<br/>"
										+ "(-1 Externe)</p></td>")
										.formatted(ergebnisManager.getSchieneE(schiene.id).kurse.size(),
												   ergebnisManager.getOfSchieneAnzahlSchueler(schiene.id)));
				else
					zeileMatrix.append("</td>");

				zeileMatrix.append(getKurseInZeile(datenManager, ergebnisManager, schuelerID, schiene, schuelerPDF));
				zeileMatrix.append("</tr>");
			}
		}
		return zeileMatrix.toString();
	}


	/**
	 * Erstellt den HTML-Code für die Kurseinträge einer Schiene.
	 *
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurs-Schienen-Zuordnung ausgegeben werden soll.
	 * @param schuelerID		ID des Schülers, dessen Kurs-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 * @param schiene			Schiene des Blockungsergebnisses, für die Kurseinträge erstellt werden sollen
	 * @param schuelerPDF		Gibt an, ob das PDF für Schüler erstellt wird (true) oder für das Ergebnis insgesamt (false)
	 * @return					Der HTML-Code für die Kurse einer Schiene in der Kurs-Schienen-Matrix
	 */
	private static String getKurseInZeile(final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final Long schuelerID, final GostBlockungSchiene schiene, final boolean schuelerPDF) {
		final StringBuilder zeileKurse = new StringBuilder();

		// Kurse mit deren Informationen in der aktuellen Zeile der Schiene ergänzen.
		for (final GostBlockungsergebnisKurs kurs : ergebnisManager.getSchieneE(schiene.id).kurse) {
			String fachFarbeClientRGB = "";
			final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(kurs.fachID).kuerzel);
			if (fach != null)
				fachFarbeClientRGB = " style=\"background-color: rgb(" + fach.getHMTLFarbeRGB().replace("rgba(", "").replace(")", "") + ");\"";

			String farbe = fachFarbeClientRGB;
			if (schuelerPDF && !kurs.schueler.contains(schuelerID))
				farbe = "";

			zeileKurse.append("<td%s><b>%s</b>" .formatted(farbe, datenManager.kursGetName(kurs.id)));

			if (schuelerPDF) {
				zeileKurse.append(("<p class=\"tinyfont\">"
								   + "%s<br/>"
								   + "%s</p></td>")
								   .formatted("".equals(farbe) ? "" : ergebnisManager.getOfSchuelerOfFachKursart(schuelerID, kurs.fachID).kuerzel,
											  datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty() ? "----" : datenManager.kursGetLehrkraefteSortiert(kurs.id).stream().map(l -> l.kuerzel).collect(Collectors.joining(","))));
				//TODO: Aktuell wird nur die allgemeine Kursart LK, GK usw. ausgegeben. Methode für die individuelle Kursrat LK1, GKS wird noch benötigt.
			} else {
				zeileKurse.append(("<p class=\"tinyfont\">"
								   + "%s<br/>"
								   + "<b>%d</b> (%d, %d, %d)</p></td>")
								   .formatted(datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty() ? "----" : datenManager.kursGetLehrkraefteSortiert(kurs.id).stream().map(l -> l.kuerzel).collect(Collectors.joining(",")),
											  ergebnisManager.getOfKursAnzahlSchueler(kurs.id),
											  ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kurs.id),
											  ergebnisManager.getOfKursAnzahlSchuelerExterne(kurs.id),
											  ergebnisManager.getOfKursAnzahlSchuelerDummy(kurs.id)));
			}
		}
		return zeileKurse.toString();
	}


	/**
	 * Erstellt ein neues Objekt des Typs PDFGostKursSchienenZuordnung für die Kurs-Schienen-Zuordnung
	 * des gewählten Blockungsergebnisses.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	die ID der GostBlockungsergebnis, auf denen der GostBlockungsergebnisManager basiert.
	 * @param schuelerIDs 			Liste der ID der Schüler, deren Kurs-Schienen-Zuordnung erstellt werden soll. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 *
	 * @return 						das Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFGostKursSchienenZuordnung getPDFmitKursSchienenZuordnung(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) throws WebApplicationException {

		// Schuldaten sammeln
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final String schulnummer = schule.SchulNr.toString();

		// Hole das Blockungsergebnis über die ID aus der DB.
		final DTOGostBlockungZwischenergebnis dtoErgebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, blockungsergebnisID);
		if (dtoErgebnis == null)
			throw OperationError.NOT_FOUND.exception();
		// Im Ergebnis ist auch die ID der Blockung enthalten. Blockungsdatenmanager auf Basis der BlockungsID des Ergebnisses erstellen.
		final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(dtoErgebnis.Blockung_ID);
		// Bestimme die Daten des Blockungsergebnisses und erstelle damit den Ergebnismanager
		final GostBlockungsergebnis blockungsergebnis = (new DataGostBlockungsergebnisse(conn)).getErgebnis(dtoErgebnis, datenManager);
		final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);

		// Variable für den späteren Dateinamen, abhängig davon, ob für ein Blockungsergebnis oder für einen Schüler daraus die PDF-Datei erstellt wird.
		String dateiname;

		if (schuelerIDs == null || schuelerIDs.isEmpty()) {
			// Allgemeine Kurs-Schienen-Zuordnung eines Blockungsergebnisses
			dateiname = "Kurs-Schienen-Zuordnung_%d-%s_(eID%d).pdf"
						.formatted(datenManager.daten().abijahrgang,
								   GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"),
								   blockungsergebnisID);
			return new PDFGostKursSchienenZuordnung(dateiname, schulnummer,	datenManager, ergebnisManager, null, null);
		}

		// Schüler in der Blockung ermitteln
		final List<Schueler> schuelerListe = datenManager.schuelerGetListe();

		// Zu Beginn Schüler-IDs auf Validität prüfen, d. h. ob diese Schüler zum Blockungsergebnis gehören
		for (final Long schuelerID : schuelerIDs) {
			if (schuelerListe.stream().noneMatch(s -> (s.id == schuelerID)))
				throw OperationError.NOT_FOUND.exception();
		}

		// Die Schüler-IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe mehrerer Kurs-Schienen-Zuordnungen sollten
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
			dateiname = "Kurs-Schienen-Zuordnung_%s-%s-%s_(eID%d)-(sID%d).pdf"
						.formatted(datenManager.schuelerGet(schuelerID).nachname.replace(" ", "_").replace(".", "_"),
								   datenManager.schuelerGet(schuelerID).vorname.replace(" ", "_").replace(".", "_"),
								   GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"),
								   blockungsergebnisID,
								   schuelerID);
		} else {
			dateiname = "Kurs-Schienen-Zuordnung_SuS-%d-%s_(eID%d).pdf"
						.formatted(datenManager.daten().abijahrgang,
								   GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"),
							 	   blockungsergebnisID);
		}

		// Erstelle die PDF-Datei, die entweder die Kurs-Schienen-Zuordnung eines Schülers beinhaltet oder die aller Schüler eines Abiturjahrgangs.
		int lfdNr = 0;
		PDFGostKursSchienenZuordnung pdf = null;
		PDFGostKursSchienenZuordnung pdfAktuelleSeite = null;
		for (final Long schuelerID : sortedSchuelerIDs) {
			lfdNr++;
			final PDFGostKursSchienenZuordnung pdfNeueSeite = new PDFGostKursSchienenZuordnung(dateiname, schulnummer,	datenManager, ergebnisManager, schuelerID, lfdNr);
			if (pdfAktuelleSeite == null) {
				pdf = pdfNeueSeite;
			} else {
				pdfAktuelleSeite.setNext(pdfNeueSeite);
			}
			pdfAktuelleSeite = pdfNeueSeite;
		}
		return pdf;
	}


	/**
	 * Erstellt das PDF-Dokument für die Kurs-Schienen-Zuordnung
	 * zum gewählten Ergebnis der gewählten Blockung.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, dessen Kurs-Schienen-Zuordnung ausgegeben werden soll.
	 * @param schuelerIDs           Liste der IDs der SuS, deren Kurs-Schienen-Zuordnung erstellt werden soll. Werden keine IDs übergeben, so wird die Matrix allgemein für das Blockungsergebnis erstellt.
	 *
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) {
		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		final PDFGostKursSchienenZuordnung pdf = getPDFmitKursSchienenZuordnung(conn, blockungsergebnisID, (schuelerIDs == null || schuelerIDs.isEmpty()) ? null : schuelerIDs);

		if (pdf == null)
			return OperationError.NOT_FOUND.getResponse();

		final byte[] data = pdf.toByteArray();
		if (data == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();

		return Response.status(Status.OK).type("application/pdf").header("Content-Disposition", "attachment; filename=\"" + pdf.filename + "\"")
			.entity(data).build();
	}

}
