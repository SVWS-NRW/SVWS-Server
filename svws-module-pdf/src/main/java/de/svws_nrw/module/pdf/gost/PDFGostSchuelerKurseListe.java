package de.svws_nrw.module.pdf.gost;

import de.svws_nrw.base.ResourceUtils;
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

import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer Liste, die nach Schülern deren belegte
 * Kurse in einem Blockungsergebnis ausgibt.
 */
public final class PDFGostSchuelerKurseListe extends PDFCreator {

	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostSchuelerKurseListe.html.txt");
	private static final String css = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostSchuelerKurseListe.css.txt");

	/** Der Dateiname für die PDF-Datei */
	private final String filename;


	/**
	 * Erstellt Liste der Schüler mit deren Kursen auf Basis der HTML-Vorlage
	 *
	 * @param dateiname        	Dateiname der finalen PDF-Datei.
	 * @param schulnummer      	Schulnummer der Schule, deren Blockungsergebnis verwendet wird.
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Schüler-Kurse-Liste ausgegeben werden soll.
	 * @param schuelerIDs 		IDs der Schüler, deren Kurse aufgelistet werden sollen.
	 * @param maxKurse			Maximale Anzahl an Kursen die einer der Schüler in der Liste hat
	 */
	private PDFGostSchuelerKurseListe(final String dateiname,
                                      final String schulnummer,
                                      final GostBlockungsdatenManager datenManager,
                                      final GostBlockungsergebnisManager ergebnisManager,
                                      final List<Long> schuelerIDs,
									  final int maxKurse) {

		super("Schüler-Kurse-Liste", html, css);

		this.querformat = true;
		this.filename = dateiname;

		// Ersetze die Felder des Templates mit Daten
		bodyData.put("INHALT", "Abitur %d".formatted(datenManager.daten().abijahrgang));
		bodyData.put("SCHUELERSTATISTIK", "");
		// TODO: Bug in schuelerGetAnzahl nach Beseitigung prüfen
		// TODO: Neue Methode notwendig, um alle Externen zu erhalten.
		bodyData.put("AKTUELLESHALBJAHR", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
		bodyData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		bodyData.put("SCHULNUMMER", schulnummer);
		bodyData.put("INFORMATIONEN", "Blockungsergebnis: %s (eID%d)".formatted(ergebnisManager.getErgebnis().name, ergebnisManager.getErgebnis().id));

		// Die Spalten der Matrix werden gemäß maximale Kurszahl fix gewählt und der evtl. Rest als Spalte zum Füllen angehängt.
		// Zudem wird die Schriftgröße der Matrix angepasst, wiederum abhängig von der maximalen Kurszahl in einer Schiene.
		int anzahlSpaltenAnsichtOptimierung;
		String spaltenbreite;
		String spaltenbreiteRest;
		String schriftgroesse;

		if (maxKurse <= 13) {
			anzahlSpaltenAnsichtOptimierung = 13;
			spaltenbreite = "7.5%";
			spaltenbreiteRest = "2.5%";
			schriftgroesse = "10px";
		} else {
			anzahlSpaltenAnsichtOptimierung = 15;
			spaltenbreite = "6.6%";
			spaltenbreiteRest = "1%";
			schriftgroesse = "9px";
		}

		// Erste Zeile der Kurs-Schienen-Matrix für die Spaltenbreiten erzeugen. Die Spalte ist unsichtbar und dient nur der Formatierung.
		final StringBuilder zeileSpaltenbreiten = new StringBuilder();
		for (int i = 0; i < anzahlSpaltenAnsichtOptimierung; i++) {
			zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreite));
		}
		zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreiteRest));
		bodyData.put("SPALTENDEFINITION", zeileSpaltenbreiten.toString());
		bodyData.put("SCHRIFTGROESSE", schriftgroesse);

		// Hier wird die eigentliche Kurs-Schienen-Matrix erstellt.
		bodyData.put("NAMENUNDKURSE", getNamenUndKurseZeilen(datenManager, ergebnisManager, schuelerIDs));
	}


	/**
	 * Erstellt der HTML-Code für die Kurs-Schienen-Matrix.
	 *
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Schüler-Kurse-Liste ausgegeben werden soll.
	 * @param schuelerIDs		ID der Schüler, deren Liste mit Kursen erstellt werden soll.
	 * @return					Der gesamte HTML-Code für die Zeilen der Kurs-Schienen-Matrix
	 */
	private static String getNamenUndKurseZeilen(final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final List<Long> schuelerIDs) {
		// Nun für jeden Schüler eine Liste mit seinen Kursen erzeugen. Dazu werden zwei Tabellenzeilen verwendet
		// 1. Zeile: Informationen zum Schüler
		// 2. Zeile: Spacing und Borders sicherstellen
		// 3. Zeile: seine Kurse
		// 4. Zeile: Spacing und Borders sicherstellen
		final StringBuilder zeilenSchuelerMitKursen = new StringBuilder();

		for (final Long schuelerID : schuelerIDs) {
			// 1. Zeile erstellen
			zeilenSchuelerMitKursen.append("<tr>");
			zeilenSchuelerMitKursen.append(("<td colspan=\"%d\" style=\"text-align: left;\"><b>%s, %s</b></td>").formatted(13, datenManager.schuelerGet(schuelerID).nachname, datenManager.schuelerGet(schuelerID).vorname));
			zeilenSchuelerMitKursen.append("</tr>");

			// 2. Zeile erstellen
			zeilenSchuelerMitKursen.append("<tr style=\"height:0px; border-style:hidden;\">");
			zeilenSchuelerMitKursen.append("<td></td>");
			zeilenSchuelerMitKursen.append("</tr>");

			// 3. Zeile erstellen
			zeilenSchuelerMitKursen.append(getKurseZeile(datenManager, ergebnisManager, schuelerID));

			// 4. Zeile erstellen
			zeilenSchuelerMitKursen.append("<tr style=\"height:0.5em; border-style:hidden;\">");
			zeilenSchuelerMitKursen.append("<td></td>");
			zeilenSchuelerMitKursen.append("</tr>");
		}
		return zeilenSchuelerMitKursen.toString();
	}


	/**
	 * Erstellt den HTML-Code für die Kurseinträge einer Schiene.
	 *
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Schüler-Kurse-Liste ausgegeben werden soll.
	 * @param schuelerID		ID des Schülers, dessen Kurse-Zeile erstellt werden soll.
	 * @return					Der HTML-Code für die Kurse eines Schülers in der Kurs-Schienen-Matrix
	 */
	private static String getKurseZeile(final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final Long schuelerID) {
		final StringBuilder zeileKurse = new StringBuilder();

		// Kurse mit deren Informationen in eigener Zeile ergänzen.
		zeileKurse.append("<tr>");
		for (final GostBlockungsergebnisKurs kurs : ergebnisManager.getOfSchuelerKursmenge(schuelerID)) {
			String fachFarbeClientRGB = "";
			final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(kurs.fachID).kuerzel);
			if (fach != null)
				fachFarbeClientRGB = " style=\"border: 0.5px solid gray; background-color: rgb(" + fach.getHMTLFarbeRGB().replace("rgba(", "").replace(")", "") + ");\"";

			zeileKurse.append("<td%s><b>%s</b>" .formatted(fachFarbeClientRGB, datenManager.kursGetName(kurs.id)));

			zeileKurse.append(("<p class=\"tinyfont\">"
							   + "%s<br/>"
							   + "%s</p></td>")
							   .formatted(ergebnisManager.getOfSchuelerOfFachKursart(schuelerID, kurs.fachID).kuerzel,
										  datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty() ? "----" : datenManager.kursGetLehrkraefteSortiert(kurs.id).stream().map(l -> l.kuerzel).collect(Collectors.joining(","))));
			//TODO: Aktuell wird nur die allgemeine Kursart LK, GK usw. ausgegeben. Methode für die individuelle Kursrat LK1, GKS wird noch benötigt.
		}
		zeileKurse.append("</tr>");
		return zeileKurse.toString();
	}


	/**
	 * Erstellt ein neues Objekt des Typs PDFGostSchuelerKurseListe für eine Liste an Schülern mit deren belegten Kursen
	 * des gewählten Blockungsergebnisses.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem die Liste der Schüler mit ihren Kursen erstellt werden soll.
	 * @param schuelerIDs           Liste der IDs der SuS, deren Kurse aufgelistet werden sollen.
	 *
	 * @return 						das Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFGostSchuelerKurseListe getPDFmitSchuelerKurseListe(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) throws WebApplicationException {

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

		// Zu Beginn Schüler-IDs auf Validität prüfen, d. h. ob diese Schüler zum Blockungsergebnis gehören.
		// Bestimme dabei gleichzeitig dessen maximale Anzahl an Kursen.
		int maxKurse = 0;
		int maxKurseSchueler;
		final List<Schueler> schuelerListe = datenManager.schuelerGetListe();
		for (final Long schuelerID : schuelerIDs) {
			if (schuelerListe.stream().noneMatch(s -> (s.id == schuelerID)))
				throw OperationError.NOT_FOUND.exception();
			else {
				maxKurseSchueler = ergebnisManager.getOfSchuelerKursmenge(schuelerID).size();
				if (maxKurseSchueler > maxKurse)
					maxKurse = maxKurseSchueler;
			}
		}

		// Variable für den späteren Dateinamen, abhängig davon, ob für ein Blockungsergebnis oder für einen Schüler daraus die PDF-Datei erstellt wird.
		final String dateiname = "Schüler-Kurse-Liste_%d-%s_(eID%d).pdf"
			.formatted(datenManager.daten().abijahrgang,
				GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"),
				blockungsergebnisID);

		// Grundwert für die Liste ermitteln, d. h. die maximale Anzahl an Kursen, die ein Schüler belegt hat. Damit wird die Spaltenbreite später festgelegt.

		// Die Schüler-IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe der Schüler-Kurse-Liste sollten
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

		return new PDFGostSchuelerKurseListe(dateiname, schulnummer, datenManager, ergebnisManager, sortedSchuelerIDs, maxKurse);
	}


	/**
	 * Erstellt das PDF-Dokument für eine Liste der Schüler mit ihren belegten Kursen
	 * im gewählten Ergebnis der gewählten Blockung.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem die Liste der Schüler mit ihren Kursen erstellt werden soll.
	 * @param schuelerIDs           Liste der IDs der SuS, deren Kurse aufgelistet werden sollen.
	 *
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) {
		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		if (schuelerIDs == null || schuelerIDs.isEmpty())
			throw OperationError.NOT_FOUND.exception("Fehlende Schüler-IDs.");

		final PDFGostSchuelerKurseListe pdf = getPDFmitSchuelerKurseListe(conn, blockungsergebnisID,  schuelerIDs);

		if (pdf == null)
			return OperationError.NOT_FOUND.getResponse();

		final byte[] data = pdf.toByteArray();
		if (data == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();

		return Response.status(Status.OK).type("application/pdf").header("Content-Disposition", "attachment; filename=\"" + pdf.filename + "\"")
			.entity(data).build();
	}

}
