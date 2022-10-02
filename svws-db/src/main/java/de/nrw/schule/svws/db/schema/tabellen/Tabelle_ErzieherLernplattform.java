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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle ErzieherLernplattform.
 */
public class Tabelle_ErzieherLernplattform extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ErzieherID */
	public SchemaTabelleSpalte col_ErzieherID = add("ErzieherID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ErzieherID für den Lernplattform-Datensatz");

	/** Die Definition der Tabellenspalte LernplattformID */
	public SchemaTabelleSpalte col_LernplattformID = add("LernplattformID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Lernplattform");

	/** Die Definition der Tabellenspalte CredentialID */
	public SchemaTabelleSpalte col_CredentialID = add("CredentialID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("CredentialD für den Lernplattform-Datensatz");

	/** Die Definition der Tabellenspalte EinwilligungAbgefragt */
	public SchemaTabelleSpalte col_EinwilligungAbgefragt = add("EinwilligungAbgefragt", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Einwilligung wurde abgefragt");

	/** Die Definition der Tabellenspalte EinwilligungNutzung */
	public SchemaTabelleSpalte col_EinwilligungNutzung = add("EinwilligungNutzung", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Einwilligung zur Nutzung liegt vor");

	/** Die Definition der Tabellenspalte EinwilligungAudiokonferenz */
	public SchemaTabelleSpalte col_EinwilligungAudiokonferenz = add("EinwilligungAudiokonferenz", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Einwilligung zur Audiokonferenz liegt vor");

	/** Die Definition der Tabellenspalte EinwilligungVideokonferenz */
	public SchemaTabelleSpalte col_EinwilligungVideokonferenz = add("EinwilligungVideokonferenz", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Einwilligung zur Videokonferenz liegt vor");


	/** Die Definition des Fremdschlüssels ErzieherLernplattform_Erzieher_FK */
	public SchemaTabelleFremdschluessel fk_ErzieherLernplattform_Erzieher_FK = addForeignKey(
			"ErzieherLernplattform_Erzieher_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_ErzieherID, Schema.tab_SchuelerErzAdr.col_ID)
		);

	/** Die Definition des Fremdschlüssels ErzieherLernplattform_Lernplattform_FK */
	public SchemaTabelleFremdschluessel fk_ErzieherLernplattform_Lernplattform_FK = addForeignKey(
			"ErzieherLernplattform_Lernplattform_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_LernplattformID, Schema.tab_Lernplattformen.col_ID)
		);

	/** Die Definition des Fremdschlüssels ErzieherLernplattform_Credential_FK */
	public SchemaTabelleFremdschluessel fk_ErzieherLernplattform_Credential_FK = addForeignKey(
			"ErzieherLernplattform_Credential_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_CredentialID, Schema.tab_CredentialsLernplattformen.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle ErzieherLernplattform.
	 */
	public Tabelle_ErzieherLernplattform() {
		super("ErzieherLernplattform", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.erzieher");
		setJavaClassName("DTOErzieherLernplattform");
		setJavaComment("Tabelle zur Speicherung der CredentialID und den Einwilligungen zu den Lernplattformen bei den Erziehern");
	}

}
