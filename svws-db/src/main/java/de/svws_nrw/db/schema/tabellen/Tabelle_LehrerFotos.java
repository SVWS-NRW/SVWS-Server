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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerFotos.
 */
public class Tabelle_LehrerFotos extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("LehrerID zu der das Foto gehört");

	/** Die Definition der Tabellenspalte Foto */
	public SchemaTabelleSpalte col_Foto = add("Foto", SchemaDatentypen.LONGBLOB, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Lehrerfoto im binär-Format");

	/** Die Definition der Tabellenspalte FotoBase64 */
	public SchemaTabelleSpalte col_FotoBase64 = add("FotoBase64", SchemaDatentypen.TEXT, false)
		.setJavaComment("Lehrerfoto im Base64-Format");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerFotos_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerFotos_Lehrer_FK = addForeignKey(
			"LehrerFotos_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerFotos.
	 */
	public Tabelle_LehrerFotos() {
		super("LehrerFotos", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerFoto");
		setJavaComment("Blobfelder für Lehrkräfte-Fotos");
	}

}
