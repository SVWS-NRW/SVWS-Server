package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

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
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
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
public final class Abi30BelegpruefungProjektkurse extends GostBelegpruefung {

	/** Eine Vektor mit den Projektfächern, die belegt wurden. Dies sollte im Regelfall nur ein Fach sein, können aber ggf. bei einer gültigen Belegung bis zu drei Fächer sein */
	private @NotNull List<AbiturFachbelegung> projektkursBelegung = new ArrayList<>();

	/** falls ein Projektkurs gültig gewählt wurde: Der Projektkurs, sonst: null */
	private AbiturFachbelegung projektkurs = null;

	/** ein Vektor, welcher die anrechenbaren Halbjahre eines gültig angewählten Projektkurses beinhaltet */
	private @NotNull List<GostHalbjahr> projektkursHalbjahre = new ArrayList<>();


	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi30BelegpruefungProjektkurse(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
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
			if ((fach != null) && GostFachUtils.istProjektkurs(fach))
				projektkursBelegung.add(fachbelegung);
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
		if (projektkurs != null)
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
		if (projektkursBelegung.isEmpty()) {
			addFehler(GostBelegungsfehler.PF_21_2);
			return;
		}

		if (projektkursBelegung.size() > 1) {
			addFehler(GostBelegungsfehler.PF_14);
			return;
		}

		final AbiturFachbelegung fachbelegung = projektkursBelegung.get(0);
		if (fachbelegung == null)
			return;

		// Prüfe auf fehlerhafte Belegungen in den Halbjahren der Q1
		if ((fachbelegung.belegungen[GostHalbjahr.Q11.id] != null) || (fachbelegung.belegungen[GostHalbjahr.Q12.id] != null)) {
			addFehler(GostBelegungsfehler.PF_20_2);
			return;
		}

		// Prüfe auf Belegungen in beiden Halbjahren der Q2
		if ((fachbelegung.belegungen[GostHalbjahr.Q21.id] == null) || (fachbelegung.belegungen[GostHalbjahr.Q22.id] == null)) {
			addFehler(GostBelegungsfehler.PF_20_2);
			return;
		}

		projektkurs = fachbelegung;
		projektkursHalbjahre.add(GostHalbjahr.Q21);
		projektkursHalbjahre.add(GostHalbjahr.Q22);
	}


	/**
	 * Prüft die Belegung der Leitfächer
	 */
	private void pruefeBelegungLeitfaecher() {
		if (projektkurs == null)
			return;
		final GostFach fach = manager.getFach(projektkurs);
		if (fach == null)
			return;
		// Prüfe nun, ob genau ein Leitfach/Referenzfach belegt wurde
		final AbiturFachbelegung leitfach1 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
		final AbiturFachbelegung leitfach2 = manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
		if ((leitfach1 == null) && (leitfach2 == null)) {
			addFehler(GostBelegungsfehler.PF_22_2);
			return;
		}

		// Prüfe, ob die Belegung des ersten Leitfaches als Referenzfach geeignet ist
		boolean hatReferenzfach1Belegung = false;
		boolean hatReferenzfach1BelegungSchriftlich = false;
		if (leitfach1 != null) {
			// Prüfe, ob die Fachdefinition des Leitfaches zulässig ist (eigentlich keine individuelle Belegprüfung)
			final GostFach lf = manager.getFach(leitfach1);
			if (lf == null) {
				addFehler(GostBelegungsfehler.PF_25);
				return;
			}
			final Fach zf = Fach.getBySchluesselOrDefault(lf.kuerzel);
			if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf == Fach.PX) || (zf == Fach.VX)))
				addFehler(GostBelegungsfehler.PF_19);

			// Prüfe die Belegung des Referenzfaches in EF und Q1 und die Schriftlichkeit in der Q1
			hatReferenzfach1Belegung = manager.pruefeBelegung(leitfach1, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
			hatReferenzfach1BelegungSchriftlich = manager.pruefeBelegungMitSchriftlichkeit(leitfach1, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12);
		}

		// Prüfe, ob die Belegung des zweiten Leitfaches als Referenzfach geeignet ist
		boolean hatReferenzfach2Belegung = false;
		boolean hatReferenzfach2BelegungSchriftlich = false;
		if (leitfach2 != null) {
			// Prüfe, ob die Fachdefinition des Leitfaches zulässig ist (eigentlich keine individuelle Belegprüfung)
			final GostFach lf = manager.getFach(leitfach2);
			if (lf == null) {
				addFehler(GostBelegungsfehler.PF_25);
				return;
			}
			final Fach zf = Fach.getBySchluesselOrDefault(lf.kuerzel);
			if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf == Fach.PX) || (zf == Fach.VX)))
				addFehler(GostBelegungsfehler.PF_19);

			// Prüfe die Belegung des Referenzfaches in EF und Q1 und die Schriftlichkeit in der Q1
			hatReferenzfach2Belegung = manager.pruefeBelegung(leitfach2, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
			hatReferenzfach2BelegungSchriftlich = manager.pruefeBelegungMitSchriftlichkeit(leitfach2, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12);
		}

		// Prüfe, ob einer der beiden Referenzfächer belegt wurde
		if (!hatReferenzfach1Belegung && !hatReferenzfach2Belegung) {
			addFehler(GostBelegungsfehler.PF_23_2);
			return;
		}

		if ((!hatReferenzfach1Belegung || !hatReferenzfach1BelegungSchriftlich)
				&& (!hatReferenzfach2Belegung || !hatReferenzfach2BelegungSchriftlich))
			addFehler(GostBelegungsfehler.PF_24_2);
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
