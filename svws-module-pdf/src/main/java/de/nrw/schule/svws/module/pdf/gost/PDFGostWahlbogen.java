package de.nrw.schule.svws.module.pdf.gost;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungErgebnisFehler;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehlerArt;
import de.nrw.schule.svws.core.data.Sprachbelegung;
import de.nrw.schule.svws.core.data.Sprachpruefung;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegungHalbjahr;
import de.nrw.schule.svws.core.data.gost.Abiturdaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.core.types.fach.ZulaessigesFach;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.module.pdf.PDFCreator;

/**
 * Diese Klasse beinhaltet den Code zur Erstellung eines Wahlbogens
 * für die Laufbahnplanung der gymnasialen Oberstufe. 
 */
public class PDFGostWahlbogen extends PDFCreator {
	
	private final static String html = 
		"""
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="width: 15%; text-align: left; vertical-align: top;">
					<p>@@PRUEFUNGSORDNUNG@@</p> 
				</td>
				<td style="width: 70%; text-align: center;"> 
					<h2>@@SCHULBEZEICHNUNG_1@@<br/>@@SCHULBEZEICHNUNG_2@@<br/>@@SCHULBEZEICHNUNG_3@@</h2>
				</td>
				<td style="width: 15%; text-align: right; vertical-align: top;">
					<p>@@KLASSE@@<br/> Abitur @@ABITURJAHR@@</p>
				</td>
			</tr>
		</table>
		<p><b>Wahlbogen für das Halbjahr @@PLANUNGSHALBJAHR@@ von @@SCHUELER_NAME@@</b></p>
		<p>@@BEMERKUNGEN@@</p>
		<p>Hiermit wähle ich verbindlich für das Halbjahr @@PLANUNGSHALBJAHR@@ die folgenden Fächer:</p>
		<table class="faecher" style="border-collapse:collapse; border:0.5px solid gray;" width="100%" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th style="width: 45%;">Fach</th>
					<th style="width: 13%;">Sprachenfolge</th>
					<th style="width: 6%;">EF.1</th>
					<th style="width: 6%;">EF.2</th>
					<th style="width: 6%;">Q1.1</th>
					<th style="width: 6%;">Q1.2</th>
					<th style="width: 6%;">Q2.1</th>
					<th style="width: 6%;">Q2.2</th>
					<th style="width: 6%;">Abitur</th>
				</tr>
			</thead>
			<tbody>
				@@ROWS@@
			</tbody>
			<tfoot>
				<tr>
					<td></td>
					<td style="text-align: right;">Anzahl Kurse: </td>
					<td>@@KURSE_EF1@@</td>
					<td>@@KURSE_EF2@@</td>
					<td>@@KURSE_Q11@@</td>
					<td>@@KURSE_Q12@@</td>
					<td>@@KURSE_Q21@@</td>
					<td>@@KURSE_Q22@@</td>
					<td>@@KURSE_Q@@ <sup>1)</sup></td>
				</tr>
				<tr>
					<td></td>
					<td style="text-align: right;">Wochenstunden: </td>
					<td>@@WSTD_EF1@@</td>
					<td>@@WSTD_EF2@@</td>
					<td>@@WSTD_Q11@@</td>
					<td>@@WSTD_Q12@@</td>
					<td>@@WSTD_Q21@@</td>
					<td>@@WSTD_Q22@@</td>
					<td>@@JSTD@@ <sup>2)</sup></td>
				</tr>
			</tfoot>
		</table>
		<p>Schulorganisatorische Gründe können zu einer Änderung der Fachwahl und der Laufbahn führen. Korrekturwünsche können
		vor jedem Halbjahreswechsel nach Rücksprache mit den Beratungslehrern durchgeführt werden.</p>		
		@@BELEGUNGSFEHLER@@
		@@BELEGUNGSHINWEISE@@
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="page-break-inside: avoid;">
			<tr style="height: 3em;">
				<td style="width: 45%;"> </td>
				<td style="width: 10%;"> </td>
				<td style="width: 45%;"> </td>
			</tr>
			<tr style="height: 1em;">
				<td style="text-align: center; border: 0.5px solid black; border-style: solid none none none;">
					Unterschrift Beratungslehrer/in
				</td>
				<td> </td>
				<td style="text-align: left;"> Beraten am: @@BERATUNGSDATUM@@ </td>
			</tr>
			<tr style="height: 3em;">
				<td> </td>
				<td> </td>
				<td> </td>
			</tr>
			<tr style="height: 1em;">
				<td style="text-align: center; border: 0.5px solid black; border-style: solid none none none;">
				  	@@UNTERSCHRIFT_SCHUELER@@
				</td>
				<td> </td>
				<td style="text-align: center; border: 0.5px solid black; border-style: solid none none none;">
					Unterschrift eines Erziehungsberechtigten
				</td>
			</tr>
		</table>
		<div class="footer">
		<p class="tinyfont" style="margin: 0px; width: 100%; border: 0.5px solid black; border-style: none none solid none;">@@ZEIT@@</p>
		<p style="margin: 0px"><span class="tinyfont" style="margin-right: 2em">1) Anzahl der anrechenbaren Kurse aus der Qualifikationsphase</span>
		<span class="tinyfont" style="margin-right: 2em">2) Summe der durchschnittlichen Jahresstunden</span></p>
		</div>
		""";
	
	private final static String css = 
		"""
		table.faecher thead {
			background-color: #E0E0E0;
			text-align: center;
		}
		table.faecher tbody {
			text-align: left;
		}
		table.faecher tfoot {
			background-color: #E0E0E0;
			text-align: center;
		}
		table.faecher th {
			border: 0.5px solid gray;
		}
		table.faecher td {
			text-align: center;
			border: 0.5px solid gray;
		}
		table.faecher tfoot td {
			border: 0px solid gray;
		}
		""";


	/** Die Laufbahndaten des Schülers */
	private Abiturdaten abidaten; 
	
	/** Die Fächer der gymnasialen Oberstufe für den Abiturjahrgang des Schülers */
	private GostFaecherManager gostFaecher;
	
	/** Der Abiturdaten-Manager */
	private AbiturdatenManager manager;


	/**
	 * Erstellt einen neuen PDF-Wahlbogen mit den übergebenen Daten. 
	 * 
	 * @param schuelerName        der Name des Schülers bestehend aus Vorname und Nachname getrennt durch ein Leerzeichen
	 * @param geschlecht          das Geschlecht des Schülers
	 * @param klasse              die Klasse des Schülers
	 * @param schulbezeichnung    die Bezeichnung der Schule bestehend aus drei Teilen
	 * @param abidaten            die Laufbahndaten des Schülers 
	 * @param gostFaecher         die Fächer der gymnasialen Oberstufe für den Abiturjahrgang des Schülers
	 * @param planungsHalbjahr    das Halbjahr der gymnasialen Oberstufe, auf welches sich die Planung bezieht
	 * @param bemerkungJahrgang   der Text, der bei diesem Schüler oben auf dem Beratungsbogen erscheinen soll.
	 * @param datumBeratung       das Datum der letzten Beratung des Schülers
	 */
	public PDFGostWahlbogen(String schuelerName, Geschlecht geschlecht, String klasse, String[] schulbezeichnung, Abiturdaten abidaten, 
			                GostFaecherManager gostFaecher, GostHalbjahr planungsHalbjahr, String bemerkungJahrgang,
			                String datumBeratung) {
		// Setze den Titel des Dokuments, das HTML-Template und die speziellen CSS-Definitionen für dieses Dokument 
		super("Wahlbogen für das Halbjahr " + planungsHalbjahr.kuerzel + " von " + schuelerName, html, css);
		this.abidaten = abidaten;
		this.gostFaecher = gostFaecher;
		this.manager = new AbiturdatenManager(this.abidaten, this.gostFaecher.toVector(), GostBelegpruefungsArt.GESAMT);		
		// Ersetze die Felder des Templates mit den Daten
		bodyData.put("PRUEFUNGSORDNUNG", "APO-GOSt");
		bodyData.put("SCHULBEZEICHNUNG_1", schulbezeichnung[0]);
		bodyData.put("SCHULBEZEICHNUNG_2", schulbezeichnung[1]);
		bodyData.put("SCHULBEZEICHNUNG_3", schulbezeichnung[2]);
		bodyData.put("SCHUELER_NAME", schuelerName);
		bodyData.put("KLASSE", klasse);
		bodyData.put("ABITURJAHR", "" + abidaten.abiturjahr);
		bodyData.put("PLANUNGSHALBJAHR", planungsHalbjahr.kuerzel);
		bodyData.put("BEMERKUNGEN", bemerkungJahrgang == null ? "" : bemerkungJahrgang);
		bodyData.put("ROWS", getRows());
		bodyData.put("BERATUNGSDATUM", datumBeratung == null ? "---" : datumBeratung);
		switch (geschlecht) {
			case M -> bodyData.put("UNTERSCHRIFT_SCHUELER", "Unterschrift des Schülers");
			case W -> bodyData.put("UNTERSCHRIFT_SCHUELER", "Unterschrift der Schülerin");
			case D -> bodyData.put("UNTERSCHRIFT_SCHUELER", "Unterschrift Schüler/in");
			case X -> bodyData.put("UNTERSCHRIFT_SCHUELER", "Unterschrift Schüler/in");
			default -> bodyData.put("UNTERSCHRIFT_SCHUELER", "Unterschrift Schüler/in");
		}
		getKurseUndWochenstunden();
		getErgebnisse();
		bodyData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
	}
	
	
	/**
	 * Bestimmt die Anzahl der belegten Kurse bzw. die Anzahl der Anrechenbaren Kurse aus der Qualifikationsphase und zusätzlich
	 * die Wochenstunden und die durchschnittlichen Jahresstunden.
	 */
	private void getKurseUndWochenstunden() {
		int[] kurse = manager.getAnrechenbareKurse();
		int[] wstd = manager.getWochenstunden();
		bodyData.put("KURSE_EF1", "" + kurse[0]);
		bodyData.put("KURSE_EF2", "" + kurse[1]);
		bodyData.put("KURSE_Q11", "" + kurse[2]);
		bodyData.put("KURSE_Q12", "" + kurse[3]);
		bodyData.put("KURSE_Q21", "" + kurse[4]);
		bodyData.put("KURSE_Q22", "" + kurse[5]);
		bodyData.put("KURSE_Q", "" + (kurse[2] + kurse[3] + kurse[4] + kurse[5]));
		bodyData.put("WSTD_EF1", "" + wstd[0]);
		bodyData.put("WSTD_EF2", "" + wstd[1]);
		bodyData.put("WSTD_Q11", "" + wstd[2]);
		bodyData.put("WSTD_Q12", "" + wstd[3]);
		bodyData.put("WSTD_Q21", "" + wstd[4]);
		bodyData.put("WSTD_Q22", "" + wstd[5]);
		bodyData.put("JSTD", "" + ((wstd[0] + wstd[1] + wstd[2] + wstd[3] + wstd[4] + wstd[5]) / 2.0));
	}
	
	/**
	 * Führt eine Laufbahnprüfung durch und gibt die Fehler- und die
	 * Informationen zur Laufbahn aus.
	 */
	private void getErgebnisse() {
		// Berechne das Ergebnis des Belegprüfung für die Abiturdaten
		GostBelegpruefungErgebnis ergebnis = manager.getBelegpruefungErgebnis();
		if (ergebnis.fehlercodes.size() <= 0) {
			bodyData.put("BELEGUNGSFEHLER", "");
			bodyData.put("BELEGUNGSHINWEISE", "");
			return;
		}
		// Unterscheide zwischen Fehler und Informationen
		Vector<String> fehler = new Vector<>();
		Vector<String> infos = new Vector<>();
		for (GostBelegpruefungErgebnisFehler f : ergebnis.fehlercodes) {
			GostBelegungsfehlerArt art = GostBelegungsfehlerArt.fromKuerzel(f.art);
			if (art == GostBelegungsfehlerArt.HINWEIS) {
				infos.add(f.beschreibung);
			} else {
				fehler.add(f.beschreibung);
			}
		}
		// Gibt die Fehler-Bemerkungen zur Laufbahn aus
		StringBuilder sb = new StringBuilder();
		if (fehler.size() > 0) {
			sb.append("<p>Bemerkungen der Schule:</p>");
			ul(sb, fehler);
		}
		bodyData.put("BELEGUNGSFEHLER", sb.toString());
		// Gib die Informationen zur Laufbahn aus
		sb = new StringBuilder();
		if (infos.size() > 0) {
			sb.append("<p>Sonstige Hinweise zur Gesamtlaufbahn:</p>");
			ul(sb, infos);
		}
		bodyData.put("BELEGUNGSHINWEISE", sb.toString());
	}
	
	
	private String getRows() {
		// Erzeuge eine Map Fach-ID -> AbiturFachbelegung aus den AbiturDaten
		Map<Long, AbiturFachbelegung> belegungen = abidaten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		// Erzeuge eine Map Einstelliges Sprachkürzel -> Sprachbelegung aus den AbiturDaten
		Map<String, Sprachbelegung> sprachbelegungen = abidaten.sprachendaten.belegungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		// Erzeuge eine Map Einstelliges Sprachkürzel -> Sprachpruefung aus den AbiturDaten
		Map<String, Sprachpruefung> sprachpruefungen = abidaten.sprachendaten.pruefungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		// Erzeuge für jedes Fach eine Zeile, wobei ggf. die Belegungen aus der Map verwendet werden
		StringBuilder rows = new StringBuilder();
		for (GostFach fach : gostFaecher.faecher()) {
			AbiturFachbelegung belegung = belegungen.get(fach.id);
			ZulaessigesFach zfach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			rows.append("<tr>");
			rows.append("<th>").append(fach.bezeichnung).append("</th>");
			if (fach.istFremdsprache) {
				Sprachbelegung sprachbelegung = sprachbelegungen.get(zfach.daten.kuerzel);
				Sprachpruefung sprachpruefung = sprachpruefungen.get(zfach.daten.kuerzel);
				if (sprachbelegung != null) {
					rows.append("<td>").append(sprachbelegung.reihenfolge).append(" (ab Jg. ").append(sprachbelegung.belegungVonJahrgang).append(")").append("</td>");
				} else if ((sprachpruefung != null) && (sprachpruefung.kannBelegungAlsFortgefuehrteSpracheErlauben)) {
					rows.append("<td>");
					if (sprachpruefung.kannErstePflichtfremdspracheErsetzen)
						rows.append(1);
					else if (sprachpruefung.kannZweitePflichtfremdspracheErsetzen)
						rows.append(2);
					else if (sprachpruefung.kannWahlpflichtfremdspracheErsetzen)
						rows.append(3);
					else
						rows.append("?");
					rows.append(" (Prf.)").append("</td>");
				} else {
					rows.append("<td></td>");
				}
			} else {
				rows.append("<td>").append("</td>");
			}
			if (belegung == null) {
				rows.append("<td></td>");
				rows.append("<td></td>");
				rows.append("<td></td>");
				rows.append("<td></td>");
				rows.append("<td></td>");
				rows.append("<td></td>");
				rows.append("<td></td>");
			} else {
				getBelegung(rows, belegung.belegungen[0]);
				getBelegung(rows, belegung.belegungen[1]);
				getBelegung(rows, belegung.belegungen[2]);
				getBelegung(rows, belegung.belegungen[3]);
				getBelegung(rows, belegung.belegungen[4]);
				getBelegung(rows, belegung.belegungen[5]);
				if (belegung.abiturFach == null) {
					rows.append("<td></td>");
				} else {
					rows.append("<td>").append(belegung.abiturFach).append("</td>");
				}
			}
			rows.append("</tr>");
		}
		return rows.toString();
	}
	
	
	/**
	 * Gibt die Tabellenzelle für die Halbjahres-Belegung zurück.
	 *  
	 * @param sb           der {@link StringBuilder}, in welchen die Tabellenzelle geschrieben wird.
	 * @param belegungHj   die Halbjahresbelegung
	 */
	private static void getBelegung(StringBuilder sb, AbiturFachbelegungHalbjahr belegungHj) {
		if (belegungHj == null) {
			sb.append("<td></td>");
			return;
		}
		if ("AT".equals(belegungHj.kursartKuerzel)) {
			sb.append("<td>AT</td>");
			return;
		}
		GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
		if ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF) || ((kursart == GostKursart.GK) && ((belegungHj.schriftlich == null) || (!belegungHj.schriftlich)))) {
			sb.append("<td>M</td>");
			return;
		}
		if ((kursart == GostKursart.GK) && (belegungHj.schriftlich != null) && (belegungHj.schriftlich)) {
			sb.append("<td>S</td>");
			return;
		}
		if (kursart == GostKursart.LK) {
			sb.append("<td>LK</td>");
			return;
		}
		sb.append("<td>ZK</td>");
		return;
	}

}
