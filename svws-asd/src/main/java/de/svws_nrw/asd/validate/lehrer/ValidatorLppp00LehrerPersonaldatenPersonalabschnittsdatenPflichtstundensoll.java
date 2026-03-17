package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

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
	 * @param idEinsatzstatus        der Einsatzstatus
	 * @param idBeschaeftigungsart   die Beschaeftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._pflichtstundensoll = pflichtstundensoll;
		_validatoren.add(new ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, kontext));
		_validatoren.add(new ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, idEinsatzstatus, kontext));
		_validatoren.add(new ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, idEinsatzstatus, idBeschaeftigungsart, kontext));
	}


	@Override
	protected boolean pruefe() {
		final Double pflichtstundensoll = this._pflichtstundensoll.get();

		if (pflichtstundensoll == null) {
			this.addFehler(0, "Kein Wert im Feld 'pflichtstundensoll'.");
			return false;
		}

		return true;
	}

}
