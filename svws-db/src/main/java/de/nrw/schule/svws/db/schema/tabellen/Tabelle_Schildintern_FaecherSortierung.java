package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_FaecherSortierung.
 */
public class Tabelle_Schildintern_FaecherSortierung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Fach */
	public SchemaTabelleSpalte col_Fach = add("Fach", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Fächerkürzel der Sortierung");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setJavaComment("Schildintern Tabelle: Bezeichnung der Sortierung");

	/** Die Definition der Tabellenspalte Sortierung1 */
	public SchemaTabelleSpalte col_Sortierung1 = add("Sortierung1", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: DEPRECATED: ");

	/** Die Definition der Tabellenspalte Sortierung2 */
	public SchemaTabelleSpalte col_Sortierung2 = add("Sortierung2", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: DEPRECATED: ");

	/** Die Definition der Tabellenspalte Fachgruppe_ID */
	public SchemaTabelleSpalte col_Fachgruppe_ID = add("Fachgruppe_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Schildintern Tabelle: Verweis auf die Schildintern Fachgruppe");

	/** Die Definition der Tabellenspalte FachgruppeKrz */
	public SchemaTabelleSpalte col_FachgruppeKrz = add("FachgruppeKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schildintern Tabelle: Kürzel der Fachgruppe");

	/** Die Definition der Tabellenspalte AufgabenbereichAbitur */
	public SchemaTabelleSpalte col_AufgabenbereichAbitur = add("AufgabenbereichAbitur", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Schildintern Tabelle: Angabe des Aufgabenbreich für das Abitur");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_FaecherSortierung.
	 */
	public Tabelle_Schildintern_FaecherSortierung() {
		super("Schildintern_FaecherSortierung", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternFaecherSortierung");
		setJavaComment("Wird in diversen Algorithmen verwendet, um zu ermitteln, zu welcher Fachgruppe ein Fach (ASD-Bezeichnung) gehört.");
	}

}
