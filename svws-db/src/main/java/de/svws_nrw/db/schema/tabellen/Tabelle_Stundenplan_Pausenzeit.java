package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Pausenzeit.
 */
public class Tabelle_Stundenplan_Pausenzeit extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eine ID, die einen Pausenzeit-Eintrag eindeutig identifiziert - hat keinen Bezug zur ID der Katalog-Tabelle");

	/** Die Definition der Tabellenspalte Stundenplan_ID */
	public SchemaTabelleSpalte col_Stundenplan_ID = add("Stundenplan_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Stundenplans, dem dies Pausenzeit zugeordnet ist");

	/** Die Definition der Tabellenspalte Tag */
	public SchemaTabelleSpalte col_Tag = add("Tag", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Der Wochentag laut ISO-8601 Standard: (1 - Montag, 2 - Dienstag, ...)");

	/** Die Definition der Tabellenspalte Beginn */
	public SchemaTabelleSpalte col_Beginn = add("Beginn", SchemaDatentypen.TIME, false)
		.setNotNull()
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Die Uhrzeit, wann die Pausenzeit beginnt");

	/** Die Definition der Tabellenspalte Ende */
	public SchemaTabelleSpalte col_Ende = add("Ende", SchemaDatentypen.TIME, false)
		.setNotNull()
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Die Uhrzeit, wann die Pausenzeit endet");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setNotNull()
		.setDefault("Pause")
		.setJavaComment("Eine kurze Bezeichnung, welche die Art der Pausenzeit genauer beschreibt (z.B. Mittagspause)");


	/** Die Definition des Fremdschlüssels Stundenplan_Pausenzeit_Stundenplan_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Pausenzeit_Stundenplan_FK = addForeignKey(
			"Stundenplan_Pausenzeit_Stundenplan_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Stundenplan_ID, Schema.tab_Stundenplan.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_Pausenzeit_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_Pausenzeit_UC1 = addUniqueIndex("Stundenplan_Pausenzeit_UC1",
			col_Stundenplan_ID,
			col_Beginn,
			col_Ende,
			col_Tag
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Pausenzeit.
	 */
	public Tabelle_Stundenplan_Pausenzeit() {
		super("Stundenplan_Pausenzeit", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanPausenzeit");
		setJavaComment("Enthält die Pausenzeiten eines Stundenplan. Diese werden üblicherweise aus der Tabelle Katalog_Pausenzeiten übernommen und hier zwischengespeichert. Änderungen im Katalog werden nicht hierhin übernommen.");
	}

}
