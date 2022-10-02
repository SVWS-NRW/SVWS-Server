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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle BenutzerKompetenzen.
 */
public class Tabelle_BenutzerKompetenzen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Benutzer_ID */
	public SchemaTabelleSpalte col_Benutzer_ID = add("Benutzer_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des Benutzers");

	/** Die Definition der Tabellenspalte Kompetenz_ID */
	public SchemaTabelleSpalte col_Kompetenz_ID = add("Kompetenz_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID der zugeordneten Kompetenz");


	/** Die Definition des Fremdschlüssels BenutzerKompetenzen_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_BenutzerKompetenzen_Benutzer_FK = addForeignKey(
			"BenutzerKompetenzen_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Benutzer_ID, Schema.tab_Benutzer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels BenutzerKompetenzen_Kompetenzen_FK */
	public SchemaTabelleFremdschluessel fk_BenutzerKompetenzen_Kompetenzen_FK = addForeignKey(
			"BenutzerKompetenzen_Kompetenzen_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Kompetenz_ID, Schema.tab_Kompetenzen.col_KO_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle BenutzerKompetenzen.
	 */
	public Tabelle_BenutzerKompetenzen() {
		super("BenutzerKompetenzen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOBenutzerKompetenz");
		setJavaComment("Die Kompetenzen, welchem dem Benutzer insgesamt zugeordnet wurden - über eien Gruppe oder individuell");
	}

}
