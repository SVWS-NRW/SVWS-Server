package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die Fachrichtung eines Lehrers aus.
 * Es wird überprüft, ob das Feld für die Fachrichtung nicht leer ist.
 */
public final class ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<Long> _idFachrichtung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung(final @NotNull Supplier<Long> idFachrichtung, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;

		_validatoren.add(new ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung(getNotNullSupplierLong(idFachrichtung), kontext));

	}

	@Override
	protected boolean pruefe() {
		final Long idFachrichtung = _idFachrichtung.get();

		if (idFachrichtung == null) {
			addFehler(0, "Lehrer Fachrichtung: Das Feld darf nicht leer sein.");
			return false;
		}

		return true;
	}

}
