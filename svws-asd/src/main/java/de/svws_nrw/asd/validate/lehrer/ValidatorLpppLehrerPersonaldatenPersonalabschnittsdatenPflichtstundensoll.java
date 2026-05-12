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
public final class ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param idEinsatzstatus        der Einsatzstatus
	 * @param idBeschaeftigungsart   die Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus = () -> LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus.get());
		final @NotNull Supplier<@AllowNull LehrerBeschaeftigungsart> beschaeftigungsart =
				() -> LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart.get());

		_validatoren.add(new ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, einsatzstatus, beschaeftigungsart,
				kontext));
	}


	@Override
	protected boolean pruefe() {
		return true;
	}

}
