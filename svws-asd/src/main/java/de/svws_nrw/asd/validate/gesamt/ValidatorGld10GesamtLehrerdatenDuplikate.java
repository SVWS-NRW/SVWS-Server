package de.svws_nrw.asd.validate.gesamt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Lehrern einer Schule aus und überprüft dort, ob Duplikate in Bezug
 * auf Namen, Vornamen, Geschlecht und Geburtsdatum vorkommen.
 */
public final class ValidatorGld10GesamtLehrerdatenDuplikate extends Validator {

	private final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste aller Lehrerstammdaten
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGld10GesamtLehrerdatenDuplikate(
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.listLehrer = listLehrer;
	}

	@Override
	protected boolean pruefe() {
		boolean success = true;
		final @NotNull List<LehrerStatistikGesamt> list = listLehrer.get();

		// Bei einer leeren Liste ist hier nichts zu prüfen. Dies ist ein Spezialfall, der nur bei einer neu angelegten Schul-Datenbank vorkommen sollte.
		if (list.isEmpty())
			return success;

		// Gehe die Liste der Lehrer durch und speichere diese in einer HashMap, um Duplikate zu erkennen
		final @NotNull Set<Long> ids = new HashSet<>();
		for (final LehrerStatistikGesamt lehrer : list) {

			// Prüfe, ob in der Liste eine ID doppelt enthalten ist. Dies wäre eine Fehler bei der Zusammenstellung der Liste
			final boolean istNeu = ids.add(lehrer.id);
			if (!istNeu) {
				this.addFehler(0, "Lehrkäfte: Die ID " + lehrer.id + " kommt in der Liste mehrfach vor.");
				success = false;
			}

		}
		return success;
	}

}
