package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Klassenart eines Schülers aus.
 */

public final class ValidatorSlk01SchuelerLernabschnittsdatenKlassenart extends Validator {

	/** Das Geburtsland */
	private final @NotNull Supplier<@NotNull Long> _idKlassenart;
	private static final @NotNull String FEHLERTEXT = "Klassenart des Schülers: Das Feld 'Klassenart' muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Klassenart im Katalog.
	 *
	 * @param idKlassenart   die Klassenart ID
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorSlk01SchuelerLernabschnittsdatenKlassenart(
			final @NotNull Supplier<@NotNull Long> idKlassenart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idKlassenart = idKlassenart;

		_validatoren.add(
				new ValidatorSlk02SchuelerLernabschnittsdatenKlassenart(idKlassenart, kontext));

	}

	@Override
	protected boolean pruefe() {

		// Bestimme Klassenart.
		final @NotNull Long idKlassenart = _idKlassenart.get();
		final @AllowNull Klassenart kArt = Klassenart.data().getWertByIDOrNull(idKlassenart);

		if (kArt == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}


		return true;
	}
}
