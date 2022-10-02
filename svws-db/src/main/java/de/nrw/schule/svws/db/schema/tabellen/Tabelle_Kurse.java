package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Kurse.
 */
public class Tabelle_Kurse extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Kurses");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte KurzBez */
	public SchemaTabelleSpalte col_KurzBez = add("KurzBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Kursbezeichnung des Kurses");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Jahrgangs_ID des Kurses");

	/** Die Definition der Tabellenspalte ASDJahrgang */
	public SchemaTabelleSpalte col_ASDJahrgang = add("ASDJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("ASD-Kürzel des Jahrgangs des Kurses");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Fach_ID des Kurses");

	/** Die Definition der Tabellenspalte KursartAllg */
	public SchemaTabelleSpalte col_KursartAllg = add("KursartAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Allgemeine Kursart des Kurses");

	/** Die Definition der Tabellenspalte WochenStd */
	public SchemaTabelleSpalte col_WochenStd = add("WochenStd", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Wochenstunden des Kurses");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Lehrer-ID des unterrichtenden Lehrers des Kurses");

	/** Die Definition der Tabellenspalte LehrerKrz */
	public SchemaTabelleSpalte col_LehrerKrz = add("LehrerKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(22)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Lehrerkürzel des unterrichtenden Lehrers des Kurses");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierung des Kurses");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Sichtbarkeit des Kurses");

	/** Die Definition der Tabellenspalte Schienen */
	public SchemaTabelleSpalte col_Schienen = add("Schienen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Auflistung der Schienen in denen der Kurs ist");

	/** Die Definition der Tabellenspalte Fortschreibungsart */
	public SchemaTabelleSpalte col_Fortschreibungsart = add("Fortschreibungsart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setConverter("KursFortschreibungsartConverter")
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Fortschreibungsart des Kurses für die Hochschreibung in den nächsten Abschnitt");

	/** Die Definition der Tabellenspalte WochenstdKL */
	public SchemaTabelleSpalte col_WochenstdKL = add("WochenstdKL", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Wochenstunden des Kurslehrers");

	/** Die Definition der Tabellenspalte SchulNr */
	public SchemaTabelleSpalte col_SchulNr = add("SchulNr", SchemaDatentypen.INT, false)
		.setJavaComment("Schulnummer des Kurses bei externen Kursen nötig");

	/** Die Definition der Tabellenspalte EpochU */
	public SchemaTabelleSpalte col_EpochU = add("EpochU", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob ein Kurs Epochal unterrichtet wird");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte ZeugnisBez */
	public SchemaTabelleSpalte col_ZeugnisBez = add("ZeugnisBez", SchemaDatentypen.VARCHAR, false).setDatenlaenge(130)
		.setJavaComment("Zeugnisbezeichnung des Kurses");

	/** Die Definition der Tabellenspalte Jahrgaenge */
	public SchemaTabelleSpalte col_Jahrgaenge = add("Jahrgaenge", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Auflistung der Jahrgänge wenn Kurs übergreifen");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr des Kurses");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt des Kurses");


	/** Die Definition des Fremdschlüssels Kurse_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_Kurse_Schuljahreabschnitt_FK = addForeignKey(
			"Kurse_Schuljahreabschnitt_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels Kurse_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Kurse_Jahrgang_FK = addForeignKey(
			"Kurse_Jahrgang_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Kurse_Fach_FK */
	public SchemaTabelleFremdschluessel fk_Kurse_Fach_FK = addForeignKey(
			"Kurse_Fach_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Kurse_Fortschreibungsart_FK */
	public SchemaTabelleFremdschluessel fk_Kurse_Fortschreibungsart_FK = addForeignKey(
			"Kurse_Fortschreibungsart_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_Fortschreibungsart, Schema.tab_KursFortschreibungsarten.col_Kuerzel)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Unique-Index Kurse_UC1 */
	public SchemaTabelleUniqueIndex unique_Kurse_UC1 = addUniqueIndex("Kurse_UC1", 
			col_Jahrgaenge, 
			col_Schuljahresabschnitts_ID, 
			col_Fach_ID, 
			col_ASDJahrgang, 
			col_KursartAllg, 
			col_Lehrer_ID, 
			col_WochenStd, 
			col_KurzBez
		)
		.setRevision(SchemaRevisionen.REV_1);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Kurse.
	 */
	public Tabelle_Kurse() {
		super("Kurse", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.kurse");
		setJavaClassName("DTOKurs");
		setJavaComment("Tabelle der Kurse");
	}

}
