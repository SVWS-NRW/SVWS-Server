package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerAbgaenge.
 */
public class Tabelle_SchuelerAbgaenge extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setJavaName("id")
			.setNotNull()
			.setJavaComment("ID der abgebenden Schule in der Liste");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public final SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idSchueler")
			.setNotNull()
			.setJavaComment("SchülerID zur abgebenden Schule");

	/** Die Definition der Tabellenspalte AbgangsSchulNr */
	public final SchemaTabelleSpalte col_AbgangsSchulNr = add("AbgangsSchulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
			.setJavaName("schulnummer")
			.setJavaComment("Schulnummer zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSSGL */
	public final SchemaTabelleSpalte col_LSSGL = add("LSSGL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
			.setJavaName("schluesselSchulgliederung")
			.setJavaComment("SGL zur abgebenden Schule");

	/** Die Definition der Tabellenspalte BemerkungIntern */
	public final SchemaTabelleSpalte col_BemerkungIntern = add("BemerkungIntern", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
			.setJavaName("bezeichnungEntlassgrund")
			.setJavaComment("interne Bemerkung zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSEntlassArt */
	public final SchemaTabelleSpalte col_LSEntlassArt = add("LSEntlassArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaName("idAbschlussart")
			.setJavaComment("Entlassart zur abgebenden Schule");

	/** Die Definition der Tabellenspalte OrganisationsformKrz */
	public final SchemaTabelleSpalte col_OrganisationsformKrz = add("OrganisationsformKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setJavaName("idOrganisationsform")
			.setJavaComment("Organisationform zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSBeginnDatum */
	public final SchemaTabelleSpalte col_LSBeginnDatum = add("LSBeginnDatum", SchemaDatentypen.DATE, false)
			.setJavaName("datumVon")
			.setConverter(DatumConverter.class)
			.setJavaComment("Aufnahmedatum zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSSchulEntlassDatum */
	public final SchemaTabelleSpalte col_LSSchulEntlassDatum = add("LSSchulEntlassDatum", SchemaDatentypen.DATE, false)
			.setJavaName("datumBis")
			.setConverter(DatumConverter.class)
			.setJavaComment("Entlassdtaum zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSBeginnJahrgang */
	public final SchemaTabelleSpalte col_LSBeginnJahrgang = add("LSBeginnJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaName("jahrgangVon")
			.setJavaComment("Aufnahmejahrgang zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSJahrgang */
	public final SchemaTabelleSpalte col_LSJahrgang = add("LSJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaName("jahrgangBis")
			.setJavaComment("Abgangsjahrgang zur abgebenden Schule");

	/** Die Definition der Tabellenspalte AbgangsSchulform */
	public final SchemaTabelleSpalte col_AbgangsSchulform = add("AbgangsSchulform", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaComment("FSchulform zur abgebenden Schule");

	/** Die Definition der Tabellenspalte AbgangsBeschreibung */
	public final SchemaTabelleSpalte col_AbgangsBeschreibung = add("AbgangsBeschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(200)
			.setJavaComment("Abgangsbeschreibung zur abgebenden Schule");

	/** Die Definition der Tabellenspalte AbgangsSchule */
	public final SchemaTabelleSpalte col_AbgangsSchule = add("AbgangsSchule", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
			.setJavaComment("Bezeichnung  zur abgebenden Schule");

	/** Die Definition der Tabellenspalte AbgangsSchuleAnschr */
	public final SchemaTabelleSpalte col_AbgangsSchuleAnschr = add("AbgangsSchuleAnschr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
			.setJavaComment("Anschrift zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSSchulformSIM */
	public final SchemaTabelleSpalte col_LSSchulformSIM = add("LSSchulformSIM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
			.setJavaComment("Statistikkürzel Schulform zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSVersetzung */
	public final SchemaTabelleSpalte col_LSVersetzung = add("LSVersetzung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaComment("Versetzungsvermerk zur abgebenden Schule");

	/** Die Definition der Tabellenspalte LSFachklKennung */
	public final SchemaTabelleSpalte col_LSFachklKennung = add("LSFachklKennung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaComment("Fachklassenkennung zur abgebenden Schule BK");

	/** Die Definition der Tabellenspalte LSFachklSIM */
	public final SchemaTabelleSpalte col_LSFachklSIM = add("LSFachklSIM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
			.setJavaComment("Statiatikkürzel Fachklasse zur abgebenden Schule");

	/** Die Definition der Tabellenspalte FuerSIMExport */
	public final SchemaTabelleSpalte col_FuerSIMExport = add("FuerSIMExport", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("-")
			.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
			.setJavaComment("SIM-Export zur abgebenden Schule");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public final SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank"
					+ " gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerAbgaenge_Schueler_FK */
	public final SchemaTabelleFremdschluessel fk_SchuelerAbgaenge_Schueler_FK = addForeignKey(
			"SchuelerAbgaenge_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);


	/** Die Definition des Non-Unique-Index SchuelerAbgaenge_IDX1 */
	public final SchemaTabelleIndex index_SchuelerAbgaenge_IDX1 = addIndex("SchuelerAbgaenge_IDX1",
			col_Schueler_ID,
			col_LSSchulEntlassDatum
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerAbgaenge.
	 */
	public Tabelle_SchuelerAbgaenge() {
		super("SchuelerAbgaenge", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerAbgaenge");
		setJavaComment("Liste der besuchten Schulen zum Schüler > Schulbesuch");
	}

}
