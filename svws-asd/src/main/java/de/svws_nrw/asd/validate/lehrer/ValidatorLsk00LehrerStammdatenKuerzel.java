package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsk00LehrerStammdatenKuerzel extends Validator {

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull Supplier<@AllowNull String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geburtsdatum des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsk00LehrerStammdatenKuerzel(final @NotNull Supplier<@AllowNull String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsk10LehrerStammdatenKuerzel(getNotNullSupplier(daten), kontext));
	}

	@Override
	protected boolean pruefe() {
		final String kuerzel = daten.get();

		if ((kuerzel == null) || (kuerzel.isEmpty())) {
			addFehler(0, "Das Feld 'Kürzel' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
