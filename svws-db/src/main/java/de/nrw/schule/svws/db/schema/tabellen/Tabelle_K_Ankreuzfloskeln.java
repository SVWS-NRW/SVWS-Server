package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Ankreuzfloskeln.
 */
public class Tabelle_K_Ankreuzfloskeln extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Ankreuzfloskel");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("FachID zu der die Ankreuzfloskel gehört, null für individuelle Ankreuzfloskeln bzw. siehe Spalte IstASV");

	/** Die Definition der Tabellenspalte IstASV */
	public SchemaTabelleSpalte col_IstASV = add("IstASV", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzfloskel um eine Floskel zum Arbeits- und Sozialverhalten handelt (1) oder nicht (0).");

	/** Die Definition der Tabellenspalte Jahrgang */
	public SchemaTabelleSpalte col_Jahrgang = add("Jahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Jahrgang zu der die Ankreuzfloskel gehört");

	/** Die Definition der Tabellenspalte Gliederung */
	public SchemaTabelleSpalte col_Gliederung = add("Gliederung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK)");

	/** Die Definition der Tabellenspalte FloskelText */
	public SchemaTabelleSpalte col_FloskelText = add("FloskelText", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Text der Ankreuzfloskel");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung der Ankreuzfloskel");

	/** Die Definition der Tabellenspalte FachSortierung */
	public SchemaTabelleSpalte col_FachSortierung = add("FachSortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung des Faches der Ankreuzfloskel");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setJavaComment("Wird in welchen Abschnitten verwendet 1Hj 2HJ beide");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Sichtbarkeit der Ankreuzfloskel");

	/** Die Definition der Tabellenspalte Aktiv */
	public SchemaTabelleSpalte col_Aktiv = add("Aktiv", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Gibt an ob die Ankreuzfloskel aktiv ist");


	/** Die Definition des Fremdschlüssels K_Ankreuzfloskeln_Fach_ID_FK */
	public SchemaTabelleFremdschluessel fk_K_Ankreuzfloskeln_Fach_ID_FK = addForeignKey(
			"K_Ankreuzfloskeln_Fach_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Ankreuzfloskeln.
	 */
	public Tabelle_K_Ankreuzfloskeln() {
		super("K_Ankreuzfloskeln", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.grundschule");
		setJavaClassName("DTOAnkreuzfloskeln");
		setJavaComment("Einzelne Textbausteine, die den Fächer dann als Ankreuzkompetenzen zugewiesen werden können");
	}

}
