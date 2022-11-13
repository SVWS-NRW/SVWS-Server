package de.nrw.schule.svws.db.utils.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import de.nrw.schule.svws.core.logger.LogConsumerConsole;
import de.nrw.schule.svws.core.logger.LogLevel;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.shell.CommandLineException;
import de.nrw.schule.svws.shell.CommandLineOption;
import de.nrw.schule.svws.shell.CommandLineParser;


/**
 * Diese Klasse stellt eine Kommandozeilen-Anwendung exportieren von Tabellen
 * aus einer Access-DB in ein Verzeichnis mit mehreren CSV-Dateien zur Verfügung.
 */
public class MDBtoCSV {
	
	/// Der Parser für die Kommandozeile
	private static CommandLineParser cmdLine;
	
	/// Der Logger
	private static Logger logger = new Logger();

	/// Die unterstützen Typen von MDBs
	private enum MDBType { UNKNOWN, STATKUE, SCHILD2_STATKUE, SCHULVER; }
	
	/// Der Typ der MDB-Datei
	private static MDBType type = MDBType.UNKNOWN;

	
	private static int cmpDate(LocalDateTime a, LocalDateTime b) {
		if ((a == null) && (b == null))
			return 0;
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		return a.compareTo(b);
	}
	

	private static int cmpString(String a, String b) {
		if ((a == null) && (b == null))
			return 0;
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		return a.compareTo(b);
	}
	

	private static int cmpInt(Integer a, Integer b) {
		if ((a == null) && (b == null))
			return 0;
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		return Integer.compare(a, b);
	}
	

	private static int cmpShort(Short a, Short b) {
		if ((a == null) && (b == null))
			return 0;
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		return Short.compare(a, b);
	}	
	
	
	private static int cmpByte(Byte a, Byte b) {
		if ((a == null) && (b == null))
			return 0;
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		return Byte.compare(a, b);
	}	
	
	
	private static Comparator<Row> compSortKurztext = (a,b) -> { 
		int cmp = cmpInt(a.getInt("Sort"),b.getInt("Sort"));
		if (cmp != 0)
			return cmp;
		return cmpString(a.getString("Kurztext"), b.getString("Kurztext")); 
	};
	

	/**
	 * Ermittelt den Typ der MDB-Datei anhand des übergebenen Strings
	 * 
	 * @param type   der Typ als String
	 * 
	 * @return der Typ der MDB-Datei
	 */
	private static MDBType getMDBTypeFromString(String type) {
		if (type == null)
			return MDBType.UNKNOWN;
		return switch (type) {
			case "statkue" -> MDBType.STATKUE;
			case "s2statkue" -> MDBType.SCHILD2_STATKUE;
			case "schulver" -> MDBType.SCHULVER;
			default -> MDBType.UNKNOWN;
		};
	}
	
	
	
	/**
	 * Bestimmt für die übergebene Tabelle eine Liste der Zeilen in
	 * einer sortiertend Reihenfolge
	 *  
	 * @param table   die Tabelle 
	 * 
	 * @return die Liste der Zeilen
	 */
	private static List<Row> getRowsStatkue(Table table) {
		return switch (table.getName()) {
			case "ASA01_Tabelle" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("ASA01_Schulnr"), b.getString("ASA01_Schulnr"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("ASA01_Sf"), b.getString("ASA01_Sf"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("ASA01_Text"), b.getString("ASA01_Text"));
				}).collect(Collectors.toList()); 
			case "AS_Schulformen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("AS_Schulf"), b.getString("AS_Schulf"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("AS_Sf2"), b.getString("AS_Sf2"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("AS_Text"), b.getString("AS_Text"));
				}).collect(Collectors.toList()); 
			case "Abgangsart" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("AbgangsJG"), b.getString("AbgangsJG"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Art"), b.getString("Art"));
					if (cmp != 0)
						return cmp;
					cmp = cmpShort(a.getShort("KZ_Bereich"),b.getShort("KZ_Bereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF"));
				}).collect(Collectors.toList()); 
			case "AllgMerkmale" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpInt(a.getInt("ID"),b.getInt("ID"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Kurztext"), b.getString("Kurztext")); 
				}).collect(Collectors.toList());  
			case "AndereGrundschulen" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("SNR"), b.getString("SNR")); 
				}).collect(Collectors.toList());  
			case "Bereiche" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpInt(a.getInt("ID_Bereich"),b.getInt("ID_Bereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SGL"), b.getString("SGL")); 
				}).collect(Collectors.toList());  
			case "Bereiche_Jahrgang" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpInt(a.getInt("ID_Bereich"),b.getInt("ID_Bereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("JG"), b.getString("JG")); 
				}).collect(Collectors.toList());  
			case "Betreuung" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Betreuung"), b.getString("Betreuung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Beschreibung"), b.getString("Beschreibung"));
				}).collect(Collectors.toList()); 
			case "Bilingual" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("Fach"), b.getString("Fach"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Einschulungsart" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("Art"), b.getString("Art")); 
				}).collect(Collectors.toList());  
			case "Fachklasse" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("AP"), b.getString("AP"));
					if (cmp != 0)
						return cmp;
					cmp = cmpShort(a.getShort("BKIndex"),b.getShort("BKIndex"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("FKS"), b.getString("FKS"));
				}).collect(Collectors.toList()); 
			case "Foerderschwerpunkt" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("FSP"), b.getString("FSP"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Gliederung" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("BKAnlage"), b.getString("BKAnlage"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("BKTyp"), b.getString("BKTyp"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Gemeinden" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpShort(a.getShort("Sortierung"),b.getShort("Sortierung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("GemKZ"), b.getString("GemKZ"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Bezeichnung"), b.getString("Bezeichnung")); 
				}).collect(Collectors.toList());  
			case "Geschlecht" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("Art"), b.getString("Art")); 
				}).collect(Collectors.toList());  
			case "Herkunftsart" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("Art"), b.getString("Art"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Herkunftslaender" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpInt(a.getInt("Sortierung"),b.getInt("Sortierung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Schluessel"), b.getString("Schluessel"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Staat_Gebiet"), b.getString("Staat_Gebiet"));
				}).collect(Collectors.toList());  
			case "Herkunftsschulform" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("HSF"), b.getString("HSF"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "JG_Bereiche" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpByte(a.getByte("JG_Bereich"),b.getByte("JG_Bereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Jahrgang"), b.getString("Jahrgang")); 
				}).collect(Collectors.toList());  
			case "LehrerAbgang" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("ASDSchluessel"), b.getString("ASDSchluessel")); 
				}).collect(Collectors.toList());  
			case "LehrerAnrechnung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerBeschaeftigungsart" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerEinsatzstatus" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerFachrAnerkennung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerFachrichtung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehramt" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehramtAnerkennung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehrbefaehigung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehrbefAnerkennung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLeitung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerMehrleistung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerMinderleistung" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerZugang" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LetzteAenderung" -> table.stream().sorted((a,b) -> {
					return cmpDate(a.getLocalDateTime("Datum"), b.getLocalDateTime("Datum"));
				}).collect(Collectors.toList());
			case "LetzteAenderung_Schild" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Datum"), b.getString("Datum"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Tabelle"), b.getString("Tabelle"));
				}).collect(Collectors.toList());
			case "Nationalitaeten" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schluessel"), b.getString("Schluessel"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Klartext"), b.getString("Klartext")); 
				}).collect(Collectors.toList());  
			case "nrw_plz_ort" -> table.stream().sorted((a,b) -> {
					int cmp = cmpShort(a.getShort("Art"), b.getShort("Art"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("PLZ"), b.getString("PLZ"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("RegSchl"), b.getString("RegSchl"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Ort"), b.getString("Ort")); 
				}).collect(Collectors.toList());  
			case "nrw_ortsteile" -> table.stream().sorted((a,b) -> {
				int cmp = cmpString(a.getString("PLZ"), b.getString("PLZ"));
				if (cmp != 0)
					return cmp;
				cmp = cmpString(a.getString("REGSCHL"), b.getString("REGSCHL"));
				if (cmp != 0)
					return cmp;
				cmp = cmpString(a.getString("ORT"), b.getString("ORT"));
				if (cmp != 0)
					return cmp;
				return cmpString(a.getString("ORTSTEIL"), b.getString("ORTSTEIL")); 
			}).collect(Collectors.toList());  
			case "nrw_strassen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("RegSchl"), b.getString("RegSchl"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Ort"), b.getString("Ort"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Strasse"), b.getString("Strasse")); 
				}).collect(Collectors.toList());  
			case "Organisationsform" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("OrgForm"), b.getString("OrgForm")); 
				}).collect(Collectors.toList());  
			case "PrfFortsetzung" -> table.stream().sorted((a,b) -> {
					int cmp = cmpInt(a.getInt("Sortierung"),b.getInt("Sortierung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Nr"), b.getString("Nr")); 
				}).collect(Collectors.toList());  
			case "PrfSemAbschl" -> table.stream().sorted((a,b) -> {
					int cmp = cmpInt(a.getInt("Sortierung"),b.getInt("Sortierung"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Nr"), b.getString("Nr")); 
				}).collect(Collectors.toList());  
			case "Reformpaedagogik" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("RPG"), b.getString("RPG")); 
				}).collect(Collectors.toList());  
			case "Religionen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schluessel"), b.getString("Schluessel"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Klartext"), b.getString("Klartext")); 
				}).collect(Collectors.toList());  
			case "SchuelerErsteSchulformSekI" -> table.stream().sorted((a,b) -> {
					return cmpInt(a.getInt("Sort"),b.getInt("Sort"));
				}).collect(Collectors.toList());  
			case "SchuelerKindergartenbesuch" -> table.stream().sorted((a,b) -> {
					return cmpInt(a.getInt("Sort"),b.getInt("Sort"));
				}).collect(Collectors.toList());  
			case "SchuelerUebergangsempfehlung5Jg" -> table.stream().sorted((a,b) -> {
					return cmpInt(a.getInt("Sort"),b.getInt("Sort"));
				}).collect(Collectors.toList());  
			case "SchuelerVerkehrssprache" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Kurztext"), b.getString("Kurztext"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Langtext"), b.getString("Langtext")); 
				}).collect(Collectors.toList());
			case "Schulformen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF"));
				}).collect(Collectors.toList());
			case "Schultraegerart" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schultraegerart"), b.getString("Schultraegerart"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Bezeichnung"), b.getString("Bezeichnung"));
				}).collect(Collectors.toList());
			case "SVWS_Zulaessige_Faecher" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Fach"), b.getString("Fach"));
				}).collect(Collectors.toList());
			case "SVWS_Zulaessige_Jahrgaenge", "SVWS_ZulaessigeJahrgaenge" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Jahrgang"), b.getString("Jahrgang"));
				}).collect(Collectors.toList());
			case "ZulFaecher" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Fach"), b.getString("Fach"));
				}).collect(Collectors.toList());
			case "ZulJahrgaenge" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Jahrgang"), b.getString("Jahrgang"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("SNR"), b.getString("SNR"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("FSP"), b.getString("FSP"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Beschreibung"), b.getString("Beschreibung"));
				}).collect(Collectors.toList());
			case "ZulKlArt" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("KlArt"), b.getString("KlArt"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("FSP"), b.getString("FSP"));
				}).collect(Collectors.toList());
			case "ZulKuArt" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Kursart"), b.getString("Kursart"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					cmp = cmpByte(a.getByte("SGLBereich"), b.getByte("SGLBereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Kursart2"), b.getString("Kursart2"));
				}).collect(Collectors.toList());
			case "ZulQualifikation" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Qualifikation"), b.getString("Qualifikation"));
				}).collect(Collectors.toList());
			default -> throw new UnsupportedOperationException("Unbekannter Tabellenname in der Statkue-MDB : " + table.getName());
		};
	}
	
	
	/**
	 * Bestimmt für die übergebene Tabelle eine Liste der Zeilen in
	 * einer sortiertend Reihenfolge
	 *  
	 * @param table   die Tabelle 
	 * 
	 * @return die Liste der Zeilen
	 */
	private static List<Row> getRowsSchild2Statkue(Table table) {
		return switch (table.getName()) {
			case "ASA01_Tabelle" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("ASA01_Schulnr"), b.getString("ASA01_Schulnr"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("ASA01_Sf"), b.getString("ASA01_Sf"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("ASA01_Text"), b.getString("ASA01_Text"));
				}).collect(Collectors.toList()); 
			case "AS_Schulformen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("AS_Schulf"), b.getString("AS_Schulf"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("AS_Sf2"), b.getString("AS_Sf2"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("AS_Text"), b.getString("AS_Text"));
				}).collect(Collectors.toList()); 
			case "Abgangsart" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("AbgangsJG"), b.getString("AbgangsJG"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Art"), b.getString("Art"));
					if (cmp != 0)
						return cmp;
					cmp = cmpInt(a.getInt("KZ_Bereich"),b.getInt("KZ_Bereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF"));
				}).collect(Collectors.toList()); 
			case "AllgMerkmale" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpInt(a.getInt("ID"),b.getInt("ID"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Kurztext"), b.getString("Kurztext")); 
				}).collect(Collectors.toList());  
			case "AndereGrundschulen" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("SNR"), b.getString("SNR")); 
				}).collect(Collectors.toList());  
			case "Bereiche" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpInt(a.getInt("ID_Bereich"),b.getInt("ID_Bereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SGL"), b.getString("SGL")); 
				}).collect(Collectors.toList());  
			case "Betreuung" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Betreuung"), b.getString("Betreuung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Beschreibung"), b.getString("Beschreibung"));
				}).collect(Collectors.toList()); 
			case "Bilingual" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("Fach"), b.getString("Fach"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Einschulungsart" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("Art"), b.getString("Art")); 
				}).collect(Collectors.toList());  
			case "Fachklasse" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("AP"), b.getString("AP"));
					if (cmp != 0)
						return cmp;
					cmp = cmpInt(a.getInt("BKIndex"),b.getInt("BKIndex"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("FKS"), b.getString("FKS"));
				}).collect(Collectors.toList()); 
			case "Förderschwerpunkt" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("FSP"), b.getString("FSP"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Gliederung" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("BKAnlage"), b.getString("BKAnlage"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("BKTyp"), b.getString("BKTyp"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Gemeinden" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpShort(a.getShort("Sortierung"),b.getShort("Sortierung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("GemKZ"), b.getString("GemKZ"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Bezeichnung"), b.getString("Bezeichnung")); 
				}).collect(Collectors.toList());  
			case "Geschlecht" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("Art"), b.getString("Art")); 
				}).collect(Collectors.toList());  
			case "Herkunftsart" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("Art"), b.getString("Art"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "Herkunftslaender" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpInt(a.getInt("Sortierung"),b.getInt("Sortierung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Schluessel"), b.getString("Schluessel"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Staat_Gebiet"), b.getString("Staat_Gebiet"));
				}).collect(Collectors.toList());  
			case "Herkunftsschulform" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("HSF"), b.getString("HSF"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF")); 
				}).collect(Collectors.toList());  
			case "JG_Bereiche" -> table.stream().sorted((a,b) -> { 
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpByte(a.getByte("JG_Bereich"),b.getByte("JG_Bereich"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Jahrgang"), b.getString("Jahrgang")); 
				}).collect(Collectors.toList());  
			case "LehrerAbgangKat" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("ASDSchluessel"), b.getString("ASDSchluessel")); 
				}).collect(Collectors.toList());  
			case "LehrerAnrechnungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerBeschaeftigungsartKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerEinsatzstatusKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerFachrAnerkennungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerFachrichtungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehramtKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehramtAnerkennungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehrbefaehigungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLehrbefAnerkennungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerLeitungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerMehrleistungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerMinderleistungKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerRechtsverhältnisKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LehrerZugangKat" -> table.stream().sorted(compSortKurztext).collect(Collectors.toList());  
			case "LetzteAenderung" -> table.stream().sorted((a,b) -> {
					return cmpDate(a.getLocalDateTime("Datum"), b.getLocalDateTime("Datum"));
				}).collect(Collectors.toList());
			case "LetzteAenderung_Schild" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Datum"), b.getString("Datum"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Tabelle"), b.getString("Tabelle"));
				}).collect(Collectors.toList());
			case "Nationalitäten" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schluessel"), b.getString("Schluessel"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Klartext"), b.getString("Klartext")); 
				}).collect(Collectors.toList());  
			case "PLZOrt" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("PLZ"), b.getString("PLZ"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("RegSchl"), b.getString("RegSchl"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Ort"), b.getString("Ort")); 
				}).collect(Collectors.toList());  
			case "Strassen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("RegSchl"), b.getString("RegSchl"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Ort"), b.getString("Ort"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Strasse"), b.getString("Strasse")); 
				}).collect(Collectors.toList());  
			case "Organisationsform" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("OrgForm"), b.getString("OrgForm")); 
				}).collect(Collectors.toList());  
			case "PrfFortsetzung" -> table.stream().sorted((a,b) -> {
					int cmp = cmpInt(a.getInt("Sortierung"),b.getInt("Sortierung"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Nr"), b.getString("Nr")); 
				}).collect(Collectors.toList());  
			case "PrfSemAbschl" -> table.stream().sorted((a,b) -> {
					int cmp = cmpInt(a.getInt("Sortierung"),b.getInt("Sortierung"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Nr"), b.getString("Nr")); 
				}).collect(Collectors.toList());  
			case "Reformpädagogik" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("RPG"), b.getString("RPG")); 
				}).collect(Collectors.toList());  
			case "Religionen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schluessel"), b.getString("Schluessel"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Klartext"), b.getString("Klartext")); 
				}).collect(Collectors.toList());  
			case "SchuelerErsteSchulformSekI" -> table.stream().sorted((a,b) -> {
					return cmpInt(a.getInt("Sort"),b.getInt("Sort"));
				}).collect(Collectors.toList());  
			case "SchuelerKindergartenbesuch" -> table.stream().sorted((a,b) -> {
					return cmpInt(a.getInt("Sort"),b.getInt("Sort"));
				}).collect(Collectors.toList());  
			case "SchuelerUebergangsempfehlung5Jg" -> table.stream().sorted((a,b) -> {
					return cmpInt(a.getInt("Sort"),b.getInt("Sort"));
				}).collect(Collectors.toList());  
			case "SchuelerVerkehrssprache" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Kurztext"), b.getString("Kurztext"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Langtext"), b.getString("Langtext")); 
				}).collect(Collectors.toList());
			case "Schulformen" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("SF"), b.getString("SF"));
				}).collect(Collectors.toList());
			case "Schultraegerart" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schultraegerart"), b.getString("Schultraegerart"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Bezeichnung"), b.getString("Bezeichnung"));
				}).collect(Collectors.toList());
			case "SVWS_BKAnlagen" -> table.stream().sorted((a,b) -> {
					return cmpString(a.getString("BKAnlage"), b.getString("BKAnlage"));
				}).collect(Collectors.toList());
			case "SVWS_Fachgruppen" -> table.stream().sorted((a,b) -> {
					return cmpInt(a.getInt("ID"), b.getInt("ID"));
				}).collect(Collectors.toList());
			case "SVWS_Schulgliederungen" -> table.stream().sorted((a,b) -> {
					return cmpString(a.getString("SGL"), b.getString("SGL"));
				}).collect(Collectors.toList());
			case "SVWS_ZulaessigeFaecher" -> table.stream().sorted((a,b) -> {
					return cmpString(a.getString("Fach"), b.getString("Fach"));
				}).collect(Collectors.toList());
			case "SVWS_ZulaessigeJahrgaenge" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Jahrgang"), b.getString("Jahrgang"));
				}).collect(Collectors.toList());
			case "SVWS_ZulaessigeKursarten" -> table.stream().sorted((a,b) -> {
					return cmpString(a.getString("ID"), b.getString("ID"));
				}).collect(Collectors.toList());
			case "ZulFächer" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Fach"), b.getString("Fach"));
				}).collect(Collectors.toList());
			case "ZulJahrgänge" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Jahrgang"), b.getString("Jahrgang"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("SNR"), b.getString("SNR"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("FSP"), b.getString("FSP"));
				}).collect(Collectors.toList());
			case "ZulKlArt" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("KlArt"), b.getString("KlArt"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("FSP"), b.getString("FSP"));
				}).collect(Collectors.toList());
			case "ZulKuArt" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SF"), b.getString("SF"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Kursart"), b.getString("Kursart"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("Flag"), b.getString("Flag"));
					if (cmp != 0)
						return cmp;
					return cmpInt(a.getInt("SGLBereich"), b.getInt("SGLBereich"));
				}).collect(Collectors.toList());
			case "ZulQualifikation" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("Schulform"), b.getString("Schulform"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("Qualifikation"), b.getString("Qualifikation"));
				}).collect(Collectors.toList());
			default -> throw new UnsupportedOperationException("Unbekannter Tabellenname in der Schild2-Statkue-MDB : " + table.getName());
		};
	}
	
	
	/**
	 * Bestimmt für die übergebene Tabelle eine Liste der Zeilen in
	 * einer sortiertend Reihenfolge
	 *  
	 * @param table   die Tabelle 
	 * 
	 * @return die Liste der Zeilen
	 */
	private static List<Row> getRowsSchulver(Table table) {
		return switch (table.getName()) {
			case "DBS" -> table.stream().sorted((a,b) -> {
					if (a.getString("Schulnr") != null)
						return cmpString(a.getString("Schulnr"), b.getString("Schulnr"));
					return cmpString(a.getString("SchulNr"), b.getString("SchulNr")); 
				}).collect(Collectors.toList());  
			case "Schulformen" -> table.stream().sorted((a,b) -> {
					return cmpInt(Integer.parseInt(a.getString("Schulform")), Integer.parseInt(b.getString("Schulform")));
				}).collect(Collectors.toList());  
			case "Schultraeger" -> table.stream().sorted((a,b) -> { 
					return cmpString(a.getString("SchulNr"), b.getString("SchulNr")); 
				}).collect(Collectors.toList());  
			case "WeitereSF" -> table.stream().sorted((a,b) -> {
					int cmp = cmpString(a.getString("SNR"), b.getString("SNR"));
					if (cmp != 0)
						return cmp;
					cmp = cmpString(a.getString("SGL"), b.getString("SGL"));
					if (cmp != 0)
						return cmp;
					return cmpString(a.getString("FSP"), b.getString("FSP"));
				}).collect(Collectors.toList()); 
			default -> throw new UnsupportedOperationException("Unbekannter Tabellenname " + table.getName() + " in der Schulver-MDB");
		};
	}
	
	
	/**
	 * Bestimmt einen iterierbares Objekt für die Tabellenzeilen, welches die Zeilen in
	 * einer sortierten Reihenfolge zurückgibt, sofern es sich nicht um einen unkannten
	 * MDB-Typ handelt. In einem solchen Fall kann keine Sortierung garantiert werden.
	 *  
	 * @param table   die Tabelle, für welche das iterierbare Objekt bestimmt werden soll. 
	 * 
	 * @return das iterierbare Objekt
	 */
	private static Iterable<Row> getRows(Table table) {
		return switch (type) {
			case SCHULVER -> getRowsSchulver(table);
			case STATKUE -> getRowsStatkue(table);
			case SCHILD2_STATKUE -> getRowsSchild2Statkue(table);
			case UNKNOWN -> table;
			default -> table;
		};
	}

	
	/**
	 * Passt die Reihenfolge der Spalten an, falls es sich um eine Schulver-DB handelt.
	 * 
	 * @param table  die Tabelle, für welche die Spaltenreihenfolge angepasst wird
	 * @param cols   die zu sortierenden Spalten
	 * 
	 * @return die sortierten Spalten
	 */
	private static List<? extends Column> reorderSchulver(Table table, List<? extends Column> cols) {
		return switch (table.getName()) {
			case "DBS" -> {
				Map<String, ? extends Column> mapCols = cols.stream().collect(Collectors.toMap(c -> c.getName(), c -> c));
				List<Column> result = new Vector<>();
				if (mapCols.get("SchulNr") == null)
					result.add(mapCols.get("Schulnr"));
				else
					result.add(mapCols.get("SchulNr"));
				if (mapCols.get("RegSchl") == null)
					result.add(mapCols.get("Regschl"));
				else
					result.add(mapCols.get("RegSchl"));
				result.add(mapCols.get("KoRe"));
				result.add(mapCols.get("KoHo"));		
				result.add(mapCols.get("ABez1"));
				result.add(mapCols.get("ABez2"));
				result.add(mapCols.get("ABez3"));
				result.add(mapCols.get("PLZ"));
				result.add(mapCols.get("Ort"));
				result.add(mapCols.get("Strasse"));
				result.add(mapCols.get("TelVorw"));
				result.add(mapCols.get("Telefon"));
				result.add(mapCols.get("FaxVorw"));
				result.add(mapCols.get("Fax"));
				result.add(mapCols.get("ModemVorw"));
				result.add(mapCols.get("Modem"));
				result.add(mapCols.get("SF"));
				result.add(mapCols.get("OeffPri"));
				result.add(mapCols.get("KurzBez"));
				result.add(mapCols.get("SchBetrSchl"));
				result.add(mapCols.get("SchBetrSchlDatum"));
				if (mapCols.get("ArtderTraegerschaft") == null)
					result.add(mapCols.get("ArtderTrägerschaft"));
				else
					result.add(mapCols.get("ArtderTraegerschaft"));
				if (mapCols.get("SchultraegerNr") == null)
					result.add(mapCols.get("SchulträgerNr"));
				else
					result.add(mapCols.get("SchultraegerNr"));
				result.add(mapCols.get("Schulgliederung"));
				result.add(mapCols.get("Schulart"));
				result.add(mapCols.get("Ganztagsbetrieb"));
				result.add(mapCols.get("FSP"));
				result.add(mapCols.get("Verbund"));
				result.add(mapCols.get("Bus"));
				result.add(mapCols.get("Fachberater"));
				if (mapCols.get("FachberHauptamtl") == null)
					result.add(mapCols.get("Fachber hauptamtl"));
				else
					result.add(mapCols.get("FachberHauptamtl"));
				result.add(mapCols.get("TelNrDBSalt"));
				result.add(mapCols.get("RP"));
				result.add(mapCols.get("Email"));
				result.add(mapCols.get("URL"));
				result.add(mapCols.get("CD"));
				result.add(mapCols.get("Stift"));
				result.add(mapCols.get("OGTS"));
				result.add(mapCols.get("SELB"));
				result.add(mapCols.get("Internat"));
				result.add(mapCols.get("InternatPlaetze"));
				if (mapCols.get("SMail") == null)
					result.add(mapCols.get("S-Mail"));
				else
					result.add(mapCols.get("SMail"));
				result.add(mapCols.get("SportImAbi"));
				result.add(mapCols.get("Tal"));
				result.add(mapCols.get("KonKop"));
				yield result;
			}
			case "Schulformen" -> cols;
			case "Schultraeger" -> cols;
			case "WeitereSF" -> cols;
			default -> throw new UnsupportedOperationException("Unbekannter Tabellenname " + table.getName() + " in der Schulver-MDB");
		};
	}
	

	/**
	 * Passt die Reihenfolge der Spalten an, falls es sich um einen bekannten MDB-Typ handelt
	 * 
	 * @param table  die Tabelle, für welche die Spaltenreihenfolge angepasst wird
	 * @param cols   die zu sortierenden Spalten
	 * 
	 * @return die sortierten Spalten
	 */
	private static List<? extends Column> reorder(Table table, List<? extends Column> cols) {
		return switch (type) {
			case SCHULVER -> reorderSchulver(table, cols);
			case UNKNOWN -> cols;
			default -> cols;
		};
	}
	

	
	private static String renameColumnSchulver(Table table, String colname) {
		return switch (table.getName()) {
			case "DBS" -> {
				yield switch(colname) {
					case "Schulnr" -> "SchulNr";
					case "Regschl" -> "RegSchl";
					case "ArtderTrägerschaft" -> "ArtderTraegerschaft";
					case "SchulträgerNr" -> "SchultraegerNr";
					case "Fachber hauptamtl" -> "FachberHauptamtl";
					case "S-Mail" -> "SMail";
					default -> colname;
				};
			}
			case "Schulformen" -> colname;
			case "Schultraeger" -> {
				yield switch(colname) {
					case "Schulnr" -> "SchulNr";
					case "Regschl" -> "RegSchl";
					case "aktiv" -> "Aktiv";
					default -> colname;
				};
			}
			case "WeitereSF" -> colname;
			default -> throw new UnsupportedOperationException("Unbekannter Tabellenname " + table.getName() + " in der Schulver-MDB");
		};
	}
	
	
	private static String renameColumn(Table table, String colname) {
		return switch (type) {
			case SCHULVER -> renameColumnSchulver(table, colname);
			case UNKNOWN -> colname;
			default -> colname;
		};		
	}
	
	
	private static String fixOutputSchulver(Table table, Row r, DataType colType, String colName) {
		return switch (table.getName()) {
			case "DBS" -> {
				yield switch(colName) {
					case "KoRe", "KoHo" -> {
						if (colType == DataType.DOUBLE)
							yield "" + r.getDouble(colName).longValue();
						yield null;
					}
					default -> null;
				};
			}
			case "Schulformen" -> null;
			case "Schultraeger" -> null;
			case "WeitereSF" -> null;
			default -> null;
		};		
	}
	
	
	private static String getCSVOutput(Table table, Row r, DataType colType, String colName) {
		String fixedOutput = switch (type) {
			case SCHULVER -> fixOutputSchulver(table, r, colType, colName);
			case UNKNOWN -> null;
			default -> null;
		};		
		if (fixedOutput != null)
			return fixedOutput;
		return switch (colType) {
			case BYTE -> (r.getByte(colName) == null) ? "null" : r.getByte(colName).toString();
			case INT -> (r.getShort(colName) == null) ? "null" : r.getShort(colName).toString();
			case LONG -> (r.getInt(colName) == null) ? "null" : r.getInt(colName).toString();
			case BIG_INT -> {
				Object o = r.get(colName);
				if (o instanceof Long l)
					yield l.toString();
				throw new UnsupportedOperationException(colType.toString());
			}
			case BOOLEAN  -> { 
				Boolean b = r.getBoolean(colName); 
				yield (b != null) && b ? "1" : "0";
			}
			case FLOAT -> (r.getFloat(colName) == null) ? "null" : r.getFloat(colName).toString();
			case DOUBLE -> (r.getDouble(colName) == null) ? "null" : r.getDouble(colName).toString();
			case TEXT, MEMO, GUID -> {
				String str = r.getString(colName);
				if (str == null)
					yield "null";
				yield "\"" + str.replace("\"", "'") + "\"";
			}
			case SHORT_DATE_TIME -> (r.getLocalDateTime(colName) == null) ? "null" : r.getLocalDateTime(colName).toString();
			case EXT_DATE_TIME -> (r.getLocalDateTime(colName) == null) ? "null" : r.getLocalDateTime(colName).toString();
			case BINARY -> throw new UnsupportedOperationException(colType.toString());
			case COMPLEX_TYPE  -> throw new UnsupportedOperationException(colType.toString());
			case MONEY -> throw new UnsupportedOperationException(colType.toString());
			case NUMERIC -> throw new UnsupportedOperationException(colType.toString());
			case OLE -> throw new UnsupportedOperationException(colType.toString());
			case UNKNOWN_0D -> throw new UnsupportedOperationException(colType.toString());
			case UNKNOWN_11 -> throw new UnsupportedOperationException(colType.toString());
			case UNSUPPORTED_FIXEDLEN -> throw new UnsupportedOperationException(colType.toString());
			case UNSUPPORTED_VARLEN -> throw new UnsupportedOperationException(colType.toString());
			default -> throw new UnsupportedOperationException(colType.toString());
		};		
	}
	

	/**
	 * Erstellt die CSV-Daten anhand der übergebenen Tabelle aus der MDB.
	 * 
	 * @param table   die zu konvertierende Tabelle
	 * 
	 * @return der CSV-String
	 */
	public static String createCSV(Table table) {
		StringBuilder sb = new StringBuilder();
		int rowCount = table.getRowCount();
		logger.logLn(LogLevel.INFO, "Lese " + table.getName() + ": " + rowCount + " Zeilen...");
		List<? extends Column> cols = reorder(table, table.getColumns());
		for (int i = 0; i < cols.size(); i++) {
			Column col = cols.get(i);
			sb.append(renameColumn(table, col.getName()));
			if (i < cols.size() - 1)
				sb.append(";");
		}
		sb.append(System.lineSeparator());
		for (Row r : getRows(table)) {
			for (int i = 0; i < cols.size(); i++) {
				Column col = cols.get(i);
				String colName = col.getName();
				boolean doWrite = true;
				if (type == MDBType.STATKUE) {
					switch (col.getName()) {
						case "geaendert", "Stand", "STAND" -> { doWrite = false; }
					}
				}
				if (doWrite) {
					DataType colType = col.getType();
					String output = getCSVOutput(table, r, colType, colName);
					sb.append(output);
				}
				if (i < cols.size() - 1)
					sb.append(";");						
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	
	/**
	 * Main-Methode für den MDB-zu-CSV-Konverter. 
	 * 
	 * @param args   die Kommandozeilenparameter
	 * 
	 * @throws IOException   wird bei Fehlern beim Zugriff auf das Dateisystem erzeugt.   
	 */
	public static void main(String[] args) throws IOException {
		logger.addConsumer(new LogConsumerConsole());
		
		// Lese die Kommandozeilenparameter ein
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("i", "input", true, "Der Dateiname der MDB-Datei"));
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Ort, wo die CSV-Dateien erzeugt werden sollen"));
			cmdLine.addOption(new CommandLineOption("t", "type", true, "Der Typ der MDB für Aspekte, wie z.B. die Sortierung der Datensätze"));
	
			// Lies den Dateinamen der MDB-Datei ein und prüfe, ob die Datei existiert
			Path inFile = Paths.get(cmdLine.getValue("i", "input.mdb"));
			if (!Files.exists(inFile)) {
				logger.logLn(LogLevel.ERROR, "Eingabe-MDB-Datei existiert nicht im Pfad " + inFile);
				System.exit(1);
			}
			
			// Erstelle das Ausgabe-Verzeichnis, sofern es noch nicht existiert
			Path outDir = Paths.get(cmdLine.getValue("o", "build/tmp/csv"));
			if (!Files.exists(outDir)) {
				logger.logLn(LogLevel.INFO, "Erstelle Ausgabe Verzeichnis '" + outDir + "'");
				Files.createDirectory(outDir);
			}
			
			// Bestimme den Typ der MDB
			type = getMDBTypeFromString(cmdLine.getValue("t", null));

			logger.logLn(LogLevel.INFO, "Lese " + inFile + " ..."); 
			logger.modifyIndent(2);
			try (Database db = DatabaseBuilder.open(inFile)) {
				List<String> tablenames = db.getTableNames().stream().sorted().collect(Collectors.toList());
				for (String tablename : tablenames) {
					if (type == MDBType.SCHULVER) {
						if (("Kreistabelle".equals(tablename)) || ("öffpriText".equals(tablename)) || ("ortsnamen_tab".equals(tablename))
								 || ("ortswahl".equals(tablename)) || ("RPTabelle".equals(tablename)) || ("Schulbetrieb".equals(tablename))
								 || ("Schulformen_alt".equals(tablename)) || ("Schulträger".equals(tablename))
								 || ("Tabelle1".equals(tablename)) || ("testschulenumsetzer".equals(tablename))
								 || ("Version_2018".equals(tablename)) || ("WeitereSFdub".equals(tablename)) || ("WeitereSFx".equals(tablename)))
							continue;
					}
					Path outFile = Paths.get(outDir.toString() + "/" + tablename + ".csv");
					Table table = db.getTable(tablename);
					String sql = createCSV(table);
					logger.logLn(LogLevel.INFO, "Schreibe " + outFile + " ...");
					Files.writeString(outFile, sql, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				}
			}
			logger.modifyIndent(-2);
			logger.logLn(LogLevel.INFO, "Fertig!");		
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}

	}

}
