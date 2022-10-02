package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Berufskolleg_Fachklassen.
 */
public class Tabelle_Berufskolleg_Fachklassen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der Fachklasse");

	/** Die Definition der Tabellenspalte FachklassenIndex */
	public SchemaTabelleSpalte col_FachklassenIndex = add("FachklassenIndex", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Der Index, welcher der Fachklasse für die Zuordnung zum Bildungsgang zugeordnet ist");

	/** Die Definition der Tabellenspalte Schluessel */
	public SchemaTabelleSpalte col_Schluessel = add("Schluessel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der erste Teil des Fachklassenschlüssels (FKS, dreistellig) ");

	/** Die Definition der Tabellenspalte Schluessel2 */
	public SchemaTabelleSpalte col_Schluessel2 = add("Schluessel2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Der zweite Teil des Fachklassenschlüssels (AP, zweistellig)");

	/** Die Definition der Tabellenspalte IstAusgelaufen */
	public SchemaTabelleSpalte col_IstAusgelaufen = add("IstAusgelaufen", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, ob die Fachklasse ausgelaufen ist oder nicht");

	/** Die Definition der Tabellenspalte BerufsfeldGruppe */
	public SchemaTabelleSpalte col_BerufsfeldGruppe = add("BerufsfeldGruppe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Die Gruppe der Berufsfelder, welcher das Berufsfeld der Fachklasse");

	/** Die Definition der Tabellenspalte Berufsfeld */
	public SchemaTabelleSpalte col_Berufsfeld = add("Berufsfeld", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Das Berufsfeld, welchem die Fachklasse zugeordnet ist");

	/** Die Definition der Tabellenspalte Berufsebene1 */
	public SchemaTabelleSpalte col_Berufsebene1 = add("Berufsebene1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Die Berufsebene 1");

	/** Die Definition der Tabellenspalte Berufsebene2 */
	public SchemaTabelleSpalte col_Berufsebene2 = add("Berufsebene2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Die Berufsebene 2");

	/** Die Definition der Tabellenspalte Berufsebene3 */
	public SchemaTabelleSpalte col_Berufsebene3 = add("Berufsebene3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Die Berufsebene 3");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Fachklasse");

	/** Die Definition der Tabellenspalte BezeichnungM */
	public SchemaTabelleSpalte col_BezeichnungM = add("BezeichnungM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Fachklasse (Text in männlicher Form)");

	/** Die Definition der Tabellenspalte BezeichnungW */
	public SchemaTabelleSpalte col_BezeichnungW = add("BezeichnungW", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die Bezeichnung der Fachklasse (Text in weiblicher Form)");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Berufskolleg_Fachklassen.
	 */
	public Tabelle_Berufskolleg_Fachklassen() {
		super("Berufskolleg_Fachklassen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOBerufskollegFachklassen");
		setJavaComment("Fachklassen des Berufskollegs");
	}

}
