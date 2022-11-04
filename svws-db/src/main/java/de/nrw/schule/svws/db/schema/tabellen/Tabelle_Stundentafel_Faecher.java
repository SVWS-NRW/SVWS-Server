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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundentafel_Faecher.
 */
public class Tabelle_Stundentafel_Faecher extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Facheintrags für die Stundentafel");

	/** Die Definition der Tabellenspalte Stundentafel_ID */
	public SchemaTabelleSpalte col_Stundentafel_ID = add("Stundentafel_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der zugehörigen Stundentafel");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("FachID das in der Stundentafel ist");

	/** Die Definition der Tabellenspalte KursartAllg */
	public SchemaTabelleSpalte col_KursartAllg = add("KursartAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kursart des Faches in der Stundentafel");

	/** Die Definition der Tabellenspalte WochenStd */
	public SchemaTabelleSpalte col_WochenStd = add("WochenStd", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Wochenstunde des Faches in der Stundentafel");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Lehrer-ID des unterrichtenden Lehrers für das Fach der Stundentafel");

	/** Die Definition der Tabellenspalte EpochenUnterricht */
	public SchemaTabelleSpalte col_EpochenUnterricht = add("EpochenUnterricht", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Merkmal Epochenunterricht des Faches in der Stundentafel");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung des Faches in der Stundentafel");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Sichtbarkeit des Faches in der Stundentafel");

	/** Die Definition der Tabellenspalte Gewichtung */
	public SchemaTabelleSpalte col_Gewichtung = add("Gewichtung", SchemaDatentypen.INT, false)
		.setDefault("1")
		.setJavaComment("??? entweder BB auch oder weg ??? Gewichtung allgemeinbidend BK  des Faches in der Stundentafel");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte LehrerKrz */
	public SchemaTabelleSpalte col_LehrerKrz = add("LehrerKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: LehrerKRZ des Faches in der Stundetafel");


	/** Die Definition des Fremdschlüssels StundentafelFaecher_Faecher_FK */
	public SchemaTabelleFremdschluessel fk_StundentafelFaecher_Faecher_FK = addForeignKey(
			"StundentafelFaecher_Faecher_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels StundentafelFaecher_Stdtafel_FK */
	public SchemaTabelleFremdschluessel fk_StundentafelFaecher_Stdtafel_FK = addForeignKey(
			"StundentafelFaecher_Stdtafel_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Stundentafel_ID, Schema.tab_Stundentafel.col_ID)
		);


	/** Die Definition des Unique-Index Stundentafel_Faecher_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundentafel_Faecher_UC1 = addUniqueIndex("Stundentafel_Faecher_UC1", 
			col_Fach_ID, 
			col_Stundentafel_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundentafel_Faecher.
	 */
	public Tabelle_Stundentafel_Faecher() {
		super("Stundentafel_Faecher", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOStundentafelFaecher");
		setJavaComment("Fächereinträge zu den Stundentafeln");
	}

}
