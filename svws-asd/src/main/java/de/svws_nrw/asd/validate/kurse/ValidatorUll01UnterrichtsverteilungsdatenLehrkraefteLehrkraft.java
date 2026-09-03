package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft die Lehrkraft.
 */
public final class ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idLehrkraft;
	private final @NotNull Supplier<List<LehrerStatistikGesamt>> _listLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrkraft   der Lehrer
	 * @param listLehrer    die Liste aller Lehrer dieser Schule
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorUll01UnterrichtsverteilungsdatenLehrkraefteLehrkraft(
			final @NotNull Supplier<@NotNull Long> idLehrkraft,
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idLehrkraft = idLehrkraft;
		_listLehrer = listLehrer;
	}

	@Override
	protected boolean pruefe() {
		final Long idLehrkraft = _idLehrkraft.get();
		final List<LehrerStatistikGesamt> listlehrer = _listLehrer.get();

		for (final LehrerStatistikGesamt lehrer : listlehrer) {
			if (idLehrkraft == lehrer.id) {
				return true;
			}
		}
			addFehler(0, "Lehrkraft: Das Feld 'Lehrkraft' muss zulässig sein.");
			return false;
	}

}
