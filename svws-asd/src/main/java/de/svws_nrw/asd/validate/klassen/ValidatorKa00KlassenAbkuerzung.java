package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Kürzel der Klasse aus
 */
public final class ValidatorKa00KlassenAbkuerzung extends Validator {

	/** das Kürzel der Klasse */
	private final @NotNull Supplier<@AllowNull String> kuerzel;
	/** eine Liste der Kürzel der Klassen der Schule */
	private final @NotNull Supplier<@AllowNull List<String>> listKuerzel;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel     das Kürzel der Klasse
	 * @param listKuerzel eine Liste der Kürzel der Klassen der Schule
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorKa00KlassenAbkuerzung(final @NotNull Supplier<@AllowNull String> kuerzel, final @NotNull Supplier<@AllowNull List<String>> listKuerzel,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.kuerzel = kuerzel;
		this.listKuerzel = listKuerzel;
		_validatoren.add(new ValidatorKa10KlassenAbkuerzung(getNotNullSupplier(kuerzel), listKuerzel, kontext));
		_validatoren.add(new ValidatorKa11KlassenAbkuerzung(getNotNullSupplier(kuerzel), kontext));
	}

	@Override
	protected boolean pruefe() {
		final String klassenkuerzel = kuerzel.get();

		if ((klassenkuerzel == null) || (klassenkuerzel.isEmpty())) {
			addFehler(0, "Kürzel der Klasse: Der eingetragene Wert darf nicht leer sein.");
			return false;
		}

		return true;
	}

}
