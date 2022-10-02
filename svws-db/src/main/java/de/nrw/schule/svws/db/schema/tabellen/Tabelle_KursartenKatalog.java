package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KursartenKatalog.
 */
public class Tabelle_KursartenKatalog extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Kursart");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Kürzel der Kursart");

	/** Die Definition der Tabellenspalte Nummer */
	public SchemaTabelleSpalte col_Nummer = add("Nummer", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Der numerische Schlüssel für die amtliche Schulstatistik");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Die textuelle Bezeichnung der Kursart");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Ergänzende Bemerkungen zu der Kursart (z.B. gemäß § 9 Abs. 2, 3  SchulG)");

	/** Die Definition der Tabellenspalte KuerzelAllg */
	public SchemaTabelleSpalte col_KuerzelAllg = add("KuerzelAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Das Kürzel der verallgemeinerten Kursart, falls angegeben (z.B. GK für GKM, GKS, AB3, etc.)");

	/** Die Definition der Tabellenspalte BezeichnungAllg */
	public SchemaTabelleSpalte col_BezeichnungAllg = add("BezeichnungAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Die textuelle Bezeichnung der verallgemeinerten Kursart, falls angegeben");

	/** Die Definition der Tabellenspalte ErlaubtGOSt */
	public SchemaTabelleSpalte col_ErlaubtGOSt = add("ErlaubtGOSt", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter("Boolean01Converter")
		.setJavaComment("Gibt an, ob die Kursart in der gymnasialen Oberstufe erlaubt ist oder nicht (1 - true / 0 - false)");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle KursartenKatalog.
	 */
	public Tabelle_KursartenKatalog() {
		super("KursartenKatalog", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.kurse");
		setJavaClassName("DTOKursartenKatalog");
		setJavaComment("Eine Tabelle mit den zulässigen Kursarten");
	}

}
