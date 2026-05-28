package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_ZeitrasterEintraege.
 */
public class Tabelle_UV_ZeitrasterEintraege extends SchemaTabelle {

    /** Eindeutige ID des Eintrags (automatisch generiert) */
    public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
            .setNotNull()
            .setJavaComment("Eindeutige ID des Zeitraster-Eintrags (automatisch generiert)");

    /** Fremdschlüssel auf das Zeitraster (Tabelle UV_Zeitraster) */
    public final SchemaTabelleSpalte col_Zeitraster_ID = add("Zeitraster_ID", SchemaDatentypen.BIGINT, false)
            .setNotNull()
            .setJavaComment("Fremdschlüssel auf das Zeitraster (Tabelle UV_Zeitraster)");

    /** Wochentag des Eintrags als INT (z. B. 1=Montag) */
    public final SchemaTabelleSpalte col_Tag = add("Tag", SchemaDatentypen.INT, false)
            .setNotNull()
            .setJavaComment("Wochentag des Zeitraster-Eintrags als Integer (z. B. 1=Montag)");

    /** Stunde innerhalb des Tages als INT */
    public final SchemaTabelleSpalte col_Stunde = add("Stunde", SchemaDatentypen.INT, false)
            .setNotNull()
            .setJavaComment("Stunde des Zeitraster-Eintrags");

    /** Beginn der Stunde (Uhrzeit als TIME) */
    public final SchemaTabelleSpalte col_Beginn = add("Beginn", SchemaDatentypen.TIME, false)
			.setNotNull()
			.setConverter(UhrzeitConverter.class)
            .setJavaComment("Beginn der Stunde (Uhrzeit als TIME)");

    /** Ende der Stunde (Uhrzeit als TIME) */
    public final SchemaTabelleSpalte col_Ende = add("Ende", SchemaDatentypen.TIME, false)
			.setNotNull()
			.setConverter(UhrzeitConverter.class)
            .setJavaComment("Ende der Stunde (Uhrzeit als TIME)");

    /** Fremdschlüssel auf die Tabelle UV_Zeitraster */
    public final SchemaTabelleFremdschluessel fk_UVZeitrasterEintraege_UVZeitraster_FK = addForeignKey(
            "UVZeitrasterEintraege_UVZeitraster_FK",
            /* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
            /* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
            new Pair<>(col_Zeitraster_ID, Schema.tab_UV_Zeitraster.col_ID)
    );

    /** Unique-Index für die Kombination Stunde, Tag und Zeitraster */
    public final SchemaTabelleUniqueIndex unique_UVZeitrasterEintraege_UC1 = addUniqueIndex("UVZeitrasterEintraege_UC1",
            col_Zeitraster_ID, col_Tag, col_Stunde
    );

    /**
     * Erstellt die Schema-Definition für die Tabelle UV_ZeitrasterEintraege.
     */
    public Tabelle_UV_ZeitrasterEintraege() {
        super("UV_ZeitrasterEintraege", SchemaRevisionen.REV_48);
        setMigrate(false);
        setImportExport(true);
        setPKAutoIncrement();
        setJavaSubPackage("uv");
        setJavaClassName("DTOUvZeitrasterEintrag");
        setJavaComment("Tabelle für die einzelnen Zeitrastereinträge eines Zeitrasters der Unterrichtsverteilung (UV)");
    }
}
