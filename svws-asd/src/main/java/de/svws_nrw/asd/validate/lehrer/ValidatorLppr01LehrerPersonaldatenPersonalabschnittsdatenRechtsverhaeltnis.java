package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

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
	public ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull Supplier<@AllowNull Long> idRechtsverhaeltnis,
			final @NotNull Supplier<DateManager> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_idRechtsverhaeltnis = idRechtsverhaeltnis;

		final @NotNull Supplier<LehrerRechtsverhaeltnis> rechtsverhaeltnisNotNull =
				() -> LehrerRechtsverhaeltnis.data().getWertByID(getNotNullSupplierLong(idRechtsverhaeltnis).get());
		final @NotNull Supplier<@NotNull String> staatsangehoerigkeitSchluessel =
				getNotNullSupplier(() -> Nationalitaeten.data().getSchluesselByIDOrNull(idStaatsangehoerigkeit.get()));

		_validatoren.add(new ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(staatsangehoerigkeitSchluessel, rechtsverhaeltnisNotNull, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Rechtsverhältnis. Ist dieses nicht angegeben, so wird im Folgenden von einem sonstigen Rechtsverhältnis ausgegangen
		final Long idRechtsverhaeltnis = _idRechtsverhaeltnis.get();
		final LehrerRechtsverhaeltnis rv = (idRechtsverhaeltnis == null) ? null : LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis);

		if (rv == null) {
			addFehler(0, "Kein gültiger Wert im Feld 'rechtsverhaeltnis'.");
			return false;
		}

		return true;
	}

}
