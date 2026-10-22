package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob der angegebene Einsatzstatus existiert.
 */
public final class ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Das aktuelle Schuljahr des Lehrers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@NotNull Long> _idRechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param schuljahr                Schuljahr
	 * @param idStaatsangehoerigkeit   die Staatsangehoerigkeit
	 * @param idRechtsverhaeltnis      das Rechtsverhaeltnis
	 * @param geburtsdatum             das Geburtsdatum
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull Supplier<@NotNull Long> idRechtsverhaeltnis,
			final @NotNull Supplier<@NotNull DateManager> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_schuljahr = schuljahr;
		_idRechtsverhaeltnis = idRechtsverhaeltnis;

		final @NotNull Supplier<LehrerRechtsverhaeltnis> rechtsverhaeltnisNotNull =
				() -> LehrerRechtsverhaeltnis.data().getWertByID(getNotNullSupplierLong(idRechtsverhaeltnis).get());
		final @NotNull Supplier<@NotNull String> staatsangehoerigkeitSchluessel =
				getNotNullSupplier(() -> Nationalitaeten.data().getSchluesselByIDOrNull(idStaatsangehoerigkeit.get()));

		_validatoren.add(new ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(schuljahr, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(schuljahr, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(schuljahr, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(schuljahr, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(staatsangehoerigkeitSchluessel, rechtsverhaeltnisNotNull, kontext));

	}

	@Override
	public boolean pruefe() {
		if (!LehrerEinsatzstatus.data().isGueltig(_idRechtsverhaeltnis.get(), _schuljahr.get())) {
			addFehler(0,
					"Lehrer Rechtsverhältnis: Der eingetragene Wert für das Feld 'Rechtsverhältnis' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}
}
