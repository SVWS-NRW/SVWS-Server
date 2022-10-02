package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_PruefOrd_Optionen.
 */
public class Tabelle_Schildintern_PruefOrd_Optionen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte OP_Schulformen */
	public SchemaTabelleSpalte col_OP_Schulformen = add("OP_Schulformen", SchemaDatentypen.VARCHAR, true).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Schulformen für die die Optionen gelten");

	/** Die Definition der Tabellenspalte OP_POKrz */
	public SchemaTabelleSpalte col_OP_POKrz = add("OP_POKrz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(30)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Kürzel der Prüfungsordung");

	/** Die Definition der Tabellenspalte OP_Krz */
	public SchemaTabelleSpalte col_OP_Krz = add("OP_Krz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(40)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Angezeigter Kurztext in Schild-NRW");

	/** Die Definition der Tabellenspalte OP_Abgangsart_B */
	public SchemaTabelleSpalte col_OP_Abgangsart_B = add("OP_Abgangsart_B", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schildintern Tabelle: Abgangsart in der Statistik B");

	/** Die Definition der Tabellenspalte OP_Abgangsart_NB */
	public SchemaTabelleSpalte col_OP_Abgangsart_NB = add("OP_Abgangsart_NB", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schildintern Tabelle: Abgangsart in der Statistik NB");

	/** Die Definition der Tabellenspalte OP_Art */
	public SchemaTabelleSpalte col_OP_Art = add("OP_Art", SchemaDatentypen.VARCHAR, true).setDatenlaenge(1)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: A=Allgemein B=Berufsbildend");

	/** Die Definition der Tabellenspalte OP_Typ */
	public SchemaTabelleSpalte col_OP_Typ = add("OP_Typ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Schildintern Tabelle: Abschlusskürzel in Schild-NRW");

	/** Die Definition der Tabellenspalte OP_Bildungsgang */
	public SchemaTabelleSpalte col_OP_Bildungsgang = add("OP_Bildungsgang", SchemaDatentypen.VARCHAR, true).setDatenlaenge(1)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Bildungsgang A oder B");

	/** Die Definition der Tabellenspalte OP_Name */
	public SchemaTabelleSpalte col_OP_Name = add("OP_Name", SchemaDatentypen.VARCHAR, true).setDatenlaenge(255)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Text des Abschlusses");

	/** Die Definition der Tabellenspalte OP_Kommentar */
	public SchemaTabelleSpalte col_OP_Kommentar = add("OP_Kommentar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Schildintern Tabelle: Paragraph in der BASS (veraltet?)");

	/** Die Definition der Tabellenspalte OP_Jahrgaenge */
	public SchemaTabelleSpalte col_OP_Jahrgaenge = add("OP_Jahrgaenge", SchemaDatentypen.VARCHAR, true).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: zulässig für diese Jahrgänge");

	/** Die Definition der Tabellenspalte OP_BKIndex */
	public SchemaTabelleSpalte col_OP_BKIndex = add("OP_BKIndex", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: zulässig für BKIndex");

	/** Die Definition der Tabellenspalte OP_BKAnl_Typ */
	public SchemaTabelleSpalte col_OP_BKAnl_Typ = add("OP_BKAnl_Typ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: zulässig für diese Gliederungen");

	/** Die Definition der Tabellenspalte OP_Reihenfolge */
	public SchemaTabelleSpalte col_OP_Reihenfolge = add("OP_Reihenfolge", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Reihenfolge");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_PruefOrd_Optionen.
	 */
	public Tabelle_Schildintern_PruefOrd_Optionen() {
		super("Schildintern_PruefOrd_Optionen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternPruefungsOrdnungOptionen");
		setJavaComment("Eintragungen der möglichen Abschlüsse und deren Texte für Schildintern_PruefungsOrdnung");
	}

}
