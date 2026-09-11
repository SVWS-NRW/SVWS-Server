package de.svws_nrw.core.utils.uvblockung;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Lerngruppen-Objekt für schnelle, dynamische Manipulationen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenLerngruppe {

	/** Ein Logger für Debug-Zwecke. */
	final @NotNull Logger log;

	/** Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung. */
	final @NotNull Random rnd;

	/** Der Index im internen Array.  */
	final int interneID;

	/** Die externe ID der DB/GUI.  */
	final long uvID;

	/** Die vorgesehenen Wochenstunden (ohne Kürzungen). */
	final double wochenstundenVorgesehenUngekuerzt;

	/** Die vorgesehenen Wochenstunden (mit potentieller Kürzung). */
	double wochenstundenVorgesehenGekuerzt;

	/** Wert für der angibt, ob es sich um ein Korrekturfach handelt (1 = ja, 0 = nein). */
	final int korrekturBelastung;


	/** Menge aller potentiellen Lehrkräfte. */
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> lehrkraeftePotentiell;

	/** Menge aller zugeordneten Lehrkräfte, die nicht fixiert sind. In der Regel eine Person. Sehr selten mehr.*/
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> lehrkraefteZugeordnet;

	/** Menge aller zugeordneten Lehrkräfte, die fixiert sind. In der Regel eine Person. Sehr selten mehr.*/
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> lehrkraefteZugeordnetFixiert;

	/** Menge aller zugeordneten Lehrkräfte.*/
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> lehrkraefteZugeordnetAlle;

	/** Die Klassen dieser Lerngruppe (in der Regel 1). */
	final @NotNull List<UvAlgorithmusDynDatenKlasse> klassenmenge;

	/** Die Jahrgänge dieser Lerngruppe (in der Regel 1). */
	final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgangmenge;

	/** Alle Zeitslots denen diese Lerngruppe zugeordnet ist (Regel 17). */
	final @NotNull List<UvAlgorithmusDynDatenRegel17> zeitslots;

	/** Die Menge aller Regel-Objekte die sich auf diese Lerngruppe beziehen. */
	final @NotNull List<UvAlgorithmusDynDatenRegel> regeln;

	/** Das Fach dieser Lerngruppe. */
	final @NotNull UvAlgorithmusDynDatenFach fach;


	/**
	 * Der Konstruktor.
	 *
	 * @param index               Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param parent              Das Elternobjekt.
	 * @param man                 Der {@link UvManager}.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt} auf den sich die Berechnung bezieht.
	 * @param klassen             Die {@link UvAlgorithmusDynDatenKlasse}-Menge dieser Lerngruppe.
	 * @param jahrgaenge          Die {@link UvAlgorithmusDynDatenJahrgang}-Menge dieser Lerngruppe.
	 */
	public UvAlgorithmusDynDatenLerngruppe(
			final int index,
			final @NotNull UvAlgorithmusDynDaten parent,
			final @NotNull UvManager man,
			final @NotNull UvPlanungsabschnitt planungsabschnitt,
			final @NotNull List<UvAlgorithmusDynDatenKlasse> klassen,
			final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgaenge) {

		final @NotNull List<UvLerngruppe> lerngruppenmenge = man.lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt);
		final @NotNull UvLerngruppe lerngruppe = lerngruppenmenge.get(index);
		this.fach = parent.gibDynFachByLerngruppe(lerngruppe);
		this.log = parent.gibLogger();
		this.rnd = parent.gibRandom();
		this.interneID = index;
		this.uvID = lerngruppe.id;
		this.klassenmenge = new ArrayList<>(klassen);
		this.jahrgangmenge = new ArrayList<>(jahrgaenge);
		this.lehrkraeftePotentiell = new ArrayList<>();
		this.lehrkraefteZugeordnet = new ArrayList<>();
		this.lehrkraefteZugeordnetFixiert = new ArrayList<>();
		this.lehrkraefteZugeordnetAlle = new ArrayList<>();
		this.wochenstundenVorgesehenUngekuerzt = lerngruppe.wochenstunden;
		this.wochenstundenVorgesehenGekuerzt = lerngruppe.wochenstunden;
		this.korrekturBelastung = man.lerngruppeIstKorrekturfach(lerngruppe) ? 1 : 0;
		this.zeitslots = new ArrayList<>();
		this.regeln = new ArrayList<>();
	}


	@Override
	public @NotNull String toString() {
		return ("UvAlgorithmusDynDatenLerngruppe { interneID=%d, uvID=%d }")
				.formatted(
						interneID,
						uvID
				);
	}


	/**
	 * Liefert true, falls die Lehrkraft der Lerngruppe zugeordnet ist.
	 * Hinweis: Der Vergleich der Objektreferenzen ist hier korrekt und effizienter als "equals".
	 *
	 * @param lehrkraft   Die Lehrkraft, die überprüft wird.
	 *
	 * @return true, falls die Lehrkraft der Lerngruppe zugeordnet ist.
	 */
	boolean gibIstLehrkraftZugeordnet(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return lehrkraefteZugeordnet.contains(lehrkraft) || lehrkraefteZugeordnetFixiert.contains(lehrkraft);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft fixiert ist.
	 *
	 * @param lehrkraft   Die angefragte {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft fixiert ist.
	 */
	boolean gibIstLehrkraftFixiert(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return lehrkraefteZugeordnetFixiert.contains(lehrkraft);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft potentiell diese Lerngruppe unterrichten darf.
	 *
	 * @param lehrkraft   Die angefragte {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft potentiell diese Lerngruppe unterrichten darf.
	 */
	boolean gibIstLehrkraftPotentiell(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return lehrkraeftePotentiell.contains(lehrkraft);
	}


	/**
	 * Liefert eine potentielle Lehrkraft, welche dieser Lerngruppe noch nicht zugeordnet ist.
	 * <br>Hinweis: Eine Optimierung lohnt sich hier nicht,
	 *              denn die zugeordneten Lehrkräfte sind fast immer 0 oder 1.
	 *
	 * @return eine potentielle Lehrkraft, welche dieser Lerngruppe noch nicht zugeordnet ist.
	 */
	UvAlgorithmusDynDatenLehrkraft gibLehrkraftPotentiellZufaelligOderNull() {
		final int size = lehrkraeftePotentiell.size();
		if (size == 0) {
			return null;
		}
		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = lehrkraeftePotentiell.get(rnd.nextInt(size));
		if (lehrkraefteZugeordnet.contains(lehrkraft)) {
			return null;
		}
		if (lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		return lehrkraft;
	}


	/**
	 * Liefert eine zufällige derzeit zugeordnete Lehrkraft die nicht fixiert ist, oder null.
	 *
	 * @return eine zufällige derzeit zugeordnete Lehrkraft die nicht fixiert ist, oder null.
	 */
	UvAlgorithmusDynDatenLehrkraft gibLehrkraftZugeordnetAberNichtFixiertZufaellig() {
		final int size = lehrkraefteZugeordnet.size();
		if (size == 0) {
			return null;
		}
		return lehrkraefteZugeordnet.get(rnd.nextInt(size));
	}


	/**
	 * Fügt die {@link UvAlgorithmusDynDatenLehrkraft} der potentiellen Menge dieser Lerngruppe hinzu.
	 *
	 * @param lehrkraft   Die Lehrkraft, welche hinzugefügt wird.
	 */
	void fuegeLehrkraftHinzuf(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		if (lehrkraeftePotentiell.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Lehrkraft %s Duplikat bei der potentiellen Menge!".formatted(lehrkraft.toString()));
		}

		lehrkraeftePotentiell.add(lehrkraft);
	}


	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte.
	 *
	 * @param lehrkraft   Die zu entfernende Lehrkraft.
	 */
	void entferneLehrkraft(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		if (lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(
					"Lehrkraft %s soll in Lerngruppe verboten werden, ist aber bereits zugeordnet!".formatted(lehrkraft.toString()));
		}
		if (lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(
					"Lehrkraft %s soll in Lerngruppe verboten werden, ist aber bereits fixiert!".formatted(lehrkraft.toString()));
		}

		lehrkraeftePotentiell.remove(lehrkraft);
	}


	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls diese Lerngruppe die Klasse tangiert.
	 *
	 * @param lehrkraft   Die zu entfernende Lehrkraft.
	 * @param klasse      Die verbotene Klasse.
	 */
	void entferneLehrkraftWennKlasseUebereinstimmt(
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final @NotNull UvAlgorithmusDynDatenKlasse klasse) {

		// Konsistenzprüfung.
		if (lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Lehrkraft %s soll verboten werden, ist aber bereits zugeordnet!".formatted(lehrkraft.toString()));
		}
		if (lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Lehrkraft %s soll verboten werden, ist aber bereits fixiert!".formatted(lehrkraft.toString()));
		}

		// Anwenden.
		if (klassenmenge.contains(klasse)) {
			lehrkraeftePotentiell.remove(lehrkraft);
		}
	}


	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls diese Lerngruppe die Stufe tangiert.
	 *
	 * @param lehrkraft   Die zu entfernende Lehrkraft.
	 * @param jahrgang    Der verbotene Jahrgang.
	 */
	void entferneLehrkraftWennStufeUebereinstimmt(
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final @NotNull UvAlgorithmusDynDatenJahrgang jahrgang) {

		// Konsistenzprüfung.
		if (lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(
					"Lehrkraft %s soll in Stufe verboten werden, ist aber bereits zugeordnet!".formatted(lehrkraft.toString()));
		}
		if (lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(
					"Lehrkraft %s soll in Stufe verboten werden, ist aber bereits fixiert!".formatted(lehrkraft.toString()));
		}

		// Anwenden.
		if (jahrgangmenge.contains(jahrgang)) {
			lehrkraeftePotentiell.remove(lehrkraft);
		}
	}


	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls das Fach und die Lerngruppe übereinstimmen.
	 *
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fach         Das {@link UvAlgorithmusDynDatenFach}.
	 * @param jahrgaenge   Die {@link UvAlgorithmusDynDatenJahrgang}-Menge.
	 */
	void entferneLehrkraftWennJahrgangUndFachUebereinstimmt(
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final @NotNull UvAlgorithmusDynDatenFach fach,
			final @NotNull List<UvAlgorithmusDynDatenJahrgang> jahrgaenge) {

		// Anwenden.
		// Transpiler kann nicht "stream --> anyMatch".
		if (this.fach == fach) {
			for (final @NotNull UvAlgorithmusDynDatenJahrgang jahrgang : jahrgaenge) {
				if (jahrgangmenge.contains(jahrgang)) {
					// Konsistenzprüfung.
					if (lehrkraefteZugeordnet.contains(lehrkraft)) {
						throw new DeveloperNotificationException(
								"Regel 24: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits zugeordnet!".formatted(lehrkraft.toString()));
					}
					if (lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
						throw new DeveloperNotificationException(
								"Regel 24: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits fixiert!".formatted(lehrkraft.toString()));
					}

					lehrkraeftePotentiell.remove(lehrkraft);
					return;
				}
			}
		}
	}


	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls diese Lerngruppe das entsprechende Fach repräsentiert.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fach        Das {@link UvAlgorithmusDynDatenFach}.
	 */
	void entferneLehrkraftWennFachUebereinstimmt(
			final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft,
			final @NotNull UvAlgorithmusDynDatenFach fach) {

		// Anwenden.
		if (this.fach == fach) {
			// Konsistenzprüfung.
			if (lehrkraefteZugeordnet.contains(lehrkraft)) {
				throw new DeveloperNotificationException(
						"Regel 25: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits zugeordnet!".formatted(lehrkraft.toString()));
			}
			if (lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
				throw new DeveloperNotificationException(
						"Regel 25: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits fixiert!".formatted(lehrkraft.toString()));
			}
			lehrkraeftePotentiell.remove(lehrkraft);
		}

	}


	/**
	 * Fügt dieser Lerngruppe eine Lehrkraft hinzu.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Falls true, wird die Lehrkraft der Liste aller fixierten Lehrkräfte zugeordnet, sonst der normalen Liste.
	 */
	void stateLehrkraftZugeordnetAdd(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft, final boolean fixiert) {
		// Konsistenz prüfen.
		if (lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(
					"Die Lerngruppe %s hat bereits die zugeordnete Lehrkraft %s!".formatted(this.toString(), lehrkraft.toString()));
		}
		if (lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(
					"Die Lerngruppe %s hat bereits die fixierte Lehrkraft %s!".formatted(this.toString(), lehrkraft.toString()));
		}

		// Zustand verändern.
		if (fixiert) {
			lehrkraefteZugeordnetFixiert.add(lehrkraft);
		} else {
			lehrkraefteZugeordnet.add(lehrkraft);
		}
		lehrkraefteZugeordnetAlle.add(lehrkraft);
	}


	/**
	 * Entfernt eine zuvor hinzugefügte Lehrkraft aus dieser Lerngruppe.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	void stateLehrkraftZugeordnetDel(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		// Konsistenz prüfen.
		if (!lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Die Lerngruppe %s hat keine zugeordnete Lehrkraft %s!".formatted(this.toString(), lehrkraft.toString()));
		}

		// Zustand verändern.
		lehrkraefteZugeordnet.remove(lehrkraft);
		lehrkraefteZugeordnetAlle.remove(lehrkraft);
	}


	/**
	 * Ändert den Stunden-SOLL dieser Lerngruppe.
	 *
	 * @param neuerSoll   Der neue Wert.
	 */
	public void setzeStundenSollAuf(final double neuerSoll) {
		this.wochenstundenVorgesehenGekuerzt = neuerSoll;
	}

}
