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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Kompetenzen.
 */
public class Tabelle_Kompetenzen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte KO_ID */
	public SchemaTabelleSpalte col_KO_ID = add("KO_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID für die Berechtigungskompetenz");

	/** Die Definition der Tabellenspalte KO_Gruppe */
	public SchemaTabelleSpalte col_KO_Gruppe = add("KO_Gruppe", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Gruppe der Berechtigungskompetenz");

	/** Die Definition der Tabellenspalte KO_Bezeichnung */
	public SchemaTabelleSpalte col_KO_Bezeichnung = add("KO_Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(64)
		.setNotNull()
		.setJavaComment("Bezeichnung der Berechtigungskompetenz");


	/** Die Definition des Fremdschlüssels Kompetenzen_Kompetenzgruppen_FK */
	public SchemaTabelleFremdschluessel fk_Kompetenzen_Kompetenzgruppen_FK = addForeignKey(
			"Kompetenzen_Kompetenzgruppen_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_KO_Gruppe, Schema.tab_Kompetenzgruppen.col_KG_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Kompetenzen.
	 */
	public Tabelle_Kompetenzen() {
		super("Kompetenzen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOKatalogBenutzerKompetenz");
		setJavaComment("Rechte für die Benutzerverwaltung von Schild-NRW");
	}

}
