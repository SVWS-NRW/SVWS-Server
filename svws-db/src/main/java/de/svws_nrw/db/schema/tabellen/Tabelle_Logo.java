package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.ReportingBildDefinitionConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Logo.
 */
public class Tabelle_Logo extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("id", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Logos");

	/** Die Definition der Tabellenspalte Kennung */
	public final SchemaTabelleSpalte col_Kennung = add("kennung", SchemaDatentypen.VARCHAR, false)
			.setConverter(ReportingBildDefinitionConverter.class)
			.setConverterRevision(SchemaRevisionen.REV_62)
			.setDatenlaenge(100)
			.setNotNull()
			.setJavaComment("Kennung des Logos");

	/** Die Definition der Tabellenspalte Logo_Base64 */
	public final SchemaTabelleSpalte col_Logo_Base64 = add("logoBase64", SchemaDatentypen.TEXT, false)
			.setNotNull()
			.setJavaComment("Das Logo der Schule als Bild im Base64-Format");

	/** Die Definition der Tabellenspalte MimeType: image/png, image/jpeg, etc. */
	public final SchemaTabelleSpalte col_MimeType = add("mimeType", SchemaDatentypen.VARCHAR, false)
			.setDatenlaenge(50)
			.setJavaComment("MimeType des Logos")
			.setVeraltet(SchemaRevisionen.REV_76);

	/** Die Definition der Tabellenspalte Hoehe_PX */
	public final SchemaTabelleSpalte col_Hoehe_PX = add("hoehePX", SchemaDatentypen.INT, false)
			.setJavaComment("Höhe des Logos in Pixel")
			.setVeraltet(SchemaRevisionen.REV_76);

	/** Die Definition der Tabellenspalte Breite_PX */
	public final SchemaTabelleSpalte col_Breite_PX = add("breitePX", SchemaDatentypen.INT, false)
			.setJavaComment("Breite des Logos in Pixel")
			.setVeraltet(SchemaRevisionen.REV_76);

	/** Die Definition der Tabellenspalte Hoehe_MM */
	public final SchemaTabelleSpalte col_Hoehe_MM = add("hoeheMM", SchemaDatentypen.INT, false)
			.setJavaComment("Höhe des Logos in Millimeter")
			.setVeraltet(SchemaRevisionen.REV_76);

	/** Die Definition der Tabellenspalte Breite_MM */
	public final SchemaTabelleSpalte col_Breite_MM  = add("breiteMM", SchemaDatentypen.INT, false)
			.setJavaComment("Breite des Logos in Millimeter")
			.setVeraltet(SchemaRevisionen.REV_76);

	/** Die Definition der Tabellenspalte Hinzugefuegt_Am */
	public final SchemaTabelleSpalte col_Hinzugefuegt_Am  = add("hinzugefuegtAm", SchemaDatentypen.DATE, false)
			.setNotNull()
			.setJavaComment("Datum des Hinzufügens des Bildes");

	/** Die Definition des Unique-Index Logo_UC1 */
	public final SchemaTabelleUniqueIndex unique_Logo_UC1 = addUniqueIndex("Logo_UC1",
			col_Kennung
	);

	/**
	 * Erstellt die Schema-Definition für die Tabelle Logo.
	 */
	public Tabelle_Logo() {
		super("Logo", SchemaRevisionen.REV_62);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOLogo");
		setJavaComment("Textbausteine, die in den Editoren angezeigt und ausgewählt werden können");
	}

}
