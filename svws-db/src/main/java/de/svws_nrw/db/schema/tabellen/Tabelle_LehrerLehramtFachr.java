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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerLehramtFachr.
 */
public class Tabelle_LehrerLehramtFachr extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("LehrerID zu der die Fachrichtung gehört");

	/** Die Definition der Tabellenspalte FachrKrz */
	public SchemaTabelleSpalte col_FachrKrz = add("FachrKrz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setJavaComment("Fachrichtungskürzel");

	/** Die Definition der Tabellenspalte FachrAnerkennungKrz */
	public SchemaTabelleSpalte col_FachrAnerkennungKrz = add("FachrAnerkennungKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("FachrichtungAnerkennungskürzel");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerLehramtFachr_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerLehramtFachr_Lehrer_FK = addForeignKey(
			"LehrerLehramtFachr_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerLehramtFachr.
	 */
	public Tabelle_LehrerLehramtFachr() {
		super("LehrerLehramtFachr", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerLehramtFachrichtung");
		setJavaComment("Fachrichtung gültige Schlüssel zur Lehrkraft");
	}

}
