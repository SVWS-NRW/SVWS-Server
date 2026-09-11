package de.svws_nrw.core.utils.uvblockung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Lehrkraft-Objekt für schnelle, dynamische Manipulationen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenLehrkraft {

	/** Ein Logger für Debug-Zwecke. */
	final @NotNull Logger log;

	/** Der Index im internen Array. */
	final int interneID;

	/** Die externe ID der DB/GUI. */
	final long uvID;

	/** Der Pflichtstunden-Soll. */
	final double stundenSoll;

	/** Die Summe aller Entlastungen. */
	final double stundenAnrechnung;

	/** Die Summe aller Stunden der zugeordneten Lerngruppen. */
	double istStundensummeLerngruppen;

	/** Die aktuelle Anzahl an Stunden in denen die Lehrkraft in der jeweiligen Klasse unterrichtet. */
	final @NotNull double @NotNull [] istStundenProKlasse;

	/** Die aktuelle Definition, ob die Lehrkraft in der Klasse Klassenlehrer ist. */
	final @NotNull boolean @NotNull [] istLeitung1InKlasse;

	/** Die aktuelle Definition, ob die Lehrkraft in der Klasse stellv. Klassenlehrer ist. */
	final @NotNull boolean @NotNull [] istLeitung2InKlasse;

	/** Die aktuelle Anzahl an Klassenlehrer-Einsätzen (Leitung 1). */
	int istAnzahlLeitung1;

	/** Die aktuelle Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2). */
	int istAnzahlLeitung2;

	/** Die aktuelle Anzahl an Lerngruppen, denen diese Lehrkraft zugeordnet ist. */
	int istAnzahlLerngruppen;

	/** Die aktuelle Anzahl an Korrekturen dieser Lehrkraft. */
	int istAnzahlKorrekturen;

	/** Die aktuelle Anzahl an zugeordneten Lerngruppen pro Fach. */
	final @NotNull int @NotNull [] istFachZuLerngruppenAnzahl;

	/** Die aktuelle Anzahl an Stunden, die diese Lehrkraft im jeweiligen Jahrgang unterrichtet. */
	final @NotNull double @NotNull [] istStundenProJahrgang;

	/** Die aktuelle Anzahl an verschiedenen Jahrgängen in denen diese Lehrkraft unterrichtet. */
	int istJahrgangAnzahl;

	/** Die aktuelle Anzahl an Lerngruppen-Zuordnungen im Zeitslot [Wochentag][Stunde][Wochentyp] (Regel 17). */
	final @NotNull int @NotNull [] @NotNull [] @NotNull [] istWochentagStundeWochentypCounter;

	/** Die Menge aller Regel-Objekte die sich auf diese Lehrkraft beziehen. */
	final @NotNull List<UvAlgorithmusDynDatenRegel> regeln;


	/**
	 * Der Konstruktor.
	 *
	 * @param index               Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param log                 Ein Logger für Debug-Zwecke.
	 * @param man                 Der {@link UvManager}.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt}.
	 * @param uvLehrer            Das {@link UvLehrer}-Objekt der DB/GUI.
	 */
	public UvAlgorithmusDynDatenLehrkraft(
			final int index,
			final @NotNull Logger log,
			final @NotNull UvManager man,
			final @NotNull UvPlanungsabschnitt planungsabschnitt,
			final @NotNull UvLehrer uvLehrer) {

		this.log = log;
		this.interneID = index;
		this.uvID = uvLehrer.id;

		final Double dStundenSoll = man.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(uvLehrer, planungsabschnitt);
		this.stundenSoll = (dStundenSoll == null) ? 0.0 : dStundenSoll;
		this.stundenAnrechnung = man.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(uvLehrer, planungsabschnitt);
		this.istStundensummeLerngruppen = 0.0;
		final @NotNull List<UvKlasse> uvKlassenMenge = man.klasseGetMengeByPlanungsabschnitt(planungsabschnitt);
		this.istStundenProKlasse = new double[uvKlassenMenge.size()];
		this.istLeitung1InKlasse = new boolean[uvKlassenMenge.size()];
		this.istLeitung2InKlasse = new boolean[uvKlassenMenge.size()];
		this.istAnzahlLeitung1 = 0;
		this.istAnzahlLeitung2 = 0;
		this.istAnzahlLerngruppen = 0;
		this.istAnzahlKorrekturen = 0;
		final @NotNull List<JahrgangsDaten> uvJahrgangMenge = man.jahrgangsdatenGetMenge();
		this.istStundenProJahrgang = new double[uvJahrgangMenge.size()];
		this.istJahrgangAnzahl = 0;

		final int nFaecher = man.fachGetMengeAsList().size();
		this.istFachZuLerngruppenAnzahl = new int[nFaecher];
		for (int i = 0; i < nFaecher; i++) {
			istFachZuLerngruppenAnzahl[i] = 0;
		}

		final int maxWochentag = man.zeitrasterGetMaxWochentag();
		final int maxStunde = man.zeitrasterGetMaxStunde();
		final int maxWochentyp = man.zeitrasterGetWochentypmodell();
		this.istWochentagStundeWochentypCounter = new int[maxWochentag + 1][maxStunde + 1][maxWochentyp + 1];
		for (int iWochentag = 0; iWochentag <= maxWochentag; iWochentag++) {
			for (int iStunde = 0; iStunde <= maxStunde; iStunde++) {
				for (int iWochentyp = 0; iWochentyp <= maxWochentyp; iWochentyp++) {
					this.istWochentagStundeWochentypCounter[iWochentag][iStunde][iWochentyp] = 0;
				}
			}
		}

		this.regeln = new ArrayList<>();
	}


	@Override
	public @NotNull String toString() {
		return ("UvAlgorithmusDynDatenLehrkraft { interneID=%d, uvID=%d }")
				.formatted(interneID, uvID);
	}


	/**
	 * Erhöht die Leitung-1-Anzahl der Lehrkraft und aktualisiert den Leitung-1-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung1Inc(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (istLeitung1InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist bereits Leitung1 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung1++;
		istLeitung1InKlasse[klasse.interneID] = true;
	}


	/**
	 * Verringert die Leitung-1-Anzahl der Lehrkraft und aktualisiert den Leitung-1-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung1Dec(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (!istLeitung1InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist gar nicht Leitung1 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}
		if (istAnzahlLeitung1 <= 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen von Leitung1 einen negativen Wert!".formatted(this.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung1--;
		istLeitung1InKlasse[klasse.interneID] = false;
	}


	/**
	 * Erhöht die Leitung-2-Anzahl der Lehrkraft und aktualisiert den Leitung-2-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung2Inc(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (istLeitung2InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist bereits Leitung2 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung2++;
		istLeitung2InKlasse[klasse.interneID] = true;
	}


	/**
	 * Verringert die Leitung-2-Anzahl der Lehrkraft und aktualisiert den Leitung-2-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung2Dec(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (!istLeitung2InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist gar nicht Leitung2 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}
		if (istAnzahlLeitung2 <= 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen von Leitung2 einen negativen Wert!".formatted(this.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung2--;
		istLeitung2InKlasse[klasse.interneID] = false;
	}


	/**
	 * Fügt der Lehrkraft eine zugeordnete Lerngruppe hinzu und aktualisiert den Stunden-Malus.
	 *
	 * @param lerngruppe   Die hinzuzufügende Lerngruppe.
	 */
	public void stateLerngruppeZugeordnetAdd(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe) {
		// Zustand verändern.
		istStundensummeLerngruppen += lerngruppe.wochenstundenVorgesehenGekuerzt;
		istAnzahlLerngruppen++;
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : lerngruppe.klassenmenge) {
			istStundenProKlasse[klasse.interneID] += lerngruppe.wochenstundenVorgesehenGekuerzt;
		}
		for (final @NotNull UvAlgorithmusDynDatenJahrgang jahrgang : lerngruppe.jahrgangmenge) {
			final double vorher = istStundenProJahrgang[jahrgang.interneID];
			final double nachher = istStundenProJahrgang[jahrgang.interneID] + lerngruppe.wochenstundenVorgesehenGekuerzt;
			istStundenProJahrgang[jahrgang.interneID] = nachher;
			if ((vorher == 0.0) && (nachher > 0.0)) {
				istJahrgangAnzahl++;
			}
		}
		istFachZuLerngruppenAnzahl[lerngruppe.fach.interneID]++;
		istAnzahlKorrekturen += lerngruppe.korrekturBelastung;
		for (final @NotNull UvAlgorithmusDynDatenRegel17 zeitslot : lerngruppe.zeitslots) {
			istWochentagStundeWochentypCounter[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]++;
		}
	}


	/**
	 * Entfernt bei der Lehrkraft eine zugeordnete Lerngruppe und aktualisiert den Stunden-Malus.
	 *
	 * @param lerngruppe  Die zu entfernende Lerngruppe.
	 */
	public void stateLerngruppeZugeordnetDel(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe) {
		// Konsistenzprüfung.
		final double stundenDanach = istStundensummeLerngruppen - lerngruppe.wochenstundenVorgesehenGekuerzt;
		if (stundenDanach < 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Stundenzahl %f!"
					.formatted(this.toString(), lerngruppe.toString(), stundenDanach));
			return;
		}

		final int lerngruppenAnzahlDanach = istAnzahlLerngruppen - 1;
		if (lerngruppenAnzahlDanach < 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Lerngruppen-Anzahl %d!"
					.formatted(this.toString(), lerngruppe.toString(), lerngruppenAnzahlDanach));
			return;
		}

		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : lerngruppe.klassenmenge) {
			if (istStundenProKlasse[klasse.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
				log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s in Klasse %s eine negative Stunden-Anzahl!"
						.formatted(this.toString(), lerngruppe.toString(), klasse.toString()));
				return;
			}
		}

		// Zustand verändern.
		istStundensummeLerngruppen = stundenDanach;
		istAnzahlLerngruppen = lerngruppenAnzahlDanach;
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : lerngruppe.klassenmenge) {
			istStundenProKlasse[klasse.interneID] -= lerngruppe.wochenstundenVorgesehenGekuerzt;
		}

		for (final @NotNull UvAlgorithmusDynDatenJahrgang jahrgang : lerngruppe.jahrgangmenge) {
			final double vorher = istStundenProJahrgang[jahrgang.interneID];
			final double nachher = istStundenProJahrgang[jahrgang.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt;
			istStundenProJahrgang[jahrgang.interneID] = nachher;
			if ((vorher > 0.0) && (nachher == 0.0)) {
				istJahrgangAnzahl--;
			}
		}
		istFachZuLerngruppenAnzahl[lerngruppe.fach.interneID]--;
		istAnzahlKorrekturen -= lerngruppe.korrekturBelastung;
		for (final @NotNull UvAlgorithmusDynDatenRegel17 zeitslot : lerngruppe.zeitslots) {
			istWochentagStundeWochentypCounter[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]--;
		}
	}


}
