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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Stundenplan_Aufsichtsbereiche.
 */
public class Tabelle_Stundenplan_Aufsichtsbereiche extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID identifiziert einen Aufsichtsbereicheintrag für einen Stundenplan eindeutig - hat keinen Bezug zur ID der Katalog-Tabelle");

	/** Die Definition der Tabellenspalte Stundenplan_ID */
	public SchemaTabelleSpalte col_Stundenplan_ID = add("Stundenplan_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Die ID des Stundenplans, dem dieser Aufsichtsbereicheintrag zugeordnet wird");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Die Kurzbezeichnung des Aufsichtsbereichs");

	/** Die Definition der Tabellenspalte Beschreibung */
	public SchemaTabelleSpalte col_Beschreibung = add("Beschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1000)
		.setNotNull()
		.setJavaComment("Gegebenenfalls eine ausführlichere Beschreibung des Aufsichtsbereichs");


	/** Die Definition des Fremdschlüssels Stundenplan_Aufsichtsbereiche_Stundenplan_FK */
	public SchemaTabelleFremdschluessel fk_Stundenplan_Aufsichtsbereiche_Stundenplan_FK = addForeignKey(
			"Stundenplan_Aufsichtsbereiche_Stundenplan_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Stundenplan_ID, Schema.tab_Stundenplan.col_ID)
		);


	/** Die Definition des Unique-Index Stundenplan_Aufsichtsbereiche_UC1 */
	public SchemaTabelleUniqueIndex unique_Stundenplan_Aufsichtsbereiche_UC1 = addUniqueIndex("Stundenplan_Aufsichtsbereiche_UC1", 
			col_Stundenplan_ID, 
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Stundenplan_Aufsichtsbereiche.
	 */
	public Tabelle_Stundenplan_Aufsichtsbereiche() {
		super("Stundenplan_Aufsichtsbereiche", SchemaRevisionen.REV_5);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.stundenplan");
		setJavaClassName("DTOStundenplanAufsichtsbereich");
		setJavaComment("Enthält die Liste von Aufsichtsbereichen, welche im Stundenplan verwendet werden. Diese werden üblicherweise aus der Tabelle Katalog_Aufsichtsbereich übernommen und hier zwischengespeichert. Änderungen im Katalog werden nicht hierhin übernommen.");
	}

}
