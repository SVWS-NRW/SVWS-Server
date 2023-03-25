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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_NtaZeiten.
 */
public class Tabelle_Gost_Klausuren_NtaZeiten extends SchemaTabelle {
	
	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Schülers");

	/** Die Definition der Tabellenspalte Vorgabe_ID */
	public SchemaTabelleSpalte col_Vorgabe_ID = add("Vorgabe_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausurvorgaben");

	/** Die Definition der Tabellenspalte Zeitzugabe */
	public SchemaTabelleSpalte col_Zeitzugabe = add("Zeitzugabe", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Das Dauer der Zeitzugabe in Minuten");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Ergänzungen/Bemerkungen zum Nachteilsausgleich");
	
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_NtaZeiten_Schueler_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_NtaZeiten_Schueler_ID_FK = addForeignKey(
			"Gost_Klausuren_NtaZeiten_Schueler_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_NtaZeiten_Vorgabe_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_NtaZeiten_Vorgabe_ID_FK = addForeignKey(
			"Gost_Klausuren_NtaZeiten_Vorgabe_ID_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Vorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_NtaZeiten.
	 */
	public Tabelle_Gost_Klausuren_NtaZeiten() {
		super("Gost_Klausuren_NtaZeiten", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenNtaZeiten");
		setJavaComment("Tabelle für die Definition von Nachteilsausgleichen");
	}

}
