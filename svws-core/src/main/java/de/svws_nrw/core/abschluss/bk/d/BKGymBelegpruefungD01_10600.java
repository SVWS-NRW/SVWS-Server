package de.svws_nrw.core.abschluss.bk.d;

import jakarta.validation.constraints.NotNull;

/**
 * Der Belegprüfungsalgorithmus für den Bildungsgang der Schulgliederung D01
 * und der Fachklasse 106 00.
 */
public final class BKGymBelegpruefungD01_10600 extends BKGymBelegpruefung {

	/**
	 * Erzeugt einen neue Belegprüfung
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public BKGymBelegpruefungD01_10600(@NotNull final BKGymAbiturdatenManager manager) {
		super(manager);
	}

	@Override
	public void pruefe() {
		// TODO
	}

}
