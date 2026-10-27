package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBG02: Prüft, ob der eingetragene Wert für das Feld 'Übergangsempfehlung'
 * für das ausgewählte Schuljahr zeitlich gültig ist.
 */
public final class ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {

	/** Die Übergangsempfehlung-Grundschule-ID des Schülers */
	private final @NotNull Supplier<Long> _idUebergangsempfehlungGrundschule;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public ValidatorSbg02SchuelerSchulbesuchsdatenUebergangsempfehlung(
			final @NotNull Supplier<@NotNull Long> idUebergangsempfehlungGrundschule,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);
		_idUebergangsempfehlungGrundschule = idUebergangsempfehlungGrundschule;
	}

	@Override
	protected boolean pruefe() {

		if (!Uebergangsempfehlung.data().isGueltig(_idUebergangsempfehlungGrundschule.get(), kontext().getSchuljahr())) {
			addFehler(0, "Übergangsempfehlung des Schülers: Der eingetragene Wert für das Feld 'Übergangsempfehlung Jg. 5' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
