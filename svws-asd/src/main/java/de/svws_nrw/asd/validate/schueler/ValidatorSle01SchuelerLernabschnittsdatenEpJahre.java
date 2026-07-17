package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die EP-Jahre eines Schülers aus.
 */

public final class ValidatorSle01SchuelerLernabschnittsdatenEpJahre extends Validator {

	/** EP - Jahre */
	private final @NotNull Supplier<@NotNull Long> _idEpJahre;
	private static final @NotNull String FEHLERTEXT = "EP-Jahr des Schülers: Das Feld 'EP-Jahr' muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der EP-Jahre.
	 *
	 * @param idEpJahre   EPJahreID
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorSle01SchuelerLernabschnittsdatenEpJahre(
			final @NotNull Supplier<@NotNull Long> idEpJahre,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEpJahre = idEpJahre;

		_validatoren.add(
				new ValidatorSle02SchuelerLernabschnittsdatenEpJahre(idEpJahre, kontext));

	}

	@Override
	protected boolean pruefe() {
		final @NotNull Long idEpJahre = _idEpJahre.get();
		final @AllowNull PrimarstufeSchuleingangsphaseBesuchsjahre epJahre = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(idEpJahre);

		if (epJahre == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
