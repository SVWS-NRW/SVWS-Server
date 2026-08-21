package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator Kl10: Prüft, ob Klassenleitungen eingetragen sind.
 */
public final class ValidatorKl10KlassenKlassenleitung extends Validator {

	private final @NotNull Supplier<@NotNull List<Long>> _klassenLeitungen;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenLeitungen   Klassenleitungen
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorKl10KlassenKlassenleitung(
			final @NotNull Supplier<@NotNull List<Long>> klassenLeitungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenLeitungen = klassenLeitungen;

	}

	@Override
	protected boolean pruefe() {
		final @NotNull List<Long> klassenLeitungen = _klassenLeitungen.get();

		if (klassenLeitungen.size() == 0) {
			addFehler(0, "Leitung der Klasse: Zu jeder Klasse muss mindestens eine Klassenleitung vorliegen.");
			return false;
		}

		return true;
	}

}
