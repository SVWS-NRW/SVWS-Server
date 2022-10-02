package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statkue_AndereGrundschulen.
 */
public class Tabelle_Statkue_AndereGrundschulen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SNR */
	public SchemaTabelleSpalte col_SNR = add("SNR", SchemaDatentypen.VARCHAR, true).setDatenlaenge(6)
		.setNotNull()
		.setJavaComment("Statkue Tabelle IT.NRW: Schulnummer anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte SF */
	public SchemaTabelleSpalte col_SF = add("SF", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Statkue Tabelle IT.NRW: Schulform anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte ABez1 */
	public SchemaTabelleSpalte col_ABez1 = add("ABez1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Statkue Tabelle IT.NRW: Bezeichnung anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Statkue Tabelle IT.NRW: leer anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte Ort */
	public SchemaTabelleSpalte col_Ort = add("Ort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Statkue Tabelle IT.NRW: leer anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte Auswahl */
	public SchemaTabelleSpalte col_Auswahl = add("Auswahl", SchemaDatentypen.INT, false)
		.setConverter("Boolean01Converter")
		.setJavaComment("Statkue Tabelle IT.NRW: imme r0  anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte RegSchl */
	public SchemaTabelleSpalte col_RegSchl = add("RegSchl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Statkue Tabelle IT.NRW: RegSchl ist identisch mit SF  anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte geaendert */
	public SchemaTabelleSpalte col_geaendert = add("geaendert", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Datum der letzten Änderung anderer Schulen oder Herkunftsformen");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statkue_AndereGrundschulen.
	 */
	public Tabelle_Statkue_AndereGrundschulen() {
		super("Statkue_AndereGrundschulen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("statkue");
		setJavaClassName("DTOStatkueAndereGrundschulen");
		setJavaComment("Statkue von IT.NRW Schulnummernschlüssel für Schulen außerhalb NRW oder sonstige Herkunftsschulen");
	}

}
