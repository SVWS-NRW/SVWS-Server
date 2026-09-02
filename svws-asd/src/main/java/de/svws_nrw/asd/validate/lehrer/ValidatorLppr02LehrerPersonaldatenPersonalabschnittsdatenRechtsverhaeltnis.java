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

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@NotNull Long> _idRechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param idSchuljahresabschnitt   der Schuljahresabschnitt
	 * @param idStaatsangehoerigkeit   die Staatsangehoerigkeit
	 * @param idRechtsverhaeltnis      das Rechtsverhaeltnis
	 * @param geburtsdatum             das Geburtsdatum
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull Supplier<@NotNull Long> idRechtsverhaeltnis,
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
	public boolean pruefe() {
		if (!LehrerEinsatzstatus.data().isGueltig(_idRechtsverhaeltnis.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Lehrer Rechtsverhältnis: Der eingetragene Wert für das Feld 'Rechtsverhältnis' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}
}
