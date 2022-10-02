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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerLehramtLehrbef.
 */
public class Tabelle_LehrerLehramtLehrbef extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("LehrerID zu der die Lehrbefähigung gehört");

	/** Die Definition der Tabellenspalte LehrbefKrz */
	public SchemaTabelleSpalte col_LehrbefKrz = add("LehrbefKrz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setJavaComment("Kürzel der Lehrbefähigung");

	/** Die Definition der Tabellenspalte LehrbefAnerkennungKrz */
	public SchemaTabelleSpalte col_LehrbefAnerkennungKrz = add("LehrbefAnerkennungKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kürzel der LehrbefähigungsAnerkennung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerLehramtLehrbef_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerLehramtLehrbef_Lehrer_FK = addForeignKey(
			"LehrerLehramtLehrbef_Lehrer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerLehramtLehrbef.
	 */
	public Tabelle_LehrerLehramtLehrbef() {
		super("LehrerLehramtLehrbef", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerLehramtBefaehigung");
		setJavaComment("Lehramt Lehrbefähigung gültige Schlüssel zur Lehrkraft");
	}

}
