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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerBKFaecher.
 */
public class Tabelle_SchuelerBKFaecher extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("SchülerIDdes Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte für den Facheintrag");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("FachID des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte FachKrz */
	public SchemaTabelleSpalte col_FachKrz = add("FachKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Kachkürzel des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte FachSchriftlich */
	public SchemaTabelleSpalte col_FachSchriftlich = add("FachSchriftlich", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Schriftlichkeit Allgemeinbildend  des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte FachSchriftlichBA */
	public SchemaTabelleSpalte col_FachSchriftlichBA = add("FachSchriftlichBA", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Schriftlichkeit Berufsbezogen des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Vornote */
	public SchemaTabelleSpalte col_Vornote = add("Vornote", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Vornote zum Fach des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte NoteSchriftlich */
	public SchemaTabelleSpalte col_NoteSchriftlich = add("NoteSchriftlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schriftliche Note  des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte MdlPruefung */
	public SchemaTabelleSpalte col_MdlPruefung = add("MdlPruefung", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Mündliche Prüfung angesetzt Ja Nein  des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte MdlPruefungFW */
	public SchemaTabelleSpalte col_MdlPruefungFW = add("MdlPruefungFW", SchemaDatentypen.CHAR, false).setDatenlaenge(1)
		.setConverter(BooleanPlusMinusConverter.class)
		.setJavaComment("Freiwilliege mündliche Prüfung angesetzt Ja Nein  des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte NoteMuendlich */
	public SchemaTabelleSpalte col_NoteMuendlich = add("NoteMuendlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Note mündliche Prüfung  des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte NoteAbschluss */
	public SchemaTabelleSpalte col_NoteAbschluss = add("NoteAbschluss", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Abschlussnote  des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte NotePrfGesamt */
	public SchemaTabelleSpalte col_NotePrfGesamt = add("NotePrfGesamt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("DEPRECATED: Wird seit Änderung der APO-BK nicht mehr genutzt. Gesamtprüfungsnote des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte FSortierung */
	public SchemaTabelleSpalte col_FSortierung = add("FSortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Fachlehrer_ID */
	public SchemaTabelleSpalte col_Fachlehrer_ID = add("Fachlehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Lehrer-ID des Facheintrags für den BK-Abschluss-Reiter");

	/** Die Definition der Tabellenspalte NoteAbschlussBA */
	public SchemaTabelleSpalte col_NoteAbschlussBA = add("NoteAbschlussBA", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Abschlussnote berufsbezogen  des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Kursart */
	public SchemaTabelleSpalte col_Kursart = add("Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kursart des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Schuljahr des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Abschnitt des Facheintrags für den BKAbschlussReiter");

	/** Die Definition der Tabellenspalte Fachlehrer */
	public SchemaTabelleSpalte col_Fachlehrer = add("Fachlehrer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Lehrer  des Facheintrags für den BKAbschlussReiter");


	/** Die Definition des Fremdschlüssels SchuelerBKFaecher_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerBKFaecher_Schuljahreabschnitt_FK = addForeignKey(
			"SchuelerBKFaecher_Schuljahreabschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerBKFaecher_Fach_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerBKFaecher_Fach_FK = addForeignKey(
			"SchuelerBKFaecher_Fach_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerBKFaecher_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerBKFaecher_Schueler_FK = addForeignKey(
			"SchuelerBKFaecher_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerBKFaecher.
	 */
	public Tabelle_SchuelerBKFaecher() {
		super("SchuelerBKFaecher", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.berufskolleg");
		setJavaClassName("DTOSchuelerBKFach");
		setJavaComment("Fächer zum Schüler in der Tabelle BKAbschluss");
	}

}
