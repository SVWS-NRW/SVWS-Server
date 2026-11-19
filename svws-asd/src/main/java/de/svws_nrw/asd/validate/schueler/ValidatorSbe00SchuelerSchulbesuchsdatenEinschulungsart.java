package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBE00: Prüft, ob für den Schüler eine Einschulungsart angegeben wurde.
 */
public final class ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	/** Die ID der Einschulungsart an der Grundschule */
	private final @NotNull Supplier<@AllowNull Long> _idEinschulungsartGrundschule;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param kontext                        der Kontext des Validators
	 */
	public ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(
			final @NotNull Supplier<@AllowNull Long> idEinschulungsartGrundschule,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEinschulungsartGrundschule = idEinschulungsartGrundschule;
		_validatoren.add(new ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart(idEinschulungsartGrundschule, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idEinschulungsartGrundschule = _idEinschulungsartGrundschule.get();

		if ((idEinschulungsartGrundschule == null) || (idEinschulungsartGrundschule < 0)) {
			addFehler(0, "Einschulungsart des Schülers: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
