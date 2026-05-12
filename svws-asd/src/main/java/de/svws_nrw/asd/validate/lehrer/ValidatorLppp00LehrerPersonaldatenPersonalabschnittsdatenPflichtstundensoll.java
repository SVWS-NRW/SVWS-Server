package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/** Das Pflichtstundensoll */
	private final @NotNull Supplier<@AllowNull Double> _pflichtstundensoll;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus        der Einsatzstatus
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull Supplier<@AllowNull LehrerBeschaeftigungsart> beschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_pflichtstundensoll = pflichtstundensoll;
		final @NotNull Supplier<@NotNull Double> pflichtstundensollNotNull = getNotNullSupplierDouble(pflichtstundensoll);
		_validatoren.add(new ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensollNotNull, einsatzstatus, kontext));
		_validatoren
				.add(new ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensollNotNull, kontext));
		_validatoren.add(
				new ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensollNotNull, einsatzstatus, beschaeftigungsart,
						kontext));
	}


	@Override
	protected boolean pruefe() {
		final Double pflichtstundensoll = _pflichtstundensoll.get();

		if (pflichtstundensoll == null) {
			addFehler(0, "Kein Wert im Feld 'pflichtstundensoll'.");
			return false;
		}

		return true;
	}

}
