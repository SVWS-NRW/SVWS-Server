package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüfung des Kurslehrers.
 */
public final class ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idsWeitereLehrer    die Liste der IDs der weiteren LehrerKursleherer
	 * @param idsAlleLehrer       die Liste der IDs aller Lehrer dieser Schule
	 * @param idLehrer            die ID einer ausgewählten, weiteren Lehrkraft
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(
			final @NotNull Supplier<@AllowNull List<Long>> idsWeitereLehrer,
			final @NotNull Supplier<@AllowNull List<Long>> idsAlleLehrer,
			final @NotNull Supplier<@AllowNull Long> idLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(idsWeitereLehrer, idsAlleLehrer, kontext));
		_validatoren.add(new ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(idsWeitereLehrer, idLehrer, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
