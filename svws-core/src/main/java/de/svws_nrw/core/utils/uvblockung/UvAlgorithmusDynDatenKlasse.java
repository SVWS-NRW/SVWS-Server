package de.svws_nrw.core.utils.uvblockung;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Klassen-Objekt (Schulklasse) für schnelle, dynamische Manipulationen
 * und zur Speicherung von Referenzen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusDynDatenKlasse {

	/** Ein Logger für Debug-Zwecke. */
	final @NotNull Logger log;

	/** Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung. */
	final @NotNull Random rnd;

	/** Der Index im internen Array.  */
	final int interneID;

	/** Die externe ID der DB/GUI.  */
	final long uvID;


	/** Menge aller potentiellen Lehrkräfte, die als Klassenleitung (Leitung 1) in Frage kommen. */
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> leitung1Potentiell;

	/** Menge aller potentiellen Lehrkräfte, die als stellv. Klassenleitung (Leitung 2) in Frage kommen. */
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> leitung2Potentiell;

	/** Die Lehrkräfte, die aktuell Klassenleitung (Leitung 1) sind.*/
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> leitung1Zugeordnet;

	/** Die Lehrkräfte, die aktuell stellv. Klassenleitung (Leitung 2) sind.*/
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> leitung2Zugeordnet;

	/** Die Lehrkräfte, die aktuell Klassenleitung (Leitung 1) und fixiert sind.*/
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> leitung1ZugeordnetFixiert;

	/** Die Lehrkräfte, die aktuell stellv. Klassenleitung (Leitung 2) und fixiert sind.*/
	final @NotNull List<UvAlgorithmusDynDatenLehrkraft> leitung2ZugeordnetFixiert;


	/** Die Menge aller Regel-Objekte, die sich auf diese Klasse beziehen. */
	final @NotNull List<UvAlgorithmusDynDatenRegel> regeln;


	/**
	 * Der Konstruktor.
	 *
	 * @param log           Ein Logger für Debug-Zwecke.
	 * @param rnd           Ein Random-Objekt für zufällige Entscheidungen.
	 * @param index         Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param uvKlasse      Die UV-Klasse, deren Daten teils kopiert werden.
	 * @param lehrerMenge   Alle {@link UvAlgorithmusDynDatenLehrkraft}-Objekte, die potentiell die Klasse leiten.
	 */
	public UvAlgorithmusDynDatenKlasse(
			final @NotNull Logger log,
			final @NotNull Random rnd,
			final int index,
			final @NotNull UvKlasse uvKlasse,
			final @NotNull UvAlgorithmusDynDatenLehrkraft @NotNull [] lehrerMenge) {

		this.log = log;
		this.rnd = rnd;
		this.interneID = index;
		this.uvID = uvKlasse.id;

		this.leitung1Potentiell = new ArrayList<>();
		this.leitung2Potentiell = new ArrayList<>();
		for (final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft : lehrerMenge) {
			this.leitung1Potentiell.add(lehrkraft);
			this.leitung2Potentiell.add(lehrkraft);
		}

		this.leitung1Zugeordnet = new ArrayList<>();
		this.leitung1ZugeordnetFixiert = new ArrayList<>();
		this.leitung2Zugeordnet = new ArrayList<>();
		this.leitung2ZugeordnetFixiert = new ArrayList<>();

		this.regeln = new ArrayList<>();
	}


	@Override
	public @NotNull String toString() {
		return ("UvAlgorithmusDynDatenKlasse { interneID=%d, uvID=%d,"
				+ " leitung1Zugeordnet=%d, leitung1Fixiert=%d,"
				+ " leitung2Zugeordnet=%d, leitung2Fixiert=%d}")
				.formatted(
						interneID,
						uvID,
						leitung1Zugeordnet.size(),
						leitung1ZugeordnetFixiert.size(),
						leitung2Zugeordnet.size(),
						leitung2ZugeordnetFixiert.size()
				);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft aktuell als Leitung 1 fixiert ist.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft aktuell als Leitung 1 fixiert ist.
	 */
	boolean gibIstLeitung1Fixiert(@NotNull final UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return leitung1ZugeordnetFixiert.contains(lehrkraft);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft aktuell als Leitung 2 fixiert ist.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft aktuell als Leitung 2 fixiert ist.
	 */
	boolean gibIstLeitung2Fixiert(@NotNull final UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return leitung2ZugeordnetFixiert.contains(lehrkraft);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft aktuell als Leitung 1 zugeordnet ist.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft aktuell als Leitung 1 zugeordnet ist.
	 */
	boolean gibIstLeitung1Zugeordnet(@NotNull final UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return leitung1Zugeordnet.contains(lehrkraft);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft aktuell als Leitung 2 zugeordnet ist.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft aktuell als Leitung 2 zugeordnet ist.
	 */
	boolean gibIstLeitung2Zugeordnet(@NotNull final UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return leitung2Zugeordnet.contains(lehrkraft);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft als Leitung 1 eingesetzt werden darf.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft als Leitung 1 eingesetzt werden darf.
	 */
	boolean gibIstLeitung1Potentiell(@NotNull final UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return leitung1Potentiell.contains(lehrkraft);
	}


	/**
	 * Liefert TRUE, falls die Lehrkraft als Leitung 2 eingesetzt werden darf.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft als Leitung 2 eingesetzt werden darf.
	 */
	boolean gibIstLeitung2Potentiell(@NotNull final UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return leitung2Potentiell.contains(lehrkraft);
	}


	/**
	 * Liefert die Menge aller momentan zugeordneten Klassenlehrer (Leitung 1) zuerst, gefolgt von stellv. Klassenlehrer (Leitung 2).
	 *
	 * @return die Menge aller momentan zugeordneten Klassenlehrer (Leitung 1) zuerst, gefolgt von stellv. Klassenlehrer (Leitung 2).
	 */
	@NotNull
	List<UvAlgorithmusDynDatenLehrkraft> gibAktuelleMengeLeitung1und2() {
		final @NotNull List<UvAlgorithmusDynDatenLehrkraft> list = new ArrayList<>();
		list.addAll(leitung1ZugeordnetFixiert);
		list.addAll(leitung1Zugeordnet);
		list.addAll(leitung2ZugeordnetFixiert);
		list.addAll(leitung2Zugeordnet);
		return list;
	}


	/**
	 * Liefert die Menge aller momentan zugeordneten stellv. Klassenlehrer (Leitung 2).
	 *
	 * @return die Menge aller momentan zugeordneten stellv. Klassenlehrer (Leitung 2).
	 */
	@NotNull
	List<UvAlgorithmusDynDatenLehrkraft> gibAktuelleMengeLeitung2() {
		final @NotNull List<UvAlgorithmusDynDatenLehrkraft> list = new ArrayList<>();
		list.addAll(leitung2ZugeordnetFixiert);
		list.addAll(leitung2Zugeordnet);
		return list;
	}


	/**
	 * Liefert eine potentielle Lehrkraft, welche der Klassenleitung (Leitung 1) noch nicht zugeordnet ist.
	 *
	 * <br>Hinweis: Eine Optimierung lohnt sich hier nicht,
	 *              denn die zugeordneten Lehrkräfte sind fast immer 0 oder 1.
	 *
	 * @return eine potentielle Lehrkraft, welche der Klassenleitung (Leitung 1) noch nicht zugeordnet ist, oder null.
	 */
	UvAlgorithmusDynDatenLehrkraft gibLeitung1PotentiellZufaelligOderNull() {
		final int size = leitung1Potentiell.size();

		if (size == 0) {
			return null;
		}

		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = leitung1Potentiell.get(rnd.nextInt(size));

		if (leitung1Zugeordnet.contains(lehrkraft) || leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		if (leitung2Zugeordnet.contains(lehrkraft) || leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}

		return lehrkraft;
	}


	/**
	 * Liefert eine potentielle Lehrkraft, welche der stellv. Klassenleitung (Leitung 2) noch nicht zugeordnet ist.
	 *
	 * <br>Hinweis: Eine Optimierung lohnt sich hier nicht,
	 *              denn die zugeordneten Lehrkräfte sind fast immer 0 oder 1.
	 *
	 * @return eine potentielle Lehrkraft, welche der stellv. Klassenleitung (Leitung 2) noch nicht zugeordnet ist, oder null.
	 */
	UvAlgorithmusDynDatenLehrkraft gibLeitung2PotentiellZufaelligOderNull() {
		final int size = leitung2Potentiell.size();

		if (size == 0) {
			return null;
		}

		final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft = leitung2Potentiell.get(rnd.nextInt(size));

		if (leitung1Zugeordnet.contains(lehrkraft) || leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		if (leitung2Zugeordnet.contains(lehrkraft) || leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}

		return lehrkraft;
	}


	/**
	 * Liefert eine zufällige derzeit zugeordnete Klassenleitung (Leitung 1), die nicht fixiert ist, oder null.
	 *
	 * @return eine zufällige derzeit zugeordnete Klassenleitung (Leitung 1), die nicht fixiert ist, oder null.
	 */
	UvAlgorithmusDynDatenLehrkraft gibLeitung1ZugeordnetAberNichtFixiertZufaellig() {
		final int size = leitung1Zugeordnet.size();
		if (size == 0) {
			return null;
		}
		return leitung1Zugeordnet.get(rnd.nextInt(size));
	}


	/**
	 * Liefert eine zufällige derzeit zugeordnete stellv. Klassenleitung (Leitung 2), die nicht fixiert ist, oder null.
	 *
	 * @return eine zufällige derzeit zugeordnete stellv. Klassenleitung (Leitung 2), die nicht fixiert ist, oder null.
	 */
	UvAlgorithmusDynDatenLehrkraft gibLeitung2ZugeordnetAberNichtFixiertZufaellig() {
		final int size = leitung2Zugeordnet.size();
		if (size == 0) {
			return null;
		}
		return leitung2Zugeordnet.get(rnd.nextInt(size));
	}


	/**
	 * Entfernt die Lehrkraft als potentielle Klassenleitung (Leitung 1).
	 * <br>Diese Methode darf nur einmalig aufgerufen werden.
	 *
	 * @param lehrkraft   Die zu entfernende potentielle Lehrkraft.
	 */
	void entferneLeitung1(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		// Konsistenzprüfung. (Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.)
		if (leitung1Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Leitung 1 %s soll verboten werden, ist aber bereits zugeordnet!".formatted(lehrkraft.toString()));
			return;
		}
		if (leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Leitung 1 %s soll verboten werden, ist aber bereits fixiert!".formatted(lehrkraft.toString()));
			return;
		}

		leitung1Potentiell.remove(lehrkraft);
	}


	/**
	 * Entfernt die Lehrkraft als potentielle stellv. Klassenleitung (Leitung 2).
	 * <br>Diese Methode darf nur einmalig aufgerufen werden.
	 *
	 * @param lehrkraft   Die zu entfernende potentielle Lehrkraft.
	 */
	void entferneLeitung2(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		// Konsistenzprüfung. (Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.)
		if (leitung2Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Leitung 2 %s soll verboten werden, ist aber bereits zugeordnet!".formatted(lehrkraft.toString()));
			return;
		}
		if (leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Leitung 2 %s soll verboten werden, ist aber bereits fixiert!".formatted(lehrkraft.toString()));
			return;
		}

		leitung2Potentiell.remove(lehrkraft);
	}


	/**
	 * Fügt dieser Klasse eine Klassenleitung (Leitung 1) hinzu. Den Malus aktualisiert der Aufrufer.
	 *
	 * <br>Die Lehrkraft darf dabei weder bereits als Leitung 1 noch als Leitung 2 zugeordnet oder fixiert sein.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}, die als Leitung 1 zugeordnet werden soll.
	 * @param fixiert     Falls true, wird die Lehrkraft in die fixierte Menge aufgenommen,
	 *                    sonst in die normale Menge.
	 */
	void stateLeitung1ZugeordnetAdd(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft, final boolean fixiert) {
		// Konsistenz überprüfen. (Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.)
		if (leitung1Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 1 hat die Lehrkraft bereits (normal)!");
			return;
		}
		if (leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 1 hat die Lehrkraft bereits (fixiert)!");
			return;
		}
		if (leitung2Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 2 hat die Lehrkraft bereits (normal)!");
			return;
		}
		if (leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 2 hat die Lehrkraft bereits (fixiert)!");
			return;
		}

		// Zustand verändern
		if (fixiert) {
			leitung1ZugeordnetFixiert.add(lehrkraft);
		} else {
			leitung1Zugeordnet.add(lehrkraft);
		}
	}


	/**
	 * Entfernt eine zuvor zugeordnete Klassenleitung (Leitung 1). Den Malus aktualisiert der Aufrufer.
	 *
	 * <br>Die Lehrkraft muss aktuell als normale Leitung 1 zugeordnet sein.
	 *
	 * @param lehrkraft   Die aktuell zugeordnete Lehrkraft.
	 */
	void stateLeitung1ZugeordnetDel(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		// Konsistenz überprüfen. (Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.)
		if (!leitung1Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 1 kann nicht entfernt werden, da sie gar nicht zugeordnet ist!");
			return;
		}

		// Zustand verändern
		leitung1Zugeordnet.remove(lehrkraft);
	}


	/**
	 * Fügt dieser Klasse eine stellv. Klassenleitung (Leitung 2) hinzu. Den Malus aktualisiert der Aufrufer.
	 *
	 * <br>Die Lehrkraft darf dabei weder bereits als Leitung 1 noch als Leitung 2
	 * zugeordnet oder fixiert sein.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}, die als Leitung 2 zugeordnet werden soll.
	 * @param fixiert     Falls true, wird die Lehrkraft in die fixierte Menge aufgenommen,
	 *                    sonst in die normale Menge.
	 */
	void stateLeitung2ZugeordnetAdd(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft, final boolean fixiert) {
		// Konsistenz überprüfen. (Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.)
		if (leitung1Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 1 hat die Lehrkraft bereits (normal)!");
			return;
		}
		if (leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 1 hat die Lehrkraft bereits (fixiert)!");
			return;
		}
		if (leitung2Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 2 hat die Lehrkraft bereits (normal)!");
			return;
		}
		if (leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 2 hat die Lehrkraft bereits (fixiert)!");
			return;
		}

		// Zustand verändern
		if (fixiert) {
			leitung2ZugeordnetFixiert.add(lehrkraft);
		} else {
			leitung2Zugeordnet.add(lehrkraft);
		}
	}


	/**
	 * Entfernt eine zuvor zugeordnete stellv. Klassenleitung (Leitung 2). Den Malus aktualisiert der Aufrufer.
	 *
	 * <br>Die Lehrkraft muss aktuell als normale Leitung 2 zugeordnet sein.
	 *
	 * @param lehrkraft   Die aktuell zugeordnete Lehrkraft.
	 */
	void stateLeitung2ZugeordnetDel(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		// Konsistenz überprüfen. (Da der Aufruf aus einem separaten Thread erfolgen kann, wird nicht geworfen, sondern geloggt und abgebrochen.)
		if (!leitung2Zugeordnet.contains(lehrkraft)) {
			log.logLn(LogLevel.ERROR, "Klassenleitung 2 kann nicht entfernt werden, da sie gar nicht zugeordnet ist!");
			return;
		}

		// Zustand verändern
		leitung2Zugeordnet.remove(lehrkraft);
	}


	boolean gibIstLeitung1ZugeordnetAddMoeglich(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return gibIstLeitung1Potentiell(lehrkraft)
				&& !gibIstLeitung1Zugeordnet(lehrkraft)
				&& !gibIstLeitung1Fixiert(lehrkraft)
				&& !gibIstLeitung2Zugeordnet(lehrkraft)
				&& !gibIstLeitung2Fixiert(lehrkraft);
	}


	boolean gibIstLeitung1ZugeordnetDelMoeglich(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return gibIstLeitung1Zugeordnet(lehrkraft) && !gibIstLeitung1Fixiert(lehrkraft);
	}


	boolean gibIstLeitung2ZugeordnetAddMoeglich(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return gibIstLeitung2Potentiell(lehrkraft)
				&& !gibIstLeitung1Zugeordnet(lehrkraft)
				&& !gibIstLeitung1Fixiert(lehrkraft)
				&& !gibIstLeitung2Zugeordnet(lehrkraft)
				&& !gibIstLeitung2Fixiert(lehrkraft);
	}


	boolean gibIstLeitung2ZugeordnetDelMoeglich(final @NotNull UvAlgorithmusDynDatenLehrkraft lehrkraft) {
		return gibIstLeitung2Zugeordnet(lehrkraft) && !gibIstLeitung2Fixiert(lehrkraft);
	}


}
