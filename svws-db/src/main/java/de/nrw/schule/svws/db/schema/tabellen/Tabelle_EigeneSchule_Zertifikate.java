package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Zertifikate.
 */
public class Tabelle_EigeneSchule_Zertifikate extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Zertifikats");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Kürzel des Zertifikats");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung des Zertifikats");

	/** Die Definition der Tabellenspalte Fach */
	public SchemaTabelleSpalte col_Fach = add("Fach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Fachbezeichnung für das Zertifikat");

	/** Die Definition der Tabellenspalte Formatvorlage */
	public SchemaTabelleSpalte col_Formatvorlage = add("Formatvorlage", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Formatforlage für das Zertifikat");


	/** Die Definition des Unique-Index EigeneSchule_Zertifikate_UC1 */
	public SchemaTabelleUniqueIndex unique_EigeneSchule_Zertifikate_UC1 = addUniqueIndex("EigeneSchule_Zertifikate_UC1", 
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Zertifikate.
	 */
	public Tabelle_EigeneSchule_Zertifikate() {
		super("EigeneSchule_Zertifikate", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.berufskolleg");
		setJavaClassName("DTOZertifikate");
		setJavaComment("Liste von Zertifikaten, die angelegt werden können (nur BK)");
	}

}
