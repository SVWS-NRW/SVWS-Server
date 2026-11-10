package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Schueler_Wiederholung.
 */
public class Tabelle_Gost_Schueler_Wiederholung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Die ID des Schülers in der Schülertabelle");

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public final SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Das Abiturjahr des Jahrgangs, in dem der Schüler wiederholt");

	/** Die Definition der Tabellenspalte Halbjahr */
	public final SchemaTabelleSpalte col_Halbjahr = add("Halbjahr", SchemaDatentypen.INT, false)
			.setNotNull()
			.setConverter(GOStHalbjahrConverter.class)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Das Halbjahr im Abiturjahrgang der Wiederholung, mit dem der Schüler dort einsteigt (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)");

	/** Die Definition der Tabellenspalte GKL_EF_AF1_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_EF_AF1_Klausurvorgabe_ID = add("GKL_EF_AF1_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 1");

	/** Die Definition der Tabellenspalte GKL_EF_AF2_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_EF_AF2_Klausurvorgabe_ID = add("GKL_EF_AF2_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 2");

	/** Die Definition der Tabellenspalte GKL_EF_AF3_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_EF_AF3_Klausurvorgabe_ID = add("GKL_EF_AF3_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 3");

	/** Die Definition der Tabellenspalte GKL_Q_AF1_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_Q_AF1_Klausurvorgabe_ID = add("GKL_Q_AF1_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 1");

	/** Die Definition der Tabellenspalte GKL_Q_AF2_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_Q_AF2_Klausurvorgabe_ID = add("GKL_Q_AF2_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 2");

	/** Die Definition der Tabellenspalte GKL_Q_AF3_Klausurvorgabe_ID */
	public final SchemaTabelleSpalte col_GKL_Q_AF3_Klausurvorgabe_ID = add("GKL_Q_AF3_Klausurvorgabe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 3");

	/** Die Definition der Tabellenspalte ZulassungSimulation */
	public final SchemaTabelleSpalte col_ZulassungSimulation = add("ZulassungSimulation", SchemaDatentypen.TEXT, false)
			.setJavaComment("Gymnasiale Oberstufe - Wiederholung: Die Eingaben der Simulation der Abiturzulassung als JSON-Text")
			.setRevision(SchemaRevisionen.REV_83);


	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_Schueler_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_Schueler_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_Schueler_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_Abi_Jahrgang_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_Abi_Jahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_GKL_EF_AF1_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_GKL_EF_AF1_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_GKL_EF_AF1_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_EF_AF1_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_GKL_EF_AF2_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_GKL_EF_AF2_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_GKL_EF_AF2_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_EF_AF2_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_GKL_EF_AF3_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_GKL_EF_AF3_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_GKL_EF_AF3_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_EF_AF3_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_GKL_Q_AF1_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_GKL_Q_AF1_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_GKL_Q_AF1_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_Q_AF1_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_GKL_Q_AF2_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_GKL_Q_AF2_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_GKL_Q_AF2_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_Q_AF2_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	);

	/** Die Definition des Fremdschlüssels Gost_Schueler_Wiederholung_GKL_Q_AF3_Klausurvorgabe_ID_FK */
	public final SchemaTabelleFremdschluessel fk_Gost_Schueler_Wiederholung_GKL_Q_AF3_Klausurvorgabe_ID_FK = addForeignKey(
			"Gost_Schueler_Wiederholung_GKL_Q_AF3_Klausurvorgabe_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_GKL_Q_AF3_Klausurvorgabe_ID, Schema.tab_Gost_Klausuren_Vorgaben.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Schueler_Wiederholung.
	 */
	public Tabelle_Gost_Schueler_Wiederholung() {
		super("Gost_Schueler_Wiederholung", SchemaRevisionen.REV_82);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("gost");
		setJavaClassName("DTOGostSchuelerWiederholung");
		setJavaComment("Gymnasiale Oberstufe: Wiederholung zweier Halbjahre eines Schülers im folgenden Abiturjahrgang, mit den GKL-Wahlen für diesen Jahrgang");
	}

}
