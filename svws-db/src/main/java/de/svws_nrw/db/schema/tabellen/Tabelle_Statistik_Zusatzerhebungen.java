package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Statistik_Zusatzerhebungen.
 */
public class Tabelle_Statistik_Zusatzerhebungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte idSchuljahresabschnitt */
	public final SchemaTabelleSpalte col_idSchuljahresabschnitt = add("idSchuljahresabschnitt", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID des Schuljahresabschnitts, zu welchem die Daten der Zusatzerhebungen gehören");

	/** Die Definition der Tabellenspalte statistikAnsprechpartnerName */
	public final SchemaTabelleSpalte col_statistikAnsprechpartnerName = add("statistikAnsprechpartnerName", SchemaDatentypen.VARCHAR, false)
			.setDatenlaenge(255)
			.setJavaComment("Der Name des Statistik-Ansprechpartners für IT.NRW");

	/** Die Definition der Tabellenspalte statistikAnsprechpartnerTelefon */
	public final SchemaTabelleSpalte col_statistikAnsprechpartnerTelefon = add("statistikAnsprechpartnerTelefon", SchemaDatentypen.VARCHAR, false)
			.setDatenlaenge(20)
			.setJavaComment("Die Telefonnummer des Statistik-Ansprechpartners für IT.NRW");

	/** Die Definition der Tabellenspalte statistikAnsprechpartnerEmail */
	public final SchemaTabelleSpalte col_statistikAnsprechpartnerEmail = add("statistikAnsprechpartnerEmail", SchemaDatentypen.VARCHAR, false)
			.setDatenlaenge(100)
			.setJavaComment("Die E-Mail-Adresse des Statistik-Ansprechpartners für IT.NRW");

	/** Die Definition der Tabellenspalte hatInternationaleKontakte */
	public final SchemaTabelleSpalte col_hatInternationaleKontakte = add("hatInternationaleKontakte", SchemaDatentypen.INT, false)
			.setNotNull()
			.setDefault("0")
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Schule internationale Kontakte pflegt oder nicht");

	/** Die Definition der Tabellenspalte anzahlSchulanmeldungen */
	public final SchemaTabelleSpalte col_anzahlSchulanmeldungen = add("anzahlSchulanmeldungen", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der Schulanmeldungen nach § 35 SchulG");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungsverfahren */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungsverfahren = add("anzahlZurueckstellungsverfahren", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der Zurückstellungsverfahren im Zuge der Anmeldungen");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenInsgesamt */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenInsgesamt = add("anzahlZurueckstellungenInsgesamt", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlichen Zurückstellungen insgesamt");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenWeiblich */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenWeiblich = add("anzahlZurueckstellungenWeiblich", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlichen Zurückstellungen weiblich");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenAuslaenderZusammen */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenAuslaenderZusammen = add("anzahlZurueckstellungenAuslaenderZusammen", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlichen Zurückstellungen Ausländer zusammen");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenAuslaenderWeiblich */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenAuslaenderWeiblich = add("anzahlZurueckstellungenAuslaenderWeiblich", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlichen Zurückstellungen Ausländer weiblich");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenInsgesamtQuartal4 */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenInsgesamtQuartal4 = add("anzahlZurueckstellungenInsgesamtQuartal4", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlich zurückgestellten Kinder, die im 4. Quartal des Erhebungsvorjahres 6 Jahre alt geworden sind");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenInsgesamtQuartal1 */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenInsgesamtQuartal1 = add("anzahlZurueckstellungenInsgesamtQuartal1", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlich zurückgestellten Kinder, die im 1. Quartal des Erhebungsjahres 6 Jahre alt geworden sind");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenInsgesamtQuartal2 */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenInsgesamtQuartal2 = add("anzahlZurueckstellungenInsgesamtQuartal2", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlich zurückgestellten Kinder, die im 2. Quartal des Erhebungsjahres 6 Jahre alt geworden sind");

	/** Die Definition der Tabellenspalte anzahlZurueckstellungenInsgesamtQuartal3 */
	public final SchemaTabelleSpalte col_anzahlZurueckstellungenInsgesamtQuartal3 = add("anzahlZurueckstellungenInsgesamtQuartal3", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der tatsächlich zurückgestellten Kinder, die im 3. Quartal des Erhebungsjahres 6 Jahre alt geworden sind");

	/** Die Definition der Tabellenspalte idInternatArt */
	public final SchemaTabelleSpalte col_idInternatArt = add("idInternatArt", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID der Art des Internats (Core-Type InternatArt)");

	/** Die Definition der Tabellenspalte anzahlInternatplaetzeJungenMoeglich */
	public final SchemaTabelleSpalte col_anzahlInternatplaetzeJungenMoeglich = add("anzahlInternatplaetzeJungenMoeglich", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der möglichen Internatsplätze für Jungen");

	/** Die Definition der Tabellenspalte anzahlInternatplaetzeJungenBelegt */
	public final SchemaTabelleSpalte col_anzahlInternatplaetzeJungenBelegt = add("anzahlInternatplaetzeJungenBelegt", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der belegten Internatsplätze für Jungen");

	/** Die Definition der Tabellenspalte anzahlInternatplaetzeMaedchenMoeglich */
	public final SchemaTabelleSpalte col_anzahlInternatplaetzeMaedchenMoeglich = add("anzahlInternatplaetzeMaedchenMoeglich", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der möglichen Internatsplätze für Mädchen");

	/** Die Definition der Tabellenspalte anzahlInternatplaetzeMaedchenBelegt */
	public final SchemaTabelleSpalte col_anzahlInternatplaetzeMaedchenBelegt = add("anzahlInternatplaetzeMaedchenBelegt", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der belegten Internatsplätze für Mädchen");

	/** Die Definition der Tabellenspalte anzahlInternatplaetzeNeutralMoeglich */
	public final SchemaTabelleSpalte col_anzahlInternatplaetzeNeutralMoeglich = add("anzahlInternatplaetzeNeutralMoeglich", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der möglichen geschlechtsneutralen Internatsplätze");

	/** Die Definition der Tabellenspalte anzahlInternatplaetzeNeutralBelegt */
	public final SchemaTabelleSpalte col_anzahlInternatplaetzeNeutralBelegt = add("anzahlInternatplaetzeNeutralBelegt", SchemaDatentypen.INT, false)
			.setJavaComment("Die Anzahl der belegten geschlechtsneutralen Internatsplätze");

	/** Die Definition der Tabellenspalte durchschnittKlinikschuleAllgemeinbildendInsgesamt */
	public final SchemaTabelleSpalte col_durchschnittKlinikschuleAllgemeinbildendInsgesamt = add("durchschnittKlinikschuleAllgemeinbildendInsgesamt", SchemaDatentypen.FLOAT, false)
			.setJavaComment("Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend insgesamt");

	/** Die Definition der Tabellenspalte durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte */
	public final SchemaTabelleSpalte col_durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = add("durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte", SchemaDatentypen.FLOAT, false)
			.setJavaComment("Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend mit Schwerstbehinderung");

	/** Die Definition der Tabellenspalte durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt */
	public final SchemaTabelleSpalte col_durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = add("durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt", SchemaDatentypen.FLOAT, false)
			.setJavaComment("Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit insgesamt");

	/** Die Definition der Tabellenspalte durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte */
	public final SchemaTabelleSpalte col_durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = add("durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte", SchemaDatentypen.FLOAT, false)
			.setJavaComment("Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit mit Schwerstbehinderung");

	/** Die Definition der Tabellenspalte durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt */
	public final SchemaTabelleSpalte col_durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = add("durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt", SchemaDatentypen.FLOAT, false)
			.setJavaComment("Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit insgesamt");

	/** Die Definition der Tabellenspalte durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte */
	public final SchemaTabelleSpalte col_durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = add("durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte", SchemaDatentypen.FLOAT, false)
			.setJavaComment("Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit mit Schwerstbehinderung");


	/** Die Definition des Fremdschlüssels Statistik_Zusatzerhebungen_Schuljahresabschnitt_FK */
	public final SchemaTabelleFremdschluessel fk_Statistik_Zusatzerhebungen_Schuljahresabschnitt_FK = addForeignKey(
			"Statistik_Zusatzerhebungen_Schuljahresabschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_idSchuljahresabschnitt, Schema.tab_Schuljahresabschnitte.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Statistik_Zusatzerhebungen.
	 */
	public Tabelle_Statistik_Zusatzerhebungen() {
		super("Statistik_Zusatzerhebungen", SchemaRevisionen.REV_85);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOStatistikZusatzerhebungen");
		setJavaComment("Tabelle mit den Daten der Zusatzerhebungen zur amtlichen Schulstatistik für einen Schuljahresabschnitt");
	}

}
