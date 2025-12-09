package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Katalog_Floskeln.
 */
public class Tabelle_Katalog_Floskeln extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Floskel (generiert)");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setNotNull()
			.setJavaComment("Kürzel für die Floskel");

	/** Die Definition der Tabellenspalte Text */
	public SchemaTabelleSpalte col_Text = add("Text", SchemaDatentypen.TEXT, false)
			.setNotNull()
			.setJavaComment("Text der Floskel");

	/** Die Definition der Tabellenspalte Gruppe_ID */
	public SchemaTabelleSpalte col_Gruppe_ID = add("Gruppe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID der Floskelgruppe, welcher die Floskel zugeordnet ist");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID des Faches bei Floskeln für fachbezogene Bemerkungen, sonst null");

	/** Die Definition der Tabellenspalte Niveau */
	public SchemaTabelleSpalte col_Niveau = add("Niveau", SchemaDatentypen.INT, false)
			.setJavaComment("Die ID des Niveaus bei Floskeln für fachbezogene Bemerkungen, sonst null");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
			.setDefault("32000")
			.setJavaComment("Sortierung der Floskel");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Sichbarkeit der Floskel");

	/** Die Definition des Fremdschlüssels Katalog_Floskeln_Gruppen_FK */
	public SchemaTabelleFremdschluessel fk_Katalog_Floskeln_Gruppen_FK = addForeignKey(
			"Katalog_Floskeln_Gruppen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Gruppe_ID, Schema.tab_Katalog_Floskeln_Gruppen.col_ID));

	/** Die Definition des Unique-Index KatalogFloskeln_UC1 */
	public SchemaTabelleUniqueIndex unique_KatalogFloskeln_UC1 = addUniqueIndex("KatalogFloskeln_UC1", col_Kuerzel);

	/**
	 * Erstellt die Schema-Definition für die Tabelle Katalog_Floskeln.
	 */
	public Tabelle_Katalog_Floskeln() {
		super("Katalog_Floskeln", SchemaRevisionen.REV_52);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("katalog");
		setJavaClassName("DTOFloskeln");
		setJavaComment("Floskeln sind Textbausteine, die in den Editoren angezeigt und ausgewählt werden können und als Vorlage für Bemerkungsfelder bei Schülern dienen.");
	}

}
