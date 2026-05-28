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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Schuelergruppen_Constraint_Schuelergruppen.
 */
public class Tabelle_UV_Schuelergruppen_Constraint_Schuelergruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schuelergruppe_ID */
	public final SchemaTabelleSpalte col_Schuelergruppe_ID = add("Schuelergruppe_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Schülergruppe");

	/** Die Definition der Tabellenspalte Schuelergruppe_Valid_ID */
	public final SchemaTabelleSpalte col_Schuelergruppe_Valid_ID = add("Schuelergruppe_Vaild_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Schülergruppe, deren Schüler dieser Schülergruppe angehören dürfen");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");


	/** Die Definition des Fremdschlüssels auf UV_Schuelergruppen */
	public final SchemaTabelleFremdschluessel fk_UVSchuelergruppenSchuelergruppen_UVSchuelergruppen_FK1 = addForeignKey(
			"UVSchuelergruppenSchuelergruppen_UVSchuelergruppen_FK1",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schuelergruppe_ID, Schema.tab_UV_Schuelergruppen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Schuelergruppen.col_Planungsabschnitt_ID)
	);

	/** Fremdschlüssel auf die Tabelle UV_Schuelergruppen */
	public final SchemaTabelleFremdschluessel fk_UVSchuelergruppenSchuelergruppen_UVSchuelergruppen_FK2 = addForeignKey(
			"UVSchuelergruppenSchuelergruppen_UVSchuelergruppen_FK2",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schuelergruppe_Valid_ID, Schema.tab_UV_Schuelergruppen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Schuelergruppen.col_Planungsabschnitt_ID)
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Schuelergruppen_Constraint_Schuelergruppen.
	 */
	public Tabelle_UV_Schuelergruppen_Constraint_Schuelergruppen() {
		super("UV_Schuelergruppen_Constraint_Schuelergruppen", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvSchuelergruppeConstraintSchuelergruppe");
		setJavaComment("Tabelle für die Zuordnung von Schülergruppen zu Schülergruppen eines Planungsabschnitts der Unterrichtsverteilung (UV)");
	}

}
