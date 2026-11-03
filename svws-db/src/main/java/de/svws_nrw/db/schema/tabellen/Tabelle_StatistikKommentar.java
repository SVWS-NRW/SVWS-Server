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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle StatistikKommentar.
 */
public class Tabelle_StatistikKommentar extends SchemaTabelle {

	/** Die Definition der Tabellenspalte id */
	public final SchemaTabelleSpalte col_id = add("id", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Statistik-Kommentars");

	/** Die Definition der Tabellenspalte idSchuljahresabschnitt */
	public final SchemaTabelleSpalte col_idSchuljahresabschnitt = add("idSchuljahresabschnitt", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Schuljahresabschnitts, auf den sich der Kommentar bezieht");

	/** Die Definition der Tabellenspalte kommentar */
	public final SchemaTabelleSpalte col_kommentar = add("kommentar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setDefault("")
			.setJavaComment("Der Kommentar zur Statistik");


	/** Die Definition des Fremdschlüssels StatistikKommentar_Schuljahresabschnitt_FK */
	public final SchemaTabelleFremdschluessel fk_StatistikKommentar_Schuljahresabschnitt_FK = addForeignKey(
			"StatistikKommentar_Schuljahresabschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_idSchuljahresabschnitt, Schema.tab_Schuljahresabschnitte.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle StatistikKommentar.
	 */
	public Tabelle_StatistikKommentar() {
		super("StatistikKommentar", SchemaRevisionen.REV_81);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOStatistikKommentar");
		setJavaComment("Tabelle mit Kommentaren zur Statistik für einen Schuljahresabschnitt");
	}

}
