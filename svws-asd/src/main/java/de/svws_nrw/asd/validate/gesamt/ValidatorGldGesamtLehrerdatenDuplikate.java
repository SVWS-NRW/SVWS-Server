package de.svws_nrw.asd.validate.gesamt;

import java.util.List;
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
public final class ValidatorGldGesamtLehrerdatenDuplikate extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer       die Liste aller Lehrerstammdaten
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorGldGesamtLehrerdatenDuplikate(
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorGld10GesamtLehrerdatenDuplikate(listLehrer, kontext));
		_validatoren.add(new ValidatorGld11GesamtLehrerdatenDuplikate(listLehrer, kontext));
	}


	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}
}
