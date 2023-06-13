package de.svws_nrw.core.abschluss.gost.belegpruefung;

import java.util.List;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche sich auf Kurszahlen und Wochenstunden beziehen.
 */
public final class KurszahlenUndWochenstunden extends GostBelegpruefung {

	/// Die Kurszahlen der einzelnen Halbjahre
	private ArrayMap<@NotNull GostHalbjahr, @NotNull ArrayMap<@NotNull GostKursart, @NotNull Integer>> kurszahlen;

	/// Die Kurszahlen der einzelnen Halbjahre
	private ArrayMap<@NotNull GostHalbjahr, @NotNull Integer> kurszahlenGrundkurse;

	/// Die Kurszahlen der einzelnen Halbjahre
	private ArrayMap<@NotNull GostHalbjahr, @NotNull Integer> kurszahlenLeistungskurse;

	/// Die Kurszahlen der anrechenbaren Kurse für die einzelnen Halbjahre
	private ArrayMap<@NotNull GostHalbjahr, @NotNull Integer> kurszahlenAnrechenbar;

	/// Die Kurszahlen der Einführungsphase
	private ArrayMap<@NotNull GostKursart, @NotNull Integer> kurszahlenEinfuehrungsphase;

	/// Die Kurszahlen der Qualifikationsphase
	private ArrayMap<@NotNull GostKursart, @NotNull Integer> kurszahlenQualifikationsphase;

	/// Die Gesamtzahl der Grundkurse der Qualifikationsphase (auch Zusatzkurse und ggf. Projektkurse, die zu keiner besonderen Lernleistung zählen)
	private int blockIAnzahlGrundkurse;

	/// Die Anzahl der belegten LK-Fächer (sollten 2 sein)
	private int anzahlLKFaecher;

	/// Die Gesamtzahl der Leistungskurse der Qualifikationsphase (sollten 8 sein)
	private int blockIAnzahlLeistungskurse;

	/// Die Gesamtzahl der anrechenbaren Kurse der Qualifikationsphase
	private int blockIAnzahlAnrechenbar;

	/// Die Anzahl der Wochenstunden in dem entsprechenden Halbjahr
	private ArrayMap<@NotNull GostHalbjahr, @NotNull Integer> wochenstunden;

	/// Die Anzahl der WochenStunden in der Einführungsphase
	private int wochenstundenEinfuehrungsphase;

	/// Die Anzahl der WochenStunden in der Qualifikationsphase
	private int wochenstundenQualifikationsphase;


	/**
	 * Erstellt eine neue Belegprüfung für die Kurszahlen.
	 *
	 * @param manager                 der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt           die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse    das Ergebnis für die Belegprüfung der Projektkurse
	 */
	public KurszahlenUndWochenstunden(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt, final @NotNull GostBelegpruefung pruefungProjektkurse) {
		super(manager, pruefungsArt, pruefungProjektkurse);
	}


	@Override
	protected void init() {
		kurszahlen = new ArrayMap<>(GostHalbjahr.values());
		kurszahlenGrundkurse = new ArrayMap<>(GostHalbjahr.values());
		kurszahlenLeistungskurse = new ArrayMap<>(GostHalbjahr.values());
		kurszahlenAnrechenbar = new ArrayMap<>(GostHalbjahr.values());
		kurszahlenEinfuehrungsphase = new ArrayMap<>(GostKursart.values());
		kurszahlenQualifikationsphase = new ArrayMap<>(GostKursart.values());
		blockIAnzahlGrundkurse = 0;
		anzahlLKFaecher = 0;
		blockIAnzahlLeistungskurse = 0;
		blockIAnzahlAnrechenbar = 0;
		wochenstunden = new ArrayMap<>(GostHalbjahr.values());
		wochenstundenEinfuehrungsphase = 0;
		wochenstundenQualifikationsphase = 0;
		final @NotNull Projektkurse projektkurse = ((@NotNull Projektkurse) pruefungen_vorher[0]);

		// Erzeuge zunächst Einträge mit 0 für die Kurszahlen und Wochenstunden in allen HashMaps
		final @NotNull GostKursart@NotNull[] kursarten = GostKursart.values();
		for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
			final @NotNull ArrayMap<@NotNull GostKursart, @NotNull Integer> kurszahlenHalbjahr = new ArrayMap<>(GostKursart.values());
			kurszahlen.put(halbjahr, kurszahlenHalbjahr);
			for (final GostKursart kursart : kursarten)
				kurszahlenHalbjahr.put(kursart, 0);
			kurszahlenGrundkurse.put(halbjahr, 0);
			kurszahlenLeistungskurse.put(halbjahr, 0);
			kurszahlenAnrechenbar.put(halbjahr, 0);
			wochenstunden.put(halbjahr, 0);
		}
		for (final GostKursart kursart : kursarten) {
			kurszahlenEinfuehrungsphase.put(kursart, 0);
			kurszahlenQualifikationsphase.put(kursart, 0);
		}

		// Zähle nun die einzelnen Kurse und die Wochenstunden...
		final @NotNull List<@NotNull AbiturFachbelegung> alleFachbelegungen = manager.getFachbelegungen();
		for (int i = 0; i < alleFachbelegungen.size(); i++) {
			final AbiturFachbelegung fachbelegung = alleFachbelegungen.get(i);
			final GostFach fach = manager.getFach(fachbelegung);
			boolean istLKFach = false;
			for (final AbiturFachbelegungHalbjahr fachbelegungHalbjahr : fachbelegung.belegungen) {
				if (fachbelegungHalbjahr == null)
					continue;

				// Prüfe, ob die Note ungenügend ist. Dann ist gilt der Kurs als nicht belegt
				if (AbiturdatenManager.istNullPunkteBelegungInQPhase(fachbelegungHalbjahr))
					continue;

				// Überspringe Sport-Kurse, die in diesem Halbjahr die Note "AT" beinhalten, bei der Zählung der Kursstunden
				// und der Wochenstunden. Der Schüler ist in diesem Halbjahr aufgrund eines Attestes von Sport befreit.
				final Note note = Note.fromKuerzel(fachbelegungHalbjahr.notenkuerzel);
				if (GostFachbereich.SPORT.hat(fach) && Note.ATTEST.equals(note))
					continue;

				// Bestimme Halbjahr und Kursart
				final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(fachbelegungHalbjahr.halbjahrKuerzel);
				if (halbjahr == null)
					continue;
				final GostKursart kursart = GostKursart.fromKuerzel(fachbelegungHalbjahr.kursartKuerzel);
				if (kursart == null)  // Dies kann z.B. bei einem Sportattest ("AT") der Fall sein.
					continue;

				// Für das Halbjahr
				ArrayMap<@NotNull GostKursart, @NotNull Integer> kurszahlenHalbjahr = kurszahlen.get(halbjahr);
				if (kurszahlenHalbjahr == null)
					kurszahlenHalbjahr = new ArrayMap<>(GostKursart.values());
				final Integer kurszahlAlt = kurszahlenHalbjahr.get(kursart);
				kurszahlenHalbjahr.put(kursart, kurszahlAlt == null ? 1 : kurszahlAlt + 1);

				// Für die Grundkurse
				if ((kursart == GostKursart.GK) || (halbjahr.istQualifikationsphase() && ((kursart == GostKursart.ZK)
						|| ((kursart == GostKursart.PJK) && (projektkurse.istAnrechenbar(fachbelegungHalbjahr)))))) {
					final Integer kurszahlGK = kurszahlenGrundkurse.get(halbjahr);
					kurszahlenGrundkurse.put(halbjahr, kurszahlGK == null ? 1 : kurszahlGK + 1);
					final Integer kurszahlAnrechenbar = kurszahlenAnrechenbar.get(halbjahr);
					kurszahlenAnrechenbar.put(halbjahr, kurszahlAnrechenbar == null ? 1 : kurszahlAnrechenbar + 1);
					if (halbjahr.istQualifikationsphase()) {
						blockIAnzahlGrundkurse++;
						blockIAnzahlAnrechenbar++;
					}
				}

				// Für die Leistungskurse
				if (halbjahr.istQualifikationsphase() && (kursart == GostKursart.LK)) {
					istLKFach = true;
					final Integer kurszahlLK = kurszahlenLeistungskurse.get(halbjahr);
					kurszahlenLeistungskurse.put(halbjahr, kurszahlLK == null ? 1 : kurszahlLK + 1);
					final Integer kurszahlAnrechenbar = kurszahlenAnrechenbar.get(halbjahr);
					kurszahlenAnrechenbar.put(halbjahr, kurszahlAnrechenbar == null ? 1 : kurszahlAnrechenbar + 1);
					blockIAnzahlLeistungskurse++;
					blockIAnzahlAnrechenbar++;
				}

				// Zähle die Wochenstunden
				int stunden = 0;
				switch (kursart.kuerzel) {
					case "GK": stunden = ((fach != null) && fach.istFremdSpracheNeuEinsetzend) ? 4 : 3; break;
					case "LK": stunden = 5; break;
					case "PJK": stunden = (fachbelegungHalbjahr.wochenstunden == 3) ? 3 : 2; break;
					case "VTF": stunden = 2; break;
					case "ZK": stunden = 3; break;
					default: stunden = 3; break;
				}
				final Integer wochenstundenAlt = wochenstunden.get(halbjahr);
				wochenstunden.put(halbjahr, wochenstundenAlt == null ? stunden : wochenstundenAlt + stunden);

				// Kurszahlen und Wochenstunden für die Einführungsphase und die Qualifikationsphase - hier werden ggf. auch unzulässige Belegungen gezählt
				if (halbjahr.istEinfuehrungsphase()) {
					final Integer kurszahlEF = kurszahlenEinfuehrungsphase.get(kursart);
					kurszahlenEinfuehrungsphase.put(kursart, kurszahlEF == null ? 1 : kurszahlEF + 1);
					wochenstundenEinfuehrungsphase += stunden;
				} else {
					final Integer kurszahlQ = kurszahlenQualifikationsphase.get(kursart);
					kurszahlenQualifikationsphase.put(kursart, kurszahlQ == null ? 1 : kurszahlQ + 1);
					wochenstundenQualifikationsphase += stunden;
				}
			}
			if (istLKFach)
				anzahlLKFaecher++;
		}
	}


	@Override
	protected void pruefeEF1() {
		// Führe die Belegprüfung für die EF1 durch
		pruefeGrundkurseEF1();
		pruefeWochenstundenEF1();
	}


	/**
	 * EF1-Prüfung Punkt 21:
	 * Prüfe, ob zu wenige Grundkurse (ohne Vertiefungskurse) in der EF belegt wurden,
	 * dh. weniger als 10 Kurse
	 */
	private void pruefeGrundkurseEF1() {
		if (kurszahlenGrundkurse == null)
			throw new NullPointerException();
		final Integer kurszahlGK = kurszahlenGrundkurse.get(GostHalbjahr.EF1);
		if ((kurszahlGK == null) || (kurszahlGK < 10))
			addFehler(GostBelegungsfehler.ANZ_10);
	}

	/**
	 * EF1-Prüfung Punkt 22:
	 * Prüfe, ob die Summe der Kursstunden in der EF.1 größer oder gleich 32 und kleiner oder gleich 36 ist.
	 */
	private void pruefeWochenstundenEF1() {
		if (wochenstunden == null)
			throw new NullPointerException();
		final Integer stunden = wochenstunden.get(GostHalbjahr.EF1);
		if ((stunden == null) || (stunden < 32) || (stunden > 36))
			addFehler(GostBelegungsfehler.ANZ_11_INFO);
	}





	@Override
	protected void pruefeGesamt() {
		// Führe die Belegprüfung für die gesamte Oberstufe durch
		pruefeGrundkurseEF();
		pruefeGrundkurseQ();
		pruefeLeistungskurse();
		pruefeVertiefungskurseEF();
		pruefeWochenstunden();
		pruefeVertiefungskurseQ();
		pruefeAnrechenbareKurse();
		pruefeKursstundenSummen();
	}


	/**
	 * Gesamtprüfung Punkt 58:
	 * Prüfe, ob zu wenige Grundkurse (ohne Vertiefungskurse) in der EF belegt wurden,
	 * dh. weniger als 10 Kurse
	 */
	private void pruefeGrundkurseEF() {
		if (kurszahlenGrundkurse == null)
			throw new NullPointerException();
		final Integer kurszahlGK_EF1 = kurszahlenGrundkurse.get(GostHalbjahr.EF1);
		final Integer kurszahlGK_EF2 = kurszahlenGrundkurse.get(GostHalbjahr.EF2);
		if ((kurszahlGK_EF1 == null) || (kurszahlGK_EF1 < 10) || (kurszahlGK_EF2 == null) || (kurszahlGK_EF2 < 10))
			addFehler(GostBelegungsfehler.ANZ_10);
	}


	/**
	 * Gesamtprüfung Punkt 59:
	 * Prüfe, ob in jedem Halbjahr die Summe der Kursstunden größer oder gleich 32 und kleiner oder gleich 36 ist.
	 */
	private void pruefeWochenstunden() {
		if (wochenstunden == null)
			throw new NullPointerException();
		for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
			final Integer stunden = wochenstunden.get(halbjahr);
			if ((stunden == null) || (stunden < 32) || (stunden > 36))
				addFehler(GostBelegungsfehler.ANZ_11_INFO);
		}
	}


	/**
	 * Gesamtprüfung Punkt 61:
	 * Prüfe, ob in den Halbjahren der Qualifikationsphase mindestens 7 Grundkurse belegt wurden.
	 * Dazu zählen auch Zusatzkurse sowie solche Projektkurse, die 2 Halbjahre belegt wurden
	 * und zu keiner besonderen Lernleistung zählen.
	 */
	private void pruefeGrundkurseQ() {
		if (kurszahlenGrundkurse == null)
			throw new NullPointerException();
		for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			final Integer kurszahlGK = kurszahlenGrundkurse.get(halbjahr);
			if ((kurszahlGK == null) || (kurszahlGK < 7))
				addFehler(GostBelegungsfehler.GKS_10);
		}
	}


	/**
	 * Gesamtprüfung Punkt 60 und 75:
	 * Wurden in der Qualifikationsphase in jedem Halbjahr zwei LKs belegt in insgesamt genau 2 Fächern.
	 */
	private void pruefeLeistungskurse() {
		if (anzahlLKFaecher != 2)
			addFehler(GostBelegungsfehler.LK_10);
		if (kurszahlenLeistungskurse == null)
			throw new NullPointerException();
		for (final GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			final Integer kurszahlLK = kurszahlenLeistungskurse.get(halbjahr);
			if (kurszahlLK != null) {
				if (kurszahlLK < 2)
					addFehler(GostBelegungsfehler.LK_10);
				else if (kurszahlLK > 2)
					addFehler(GostBelegungsfehler.LK_11);
			}
		}
	}



	/**
	 * Gesamtprüfung Punkt 62:
	 * Ist die Summe aller belegten Vertiefungskurse in der EF kleiner gleich 4?
	 */
	private void pruefeVertiefungskurseEF() {
		if (kurszahlenEinfuehrungsphase == null)
			throw new NullPointerException();
		final Integer kurszahlEF_VTF = kurszahlenEinfuehrungsphase.get(GostKursart.VTF);
		if ((kurszahlEF_VTF != null) && (kurszahlEF_VTF > 4))
			addFehler(GostBelegungsfehler.VF_10);
	}


	/**
	 * Gesamtprüfung Punkt 63:
	 * Ist die Summe aller belegten Vertiefungskurse in der Qualifikationsphase kleiner gleich 2?
	 */
	private void pruefeVertiefungskurseQ() {
		if (kurszahlenQualifikationsphase == null)
			throw new NullPointerException();
		final Integer kurszahlQ_VTF = kurszahlenQualifikationsphase.get(GostKursart.VTF);
		if ((kurszahlQ_VTF != null) && (kurszahlQ_VTF > 2))
			addFehler(GostBelegungsfehler.VF_11);
	}


	/**
	 * Gesamtprüfung Punkt 69:
	 * Ist die Anzahl anrechenbarer Kurse für Block I des Abiturs (Qualifikationsphase) größer gleich 38?
	 */
	private void pruefeAnrechenbareKurse() {
		if (blockIAnzahlAnrechenbar < 38)
			addFehler(GostBelegungsfehler.ANZ_12);
	}


	/**
	 * Gesamtprüfung Punkte 80-82:
	 * Prüfe, ob die Summe der durschnittlichen Kursstunden der 3 Jahre größer oder gleich 100 bzw. 102 ist
	 * und ob die durchschnittliche Summe der Kursstunden in der Einführungsphase under Qualifikationsphase
	 * größer oder gleich 34 ist.
	 */
	private void pruefeKursstundenSummen() {
		if (wochenstundenEinfuehrungsphase / 2.0 < 34.0)
			addFehler(GostBelegungsfehler.WST_20);
		if (wochenstundenQualifikationsphase / 4.0 < 34.0)
			addFehler(GostBelegungsfehler.WST_21);
		final double summeKursstundenDurchschnitte = (wochenstundenEinfuehrungsphase / 2.0) + (wochenstundenQualifikationsphase / 4.0) * 2.0;
		if (summeKursstundenDurchschnitte < 102) {
			if (summeKursstundenDurchschnitte < 100) {
				addFehler(GostBelegungsfehler.STD_10);
			} else {
				addFehler(GostBelegungsfehler.STD_11_INFO);
			}
		}
	}




	/**
	 * Gibt die Kurszahlen für das Halbjahr und die Kursart zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 * @param kursart    die Kursart
	 *
	 * @return die Kurszahlen
	 */
	public int getKurszahlen(final @NotNull GostHalbjahr halbjahr, final @NotNull GostKursart kursart) {
		if (kurszahlen == null)
			return 0;
		final ArrayMap<@NotNull GostKursart, @NotNull Integer> kurszahlenHalbjahr = kurszahlen.get(halbjahr);
		if (kurszahlenHalbjahr == null)
			return 0;
		final Integer kurszahl = kurszahlenHalbjahr.get(kursart);
		if (kurszahl == null)
			return 0;
		return kurszahl;
	}


	/**
	 * Gibt die Kurszahlen für die Grundkurse für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Kurszahlen
	 */
	public int getKurszahlenGrundkurse(final @NotNull GostHalbjahr halbjahr) {
		if (kurszahlenGrundkurse == null)
			return 0;
		final Integer kurszahl = kurszahlenGrundkurse.get(halbjahr);
		if (kurszahl == null)
			return 0;
		return kurszahl;
	}


	/**
	 * Gibt die Kurszahlen für die Leistungskurse für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Kurszahlen
	 */
	public int getKurszahlenLeistungskurse(final @NotNull GostHalbjahr halbjahr) {
		if (kurszahlenLeistungskurse == null)
			return 0;
		final Integer kurszahl = kurszahlenLeistungskurse.get(halbjahr);
		if (kurszahl == null)
			return 0;
		return kurszahl;
	}


	/**
	 * Gibt die Zahl der anrechenbaren Kurse für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Kurszahlen
	 */
	public int getKurszahlenAnrechenbar(final @NotNull GostHalbjahr halbjahr) {
		if (kurszahlenAnrechenbar == null)
			return 0;
		final Integer kurszahl = kurszahlenAnrechenbar.get(halbjahr);
		if (kurszahl == null)
			return 0;
		return kurszahl;
	}


	/**
	 * Gibt die Zahl der Kurse mit der angegebenen Kursart in der Einführungsphase zurück.
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die Kurszahlen
	 */
	public int getKurszahlenEinfuehrungsphase(final @NotNull GostKursart kursart) {
		if (kurszahlenEinfuehrungsphase == null)
			return 0;
		final Integer kurszahl = kurszahlenEinfuehrungsphase.get(kursart);
		if (kurszahl == null)
			return 0;
		return kurszahl;
	}


	/**
	 * Gibt die Zahl der Kurse mit der angegebenen Kursart in der Qualifikationsphase zurück.
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die Kurszahlen
	 */
	public int getKurszahlenQualifikationsphase(final @NotNull GostKursart kursart) {
		if (kurszahlenQualifikationsphase == null)
			return 0;
		final Integer kurszahl = kurszahlenQualifikationsphase.get(kursart);
		if (kurszahl == null)
			return 0;
		return kurszahl;
	}


	/**
	 * Gibt die Anzahl der Grundkurse für Block I zurück.
	 *
	 * @return die Anzahl der Grundkurse
	 */
	public int getBlockIAnzahlGrundkurse() {
		return blockIAnzahlGrundkurse;
	}


	/**
	 * Gibt die Anzahl der Leistungskurse für Block I zurück.
	 *
	 * @return die Anzahl der Leistungskurse
	 */
	public int getBlockIAnzahlLeistungskurse() {
		return blockIAnzahlLeistungskurse;
	}


	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I zurück.
	 *
	 * @return die Anzahl der anrechenbaren Kurse
	 */
	public int getBlockIAnzahlAnrechenbar() {
		return blockIAnzahlAnrechenbar;
	}


	/**
	 * Gibt die Anzahl der Wochenstunden für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr  das Halbjahr
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public int getWochenstunden(final @NotNull GostHalbjahr halbjahr) {
		if (wochenstunden == null)
			return 0;
		Integer stunden = wochenstunden.get(halbjahr);
		if (stunden == null)
			stunden = 0;
		return stunden;
	}


	/**
	 * Gibt die Anzahl der Wochenstunden für die Einführungsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public int getWochenstundenEinfuehrungsphase() {
		return wochenstundenEinfuehrungsphase;
	}


	/**
	 * Gibt die Anzahl der Wochenstunden für die Qualifikationsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public int getWochenstundenQualifikationsphase() {
		return wochenstundenQualifikationsphase;
	}


}
