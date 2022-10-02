package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Ankreuzdaten.
 */
public class Tabelle_K_Ankreuzdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes für die Angaben zu den Ankreuzkompetenzen");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte TextStufe1 */
	public SchemaTabelleSpalte col_TextStufe1 = add("TextStufe1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung Stufe 1");

	/** Die Definition der Tabellenspalte TextStufe2 */
	public SchemaTabelleSpalte col_TextStufe2 = add("TextStufe2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung Stufe 2");

	/** Die Definition der Tabellenspalte TextStufe3 */
	public SchemaTabelleSpalte col_TextStufe3 = add("TextStufe3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung Stufe 3");

	/** Die Definition der Tabellenspalte TextStufe4 */
	public SchemaTabelleSpalte col_TextStufe4 = add("TextStufe4", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung Stufe 4");

	/** Die Definition der Tabellenspalte TextStufe5 */
	public SchemaTabelleSpalte col_TextStufe5 = add("TextStufe5", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung Stufe 5");

	/** Die Definition der Tabellenspalte BezeichnungSONST */
	public SchemaTabelleSpalte col_BezeichnungSONST = add("BezeichnungSONST", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung Zusatzstufe");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Ankreuzdaten.
	 */
	public Tabelle_K_Ankreuzdaten() {
		super("K_Ankreuzdaten", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.grundschule");
		setJavaClassName("DTOAnkreuzdaten");
		setJavaComment("Angaben zu den Ankreuzkompetenzen (Ankreutzzeugnisse GS / Kompetenz-Ausdrucke für Schüler zum Zeugnis)");
	}

}
