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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerAnrechnung.
 */
public class Tabelle_LehrerAnrechnung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID für den Eintrag für die Anrechnungsstunden");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Lehrer-ID für die Anrechnungsstunden, in LehrerAbchnittsdaten enthalten");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Lehrerabschnittsdaten");

	/** Die Definition der Tabellenspalte AnrechnungsgrundKrz */
	public SchemaTabelleSpalte col_AnrechnungsgrundKrz = add("AnrechnungsgrundKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Anrechnungsstundentext  für die Anrechnungsstunden");

	/** Die Definition der Tabellenspalte AnrechnungStd */
	public SchemaTabelleSpalte col_AnrechnungStd = add("AnrechnungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Zahl der Anrechnungsstunden für die Anrechnungsstunden");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr für die Anrechnungsstunden");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnit für die Anrechnungsstunden");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels LehrerAnrechnung_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_LehrerAnrechnung_Abschnitt_FK = addForeignKey(
			"LehrerAnrechnung_Abschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abschnitt_ID, Schema.tab_LehrerAbschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels LehrerAnrechnung_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerAnrechnung_Lehrer_FK = addForeignKey(
			"LehrerAnrechnung_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setVeraltet(SchemaRevisionen.REV_1);


	/** Die Definition des Unique-Index LehrerAnrechnung_UC1 */
	public SchemaTabelleUniqueIndex unique_LehrerAnrechnung_UC1 = addUniqueIndex("LehrerAnrechnung_UC1",
			col_Abschnitt_ID,
			col_AnrechnungsgrundKrz
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerAnrechnung.
	 */
	public Tabelle_LehrerAnrechnung() {
		super("LehrerAnrechnung", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerAnrechnungsstunde");
		setJavaComment("Anrechnungsstunden für Lehrkräfte gültige ASD-Schlüssel");
	}

}
