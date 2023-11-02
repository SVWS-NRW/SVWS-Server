package de.svws_nrw.module.pdf.gost.laufbahnplanung;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnisFehler;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehlerArt;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchueler;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFachwahlen;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFehler;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerHinweise;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
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
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
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
 * Eine Sammlung von Methoden zum Erstellen von Daten-Contexts zum Bereich "GostLaufbahnplanung" für die html-Templates zur Erstellung von PDF-Dateien.
 */
public final class PDFContextGostLaufbahnplanung {

	private PDFContextGostLaufbahnplanung() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Liefert die Daten in Form eines Context zum Füllen eines html-Templates.
	 *
	 * @param conn         			die Datenbank-Verbindung
	 * @param schuelerIDs           Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 *
	 * @return 						der Context
	 */
	public static Context setContext(final DBEntityManager conn, final List<Long> schuelerIDs) throws WebApplicationException {

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
		final Map<Long, DTOSchueler> mapSchueler = conn
			.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long sID : schuelerIDs)
			if (mapSchueler.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden ungültige Schüler-IDs übergeben.");

		final Map<Long, DTOGostSchueler> mapGostSchueler = conn
			.queryNamed("DTOGostSchueler.schueler_id.multiple", schuelerIDs, DTOGostSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));
		for (final Long sID : schuelerIDs)
			if (mapGostSchueler.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");


		// ####### Daten sind valide. Bereite nun Daten vor, um den Daten-Context später erzeugen zu können. #################################################################

		// Die Schüler bzw. ihre IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe sollten
		// sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schülern, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		final List<DTOSchueler> sortiertSchueler = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class).stream()
			.sorted(Comparator.comparing((final DTOSchueler s) -> s.Nachname, colGerman)
				.thenComparing((final DTOSchueler s) -> s.Vorname, colGerman)
				.thenComparing((final DTOSchueler s) -> s.ID))
			.toList();
		final List<Long> sortiertSchuelerIDs = sortiertSchueler.stream().map(s -> s.ID).toList();


		// Bestimme für die verifizierten Schüler deren aktuellen Lernabschnitt gemäß dem Schuljahresabschnitt unter EigeneSchule.
		// Erstelle damit eine Map mit der JahrgangsID zur SchuelerID.
		// Damit kann später die aktuelle Jahrgangsstufe des Schülers und das Beratungshalbjahr bestimmt werden.

		// 1. Informationen zur eigenen Schule bestimmen
		final DTOEigeneSchule eigeneSchule = conn.queryNamed("DTOEigeneSchule.all", DTOEigeneSchule.class).getResultList().get(0);

		// 2. Schuljahresabschnitt der eigenen Schule bestimmen
		final DTOSchuljahresabschnitte aktuellerSchulabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, eigeneSchule.Schuljahresabschnitts_ID);
		if (aktuellerSchulabschnitt == null)
			throw OperationError.NOT_FOUND.exception("Parameter der Abfrage ungültig: Ein Schuljahresabschnitt mit der ID " + eigeneSchule.Schuljahresabschnitts_ID.toString() + " existiert nicht.");

		// 3. Aktuelles und folgendes Halbjahr der Schule ermitteln
		final int aktuellesHalbjahr = aktuellerSchulabschnitt.Abschnitt;
		final int folgeHalbjahr = (aktuellesHalbjahr % 2) + 1;

		// 4. Jahrgänge der eigenen Schule bestimmen
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn
			.queryNamed("DTOJahrgang.all", DTOJahrgang.class).getResultList()
			.stream().collect(Collectors.toMap(j -> j.ID, j -> j));

		// 5. Klassen der eigenen Schule bestimmen
		final Map<Long, DTOKlassen> mapKlassen = conn
			.queryNamed("DTOKlassen.all", DTOKlassen.class).getResultList()
			.stream().collect(Collectors.toMap(j -> j.ID, j -> j));

		// 6. Prüfungsordnung zur SchuelerID ablegen
		final Map<Long, String> mapSchuelerPruefungsordnungen = conn
			.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class, schuelerIDs, eigeneSchule.Schuljahresabschnitts_ID)
			.stream().collect(Collectors.toMap(sla -> sla.Schueler_ID, sla -> sla.PruefOrdnung));

		// 7. JahrgangsID zur SchuelerID ablegen
		final Map<Long, Long> mapSchuelerJahrgangIDs = conn
			.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class, schuelerIDs, eigeneSchule.Schuljahresabschnitts_ID)
			.stream().collect(Collectors.toMap(sla -> sla.Schueler_ID, sla -> sla.Jahrgang_ID));

		// 8. KlassenID zur SchuelerID ablegen
		final Map<Long, Long> mapSchuelerKlassenIDs = conn
			.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class, schuelerIDs, eigeneSchule.Schuljahresabschnitts_ID)
			.stream().collect(Collectors.toMap(sla -> sla.Schueler_ID, sla -> sla.Klassen_ID));

		// 9. Maps für die GostJahrgangsdaten und GostFaecherManager der Abiturjahrgänge, damit diese nur einmal geladen werden müssen. Wir später beim Schülerdurchlauf gefüllt.
		final Map<Integer, GostJahrgangsdaten> mapGostJahrgangsdaten = new HashMap<>();
		final Map<Integer, GostFaecherManager> mapGostFaecherManager = new HashMap<>();


		// ####### Daten sind vorbereitet. Fülle nun die Objekte für den Daten-Context. #################################################################
		final ArrayList<DruckGostLaufbahnplanungSchueler> laufbahnplanungenSchueler = new ArrayList<>();

		for (final Long schuelerID : sortiertSchuelerIDs) {

			final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler = new DruckGostLaufbahnplanungSchueler();

			// Grunddaten des Schülers setzen.
			laufbahnplanungSchueler.SchuelerID = schuelerID;
			laufbahnplanungSchueler.Nachname = mapSchueler.get(schuelerID).Nachname;
			laufbahnplanungSchueler.Vorname = mapSchueler.get(schuelerID).Vorname;
			laufbahnplanungSchueler.Geschlecht = mapSchueler.get(schuelerID).Geschlecht.kuerzel;
			laufbahnplanungSchueler.Geburtsdatum = LocalDate.parse(mapSchueler.get(schuelerID).Geburtsdatum).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			laufbahnplanungSchueler.ExterneSchulnummer = mapSchueler.get(schuelerID).ExterneSchulNr;

			final Abiturdaten abiturdaten = DBUtilsGostLaufbahn.get(conn, schuelerID);

			// Wenn zum Schüler kein Abiturjahr gefunden wird, dann gebe leere Daten zurück.
			// So wird der Context auch gefüllt, wenn bei Anfragen zu mehreren Schülern die Daten von nur einem Schüler nicht existiert (wenn auch nur mit Grunddaten).
			// Alle Informationen werden nur initialisiert
			if (abiturdaten.abiturjahr <= 0) {
				setLeereEintragungen(laufbahnplanungSchueler);
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

				final int[] kurse = abiturdatenManager.getAnrechenbareKurse();
				final int[] wstd = abiturdatenManager.getWochenstunden();

				// ##### Grunddaten und Summen setzen ###############
				laufbahnplanungSchueler.Abiturjahr = abiturdaten.abiturjahr;
				laufbahnplanungSchueler.Pruefungsordnung = mapSchuelerPruefungsordnungen.get(schuelerID);
				if (!laufbahnplanungSchueler.Pruefungsordnung.toLowerCase().contains("gost"))
					laufbahnplanungSchueler.Pruefungsordnung = "APO-GOSt";
				laufbahnplanungSchueler.Beratungsbogentext = gostJahrgangsdaten.textBeratungsbogen;
				laufbahnplanungSchueler.Emailtext = gostJahrgangsdaten.textMailversand;
				laufbahnplanungSchueler.AktuellesGOStHalbjahr = mapJahrgaenge.get(mapSchuelerJahrgangIDs.get(schuelerID)).ASDJahrgang + '.' + aktuellesHalbjahr;
				laufbahnplanungSchueler.AktuelleKlasse = mapKlassen.get(mapSchuelerKlassenIDs.get(schuelerID)).Klasse;
				eintragBeratungsGOStHalbjahrInLaufbahnplanungGrunddatenErgaenzen(laufbahnplanungSchueler, mapJahrgaenge.get(mapSchuelerJahrgangIDs.get(schuelerID)), mapJahrgaenge.get(mapJahrgaenge.get(mapSchuelerJahrgangIDs.get(schuelerID)).Folgejahrgang_ID), folgeHalbjahr);
				eintragBeratungslehrkraefteInLaufbahnplanungGrunddatenErgaenzen(laufbahnplanungSchueler, conn, mapGostSchueler.get(schuelerID), abiturdaten);
				laufbahnplanungSchueler.Ruecklaufdatum = mapGostSchueler.get(schuelerID).DatumRuecklauf;
				laufbahnplanungSchueler.Beratungsdatum = mapGostSchueler.get(schuelerID).DatumBeratung;
				laufbahnplanungSchueler.LetzteBeratungText = getLetzteBeratungText(laufbahnplanungSchueler);
				laufbahnplanungSchueler.Kommentar = mapGostSchueler.get(schuelerID).Kommentar;

				laufbahnplanungSchueler.KursanzahlEF1 = kurse[0];
				laufbahnplanungSchueler.KursanzahlEF2 = kurse[1];
				laufbahnplanungSchueler.KursanzahlQ11 = kurse[2];
				laufbahnplanungSchueler.KursanzahlQ12 = kurse[3];
				laufbahnplanungSchueler.KursanzahlQ21 = kurse[4];
				laufbahnplanungSchueler.KursanzahlQ22 = kurse[5];

				laufbahnplanungSchueler.KursanzahlQPh = laufbahnplanungSchueler.KursanzahlQ11 + laufbahnplanungSchueler.KursanzahlQ12 + laufbahnplanungSchueler.KursanzahlQ21 + laufbahnplanungSchueler.KursanzahlQ22;

				laufbahnplanungSchueler.WochenstundenEF1 = wstd[0];
				laufbahnplanungSchueler.WochenstundenEF2 = wstd[1];
				laufbahnplanungSchueler.WochenstundenQ11 = wstd[2];
				laufbahnplanungSchueler.WochenstundenQ12 = wstd[3];
				laufbahnplanungSchueler.WochenstundenQ21 = wstd[4];
				laufbahnplanungSchueler.WochenstundenQ22 = wstd[5];

				laufbahnplanungSchueler.WochenstundenDurchschnittEF = (laufbahnplanungSchueler.WochenstundenEF1 + laufbahnplanungSchueler.WochenstundenEF2) / 2.0;
				laufbahnplanungSchueler.WochenstundenDurchschnittQ1 = (laufbahnplanungSchueler.WochenstundenQ11 + laufbahnplanungSchueler.WochenstundenQ12) / 2.0;
				laufbahnplanungSchueler.WochenstundenDurchschnittQ2 = (laufbahnplanungSchueler.WochenstundenQ21 + laufbahnplanungSchueler.WochenstundenQ22) / 2.0;
				laufbahnplanungSchueler.WochenstundenDurchschnittQPh = (laufbahnplanungSchueler.WochenstundenQ11 + laufbahnplanungSchueler.WochenstundenQ12 + laufbahnplanungSchueler.WochenstundenQ21 + laufbahnplanungSchueler.WochenstundenQ22) / 4.00;
				laufbahnplanungSchueler.WochenstundenGesamt = (laufbahnplanungSchueler.WochenstundenEF1 + laufbahnplanungSchueler.WochenstundenEF2 + laufbahnplanungSchueler.WochenstundenQ11 + laufbahnplanungSchueler.WochenstundenQ12 + laufbahnplanungSchueler.WochenstundenQ21 + laufbahnplanungSchueler.WochenstundenQ22) / 2.0;

				// ##### Fachwahlliste erstellen ###############
				laufbahnplanungSchueler.Fachwahlen = getListFachwahlen(schuelerID, abiturdaten, mapGostFaecherManager.get(abiturdaten.abiturjahr));

				// ##### Fehlerliste und Hinweisliste erstellen ###############
				final GostBelegpruefungErgebnis ergebnis = abiturdatenManager.getBelegpruefungErgebnis();
				for (final GostBelegpruefungErgebnisFehler f : ergebnis.fehlercodes) {
					final GostBelegungsfehlerArt art = GostBelegungsfehlerArt.fromKuerzel(f.art);
					if (art == GostBelegungsfehlerArt.HINWEIS) {
						final DruckGostLaufbahnplanungSchuelerHinweise laufbahnplanungHinweis = new DruckGostLaufbahnplanungSchuelerHinweise();
						laufbahnplanungHinweis.schuelerID = schuelerID;
						laufbahnplanungHinweis.belegungshinweis = f.beschreibung;
						laufbahnplanungSchueler.Hinweise.add(laufbahnplanungHinweis);
					} else {
						final DruckGostLaufbahnplanungSchuelerFehler laufbahnplanungFehler = new DruckGostLaufbahnplanungSchuelerFehler();
						laufbahnplanungFehler.schuelerID = schuelerID;
						laufbahnplanungFehler.belegungsfehler = f.beschreibung;
						laufbahnplanungSchueler.Fehler.add(laufbahnplanungFehler);
					}
				}
			} catch (final Exception ex) {
				setLeereEintragungen(laufbahnplanungSchueler);
				// Alternative zu leeren Daten wäre Abbruch mit Fehlermeldung:
				// throw OperationError.INTERNAL_SERVER_ERROR.exception("Die Daten zur Laufbahn und zum Abitur des Schülers mit der ID " + schuelerID + " und die Einstellungen zu den Fächern der Oberstufe des Abiturjahrgangs " + abiturdaten.abiturjahr + " sind vermutlich inkonsistent. Folgender Fehler ist aufgetreten: " + ex.getMessage())
			}

			laufbahnplanungenSchueler.add(laufbahnplanungSchueler);
		}

		// Daten-Context für thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("LaufbahnplanungSchueler", laufbahnplanungenSchueler);
		return context;
		}


	/**
	 * Setzt leere Werte in die Attribute des übergebenen Objektes ein.
	 *
	 * @param laufbahnplanungSchueler	Objekt mit den Daten zur Laufbahnplanung des Schülers.
	 */
	private static void setLeereEintragungen(final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler) {
		laufbahnplanungSchueler.Abiturjahr = 0;
		laufbahnplanungSchueler.Pruefungsordnung = "";
		laufbahnplanungSchueler.Beratungsbogentext = "";
		laufbahnplanungSchueler.Emailtext = "";
		laufbahnplanungSchueler.AktuellesGOStHalbjahr = "";
		laufbahnplanungSchueler.AktuelleKlasse = "";
		laufbahnplanungSchueler.BeratungsGOStHalbjahr = "";
		laufbahnplanungSchueler.Beratungslehrkraft = "";
		laufbahnplanungSchueler.Beratungslehrkraefte = "";
		laufbahnplanungSchueler.Ruecklaufdatum = "";
		laufbahnplanungSchueler.Beratungsdatum = "";
		laufbahnplanungSchueler.LetzteBeratungText = "";
		laufbahnplanungSchueler.Kommentar = "";

		laufbahnplanungSchueler.KursanzahlEF1 = 0;
		laufbahnplanungSchueler.KursanzahlEF2 = 0;
		laufbahnplanungSchueler.KursanzahlQ11 = 0;
		laufbahnplanungSchueler.KursanzahlQ12 = 0;
		laufbahnplanungSchueler.KursanzahlQ21 = 0;
		laufbahnplanungSchueler.KursanzahlQ22 = 0;
		laufbahnplanungSchueler.KursanzahlQPh = 0;

		laufbahnplanungSchueler.WochenstundenEF1 = 0;
		laufbahnplanungSchueler.WochenstundenEF2 = 0;
		laufbahnplanungSchueler.WochenstundenQ11 = 0;
		laufbahnplanungSchueler.WochenstundenQ12 = 0;
		laufbahnplanungSchueler.WochenstundenQ21 = 0;
		laufbahnplanungSchueler.WochenstundenQ22 = 0;

		laufbahnplanungSchueler.WochenstundenDurchschnittEF = 0;
		laufbahnplanungSchueler.WochenstundenDurchschnittQ1 = 0;
		laufbahnplanungSchueler.WochenstundenDurchschnittQ2 = 0;
		laufbahnplanungSchueler.WochenstundenDurchschnittQPh = 0;
		laufbahnplanungSchueler.WochenstundenGesamt = 0;

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
	private static void eintragBeratungsGOStHalbjahrInLaufbahnplanungGrunddatenErgaenzen(final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler, final DTOJahrgang aktuellerJahrgang, final DTOJahrgang folgeJahrgang, final int folgeHalbjahr) {
		if (folgeHalbjahr == 2) {
			laufbahnplanungSchueler.BeratungsGOStHalbjahr = aktuellerJahrgang.ASDJahrgang + ".2";
		} else if (folgeHalbjahr == 1) {
			if (aktuellerJahrgang.Folgejahrgang_ID != null)
				laufbahnplanungSchueler.BeratungsGOStHalbjahr = folgeJahrgang.ASDJahrgang + ".1";
			else
				laufbahnplanungSchueler.BeratungsGOStHalbjahr = aktuellerJahrgang.ASDJahrgang + ".2";

		} else
			laufbahnplanungSchueler.BeratungsGOStHalbjahr = "";
		// Frühestes Beratungshalbjahr kann die EF.1 sein.
		if (laufbahnplanungSchueler.BeratungsGOStHalbjahr.compareTo("EF.1") < 0)
			laufbahnplanungSchueler.BeratungsGOStHalbjahr = "EF.1";
	}


	/**
	 * Ergänzt im übergebenen LaufbahnplanungGrunddaten-Objekt die Daten des letzten Beratungslehrers und der Beratungslehrkräfte der Jahrgangsstufe
	 *
	 * @param laufbahnplanungSchueler	Die Laufbahnplanungsgrunddaten, bei denen die Daten der Beratungslehrkräfte ergänzt werden sollen.
	 * @param conn 						DB-Verbindung zur Ermittelung der Daten
	 * @param gostSchueler 				Oberstufenschüler, dessen Beratungslehrkräfte ermitteln werden sollen.
	 * @param abiturdaten 				Abiturdaten des entsprechenden Schülers
	 */
	private static void eintragBeratungslehrkraefteInLaufbahnplanungGrunddatenErgaenzen(final DruckGostLaufbahnplanungSchueler laufbahnplanungSchueler, final DBEntityManager conn, final DTOGostSchueler gostSchueler, final Abiturdaten abiturdaten) {
		List<DTOLehrer> beratungslehrerdaten = null;
		// Letzte Beratungslehrkraft bestimmen aus den GOSt-Daten des Schülers
		if (gostSchueler.Beratungslehrer_ID != null) {
			beratungslehrerdaten = conn.queryNamed("DTOLehrer.id", gostSchueler.Beratungslehrer_ID, DTOLehrer.class);
		}
		if (beratungslehrerdaten != null) {
			final DTOLehrer lehrer = beratungslehrerdaten.get(0);
			laufbahnplanungSchueler.Beratungslehrkraft = lehrer.Vorname.charAt(0) + ". " + lehrer.Nachname;
		}
		// Beratungslehrkräfte der Stufe bestimmen aus den Daten der Jahrgangsstufe
		final List<DTOGostJahrgangBeratungslehrer> beratungslehrer = conn.queryNamed("DTOGostJahrgangBeratungslehrer.abi_jahrgang", abiturdaten.abiturjahr, DTOGostJahrgangBeratungslehrer.class);
		if (beratungslehrer != null && !beratungslehrer.isEmpty()) {
			beratungslehrerdaten = conn.queryNamed("DTOLehrer.id.multiple", beratungslehrer.stream().map(l -> l.Lehrer_ID).toList(), DTOLehrer.class);
		}
		if (beratungslehrerdaten != null && !beratungslehrerdaten.isEmpty()) {
			final StringBuilder strBuilder = new StringBuilder();
			for (final DTOLehrer lehrer : beratungslehrerdaten) {
				strBuilder.append(lehrer.Vorname.charAt(0)).append(". ").append(lehrer.Nachname).append("; ");
			}
			laufbahnplanungSchueler.Beratungslehrkraefte = strBuilder.toString();
			if (laufbahnplanungSchueler.Beratungslehrkraefte.length() >= 2)
				laufbahnplanungSchueler.Beratungslehrkraefte = laufbahnplanungSchueler.Beratungslehrkraefte.substring(0, laufbahnplanungSchueler.Beratungslehrkraefte.length() - 2);
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
		if (laufbahnplanungSchueler.Beratungsdatum != null && !laufbahnplanungSchueler.Beratungsdatum.isBlank() && !laufbahnplanungSchueler.Beratungsdatum.isEmpty()) {
			final LocalDate beratungsdatum = LocalDate.parse(laufbahnplanungSchueler.Beratungsdatum, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			letzteBeratung = letzteBeratung + " am " + beratungsdatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			hatLetzteBeratung = true;
		}
		if (laufbahnplanungSchueler.Beratungslehrkraft != null && !laufbahnplanungSchueler.Beratungslehrkraft.isBlank() && !laufbahnplanungSchueler.Beratungslehrkraft.isEmpty()) {
			letzteBeratung = letzteBeratung + " von " + laufbahnplanungSchueler.Beratungslehrkraft;
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
	 * @param belegungHj die Halbjahresbelegung des Faches
	 *
	 * @return String mit dem Belegungskürzel des Faches gemäß dessen Halbjahresbelegung
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
