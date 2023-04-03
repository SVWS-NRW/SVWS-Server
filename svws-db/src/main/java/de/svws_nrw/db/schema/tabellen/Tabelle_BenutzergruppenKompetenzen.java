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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle BenutzergruppenKompetenzen.
 */
public class Tabelle_BenutzergruppenKompetenzen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Gruppe_ID */
	public SchemaTabelleSpalte col_Gruppe_ID = add("Gruppe_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der Benutzergruppe");

	/** Die Definition der Tabellenspalte Kompetenz_ID */
	public SchemaTabelleSpalte col_Kompetenz_ID = add("Kompetenz_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der zugeordneten Kompetenz");


	/** Die Definition des Fremdschlüssels BenutzergruppenKompetenzen_Benutzergruppen_FK */
	public SchemaTabelleFremdschluessel fk_BenutzergruppenKompetenzen_Benutzergruppen_FK = addForeignKey(
			"BenutzergruppenKompetenzen_Benutzergruppen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Gruppe_ID, Schema.tab_Benutzergruppen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels BenutzergruppenKompetenzen_Kompetenzen_FK */
	public SchemaTabelleFremdschluessel fk_BenutzergruppenKompetenzen_Kompetenzen_FK = addForeignKey(
			"BenutzergruppenKompetenzen_Kompetenzen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Kompetenz_ID, Schema.tab_Kompetenzen.col_KO_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle BenutzergruppenKompetenzen.
	 */
	public Tabelle_BenutzergruppenKompetenzen() {
		super("BenutzergruppenKompetenzen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOBenutzergruppenKompetenz");
		setJavaComment("Die Kompetenzen, welche der Benutzergruppe - also der Vorlage - zugeordnet wurden.");
	}

}
