package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafel;
import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.bk.BKGymBelegungsfehlerTyp;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Die abstrakte Klasse für die Belegprüfungen bei Bildungsgängen.
 */
public class BKGymBelegpruefung {

	/** Der Abiturdaten-Manager */
	protected final @NotNull BKGymAbiturdatenManager abidatenManager;

	/** Die Belegungsfehler, die für jede Stundentafel bei der Prüfung festgehalten werden. */
	private final @NotNull HashMap<BeruflichesGymnasiumStundentafel, List<BKGymBelegungsfehler>> mapBelegungsfehler = new HashMap<>();

	/** Die Liste von Belegungsfehlern der am besten passenden Stundentafel */
	private @NotNull List<BKGymBelegungsfehler> besteFehlerliste = new ArrayList<>();

	/** Flag ob neue Fehler hinzugekommen sind */
	private boolean dirty = false;


	/**
	 * Erzeugt eine neue Belegprüfung mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public BKGymBelegpruefung(final @NotNull BKGymAbiturdatenManager manager) {
		this.abidatenManager = manager;
	}


	/**
	 * Fügt einen Belegungsfehler zu der Belegprüfung hinzu.
	 *
	 * @param tafel       die Stundentafel
	 * @param fehlerTyp   der hinzuzufügende Belegungsfehlertyp
	 * @param params      die Parameter für den Belegungsfehlertyp
	 *
	 * @return true, falls ein Fehler vorliegt false, wenn nur ein Hinweis ausgegeben wurde.
	 */
	private boolean addFehler(final @NotNull BeruflichesGymnasiumStundentafel tafel, final @NotNull BKGymBelegungsfehlerTyp fehlerTyp, final Object... params) {
		final @NotNull BKGymBelegungsfehler fehler = new BKGymBelegungsfehler(fehlerTyp, params);
		final List<BKGymBelegungsfehler> fehlerliste = mapBelegungsfehler.get(tafel);
		if (fehlerliste != null && !fehlerliste.contains(fehler)) {
			fehlerliste.add(fehler);
			dirty = true;
		}
		return fehler.istFehler();
	}


	/**
	 * Ermittelt die Stundentafel mit den wenigsten Fehlern und gibt die zugehörigen Belegungsfehler aus
	 */
	private void ermittleBesteTafel() {
		if (dirty) {
			int minFehlerZahl = Integer.MAX_VALUE;
			for (final @NotNull List<BKGymBelegungsfehler> fehlerliste : mapBelegungsfehler.values()) {
				int fehlerZahl = 0;
				for (final @NotNull BKGymBelegungsfehler fehler : fehlerliste)
					fehlerZahl += fehler.wert;
				if (fehlerZahl < minFehlerZahl) {
					minFehlerZahl = fehlerZahl;
					besteFehlerliste = fehlerliste;
				}
			}
		}
		dirty = false;
	}


	/**
	 * Ermittelt die Stundentafel mit den wenigsten Fehlern und gibt die zugehörigen Belegungsfehler aus
	 *
	 * @return die Belegungsfehler der Stundentafel
	 */
	public @NotNull List<BKGymBelegungsfehler> getBelegungsfehler() {
		ermittleBesteTafel();
		return besteFehlerliste;
	}


	/**
	 * Gibt zurück, ob mindestens eine Stundentafel existiert, die keine "echten" Belegungsfehler hat. Warnungen und Hinweise werden toleriert.
	 *
	 * @return true, wenn kein "echter" Belegungsfehler vorliegt, und ansonsten false.
	 */
	public boolean istErfolgreich() {
		for (final @NotNull BKGymBelegungsfehler fehler : getBelegungsfehler())
			if (!fehler.istInfo() && fehler.wert > 0)
				return false;
		return true;
	}


	/**
	 * Die Methode wird zur Durchführung der Belegprüfung aufgerufen.
	 *
	 * Sie führt zuerst die allgemeinen Prüfungen aus, die für alle Anlagen des beruflichen Gymnasiums identisch sind.
	 */
	public void pruefe() {
		for (final @NotNull BeruflichesGymnasiumStundentafel tafel : abidatenManager.getStundentafelManager().getStundentafeln()) {
			// damit fehlerfreie Belegungen erkannt werden
			mapBelegungsfehler.put(tafel, new ArrayList<>());
			nichtBelegteHalbjahreHinweis(tafel);
			pruefeEineTafel(tafel);
		}
	}


	/**
	 * Führt die Belegprüfung für eine Stundentafel durch.
	 *
	 * @param tafel   die zu überprüfende Stundentafel
	 */
	private void pruefeEineTafel(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		boolean zweiteFremdspracheBelegt = true;
		boolean religionVollbelegt = false;
		final @NotNull BKGymFachbelegungManager fbManager = abidatenManager.getFachbelegungManager();
		final @NotNull BKGymFachbelegungZuStundentafelfachManager fb2TafelManager = fbManager.newFachbelegungZuStundentafelfachManager(BKGymStundentafelManager.getMaximalSortierung(tafel));
		pruefeAbiGrundkurse(tafel);
		for (final @NotNull BeruflichesGymnasiumStundentafelFach fach : tafel.faecher) {
			if (BKGymStundentafelManager.istZweiteFremdsprache(fach.fachbezeichnung))
				zweiteFremdspracheBelegt = pruefeBelegungZweiteFremdsprache(fb2TafelManager, tafel, fach);
			else if (BKGymStundentafelManager.istNeueFremdsprache(fach.fachbezeichnung))
				pruefeBelegungNeueFremdsprache(fb2TafelManager, tafel, fach);
			else if (BKGymStundentafelManager.istReligion(fach.fachbezeichnung))
				religionVollbelegt = pruefeBelegungReligion(fb2TafelManager, fach);
			else if (BKGymStundentafelManager.istWahlfach(fach.fachbezeichnung)) {
				if (!zweiteFremdspracheBelegt)
					pruefeBelegungFremdsprachenErsatzfach(fb2TafelManager, tafel);
				if (!religionVollbelegt)
					pruefeBelegungReligionErsatzfach(fb2TafelManager, tafel);
				pruefeBelegungWahlfach(fb2TafelManager, tafel, fach);
			} else
				pruefeBelegungFach(fb2TafelManager, tafel, fach);
		}
	}


	/**
	 * Prüfe auf korrekte Belegung des 3. und 4. Abiturfachs
	 *
	 * @param tafel   die zu prüfende Stundentafel
	 */
	private void pruefeAbiGrundkurse(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		final @NotNull BKGymFachbelegungManager fbManager = abidatenManager.getFachbelegungManager();
		// Bestimme das dritte Abiturfach
		final BKGymAbiturFachbelegung ab3 = fbManager.getAbiFachbelegung(GostAbiturFach.AB3);
		if (ab3 == null)
			addFehler(tafel, BKGymBelegungsfehlerTyp.AB_3);

		// Bestimme das vierte Abiturfach
		final BKGymAbiturFachbelegung ab4 = fbManager.getAbiFachbelegung(GostAbiturFach.AB4);
		if (ab4 == null)
			addFehler(tafel, BKGymBelegungsfehlerTyp.AB_4);

		// Prüfe ob ab3 und ab4 eine gültige Wahl ist
		final String ab3Bezeichnung = ab3 == null ? null : abidatenManager.getFaecherManager().getBezeichnungByFachID(ab3.fachID);
		final String ab4Bezeichnung = ab4 == null ? null : abidatenManager.getFaecherManager().getBezeichnungByFachID(ab4.fachID);
		if ((ab3Bezeichnung != null) && (ab4Bezeichnung != null)
				&& !abidatenManager.getStundentafelManager().pruefeAbiGrundkurswahl(tafel, ab3Bezeichnung, ab4Bezeichnung))
			addFehler(tafel, BKGymBelegungsfehlerTyp.AB_5, ab3Bezeichnung, ab4Bezeichnung, abidatenManager.getGliederung().name(), abidatenManager.getFachklassenschluessel());
	}


	/**
	 * Gibt Hinweise ins Log aus, wenn Halbjahre nicht bewertet wurden.
	 *
	 * @param tafel   die zu prüfende Stundentafel
	 */
	private void nichtBelegteHalbjahreHinweis(final @NotNull BeruflichesGymnasiumStundentafel tafel) {
		for (final GostHalbjahr hj : GostHalbjahr.values())
			if (!abidatenManager.istBewertet(hj))
				addFehler(tafel, BKGymBelegungsfehlerTyp.HJ_1_INFO, hj.kuerzel);
	}


	/**
	 * Führt die Belegung für die zweite Fremdsprache durch.
	 * Wenn keine zweite Fremdsprache belegt werden kann, wird das als false mitgeteilt, damit
	 * später geprüft wird, ob ein Ersatzfach belegt wurde. Das muss direkt vor der Prüfung des Wahlfachs durchgeführt werden
	 * und nach der Prüfung eines Ersatzfaches für Religion, da hier die möglichen Ersatzfächer eingeschränkt sind.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die zu überprüfende Stundentafel
	 * @param fach            das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn die Belegung erfolgreich war, sonst false
	 */
	private boolean pruefeBelegungZweiteFremdsprache(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager,
			@NotNull final BeruflichesGymnasiumStundentafel tafel, @NotNull final BeruflichesGymnasiumStundentafelFach fach) {
		final String bezeichnerFremdsprache = abidatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (bezeichnerFremdsprache == null)
			return false;
		final @NotNull BeruflichesGymnasiumStundentafelFach fachFremdsprache = copyStundentafelFach(fach, bezeichnerFremdsprache);
		return pruefeBelegungFach(fb2TafelManager, tafel, fachFremdsprache);
	}


	/**
	 * Führt die Belegung für die neue Fremdsprache durch.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel   die zu überprüfende Stundentafel
	 * @param fach    das zu prüfende Fach der Stundentafel
	 */
	private void pruefeBelegungNeueFremdsprache(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager,
			@NotNull final BeruflichesGymnasiumStundentafel tafel, @NotNull final BeruflichesGymnasiumStundentafelFach fach) {
		final String bezeichnerFremdsprache = abidatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (bezeichnerFremdsprache == null) {
			addFehler(tafel, BKGymBelegungsfehlerTyp.ST_4, fach.fachbezeichnung);
			return;
		}
		final @NotNull BeruflichesGymnasiumStundentafelFach fachFremdsprache = copyStundentafelFach(fach, bezeichnerFremdsprache);
		pruefeBelegungFach(fb2TafelManager, tafel, fachFremdsprache);
	}


	/**
	 * Führt die Belegung für das Ersatzfach der zweiten Fremdsprache durch. Dies ist beliebig muss aberfür Religion durch.
	 * für alle vier Halbjahre belegt werden.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel      die zu überprüfende Stundentafel
	 */
	private void pruefeBelegungFremdsprachenErsatzfach(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager,
			@NotNull final BeruflichesGymnasiumStundentafel tafel) {
		final @NotNull BKGymStundentafelManager stManager = abidatenManager.getStundentafelManager();
		// hole das Fach der zweiten Fremdsprache aus der Stundentafel
		final BeruflichesGymnasiumStundentafelFach fachZweiteFremdsprache =
				stManager.getFachByTafelAndBezeichnung(tafel, BKGymStundentafelManager.ZWEITE_FREMDSPRACHE);
		if (fachZweiteFremdsprache == null)
			throw new DeveloperNotificationException("Das Fach \"Zweite Fremdsprache\" fehlt in der Stundentafel.");
		for (final @NotNull String ersatzfachBezeichnung : fb2TafelManager.getFachbezeichnungenFreierBelegungen()) {
			final @NotNull BeruflichesGymnasiumStundentafelFach ersatzfach = copyStundentafelFach(fachZweiteFremdsprache, ersatzfachBezeichnung);
			if (fb2TafelManager.belegeErsatzfachVomEndeHer(ersatzfach))
				break;
		}
		pruefeStundenumfang(fb2TafelManager, tafel, fachZweiteFremdsprache);
	}


	/**
	 * Führt die Belegung für das Fach Religion durch.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param fach    das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn die Belegung erfolgreich war, sonst false
	 */
	private static boolean pruefeBelegungReligion(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager,
			@NotNull final BeruflichesGymnasiumStundentafelFach fach) {
		fb2TafelManager.belegeFach(fach);
		return fb2TafelManager.istVollbelegt(fach);
	}


	/**
	 * Führt die Belegung für das Ersatzfach für Religion durch. Dies ist beliebig muss aber
	 * für alle vier Halbjahre belegt werden.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel      die zu überprüfende Stundentafel
	 */
	private void pruefeBelegungReligionErsatzfach(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager, @NotNull final BeruflichesGymnasiumStundentafel tafel) {
		final @NotNull BKGymStundentafelManager stManager = abidatenManager.getStundentafelManager();
		// hole das Fach Religion aus der Stundentafel
		final BeruflichesGymnasiumStundentafelFach fachReligion =
				stManager.getFachByTafelAndBezeichnung(tafel, BKGymStundentafelManager.RELIGION);
		if (fachReligion == null)
			throw new DeveloperNotificationException("Das Fach " + BKGymStundentafelManager.RELIGION + " fehlt in der Stundentafel.");
		for (final @NotNull String ersatzfachBezeichnung : BKGymStundentafelManager.ERSATZ_FUER_RELIGION) {
			final @NotNull BeruflichesGymnasiumStundentafelFach ersatzfach = copyStundentafelFach(fachReligion, ersatzfachBezeichnung);
			if (fb2TafelManager.belegeErsatzfach(ersatzfach))
				break;
		}
		pruefeStundenumfang(fb2TafelManager, tafel, fachReligion);
	}


	/**
	 * Führt die Belegung für das Wahlfach durch.
	 *
	 * @param fb2TafelManager   der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 */
	private void pruefeBelegungWahlfach(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager, @NotNull final BeruflichesGymnasiumStundentafel tafel,
			@NotNull final BeruflichesGymnasiumStundentafelFach fach) {
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase())
			fb2TafelManager.belegeBeliebigesFachFuerHalbjahr(hj, fach);
		pruefeStundenumfang(fb2TafelManager, tafel, fach);
	}


	/**
	 * Führt die Belegung des Fachs aus dem Pool der noch nicht verwendeten Belegungen durch.
	 * Dabei wird die Stundenumfang überprüft und Fehler werden in der Tafel eingetragen.
	 *
	 * @param fb2TafelManager   der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn die Belegung erfolgreich war, sonst false
	 */
	private boolean pruefeBelegungFach(final @NotNull BKGymFachbelegungZuStundentafelfachManager fb2TafelManager, final @NotNull BeruflichesGymnasiumStundentafel tafel,
			final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		fb2TafelManager.belegeFach(fach);
		final boolean resultStundenumfang = pruefeStundenumfang(fb2TafelManager, tafel, fach);
		final boolean resultKursart = pruefeKursart(tafel, fach);
		final boolean resultSchriftlich = pruefeSchriftlich(fb2TafelManager, tafel, fach);
		return resultStundenumfang && resultKursart && resultSchriftlich;
	}


	/**
	 * Prüft, ob der Stundenumfang einer Stundentafelposition erfüllt wird.
	 *
	 * @param fb2TafelManager   der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn der Stundenumfang ausreichend ist, sonst false
	 */
	private boolean pruefeStundenumfang(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager,
			final @NotNull BeruflichesGymnasiumStundentafel tafel, final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		boolean success = true;
		int summeTafel = 0;
		int summeBelegung = 0;
		boolean unterbelegung = false;
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
			final int belegteStunden = fb2TafelManager.getBelegteStundenByHalbjahrAndFach(hj, fach);
			summeTafel += fach.stundenumfang[hj.id];
			summeBelegung += belegteStunden;
			if (belegteStunden == -1) {
				if (fach.stundenumfang[hj.id] > 0) {
					// Es ist eine Belegung vorhanden, aber keine Note
					success = !addFehler(tafel, BKGymBelegungsfehlerTyp.ST_2, fach.fachbezeichnung, hj.kuerzel) && success;
					unterbelegung = true;
				}
			} else if (belegteStunden < fach.stundenumfang[hj.id]) {
				unterbelegung = true;
				if (belegteStunden == 0)
					// Es ist keine Belegung vorhanden
					success = !addFehler(tafel, BKGymBelegungsfehlerTyp.ST_6, fach.fachbezeichnung, hj.kuerzel) && success;
			}
		}

		if (summeTafel > summeBelegung)
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.ST_3, fach.fachbezeichnung) && success;

		if (unterbelegung)
			success = !addFehler(tafel, BKGymBelegungsfehlerTyp.ST_5_INFO, fach.fachbezeichnung) && success;
		return success;
	}


	/**
	 * Prüft d, ob das Fach als Leistungskurs belegt wurde, wenn dies in der Stundentafel gefordert ist.
	 *
	 * @param tafel             die zu überprüfende Stundentafel
	 * @param fach              das zu prüfende Fach der Stundentafel
	 *
	 * @return true, wenn in Stundentafel und Belegung die Kursarten zueinander passen, sonst false
	 */
	@SuppressWarnings("java:S6916")
	private boolean pruefeKursart(@NotNull final BeruflichesGymnasiumStundentafel tafel, @NotNull final BeruflichesGymnasiumStundentafelFach fach) {
		final BKGymAbiturFachbelegung fachBelegung = abidatenManager.getFachbelegungManager().getFachbelegungByBezeichnung(fach.fachbezeichnung);
		if (fachBelegung == null)
			return !BKGymStundentafelManager.brauchtBelegungInQPhase(fach);
		final @NotNull String kursartBelegung = fachBelegung.letzteKursart == null ? "" : fachBelegung.letzteKursart;
		int lkNummerTafel = 0;
		int lkNummerBelegung = 0;
		if (fach.abifach != null)
			lkNummerTafel = fach.abifach;
		if (fachBelegung.abiturFach != null)
			lkNummerBelegung = fachBelegung.abiturFach;
		switch (fach.kursart) {
			case "GK" -> {
					if ("LK".equals(kursartBelegung))
						return !addFehler(tafel, BKGymBelegungsfehlerTyp.LK_3, fach.fachbezeichnung);
				}
			case "LK" -> {
				if (!"LK".equals(kursartBelegung) || (lkNummerTafel != lkNummerBelegung))
					return !addFehler(tafel, lkNummerTafel == 1 ? BKGymBelegungsfehlerTyp.LK_1 : BKGymBelegungsfehlerTyp.LK_2, fach.fachbezeichnung);
				}
			default ->
				throw new DeveloperNotificationException("Ungültige Kursart '" + fach.kursart + "' in der Stundentafel.");
		}
		return true;
	}


	/**
	 * Prüft, ob die Schriftlichkeit der Fächer korrekt erfüllt ist.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel         die Stundentafel der Anlage
	 * @param fach          das zu prüfende Fach aus der Stundentafel
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlich(@NotNull final BKGymFachbelegungZuStundentafelfachManager fb2TafelManager,
			final @NotNull BeruflichesGymnasiumStundentafel tafel, final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		boolean success;
		final BKGymAbiturFachbelegung fachBelegung = abidatenManager.getFachbelegungManager().getFachbelegungByBezeichnung(fach.fachbezeichnung);
		if (fachBelegung == null)
			return true;
		success = pruefeSchriftlichEF(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.EF1);
		success = pruefeSchriftlichEF(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.EF2) && success;
		success = pruefeSchriftlichQ1(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q11) && success;
		success = pruefeSchriftlichQ1(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q12) && success;
		success = pruefeSchriftlichQ2(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q21) && success;
		success = pruefeSchriftlichQ2(fb2TafelManager, tafel, fach, fachBelegung, GostHalbjahr.Q22) && success;
		return success;
	}


	/**
	 * In der EF muss in mindestens vier Fächern, in den LK-Fächern, Deutsch, Mathematik und Fremdsprachen in jedem Fall
	 * Da es die LK-Kombination Mathe-Deutsch nicht gibt, sind mindestens vier Fächer gegeben, wenn die obligatorischen
	 * Klausurfächer geprüft sind.
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die Stundentafel der Anlage
	 * @param fachBelegung    die Fachbelegung zur Halbjahresbelegung
	 * @param fach            das zu prüfende Fach aus der Stundentafel
	 * @param hj              das Oberstufenhalbjahr
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlichEF(final @NotNull BKGymFachbelegungZuStundentafelfachManager fb2TafelManager, final @NotNull BeruflichesGymnasiumStundentafel tafel,
			@NotNull final BeruflichesGymnasiumStundentafelFach fach, @NotNull final BKGymAbiturFachbelegung fachBelegung, final @NotNull GostHalbjahr hj) {
		if (fb2TafelManager.getSchriftlichBelegt(hj, fach))
			return true;
		if ("Deutsch".equals(fach.fachbezeichnung) || "Mathematik".equals(fach.fachbezeichnung))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_1_INFO, fach.fachbezeichnung, hj.kuerzel);
		if ((fachBelegung.abiturFach != null) && (fachBelegung.abiturFach <= 2))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_2, fach.fachbezeichnung, hj.kuerzel);
		if (abidatenManager.istFremdsprachenbelegung(fachBelegung))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_3_INFO, fach.fachbezeichnung, hj.kuerzel);
		return true;
	}


	/**
	 * In der Q1 müssen allen Abiturfächer schriftlich belegt sein. Deutsch, Mathematik,
	 * Fremdsprachen und die Fächer der Berufsabschlussprüfung in jedem Fall
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die Stundentafel der Anlage
	 * @param fachBelegung    die Fachbelegung zur Halbjahresbelegung
	 * @param fach            das zu prüfende Fach aus der Stundentafel
	 * @param hj              das Oberstufenhalbjahr
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlichQ1(final @NotNull BKGymFachbelegungZuStundentafelfachManager fb2TafelManager, final @NotNull BeruflichesGymnasiumStundentafel tafel,
			@NotNull final BeruflichesGymnasiumStundentafelFach fach, @NotNull final BKGymAbiturFachbelegung fachBelegung, final @NotNull GostHalbjahr hj) {
		if (fb2TafelManager.getSchriftlichBelegt(hj, fach))
			return true;
		if ("Deutsch".equals(fach.fachbezeichnung) || "Mathematik".equals(fach.fachbezeichnung))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_1, fach.fachbezeichnung, hj.kuerzel);
		if ((fachBelegung.abiturFach != null) && (fachBelegung.abiturFach <= 4))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_4, fach.fachbezeichnung, hj.kuerzel);
		if (abidatenManager.istFremdsprachenbelegung(fachBelegung))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_3, fach.fachbezeichnung, hj.kuerzel);
		return true;
	}


	/**
	 * In der Q2 müssen das erste bis dritte Abiturfach schriftlich belegt sein. Deutsch, Mathematik,
	 * Nur in der Q21 auch die Fremdsprachen
	 *
	 * @param fb2TafelManager der Manager für die Zuordnung der Fachbelegungen zu Stundentafelfächern
	 * @param tafel           die Stundentafel der Anlage
	 * @param fachBelegung    die Fachbelegung zur Halbjahresbelegung
	 * @param fach            das zu prüfende Fach aus der Stundentafel
	 * @param hj              das Oberstufenhalbjahr
	 *
	 * @return true, wenn die Prüfung keinen Fehler entdeckt, sonst false
	 */
	private boolean pruefeSchriftlichQ2(final @NotNull BKGymFachbelegungZuStundentafelfachManager fb2TafelManager, final @NotNull BeruflichesGymnasiumStundentafel tafel,
			@NotNull final BeruflichesGymnasiumStundentafelFach fach, @NotNull final BKGymAbiturFachbelegung fachBelegung, final @NotNull GostHalbjahr hj) {
		if (fb2TafelManager.getSchriftlichBelegt(hj, fach))
			return true;
		if ((fachBelegung.abiturFach != null) && (fachBelegung.abiturFach <= 3))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_4, fach.fachbezeichnung, hj.kuerzel);
		if ((GostHalbjahr.Q21 == hj) && abidatenManager.istFremdsprachenbelegung(fachBelegung))
			return !addFehler(tafel, BKGymBelegungsfehlerTyp.KL_3, fach.fachbezeichnung, hj.kuerzel);
		return true;
	}


	/**
	 * Hilfsmethode. Erstellt eine Kopie des Stundentafelfaches mit einer neuen Fachbezeichnung.
	 *
	 * @param fach              das zu kopierende Fach
	 * @param fachbezeichnung   die neue Fachbezeichnung
	 *
	 * @return die Kopie des Faches mit der neuen Fachbezeichnung
	 */
	private static @NotNull BeruflichesGymnasiumStundentafelFach copyStundentafelFach(final @NotNull BeruflichesGymnasiumStundentafelFach fach,
			final @NotNull String fachbezeichnung) {
		final @NotNull BeruflichesGymnasiumStundentafelFach copy = new BeruflichesGymnasiumStundentafelFach();
		copy.fachbezeichnung = fachbezeichnung;
		copy.sortierung = fach.sortierung;
		copy.abifach = fach.abifach;
		copy.kursart = fach.kursart;
		copy.stundenumfang = fach.stundenumfang;
		copy.zeugnisbereich = fach.zeugnisbereich;
		return copy;
	}
}
