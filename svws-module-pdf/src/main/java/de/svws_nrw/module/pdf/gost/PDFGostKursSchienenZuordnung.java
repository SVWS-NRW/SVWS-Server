package de.svws_nrw.module.pdf.gost;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


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
	 * Erstellt den PDF-Wahlbogen auf Basis der HTML-Vorlage
	 *
	 * @param dateiname        	Dateiname der finalen PDF-Datei. Ist dieser leer, so wird aus den übergebenen Daten des Schülers ein eindeutiger Dateiname erzeugt.
	 * @param schulnummer      	Schulnummer der Schule des Schülers, dessen Wahlbogen erstellt werden soll
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurs-Schienen-Zuordnung ausgegeben werden soll.
	 */
	private PDFGostKursSchienenZuordnung(final String dateiname,
										 final String schulnummer,
										 final GostBlockungsdatenManager datenManager,
										 final GostBlockungsergebnisManager ergebnisManager) {

		super("Kurs-Schienen-Zuordnung", html, css);

		this.querformat = true;
		this.filename = dateiname;

		// Ersetze die Felder des Templates mit Daten
		bodyData.put("ABITURJAHR", String.valueOf(datenManager.daten().abijahrgang));
		bodyData.put("AKTUELLESHALBJAHR", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
		// TODO: Bug in schuelerGetAnzahl nach Beseitigung prüfen
		bodyData.put("SUSGESAMT", String.valueOf(datenManager.schuelerGetAnzahl()));
		// TODO: Neue Methode notwendig, um alle Externen zu erhalten.
		bodyData.put("SUSEXTERN", String.valueOf(-1));
		bodyData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		bodyData.put("SCHULNUMMER", schulnummer);

		// Grundwerte für die Kurs-Schienenmatrix ermitteln
		final List<GostBlockungSchiene> schienen = datenManager.schieneGetListe();
		final int anzahlSchiene = schienen.size();
		final int maxKurseInSchienen = ergebnisManager.getOfSchieneMaxKursanzahl();

		// Die Spalten der Matrix werden gemäß maximale Kurszahl fix gewählt und der evtl. Rest als Spalte zum Füllen angehängt.
		// Zudem wird die Schriftgröße der Matrix angepasst, wiederum abhängig von der maximalen Kurszahl in einer Schiene.
		int anzahlSpaltenAnsichtOptimierung;
		String spaltenbreite;
		String spaltenbreiteRest;
		String schriftgroesse;

		if (maxKurseInSchienen <= 8 && anzahlSchiene <= 1) {
			anzahlSpaltenAnsichtOptimierung = 8;
			spaltenbreite = "11%";
			spaltenbreiteRest = "1%";
			schriftgroesse = "11px";
		} else if (maxKurseInSchienen <= 12  && anzahlSchiene <= 15) {
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
		StringBuilder zeileSpaltenbreiten = new StringBuilder();
		for (int i = 0; i <= anzahlSpaltenAnsichtOptimierung; i++) {
			zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreite));
		}
		zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreiteRest));
		bodyData.put("SPALTENDEFINITION", zeileSpaltenbreiten.toString());
		bodyData.put("SCHRIFTGROESSE", schriftgroesse);

		// Nun für jede Schiene eine Zeile ind er Matrix erzeugen.
		StringBuilder zeileMatrix = new StringBuilder();
		for (GostBlockungSchiene schiene : schienen) {
			zeileMatrix.append("<tr>");

			// Zeilenkopf für Schiene mit zugehörigen Informationen erstellen.
			zeileMatrix.append(("<td style=\"text-align: left;%s\">"
								+ "<b>%s</b><p class=\"tinyfont\">"
								+ "<b>%d</b> K. mit "
								+ "<b>%d</b> SuS<br/>"
								+ "davon -1 Externe</p></td>")
								.formatted((ergebnisManager.getOfSchieneHatKollision(schiene.id) ? " background-color: rgb(255,0,0);" : ""),
										   schiene.bezeichnung,
										   ergebnisManager.getSchieneE(schiene.id).kurse.size(),
										   ergebnisManager.getOfSchieneAnzahlSchueler(schiene.id)));

			// Kurse mit deren Informationen in er aktuellen Zeile der Schiene ergänzen.
			for (GostBlockungsergebnisKurs kurs : ergebnisManager.getSchieneE(schiene.id).kurse) {
				String fachFarbeClientRGB = "";
				final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(kurs.fachID).kuerzel);
				if (fach != null)
					fachFarbeClientRGB = " style=\"background-color: rgb(" + fach.getHMTLFarbeRGB().replace("rgba(", "").replace(")", "") + ");\"";

				zeileMatrix.append(("<td%s><b>%s</b>"
									+ "<p class=\"tinyfont\">"
									+ "%s<br/>"
									+ "SuS: %d (%d, %d, %d)</p></td>")
									.formatted(fachFarbeClientRGB,
											   datenManager.kursGetName(kurs.id),
											   datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty() ? "----" : datenManager.kursGetLehrkraefteSortiert(kurs.id).stream().map(l -> l.kuerzel).collect(Collectors.joining(",")),
										       ergebnisManager.getOfKursAnzahlSchueler(kurs.id),
										       ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kurs.id),
											   ergebnisManager.getOfKursAnzahlSchuelerExterne(kurs.id),
											   ergebnisManager.getOfKursAnzahlSchuelerDummy(kurs.id)));
			}

			zeileMatrix.append("</tr>");
		}
		bodyData.put("SCHIENENUNDKURSE", zeileMatrix.toString());
	}


	/**
	 * Erstellt ein neues Objekt des Typs PDFGostKursSchienenZuordnung für die Kurs-Schienen-Zuordnung
	 * des gewählten Blockungsergebnisses.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	die ID der GostBlockungsergebnis, auf denen der GostBlockungsergebnisManager basiert.
	 *
	 * @return 						das Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFGostKursSchienenZuordnung getPDFmitKursSchienenZuordnung(final DBEntityManager conn, final Long blockungsergebnisID) throws WebApplicationException {

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

		// Dateinamen erzeugen und übergeben.
		String dateiname = "Kurs-Schienen-Zuordnung_EID-%d.pdf".formatted(blockungsergebnisID);

		// Erstelle die PDF-Datei, die die Kurs-Schienen-Zuordnung des Blockungsergebnisses enthält. Daten sind in den Managern abrufbar.
		return new PDFGostKursSchienenZuordnung(dateiname,
												schulnummer,
												datenManager,
												ergebnisManager);
	}


	/**
	 * Erstellt das PDF-Dokument für die Kurs-Schienen-Zuordnung
	 * zum gewählten Ergebnis der gewählten Blockung.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	die ID der GostBlockungsergebnis, auf denen der GostBlockungsergebnisManager basiert.
	 *
	 * @return 						die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final Long blockungsergebnisID) {
		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception();

		final PDFGostKursSchienenZuordnung pdf = getPDFmitKursSchienenZuordnung(conn, blockungsergebnisID);

		if (pdf == null)
			return OperationError.NOT_FOUND.getResponse();

		final byte[] data = pdf.toByteArray();
		if (data == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();

		return Response.status(Status.OK)
			.type("application/pdf")
			.header("Content-Disposition", "attachment; filename=\"" + pdf.filename + "\"")
			.entity(data)
			.build();
	}
}
