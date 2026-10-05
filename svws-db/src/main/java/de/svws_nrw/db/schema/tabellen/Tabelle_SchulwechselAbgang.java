package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.StatusSchulwechselAbgangConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchulwechselAbgang.
 */
public class Tabelle_SchulwechselAbgang extends SchemaTabelle {

	/** Die Definition der Tabellenspalte id */
	public final SchemaTabelleSpalte col_id = add("id", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die Id des Wechselvorgangs");

	/** Die Definition der Tabellenspalte id_schueler */
	public final SchemaTabelleSpalte col_id_schueler = add("id_schueler", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaName("idSchueler")
			.setJavaComment("Die eindeutige Id des Schülers – verweist auf den Schüler");

	/** Die Definition der Tabellenspalte status */
	public final SchemaTabelleSpalte col_status = add("status", SchemaDatentypen.VARCHAR, false)
			.setDatenlaenge(20)
			.setNotNull()
			.setJavaComment("Der Status des Wechselvorgangs")
			.setConverter(StatusSchulwechselAbgangConverter.class)
			.setConverterRevision(SchemaRevisionen.REV_79);

	/** Die Definition der Tabellenspalte last_modified */
	public final SchemaTabelleSpalte col_last_modified = add("last_modified", SchemaDatentypen.DATETIME, false)
			.setNotNull()
			.setJavaName("lastModified")
			.setJavaComment("Der Zeitpunkt der letzen Statusänderung (UTC)");

	/** Die Definition der Tabellenspalte id_document */
	public final SchemaTabelleSpalte col_id_document = add("id_document", SchemaDatentypen.BIGINT, false)
			.setJavaName("idDocument")
			.setJavaComment("Die eindeutige Id des Dokuments – verweist auf das Schulwechsel-Dokument");

	/** Die Definition der Tabellenspalte id_schulkind_schulbewerbung */
	public final SchemaTabelleSpalte col_id_schulkind_schulbewerbung = add("id_schulkind_schulbewerbung", SchemaDatentypen.VARCHAR, false)
			.setDatenlaenge(36)
			.setJavaName("idSchulkindSchulbewerbung")
			.setJavaComment("Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang");

	/** Die Definition des Fremdschlüssels SchulwechselAbgang_Schueler_FK */
	public final SchemaTabelleFremdschluessel fk_SchulwechselAbgang_Schueler_FK = addForeignKey(
			"SchulwechselAbgang_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_id_schueler, Schema.tab_Schueler.col_ID)
	);

	/** Die Definition des Fremdschlüssels SchulwechselAbgang_Dokument_FK */
	public final SchemaTabelleFremdschluessel fk_SchulwechselAbgang_Dokument_FK = addForeignKey(
			"SchulwechselAbgang_Dokument_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_id_document, Schema.tab_SchulwechselDokument.col_id)
	);

	/** Die Definition des Unique-Index SchulwechselAbgang_UC1 */
	public final SchemaTabelleUniqueIndex unique_SchulwechselAbgang_UC1 = addUniqueIndex("SchulwechselAbgang_UC1",
			col_id_schueler
	);

	/** Die Definition des Unique-Index SchulwechselAbgang_UC2 */
	public final SchemaTabelleUniqueIndex unique_SchulwechselAbgang_UC2 = addUniqueIndex("SchulwechselAbgang_UC2",
			col_id_document
	);

	/** Die Definition des Unique-Index SchulwechselAbgang_UC3 */
	public final SchemaTabelleUniqueIndex unique_SchulwechselAbgang_UC3 = addUniqueIndex("SchulwechselAbgang_UC3",
			col_id_schulkind_schulbewerbung
	);

	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchulwechselAbgang.
	 */
	public Tabelle_SchulwechselAbgang() {
		super("SchulwechselAbgang", SchemaRevisionen.REV_79);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOSchulwechselAbgang");
		setJavaComment("Tabelle mit Wechselvorgängen abgehender Schüler");
	}
}
