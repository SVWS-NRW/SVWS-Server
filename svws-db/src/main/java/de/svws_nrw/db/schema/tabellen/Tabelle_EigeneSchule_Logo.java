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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Logo.
 */
public class Tabelle_EigeneSchule_Logo extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_EigeneSchule_ID = add("EigeneSchule_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Datensatzes der eigenen Schule");

	/** Die Definition der Tabellenspalte LogoBase64 */
	public SchemaTabelleSpalte col_LogoBase64 = add("LogoBase64", SchemaDatentypen.TEXT, false)
		.setJavaComment("Das Logo der Schule als Bild im Base64-Format");


	/** Die Definition des Fremdschlüssels EigeneSchule_Logo_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Logo_FK = addForeignKey(
		"EigeneSchule_Logo_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_EigeneSchule_ID, Schema.tab_EigeneSchule.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Logo.
	 */
	public Tabelle_EigeneSchule_Logo() {
		super("EigeneSchule_Logo", SchemaRevisionen.REV_1);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOEigeneSchuleLogo");
		setJavaComment("Tabelle in der das Logo der Schule verwaltet wird. Diese Tabelle darf in einer Einzelinstallation nur eine Zeile haben");
	}

}
