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
 * Diese Klasse beinhaltet den Code zur Erstellung einer Liste, die nach Schülern deren belegte
 * Kurse in einem Blockungsergebnis ausgibt.
 */
public final class PDFGostSchuelerKurseListe extends PDFCreator {

	/** Der HTML-Code des body für die HTML-Vorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostSchuelerKurseListe.html.txt");

	/** Das CSS für den Header der HTML-Vorlage, aus der später die PDF-Datei erzeugt wird. */
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

		this.filename = dateiname;

		// Ersetze die Felder des Templates mit Daten
		bodyData.put("INHALT", "Abitur %d".formatted(datenManager.daten().abijahrgang));
		bodyData.put("SCHUELERSTATISTIK", "");
		bodyData.put("AKTUELLESHALBJAHR", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
		bodyData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		bodyData.put("SCHULNUMMER", schulnummer);
		bodyData.put("INFORMATIONEN", "%s (Erg-ID %d)".formatted(datenManager.daten().name, ergebnisManager.getErgebnis().id));

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
			zeilenSchuelerMitKursen.append("<tr style=\"height:0px;\">");
			zeilenSchuelerMitKursen.append("<td></td>");
			zeilenSchuelerMitKursen.append("</tr>");

			// 3. Zeile erstellen
			zeilenSchuelerMitKursen.append(getKurseZeile(datenManager, ergebnisManager, schuelerID));

			// 4. Zeile erstellen
			zeilenSchuelerMitKursen.append("<tr style=\"height:0.5em;\">");
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

			final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(kurs.fachID).kuerzel);

			if (fach != null)
				zeileKurse.append("<td style=\"border: 0.5px solid gray; background-color: %s;\"><b>%s</b>".formatted(fach.getHMTLFarbeRGB().replace("rgba", "rgb"), datenManager.kursGetName(kurs.id)));
			else
				zeileKurse.append("<td><b>%s</b>".formatted(datenManager.kursGetName(kurs.id)));

			// Kursbelegung: Schriftlichkeit und evtl. Abiturfach
			String eintragSchriMuendAbiFach = ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).istSchriftlich ? "s" : "m";
			if (ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).abiturfach != null)
				eintragSchriMuendAbiFach += " (Abifach " + ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).abiturfach + ")";

			// Lehrkräfte des Kurses als kommaspeparierte Liste darstellen
			final String eintragLehrkraefte = datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty() ? "----" : datenManager.kursGetLehrkraefteSortiert(kurs.id).stream().map(l -> l.kuerzel).collect(Collectors.joining(","));

			zeileKurse.append(("<p class=\"tinyfont\">"
							   + "%s-%s<br/>"
							   + "%s</p></td>")
							   .formatted(ergebnisManager.getOfSchuelerOfFachKursart(schuelerID, kurs.fachID).kuerzel, eintragSchriMuendAbiFach, eintragLehrkraefte));
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

		if (blockungsergebnisID == null)
			throw OperationError.NOT_FOUND.exception("Ungültige Blockungsergebnis-ID übergeben.");

		if (schuelerIDs == null || schuelerIDs.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Schüler-IDs übergeben.");

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
		final String dateiname = "Schüler-Kurse-Liste_%d-%s_(Erg-ID%d).pdf".formatted(datenManager.daten().abijahrgang, GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel.replace(".", "-"), blockungsergebnisID);

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

		try {
			final PDFGostSchuelerKurseListe pdf = getPDFmitSchuelerKurseListe(conn, blockungsergebnisID,  schuelerIDs);

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
