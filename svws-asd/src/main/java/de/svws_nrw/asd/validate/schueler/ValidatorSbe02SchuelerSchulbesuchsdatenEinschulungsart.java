package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBE02: Prüft, ob der eingetragene Wert für das Feld 'Einschulungsart'
 * für das ausgewählte Schuljahr zeitlich gültig ist.
 */
public final class ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	/** Die ID der Einschulungsart an der Grundschule */
	private final @NotNull Supplier<@AllowNull Long> _idEinschulungsartGrundschule;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param kontext                        der Kontext des Validators
	 */
	public ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(
			final @NotNull Supplier<@AllowNull Long> idEinschulungsartGrundschule,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEinschulungsartGrundschule = idEinschulungsartGrundschule;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idEinschulungsartGrundschule = _idEinschulungsartGrundschule.get();

		if ((idEinschulungsartGrundschule == null) || (idEinschulungsartGrundschule < 0)) {
			return true;
		}

		final Einschulungsart art = Einschulungsart.data().getWertByIDOrNull(idEinschulungsartGrundschule);
		if (art == null) {
			return true;
		}

		// Zeitliche Gültigkeit prüfen
		if (art.daten(kontext().getSchuljahr()) == null) {
			addFehler(0,
					"Einschulungsart des Schülers: Der eingetragene Wert für das Feld 'Einschulungsart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
