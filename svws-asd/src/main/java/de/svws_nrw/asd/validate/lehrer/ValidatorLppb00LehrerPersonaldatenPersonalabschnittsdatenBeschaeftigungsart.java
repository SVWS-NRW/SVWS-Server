package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschaeftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschaeftigungsart */
	private final @NotNull Supplier<@AllowNull LehrerBeschaeftigungsart> _beschaeftigungsart;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus        der Einsatzstatus
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<@AllowNull LehrerBeschaeftigungsart> beschaeftigungsart,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_beschaeftigungsart = beschaeftigungsart;
		final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> beschaeftigungsartNotNull = getNotNullObjectSupplier(beschaeftigungsart);
		_validatoren.add(new ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, kontext));
		_validatoren.add(
				new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, pflichtstundensoll,
						kontext));

	}


	@Override
	protected boolean pruefe() {
		final LehrerBeschaeftigungsart beschaeftigungsart = _beschaeftigungsart.get();

		if (beschaeftigungsart == null) {
			addFehler(0, "Kein Wert im Feld 'beschaeftigungsart'.");
			return false;
		}

		return true;
	}

}
