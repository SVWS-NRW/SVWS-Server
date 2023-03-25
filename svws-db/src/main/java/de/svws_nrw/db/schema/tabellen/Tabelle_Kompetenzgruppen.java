package de.svws_nrw.db.schema.tabellen;

import java.util.Arrays;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Kompetenzgruppen.
 */
public class Tabelle_Kompetenzgruppen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte KG_ID */
	public SchemaTabelleSpalte col_KG_ID = add("KG_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Kompetenzgruppe");

	/** Die Definition der Tabellenspalte KG_Bezeichnung */
	public SchemaTabelleSpalte col_KG_Bezeichnung = add("KG_Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Bezeichnung der Kompetenzgruppe");

    /** Die Definition der Tabellenspalte KG_Spalte */
    public SchemaTabelleSpalte col_KG_Spalte = add("KG_Spalte", SchemaDatentypen.BIGINT, false)
        .setNotNull()
        .setJavaComment("Spalte in der Benutzerverwaltung für die Kompetenzgruppe");

    /** Die Definition der Tabellenspalte KG_Zeile */
    public SchemaTabelleSpalte col_KG_Zeile = add("KG_Zeile", SchemaDatentypen.BIGINT, false)
        .setNotNull()
        .setJavaComment("Zeile in der Benutzerverwaltung für die Kompetenzgruppe");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Kompetenzgruppen.
	 */
	public Tabelle_Kompetenzgruppen() {
		super("Kompetenzgruppen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOKatalogBenutzerKompetenzGruppe");
		setJavaComment("definierte Usergruppen, die wiederum die Kompetenzen enthält");
        setCoreType(new SchemaTabelleCoreType(this, BenutzerKompetenzGruppe.class, BenutzerKompetenzGruppe.VERSION, (rev) -> Arrays
            .stream(BenutzerKompetenzGruppe.values())
            .map(k -> k.daten.id + ",'" + k.daten.bezeichnung + "'" + "," + k.daten.spalte + "," + k.daten.zeile)
            .toList()));
	}

}
