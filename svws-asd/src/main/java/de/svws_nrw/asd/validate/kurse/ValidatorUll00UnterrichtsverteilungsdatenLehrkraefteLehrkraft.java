package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft die Lehrkraft.
 */
public final class ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idLehrkraft;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrkraft   die Wochenstunden des Lehrer
	 * @param listLehrer    die Liste aller Lehrer dieser Schule
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft(
			final @NotNull Supplier<@AllowNull Long> idLehrkraft,
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idLehrkraft = idLehrkraft;
		_validatoren.add(new ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft(getNotNullSupplierLong(idLehrkraft), listLehrer, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Prüfe, ob eine Lehrkraft eingetragen ist
		final Long idLehrkraft = _idLehrkraft.get();

		if ((idLehrkraft == null)) {
			addFehler(0, "Lehrkraft: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
