package de.svws_nrw.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.svws_nrw.core.types.schule.BerufskollegAnlage;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Berufskolleg_Anlagen.
 */
public class Tabelle_Berufskolleg_Anlagen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Anlage");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Einstelliges Kürzel der Anlage (A,B,C,D,E,X,Z)");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Bezeichnung der Anlage");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/** Die Definition des Unique-Index Berufskolleg_Anlagen_UC1 */
	public SchemaTabelleUniqueIndex unique_Berufskolleg_Anlagen_UC1 = addUniqueIndex("Berufskolleg_Anlagen_UC1",
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Berufskolleg_Anlagen.
	 */
	public Tabelle_Berufskolleg_Anlagen() {
		super("Berufskolleg_Anlagen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOBerufskollegAnlagen");
		setJavaComment("Informationen Anlagen des Berufskollegs");
        setCoreType(new SchemaTabelleCoreType(this, BerufskollegAnlage.class, BerufskollegAnlage.VERSION, rev -> Arrays
            .stream(BerufskollegAnlage.values())
            .map(a -> Arrays.stream(a.historie)
                .map(h -> h.id + ",'" + h.kuerzel + "'" + ",'" + h.bezeichnung + "'" + "," + h.gueltigVon + "," + h.gueltigBis)
                .toList()
            ).flatMap(Collection::stream).toList()));
	}

}
