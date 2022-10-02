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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerFHR.
 */
public class Tabelle_SchuelerFHR extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des FHR Datensatzes");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schülers zum FHR Datensatz");

	/** Die Definition der Tabellenspalte FHRErreicht */
	public SchemaTabelleSpalte col_FHRErreicht = add("FHRErreicht", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Gibt an ob das FHR erreicht wurde");

	/** Die Definition der Tabellenspalte Note */
	public SchemaTabelleSpalte col_Note = add("Note", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Gesamtnote FHR");

	/** Die Definition der Tabellenspalte GesamtPunktzahl */
	public SchemaTabelleSpalte col_GesamtPunktzahl = add("GesamtPunktzahl", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Gesamtpunktzahl FHR");

	/** Die Definition der Tabellenspalte SummeGK */
	public SchemaTabelleSpalte col_SummeGK = add("SummeGK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Summer der Grundkurse FHR");

	/** Die Definition der Tabellenspalte SummeLK */
	public SchemaTabelleSpalte col_SummeLK = add("SummeLK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Summer der Leistungskurse FHR");

	/** Die Definition der Tabellenspalte SummenOK */
	public SchemaTabelleSpalte col_SummenOK = add("SummenOK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Gibt an ob die Anzahl der eingebrachten Kurse ok ist");

	/** Die Definition der Tabellenspalte AnzRelLK */
	public SchemaTabelleSpalte col_AnzRelLK = add("AnzRelLK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Anzahl der eingebrachten LKs");

	/** Die Definition der Tabellenspalte AnzRelGK */
	public SchemaTabelleSpalte col_AnzRelGK = add("AnzRelGK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Anzahl der eingebrachten Gks");

	/** Die Definition der Tabellenspalte AnzRelOK */
	public SchemaTabelleSpalte col_AnzRelOK = add("AnzRelOK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Gibt an ob die Anzahl GK ok ist");

	/** Die Definition der Tabellenspalte AnzDefLK */
	public SchemaTabelleSpalte col_AnzDefLK = add("AnzDefLK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Gibt an ob die Anzahl Defizite LK ok ist");

	/** Die Definition der Tabellenspalte AnzDefGK */
	public SchemaTabelleSpalte col_AnzDefGK = add("AnzDefGK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Anzahl der Defizite in den Gks");

	/** Die Definition der Tabellenspalte AnzDefOK */
	public SchemaTabelleSpalte col_AnzDefOK = add("AnzDefOK", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("Gibt an ob die Anzahl Defizite GK ok ist");

	/** Die Definition der Tabellenspalte JSII_2_1 */
	public SchemaTabelleSpalte col_JSII_2_1 = add("JSII_2_1", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte JSII_2_1_W */
	public SchemaTabelleSpalte col_JSII_2_1_W = add("JSII_2_1_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte JSII_2_2 */
	public SchemaTabelleSpalte col_JSII_2_2 = add("JSII_2_2", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte JSII_2_2_W */
	public SchemaTabelleSpalte col_JSII_2_2_W = add("JSII_2_2_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte JSII_3_1 */
	public SchemaTabelleSpalte col_JSII_3_1 = add("JSII_3_1", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte JSII_3_1_W */
	public SchemaTabelleSpalte col_JSII_3_1_W = add("JSII_3_1_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte JSII_3_2 */
	public SchemaTabelleSpalte col_JSII_3_2 = add("JSII_3_2", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte JSII_3_2_W */
	public SchemaTabelleSpalte col_JSII_3_2_W = add("JSII_3_2_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte ASII_2_1 */
	public SchemaTabelleSpalte col_ASII_2_1 = add("ASII_2_1", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte ASII_2_2 */
	public SchemaTabelleSpalte col_ASII_2_2 = add("ASII_2_2", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte ASII_2_1_W */
	public SchemaTabelleSpalte col_ASII_2_1_W = add("ASII_2_1_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte ASII_2_2_W */
	public SchemaTabelleSpalte col_ASII_2_2_W = add("ASII_2_2_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte ASII_3_1 */
	public SchemaTabelleSpalte col_ASII_3_1 = add("ASII_3_1", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte ASII_3_2 */
	public SchemaTabelleSpalte col_ASII_3_2 = add("ASII_3_2", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR) ");

	/** Die Definition der Tabellenspalte ASII_3_1_W */
	public SchemaTabelleSpalte col_ASII_3_1_W = add("ASII_3_1_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte ASII_3_2_W */
	public SchemaTabelleSpalte col_ASII_3_2_W = add("ASII_3_2_W", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt");

	/** Die Definition der Tabellenspalte WSII_2_1 */
	public SchemaTabelleSpalte col_WSII_2_1 = add("WSII_2_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt.");

	/** Die Definition der Tabellenspalte WSII_2_2 */
	public SchemaTabelleSpalte col_WSII_2_2 = add("WSII_2_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt.");

	/** Die Definition der Tabellenspalte WSII_2_1_W */
	public SchemaTabelleSpalte col_WSII_2_1_W = add("WSII_2_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt.  W=wiederholt");

	/** Die Definition der Tabellenspalte WSII_2_2_W */
	public SchemaTabelleSpalte col_WSII_2_2_W = add("WSII_2_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. W=wiederholt");

	/** Die Definition der Tabellenspalte WSII_3_1 */
	public SchemaTabelleSpalte col_WSII_3_1 = add("WSII_3_1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt.");

	/** Die Definition der Tabellenspalte WSII_3_2 */
	public SchemaTabelleSpalte col_WSII_3_2 = add("WSII_3_2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt.");

	/** Die Definition der Tabellenspalte WSII_3_1_W */
	public SchemaTabelleSpalte col_WSII_3_1_W = add("WSII_3_1_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. W=wiederholt");

	/** Die Definition der Tabellenspalte WSII_3_2_W */
	public SchemaTabelleSpalte col_WSII_3_2_W = add("WSII_3_2_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. W=wiederholt");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerFHR_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerFHR_Schueler_FK = addForeignKey(
			"SchuelerFHR_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/** Die Definition des Unique-Index SchuelerFHR_UC1 */
	public SchemaTabelleUniqueIndex unique_SchuelerFHR_UC1 = addUniqueIndex("SchuelerFHR_UC1", 
			col_Schueler_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerFHR.
	 */
	public Tabelle_SchuelerFHR() {
		super("SchuelerFHR", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler.fhr");
		setJavaClassName("DTOSchuelerFHR");
		setJavaComment("Tabelle der FHR-Daten zum Schüler");
	}

}
