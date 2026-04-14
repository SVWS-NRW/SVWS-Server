package de.svws_nrw.asd.validate.gesamt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Schuelern einer Schule aus und überprüft dort, ob Duplikate in Bezug
 * auf die ID vorkommen.
 */
public final class ValidatorGsd10GesamtSchuelerdatenDuplikate extends Validator {

	private final @NotNull Supplier<List<SchuelerStatistikGesamt>> listSchueler;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listSchueler        die Liste aller Schuelerstammdaten
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGsd10GesamtSchuelerdatenDuplikate(
			final @NotNull Supplier<List<SchuelerStatistikGesamt>> listSchueler,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.listSchueler = listSchueler;
	}

	@Override
	protected boolean pruefe() {
		boolean success = true;
		final @NotNull List<SchuelerStatistikGesamt> schuelerListe = this.listSchueler.get();

		// Bei einer leeren Liste ist hier nichts zu prüfen. Dies ist ein Spezialfall, der nur bei einer neu angelegten Schul-Datenbank vorkommen sollte.
		if (schuelerListe.isEmpty()) {
			return true;
		}

		// Gehe die Liste der Schüler durch und speichere diese in einer HashMap, um Duplikate zu erkennen
		final @NotNull Set<Long> ids = new HashSet<>();
		for (final SchuelerStatistikGesamt schueler : schuelerListe) {

			// Prüfe, ob in der Liste eine ID doppelt enthalten ist. Dies wäre eine Fehler bei der Zusammenstellung der Liste
			final boolean istNeu = ids.add(schueler.id);
			if (!istNeu) {
				this.addFehler(0, "Schüler: Die ID " + schueler.id + " kommt mehrfach vor.");
				success = false;
			}

		}
		return success;
	}

}
