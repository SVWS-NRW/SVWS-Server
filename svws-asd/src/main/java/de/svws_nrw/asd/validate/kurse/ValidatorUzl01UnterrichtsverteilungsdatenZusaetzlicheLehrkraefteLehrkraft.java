package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob jeder Eintrag in dem Array weitereLehrer einer gültigen Lehrer ID zuordnenbar ist.
 */
public final class ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/** Die Liste der IDs weiterer Lehrer. */
	private final @NotNull Supplier<@AllowNull List<Long>> _idsWeitereLehrer;
	/** Die Liste der IDs aller Lehrer. */
	private final @NotNull Supplier<@AllowNull List<Long>> _idsAlleLehrer;

	/**
	 * Erstellt einen neuen Validator für die Prüfung zusätzlicher Lehrkräfte
	 *
	 * @param idsWeitereLehrer    die Liste der IDs der weiteren LehrerKursleherer
	 * @param idsAlleLehrer       die Liste der IDs aller Lehrer dieser Schule
	 * @param kontext             der Kontext des Validators
	 */

	public ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(@NotNull final Supplier<@AllowNull List<Long>> idsWeitereLehrer,
			@NotNull final Supplier<@AllowNull List<Long>> idsAlleLehrer, @NotNull final ValidatorKontext kontext) {
		super(kontext);
		_idsWeitereLehrer = idsWeitereLehrer;
		_idsAlleLehrer = idsAlleLehrer;
	}

	@Override
	protected boolean pruefe() {
		final List<Long> idsWeitereLehrer = _idsWeitereLehrer.get();
		final List<Long> idsAlleLehrer = _idsAlleLehrer.get();

		if (idsWeitereLehrer == null || idsAlleLehrer == null) {
			return true;
		}

		for (final Long idKurslehrer : idsWeitereLehrer) {
			boolean gefunden = false;
			for (final Long idLehrer : idsAlleLehrer) {
				if (idKurslehrer == idLehrer) {
					gefunden = true;
					break;
				}
			}
			if (!gefunden) {
				addFehler(0,
						"Zusätzliche Lehrkraft: Ungültige ausgewählte Lehrkraft.");
				return false;
			}
		}
		return true;
	}
}
