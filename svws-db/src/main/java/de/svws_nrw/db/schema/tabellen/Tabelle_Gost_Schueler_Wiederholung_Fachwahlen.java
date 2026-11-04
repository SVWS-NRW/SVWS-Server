package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Schueler_Wiederholung_Fachwahlen.
 */
public class Tabelle_Gost_Schueler_Wiederholung_Fachwahlen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: ID des Schülers in der Schuelertabelle");

	/** Die Definition der Tabellenspalte Fach_ID */
	public final SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: ID des Faches in der Fächertabelle");

	/** Die Definition der Tabellenspalte EF1_Kursart */
	public final SchemaTabelleSpalte col_EF1_Kursart = add("EF1_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Kursart des belegten Faches in EF.1");

	/** Die Definition der Tabellenspalte EF2_Kursart */
	public final SchemaTabelleSpalte col_EF2_Kursart = add("EF2_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Kursart des belegten Faches in EF.2");

	/** Die Definition der Tabellenspalte Q11_Kursart */
	public final SchemaTabelleSpalte col_Q11_Kursart = add("Q11_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Kursart des belegten Faches in Q1.1");

	/** Die Definition der Tabellenspalte Q12_Kursart */
	public final SchemaTabelleSpalte col_Q12_Kursart = add("Q12_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Kursart des belegten Faches in Q1.2");

	/** Die Definition der Tabellenspalte Q21_Kursart */
	public final SchemaTabelleSpalte col_Q21_Kursart = add("Q21_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Kursart des belegten Faches in Q2.1");

	/** Die Definition der Tabellenspalte Q22_Kursart */
	public final SchemaTabelleSpalte col_Q22_Kursart = add("Q22_Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Kursart des belegten Faches in Q2.2");

	/** Die Definition der Tabellenspalte AbiturFach */
	public final SchemaTabelleSpalte col_AbiturFach = add("AbiturFach", SchemaDatentypen.INT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Abiturfach 1 bis 4 (bzw. 5) oder null");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public final SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: Bemerkungen zum belegten Fach");

	/** Die Definition der Tabellenspalte Referenzfach_ID */
	public final SchemaTabelleSpalte col_Referenzfach_ID = add("Referenzfach_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung - Fachwahlen: ID des Referenzfachs für den Projektkurs");


	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_Fachwahlen_Schueler_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_Fachwahlen_Schueler_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_Fachwahlen_Schueler_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Gost_Schueler_Wiederholung.col_Schueler_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_Fachwahlen_Fach_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_Fachwahlen_Fach_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_Fachwahlen_Fach_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_Fachwahlen_Referenzfach_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_Fachwahlen_Referenzfach_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_Fachwahlen_Referenzfach_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Referenzfach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Schueler_Wiederholung_Fachwahlen.
	 */
	public Tabelle_Gost_Schueler_Wiederholung_Fachwahlen() {
		super("Gost_Schueler_Wiederholung_Fachwahlen", SchemaRevisionen.REV_82);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostSchuelerWiederholungFachwahlen");
		setJavaComment("Gymnasiale Oberstufe - Wiederholung: Fachwahlen des Schülers für den Abiturjahrgang der Wiederholung");
	}

}
