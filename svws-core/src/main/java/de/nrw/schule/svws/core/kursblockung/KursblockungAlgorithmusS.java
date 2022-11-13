package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Algorithmus des <b>Typs S</b> dient dazu SuS bei fixierter Kurslage auf ihre Kurse zu verteilen. Im Konstruktor
 * können die Unterklassen die jeweiligen Datenstrukturen aufbauen. SuS dürfen im Konstruktor noch nicht auf die Kurse
 * verteilt werden.
 * 
 * @author Benjamin A. Bartsch
 */
public abstract class KursblockungAlgorithmusS {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected final @NotNull Random _random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	protected final @NotNull Logger logger;

	/**
	 * Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges.
	 */
	protected final @NotNull KursblockungDynDaten dynDaten;

	/**
	 * Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 * 
	 * @param pRandom  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param dynDaten Die aktuellen Blockungsdaten.
	 */
	public KursblockungAlgorithmusS(@NotNull Random pRandom, @NotNull Logger logger,
			@NotNull KursblockungDynDaten dynDaten) {
		_random = pRandom;
		this.logger = logger;
		this.dynDaten = dynDaten;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der SuS auf die Kurse. Es gibt keine
	 * festgelegte Zeitgrenze, aber die Methode sollte sehr schnell sein.
	 */
	public abstract void berechne();

}
