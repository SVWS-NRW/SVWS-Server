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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulbewerbung_Importe.
 */
public class Tabelle_Schulbewerbung_Importe extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Schüler-ID des Schülers, für welchen die Import-Daten speichert werden");

	/** Die Definition der Tabellenspalte LastSync */
	public SchemaTabelleSpalte col_LastSync = add("LastSync", SchemaDatentypen.DATETIME, false)
	    .setDatenlaenge(3)
		.setNotNull()
		.setJavaComment("Der Zeitstempel der letzten Synchronisation der Daten mit schulbewerbung.de");

	/** Die Definition der Tabellenspalte LastXML */
	public SchemaTabelleSpalte col_LastXML = add("LastXML", SchemaDatentypen.TEXT, false)
		.setNotNull()
		.setJavaComment("Das XML der letzten Synchronisation der Daten mit schulbewerbung.de");


	/** Die Definition des Fremdschlüssels Schulbewerbung_Importe_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_Schulbewerbung_Importe_Schueler_FK = addForeignKey(
		"Schulbewerbung_Importe_Schueler_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulbewerbung_Importe.
	 */
	public Tabelle_Schulbewerbung_Importe() {
		super("Schulbewerbung_Importe", SchemaRevisionen.REV_1);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schulbewerbung");
		setJavaClassName("DTOSchulbewerbungImporte");
		setJavaComment("Die letzten Daten eines Imports von Schulbewerbung.de");
	}

}
