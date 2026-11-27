package de.svws_nrw.core.utils.uvblockung;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.core.utils.uv.UvRegelManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse implementiert einen Algorithmus, um Lerngruppen Lehrkräften zuzuordnen.
 * Dabei gibt es {@link UvBlockungRegel}-Objekte, die bei der Berechnung mit Priorität einbezogen werden.
 *
 * <p><b>Invarianten:</b></p>
 * <ul>
 * <li>Zustandsänderungen laufen ausschließlich über die state*-Methoden; diese aktualisieren Malus
 *     und Regel-Caches dynamisch.</li>
 * <li>Strategien sind bewertungsgesichert: bewertungSpeichern → Manipulation → undoAll bei Verschlechterung.</li>
 * <li>Der Malus ist ein Vektor in Prioritätsreihenfolge (kleiner Index = höhere Priorität, kleinerer Wert = besser).</li>
 * </ul>
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDaten {

	/** Debug-Ausgaben. */
	private static final boolean LOG_ADDITIONAL = false;

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger log;

	/** Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung. */
	private final @NotNull Random rnd;

	/** Die Eingabedaten von der GUI (UV). */
	private final @NotNull UvManager man;

	/** Die Eingabedaten von der GUI (UV-Regeln). */
	private final @NotNull UvRegelManager manRegeln;

	/** Der Planungsabschnitt, auf den sich diese Berechnung bezieht. */
	private final @NotNull UvPlanungsabschnitt planungsabschnitt;

	/** Die Menge aller Lehrkräfte (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenLehrkraft @NotNull [] aLehrkraft;

	/** Die Menge aller Lerngruppen (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenLerngruppe @NotNull [] aLerngruppe;

	/** Die Menge aller Klassen (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenKlasse @NotNull [] aKlasse;

	/** Die Menge aller Jahrgänge (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenJahrgang @NotNull [] aJahrgaenge;

	/** Die Menge aller Fächer (dynamisch). */
	private final @NotNull UvAlgorithmusDynDatenFach @NotNull [] aFaecher;

	/** Mapping von der externen Lehrkraft-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenLehrkraft> mapIDzuDynLehrkraft;

	/** Mapping von der externen Lerngruppen-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenLerngruppe> mapIDzuDynLerngruppe;

	/** Mapping von der externen Klassen-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenKlasse> mapIDzuDynKlasse;

	/** Mapping von der externen Jahrgang-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenJahrgang> mapIDzuDynJahrgang;

	/** Mapping von der externen Fach-ID zum internen Objekt. */
	private final @NotNull Map<Long, UvAlgorithmusDynDatenFach> mapIDzuDynFach;

	/** Die Menge aller Undo-Objekte, um Manipulationen schnell rückgängig zu machen. */
	private final @NotNull List<UvAlgorithmusDynDatenUndo> undos;

	/** Bewertung des Malus. */
	private final int @NotNull [] malus;

	/** Zur Speicherung der Bewertung des Malus. */
	private final int @NotNull [] malusSave;


	/** Die Menge aller Regel-Objekte, die sich auf (Lerngruppe, Lehrkraft) beziehen. */
	private final @NotNull List<List<List<UvAlgorithmusDynDatenRegel>>> mapLerngruppeLehrkraftZuRegeln;

	/** Die Menge aller Regel-Objekte, die sich auf (Klasse, Lehrkraft) beziehen. */
	private final @NotNull List<List<List<UvAlgorithmusDynDatenRegel>>> mapKlasseLehrkraftZuRegeln;

	/** Die Menge aller Regel-Objekte, die sich auf (Fach, Lehrkraft) beziehen. */
	private final @NotNull List<List<List<UvAlgorithmusDynDatenRegel>>> mapFachLehrkraftZuRegeln;

	/**
	 * Der Konstruktor.
	 *
	 * @param log                 Ein Logger für Debug-Zwecke.
	 * @param rnd                 Ein Random-Objekt.
	 * @param man                 Ein {@link UvManager}-Objekt, das alle Daten hat.
	 * @param manRegeln           Ein {@link UvRegelManager}-Objekt, das alle Regeln hat.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt}, auf den sich die Manager-Daten beziehen.
	 */
	UvAlgorithmusDynDaten(
			final @NotNull Logger log,
			final @NotNull Random rnd,
			final @NotNull UvManager man,
			final @NotNull UvRegelManager manRegeln,
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {

		this.log = log;
		this.rnd = rnd;
		this.man = man;
		this.planungsabschnitt = planungsabschnitt;
		this.manRegeln = manRegeln;
		this.undos = new ArrayList<>();
		this.mapIDzuDynLehrkraft = new HashMap<>();
		this.mapIDzuDynKlasse = new HashMap<>();
		this.mapIDzuDynLerngruppe = new HashMap<>();
		this.mapIDzuDynJahrgang = new HashMap<>();
		this.mapIDzuDynFach = new HashMap<>();

		// Malus auf 0 setzen.
		this.malus = new int[UvBlockungRegelPrioritaet.values().length];
		this.malusSave = new int[malus.length];
		for (int i = 0; i < malus.length; i++) {
			malus[i] = 0;
			malusSave[i] = 0;
		}

		// Fächer initialisieren.
		this.aFaecher = createFaecher();

		// Jahrgänge initialisieren.
		this.aJahrgaenge = createJahrgaenge();

		// Lehrkräfte initialisieren.
		this.aLehrkraft = createLehrkraefte();

		// Klassen initialisieren.
		this.aKlasse = createKlassen();

		// Lerngruppen initialisieren.
		this.aLerngruppe = createLerngruppen();

		// Dyn-Lerngruppen vs. Dyn-Lehrkräfte
		this.initLerngruppenLehrkraftPotentielle();

		// Alle Regeln mit Bezug zu (Lerngruppe, Lehrkraft).
		this.mapLerngruppeLehrkraftZuRegeln = new ArrayList<>();
		for (int i = 0; i < aLerngruppe.length; i++) {
			this.mapLerngruppeLehrkraftZuRegeln.add(new ArrayList<>());
			for (int j = 0; j < aLehrkraft.length; j++) {
				this.mapLerngruppeLehrkraftZuRegeln.get(i).add(new ArrayList<>());
			}
		}

		// Alle Regeln mit Bezug zu (Klasse, Lehrkraft).
		this.mapKlasseLehrkraftZuRegeln = new ArrayList<>();
		for (int i = 0; i < aKlasse.length; i++) {
			this.mapKlasseLehrkraftZuRegeln.add(new ArrayList<>());
			for (int j = 0; j < aLehrkraft.length; j++) {
				this.mapKlasseLehrkraftZuRegeln.get(i).add(new ArrayList<>());
			}
		}

		// Alle Regeln mit Bezug zu (Fach, Lehrkraft).
		this.mapFachLehrkraftZuRegeln = new ArrayList<>();
		for (int i = 0; i < aFaecher.length; i++) {
			this.mapFachLehrkraftZuRegeln.add(new ArrayList<>());
			for (int j = 0; j < aLehrkraft.length; j++) {
				this.mapFachLehrkraftZuRegeln.get(i).add(new ArrayList<>());
			}
		}

		// Standardregel: Jede Lerngruppe benötigt 1 Lehrkraft.
		for (int i = 0; i < aLerngruppe.length; i++) {
			final int sollLehrkraftAnzahl = 1;
			final int prioritaet = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r28 = new UvAlgorithmusDynDatenRegel28(log, malus, aLerngruppe[i], sollLehrkraftAnzahl, prioritaet);
			aLerngruppe[i].regeln.add(r28);
		}

		// Standardregel: Jede Lehrkraft hat einen Malus für die Soll-Ist-Abweichung.
		for (int i = 0; i < aLehrkraft.length; i++) {
			final boolean aktiviert = true;
			final int prioritaet = UvBlockungRegelPrioritaet.MITTEL.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r45 = new UvAlgorithmusDynDatenRegel45(log, malus, aLehrkraft[i], aktiviert, prioritaet);
			aLehrkraft[i].regeln.add(r45);
		}

		// Standardregel: Jede Klasse erhält einen Malus für die Klassenlehreranzahl und stellv. Klassenlehreranzahl.
		for (int i = 0; i < aKlasse.length; i++) {
			final int sollLeitung1 = 1;
			final int prioritaet1 = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r02 = new UvAlgorithmusDynDatenRegel02(log, malus, aKlasse[i], sollLeitung1, prioritaet1);
			aKlasse[i].regeln.add(r02);
			final int sollLeitung2 = 1;
			final int prioritaet2 = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			final @NotNull UvAlgorithmusDynDatenRegel r04 = new UvAlgorithmusDynDatenRegel04(log, malus, aKlasse[i], sollLeitung2, prioritaet2);
			aKlasse[i].regeln.add(r04);
		}

		// Initialisierung aller Regeln.
		this.initRegeln();

		// Ausgabe einiger Log-Informationen.
		if (LOG_ADDITIONAL) {
			this.logging();
		}
	}


	private void logging() {
		for (int i = 0; i < aLehrkraft.length; i++) {
			log.logLn(LogLevel.INFO, "Dyn-Lehrkraft: " + aLehrkraft[i].toString());
		}
		for (int i = 0; i < aLerngruppe.length; i++) {
			log.logLn(LogLevel.INFO, "Dyn-Lerngruppe: " + aLerngruppe[i].toString());
		}
		log.logLn(LogLevel.INFO, "Malus = " + gibMalusBeschreibung());
	}


	private void bewertungSpeichern() {
		System.arraycopy(malus, 0, malusSave, 0, malus.length);
	}


	private boolean bewertungIstSchlechterAlsGespeichert() {
		// Scanne nach Priorität (Niedriger Index = Höhere Priorität).
		for (int i = 0; i < malus.length; i++) {
			if (malus[i] != malusSave[i]) {
				return malus[i] > malusSave[i];
			}
		}
		// Gleich, also nicht schlechter.
		return false;
	}


	private boolean bewertungIstBesserAlsGespeichert() {
		// Scanne nach Priorität (Niedriger Index = Höhere Priorität).
		for (int i = 0; i < malus.length; i++) {
			if (malus[i] != malusSave[i]) {
				return malus[i] < malusSave[i];
			}
		}
		// Gleich, also nicht besser.
		return false;
	}


	private void undoClear() {
		undos.clear();
	}


	/**
	 * Macht alle gespeicherten Undo-Schritte rückgängig.
	 */
	void undoAll() {
		undoBis(0);
	}


	/**
	 * Macht die Undo-Schritte bis zur angegebenen Stapelgröße rückgängig.
	 *
	 * @param zielGroesse   Die angestrebte Größe des Undo-Stapels.
	 */
	private void undoBis(final int zielGroesse) {
		while (undos.size() > zielGroesse) {
			final @NotNull UvAlgorithmusDynDatenUndo undo = undos.removeLast();
			final int typ = undo.gibTyp();
			final int @NotNull [] data = undo.gibData();

			switch (typ) {
				case 1 -> stateLerngruppeLehrkraftDel(aLerngruppe[data[0]], aLehrkraft[data[1]]);         // Undo von Lehrkraft ADD.
				case 2 -> stateLerngruppeLehrkraftAdd(aLerngruppe[data[0]], aLehrkraft[data[1]], false);  // Undo von Lehrkraft DEL.
				case 3 -> stateKlassenLeitung1Del(aKlasse[data[0]], aLehrkraft[data[1]]);                 // Undo von Klassenleitung 1 ADD.
				case 4 -> stateKlassenLeitung1Add(aKlasse[data[0]], aLehrkraft[data[1]], false);          // Undo von Klassenleitung 1 DEL.
				case 5 -> stateKlassenLeitung2Del(aKlasse[data[0]], aLehrkraft[data[1]]);                 // Undo von Klassenleitung 2 ADD.
				case 6 -> stateKlassenLeitung2Add(aKlasse[data[0]], aLehrkraft[data[1]], false);          // Undo von Klassenleitung 2 DEL.
				// Undo ist unbekannt!
				default ->
					log.logLn(LogLevel.ERROR, "Undo-Typ unbekannt: %d - Undo wird übersprungen.".formatted(typ));
			}

		}
	}


	// ################################################################################
	// Strategien manipulieren den Zustand, ohne die Bewertung zu verschlechtern.
	// ################################################################################


	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige Lehrkraft hinzu.
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	void strategieLerngruppeLehrkraftHinzufuegen() {
		bewertungSpeichern();
		undoClear();

		manipulationLerngruppeLehrkraftAdd();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Lerngruppe entfernt eine zufällige zugeordnete Lehrkraft (falls vorhanden).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	void strategieLerngruppeLehrkraftEntfernen() {
		bewertungSpeichern();
		undoClear();

		manipulationLerngruppeLehrkraftDel();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige Klassenleitung (Leitung 1).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	void strategieKlassenleitung1Hinzufuegen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung1Add();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete Klassenleitung (Leitung 1).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	void strategieKlassenleitung1Entfernen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung1Del();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige stellv. Klassenleitung (Leitung 2).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	void strategieKlassenleitung2Hinzufuegen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung2Add();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete stellv. Klassenleitung (Leitung 2).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	void strategieKlassenleitung2Entfernen() {
		bewertungSpeichern();
		undoClear();

		manipulationKlassenleitung2Del();

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Ein generischer Ring-Ketten-Übernahme-Mix (Ringe zuerst) mit einstellbaren Anzahlen, Runden und
	 * Ringgröße. Bei Verschlechterung wird alles rückgängig gemacht.
	 *
	 * @param ketten        Die Anzahl der Ketten je Runde.
	 * @param ringe         Die Anzahl der Ring-Züge je Runde.
	 * @param uebernahmen   Die Anzahl der Übernahmen je Runde.
	 * @param runden        Die Anzahl der Runden.
	 * @param ringGroesse   Die feste Ringgröße (0 = zufällig).
	 */
	void strategieMixRingZuerst(final int ketten, final int ringe, final int uebernahmen, final int runden, final int ringGroesse) {
		bewertungSpeichern();
		undoClear();

		for (int i = 0; i < runden; i++) {
			if (fuehreRingeAus(ringe, ringGroesse) || fuehreKettenAus(ketten) || fuehreUebernahmenAus(uebernahmen)) {
				return;
			}
		}

		if (bewertungIstSchlechterAlsGespeichert()) {
			undoAll();
		}
	}


	/**
	 * Führt Ring-Züge aus und meldet, ob dabei eine Verbesserung erzielt wurde.
	 *
	 * @param ringe         Die Anzahl der Ring-Züge.
	 * @param ringGroesse   Die feste Ringgröße (0 = zufällig).
	 *
	 * @return TRUE, falls eine Verbesserung erzielt wurde.
	 */
	private boolean fuehreRingeAus(final int ringe, final int ringGroesse) {
		for (int j = 0; j < ringe; j++) {
			if (ringGroesse > 0) {
				manipulationLerngruppeLehrkraftRing(ringGroesse);
			} else {
				manipulationLerngruppeLehrkraftRing();
			}
			if (bewertungIstBesserAlsGespeichert()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Führt Abweichungs-Ketten aus und meldet, ob dabei eine Verbesserung erzielt wurde.
	 *
	 * @param ketten   Die Anzahl der Ketten.
	 *
	 * @return TRUE, falls eine Verbesserung erzielt wurde.
	 */
	private boolean fuehreKettenAus(final int ketten) {
		for (int j = 0; j < ketten; j++) {
			manipulationLerngruppeLehrkraftAbweichungKette();
			if (bewertungIstBesserAlsGespeichert()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Führt Übernahmen aus und meldet, ob dabei eine Verbesserung erzielt wurde.
	 *
	 * @param uebernahmen   Die Anzahl der Übernahmen.
	 *
	 * @return TRUE, falls eine Verbesserung erzielt wurde.
	 */
	private boolean fuehreUebernahmenAus(final int uebernahmen) {
		for (int j = 0; j < uebernahmen; j++) {
			manipulationLerngruppeLehrkraftRing(1);
			if (bewertungIstBesserAlsGespeichert()) {
				return true;
			}
		}
		return false;
	}


	// ################################################################################
	// SDA2: Gezielter Bilanz-Ring (GBR) – exakte Ring-/Tausch-Züge für das Endspiel.
	// ################################################################################


	// ################################################################################
	// Sweep: Mini-Endspiel-Voll-Sweep (MEVS) – deterministische Nullung bei wenigen Residuen.
	// ################################################################################


	// ################################################################################
	// RW1: Dual-Plan 2.0 – Purist-Kern mit Sofort-Tor (≤2 Residuen) und Takt-Tor (3..8).
	// ################################################################################


	// ################################################################################
	// Basis-Strategie der Algorithmen.
	// ################################################################################


	/**
	 * Führt eine zufällige der sechs Basis-Strategien aus.
	 */
	void basisStrategieZufaellig() {
		switch (rnd.nextInt(6)) {
			case 0 -> strategieLerngruppeLehrkraftHinzufuegen();
			case 1 -> strategieLerngruppeLehrkraftEntfernen();
			case 2 -> strategieKlassenleitung1Hinzufuegen();
			case 3 -> strategieKlassenleitung1Entfernen();
			case 4 -> strategieKlassenleitung2Hinzufuegen();
			case 5 -> strategieKlassenleitung2Entfernen();
			default -> {
				// Kann nicht eintreten: rnd.nextInt(6) liefert nur Werte von 0 bis 5.
			}
		}
	}


	/**
	 * Eine Abweichungs-Kette: Eine Über-Soll-Lehrkraft gibt eine Lerngruppe an eine Zwischenlehrkraft
	 * ab, welche dafür eine Lerngruppe an eine Unter-Soll-Lehrkraft abgibt.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationLerngruppeLehrkraftAbweichungKette() {
		manipulationLerngruppeLehrkraftAbweichungKetteVon(gibLehrkraftUeberSollZufaelligOderNull());
	}


	/**
	 * Eine Abweichungs-Kette mit vorgegebener Über-Soll-Lehrkraft (ohne Bewertungsprüfung).
	 *
	 * @param lehrkraftUeber   Die abgebende Über-Soll-Lehrkraft (oder null).
	 */
	void manipulationLerngruppeLehrkraftAbweichungKetteVon(final UvAlgorithmusDynDatenLehrkraft lehrkraftUeber) {
		if (lehrkraftUeber == null) {
			return;
		}
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppenUeber = gibNichtFixierteLerngruppen(lehrkraftUeber);
		if (lernGruppenUeber.isEmpty()) {
			return;
		}

		final UvAlgorithmusDynDatenLerngruppe lerngruppe1 = lernGruppenUeber.get(rnd.nextInt(lernGruppenUeber.size()));
		final @NotNull List<UvAlgorithmusDynDatenLehrkraft> kandidatenZwischen = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenLehrkraft kandidat : aLehrkraft) {
			if ((kandidat != lehrkraftUeber) && lerngruppe1.gibIstLehrkraftPotentiell(kandidat)
					&& !lerngruppe1.gibIstLehrkraftZugeordnet(kandidat) && !gibNichtFixierteLerngruppen(kandidat).isEmpty()) {
				kandidatenZwischen.add(kandidat);
			}
		}
		if (kandidatenZwischen.isEmpty()) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraftZwischen = kandidatenZwischen.get(rnd.nextInt(kandidatenZwischen.size()));
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppenZwischen = gibNichtFixierteLerngruppen(lehrkraftZwischen);
		final UvAlgorithmusDynDatenLerngruppe lerngruppe2 = lernGruppenZwischen.get(rnd.nextInt(lernGruppenZwischen.size()));
		final @NotNull List<UvAlgorithmusDynDatenLehrkraft> kandidatenUnter = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenLehrkraft kandidat : aLehrkraft) {
			if ((kandidat != lehrkraftUeber) && (kandidat != lehrkraftZwischen) && lerngruppe2.gibIstLehrkraftPotentiell(kandidat)
					&& !lerngruppe2.gibIstLehrkraftZugeordnet(kandidat)
					&& ((kandidat.stundenSoll - kandidat.stundenAnrechnung - kandidat.istStundensummeLerngruppen) > 0)) {
				kandidatenUnter.add(kandidat);
			}
		}
		if (kandidatenUnter.isEmpty()) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraftUnter = kandidatenUnter.get(rnd.nextInt(kandidatenUnter.size()));
		stateLerngruppeLehrkraftDel(lerngruppe1, lehrkraftUeber);
		undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe1.interneID, lehrkraftUeber.interneID));
		stateLerngruppeLehrkraftAdd(lerngruppe1, lehrkraftZwischen, false);
		undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe1.interneID, lehrkraftZwischen.interneID));
		stateLerngruppeLehrkraftDel(lerngruppe2, lehrkraftZwischen);
		undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe2.interneID, lehrkraftZwischen.interneID));
		stateLerngruppeLehrkraftAdd(lerngruppe2, lehrkraftUnter, false);
		undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe2.interneID, lehrkraftUnter.interneID));
	}


	// ################################################################################
	// Ring-Züge: 1 bis 3 Lehrkräfte geben jeweils genau eine Lerngruppe im Kreis weiter.
	// Jede Kante verlangt die Qualifikation des Nachfolgers (Fach-Brücken-Test).
	// ################################################################################


	/**
	 * Liefert eine zufällige Lehrkraft oder null, falls keine Lehrkraft existiert.
	 *
	 * @return eine zufällige Lehrkraft oder null, falls keine existiert.
	 */
	private UvAlgorithmusDynDatenLehrkraft gibLehrkraftZufaelligOderNull() {
		if (aLehrkraft.length == 0) {
			return null;
		}
		return aLehrkraft[rnd.nextInt(aLehrkraft.length)];
	}


	/**
	 * Liefert eine zufällige Lehrkraft mit Unter-Soll (Soll − Anrechnung − Ist > 0) oder null,
	 * falls keine Lehrkraft ein Unter-Soll hat.
	 *
	 * @return eine zufällige Lehrkraft mit Unter-Soll oder null.
	 */
	private UvAlgorithmusDynDatenLehrkraft gibLehrkraftUnterSollZufaelligOderNull() {
		final @NotNull List<UvAlgorithmusDynDatenLehrkraft> kandidaten = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			if ((lehrkraft.stundenSoll - lehrkraft.stundenAnrechnung - lehrkraft.istStundensummeLerngruppen) > 0) {
				kandidaten.add(lehrkraft);
			}
		}
		if (kandidaten.isEmpty()) {
			return null;
		}
		return kandidaten.get(rnd.nextInt(kandidaten.size()));
	}


	/**
	 * Liefert mit 50 %-iger Wahrscheinlichkeit eine zufällige Lehrkraft mit Unter-Soll,
	 * ansonsten eine vollständig zufällige Lehrkraft (oder null, falls keine existiert).
	 *
	 * @return eine Lehrkraft mit bevorzugtem Unter-Soll oder null.
	 */
	private UvAlgorithmusDynDatenLehrkraft gibLehrkraftUnterSollBevorzugtZufaelligOderNull() {
		if (rnd.nextBoolean()) {
			final UvAlgorithmusDynDatenLehrkraft kandidat = gibLehrkraftUnterSollZufaelligOderNull();
			if (kandidat != null) {
				return kandidat;
			}
		}
		return gibLehrkraftZufaelligOderNull();
	}


	/**
	 * Liefert alle nicht fixierten Lerngruppen der Lehrkraft, welche überhaupt an eine andere
	 * Lehrkraft übergeben werden können (dafür sind mindestens zwei potentielle Lehrkräfte nötig).
	 *
	 * @param lehrkraft   Die angefragte {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return alle nicht fixierten, übertragbaren Lerngruppen der Lehrkraft.
	 */
	private static @NotNull List<UvAlgorithmusDynDatenLerngruppe> gibNichtFixierteUebertragbareLerngruppen(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> result = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lehrkraft.istLerngruppenZugeordnet) {
			if (!lerngruppe.gibIstLehrkraftFixiert(lehrkraft) && (lerngruppe.lehrkraeftePotentiell.size() > 1)) {
				result.add(lerngruppe);
			}
		}
		return result;
	}


	/**
	 * Prüft, ob die Lehrkraft die Lerngruppe übernehmen könnte
	 * (identisch zur Guard-Prüfung des Zustandswechsels beim Hinzufügen).
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param nehmer       Die {@link UvAlgorithmusDynDatenLehrkraft}, welche die Lerngruppe übernehmen soll.
	 *
	 * @return TRUE, falls die Übernahme möglich ist.
	 */
	private static boolean istUebernahmeMoeglich(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe, final @NotNull UvAlgorithmusDynDatenLehrkraft nehmer) {
		return lerngruppe.gibIstLehrkraftZugeordnetAddMoeglich(nehmer);
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Die Lehrkraft "abgeber" verliert die Lerngruppe, die Lehrkraft "nehmer" erhält sie.
	 * <br> Aktualisiert die Bewertung und erzeugt für beide Schritte die passenden Undo-Objekte.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}, welche den Halter wechselt.
	 * @param abgeber      Die {@link UvAlgorithmusDynDatenLehrkraft}, welche die Lerngruppe abgibt.
	 * @param nehmer       Die {@link UvAlgorithmusDynDatenLehrkraft}, welche die Lerngruppe übernimmt.
	 */
	private void stateLerngruppeLehrkraftWechsel(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft abgeber,
			final @NotNull UvAlgorithmusDynDatenLehrkraft nehmer) {
		stateLerngruppeLehrkraftDel(lerngruppe, abgeber);
		undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe.interneID, abgeber.interneID));
		stateLerngruppeLehrkraftAdd(lerngruppe, nehmer, false);
		undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe.interneID, nehmer.interneID));
	}


	/**
	 * Ein Ring-Zug: Ein bis drei zufällige Lehrkräfte geben jeweils genau eine nicht fixierte,
	 * übertragbare Lerngruppe im Kreis an die nächste Lehrkraft weiter (Ring-Größe 1 = Übernahme).
	 * Die Startlehrkraft wird zur Hälfte bevorzugt unter den Über-Soll-Lehrkräften gezogen.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationLerngruppeLehrkraftRing() {
		// Ring-Größe k: 2 (60 %), 3 (30 %), 1 (10 %).
		final int rndK = rnd.nextInt(10);
		int k = 1;
		if (rndK < 6) {
			k = 2;
		} else if (rndK < 9) {
			k = 3;
		}
		manipulationLerngruppeLehrkraftRing(k);
	}


	/**
	 * Ein Ring-Zug mit fester Ring-Größe (ohne Bewertungsprüfung).
	 *
	 * @param k   Die Ring-Größe: 1 (Übernahme), 2 (Tausch) oder 3 (Rotation).
	 */
	private void manipulationLerngruppeLehrkraftRing(final int k) {
		// Startlehrkraft: 50 % eine zufällige Über-Soll-Lehrkraft, sonst eine zufällige Lehrkraft.
		UvAlgorithmusDynDatenLehrkraft lehrkraft1 = null;
		if (rnd.nextBoolean()) {
			lehrkraft1 = gibLehrkraftUeberSollZufaelligOderNull();
		}
		if (lehrkraft1 == null) {
			lehrkraft1 = gibLehrkraftZufaelligOderNull();
		}
		manipulationLerngruppeLehrkraftRingVon(k, lehrkraft1);
	}


	/**
	 * Ein Ring-Zug mit fester Ring-Größe und vorgegebener Startlehrkraft (ohne Bewertungsprüfung).
	 *
	 * @param k            Die Ring-Größe (1–4).
	 * @param lehrkraft1   Die Startlehrkraft (oder null).
	 */
	private void manipulationLerngruppeLehrkraftRingVon(final int k, final UvAlgorithmusDynDatenLehrkraft lehrkraft1) {
		if (lehrkraft1 == null) {
			return;
		}

		// Lerngruppe der Startlehrkraft.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppen1 = gibNichtFixierteUebertragbareLerngruppen(lehrkraft1);
		if (lernGruppen1.isEmpty()) {
			return;
		}
		final UvAlgorithmusDynDatenLerngruppe lerngruppe1 = lernGruppen1.get(rnd.nextInt(lernGruppen1.size()));

		if (k == 1) {
			manipulationLerngruppeLehrkraftRingUebernahme(lerngruppe1, lehrkraft1);
			return;
		}
		if (k == 2) {
			manipulationLerngruppeLehrkraftRingZwei(lerngruppe1, lehrkraft1);
			return;
		}
		if (k == 3) {
			manipulationLerngruppeLehrkraftRingDrei(lerngruppe1, lehrkraft1);
			return;
		}
		manipulationLerngruppeLehrkraftRingVier(lerngruppe1, lehrkraft1);
	}


	/**
	 * Ein Ring-Zug der Größe 1: Die abgebende Lehrkraft übergibt eine Lerngruppe an eine
	 * (bevorzugt Unter-Soll-)Lehrkraft. Es wird bis zu dreimal ein Nehmer gezogen.
	 *
	 * @param lerngruppe   Die zu übergebende {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param abgeber      Die abgebende {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void manipulationLerngruppeLehrkraftRingUebernahme(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft abgeber) {

		for (int versuch = 0; versuch < 3; versuch++) {
			final UvAlgorithmusDynDatenLehrkraft nehmer = (versuch == 0)
					? gibLehrkraftUnterSollBevorzugtZufaelligOderNull()
					: gibLehrkraftZufaelligOderNull();
			if (nehmer == null) {
				return;
			}
			if ((nehmer != abgeber) && istUebernahmeMoeglich(lerngruppe, nehmer)) {
				stateLerngruppeLehrkraftWechsel(lerngruppe, abgeber, nehmer);
				return;
			}
		}
	}


	/**
	 * Ein Ring-Zug der Größe 2: Zwei Lehrkräfte tauschen je eine übertragbare Lerngruppe.
	 * Die zweite Lehrkraft wird bevorzugt unter den Unter-Soll-Lehrkräften gezogen.
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void manipulationLerngruppeLehrkraftRingZwei(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe1,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft1) {

		for (int versuch = 0; versuch < 3; versuch++) {
			final UvAlgorithmusDynDatenLehrkraft lehrkraft2 = (versuch == 0)
					? gibLehrkraftUnterSollBevorzugtZufaelligOderNull()
					: gibLehrkraftZufaelligOderNull();
			if ((lehrkraft2 != null) && (lehrkraft2 != lehrkraft1)) {
				final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppen2 = gibNichtFixierteUebertragbareLerngruppen(lehrkraft2);
				if (!lernGruppen2.isEmpty()) {
					final UvAlgorithmusDynDatenLerngruppe lerngruppe2 = lernGruppen2.get(rnd.nextInt(lernGruppen2.size()));
					if ((lerngruppe2 != lerngruppe1) && istUebernahmeMoeglich(lerngruppe1, lehrkraft2)
							&& istUebernahmeMoeglich(lerngruppe2, lehrkraft1)) {
						stateLerngruppeLehrkraftWechsel(lerngruppe1, lehrkraft1, lehrkraft2);
						stateLerngruppeLehrkraftWechsel(lerngruppe2, lehrkraft2, lehrkraft1);
						return;
					}
				}
			}
		}
	}


	/**
	 * Ein Ring-Zug der Größe 3: Drei Lehrkräfte rotieren ihre Lerngruppen im Kreis
	 * (Lehrkraft 1 gibt an 2, 2 an 3, 3 an 1).
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void manipulationLerngruppeLehrkraftRingDrei(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe1,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft1) {

		for (int versuch = 0; versuch < 2; versuch++) {
			final UvAlgorithmusDynDatenLehrkraft lehrkraft2 = gibLehrkraftZufaelligOderNull();
			final UvAlgorithmusDynDatenLehrkraft lehrkraft3 = gibLehrkraftZufaelligOderNull();
			if ((lehrkraft2 != null) && (lehrkraft3 != null) && (lehrkraft2 != lehrkraft1) && (lehrkraft3 != lehrkraft1)
					&& (lehrkraft2 != lehrkraft3)) {
				final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppen2 = gibNichtFixierteUebertragbareLerngruppen(lehrkraft2);
				final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppen3 = gibNichtFixierteUebertragbareLerngruppen(lehrkraft3);
				if (!lernGruppen2.isEmpty() && !lernGruppen3.isEmpty()) {
					final UvAlgorithmusDynDatenLerngruppe lerngruppe2 = lernGruppen2.get(rnd.nextInt(lernGruppen2.size()));
					final UvAlgorithmusDynDatenLerngruppe lerngruppe3 = lernGruppen3.get(rnd.nextInt(lernGruppen3.size()));
					if ((lerngruppe2 != lerngruppe1) && (lerngruppe3 != lerngruppe1) && (lerngruppe3 != lerngruppe2)
							&& istUebernahmeMoeglich(lerngruppe1, lehrkraft2) && istUebernahmeMoeglich(lerngruppe2, lehrkraft3)
							&& istUebernahmeMoeglich(lerngruppe3, lehrkraft1)) {
						stateLerngruppeLehrkraftWechsel(lerngruppe1, lehrkraft1, lehrkraft2);
						stateLerngruppeLehrkraftWechsel(lerngruppe2, lehrkraft2, lehrkraft3);
						stateLerngruppeLehrkraftWechsel(lerngruppe3, lehrkraft3, lehrkraft1);
						return;
					}
				}
			}
		}
	}


	/**
	 * Ein Ring-Zug der Größe 4: Vier Lehrkräfte rotieren ihre Lerngruppen im Kreis
	 * (Lehrkraft 1 gibt an 2, 2 an 3, 3 an 4, 4 an 1).
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void manipulationLerngruppeLehrkraftRingVier(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe1,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft1) {

		for (int versuch = 0; versuch < 2; versuch++) {
			if (versucheRingVier(lerngruppe1, lehrkraft1)) {
				return;
			}
		}
	}


	/**
	 * Führt einen Versuch für einen Ring-Zug der Größe 4 aus.
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls der Ring-Zug ausgeführt wurde.
	 */
	private boolean versucheRingVier(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe1,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft1) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft2 = gibLehrkraftZufaelligOderNull();
		final UvAlgorithmusDynDatenLehrkraft lehrkraft3 = gibLehrkraftZufaelligOderNull();
		final UvAlgorithmusDynDatenLehrkraft lehrkraft4 = gibLehrkraftZufaelligOderNull();
		if ((lehrkraft2 == null) || (lehrkraft3 == null) || (lehrkraft4 == null)
				|| (lehrkraft2 == lehrkraft1) || (lehrkraft3 == lehrkraft1) || (lehrkraft4 == lehrkraft1)
				|| (lehrkraft2 == lehrkraft3) || (lehrkraft2 == lehrkraft4) || (lehrkraft3 == lehrkraft4)) {
			return false;
		}
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppen2 = gibNichtFixierteUebertragbareLerngruppen(lehrkraft2);
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppen3 = gibNichtFixierteUebertragbareLerngruppen(lehrkraft3);
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lernGruppen4 = gibNichtFixierteUebertragbareLerngruppen(lehrkraft4);
		if (lernGruppen2.isEmpty() || lernGruppen3.isEmpty() || lernGruppen4.isEmpty()) {
			return false;
		}
		final UvAlgorithmusDynDatenLerngruppe lerngruppe2 = lernGruppen2.get(rnd.nextInt(lernGruppen2.size()));
		final UvAlgorithmusDynDatenLerngruppe lerngruppe3 = lernGruppen3.get(rnd.nextInt(lernGruppen3.size()));
		final UvAlgorithmusDynDatenLerngruppe lerngruppe4 = lernGruppen4.get(rnd.nextInt(lernGruppen4.size()));
		if ((lerngruppe2 == lerngruppe1) || (lerngruppe3 == lerngruppe1) || (lerngruppe4 == lerngruppe1)
				|| (lerngruppe2 == lerngruppe3) || (lerngruppe2 == lerngruppe4) || (lerngruppe3 == lerngruppe4)
				|| !istUebernahmeMoeglich(lerngruppe1, lehrkraft2) || !istUebernahmeMoeglich(lerngruppe2, lehrkraft3)
				|| !istUebernahmeMoeglich(lerngruppe3, lehrkraft4) || !istUebernahmeMoeglich(lerngruppe4, lehrkraft1)) {
			return false;
		}
		stateLerngruppeLehrkraftWechsel(lerngruppe1, lehrkraft1, lehrkraft2);
		stateLerngruppeLehrkraftWechsel(lerngruppe2, lehrkraft2, lehrkraft3);
		stateLerngruppeLehrkraftWechsel(lerngruppe3, lehrkraft3, lehrkraft4);
		stateLerngruppeLehrkraftWechsel(lerngruppe4, lehrkraft4, lehrkraft1);
		return true;
	}


	// ################################################################################
	// Manipulationen verändern den Zustand. Der Zustand kann sich dabei verschlechtern.
	// ################################################################################


	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige potentielle Lehrkraft hinzu.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationLerngruppeLehrkraftAdd() {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeZufaelligOderNull();
		if (lerngruppe == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = lerngruppe.gibLehrkraftPotentiellZufaelligOderNull();
		if (lehrkraft == null) {
			return;
		}

		stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, false);
		undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Lerngruppe entfernt eine zufällige zugeordnete, nicht fixierte Lehrkraft (falls vorhanden).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationLerngruppeLehrkraftDel() {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeZufaelligOderNull();
		if (lerngruppe == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = lerngruppe.gibLehrkraftZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft == null) {
			return;
		}

		stateLerngruppeLehrkraftDel(lerngruppe, lehrkraft);
		undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige potentielle Klassenleitung (Leitung 1).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationKlassenleitung1Add() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaelligOderNull();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung1PotentiellZufaelligOderNull();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung1Add(klasse, lehrkraft, false);
		undos.add(new UvAlgorithmusDynDatenUndo(3, klasse.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete, nicht fixierte Klassenleitung (Leitung 1).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationKlassenleitung1Del() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaelligOderNull();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung1ZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung1Del(klasse, lehrkraft);
		undos.add(new UvAlgorithmusDynDatenUndo(4, klasse.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse erhält eine zufällige potentielle stellv. Klassenleitung (Leitung 2).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationKlassenleitung2Add() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaelligOderNull();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung2PotentiellZufaelligOderNull();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung2Add(klasse, lehrkraft, false);
		undos.add(new UvAlgorithmusDynDatenUndo(5, klasse.interneID, lehrkraft.interneID));
	}


	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete, nicht fixierte stellv. Klassenleitung (Leitung 2).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	void manipulationKlassenleitung2Del() {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseZufaelligOderNull();
		if (klasse == null) {
			return;
		}

		final UvAlgorithmusDynDatenLehrkraft lehrkraft = klasse.gibLeitung2ZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft == null) {
			return;
		}

		stateKlassenLeitung2Del(klasse, lehrkraft);
		undos.add(new UvAlgorithmusDynDatenUndo(6, klasse.interneID, lehrkraft.interneID));
	}


	// ################################################################################
	// Setze-Methoden sind für die externe Manipulation gedacht (z. B. Unit-Tests),
	// sie triggern die jeweilige "state..." Methode.
	// ################################################################################


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt eine Lerngruppen-Lehrkraft-Zuordnung hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param uvLehrkraftID    Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert          Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	void setzeLerngruppeLehrkraftAdd(final long uvLerngruppeID, final long uvLehrkraftID, final boolean fixiert) {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(uvLerngruppeID);
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "setzeLerngruppeLehrkraftAdd: Unbekannte Lerngruppen-ID=%d - Aufruf wird ignoriert.".formatted(uvLerngruppeID));
			return;
		}
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "setzeLerngruppeLehrkraftAdd: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.".formatted(uvLehrkraftID));
			return;
		}
		stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, fixiert);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt eine Lerngruppen-Lehrkraft-Zuordnung.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param uvLehrkraftID    Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	void setzeLerngruppeLehrkraftDel(final long uvLerngruppeID, final long uvLehrkraftID) {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(uvLerngruppeID);
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "setzeLerngruppeLehrkraftDel: Unbekannte Lerngruppen-ID=%d - Aufruf wird ignoriert.".formatted(uvLerngruppeID));
			return;
		}
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "setzeLerngruppeLehrkraftDel: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.".formatted(uvLehrkraftID));
			return;
		}
		stateLerngruppeLehrkraftDel(lerngruppe, lehrkraft);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen Klassenlehrer (Leitung 1) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert         Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	void setzeKlassenLeitung1Add(final long uvKlasseID, final long uvLehrkraftID, final boolean fixiert) {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(uvKlasseID);
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung1Add: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.".formatted(uvKlasseID));
			return;
		}
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung1Add: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.".formatted(uvLehrkraftID));
			return;
		}
		stateKlassenLeitung1Add(klasse, lehrkraft, fixiert);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt aus einer Klasse die aktuelle Klassenleitung (Leitung 1).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	void setzeKlassenLeitung1Del(final long uvKlasseID, final long uvLehrkraftID) {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(uvKlasseID);
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung1Del: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.".formatted(uvKlasseID));
			return;
		}
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung1Del: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.".formatted(uvLehrkraftID));
			return;
		}
		stateKlassenLeitung1Del(klasse, lehrkraft);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellv. Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert         Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	void setzeKlassenLeitung2Add(final long uvKlasseID, final long uvLehrkraftID, final boolean fixiert) {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(uvKlasseID);
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung2Add: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.".formatted(uvKlasseID));
			return;
		}
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung2Add: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.".formatted(uvLehrkraftID));
			return;
		}
		stateKlassenLeitung2Add(klasse, lehrkraft, fixiert);
	}


	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt aus einer Klasse die aktuelle stellv. Klassenleitung (Leitung 2).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	void setzeKlassenLeitung2Del(final long uvKlasseID, final long uvLehrkraftID) {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(uvKlasseID);
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung2Del: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.".formatted(uvKlasseID));
			return;
		}
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "setzeKlassenLeitung2Del: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.".formatted(uvLehrkraftID));
			return;
		}
		stateKlassenLeitung2Del(klasse, lehrkraft);
	}


	// ################################################################################
	// State-Methoden sind die einzigen Methoden, die Zuordnungen verändern dürfen.
	// Alle Bewertungen werden dabei dynamisch aktualisiert.
	// ################################################################################


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt eine Lerngruppen-Lehrkraft-Zuordnung hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert      Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private void stateLerngruppeLehrkraftAdd(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean fixiert) {

		if (lerngruppe.gibIstLehrkraftZugeordnetAddMoeglich(lehrkraft)) {
			// Malus subtrahieren.
			changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);

			// Zustand verändern.
			lerngruppe.stateLehrkraftZugeordnetAdd(lehrkraft, fixiert);
			lehrkraft.stateLerngruppeZugeordnetAdd(lerngruppe);

			// Malus addieren.
			changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
		} else {
			log.logLn(LogLevel.ERROR, "Lerngruppe %s und Lehrkraft %s: Hinzufügen der Zuordnung ist nicht möglich - Aufruf wird ignoriert."
					.formatted(lerngruppe.toString(), lehrkraft.toString()));
		}
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt eine Lerngruppen-Lehrkraft-Zuordnung.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void stateLerngruppeLehrkraftDel(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {

		if (lerngruppe.gibIstLehrkraftZugeordnetDelMoeglich(lehrkraft) && lehrkraft.gibIstLerngruppeZugeordnetDelMoeglich(lerngruppe)) {
			// Malus subtrahieren.
			changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);

			// Zustand verändern.
			lerngruppe.stateLehrkraftZugeordnetDel(lehrkraft);
			lehrkraft.stateLerngruppeZugeordnetDel(lerngruppe);

			// Malus addieren.
			changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
		} else {
			log.logLn(LogLevel.ERROR, "Lerngruppe %s und Lehrkraft %s: Entfernen der Zuordnung ist nicht möglich - Aufruf wird ignoriert."
					.formatted(lerngruppe.toString(), lehrkraft.toString()));
		}
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen Klassenlehrer (Leitung 1) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private void stateKlassenLeitung1Add(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean fixiert) {

		if (klasse.gibIstLeitung1ZugeordnetAddMoeglich(lehrkraft) && lehrkraft.gibIstLeitung1IncMoeglich(klasse)) {
			// Malus subtrahieren
			changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

			// Zustand verändern.
			klasse.stateLeitung1ZugeordnetAdd(lehrkraft, fixiert);
			lehrkraft.stateLeitung1Inc(klasse);

			// Malus addieren.
			changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			log.logLn(LogLevel.ERROR, "Klasse %s und Lehrkraft %s: Hinzufügen von Leitung 1 ist nicht möglich - Aufruf wird ignoriert."
					.formatted(klasse.toString(), lehrkraft.toString()));
		}
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt aus der Klasse die aktuelle Klassenleitung (Leitung 1).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void stateKlassenLeitung1Del(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {

		if (klasse.gibIstLeitung1ZugeordnetDelMoeglich(lehrkraft) && lehrkraft.gibIstLeitung1DecMoeglich(klasse)) {
			// Malus subtrahieren
			changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

			// Zustand verändern.
			klasse.stateLeitung1ZugeordnetDel(lehrkraft);
			lehrkraft.stateLeitung1Dec(klasse);

			// Malus addieren.
			changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			log.logLn(LogLevel.ERROR, "Klasse %s und Lehrkraft %s: Entfernen von Leitung 1 ist nicht möglich - Aufruf wird ignoriert."
					.formatted(klasse.toString(), lehrkraft.toString()));
		}
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellv. Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private void stateKlassenLeitung2Add(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final boolean fixiert) {

		if (klasse.gibIstLeitung2ZugeordnetAddMoeglich(lehrkraft) && lehrkraft.gibIstLeitung2IncMoeglich(klasse)) {
			// Malus subtrahieren
			changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

			// Zustand verändern.
			klasse.stateLeitung2ZugeordnetAdd(lehrkraft, fixiert);
			lehrkraft.stateLeitung2Inc(klasse);

			// Malus addieren.
			changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			log.logLn(LogLevel.ERROR, "Klasse %s und Lehrkraft %s: Hinzufügen von Leitung 2 ist nicht möglich - Aufruf wird ignoriert."
					.formatted(klasse.toString(), lehrkraft.toString()));
		}
	}


	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt aus der Klasse die aktuelle stellv. Klassenleitung (Leitung 2).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private void stateKlassenLeitung2Del(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {

		if (klasse.gibIstLeitung2ZugeordnetDelMoeglich(lehrkraft) && lehrkraft.gibIstLeitung2DecMoeglich(klasse)) {
			// Malus subtrahieren
			changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);

			// Zustand verändern.
			klasse.stateLeitung2ZugeordnetDel(lehrkraft);
			lehrkraft.stateLeitung2Dec(klasse);

			// Malus addieren.
			changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			log.logLn(LogLevel.ERROR, "Klasse %s und Lehrkraft %s: Entfernen von Leitung 2 ist nicht möglich - Aufruf wird ignoriert."
					.formatted(klasse.toString(), lehrkraft.toString()));
		}

	}


	// ################################################################################
	// Diese Methoden benachrichtigen nach einer Zustandsänderung alle betroffenen
	// Regeln, damit diese ihren Malusbeitrag dynamisch neu berechnen können.
	// ################################################################################


	private void changeMalusLerngruppeLehrkraft(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int faktor) {

		// Regeln mit Bezug zu "Lerngruppe".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : lerngruppe.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : lehrkraft.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Klasse".
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : lerngruppe.klassenmenge) {
			// Regeln mit Bezug zu "Klasse" und "Lehrkraft".
			for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID)) {
				regel.changeMalus(faktor);
			}
		}

		// Regeln mit Bezug zu "Lerngruppe" und "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}


		// Regeln mit Bezug zu "Fach" und "Lehrkraft".
		if (lerngruppe.fach != null) {
			for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapFachLehrkraftZuRegeln.get(lerngruppe.fach.interneID).get(lehrkraft.interneID)) {
				regel.changeMalus(faktor);
			}
		}

		// Regeln mit Bezug zu "Wochentag, Stunde, Wochentyp" (Regel 17).
		for (final @NotNull UvAlgorithmusDynDatenRegel17 r17 : sammleBetroffeneRegel17(lerngruppe, lehrkraft)) {
			r17.changeMalus(faktor);
		}
	}


	/**
	 * Sammelt alle Zeitslot-Regeln (Regel 17), deren Malus sich durch eine Änderung der Zuordnung
	 * der Lehrkraft zur Lerngruppe ändern kann. TODO BAR Umsetzung von Regel 17 nioch unschön
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}, deren Zuordnung sich ändert.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}, deren Zuordnung sich ändert.
	 *
	 * @return alle betroffenen Zeitslot-Regeln (Regel 17).
	 */
	private static @NotNull List<UvAlgorithmusDynDatenRegel17> sammleBetroffeneRegel17(
			final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		final @NotNull List<UvAlgorithmusDynDatenRegel17> ergebnis = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenRegel17 slot : lerngruppe.zeitslots) {
			// Die eigenen Zeitslot-Regeln der Lerngruppe.
			sammleZeitslotRegeln(lerngruppe.zeitslots, slot, ergebnis);
			// Die Zeitslot-Regeln aller anderen zugeordneten Lerngruppen im selben (Wochentag, Stunde).
			for (final @NotNull UvAlgorithmusDynDatenLerngruppe andere : lehrkraft.istLerngruppenZugeordnet) {
				sammleZeitslotRegeln(andere.zeitslots, slot, ergebnis);
			}
		}
		return ergebnis;
	}


	/**
	 * Sammelt alle Zeitslot-Regeln der Quelle, die zum selben (Wochentag, Stunde) wie der Referenz-Slot gehören.
	 *
	 * @param quelle    Die zu durchsuchenden Zeitslot-Regeln.
	 * @param slot      Der Referenz-Slot (Wochentag und Stunde).
	 * @param ergebnis  Die Ergebnisliste, in die neue Regeln aufgenommen werden.
	 */
	private static void sammleZeitslotRegeln(final @NotNull List<UvAlgorithmusDynDatenRegel17> quelle,
			final @NotNull UvAlgorithmusDynDatenRegel17 slot, final @NotNull List<UvAlgorithmusDynDatenRegel17> ergebnis) {
		for (final @NotNull UvAlgorithmusDynDatenRegel17 r17 : quelle) {
			if ((r17.wochentag == slot.wochentag) && (r17.stunde == slot.stunde) && !ergebnis.contains(r17)) {
				ergebnis.add(r17);
			}
		}
	}


	private void changeMalusKlasseLehrkraft(
			final @NotNull UvAlgorithmusDynDatenKlasse klasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final int faktor) {

		// Regeln mit Bezug zu "Klasse".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : klasse.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : lehrkraft.regeln) {
			regel.changeMalus(faktor);
		}

		// Regeln mit Bezug zu "Klasse" und "Lehrkraft".
		for (final @NotNull UvAlgorithmusDynDatenRegel regel : mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}

	}


	// ################################################################################
	// Aufbau: Erzeugen der dynamischen Objekte, ID-Mappings und Getter.
	// ################################################################################


	private @NotNull List<UvLehrer> gibUvLehrkraefteOderLeer() {
		try {
			return man.lehrerGetMengeByPlanungsabschnitt(planungsabschnitt);
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			log.logLn(LogLevel.ERROR, "Die Lehrkräfte konnten nicht ermittelt werden - sie werden übersprungen.");
			return new ArrayList<>();
		}
	}


	private @NotNull List<JahrgangsDaten> gibUvJahrgaengeDerLerngruppeOderLeer(final @NotNull UvLerngruppe uvLerngruppe) {
		try {
			return man.jahrgangsdatenGetMengeByLerngruppe(uvLerngruppe);
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			log.logLn(LogLevel.ERROR, "Die Jahrgänge der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.".formatted(uvLerngruppe.id));
			return new ArrayList<>();
		}
	}


	private @NotNull List<UvKlasse> gibUvKlassenDerLerngruppeOderLeer(final @NotNull UvLerngruppe uvLerngruppe) {
		try {
			return man.klasseGetMengeByLerngruppe(uvLerngruppe);
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			log.logLn(LogLevel.ERROR, "Die Klassen der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.".formatted(uvLerngruppe.id));
			return new ArrayList<>();
		}
	}


	private @NotNull UvAlgorithmusDynDatenFach @NotNull [] createFaecher() {
		// Hole die UV-Fächer.
		final @NotNull List<UvFach> uvFachMenge = new ArrayList<>(man.fachGetMengeAsList());


		// Konvertiere UV-Fach zu DYN-Fach.
		final @NotNull UvAlgorithmusDynDatenFach @NotNull [] temp = new UvAlgorithmusDynDatenFach[uvFachMenge.size()];

		for (int i = 0; i < temp.length; i++) {
			final @NotNull UvFach uvFach = uvFachMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenFach(i, uvFach);

			if (mapIDzuDynFach.get(uvFach.id) != null) {
				log.logLn(LogLevel.ERROR, "Bei der Initialisierung der Fächer gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.".formatted(uvFach.id));
			} else {
				mapIDzuDynFach.put(uvFach.id, temp[i]);
			}
		}

		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenJahrgang @NotNull [] createJahrgaenge() {
		// Hole die UV-Jahrgänge.
		final @NotNull List<JahrgangsDaten> uvJahrgangMenge = new ArrayList<>(man.jahrgangsdatenGetMenge());


		// Konvertiere UV-Jahrgang zu DYN-Jahrgang.
		final @NotNull UvAlgorithmusDynDatenJahrgang @NotNull [] temp = new UvAlgorithmusDynDatenJahrgang[uvJahrgangMenge.size()];

		for (int i = 0; i < temp.length; i++) {
			final @NotNull JahrgangsDaten uvJahrgang = uvJahrgangMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenJahrgang(i, uvJahrgang);

			if (mapIDzuDynJahrgang.get(uvJahrgang.id) != null) {
				log.logLn(LogLevel.ERROR, "Bei der Initialisierung der Jahrgänge gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.".formatted(uvJahrgang.id));
			} else {
				mapIDzuDynJahrgang.put(uvJahrgang.id, temp[i]);
			}
		}

		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenLehrkraft @NotNull [] createLehrkraefte() {
		// Hole die UV-Lehrkräfte.
		final @NotNull List<UvLehrer> uvLehrkraftMenge = gibUvLehrkraefteOderLeer();
		if (uvLehrkraftMenge.isEmpty()) {
			return new UvAlgorithmusDynDatenLehrkraft[0];
		}

		// Konvertiere UV-Lehrkräfte zu DYN-Lehrkräften.
		final @NotNull UvAlgorithmusDynDatenLehrkraft @NotNull [] temp = new UvAlgorithmusDynDatenLehrkraft[uvLehrkraftMenge.size()];
		for (int i = 0; i < temp.length; i++) {
			final @NotNull UvLehrer uvLehrkraft = uvLehrkraftMenge.get(i);

			temp[i] = new UvAlgorithmusDynDatenLehrkraft(i, log, man, planungsabschnitt, uvLehrkraft);

			if (mapIDzuDynLehrkraft.get(uvLehrkraft.id) != null) {
				log.logLn(LogLevel.ERROR, "Bei der Initialisierung der Lehrkräfte gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.".formatted(uvLehrkraft.id));
			} else {
				mapIDzuDynLehrkraft.put(uvLehrkraft.id, temp[i]);
			}
		}


		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenKlasse @NotNull [] createKlassen() {

		final @NotNull List<UvKlasse> uvKlassenMenge = man.klasseGetMengeByPlanungsabschnitt(planungsabschnitt);
		final @NotNull UvAlgorithmusDynDatenKlasse @NotNull [] temp = new UvAlgorithmusDynDatenKlasse[uvKlassenMenge.size()];

		for (int i = 0; i < temp.length; i++) {
			final @NotNull UvKlasse uvKlasse = uvKlassenMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenKlasse(log, rnd, i, uvKlasse, aLehrkraft);

			if (mapIDzuDynKlasse.get(uvKlasse.id) != null) {
				log.logLn(LogLevel.ERROR, "Bei der Initialisierung der Klassen gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.".formatted(uvKlasse.id));
			} else {
				mapIDzuDynKlasse.put(uvKlasse.id, temp[i]);
			}
		}

		return temp;
	}


	private @NotNull UvAlgorithmusDynDatenLerngruppe @NotNull [] createLerngruppen() {
		// Hole die UV-Lerngruppen.
		final @NotNull List<UvLerngruppe> uvLerngruppenMenge = man.lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt);

		// Konvertiere UV-Lerngruppen zu DYN-Lerngruppen.
		final @NotNull UvAlgorithmusDynDatenLerngruppe @NotNull [] temp = new UvAlgorithmusDynDatenLerngruppe[uvLerngruppenMenge.size()];
		for (int i = 0; i < temp.length; i++) {
			// Hole und konvertiere die Klassen der Lerngruppe.
			final @NotNull UvLerngruppe uvLerngruppe = uvLerngruppenMenge.get(i);
			final @NotNull List<UvAlgorithmusDynDatenKlasse> klassenmenge = createKlassenOfLerngruppe(uvLerngruppe);
			final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgangmenge = createJahrgaengeOfLerngruppe(uvLerngruppe);

			// Erzeuge die DYN-Lerngruppe.
			temp[i] = new UvAlgorithmusDynDatenLerngruppe(i, this, man, planungsabschnitt, klassenmenge, jahrgangmenge);

			// Fehler?
			if (mapIDzuDynLerngruppe.get(uvLerngruppe.id) != null) {
				log.logLn(LogLevel.ERROR, "Bei der Initialisierung der Lerngruppen gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.".formatted(uvLerngruppe.id));
			} else {
				mapIDzuDynLerngruppe.put(uvLerngruppe.id, temp[i]);
			}
		}

		return temp;
	}


	private @NotNull List<UvAlgorithmusDynDatenJahrgang> createJahrgaengeOfLerngruppe(final @NotNull UvLerngruppe uvLerngruppe) {
		final @NotNull List<JahrgangsDaten> list = gibUvJahrgaengeDerLerngruppeOderLeer(uvLerngruppe);

		final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgangmenge = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			final @NotNull JahrgangsDaten uvJahrgang = list.get(i);

			final UvAlgorithmusDynDatenJahrgang dynJahrgang = mapIDzuDynJahrgang.get(uvJahrgang.id);
			if (dynJahrgang == null) {
				log.logLn(LogLevel.ERROR, "Das Mapping des Jahrgangs UV-ID=%d zu DYN wurde nicht gefunden! - Der Jahrgang wird ignoriert.".formatted(uvJahrgang.id));
				continue;
			}

			jahrgangmenge.add(dynJahrgang);
		}

		return jahrgangmenge;
	}


	private @NotNull List<UvAlgorithmusDynDatenKlasse> createKlassenOfLerngruppe(final @NotNull UvLerngruppe uvLerngruppe) {

		final @NotNull List<UvKlasse> list = gibUvKlassenDerLerngruppeOderLeer(uvLerngruppe);

		final @NotNull List<UvAlgorithmusDynDatenKlasse> klassenmenge = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			final @NotNull UvKlasse uvKlasse = list.get(i);

			final UvAlgorithmusDynDatenKlasse dynKlasse = mapIDzuDynKlasse.get(uvKlasse.id);
			if (dynKlasse == null) {
				log.logLn(LogLevel.ERROR, "Das Mapping der Klasse UV-ID=%d zu DYN wurde nicht gefunden! - Die Klasse wird ignoriert.".formatted(uvKlasse.id));
				continue;
			}

			klassenmenge.add(dynKlasse);
		}

		return klassenmenge;
	}


	/**
	 * Liefert das {@link UvAlgorithmusDynDatenFach} der Lerngruppe.
	 *
	 * @param uvLerngruppe   Die angefragte Lerngruppe.
	 *
	 * @return das {@link UvAlgorithmusDynDatenFach} der Lerngruppe oder null, falls das Mapping nicht gefunden wurde.
	 */
	UvAlgorithmusDynDatenFach gibDynFachByLerngruppe(@NotNull final UvLerngruppe uvLerngruppe) {

		// Bei Kursunterricht wird das Fach über den zugeordneten Kurs ermittelt.
		Long idFach = null;
		try {
			final UvFach fach = man.fachGetByLerngruppe(uvLerngruppe);
			if (fach != null) {
				idFach = fach.id;
			}
		} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
			log.logLn(LogLevel.ERROR, "Das Fach der Lerngruppe UV-ID=%d konnte nicht ermittelt werden - null wird verwendet.".formatted(uvLerngruppe.id));
		}
		final UvAlgorithmusDynDatenFach dynFach = mapIDzuDynFach.get(idFach);
		if (dynFach == null) {
			log.logLn(LogLevel.ERROR, "Das Mapping des Faches UV-ID=%d zu DYN wurde nicht gefunden! - null wird verwendet.".formatted(idFach));
		}

		return dynFach;
	}


	/**
	 * Liefert den {@link Logger}.
	 *
	 * @return den {@link Logger}.
	 */
	@NotNull Logger gibLogger() {
		return log;
	}


	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	@NotNull Random gibRandom() {
		return rnd;
	}


	private UvAlgorithmusDynDatenLerngruppe gibLerngruppeZufaelligOderNull() {
		if (aLerngruppe.length == 0) {
			return null;
		}
		return aLerngruppe[rnd.nextInt(aLerngruppe.length)];
	}


	private static @NotNull List<UvAlgorithmusDynDatenLerngruppe> gibNichtFixierteLerngruppen(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> result = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lehrkraft.istLerngruppenZugeordnet) {
			if (!lerngruppe.gibIstLehrkraftFixiert(lehrkraft)) {
				result.add(lerngruppe);
			}
		}
		return result;
	}


	private UvAlgorithmusDynDatenLehrkraft gibLehrkraftUeberSollZufaelligOderNull() {
		final @NotNull List<UvAlgorithmusDynDatenLehrkraft> kandidaten = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			if ((lehrkraft.stundenSoll - lehrkraft.stundenAnrechnung - lehrkraft.istStundensummeLerngruppen) < 0) {
				kandidaten.add(lehrkraft);
			}
		}
		if (kandidaten.isEmpty()) {
			return null;
		}
		return kandidaten.get(rnd.nextInt(kandidaten.size()));
	}


	private UvAlgorithmusDynDatenKlasse gibKlasseZufaelligOderNull() {
		if (aKlasse.length == 0) {
			return null;
		}
		return aKlasse[rnd.nextInt(aKlasse.length)];
	}


	/**
	 * Liefert die aktuelle Lerngruppen-Lehrkraft-Zuordnung.
	 * <br>Hinweis: Die Liste kann potentiell auch leer sein.
	 *
	 * @return die aktuelle Lerngruppen-Lehrkraft-Zuordnung.
	 */
	@NotNull List<UvLerngruppenLehrer> gibAktuelleZuordnung() {
		final @NotNull List<UvLerngruppenLehrer> list = new ArrayList<>();

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			final @NotNull List<UvAlgorithmusDynDatenLehrkraft> listLehrkraefte = lerngruppe.lehrkraefteZugeordnetAlle;
			for (int i = 0; i < listLehrkraefte.size(); i++) {
				final @NotNull UvLerngruppenLehrer le = new UvLerngruppenLehrer();
				le.id = -1;
				le.idLehrer = listLehrkraefte.get(i).uvID;
				le.idLerngruppe = lerngruppe.uvID;
				le.idPlanungsabschnitt = planungsabschnitt.id;
				le.reihenfolge = i + 1;
				le.wochenstunden = lerngruppe.wochenstundenVorgesehenGekuerzt;
				le.wochenstundenAngerechnet = lerngruppe.wochenstundenVorgesehenGekuerzt;

				list.add(le);
			}
		}

		return list;
	}


	/**
	 * Liefert die aktuellen Klasse-Lehrkraft-Zuordnungen (Klassenleitungen).
	 * <br>Hinweis: Die Liste kann potentiell auch leer sein.
	 *
	 * @return die aktuellen Klasse-Lehrkraft-Zuordnungen (Klassenleitungen).
	 */
	@NotNull List<UvKlassenLehrer> gibAktuelleKlassenleitungen() {
		final @NotNull List<UvKlassenLehrer> list = new ArrayList<>();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			int reihenfolge = 1; // Klassenlehrer beginnen mit Index 1.
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft leitung1und2 : klasse.gibAktuelleMengeLeitung1und2()) {
				final @NotNull UvKlassenLehrer kl = new UvKlassenLehrer();
				kl.id = -1;
				kl.idPlanungsabschnitt = planungsabschnitt.id;
				kl.idKlasse = klasse.uvID;
				kl.idLehrer = leitung1und2.uvID;
				kl.reihenfolge = reihenfolge++;
				list.add(kl);
			}
		}

		return list;
	}


	/**
	 * Liefert den aktuellen Malus der bestimmten {@link UvBlockungRegelPrioritaet}.
	 *
	 * @param prioritaet   Das {@link UvBlockungRegelPrioritaet}-Objekt.
	 *
	 * @return den aktuellen Malus der bestimmten {@link UvBlockungRegelPrioritaet}.
	 */
	int gibMalusDerPrioritaet(final @NotNull UvBlockungRegelPrioritaet prioritaet) {
		return malus[prioritaet.nr];
	}


	/**
	 * Liefert eine Kopie des aktuellen Malus-Arrays.
	 *
	 * @return eine Kopie des aktuellen Malus-Arrays.
	 */
	int @NotNull [] gibMalusKopie() {
		final int @NotNull [] c = new int[malus.length];
		System.arraycopy(malus, 0, c, 0, c.length);
		return c;
	}


	/**
	 * Liefert eine Beschreibung des aktuellen Malus-Zustandes.
	 *
	 * @return eine Beschreibung des aktuellen Malus-Zustandes.
	 */
	@NotNull String gibMalusBeschreibung() {
		return Arrays.toString(malus);
	}


	/**
	 * Prüft, ob die übergebene Regel in der Menge fehlerhafter Regeln enthalten ist.
	 *
	 * @param regel   Die zu prüfende Regel.
	 *
	 * @return {@code true}, wenn die Regel als fehlerhaft erkannt wurde, sonst {@code false}.
	 */
	boolean gibIstRegelFehlerhaft(final @NotNull UvBlockungRegel regel) {
		return manRegeln.regelnGetMengeFehlerhaftAsList().contains(regel);
	}


	/**
	 * Liefert die {@link UvAlgorithmusDynDatenKlasse} zur übergebenen UV-ID oder null.
	 *
	 * @param uvKlasseID   Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 *
	 * @return die {@link UvAlgorithmusDynDatenKlasse} zur übergebenen UV-ID oder null.
	 */
	private UvAlgorithmusDynDatenKlasse gibKlasseOrNull(final long uvKlasseID) {
		return mapIDzuDynKlasse.get(uvKlasseID);
	}


	/**
	 * Liefert die {@link UvAlgorithmusDynDatenLehrkraft} zur übergebenen UV-ID oder null.
	 *
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return die {@link UvAlgorithmusDynDatenLehrkraft} zur übergebenen UV-ID oder null.
	 */
	private UvAlgorithmusDynDatenLehrkraft gibLehrkraftOrNull(final long uvLehrkraftID) {
		return mapIDzuDynLehrkraft.get(uvLehrkraftID);
	}


	/**
	 * Liefert die {@link UvAlgorithmusDynDatenLerngruppe} zur übergebenen UV-ID oder null.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 *
	 * @return die {@link UvAlgorithmusDynDatenLerngruppe} zur übergebenen UV-ID oder null.
	 */
	private UvAlgorithmusDynDatenLerngruppe gibLerngruppeOrNull(final long uvLerngruppeID) {
		return mapIDzuDynLerngruppe.get(uvLerngruppeID);
	}


	/**
	 * Liefert den {@link UvAlgorithmusDynDatenJahrgang} zur übergebenen UV-ID oder null.
	 *
	 * @param uvJahrgangID   Die UV-ID des {@link UvAlgorithmusDynDatenJahrgang}.
	 *
	 * @return den {@link UvAlgorithmusDynDatenJahrgang} zur übergebenen UV-ID oder null.
	 */
	private UvAlgorithmusDynDatenJahrgang gibJahrgangOrNull(final Long uvJahrgangID) {
		return mapIDzuDynJahrgang.get(uvJahrgangID);
	}


	/**
	 * Liefert das {@link UvAlgorithmusDynDatenFach} zur übergebenen UV-ID oder null.
	 *
	 * @param uvFachID   Die UV-ID des {@link UvAlgorithmusDynDatenFach}.
	 *
	 * @return das {@link UvAlgorithmusDynDatenFach} zur übergebenen UV-ID oder null.
	 */
	private UvAlgorithmusDynDatenFach gibFachOrNull(final Long uvFachID) {
		return mapIDzuDynFach.get(uvFachID);
	}


	// ################################################################################
	// Regeln-Initialisierung der dynamischen Daten.
	// ################################################################################


	private void initLerngruppenLehrkraftPotentielle() {
		final @NotNull List<UvLerngruppe> uvLerngruppen = man.lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt);
		final @NotNull List<UvLehrer> uvLehrkraft = gibUvLehrkraefteOderLeer();
		if (uvLehrkraft.isEmpty()) {
			return;
		}

		for (int iLerngruppe = 0; iLerngruppe < aLerngruppe.length; iLerngruppe++) {
			final @NotNull UvLerngruppe lerngruppe = uvLerngruppen.get(iLerngruppe);

			for (int iLehrkraft = 0; iLehrkraft < aLehrkraft.length; iLehrkraft++) {
				final @NotNull UvLehrer lehrkraft = uvLehrkraft.get(iLehrkraft);

				boolean hatLehrbefaehigung;
				try {
					hatLehrbefaehigung = man.lehrerHatLehrbefaehigungLerngruppe(lehrkraft, lerngruppe);
				} catch (@SuppressWarnings("unused") final DeveloperNotificationException ex) {
					log.logLn(LogLevel.ERROR,
							"Die Lehrbefähigung der Lehrkraft UV-ID=%d für die Lerngruppe UV-ID=%d konnte nicht ermittelt werden - sie wird übersprungen."
									.formatted(lehrkraft.id, lerngruppe.id));
					hatLehrbefaehigung = false;
				}
				if (hatLehrbefaehigung) {
					aLerngruppe[iLerngruppe].fuegeLehrkraftHinzu(aLehrkraft[iLehrkraft]);
				}
			}
		}
	}


	private void initRegeln() {
		// Alle Regel-Typen haben eine Sortierreihenfolge von 10.
		final int @NotNull [] sortierungProRegeltyp = new int[UvBlockungRegelTyp.values().length];
		Arrays.fill(sortierungProRegeltyp, 10);

		// Default-Regeln zuerst.
		sortierungProRegeltyp[UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr] = 0;

		// Normale Regeln sind an dieser Stelle in der Sortierreihenfolge.

		// Verbotsregeln.
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr] = 20;

		// Fixierungs-Regeln.
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr] = 30;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr] = 30;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr] = 30;

		// Der Transpiler kann keinen "inline" Comparator verarbeiten, deswegen muss er vorher in dieser Form definiert werden.
		final @NotNull Comparator<UvBlockungRegel> compRegeln = (final @NotNull UvBlockungRegel a, final @NotNull UvBlockungRegel b) -> {
			final int sa = sortierungProRegeltyp[a.typ];
			final int sb = sortierungProRegeltyp[b.typ];
			if (sa != sb) {
				return Integer.compare(sa, sb);
			}
			return Integer.compare(a.typ, b.typ);
		};

		// Sortiere die Regeln.
		final @NotNull List<UvBlockungRegel> regeln = manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList();
		regeln.sort(compRegeln);

		// Wende die Regeln an.
		for (final @NotNull UvBlockungRegel regel : regeln) {
			initRegel(regel);
		}
	}


	private void initRegel(final @NotNull UvBlockungRegel regel) {
		final @NotNull UvBlockungRegelTyp typ = UvBlockungRegelTyp.ofNr(regel.typ);
		final @NotNull List<Long> parameter = regel.parameter;
		final int prioritaet = regel.prioritaet;

		switch (typ) {
			case UNDEFINIERT -> {
				// überspringen.
			}

			case KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER -> initRegel01KlassenlehrerDefault(parameter, prioritaet);
			case KLASSE_A_BENOETIGT_B_KLASSENLEHRER -> initRegel02KlasseBenoetigtKlassenlehrer(parameter, prioritaet);
			case KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER -> initRegel03StellvKlassenlehrerDefault(parameter, prioritaet);
			case KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER -> initRegel04KlasseBenoetigtStellvKlassenlehrer(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE -> initRegel05WennLeitung1Dann(parameter, prioritaet);
			case LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE -> initRegel06WennLehrkraftLeitung1Dann(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE -> initRegel07WennLeitung2Dann(parameter, prioritaet);
			case LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE -> initRegel08WennLehrkraftLeitung2Dann(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER -> initRegel09LehrkraftMaxLeitung1Default(parameter, prioritaet);
			case LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER -> initRegel10LehrkraftMaxLeitung1(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER -> initRegel11LehrkraftMaxLeitung2Default(parameter, prioritaet);
			case LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER -> initRegel12LehrkraftMaxLeitung2(parameter, prioritaet);
			case LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B -> initRegel13LehrkraftVerbotenInLerngruppe(parameter);
			case LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B -> initRegel14LehrkraftFixiertInLerngruppe(parameter);
			case LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B -> initRegel15LehrkraftGerneInLerngruppe(parameter, prioritaet);
			case LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B -> initRegel16LehrkraftUngernInLerngruppe(parameter, prioritaet);
			case LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D -> initRegel17LerngruppeHatWochentagStundeWochentyp(parameter, prioritaet);
			case LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B -> initRegel18LehrkraftVerbotenInKlasse(parameter);
			case LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B -> initRegel19LehrkraftVerbotenInJahrgang(parameter);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C -> initRegel20LehrkraftHatMindestensFach(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C -> initRegel21LehrkraftHatMaximalFach(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN -> initRegel22LehrkraftHatKorrekturenMin(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN -> initRegel23LehrkraftHatKorrekturenMax(parameter, prioritaet);
			case LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN -> initRegel24LehrkraftDarfNichtFachUnterrichtenInDenJahrgaengen(parameter);
			case LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN -> initRegel25LehrkraftDarfNichtFachUnterrichten(parameter);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE -> initRegel26LehrkraftHatMaximaleJahrgangsAnzahl(parameter, prioritaet);
			case LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE -> initRegel27LehrkraftNichtInDerSelbenKlasse(parameter, prioritaet);
			case LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE -> initRegel28LerngruppeBenoetigtBLehrkraefte(parameter, prioritaet);
			case LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT -> initRegel29LerngruppenHabenSelbeLehrkraft(parameter, prioritaet);
			case LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE -> initRegel30LerngruppenHabenVerschiedeneLehrkraefte(parameter, prioritaet);
			case LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN -> initRegel31LerngruppenGemeinsamKuerzenAuf(parameter);
			case LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B -> initRegel32LehrkraftFixiertAlsLeitung1(parameter);
			case LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel33LehrkraftFixiertAlsLeitung2(parameter);
			case LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B -> initRegel34LehrkraftVerbotenAlsLeitung1(parameter);
			case LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel35LehrkraftVerbotenAlsLeitung2(parameter);
			case LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> initRegel36LehrkraftGerneAlsLeitung1(parameter, prioritaet);
			case LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel37LehrkraftGerneAlsLeitung2(parameter, prioritaet);
			case LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B -> initRegel38LehrkraftUngerneAlsLeitung1(parameter, prioritaet);
			case LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B -> initRegel39LehrkraftUngerneAlsLeitung2(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN -> initRegel40LehrkraftMaxAnzahlLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN -> initRegel41LehrkraftMinAnzahlLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN -> initRegel42LehrkraftMinOftInLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN -> initRegel43LehrkraftMaxOftInLerngruppen(parameter, prioritaet);
			case LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A -> initRegel44LehrkraftDefaultSollIstAbweichungAktivierung(parameter, prioritaet);
			case LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B -> initRegel45LehrkraftSollIstAbweichungAktivierung(parameter, prioritaet);

			// ...
			default -> log.logLn(LogLevel.ERROR, "Unbekannter Regeltyp: %d - Regel wird ignoriert.".formatted(regel.typ));
		}
	}


	private void initRegel01KlassenlehrerDefault(final @NotNull List<Long> parameter, final int prioritaet) {
		final int neuLeitung1Anzahl = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			// Die alte Regel muss gefunden und verändert werden.
			for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
				if (r instanceof final UvAlgorithmusDynDatenRegel02 r02) {
					r02.setzeLeitung1AnzahlUndPrioritaet(neuLeitung1Anzahl, prioritaet);
				}
			}
		}
	}


	private void initRegel02KlasseBenoetigtKlassenlehrer(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(0));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 02: Unbekannte Klassen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int neuLeitung1Anzahl = parameter.get(1).intValue();

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel02 r02) {
				r02.setzeLeitung1AnzahlUndPrioritaet(neuLeitung1Anzahl, prioritaet);
			}
		}
	}


	private void initRegel03StellvKlassenlehrerDefault(final @NotNull List<Long> parameter, final int prioritaet) {
		final int neuLeitung2Anzahl = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			// Die alte Regel muss gefunden und verändert werden.
			for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
				if (r instanceof final UvAlgorithmusDynDatenRegel04 r04) {
					r04.setzeLeitung2AnzahlUndPrioritaet(neuLeitung2Anzahl, prioritaet);
				}
			}
		}
	}


	private void initRegel04KlasseBenoetigtStellvKlassenlehrer(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(0));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 04: Unbekannte Klassen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int neuLeitung2Anzahl = parameter.get(1).intValue();

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : klasse.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel04 r04) {
				r04.setzeLeitung2AnzahlUndPrioritaet(neuLeitung2Anzahl, prioritaet);
			}
		}
	}


	private void initRegel05WennLeitung1Dann(final @NotNull List<Long> parameter, final int prioritaet) {
		final int minKlassenstundenBeiLeitung1 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
				final @NotNull UvAlgorithmusDynDatenRegel r06 =
						new UvAlgorithmusDynDatenRegel06(log, malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
			}
		}
	}


	private void initRegel06WennLehrkraftLeitung1Dann(@NotNull final List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 06: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int minKlassenstundenBeiLeitung1 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel06) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull UvAlgorithmusDynDatenRegel r06 =
					new UvAlgorithmusDynDatenRegel06(log, malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
		}
	}


	private void initRegel07WennLeitung2Dann(final @NotNull List<Long> parameter, final int prioritaet) {
		final int minKlassenstundenBeiLeitung2 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
				final @NotNull UvAlgorithmusDynDatenRegel r08 =
						new UvAlgorithmusDynDatenRegel08(log, malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
			}
		}
	}


	private void initRegel08WennLehrkraftLeitung2Dann(@NotNull final List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 08: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int minKlassenstundenBeiLeitung2 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel08) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull UvAlgorithmusDynDatenRegel r08 =
					new UvAlgorithmusDynDatenRegel08(log, malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
		}
	}


	private void initRegel09LehrkraftMaxLeitung1Default(final @NotNull List<Long> parameter, final int prioritaet) {
		final int maxLeitung1 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			final @NotNull UvAlgorithmusDynDatenRegel r10 = new UvAlgorithmusDynDatenRegel10(log, malus, lehrkraft, maxLeitung1, prioritaet);
			for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
			}
		}
	}


	private void initRegel10LehrkraftMaxLeitung1(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 10: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int maxLeitung1 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel10) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		final @NotNull UvAlgorithmusDynDatenRegel r10 = new UvAlgorithmusDynDatenRegel10(log, malus, lehrkraft, maxLeitung1, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
		}
	}


	private void initRegel11LehrkraftMaxLeitung2Default(final @NotNull List<Long> parameter, final int prioritaet) {
		final int maxLeitung2 = parameter.get(0).intValue();

		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			final @NotNull UvAlgorithmusDynDatenRegel r12 = new UvAlgorithmusDynDatenRegel12(log, malus, lehrkraft, maxLeitung2, prioritaet);
			for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
				mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
			}
		}
	}


	private void initRegel12LehrkraftMaxLeitung2(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 12: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int maxLeitung2 = parameter.get(1).intValue();

		// Wenn es die Regel aufgrund eines DEFAULTS bereits gibt, lösche sie.
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull List<UvAlgorithmusDynDatenRegel> regeln = mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (final @NotNull Iterator<UvAlgorithmusDynDatenRegel> iterator = regeln.iterator(); iterator.hasNext();) {
				final @NotNull UvAlgorithmusDynDatenRegel r = iterator.next();
				if (r instanceof UvAlgorithmusDynDatenRegel12) {
					iterator.remove();
				}
			}
		}

		// Füge die Regel nun hinzu.
		final @NotNull UvAlgorithmusDynDatenRegel r12 = new UvAlgorithmusDynDatenRegel12(log, malus, lehrkraft, maxLeitung2, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
		}
	}


	private void initRegel13LehrkraftVerbotenInLerngruppe(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 13: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "Regel 13: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}
		lerngruppe.entferneLehrkraft(lehrkraft);
	}


	private void initRegel14LehrkraftFixiertInLerngruppe(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 14: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "Regel 14: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}
		stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, true);
	}


	private void initRegel15LehrkraftGerneInLerngruppe(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 15: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "Regel 15: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r15 = new UvAlgorithmusDynDatenRegel15(log, malus, lerngruppe, lehrkraft, prioritaet);
		mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r15);
	}


	private void initRegel16LehrkraftUngernInLerngruppe(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 16: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "Regel 16: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r16 = new UvAlgorithmusDynDatenRegel16(log, malus, lerngruppe, lehrkraft, prioritaet);
		mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r16);
	}


	private void initRegel17LerngruppeHatWochentagStundeWochentyp(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(0));
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "Regel 17: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int wochentag = parameter.get(1).intValue();
		final int stunde = parameter.get(2).intValue();
		final int wochentyp = parameter.get(3).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel17 r17 = new UvAlgorithmusDynDatenRegel17(log, malus, lerngruppe, wochentag, stunde, wochentyp, prioritaet);
		lerngruppe.zeitslots.add(r17);
	}


	private void initRegel18LehrkraftVerbotenInKlasse(@NotNull final List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 18: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 18: Unbekannte Klassen-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennKlasseUebereinstimmt(lehrkraft, klasse);
		}
	}


	private void initRegel19LehrkraftVerbotenInJahrgang(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 19: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenJahrgang jahrgang = gibJahrgangOrNull(parameter.get(1));
		if (jahrgang == null) {
			log.logLn(LogLevel.ERROR, "Regel 19: Unbekannte Jahrgang-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennJahrgangUebereinstimmt(lehrkraft, jahrgang);
		}
	}


	private void initRegel20LehrkraftHatMindestensFach(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 20: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int minimum = parameter.get(1).intValue();
		final UvAlgorithmusDynDatenFach fach = gibFachOrNull(parameter.get(2));
		if (fach == null) {
			log.logLn(LogLevel.ERROR, "Regel 20: Unbekannte Fach-ID=%d - Regel wird ignoriert.".formatted(parameter.get(2)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r20 = new UvAlgorithmusDynDatenRegel20(log, malus, fach, lehrkraft, minimum, prioritaet);
		mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r20);
	}


	private void initRegel21LehrkraftHatMaximalFach(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 21: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int maximum = parameter.get(1).intValue();
		final UvAlgorithmusDynDatenFach fach = gibFachOrNull(parameter.get(2));
		if (fach == null) {
			log.logLn(LogLevel.ERROR, "Regel 21: Unbekannte Fach-ID=%d - Regel wird ignoriert.".formatted(parameter.get(2)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r21 = new UvAlgorithmusDynDatenRegel21(log, malus, fach, lehrkraft, maximum, prioritaet);
		mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r21);
	}


	private void initRegel22LehrkraftHatKorrekturenMin(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 22: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int minimum = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r22 = new UvAlgorithmusDynDatenRegel22(log, malus, lehrkraft, minimum, prioritaet);
		lehrkraft.regeln.add(r22);
	}


	private void initRegel23LehrkraftHatKorrekturenMax(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 23: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int maximum = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r23 = new UvAlgorithmusDynDatenRegel23(log, malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r23);
	}


	private void initRegel24LehrkraftDarfNichtFachUnterrichtenInDenJahrgaengen(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 24: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenFach fach = gibFachOrNull(parameter.get(1));
		if (fach == null) {
			log.logLn(LogLevel.ERROR, "Regel 24: Unbekannte Fach-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgaenge = new ArrayList<>();
		for (int i = 2; i < parameter.size(); i++) {
			final UvAlgorithmusDynDatenJahrgang jahrgang = gibJahrgangOrNull(parameter.get(i));
			if (jahrgang == null) {
				log.logLn(LogLevel.ERROR, "Regel 24: Unbekannte Jahrgang-ID=%d - Regel wird ignoriert.".formatted(parameter.get(i)));
				return;
			}
			jahrgaenge.add(jahrgang);
		}

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennJahrgangUndFachUebereinstimmt(lehrkraft, fach, jahrgaenge);
		}
	}


	private void initRegel25LehrkraftDarfNichtFachUnterrichten(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 25: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenFach fach = gibFachOrNull(parameter.get(1));
		if (fach == null) {
			log.logLn(LogLevel.ERROR, "Regel 25: Unbekannte Fach-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : aLerngruppe) {
			lerngruppe.entferneLehrkraftWennFachUebereinstimmt(lehrkraft, fach);
		}
	}


	private void initRegel26LehrkraftHatMaximaleJahrgangsAnzahl(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 26: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int maximum = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r26 = new UvAlgorithmusDynDatenRegel26(log, malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r26);
	}


	private void initRegel27LehrkraftNichtInDerSelbenKlasse(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft1 = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft1 == null) {
			log.logLn(LogLevel.ERROR, "Regel 27: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenLehrkraft lehrkraft2 = gibLehrkraftOrNull(parameter.get(1));
		if (lehrkraft2 == null) {
			log.logLn(LogLevel.ERROR, "Regel 27: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : aKlasse) {
			final @NotNull UvAlgorithmusDynDatenRegel r27 = new UvAlgorithmusDynDatenRegel27(log, malus, klasse, lehrkraft1, lehrkraft2, prioritaet);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft1.interneID).add(r27);
			mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft2.interneID).add(r27);
		}
	}


	private void initRegel28LerngruppeBenoetigtBLehrkraefte(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(0));
		if (lerngruppe == null) {
			log.logLn(LogLevel.ERROR, "Regel 28: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int neuesSoll = parameter.get(1).intValue();

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : lerngruppe.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel28 r28) {
				r28.setzeAuf(neuesSoll, prioritaet);
			}
		}
	}


	private void initRegel29LerngruppenHabenSelbeLehrkraft(final @NotNull List<Long> parameter, final int prioritaet) {
		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 0; i < parameter.size(); i++) {
			final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe == null) {
				log.logLn(LogLevel.ERROR, "Regel 29: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.".formatted(parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}

		// Diese Menge allen Lerngruppen zuordnen.
		final @NotNull UvAlgorithmusDynDatenRegel r29 = new UvAlgorithmusDynDatenRegel29(log, malus, lerngruppenMenge, aLehrkraft.length, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			lerngruppe.regeln.add(r29);
		}
	}


	private void initRegel30LerngruppenHabenVerschiedeneLehrkraefte(final @NotNull List<Long> parameter, final int prioritaet) {
		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 0; i < parameter.size(); i++) {
			final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe == null) {
				log.logLn(LogLevel.ERROR, "Regel 30: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.".formatted(parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}

		// Diese Menge allen Lerngruppen zuordnen.
		final @NotNull UvAlgorithmusDynDatenRegel r30 = new UvAlgorithmusDynDatenRegel30(log, malus, lerngruppenMenge, aLehrkraft.length, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			lerngruppe.regeln.add(r30);
		}
	}


	private void initRegel31LerngruppenGemeinsamKuerzenAuf(final @NotNull List<Long> parameter) {
		// Hinweis: Regel 31 bricht die Konvention und setzt das Attribut nicht dynamisch, sondern als PRE-DEFAULT.
		//          Falls das Stunden-Soll irgendwann mal dynamisch änderbar sein soll, dann muss zweierlei angepasst werden:
		//          1) Das Stunden-Soll bei der Lerngruppe (ändert nie einen Malus).
		//          2) Die Stundensummen aller Lehrkräfte der Lerngruppe (ändert den Malus!).
		final int neuerStundenSoll = parameter.get(0).intValue();

		for (int i = 1; i < parameter.size(); i++) {
			final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe == null) {
				log.logLn(LogLevel.ERROR, "Regel 31: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.".formatted(parameter.get(i)));
				return;
			}
			lerngruppe.setzeStundenSollAuf(neuerStundenSoll);
		}

	}


	private void initRegel32LehrkraftFixiertAlsLeitung1(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 32: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 32: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}
		stateKlassenLeitung1Add(klasse, lehrkraft, true);
	}


	private void initRegel33LehrkraftFixiertAlsLeitung2(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 33: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 33: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}
		stateKlassenLeitung2Add(klasse, lehrkraft, true);
	}


	private void initRegel34LehrkraftVerbotenAlsLeitung1(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 34: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 34: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}
		klasse.entferneLeitung1(lehrkraft);
	}


	private void initRegel35LehrkraftVerbotenAlsLeitung2(final @NotNull List<Long> parameter) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 35: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 35: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}
		klasse.entferneLeitung2(lehrkraft);
	}


	private void initRegel36LehrkraftGerneAlsLeitung1(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 36: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 36: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r36 = new UvAlgorithmusDynDatenRegel36(log, malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r36);
	}


	private void initRegel37LehrkraftGerneAlsLeitung2(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 37: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 37: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r37 = new UvAlgorithmusDynDatenRegel37(log, malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r37);
	}


	private void initRegel38LehrkraftUngerneAlsLeitung1(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 38: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 38: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r38 = new UvAlgorithmusDynDatenRegel38(log, malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r38);
	}


	private void initRegel39LehrkraftUngerneAlsLeitung2(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 39: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final UvAlgorithmusDynDatenKlasse klasse = gibKlasseOrNull(parameter.get(1));
		if (klasse == null) {
			log.logLn(LogLevel.ERROR, "Regel 39: Unbekannte Klasse-ID=%d - Regel wird ignoriert.".formatted(parameter.get(1)));
			return;
		}

		final @NotNull UvAlgorithmusDynDatenRegel r39 = new UvAlgorithmusDynDatenRegel39(log, malus, klasse, lehrkraft, prioritaet);
		mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r39);
	}


	private void initRegel40LehrkraftMaxAnzahlLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 40: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int max = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r40 = new UvAlgorithmusDynDatenRegel40(log, malus, lehrkraft, max, prioritaet);
		lehrkraft.regeln.add(r40);
	}


	private void initRegel41LehrkraftMinAnzahlLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 41: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int min = parameter.get(1).intValue();

		final @NotNull UvAlgorithmusDynDatenRegel r41 = new UvAlgorithmusDynDatenRegel41(log, malus, lehrkraft, min, prioritaet);
		lehrkraft.regeln.add(r41);
	}


	private void initRegel42LehrkraftMinOftInLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 42: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int min = parameter.get(1).intValue();

		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 2; i < parameter.size(); i++) {
			final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe == null) {
				log.logLn(LogLevel.ERROR, "Regel 42: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.".formatted(parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}

		// Alle Lerngruppen erhalten die Regel, wobei sie nur einmal erzeugt werden darf.
		final @NotNull UvAlgorithmusDynDatenRegel r42 = new UvAlgorithmusDynDatenRegel42(log, malus, lehrkraft, min, lerngruppenMenge, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r42);
		}

	}


	private void initRegel43LehrkraftMaxOftInLerngruppen(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 43: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final int max = parameter.get(1).intValue();

		// Menge aller Lerngruppen erzeugen.
		final @NotNull List<UvAlgorithmusDynDatenLerngruppe> lerngruppenMenge = new ArrayList<>();
		for (int i = 2; i < parameter.size(); i++) {
			final UvAlgorithmusDynDatenLerngruppe lerngruppe = gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe == null) {
				log.logLn(LogLevel.ERROR, "Regel 43: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.".formatted(parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}

		// Alle Lerngruppen erhalten die Regel, wobei sie nur einmal erzeugt werden darf.
		final @NotNull UvAlgorithmusDynDatenRegel r43 = new UvAlgorithmusDynDatenRegel43(log, malus, lehrkraft, max, lerngruppenMenge, prioritaet);
		for (final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe : lerngruppenMenge) {
			mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r43);
		}
	}


	private void initRegel44LehrkraftDefaultSollIstAbweichungAktivierung(final @NotNull List<Long> parameter, final int prioritaet) {
		final boolean aktiviert = (parameter.get(0) == 1);

		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : aLehrkraft) {
			// Die alte Regel muss gefunden und verändert werden.
			for (final @NotNull UvAlgorithmusDynDatenRegel r : lehrkraft.regeln) {
				if (r instanceof final UvAlgorithmusDynDatenRegel45 r45) {
					r45.setzeAuf(aktiviert, prioritaet);
				}
			}
		}
	}


	private void initRegel45LehrkraftSollIstAbweichungAktivierung(final @NotNull List<Long> parameter, final int prioritaet) {
		final UvAlgorithmusDynDatenLehrkraft lehrkraft = gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft == null) {
			log.logLn(LogLevel.ERROR, "Regel 45: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.".formatted(parameter.get(0)));
			return;
		}
		final boolean aktiviert = (parameter.get(1) == 1);

		// Die alte Regel muss gefunden und verändert werden.
		for (final @NotNull UvAlgorithmusDynDatenRegel r : lehrkraft.regeln) {
			if (r instanceof final UvAlgorithmusDynDatenRegel45 r45) {
				r45.setzeAuf(aktiviert, prioritaet);
			}
		}
	}


}
