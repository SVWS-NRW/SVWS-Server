package de.svws_nrw.asd.validate.schule;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSss01SchuleStammdatenSchulform extends Validator {

	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Schulform
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSss01SchuleStammdatenSchulform(final @NotNull Supplier<String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {

		// Prüfe, ob die Schulform gültig gesetzt ist oder nicht
		final @NotNull String schulformKrz = daten.get();

		try {
			return Schulform.data().getWertByKuerzel(schulformKrz) == null;
		} catch (@SuppressWarnings("unused") final CoreTypeException e) {
			addFehler(1, "Das Kürzel für die Schulform ist ungültig.");
			return false;
		}
	}

}
