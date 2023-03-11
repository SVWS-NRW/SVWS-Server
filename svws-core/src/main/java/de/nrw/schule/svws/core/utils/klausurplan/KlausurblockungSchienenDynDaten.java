package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.adt.collection.LinkedCollection;
import de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur;
import de.nrw.schule.svws.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/** Eine dynamische Datenstruktur zum Speichern der aktuellen Lage der Klausuren auf ihren Schienen.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenDynDaten {

	private static final int SCHIENEN_MAX_ANZAHL = 1000;

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _mapKlausurZuNummer = new HashMap<>();

	/** Mapping, um eine Sammlung von Long-Werten in laufende Integer-Werte umzuwandeln. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _mapSchuelerZuNummer = new HashMap<>();

	/** Die Anzahl der Klausuren. */
	private final int _klausurenAnzahl;

	/** Jeder Klausurnummer wird eine Schiene zugeordnet. Der Wert -1 definiert eine temporäre Nicht-Zuordnung. Am Ende
	 * des Algorithmus hat jede Klausur einen Wert >= 0. Und die Speicher-Zustände. */
	private final @NotNull int @NotNull [] _klausurZuSchiene;
	private final @NotNull int @NotNull [] _klausurZuSchiene1;
	private final @NotNull int @NotNull [] _klausurZuSchiene2;

	/** Jeder Klausurnummer wird der Knotengrad (Anzahl an Nachbarn) zugeordnet. */
	private final @NotNull int @NotNull [] _klausurnummerZuGrad;

	/** Ein Array aller Klausurnummern, sortiert nach ihrem Knotengrad (Anzahl an Nachbarn). */
	private final @NotNull int @NotNull [] _klausurenSortiertGrad;

	/** Bestimmt, ob ein Klausurnummer-Paar in der selben Schiene verboten ist. */
	private final @NotNull boolean @NotNull [] @NotNull [] _verboten;

	/** Die Anzahl der derzeitigen verwendeten Schienen. Und die Speicher-Zustände. */
	private int _schienenAnzahl = 0;
	private int _schienenAnzahl1 = SCHIENEN_MAX_ANZAHL;
	private int _schienenAnzahl2 = SCHIENEN_MAX_ANZAHL;

	/** Der Konstruktor konvertiert die Eingabedaten der GUI in eine dynamische Datenstruktur als Basis für die
	 * Algorithmen zur schnellen Manipulation.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI). */
	KlausurblockungSchienenDynDaten(@NotNull Random pRandom, @NotNull List<@NotNull GostKursklausur> pInput) {
		// Parameter
		_random = pRandom;

		initialisiereMapSchueler(pInput);
		initialisiereMapKlausuren(pInput);

		_klausurenAnzahl = _mapKlausurZuNummer.size();
		_klausurZuSchiene = new int[_klausurenAnzahl];
		_klausurnummerZuGrad = new int[_klausurenAnzahl];
		_klausurenSortiertGrad = gibErzeugeKlausurenInReihenfolge();
		_klausurZuSchiene1 = new int[_klausurenAnzahl];
		_klausurZuSchiene2 = new int[_klausurenAnzahl];
		_verboten = new boolean[_klausurenAnzahl][_klausurenAnzahl];

		initialisiereMatrixVerboten(pInput);
		initialisiereKnotenGrad();

		aktionKlausurenAusSchienenEntfernen();
	}
	

	private void initialisiereMapSchueler(@NotNull List<@NotNull GostKursklausur> pInput) {
		@NotNull HashSet<@NotNull Long> setSchueler = new HashSet<>();
		for (@NotNull GostKursklausur gostKursklausur : pInput) {
			for (@NotNull Long schuelerID : gostKursklausur.schuelerIds) {
				if (schuelerID < 0) // Check: Ungültige Schüler-ID
					throw new DeveloperNotificationException("Schüler-ID " + schuelerID + " ist negativ!");
				if (setSchueler.add(schuelerID)) {
					int schuelerNummer = _mapSchuelerZuNummer.size(); // Mapping: schuelerID --> laufende Nummer
					_mapSchuelerZuNummer.put(schuelerID, schuelerNummer);
				}
			}
		}
	}

	private void initialisiereMapKlausuren(@NotNull List<@NotNull GostKursklausur> pInput) {
		for (@NotNull GostKursklausur gostKursklausur : pInput) {
			if (gostKursklausur.id < 0) // Check: Ungültige Klausur-ID
				throw new DeveloperNotificationException("Klausur-ID=" + gostKursklausur.id + " ist negativ!");
			int klausurNummer = _mapKlausurZuNummer.size();
			_mapKlausurZuNummer.put(gostKursklausur.id, klausurNummer); // Mapping: datenbankKlausurID --> laufende Nummer
		}
	}
	
	private void initialisiereMatrixVerboten(@NotNull List<@NotNull GostKursklausur> pInput) {
		
		@NotNull HashMap<@NotNull Long, @NotNull LinkedCollection<@NotNull Long>> mapSchuelerKlausuren = new HashMap<>();

		for (@NotNull GostKursklausur gostKursklausur : pInput) {
			for (@NotNull Long schuelerID : gostKursklausur.schuelerIds) {
				// Liste des Schülers holen
				LinkedCollection<@NotNull Long> list = mapSchuelerKlausuren.get(schuelerID);
				if (list == null) {
					list = new LinkedCollection<>();
					mapSchuelerKlausuren.put(schuelerID, list);
				}
				// Liste des Schülers füllen
				list.addLast(gostKursklausur.id);
			}
		}
		
		for (@NotNull Long schuelerID : mapSchuelerKlausuren.keySet()) {
			LinkedCollection<@NotNull Long> list = mapSchuelerKlausuren.get(schuelerID);
			if (list == null)
				throw new DeveloperNotificationException("Die Liste darf nicht NULL sein.");
			for (@NotNull Long klausurID1 : list) {
				for (@NotNull Long klausurID2 : list) {
					Integer klausurNr1 = _mapKlausurZuNummer.get(klausurID1);
					Integer klausurNr2 = _mapKlausurZuNummer.get(klausurID2);
					if (klausurNr1 == null)
						throw new DeveloperNotificationException("NULL-Wert beim Mapping von klausurID1 --> " + klausurID1);
					if (klausurNr2 == null)
						throw new DeveloperNotificationException("NULL-Wert beim Mapping von klausurID2 --> " + klausurID2);
					_verboten[klausurNr1][klausurNr2] = true;
				}
			}
		}

	}

	private void initialisiereKnotenGrad() {

		// Initialisierung von '_klausurnummerZuGrad'.
		for (int klausurNr1 = 0; klausurNr1 < _klausurenAnzahl; klausurNr1++) {
			int kanten = 0;
			for (int klausurNr2 = 0; klausurNr2 < _klausurenAnzahl; klausurNr2++)
				if (_verboten[klausurNr1][klausurNr2])
					kanten++;
			_klausurnummerZuGrad[klausurNr1] = kanten;
		}

		// InsertionSort von '_klausurenSortiertGrad'.
		for (int i = 1; i < _klausurenAnzahl; i++)
			for (int j = i; j >= 1; j--) {
				int nummerR = _klausurenSortiertGrad[j];
				int nummerL = _klausurenSortiertGrad[j - 1];
				if (_klausurnummerZuGrad[nummerL] >= _klausurnummerZuGrad[nummerR])
					break; // Bereits richtig einsortiert.
				_klausurenSortiertGrad[j] = nummerL;
				_klausurenSortiertGrad[j - 1] = nummerR;
			}

	}



	/** Liefert ein Array aller Klausurnummern in aufsteigender Reihenfolge ihrer Nummer.
	 * 
	 * @return ein Array aller Klausurnummern in aufsteigender Reihenfolge ihrer Nummer. */
	private @NotNull int[] gibErzeugeKlausurenInReihenfolge() {
		int[] temp = new int[_klausurenAnzahl];

		for (int i = 0; i < _klausurenAnzahl; i++)
			temp[i] = i;

		return temp;
	}

	/** Liefert ein Ausgabe-Objekt, welches jeder Klausur genau eine Schiene zuordnet.
	 * 
	 * @return ein Ausgabe-Objekt, welches jeder Klausur genau eine Schiene zuordnet. */
	@NotNull List<@NotNull List<@NotNull Long>> gibErzeugeOutput() {
		
		@NotNull List<@NotNull List<@NotNull Long>> out = new Vector<>();
		for (int i = 0; i < _schienenAnzahl; i++) {
			out.add(new Vector<>());
		}
		
	
		for (Entry<Long, Integer> e : _mapKlausurZuNummer.entrySet()) {
			Long klausurID = e.getKey();
			Integer klausurNr = e.getValue();

			if (klausurID == null) 
				throw new DeveloperNotificationException("gibErzeugeOutput(): NULL-Wert bei 'klausurID'!");
			
			if (klausurNr == null) 
				throw new DeveloperNotificationException("gibErzeugeOutput(): NULL-Wert bei 'klausurNr'!");

			int schiene = _klausurZuSchiene[klausurNr];
			if (schiene < 0) 
				throw new DeveloperNotificationException("gibErzeugeOutput(): negativer Wert bei 'schiene'!");
			if (schiene >= _schienenAnzahl) 
				throw new DeveloperNotificationException("gibErzeugeOutput(): zu großer Wert bei 'schiene'!");

			// Klausur-Schienen-Zuordnung
			out.get(schiene).add(klausurID);
		}

		return out;
	}

	/** Liefert ein Array aller Klausurnummern in zufälliger Reihenfolge.
	 * 
	 * @return ein Array aller Klausurnummern in zufälliger Reihenfolge. */
	@NotNull
	int[] gibErzeugeKlausurenInZufaelligerReihenfolge() {
		int[] temp = new int[_klausurenAnzahl];

		for (int i = 0; i < _klausurenAnzahl; i++)
			temp[i] = i;

		for (int i1 = 0; i1 < _klausurenAnzahl; i1++) {
			int i2 = _random.nextInt(_klausurenAnzahl);
			int save1 = temp[i1];
			int save2 = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}

		return temp;
	}

	/** Liefert ein leicht permutiertes Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 * 
	 * @return ein leicht permutiertes Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst. */
	@NotNull
	int[] gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert() {
		int[] temp = new int[_klausurenAnzahl];

		for (int i = 0; i < _klausurenAnzahl; i++)
			temp[i] = _klausurenSortiertGrad[i];

		for (int i1 = 0; i1 < _klausurenAnzahl; i1++) {
			int i2 = _random.nextInt(_klausurenAnzahl);
			if ((i1 - i2) * (i1 - i2) > _klausurenAnzahl)
				continue;
			// Tausche nur dann, wenn "nahe" beieinander.
			int save1 = temp[i1];
			int save2 = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}

		return temp;
	}

	/** Liefert ein Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst.
	 * 
	 * @return ein Array aller Klausurnummern sortiert nach höheren Knotengrad zuerst. */
	@NotNull
	int[] gibErzeugeKlausurenMitHoeheremGradZuerst() {
		int[] temp = new int[_klausurenAnzahl];

		for (int i = 0; i < _klausurenAnzahl; i++)
			temp[i] = _klausurenSortiertGrad[i];

		return temp;
	}

	/** Liefert ein Array aller derzeit verwendeten Schienen in zufälliger Reihenfolge.
	 * 
	 * @return ein Array aller derzeit verwendeten Schienen in zufälliger Reihenfolge. */
	@NotNull
	int[] gibErzeugeSchienenInZufaelligerReihenfolge() {
		int[] temp = new int[_schienenAnzahl];

		for (int i = 0; i < _schienenAnzahl; i++)
			temp[i] = i;

		for (int i1 = 0; i1 < _schienenAnzahl; i1++) {
			int i2 = _random.nextInt(_schienenAnzahl);
			int save1 = temp[i1];
			int save2 = temp[i2];
			temp[i1] = save2;
			temp[i2] = save1;
		}

		return temp;
	}

	/** Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist.
	 * 
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 1 ist. */
	boolean gibIstBesserAlsZustand1() {
		return gibVergleicheMitAktuellemZustand(_schienenAnzahl1, _klausurZuSchiene1);
	}

	/** Liefert TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist.
	 * 
	 * @return TRUE, wenn der aktuelle Zustand besser als der gespeicherte Zustand 2 ist. */
	boolean gibIstBesserAlsZustand2() {
		return gibVergleicheMitAktuellemZustand(_schienenAnzahl2, _klausurZuSchiene2);
	}

	private boolean gibVergleicheMitAktuellemZustand(int schienenAnzahlX, @NotNull int @NotNull [] klausurZuSchieneX) {
		if (_schienenAnzahl < schienenAnzahlX)
			return true;

		if (_schienenAnzahl > schienenAnzahlX)
			return false;

		// Schienenanzahl ist ab hier identisch
		int[] histogramm = new int[_schienenAnzahl];
		int[] histogrammX = new int[_schienenAnzahl];

		for (int i = 0; i < _klausurenAnzahl; i++) {
			histogramm[_klausurZuSchiene[i]]++;
			histogrammX[klausurZuSchieneX[i]]++;
		}

		int minHisto = _klausurenAnzahl;
		int minHistoX = _klausurenAnzahl;

		for (int i = 0; i < _schienenAnzahl; i++) {
			minHisto = Math.min(minHisto, histogramm[i]);
			minHistoX = Math.min(minHistoX, histogrammX[i]);
		}

		return minHisto < minHistoX;
	}

	/** Liefert die Anzahl noch nicht verteilter Klausuren.
	 * 
	 * @return die Anzahl noch nicht verteilter Klausuren. */
	int gibAnzahlNichtverteilterKlausuren() {
		int summe = 0;
		for (int klausurNr = 0; klausurNr < _klausurenAnzahl; klausurNr++)
			if (_klausurZuSchiene[klausurNr] < 0)
				summe++;
		return summe;
	}

	/** Liefert die Anzahl an Klausuren.
	 * 
	 * @return die Anzahl an Klausuren. */
	int gibAnzahlKlausuren() {
		return _klausurenAnzahl;
	}

	/** Liefert TRUE, falls die übergebene Klausurnummer noch nicht verteilt wurde.
	 * 
	 * @param  klausurNr die Klausurnummer, nach der gefragt wird.
	 * @return           TRUE, falls die übergebene Klausurnummer noch nicht verteilt wurde. */
	boolean gibIstKlausurUnverteilt(int klausurNr) {
		return _klausurZuSchiene[klausurNr] < 0;
	}

	/** Liefert die Nummer einer neu erzeugten Schiene.
	 * 
	 * @return die Nummer einer neu erzeugten Schiene. */
	int gibErzeugeNeueSchiene() {
		_schienenAnzahl++;
		return _schienenAnzahl - 1;
	}

	/** Liefert die Anzahl der derzeit verwendeten Schienen.
	 * 
	 * @return die Anzahl der derzeit verwendeten Schienen. */
	int gibAnzahlSchienen() {
		return _schienenAnzahl;
	}

	/** Liefert den freien Knoten (Klausur), der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt. Falls
	 * mehrere Knoten dieses Kriterium erfüllen, wird ein zufälliger ausgewählt.
	 * 
	 * @return den Knoten, der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt. */
	int gibKlausurDieFreiIstMitDenMeistenNachbarsfarben() {
		int maxFarben = -1;
		int maxNr = -1;

		for (int klausurNr : gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (_klausurZuSchiene[klausurNr] >= 0)
				continue; // Überspringe bereits zugeordnete Knoten.

			int farben = gibNachbarsfarbenDerKlausur(klausurNr);
			if (farben < maxFarben)
				continue; // Überspringe schlechteren Knoten.

			maxFarben = farben;
			maxNr = klausurNr;
		}

		return maxNr;
	}

	private int gibNachbarsfarbenDerKlausur(int klausurNr) {
		int summe = 0;
		boolean[] benutzt = new boolean[_schienenAnzahl];

		for (int klausurNr2 = 0; klausurNr2 < _klausurenAnzahl; klausurNr2++) {
			int farbe = _klausurZuSchiene[klausurNr2];
			if ((farbe >= 0) && (_verboten[klausurNr][klausurNr2]))
				if (!benutzt[farbe]) {
					benutzt[farbe] = true;
					summe++;
				}
		}

		return summe;
	}

	/** Liefert den freien Knoten, der die meisten freien Nachbarn hat oder -1 falls es keinen gibt. Falls mehrere
	 * Knoten dieses Kriterium erfüllen, wird ein zufälliger ausgewählt.
	 * 
	 * @return den Knoten, der die meisten Nachbarsfarben hat oder -1 falls es keinen gibt. */
	int gibKlausurDieFreiIstMitDenMeistenFreienNachbarn() {

		int maxNachbarn = -1;
		int maxNr = -1;

		for (int nr : gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (_klausurZuSchiene[nr] >= 0)
				continue; // Überspringe bereits zugeordnete Knoten.

			int nachbarn = gibAnzahlFreierNachbarn(nr);
			if (nachbarn < maxNachbarn)
				continue; // Überspringe schlechteren Knoten.

			maxNachbarn = nachbarn;
			maxNr = nr;
		}

		return maxNr;
	}

	private int gibAnzahlFreierNachbarn(int nr) {
		int summe = 0;

		for (int nr2 = 0; nr2 < _klausurenAnzahl; nr2++)
			if ((_klausurZuSchiene[nr2] >= 0) && (_verboten[nr][nr2]))
				summe++;

		return summe;
	}

	/** Liefert eine freie Klausur, die nicht mit "nr1" benachbart ist, welche aber die meisten freien Nachbarn hat, die
	 * widerum mit "nr1" benachbart sind.
	 * 
	 * @param  setS Die Menge an Nachbarn, die denen der gesuchte Knoten nicht benachbart sein darf.
	 * @return      eine freie Klausur, die nicht mit "nr1" benachbart ist, welche aber die meisten freien Nachbarn hat,
	 *              die widerum mit "nr1" benachbart sind. */
	int gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(
			@NotNull LinkedCollection<@NotNull Integer> setS) {
		int maxNachbarn = -1;
		int maxNr = -1;

		for (int nr2 : gibErzeugeKlausurenInZufaelligerReihenfolge()) {
			if (_klausurZuSchiene[nr2] >= 0)
				continue; // Überspringe, da "nr2" bereits zugeordnet ist.

			if (gibIstBenachbart(nr2, setS))
				continue; // Überspringe, da "nr2" einen Nachbarn in setS hat.

			int nachbarn = gibAnzahlFreierNachbarnVonNr2DieMitDerMengeBenachbartSind(setS, nr2);
			if (nachbarn < maxNachbarn)
				continue; // Überspringe schlechteren Knoten.

			maxNachbarn = nachbarn;
			maxNr = nr2;
		}

		return maxNr;
	}

	private int gibAnzahlFreierNachbarnVonNr2DieMitDerMengeBenachbartSind(
			@NotNull LinkedCollection<@NotNull Integer> setS, int nr2) {
		int summe = 0;

		for (int nr3 = 0; nr3 < _klausurenAnzahl; nr3++)
			if ((_verboten[nr2][nr3]) && (_klausurZuSchiene[nr3] < 0)) // "nr3" ist freier Nachbar von "nr2".
				if (gibIstBenachbart(nr3, setS)) // "nr3" ist Nachbar von einem Knoten in setS
					summe++;

		return summe;
	}

	private boolean gibIstBenachbart(int nr3, @NotNull LinkedCollection<@NotNull Integer> setS) {
		for (Integer nr4 : setS)
			if (_verboten[nr3][nr4.intValue()])
				return true;
		return false;
	}

	/** Speichert die aktuelle Klausur-Schienen-Lage in Zustand 1. */
	void aktionZustand1Speichern() {
		_schienenAnzahl1 = _schienenAnzahl;

		for (int nr = 0; nr < _klausurenAnzahl; nr++)
			_klausurZuSchiene1[nr] = _klausurZuSchiene[nr];
	}

	/** Speichert die aktuelle Klausur-Schienen-Lage in Zustand 2. */
	void aktionZustand2Speichern() {
		_schienenAnzahl2 = _schienenAnzahl;

		for (int nr = 0; nr < _klausurenAnzahl; nr++)
			_klausurZuSchiene2[nr] = _klausurZuSchiene[nr];

		// debug("NEW RECORD!");
	}

	/** Lädt die aktuelle Klausur-Schienen-Lage aus Zustand 1. */
	void aktionZustand1Laden() {
		aktionKlausurenAusSchienenEntfernen();

		_schienenAnzahl = _schienenAnzahl1;

		for (int nr = 0; nr < _klausurenAnzahl; nr++)
			_klausurZuSchiene[nr] = _klausurZuSchiene1[nr];
	}

	/** Lädt die aktuelle Klausur-Schienen-Lage aus Zustand 2. */
	void aktionZustand2Laden() {
		aktionKlausurenAusSchienenEntfernen();

		_schienenAnzahl = _schienenAnzahl2;

		for (int nr = 0; nr < _klausurenAnzahl; nr++)
			_klausurZuSchiene[nr] = _klausurZuSchiene2[nr];
	}

	/** Entfernt alle Klausur-Schienen-Zuordnungen und passt die Datenstrukturen entsprechend an. */
	void aktionKlausurenAusSchienenEntfernen() {
		for (int i = 0; i < _klausurenAnzahl; i++)
			_klausurZuSchiene[i] = -1;

		_schienenAnzahl = 0;
	}

	/** Liefert TRUE, falls die übergebene Klausur in die übergebene Schiene gesetzt werden konnte.
	 * 
	 * @param  nr die Klausur, die in die übergebene Schiene gesetzt werden soll.
	 * @param  s  die Schiene, in der die übergebene Klausur landen soll.
	 * @return    TRUE, falls die übergebene Klausur in die übergebene Schiene gesetzt werden konnte. */
	boolean aktionSetzeKlausurInSchiene(int nr, int s) {

		if (s < 0)
			throw new DeveloperNotificationException("aktionSetzeKlausurInSchiene("+nr+", "+s+") --> Schiene zu klein!");
			
		if (s >= _schienenAnzahl)
			throw new DeveloperNotificationException("aktionSetzeKlausurInSchiene("+nr+", "+s+") --> Schiene zu groß!");

		for (int nr2 = 0; nr2 < _klausurenAnzahl; nr2++)
			if (_klausurZuSchiene[nr2] == s)
				if (_verboten[nr][nr2])
					return false;

		_klausurZuSchiene[nr] = s;

		return true;
	}

	/** Entfernt die übergebene Klausur aus ihrer aktuellen Schiene. Falls die Klausur keiner Schiene zugeordnet war,
	 * wird eine {@link KlausurblockungException} geworfen.
	 * 
	 * @param klausurNr die Nummer der Klausur, die entfernt werden soll. */
	void aktionEntferneKlausurAusSchiene(int klausurNr) {
		if (_klausurZuSchiene[klausurNr] < 0) 
			throw new DeveloperNotificationException("aktionEntferneKlausurAusSchiene("+klausurNr+") --> Die Klausur hatte gar keine Schiene!");
		_klausurZuSchiene[klausurNr] = -1;
	}

	/** Erhöht die Schienenanzahl um 1, setzt die übergebene Klausur in die neue Schiene und liefert die neue
	 * Schienennummer.
	 * 
	 * @param  klausurNr die Klausur, die in eine neue Schiene gesetzt werden soll.
	 * @return           die neue Schiene, in welche die Klausur gesetzt wurde. */
	int aktionSetzeKlausurInNeueSchiene(int klausurNr) {
		int schiene = _schienenAnzahl;
		if (_klausurZuSchiene[klausurNr] >= 0)
			throw new DeveloperNotificationException("aktionSetzeKlausurInNeueSchiene("+klausurNr+") --> Die Klausur ist bereits in einer Schiene!");
		_klausurZuSchiene[klausurNr] = _schienenAnzahl;
		_schienenAnzahl++;
		return schiene;
	}

	/** Addiert pDifferenz zur Schienenanzahl.
	 * 
	 * @param pDifferenz Die Differenz der Veränderung. */
	void aktionSchienenAnzahlVeraendern(int pDifferenz) {
		_schienenAnzahl += pDifferenz;
	}

	/** Setzt die übergebene Klausur in eine zufällige vorhandene Schiene. Falls dies nicht möglich ist, wird die
	 * Klausur in eine neue Schiene gesetzt.
	 * 
	 * @param klausurNr Setzt die übergebene Klausur in eine zufällige vorhandene Schiene. Falls dies nicht möglich ist,
	 *                  wird die Klausur in eine neue Schiene gesetzt. */
	void aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(int klausurNr) {
		for (int schienenNr : gibErzeugeSchienenInZufaelligerReihenfolge())
			if (aktionSetzeKlausurInSchiene(klausurNr, schienenNr) == true)
				return; // Die Klausur wurde erfolgreich in eine Schiene gesetzt.

		aktionSetzeKlausurInNeueSchiene(klausurNr); // neue Schiene
	}

	/** Verteilt nicht verteilte Klausuren in zufällige Schienen. Falls dies nicht klappt, wird eine neue Schiene
	 * erzeugt. */
	void aktionSetzeNichtverteilteKlausurenZufaellig() {
		for (int nr : gibErzeugeKlausurenInZufaelligerReihenfolge())
			if (_klausurZuSchiene[nr] == -1)
				aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(nr);
	}

	/** Zerstört einige Schienen, d.h. entfernt alle Klausuren aus den zerstörten Schienen und setzt danach alle
	 * entfernen Klausuren neu. */
	void aktionZerstoereEinigeSchienenUndVerteileNeu() {

		while (_schienenAnzahl > 0) {
			// Schienen s zerstören (letzte Farbe wird mit s getauscht).
			int s = _random.nextInt(_schienenAnzahl);
			for (int nr = 0; nr < _klausurenAnzahl; nr++) {
				if (_klausurZuSchiene[nr] == s) // Dieses IF muss zuerst sein!
					_klausurZuSchiene[nr] = -1;
				if (_klausurZuSchiene[nr] == _schienenAnzahl - 1)
					_klausurZuSchiene[nr] = s;
			}
			_schienenAnzahl--;

			// Aufhören?
			if (_random.nextBoolean())
				break;
		}

		aktionSetzeNichtverteilteKlausurenZufaellig();
	}

	/** Ausgabe zum Debuggen der Tests.
	 * 
	 * @param header Überschrift der Debug-Ausgabe. */
	void debug(String header) {
		System.out.println();
		System.out.println(header);

		for (int s = 0; s < _schienenAnzahl; s++) {
			System.out.println("    Schiene " + (s + 1));
			for (int nr = 0; nr < _klausurenAnzahl; nr++)
				if (_klausurZuSchiene[nr] == s)
					System.out.println("        Klausur " + (nr + 1));
		}

		for (int nr = 0; nr < _klausurenAnzahl; nr++)
			if (_klausurZuSchiene[nr] < 0)
				System.out.println("        Klausur " + (nr + 1) + " --> ohne Schiene!");

	}

	/** Entfernt zunächst alle Klausuren aus ihren Schienen und setzt sie dann in eine zufällige Schiene. Falls dies
	 * nicht klappt, wird eine neue Schiene erzeugt. */
	void aktionEntferneAllesSetzeKlausurenZufaelligAufSchienenZufaellig() {
		aktionKlausurenAusSchienenEntfernen();

		for (int nr : gibErzeugeKlausurenInZufaelligerReihenfolge())
			aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(nr);
	}

	/** Entfernt zunächst alle Klausuren aus ihren Schienen. Verteilt dann die Klausuren mit höherem Grad zuerst auf
	 * eine zufällige Schiene. Falls dies nicht klappt, wird eine neue Schiene erzeugt. */
	void aktionEntferneAllesSetzeKlausurenHoherGradAufSchienenZufaellig() {
		aktionKlausurenAusSchienenEntfernen();

		for (int klausurNr : gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert())
			aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr);
	}

	/** Entfernt zunächst alle Klausuren aus ihren Schienen. Verteilt dann die Klausuren mit den meisten Nachbarsfarben
	 * auf eine zufällige Schiene. Falls dies nicht klappt, wird eine neue Schiene erzeugt. */
	void aktionEntferneAllesSetzeKlausurenMitDenMeistenNachbarsfarbenAufSchienenZufaellig() {
		// reset
		aktionKlausurenAusSchienenEntfernen();

		// first
		int klausurNr = gibKlausurDieFreiIstMitDenMeistenNachbarsfarben();

		while (klausurNr >= 0) {
			// finde mögliche schiene
			aktionSetzeKlausurInZufaelligeSchieneOderErzeugeNeue(klausurNr);

			// next
			klausurNr = gibKlausurDieFreiIstMitDenMeistenNachbarsfarben();
		}
	}

	/** Entfernt zunächst alle Klausuren aus ihren Schienen und füllt dann die Schienen nacheinander auf. */
	void aktionEntferneAllesFuelleSchienenNacheinandeAuf() {
		aktionKlausurenAusSchienenEntfernen();

		while (gibAnzahlNichtverteilterKlausuren() > 0) {
			int schienenNr = gibErzeugeNeueSchiene();

			for (int klausurNr : gibErzeugeKlausurenInZufaelligerReihenfolge())
				if (gibIstKlausurUnverteilt(klausurNr))
					aktionSetzeKlausurInSchiene(klausurNr, schienenNr);
		}
	}

	/** Entfernt zunächst alle Klausuren aus ihren Schienen und füllt dann die Schienen nacheinander auf. Dabei werden
	 * Klausuren mit höherem Grad bevorzugt. */
	void aktionEntferneAllesFuelleSchienenNacheinandeAufHoherGradZuerst() {
		aktionKlausurenAusSchienenEntfernen();

		while (gibAnzahlNichtverteilterKlausuren() > 0) {
			int schienenNr = gibErzeugeNeueSchiene();

			for (int klausurNr : gibErzeugeKlausurenMitHoeheremGradZuerstEtwasPermutiert())
				if (gibIstKlausurUnverteilt(klausurNr))
					aktionSetzeKlausurInSchiene(klausurNr, schienenNr);
		}
	}

}
