package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBG00: Prüft, ob für den Schüler eine Übergangsempfehlung angegeben wurde.
 */
public final class ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {

	/** Die Übergangsempfehlung-Grundschule-ID des Schülers */
	final @NotNull Supplier<@AllowNull Long> _idUebergangsempfehlungGrundschule;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung(
			final @NotNull Supplier<@AllowNull Long> idUebergangsempfehlungGrundschule,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idUebergangsempfehlungGrundschule = idUebergangsempfehlungGrundschule;

		_validatoren.add(new ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung(getNotNullSupplierLong(idUebergangsempfehlungGrundschule), kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idUebergangsempfehlungGrundschule = _idUebergangsempfehlungGrundschule.get();

		if (idUebergangsempfehlungGrundschule == null) {
			addFehler(0, "Übergangsempfehlung des Schülers: Das Feld 'Übergangsempfehlung Jg. 5' darf nicht leer sein.");
			return false;
		}

		return true;
	}

}
