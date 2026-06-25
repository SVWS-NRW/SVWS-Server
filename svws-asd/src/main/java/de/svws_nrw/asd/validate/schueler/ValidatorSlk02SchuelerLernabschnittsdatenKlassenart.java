package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Klassenart eines Schülers aus.
 */

public final class ValidatorSlk02SchuelerLernabschnittsdatenKlassenart extends Validator {

	/** Klassenart */
	private final @NotNull Supplier<@NotNull Long> _idKlassenart;

	private static final @NotNull String FEHLERTEXT =
			"Schüler Klassenart: Der eingetragene Wert für das Feld 'Klassenart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Klassenart im Katalog.
	 *
	 * @param idKlassenart  die Klassenart ID
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorSlk02SchuelerLernabschnittsdatenKlassenart(
			final @NotNull Supplier<@NotNull Long> idKlassenart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idKlassenart = idKlassenart;

	}

	@Override
	protected boolean pruefe() {
		if (!Klassenart.data().isGueltig(_idKlassenart.get(), kontext().getSchuljahr())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
