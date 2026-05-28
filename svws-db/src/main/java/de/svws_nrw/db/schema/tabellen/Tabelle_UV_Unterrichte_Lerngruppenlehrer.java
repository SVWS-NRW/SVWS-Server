package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Unterrichte_Lerngruppenlehrer.
 */
public class Tabelle_UV_Unterrichte_Lerngruppenlehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public final SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des UV_Unterrichts");

	/** Die Definition der Tabellenspalte LerngruppenLehrer_ID */
	public final SchemaTabelleSpalte col_LerngruppenLehrer_ID = add("LerngruppenLehrer_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Lehrers, welcher der Lerngruppe zugeordnet ist");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");


	/** Die Definition des Fremdschlüssels UV_UnterrichteLerngruppenlehrer_Unterrichte_FK */
	public final SchemaTabelleFremdschluessel fk_UV_UnterrichteLerngruppenlehrer_Unterrichte_FK = addForeignKey(
			"UV_UnterrichteLerngruppenlehrer_Unterrichte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Unterricht_ID, Schema.tab_UV_Unterrichte.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Unterrichte.col_Planungsabschnitt_ID)
	);

	/** Die Definition des Fremdschlüssels UV_UnterrichteLerngruppenlehrer_LerngruppenLehrer_FK */
	public final SchemaTabelleFremdschluessel fk_UV_UnterrichteLerngruppenlehrer_LerngruppenLehrer_FK = addForeignKey(
			"UV_UnterrichteLerngruppenlehrer_LerngruppenLehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_LerngruppenLehrer_ID, Schema.tab_UV_Lerngruppen_Lehrer.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Lerngruppen_Lehrer.col_Planungsabschnitt_ID)
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Unterrichte_Lerngruppenlehrer.
	 */
	public Tabelle_UV_Unterrichte_Lerngruppenlehrer() {
		super("UV_Unterrichte_Lerngruppenlehrer", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvUnterrichteLerngruppenlehrer");
		setJavaComment("Tabelle für die Lerngruppenlehrer, welche einem Unterricht der UV zugeordnet sind");
	}

}
