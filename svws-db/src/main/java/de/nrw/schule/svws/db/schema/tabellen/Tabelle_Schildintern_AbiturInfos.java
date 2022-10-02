package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_AbiturInfos.
 */
public class Tabelle_Schildintern_AbiturInfos extends SchemaTabelle {

	/** Die Definition der Tabellenspalte PrfOrdnung */
	public SchemaTabelleSpalte col_PrfOrdnung = add("PrfOrdnung", SchemaDatentypen.VARCHAR, true).setDatenlaenge(20)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin");

	/** Die Definition der Tabellenspalte AbiFach */
	public SchemaTabelleSpalte col_AbiFach = add("AbiFach", SchemaDatentypen.VARCHAR, true).setDatenlaenge(1)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin");

	/** Die Definition der Tabellenspalte Bedingung */
	public SchemaTabelleSpalte col_Bedingung = add("Bedingung", SchemaDatentypen.VARCHAR, true).setDatenlaenge(3)
		.setJavaComment("Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin");

	/** Die Definition der Tabellenspalte AbiInfoKrz */
	public SchemaTabelleSpalte col_AbiInfoKrz = add("AbiInfoKrz", SchemaDatentypen.VARCHAR, true).setDatenlaenge(20)
		.setJavaComment("Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin");

	/** Die Definition der Tabellenspalte AbiInfoBeschreibung */
	public SchemaTabelleSpalte col_AbiInfoBeschreibung = add("AbiInfoBeschreibung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin");

	/** Die Definition der Tabellenspalte AbiInfoText */
	public SchemaTabelleSpalte col_AbiInfoText = add("AbiInfoText", SchemaDatentypen.TEXT, false)
		.setJavaComment("Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_AbiturInfos.
	 */
	public Tabelle_Schildintern_AbiturInfos() {
		super("Schildintern_AbiturInfos", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternAbiturInfos");
		setJavaComment("Abfragen für die Algorithmen der alten Abitur-Prüfungsordnungen (ausgelaufen???)");
	}

}
