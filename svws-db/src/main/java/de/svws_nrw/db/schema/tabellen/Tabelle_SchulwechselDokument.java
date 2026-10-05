package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchulwechselDokument.
 */
public class Tabelle_SchulwechselDokument extends SchemaTabelle {

	/** Die Definition der Tabellenspalte id */
	public final SchemaTabelleSpalte col_id = add("id", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die Id des Dokuments");

	/** Die Definition der Tabellenspalte fileName */
	public final SchemaTabelleSpalte col_file_name = add("file_name", SchemaDatentypen.VARCHAR, false)
			.setDatenlaenge(100)
			.setNotNull()
			.setJavaName("fileName")
			.setJavaComment("Der Dateiname des Dokuments");

	/** Die Definition der Tabellenspalte fileName */
	public final SchemaTabelleSpalte col_xml_document = add("xml_document", SchemaDatentypen.TEXT, false)
			.setNotNull()
			.setJavaName("xmlDocument")
			.setJavaComment("Das Dokument selbst");

	/** Die Definition der Tabellenspalte createdAt */
	public final SchemaTabelleSpalte col_created_at = add("created_at", SchemaDatentypen.DATETIME, false)
			.setNotNull()
			.setJavaName("createdAt")
			.setJavaComment("Der Zeitpunkt der Erstellung des Dokuments (UTC)");

	/** Die Definition der Tabellenspalte lastModified */
	public final SchemaTabelleSpalte col_last_modified = add("last_modified", SchemaDatentypen.DATETIME, false)
			.setNotNull()
			.setJavaName("lastModified")
			.setJavaComment("Der Zeitpunkt der letzen Änderung des Dokuments (UTC)");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchulwechselDokument.
	 */
	public Tabelle_SchulwechselDokument() {
		super("SchulwechselDokument", SchemaRevisionen.REV_79);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOSchulwechselDokument");
		setJavaComment("Tabelle mit Dokumenten zu Wechselvorgängen");
	}
}
