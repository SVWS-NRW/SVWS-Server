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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerListe_Inhalt.
 */
public class Tabelle_SchuelerListe_Inhalt extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Liste_ID */
	public SchemaTabelleSpalte col_Liste_ID = add("Liste_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der individuellen Schülerliste");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("SchülerID des Schülers der zur individuellen Schülerliste gehört");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerListeInhalt_Liste_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerListeInhalt_Liste_FK = addForeignKey(
			"SchuelerListeInhalt_Liste_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Liste_ID, Schema.tab_SchuelerListe.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerListeInhalt_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerListeInhalt_Schueler_FK = addForeignKey(
			"SchuelerListeInhalt_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerListe_Inhalt.
	 */
	public Tabelle_SchuelerListe_Inhalt() {
		super("SchuelerListe_Inhalt", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild");
		setJavaClassName("DTOSchuelerIndividuelleGruppeSchueler");
		setJavaComment("Schüler zu den Einträgen in SchuelerListe");
	}

}
