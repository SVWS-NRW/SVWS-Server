package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerBKAbschluss.
 */
public class Tabelle_SchuelerBKAbschluss extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("SchülerID für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte Zulassung */
	public SchemaTabelleSpalte col_Zulassung = add("Zulassung", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Zulassung Ja Nein für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Bestanden */
	public SchemaTabelleSpalte col_Bestanden = add("Bestanden", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Bestanden Ja Nein  für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte ZertifikatBK */
	public SchemaTabelleSpalte col_ZertifikatBK = add("ZertifikatBK", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("DEPRECATED: Zertifikat für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte ZulassungErwBK */
	public SchemaTabelleSpalte col_ZulassungErwBK = add("ZulassungErwBK", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Zulassung erweiterte Beruflliche Kenntnisse");

	/** Die Definition der Tabellenspalte BestandenErwBK */
	public SchemaTabelleSpalte col_BestandenErwBK = add("BestandenErwBK", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Bestanden erweiterte Beruflliche Kenntnisse");

	/** Die Definition der Tabellenspalte ZulassungBA */
	public SchemaTabelleSpalte col_ZulassungBA = add("ZulassungBA", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Zulassung Berufsabschluss  für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte BestandenBA */
	public SchemaTabelleSpalte col_BestandenBA = add("BestandenBA", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Bestanden Berufsabschluss für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte PraktPrfNote */
	public SchemaTabelleSpalte col_PraktPrfNote = add("PraktPrfNote", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Note Praktische Prüfung für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte NoteKolloquium */
	public SchemaTabelleSpalte col_NoteKolloquium = add("NoteKolloquium", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Note Kolloqium für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte ThemaAbschlussarbeit */
	public SchemaTabelleSpalte col_ThemaAbschlussarbeit = add("ThemaAbschlussarbeit", SchemaDatentypen.TEXT, false)
		.setJavaComment("Thema Abschlussarbeit für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte BAP_Vorhanden */
	public SchemaTabelleSpalte col_BAP_Vorhanden = add("BAP_Vorhanden", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Berufsabschlussprüfung vorhanden für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte NoteFachpraxis */
	public SchemaTabelleSpalte col_NoteFachpraxis = add("NoteFachpraxis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Note der Fachpraxis für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte FachPraktAnteilAusr */
	public SchemaTabelleSpalte col_FachPraktAnteilAusr = add("FachPraktAnteilAusr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Fachpraktische Anteile mindestens ausreichend für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt  für den BKAbschlussReiter");


	/** Die Definition des Fremdschlüssels SchuelerBKAbschluss_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerBKAbschluss_Schuljahreabschnitt_FK = addForeignKey(
			"SchuelerBKAbschluss_Schuljahreabschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerBKAbschluss_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerBKAbschluss_Schueler_FK = addForeignKey(
			"SchuelerBKAbschluss_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerBKAbschluss.
	 */
	public Tabelle_SchuelerBKAbschluss() {
		super("SchuelerBKAbschluss", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.berufskolleg");
		setJavaClassName("DTOSchuelerBKAbschluss");
		setJavaComment("BKAbschluss-Daten (BK) und ZP10 andere Schulformen mit ZP10 oder ZK");
	}

}
