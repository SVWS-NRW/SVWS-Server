package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFachUtils;
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
 * für die Gesamtprüfungen, welche in Bezug auf Projektkurse durchgeführt werden.
 */
public final class Abi29BelegpruefungProjektkurse extends GostBelegpruefung {

	/// Eine Vektor mit den Projektfächern, die belegt wurden. Dies sollte im Regelfall nur ein Fach sein, können aber ggf. bei einer gültigen Belegung bis zu drei Fächer sein
	private @NotNull List<AbiturFachbelegung> projektkursBelegung = new ArrayList<>();

	/// falls ein Projektkurs gültig gewählt wurde: Der Projektkurs, sonst: null
	private AbiturFachbelegung projektkurs;

	/// ein Vektor, welcher die anrechenbaren Halbjahre eines gültig angewählten Projektkurses beinhaltet
	private @NotNull List<GostHalbjahr> projektkursHalbjahre = new ArrayList<>();


	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi29BelegpruefungProjektkurse(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		projektkurs = null;
		projektkursBelegung = new ArrayList<>();
		projektkursHalbjahre = new ArrayList<>();

		// Bestimme die belegten Projektfächer
		final @NotNull List<AbiturFachbelegung> alleFachbelegungen = manager.getRelevanteFachbelegungen();
		for (final @NotNull AbiturFachbelegung fachbelegung : alleFachbelegungen) {
			if (manager.zaehleBelegung(fachbelegung) <= 0)
				continue;

			final GostFach fach = manager.getFach(fachbelegung);
			if ((fach != null) && GostFachUtils.istProjektkurs(fach)) {
				projektkursBelegung.add(fachbelegung);
			}
		}
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe auf Projektkurse - diese sind nicht in der EF erlaubt
		pruefeBelegungEF();
	}


	@Override
	protected void pruefeGesamt() {
		// Prüfe die Belegung des Projektkurses und der Leitfächer
		pruefeBelegungEF();
		pruefeBelegung();
		pruefeAufAnrechenbarenProjektkurs();
		pruefeBelegungLeitfaecher();

		// ist der Kurs eine besondere Lernleistung?
		if (manager.istProjektKursBesondereLernleistung())
			addFehler((projektkurs != null) ? GostBelegungsfehler.PF_16_INFO : GostBelegungsfehler.PF_15);
	}




	/**
	 * Prüft, ob ein Projektfach in der EF belegt wurde. Eine solche Belegung ist nicht zulässig.
	 */
	private void pruefeBelegungEF() {
		for (final AbiturFachbelegung fachbelegung : projektkursBelegung) {
			for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if (belegungHalbjahr == null)
					continue;
				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if ((halbjahr == GostHalbjahr.EF1) || (halbjahr == GostHalbjahr.EF2))
					addFehler(GostBelegungsfehler.PF_10);
			}
		}
	}


	/**
	 * Prüft, ob genau eine Projektkurs belegt wurde.
	 */
	private void pruefeBelegung() {
		if (projektkursBelegung.isEmpty())
			addFehler(GostBelegungsfehler.PF_21_2);
	}


	/**
	 * Prüft, ob ein anrechenbarer Projektkurs unter den belegten Projektfächern existiert. Es darf aber
	 * auch nur genau ein anrechenbarer Projektkurs existieren!
	 */
	private void pruefeAufAnrechenbarenProjektkurs() {
		// Prüfe ggf. auch mehrere Projektfächer, da abgebrochene Einzelbelegungen vorliegen können
		for (final AbiturFachbelegung fachbelegung : projektkursBelegung) {
			// Prüfe die einzelnen Halbjahresbelegungen der Projektfächer
			for (final AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if (belegungHalbjahr == null)
					continue;

				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr == null)
					continue;

				// Ignoriere fehlerhafte EF-Belegungen an dieser Stelle
				if ((halbjahr == GostHalbjahr.EF1) || (halbjahr == GostHalbjahr.EF2))
					continue;

				// Prüfe, ob der Projektkurs in der Q2 belegt wurde
				if ((halbjahr != GostHalbjahr.Q21) && (halbjahr != GostHalbjahr.Q22)) {
					addFehler(GostBelegungsfehler.PF_20_2);
					continue;
				}

				// Der Projektkurs ist nur anrechenbar, sofern das Fach im nachfolgenden Halbjahr belegt wurde.
				final GostHalbjahr nextHalbjahr = halbjahr.next();
				if (nextHalbjahr == null) {
					addFehler(GostBelegungsfehler.PF_18);
					continue;
				} else if (!manager.pruefeBelegung(fachbelegung, nextHalbjahr)) {
					addFehler(GostBelegungsfehler.PF_17_2);
					continue;
				}

				// Wurde bereits ein anderer Projektkurs in zwei aufeinanderfolgenden Jahren belegt?
				if (projektkurs != null) {
					addFehler(GostBelegungsfehler.PF_14);
					break;
				}

				// Speichere den anrechenbaren Projektkurs für spätere Prüfungen
				projektkurs = fachbelegung;
				projektkursHalbjahre.add(halbjahr);
				projektkursHalbjahre.add(nextHalbjahr);
				break;
			}
		}
	}



	/**
	 * Prüft die Belegung der Leitfächer
	 */
	private void pruefeBelegungLeitfaecher() {
		for (final AbiturFachbelegung fachbelegung : projektkursBelegung) {
			final GostFach fach = manager.getFach(fachbelegung);
			if (fach == null)
				continue;
			// Prüfe nun, ob genau ein Leitfach/Referenzfach belegt wurde
			final AbiturFachbelegung leitfach1 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			final AbiturFachbelegung leitfach2 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
			if ((leitfach1 == null) || (leitfach2 != null)) {
				addFehler(GostBelegungsfehler.PF_22_2);
				continue;
			}
			// Prüfe die durchgängige Belegung des Referenzfaches in EF und Q1
			if (!manager.pruefeBelegung(leitfach1, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
				addFehler(GostBelegungsfehler.PF_23_2);
				continue;
			}
			// Prüfe, ob die Fachdefinition des Leitfaches zulässig ist (eigentlich keine individuelle Belegprüfung)
			final GostFach lf = manager.getFach(leitfach1);
			if (lf == null) {
				addFehler(GostBelegungsfehler.PF_25);
				continue;
			}
			final Fach zf = Fach.getBySchluesselOrDefault(lf.kuerzel);
			if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf == Fach.PX) || (zf == Fach.VX)))
				addFehler(GostBelegungsfehler.PF_19);
		}
	}


	/**
	 * Gibt den belegten Projektkurs zurück, fall ein Kurs gültig belegt wurde.
	 *
	 * @return die Fachbelegung des Projektkurses oder null
	 */
	public AbiturFachbelegung getProjektkurs() {
		return projektkurs;
	}


	/**
	 * Gibt zurück, ob die angegebene Fachbelegung des Halbjahres eine Fachbelegung des
	 * angewählten Projektkurses ist und anrechenbar ist. Sollte sie Teil des Projektkurses
	 * sein, aber auch zu einer besonderen Lernleistung gehören, so ist sie nicht anrechenbar.
	 *
	 * @param fachbelegungHalbjahr   die Fachbelegung des Halbjahres
	 *
	 * @return true, wenn die Fachbelegung anrechenbar ist.
	 */
	public boolean istAnrechenbar(final AbiturFachbelegungHalbjahr fachbelegungHalbjahr) {
		if (fachbelegungHalbjahr == null)
			return false;
		if (GostKursart.fromKuerzel(fachbelegungHalbjahr.kursartKuerzel) != GostKursart.PJK)
			return false;
		final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(fachbelegungHalbjahr.halbjahrKuerzel);
		if ((projektkurs == null) || (projektkursHalbjahre.size() != 2) || (manager.istProjektKursBesondereLernleistung()))
			return false;
		return (halbjahr == projektkursHalbjahre.get(0)) || (halbjahr == projektkursHalbjahre.get(1));
	}


	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I des Abiturs zurück
	 *
	 * @return die Anzahl der anrechenbaren Kurse
	 */
	public int getAnrechenbareKurse() {
		if ((projektkurs == null) || (manager.istProjektKursBesondereLernleistung()))
			return 0;
		return 2;
	}

}
