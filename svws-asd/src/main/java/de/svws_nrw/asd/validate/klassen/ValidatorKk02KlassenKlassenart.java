package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KK02: Prüft, ob die angegebene Klassenart einer Klasse
 * im ausgewählten Schuljahr zeitlich gültig ist.
 */
public final class ValidatorKk02KlassenKlassenart extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKlassenart;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart  KlassenartID
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorKk02KlassenKlassenart(
			final @NotNull Supplier<@NotNull Long> idKlassenart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
	}

	@Override
	protected boolean pruefe() {

		// Prüft auf fehlende Werte, inaktive Schuljahre und falsche Historien-IDs
		if (!Klassenart.data().isGueltig(_idKlassenart.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Art der Klasse: Der eingetragene Wert für das Feld 'Klassenart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;

	}
}
