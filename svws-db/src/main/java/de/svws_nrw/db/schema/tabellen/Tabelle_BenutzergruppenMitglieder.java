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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle BenutzergruppenMitglieder.
 */
public class Tabelle_BenutzergruppenMitglieder extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Gruppe_ID */
	public SchemaTabelleSpalte col_Gruppe_ID = add("Gruppe_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der Benutzergruppe");

	/** Die Definition der Tabellenspalte Benutzer_ID */
	public SchemaTabelleSpalte col_Benutzer_ID = add("Benutzer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des Benutzers");


	/** Die Definition des Fremdschlüssels BenutzergruppenMitglieder_Benutzergruppen_FK */
	public SchemaTabelleFremdschluessel fk_BenutzergruppenMitglieder_Benutzergruppen_FK = addForeignKey(
			"BenutzergruppenMitglieder_Benutzergruppen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Gruppe_ID, Schema.tab_Benutzergruppen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels BenutzergruppenMitglieder_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_BenutzergruppenMitglieder_Benutzer_FK = addForeignKey(
			"BenutzergruppenMitglieder_Benutzer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Benutzer_ID, Schema.tab_Benutzer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle BenutzergruppenMitglieder.
	 */
	public Tabelle_BenutzergruppenMitglieder() {
		super("BenutzergruppenMitglieder", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOBenutzergruppenMitglied");
		setJavaComment("Die Zuordnung von Benutzern zu Benutzergruppen");
	}

}
