package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die Fachrichtung eines Lehrers aus.
 * Es wird überprüft, ob der eingetragene Wert für das ausgewählte Schuljahr gültig ist.
 */
public final class ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<Long> _idFachrichtung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung(final @NotNull Supplier<Long> idFachrichtung, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;
	}

	@Override
	protected boolean pruefe() {

		// Prüft auf fehlende Werte, inaktive Schuljahre und falsche Historien-IDs
		if (!LehrerFachrichtung.data().isGueltig(_idFachrichtung.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Lehrer Fachrichtung: Der eingetragene Wert für das Feld 'Fachrichtung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
