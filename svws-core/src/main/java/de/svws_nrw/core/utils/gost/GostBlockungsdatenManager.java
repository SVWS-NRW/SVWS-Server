package de.svws_nrw.core.utils.gost;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisListeneintrag;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten}.
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class GostBlockungsdatenManager {

	/** Ein Comparator für Schienen der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungSchiene> compSchiene =
			(final @NotNull GostBlockungSchiene a, final @NotNull GostBlockungSchiene b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für Regeln der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungRegel> compRegel =
			(final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) -> {
		final int result = Integer.compare(a.typ, b.typ);
		if (result != 0)
			return result;
		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die Lehrkräfte eines Kurses */
	private static final @NotNull Comparator<@NotNull GostBlockungKursLehrer> compLehrkraefte =
			(final @NotNull GostBlockungKursLehrer a, final @NotNull GostBlockungKursLehrer b) -> {
		final int result = Integer.compare(a.reihenfolge, b.reihenfolge);
		if (result != 0)
			return result;
		return Long.compare(a.id, b.id);
	};

	/** Die Blockungsdaten, die im Manager vorhanden sind. */
	private final @NotNull GostBlockungsdaten _daten;

	/** Der Fächermanager mit den Fächern der gymnasialen Oberstufe. */
	private final @NotNull GostFaecherManager _faecherManager;

	/** Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungKurs> _mapKurse = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungSchiene> _mapSchienen = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungRegel> _mapRegeln = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Schueler> _map_id_schueler = new HashMap<>();

	/** Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull GostFachwahl>> _map_schuelerID_fachwahlen = new HashMap<>();

	/** Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull GostFachwahl>> _map_fachartID_fachwahlen = new HashMap<>();

	/** Schüler-ID --> Fach-ID --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet. */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull GostFachwahl>> _map_schulerID_fachID_fachwahl = new HashMap<>();

	// /** Schüler-ID --> List<Facharten> */
	// private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull Long>> _map_schulerID_facharten = new
	// HashMap<>();

	/** Ergebnis-ID --> {@link GostBlockungsergebnisListeneintrag} */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisListeneintrag> _mapErgebnis = new HashMap<>();

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_fach_kursart_kursnummer;

	/** Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER). */
	private final @NotNull List<@NotNull GostBlockungKurs> _kurse_sortiert_fach_kursart_kursnummer = new ArrayList<>();

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_kursart_fach_kursnummer;

	/** Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER) */
	private final @NotNull List<@NotNull GostBlockungKurs> _kurse_sortiert_kursart_fach_kursnummer = new ArrayList<>();

	/** Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART) */
	private final @NotNull Comparator<@NotNull GostFachwahl> _compFachwahlen;

	/** Ein Comparator für die {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung. */
	private final @NotNull Comparator<@NotNull GostBlockungsergebnisListeneintrag> _compErgebnisse = new GostBlockungsergebnisComparator();

	/** Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf. */
	private long _maxTimeMillis = 1000;

	/**
	 * Erstellt einen neuen Manager mit leeren Blockungsdaten und einem leeren Fächer-Manager.
	 */
	public GostBlockungsdatenManager() {
		_faecherManager = new GostFaecherManager();
		_daten = new GostBlockungsdaten();
		_daten.gostHalbjahr = GostHalbjahr.EF1.id;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();
	}

	/** Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 *
	 * @param pDaten          die Blockungsdaten
	 * @param pFaecherManager der Fächer-Manager
	 */
	public GostBlockungsdatenManager(final @NotNull GostBlockungsdaten pDaten, final @NotNull GostFaecherManager pFaecherManager) {
		_faecherManager = pFaecherManager;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();

		// Tiefe Kopie (deep copy) der GostBlockungsdaten.
		_daten = new GostBlockungsdaten();
		_daten.id = pDaten.id;
		_daten.name = pDaten.name;
		_daten.abijahrgang = pDaten.abijahrgang;
		_daten.gostHalbjahr = pDaten.gostHalbjahr;
		_daten.istAktiv = pDaten.istAktiv;

		// Kopieren und Mappings aufbauen.
		addSchienListe(pDaten.schienen); // Muss vor den Kursen erzeugt werden.
		addRegelListe(pDaten.regeln);
		addKursListe(pDaten.kurse);
		addSchuelerListe(pDaten.schueler);
		addFachwahlListe(pDaten.fachwahlen);
		addErgebnisListe(pDaten.ergebnisse);
	}

	private @NotNull Comparator<@NotNull GostBlockungKurs> createComparatorKursFachKursartNummer() {
		final @NotNull Comparator<@NotNull GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a,
				final @NotNull GostBlockungKurs b) -> {
			final @NotNull GostFach aFach = _faecherManager.getOrException(a.fach_id);
			final @NotNull GostFach bFach = _faecherManager.getOrException(b.fach_id);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0)
				return cmpFach;

			if (a.kursart < b.kursart)
				return -1;
			if (a.kursart > b.kursart)
				return +1;

			return Integer.compare(a.nummer, b.nummer);
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostBlockungKurs> createComparatorKursKursartFachNummer() {
		final @NotNull Comparator<@NotNull GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a,
				final @NotNull GostBlockungKurs b) -> {
			if (a.kursart < b.kursart)
				return -1;
			if (a.kursart > b.kursart)
				return +1;

			final @NotNull GostFach aFach = _faecherManager.getOrException(a.fach_id);
			final @NotNull GostFach bFach = _faecherManager.getOrException(b.fach_id);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0)
				return cmpFach;

			return Integer.compare(a.nummer, b.nummer);
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostFachwahl> createComparatorFachwahlen() {
		final @NotNull Comparator<@NotNull GostFachwahl> comp = (final @NotNull GostFachwahl a, final @NotNull GostFachwahl b) -> {
			if (a.schuelerID < b.schuelerID)
				return -1;
			if (a.schuelerID > b.schuelerID)
				return +1;

			if (a.kursartID < b.kursartID)
				return -1;
			if (a.kursartID > b.kursartID)
				return +1;

			final @NotNull GostFach aFach = _faecherManager.getOrException(a.fachID);
			final @NotNull GostFach bFach = _faecherManager.getOrException(b.fachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		};
		return comp;
	}

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 */
	public boolean getIstBlockungsVorlage() {
		return _daten.ergebnisse.size() == 1;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 1. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public int getOfBewertung1Wert(final long pErgebnisID) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		int summe = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public double getOfBewertung1Intervall(final long pErgebnisID) throws DeveloperNotificationException {
		final double summe = getOfBewertung1Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 2. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public int getOfBewertung2Wert(final long pErgebnisID) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		int summe = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public double getOfBewertung2Intervall(final long pErgebnisID) throws DeveloperNotificationException {
		final double summe = getOfBewertung2Wert(pErgebnisID);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 3. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public int getOfBewertung3Wert(final long pErgebnisID) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public double getOfBewertung3Intervall(final long pErgebnisID) throws DeveloperNotificationException {
		int wert = getOfBewertung3Wert(pErgebnisID);
		if (wert > 0)
			wert--; // Jede Kursdifferenz wird um 1 reduziert, außer die 0.
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Den Wert des 4. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public int getOfBewertung4Wert(final long pErgebnisID) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param pErgebnisID Die Datenbank-ID des Listeneintrages.
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public double getOfBewertung4Intervall(final long pErgebnisID) throws DeveloperNotificationException {
		final int wert = getOfBewertung4Wert(pErgebnisID);
		return 1 - 1 / (0.25 * wert + 1);
	}

	/** Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 *
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public @NotNull GostFaecherManager faecherManager() {
		return this._faecherManager;
	}

	/** Liefert die Anzahl an Fächern.
	 *
	 * @return Die Anzahl an Fächern.
	 */
	public int getFaecherAnzahl() {
		return _faecherManager.faecher().size();
	}

	/** Liefert die Anzahl verschiedenen Kursarten. Dies passiert indem über alle Fachwahlen summiert wird.
	 *
	 * @return Die Anzahl verschiedenen Kursarten.
	 */
	public int getKursartenAnzahl() {
		final @NotNull HashSet<@NotNull Integer> setKursartenIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 *
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public @NotNull GostBlockungsdaten daten() {
		return this._daten;
	}

	/**
	 * Gibt das Halbjahr der gymnasialen Oberstufe zurück, für welches die Blockung angelegt wurde.
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe
	 */
	public @NotNull GostHalbjahr getHalbjahr() {
		return GostHalbjahr.fromIDorException(_daten.gostHalbjahr);
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @param pHalbjahr das Halbjahr der gymnasialen Oberstufe
	 */
	public void setHalbjahr(final @NotNull GostHalbjahr pHalbjahr) {
		_daten.gostHalbjahr = pHalbjahr.id;
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 *
	 * @return die ID der Blockung
	 */
	public long getID() {
		return _daten.id;
	}

	/**
	 * Setzt die ID der Blockung.
	 *
	 * @param pBlockungsID die ID, welche der Blockung zugewiesen wird.
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig bzw. negativ ist.
	 */
	public void setID(final long pBlockungsID) throws DeveloperNotificationException {
		if (pBlockungsID < 0)
			throw new DeveloperNotificationException("Ungültige Blockungs-ID (" + pBlockungsID + ")!");
		_daten.id = pBlockungsID;
	}

	/**
	 * Gibt den Namen der Blockung zurück.
	 *
	 * @return der Name der Blockung
	 */
	public @NotNull String getName() {
		return _daten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 *
	 * @param pName der Name, welcher der Blockung zugewiesen wird.
	 * @throws UserNotificationException Falls der übergebene String leer ist.
	 */
	public void setName(final @NotNull String pName) throws UserNotificationException {
		if ("".equals(pName))
			throw new UserNotificationException("Ein leerer Name ist für die Blockung nicht zulässig.");
		_daten.name = pName;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden.
	 *
	 * @return Die maximale Blockungszeit in Millisekunden.
	 */
	public long getMaxTimeMillis() {
		return _maxTimeMillis;
	}

	/**
	 * Setzt die maximale Blockungszeit in Millisekunden.
	 *
	 * @param pZeit die maximale Blockungszeit in Millisekunden.
	 */
	public void setMaxTimeMillis(final long pZeit) {
		_maxTimeMillis = pZeit;
	}

	private void addErgebnisOhneSortierung(final @NotNull GostBlockungsergebnisListeneintrag pErgebnis) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		if (pErgebnis.id < 1)
			throw new DeveloperNotificationException("Ergebnis.id = " + pErgebnis.id + " --> zu gering!");
		if (_mapErgebnis.containsKey(pErgebnis.id))
			throw new DeveloperNotificationException("Ergebnis.id =  " + pErgebnis.id + " --> doppelt!");
		if (pErgebnis.blockungID < 1)
			throw new DeveloperNotificationException("Ergebnis.blockungID = " + pErgebnis.blockungID + " --> zu gering!");
		if (GostHalbjahr.fromID(pErgebnis.gostHalbjahr) == null)
			throw new DeveloperNotificationException("Ergebnis.gostHalbjahr = " + pErgebnis.gostHalbjahr + " --> unbekannt!");

		// Hinzufügen des Kurses.
		_daten.ergebnisse.add(pErgebnis);
		_mapErgebnis.put(pErgebnis.id, pErgebnis);
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param pErgebnis Das {@link GostBlockungsergebnisListeneintrag}-Objekt, welches hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls in den Daten des Listeneintrags Inkonsistenzen sind.
	 */
	public void addErgebnis(final @NotNull GostBlockungsergebnisListeneintrag pErgebnis) throws DeveloperNotificationException {
		addErgebnisOhneSortierung(pErgebnis);

		// Liste sortieren
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnisListeneintrag} hinzu.
	 *
	 * @param pErgebnisse Die Menge an Ergebnissen.
	 * @throws DeveloperNotificationException Falls in den Daten der Listeneinträge Inkonsistenzen sind.
	 */
	public void addErgebnisListe(final @NotNull List<@NotNull GostBlockungsergebnisListeneintrag> pErgebnisse) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungsergebnisListeneintrag ergebnis : pErgebnisse)
			addErgebnisOhneSortierung(ergebnis);

		// Liste sortieren
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param pErgebnisID           Die Datenbank-ID des Ergebnisses.
	 * @return                      Liefert einen {@link GostBlockungsergebnisListeneintrag} aus der Liste der Ergebnisse.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public @NotNull GostBlockungsergebnisListeneintrag getErgebnis(final long pErgebnisID) throws DeveloperNotificationException {
		final GostBlockungsergebnisListeneintrag e = _mapErgebnis.get(pErgebnisID);
		if (e == null)
			throw new DeveloperNotificationException("Ergebnis mit ID = " + pErgebnisID + " nicht vorhanden!");
		return e;
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnisListeneintrag} nach ihrer Bewertung.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisListeneintrag> getErgebnisseSortiertNachBewertung() {
		return _daten.ergebnisse;
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param pErgebnisID Die Datenbank-ID des zu entfernenden Ergebnisses.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public void removeErgebnisByID(final long pErgebnisID) throws DeveloperNotificationException {
		// Gibt es das Ergebnis?
		final @NotNull GostBlockungsergebnisListeneintrag e = getErgebnis(pErgebnisID);

		// Entfernen des Ergebnisses. Neusortierung nicht nötig.
		_daten.ergebnisse.remove(e);
		_mapErgebnis.remove(pErgebnisID);
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param pErgebnis Das zu entfernende Ergebnis.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public void removeErgebnis(final @NotNull GostBlockungsergebnisListeneintrag pErgebnis) throws DeveloperNotificationException {
		removeErgebnisByID(pErgebnis.id);
	}

	/**
	 * Aktualisiert die Bewertung im {@link GostBlockungsdatenManager} mit der aus dem {@link GostBlockungsergebnis}. <br>
	 * Wirft eine Exception, falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 *
	 * @param pErgebnis Das Ergebnis mit der neuen Bewertung.
	 * @throws DeveloperNotificationException Falls kein  {@link GostBlockungsergebnisListeneintrag} mit der ID gefunden wurde.
	 */
	public void updateErgebnisBewertung(final @NotNull GostBlockungsergebnis pErgebnis) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		if (pErgebnis.id < 0)
			throw new DeveloperNotificationException("GostBlockungsergebnis.id=" + pErgebnis.id + " zu klein!");
		if (pErgebnis.blockungID < 0)
			throw new DeveloperNotificationException("GostBlockungsergebnis.blockungID=" + pErgebnis.blockungID + " zu klein!");

		// Bewertung aktualisieren.
		for (final @NotNull GostBlockungsergebnisListeneintrag eintrag : _daten.ergebnisse)
			if (eintrag.id == pErgebnis.id)
				eintrag.bewertung = pErgebnis.bewertung;

		// Ergebnisse sortieren.
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	private void addKursOhneSortierung(final @NotNull GostBlockungKurs pKurs) throws DeveloperNotificationException {
		if (pKurs.id < 0)
			throw new DeveloperNotificationException("GostBlockungKurs.id=" + pKurs.id + " zu klein!");
		if (_mapKurse.containsKey(pKurs.id))
			throw new DeveloperNotificationException("GostBlockungKurs.id =  " + pKurs.id + " --> doppelt!");
		if (pKurs.anzahlSchienen < 1)
			throw new DeveloperNotificationException("GostBlockungKurs.anzahlSchienen = " + pKurs.anzahlSchienen + " --> zu gering!");
		final int nSchienen = getSchienenAnzahl();
		if (pKurs.anzahlSchienen > nSchienen)
			throw new DeveloperNotificationException("GostBlockungKurs.anzahlSchienen = " + nSchienen + " --> zu groß!");
		if (pKurs.nummer < 1)
			throw new DeveloperNotificationException("GostBlockungKurs.nummer = " + pKurs.nummer + " --> zu gering!");
		if (_faecherManager.get(pKurs.fach_id) == null)
			throw new DeveloperNotificationException("GostBlockungKurs.fach_id = " + pKurs.fach_id + " --> unbekannt!");
		if (GostKursart.fromIDorNull(pKurs.kursart) == null)
			throw new DeveloperNotificationException("GostBlockungKurs.kursart = " + pKurs.kursart + " --> unbekannt!");
		if (pKurs.wochenstunden < 0)
			throw new DeveloperNotificationException("GostBlockungKurs.wochenstunden = " + pKurs.wochenstunden + " --> zu gering!");

		// Hinzufügen des Kurses.
		_daten.kurse.add(pKurs);
		_mapKurse.put(pKurs.id, pKurs);
		_kurse_sortiert_fach_kursart_kursnummer.add(pKurs);
		_kurse_sortiert_kursart_fach_kursnummer.add(pKurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param pKurs Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls die Daten des Kurses inkonsistent sind.
	 */
	public void addKurs(final @NotNull GostBlockungKurs pKurs) throws DeveloperNotificationException {
		// Hinzufügen des Kurses.
		addKursOhneSortierung(pKurs);

		// Sortieren der Kursmengen.
		_kurse_sortiert_fach_kursart_kursnummer.sort(_compKurs_fach_kursart_kursnummer);
		_kurse_sortiert_kursart_fach_kursnummer.sort(_compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param pKurse Die Menge an Kursen.
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public void addKursListe(final @NotNull List<@NotNull GostBlockungKurs> pKurse) throws DeveloperNotificationException {
		// Hinzufügen der Kurse.
		for (final @NotNull GostBlockungKurs gKurs : pKurse)
			addKursOhneSortierung(gKurs);

		// Sortieren der Kursmengen.
		_kurse_sortiert_fach_kursart_kursnummer.sort(_compKurs_fach_kursart_kursnummer);
		_kurse_sortiert_kursart_fach_kursnummer.sort(_compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Gibt den Kurs der Blockung anhand von dessen ID zurück.
	 *
	 * @param  pKursID die ID des Kurses
	 * @return Das zugehörige {@link GostBlockungKurs} Objekt.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungKurs getKurs(final long pKursID) throws DeveloperNotificationException {
		final GostBlockungKurs kurs = _mapKurse.get(pKursID);
		if (kurs == null)
			throw new DeveloperNotificationException("Kurs mit ID = " + pKursID + " existiert nicht in der Blockung!");
		return kurs;
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public boolean getKursExistiert(final long pKursID) {
		final GostBlockungKurs kurs = _mapKurse.get(pKursID);
		return kurs != null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return Die Anzahl an Kursen.
	 */
	public int getKursAnzahl() {
		return _mapKurse.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 *
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return Den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull String getNameOfKurs(final long pKursID) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = getKurs(pKursID);
		final @NotNull GostFach gFach = _faecherManager.getOrException(kurs.fach_id);
		final @NotNull String sSuffix = kurs.suffix.equals("") ? "" : ("-" + kurs.suffix);
		return gFach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer + sSuffix;
	}

	/**
	 * Liefert den Namen (Fach-Kursart) der Fachwahl.
	 *
	 * @param pFachwahl Das Fachwahl-Objekt.
	 * @return Den Namen (Fach-Kursart) der Fachwahl.
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public @NotNull String getNameOfFachwahl(final @NotNull GostFachwahl pFachwahl) throws DeveloperNotificationException {
		final @NotNull GostFach gFach = _faecherManager.getOrException(pFachwahl.fachID);
		final @NotNull GostKursart gKursart = GostKursart.fromID(pFachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Setzt den Suffix des Kurses.
	 *
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @param  pSuffix  Der neue Suffix des Kurses.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public void setSuffixOfKurs(final long pKursID, final @NotNull String pSuffix) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = getKurs(pKursID);
		kurs.suffix = pSuffix;
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<@NotNull GostBlockungKurs> getKursmengeSortiertNachFachKursartNummer() {
		return _kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<@NotNull GostBlockungKurs> getKursmengeSortiertNachKursartFachNummer() {
		return _kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @return                       TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public boolean removeKursAllowed(final long pKursID) throws DeveloperNotificationException {
		return (getKurs(pKursID) != null) && getIstBlockungsVorlage();
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param pKursID Die Datenbank-ID des zu entfernenden Kurses.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 * @throws UserNotificationException Falls es sich derzeit nicht um die Blockungsvorlage handelt.
	 */
	public void removeKursByID(final long pKursID) throws DeveloperNotificationException, UserNotificationException {
		if (!getIstBlockungsVorlage())
			throw new UserNotificationException("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!");

		// Entfernen des Kurses.
		final @NotNull GostBlockungKurs kurs = this.getKurs(pKursID);
		_kurse_sortiert_fach_kursart_kursnummer.remove(kurs); // Neusortierung nicht nötig.
		_kurse_sortiert_kursart_fach_kursnummer.remove(kurs); // Neusortierung nicht nötig.
		_mapKurse.remove(pKursID);
		_daten.kurse.remove(kurs);
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 *
	 * @param pKurs  Der zu entfernende Kurs.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public void removeKurs(final @NotNull GostBlockungKurs pKurs) throws DeveloperNotificationException {
		removeKursByID(pKurs.id);
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return Alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull List<@NotNull GostBlockungKursLehrer> getOfKursLehrkraefteSortiert(final long pKursID) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = getKurs(pKursID);
		return kurs.lehrer;
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pReihenfolgeNr Die Lehrkraft mit der Nummer, die gesucht wird.
	 * @return Die Lehrkraft des Kurses, welche die angegebene Nummer hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer getOfKursLehrkraftMitNummer(final long pKursID, final int pReihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.reihenfolge == pReihenfolgeNr)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + pKursID + " keine Lehrkraft mit ReihenfolgeNr. " + pReihenfolgeNr + "!");
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pReihenfolgeNr Die Lehrkraft mit der Nummer, die gesucht wird.
	 * @return TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public boolean getOfKursLehrkraftMitNummerExists(final long pKursID, final int pReihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.reihenfolge == pReihenfolgeNr)
				return true;
		return false;
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pLehrkraftID Die Datenbank-ID der gesuchten Lehrkraft.
	 * @return Die Lehrkraft des Kurses, welche die angegebene ID hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer getOfKursLehrkraftMitID(final long pKursID, final int pLehrkraftID) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.id == pLehrkraftID)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + pKursID + " keine Lehrkraft mit ID " + pLehrkraftID);
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pLehrkraftID Die Datenbank-ID der gesuchten Lehrkraft.
	 * @return TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public boolean getOfKursLehrkraftMitIDExists(final long pKursID, final int pLehrkraftID) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : getOfKursLehrkraefteSortiert(pKursID))
			if (lehrkraft.id == pLehrkraftID)
				return true;
		return false;
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pKursLehrer Das {@link GostBlockungKursLehrer}-Objekt.
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es die Lehrkraft oder die ReihenfolgeNr im Kurs bereits gibt.
	 */
	public void patchOfKursAddLehrkraft(final long pKursID, final @NotNull GostBlockungKursLehrer pKursLehrer) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = getKurs(pKursID);
		final @NotNull List<@NotNull GostBlockungKursLehrer> lehrer = kurs.lehrer;
		// Datenkonsistenz überprüfen
		for (final @NotNull GostBlockungKursLehrer lehrkraft : lehrer) {
			if (lehrkraft.id == pKursLehrer.id)
				throw new DeveloperNotificationException("Kurs (" + pKursID + ") hat bereits Lehrkraft (" + pKursLehrer.id + ")!");
			if (lehrkraft.reihenfolge == pKursLehrer.reihenfolge)
				throw new DeveloperNotificationException("Kurs (" + pKursID + ") hat bereits eine Lehrkraft mit ReihenfolgeNr. (" + pKursLehrer.reihenfolge + ")!");
		}
		lehrer.add(pKursLehrer);
		lehrer.sort(compLehrkraefte);
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @param pLehrkraftID Die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt.
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public void patchOfKursRemoveLehrkraft(final long pKursID, final long pLehrkraftID) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = getKurs(pKursID);
		final @NotNull ArrayList<@NotNull GostBlockungKursLehrer> lehrer = kurs.lehrer;
		for (int i = 0; i < lehrer.size(); i++)
			if (lehrer.get(i).id == pLehrkraftID) {
				lehrer.remove(lehrer.get(i));
				return;
			}
		throw new DeveloperNotificationException("Kurs (" + pKursID + ") hat keine Lehrkraft (" + pLehrkraftID + ")!");
	}

	private void addSchieneOhneSortierung(final @NotNull GostBlockungSchiene pSchiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		if (pSchiene.id < 1)
			throw new DeveloperNotificationException("GostBlockungSchiene.id =  " + pSchiene.id + " --> zu klein!");
		if ("".equals(pSchiene.bezeichnung))
			throw new DeveloperNotificationException("GostBlockungSchiene.bezeichnung darf nicht leer sein!");
		if (pSchiene.nummer < 1)
			throw new DeveloperNotificationException("GostBlockungSchiene.nummer =  " + pSchiene.nummer + " --> zu klein!");
		if (pSchiene.wochenstunden < 1)
			throw new DeveloperNotificationException("GostBlockungSchiene.wochenstunden =  " + pSchiene.wochenstunden + " --> zu klein!");
		if (_mapSchienen.containsKey(pSchiene.id))
			throw new DeveloperNotificationException("GostBlockungSchiene " + pSchiene.id + " doppelt!");

		// Hinzufügen der Schiene.
		_mapSchienen.put(pSchiene.id, pSchiene);
		_daten.schienen.add(pSchiene);
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 *
	 * @param pSchiene Die hinzuzufügende Schiene.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void addSchiene(final @NotNull GostBlockungSchiene pSchiene) throws DeveloperNotificationException {
		// Hinzufügen der Schiene.
		addSchieneOhneSortierung(pSchiene);

		// Sortieren der Schienenmenge.
		_daten.schienen.sort(compSchiene);
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 *
	 * @param pSchienen Die Menge an Schienen.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void addSchienListe(final @NotNull List<@NotNull GostBlockungSchiene> pSchienen) throws DeveloperNotificationException {
		// Hinzufügen der Schienen.
		for (final @NotNull GostBlockungSchiene schiene : pSchienen)
			addSchieneOhneSortierung(schiene);

		// Sortieren der Schienenmenge.
		_daten.schienen.sort(compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * @return Das zugehörige {@link GostBlockungSchiene} Objekt.
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungSchiene getSchiene(final long pSchienenID) throws DeveloperNotificationException {
		final GostBlockungSchiene schiene = _mapSchienen.get(pSchienenID);
		if (schiene == null)
			throw new DeveloperNotificationException("Schiene (" + pSchienenID + ") existiert nicht in der Blockung!");
		return schiene;
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param pSchienenID  Die Datenbank-ID der Schiene.
	 * @return TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public boolean getSchieneExistiert(final long pSchienenID) {
		return _mapSchienen.get(pSchienenID) != null;
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public int getSchienenAnzahl() {
		return _mapSchienen.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen.
	 * Das ist die interne Referenz zur Liste der Schienen im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach der Schienen-Nummer.
	 *
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public @NotNull List<@NotNull GostBlockungSchiene> getMengeOfSchienen() {
		return _daten.schienen;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Die Schiene muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public boolean removeSchieneAllowed(final long pSchienenID) throws DeveloperNotificationException {
		return (getSchiene(pSchienenID) != null) && getIstBlockungsVorlage();
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein, sonst Exception.
	 * (2) Die Schienen werden neu nummeriert. <br>
	 * (3) Die Konsistenz der sortierten Schienen muss überprüft werden. <br>
	 * (4) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param pSchienenID Die Datenbank-ID der zu entfernenden Schiene.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void removeSchieneByID(final long pSchienenID) throws DeveloperNotificationException {
		// (1)
		if (!getIstBlockungsVorlage())
			throw new DeveloperNotificationException("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!");
		final @NotNull GostBlockungSchiene schieneR = this.getSchiene(pSchienenID);

		// (2)
		_mapSchienen.remove(pSchienenID);
		_daten.schienen.remove(schieneR);
		for (final @NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.nummer > schieneR.nummer)
				schiene.nummer--;

		// (3)
		for (int index = 0; index < _daten.schienen.size(); index++)
			if (_daten.schienen.get(index).nummer != index + 1)
				throw new DeveloperNotificationException("Schiene am Index " + index + " hat nicht Nr. " + (index + 1) + "!");

		// (4)
		final Iterator<@NotNull GostBlockungRegel> iRegel = _daten.regeln.iterator();
		if (iRegel == null)
			return;
		while (iRegel.hasNext()) {
			final @NotNull GostBlockungRegel r = iRegel.next();
			final long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a == null)
				iRegel.remove();
			else
				for (int i = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}

	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung.
	 *
	 * @param pSchiene Die zu entfernende Schiene.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void removeSchiene(final @NotNull GostBlockungSchiene pSchiene) throws DeveloperNotificationException {
		removeSchieneByID(pSchiene.id);
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param  pHalbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 * @return Die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 */
	public static int getDefaultSchienenAnzahl(final @NotNull GostHalbjahr pHalbjahr) {
		return (pHalbjahr.id < 2) ? 13 : 11;
	}

	private void addRegelOhneSortierung(final @NotNull GostBlockungRegel pRegel) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		if (pRegel.id < 1)
			throw new DeveloperNotificationException("Regel.id =  " + pRegel.id + " --> zu klein!");
		if (_mapRegeln.containsKey(pRegel.id))
			throw new DeveloperNotificationException("Regel.id = " + pRegel.id + " --> doppelt!");
		if (GostKursblockungRegelTyp.fromTyp(pRegel.typ) == GostKursblockungRegelTyp.UNDEFINIERT)
			throw new DeveloperNotificationException("Regel.typ = " + pRegel.typ + " --> unbekannt!");

		// Hinzufügen der Regel.
		_daten.regeln.add(pRegel);
		_mapRegeln.put(pRegel.id, pRegel);
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param pRegel die hinzuzufügende Regel
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public void addRegel(final @NotNull GostBlockungRegel pRegel) throws DeveloperNotificationException {
		// Regel hinzufügen.
		addRegelOhneSortierung(pRegel);

		// Sortieren der Regelmenge.
		_daten.regeln.sort(compRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param pRegeln Die Menge an Regeln.
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void addRegelListe(final @NotNull List<@NotNull GostBlockungRegel> pRegeln) throws DeveloperNotificationException {
		// Regeln hinzufügen.
		for (final @NotNull GostBlockungRegel regel : pRegeln)
			addRegelOhneSortierung(regel);

		// Sortieren der Regelmenge.
		_daten.regeln.sort(compRegel);
	}

	/**
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 *
	 * @param  pRegelID Die Datenbank-ID der Regel.
	 * @return Das {@link GostBlockungRegel} Objekt.
	 * @throws DeveloperNotificationException Falls die Regel nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungRegel getRegel(final long pRegelID) throws DeveloperNotificationException {
		final GostBlockungRegel regel = _mapRegeln.get(pRegelID);
		if (regel == null)
			throw new DeveloperNotificationException("Regel.id (" + pRegelID + ") existiert nicht in der Blockung!");
		return regel;
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param pRegelID Die Datenbank-ID der Regel.
	 * @return TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public boolean getRegelExistiert(final long pRegelID) {
		return _mapRegeln.get(pRegelID) != null;
	}

	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return Die Anzahl an Regeln.
	 */
	public int getRegelAnzahl() {
		return _mapRegeln.size();
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public @NotNull List<@NotNull GostBlockungRegel> getMengeOfRegeln() {
		return _daten.regeln;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist. <br>
	 * Kriterium: Die Regel muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  pRegelID Die Datenbank-ID der Regel.
	 * @return TRUE, falls ein Löschen der Regel erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public boolean removeRegelAllowed(final long pRegelID) throws DeveloperNotificationException {
		return (getRegel(pRegelID) != null) && getIstBlockungsVorlage();
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 * Wirft eine Exception, falls es sich nicht um eine Blockungsvorlage handelt.
	 *
	 * @param pRegelID Die Datenbank-ID der zu entfernenden Regel.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void removeRegelByID(final long pRegelID) throws DeveloperNotificationException, UserNotificationException {
		// Datenkonsistenz überprüfen.
		if (!getIstBlockungsVorlage())
			throw new UserNotificationException("Ein Löschen einer Regel ist nur bei einer Blockungsvorlage erlaubt!");

		// Regel entfernen.
		final @NotNull GostBlockungRegel regel = this.getRegel(pRegelID);
		_mapRegeln.remove(pRegelID);
		_daten.regeln.remove(regel);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param regel die zu entfernende Regel
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void removeRegel(final @NotNull GostBlockungRegel regel)  throws DeveloperNotificationException, UserNotificationException {
		removeRegelByID(regel.id);
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param pSchueler Der Schüler, der hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public void addSchueler(final @NotNull Schueler pSchueler) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		if (pSchueler.id < 1)
			throw new DeveloperNotificationException("Schueler.id =  " + pSchueler.id + " --> zu klein!");
		if (_map_id_schueler.containsKey(pSchueler.id))
			throw new DeveloperNotificationException("Schueler.id =  " + pSchueler.id + " --> doppelt!");
		if (pSchueler.geschlecht < 0)
			throw new DeveloperNotificationException("Schueler.geschlecht =  " + pSchueler.geschlecht + " --> zu klein!");

		// Schüler hinzufügen
		_daten.schueler.add(pSchueler);
		_map_id_schueler.put(pSchueler.id, pSchueler);
		if (!_map_schuelerID_fachwahlen.containsKey(pSchueler.id))
			_map_schuelerID_fachwahlen.put(pSchueler.id, new ArrayList<>());
		if (!_map_schulerID_fachID_fachwahl.containsKey(pSchueler.id))
			_map_schulerID_fachID_fachwahl.put(pSchueler.id, new HashMap<>());
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param pSchueler Die Menge an Schülern.
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public void addSchuelerListe(final @NotNull List<@NotNull Schueler> pSchueler) throws DeveloperNotificationException {
		for (final @NotNull Schueler schueler : pSchueler)
			addSchueler(schueler);
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Wirft eine DeveloperNotificationException, falls die Schüler-ID unbekannt ist.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Das zugehörige {@link Schueler}-Objekt.
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public @NotNull Schueler getSchueler(final long pSchuelerID) throws DeveloperNotificationException {
		final Schueler schueler = _map_id_schueler.get(pSchuelerID);
		if (schueler == null)
			throw new DeveloperNotificationException("Schüler-ID (" + pSchuelerID + ") existiert nicht!");
		return schueler;
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return Die aktuelle Menge aller Schüler.
	 */
	public @NotNull List<@NotNull Schueler> getMengeOfSchueler() {
		return _daten.schueler;
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public int getSchuelerAnzahlMitFachwahlen() {
		final HashSet<@NotNull Long> setSchuelerIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setSchuelerIDs.add(fachwahl.schuelerID);
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 *
	 * @return Die Anzahl an Schülern.
	 */
	public int getSchuelerAnzahl() {
		return _daten.schueler.size();
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostKursart getOfSchuelerOfFachKursart(final long pSchuelerID, final long pFachID) throws DeveloperNotificationException {
		final @NotNull GostFachwahl fachwahl = getOfSchuelerOfFachFachwahl(pSchuelerID, pFachID);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostFachwahl getOfSchuelerOfFachFachwahl(final long pSchuelerID, final long pFachID) throws DeveloperNotificationException {
		final HashMap<@NotNull Long, @NotNull GostFachwahl> mapFachFachwahl = _map_schulerID_fachID_fachwahl.get(pSchuelerID);
		if (mapFachFachwahl == null)
			throw new DeveloperNotificationException("Schüler-ID (" + pSchuelerID + ") unbekannt!");

		final GostFachwahl fachwahl = mapFachFachwahl.get(pFachID);
		if (fachwahl == null)
			throw new DeveloperNotificationException("Schüler-ID (" + pSchuelerID + "), Fach-ID (" + pFachID + ") unbekannt!");

		return fachwahl;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller {@link GostFachwahl} des Schülers.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public @NotNull List<@NotNull GostFachwahl> getOfSchuelerFacharten(final long pSchuelerID) throws DeveloperNotificationException {
		final List<@NotNull GostFachwahl> fachwahlenDesSchuelers = _map_schuelerID_fachwahlen.get(pSchuelerID);
		if (fachwahlenDesSchuelers == null)
			throw new DeveloperNotificationException("Schüler-ID (" + pSchuelerID + ") unbekannt!");
		return fachwahlenDesSchuelers;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *
	 * @param pSchuelerID Die Datenbank.ID des Schülers.
	 * @param pFach Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param pKursart Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public boolean getOfSchuelerHatFachart(final long pSchuelerID, final long pFach, final long pKursart) throws DeveloperNotificationException {
		final HashMap<@NotNull Long, @NotNull GostFachwahl> map = _map_schulerID_fachID_fachwahl.get(pSchuelerID);
		if (map == null)
			throw new DeveloperNotificationException("Schüler-ID (" + pSchuelerID + ") unbekannt!");
		final GostFachwahl wahl = map.get(pFach);
		if (wahl == null)
			return false;
		return wahl.kursartID == pKursart;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 * Wirft eine Exception, falls die Fachwahl-Daten inkonsistent sind.
	 *
	 * @param pFachwahl Die Fachwahl, die hinzugefügt wird.
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void addFachwahl(final @NotNull GostFachwahl pFachwahl) throws DeveloperNotificationException {
		// _map_schulerID_fachID_fachwahl: Teil 1
		HashMap<@NotNull Long, @NotNull GostFachwahl> mapSFA = _map_schulerID_fachID_fachwahl.get(pFachwahl.schuelerID);
		if (mapSFA == null) {
			mapSFA = new HashMap<>();
			_map_schulerID_fachID_fachwahl.put(pFachwahl.schuelerID, mapSFA);
		}

		// _map_schulerID_fachID_fachwahl: Teil 2
		final long fachID = pFachwahl.fachID;
		if (mapSFA.put(fachID, pFachwahl) != null)
			throw new DeveloperNotificationException("Schüler-ID (" + pFachwahl.schuelerID + "), (Fach-ID" + fachID + ") doppelt!");

		// _map_schuelerID_fachwahlen
		List<@NotNull GostFachwahl> fachwahlenDesSchuelers = _map_schuelerID_fachwahlen.get(pFachwahl.schuelerID);
		if (fachwahlenDesSchuelers == null) {
			fachwahlenDesSchuelers = new ArrayList<>();
			_map_schuelerID_fachwahlen.put(pFachwahl.schuelerID, fachwahlenDesSchuelers);
		}
		fachwahlenDesSchuelers.add(pFachwahl);
		fachwahlenDesSchuelers.sort(_compFachwahlen);

		// _map_fachartID_fachwahlen
		final long fachartID = GostKursart.getFachartIDByFachwahl(pFachwahl);
		getOfFachartMengeFachwahlen(fachartID).add(pFachwahl);

		// fachwahlen
		_daten.fachwahlen.add(pFachwahl);
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param pFachwahlen Die Menge an Fachwahlen.
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void addFachwahlListe(final @NotNull List<@NotNull GostFachwahl> pFachwahlen) throws DeveloperNotificationException {
		for (final @NotNull GostFachwahl gFachwahl : pFachwahlen)
			addFachwahl(gFachwahl);
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return Die Anzahl an Fachwahlen.
	 */
	public int getFachwahlAnzahl() {
		return _daten.fachwahlen.size();
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID. <br>
	 * Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen. <br>
	 *
	 * @param pFachartID Die Fachart-ID berechnet aus Fach-ID und Kursart-ID.
	 * @return Die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 */
	public @NotNull List<@NotNull GostFachwahl> getOfFachartMengeFachwahlen(final long pFachartID) {
		List<@NotNull GostFachwahl> fachwahlenDerFachart = _map_fachartID_fachwahlen.get(pFachartID);
		if (fachwahlenDerFachart == null) {
			fachwahlenDerFachart = new ArrayList<>();
			_map_fachartID_fachwahlen.put(pFachartID, fachwahlenDerFachart);
		}
		return fachwahlenDerFachart;
	}

}
