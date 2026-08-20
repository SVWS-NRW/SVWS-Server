package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüfung des Kurslehrers.
 */
public final class ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrkraft   die ID des Lehrer
	 * @param listLehrer    die Liste aller Lehrer dieser Schule
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft(
			final @NotNull Supplier<@AllowNull Long> idLehrkraft,
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft(idLehrkraft, listLehrer, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
