package de.svws_nrw.db.schema.tabellen;

import java.util.Collection;

import de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.json.JsonDaten;

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
        setCoreType(new SchemaTabelleCoreType(this, BerufskollegFachklassenManager.class, JsonDaten.fachklassenManager.getVersion(), rev ->
            JsonDaten.fachklassenManager.getKatalog().indizes.stream()
            .map(i -> i.fachklassen.stream()
                .map(f -> i.index + ",'" + f.schluessel + "','" + f.schluessel2 + "'")
                .toList()
            ).flatMap(Collection::stream).toList()));
	}

}
