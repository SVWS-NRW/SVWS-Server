package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Unterrichte.
 */
public class Tabelle_UV_Unterrichte extends SchemaTabelle {

    /** Eindeutige ID der Unterrichtseinheit (automatisch generiert) */
    public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
            .setNotNull()
            .setJavaComment("Eindeutige ID der Unterrichtseinheit (automatisch generiert)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");

    /** Fremdschlüssel auf den Zeitrastereintrag (Tabelle UV_ZeitrasterEintraege) */
    public final SchemaTabelleSpalte col_ZeitrasterEintrag_ID = add("ZeitrasterEintrag_ID", SchemaDatentypen.BIGINT, false)
            .setJavaComment("Fremdschlüssel auf den Zeitrastereintrag (Tabelle UV_ZeitrasterEintraege)");

    /** Fremdschlüssel auf die Lerngruppe (Tabelle UV_Lerngruppen) */
    public final SchemaTabelleSpalte col_Lerngruppe_ID = add("Lerngruppe_ID", SchemaDatentypen.BIGINT, false)
            .setNotNull()
            .setJavaComment("Fremdschlüssel auf die Lerngruppe (Tabelle UV_Lerngruppen)");

    /** Fremdschlüssel auf die Tabelle UV_ZeitrasterEintraege */
    public final SchemaTabelleFremdschluessel fk_UVUnterrichte_UVZeitrasterEintraege_FK = addForeignKey(
            "UVUnterrichte_UVZeitrasterEintraege_FK",
            /* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
            /* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
            new Pair<>(col_ZeitrasterEintrag_ID, Schema.tab_UV_Zeitraster_Eintraege.col_ID)
    );

    /** Fremdschlüssel auf die Tabelle UV_Lerngruppen */
    public final SchemaTabelleFremdschluessel fk_UVUnterrichte_UVLerngruppen_FK = addForeignKey(
            "UVUnterrichte_UVLerngruppen_FK",
            /* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
            /* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
            new Pair<>(col_Lerngruppe_ID, Schema.tab_UV_Lerngruppen.col_ID),
            new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Lerngruppen.col_Planungsabschnitt_ID)
    );

	/** Die Definition des Unique-Index UV_Unterrichte_UC1 */
	public final SchemaTabelleUniqueIndex unique_UV_Unterrichte_UC1 = addUniqueIndex("UV_Unterrichte_UC1",
			col_ID,
			col_Planungsabschnitt_ID
	);

    /**
     * Erstellt die Schema-Definition für die Tabelle UV_Unterrichte.
     */
    public Tabelle_UV_Unterrichte() {
        super("UV_Unterrichte", SchemaRevisionen.REV_48);
        setMigrate(false);
        setImportExport(true);
        setPKAutoIncrement();
        setJavaSubPackage("uv");
        setJavaClassName("DTOUvUnterricht");
        setJavaComment("Tabelle für die Unterrichtseinheiten der Unterrichtsverteilung (UV)");
    }

}
