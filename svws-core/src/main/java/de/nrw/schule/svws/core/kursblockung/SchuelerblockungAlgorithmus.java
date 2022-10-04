package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.core.Service;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungOutput;
import de.nrw.schule.svws.logger.LogLevel;
import jakarta.validation.constraints.NotNull;

/** Dieser Service teilt EINEN Schüler anhand seiner Fachwahlen auf Kurse zu. Dabei geht der Algorithmus davon aus, dass
 * die Kurse bereits auf Schienen verteilt wurden. Die Eingabe {@link SchuelerblockungInput} wird in
 * {@link SchuelerblockungOutput} umgewandelt.
 * 
 * @author Benjamin A. Bartsch */
public class SchuelerblockungAlgorithmus
		extends Service<@NotNull SchuelerblockungInput, @NotNull SchuelerblockungOutput> {

	@Override
	public @NotNull SchuelerblockungOutput handle(@NotNull SchuelerblockungInput pInput) {
		// Logger-Einrückung (relativ +4).
		logger.modifyIndent(+4);

		// Random-Objekt mit bestimmten Seed erzeugen.
		long seed = new Random().nextLong();
		@NotNull Random random = new Random(seed);
		logger.log(LogLevel.APP, "Seed verwendet --> " + seed);

		// SchuelerblockungInput --> SchuelerblockungDynDaten
		@NotNull SchuelerblockungDynDaten dynDaten = new SchuelerblockungDynDaten(random, logger, pInput);

		// Rückgabe des besten Ergebnisses
		return dynDaten.gibBestesMatching();
	}

}
