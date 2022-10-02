package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Berufskolleg_Fachklassen_Keys.
 */
public class Tabelle_Berufskolleg_Fachklassen_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte FachklassenIndex */
	public SchemaTabelleSpalte col_FachklassenIndex = add("FachklassenIndex", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Der Index, welcher der Fachklasse für die Zuordnung zum Bildungsgang zugeordnet ist");

	/** Die Definition der Tabellenspalte Schluessel */
	public SchemaTabelleSpalte col_Schluessel = add("Schluessel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der erste Teil des Fachklassenschlüssels (FKS, dreistellig) ");

	/** Die Definition der Tabellenspalte Schluessel2 */
	public SchemaTabelleSpalte col_Schluessel2 = add("Schluessel2", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Der zweite Teil des Fachklassenschlüssels (AP, zweistellig)");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Berufskolleg_Fachklassen_Keys.
	 */
	public Tabelle_Berufskolleg_Fachklassen_Keys() {
		super("Berufskolleg_Fachklassen_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOBerufskollegFachklassenKeys");
		setJavaComment("Die Schlüssel von Fachklassen des Berufskollegs - auch von mittlerweile ausgelaufenen Fachklassen");
	}

}
