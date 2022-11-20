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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Floskeln.
 */
public class Tabelle_Floskeln extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Kürzel für die Floskel wird beim Import automatisch vergeben");

	/** Die Definition der Tabellenspalte FloskelText */
	public SchemaTabelleSpalte col_FloskelText = add("FloskelText", SchemaDatentypen.TEXT, false)
		.setNotNull()
		.setJavaComment("Text der Floskel");

	/** Die Definition der Tabellenspalte FloskelGruppe */
	public SchemaTabelleSpalte col_FloskelGruppe = add("FloskelGruppe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Gruppe der Floskel");

	/** Die Definition der Tabellenspalte FloskelFach */
	public SchemaTabelleSpalte col_FloskelFach = add("FloskelFach", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Fach bei Fachfloskeln");

	/** Die Definition der Tabellenspalte FloskelNiveau */
	public SchemaTabelleSpalte col_FloskelNiveau = add("FloskelNiveau", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Niveau bei Fachfloskeln");

	/** Die Definition der Tabellenspalte FloskelJahrgang */
	public SchemaTabelleSpalte col_FloskelJahrgang = add("FloskelJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Jahrgang bei Fachfloskeln");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels Floskeln_Floskelgruppe_Floskelgruppen_FK */
	public SchemaTabelleFremdschluessel fk_Floskeln_Floskelgruppe_Floskelgruppen_FK = addForeignKey(
			"Floskeln_Floskelgruppe_Floskelgruppen_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_FloskelGruppe, Schema.tab_Floskelgruppen.col_Kuerzel)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/**
	 * Erstellt die Schema-Defintion für die Tabelle Floskeln.
	 */
	public Tabelle_Floskeln() {
		super("Floskeln", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOFloskeln");
		setJavaComment("Textbausteine, die in den Editoren angezeigt und ausgewählt werden können");
	}

}
