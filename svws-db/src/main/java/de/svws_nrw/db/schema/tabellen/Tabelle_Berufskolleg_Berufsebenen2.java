package de.svws_nrw.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.svws_nrw.core.types.schule.BerufskollegBerufsebene2;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Berufskolleg_Berufsebenen2.
 */
public class Tabelle_Berufskolleg_Berufsebenen2 extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID der Berufsebene ");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setNotNull()
			.setJavaComment("Kürzel der Berufsebene");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setJavaComment("Bezeichnung der Berufsebene");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
			.setJavaComment("Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
			.setJavaComment("Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Berufskolleg_Berufsebenen2.
	 */
	public Tabelle_Berufskolleg_Berufsebenen2() {
		super("Berufskolleg_Berufsebenen2", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOBerufskollegBerufsebenen2");
		setJavaComment("Informationen Berufsebenen der Ebene 2 des Berufskollegs");
		setCoreType(new SchemaTabelleCoreType(this, BerufskollegBerufsebene2.class, BerufskollegBerufsebene2.VERSION, rev -> Arrays
				.stream(BerufskollegBerufsebene2.values())
				.map(a -> Arrays.stream(a.historie)
						.map(h -> h.id + ",'" + h.kuerzel + "'" + ",'" + h.bezeichnung + "'" + "," + h.gueltigVon + "," + h.gueltigBis)
						.toList())
				.flatMap(Collection::stream).toList()));
	}

}
