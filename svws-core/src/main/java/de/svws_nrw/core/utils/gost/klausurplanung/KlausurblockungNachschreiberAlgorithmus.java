package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Algorithmus zur Blockung von Klausuren auf eine minimale Anzahl von Schienen
 * (ergo Klausurtage).
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurblockungNachschreiberAlgorithmus {

	private static final @NotNull Random _random = new Random();

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger _logger;

	/**
	 * Der Konstruktor.
	 */
	public KlausurblockungNachschreiberAlgorithmus() {
		_logger = new Logger();
	}

	/**
	 * Der Konstruktor.
	 *
	 * @param pLogger  Ein Logger für Debug-Zwecke.
	 */
	public KlausurblockungNachschreiberAlgorithmus(final @NotNull Logger pLogger) {
		_logger = pLogger;
	}

	/**
	 * @param pNachschreiber        Die Eingabe beinhaltet alle Schülerklausurtermine, die auf die Klausurtermine in der Liste pTermine verteilt werden müssen.
	 * @param pTermine				Die Liste der GostKlausurtermine, auf die die Nachschreiber verteilt werden sollen
	 * @param pManager				Der Kursklausur-Manager
	 * @param pMaxTimeMillis  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @return Eine Liste von Paaren: 1. Element = GostSchuelerklausurtermin (Nachschreiber), 2. Element = ID des Termins / der Schiene
	 */
	public @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> berechne(final @NotNull List<@NotNull GostSchuelerklausurTermin> pNachschreiber, final @NotNull List<@NotNull GostKlausurtermin> pTermine, final @NotNull GostKursklausurManager pManager, final long pMaxTimeMillis) {

		for (GostSchuelerklausurTermin skt : pNachschreiber) {
			// Diese Schleife läuft über alle Nachschreiber, die verteilt werden müssen

			// ZU VERTEILENDER SCHÜLER
			GostSchuelerklausur sk = pManager.schuelerklausurBySchuelerklausurtermin(skt);
			long idSchueler = sk.idSchueler; // die ID des Schülers, die innerhalb jedes Klausurtermins / Schiene einzigartig sein muss
			long idKurs = sk.idKursklausur; // die ID der Kursklausur als Gütekriterium, möglichst alle mit gleicher Kursklausur innerhalb eines Termins

			// SCHIENE MIT SCHON VORHANDENEN SCHÜLERN
			GostKlausurtermin schiene1 = pTermine.getFirst();
			if (schiene1 != null) {
				List<GostSchuelerklausur> schuelerklausuren = pManager.schuelerklausurGetMengeByTerminid(schiene1.id);
			}
			// alle Schülerklausuren, die an dem Termin bereits in der Schiene sitzen

		}

		return new ArrayList<>();
	}

}
