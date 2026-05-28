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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_Lerngruppen_Schienen.
 */
public class Tabelle_UV_Lerngruppen_Schienen extends SchemaTabelle {

    /** Eindeutige ID der Lerngruppe im Planungsabschnitt */
    public final SchemaTabelleSpalte col_Lerngruppe_ID = add("Lerngruppe_ID", SchemaDatentypen.BIGINT, true)
            .setNotNull()
            .setJavaComment("Eindeutige ID der Lerngruppe im Planungsabschnitt");

    /** Fremdschlüssel auf die Schiene (Tabelle UV_Schienen) */
    public final SchemaTabelleSpalte col_Schiene_ID = add("Schiene_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
            .setJavaComment("Fremdschlüssel auf die Schiene (Tabelle UV_Schienen)");

	/** Die Definition der Tabellenspalte Planungsabschnitt_ID */
	public final SchemaTabelleSpalte col_Planungsabschnitt_ID = add("Planungsabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte");


	/** Die Definition des Fremdschlüssels auf UV_Lerngruppen */
	public final SchemaTabelleFremdschluessel fk_UVLerngruppenSchienen_UVLerngruppen_FK = addForeignKey(
			"UVLerngruppenSchienen_UVLerngruppen_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lerngruppe_ID, Schema.tab_UV_Lerngruppen.col_ID),
			new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Lerngruppen.col_Planungsabschnitt_ID)
	);

    /** Fremdschlüssel auf die Tabelle UV_Schienen */
    public final SchemaTabelleFremdschluessel fk_UVLerngruppen_UVSchienen_FK = addForeignKey(
            "UVLerngruppenSchienen_UVSchienen_FK",
            /* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
            /* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
            new Pair<>(col_Schiene_ID, Schema.tab_UV_Schienen.col_ID),
            new Pair<>(col_Planungsabschnitt_ID, Schema.tab_UV_Schienen.col_Planungsabschnitt_ID)
    );


    /**
     * Erstellt die Schema-Definition für die Tabelle UV_Lerngruppen_Schienen.
     */
    public Tabelle_UV_Lerngruppen_Schienen() {
        super("UV_Lerngruppen_Schienen", SchemaRevisionen.REV_48);
        setMigrate(false);
        setImportExport(true);
        setJavaSubPackage("uv");
        setJavaClassName("DTOUvLerngruppeSchiene");
        setJavaComment("Tabelle für die Zuordnung einer Schiene zu einer Lerngruppe eines Planungsabschnitts der Unterrichtsverteilung (UV)");
    }

}
