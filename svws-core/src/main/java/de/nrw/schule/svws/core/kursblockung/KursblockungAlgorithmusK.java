package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Ein Algorithmus des <b>Typs K</b> dient dazu Kurse auf Schienen zu verteilen. Dabei dürfen Kurse über 1 oder mehr
 * Schienen gehen, aber nicht über 0. Im Konstruktor können die Unterklassen die jeweiligen Datenstrukturen aufbauen.
 * Kurse dürfen im Konstruktor noch nicht auf die Schienen verteilt werden.
 * 
 * @author Benjamin A. Bartsch */
public abstract class KursblockungAlgorithmusK {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	protected final @NotNull Random _random;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	protected final @NotNull Logger logger;

	/** Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges. */
	protected final @NotNull KursblockungDynDaten dynDaten;

	/** Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten. */
	public KursblockungAlgorithmusK(final @NotNull Random pRandom, final @NotNull Logger pLogger,
			final @NotNull KursblockungDynDaten pDynDaten) {
		_random = pRandom;
		logger = pLogger;
		dynDaten = pDynDaten;
	}

	/** Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Kurse auf die Schienen und
	 * überschreitet dabei nicht die Endzeit (in Millisekunden).
	 * 
	 * @param pTimeEndMillis Die Endzeit (in Millisekunden). */
	public abstract void berechne(long pTimeEndMillis);

}
