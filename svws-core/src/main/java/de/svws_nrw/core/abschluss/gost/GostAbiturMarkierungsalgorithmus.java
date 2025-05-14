package de.svws_nrw.core.abschluss.gost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.abschluss.gost.belegpruefung.AbiFaecher;
import de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse führt eine automatische Markierung von Halbjahresbelegungen von
 * anrechenbaren Kursen für die Abiturberechnung durch.
 */
public final class GostAbiturMarkierungsalgorithmus {

	/** Das Ergebnis des Algorithmus */
	private final @NotNull GostAbiturMarkierungsalgorithmusErgebnis ergebnis = new GostAbiturMarkierungsalgorithmusErgebnis();

	/** Die aktuelle Einrückung für das Logging */
	private @NotNull String logIndent = "";

	/** Der Abiturdaten-Manager */
	private final @NotNull AbiturdatenManager manager;

	/** Die zuvor durchgeführten Belegprüfung zu dem Projektkurs */
	private final @NotNull Projektkurse belegpruefungProjektkurse;

	/** Die zuvor durchgeführten Belegprüfung zu den Abiturfächern */
	private final @NotNull AbiFaecher belegpruefungAbiturfaecher;

	/** Die Belegungen der vier Abiturfächer */
	final @NotNull AbiturFachbelegung[] abi = new AbiturFachbelegung[4];

	/** Gibt an, ob die Zulassung mit diesem Ergebniss erreicht wurde oder nicht. */
	private boolean hatZulassung = false;

	/** Die Summe der Notenpunkte, normiert nach der Formel aus der APO-Gost */
	private int summeNormiert = -1;

	/** Die Summe der Notenpunkte aller Markierungen, LKs sind doppelt gezählt */
	private int summeNotenpunkte = 0;

	/** Die Summe der markierten Kurse, LKs sind doppelt gezählt */
	private int summeKurse = 0;

	/** Die Summe der markierten Defizite im LK-Bereich */
	private int defiziteLK = 0;

	/** Die Summe der markierten Defizite im GK-Bereich */
	private int defiziteGK = 0;

	/** Gibt an, wieviele Fremdsprachen im Abiturbereich markiert wurden. */
	private int anzahlAbiFremdsprachen = 0;

	/** Gibt an, ob im Abitur eine neu einsetzende Fremdsprache markiert wurde */
	private boolean hatAbiFremspracheNeueinsetzend = false;

	/** Gibt an, ob im Abitur eine Gesellschaftswissenschaft - außer Religion - markiert wurde */
	private boolean hatAbiGesellschaftswissenschaft = false;

	/** Gibt an, ob im Abitur Kunst oder Musik gewählt wurde */
	private boolean hatAbiKunstOderMusik = false;

	/** Die Anzahl der noch erlaubten Musik-Kurse */
	private int restErlaubtMusik = 5;

	/** Die Anzahl der noch erlaubten Vokal- oder Instrumentalpraktischen Kurse */
	private int restErlaubtVPIP = 2;

	/** Die Anzahl der noch erlaubten Literatur Kurse */
	private int restErlaubtKUMUErsatz = 2;

	/** Eine Map für die Anzahl von Belegungen in einem Fachbereich */
	private final @NotNull Map<GostFachbereich, Integer> anzahlBelegungen = new HashMap<>();

	/** Die Menge der markierten Halbjahresbelegungen */
	private final @NotNull HashMap2D<Long, Integer, GostAbiturMarkierungsalgorithmusMarkierung> markiert = new HashMap2D<>();

	/** Vergleicht zwei bewertete Belegungen anhand der Note und als zweiten Kriterium anhand des Faches und als drittes Kriterium anhand des Halbjahres */
	private final @NotNull Comparator<GostAbiturMarkierungsalgorithmusBelegung> comparatorBelegungen =
			(final @NotNull GostAbiturMarkierungsalgorithmusBelegung a, final @NotNull GostAbiturMarkierungsalgorithmusBelegung b) -> {
				int tmp = b.notenpunkte - a.notenpunkte;
				if (tmp != 0)
					return tmp;
				// Ansonsten gilt die Sortierung des Faches ...
				final GostFach aFach = getFach(a.belegung);
				final GostFach bFach = getFach(b.belegung);
				if ((aFach == null) || (bFach == null)) // Kann hier nicht auftreten - nur für den Compiler...
					return -1;
				tmp = GostFachbereich.compareGostFach(aFach, bFach);
				if (tmp != 0)
					return tmp;
				// Ansonsten die des Halbjahres
				final GostHalbjahr hjA = GostHalbjahr.fromKuerzel(a.belegungHalbjahr.halbjahrKuerzel);
				final GostHalbjahr hjB = GostHalbjahr.fromKuerzel(b.belegungHalbjahr.halbjahrKuerzel);
				if ((hjA == null) || (hjB == null)) // Kann hier nicht auftreten - nur für den Compiler...
					return -1;
				return hjB.id - hjA.id;
			};

	/** Vergleicht zwei Ergebnisse miteinander und sortiert diese */
	private static final @NotNull Comparator<GostAbiturMarkierungsalgorithmus> comparatorStates =
			(final @NotNull GostAbiturMarkierungsalgorithmus a, final @NotNull GostAbiturMarkierungsalgorithmus b) -> {
				if (a.ergebnis.erfolgreich == !b.ergebnis.erfolgreich)
					return a.ergebnis.erfolgreich ? -1 : 1;
				if (a.hatZulassung == !b.hatZulassung)
					return a.hatZulassung ? -1 : 2;
				int tmp = b.summeNormiert - a.summeNormiert;
				if (tmp != 0)
					return tmp;
				final int aDefizite = a.defiziteLK + a.defiziteGK;
				final int bDefizite = b.defiziteLK + b.defiziteGK;
				tmp = aDefizite - bDefizite;
				if (tmp != 0)
					return tmp;
				tmp = a.defiziteLK - b.defiziteLK;
				if (tmp != 0)
					return tmp;
				tmp = a.summeKurse - b.summeKurse;
				if (tmp != 0)
					return tmp;
				final Integer aPjk = a.anzahlBelegungen.get(GostFachbereich.PROJEKTKURSE);
				final Integer bPjk = b.anzahlBelegungen.get(GostFachbereich.PROJEKTKURSE);
				return ((aPjk == null) ? 0 : aPjk) - ((bPjk == null) ? 0 : bPjk);
			};

	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private GostAbiturMarkierungsalgorithmus(final @NotNull AbiturdatenManager manager, final @NotNull List<GostBelegpruefung> belegpruefungen) {
		this.manager = manager;
		// Bestimme die zuvor durchgeführten Belegprüfungen zu den Projektkursen, dem Schwerpunkt und den Abiturfächern
		Projektkurse belegpruefungProjektkurse = null;
		AbiFaecher belegpruefungAbiturfaecher = null;
		for (final @NotNull GostBelegpruefung pruefung : belegpruefungen) {
			if (pruefung instanceof Projektkurse)
				belegpruefungProjektkurse = (Projektkurse) pruefung;
			if (pruefung instanceof AbiFaecher)
				belegpruefungAbiturfaecher = (AbiFaecher) pruefung;
		}
		if (belegpruefungProjektkurse == null)
			throw new DeveloperNotificationException("Die Projektkursprüfung muss als Belegprüfung vorhanden sein.");
		this.belegpruefungProjektkurse = belegpruefungProjektkurse;
		if (belegpruefungAbiturfaecher == null)
			throw new DeveloperNotificationException("Die Abiturfächerprüfung muss als Belegprüfung vorhanden sein.");
		this.belegpruefungAbiturfaecher = belegpruefungAbiturfaecher;
	}


	/**
	 * Erstellt einen neue Instanz des Algorithmus basierend auf der übergebenen Instanz.
	 *
	 * @param original   die ursprüngliche Instanz, welche kopiert wird
	 */
	private GostAbiturMarkierungsalgorithmus(final @NotNull GostAbiturMarkierungsalgorithmus original) {
		// Kopiere die Liste der Markierungen, hier wird eine andere Liste verwendet, da in dem Baum unterschiedliche Zustände vorhanden sein können
		this.ergebnis.markierungen.addAll(original.ergebnis.markierungen);
		// Verwende ein gemeinsames Log, um die möglichen Fälle durchzuspielen
		this.ergebnis.log = original.ergebnis.log;
		this.logIndent = original.logIndent + "    ";
		// Kopiere die initial ermittelten Daten zur weiteren Verwendung
		this.ergebnis.erfolgreich = original.ergebnis.erfolgreich;
		this.manager = original.manager;
		this.belegpruefungProjektkurse = original.belegpruefungProjektkurse;
		this.belegpruefungAbiturfaecher = original.belegpruefungAbiturfaecher;
		for (int i = 0; i < this.abi.length; i++)
			this.abi[i] = original.abi[i];
		// Übernehme den aktuellen Zustand des Originals, da an diesem Baum-Knoten eine Aufspaltung von diesem Zustand aus erfolgt
		this.summeNormiert = original.summeNormiert;
		this.hatZulassung = original.hatZulassung;
		this.summeNotenpunkte = original.summeNotenpunkte;
		this.summeKurse = original.summeKurse;
		this.defiziteLK = original.defiziteLK;
		this.defiziteGK = original.defiziteGK;
		this.anzahlAbiFremdsprachen = original.anzahlAbiFremdsprachen;
		this.hatAbiGesellschaftswissenschaft = original.hatAbiGesellschaftswissenschaft;
		this.hatAbiFremspracheNeueinsetzend = original.hatAbiFremspracheNeueinsetzend;
		this.hatAbiKunstOderMusik = original.hatAbiKunstOderMusik;
		this.restErlaubtMusik = original.restErlaubtMusik;
		this.restErlaubtVPIP = original.restErlaubtVPIP;
		this.restErlaubtKUMUErsatz = original.restErlaubtKUMUErsatz;
		for (final Map.@NotNull Entry<GostFachbereich, Integer> e : original.anzahlBelegungen.entrySet())
			this.anzahlBelegungen.put(e.getKey(), e.getValue());
		for (final Map.@NotNull Entry<Long, Map<Integer, GostAbiturMarkierungsalgorithmusMarkierung>> e1 : original.markiert.getEntrySet())
			for (final Map.@NotNull Entry<Integer, GostAbiturMarkierungsalgorithmusMarkierung> e2 : e1.getValue().entrySet())
				this.markiert.put(e1.getKey(), e2.getKey(), e2.getValue());
	}


	/**
	 * Führt eine automatische Markierung von Halbjahresbelegungen zur Verwendung in Block II
	 * von anrechenbaren Kursen für die Abiturberechnung durch. Vorraussetzung hierfür ist, dass
	 * alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @param manager           der Manager zur Verwaltung der Abiturdaten.
	 * @param belegpruefungen   die zuvor durchgeführten Belegprüfung der Laufbahnplanung
	 *
	 * @return das Ergebnis der Berechnung
	 */
	public static @NotNull GostAbiturMarkierungsalgorithmusErgebnis berechne(final @NotNull AbiturdatenManager manager,
			final @NotNull List<GostBelegpruefung> belegpruefungen) {
		// Initialisiere den Algorithmus
		final @NotNull GostAbiturMarkierungsalgorithmus initialState = new GostAbiturMarkierungsalgorithmus(manager, belegpruefungen);
		if (!initialState.init())
			return initialState.ergebnis;

		// Prüfe ausgehend vom initialen Zustand als Wurzel eines Baums mit den möglichen States die unterschiedlichen Varianten
		// von Markierungen und bestimme deren Gesamtpunktzahl für den Vergleich. Beginne dabei mit der Markierung der ersten Fremdsprache
		final @NotNull List<GostAbiturMarkierungsalgorithmus> states = initialState.markiereErsteFremsprache();
		if (states.isEmpty())
			return initialState.ergebnis;

		// Sortiere die Zustände, um das Ergebnis des Markierungsalgorithmus zu ermitteln, welches am besten geeignet ist
		states.sort(comparatorStates);

		// Schreibe das Ergebnis für den gewählten Zustand der Markierungsberechnung in das Ergebnis und gebe dieses zurück
		final @NotNull GostAbiturMarkierungsalgorithmus state = states.getFirst();
		state.schreibeErgebnis();
		final @NotNull GostAbiturMarkierungsalgorithmusErgebnis ergebnis = state.ergebnis;
		return ergebnis;
	}


	/**
	 * Initialisiert den Zustand des Markierungsalgorithmus mit Markierungen von Kursen, welche auf jeden Fall
	 * markiert werden müssen. Diese bilden die Grundlage für späteren Markierungsschritte.
	 *
	 * @return true, falls nach der Initialisierung noch eine Markierung möglich ist, und ansonsten false
	 */
	private boolean init() {
		// Anzahl der generell fortführbaren Sprachen gemäß Sprachenfolge und Sprachprüfungen ermitteln (Projektkurs, ProjBll)
		final int anzahlFortfuehrbareFremdsprachen = SprachendatenUtils.getFortfuehrbareSprachenInGOSt(manager.getSprachendaten()).size();
		ergebnis.log.add(logIndent + "Anzahl der Fortführbaren Fremdsprachen: " + anzahlFortfuehrbareFremdsprachen);

		// Schwerpunkt bestimmen (SprachenSP, NWSP)
		final boolean hatSchwerpunktFremdsprachen = 2 <= manager.zaehleBelegungInHalbjahren(manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE), GostHalbjahr.Q22);
		final boolean hatSchwerpunktNaturwissenschaften = 2 <= manager.zaehleBelegungInHalbjahren(manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH), GostHalbjahr.Q22);
		ergebnis.log.add(logIndent + "Schwerpunkt: " + (hatSchwerpunktFremdsprachen ? "Fremdsprachen" : "") + " "
				+ (hatSchwerpunktNaturwissenschaften ? "Naturwissenschaften" : ""));
		// Markierung der Abiturfächer
		if (!markiereAbiturfaecher())
			return false;
		// Markierung des Faches Deutsch, sofern es kein Abiturfach ist
		if (!markiereDeutsch())
			return false;
		// Markierung des Faches Mathematik, sofern es kein Abiturfach ist
		if (!markiereMathematik())
			return false;
		return true;
	}


	/**
	 * Schreibt die gesetzten Markierung in das Ergebnis und trägt zusätzlich für die nicht markierten Kursbelegungen ein, dass diese
	 * nicht markiert sind.
	 */
	private void schreibeErgebnis() {
		// Iteriere durch alle Fachbelegung und erzeuge jeweils die Einträge für die zu setzenden Markierungen
		for (final @NotNull AbiturFachbelegung belegung : manager.daten().fachbelegungen) {
			// Bestimme das Fach der Belegung
			final GostFach fach = getFach(belegung);
			if (fach == null)
				return;
			// Prüfe, ob die Belegung überhaupt Abiturrelevant ist
			if (GostFachbereich.getBereiche(fach).isEmpty())
				continue;
			// Durchwandere die einzelnen Halbjahresbelegungen der Qualifikationsphase, und füge die Markierungsinformationen zum Ergebnis hinzu
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr hjBelegung = belegung.belegungen[hj.id];
				if (hjBelegung == null)
					continue;
				GostAbiturMarkierungsalgorithmusMarkierung markierung = markiert.getOrNull(fach.id, hj.id);
				if (markierung == null) {
					markierung = new GostAbiturMarkierungsalgorithmusMarkierung();
					markierung.idFach = fach.id;
					markierung.idHalbjahr = hj.id;
					markierung.markiert = false;
				}
				ergebnis.markierungen.add(markierung);
			}
		}
	}


	/**
	 * Bestimmt das Fach für die übergeben Abiturfachbelegung.
	 *
	 * @param belegung   die Fachbelegung
	 *
	 * @return das Fach
	 */
	private GostFach getFach(final @NotNull AbiturFachbelegung belegung) {
		final GostFach fach = manager.faecher().get(belegung.fachID);
		if (fach == null) {
			ergebnis.log.add(logIndent + "Fehler im Algorithmus: Kann das Fach mit der ID " + belegung.fachID
					+ " nicht bestimmen. Überprüfen Sie die Fächerdefinition für dieses Fach.");
			ergebnis.erfolgreich = false;
		}
		return fach;
	}


	/**
	 * Bestimme die zwei besten Halbjahresbelegungen und gib die Halbjahre zurück.
	 *
	 * @param belegung   die Fachbelegung
	 *
	 * @return die beiden besten Halbjahre
	 */
	private @NotNull GostHalbjahr[] getZweiBesteBelegungen(final @NotNull AbiturFachbelegung belegung) {
		// Bestimme die beiden besten Halbjahresbewertungen
		final @NotNull GostHalbjahr[] result = new GostHalbjahr[2];
		int np1 = -1;
		int np2 = -1;
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
			final int np = getNotenpunkte(belegung, hj); // 0 Punkte ist nicht belegt und -1 wird hier bei Nicht-Belegung zurückgegeben
			if (np <= 0)
				continue;
			if ((result[0] == null) || (np > np1)) {
				result[1] = result[0];
				np2 = np1;
				result[0] = hj;
				np1 = np;
			} else if ((result[1] == null) || (np > np2)) {
				result[1] = hj;
				np2 = np;
			}
		}
		return result;
	}


	/**
	 * Erhöht die Anzahl der Belegungen in dem angegebenen Fachbereich, sofern das Fach zu dem Fachbereich
	 * gehört.
	 *
	 * @param fb     der Fachbereich
	 * @param fach   das Fach, welches belegt wurde
	 */
	private void increaseBelegungInFachbereich(final @NotNull GostFachbereich fb, final @NotNull GostFach fach) {
		if (fb.hat(fach)) {
			Integer anzahl = anzahlBelegungen.computeIfAbsent(fb, k -> 0);
			if (anzahl == null)
				anzahl = 0;
			anzahlBelegungen.put(fb, anzahl + 1);
		}
	}


	/**
	 * Bestimmt die Summe der Notenpunkte einer Belegung in den angegebenen Halbjahren. Sollte die Belegung
	 * nicht vorhanden oder fehlerhaft sein, so wird -1 zurückgegeben.
	 *
	 * @param belegung    die Fachbelegung
	 * @param halbjahre   die Halbjahre
	 *
	 * @return die Summe der Notenpunkte
	 */
	private int getNotenpunkte(final @NotNull AbiturFachbelegung belegung, final @NotNull GostHalbjahr... halbjahre) {
		int summe = 0;
		for (final GostHalbjahr hj : halbjahre) {
			final AbiturFachbelegungHalbjahr hjBelegung = belegung.belegungen[hj.id];
			// Prüfe ob die Halbjahresbelegung existiert
			if (hjBelegung == null)
				return -1;
			// Prüfe, ob eine Note gesetzt wurde
			if ((hjBelegung.notenkuerzel == null) || (hjBelegung.notenkuerzel.isBlank()))
				return -1;
			final @NotNull Note note = Note.fromKuerzel(hjBelegung.notenkuerzel);
			if (!note.istNote(manager.getSchuljahr()))
				return -1;
			final NoteKatalogEintrag nke = note.daten(manager.getSchuljahr());
			if (nke == null)
				return -1;
			if (nke.notenpunkte == null)
				return -1;
			summe += nke.notenpunkte;
		}
		return summe;
	}


	private boolean markiereHalbjahresbelegung(final @NotNull AbiturFachbelegung belegung, final @NotNull GostHalbjahr hj) {
		final boolean istAbiturbereich = (belegung.abiturFach != null);
		final GostFach fach = getFach(belegung);
		if (fach == null)
			return false;
		final AbiturFachbelegungHalbjahr hjBelegung = belegung.belegungen[hj.id];
		// Prüfe ob die Halbjahresbelegung existiert
		if (hjBelegung == null) {
			if (istAbiturbereich)
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel + " fehlt eine Belegung des Abiturfaches");
			return false;
		}
		// Prüfe, ob eine Note gesetzt wurde
		if ((hjBelegung.notenkuerzel == null) || (hjBelegung.notenkuerzel.isBlank())) {
			ergebnis.log.add(
					logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach keine gültige Note erteilt. Der Kurs kann daher nicht anrechenbar sein");
			return false;
		}
		final @NotNull Note note = Note.fromKuerzel(hjBelegung.notenkuerzel);
		if (!note.istNote(manager.getSchuljahr())) {
			ergebnis.log.add(
					logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach eine Pseudonote erteilt. Der Kurs kann daher nicht anrechenbar sein.");
			return false;
		}
		// Prüfe, ob es sich bei der Note um ein Ungenügend handelt oder nicht
		if (note == Note.UNGENUEGEND) {
			if (istAbiturbereich)
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel
						+ " wurde die Note ungenügend für das Abiturfach erteilt. Somit ist keine Zulassung mehr möglich, da der Kurs somit als nicht belegt gilt.");
			return false;
		}
		// Prüfe LK-Bereich bzw. GK-Bereich
		final boolean istLKBelegung = GostKursart.LK.kuerzel.equals(hjBelegung.kursartKuerzel);
		if ((note == Note.AUSREICHEND_MINUS) || (note == Note.MANGELHAFT_PLUS) || (note == Note.MANGELHAFT) || (note == Note.MANGELHAFT_MINUS)) {
			if (istLKBelegung) {
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel + " liegt ein Defizit im LK-Bereich vor.");
				defiziteLK++;
			} else {
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel + " liegt ein einbringungspflichtiges Defizit im GK-Bereich vor.");
				defiziteGK++;
			}
		}
		if (defiziteLK > 3) {
			ergebnis.log
					.add(logIndent + "  Es ist keine Zulassung mehr möglich, da die maximale Anzahl der Defizite im Leistungskursbereich überschritten wurde.");
			return false;
		}
		// Addiere die Notenpunkte und die Anzahl der Kurse, zähle LK-Belegungen dabei doppelt
		final NoteKatalogEintrag nke = note.daten(manager.getSchuljahr());
		if ((nke == null) || (nke.notenpunkte == null)) {
			ergebnis.log.add(logIndent + "  Interner Fehler: Der Note sind keine Notenpunkte zugeordnet.");
			return false;
		}
		summeNotenpunkte += (istLKBelegung ? 2 : 1) * nke.notenpunkte;
		summeKurse++;

		// Zähle die Belegungen in einzelnen Fachbereich
		increaseBelegungInFachbereich(GostFachbereich.DEUTSCH, fach);
		increaseBelegungInFachbereich(GostFachbereich.FREMDSPRACHE, fach);
		increaseBelegungInFachbereich(GostFachbereich.KUNST_MUSIK, fach);
		increaseBelegungInFachbereich(GostFachbereich.GESCHICHTE, fach);
		increaseBelegungInFachbereich(GostFachbereich.SOZIALWISSENSCHAFTEN, fach);
		increaseBelegungInFachbereich(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH, fach);
		increaseBelegungInFachbereich(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION, fach);
		increaseBelegungInFachbereich(GostFachbereich.PHILOSOPHIE, fach);
		increaseBelegungInFachbereich(GostFachbereich.RELIGION, fach);
		increaseBelegungInFachbereich(GostFachbereich.MATHEMATIK, fach);
		increaseBelegungInFachbereich(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, fach);
		increaseBelegungInFachbereich(GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND, fach);
		increaseBelegungInFachbereich(GostFachbereich.NATURWISSENSCHAFTLICH, fach);
		increaseBelegungInFachbereich(GostFachbereich.PROJEKTKURSE, fach);
		// Setze die Markierung
		final @NotNull GostAbiturMarkierungsalgorithmusMarkierung markierung = new GostAbiturMarkierungsalgorithmusMarkierung();
		markierung.idFach = belegung.fachID;
		markierung.idHalbjahr = hj.id;
		markierung.markiert = true;
		markiert.put(markierung.idFach, markierung.idHalbjahr, markierung);
		return true;
	}


	private boolean markiereBelegungDurchgaengig(final AbiturFachbelegung belegung) {
		if (belegung == null) {
			ergebnis.log.add(logIndent + "  Es liegt keine Fachbelegung vor.");
			return false;
		}
		final GostFach fach = getFach(belegung);
		if (fach == null)
			return false;
		// Prüfe, auch ob die Fachbelegung auch zwischenzeitlich bilinguale und nicht bilinguale Kurse hat
		final @NotNull List<AbiturFachbelegung> fachbelegungen = manager.getFachbelegungByFachkuerzel(fach.kuerzel);

		// Iteriere durch alle Halbjahres-Belegungen
		for (final GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
			// Bestimme die Halbjahresbelegung so, dass auch ein Wechsel zwischen bilingual und nicht bilingual berücksichtigt wird
			AbiturFachbelegung current = null;
			for (final @NotNull AbiturFachbelegung fb : fachbelegungen) {
				if (fb.belegungen[hj.id] != null) {
					if (current != null) {
						ergebnis.log.add(logIndent + "  Die gleichzeitige Belegung eines Sachfaches bilingual und nicht-biligual ist nicht zulässig.");
						return false;
					}
					current = fb;
				}
			}

			// Prüfe, ob eine Note gesetzt wurde
			if (current == null) {
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach keine gültige Belegung gefunden.");
				return false;
			}
			final AbiturFachbelegungHalbjahr hjBelegung = current.belegungen[hj.id];
			if ((hjBelegung == null) || (hjBelegung.notenkuerzel == null) || (hjBelegung.notenkuerzel.isBlank())) {
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach keine gültige Note erteilt.");
				return false;
			}
			final @NotNull Note note = Note.fromKuerzel(hjBelegung.notenkuerzel);
			if (!note.istNote(manager.getSchuljahr())) {
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach eine Pseudonote erteilt. Das ist nicht zulässig.");
				return false;
			}
			// Prüfe, ob es sich bei der Note um ein Ungenügend handelt oder nicht
			if (note == Note.UNGENUEGEND) {
				ergebnis.log.add(logIndent + "  Im Halbjahr " + hj.kuerzel
						+ " wurde die Note ungenügend für das Fach erteilt. Somit ist keine Zulassung mehr möglich, da das Fach somit als nicht belegt gilt.");
				return false;
			}

			// Prüfe, on die Belegung bereits markiert wurde - wenn nicht, dann versuche eine Belegung
			if ((this.markiert.getOrNull(fach.id, hj.id) == null) && (!markiereHalbjahresbelegung(current, hj)))
				return false;
		}
		return true;
	}


	/**
	 * Markiert die beiden besten Halbjahresbelegungen der Qualifikationsphase aus der
	 * übergebenen Menge der Fachbelegungen.
	 *
	 * @param belegungen   die Belegungen
	 *
	 * @return true, wenn die Markierungen erfolgreich waren, und ansonsten false
	 */
	private boolean markiereZweiBeste(final @NotNull List<AbiturFachbelegung> belegungen) {
		if (manager.zaehleBelegungInHalbjahren(belegungen, GostHalbjahr.getQualifikationsphase()) < 2) {
			ergebnis.log.add(logIndent + "  Keine Fachbelegung in zwei Halbjahren vorhanden.");
			return false;
		}

		final @NotNull AbiturFachbelegung[] resBelegung = new AbiturFachbelegung[2];
		final @NotNull GostFach[] resFach = new GostFach[2];
		final @NotNull GostHalbjahr[] resHalbjahre = new GostHalbjahr[2];
		final @NotNull int[] resNotenpunkte = new int[2];
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			// Bestimme die beiden besten Halbjahresbewertungen
			final @NotNull GostHalbjahr[] halbjahre = getZweiBesteBelegungen(belegung);
			for (final @NotNull GostHalbjahr hj : halbjahre) {
				if (hj == null)
					continue;
				final int np = getNotenpunkte(belegung, hj);
				if ((resBelegung[0] == null) || (np > resNotenpunkte[0])) {
					resBelegung[1] = resBelegung[0];
					resFach[1] = resFach[0];
					resHalbjahre[1] = resHalbjahre[0];
					resNotenpunkte[1] = resNotenpunkte[0];
					resBelegung[0] = belegung;
					resFach[0] = fach;
					resHalbjahre[0] = hj;
					resNotenpunkte[0] = np;
				} else if ((resBelegung[1] == null) || (np > resNotenpunkte[1])) {
					resBelegung[1] = belegung;
					resFach[1] = fach;
					resHalbjahre[1] = hj;
					resNotenpunkte[1] = np;
				}
			}
		}

		if ((resBelegung[0] == null) || (resBelegung[1] == null) || (resHalbjahre[0] == null) || (resHalbjahre[1] == null)
				|| (resFach[0] == null) || (resFach[1] == null)) {
			ergebnis.log.add(logIndent + "  Fehler: Konnte keine Bewertung für zwei Halbjahre bestimmen.");
			return false;
		}
		// Erzeuge einen neuen State, bei welchem die beiden besten Halbjahresmarkierungen markiert sind
		ergebnis.log.add(logIndent + "  Markiere die beiden Kurse in "
				+ resHalbjahre[0].kuerzel + " (" + resNotenpunkte[0] + " Punkte) für " + resFach[0].kuerzelAnzeige + " und "
				+ resHalbjahre[1].kuerzel + " (" + resNotenpunkte[1] + " Punkte) für " + resFach[1].kuerzelAnzeige + "...");

		return markiereHalbjahresbelegung(resBelegung[0], resHalbjahre[0]) && markiereHalbjahresbelegung(resBelegung[1], resHalbjahre[1]);
	}



	private boolean markiereAbiturfaecher() {
		abi[0] = belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK1);
		abi[1] = belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK2);
		abi[2] = belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB3);
		abi[3] = belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB4);
		for (int i = 1; i <= 4; i++) {
			final AbiturFachbelegung belegung = abi[i - 1];
			if (belegung == null) {
				ergebnis.log.add(logIndent + "Es wurde keine Belegung für das  " + i + ". Abiturfaches gefunden.");
				return false;
			}
			final GostFach fach = getFach(belegung);
			if (fach == null)
				return false;
			ergebnis.log.add(logIndent + "Markiere die vier Kurse des " + i + ". Abiturfaches (" + fach.kuerzelAnzeige + ")...");
			// Prüfe, ob es sich bei der Belegung um eine neu einsetzen Fremdsprache handelt
			if (fach.istFremdsprache)
				anzahlAbiFremdsprachen++;
			if (fach.istFremdsprache && fach.istFremdSpracheNeuEinsetzend)
				this.hatAbiFremspracheNeueinsetzend = true;
			// Prüfe auf die Belegung einer Gesellschaftswissenschaft außer Religion
			if (GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH.hat(fach))
				hatAbiGesellschaftswissenschaft = true;
			// Prüfe die Belegung auf Kunst oder Musik
			final boolean istKunst = manager.faecher().fachIstKunst(fach.id);
			final boolean istMusik = manager.faecher().fachIstMusik(fach.id);
			if (istKunst || istMusik)
				this.hatAbiKunstOderMusik = true;
			if (istMusik && ((i == 1) || (i == 2))) { // LK1 oder LK2
				this.restErlaubtMusik = 0;
				this.restErlaubtVPIP = 0;
			} else if (istMusik) { // AB3 oder AB4
				this.restErlaubtMusik = 2;
			}

			this.restErlaubtMusik = 1;
			// Markiere alle vier Halbjahresbelegungen
			if (!markiereBelegungDurchgaengig(belegung))
				return false;
		}
		return true;
	}


	/**
	 * Markiere alle Kurse des Faches Deutsch, sofern dieses noch nicht belegt wurde
	 *
	 * @return true, wenn die Deutsch-Fachbelegung sichergestellt ist
	 */
	private boolean markiereDeutsch() {
		if (anzahlBelegungen.computeIfAbsent(GostFachbereich.DEUTSCH, k -> 0) == 0) {
			ergebnis.log.add(logIndent + "Markiere die vier Kurse für das Fach Deutsch...");
			final AbiturFachbelegung deutsch = manager.getRelevanteFachbelegung(GostFachbereich.DEUTSCH);
			if (!markiereBelegungDurchgaengig(deutsch))
				return false;
		}
		return true;
	}


	/**
	 * Markiere alle Kurse des Faches Mathematik, sofern dieses noch nicht belegt wurde
	 *
	 * @return true, wenn die Mathematik-Fachbelegung sichergestellt ist
	 */
	private boolean markiereMathematik() {
		if (anzahlBelegungen.computeIfAbsent(GostFachbereich.MATHEMATIK, k -> 0) == 0) {
			ergebnis.log.add(logIndent + "Markiere die vier Kurse für das Fach Mathematik...");
			final AbiturFachbelegung mathematik = manager.getRelevanteFachbelegung(GostFachbereich.MATHEMATIK);
			if (!markiereBelegungDurchgaengig(mathematik))
				return false;
		}
		return true;
	}


	/**
	 * Markierungsbaum - Ebene 1:
	 * Markiere alle Kurse einer Fremdsprache, die durchgängig belegt wurde, sofern nicht schon
	 * eine als Abiturfach belegt wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 2. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private @NotNull List<GostAbiturMarkierungsalgorithmus> markiereErsteFremsprache() {
		final @NotNull List<GostAbiturMarkierungsalgorithmus> newStates = new ArrayList<>();
		ergebnis.log.add(logIndent + "Markierung der ersten Fremdsprache:");

		// Prüfe zunächst, ob die Fremdsprachenverpflichtung für eine Fremdsprache nicht bereits durch die Abiturfächer erfüllt wurde
		if (anzahlAbiFremdsprachen >= 1) {
			ergebnis.log.add(logIndent + "  Eine erste Fremdsprache wurde bereits im Abiturbereich markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereErsteKlassischeNaturwissenschaft());
			return newStates;
		}

		// Bestimme die möglichen Fremdsprachen ...
		final @NotNull List<AbiturFachbelegung> fremdsprachen = new ArrayList<>();
		for (final @NotNull AbiturFachbelegung tmpFremdsprache : manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE))
			if (manager.pruefeBelegung(tmpFremdsprache, GostHalbjahr.getQualifikationsphase()))
				fremdsprachen.add(tmpFremdsprache);
		// ... prüfe die einzelnen Markierungsmöglichkeiten und erzeuge dafür weitere Instanzen des Algorithmus mit getrennten States
		if (fremdsprachen.isEmpty()) {
			ergebnis.log.add(logIndent + "  Konnte keine durchgängig belegte Fremdsprache zur Markierung ermitteln.");
			return newStates;
		}
		for (final @NotNull AbiturFachbelegung fremdsprache : fremdsprachen) {
			final GostFach fach = getFach(fremdsprache);
			if (fach == null)
				continue;
			// Erzeuge einen neuen State, bei welchem die Belegung durchgängig markiert wird
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);

			ergebnis.log.add(logIndent + "  Fallunterscheidung: Markiere die vier Kurse für die Fremdsprache " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereBelegungDurchgaengig(fremdsprache))
				continue;

			newStates.addAll(newState.markiereErsteKlassischeNaturwissenschaft());
		}
		return newStates;
	}


	/**
	 * Markierungsbaum - Ebene 2:
	 * Markiere alle Kurse klassischen Naturwissenschaft, die durchgängig belegt wurde, sofern nicht schon
	 * eine als Abiturfach belegt wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 3. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private @NotNull List<GostAbiturMarkierungsalgorithmus> markiereErsteKlassischeNaturwissenschaft() {
		ergebnis.log.add(logIndent + "Markierung der ersten klassischen Naturwissenschaft:");
		final @NotNull List<GostAbiturMarkierungsalgorithmus> newStates = new ArrayList<>();

		// Prüfe zunächst, ob die Verpflichtung für eine klassische Naturwissenschaft nicht bereits durch die Abiturfächer erfüllt wurde
		final Integer anzahlNW = anzahlBelegungen.computeIfAbsent(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, k -> 0);
		if ((anzahlNW != null) && (anzahlNW > 0)) {
			ergebnis.log.add(logIndent + "  Eine klassische Naturwissenschaft wurde bereits im Abiturbereich markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereZweitesSchwerpunktfachQ2());
			return newStates;
		}

		// Bestimme die möglichen klassischen Naturwissenschaften ...
		final @NotNull List<AbiturFachbelegung> belegungen = new ArrayList<>();
		final @NotNull Set<String> bereitsGeprueft = new HashSet<>(); // Wird ggf. bei bilingualen Sachfächern benötigt, um doppelte Erkennung zu vermeiden
		for (final @NotNull AbiturFachbelegung belegung : manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH)) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			if (bereitsGeprueft.contains(fach.kuerzel))
				continue;
			final @NotNull List<AbiturFachbelegung> tmpBelegung = new ArrayList<>(); // Wird ggf. benötigt, um die Abwahl eines bilingualen Faches zu erkennen
			tmpBelegung.add(belegung);
			if (manager.pruefeBelegungExistiert(tmpBelegung, GostHalbjahr.getQualifikationsphase()))
				belegungen.add(belegung);
			bereitsGeprueft.add(fach.kuerzel);
		}
		// ... prüfe die einzelnen Markierungsmöglichkeiten und erzeuge dafür weitere Instanzen des Algorithmus mit getrennten States
		if (belegungen.isEmpty()) {
			ergebnis.log.add(logIndent + "  Konnte keine durchgängig belegte klassische Naturwissenschaft zur Markierung ermitteln.");
			return newStates;
		}
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			// Erzeuge einen neuen State, bei welchem die Belegung durchgängig markiert wird
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);

			ergebnis.log.add(logIndent + "  Fallunterscheidung: Markiere die vier Kurse für die klassische Naturwissenschaft " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereBelegungDurchgaengig(belegung))
				continue;

			newStates.addAll(newState.markiereZweitesSchwerpunktfachQ2());
		}
		return newStates;
	}


	/**
	 * Markierungsbaum - Ebene 3:
	 * Markiere je nach Schwerpunkt die beiden Kurse aus der Q2 des weiteren Schwerpunktfaches. Im Falle einer
	 * Fremdsprache muss die Belegung schriftlich sein.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 4. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private @NotNull List<GostAbiturMarkierungsalgorithmus> markiereZweitesSchwerpunktfachQ2() {
		ergebnis.log.add(logIndent + "Markierung der zweiten Schwerpunktfaches (Fremdsprache oder Naturwissenschaft in der Q2):");
		final @NotNull List<GostAbiturMarkierungsalgorithmus> newStates = new ArrayList<>();

		// Prüfe, ob im Abiturbereich bereits zwei fremdsprachen markiert wurden.
		if (anzahlAbiFremdsprachen >= 2) {
			ergebnis.log.add(logIndent + "  Eine zweite Fremdsprache wurde bereits im Abiturbereich markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereKunstMusikOderErsatz());
			return newStates;
		}

		// Prüfe, ob bereits zwei Naturwissenschaften markiert wurden - z.B. wenn eine nicht-klassische Naturwissenschaft im Abiturbereich gewählt wurde
		final Integer anzahlNW = this.anzahlBelegungen.computeIfAbsent(GostFachbereich.NATURWISSENSCHAFTLICH, k -> 0);
		if ((anzahlNW != null) && (anzahlNW >= 8)) {
			ergebnis.log.add(logIndent + "  Es wurden bereits zwei Naturwissenschaften markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereKunstMusikOderErsatz());
			return newStates;
		}

		final boolean hatSchwerpunktFremdsprachen = 2 <= manager.zaehleBelegungInHalbjahren(manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE), GostHalbjahr.Q22);
		final boolean hatSchwerpunktNaturwissenschaften = 2 <= manager.zaehleBelegungInHalbjahren(manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH), GostHalbjahr.Q22);
		// Bestimme alle Fachbelegungen, die noch möglich sind: Fremdsprachen, Naturwissenschaften und bilinguale Sachfächer
		final @NotNull Set<AbiturFachbelegung> belegungen = new HashSet<>();
		if (hatSchwerpunktFremdsprachen) {
			for (final @NotNull AbiturFachbelegung belegung : manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE))
				if ((manager.pruefeBelegungMitSchriftlichkeit(belegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
						&& (manager.pruefeBelegung(belegung, GostHalbjahr.Q22))
						&& (!markiert.contains(belegung.fachID, GostHalbjahr.Q21.id) && !markiert.contains(belegung.fachID, GostHalbjahr.Q22.id)))
					belegungen.add(belegung);
			for (final @NotNull AbiturFachbelegung belegung : manager.getFachbelegungenBilingual())
				if ((manager.pruefeBelegungMitSchriftlichkeit(belegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
						&& (manager.pruefeBelegung(belegung, GostHalbjahr.Q22))
						&& (!markiert.contains(belegung.fachID, GostHalbjahr.Q21.id) && !markiert.contains(belegung.fachID, GostHalbjahr.Q22.id)))
					belegungen.add(belegung);
		}
		if (hatSchwerpunktNaturwissenschaften)
			for (final @NotNull AbiturFachbelegung belegung : manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH))
				if (manager.pruefeBelegung(belegung, GostHalbjahr.getQualifikationsphase())
						&& (!markiert.contains(belegung.fachID, GostHalbjahr.Q21.id) && !markiert.contains(belegung.fachID, GostHalbjahr.Q22.id)))
					belegungen.add(belegung);

		// ... prüfe die einzelnen Markierungsmöglichkeiten und erzeuge dafür weitere Instanzen des Algorithmus mit getrennten States
		if (belegungen.isEmpty()) {
			ergebnis.log.add(logIndent + "  Konnte kein durchgängig belegtes Schwerpunktfach zur Markierung ermitteln.");
			return newStates;
		}
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			// Erzeuge einen neuen State, bei welchem die Belegung durchgängig markiert wird
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);

			ergebnis.log.add(logIndent + "  Fallunterscheidung: Markiere die beiden Kurse in der Q2 für das Schwerpunktfach " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q21))
				continue;
			if (!newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q22))
				continue;

			newStates.addAll(newState.markiereKunstMusikOderErsatz());
		}
		return newStates;
	}


	/**
	 * Markierungsbaum - Ebene 4:
	 * Markiere Kunst, Musik oder ein Ersatzfach, wenn nicht bereits Kunst oder Musik im Abiturbereich
	 * markiert wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 5. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private @NotNull List<GostAbiturMarkierungsalgorithmus> markiereKunstMusikOderErsatz() {
		ergebnis.log.add(logIndent + "Markierung von Kunst, Musik oder einem Ersatzfach:");
		final @NotNull List<GostAbiturMarkierungsalgorithmus> newStates = new ArrayList<>();

		// Prüfe, ob Kunst oder Musik im Abiturbereich bereits markiert wurde.
		if (hatAbiKunstOderMusik) {
			ergebnis.log.add(logIndent + "  Kunst oder Musik wurde bereits im Abiturbereich markiert.");
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);
			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.GESCHICHTE))
				return newStates;
			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.SOZIALWISSENSCHAFTEN))
				return newStates;
			newStates.addAll(newState.markiereEinsprachlerNeuEinsetzendeFremdsprache());
			return newStates;
		}

		// Bestimme die möglichen Fachbelegungen (mindestens zwei Belegungen in der QPhase - keine Null-Punkte-Belegung zählen!)
		final @NotNull List<AbiturFachbelegung> belegungen = new ArrayList<>();
		for (final @NotNull AbiturFachbelegung belegung : manager.getRelevanteFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH))
			if (manager.zaehleHalbjahresbelegungen(belegung, GostHalbjahr.Q11, GostHalbjahr.Q12) >= 2)
				belegungen.add(belegung);
		// ... prüfe die einzelnen Markierungsmöglichkeiten und erzeuge dafür weitere Instanzen des Algorithmus mit getrennten States
		if (belegungen.isEmpty()) {
			ergebnis.log.add(logIndent + "  Keine Fachbelegung von Kunst, Musik oder einem Ersatzfach in zwei Halbjahren vorhanden.");
			return newStates;
		}

		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			// Bestimme die beiden besten Halbjahresbewertungen
			final @NotNull GostHalbjahr[] hj = getZweiBesteBelegungen(belegung);
			if ((hj[0] == null) || (hj[1] == null)) {
				ergebnis.log.add(logIndent + "  Fehler: Konnte keine Bewertung für zwei Halbjahre bestimmen.");
				continue;
			}
			// Erzeuge einen neuen State, bei welchem die beiden besten Halbjahresmarkierungen markiert sind
			final int np1 = getNotenpunkte(belegung, hj[0]);
			final int np2 = getNotenpunkte(belegung, hj[1]);
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);
			ergebnis.log.add(logIndent + "  Fallunterscheidung: Markiere die beiden Kurse in "
					+ hj[0].kuerzel + " (" + np1 + " Punkte) und " + hj[1].kuerzel + " (" + np2 + " Punkte) für das " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereHalbjahresbelegung(belegung, hj[0]))
				continue;
			if (!newState.markiereHalbjahresbelegung(belegung, hj[1]))
				continue;

			if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
				newState.restErlaubtVPIP = 0;
				newState.restErlaubtKUMUErsatz = 0;
			}
			final @NotNull Fach f = Fach.getBySchluesselOrDefault(fach.kuerzel);
			if ((f == Fach.MU) || (f == Fach.IN) || (f == Fach.VO))
				newState.restErlaubtMusik = 3;

			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.GESCHICHTE))
				continue;

			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.SOZIALWISSENSCHAFTEN))
				continue;

			newStates.addAll(newState.markiereEinsprachlerNeuEinsetzendeFremdsprache());
		}
		return newStates;
	}


	/**
	 * Markierungsbaum - Ebene 5:
	 * Markiere die beiden besten Geschichts- bzw. Sozialwissenschaftskurse,
	 * wenn dieses nicht als bereits im Abiturbereich markiert wurde.
	 *
	 * @param fb   der Fachbereich Geschichte oder Sozialwissenschaften
	 *
	 * @return true, wenn der Markierungsvorgang erfolgreich war
	 */
	private boolean markiereGeschichteBzwSozialwissenschaften(final @NotNull GostFachbereich fb) {
		final @NotNull String strFB = (fb == GostFachbereich.GESCHICHTE) ? "Geschichte" : "Sozialwissenschaften";
		ergebnis.log.add(logIndent + "Markierung zwei Kursen in " + strFB + ":");

		// Prüfe, ob Gesichte/Sozialwissenschaften bereits im Abiturbereich markiert wurde.
		final Integer anzahlGE = anzahlBelegungen.get(fb);
		if ((anzahlGE != null) && (anzahlGE > 0)) {
			ergebnis.log.add(logIndent + "  " + strFB + " wurde bereits im Abiturbereich markiert.");
			return true;
		}

		// Bestimme die möglichen Fachbelegungen (mindestens zwei Belegungen in der QPhase - keine Null-Punkte-Belegung zählen!)
		// Im Falle von bilingualem Sachunterricht kann hier ggf. ein Wechsel vorkommen, so dass mehrere Fachbelegungen geprüft werden müssen
		return this.markiereZweiBeste(manager.getRelevanteFachbelegungen(fb));
	}



	/**
	 * Markierungsbaum - Ebene 5:
	 * Markiere bei Einsprachlern die beiden Halbjahre in der Q2 einer neu einsetzenden Fremdsprache, falls
	 * diese Bedingungn nicht bereits zuvor erfüllt wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 6. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private @NotNull List<GostAbiturMarkierungsalgorithmus> markiereEinsprachlerNeuEinsetzendeFremdsprache() {
		ergebnis.log.add(logIndent + "Markierung einer neu einsetzenden Fremdsprache in der Q2 bei Einsprachlern...");
		final @NotNull List<GostAbiturMarkierungsalgorithmus> newStates = new ArrayList<>();

		// Prüfe, ob es sich um einen Einsprachler handelt oder nicht
		final boolean istEinsprachler = (SprachendatenUtils.getZweiteSpracheInSekI(manager.getSprachendaten()) == null);
		if (!istEinsprachler)
			ergebnis.log.add(logIndent + "  Es wurde in der Sek I mehr als eine Fremdsprache belegt.");

		// Prüfe, on ein Einsprachler bereits die Q2-Halbjahre einer durchgehend belegten neu einsetzenen Fremdsprache belegt hat
		// Bestimme dabei auch mögliche Belegungen einer neu einsetzenden Fremdsprache
		final @NotNull List<AbiturFachbelegung> belegungen = new ArrayList<>();
		boolean hatEinsprachlerBereitsNeueinsetzendeFremdsprache = false;
		if (istEinsprachler) {
			for (final @NotNull AbiturFachbelegung belegung : manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE)) {
				if (belegung.istFSNeu && manager.pruefeBelegung(belegung, GostHalbjahr.getQualifikationsphase())) {
					belegungen.add(belegung);
					if ((markiert.getOrNull(belegung.fachID, GostHalbjahr.Q21.id) != null)
							&& (markiert.getOrNull(belegung.fachID, GostHalbjahr.Q22.id) != null))
						hatEinsprachlerBereitsNeueinsetzendeFremdsprache = true;
				}
			}
		}

		if (!istEinsprachler && hatEinsprachlerBereitsNeueinsetzendeFremdsprache)
			ergebnis.log.add(logIndent + "  Es ist bereits eine neu einsetzende Fremdsprache in der Q2 markiert.");

		if (!istEinsprachler || hatEinsprachlerBereitsNeueinsetzendeFremdsprache) {
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);
			newStates.addAll(newState.markiereReligionOderPhilosophieUndEineGesellschaftswissenschaft());
			return newStates;
		}

		// Prüfe die einzelnen Markierungsmöglichkeiten und erzeuge dafür weitere Instanzen des Algorithmus mit getrennten States
		if (belegungen.isEmpty()) {
			ergebnis.log.add(
					logIndent + "  Keine Fachbelegungen einer durchgängig belegten neu einsetzenden Fremdsprache in den zwei Halbjahren der Q2 vorhanden.");
			return newStates;
		}
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			// Erzeuge einen neuen State, bei welchem die beiden besten Halbjahresmarkierungen markiert sind
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);
			ergebnis.log.add(logIndent + "  Fallunterscheidung: Markiere die beiden Kurse in der Q2 für das " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q21))
				continue;
			if (!newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q22))
				continue;

			newStates.addAll(newState.markiereReligionOderPhilosophieUndEineGesellschaftswissenschaft());
		}
		return newStates;
	}


	/**
	 * Markierungsbaum - Ebene 6:
	 * Markiere die beiden besten Religions- oder Philosophiekurse, sofern
	 * diese nicht bereits im Abiturbereich markiert wurden.
	 * Markiere außerdem eine Gesellschaftswissenschaft, sofern diese nicht bereits im Abitur markiert wurde
	 * TODO: Führe die Markierungen aus Ebene 7 aus
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private @NotNull List<GostAbiturMarkierungsalgorithmus> markiereReligionOderPhilosophieUndEineGesellschaftswissenschaft() {
		ergebnis.log.add(logIndent + "Markierung zwei Religions- oder Philosophiekurse (oder ggf. einer Gesellschaftswissenschaft als Ersatz):");
		final @NotNull List<GostAbiturMarkierungsalgorithmus> newStates = new ArrayList<>();

		// Prüfe, ob Philosophie bereits im Abiturbereich markiert wurde.
		final Integer anzahlPL = anzahlBelegungen.get(GostFachbereich.PHILOSOPHIE);
		final boolean hatAbiPL = (anzahlPL != null) && (anzahlPL > 0);
		// Prüfe, ob Religion bereits im Abiturbereich markiert wurde.
		final Integer anzahlRE = anzahlBelegungen.get(GostFachbereich.RELIGION);
		final boolean hatAbiRE = (anzahlRE != null) && (anzahlRE > 0);
		boolean hatReBelegungErfuellt = false;
		if (hatAbiRE) {
			ergebnis.log.add(logIndent + "  Es wurde bereits im Abiturbereich bereits ein Religionsfach markiert.");
			hatReBelegungErfuellt = true;
		} else {
			if (hatAbiPL)
				ergebnis.log
						.add(logIndent + "  Philosophie wurde im Abiturbereich gewählt und kann daher nicht als Ersatz für ein Religionsfach genutzt werden.");

			// Wenn weder ein Religionsfach noch Philosophie im Abitur belegt wurde, dann können die beiden besten Halbjahrebelegungen gewählt werden
			if (!hatAbiPL) { // && !hatAbiRE
				if (!this.markiereZweiBeste(manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION, GostFachbereich.PHILOSOPHIE)))
					return newStates;
				hatReBelegungErfuellt = true;
			}

			// Wenn Philosophie wurde als Abiturfach gewählt wurde, dann kann es daher nicht als Ersatzfach gewählt werden...
			if (hatAbiPL) {
				final @NotNull List<AbiturFachbelegung> belegungen = manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION);
				final boolean hatReligionsbelegungen = manager.pruefeBelegungExistiert(belegungen, GostHalbjahr.Q11, GostHalbjahr.Q12);
				if (hatReligionsbelegungen) {
					if (!this.markiereZweiBeste(belegungen))
						return newStates;
					hatReBelegungErfuellt = true;
				}
			}
		}

		// Es wurde bereits eine Gesellschaftswissenschaft im Abitur markiert
		if (hatAbiGesellschaftswissenschaft) {
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);
			// Wenn bereits PL Abiturfach ist, dann kann die zweite Gesellschaftswissenschaft als Religionsersatz dienen
			if ((!hatReBelegungErfuellt) && (!hatAbiPL))
				newState.markiereReligionOderErsatzAusGesellschaftswissenschaften();
			newStates.addAll(newState.markiereProjektkurs());
			return newStates;
		}

		// Es muss noch eine Gesellschaftswissenschaft markiert werden (z.B. Religion als Abiturfach)
		final @NotNull List<AbiturFachbelegung> belegungen = new ArrayList<>();
		final @NotNull Set<String> bereitsGeprueft = new HashSet<>(); // Wird ggf. bei bilingualen Sachfächern benötigt, um doppelte Erkennung zu vermeiden
		for (final @NotNull AbiturFachbelegung belegung : manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH)) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			if (bereitsGeprueft.contains(fach.kuerzel))
				continue;
			final @NotNull List<AbiturFachbelegung> tmpBelegung = new ArrayList<>(); // Wird ggf. benötigt, um die Abwahl eines bilingualen Faches zu erkennen
			tmpBelegung.add(belegung);
			if (manager.pruefeBelegungExistiert(tmpBelegung, GostHalbjahr.getQualifikationsphase()))
				belegungen.add(belegung);
			bereitsGeprueft.add(fach.kuerzel);
		}
		// ... prüfe die einzelnen Markierungsmöglichkeiten und erzeuge dafür weitere Instanzen des Algorithmus mit getrennten States
		if (belegungen.isEmpty()) {
			ergebnis.log.add(logIndent + "  Konnte keine durchgängig belegte Gesellschaftswissenschaft zur Markierung ermitteln.");
			return newStates;
		}
		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			// Erzeuge einen neuen State, bei welchem die Belegung durchgängig markiert wird
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);

			ergebnis.log.add(logIndent + "  Fallunterscheidung: Markiere die vier Kurse für die Gesellschaftswissenschaft " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereBelegungDurchgaengig(belegung))
				continue;

			if (!hatReBelegungErfuellt)
				newState.markiereReligionOderErsatzAusGesellschaftswissenschaften();
			newStates.addAll(newState.markiereProjektkurs());
		}
		return newStates;
	}


	private boolean markiereReligionOderErsatzAusGesellschaftswissenschaften() {
		ergebnis.log.add(
				logIndent + "    Ergänze Markierungen im Bereich Gesellschaftswissenschaften und Religion (Philosophie im Abitur und Relgion nicht in Q1).");
		// Bestimme die zwei besten Noten aus den noch nicht markierten Kursen des Fachbereichs Gesellschaftswissenthschaftclich mit Religion
		final @NotNull List<AbiturFachbelegung> belegungen = manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION);
		if (belegungen.isEmpty()) {
			ergebnis.log.add(logIndent + "    Keine Fachbelegungen gefunden.");
			return false;
		}

		final AbiturFachbelegung[] resBelegung = new AbiturFachbelegung[2];
		final GostFach[] resFach = new GostFach[2];
		final GostHalbjahr[] resHalbjahre = new GostHalbjahr[2];
		final int[] resNotenpunkte = new int[2];

		for (final @NotNull AbiturFachbelegung belegung : belegungen) {
			final GostFach fach = getFach(belegung);
			if (fach == null)
				continue;
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
				if (markiert.getOrNull(belegung.fachID, hj.id) != null)
					continue;
				if (!manager.pruefeBelegung(belegung, hj))
					continue;
				final int np = getNotenpunkte(belegung, hj);
				if ((resBelegung[0] == null) || (np > resNotenpunkte[0])) {
					resBelegung[1] = resBelegung[0];
					resFach[1] = resFach[0];
					resHalbjahre[1] = resHalbjahre[0];
					resNotenpunkte[1] = resNotenpunkte[0];
					resBelegung[0] = belegung;
					resFach[0] = fach;
					resHalbjahre[0] = hj;
					resNotenpunkte[0] = np;
				} else if ((resBelegung[1] == null) || (np > resNotenpunkte[1])) {
					resBelegung[1] = belegung;
					resFach[1] = fach;
					resHalbjahre[1] = hj;
					resNotenpunkte[1] = np;
				}
			}
		}
		if ((resBelegung[0] == null) || (resBelegung[1] == null) || (resHalbjahre[0] == null) || (resHalbjahre[1] == null)
				|| (resFach[0] == null) || (resFach[1] == null)) {
			ergebnis.log.add(logIndent + "  Fehler: Konnte keine Bewertung für zwei Halbjahre bestimmen.");
			return false;
		}
		// Erzeuge einen neuen State, bei welchem die beiden besten Halbjahresmarkierungen markiert sind
		ergebnis.log.add(logIndent + "  Markiere die beiden Kurse in "
				+ resHalbjahre[0].kuerzel + " (" + resNotenpunkte[0] + " Punkte) für " + resFach[0].kuerzelAnzeige + " und "
				+ resHalbjahre[1].kuerzel + " (" + resNotenpunkte[1] + " Punkte) für " + resFach[1].kuerzelAnzeige + "...");

		return markiereHalbjahresbelegung(resBelegung[0], resHalbjahre[0]) && markiereHalbjahresbelegung(resBelegung[1], resHalbjahre[1]);
	}


	/**
	 * Markierungsbaum - Ebene 7:
	 * Berücksichtige die beiden Fälle, dass ein Projektkurs markiert werden kann oder nicht,
	 * sofern einer belegt wurde.
	 * TODO: Führe die Markierungen aus Ebene 8 aus
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private @NotNull List<GostAbiturMarkierungsalgorithmus> markiereProjektkurs() {
		ergebnis.log.add(logIndent + "Markierung eines Projektkurses, sofern einer belegt wurde:");
		final @NotNull List<GostAbiturMarkierungsalgorithmus> newStates = new ArrayList<>();

		// Projektkurs ermitteln (Projektkurs, ProjBll)
		final AbiturFachbelegung projektkurs = belegpruefungProjektkurse.getProjektkurs();
		final GostFach fach = (projektkurs == null) ? null : getFach(projektkurs);
		if (fach != null)
			ergebnis.log.add(logIndent + "  Es wurde ein Projektkurs belegt: " + fach.kuerzelAnzeige);
		final @NotNull List<GostHalbjahr> pjkHalbjahre = belegpruefungProjektkurse.getAnrechenbareHalbjahre();
		final boolean pjkAnrechenbar = (projektkurs != null) && (!manager.istProjektKursBesondereLernleistung()) && (pjkHalbjahre.size() == 2);
		if (pjkAnrechenbar)
			ergebnis.log.add(logIndent + "  und dieser ist anrechenbar.");
		final boolean pjkIstBll = (projektkurs != null) && (manager.istProjektKursBesondereLernleistung());
		if (pjkIstBll)
			ergebnis.log.add(logIndent + "  und dieser wird für eine besondere Lernleistung verwendet.");

		// Fall 1: Belege den Projektkurs
		if (pjkAnrechenbar && (projektkurs != null)) {
			ergebnis.log.add(logIndent + "  Fallunterscheidung: Markiere die beiden Projektkursbelegung...");
			final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);
			final @NotNull GostHalbjahr hj1 = pjkHalbjahre.get(0);
			final @NotNull GostHalbjahr hj2 = pjkHalbjahre.get(1);
			final AbiturFachbelegungHalbjahr bel1 = projektkurs.belegungen[hj1.id];
			if (bel1 == null) {
				ergebnis.log.add(logIndent + "  Die Belegung im Halbjahr " + hj1.kuerzel + " ist fehlerhaft.");
				return newStates;
			}
			final AbiturFachbelegungHalbjahr bel2 = projektkurs.belegungen[hj2.id];
			if (bel2 == null) {
				ergebnis.log.add(logIndent + "  Die Belegung im Halbjahr " + hj2.kuerzel + " ist fehlerhaft.");
				return newStates;
			}
			final Note n2 = (bel2.notenkuerzel == null) ? null : Note.fromKuerzel(bel2.notenkuerzel);
			final boolean hatNote2 = (n2 != null) && n2.istNote(manager.getSchuljahr());
			if (!hatNote2) {
				ergebnis.log.add(logIndent + "  Die Belegungen in dem Halbjahr " + hj2.kuerzel
						+ " hat keine Note gesetzt. Diese muss als Jahresabschluss die Jahresnote des Projektkurses beinhalten.");
				return newStates;
			}
			// Workaround: Setze die Jahresbewertung auch bei dem ersten Halbjahr, so dass die normale Markierung-Funktion genutzt werden kann
			bel1.notenkuerzel = bel2.notenkuerzel;

			// Markiere die beiden Halbjahre
			if (!newState.markiereHalbjahresbelegung(projektkurs, hj1) || !newState.markiereHalbjahresbelegung(projektkurs, hj2))
				return newStates;

			if (newState.markiereWeitereKurse())
				newStates.add(newState);
		}

		// Fall 2: Belege keinen Projektkurs
		ergebnis.log.add(logIndent + "  " + (pjkAnrechenbar ? "Fallunterscheidung:" : "") + "Markiere keine Projektkursbelegung");
		final @NotNull GostAbiturMarkierungsalgorithmus newState = new GostAbiturMarkierungsalgorithmus(this);
		if (newState.markiereWeitereKurse())
			newStates.add(newState);
		return newStates;
	}


	/**
	 * Markierungsbaum - Ebene 7:
	 * Dieser Knoten stellt das Blatt im Baum dar.
	 * Markiert weitere Kurse, die noch nicht markiert sind und anrechenbar sind.
	 * - Dabei werden keine Projektkursbelegungen berücksichtigt, da sie auf einer höheren
	 * Ebene bereits beachtet wurden.
	 * - Bei Musik oder KU-MU-Ersatzfächern werden nur Kurse bis zur erlaubten Zahl berücksichtigt.
	 * - Bis 35 Kurse wird aufgefüllt
	 * - Danach nur, wenn es aufgrund der Defizitanzahl nötig ist oder den Durchschnitt verbessert
	 *
	 * @return true, wenn genügend Kurse markiert wurden und ansonsten false.
	 */
	private boolean markiereWeitereKurse() {
		ergebnis.log.add(logIndent + "Markierung weiterer Kurse:");
		// Erzeuge eine Liste der noch nicht genutzten Belegungen ohne Projektkursbelegungen
		final @NotNull List<GostAbiturMarkierungsalgorithmusBelegung> auswahlliste = new ArrayList<>();
		for (final @NotNull AbiturFachbelegung belegung : manager.daten().fachbelegungen) {
			// Prüfe das Fach - Projektkurse brauchen hier nicht beachtet zu werden - das passiert bereits früher
			final GostFach fach = getFach(belegung);
			if ((fach == null) || ("PX".equals(fach.kuerzel)))
				continue;
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
				if (markiert.contains(belegung.fachID, hj.id))
					continue;
				if (!manager.pruefeBelegung(belegung, hj))
					continue;
				final AbiturFachbelegungHalbjahr belHj = belegung.belegungen[hj.id];
				if (belHj == null)
					continue;
				final Note note = (belHj.notenkuerzel == null) ? null : Note.fromKuerzel(belHj.notenkuerzel);
				if ((note == null) || !note.istNote(manager.getSchuljahr()))
					continue;
				final NoteKatalogEintrag nke = note.daten(manager.getSchuljahr());
				if ((nke == null) || (nke.notenpunkte == null))
					continue;
				auswahlliste.add(new GostAbiturMarkierungsalgorithmusBelegung(belegung, belHj, nke.notenpunkte));
			}
		}

		// Sortiere die Auswahlliste
		auswahlliste.sort(comparatorBelegungen);

		// Füge die Kurse schrittweise aus der Auswahl hinzu
		while ((summeKurse < 40) && !auswahlliste.isEmpty()) {
			// Nehme den ersten Eintrag aus der Liste und ermittle das zugehörige Fach und Halbjahr
			final @NotNull GostAbiturMarkierungsalgorithmusBelegung eintrag = auswahlliste.removeFirst();
			final GostFach fach = getFach(eintrag.belegung);
			if (fach == null)
				continue;
			final @NotNull Fach f = Fach.getBySchluesselOrDefault(fach.kuerzel);

			// Je nach Fach kann keine Markierung vorgenommen werden...
			if ((((f == Fach.IN) || (f == Fach.VO)) && ((restErlaubtVPIP == 0) || (restErlaubtMusik == 0) || (restErlaubtKUMUErsatz == 0)))
					|| ((f == Fach.LI) && (restErlaubtKUMUErsatz == 0))
					|| ((f == Fach.MU) && (restErlaubtMusik == 0)))
				continue;

			// ... wenn 35 oder mehr Kurse vorhanden sind, dann werden Kurse nur noch verwendet
			// , wenn dadurch der Durchschnitt verbessert wird oder die Anzahl der Defizite sonst zu hoch wäre ...
			final double durchschnittNotepunkte = summeNotenpunkte / (summeKurse + 8.0); // LK-Belegungen doppelt zählen, also + 2*4
			final int defizite = defiziteLK + defiziteGK;
			if ((((summeKurse >= 35) && (summeKurse <= 37) && (defizite <= 7)) || ((summeKurse >= 38) && (defizite <= 8)))
					&& (eintrag.notenpunkte <= durchschnittNotepunkte))
				break;

			// ... eine Markierung sollte vorgenommen werden ...
			if (!markiereHalbjahresbelegung(eintrag.belegung, eintrag.halbjahr)) {
				ergebnis.log.add(logIndent + "  Markierung unerwartet fehlgeschlagen, breche Algorithmus ab.");
				return false;
			}

			// ... je nach Fach muss der Zustand des Algorithmus angepasst werden
			if ((f == Fach.IN) || (f == Fach.VO)) {
				restErlaubtVPIP--;
				restErlaubtMusik--;
				restErlaubtKUMUErsatz--;
			} else if (f == Fach.LI) {
				restErlaubtKUMUErsatz--;
			} else if (f == Fach.MU) {
				restErlaubtMusik--;
			}
		}

		if (summeKurse < 35) {
			ergebnis.log.add(logIndent + "  Es wurden zu wenig Kurse markiert.");
			return false;
		}

		final int defizite = defiziteLK + defiziteGK;
		if (defizite > 8) {
			ergebnis.log.add(logIndent + "  Es liegen mehr als 8 Defizite vor.");
			return false;
		}

		if ((defizite > 7) && (summeKurse < 38)) {
			ergebnis.log.add(logIndent + "  Es liegen mehr als 7 Defizite bei wenige als 38 Kursen vor.");
			return false;
		}

		summeNormiert = (int) Math.round((40.0 * summeNotenpunkte) / (summeKurse + 8.0)); // LK-Belegungen doppelt zählen, also + 2*4
		hatZulassung = (this.summeNormiert >= 200)
				&& (((summeKurse >= 35) && (summeKurse <= 37) && (defizite <= 7)) || ((summeKurse >= 38) && (defizite <= 8)));

		// Setze, dass das Ergebnis erfolgreich berechnet wurde
		this.ergebnis.erfolgreich = true;
		return true;
	}

}
