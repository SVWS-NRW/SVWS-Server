package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die Fachrichtung eines Lehrers aus.
 * Es wird überprüft, ob ein Wert eingetragen ist.
 */
public final class ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/** Das aktuelle Schuljahr des Lehrers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<@AllowNull Long> _idFachrichtung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr        Schuljahr
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> idFachrichtung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_schuljahr = schuljahr;
		_idFachrichtung = idFachrichtung;
	}

	@Override
	protected boolean pruefe() {

		// Prüft ob Eintragungen in Fachrichtung sind
		final @AllowNull Long fachrichtungID = _idFachrichtung.get();

		if (fachrichtungID == null) {
			addFehler(0,
					"Fachrichtung des Lehrers: Bei allen Lehrkräften muss das Feld 'Fachrichtung' besetzt sein.");
			return false;
		}

		return true;
	}

}
