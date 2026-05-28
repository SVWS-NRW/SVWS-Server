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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Lehrer.
 */
public class Tabelle_UV_Lehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Lehrers im Planungsabschnitt (generiert, planungsspezifisch)");

	/** Die Definition der Tabellenspalte K_Lehrer_ID */
	public final SchemaTabelleSpalte col_K_Lehrer_ID = add("K_Lehrer_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID des Lehrers als Fremdschlüssel auf die Tabelle K_Lehrer");

	/** Die Definition der Tabellenspalte Kuerzel */
	public final SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Lehrer-Kürzel für eine lesbare eindeutige Identifikation des Lehrers");

	/** Die Definition der Tabellenspalte Nachname */
	public final SchemaTabelleSpalte col_Nachname = add("Nachname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
			.setJavaComment("Der Nachname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Vorname */
	public final SchemaTabelleSpalte col_Vorname = add("Vorname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
			.setJavaComment("Der Vorname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt.");


	/** Die Definition des Fremdschlüssels auf K_Lehrer */
	public final SchemaTabelleFremdschluessel fk_UVLehrer_KLehrer_FK = addForeignKey(
			"UVLehrer_KLehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_K_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
	);

	/** Die Definition des Unique-Index UV_Lehrer_UC1 */
	public final SchemaTabelleUniqueIndex unique_UVLehrer_UC1 = addUniqueIndex("UVLehrer_UC1",
			col_K_Lehrer_ID
	);

	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Lehrer.
	 */
	public Tabelle_UV_Lehrer() {
		super("UV_Lehrer", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvLehrer");
		setJavaComment("Tabelle für die Lehrkräfte der UV-Planungsabschnitte");
	}

}
