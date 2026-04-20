package de.svws_nrw.core.kursblockung;

import java.util.Random;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Algorithmus des Typs <b>PermanentK</b> dient dazu über einen beliebigen Zeitraum Kurse auf Schienen zu verteilen.
 * Der Algorithmus speichert seinen eigenen Zustand in einem {KursblockungDynDaten}-Objekt.
 *
 * @author Benjamin A. Bartsch
 */
public abstract class KursblockungAlgorithmusPermanentK {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	final @NotNull Random rnd;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	final @NotNull Logger log;

	/** Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges. */
	final @NotNull KursblockungDynDaten dynDaten;

	/**
	 * Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 *
	 * @param random   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input    Die Eingabedaten.
	 */
	protected KursblockungAlgorithmusPermanentK(final @NotNull Random random, final @NotNull Logger logger, final @NotNull GostBlockungsdatenManager input) {
		rnd = random;
		log = logger;
		dynDaten = new KursblockungDynDaten(random, logger, input);
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, optimiert die Blockung weiter.
	 *
	 * @param zeitEnde  Der Zeitpunkt (in Millisekunden), bis zu dem der Algorithmus weiter optimieren darf.
	 */
	public abstract void next(long zeitEnde);

	/**
	 * Liefert das KursblockungDynDaten-Objekt des Algorithmus.
	 *
	 * @return das KursblockungDynDaten-Objekt des Algorithmus.
	 */
	public @NotNull KursblockungDynDaten gibDynDaten() {
		return dynDaten;
	}

	/**
	 * Lädt das beste Blockungsergebnis und verteilt SuS, falls diese aufgrund des Algorithmus zuvor nicht verteilt wurden.
	 */
	public abstract void ladeBestMitSchuelerverteilung();

}
