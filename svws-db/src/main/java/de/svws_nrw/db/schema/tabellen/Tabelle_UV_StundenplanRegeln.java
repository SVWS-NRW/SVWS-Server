package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_StundenplanRegeln.
 */
public class Tabelle_UV_StundenplanRegeln extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Stundenplanregel (generiert)");

	/** Die Definition der Tabellenspalte StundenplanKonfiguration_ID */
	public final SchemaTabelleSpalte col_StundenplanKonfiguration_ID = add("StundenplanKonfiguration_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID der Stundenplankonfiguration");

	/** Die Definition der Tabellenspalte Typ */
	public final SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Die Nummer des UV-Blockungsregeltyps (siehe UvBlockungRegelTyp)");


	/** Die Definition des Fremdschlüssels UVStundenplanRegeln_UVStundenplanKonfiguration_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanRegeln_UVStundenplanKonfiguration_FK = addForeignKey(
			"UVStundenplanRegeln_UVStundenplanKonfiguration_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_StundenplanKonfiguration_ID, Schema.tab_UV_StundenplanKonfigurationen.col_ID));


	/** Die Definition des Non-Unique-Index UV_StundenplanRegeln_IDX_StundenplanKonfiguration_ID_Typ */
	public final SchemaTabelleIndex index_UV_StundenplanRegeln_IDX_StundenplanKonfiguration_ID_Typ = addIndex("UV_StundenplanRegeln_IDX_StundenplanKonfiguration_ID_Typ",
			col_StundenplanKonfiguration_ID,
			col_Typ
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_StundenplanRegeln.
	 */
	public Tabelle_UV_StundenplanRegeln() {
		super("UV_StundenplanRegeln", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvStundenplanRegel");
		setJavaComment("Tabelle für die Stundenplanregeln der UV");
	}

}
