package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerLD_PSFachBem.
 */
public class Tabelle_SchuelerLD_PSFachBem extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Bemerkungseintrags");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Lernabschnittes");

	/** Die Definition der Tabellenspalte ASV */
	public SchemaTabelleSpalte col_ASV = add("ASV", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text zum Arvbeits und Sozialverhalten");

	/** Die Definition der Tabellenspalte LELS */
	public SchemaTabelleSpalte col_LELS = add("LELS", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text zur Lernentwicklung bei Grundschulen");

	/** Die Definition der Tabellenspalte AUE */
	public SchemaTabelleSpalte col_AUE = add("AUE", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text zum Außerunterrichtlichen Engagement (früher in LELS integeriert, falls, die Schulform keine Grundschule ist)");

	/** Die Definition der Tabellenspalte ESF */
	public SchemaTabelleSpalte col_ESF = add("ESF", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für die \"Empfehlung der Schulform\" beim Übergang von Primar- nach SekI");

	/** Die Definition der Tabellenspalte BemerkungFSP */
	public SchemaTabelleSpalte col_BemerkungFSP = add("BemerkungFSP", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Förderschwerpunktbemerkung");

	/** Die Definition der Tabellenspalte BemerkungVersetzung */
	public SchemaTabelleSpalte col_BemerkungVersetzung = add("BemerkungVersetzung", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Verstungsentscheidung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerLD_PSFachBem_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerLD_PSFachBem_Abschnitt_FK = addForeignKey(
			"SchuelerLD_PSFachBem_Abschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
		);


	/** Die Definition des Unique-Index SchuelerLD_PSFachBem_UC1 */
	public SchemaTabelleUniqueIndex unique_SchuelerLD_PSFachBem_UC1 = addUniqueIndex("SchuelerLD_PSFachBem_UC1", 
			col_Abschnitt_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerLD_PSFachBem.
	 */
	public Tabelle_SchuelerLD_PSFachBem() {
		super("SchuelerLD_PSFachBem", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerPSFachBemerkungen");
		setJavaComment("Einträge bei den Lernabschnittsdaten ASV, AUE Zeugnisbemerkungen zum Schüler");
	}

}
