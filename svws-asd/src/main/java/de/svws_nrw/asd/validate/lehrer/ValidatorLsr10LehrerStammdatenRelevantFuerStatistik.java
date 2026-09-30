package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsr10LehrerStammdatenRelevantFuerStatistik extends Validator {

	/** Das Abgangsdatum */
	private final @NotNull Supplier<@AllowNull String> _abgangsdatum;

	/** Relevant für Statistik */
	private final @NotNull Supplier<@AllowNull Boolean> _istRelevantFuerStatistik;

	private @NotNull ValidatorKontext _kontextNeu;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param istRelevantFuerStatistik     ist relevant für Statistik
	 * @param abgangsdatum                 das Abgangsdatum des Lehrers
	 * @param kontext                      der Kontext des Validators
	 */
	public ValidatorLsr10LehrerStammdatenRelevantFuerStatistik(final @NotNull Supplier<@AllowNull Boolean> istRelevantFuerStatistik, final @NotNull Supplier<@AllowNull String> abgangsdatum, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_istRelevantFuerStatistik = istRelevantFuerStatistik;
		_abgangsdatum = abgangsdatum;
		_kontextNeu = kontext;
	}

	@Override
	protected boolean pruefe() {

		if (_istRelevantFuerStatistik.get() == null) {
			return true;
		}
		final @AllowNull Boolean istRelevantFuerStatistik = _istRelevantFuerStatistik.get();


		// Bestimme das Abgangsdatum
		DateManager abgangsdatum = null;
		@NotNull String errorMsg = "";
		try {
			abgangsdatum = DateManager.from(_abgangsdatum.get());
		} catch (final InvalidDateException e) {
			errorMsg = e.getMessage();
		}

		if (abgangsdatum == null) {
			return true;
		}


		if (istRelevantFuerStatistik) {
			 if ((abgangsdatum.getJahr() < _kontextNeu.getSchuljahr()) || (((abgangsdatum.getJahr() == _kontextNeu.getSchuljahr())) && ((abgangsdatum.getMonat() < 8)))) {
				addFehler(1, "Statistikrelevanz des Lehrers: Das Feld 'Ist relevant für Statistik' darf nicht ausgewählt sein, wenn die Lehrkraft im vorherigen Schuljahr abgegangen ist." + errorMsg);
				return false;
			}
		}

		return true;
	}
}
