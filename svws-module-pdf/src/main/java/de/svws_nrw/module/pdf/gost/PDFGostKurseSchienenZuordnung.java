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

	/** Der HTML-Code des body für die HTML-Vorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostKurseSchienenZuordnung.html");

	/** Der Dateiname für die PDF-Datei */
	private final String filename;


	/**
	 * Erstellt die Kurse-Schienen-Matrix für eine Jahrgangsstufe oder für einen Schüler der Stufe auf Basis der HTML-Vorlage
	 *
	 * @param dateiname        	Dateiname der finalen PDF-Datei.
	 * @param schulnummer      	Schulnummer der Schule, deren Blockungsergebnis verwendet wird.
	 * @param schulbezeichnung 	Array mit den drei Einträgen der Schulbezeichnung
	 * @param datenManager		Manager für die Blockungsgrunddaten des Blockungsergebnisses
	 * @param ergebnisManager	Manager für die Daten des Blockungsergebnisses, dessen Kurse-Schienen-Zuordnung ausgegeben werden soll.
	 * @param zeigeExterneDummy Legt fest, ob die Anzahlen zu Externen und Dummy in der Übersicht angezeigt werden sollen.
	 * @param schuelerIDs 		IDs der Schüler, deren Kurse-Schienen-Zuordnung aufgelistet werden sollen. Ist der Eintrag NULL, so wird die Matrix für den Abiturjahrgang des Blockungsergebnisses erstellt.
	 */
	private PDFGostKurseSchienenZuordnung(final String dateiname,
										  final String schulnummer,
										  final String[] schulbezeichnung,
										  final GostBlockungsdatenManager datenManager,
										  final GostBlockungsergebnisManager ergebnisManager,
										  final boolean zeigeExterneDummy,
										  final List<Long> schuelerIDs) {
		super(html);

		this.filename = dateiname;

		// Definiert, ob die PDF-Datei für einen Schüler (oder einen Abiturjahrgang) erstellt wird.
		final boolean istSchuelerPDF = (schuelerIDs != null);

		// Ersetze die Felder des Templates mit Daten
		htmlData.put("SCHULNUMMER", schulnummer);
		htmlData.put("SCHULBEZEICHNUNG_1", schulbezeichnung[0] == null ? "" : schulbezeichnung[0]);
		htmlData.put("SCHULBEZEICHNUNG_2", schulbezeichnung[1] == null ? "" : schulbezeichnung[1]);
		htmlData.put("SCHULBEZEICHNUNG_3", schulbezeichnung[2] == null ? "" : schulbezeichnung[2]);
		htmlData.put("ABITURJAHR", String.valueOf(datenManager.daten().abijahrgang));
		htmlData.put("AKTUELLESHALBJAHR", GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
		htmlData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

		if (istSchuelerPDF) {
			htmlData.put("SCHUELERSTATISTIK", "");
			htmlData.put("INFORMATIONEN", "%s (Erg-ID %d)".formatted(datenManager.daten().name, ergebnisManager.getErgebnis().id));
		} else {
			if (zeigeExterneDummy) {
				htmlData.put("SCHUELERSTATISTIK", " - Gesamt: %d SuS, davon %d/%d Externe/Platzhalter".formatted(datenManager.schuelerGetAnzahl(), ergebnisManager.getAnzahlSchuelerExterne(), ergebnisManager.getAnzahlSchuelerDummy()));
				htmlData.put("INFORMATIONEN", "%s (Erg-ID %d) - Angaben zu SuS in den Kursen: Gesamt (Schriftlich, Abiturfach) (Externe, Platzhalter)".formatted(datenManager.daten().name, ergebnisManager.getErgebnis().id));
			} else {
				htmlData.put("SCHUELERSTATISTIK", " - Gesamt: %d SuS".formatted(datenManager.schuelerGetAnzahl()));
				htmlData.put("INFORMATIONEN", "%s (Erg-ID %d) - Angaben zu SuS in den Kursen: Gesamt (Schriftlich, Abiturfach)".formatted(datenManager.daten().name, ergebnisManager.getErgebnis().id));
			}
		}

		// Hier wird die eigentliche Kurse-Schienen-Matrix erstellt.
		htmlData.put("SCHIENENKURSETABELLEN", getSchienenKurseTabellen(datenManager, ergebnisManager, zeigeExterneDummy, schuelerIDs, istSchuelerPDF));
	}

	private static String getSchienenKurseTabellen(final GostBlockungsdatenManager datenManager, final GostBlockungsergebnisManager ergebnisManager, final boolean zeigeExterneDummy, final List<Long> schuelerIDs, final boolean istSchuelerPDF) {

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
			schriftgroesse = "8pt";
		} else if (maxKurseInSchienen <= 12  && anzahlSchienen <= 16) {
			anzahlSpaltenAnsichtOptimierung = 12;
			spaltenbreite = "7.5%";
			spaltenbreiteRest = "2.5%";
			schriftgroesse = "7.5pt";
		} else {
			anzahlSpaltenAnsichtOptimierung = 16;
			spaltenbreite = "5.8%";
			spaltenbreiteRest = "1.4%";
			schriftgroesse = "7pt";
		}

		// Erste Zeile der Kurse-Schienen-Matrix für die Spaltenbreiten erzeugen. Die Spalte ist unsichtbar und dient nur der Formatierung.
		final StringBuilder zeileSpaltenbreiten = new StringBuilder();
		for (int i = 0; i <= anzahlSpaltenAnsichtOptimierung; i++) {
			zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreite));
		}
		zeileSpaltenbreiten.append("<td style=\"width: %s; border-width: 0em;\"></td>".formatted(spaltenbreiteRest));

		// Tabellen für die Schüler mit ihren Kursen erzeugen.
		final StringBuilder tabellenSchienenMitKursen = new StringBuilder();

		if (istSchuelerPDF) {
			int lfdNr = 0;
			final int maxSchueler = schuelerIDs.size();

			// Erzeuge für jeden Schüler eine Tabelle mit den Schienen und Kursen
			for (final Long schuelerID : schuelerIDs) {
				lfdNr++;
				tabellenSchienenMitKursen.append(
					"""
                    <table style="width: 100%%; table-layout:fixed; border-collapse:collapse; font-size: %s;%s">
                        <tbody>
                            <tr style="height: 0em" cellspacing="0" cellpadding="0">
                                %s
                            </tr>
                            <tr>
                                <td colspan="%d" style="text-align: left;"><h2>Kurse-Schienen-Zuordnung für %s, %s</h2></td>
                            </tr>
                            <tr style="height:0px;">
                                <td></td>
                            </tr>
                            %s
                        </tbody>
                    </table>
                    """.formatted(
						(lfdNr < maxSchueler) ? " page-break-after: always;" : "",  // <<< hier wird für alle Schüler, bis auf den letzten, ein Seitenumbruch eingefügt.
						schriftgroesse,
						zeileSpaltenbreiten,
						anzahlSpaltenAnsichtOptimierung,
						datenManager.schuelerGet(schuelerID).nachname,
						datenManager.schuelerGet(schuelerID).vorname,
						getSchienenUndKurseZeilen(datenManager, ergebnisManager, zeigeExterneDummy, schuelerID, schienen, true)
				));
			}
		} else {
			// Erzeuge nur eine Tabelle mit den Schienen und Kursen für den ganzen Abiturjahrgang.
			tabellenSchienenMitKursen.append(
    		"""
            <table style="width: 100%%; table-layout:fixed; border-collapse:collapse; font-size: %s;">
                <tbody>
                    <tr style="height: 0em" cellspacing="0" cellpadding="0">
                        %s
                    </tr>
                    <tr>
                        <td colspan="%d" style="text-align: left;"><h2>Kurse-Schienen-Zuordnung Abitur %s - %s</h2></td>
                    </tr>
                    <tr style="height:0px;">
                        <td></td>
                    </tr>
                    %s
                </tbody>
            </table>
            """.formatted(
				schriftgroesse,
				zeileSpaltenbreiten,
				anzahlSpaltenAnsichtOptimierung,
				String.valueOf(datenManager.daten().abijahrgang),
				GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel,
				getSchienenUndKurseZeilen(datenManager, ergebnisManager, zeigeExterneDummy, null, schienen, false)
			));
		}

		return tabellenSchienenMitKursen.toString();
	}


	/**
	 * Erstellt der HTML-Code für die Kurse-Schienen-Matrix.
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

		final StringBuilder zeileMatrix = new StringBuilder();

		// Nun für jede Schiene eine Zeile in der Matrix erzeugen.
		for (final GostBlockungSchiene schiene : schienen) {
			if (!ergebnisManager.getSchieneE(schiene.id).kurse.isEmpty()) {

				if (istSchuelerPDF) {
					// Zeilenkopf für Schiene mit zugehörigen Informationen erstellen.
					zeileMatrix.append(
                        """
                        <tr style="page-break-inside: avoid;">
                            <td class="bGray" style="text-align: left;">
                                <b>%s</b>
                            </td>
                        """.formatted(
							schiene.bezeichnung
						));
				} else {
					final String eintragKollision = (ergebnisManager.getOfSchieneHatKollision(schiene.id)) ? " background-color: rgb(255,0,0);" : "";
					final String eintragSchienendetails = zeigeExterneDummy ? "<br/>(%d/%d Ext./P.h.)".formatted(ergebnisManager.getOfSchieneAnzahlSchuelerExterne(schiene.id), ergebnisManager.getOfSchieneAnzahlSchuelerDummy(schiene.id)) : "";

					zeileMatrix.append(
					"""
                    <tr style="page-break-inside: avoid;">
                        <td class="bGray" style="text-align: left;%s">
                            <b>%s</b>
                            <p class="tiny"><b>%d</b> K. mit <b>%d</b> S.%s</p>
                        </td>
                    """.formatted(
						eintragKollision,
						schiene.bezeichnung,
						ergebnisManager.getSchieneE(schiene.id).kurse.size(),
						ergebnisManager.getOfSchieneAnzahlSchueler(schiene.id),
						eintragSchienendetails
					));
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

			// Fach für Hintergrundfarbe ermitteln
			final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(kurs.fachID).kuerzel);
			String eintragFarbe = (fach != null) ? " background-color: " + fach.getHMTLFarbeRGB().replace("rgba", "rgb") + ";" : "";

			// Lehrkräfte des Kurses als kommaspeparierte Liste darstellen
			final String eintragLehrkraefte = datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty() ? "----" : datenManager.kursGetLehrkraefteSortiert(kurs.id).stream().map(l -> l.kuerzel).collect(Collectors.joining(","));

			// Ab hier werden der Kurs und seine Informationen ergänzt, abhängig davon, ob es eine schülerbezogene Ausgabe ist oder für die ganze Stufe.
			if (istSchuelerPDF) {
				// Ausgabe für einen Schüler
				// Prüfe, ob der Kurs vom Schüler belegt ist. wenn ja, ermittle die Schriftlichkeit und evtl. welches Abiturfach es ist.
				final boolean istSchuelerKursbelegung = (kurs.schueler.contains(schuelerID));
				final String eintragKursart = istSchuelerKursbelegung ? ergebnisManager.getOfSchuelerOfFachKursart(schuelerID, kurs.fachID).kuerzel : "";
				String eintragSchriMuend = "";
				String eintragAbifach = "";
				if (istSchuelerKursbelegung) {
					eintragSchriMuend = ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).istSchriftlich ? "-s" : "-m";
					eintragAbifach = (ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).abiturfach != null) ? " (Abifach " + ergebnisManager.getOfSchuelerOfKursFachwahl(schuelerID, kurs.id).abiturfach + ")" : "";
				} else {
					eintragFarbe = " background-color: rgb(255,255,255);";
				}

				zeileKurse.append(
					"""
                    <td class="bGray" style="%s">
                        <b>%s</b>
                        <p class="tiny">%s%s%s<br/>%s</p>
                    </td>
                    """.formatted(
						eintragFarbe,
						datenManager.kursGetName(kurs.id),
						eintragKursart,
						eintragSchriMuend,
						eintragAbifach,
						eintragLehrkraefte));
			} else {
				// Ausgabe für eine Stufe
				final int anzahlAbitur = ergebnisManager.getOfKursAnzahlSchuelerAbiturLK(kurs.id) + ergebnisManager.getOfKursAnzahlSchuelerAbitur3(kurs.id) + ergebnisManager.getOfKursAnzahlSchuelerAbitur4(kurs.id);
				final String eintragDetailzahlen = zeigeExterneDummy ? "(%d,%d)".formatted(ergebnisManager.getOfKursAnzahlSchuelerExterne(kurs.id), ergebnisManager.getOfKursAnzahlSchuelerDummy(kurs.id)) : "";

				zeileKurse.append(
					"""
                    <td class="bGray" style="%s">
                        <b>%s</b>
                        <p class="tiny">%s<br/><b>%d</b> (%d,%d)%s</p>
                    </td>
                    """.formatted(
						eintragFarbe,
						datenManager.kursGetName(kurs.id),
						eintragLehrkraefte,
						ergebnisManager.getOfKursAnzahlSchueler(kurs.id),
						ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kurs.id),
						anzahlAbitur,
						eintragDetailzahlen));
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
		final String[] schulbezeichnung = new String[] {schule.Bezeichnung1, schule.Bezeichnung2, schule.Bezeichnung3};

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
			return new PDFGostKurseSchienenZuordnung(dateiname, schulnummer, schulbezeichnung, datenManager, ergebnisManager, zeigeExterneDummy, null);
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

		return new PDFGostKurseSchienenZuordnung(dateiname, schulnummer, schulbezeichnung, datenManager, ergebnisManager, zeigeExterneDummy, sortedSchuelerIDs);
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
