package de.svws_nrw.module.pdf.htmlcontexts;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnisFehler;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehlerArt;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFachwahlen;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFehler;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerHinweise;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBeratungslehrer;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DBUtilsGostLaufbahn;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.HtmlContext;
import de.svws_nrw.module.pdf.drucktypes.DruckGostLaufbahnplanungSchueler;
import de.svws_nrw.module.pdf.drucktypes.DruckSchule;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "Laufbahnplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostLaufbahnplanungSchueler extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param conn         	die Datenbank-Verbindung
	 * @param schuelerIDs	Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 */
	public HtmlContextGostLaufbahnplanungSchueler(final DBEntityManager conn, final List<Long> schuelerIDs) {
		erzeugeContext(conn, schuelerIDs);
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param conn         	die Datenbank-Verbindung
	 * @param schuelerIDs   Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 */
	private void erzeugeContext(final DBEntityManager conn, final List<Long> schuelerIDs) throws WebApplicationException {

		// ####### Daten validieren. Wirft eine Exception bei Fehlern, andernfalls werden die Manager für die Blockung erzeugt. ###############################

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Datenbankverbindung ungültig.");

		if (schuelerIDs == null)
			throw OperationError.NOT_FOUND.exception("Keine Schueler-IDs übergeben.");

		// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		try {
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (WebApplicationException ex) {
			throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
		}

		// Prüfe die Schüler-IDs. Erzeuge Maps, damit auch später leicht auf die Schülerdaten zugegriffen werden kann.
		final Map<Long, SchuelerStammdaten> mapSchueler = DataSchuelerStammdaten.getListStammdaten(conn, schuelerIDs).stream().collect(Collectors.toMap(s -> s.id, s -> s));
		for (final Long sID : schuelerIDs)
			if (mapSchueler.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden ungültige Schüler-IDs übergeben.");

		final Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten = new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn).getMapFromIDs(schuelerIDs);
		for (final Long sID : schuelerIDs)
			if (mapGostBeratungsdaten.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");


		// ####### Daten sind valide. Bereite nun Daten vor, um den Daten-Context später erzeugen zu können. #################################################################

		// Die Schüler bzw. ihre IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe sollten
		// sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schülern, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		final List<SchuelerStammdaten> sortierteSchueler = mapSchueler.values().stream()
				.sorted(Comparator.comparing((final SchuelerStammdaten s) -> s.nachname, colGerman)
						.thenComparing((final SchuelerStammdaten s) -> s.vorname, colGerman)
						.thenComparing((final SchuelerStammdaten s) -> s.id))
				.toList();
		final List<Long> sortierteSchuelerIDs = sortierteSchueler.stream().map(s -> s.id).toList();


		// Lese die Daten der Schule und die Lernabschnittsdaten der Schüler ein
		final DruckSchule schule = new DruckSchule(DataSchuleStammdaten.getStammdaten(conn));
		final Map<Long, SchuelerLernabschnittsdaten> mapSchuelerLernabschnittsdaten = new DataSchuelerLernabschnittsdaten(conn).getMapFromSchuelerIDsUndSchuljahresabschnittID(schuelerIDs, schule.idSchuljahresabschnitt);

		// Bestimme das aktuelle Halbjahr und das folgende Halbjahr der Schule. Sofern kein Abschnitt gefunden wird, wird 0 zurückgegeben.
		final int aktuellesHalbjahr = schule.aktuellerAbschnitt();
		final int folgeHalbjahr = (aktuellesHalbjahr % 2) + 1;

		// Initialisiere Maps für die Jahrgänge, Klassen, GostJahrgangsdaten und GostFaecherManager, damit diese nur einmal geladen werden müssen. Sie werden später beim Schülerdurchlauf gefüllt, sofern noch nicht in der Map vorhanden.
		final Map<Long, KlassenDaten> mapKlassendatenOhneSchueler = new HashMap<>();
		final Map<Long, JahrgangsDaten> mapJahrgangsdaten = new HashMap<>();
		final Map<Integer, GostJahrgangsdaten> mapGostJahrgangsdaten = new HashMap<>();
		final Map<Integer, GostFaecherManager> mapGostFaecherManager = new HashMap<>();


		// ####### Daten und Strukturen sind vorbereitet. Fülle nun die Objekte mit den Daten der Schüler für den Daten-Context. #################################################################
		final ArrayList<DruckGostLaufbahnplanungSchueler> laufbahnplanungenSchueler = new ArrayList<>();

		for (final Long schuelerID : sortierteSchuelerIDs) {

			// Grunddaten des Schülers setzen.
			final DruckGostLaufbahnplanungSchueler schuelerLaufbahnplanung = new DruckGostLaufbahnplanungSchueler(mapSchueler.get(schuelerID));

			// Ergänze ggf. die Maps zu den Klassen und Jahrgängen.
			final Long schuelerJahrgangID = mapSchuelerLernabschnittsdaten.get(schuelerID).jahrgangID;
			final Long schuelerKlasseID = mapSchuelerLernabschnittsdaten.get(schuelerID).klassenID;
			if (!mapJahrgangsdaten.containsKey(schuelerJahrgangID)) {
				mapJahrgangsdaten.put(schuelerJahrgangID, new DataJahrgangsdaten(conn).getFromID(schuelerJahrgangID));
			}
			if (!mapKlassendatenOhneSchueler.containsKey(schuelerKlasseID)) {
				mapKlassendatenOhneSchueler.put(schuelerKlasseID, new DataKlassendaten(conn).getFromIDOhneSchueler(schuelerKlasseID));
			}

			// Abiturdaten zum Schüler holen.
			final Abiturdaten abiturdaten = DBUtilsGostLaufbahn.get(conn, schuelerID);

			// Wenn zum Schüler kein Abiturjahr gefunden wird, dann gebe leere Daten zurück.
			// So wird der Context auch gefüllt, wenn bei Anfragen zu mehreren Schülern die Daten von nur einem Schüler nicht existiert (wenn auch nur mit Grunddaten).
			// Alle Informationen werden nur initialisiert
			if (abiturdaten.abiturjahr <= 0) {
				setLeereEintragungen(schuelerLaufbahnplanung);
				continue;
			}

			// Da unter Umständen durch Migration und Importe alter Daten aus Schild und LuPO die GOSt-Fächer nicht mit den Fachwahlen übereinstimmen könnten,
			// kann beim Erzeugen der Manager oder Datenstrukturen ein Fehler auftreten. Dieser wird hier abgefangen und ein leerer Eintrag ergänzt.
			try {
				// Erstelle die Maps, welche zum Abiturjahr die Jahrgangsdaten und den FächerManager liefern, und ergänze sie jeweils bei Bedarf.
				// Erstelle dann damit den Abiturdatenmanager des Schülers zum Abruf weiterer Daten.
				if (!mapGostJahrgangsdaten.containsKey(abiturdaten.abiturjahr)) {
					mapGostJahrgangsdaten.put(abiturdaten.abiturjahr, DataGostJahrgangsdaten.getJahrgangsdaten(conn, abiturdaten.abiturjahr));
				}
				if (!mapGostFaecherManager.containsKey(abiturdaten.abiturjahr)) {
					final GostFaecherManager tempGostFaecherManager = DBUtilsFaecherGost.getNurWaehlbareFaecherListeGost(conn, abiturdaten.abiturjahr);
					tempGostFaecherManager.addFachkombinationenAll(DataGostJahrgangFachkombinationen.getFachkombinationen(conn, abiturdaten.abiturjahr));
					mapGostFaecherManager.put(abiturdaten.abiturjahr, tempGostFaecherManager);
				}

				final GostJahrgangsdaten gostJahrgangsdaten = mapGostJahrgangsdaten.get(abiturdaten.abiturjahr);
				final GostFaecherManager gostFaecherManager = mapGostFaecherManager.get(abiturdaten.abiturjahr);
				final AbiturdatenManager abiturdatenManager = new AbiturdatenManager(abiturdaten, gostJahrgangsdaten, gostFaecherManager, GostBelegpruefungsArt.GESAMT);
				final GostLaufbahnplanungBeratungsdaten schuelerBeratungsdaten = mapGostBeratungsdaten.get(schuelerID);
				final JahrgangsDaten schuelerJahrgangsdaten = mapJahrgangsdaten.get(schuelerJahrgangID);

				final int[] kurse = abiturdatenManager.getAnrechenbareKurse();
				final int[] wstd = abiturdatenManager.getWochenstunden();

				// ##### Grunddaten und Summen setzen ###############
				schuelerLaufbahnplanung.abiturjahr = abiturdaten.abiturjahr;
				schuelerLaufbahnplanung.pruefungsordnung = mapSchuelerLernabschnittsdaten.get(schuelerID).pruefungsOrdnung;
				if (!schuelerLaufbahnplanung.pruefungsordnung.toLowerCase().contains("gost"))
					schuelerLaufbahnplanung.pruefungsordnung = "APO-GOSt";
				schuelerLaufbahnplanung.beratungsbogenText = gostJahrgangsdaten.textBeratungsbogen;
				schuelerLaufbahnplanung.emailText = gostJahrgangsdaten.textMailversand;
				schuelerLaufbahnplanung.aktuellesGOStHalbjahr = schuelerJahrgangsdaten.kuerzelStatistik + '.' + aktuellesHalbjahr;
				schuelerLaufbahnplanung.aktuelleKlasse = mapKlassendatenOhneSchueler.get(schuelerKlasseID).kuerzel;
				eintragBeratungGostHalbjahrErgaenzen(schuelerLaufbahnplanung, schuelerJahrgangsdaten, mapJahrgangsdaten.get(schuelerJahrgangsdaten.idFolgejahrgang), folgeHalbjahr);
				eintragBeratungslehrkraefteErgaenzen(schuelerLaufbahnplanung, conn, schuelerBeratungsdaten, gostJahrgangsdaten);
				schuelerLaufbahnplanung.ruecklaufdatum = schuelerBeratungsdaten.beratungsdatum;
				schuelerLaufbahnplanung.beratungsdatum = schuelerBeratungsdaten.beratungsdatum;
				schuelerLaufbahnplanung.letzteBeratungText = getLetzteBeratungText(schuelerLaufbahnplanung);
				schuelerLaufbahnplanung.kommentar = schuelerBeratungsdaten.kommentar;

				schuelerLaufbahnplanung.kursanzahlEF1 = kurse[0];
				schuelerLaufbahnplanung.kursanzahlEF2 = kurse[1];
				schuelerLaufbahnplanung.kursanzahlQ11 = kurse[2];
				schuelerLaufbahnplanung.kursanzahlQ12 = kurse[3];
				schuelerLaufbahnplanung.kursanzahlQ21 = kurse[4];
				schuelerLaufbahnplanung.kursanzahlQ22 = kurse[5];

				schuelerLaufbahnplanung.kursanzahlQPh = schuelerLaufbahnplanung.kursanzahlQ11 + schuelerLaufbahnplanung.kursanzahlQ12 + schuelerLaufbahnplanung.kursanzahlQ21 + schuelerLaufbahnplanung.kursanzahlQ22;

				schuelerLaufbahnplanung.wochenstundenEF1 = wstd[0];
				schuelerLaufbahnplanung.wochenstundenEF2 = wstd[1];
				schuelerLaufbahnplanung.wochenstundenQ11 = wstd[2];
				schuelerLaufbahnplanung.wochenstundenQ12 = wstd[3];
				schuelerLaufbahnplanung.wochenstundenQ21 = wstd[4];
				schuelerLaufbahnplanung.wochenstundenQ22 = wstd[5];

				schuelerLaufbahnplanung.wochenstundenDurchschnittEF = (schuelerLaufbahnplanung.wochenstundenEF1 + schuelerLaufbahnplanung.wochenstundenEF2) / 2.0;
				schuelerLaufbahnplanung.wochenstundenDurchschnittQ1 = (schuelerLaufbahnplanung.wochenstundenQ11 + schuelerLaufbahnplanung.wochenstundenQ12) / 2.0;
				schuelerLaufbahnplanung.wochenstundenDurchschnittQ2 = (schuelerLaufbahnplanung.wochenstundenQ21 + schuelerLaufbahnplanung.wochenstundenQ22) / 2.0;
				schuelerLaufbahnplanung.wochenstundenDurchschnittQPh = (schuelerLaufbahnplanung.wochenstundenQ11 + schuelerLaufbahnplanung.wochenstundenQ12 + schuelerLaufbahnplanung.wochenstundenQ21 + schuelerLaufbahnplanung.wochenstundenQ22) / 4.00;
				schuelerLaufbahnplanung.wochenstundenGesamt = (schuelerLaufbahnplanung.wochenstundenEF1 + schuelerLaufbahnplanung.wochenstundenEF2 + schuelerLaufbahnplanung.wochenstundenQ11 + schuelerLaufbahnplanung.wochenstundenQ12 + schuelerLaufbahnplanung.wochenstundenQ21 + schuelerLaufbahnplanung.wochenstundenQ22) / 2.0;

				// ##### Fachwahlliste erstellen ###############
				schuelerLaufbahnplanung.Fachwahlen = getListFachwahlen(schuelerID, abiturdaten, mapGostFaecherManager.get(abiturdaten.abiturjahr));

				// ##### Fehlerliste und Hinweisliste erstellen ###############
				final GostBelegpruefungErgebnis ergebnis = abiturdatenManager.getBelegpruefungErgebnis();
				for (final GostBelegpruefungErgebnisFehler f : ergebnis.fehlercodes) {
					final GostBelegungsfehlerArt art = GostBelegungsfehlerArt.fromKuerzel(f.art);
					if (art == GostBelegungsfehlerArt.HINWEIS) {
						final DruckGostLaufbahnplanungSchuelerHinweise laufbahnplanungHinweis = new DruckGostLaufbahnplanungSchuelerHinweise();
						laufbahnplanungHinweis.schuelerID = schuelerID;
						laufbahnplanungHinweis.belegungshinweis = f.beschreibung;
						schuelerLaufbahnplanung.Hinweise.add(laufbahnplanungHinweis);
					} else {
						final DruckGostLaufbahnplanungSchuelerFehler laufbahnplanungFehler = new DruckGostLaufbahnplanungSchuelerFehler();
						laufbahnplanungFehler.schuelerID = schuelerID;
						laufbahnplanungFehler.belegungsfehler = f.beschreibung;
						schuelerLaufbahnplanung.Fehler.add(laufbahnplanungFehler);
					}
				}
			} catch (final Exception ex) {
				setLeereEintragungen(schuelerLaufbahnplanung);
				// Alternative zu leeren Daten wäre Abbruch mit Fehlermeldung:
				// throw OperationError.INTERNAL_SERVER_ERROR.exception("Die Daten zur Laufbahn und zum Abitur des Schülers mit der ID " + schuelerID + " und die Einstellungen zu den Fächern der Oberstufe des Abiturjahrgangs " + abiturdaten.abiturjahr + " sind vermutlich inkonsistent. Folgender Fehler ist aufgetreten: " + ex.getMessage())
			}

			laufbahnplanungenSchueler.add(schuelerLaufbahnplanung);
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("LaufbahnplanungSchueler", laufbahnplanungenSchueler);

		super.setContext(context);
	}


	/**
	 * Setzt leere Werte in die Attribute des übergebenen Objektes ein.
	 *
	 * @param laufbahnplanungSchueler	Objekt mit den Daten zur Laufbahnplanung des Schülers.
	 */
	private static void setLeereEintragungen(final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler) {
		laufbahnplanungSchueler.abiturjahr = 0;
		laufbahnplanungSchueler.pruefungsordnung = "";
		laufbahnplanungSchueler.beratungsbogenText = "";
		laufbahnplanungSchueler.emailText = "";
		laufbahnplanungSchueler.aktuellesGOStHalbjahr = "";
		laufbahnplanungSchueler.aktuelleKlasse = "";
		laufbahnplanungSchueler.beratungsGOStHalbjahr = "";
		laufbahnplanungSchueler.beratungslehrkraft = "";
		laufbahnplanungSchueler.beratungslehrkraefte = "";
		laufbahnplanungSchueler.ruecklaufdatum = "";
		laufbahnplanungSchueler.beratungsdatum = "";
		laufbahnplanungSchueler.letzteBeratungText = "";
		laufbahnplanungSchueler.kommentar = "";

		laufbahnplanungSchueler.kursanzahlEF1 = 0;
		laufbahnplanungSchueler.kursanzahlEF2 = 0;
		laufbahnplanungSchueler.kursanzahlQ11 = 0;
		laufbahnplanungSchueler.kursanzahlQ12 = 0;
		laufbahnplanungSchueler.kursanzahlQ21 = 0;
		laufbahnplanungSchueler.kursanzahlQ22 = 0;
		laufbahnplanungSchueler.kursanzahlQPh = 0;

		laufbahnplanungSchueler.wochenstundenEF1 = 0;
		laufbahnplanungSchueler.wochenstundenEF2 = 0;
		laufbahnplanungSchueler.wochenstundenQ11 = 0;
		laufbahnplanungSchueler.wochenstundenQ12 = 0;
		laufbahnplanungSchueler.wochenstundenQ21 = 0;
		laufbahnplanungSchueler.wochenstundenQ22 = 0;

		laufbahnplanungSchueler.wochenstundenDurchschnittEF = 0;
		laufbahnplanungSchueler.wochenstundenDurchschnittQ1 = 0;
		laufbahnplanungSchueler.wochenstundenDurchschnittQ2 = 0;
		laufbahnplanungSchueler.wochenstundenDurchschnittQPh = 0;
		laufbahnplanungSchueler.wochenstundenGesamt = 0;

		laufbahnplanungSchueler.Fachwahlen = new ArrayList<>();
		laufbahnplanungSchueler.Fehler = new ArrayList<>();
		laufbahnplanungSchueler.Hinweise = new ArrayList<>();
	}


	/**
	 * Ergänzt im übergebenen LaufbahnplanungGrunddaten-Objekt das Beratungshalbjahr für den angegebenen Schüler
	 *
	 * @param laufbahnplanungSchueler	Die Laufbahnplanungsgrunddaten, bei denen die Daten ergänzt werden sollen.
	 * @param aktuellerJahrgang 		Der aktuelle Jahrgang des Schülers
	 * @param folgeJahrgang 			Der Folgejahrgang zum aktuellen Jahrgang des Schülers
	 * @param folgeHalbjahr 			Das Folgehalbjahr (1 oder 2) zum aktuellen Halbjahr der eigenen Schule
	 */
	private static void eintragBeratungGostHalbjahrErgaenzen(final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler, final JahrgangsDaten aktuellerJahrgang, final JahrgangsDaten folgeJahrgang, final int folgeHalbjahr) {
		if (folgeHalbjahr == 2) {
			laufbahnplanungSchueler.beratungsGOStHalbjahr = aktuellerJahrgang.kuerzelStatistik + ".2";
		} else if (folgeHalbjahr == 1) {
			if (aktuellerJahrgang.idFolgejahrgang != null)
				laufbahnplanungSchueler.beratungsGOStHalbjahr = folgeJahrgang.kuerzelStatistik + ".1";
			else
				laufbahnplanungSchueler.beratungsGOStHalbjahr = aktuellerJahrgang.kuerzelStatistik + ".2";

		} else
			laufbahnplanungSchueler.beratungsGOStHalbjahr = "";
		// Frühestes Beratungshalbjahr kann die EF.1 sein.
		if (laufbahnplanungSchueler.beratungsGOStHalbjahr.compareTo("EF.1") < 0)
			laufbahnplanungSchueler.beratungsGOStHalbjahr = "EF.1";
	}


	/**
	 * Ergänzt im übergebenen LaufbahnplanungGrunddaten-Objekt die Daten des letzten Beratungslehrers und der Beratungslehrkräfte der Jahrgangsstufe
	 *
	 * @param laufbahnplanungSchueler 	Die Laufbahnplanungsgrunddaten, bei denen die Daten der Beratungslehrkräfte ergänzt werden sollen.
	 * @param conn                    	DB-Verbindung zur Ermittelung der Daten
	 * @param gostBeratungsdaten      	Oberstufenschüler, dessen Beratungslehrkräfte ermitteln werden sollen.
	 * @param gostJahrgangsdaten		Die Daten des GOSt-Jahrgangs.
	 */
	private static void eintragBeratungslehrkraefteErgaenzen(final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler, final DBEntityManager conn, final GostLaufbahnplanungBeratungsdaten gostBeratungsdaten, final GostJahrgangsdaten gostJahrgangsdaten) {
		// Letzte Beratungslehrkraft bestimmen aus den GOSt-Daten des Schülers
		if (gostBeratungsdaten.beratungslehrerID != null) {
			final LehrerStammdaten lehrer = new DataLehrerStammdaten(conn).getFromID(gostBeratungsdaten.beratungslehrerID);
			laufbahnplanungSchueler.beratungslehrkraft = ((lehrer.titel != null && !lehrer.titel.isBlank()) ? lehrer.titel + " " : "") + lehrer.vorname.charAt(0) + ". " + lehrer.nachname;
		}
		// Beratungslehrkräfte der Stufe bestimmen aus den Daten der Jahrgangsstufe
		final List<GostBeratungslehrer> beratungslehrer = gostJahrgangsdaten.beratungslehrer;
		if (beratungslehrer != null && !beratungslehrer.isEmpty()) {
			final StringBuilder strBuilder = new StringBuilder();
			for (final GostBeratungslehrer beratungslehrkraft : beratungslehrer) {
				final LehrerStammdaten lehrer = new DataLehrerStammdaten(conn).getFromID(beratungslehrkraft.id);
				strBuilder.append((lehrer.titel != null && !lehrer.titel.isBlank()) ? lehrer.titel + " " : "").append(lehrer.vorname.charAt(0)).append(". ").append(lehrer.nachname).append("; ");
			}
			laufbahnplanungSchueler.beratungslehrkraefte = strBuilder.toString();
			if (laufbahnplanungSchueler.beratungslehrkraefte.length() >= 2)
				laufbahnplanungSchueler.beratungslehrkraefte = laufbahnplanungSchueler.beratungslehrkraefte.substring(0, laufbahnplanungSchueler.beratungslehrkraefte.length() - 2);
		}
	}


	/**
	 * Erstellt einen Satz für die Angaben zur letzten Beratung.
	 *
	 * @param laufbahnplanungSchueler	Die Daten der Laufbahnplanung des Schülers
	 *
	 * @return 							Ein Satz mit den Informationen zur letzten Beratung.
	 */
	private static String getLetzteBeratungText(final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler) {
		// Ausgabe der Informationen (Zeit und Person) der letzten Beratung zusammenstellen, je nachdem, welche Informationen hinterlegt sind.
		String letzteBeratung = "Die letzte Beratung wurde durchgeführt";
		boolean hatLetzteBeratung = false;
		if (laufbahnplanungSchueler.beratungsdatum != null && !laufbahnplanungSchueler.beratungsdatum.isBlank() && !laufbahnplanungSchueler.beratungsdatum.isEmpty()) {
			final LocalDate beratungsdatum = LocalDate.parse(laufbahnplanungSchueler.beratungsdatum, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			letzteBeratung = letzteBeratung + " am " + beratungsdatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			hatLetzteBeratung = true;
		}
		if (laufbahnplanungSchueler.beratungslehrkraft != null && !laufbahnplanungSchueler.beratungslehrkraft.isBlank() && !laufbahnplanungSchueler.beratungslehrkraft.isEmpty()) {
			letzteBeratung = letzteBeratung + " von " + laufbahnplanungSchueler.beratungslehrkraft;
			hatLetzteBeratung = true;
		}
		letzteBeratung = Boolean.TRUE.equals(hatLetzteBeratung) ? letzteBeratung + "." : "";
		return letzteBeratung;
	}


	/**
	 * Erstellt eine Liste von Fachwahlen aus der Laufbahnplanung eines Schülers für die GOSt.
	 *
	 * @param schuelerID			Die ID des Schülers.
	 * @param abiturdaten			Abiturdaten des Schülers.
	 * @param gostFaecherManager	Die Fächer des Abiturjahrgangs und im Fächermanager.

	 *
	 * @return						Eine Liste der Fachwahlen.
	 */
	private static ArrayList<DruckGostLaufbahnplanungSchuelerFachwahlen> getListFachwahlen(final Long schuelerID, final Abiturdaten abiturdaten, final GostFaecherManager gostFaecherManager) {

		final ArrayList<DruckGostLaufbahnplanungSchuelerFachwahlen> fachwahlen = new ArrayList<>();

		// Erzeuge eine Map Fach-ID → AbiturFachbelegung aus den AbiturDaten
		final Map<Long, AbiturFachbelegung> belegungen = abiturdaten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		// Erzeuge eine Map einstelliges Sprachkürzel → Sprachbelegung aus den AbiturDaten
		final Map<String, Sprachbelegung> sprachbelegungen = abiturdaten.sprachendaten.belegungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		// Erzeuge eine Map einstelliges Sprachkürzel → Sprachprüfung aus den AbiturDaten
		final Map<String, Sprachpruefung> sprachpruefungen = abiturdaten.sprachendaten.pruefungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));

		// Erzeuge für jedes Fach des Abiturjahrgangs eine Zeile, wobei ggf. die Belegungen aus der Map verwendet werden
		for (final GostFach fach : gostFaecherManager.faecher()) {
			final DruckGostLaufbahnplanungSchuelerFachwahlen fachwahl = new DruckGostLaufbahnplanungSchuelerFachwahlen();

			fachwahl.SchuelerID = schuelerID;

			// Erzeuge die Core-DTOs für das Ergebnis der Datenquelle
			final AbiturFachbelegung belegung = belegungen.get(fach.id);

			fachwahl.Kuerzel = fach.kuerzelAnzeige;
			fachwahl.Bezeichnung = fach.bezeichnung;
			fachwahl.FachIstFortfuehrbareFremdspracheInGOSt = false;
			fachwahl.JahrgangFremdsprachenbeginn = "";
			fachwahl.PositionFremdsprachenfolge = "";

			eintragFremdspracheInLaufbahnplanungFachErgaenzen(fachwahl, fach, abiturdaten, sprachbelegungen, sprachpruefungen);

			if (belegung == null) {
				fachwahl.BelegungEF1 = "";
				fachwahl.BelegungEF2 = "";
				fachwahl.BelegungQ11 = "";
				fachwahl.BelegungQ12 = "";
				fachwahl.BelegungQ21 = "";
				fachwahl.BelegungQ22 = "";
				fachwahl.FachIstBelegtInGOSt = false;
			} else {
				fachwahl.BelegungEF1 = eintragFachbelegung(belegung.belegungen[0]);
				fachwahl.BelegungEF2 = eintragFachbelegung(belegung.belegungen[1]);
				fachwahl.BelegungQ11 = eintragFachbelegung(belegung.belegungen[2]);
				fachwahl.BelegungQ12 = eintragFachbelegung(belegung.belegungen[3]);
				fachwahl.BelegungQ21 = eintragFachbelegung(belegung.belegungen[4]);
				fachwahl.BelegungQ22 = eintragFachbelegung(belegung.belegungen[5]);
				fachwahl.FachIstBelegtInGOSt = true;

				if (belegung.abiturFach == null) {
					fachwahl.Abiturfach = "";
				} else {
					fachwahl.Abiturfach = belegung.abiturFach.toString();
				}
			}

			final ZulaessigesFach zulaessigesFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			fachwahl.Aufgabenfeld = Objects.requireNonNullElse(zulaessigesFach.daten.aufgabenfeld, 0);
			fachwahl.Fachgruppe = zulaessigesFach.daten.fachgruppe;
			fachwahl.FarbeClientRGB = zulaessigesFach.getHMTLFarbeRGB().replace("rgba(", "").replace(")", "");

			fachwahlen.add(fachwahl);
		}

		return fachwahlen;
	}


	/**
	 * Ergänzt im übergebenen LaufbahnplanungFachwahl-Objekt den Fremdspracheneintrag, wenn es sich um eine Fremdsprache handelt.
	 * @param laufbahnplanungFach 	Das Laufbahnplanungsfach, bei dem die Fremdspracheninformationen ergänzt werden sollen.
	 * @param fach 					GOST-Fach der Fremdsprache
	 * @param abiturdaten 			Abiturdaten des Schülers
	 * @param sprachbelegungen 		Sprachbelegungen des Schülers aus der Sprachenfolge
	 * @param sprachpruefungen 		Sprachprüfungen des Schülers
	 */
	private static void eintragFremdspracheInLaufbahnplanungFachErgaenzen(final DruckGostLaufbahnplanungSchuelerFachwahlen laufbahnplanungFach, final GostFach fach, final Abiturdaten abiturdaten, final Map<String, Sprachbelegung> sprachbelegungen, final Map<String, Sprachpruefung> sprachpruefungen) {

		if (!fach.istFremdsprache)
			return;

		final ZulaessigesFach zfach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);

		// Verhindern, dass Pseudofächer der Statistik hier als zulässiges Fach verwendet werden.
		if (!(zfach.daten.kuerzelASD.equals("PX") || zfach.daten.kuerzelASD.equals("VX"))) {
			final Sprachbelegung sprachbelegung = sprachbelegungen.get(zfach.daten.kuerzel);
			final Sprachpruefung sprachpruefung = sprachpruefungen.get(zfach.daten.kuerzel);

			if (sprachbelegung != null) {
				if (((sprachbelegung.belegungVonJahrgang != null) && !sprachbelegung.belegungVonJahrgang.isEmpty())
						&& ((zfach.daten.abJahrgang == null)
						|| zfach.daten.abJahrgang.isEmpty()
						|| ((zfach.daten.abJahrgang.compareToIgnoreCase("EF") >= 0) && fach.istFremdSpracheNeuEinsetzend && (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") >= 0))
						|| ((zfach.daten.abJahrgang.compareToIgnoreCase("EF") < 0) && !fach.istFremdSpracheNeuEinsetzend && (sprachbelegung.belegungVonJahrgang.compareToIgnoreCase("EF") < 0)))) {
					// Nur Sprachen heranziehen, die auch vor oder mit der eigenen Belegung hätten starten können. So wird bspw. die neue Fremdsprache ab EF nicht durch die Belegung der gleichen Sprache in der Sek-I als belegt markiert.
					laufbahnplanungFach.FachIstFortfuehrbareFremdspracheInGOSt = true;
					laufbahnplanungFach.JahrgangFremdsprachenbeginn = sprachbelegung.belegungVonJahrgang;
					laufbahnplanungFach.PositionFremdsprachenfolge = sprachbelegung.reihenfolge.toString();
				}
			} else if ((sprachpruefung != null) && (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abiturdaten.sprachendaten, zfach.daten.kuerzel))) {
				laufbahnplanungFach.FachIstFortfuehrbareFremdspracheInGOSt = true;
				if (sprachpruefung.istFeststellungspruefung) {
					laufbahnplanungFach.JahrgangFremdsprachenbeginn = "SFP";
				} else if (sprachpruefung.istHSUPruefung) {
					laufbahnplanungFach.JahrgangFremdsprachenbeginn = "HSU";
				}
				if (sprachpruefung.kannErstePflichtfremdspracheErsetzen)
					laufbahnplanungFach.PositionFremdsprachenfolge = "1";
				else if (sprachpruefung.kannZweitePflichtfremdspracheErsetzen)
					laufbahnplanungFach.PositionFremdsprachenfolge = "2";
				else if (sprachpruefung.kannWahlpflichtfremdspracheErsetzen)
					laufbahnplanungFach.PositionFremdsprachenfolge = "2";
				else {
					laufbahnplanungFach.PositionFremdsprachenfolge = "";
				}
			}
		}
	}


	/**Gibt den Belegungseintrag eines Faches für die Halbjahres-Belegung zurück.
	 *
	 * @param belegungHj 	Halbjahresbelegung des Faches
	 *
	 * @return 				String mit dem Belegungskürzel des Faches gemäß dessen Halbjahresbelegung
	 */
	private static String eintragFachbelegung(final AbiturFachbelegungHalbjahr belegungHj) {
		if (belegungHj == null)
			return "";

		final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
		if (kursart == GostKursart.GK)
			return belegungHj.schriftlich ? "S" : "M";
		if (kursart == GostKursart.LK)
			return "LK";
		if ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF))
			return "M";
		if (kursart == GostKursart.ZK)
			return "ZK";
		if ("AT".equals(belegungHj.kursartKuerzel))
			return "AT";
		return "";
	}
}
