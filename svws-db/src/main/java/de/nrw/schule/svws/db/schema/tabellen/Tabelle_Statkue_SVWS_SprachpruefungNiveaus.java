package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statkue_SVWS_SprachpruefungNiveaus.
 */
public class Tabelle_Statkue_SVWS_SprachpruefungNiveaus extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Niveaus der Sprachprüfung");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(16)
		.setNotNull()
		.setJavaComment("Bezeichnung des Niveaus der Sprachprüfung");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(200)
		.setNotNull()
		.setJavaComment("Beschreibung des Niveaus der Sprachprüfung");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Sortierung der Niveaus nach Anspruchshöhe");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Das Schuljahr, ab dem das Niveau eingeführt wurde");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Das Schuljahr, bis wann das Niveau gültig ist");


	/** Die Definition des Unique-Index Statkue_SVWS_SprachpruefungNiveaus_UC1 */
	public SchemaTabelleUniqueIndex unique_Statkue_SVWS_SprachpruefungNiveaus_UC1 = addUniqueIndex("Statkue_SVWS_SprachpruefungNiveaus_UC1", 
			col_Bezeichnung
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statkue_SVWS_SprachpruefungNiveaus.
	 */
	public Tabelle_Statkue_SVWS_SprachpruefungNiveaus() {
		super("Statkue_SVWS_SprachpruefungNiveaus", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("statkue");
		setJavaClassName("DTOSVWSSprachpruefungNiveaus");
		setJavaComment("Statkue SVWS selbst generierte Tabelle für die Sprachprüfungsniveaus");
	}

}
