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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerAnkreuzfloskeln.
 */
public class Tabelle_SchuelerAnkreuzfloskeln extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Ankreuzfloskeleintrags");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Schüler-ID zum Ankreuzfloskeleintrag, in Abschnitt_ID enthalten");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der zugehörigen Schülerlernabschnittsdaten");

	/** Die Definition der Tabellenspalte Floskel_ID */
	public SchemaTabelleSpalte col_Floskel_ID = add("Floskel_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Ankreuzfloskel aus dem Katalog");

	/** Die Definition der Tabellenspalte Stufe1 */
	public SchemaTabelleSpalte col_Stufe1 = add("Stufe1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob diese Stufe angekreuzt ist");

	/** Die Definition der Tabellenspalte Stufe2 */
	public SchemaTabelleSpalte col_Stufe2 = add("Stufe2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob diese Stufe angekreuzt ist");

	/** Die Definition der Tabellenspalte Stufe3 */
	public SchemaTabelleSpalte col_Stufe3 = add("Stufe3", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob diese Stufe angekreuzt ist");

	/** Die Definition der Tabellenspalte Stufe4 */
	public SchemaTabelleSpalte col_Stufe4 = add("Stufe4", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob diese Stufe angekreuzt ist");

	/** Die Definition der Tabellenspalte Stufe5 */
	public SchemaTabelleSpalte col_Stufe5 = add("Stufe5", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob diese Stufe angekreuzt ist");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr zum Ankreusfloskeleintrag");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt zum Ankreusfloskeleintrag");


	/** Die Definition des Fremdschlüssels SchuelerAnkreuzfloskeln_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAnkreuzfloskeln_Abschnitt_FK = addForeignKey(
			"SchuelerAnkreuzfloskeln_Abschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerAnkreuzfloskeln_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAnkreuzfloskeln_Schueler_FK = addForeignKey(
			"SchuelerAnkreuzfloskeln_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		)
		.setVeraltet(SchemaRevisionen.REV_1);

	/** Die Definition des Fremdschlüssels SchuelerAnkreuzfloskeln_Floskel_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerAnkreuzfloskeln_Floskel_FK = addForeignKey(
			"SchuelerAnkreuzfloskeln_Floskel_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Floskel_ID, Schema.tab_K_Ankreuzfloskeln.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerAnkreuzfloskeln.
	 */
	public Tabelle_SchuelerAnkreuzfloskeln() {
		super("SchuelerAnkreuzfloskeln", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.grundschule");
		setJavaClassName("DTOSchuelerAnkreuzfloskeln");
		setJavaComment("Ankreuzfloskeln zum Schüler mit Jahr, Abschnitt und Floskel_ID");
	}

}
