package de.svws_nrw.db.schema.app;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogData;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.converter.DBAttributeConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;


/**
 * Diese Klasse stellt Methoden zum Erstellen des Java Quellcodes
 * für eine DTO-Klasse zum Zugriff auf eine Tabelle der SVWS-Datenbank
 * zur Verfügung.
 */
public final class DTOCreatorTable {

	/** Der intern genutzte Logger für das Erzeugen des Quellcodes */
	private static final Logger logger = new Logger();

	/** Enthält alle definierten Objekt dieser Klasse */
	protected static final List<DTOCreatorTable> all = new ArrayList<>();


	/** Die Tabelle für die der Java-Code erzeugt werden soll */
	public final SchemaTabelle tabelle;


	/**
	 * Erzeugt ein neues Objekt der Klasse DTOCreatorTable.
	 *
	 * @param tabelle    die Tabelle, für die der Java-Code erstellt werden soll.
	 */
	private DTOCreatorTable(final SchemaTabelle tabelle) {
		this.tabelle = tabelle;
	}


	/**
	 * Fügt einen Consumer für das Logging beim Erstellen des Java-Quellcodes
	 * zum Logger hinzu. <br>
	 * Siehe auch {@link LogConsumerList} und {@link LogConsumerConsole}.
	 *
	 * @param consumer   der hinzuzufügende Log-Consumer
	 */
	public static void addLogConsumer(final Consumer<LogData> consumer) {
		logger.addConsumer(consumer);
	}


	/**
	 * Initialisiert die interne Liste mit dem Quellcode sämtlicher
	 * Java-Code-Klassen in Bezug auf die einzelnen Revisionen.
	 */
	public static void init() {
		if (all.isEmpty()) {
			for (final SchemaTabelle tab : Schema.tabellen().stream().sorted((a, b) -> {
				final String a_cmp = a.javaSubPackage() + "." + a.javaClassName();
				final String b_cmp = b.javaSubPackage() + "." + b.javaClassName();
				return a_cmp.compareTo(b_cmp);
			}).toList()) {
				if (tab == null) {
					logger.log("Tabellen-Definition fehlerhaft!");
					return;
				}
				if (tab.getJavaKlasse(-1) == null) {
					logger.log("Tabelle " + tab.name() + " wird nicht nach Java übernommen. ");
					return;
				}
				logger.modifyIndent(2);
				final DTOCreatorTable dtoTable = new DTOCreatorTable(tab);
				all.add(dtoTable);
				logger.modifyIndent(-2);
			}
		}
	}


	/**
	 * Gibt den Package-Namen für diese Tabelle zurück.
	 *
	 * @param rev   die Revision des Datenbankschemas, für welche die Tabelle erzeugt wird.
	 *
	 * @return der Package-Name
	 */
	public String getPackageName(final long rev) {
		String packagename = Schema.javaPackage + "." + Schema.javaDTOPackage;
		if (rev < 0) {
			packagename += ".current." + tabelle.javaSubPackage();
		} else if (rev == 0) {
			packagename += ".migration." + tabelle.javaSubPackage();
		} else {
			packagename += ".dev." + tabelle.javaSubPackage();
		}
		return packagename;
	}


	/**
	 * Gibt den Java-Attributnamen für die angegebene Spalte zurück.
	 *
	 * @param spalte  die Spalte
	 *
	 * @return der Java-Attributname
	 */
	private static String getJavaAttributeName(final SchemaTabelleSpalte spalte) {
		if (spalte.javaAttributName() != null)
			return spalte.javaAttributName().startsWith("-") ? null : spalte.javaAttributName();
		return spalte.name();
	}


	/**
	 * Gibt den Java-Datentyp der angegeben Spalte zurück. Hierbei wird ein Converter berücksichtigt.
	 *
	 * @param spalte   die Datenbankspalte
	 * @param rev      die Revision, um den Converter bestimmen zu können
	 *
	 * @return der Java-Datentyp
	 */
	private static String getDataType(final SchemaTabelleSpalte spalte, final long rev) {
		String dataType = spalte.datentyp().java((rev != 0) && spalte.notNull());
		final DBAttributeConverter<?, ?> attrConverter = spalte.javaConverter(rev);
		if (attrConverter != null)
			dataType = attrConverter.getResultType().getSimpleName();
		return dataType;
	}



	/**
	 * Generiert die Equals und HashCode-Methoden für die DTO-Klasse.
	 *
	 * @param classname   der Name der DTO-Klasse
	 * @param pkspalten   die Primärschlüsselattribute für die DTO-Klassen
	 * @param rev         die Revision, für welche die DTO-Klasse erstellt wird
	 *
	 * @return der Java-Code für die beiden Methoden
	 */
	private static String getCode4EqualsAndHashcode(final String classname, final Collection<SchemaTabelleSpalte> pkspalten, final long rev) {
		final StringBuilder sb = new StringBuilder();
		sb.append("\t@Override" + System.lineSeparator());
		sb.append("\tpublic boolean equals(final Object obj) {" + System.lineSeparator());
		sb.append("\t\tif (this == obj)" + System.lineSeparator());
		sb.append("\t\t\treturn true;" + System.lineSeparator());
		sb.append("\t\tif (obj == null)" + System.lineSeparator());
		sb.append("\t\t\treturn false;" + System.lineSeparator());
		sb.append("\t\tif (getClass() != obj.getClass())" + System.lineSeparator());
		sb.append("\t\t\treturn false;" + System.lineSeparator());
		sb.append("\t\t" + classname + " other = (" + classname + ") obj;" + System.lineSeparator());
		int i = 0;
		boolean combinedReturn = false;
		for (final SchemaTabelleSpalte col : pkspalten) {
			final String colname = getJavaAttributeName(col);
			if (colname == null) {
				i++;
				continue;
			}
			if (col.datentyp().isJavaPrimitiveType((rev != 0) && col.notNull())) {
				if (i == (pkspalten.size() - 1)) {
					sb.append("\t\treturn " + colname + " == other." + colname + ";" + System.lineSeparator());
					combinedReturn = true;
				} else {
					sb.append("\t\tif (" + colname + " != other." + colname + ")" + System.lineSeparator());
					sb.append("\t\t\treturn false;" + System.lineSeparator());
				}
			} else {
				sb.append("\t\tif (" + colname + " == null) {" + System.lineSeparator());
				sb.append("\t\t\tif (other." + colname + " != null)" + System.lineSeparator());
				sb.append("\t\t\t\treturn false;" + System.lineSeparator());
				sb.append("\t\t} else if (!" + colname + ".equals(other." + colname + "))" + System.lineSeparator());
				sb.append("\t\t\treturn false;" + System.lineSeparator());
			}
			i++;
		}
		if (!combinedReturn)
			sb.append("\t\treturn true;" + System.lineSeparator());
		sb.append("\t}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\t@Override" + System.lineSeparator());
		sb.append("\tpublic int hashCode() {" + System.lineSeparator());
		sb.append("\t\tfinal int prime = 31;" + System.lineSeparator());
		sb.append("\t\tint result = 1;" + System.lineSeparator());
		sb.append(pkspalten.stream().map(col -> {
			final String colname = getJavaAttributeName(col);
			if (colname == null)
				return null;
			if (col.datentyp().isJavaPrimitiveType((rev != 0) && col.notNull()))
				return "\t\tresult = prime * result + " + col.datentyp().java(false) + ".hashCode(" + colname + ");" + System.lineSeparator();
			return "\t\tresult = prime * result + ((" + colname + " == null) ? 0 : " + colname + ".hashCode());" + System.lineSeparator();
		})
				.filter(Objects::nonNull)
				.collect(Collectors.joining(System.lineSeparator())));
		sb.append("\t\treturn result;" + System.lineSeparator());
		sb.append("\t}" + System.lineSeparator());
		return sb.toString();
	}



	/**
	 * Ermittelt die Attribut-Konverter {@link DBAttributeConverter}, die für diese Tabelle definiert wurden.
	 *
	 * @param rev   die DB-Revision, für welche die Attribut-Konverter bestimmt werden sollen
	 *
	 * @return eine Liste mit den Attribut-Konvertern.
	 */
	private List<DBAttributeConverter<?, ?>> getAttributeConverter(final long rev) {
		return tabelle.getSpalten(rev).stream()
				.map(spalte -> spalte.javaConverter(rev))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}


	/**
	 * Generiert den Code für den Import der übergebenen Attribut-Konverter {@link DBAttributeConverter}.
	 *
	 * @param acs   eine Liste von Attribut-Konvertern
	 *
	 * @return der Java-Code für den Import der Attribut-Konverter
	 */
	private static String getCodeImportConverter(final List<DBAttributeConverter<?, ?>> acs) {
		if (acs.isEmpty())
			return "";
		String result = "import "
				+ acs.stream().map(ac -> ac.getClass().getName()).filter(Objects::nonNull).sorted().distinct()
						.collect(Collectors.joining(";" + System.lineSeparator() + "import "))
				+ ";" + System.lineSeparator()
				+ System.lineSeparator();
		final String resultTypeImports = acs.stream().map(ac -> ac.getResultType().getName())
				.filter(Objects::nonNull).filter(cntt -> !cntt.startsWith("java.lang")).sorted().distinct()
				.collect(Collectors.joining(";" + System.lineSeparator() + "import "));
		if (!"".equals(resultTypeImports))
			result += "import "
					+ resultTypeImports
					+ ";" + System.lineSeparator()
					+ System.lineSeparator();
		return result;
	}



	/**
	 * Generiert den Code für einen Query-String und hängt diesen an den Stringbuilder an.
	 *
	 * @param name      der Name der Query
	 * @param query     die Query
	 * @param comment   der JavaDoc-Kommentar für das Attribut
	 *
	 * @return der Query-String.
	 */
	private static String getQueryAttribute(final String name, final String query, final String comment) {
		return "%1$s\t/** %2$s */%1$s\tpublic static final String QUERY_%3$s = \"%4$s\";%1$s".formatted(System.lineSeparator(), comment, name, query);
	}


	/**
	 * Generiert den Java Code für Queries in statischen Attributen.
	 *
	 * @param rev   die Revision der Tabelle, für welche die Queries generiert werden
	 *
	 * @return der Java Code
	 */
	private String getCode4Queries(final long rev) {
		final StringBuilder sb = new StringBuilder();
		// alle Tabellen: Generiere Code für eine Query auf alle Datensätze der Tabelle
		String query = "SELECT e FROM " + tabelle.getJavaKlasse(rev) + " e";
		sb.append(getQueryAttribute("ALL", query, "Die Datenbankabfrage für alle DTOs"));
		// nur für Tabellen mit Primärschlüssel...
		if (!tabelle.pkSpalten().isEmpty()) {
			// Generiere Code für eine parametrisierte Query mit den Spalten des Primärschlüssels als Parameter
			query = tabelle.getJPQLParameterizedQuery(rev);
			sb.append(getQueryAttribute("PK", query, "Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute"));
			// Generiere Code für eine parametrisierte Query mit einer Liste von Primärschlüsselwerten als Parameter, wenn es sich um ein DTO mit einem Primärschlüssel mit einem Attribute handelt
			if (tabelle.pkSpalten().size() == 1) {
				final SchemaTabelleSpalte col = tabelle.pkSpalten().iterator().next();
				query = "SELECT e FROM " + tabelle.getJavaKlasse(rev) + " e WHERE e." + col.javaAttributName() + " IN ?1";
				sb.append(getQueryAttribute("LIST_PK", query, "Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten"));
			}
			// Generiere Code für eine Query auf alle Datensätze der Tabelle, welche für eine Migration Datensätze entfernt, die nicht der Primary-key-Constraint entsprechen
			query = "SELECT e FROM " + tabelle.getJavaKlasse(rev) + " e WHERE "
					+ tabelle.pkSpalten().stream().map(col -> "e." + col.javaAttributName() + " IS NOT NULL").collect(Collectors.joining(" AND "))
					+ "";
			sb.append(getQueryAttribute("MIGRATION_ALL", query,
					"Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen"));
		}
		// alle Tabellen: Generiere Annotationen für Queries für einzelne Attribute der Tabelle
		for (final SchemaTabelleSpalte spalte : tabelle.getSpalten(rev)) {
			if (spalte.javaAttributName().startsWith("-"))
				continue; // ignoriere Datenbank-Spalten, welche nicht als Java-Attribute umgesetzt werden sollen
			query = "SELECT e FROM " + tabelle.getJavaKlasse(rev) + " e WHERE e." + spalte.javaAttributName() + " = ?1";
			sb.append(getQueryAttribute("BY_" + spalte.javaAttributName().toUpperCase(), query,
					"Die Datenbankabfrage für DTOs anhand des Attributes " + spalte.javaAttributName()));
			query = "SELECT e FROM " + tabelle.getJavaKlasse(rev) + " e WHERE e." + spalte.javaAttributName() + " IN ?1";
			sb.append(getQueryAttribute("LIST_BY_" + spalte.javaAttributName().toUpperCase(), query,
					"Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes " + spalte.javaAttributName()));
		}
		return sb.toString();
	}



	/**
	 * Generiert das Attribut für eine Java-Klasse, welches die angegebene Datenbank-Spalte
	 * repräsentiert. <br>
	 * Eine Datenbank-Spalte wird ignoriert, falls der Java-Attribut-Name mit "-" beginnt.
	 * In diesem Fall wird kein Code erzeugt. <br>
	 * Ist der Java-Attribut-Name null oder "", so wird einfach der DB-Spaltenname übernommen.
	 *
	 * @param spalte            die Spalte für die das Java-Attribut generiert wird
	 * @param rev               die DB Revision, für welche das Attribut erzeugt wird.
	 * @param withAnnotations   gibt an, ob auch Annotationen für die Spalte generiert werden sollen.
	 *
	 * @return der Java-Code für das Attribut für die DB-Spalte
	 */
	private String getCode4Attributes(final SchemaTabelleSpalte spalte, final long rev, final boolean withAnnotations) {
		final String javaAttributName = getJavaAttributeName(spalte);
		if (javaAttributName == null)
			return null;
		final StringBuilder sb = new StringBuilder();
		if (spalte.javaComment() != null)
			sb.append("\t/** " + spalte.javaComment() + " */" + System.lineSeparator());
		if (withAnnotations) {
			if ((tabelle.pkSpalten().isEmpty()) || tabelle.pkSpalten().contains(spalte))
				sb.append("\t@Id" + System.lineSeparator());
			sb.append("\t@Column(name = \"" + spalte.name() + "\")" + System.lineSeparator());
			sb.append("\t@JsonProperty" + System.lineSeparator());
		}
		String dataType = spalte.datentyp().java((rev != 0) && spalte.notNull());
		final DBAttributeConverter<?, ?> attrConverter = spalte.javaConverter(rev);
		if (attrConverter != null) {
			if (withAnnotations) {
				final var simpleConverterClassName = attrConverter.getClass().getSimpleName();
				sb.append("\t@Convert(converter = " + simpleConverterClassName + ".class)" + System.lineSeparator());
				sb.append("\t@JsonSerialize(using = " + simpleConverterClassName + "Serializer.class)" + System.lineSeparator());
				sb.append("\t@JsonDeserialize(using = " + simpleConverterClassName + "Deserializer.class)" + System.lineSeparator());
			}
			dataType = attrConverter.getResultType().getSimpleName();
		}
		sb.append("\tpublic " + dataType + " " + javaAttributName + ";" + System.lineSeparator());
		return sb.toString();
	}



	/**
	 * Generiert für die angegebene Revision den Java-Code der dazugehörigen Java-DTO-Klasse.
	 *
	 * @param rev    die Revision
	 *
	 * @return der Java-Code für die DTO-Klasse in der angegebenen Revision.
	 */
	public String getCode(final long rev) {
		// Prüfe, ob überhaupt eine Java-Klasse erzeugt werden soll
		if ((tabelle.getJavaKlasse(rev) == null) || (!tabelle.isDefined(rev)))
			return null;

		final var acs = getAttributeConverter(rev);

		final StringBuilder sb = new StringBuilder();
		sb.append("package " + getPackageName(rev) + ";" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("import de.svws_nrw.db.DBEntityManager;" + System.lineSeparator());
		if (!acs.isEmpty())
			sb.append(getCodeImportConverter(acs));
		sb.append(System.lineSeparator());

		sb.append("import jakarta.persistence.Cacheable;" + System.lineSeparator());
		sb.append("import jakarta.persistence.Column;" + System.lineSeparator());
		if (!acs.isEmpty())
			sb.append("import jakarta.persistence.Convert;" + System.lineSeparator());
		sb.append("import jakarta.persistence.Entity;" + System.lineSeparator());
		sb.append("import jakarta.persistence.Id;" + System.lineSeparator());
		if (tabelle.pkSpalten().size() != 1)
			sb.append("import jakarta.persistence.IdClass;" + System.lineSeparator());
		sb.append("import jakarta.persistence.Table;" + System.lineSeparator());
		sb.append(System.lineSeparator());

		sb.append("import com.fasterxml.jackson.annotation.JsonProperty;" + System.lineSeparator());
		sb.append("import com.fasterxml.jackson.annotation.JsonPropertyOrder;" + System.lineSeparator());
		if (!acs.isEmpty()) {
			sb.append("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;" + System.lineSeparator());
			sb.append("import com.fasterxml.jackson.databind.annotation.JsonSerialize;" + System.lineSeparator());
			final String csvImportConverter = acs.stream()
					.map(ac -> ac.getClass().getName())
					.filter(Objects::nonNull).sorted().distinct()
					.map(cn -> cn.replace(".db.", ".csv.") + "Serializer;" + System.lineSeparator() + "import " + cn.replace(".db.", ".csv.") + "Deserializer")
					.collect(Collectors.joining(";" + System.lineSeparator() + "import "));
			sb.append("import " + csvImportConverter + ";" + System.lineSeparator());
			sb.append(System.lineSeparator());
		}

		sb.append("/**" + System.lineSeparator());
		sb.append(" * Diese Klasse dient als DTO für die Datenbanktabelle " + tabelle.name() + "." + System.lineSeparator());
		sb.append(" * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden," + System.lineSeparator());
		sb.append(" * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird." + System.lineSeparator());
		sb.append(" */" + System.lineSeparator());
		sb.append("@Entity" + System.lineSeparator());
		if (tabelle.pkSpalten().size() != 1) {
			sb.append("@IdClass(" + tabelle.getJavaKlasse(rev) + "PK.class)" + System.lineSeparator());
		}
		sb.append("@Cacheable(DBEntityManager.use_db_caching)" + System.lineSeparator());
		sb.append("@Table(name = \"" + tabelle.name() + "\")" + System.lineSeparator());
		sb.append(tabelle.getSpalten(rev).stream()
				.map(spalte -> "\"" + spalte.javaAttributName() + "\"")
				.collect(Collectors.joining(", ", "@JsonPropertyOrder({", "})" + System.lineSeparator())));
		sb.append("public final class " + tabelle.getJavaKlasse(rev) + " {" + System.lineSeparator());
		sb.append(getCode4Queries(rev));
		sb.append(System.lineSeparator());
		sb.append(tabelle.getSpalten(rev).stream()
				.map(spalte -> getCode4Attributes(spalte, rev, true))
				.filter(Objects::nonNull)
				.collect(Collectors.joining(System.lineSeparator())));
		sb.append(System.lineSeparator());
		sb.append("\t/**" + System.lineSeparator());
		sb.append("\t * Erstellt ein neues Objekt der Klasse " + tabelle.getJavaKlasse(rev) + " ohne eine Initialisierung der Attribute."
				+ System.lineSeparator());
		sb.append("\t */" + System.lineSeparator());
		sb.append("\t@SuppressWarnings(\"unused\")" + System.lineSeparator());
		sb.append("\tprivate " + tabelle.getJavaKlasse(rev) + "() {" + System.lineSeparator());
		sb.append("\t}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		if (tabelle.getSpalten(rev).stream().anyMatch(spalte -> spalte.notNull())) {
			sb.append("\t/**" + System.lineSeparator());
			sb.append("\t * Erstellt ein neues Objekt der Klasse " + tabelle.getJavaKlasse(rev) + " ohne eine Initialisierung der Attribute."
					+ System.lineSeparator());
			tabelle.getSpalten(rev).stream().filter(spalte -> spalte.notNull()).forEach(spalte -> {
				final String colname = getJavaAttributeName(spalte);
				if (colname != null)
					sb.append("\t * @param " + colname + "   der Wert für das Attribut " + colname + "" + System.lineSeparator());
			});
			sb.append("\t */" + System.lineSeparator());
			sb.append("\tpublic " + tabelle.getJavaKlasse(rev) + "(");
			sb.append(tabelle.getSpalten(rev).stream()
					.filter(spalte -> spalte.notNull())
					.filter(spalte -> getJavaAttributeName(spalte) != null)
					.map(spalte -> "final " + getDataType(spalte, rev) + " " + getJavaAttributeName(spalte))
					.collect(Collectors.joining(", ")));
			sb.append(") {" + System.lineSeparator());
			sb.append(tabelle.getSpalten(rev).stream()
					.filter(spalte -> spalte.notNull())
					.filter(spalte -> getJavaAttributeName(spalte) != null)
					.map(spalte -> {
						final String zuweisung =
								"\t\tthis." + getJavaAttributeName(spalte) + " = " + getJavaAttributeName(spalte) + ";" + System.lineSeparator();
						if (spalte.datentyp().isJavaPrimitiveType((rev != 0) && spalte.notNull()))
							return zuweisung;
						return "\t\tif (" + getJavaAttributeName(spalte) + " == null) {" + System.lineSeparator()
								+ "\t\t\tthrow new NullPointerException(\"" + getJavaAttributeName(spalte) + " must not be null\");" + System.lineSeparator()
								+ "\t\t}" + System.lineSeparator()
								+ zuweisung;
					}).collect(Collectors.joining()));
			sb.append("\t}" + System.lineSeparator());
		}
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		final Collection<SchemaTabelleSpalte> tmpPkSpalten = tabelle.pkSpalten().isEmpty() ? tabelle.getSpalten(rev) : tabelle.pkSpalten();
		sb.append(getCode4EqualsAndHashcode(tabelle.getJavaKlasse(rev), tmpPkSpalten, rev));
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\t/**" + System.lineSeparator());
		sb.append("\t * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden." + System.lineSeparator());
		sb.append("\t *" + System.lineSeparator());
		sb.append("\t * @return die String-Repräsentation des Objektes" + System.lineSeparator());
		sb.append("\t */" + System.lineSeparator());
		sb.append("\t@Override" + System.lineSeparator());
		sb.append("\tpublic String toString() {" + System.lineSeparator());
		sb.append("\t\treturn \"" + tabelle.getJavaKlasse(rev) + "("
				+ tabelle.getSpalten(rev).stream()
						.filter(spalte -> getJavaAttributeName(spalte) != null)
						.map(spalte -> "" + getJavaAttributeName(spalte) + "=\" + this." + getJavaAttributeName(spalte) + " + \"")
						.collect(Collectors.joining(", "))
				+ ")\";" + System.lineSeparator());
		sb.append("\t}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("}");
		sb.append(System.lineSeparator());
		return sb.toString();
	}


	/**
	 * Generiert für die Tabelle eine DTO-Klasse für den Primärschlüssel
	 *
	 * @param rev    die Revision
	 *
	 * @return der Java-Code für die Primärschlüssel-DTO-Klasse.
	 */
	public String getCode4PrimaryKeyClass(final long rev) {
		// Prüfe, ob überhaupt eine Java-Klasse erzeugt werden soll
		if ((tabelle.getJavaKlasse(rev) == null) || (!tabelle.isDefined(rev)))
			return null;

		// Bestimme die Spalten für den Primärschlüssel und erzeuge nur Code, wenn es sich nicht um einen einfachen Primärschlüssel handelt
		Collection<SchemaTabelleSpalte> pkSpalten = tabelle.pkSpalten();
		if (pkSpalten == null)
			pkSpalten = tabelle.getSpalten(rev);
		if (pkSpalten.size() <= 1)
			return null;

		final StringBuilder sb = new StringBuilder();
		sb.append("package " + getPackageName(rev) + ";" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("import java.io.Serializable;" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("/**" + System.lineSeparator());
		sb.append(" * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle " + tabelle.name() + "." + System.lineSeparator());
		sb.append(" * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden," + System.lineSeparator());
		sb.append(" * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird." + System.lineSeparator());
		sb.append(" */" + System.lineSeparator());
		sb.append("public final class " + tabelle.getJavaKlasse(rev) + "PK implements Serializable {" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\t/** Die UID für diese Klasse */" + System.lineSeparator());
		sb.append("\tprivate static final long serialVersionUID = 1L;" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(tabelle.pkSpalten().stream()
				.map(col -> getCode4Attributes(col, rev, false))
				.filter(Objects::nonNull)
				.collect(Collectors.joining(System.lineSeparator())));
		sb.append(System.lineSeparator());
		sb.append("\t/**" + System.lineSeparator());
		sb.append("\t * Erstellt ein neues Objekt der Klasse " + tabelle.getJavaKlasse(rev) + "PK ohne eine Initialisierung der Attribute."
				+ System.lineSeparator());
		sb.append("\t */" + System.lineSeparator());
		sb.append("\t@SuppressWarnings(\"unused\")" + System.lineSeparator());
		sb.append("\tprivate " + tabelle.getJavaKlasse(rev) + "PK() {" + System.lineSeparator());
		sb.append("\t}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\t/**" + System.lineSeparator());
		sb.append("\t * Erstellt ein neues Objekt der Klasse " + tabelle.getJavaKlasse(rev) + "PK." + System.lineSeparator());
		tabelle.pkSpalten().stream().forEach(spalte -> {
			final String colname = getJavaAttributeName(spalte);
			if (colname != null)
				sb.append("\t * @param " + colname + "   der Wert für das Attribut " + colname + "" + System.lineSeparator());
		});
		sb.append("\t */" + System.lineSeparator());
		sb.append("\tpublic " + tabelle.getJavaKlasse(rev) + "PK(");
		sb.append(tabelle.pkSpalten().stream()
				.filter(spalte -> getJavaAttributeName(spalte) != null)
				.map(spalte -> "final " + getDataType(spalte, rev) + " " + getJavaAttributeName(spalte))
				.collect(Collectors.joining(", ")));
		sb.append(") {" + System.lineSeparator());
		sb.append(tabelle.pkSpalten().stream()
				.filter(spalte -> getJavaAttributeName(spalte) != null)
				.map(spalte -> {
					final String zuweisung = "\t\tthis." + getJavaAttributeName(spalte) + " = " + getJavaAttributeName(spalte) + ";" + System.lineSeparator();
					if (spalte.datentyp().isJavaPrimitiveType((rev != 0) && spalte.notNull()))
						return zuweisung;
					return "\t\tif (" + getJavaAttributeName(spalte) + " == null) {" + System.lineSeparator()
							+ "\t\t\tthrow new NullPointerException(\"" + getJavaAttributeName(spalte) + " must not be null\");" + System.lineSeparator()
							+ "\t\t}" + System.lineSeparator()
							+ zuweisung;
				}).collect(Collectors.joining()));
		sb.append("\t}" + System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append(getCode4EqualsAndHashcode(tabelle.getJavaKlasse(rev) + "PK", pkSpalten, rev));
		sb.append("}");
		sb.append(System.lineSeparator());
		return sb.toString();
	}

}
