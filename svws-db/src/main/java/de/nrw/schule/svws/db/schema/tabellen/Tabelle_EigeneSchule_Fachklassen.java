package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Fachklassen.
 */
public class Tabelle_EigeneSchule_Fachklassen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Fachklasse im schulinternen Katalog der Fachklassen nur BK und SBK");

	/** Die Definition der Tabellenspalte BKIndex */
	public SchemaTabelleSpalte col_BKIndex = add("BKIndex", SchemaDatentypen.SMALLINT, false)
		.setJavaComment("BKIndex aus der Statkue bildet mit FKS und AP eine eindeutige Kombination IT.NRW");

	/** Die Definition der Tabellenspalte FKS */
	public SchemaTabelleSpalte col_FKS = add("FKS", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Fachklassenschlüssel bildet mit BKIndex und AP eine eindeutige Kombination IT.NRW");

	/** Die Definition der Tabellenspalte AP */
	public SchemaTabelleSpalte col_AP = add("AP", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Laufende Nummer zum FKS bildet mit FKS und BKIndex eine eindeutige Kombination IT.NRW");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bezeichnung der Fachklasse Text");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortiernummer der Fachklasse");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("steuert die Sichtbarkeit der Fachklasse");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Gibt an ob die Fachklasse änderbar ist");

	/** Die Definition der Tabellenspalte Kennung */
	public SchemaTabelleSpalte col_Kennung = add("Kennung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kennung der Fachklasse");

	/** Die Definition der Tabellenspalte FKS_AP_SIM */
	public SchemaTabelleSpalte col_FKS_AP_SIM = add("FKS_AP_SIM", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaComment("Kombination aus FKS und AP mit Minuszeichen");

	/** Die Definition der Tabellenspalte BKIndexTyp */
	public SchemaTabelleSpalte col_BKIndexTyp = add("BKIndexTyp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Typ des BKIndex IT.NW");

	/** Die Definition der Tabellenspalte Beschreibung_W */
	public SchemaTabelleSpalte col_Beschreibung_W = add("Beschreibung_W", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Weiblicher Beschreibungstext für die Fachklassenbezeichnung");

	/** Die Definition der Tabellenspalte Status */
	public SchemaTabelleSpalte col_Status = add("Status", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Status der Fachklasse kann auslaufend sein");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Lernfelder */
	public SchemaTabelleSpalte col_Lernfelder = add("Lernfelder", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für die Lernfelder die zur Fachklasse auf dem Zeugnis ausgewiesen werden");

	/** Die Definition der Tabellenspalte DQR_Niveau */
	public SchemaTabelleSpalte col_DQR_Niveau = add("DQR_Niveau", SchemaDatentypen.INT, false)
		.setJavaComment("DQR-Niveau der Fachklasse Deutscher Qualitätsrahmen");

	/** Die Definition der Tabellenspalte Ebene1Klartext */
	public SchemaTabelleSpalte col_Ebene1Klartext = add("Ebene1Klartext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Berufsebene 1");

	/** Die Definition der Tabellenspalte Ebene2Klartext */
	public SchemaTabelleSpalte col_Ebene2Klartext = add("Ebene2Klartext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Berufsebene 2");

	/** Die Definition der Tabellenspalte Ebene3Klartext */
	public SchemaTabelleSpalte col_Ebene3Klartext = add("Ebene3Klartext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Berufsebene 3");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Fachklassen.
	 */
	public Tabelle_EigeneSchule_Fachklassen() {
		super("EigeneSchule_Fachklassen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.berufskolleg");
		setJavaClassName("DTOFachklassen");
		setJavaComment("Berufskolleg: Tabelle der vorhandenen Fachklassen");
	}

}
