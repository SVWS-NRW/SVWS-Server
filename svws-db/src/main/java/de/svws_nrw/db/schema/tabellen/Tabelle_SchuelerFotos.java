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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerFotos.
 */
public class Tabelle_SchuelerFotos extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, true)
			.setJavaName("idSchueler")
			.setNotNull()
			.setJavaComment("SchülerID zum Foto");

	/** Die Definition der Tabellenspalte Foto */
	public final SchemaTabelleSpalte col_Foto = add("Foto", SchemaDatentypen.LONGBLOB, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Schülerfoto im binär-Format");

	/** Die Definition der Tabellenspalte FotoBase64 */
	public final SchemaTabelleSpalte col_FotoBase64 = add("FotoBase64", SchemaDatentypen.TEXT, false)
			.setJavaName("fotoBase64")
			.setJavaComment("Schülerfoto im Base64-Format");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public final SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank"
					+ " gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerFotos_Schueler_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerFotos_Schueler_FK = addForeignKey(
			"SchuelerFotos_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerFotos.
	 */
	public Tabelle_SchuelerFotos() {
		super("SchuelerFotos", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerFoto");
		setJavaComment("Blobfelder für Fotos zum Schüler");
	}

}
