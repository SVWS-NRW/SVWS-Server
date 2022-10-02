package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_Zusatzinfos.
 */
public class Tabelle_Schildintern_Zusatzinfos extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SGL_BKAbschl */
	public SchemaTabelleSpalte col_SGL_BKAbschl = add("SGL_BKAbschl", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte JG_BKAbschl */
	public SchemaTabelleSpalte col_JG_BKAbschl = add("JG_BKAbschl", SchemaDatentypen.VARCHAR, true).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_Zusatzinfos.
	 */
	public Tabelle_Schildintern_Zusatzinfos() {
		super("Schildintern_Zusatzinfos", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternZusatzinfos");
		setJavaComment("Enthält Angaben, welcher Jahrgang in welcher Gliederung der Abschlussjahrgang ist. Darüber wurde früher mal gesteuert, ob der BK-Abschluss-Reiter sichtbar war. Ist im Prinzip jetzt überflüssig, allerdings wird an einer Stelle im Programm noch der Inhalt geladen. Wenn man das rausnimmt, kann die Tabelle wegfallen.");
	}

}
