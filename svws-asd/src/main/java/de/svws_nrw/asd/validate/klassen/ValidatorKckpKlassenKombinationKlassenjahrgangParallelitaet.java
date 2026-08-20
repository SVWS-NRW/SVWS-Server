package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf allen Klassen aus.
 */
public final class ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listKlassenDaten   ein Supplier für die Klassendaten, die geprüft werden sollen
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet(
			final @NotNull Supplier<List<KlassenStatistikGesamt>> listKlassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet(listKlassenDaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
