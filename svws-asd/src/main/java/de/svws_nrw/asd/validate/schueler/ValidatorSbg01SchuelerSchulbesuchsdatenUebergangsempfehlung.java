package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBG01: Prüft, ob die angegebene Übergangsempfehlung ein gültiger
 * und zulässiger Eintrag im Katalog ist.
 */
public final class ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {

	/** Die Übergangsempfehlung-Grundschule-ID des Schülers */
	private final @NotNull Supplier<@NotNull Long> _idUebergangsempfehlungGrundschule;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung(
			final @NotNull Supplier<@NotNull Long> idUebergangsempfehlungGrundschule,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idUebergangsempfehlungGrundschule = idUebergangsempfehlungGrundschule;

		_validatoren.add(new ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung(idUebergangsempfehlungGrundschule, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Uebergangsempfehlung uge = Uebergangsempfehlung.data().getWertByIDOrNull(_idUebergangsempfehlungGrundschule.get());

		if (uge == null) {
			this.addFehler(0, "Übergangsempfehlung des Schülers: Das Feld 'Übergangsempfehlung Jg. 5' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
