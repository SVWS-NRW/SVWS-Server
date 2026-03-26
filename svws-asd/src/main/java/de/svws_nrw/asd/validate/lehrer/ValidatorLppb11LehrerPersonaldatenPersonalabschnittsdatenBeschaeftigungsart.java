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
	private final @NotNull Supplier<Long> _idBeschaeftigungsart;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<Long> _idEinsatzstatus;

	/** Das Pflichtstundensoll */
	private final @NotNull Supplier<@AllowNull Double> _pflichtstundensoll;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idBeschaeftigungsart     	die Beschäftigungsart
	 * @param idEinsatzstatus     		der Einsatzstatus
	 * @param pflichtstundensoll     	das Pflichtstundensoll
	 * @param kontext   				der Kontext des Validators
	 */
	public ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<Long> idBeschaeftigungsart,
			final @NotNull Supplier<Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		this._idEinsatzstatus = idEinsatzstatus;
		this._pflichtstundensoll = pflichtstundensoll;
	}


	@Override
	protected boolean pruefe() {
		final Long idBeschaeftigungsart = this._idBeschaeftigungsart.get();
		final Long idEinsatzstatus = this._idEinsatzstatus.get();

		// LPPB3 ex BW15
		final Double pflichtstundensoll = this._pflichtstundensoll.get();
		if (pflichtstundensoll == null) { //Wenn der Pflichtstundensoll nicht gesetzt ist, kann diese Prüfung nicht durchgeführt werden.
			return true;
		}
		final String fehlertext3 = "Laut Ihren Angaben handelt es sich um eine voll abgeordnete Lehrkraft mit Gestellungsvertrag. Es ist zu erwarten, "
				+ "dass eine Lehrkraft mit Gestellungsvertrag Unterricht an Ihrer Schule erteilt. Bitte überprüfen Sie Ihre Angaben.";

		if ((LehrerBeschaeftigungsart.G == LehrerBeschaeftigungsart.data().getWertByID(idBeschaeftigungsart))
				&& (LehrerEinsatzstatus.A == LehrerEinsatzstatus.data().getWertByID(idEinsatzstatus))
				&& pflichtstundensoll == 0) {
			this.addFehler(3, fehlertext3);
			return false;
		}

		return true;
	}

}
