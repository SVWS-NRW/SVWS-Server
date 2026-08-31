package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüfung des Kurslehrers.
 */
public final class ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listWeitereLehrer  die Liste der Kursleherer
	 * @param listLehrer         die Liste aller Lehrer dieser Schule
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(
			final @NotNull Supplier<List<KursLehrer>> listWeitereLehrer,
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(listWeitereLehrer, listLehrer, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
