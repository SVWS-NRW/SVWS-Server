package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschäftigungsart */
	private final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> _beschaeftigungsart;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> _einsatzstatus;

	/** Das Pflichtstundensoll */
	private final @NotNull Supplier<@AllowNull Double> _pflichtstundensoll;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param beschaeftigungsart     	die Beschäftigungsart
	 * @param einsatzstatus     		der Einsatzstatus
	 * @param pflichtstundensoll     	das Pflichtstundensoll
	 * @param kontext   				der Kontext des Validators
	 */
	public ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> beschaeftigungsart,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_beschaeftigungsart = beschaeftigungsart;
		_einsatzstatus = einsatzstatus;
		_pflichtstundensoll = pflichtstundensoll;
	}


	@Override
	protected boolean pruefe() {
		final LehrerBeschaeftigungsart beschaeftigungsart = _beschaeftigungsart.get();
		final LehrerEinsatzstatus einsatzstatus = _einsatzstatus.get();
		final Double pflichtstundensoll = _pflichtstundensoll.get();
		final String fehlertext3 = "Laut Ihren Angaben handelt es sich um eine voll abgeordnete Lehrkraft mit Gestellungsvertrag. Es ist zu erwarten, "
				+ "dass eine Lehrkraft mit Gestellungsvertrag Unterricht an Ihrer Schule erteilt. Bitte überprüfen Sie Ihre Angaben.";

		if (LehrerBeschaeftigungsart.G == beschaeftigungsart
				&& LehrerEinsatzstatus.A == einsatzstatus
				&& pflichtstundensoll == 0) {
			addFehler(3, fehlertext3);
			return false;
		}

		return true;
	}

}
