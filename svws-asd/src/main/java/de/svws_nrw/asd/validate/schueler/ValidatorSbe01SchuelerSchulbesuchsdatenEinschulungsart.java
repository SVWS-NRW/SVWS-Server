package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBE01: Prüft, ob die angegebene Einschulungsart ein gültiger
 * und zulässiger Eintrag im Katalog ist.
 */
public final class ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	/** Die ID der Einschulungsart an der Grundschule */
	private final @NotNull Supplier<@AllowNull Long> _idEinschulungsartGrundschule;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param kontext                        der Kontext des Validators
	 */
	public ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart(
			final @NotNull Supplier<@AllowNull Long> idEinschulungsartGrundschule,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEinschulungsartGrundschule = idEinschulungsartGrundschule;
		_validatoren.add(new ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(idEinschulungsartGrundschule, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idEinschulungsartGrundschule = _idEinschulungsartGrundschule.get();

		if ((idEinschulungsartGrundschule == null) || (idEinschulungsartGrundschule < 0)) {
			return true;
		}

		if (Einschulungsart.data().getWertByIDOrNull(idEinschulungsartGrundschule) == null) {
			addFehler(0, "Einschulungsart des Schülers: Das Feld 'Einschulungsart' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
