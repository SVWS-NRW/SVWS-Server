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
 * Ein Lehrkraft-Objekt für schnelle, dynamische Manipulationen.
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

	/** Das Pflichtstunden-Soll. */
	final double stundenSoll;

	/** Die Summe aller Entlastungen. */
	final double stundenAnrechnung;

	/** Die Summe aller Stunden der zugeordneten Lerngruppen. */
	double istStundensummeLerngruppen;

	/** Die aktuelle Anzahl an Stunden, in denen die Lehrkraft in der jeweiligen Klasse unterrichtet. */
	final double @NotNull [] istStundenProKlasse;

	/** Die aktuelle Definition, ob die Lehrkraft in der Klasse Klassenlehrer ist. */
	final boolean @NotNull [] istLeitung1InKlasse;

	/** Die aktuelle Definition, ob die Lehrkraft in der Klasse stellv. Klassenlehrer ist. */
	final boolean @NotNull [] istLeitung2InKlasse;

	/** Die aktuelle Anzahl an Klassenlehrer-Einsätzen (Leitung 1). */
	int istAnzahlLeitung1;

	/** Die aktuelle Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2). */
	int istAnzahlLeitung2;

	/** Die aktuelle Anzahl an Lerngruppen, denen diese Lehrkraft zugeordnet ist. */
	int istAnzahlLerngruppen;

	/** Die aktuelle Anzahl an Korrekturen dieser Lehrkraft. */
	int istAnzahlKorrekturen;

	/** Die aktuelle Anzahl an zugeordneten Lerngruppen pro Fach. */
	final int @NotNull [] istFachZuLerngruppenAnzahl;

	/** Die aktuelle Anzahl an Stunden, die diese Lehrkraft im jeweiligen Jahrgang unterrichtet. */
	final double @NotNull [] istStundenProJahrgang;

	/** Die aktuelle Anzahl an verschiedenen Jahrgängen, in denen diese Lehrkraft unterrichtet. */
	int istJahrgangAnzahl;

	/** Der Stundenplan dieser Lehrkraft: [Wochentag][Stunde][Wochentyp] --> Anzahl der belegenden Lerngruppen (Regel 17). */
	final int @NotNull [] @NotNull [] @NotNull [] stundenplanAnzahl;

	/** Alle Lerngruppen, denen diese Lehrkraft aktuell zugeordnet ist. */
	final @NotNull List<UvAlgorithmusDynDatenLerngruppe> istLerngruppenZugeordnet;

	/** Die Menge aller Regel-Objekte, die sich auf diese Lehrkraft beziehen. */
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

		this.stundenplanAnzahl =
				new int[man.zeitrasterGetMaxWochentag() + 1][man.zeitrasterGetMaxStunde() + 1][man.zeitrasterGetWochentypmodell() + 1];
		this.istLerngruppenZugeordnet = new ArrayList<>();

		this.regeln = new ArrayList<>();
	}


	@Override
	public @NotNull String toString() {
		return ("UvAlgorithmusDynDatenLehrkraft { interneID=%d, uvID=%d }")
				.formatted(interneID, uvID);
	}


	/**
	 * Erhöht die Leitung-1-Anzahl der Lehrkraft. Den Leitung-1-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung1Inc(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (istLeitung1InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist bereits Leitung 1 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung1++;
		istLeitung1InKlasse[klasse.interneID] = true;
	}


	/**
	 * Verringert die Leitung-1-Anzahl der Lehrkraft. Den Leitung-1-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung1Dec(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (!istLeitung1InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist gar nicht Leitung 1 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}
		if (istAnzahlLeitung1 <= 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen von Leitung 1 einen negativen Wert!".formatted(this.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung1--;
		istLeitung1InKlasse[klasse.interneID] = false;
	}


	/**
	 * Erhöht die Leitung-2-Anzahl der Lehrkraft. Den Leitung-2-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung2Inc(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (istLeitung2InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist bereits Leitung 2 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung2++;
		istLeitung2InKlasse[klasse.interneID] = true;
	}


	/**
	 * Verringert die Leitung-2-Anzahl der Lehrkraft. Den Leitung-2-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public void stateLeitung2Dec(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		// Konsistenz prüfen.
		if (!istLeitung2InKlasse[klasse.interneID]) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s ist gar nicht Leitung 2 in Klasse %s!".formatted(this.toString(), klasse.toString()));
			return;
		}
		if (istAnzahlLeitung2 <= 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen von Leitung 2 einen negativen Wert!".formatted(this.toString()));
			return;
		}

		// Zustand verändern.
		istAnzahlLeitung2--;
		istLeitung2InKlasse[klasse.interneID] = false;
	}


	/**
	 * Fügt der Lehrkraft eine zugeordnete Lerngruppe hinzu. Den Stunden-Malus aktualisiert der Aufrufer.
	 *
	 * @param lerngruppe   Die hinzuzufügende Lerngruppe.
	 */
	public void stateLerngruppeZugeordnetAdd(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe) {
		// Konsistenz prüfen. (Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.)
		if (lerngruppe.fach == null) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s kann die Lerngruppe %s nicht hinzufügen, da das Fach der Lerngruppe nicht gemappt ist!"
					.formatted(this.toString(), lerngruppe.toString()));
			return;
		}

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
			if (zeitslot.wochentyp == 0) {
				// Eine Lerngruppe mit Wochentyp 0 findet jede Woche statt. Der Index 0 wird dabei ignoriert.
				for (int wochentyp = 1; wochentyp < stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde].length; wochentyp++) {
					stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][wochentyp]++;
				}
			} else {
				stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]++;
			}
		}
		istLerngruppenZugeordnet.add(lerngruppe);
	}


	/**
	 * Entfernt bei der Lehrkraft eine zugeordnete Lerngruppe. Den Stunden-Malus aktualisiert der Aufrufer.
	 *
	 * @param lerngruppe  Die zu entfernende Lerngruppe.
	 */
	public void stateLerngruppeZugeordnetDel(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe) {
		if (istEntfernenMoeglich(lerngruppe)) {
			entferneLerngruppeZustand(lerngruppe);
		}
	}


	/**
	 * Prüft, ob die Lerngruppe bei dieser Lehrkraft entfernt werden kann.
	 *
	 * @param lerngruppe   Die zu entfernende Lerngruppe.
	 *
	 * @return TRUE, falls das Entfernen möglich ist.
	 */
	private boolean istEntfernenMoeglich(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe) {
		// Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.
		if (lerngruppe.fach == null) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s kann die Lerngruppe %s nicht entfernen, da das Fach der Lerngruppe nicht gemappt ist!"
					.formatted(this.toString(), lerngruppe.toString()));
			return false;
		}
		final double stundenDanach = istStundensummeLerngruppen - lerngruppe.wochenstundenVorgesehenGekuerzt;
		if (stundenDanach < 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Stunden-Anzahl %f!"
					.formatted(this.toString(), lerngruppe.toString(), stundenDanach));
			return false;
		}
		if ((istAnzahlLerngruppen - 1) < 0) {
			log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Lerngruppen-Anzahl %d!"
					.formatted(this.toString(), lerngruppe.toString(), istAnzahlLerngruppen - 1));
			return false;
		}
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : lerngruppe.klassenmenge) {
			if (istStundenProKlasse[klasse.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
				log.logLn(LogLevel.ERROR, "Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s in Klasse %s eine negative Stunden-Anzahl!"
						.formatted(this.toString(), lerngruppe.toString(), klasse.toString()));
				return false;
			}
		}
		return true;
	}


	/**
	 * Entfernt die Lerngruppe aus dem Zustand der Lehrkraft.
	 *
	 * @param lerngruppe   Die zu entfernende Lerngruppe.
	 */
	private void entferneLerngruppeZustand(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe) {
		istStundensummeLerngruppen -= lerngruppe.wochenstundenVorgesehenGekuerzt;
		istAnzahlLerngruppen--;
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
		final UvAlgorithmusDynDatenFach fach = lerngruppe.fach;
		if (fach != null) {
			istFachZuLerngruppenAnzahl[fach.interneID]--;
		}
		istAnzahlKorrekturen -= lerngruppe.korrekturBelastung;
		for (final @NotNull UvAlgorithmusDynDatenRegel17 zeitslot : lerngruppe.zeitslots) {
			if (zeitslot.wochentyp == 0) {
				// Eine Lerngruppe mit Wochentyp 0 findet jede Woche statt. Der Index 0 wird dabei ignoriert.
				for (int wt = 1; wt < stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde].length; wt++) {
					stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][wt]--;
				}
			} else {
				stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]--;
			}
		}
		istLerngruppenZugeordnet.remove(lerngruppe);
	}


	boolean gibIstLeitung1IncMoeglich(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		return !istLeitung1InKlasse[klasse.interneID];
	}


	boolean gibIstLeitung1DecMoeglich(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		return istLeitung1InKlasse[klasse.interneID] && (istAnzahlLeitung1 > 0);
	}


	boolean gibIstLeitung2IncMoeglich(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		return !istLeitung2InKlasse[klasse.interneID];
	}


	boolean gibIstLeitung2DecMoeglich(final @NotNull UvAlgorithmusDynDatenKlasse klasse) {
		return istLeitung2InKlasse[klasse.interneID] && (istAnzahlLeitung2 > 0);
	}


	boolean gibIstLerngruppeZugeordnetDelMoeglich(final @NotNull UvAlgorithmusDynDatenLerngruppe lerngruppe) {
		if (istAnzahlLerngruppen <= 0) {
			return false;
		}
		if (istStundensummeLerngruppen - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
			return false;
		}
		for (final @NotNull UvAlgorithmusDynDatenKlasse klasse : lerngruppe.klassenmenge) {
			if (istStundenProKlasse[klasse.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
				return false;
			}
		}
		return true;
	}


}
