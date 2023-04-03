package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Raeume.
 */
public class Tabelle_Stundenplan_Raeume extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID identifiziert einen Raumeintrag für einen Stundenplan eindeutig - hat keinen Bezug zur ID der Katalog-Tabelle");

	/** Die Definition der Tabellenspalte Stundenplan_ID */
	public SchemaTabelleSpalte col_Stundenplan_ID = add("Stundenplan_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Stundenplans, dem dieser Raumeintrag zugeordnet wird");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Das Kürzel des Raums");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
		.setNotNull()
		.setJavaComment("Gegebenenfalls eine ausführlichere Beschreibung des Raumes");

	/** Die Definition der Tabellenspalte Groesse */
	public SchemaTabelleSpalte col_Groesse = add("Groesse", SchemaDatentypen.INT, false)
		.setDefault("40")
		.setNotNull()
		.setJavaComment("Die Größe des Raumes, d.h. wie viele Schüler hier max. Platz haben");


	/** Die Definition des Fremdschlüssels Stundenplan_Raeume_Stundenplan_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Raeume_Stundenplan_FK = addForeignKey(
			"Stundenplan_Raeume_Stundenplan_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Stundenplan_ID, Schema.tab_Stundenplan.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_Raeume_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_Raeume_UC1 = addUniqueIndex("Stundenplan_Raeume_UC1",
			col_Stundenplan_ID,
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Raeume.
	 */
	public Tabelle_Stundenplan_Raeume() {
		super("Stundenplan_Raeume", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanRaum");
		setJavaComment("Enthält die Liste von Räumen, welche im Stundenplan verwendet werden. Dieser wird üblicherweise aus der Tabelle Katalog_Raeume übernommen und hier zwischengespeichert. Änderungen im Katalog werden nicht hierhin übernommen.");
	}

}
