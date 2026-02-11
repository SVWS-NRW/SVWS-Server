package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegung;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusMarkierung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.bk.BKGymAbiturUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse enthält eine mögliche Markierungsoption entsprechend den Regeln der Prüfungsordnung
 * Wenn Kurse markiert werden, werden die Daten zur Ermittlung der Punktzahl in Block I direkt mit
 * aufaddiert. Ebenso werden die Anzahlen der Defizite mitgehalten.
 */
public class BKGymAbiturMarkierungsVariante {
	/** Die Verwaltung der Varianten */
	public final @NotNull BKGymAbiturMarkierungsVarianten varianten;

	/** Kennung der Variante */
	private final @NotNull String kennung;

	/** Gibt an, ob die Zulassung mit diesem Ergebnis erreicht wurde oder nicht. */
	private boolean hatZulassung;

	/** Markierung stoppen */
	private boolean gestoppt;

	/** Defizitregeln abschließend geprüft */
	private boolean defizitregelnAbgeschlossen;

	/** Die Summe der Notenpunkte aller Markierungen, LKs sind doppelt gezählt */
	private int summeNotenpunkte;

	/** Die Summe der markierten Kurse, LKs sind doppelt gezählt */
	private int anzahlKurse;

	/** Die Anzahl der defizitären markierten Leistungskurse */
	private int defiziteLK;

	/** Die Anzahl der defizitären markierten Grundkurse */
	private int defiziteGK;

	/** Facharbeit einbeziehen */
	private final boolean facharbeitEinbeziehen;

	/** eine Liste der vorgenommenen Markierungen von Halbjahres-Belegungen in der Qualifikationsphase */
	private final @NotNull List<BKGymAbiturMarkierungsalgorithmusMarkierung> markiert = new ArrayList<>();

	/** eine Liste der Halbjahres-Belegungen in der Qualifikationsphase, die nicht markiert sind */
	private final @NotNull List<BKGymAbiturMarkierungsalgorithmusMarkierung> unmarkiert = new ArrayList<>();

	/** Ein Log, der den Ablauf des Markierungsalgorithmus verdeutlicht */
	private final @NotNull List<String> log = new ArrayList<>();


	/**
	 * Konstruktor für die Root Markierungsvariante. Diese sollte nur einmal vom Markierungsalgorithmus
	 * aufgerufen werden.
	 * Die Fachbelegungen zur Initialisierung wird via Varianten aus dem Manager-Objekt bezogen.
	 *
	 * @param v   die Kollektion der Varianten, zu der auch dieses Objekt gehört.
	 */
	public BKGymAbiturMarkierungsVariante(final @NotNull BKGymAbiturMarkierungsVarianten v) {
		this.varianten = v;
		this.kennung = "Root";
		this.facharbeitEinbeziehen = false;
		this.defizitregelnAbgeschlossen = false;
		init();
	}


	/**
	 * Copy-Konstruktor für neue Varianten. Die Kennung wird an die Kopie angehängt.
	 * Dieser Konstruktor ist zur Verwendung durch die Regel BKGymAbiturMarkierungsregelKopie vorgesehen.
	 *
	 * @param other        das zu kopierende Objekt
	 * @param kennung      die Kennung der neuen Variante
	 * @param facharbeit   ob die Facharbeit in dieser Variante einbezogen wird
	 */
	public BKGymAbiturMarkierungsVariante(final @NotNull BKGymAbiturMarkierungsVariante other, final @NotNull String kennung,
			final boolean facharbeit) {
		this.varianten = other.varianten;
		this.gestoppt = other.gestoppt;
		this.defizitregelnAbgeschlossen = other.defizitregelnAbgeschlossen;
		this.kennung = other.kennung + "#" + kennung;
		this.summeNotenpunkte = other.summeNotenpunkte;
		this.anzahlKurse = other.anzahlKurse;
		this.defiziteLK = other.defiziteLK;
		this.defiziteGK = other.defiziteGK;
		if (facharbeit) {
			this.facharbeitEinbeziehen = true;
			final Integer punkte = varianten.abiturdatenManager.getAbidaten().facharbeitNotenpunkte;
			anzahlKurse += 2;
			if (punkte != null)
				summeNotenpunkte += 2 * punkte;
			if (!varianten.abiturdatenManager.getFachbelegungManager().getIstFacharbeitLK())
				setHatZulassung(false);
		} else {
			this.facharbeitEinbeziehen = other.facharbeitEinbeziehen;
		}
		this.hatZulassung = other.hatZulassung;
		this.markiert.addAll(other.markiert);
		this.unmarkiert.addAll(other.unmarkiert);
		this.log.addAll(other.log);
		addLogEintrag(1, "Die Variante " + this.kennung + " wurde erzeugt.");
	}


	/**
	 * Initialisiert die Liste unmarkiert mit allen Belegungen und sortiert diese
	 * absteigend nach erreichter Punktzahl
	 */
	public void init() {
		final @NotNull List<BKGymAbiturFachbelegung> fachbelegungen = varianten.abiturdatenManager.getAbidaten().fachbelegungen;
		final int schuljahr = varianten.abiturdatenManager.getSchuljahrAbitur();
		this.hatZulassung = true;
		this.gestoppt = false;
		this.summeNotenpunkte = 0;
		this.anzahlKurse = 0;
		this.defiziteLK = 0;
		this.defiziteGK = 0;

		for (final BKGymAbiturFachbelegung fachbelegung : fachbelegungen) {
			for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
				final BKGymAbiturFachbelegungHalbjahr belegung = fachbelegung.belegungen[hj.id];
				if (belegung == null)
					continue;
				if ((belegung.notenkuerzel != null) && (!belegung.notenkuerzel.isEmpty())) {
					final BKGymAbiturMarkierungsalgorithmusMarkierung	markierung = new BKGymAbiturMarkierungsalgorithmusMarkierung();
					markierung.fachID = fachbelegung.fachID;
					markierung.halbjahrID = hj.id;
					markierung.punkte = Note.getPunkteFromNotenkuerzel(belegung.notenkuerzel, schuljahr);
					if (markierung.punkte != null)
						unmarkiert.add(markierung);
				}
			}
		}
		BKGymAbiturMarkierungsVariante.sortMarkierungsliste(unmarkiert);
	}


	/**
	 * Getter für erfolgreich
	 *
	 * @return den Erfolgsstatus
	 */
	public boolean istErfolgreich() {
		return hatZulassung;
	}


	/**
	 * Getter für kennung
	 *
	 * @return die Kennung
	 */
	public String getKennung() {
		return kennung;
	}

	/**
	 * Setter für gestoppt
	 * @param gestoppt   ob fortgesetzt werden soll oder nicht.
	 */
	public void setGestoppt(final boolean gestoppt) {
		this.gestoppt = gestoppt;
	}

	/**
	 * Getter für gestoppt
	 *
	 * @return gestoppt
	 */
	public boolean istGestoppt() {
		return gestoppt;
	}

	/** Getter für defizitregelnAbgeschlossen
	 *
	 * @return defizitregelnAbgeschlossen
	 */
	public boolean sindDefizitregelnAbgeschlossen() {
		return defizitregelnAbgeschlossen;
	}

	/**
	 * Setter für defizitregelnAbgeschlossen
	 *
	 * @param defizitregelnAbgeschlossen   ob die Defizitregeln bereits geprüft wurden
	 */
	public void setDefizitregelnAbgeschlossen(final boolean defizitregelnAbgeschlossen) {
		this.defizitregelnAbgeschlossen = defizitregelnAbgeschlossen;
	}

	/**
	 * Liefert die Gesamtanzahl der eingebrachten Defizite
	 *
	 * @return Anzahl der eingebrachten Defizite
	 */
	public int getDefizite() {
		return defiziteLK + defiziteGK;
	}


	/**
	 * Liefert die Anzahl der eingebrachten Kurse ohne doppelte Gewichtung der LKs
	 *
	 * @return die Anzahl der eingebrachten Kurse
	 */
	public int anzahlEingebrachteKurse() {
		return markiert.size();
	}


	/**
	 * Setzt diese Ergebnisvariante auf nicht erfolgreich
	 *
	 * @param hatZulassung   der entsprechende boolesche Wert
	 */
	public void setHatZulassung(final boolean hatZulassung) {
		this.hatZulassung = hatZulassung;
	}


	/**
	 * Fügt den angegebenen Eintrag der Liste der markierten Einträge hinzu.
	 * Die Auswertungsdaten werden entsprechend der Markierung aktualisiert.
	 *
	 * @param markierung   der zu markierende Eintrag
	 */
	public void markiereEintrag(final BKGymAbiturMarkierungsalgorithmusMarkierung markierung) {
		if (markierung == null)
			return;
		final int defizit = (markierung.punkte == null) || (markierung.punkte < 5) ? 1 : 0;
		markiert.add(markierung);
		anzahlKurse++;
		summeNotenpunkte += (markierung.punkte == null ? 0 : markierung.punkte);
		final Long fachIDLK1 = varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(GostAbiturFach.LK1);
		final Long fachIDLK2 = varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(GostAbiturFach.LK2);
		if ((markierung.punkte != null)
				&& (((fachIDLK1 != null) && (fachIDLK1 == markierung.fachID)) || ((fachIDLK2 != null) && (fachIDLK2 == markierung.fachID)))) {
			//LKs werden doppelt gewichtet
			anzahlKurse++;
			summeNotenpunkte += markierung.punkte;
			defiziteLK += defizit;
		} else {
			defiziteGK += defizit;
		}
	}


	/**
	 * Markiert maximal bis zur angegeben Kursanzahl Kurse, wenn die Bedingung erfüllt ist
	 *
	 * @param kursanzahl   die maximale Anzahl zu markierender Kurse
	 * @param bedingung    die Bedingung, unter der ein Eintrag markiert wird
	 *
	 * @return die Anzahl verbleibender Kurse, die nicht markiert werden konnte
	 */
	public int markiereKursanzahl(final int kursanzahl, final Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung) {
		if (kursanzahl <= 0)
			return 0;
		if (bedingung == null)
			return kursanzahl;
		int verbleibend = kursanzahl;
		final List<BKGymAbiturMarkierungsalgorithmusMarkierung> weiterUnmarkiert = new ArrayList<>(unmarkiert.size());
		for (final @NotNull BKGymAbiturMarkierungsalgorithmusMarkierung unmarked : unmarkiert) {
			if ((verbleibend > 0) && bedingung.test(unmarked)) {
				markiereEintrag(unmarked);
				verbleibend--;
			} else {
				weiterUnmarkiert.add(unmarked);
			}
		}
		unmarkiert.clear();
		unmarkiert.addAll(weiterUnmarkiert);
		return verbleibend;
	}


	/**
	 * prüft, ob die angegebene Anzahl an unmarkierten Kursen der Bedingung genügen.
	 *
	 * @param kursanzahl   die geforderte Anzahl an Kursen, mit der Bedingung
	 * @param bedingung    die Bedingung, unter der ein Eintrag gezählt wird
	 *
	 * @return die Anzahl verbleibender Kurse, die nicht der Bedingung genügen
	 */
	public int pruefeKursanzahl(final int kursanzahl, final Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung) {
		if (bedingung == null)
			return kursanzahl;
		int verbleibend = kursanzahl;
		for (final @NotNull BKGymAbiturMarkierungsalgorithmusMarkierung marked : markiert)
			if ((verbleibend > 0) && bedingung.test(marked))
				verbleibend--;
		for (final @NotNull BKGymAbiturMarkierungsalgorithmusMarkierung unmarked : unmarkiert)
			if ((verbleibend > 0) && bedingung.test(unmarked))
				verbleibend--;
		return verbleibend;
	}


	/**
	 * zählt die markierten Kurse, die die übergebene Bedingung erfüllen
	 *
	 * @param bedingung    die Bedingung, unter der ein Eintrag gezählt wird
	 *
	 * @return die Anzahl der markierten Kurse, die die Bedingung erfüllen
	 */
	public int zaehleMarkierte(final Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> bedingung) {
		if (bedingung == null)
			return 0;
		int anzahl = 0;
		for (final @NotNull BKGymAbiturMarkierungsalgorithmusMarkierung marked : markiert)
			if (bedingung.test(marked))
				anzahl++;
		return anzahl;
	}


	/**
	 * Ermittelt die Punktzahl für das Fach, wenn alle Belegungen vorhanden sind.
	 *
	 * @param fachID     die ID des Fachs
	 * @param anzahl     die Anzahl einzubringender Kurse aus der Q-Phase
	 *
	 * @return die Anzahl der Punkte der eingebrachten Kurse oder 0, wenn die Bedingung belegtSeit nicht erfüllt ist
	 * oder Anzahl nicht erreicht wird.
	 */
	public int punktsummeFuerFach(final long fachID, final int anzahl) {
		int summe = 0;
		int verbleibend = anzahl;
		for (final @NotNull BKGymAbiturMarkierungsalgorithmusMarkierung unmarked : unmarkiert) {
			if (verbleibend <= 0)
				break;
			if (fachID == unmarked.fachID) {
				summe += (unmarked.punkte == null ? 0 : unmarked.punkte);
				verbleibend--;
			}
		}
		if (verbleibend <= 0)
			return summe;
		return 0;
	}


	/**
	 * Ergänzt den Logeintrag mit der angegebenen Einrückung
	 *
	 * @param text     der zu loggende Text
	 * @param indent   die Einrückung mal 4 Leerzeichen des Logeintrags.
	 */
	public void addLogEintrag(final int indent, final @NotNull String text) {
		switch (indent) {
			case 0 -> log.add(text);
			case 1 -> log.add("    " + text);
			case 2 -> log.add("        " + text);
			default -> throw new DeveloperNotificationException("Der Indent " + indent + " ist nicht vorgesehen.");
		}
	}


	/**
	 * Wertet aus ob noch zu markierende Kurse verbleiben
	 *
	 * @param restAnzahl   Anzahl noch zu markierender Kurse
	 * @param sollAnzahl   Anzahl der notwendig einzubringenden Kurse
	 * @param indent       Einrückung für den Log-Eintrag
	 */
	public void addLogAnzahlMarkierungen(final int restAnzahl, final int sollAnzahl, final int indent) {
		if (restAnzahl > 0) {
			addLogEintrag(1, "Fehler: Konnte nicht die nötige Anzahl von " + sollAnzahl + " Kursen einbringen.");
			setHatZulassung(false);
		} else {
			addLogEintrag(1, "Erforderliche Anzahl von " + sollAnzahl + " Kursen konnten eingebracht werden.");
		}
	}


	/**
	 * Berechnet den Punktedurchschnitt, wobei die doppelte Gewichtung der LKs
	 * in den Werten berücksichtigt ist.
	 *
	 * @return der Punktedurchschnitt
	 */
	public float getDurchschnitt() {
		if (anzahlKurse == 0)
			return 0;
		return summeNotenpunkte / (float) anzahlKurse;
	}


	/**
	 * Berechnet die normierte Punktezahl in Block I.
	 * ab 0.5 wird aufgerundet.
	 *
	 * @return die Punktanzahl
	 */
	public int getPunktzahlBlockI() {
		return (int) (getDurchschnitt() * 40 + 0.5);
	}


	/**
	 * Comparator für dieses Objekt.
	 * Kriterien: ZulassungJa, größte PunktzahlBlockI
	 */
	public static final @NotNull Comparator<BKGymAbiturMarkierungsVariante> comparator =
			(final @NotNull BKGymAbiturMarkierungsVariante a, final @NotNull BKGymAbiturMarkierungsVariante b) -> {
				if (a.istErfolgreich() != b.istErfolgreich())
					return a.istErfolgreich() ? -1 : 1;
				return b.getPunktzahlBlockI() - a.getPunktzahlBlockI();
			};


	/**
	 * Liefert das Ergebnis als BKGymAbiturMarkierungsalgorithmusErgebnis DTO
	 *
	 * @return das Ergebnis
	 */
	public BKGymAbiturMarkierungsalgorithmusErgebnis getErgebnis() {
		final BKGymAbiturMarkierungsalgorithmusErgebnis ergebnis = new BKGymAbiturMarkierungsalgorithmusErgebnis();
		ergebnis.erfolgreich = istErfolgreich();
		ergebnis.eingebrachteKurse = anzahlEingebrachteKurse();
		ergebnis.gesamtDefizite = getDefizite();
		ergebnis.lkDefizite = defiziteLK;
		ergebnis.punkteBlockI = getPunktzahlBlockI();
		erzeugeFehlerlog(ergebnis.fehlerLog);
		ergebnis.log.addAll(log);
		ergebnis.markierungen.addAll(markiert);
		return ergebnis;
	}


	/**
	 * Extrahiert die Fehlermeldungen aus dem log
	 * Einträge, die mit Hinweis: beginnen, werden übernommen
	 * Einträge, die mit Fehler: beginnen, werden übernommen und zusätzlich die Zeile davor.
	 * 	 *
	 * @param fehlerLog   die Liste, in die die Fehler eingetragen werden
	 */
	private void erzeugeFehlerlog(final @NotNull List<String> fehlerLog) {
		@NotNull String vorherigeZeile = "";
		for (final @NotNull String zeile : log) {
			if (zeile.startsWith("Hinweis:"))
				fehlerLog.add(zeile);
			else if (zeile.contains("Fehler:")) {
				fehlerLog.add(vorherigeZeile);
				fehlerLog.add(zeile);
			}
			if (zeile.startsWith("Regel"))
				vorherigeZeile = zeile;
		}
	}


	/**
	 * Sortiert die übergebene Liste an Markierungen, so dass die höchsten Punktwerte am Anfang der Liste stehen.
	 * Es wird auch nach den weiteren Attributen einer BKGymAbiturMarkierungsalgorithmusMarkierung sortiert,
	 * so dass die Reihenfolge immer eindeutig ist.
	 *
	 * @param markierungen   die zu sortierende Liste mit den Markierungen
	 */
	public static void sortMarkierungsliste(final @NotNull List<BKGymAbiturMarkierungsalgorithmusMarkierung> markierungen) {
		markierungen.sort(BKGymAbiturUtils.comparatorMarkierung);
	}
}
