package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Schienen.
 */
public class Tabelle_UV_Schienen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Schiene im Planungsabschnitt (generiert, planungsspezifisch)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Nummer */
	public final SchemaTabelleSpalte col_Nummer = add("Nummer", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Die Nummer der Schiene");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public final SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
			.setNotNull()
			.setJavaComment("Die Bezeichnung der Schiene");


	/** Die Definition des Fremdschlüssels auf UV_Planungsabschnitte */
	public final SchemaTabelleFremdschluessel fk_UVSchienen_UVPlanungsabschnitte_FK = addForeignKey(
			"UVSchienen_UVPlanungsabschnitte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Planungsabschnitte.col_ID)
	);

	/** Unique-Index für die Kombination ID und Planungsabschnitt_ID, benötigt durch 2-teiligen FK */
	public final SchemaTabelleUniqueIndex unique_UVSchienen_UC1 = addUniqueIndex("UVSchienen_UC1",
			col_ID, col_Planungsabschnitt_ID
	);

	/** Unique-Index für die Kombination ID und Planungsabschnitt_ID, benötigt durch 2-teiligen FK */
	public final SchemaTabelleUniqueIndex unique_UVSchienen_UC2 = addUniqueIndex("UVSchienen_UC2",
			col_Planungsabschnitt_ID, col_Nummer
	).setRevision(SchemaRevisionen.REV_49);

	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Schienen.
	 */
	public Tabelle_UV_Schienen() {
		super("UV_Schienen", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvSchiene");
		setJavaComment("Tabelle für die konkreten Schienen der UV-Planungsabschnitte");
	}

}
