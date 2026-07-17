package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die EP-Jahre eines Schülers aus.
 */

public final class ValidatorSle02SchuelerLernabschnittsdatenEpJahre extends Validator {

	/** EP - Jahre */
	private final @NotNull Supplier<@NotNull Long> _idEpJahre;

	private static final @NotNull String FEHLERTEXT =
			"EP-Jahr des Schülers: Der eingetragene Wert für das Feld 'EP-Jahr' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der EP-Jahre.
	 *
	 * @param idEpJahre   EPJahreID
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorSle02SchuelerLernabschnittsdatenEpJahre(
			final @NotNull Supplier<@NotNull Long> idEpJahre,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEpJahre = idEpJahre;

	}

	@Override
	protected boolean pruefe() {
		if (!PrimarstufeSchuleingangsphaseBesuchsjahre.data().isGueltig(_idEpJahre.get(), kontext().getSchuljahr())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
