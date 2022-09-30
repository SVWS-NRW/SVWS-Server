package de.nrw.schule.svws.db.schema.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.db.schema.DBSchemaDefinition;
import de.nrw.schule.svws.db.schema.DBSchemaViews;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.View;
import de.nrw.schule.svws.db.schema.csv.TabelleSpalte;
import de.nrw.schule.svws.logger.LogConsumerConsole;
import de.nrw.schule.svws.logger.Logger;
import de.nrw.schule.svws.shell.CommandLineException;
import de.nrw.schule.svws.shell.CommandLineOption;
import de.nrw.schule.svws.shell.CommandLineParser;



/**
 * Diese Klasse generiert den Java-Code für die JavaDTO-Klassen entsprechend eines spezifierten Datenbankschemas.
 */
public class DTOCreator {

	/** Der Parser für die Kommandozeile */
	private static CommandLineParser cmdLine;

	/** Der intern genutzte Logger */
	private static Logger logger = new Logger();

	/** Die Konsole als Ziel der Log-Informationen */
	private static LogConsumerConsole logConsumerConsole = new LogConsumerConsole();
	
	/** Ein Vector mit allen generierten DTO-Klassen (voll qualifizierte Klassennamen) */
	private static Vector<String> allClasses = new Vector<>();


	/**
	 * Erstellt das Verzeichnis für die DTO-Packages
	 * 
	 * @param baseDir   das Verzeichnis, in welchem das Package für die DTOs erzeugt werden soll
	 * @param rev        die Revision, für die die DTOs erzeugt werden sollen (-1 für die neueste Revision)
	 * 
	 * @return das {@link File}-Objekt, falls das Verzeichnis erstellt wurde, im Fehlerfall null 
	 */
	private static File createPackageDirectory(final String baseDir, final long rev) {
		try {
			// Erstelle das Verzeichnis für das Package
			String pack = DBSchemaDefinition.javaPackage + "." + DBSchemaDefinition.javaDTOPackage;
			logger.logLn("Erzeuge Package " + pack);
			String packPath = pack.replace(".", "/");
			if ((baseDir != null) && !baseDir.isEmpty())
				packPath = baseDir + ((baseDir.endsWith("/") ? "" : "/")) + packPath;
			Path dir = Paths.get(packPath);
			Files.createDirectories(dir);
			return dir.toFile();
		} catch (IOException e) {
			return null;
		}
	}
	

	/**
	 * Generiert den JavaDTO-Klassencode des Parameter Schemas und legt ihn an dem 
	 * spezifiziertem Pfad ab.
	 * 
	 * @param schema     das Schema, für das der Javacode generiert wird 
	 * @param baseDir    das Verzeichnis, in welchem das Package für die DTOs erzeugt werden soll
	 * @param rev        die Revision, für die die DTOs erzeugt werden sollen (-1 für die neueste Revision)
	 * 
	 * @return das {@link File}-Objekt für das Package-Verzeichnis
	 */
	private static File createJavaCode(final DBSchemaDefinition schema, final String baseDir, final long rev) {
		final File packageDir = createPackageDirectory(baseDir, rev);
		if (packageDir == null)
			cmdLine.printOptionsAndExit(2, "Fehler beim Erstellen des Verzeichnisses für das DTO-Package. Korrigieren Sie den Ausgabe-Pfad.");

		// Generiere den Code für die Java DTO-Klasse
		final String dtosClassname = ((rev > 0) ? "Rev" + rev : ((rev == 0) ? "Migration" : "")) + "DTOs";
		final File dtosFile = new File(packageDir, dtosClassname + ".java");
		String codeDTOImports = "";
		String codeMapDTOName2DTOClass = "";
		String codeMapTablename2DTOClass = "";

		// Erzeuge die DTOs für die einzelnen Tabellen
		for (DTOCreatorTable dto : DTOCreatorTable.all) {
			logger.log("Tabelle " + dto.tabelle.Name + ": ");
			String javaPackage = dto.getPackageName(rev);
			if (!dto.tabelle.isDefined(rev)) {
				logger.logLn("---");
				continue;
			}
			try {
				// Erstelle das Verzeichnis für das Package
				String fullqualifiedClassname = javaPackage + "." + dto.tabelle.getJavaKlasse(rev);
				logger.log("-> " + fullqualifiedClassname);
				allClasses.add(fullqualifiedClassname);
				File dir = new File(baseDir + ((baseDir.endsWith("/") ? "" : "/") + javaPackage.replace(".", "/")));
				Files.createDirectories(dir.toPath());
				// Generiere den Code für die Java DTO-Klasse
				File file = new File(dir, dto.tabelle.getJavaKlasse(rev) + ".java");
				String code = dto.getCode(rev);
				try {
					Files.writeString(file.toPath(), code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				} catch (IOException e) {
					cmdLine.printOptionsAndExit(3, "Fehler beim Schreiben des Java-Codes in die Datei " + file.getPath());
				}
				// Erzeuge den Code zum Registrieren der DTO-Klasse in dem Verzeichnis der DTO-Klassen
				codeDTOImports += "import " + javaPackage + "." + dto.tabelle.getJavaKlasse(rev) + ";" + System.lineSeparator();
				codeMapDTOName2DTOClass += "             mapDTOName2DTOClass.put(" + dto.tabelle.getJavaKlasse(rev) + ".class.getSimpleName()," + dto.tabelle.getJavaKlasse(rev) + ".class);" + System.lineSeparator();
				codeMapTablename2DTOClass += "             mapTablename2DTOClass.put(\"" + dto.tabelle.Name + "\"," + dto.tabelle.getJavaKlasse(rev) + ".class);" + System.lineSeparator();
				// Generierere ggf. zusätzliche Code für eine Primary Key - Klasse
				List<TabelleSpalte> pkSpalten = dto.tabelle.primaerschluessel.spalten;
				if ((pkSpalten == null) || (pkSpalten.size() > 1)) {
					logger.log(" (" + dto.tabelle.getJavaKlasse(rev) + "PK)");
					file = new File(dir, dto.tabelle.getJavaKlasse(rev) + "PK.java");
					code = dto.getCode4PrimaryKeyClass(rev);
					try {
						Files.writeString(file.toPath(), code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
					} catch (IOException e) {
						cmdLine.printOptionsAndExit(4, "Fehler beim Schreiben des Java-Codes in die Datei " + file.getPath());
					}
				}
				logger.logLn("");
			} catch (IOException e) {
				cmdLine.printOptionsAndExit(5, "Fehler beim Erstellen des Verzeichnisses für das Package. Korrigieren Sie entweder den Ausgabe-Pfad oder die CSV-Datei.");
				return packageDir;
			}
		}
		
		// Erzeuge die DTOs für die einzelnen Views
		if (rev != 0) {
			long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
			String packagename = DBSchemaDefinition.javaPackage + "." + DBSchemaDefinition.javaDTOPackage + ((rev < 0) ? ".current" : ".rev" + rev) + ".";
			for (View view : DBSchemaViews.getInstance().getViewsActive(revision)) {
				logger.log("View " + view.name + ": ");
				DTOCreatorView creator = new DTOCreatorView(view);
				String javaPackage = packagename + view.packageName; 
				String className = (rev < 0) ? view.dtoName : "Rev" + rev + view.dtoName;
				try {
					// Erstelle das Verzeichnis für das Package
					String fullqualifiedClassname = javaPackage + "." + className;
					logger.log("-> " + fullqualifiedClassname);
					allClasses.add(fullqualifiedClassname);
					File dir = new File(baseDir + ((baseDir.endsWith("/") ? "" : "/") + javaPackage.replace(".", "/")));
					Files.createDirectories(dir.toPath());
					// Generiere den Code für die Java DTO-Klasse
					File file = new File(dir, className + ".java");
					String code = creator.getCode(rev);
					try {
						Files.writeString(file.toPath(), code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
					} catch (IOException e) {
						cmdLine.printOptionsAndExit(3, "Fehler beim Schreiben des Java-Codes in die Datei " + file.getPath());
					}
					// Erzeuge den Code zum Registrieren der DTO-Klasse in dem Verzeichnis der DTO-Klassen
					codeDTOImports += "import " + javaPackage + "." + className + ";" + System.lineSeparator();
					codeMapDTOName2DTOClass += "             mapDTOName2DTOClass.put(" + className + ".class.getSimpleName()," + className + ".class);" + System.lineSeparator();
					codeMapTablename2DTOClass += "             mapTablename2DTOClass.put(\"" + view.name + "\"," + className + ".class);" + System.lineSeparator();
					// Generierere ggf. zusätzliche Code für eine Primary Key - Klasse
					if (!view.hasSimplePrimaryKey()) {
						logger.log(" (" + className + "PK)");
						file = new File(dir, className + "PK.java");
						code = creator.getCode4PrimaryKeyClass(rev);
						try {
							Files.writeString(file.toPath(), code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
						} catch (IOException e) {
							cmdLine.printOptionsAndExit(4, "Fehler beim Schreiben des Java-Codes in die Datei " + file.getPath());
						}
					}				
					logger.logLn("");				
				} catch (IOException e) {
					cmdLine.printOptionsAndExit(5, "Fehler beim Erstellen des Verzeichnisses für das Package. Korrigieren Sie entweder den Ausgabe-Pfad oder die CSV-Datei.");
					return packageDir;
				}
			}
		}
		
		try {
			String dtosCode = "package " + DBSchemaDefinition.javaPackage + "." + DBSchemaDefinition.javaDTOPackage + ";" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "import java.util.HashMap;" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + codeDTOImports
			 + "" + System.lineSeparator()
	         + "/**" + System.lineSeparator()
			 + " * Diese Klasse dient als Verzeichnis aller Datenbank-DTO-Klassen." + System.lineSeparator()
			 + " * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden," + System.lineSeparator()
			 + " * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird." + System.lineSeparator()
			 + " */" + System.lineSeparator()
			 + "public class " + dtosClassname + " {" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /** Enthält das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den entsprechenden Java-DTO-Klassen. */" + System.lineSeparator()
			 + "    private static HashMap<String, Class<? extends Object>> mapDTOName2DTOClass = null;" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /** Enthält das Mapping der Datenbank-Tabellennamen zu den zugehörigen Java-DTO-Klassen für die SVWS-DB. */" + System.lineSeparator()
			 + "    private static HashMap<String, Class<? extends Object>> mapTablename2DTOClass = null;" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /**" + System.lineSeparator()
			 + "     * Gibt das Mapping der Datenbank-Tabellennamen zu den zugehörigen Java-DTO-Klassen für die SVWS-DB zurück." + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @return eine Hashmap mit dem Mapping" + System.lineSeparator() 
			 + "     */" + System.lineSeparator()
			 + "     private static final HashMap<String, Class<? extends Object>> getMapDTOName2DTOClass() {" + System.lineSeparator()
			 + "         if (mapDTOName2DTOClass == null) {" + System.lineSeparator()
		     + "             mapDTOName2DTOClass = new HashMap<>();" + System.lineSeparator()
			 + codeMapDTOName2DTOClass
			 + "         }" + System.lineSeparator()
		     + "         return mapDTOName2DTOClass;" + System.lineSeparator()
			 + "     }" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /**" + System.lineSeparator()
			 + "     * Gibt die DTO-Klasse mit dem angegebenen DTO-Namen zurück." + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @param name   der DTO-Name" + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @return die DTO-Klasse" + System.lineSeparator()
			 + "     */" + System.lineSeparator()
			 + "    public static Class<? extends Object> getFromDTOName(String name) {" + System.lineSeparator()
			 + "    	return getMapDTOName2DTOClass().get(name);" + System.lineSeparator()
			 + "    }" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /**" + System.lineSeparator()
			 + "     * Gibt das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den zugehörigen" + System.lineSeparator()
			 + "     * Java-DTO-Klassen zurück." + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @return eine Hashmap mit dem Mapping" + System.lineSeparator() 
			 + "     */" + System.lineSeparator()
			 + "     private static final HashMap<String, Class<? extends Object>> getMapTablename2DTOClass() {" + System.lineSeparator()
			 + "         if (mapTablename2DTOClass == null) {" + System.lineSeparator()
		     + "             mapTablename2DTOClass = new HashMap<>();" + System.lineSeparator()
			 + codeMapTablename2DTOClass
			 + "         }" + System.lineSeparator()
		     + "         return mapTablename2DTOClass;" + System.lineSeparator()
			 + "     }" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /**" + System.lineSeparator()
			 + "     * Gibt die DTO-Klasse mit dem angegebenen Tabellennamen zurück." + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @param name   der Tabellenname" + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @return die DTO-Klasse" + System.lineSeparator()
			 + "     */" + System.lineSeparator()
			 + "    public static Class<? extends Object> getFromTableName(String name) {" + System.lineSeparator()
			 + "    	return getMapTablename2DTOClass().get(name);" + System.lineSeparator()
			 + "    }" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "}" + System.lineSeparator();
			Files.writeString(dtosFile.toPath(), dtosCode, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			cmdLine.printOptionsAndExit(6, "Fehler beim Schreiben des Java-Codes in die Datei " + dtosFile.getPath());
		}
		
		return packageDir;
	}
	
	
	/**
	 * Schreibt eine Hilfsklasse, um auf die DTOs der Migrationsversion, der aktuellen Revision und der Entwicklerversion
	 * zugreifen zu können.
	 * 
	 * @param packageDir   das {@link File}-Objekt für das DTO-Package-Verzeichnis
	 */
	private static void writeDTOHelper(File packageDir) {
		final File dtosFile = new File(packageDir, "DTOHelper.java");
		try {
			String dtosCode = "package " + DBSchemaDefinition.javaPackage + "." + DBSchemaDefinition.javaDTOPackage + ";" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "import de.nrw.schule.svws.db.schema.SchemaRevisionen;" + System.lineSeparator()
			 + "" + System.lineSeparator()
	         + "/**" + System.lineSeparator()
			 + " * Diese Klasse dient als Hilfsklasse zum Zugriff auf die Datenbank-DTO-Klassen unterschiedlicher Revisionen." + System.lineSeparator()
			 + " * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden," + System.lineSeparator()
			 + " * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird." + System.lineSeparator()
			 + " */" + System.lineSeparator()
			 + "public class DTOHelper {" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /**" + System.lineSeparator()
			 + "     * Gibt die DTO-Klasse mit dem angegebenen DTO-Namen zurück." + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @param name   der DTO-Name" + System.lineSeparator()
			 + "     * @param rev    die Datenbank-Revision für welche die DTO benötigt werden" + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @return die DTO-Klasse" + System.lineSeparator()
			 + "     */" + System.lineSeparator()
			 + "    public static Class<? extends Object> getFromDTOName(String name, long rev) {" + System.lineSeparator()
			 + "        if (rev == 0) {" + System.lineSeparator()
		     + "            return MigrationDTOs.getFromDTOName(name);" + System.lineSeparator()
		     + "        } else if ((rev < 0) || (rev <= SchemaRevisionen.maxRevision.revision)) {" + System.lineSeparator()
    		 + "            return DTOs.getFromDTOName(name);" + System.lineSeparator()
    		 + "        } else if (rev <= SchemaRevisionen.maxDeveloperRevision.revision) {" + System.lineSeparator()
		     + "            return Rev" + SchemaRevisionen.maxDeveloperRevision.revision + "DTOs.getFromDTOName(name);" + System.lineSeparator()
		     + "        } else {" + System.lineSeparator()
		     + "            return null;" + System.lineSeparator()
		     + "        }" + System.lineSeparator()
			 + "    }" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "    /**" + System.lineSeparator()
			 + "     * Gibt die DTO-Klasse mit dem angegebenen Tabellennamen zurück." + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @param name   der Tabellenname" + System.lineSeparator()
			 + "     * @param rev    die Datenbank-Revision für welche die DTO benötigt werden" + System.lineSeparator()
			 + "     *" + System.lineSeparator()
			 + "     * @return die DTO-Klasse" + System.lineSeparator()
			 + "     */" + System.lineSeparator()
			 + "    public static Class<? extends Object> getFromTableName(String name, long rev) {" + System.lineSeparator()
			 + "        if (rev == 0) {" + System.lineSeparator()
		     + "            return MigrationDTOs.getFromTableName(name);" + System.lineSeparator()
		     + "        } else if ((rev < 0) || (rev <= SchemaRevisionen.maxRevision.revision)) {" + System.lineSeparator()
    		 + "            return DTOs.getFromTableName(name);" + System.lineSeparator()
    		 + "        } else if (rev <= SchemaRevisionen.maxDeveloperRevision.revision) {" + System.lineSeparator()
		     + "            return Rev" + SchemaRevisionen.maxDeveloperRevision.revision + "DTOs.getFromTableName(name);" + System.lineSeparator()
		     + "        } else {" + System.lineSeparator()
		     + "            return null;" + System.lineSeparator()
		     + "        }" + System.lineSeparator()
			 + "    }" + System.lineSeparator()
			 + "" + System.lineSeparator()
			 + "}" + System.lineSeparator();
			Files.writeString(dtosFile.toPath(), dtosCode, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			cmdLine.printOptionsAndExit(6, "Fehler beim Schreiben des Java-Codes in die Datei " + dtosFile.getPath());
		}
	}
	
	
	private static void createPersistenceXml(String pathxml) {
		Path p = Paths.get(pathxml);
		String xml = """
                     <?xml version="1.0" encoding="UTF-8"?>
                     <persistence version="2.1"
                         xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
                         <persistence-unit name="SVWSDB" transaction-type="RESOURCE_LOCAL">
                             <class>de.nrw.schule.svws.db.converter.BenutzerKompetenzConverter</class>
                             <class>de.nrw.schule.svws.db.converter.Boolean01Converter</class>
                             <class>de.nrw.schule.svws.db.converter.Boolean01StringConverter</class>
                             <class>de.nrw.schule.svws.db.converter.BooleanPlusMinusConverter</class>
                             <class>de.nrw.schule.svws.db.converter.DatumConverter</class>
                             <class>de.nrw.schule.svws.db.converter.EigeneSchuleEinstellungenConverter</class>
                             <class>de.nrw.schule.svws.db.converter.GeschlechtConverter</class>
                             <class>de.nrw.schule.svws.db.converter.GeschlechtConverterFromString</class>
                             <class>de.nrw.schule.svws.db.converter.KursFortschreibungsartConverter</class>
                             <class>de.nrw.schule.svws.db.converter.NoteConverterFromInteger</class>
                             <class>de.nrw.schule.svws.db.converter.NoteConverterFromKuerzel</class>
                             <class>de.nrw.schule.svws.db.converter.NoteConverterFromNotenpunkte</class>
                             <class>de.nrw.schule.svws.db.converter.NoteConverterFromNotenpunkteString</class>
                             <class>de.nrw.schule.svws.db.converter.PersonalTypConverter</class>
                             <class>de.nrw.schule.svws.db.converter.SchuelerStatusConverter</class>
                             <class>de.nrw.schule.svws.db.converter.StringToIntegerConverter</class>
                             <class>de.nrw.schule.svws.db.converter.gost.AbiturBelegungsartConverter</class>
                             <class>de.nrw.schule.svws.db.converter.gost.AbiturKursMarkierungConverter</class>
                             <class>de.nrw.schule.svws.db.converter.gost.GOStAbiturFachConverter</class>
                             <class>de.nrw.schule.svws.db.converter.gost.GOStBesondereLernleistungConverter</class>
                             <class>de.nrw.schule.svws.db.converter.gost.GOStKursartConverter</class>
                             <class>de.nrw.schule.svws.db.utils.dto.enm.DTOENMLehrerSchuelerAbschnittsdaten</class>
                     """;
		for (String cl : allClasses)
			xml += "        <class>" + cl + "</class>" + System.lineSeparator();
		xml += """
                        <exclude-unlisted-classes>false</exclude-unlisted-classes>
                    </persistence-unit>
               </persistence>
               """;
		try {
			Files.writeString(p, xml, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			cmdLine.printOptionsAndExit(6, "Fehler beim Schreiben der persistence.xml in die Datei " + pathxml);
		}
	}
	
	
	/**
	 * Liest den Pfad zu dem Ordner ein, in dem der generierte Code abgelegt wird. Anschließend wird die 
	 * Java-DTO-Codegenerierung angestoßen.
	 *   
	 * @param args  die Optionen für die Codegenerierung, @see options
	 */
	public static void main(String[] args) {
		logger.addConsumer(logConsumerConsole);

		// Gib das aktuelle Verzeichnis aus
		logger.logLn("Aktuelles Verzeichnis: " + Paths.get("").toAbsolutePath().toString());
		
		cmdLine = new CommandLineParser(args);
		try {
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Source-Ordner, wo die Java-DTO-Klassen abgelegt werden"));
			cmdLine.addOption(new CommandLineOption("p", "persistence", true, "Der Dateiname der persistence.xml-Datei"));
			String path = cmdLine.getValue("o", "../svws-db-dto/src/main/java");
			String pathxml = cmdLine.getValue("p", "src/main/resources/META-INF/persistence.xml");

			DTOCreatorTable.addLogConsumer(logConsumerConsole);
			DTOCreatorTable.init();
			
			logger.logLn("Erzeuge DTO-Klassen für die Revision 0, d.h. für die Migration alter Datenbanken...");
			logger.modifyIndent(2);
			createJavaCode(DBSchemaDefinition.getInstance(), path, 0);
			logger.modifyIndent(-2);
			
			logger.logLn("Erzeuge DTO-Klassen für die neueste Revision, d.h. für den normalen SVWS-Server-Betrieb...");
			logger.modifyIndent(2);
			File packageDir = createJavaCode(DBSchemaDefinition.getInstance(), path, -1);
			logger.modifyIndent(-2);
			
			logger.logLn("Erzeuge DTO-Klassen für die aktuelle Entwickler-Revision, d.h. für den experimentellen SVWS-Server-Betrieb...");
			logger.modifyIndent(2);
			createJavaCode(DBSchemaDefinition.getInstance(), path, SchemaRevisionen.maxDeveloperRevision.revision);
			logger.modifyIndent(-2);

			logger.logLn("Erzeuge persistence.xml, so dass named queries verwendet werden können...");
			logger.modifyIndent(2);
			createPersistenceXml(pathxml);
			logger.modifyIndent(-2);
			
			writeDTOHelper(packageDir);
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}
	}

}
