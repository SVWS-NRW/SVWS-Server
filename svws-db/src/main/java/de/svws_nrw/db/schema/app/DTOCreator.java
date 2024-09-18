package de.svws_nrw.db.schema.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.schema.DBSchemaViews;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.View;



/**
 * Diese Klasse generiert den Java-Code für die JavaDTO-Klassen entsprechend eines spezifierten Datenbankschemas.
 */
public class DTOCreator {

	/** Der Parser für die Kommandozeile */
	private static CommandLineParser cmdLine;

	/** Der intern genutzte Logger */
	private static final Logger logger = new Logger();

	/** Die Konsole als Ziel der Log-Informationen */
	private static final LogConsumerConsole logConsumerConsole = new LogConsumerConsole();

	/** Ein ArrayList mit allen generierten DTO-Klassen (voll qualifizierte Klassennamen) */
	private static final ArrayList<String> allClasses = new ArrayList<>();


	/**
	 * Erstellt das Verzeichnis für die DTO-Packages
	 *
	 * @param baseDir   das Verzeichnis, in welchem das Package für die DTOs erzeugt werden soll
	 *
	 * @return das {@link File}-Objekt, falls das Verzeichnis erstellt wurde, im Fehlerfall null
	 */
	private static File createPackageDirectory(final String baseDir) {
		try {
			// Erstelle das Verzeichnis für das Package
			final String pack = Schema.javaPackage + "." + Schema.javaDTOPackage;
			logger.logLn("Erzeuge Package " + pack);
			String packPath = pack.replace(".", "/");
			if ((baseDir != null) && !baseDir.isEmpty())
				packPath = baseDir + (baseDir.endsWith("/") ? "" : "/") + packPath;
			final Path dir = Paths.get(packPath);
			Files.createDirectories(dir);
			return dir.toFile();
		} catch (@SuppressWarnings("unused") final IOException e) {
			return null;
		}
	}


	/**
	 * Erstellt oder ersetzt die angegebene Datei mit den Daten aus dem übergebenen String
	 *
	 * @param file   die Datei
	 * @param data   die Daten als String
	 */
	private static void createOrReplaceFile(final File file, final String data) {
		try {
			Files.writeString(file.toPath(), data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (@SuppressWarnings("unused") final IOException e) {
			cmdLine.printOptionsAndExit(1, "Fehler beim Erstellen der Datei " + file.getName() + " (" + file.getPath() + ")");
		}
	}



	/**
	 * Generiert den JavaDTO-Klassencode für die übergebene Tabelle und legt ihn an dem
	 * spezifiziertem Pfad ab.
	 *
	 * @param baseDir                     das Verzeichnis, in welchem das Package für die DTOs erzeugt werden soll
	 * @param rev                         die Revision, für die die DTOs erzeugt werden sollen (-1 für die neueste Revision)
	 * @param dto                         die Tabelle, für welche der Code erzeugt wird
	 * @param codeDTOImports              der bisherige Code für die DTO-Importe (zum Anhängen)
	 * @param codeMapDTOName2DTOClass     der bisherige Code für die Map DTO-Name zu DTO-Klasse (zum Anhängen)
	 * @param codeMapTablename2DTOClass   der bisherige Code für die Map DTO-Tabellenname zu DTO-Klasse (zum Anhängen)
	 *
	 * @return true im Erfolgsfall und ansonsten false
	 */
	private static boolean createJavaCodeForTable(final String baseDir, final long rev, final DTOCreatorTable dto,
			final StringBuilder codeDTOImports, final StringBuilder codeMapDTOName2DTOClass, final StringBuilder codeMapTablename2DTOClass) {
		logger.log("Tabelle " + dto.tabelle.name() + ": ");
		final String javaPackage = dto.getPackageName(rev);
		if (!dto.tabelle.isDefined(rev)) {
			logger.logLn("---");
			return true;
		}
		if ((rev == SchemaRevisionen.maxRevision.revision) || ((rev > SchemaRevisionen.maxRevision.revision) && (!dto.tabelle.brauchtDeveloperDTO()))) {
			logger.logLn("- nicht benötigt");
			return true;
		}
		try {
			// Erstelle das Verzeichnis für das Package
			final String fullqualifiedClassname = javaPackage + "." + dto.tabelle.getJavaKlasse(rev);
			logger.log("-> " + fullqualifiedClassname);
			allClasses.add(fullqualifiedClassname);
			final File dir = new File(baseDir + ((baseDir.endsWith("/") ? "" : "/") + javaPackage.replace(".", "/")));
			Files.createDirectories(dir.toPath());
			// Generiere den Code für die Java DTO-Klasse
			File file = new File(dir, dto.tabelle.getJavaKlasse(rev) + ".java");
			String code = dto.getCode(rev);
			createOrReplaceFile(file, code);
			// Erzeuge den Code zum Registrieren der DTO-Klasse in dem Verzeichnis der DTO-Klassen
			codeDTOImports.append("import ").append(javaPackage).append(".").append(dto.tabelle.getJavaKlasse(rev)).append(";")
					.append(System.lineSeparator());
			codeMapDTOName2DTOClass.append("             mapDTOName2DTOClass.put(").append(dto.tabelle.getJavaKlasse(rev))
					.append(".class.getSimpleName(), ").append(dto.tabelle.getJavaKlasse(rev)).append(".class);").append(System.lineSeparator());
			codeMapTablename2DTOClass.append("             mapTablename2DTOClass.put(\"").append(dto.tabelle.name()).append("\", ")
					.append(dto.tabelle.getJavaKlasse(rev)).append(".class);").append(System.lineSeparator());
			// Generierere ggf. zusätzliche Code für eine Primary Key - Klasse
			if ((dto.tabelle.pkSpalten() == null) || (dto.tabelle.pkSpalten().size() != 1)) {
				logger.log(" (" + dto.tabelle.getJavaKlasse(rev) + "PK)");
				file = new File(dir, dto.tabelle.getJavaKlasse(rev) + "PK.java");
				code = dto.getCode4PrimaryKeyClass(rev);
				createOrReplaceFile(file, code);
			}
			logger.logLn("");
			return true;
		} catch (@SuppressWarnings("unused") final IOException e) {
			cmdLine.printOptionsAndExit(5, "Fehler beim Erstellen des Verzeichnisses für das Package. Korrigieren Sie entweder den Ausgabe-Pfad"
					+ " oder die CSV-Datei.");
			return false;
		}
	}


	/**
	 * Generiert den JavaDTO-Klassencode für die übergebene View und legt ihn an dem
	 * spezifiziertem Pfad ab.
	 *
	 * @param baseDir                     das Verzeichnis, in welchem das Package für die DTOs erzeugt werden soll
	 * @param rev                         die Revision, für die die DTOs erzeugt werden sollen (-1 für die neueste Revision)
	 * @param view                        die View, für welche der Code erzeugt wird
	 * @param codeDTOImports              der bisherige Code für die DTO-Importe (zum Anhängen)
	 * @param codeMapDTOName2DTOClass     der bisherige Code für die Map DTO-Name zu DTO-Klasse (zum Anhängen)
	 * @param codeMapTablename2DTOClass   der bisherige Code für die Map DTO-Tabellenname zu DTO-Klasse (zum Anhängen)
	 *
	 * @return true im Erfolgsfall und ansonsten false
	 */
	private static boolean createJavaCodeForView(final String baseDir, final long rev, final View view,
			final StringBuilder codeDTOImports, final StringBuilder codeMapDTOName2DTOClass, final StringBuilder codeMapTablename2DTOClass) {
		final String packagename = Schema.javaPackage + "." + Schema.javaDTOPackage + ((rev < 0) ? ".current" : ".dev") + ".";
		logger.log("View " + view.name + ": ");
		if ((rev == SchemaRevisionen.maxRevision.revision) || ((rev > SchemaRevisionen.maxRevision.revision) && (!view.brauchtDeveloperDTO()))) {
			logger.logLn("- nicht benötigt");
			return true;
		}
		final DTOCreatorView creator = new DTOCreatorView(view);
		final String javaPackage = packagename + view.packageName;
		final String className = (rev < 0) ? view.dtoName : ("Dev" + view.dtoName);
		try {
			// Erstelle das Verzeichnis für das Package
			final String fullqualifiedClassname = javaPackage + "." + className;
			logger.log("-> " + fullqualifiedClassname);
			allClasses.add(fullqualifiedClassname);
			final File dir = new File(baseDir + ((baseDir.endsWith("/") ? "" : "/") + javaPackage.replace(".", "/")));
			Files.createDirectories(dir.toPath());
			// Generiere den Code für die Java DTO-Klasse
			File file = new File(dir, className + ".java");
			String code = creator.getCode(rev);
			createOrReplaceFile(file, code);
			// Erzeuge den Code zum Registrieren der DTO-Klasse in dem Verzeichnis der DTO-Klassen
			codeDTOImports.append("import ").append(javaPackage).append(".").append(className).append(";").append(System.lineSeparator());
			codeMapDTOName2DTOClass.append("             mapDTOName2DTOClass.put(").append(className).append(".class.getSimpleName(), ")
					.append(className).append(".class);").append(System.lineSeparator());
			codeMapTablename2DTOClass.append("             mapTablename2DTOClass.put(\"").append(view.name).append("\", ").append(className)
					.append(".class);").append(System.lineSeparator());
			// Generierere ggf. zusätzliche Code für eine Primary Key - Klasse
			if (!view.hasSimplePrimaryKey()) {
				logger.log(" (" + className + "PK)");
				file = new File(dir, className + "PK.java");
				code = creator.getCode4PrimaryKeyClass(rev);
				createOrReplaceFile(file, code);
			}
			logger.logLn("");
			return true;
		} catch (@SuppressWarnings("unused") final IOException e) {
			cmdLine.printOptionsAndExit(5, "Fehler beim Erstellen des Verzeichnisses für das Package. Korrigieren Sie entweder den Ausgabe-Pfad"
					+ " oder die CSV-Datei.");
			return false;
		}
	}


	/**
	 * Generiert den JavaDTO-Klassencode des Parameter Schemas und legt ihn an dem
	 * spezifiziertem Pfad ab.
	 *
	 * @param baseDir    das Verzeichnis, in welchem das Package für die DTOs erzeugt werden soll
	 * @param rev        die Revision, für die die DTOs erzeugt werden sollen (-1 für die neueste Revision)
	 *
	 * @return das {@link File}-Objekt für das Package-Verzeichnis
	 */
	private static File createJavaCode(final String baseDir, final long rev) {
		final File packageDir = createPackageDirectory(baseDir);
		if (packageDir == null)
			cmdLine.printOptionsAndExit(2, "Fehler beim Erstellen des Verzeichnisses für das DTO-Package. Korrigieren Sie den Ausgabe-Pfad.");

		// Generiere den Code für die Java DTO-Klasse
		String dtosClassname = "DTOs";
		if (rev > 0)
			dtosClassname = "DevDTOs";
		else if (rev == 0)
			dtosClassname = "MigrationDTOs";
		final File dtosFile = new File(packageDir, dtosClassname + ".java");
		final StringBuilder codeDTOImports = new StringBuilder();
		final StringBuilder codeMapDTOName2DTOClass = new StringBuilder();
		final StringBuilder codeMapTablename2DTOClass = new StringBuilder();

		// Erzeuge die DTOs für die einzelnen Tabellen
		for (final DTOCreatorTable dto : DTOCreatorTable.all)
			if (!createJavaCodeForTable(baseDir, rev, dto, codeDTOImports, codeMapDTOName2DTOClass, codeMapTablename2DTOClass))
				return packageDir;

		// Erzeuge die DTOs für die einzelnen Views
		if (rev != 0) {
			final long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
			for (final View view : DBSchemaViews.getInstance().getViewsActive(revision))
				if (!createJavaCodeForView(baseDir, rev, view, codeDTOImports, codeMapDTOName2DTOClass, codeMapTablename2DTOClass))
					return packageDir;
		}

		final String dtosCode = "package " + Schema.javaPackage + "." + Schema.javaDTOPackage + ";" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ "import java.util.HashMap;" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ codeDTOImports.toString()
				+ "" + System.lineSeparator()
				+ "/**" + System.lineSeparator()
				+ " * Diese Klasse dient als Verzeichnis aller Datenbank-DTO-Klassen." + System.lineSeparator()
				+ " * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden," + System.lineSeparator()
				+ " * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird." + System.lineSeparator()
				+ " */" + System.lineSeparator()
				+ "public final class " + dtosClassname + " {" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ "    /** Enthält das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den entsprechenden Java-DTO-Klassen. */"
				+ System.lineSeparator()
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
				+ "     private static HashMap<String, Class<? extends Object>> getMapDTOName2DTOClass() {" + System.lineSeparator()
				+ "         if (mapDTOName2DTOClass == null) {" + System.lineSeparator()
				+ "             mapDTOName2DTOClass = new HashMap<>();" + System.lineSeparator()
				+ codeMapDTOName2DTOClass.toString()
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
				+ "    public static Class<? extends Object> getFromDTOName(final String name) {" + System.lineSeparator()
				+ "        return getMapDTOName2DTOClass().get(name);" + System.lineSeparator()
				+ "    }" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ "    /**" + System.lineSeparator()
				+ "     * Gibt das Mapping der Namen aller Java-DTO-Klassen für die SVWS-DB zu den zugehörigen" + System.lineSeparator()
				+ "     * Java-DTO-Klassen zurück." + System.lineSeparator()
				+ "     *" + System.lineSeparator()
				+ "     * @return eine Hashmap mit dem Mapping" + System.lineSeparator()
				+ "     */" + System.lineSeparator()
				+ "     private static HashMap<String, Class<? extends Object>> getMapTablename2DTOClass() {" + System.lineSeparator()
				+ "         if (mapTablename2DTOClass == null) {" + System.lineSeparator()
				+ "             mapTablename2DTOClass = new HashMap<>();" + System.lineSeparator()
				+ codeMapTablename2DTOClass.toString()
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
				+ "    public static Class<? extends Object> getFromTableName(final String name) {" + System.lineSeparator()
				+ "        return getMapTablename2DTOClass().get(name);" + System.lineSeparator()
				+ "    }" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ "}" + System.lineSeparator();
		createOrReplaceFile(dtosFile, dtosCode);

		return packageDir;
	}


	/**
	 * Schreibt eine Hilfsklasse, um auf die DTOs der Migrationsversion, der aktuellen Revision und der Entwicklerversion
	 * zugreifen zu können.
	 *
	 * @param packageDir   das {@link File}-Objekt für das DTO-Package-Verzeichnis
	 */
	private static void writeDTOHelper(final File packageDir) {
		final File dtosFile = new File(packageDir, "DTOHelper.java");
		final String dtosCode = "package " + Schema.javaPackage + "." + Schema.javaDTOPackage + ";" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ "import de.svws_nrw.db.schema.SchemaRevisionen;" + System.lineSeparator()
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
				+ "    public static Class<? extends Object> getFromDTOName(final String name, final long rev) {" + System.lineSeparator()
				+ "        if (rev == 0) {" + System.lineSeparator()
				+ "            return MigrationDTOs.getFromDTOName(name);" + System.lineSeparator()
				+ "        } else if ((rev < 0) || (rev <= SchemaRevisionen.maxRevision.revision)) {" + System.lineSeparator()
				+ "            return DTOs.getFromDTOName(name);" + System.lineSeparator()
				+ "        } else if (rev <= SchemaRevisionen.maxDeveloperRevision.revision) {" + System.lineSeparator()
				+ "            return DevDTOs.getFromDTOName(name);" + System.lineSeparator()
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
				+ "    public static Class<? extends Object> getFromTableName(final String name, final long rev) {" + System.lineSeparator()
				+ "        if (rev == 0) {" + System.lineSeparator()
				+ "            return MigrationDTOs.getFromTableName(name);" + System.lineSeparator()
				+ "        } else if ((rev < 0) || (rev <= SchemaRevisionen.maxRevision.revision)) {" + System.lineSeparator()
				+ "            return DTOs.getFromTableName(name);" + System.lineSeparator()
				+ "        } else if (rev <= SchemaRevisionen.maxDeveloperRevision.revision) {" + System.lineSeparator()
				+ "            return DevDTOs.getFromTableName(name);" + System.lineSeparator()
				+ "        } else {" + System.lineSeparator()
				+ "            return null;" + System.lineSeparator()
				+ "        }" + System.lineSeparator()
				+ "    }" + System.lineSeparator()
				+ "" + System.lineSeparator()
				+ "}" + System.lineSeparator();
		createOrReplaceFile(dtosFile, dtosCode);
	}


	private static void createPersistenceXml(final String pathxml) {
		final Path p = Paths.get(pathxml);
		final StringBuilder sb = new StringBuilder();
		sb.append("""
                  <?xml version="1.0" encoding="UTF-8"?>
                  <persistence version="2.1"
                      xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
                      <persistence-unit name="SVWSDB" transaction-type="RESOURCE_LOCAL">
                          <class>de.svws_nrw.db.converter.migration.MigrationBoolean01Converter</class>
                          <class>de.svws_nrw.db.converter.migration.MigrationBoolean01StringConverter</class>
                          <class>de.svws_nrw.db.converter.migration.MigrationBooleanJNConverter</class>
                          <class>de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusConverter</class>
                          <class>de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter</class>
                          <class>de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter</class>
                          <class>de.svws_nrw.db.converter.migration.MigrationDatumConverter</class>
                          <class>de.svws_nrw.db.converter.migration.MigrationStringToIntegerConverter</class>
                          <class>de.svws_nrw.db.converter.current.BenutzerKompetenzConverter</class>
                          <class>de.svws_nrw.db.converter.current.BenutzerTypConverter</class>
                          <class>de.svws_nrw.db.converter.current.Boolean01Converter</class>
                          <class>de.svws_nrw.db.converter.current.Boolean01StringConverter</class>
                          <class>de.svws_nrw.db.converter.current.BooleanJNConverter</class>
                          <class>de.svws_nrw.db.converter.current.BooleanPlusMinusConverter</class>
                          <class>de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter</class>
                          <class>de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter</class>
                          <class>de.svws_nrw.db.converter.current.DatumConverter</class>
                          <class>de.svws_nrw.db.converter.current.DatumUhrzeitConverter</class>
                          <class>de.svws_nrw.db.converter.current.DavRessourceCollectionTypConverter</class>
                          <class>de.svws_nrw.db.converter.current.GeschlechtConverter</class>
                          <class>de.svws_nrw.db.converter.current.GeschlechtConverterFromString</class>
                          <class>de.svws_nrw.db.converter.current.KursFortschreibungsartConverter</class>
                          <class>de.svws_nrw.db.converter.current.NationalitaetenConverter</class>
                          <class>de.svws_nrw.db.converter.current.PersonTypConverter</class>
                          <class>de.svws_nrw.db.converter.current.PersonTypNullableConverter</class>
                          <class>de.svws_nrw.db.converter.current.PersonalTypConverter</class>
                          <class>de.svws_nrw.db.converter.current.SprachpruefungniveauConverter</class>
                          <class>de.svws_nrw.db.converter.current.StringToIntegerConverter</class>
                          <class>de.svws_nrw.db.converter.current.UhrzeitConverter</class>
                          <class>de.svws_nrw.db.converter.current.UhrzeitConverterString</class>
                          <class>de.svws_nrw.db.converter.current.VerkehrssprachenConverter</class>
                          <class>de.svws_nrw.db.converter.current.gost.AbiturBelegungsartConverter</class>
                          <class>de.svws_nrw.db.converter.current.gost.AbiturKursMarkierungConverter</class>
                          <class>de.svws_nrw.db.converter.current.gost.GOStAbiturFachConverter</class>
                          <class>de.svws_nrw.db.converter.current.gost.GOStBesondereLernleistungConverter</class>
                          <class>de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter</class>
                          <class>de.svws_nrw.db.converter.current.gost.GOStKursartConverter</class>
                          <class>de.svws_nrw.db.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverter</class>
                          <class>de.svws_nrw.db.converter.current.kursblockung.GostKursblockungRegelTypConverter</class>
                          <class>de.svws_nrw.db.utils.dto.enm.DTOENMLehrerSchuelerAbschnittsdaten</class>
                  """);
		for (final String cl : allClasses)
			sb.append("        <class>" + cl + "</class>" + System.lineSeparator());
		sb.append("""
                           <exclude-unlisted-classes>false</exclude-unlisted-classes>
                       </persistence-unit>
                  </persistence>
                  """);
		createOrReplaceFile(p.toFile(), sb.toString());
	}


	/**
	 * Liest den Pfad zu dem Ordner ein, in dem der generierte Code abgelegt wird. Anschließend wird die
	 * Java-DTO-Codegenerierung angestoßen.
	 *
	 * @param args  die Optionen für die Codegenerierung, @see options
	 */
	public static void main(final String[] args) {
		logger.addConsumer(logConsumerConsole);

		// Initialisieren der Core-Types
		logger.logLn("Initialisiere...");
		ASDCoreTypeUtils.initAll();

		// Gib das aktuelle Verzeichnis aus
		logger.logLn("Aktuelles Verzeichnis: " + Paths.get("").toAbsolutePath().toString());

		cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Source-Ordner, wo die Java-DTO-Klassen abgelegt werden"));
			cmdLine.addOption(new CommandLineOption("p", "persistence", true, "Der Dateiname der persistence.xml-Datei"));
			final String path = cmdLine.getValue("o", "../svws-db-dto/src/main/java");
			final String pathxml = cmdLine.getValue("p", "src/main/resources/META-INF/persistence.xml");

			DTOCreatorTable.addLogConsumer(logConsumerConsole);
			DTOCreatorTable.init();

			logger.logLn("Erzeuge DTO-Klassen für die Revision 0, d.h. für die Migration alter Datenbanken...");
			logger.modifyIndent(2);
			createJavaCode(path, 0);
			logger.modifyIndent(-2);

			logger.logLn("Erzeuge DTO-Klassen für die neueste Revision, d.h. für den normalen SVWS-Server-Betrieb...");
			logger.modifyIndent(2);
			final File packageDir = createJavaCode(path, -1);
			logger.modifyIndent(-2);

			logger.logLn("Erzeuge DTO-Klassen für die aktuelle Entwickler-Revision, d.h. für den experimentellen SVWS-Server-Betrieb...");
			logger.modifyIndent(2);
			createJavaCode(path, SchemaRevisionen.maxDeveloperRevision.revision);
			logger.modifyIndent(-2);

			logger.logLn("Erzeuge persistence.xml, so dass named queries verwendet werden können...");
			logger.modifyIndent(2);
			createPersistenceXml(pathxml);
			logger.modifyIndent(-2);

			writeDTOHelper(packageDir);
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}
	}

}
