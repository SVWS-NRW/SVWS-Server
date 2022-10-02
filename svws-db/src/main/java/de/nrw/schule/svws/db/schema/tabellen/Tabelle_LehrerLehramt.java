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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerLehramt.
 */
public class Tabelle_LehrerLehramt extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("LehrerID zu der das Lehramt gehört");

	/** Die Definition der Tabellenspalte LehramtKrz */
	public SchemaTabelleSpalte col_LehramtKrz = add("LehramtKrz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setJavaComment("Lehramtskürzel");

	/** Die Definition der Tabellenspalte LehramtAnerkennungKrz */
	public SchemaTabelleSpalte col_LehramtAnerkennungKrz = add("LehramtAnerkennungKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Lehramts-Anerkennung-Kürzel");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerLehramt_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerLehramt_Lehrer_FK = addForeignKey(
			"LehrerLehramt_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerLehramt.
	 */
	public Tabelle_LehrerLehramt() {
		super("LehrerLehramt", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerLehramt");
		setJavaComment("Lehrämter gültige Schlüssel zur Lehrkraft");
	}

}
