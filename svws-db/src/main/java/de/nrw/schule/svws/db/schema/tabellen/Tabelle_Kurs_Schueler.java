package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Kurs_Schueler.
 */
public class Tabelle_Kurs_Schueler extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kurs_ID */
	public SchemaTabelleSpalte col_Kurs_ID = add("Kurs_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Kurses – verweist auf den Kurs");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Schülers – verweist auf den Schüler");


	/** Die Definition des Fremdschlüssels KursSchueler_Kurse_FK */
	public SchemaTabelleFremdschluessel fk_KursSchueler_Kurse_FK = addForeignKey(
			"KursSchueler_Kurse_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Kurs_ID, Schema.tab_Kurse.col_ID)
		);

	/** Die Definition des Fremdschlüssels KursSchueler_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_KursSchueler_Schueler_FK = addForeignKey(
			"KursSchueler_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Kurs_Schueler.
	 */
	public Tabelle_Kurs_Schueler() {
		super("Kurs_Schueler", SchemaRevisionen.REV_1);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.kurse");
		setJavaClassName("DTOKursSchueler");
		setJavaComment("Tabelle mit KursSchueler-Zuordnungen wird ab Schild3.0 getriggert für performanteren Druck");
	}

}
