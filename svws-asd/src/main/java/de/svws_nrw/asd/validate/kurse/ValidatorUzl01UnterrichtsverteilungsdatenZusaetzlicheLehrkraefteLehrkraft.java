package de.svws_nrw.asd.validate.kurse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob jeder Eintrag in dem Array weitereLehrer einer gültigen Lehrer ID zuordnenbar ist.
 */
public final class ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft extends Validator {

	/** Die Liste weiterer Lehrer. */
	private final @NotNull Supplier<List<KursLehrer>> _listWeitereLehrer;
	/** Die Liste der Lehrer. */
	private final @NotNull Supplier<List<LehrerStatistikGesamt>> _listLehrer;

	/**
	 * Erstellt einen neuen Validator für die Prüfung zusätzlicher Lehrkräfte
	 *
	 * @param listWeitereLehrer   die Liste der Kurslehrer
	 * @param listLehrer          die Liste der Lehrer
	 * @param kontext             der Kontext des Validators
	 */

	public ValidatorUzl01UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(@NotNull final Supplier<List<KursLehrer>> listWeitereLehrer,
			@NotNull final Supplier<List<LehrerStatistikGesamt>> listLehrer, @NotNull final ValidatorKontext kontext) {
		super(kontext);
		_listWeitereLehrer = listWeitereLehrer;
		_listLehrer = listLehrer;
	}

	@Override
	protected boolean pruefe() {
		final List<KursLehrer> listWeitereLehrer = _listWeitereLehrer.get();
		final List<LehrerStatistikGesamt> listLehrer = _listLehrer.get();

		if (listWeitereLehrer == null || listLehrer == null) {
			return true;
		}

		for (final KursLehrer idKurslehrer : listWeitereLehrer) {
			boolean gefunden = false;
			for (final LehrerStatistikGesamt idLehrer : listLehrer) {
				if (idKurslehrer.idLehrer == idLehrer.id) {
					gefunden = true;
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
