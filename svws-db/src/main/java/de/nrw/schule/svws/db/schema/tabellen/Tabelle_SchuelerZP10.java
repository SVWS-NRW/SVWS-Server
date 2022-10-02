package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerZP10.
 */
public class Tabelle_SchuelerZP10 extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Facheintrags für den ZP10 Abschluss");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Schüler-ID des Facheintrags für den ZP10 Abschluss");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Schuljahresabschnitt-ID für den Facheintrag des ZP10 Abschlusses");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Fach-ID des Facheintrags für den ZP10 Abschluss");

	/** Die Definition der Tabellenspalte Vornote */
	public SchemaTabelleSpalte col_Vornote = add("Vornote", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Vornote zum ZP10-Facheintrag");

	/** Die Definition der Tabellenspalte NoteSchriftlich */
	public SchemaTabelleSpalte col_NoteSchriftlich = add("NoteSchriftlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schriftliche Note zum ZP10-Facheintrag");

	/** Die Definition der Tabellenspalte MdlPruefung */
	public SchemaTabelleSpalte col_MdlPruefung = add("MdlPruefung", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter("BooleanPlusMinusConverter")
		.setJavaComment("Gibt an, ob zum ZP10-Facheintrag eine mündliche Prüfung angesetzt ist");

	/** Die Definition der Tabellenspalte MdlPruefungFW */
	public SchemaTabelleSpalte col_MdlPruefungFW = add("MdlPruefungFW", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter("BooleanPlusMinusConverter")
		.setJavaComment("Gibt an, ob zum ZP10-Facheintrag eine freiwilliege mündliche Prüfung angesetzt ist");

	/** Die Definition der Tabellenspalte NoteMuendlich */
	public SchemaTabelleSpalte col_NoteMuendlich = add("NoteMuendlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Die Note der mündlichen Prüfung zum ZP10-Facheintrag");

	/** Die Definition der Tabellenspalte NoteAbschluss */
	public SchemaTabelleSpalte col_NoteAbschluss = add("NoteAbschluss", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Die Abschlussnote zum ZP10-Facheintrag");

	/** Die Definition der Tabellenspalte Fachlehrer_ID */
	public SchemaTabelleSpalte col_Fachlehrer_ID = add("Fachlehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die Lehrer-ID zum ZP10-Facheintrag");


	/** Die Definition des Fremdschlüssels SchuelerZP10_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerZP10_Schuljahreabschnitt_FK = addForeignKey(
			"SchuelerZP10_Schuljahreabschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerZP10_Fach_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerZP10_Fach_FK = addForeignKey(
			"SchuelerZP10_Fach_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerZP10_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerZP10_Schueler_FK = addForeignKey(
			"SchuelerZP10_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/** Die Definition des Non-Unique-Index SchuelerZP10_IDX1 */
	public SchemaTabelleIndex index_SchuelerZP10_IDX1 = addIndex("SchuelerZP10_IDX1", 
			col_Schueler_ID
		);

	/** Die Definition des Non-Unique-Index SchuelerZP10_IDX2 */
	public SchemaTabelleIndex index_SchuelerZP10_IDX2 = addIndex("SchuelerZP10_IDX2", 
			col_Schuljahresabschnitts_ID
		);

	/** Die Definition des Non-Unique-Index SchuelerZP10_IDX3 */
	public SchemaTabelleIndex index_SchuelerZP10_IDX3 = addIndex("SchuelerZP10_IDX3", 
			col_Fach_ID
		);

	/** Die Definition des Non-Unique-Index SchuelerZP10_IDX4 */
	public SchemaTabelleIndex index_SchuelerZP10_IDX4 = addIndex("SchuelerZP10_IDX4", 
			col_Fachlehrer_ID
		);


	/** Die Definition des Unique-Index SchuelerZP10_UC1 */
	public SchemaTabelleUniqueIndex unique_SchuelerZP10_UC1 = addUniqueIndex("SchuelerZP10_UC1", 
			col_Schueler_ID, 
			col_Schuljahresabschnitts_ID, 
			col_Fach_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerZP10.
	 */
	public Tabelle_SchuelerZP10() {
		super("SchuelerZP10", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerZP10");
		setJavaComment("Die fachspezifischen Abschluss-Daten für die ZP10-Prüfungen");
	}

}
