package de.nrw.schule.svws.db.utils.app;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;

import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.dto.DTOs;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.utils.DBDefaultData;
import de.nrw.schule.svws.shell.CommandLineException;
import de.nrw.schule.svws.shell.CommandLineOption;
import de.nrw.schule.svws.shell.CommandLineParser;

/**
 * Dieses Programm erstellt die Skripte für die aktuelle Revision in dem Verzeichnis 
 * schema/sql/[n]/[dbms]/, wobei n die Revision und dbms der Name des DBMS ist 
 * (z.B. MARIA_DB).  
 */
public class SQLGenerator {
	
	private static String dirOutput = "build/sql";

	/// Der Parser für die Kommandozeile
	private static CommandLineParser cmdLine;


    /**
     * Gibt den SQL-Befehl zum Einfügen aller Default-Daten in diese Tabelle zurück
     * 
     * @param tabelle   die Tabelle mit den Default-Daten
     * @param rev       die Revision, für welche das Skript erzeugt wird
     * 
     * @return der SQL-Befehl zum Einfügen aller Default-Daten
     */
    public static String getSQLInsert(SchemaTabelle tabelle, long rev) {
    	// Bestimme die Spalten der Tabelle und erzeuge die INSERT INTO - Zeile mit den Spaltennamen
    	var cols = tabelle.getSpalten().stream()
    			.filter(col -> ((rev == -1) && (col.veraltet().revision == -1)) 
        				|| ((rev != -1) && (rev >= col.revision().revision) && ((col.veraltet().revision == -1) || (rev < col.veraltet().revision))))
    			.sorted((a,b) -> Integer.compare(a.sortierung(), b.sortierung()))
    			.collect(Collectors.toList());
    	String newline = System.lineSeparator();
    	String script = "INSERT INTO " + tabelle.name() + "(" 
    			+ cols.stream().map(col -> col.name()).collect(Collectors.joining(","))
    			+ ") VALUES" + newline;
    	// Lese die Default-Daten ein
		Class<?> dtoClass = DTOs.getFromTableName(tabelle.name());
		if (dtoClass == null)
			throw new RuntimeException("Fehler beim Erstellen des SQL-Skriptes für die Revision " + rev + " zum Einfügen der Default-Daten in die Tabelle " + tabelle.name());
        var data = CsvReader.fromResource(DBDefaultData.getFileName(tabelle), dtoClass);
        // Erstelle den einzelnen Zeilen für den Einfüge-Befehl
        List<String> rows = data.stream()
        		.map(dto -> {
        			return "(" + cols.stream().map(col -> {
        				try {
            				Field field = dtoClass.getDeclaredField(col.name());
            				field.setAccessible(true);
							var obj = field.get(dto);
							if (obj == null)
								return "NULL";
							var coldata = obj.toString();
							if (col.datentyp().isQuoted()) 
								coldata = "'" + coldata.replace("'", "''") + "'";
	        				return coldata;
						} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
							throw new RuntimeException("Fehler beim Erstellen des SQL-Skriptes für die Revision " + rev + " zum Einfügen der Default-Daten in die Tabelle " + tabelle.name() + " (kann die Daten nicht korrekt einlesen)", e);
						} 
        			}).collect(Collectors.joining(",")) + ")";
        		}).collect(Collectors.toList());
    	return script + rows.stream().collect(Collectors.joining("," + newline)) + ";";
    }
    
    
	/**
	 * Generiert das SQL-Skript zum Erstellen eines Schema für die angebene Revision 
	 * und den SQL-Dialekt des angegebenen DBMS. 
	 *
	 * @param dbms   das DBMS
	 * @param rev    die Revision oder -1 für die aktuelle Revision
	 *
	 * @return das SQL-Create-Skript
	 */
	public static String getCreateSchemaSkript(DBDriver dbms, long rev) {
		String newline = System.lineSeparator();
		String result = "";
		for (SchemaTabelle t : Schema.tabellen.values()) {
			result += t.getSQL(dbms, rev);
			var sql_indizes = t.getSQLIndizes(rev);
			if ((sql_indizes != null) && (!"".equals(sql_indizes)))
				result += newline + newline + sql_indizes;
			result += newline + newline + newline;
		}
		result += newline
		        + ((dbms == DBDriver.MSSQL) ? "GO" + newline + newline : "")
		        + Schema.tabellen.values().stream()
		       		.map(tab -> tab.getSQLTrigger(dbms, rev, true))
		       		.filter(sql -> (sql != null) && (!"".equals(sql)))
					.collect(Collectors.joining(newline + newline))
                + newline + newline + newline
				+ "INSERT INTO SVWS_DB_Version(Revision) VALUES (" + ((rev == - 1) ? SchemaRevisionen.maxRevision.revision : rev) + ");" + newline
		        + newline 
		        + getInserts(rev)
		        + newline 
		        + Schema.getCreateBenutzerSQL(rev).stream().collect(Collectors.joining(newline + newline)) + newline;
		return result;
	}

	
	/**
	 * Generiert das SQL-Skript zum Verwerfen eines Schema für die angebene Revision 
	 * und den SQL-Dialekt des angegebenen DBMS. 
	 *
	 * @param dbms   das DBMS 
	 * @param rev    die Revision oder -1 für die aktuelle Revision
	 *
	 * @return das SQL-Drop-Skript
	 */
	public static String getDropSchemaSkript(DBDriver dbms, long rev) {
		var tmp = Schema.getTabellen(rev);
		Collections.reverse(tmp);
		return tmp.stream().map(t -> t.getSQLDrop(dbms)).collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator();
	}



	/**
	 * Erstellt aus der Schema-Definition ein SQL-Skript zum Einfügen 
	 * von Default-Daten (z.B. für Core-Types) für die angebene Revision. 
	 *
	 * @param revision   die Revision oder -1 für die aktuelle Revision
	 *
	 * @return das SQL-Skript zum Einfügen der Default-Daten
	 */
	public static String getInserts(long revision) {
		final long rev = (revision == -1) ? SchemaRevisionen.maxRevision.revision : revision;
		StringBuilder result = new StringBuilder();
		Set<SchemaTabelle> tabsDefaultData = Arrays.stream(Schema.tabellenDefaultDaten).collect(Collectors.toSet());
		for (SchemaTabelle tab : Schema.tabellen.values()) {
		    if (!(((rev == -1) && (tab.veraltet().revision == -1)) || ((rev != -1) && (rev >= tab.revision().revision) && ((tab.veraltet().revision == -1) || (rev < tab.veraltet().revision)))))
		        continue;
		    if (tabsDefaultData.contains(tab)) {
		        result.append(getSQLInsert(tab, rev));
		        result.append(System.lineSeparator());
                result.append(System.lineSeparator());
                result.append(System.lineSeparator());
		    }
		}
		return result.toString();
	}



	/**
	 * Diese Methode schreibt die übergebenen Daten in die angebene Datei.
	 * 
	 * @param file    die Datei, in welcher geschrieben werden soll
	 * @param data    die zu schreibenden Daten
	 * 
	 * @throws IOException    tritt auf, wenn die Daten nicht erfolgreich geschrieben werden konnten
	 */
	public static void writeTo(Path file, String data) throws IOException {
		System.out.print("  Schreibe " + file + "... ");
		try (InputStream in = IOUtils.toInputStream(data, StandardCharsets.UTF_8)) {
			Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
			in.close();
		}
		System.out.println("[OK]");
	}


	/**
	 * Schreibt die jeweiligen Create-Schema-, Drop-Schema- und Default-Daten-SQL-Skript für die einzelnen
	 * DBMS für die angebene Revision in das angegebene Verzeichnis.  
	 * 
	 * @param baseDir     das Verzeichnis, in welches die Skripte geschrieben werden sollen
	 * @param revision    die Revision der SVWS-Datenbank, für welche die Skripte geschrieben werden sollen
	 * 
	 * @throws IOException   tritt auf, wenn die Skripte nicht erfolgreich geschrieben werden konnten
	 */
	private static void writeScript(Path baseDir, long revision) throws IOException {
		// Create- und Drop-Schema-Skripte in Abhängigkeit von dem DBMS
		System.out.println("- Erzeuge Skripte für die Revision " + revision);
		for (DBDriver driver : DBDriver.values()) {
			Path dir = Paths.get(baseDir.toString());
			System.out.println("Treiber " + driver + " -> " + dir + " : "); 
			Files.createDirectories(dir);

			String revStr = (revision == -1) ? "current." : "rev" + revision + ".";
			writeTo(Paths.get(dir.toString(), "drop_schema." + revStr + driver.toString().toLowerCase() + ".sql"), getDropSchemaSkript(driver, revision));
			writeTo(Paths.get(dir.toString(), "create_schema." + revStr + driver.toString().toLowerCase() + ".sql"), getCreateSchemaSkript(driver, revision));
		}
	}
	
	
	/**
	 * Schreibt die jeweiligen Create-Schema-, Drop-Schema- und Default-Daten-SQL-Skript für die einzelnen
	 * DBMS für die angebenen Revisionen in das angegebene Verzeichnis.  
	 * 
	 * @param baseDir     das Verzeichnis, in welches die Skripte geschrieben werden sollen
	 * @param revision    die Revision der SVWS-Datenbank, bis zu welcher die Skripte geschrieben werden sollen
	 * @param allrev      gibt an, ob alle Revision ab Revision 0 geschrieben werden sollen oder nur eine
	 * 
	 * @throws IOException   tritt auf, wenn die Skripte nicht erfolgreich geschrieben werden konnten
	 */
	public static void writeScripts(Path baseDir, long revision, boolean allrev) throws IOException {
		for (long r = (allrev ? 0 : revision); r <= revision; r++) {
			writeScript(Paths.get(baseDir.toString(), "" + r), r);
		}
		writeScript(Paths.get(baseDir.toString(), "current"), -1);
	}


	/**
	 * Dieses Programm erstellt die Skripte für die aktuelle Revision in dem Verzeichnis 
     * schema/sql/[n]/[dbms]/, wobei n die Revision und dbms der Name des DBMS ist 
     * (z.B. MARIA_DB).
     * 
	 * @param args   die Kommandozeilen-Optionen für dieses Programm
	 * 
	 * @throws IOException   tritt auf, wenn die Skripte nicht erfolgreich geschrieben werden konnten
	 */
	public static void main(String[] args) throws IOException {
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Ort, an welchem die Skripte in Unterordnern paltziert werden sollen (Default: " + dirOutput + ")"));	
			cmdLine.addOption(new CommandLineOption("a", "all", false, "Gibt an, dass Skripte für alle Schema-Revision bis zu der mit r angegebenen Revision erstellt werden"));	
			cmdLine.addOption(new CommandLineOption("r", "revision", true, "Die Schema-Revision für die das Skript erstellt wird (Default: -1 für die aktuelle Revision)"));
			
			// Lese ggf. den Ausgabe-Pfad ein
			Path dirScripts = Paths.get(cmdLine.getValue("o", dirOutput));
			System.out.println("- Ausgabe-Verzeichnis \"" + dirScripts + "\"");
			Files.createDirectories(dirScripts);
			
			// Lese optional eine spezielle Revision ein...
			long rev = NumberUtils.toLong(cmdLine.getValue("r", "-1"), -1);
			long revision = (rev == -1) ? SchemaRevisionen.maxRevision.revision : rev;
	
			// Schreibe die Daten in das Revisions-Verzeichnis und ggf. in das Verzeichnis für die aktuelle Revision (-1)
			boolean allrev = cmdLine.isSet("a");  
			writeScripts(Paths.get(dirScripts.toString()), revision, allrev);			
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}
	}

}
