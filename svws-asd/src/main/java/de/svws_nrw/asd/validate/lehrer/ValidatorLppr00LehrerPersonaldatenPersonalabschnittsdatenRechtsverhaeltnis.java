package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Das Rechtsverhältnis */
	private final @NotNull Supplier<@AllowNull Long> _idRechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param idRechtsverhaeltnis      die ID des Rechtsverhältnis
	 * @param geburtsdatum             das Geburtsdatum des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull Supplier<@AllowNull Long> idRechtsverhaeltnis,
			final @NotNull Supplier<DateManager> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_idRechtsverhaeltnis = idRechtsverhaeltnis;

		_validatoren.add(new ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, idStaatsangehoerigkeit, getNotNullSupplierLong(idRechtsverhaeltnis), geburtsdatum, kontext));
	}

	@Override
	public boolean pruefe() {

		// Bestimme das Rechtsverhaeltnis.
		final Long idRechtsverhaeltnis = _idRechtsverhaeltnis.get();

		if (idRechtsverhaeltnis == null) {
			addFehler(0, "Lehrer Rechtsverhältnis: Das Feld darf nicht leer sein.");
			return false;
		}

		return true;
	}

}
