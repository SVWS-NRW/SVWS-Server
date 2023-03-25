package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan.
 */
public class Tabelle_Stundenplan extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des Stundenplans");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Schuljahresabschnittes des Stundenplans als Fremdschlüssel auf die Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte Beginn */
	public SchemaTabelleSpalte col_Beginn = add("Beginn", SchemaDatentypen.DATE, false)
		.setDefault("1899-01-01")
		.setNotNull()
		.setConverter(DatumConverter.class)
		.setJavaComment("Das Datum, ab dem der Stundenplan gültig ist");

	/** Die Definition der Tabellenspalte Ende */
	public SchemaTabelleSpalte col_Ende = add("Ende", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Das Datum, bis wann der Stundenplan gültig ist - null, wenn kein Ende innerhalb des Abschnitts festgelegt wurde (letzter Stundenplan im Abschnitt)");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
		.setNotNull()
		.setJavaComment("Eine Beschreibung / Kommentar zu diesem Stundenplan");

	/** Die Definition der Tabellenspalte WochentypModell */
	public SchemaTabelleSpalte col_WochentypModell = add("WochentypModell", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setJavaComment("Gibt das Modell für die Wochen an, d.h. ob es sich um einen Stundenplan für jede Woche handelt (0) oder ob es sich um einen unterschiedliche Stundenpläne in Abhängigkeit des Wochentyps handelt - z.B. A-/B-Wochen (2) handelt. Hier wird dann die Anzahl der unterschiedlichen Wochentypen festgelegt.");
	

	/** Die Definition des Fremdschlüssels Stundenplan_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Schuljahreabschnitt_FK = addForeignKey(
			"Stundenplan_Schuljahreabschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan.
	 */
	public Tabelle_Stundenplan() {
		super("Stundenplan", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplan");
		setJavaComment("Enthält die Grunddaten für einen Stundenplan, welcher anhand seiner ID eindeutig identifiziert werden kann. Hierzu zählen zum Einen das Schuljahr und der Abschnitt und zum Anderen der Beginn und das Ende der Gültigkeit des Stundenplans in dem Halbjahr");
	}

}
