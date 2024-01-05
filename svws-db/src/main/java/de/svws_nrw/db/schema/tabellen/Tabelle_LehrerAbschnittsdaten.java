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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerAbschnittsdaten.
 */
public class Tabelle_LehrerAbschnittsdaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Eintrags für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("LehrerID für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte Rechtsverhaeltnis */
	public SchemaTabelleSpalte col_Rechtsverhaeltnis = add("Rechtsverhaeltnis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Rechtsverhältnis für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte Beschaeftigungsart */
	public SchemaTabelleSpalte col_Beschaeftigungsart = add("Beschaeftigungsart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Beschäftigungsart für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte Einsatzstatus */
	public SchemaTabelleSpalte col_Einsatzstatus = add("Einsatzstatus", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Einsatzstatus für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte StammschulNr */
	public SchemaTabelleSpalte col_StammschulNr = add("StammschulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Die Schulnummer der Stammschule, sofern diese abweicht");

	/** Die Definition der Tabellenspalte PflichtstdSoll */
	public SchemaTabelleSpalte col_PflichtstdSoll = add("PflichtstdSoll", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Pflichtstundensoll für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte UnterrichtsStd */
	public SchemaTabelleSpalte col_UnterrichtsStd = add("UnterrichtsStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Unterichsstunden für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte MehrleistungStd */
	public SchemaTabelleSpalte col_MehrleistungStd = add("MehrleistungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Mehrleistungsstunden für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte EntlastungStd */
	public SchemaTabelleSpalte col_EntlastungStd = add("EntlastungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Entlastungsstunden für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte AnrechnungStd */
	public SchemaTabelleSpalte col_AnrechnungStd = add("AnrechnungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Anrechnungstunden für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte RestStd */
	public SchemaTabelleSpalte col_RestStd = add("RestStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Reststunden die nicht vergeben wurden  für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr für die LehrerAbschnittsdaten");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnit für die LehrerAbschnittsdaten");


	/** Die Definition des Fremdschlüssels LehrerAbschnittsdaten_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_LehrerAbschnittsdaten_Schuljahreabschnitt_FK = addForeignKey(
			"LehrerAbschnittsdaten_Schuljahreabschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels LehrerAbschnittsdaten_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_LehrerAbschnittsdaten_Lehrer_FK = addForeignKey(
			"LehrerAbschnittsdaten_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/** Die Definition des Unique-Index LehrerAbschnittsdaten_UC1 */
	public SchemaTabelleUniqueIndex unique_LehrerAbschnittsdaten_UC1 = addUniqueIndex("LehrerAbschnittsdaten_UC1",
			col_Schuljahresabschnitts_ID,
			col_Lehrer_ID
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle LehrerAbschnittsdaten.
	 */
	public Tabelle_LehrerAbschnittsdaten() {
		super("LehrerAbschnittsdaten", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerAbschnittsdaten");
		setJavaComment("Lehrerdaten, die Abschnittsweise gespeichert werden");
	}

}
