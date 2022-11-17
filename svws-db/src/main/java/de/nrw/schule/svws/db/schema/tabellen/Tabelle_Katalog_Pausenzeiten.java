package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.converter.current.DatumConverter;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Katalog_Pausenzeiten.
 */
public class Tabelle_Katalog_Pausenzeiten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eine ID, die einen Pausenzeit-Eintrag eindeutig identifiziert");

	/** Die Definition der Tabellenspalte Tag */
	public SchemaTabelleSpalte col_Tag = add("Tag", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Der Wochentag laut ISO-8601 Standard: (1 - Montag, 2 - Dienstag, ...)");

	/** Die Definition der Tabellenspalte Beginn */
	public SchemaTabelleSpalte col_Beginn = add("Beginn", SchemaDatentypen.TIME, false)
		.setNotNull()
		.setConverter(DatumConverter.class)
		.setJavaComment("Die Uhrzeit, wann die Pausenzeit beginnt");

	/** Die Definition der Tabellenspalte Ende */
	public SchemaTabelleSpalte col_Ende = add("Ende", SchemaDatentypen.TIME, false)
		.setNotNull()
		.setConverter(DatumConverter.class)
		.setJavaComment("Die Uhrzeit, wann die Pausenzeit endet");


	/** Die Definition des Unique-Index Katalog_Pausenzeiten_UC1 */
	public SchemaTabelleUniqueIndex unique_Katalog_Pausenzeiten_UC1 = addUniqueIndex("Katalog_Pausenzeiten_UC1", 
			col_Beginn, 
			col_Ende, 
			col_Tag
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Katalog_Pausenzeiten.
	 */
	public Tabelle_Katalog_Pausenzeiten() {
		super("Katalog_Pausenzeiten", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOKatalogPausenzeit");
		setJavaComment("Enthält die aktuellen Zeiten für die Pausenaufsichten.");
	}

}
