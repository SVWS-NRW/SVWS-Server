package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle NichtMoeglAbiFachKombi.
 */
public class Tabelle_NichtMoeglAbiFachKombi extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Fach1_ID */
	public SchemaTabelleSpalte col_Fach1_ID = add("Fach1_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("FACH1ID für eine nicht mögliche Kombination");

	/** Die Definition der Tabellenspalte Fach2_ID */
	public SchemaTabelleSpalte col_Fach2_ID = add("Fach2_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("FACH2ID für eine nicht mögliche Kombination");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Kursart1 */
	public SchemaTabelleSpalte col_Kursart1 = add("Kursart1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kursart Fach1");

	/** Die Definition der Tabellenspalte Kursart2 */
	public SchemaTabelleSpalte col_Kursart2 = add("Kursart2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kursart Fach2");

	/** Die Definition der Tabellenspalte PK */
	public SchemaTabelleSpalte col_PK = add("PK", SchemaDatentypen.VARCHAR, true).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Primärschlüssel aus FachIDs und  Minuszeichen");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung der nicht möglichen Kombination");

	/** Die Definition der Tabellenspalte Phase */
	public SchemaTabelleSpalte col_Phase = add("Phase", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Über welche Jahrgangsstufen geht die Kombination nicht");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Nicht mögliche Fächerkombination (-) oder Fächerprofil (+)");


	/** Die Definition des Fremdschlüssels NichtMoeglAbiFachKombi_Fach1_FK */
	public SchemaTabelleFremdschluessel fk_NichtMoeglAbiFachKombi_Fach1_FK = addForeignKey(
			"NichtMoeglAbiFachKombi_Fach1_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach1_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels NichtMoeglAbiFachKombi_Fach2_FK */
	public SchemaTabelleFremdschluessel fk_NichtMoeglAbiFachKombi_Fach2_FK = addForeignKey(
			"NichtMoeglAbiFachKombi_Fach2_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach2_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle NichtMoeglAbiFachKombi.
	 */
	public Tabelle_NichtMoeglAbiFachKombi() {
		super("NichtMoeglAbiFachKombi", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.gost");
		setJavaClassName("DTOFaecherNichtMoeglicheKombination");
		setJavaComment("Nicht mögliche Abiturfach-Kombinationen unter Fächer der Oberstufe bearbeiten");
	}

}
