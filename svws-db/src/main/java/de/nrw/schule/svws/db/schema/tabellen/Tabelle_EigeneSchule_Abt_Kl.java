package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Abt_Kl.
 */
public class Tabelle_EigeneSchule_Abt_Kl extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klassenzugehörigkeit zu einer Abteilung");

	/** Die Definition der Tabellenspalte Abteilung_ID */
	public SchemaTabelleSpalte col_Abteilung_ID = add("Abteilung_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Abteilung in der übergeordneten Tabelle");

	/** Die Definition der Tabellenspalte Klassen_ID */
	public SchemaTabelleSpalte col_Klassen_ID = add("Klassen_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setRevision(SchemaRevisionen.REV_1)
		.setJavaComment("ID der Klasse die zur Abteilung gehört");

	/** Die Definition der Tabellenspalte Klasse */
	public SchemaTabelleSpalte col_Klasse = add("Klasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Klassenbezeichnung die zur Abteilung gehört");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("steuert die Sichtbarkeit der Klasse zur Abteilung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels EigeneSchule_Abt_Kl_Abteilung_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Abt_Kl_Abteilung_FK = addForeignKey(
			"EigeneSchule_Abt_Kl_Abteilung_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abteilung_ID, Schema.tab_EigeneSchule_Abteilungen.col_ID)
		);

	/** Die Definition des Fremdschlüssels EigeneSchule_Abt_Kl_Klassen_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Abt_Kl_Klassen_FK = addForeignKey(
			"EigeneSchule_Abt_Kl_Klassen_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Klassen_ID, Schema.tab_Klassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Abt_Kl.
	 */
	public Tabelle_EigeneSchule_Abt_Kl() {
		super("EigeneSchule_Abt_Kl", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAbteilungsKlassen");
		setJavaComment("Tabelle ordnet den Abteilungen (EigeneSchule_Abteilungen) die zugehörigen Klassen zu");
	}

}
