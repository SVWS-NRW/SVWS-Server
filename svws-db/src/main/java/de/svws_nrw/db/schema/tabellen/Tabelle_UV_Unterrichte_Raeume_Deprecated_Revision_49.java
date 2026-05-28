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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Unterrichte_Raeume.
 */
public class Tabelle_UV_Unterrichte_Raeume_Deprecated_Revision_49 extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public final SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des UV_Unterrichts");

  /** Fremdschlüssel auf den Raum (Tabelle UV_Raeume) */
  public final SchemaTabelleSpalte col_Raum_ID = add("Raum_ID", SchemaDatentypen.BIGINT, false)
          .setJavaComment("Fremdschlüssel auf den Raum (Tabelle UV_Raeume)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");


	/** Die Definition des Fremdschlüssels UV_UnterrichteRaeume_Unterrichte_FK */
	public final SchemaTabelleFremdschluessel fk_UV_UnterrichteRaeume_Unterrichte_FK = addForeignKey(
			"UV_UnterrichteRaeume_Unterrichte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Unterricht_ID, Schema.tab_UV_Unterrichte.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Unterrichte.col_Planungsabschnitt_ID)
	);

	/** Die Definition des Fremdschlüssels UV_UnterrichteRaeume_Raeume_FK */
	public final SchemaTabelleFremdschluessel fk_UV_UnterrichteRaeume_Raeume_FK = addForeignKey(
			"UV_UnterrichteRaeume_Raeume_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Raum_ID, Schema.tab_UV_Raeume.col_ID)
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Unterrichte_Raeume.
	 */
	public Tabelle_UV_Unterrichte_Raeume_Deprecated_Revision_49() {
		super("UV_Unterrichte_Raeume", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvUnterrichtRaum_Deprecated_Revision_49");
		setJavaComment("Tabelle für die Räume, welche einem Unterricht der UV zugeordnet sind");
		setVeraltet(SchemaRevisionen.REV_49);
	}

}
