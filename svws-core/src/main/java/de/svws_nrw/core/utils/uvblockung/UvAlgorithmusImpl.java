package de.svws_nrw.core.utils.uvblockung;

import java.util.List;
import java.util.Random;

import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.core.utils.uv.UvRegelManager;
import jakarta.validation.constraints.NotNull;

/**
 * Algorithmus um Lerngruppen potentielle Lehrkräfte zuzuordnen.
 *
 * @author Benjamin A. Bartsch
 */
public final class UvAlgorithmusImpl {

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger log;

	/** Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung. */
	private final @NotNull Random rnd;

	/** Die Eingabedaten von der GUI. */
	private final @NotNull UvManager man;

	/** Zur dynamischen Bewertung der aktuellen Zuordnung. */
	private final @NotNull UvAlgorithmusDynDaten dyn;

	/** Alle Algorithmen, die für die Lehrkraft-Lerngruppen-Zuordnung implementiert wurden. */
	private final @NotNull UvAlgorithmusImplAbstract @NotNull [] alg;


	/**
	 * Der Konstruktor.
	 *
	 * @param manager             Ein {@link UvManager}-Objekt, welches alle Daten hat.
	 * @param manRegeln           Ein {@link UvRegelManager}-Objekt, der alle Regeln hat.
     * @param planungsabschnitt   Der aktuelle Planungsabschnitt.
	 */
	public UvAlgorithmusImpl(final @NotNull UvManager manager, final @NotNull UvRegelManager manRegeln, final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		this.log = Logger.global();
		this.man = manager;

		// Trick, um den Seed-Wert zu kennen.
		final long seed = new Random().nextLong();
		this.rnd = new Random(seed);
		log.logLn(LogLevel.INFO, "UvAlgorithmus startet mit Seed " + seed + ".");

		// Es gibt das SELBE "UvAlgorithmusDynDaten"-Objekt für alle Algorithmen.
		this.dyn = new UvAlgorithmusDynDaten(log, rnd, manager, manRegeln, planungsabschnitt);

		// Algorithmen erzeugen.
		this.alg = new UvAlgorithmusImplAbstract @NotNull [] {
			// Alle Algorithmen ...
			new UvAlgorithmusImplRandomWalk(log, rnd, dyn),
			// ... Ende der Algorithmen.
		};
	}


	/**
	 * Optimiert die derzeitige Lehrkraft-Lerngruppen-Zuordnung.
	 *
	 * @param zeitlimit   Die Zeitspanne (in ms), die für die Berechnung zur Verfügung steht.
	 */
	public void berechneInnerhalb(final long zeitlimit) {
		final long zeitEndeGesamt = System.currentTimeMillis() + zeitlimit;
		final long zeitProAlgorithmus = Math.max(10, zeitlimit / (alg.length));

		log.logLn(LogLevel.INFO, "Schleife-Vorher: Malus = " + dyn.gibMalusBeschreibung());
		while (System.currentTimeMillis() < zeitEndeGesamt) {
			for (int i = 0; i < alg.length; i++) {
				alg[i].berechneInnerhalb(zeitProAlgorithmus);
			}
		}
		log.logLn(LogLevel.INFO, "Schleife-Danach: Malus1 = " + dyn.gibMalusBeschreibung());

	}


	/**
	 * Liefert die bisher beste Lehrkraft-Lerngruppen-Zuordnung.
	 *
	 * @return die bisher beste Lehrkraft-Lerngruppen-Zuordnung.
	 */
	public @NotNull List<UvLerngruppenLehrer> gibBestesAktuellesErgebnis() {
		return dyn.gibAktuelleZuordnung();
	}


}
