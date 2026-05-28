package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Kurse.
 */
public class Tabelle_UV_Kurse extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Kurses (generiert, planungsspezifisch)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public final SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte Fach_ID */
	public final SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Faches");

	/** Die Definition der Tabellenspalte Kursart */
	public final SchemaTabelleSpalte col_Kursart = add("Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setDefault("GK")
			.setNotNull()
			.setConverter(GOStKursartConverter.class)
			.setJavaComment("ID der Kursart (siehe ID des Core-Types GostKursart)");

	/** Die Definition der Tabellenspalte Kursnummer */
	public final SchemaTabelleSpalte col_Kursnummer = add("Kursnummer", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Die Nummer des Kurses in Bezug auf das Fach (Kurse eines Faches sind in einer Blockung üblicherweise von 1 ab durchnummeriert)");

    /** Fremdschlüssel auf die Schülergruppe (Tabelle UV_Schuelergruppen) */
    public final SchemaTabelleSpalte col_Schuelergruppe_ID = add("Schuelergruppe_ID", SchemaDatentypen.BIGINT, false)
    		.setNotNull()
            .setJavaComment("Fremdschlüssel auf die Schülergruppe (Tabelle UV_Schuelergruppen)");


    /** Fremdschlüssel auf die Tabelle Schuljahresabschnitte */
    public final SchemaTabelleFremdschluessel fk_UVKurse_Schuljahresabschnitte_FK = addForeignKey(
            "UVKurse_Schuljahresabschnitte_FK",
            /* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
            /* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
            new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
    );

    /** Fremdschlüssel auf die Tabelle UV_Faecher DEPRECATED wegen Bug bei Erstellung des FKs (Spaltenreihenfolge #4a702e6e) */
    public final SchemaTabelleFremdschluessel fk_UVKurse_UVFaecher_FK_Deprecated_Revision_49 = addForeignKey(
            "UVKurse_UVFaecher_FK",
            /* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
            /* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
            new Pair<>(col_Fach_ID, Schema.tab_UV_Faecher.col_ID)
    ).setVeraltet(SchemaRevisionen.REV_49);

	/** Fremdschlüssel auf die Tabelle UV_Faecher DEPRECATED wegen OnDelete */
	public final SchemaTabelleFremdschluessel fk_UVKurse_UVFaecher_FK_Deprecated_Revision_53 = addForeignKey(
			"UVKurse_UVStundentafelnFaecher_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Fach_ID, Schema.tab_UV_Stundentafeln_Faecher.col_ID)
	).setRevision(SchemaRevisionen.REV_50)
			.setVeraltet(SchemaRevisionen.REV_65);

	/** Fremdschlüssel auf die Tabelle UV_Faecher */
	public final SchemaTabelleFremdschluessel fk_UVKurse_UVFaecher_FK = addForeignKey(
			"UVKurse_UVFaecher_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Fach_ID, Schema.tab_UV_Faecher.col_ID)
	).setRevision(SchemaRevisionen.REV_66);

    /** Fremdschlüssel auf die Tabelle UV_Schuelergruppen DEPRECATED wegen Bug bei Erstellung des FKs (Spaltenreihenfolge #4a702e6e) */
    public final SchemaTabelleFremdschluessel fk_UVKurse_UVSchuelergruppen_FK_Deprecated_Revision_49 = addForeignKey(
            "UVKurse_UVSchuelergruppen_FK",
            /* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
            /* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
            new Pair<>(col_Schuelergruppe_ID, Schema.tab_UV_Schuelergruppen.col_ID),
            new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Schuelergruppen.col_Planungsabschnitt_ID)
    ).setVeraltet(SchemaRevisionen.REV_49);

	/** Fremdschlüssel auf die Tabelle UV_Schuelergruppen */
	public final SchemaTabelleFremdschluessel fk_UVKurse_UVSchuelergruppen_FK = addForeignKey(
			"UVKurse_UVSchuelergruppen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_Schuelergruppe_ID, Schema.tab_UV_Schuelergruppen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Schuelergruppen.col_Planungsabschnitt_ID)
	).setRevision(SchemaRevisionen.REV_50);

	/** Unique-Index für die Kombination ID und Planungsabschnitt_ID, benötigt durch 2-teiligen FK */
	public final SchemaTabelleUniqueIndex unique_UVKurse_UC1 = addUniqueIndex("UVKurse_UC1",
			col_ID, col_Planungsabschnitt_ID
	 );




	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_Kurse.
	 */
	public Tabelle_UV_Kurse() {
		super("UV_Kurse", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvKurs");
		setJavaComment("Tabelle für die Kurse eines Planungsabschnitts der Unterrichtsverteilung (UV)");
	}

}
