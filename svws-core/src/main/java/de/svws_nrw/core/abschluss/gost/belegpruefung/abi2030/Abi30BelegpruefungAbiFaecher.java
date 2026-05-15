package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;

/*
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche für die ABitur-Fächer durchgeführt werden.
 */
public final class Abi30BelegpruefungAbiFaecher extends GostBelegpruefung {

	/** Eine ArrayMap für den schnellen Zugriff auf die 5 Abiturfachbelegungen, sofern diese zugeordnet sind. */
	private ArrayMap<GostAbiturFach, AbiturFachbelegung> mapAbiturFachbelegungen;

	/**  Die Anzahl der belegten Abitur-Fächer (sollten 5 sein). */
	private int anzahlAbiFaecher;

	/**  Die Anzahl der Abiturfächer im Bereich Deutsch, Mathematik oder Fremdsprache (muss mindestens 2 sein). */
	private int anzahlDeutschMatheFremdsprache;

	/**  Die Anzahl der Fremdsprachen. */
	private int anzahlFremdsprachen;

	/** Gibt an, ob das AufgabenFeld I abgedeckt ist. */
	private boolean hatAufgabenfeldI;

	/** Gibt an, ob das AufgabenFeld II abgedeckt ist. */
	private boolean hatAufgabenfeldII;

	/** Gibt an, ob das AufgabenFeld III abgedeckt ist. */
	private boolean hatAufgabenfeldIII;


	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 *
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse    das Ergebnis für die Belegprüfung der Projektkurse
	 */
	public Abi30BelegpruefungAbiFaecher(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt,
			final @NotNull Abi30BelegpruefungProjektkurse pruefungProjektkurse) {
		super(manager, pruefungsArt, pruefungProjektkurse);
	}


	@Override
	protected void init() {
		mapAbiturFachbelegungen = new ArrayMap<>(GostAbiturFach.values());
		anzahlAbiFaecher = 0;
		anzahlDeutschMatheFremdsprache = 0;
		anzahlFremdsprachen = 0;
		hatAufgabenfeldI = false;
		hatAufgabenfeldII = false;
		hatAufgabenfeldIII = false;

		// Bestimme die Abiturfächer aus den Belegungsdaten
		final @NotNull List<AbiturFachbelegung> alleFachbelegungen = manager.getRelevanteFachbelegungen();
		for (final AbiturFachbelegung fachbelegung : alleFachbelegungen) {
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach == null) {
				continue;
			}
			mapAbiturFachbelegungen.put(abiturFach, fachbelegung);
			anzahlAbiFaecher++;
			// Bestimme Aufgabenfelder
			final GostFach fach = manager.getFach(fachbelegung);
			if (fach == null) {
				continue;
			}
			if (GostFachbereich.FREMDSPRACHE.hat(fach) || GostFachbereich.DEUTSCH.hat(fach)) {
				hatAufgabenfeldI = true;
			}
			if (GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION.hat(fach)) {
				hatAufgabenfeldII = true;
			}
			if (GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH.hat(fach)) {
				hatAufgabenfeldIII = true;
			}

			// Zähle Fächer im Bereich Deutsch, Mathematik und Fremdsprache
			if (GostFachbereich.FREMDSPRACHE.hat(fach)
					|| GostFachbereich.DEUTSCH.hat(fach)
					|| GostFachbereich.MATHEMATIK.hat(fach)) {
				anzahlDeutschMatheFremdsprache++;
			}

			// Zähle die Anzahl der Fremdsprachen
			if (GostFachbereich.FREMDSPRACHE.hat(fach)) {
				anzahlFremdsprachen++;
			}
		}
	}


	@Override
	protected void pruefeEF1() {
		// nichts zu tun, da diese in der EF.1 noch nicht festgelegt werden...
	}


	@Override
	protected void pruefeGesamt() {
		pruefeLK1();
		pruefeAnzahlUndAufgabenfelderAbiFaecher();
		pruefeMehrfacheAbiturfaecher();
		pruefeSchriftlichkeitAB3undAB4();
		pruefeSchriftlichkeitAB5();
	}




	/**
	 * Gesamtprüfung Punkt 70:
	 * Prüfe, ob der erste LK eine fortgeführte Fremdsprache, eine klassische Naturwissenschaft, Mathematik oder Deutsch ist
	 */
	private void pruefeLK1() {
		final AbiturFachbelegung lk1 = (mapAbiturFachbelegungen == null) ? null : mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		final GostFach lk1fach = manager.getFach(lk1);
		if ((lk1 == null) || (lk1fach == null) || !((GostFachbereich.DEUTSCH.hat(lk1fach))
				|| (GostFachbereich.FREMDSPRACHE.hat(lk1fach) && !lk1.istFSNeu)
				|| (GostFachbereich.MATHEMATIK.hat(lk1fach))
				|| (GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(lk1fach)))) {
			addFehler(GostBelegungsfehler.GOST30_LK1_11);
		}
	}


	/**
	 * Gesamtprüfung Punkt 71-74:
	 * Prüfe, ob die Zahl der Abiturfächer 5 ist und diese alle Aufgabenfelder abdecken
	 *    und ob mindestens 2 Fächer im Bereich Deutsch, Fremdsprache, Mathematik liegen
	 *    und ob maximale 1 Fach im Bereich Sport und Religion liegt
	 *    und ob Sport nicht als erstes oder drittes Abiturfach gewählt wurde
	 */
	private void pruefeAnzahlUndAufgabenfelderAbiFaecher() {
		if ((!hatAufgabenfeldI) || (!hatAufgabenfeldII) || (!hatAufgabenfeldIII)) {
			addFehler(GostBelegungsfehler.GOST30_LK1_12);
		}
		if (anzahlAbiFaecher != 5) {
			addFehler(GostBelegungsfehler.GOST30_LK1_13);
		}
		if (anzahlDeutschMatheFremdsprache < 2) {
			addFehler(GostBelegungsfehler.GOST30_ABI_10);
		}
		if ((anzahlDeutschMatheFremdsprache < 3) && (anzahlFremdsprachen > 1)) {
			addFehler(GostBelegungsfehler.GOST30_ABI_19);
		}
		final AbiturFachbelegung lk1 = (mapAbiturFachbelegungen == null) ? null : mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		final GostFach lk1fach = manager.getFach(lk1);
		final AbiturFachbelegung ab3 = (mapAbiturFachbelegungen == null) ? null : mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		final GostFach ab3fach = manager.getFach(ab3);
		if (((lk1fach != null) && (GostFachbereich.SPORT.hatKuerzel(lk1fach.kuerzel)))
				|| ((ab3fach != null) && (GostFachbereich.SPORT.hatKuerzel(ab3fach.kuerzel)))) {
			addFehler(GostBelegungsfehler.GOST30_ABI_15);
		}
	}


	/**
	 * Gesamtprüfung: Prüfe, ob eines der Abiturfächer mehrfach belegt wurde. Es ist nicht zulässig
	 * Abiturfächer mehrfach belegt zu haben.
	 */
	private void pruefeMehrfacheAbiturfaecher() {
		final @NotNull HashSet<GostAbiturFach> abiFaecher = new HashSet<>();
		final @NotNull List<AbiturFachbelegung> alleFachbelegungen = manager.getRelevanteFachbelegungen();
		for (final AbiturFachbelegung fachbelegung : alleFachbelegungen) {
			final GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach == null) {
				continue;
			}
			if (!abiFaecher.contains(abiturFach)) {
				abiFaecher.add(abiturFach);
				continue;
			}
			switch (abiturFach) {
				case LK1:
					addFehler(GostBelegungsfehler.GOST30_ABI_21);
					break;
				case LK2:
					addFehler(GostBelegungsfehler.GOST30_ABI_22);
					break;
				case AB3:
					addFehler(GostBelegungsfehler.GOST30_ABI_23);
					break;
				case AB4:
					addFehler(GostBelegungsfehler.GOST30_ABI_24);
					break;
				case AB5:
					addFehler(GostBelegungsfehler.GOST30_ABI_25);
					break;
			}
		}
	}


	private boolean pruefeSchriftlichkeitVorQ22(final AbiturFachbelegung belegung) {
		// Prüfe zunächst, ob das Fach selbst schriftlich belegt wurde
		if (manager.pruefeBelegungMitSchriftlichkeit(belegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) {
			return true;
		}
		// Prüfe weitere Fach-spezifische Fälle...
		final GostFach fach = manager.getFach(belegung);
		if (fach != null) {
			@NotNull List<AbiturFachbelegung> belegungen;
			// Fall Kurs-Wechsel bei bilingualen Fächern: Prüfe, ob ein Fach mit dem gleichen Statistik-Kürzel schriftlich belegt wurde
			belegungen = manager.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11))
					&& (manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12))
					&& (manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21))) {
				return true;
			}
			// Fall Kurs-Wechsel bei einem Religionskurs: Prüfe, ob in einer anderen Konfession ein Kurs belegt wurde
			if (GostFachbereich.RELIGION.hat(fach)
					&& ((Fach.getBySchluesselOrDefault(fach.kuerzel) == Fach.KR) || (Fach.getBySchluesselOrDefault(fach.kuerzel) == Fach.ER))) {
				belegungen = manager.getRelevanteFachbelegungenByFach(Fach.KR, Fach.ER);
				if ((manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11))
						&& (manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12))
						&& (manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21))) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Gesamtprüfung Punkte 76 und 77:
	 * Prüfe ob das 3. Abiturfach von Q1.1 bis Q2.2 schriftlich belegt wurde
	 *   und on das 4. Abiturfach von Q1.1 bis Q2.1 schriftlich und in Q2.2 mündlich belegt wurde
	 */
	private void pruefeSchriftlichkeitAB3undAB4() {
		final AbiturFachbelegung ab3 = (mapAbiturFachbelegungen == null) ? null : mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		if (ab3 != null) {
			if (!pruefeSchriftlichkeitVorQ22(ab3)) {
				addFehler(GostBelegungsfehler.GOST30_ABI_17);
			}
			if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab3, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q22)) {
				addFehler(GostBelegungsfehler.GOST30_ABI_12);
			}
		}
		final AbiturFachbelegung ab4 = (mapAbiturFachbelegungen == null) ? null : mapAbiturFachbelegungen.get(GostAbiturFach.AB4);
		if (ab4 != null) {
			if (!pruefeSchriftlichkeitVorQ22(ab4)) {
				addFehler(GostBelegungsfehler.GOST30_ABI_18);
			}
			if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab4, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.Q22)) {
				addFehler(GostBelegungsfehler.GOST30_ABI_13);
			}
		}
	}

	/**
	 * Gesamtprüfung:
	 * Prüfe, ob das 5. Abiturfach ...
	 */
	private void pruefeSchriftlichkeitAB5() {
		final AbiturFachbelegung ab5 = (mapAbiturFachbelegungen == null) ? null : mapAbiturFachbelegungen.get(GostAbiturFach.AB5);
		if (ab5 == null) {
			return;
		}
		// Prüfe, ob es sich um eine durchgängige Grundkursbelegung handelt
		if (manager.pruefeBelegungMitKursart(ab5, GostKursart.GK, GostHalbjahr.Q22)) {
			if (!pruefeSchriftlichkeitVorQ22(ab5)) {
				addFehler(GostBelegungsfehler.GOST30_ABI_29);
			}
			if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab5, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.Q22)) {
				addFehler(GostBelegungsfehler.GOST30_ABI_30);
			}
			return;
		}
		// Prüfe, ob es sich um einen Projektkurs handelt
		if (manager.pruefeBelegungMitKursart(ab5, GostKursart.PJK, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			final @NotNull Abi30BelegpruefungProjektkurse pruefungProjektkurse = ((@NotNull Abi30BelegpruefungProjektkurse) pruefungen_vorher[0]);
			if (pruefungProjektkurse.getProjektkurs() == null) {
				return;
			}
			final GostFach fach = manager.getFach(pruefungProjektkurse.getProjektkurs());
			if (fach == null) {
				return;
			}
			final @NotNull List<AbiturFachbelegung> leitfaecher = new ArrayList<>();
			final AbiturFachbelegung leitfach1 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			if (leitfach1 != null) {
				leitfaecher.add(leitfach1);
			}
			final AbiturFachbelegung leitfach2 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
			if (leitfach2 != null) {
				leitfaecher.add(leitfach2);
			}
			if (!leitfaecher.isEmpty()) {
				pruefeProjektkursReferenzfaecherAB5(leitfaecher);
			}
		}
	}


	/**
	 * Prüfung der Referenzfach-Belegungen bei einer Wahl eines Projektkurses als 5. Abiturfach
	 *
	 * @param referenzfaecher   die Belegegungen zu den Referenzfächern
	 */
	private void pruefeProjektkursReferenzfaecherAB5(final @NotNull List<AbiturFachbelegung> referenzfaecher) {
		final @NotNull Set<GostBelegungsfehler> fehler = new HashSet<>();
		for (final AbiturFachbelegung referenzfach : referenzfaecher) {
			// Prüfe die durchgängige Belegung des Referenzfaches in EF und Q1
			final boolean istDurchgaengig = manager.pruefeBelegung(referenzfach, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);

			// Prüfe die schriftliche Belegung des Referenzfaches in Q1
			final boolean istSchriftlichQ1 =
					manager.pruefeBelegungMitSchriftlichkeit(referenzfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12);

			// Prüfe, ob das Referenzfach als Abiturfach gewählt wurde. Das ist nicht zulässig.
			final boolean istAbifach = (GostAbiturFach.fromID(referenzfach.abiturFach) != null);

			// Wenn kein Fehler aufgetreten ist, dann wurde ein passendes Referenzfach gefunden und eine Wahl als AB5 ist möglich
			if (istDurchgaengig && istSchriftlichQ1 && !istAbifach) {
				return;
			}

			// Ansonsten ergänze den ersten relevanten Fehler...
			if (!istDurchgaengig) {
				fehler.add(GostBelegungsfehler.GOST30_ABI_26);
			} else if (!istSchriftlichQ1) {
				fehler.add(GostBelegungsfehler.GOST30_ABI_27);
			} else if (istAbifach) {
				fehler.add(GostBelegungsfehler.GOST30_ABI_28);
			}
		}
		// Gebe Fehler nur zurück, wenn nicht eines der Leitfächer als Grundlage für AB5 dienen kann
		for (final @NotNull GostBelegungsfehler f : fehler) {
			addFehler(f);
		}
	}


	/**
	 * Liefert die zugehörige Abitur-Fachbelegung zurück.
	 *
	 * @param abifach  die Art des Abifachs (1., 2., 3. oder 4. Fach)
	 *
	 * @return die Abitur-Fachbelegung oder null, falls es (noch) nicht festgelegt wurde
	 */
	public AbiturFachbelegung getAbiturfach(final GostAbiturFach abifach) {
		return (mapAbiturFachbelegungen == null) ? null : mapAbiturFachbelegungen.get(abifach);
	}


}
