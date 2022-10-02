package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerFHRFaecher.
 */
public class Tabelle_SchuelerFHRFaecher extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Facheintrags für den FHR-Reiter");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schuelers");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Faches");

	/** Die Definition der Tabellenspalte KursartAllg */
	public SchemaTabelleSpalte col_KursartAllg = add("KursartAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Kursart des Faches im Klartext");

	/** Die Definition der Tabellenspalte FachKrz */
	public SchemaTabelleSpalte col_FachKrz = add("FachKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Kürzel des Faches");

	/** Die Definition der Tabellenspalte PSII_2_1 */
	public SchemaTabelleSpalte col_PSII_2_1 = add("PSII_2_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("P: Punkte im betreffenden Abschnitt");

	/** Die Definition der Tabellenspalte HSII_2_1 */
	public SchemaTabelleSpalte col_HSII_2_1 = add("HSII_2_1", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK. ");

	/** Die Definition der Tabellenspalte RSII_2_1 */
	public SchemaTabelleSpalte col_RSII_2_1 = add("RSII_2_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) ");

	/** Die Definition der Tabellenspalte PSII_2_2 */
	public SchemaTabelleSpalte col_PSII_2_2 = add("PSII_2_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("P: Punkte im betreffenden Abschnitt ");

	/** Die Definition der Tabellenspalte HSII_2_2 */
	public SchemaTabelleSpalte col_HSII_2_2 = add("HSII_2_2", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK. ");

	/** Die Definition der Tabellenspalte RSII_2_2 */
	public SchemaTabelleSpalte col_RSII_2_2 = add("RSII_2_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) ");

	/** Die Definition der Tabellenspalte PSII_2_1_W */
	public SchemaTabelleSpalte col_PSII_2_1_W = add("PSII_2_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("P: Punkte im betreffenden Abschnitt W=wiederholt");

	/** Die Definition der Tabellenspalte HSII_2_1_W */
	public SchemaTabelleSpalte col_HSII_2_1_W = add("HSII_2_1_W", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt ");

	/** Die Definition der Tabellenspalte RSII_2_1_W */
	public SchemaTabelleSpalte col_RSII_2_1_W = add("RSII_2_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt");

	/** Die Definition der Tabellenspalte PSII_2_2_W */
	public SchemaTabelleSpalte col_PSII_2_2_W = add("PSII_2_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("P: Punkte im betreffenden Abschnitt W=wiederholt");

	/** Die Definition der Tabellenspalte HSII_2_2_W */
	public SchemaTabelleSpalte col_HSII_2_2_W = add("HSII_2_2_W", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt ");

	/** Die Definition der Tabellenspalte RSII_2_2_W */
	public SchemaTabelleSpalte col_RSII_2_2_W = add("RSII_2_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt");

	/** Die Definition der Tabellenspalte PSII_3_1 */
	public SchemaTabelleSpalte col_PSII_3_1 = add("PSII_3_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Punkte im betreffenden Abschnitt");

	/** Die Definition der Tabellenspalte HSII_3_1 */
	public SchemaTabelleSpalte col_HSII_3_1 = add("HSII_3_1", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK. ");

	/** Die Definition der Tabellenspalte RSII_3_1 */
	public SchemaTabelleSpalte col_RSII_3_1 = add("RSII_3_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) ");

	/** Die Definition der Tabellenspalte PSII_3_2 */
	public SchemaTabelleSpalte col_PSII_3_2 = add("PSII_3_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("P: Punkte im betreffenden Abschnitt");

	/** Die Definition der Tabellenspalte HSII_3_2 */
	public SchemaTabelleSpalte col_HSII_3_2 = add("HSII_3_2", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users, der die Hochrechnung angestoßen hat (diese Daten werden dann bei Beendigung von SchILD wieder gelöscht). Nur noch BK. ");

	/** Die Definition der Tabellenspalte RSII_3_2 */
	public SchemaTabelleSpalte col_RSII_3_2 = add("RSII_3_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) ");

	/** Die Definition der Tabellenspalte PSII_3_1_W */
	public SchemaTabelleSpalte col_PSII_3_1_W = add("PSII_3_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("P: Punkte im betreffenden Abschnitt W=wiederholt");

	/** Die Definition der Tabellenspalte HSII_3_1_W */
	public SchemaTabelleSpalte col_HSII_3_1_W = add("HSII_3_1_W", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt ");

	/** Die Definition der Tabellenspalte RSII_3_1_W */
	public SchemaTabelleSpalte col_RSII_3_1_W = add("RSII_3_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt");

	/** Die Definition der Tabellenspalte PSII_3_2_W */
	public SchemaTabelleSpalte col_PSII_3_2_W = add("PSII_3_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("P: Punkte im betreffenden Abschnitt W=wiederholt");

	/** Die Definition der Tabellenspalte HSII_3_2_W */
	public SchemaTabelleSpalte col_HSII_3_2_W = add("HSII_3_2_W", SchemaDatentypen.INT, false)
		.setJavaComment("H: Wenn ungleich 0: Es handelt sich um eine Hochrechnung, dann steht darin die ID des Users nur noch BK. Daten sind temporär. W=wiederholt ");

	/** Die Definition der Tabellenspalte RSII_3_2_W */
	public SchemaTabelleSpalte col_RSII_3_2_W = add("RSII_3_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("R: Relevanz für Berechnung (+ wenn das Fach im betreffenden Abschnitt in die Berechnung eingeht, in SchILD wird das dann hellblau im Grid dargestellt) W=wiederholt");

	/** Die Definition der Tabellenspalte KSII_2_1 */
	public SchemaTabelleSpalte col_KSII_2_1 = add("KSII_2_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt ");

	/** Die Definition der Tabellenspalte KSII_2_2 */
	public SchemaTabelleSpalte col_KSII_2_2 = add("KSII_2_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt ");

	/** Die Definition der Tabellenspalte KSII_2_1_W */
	public SchemaTabelleSpalte col_KSII_2_1_W = add("KSII_2_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt W: Wiederholter Abschnitt");

	/** Die Definition der Tabellenspalte KSII_2_2_W */
	public SchemaTabelleSpalte col_KSII_2_2_W = add("KSII_2_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt ");

	/** Die Definition der Tabellenspalte KSII_3_1 */
	public SchemaTabelleSpalte col_KSII_3_1 = add("KSII_3_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt ");

	/** Die Definition der Tabellenspalte KSII_3_2 */
	public SchemaTabelleSpalte col_KSII_3_2 = add("KSII_3_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt ");

	/** Die Definition der Tabellenspalte KSII_3_1_W */
	public SchemaTabelleSpalte col_KSII_3_1_W = add("KSII_3_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt W: Wiederholter Abschnitt");

	/** Die Definition der Tabellenspalte KSII_3_2_W */
	public SchemaTabelleSpalte col_KSII_3_2_W = add("KSII_3_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("K: Kursart im betreffenden Abschnitt W: Wiederholter Abschnitt");

	/** Die Definition der Tabellenspalte FSortierung */
	public SchemaTabelleSpalte col_FSortierung = add("FSortierung", SchemaDatentypen.INT, false)
		.setJavaComment("K: Kursart im betreffenden Abschnitt ");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerFHRFaecher_Fach_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFHRFaecher_Fach_FK = addForeignKey(
			"SchuelerFHRFaecher_Fach_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerFHRFaecher_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFHRFaecher_Schueler_FK = addForeignKey(
			"SchuelerFHRFaecher_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerFHRFaecher.
	 */
	public Tabelle_SchuelerFHRFaecher() {
		super("SchuelerFHRFaecher", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler.fhr");
		setJavaClassName("DTOSchuelerFHRFach");
		setJavaComment("Liste der Fächer zu SchuelerFHR");
	}

}
