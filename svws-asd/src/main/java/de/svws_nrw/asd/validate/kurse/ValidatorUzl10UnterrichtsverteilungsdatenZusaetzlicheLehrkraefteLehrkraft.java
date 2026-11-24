package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die ausgwählte zusätzliche Lehrkraft dem Kurslehrer entspricht.
 */
public final class ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/** Die Liste der IDs weiterer Lehrer. */
	private final @NotNull Supplier<@AllowNull List<Long>> _idsWeitereLehrer;
	/** Die ID des Kurslehrers */
	private final @NotNull Supplier<@AllowNull Long> _idLehrer;

	/**
	 * Erstellt einen neuen Validator für die Prüfung zusätzlicher Lehrkräfte
	 *
	 * @param idsWeitereLehrer    die Liste der IDs der weiteren LehrerKursleherer
	 * @param idLehrer            die ID des Kurslehrers
	 * @param kontext             der Kontext des Validators
	 */

	public ValidatorUzl10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(@NotNull final Supplier<@AllowNull List<Long>> idsWeitereLehrer,
			@NotNull final Supplier<@AllowNull Long> idLehrer, @NotNull final ValidatorKontext kontext) {
		super(kontext);
		_idsWeitereLehrer = idsWeitereLehrer;
		_idLehrer = idLehrer;
	}

	@Override
	protected boolean pruefe() {
		final List<Long> idsWeitereLehrer = _idsWeitereLehrer.get();
		final Long idLehrer = _idLehrer.get();

		if (idsWeitereLehrer == null || idLehrer == null) {
			return true;
		}

		for (final Long idKurslehrer : idsWeitereLehrer) {

			if (idKurslehrer != null && idLehrer.equals(idKurslehrer)) {
				addFehler(0,
						"Zusätzliche Lehrkraft: Die ausgewählte zusätzliche Lehrkraft ist bereits oben im Feld 'Lehrkraft' angegeben. Bitte prüfen.");
				return false;
			}

		}
		return true;
	}
}
