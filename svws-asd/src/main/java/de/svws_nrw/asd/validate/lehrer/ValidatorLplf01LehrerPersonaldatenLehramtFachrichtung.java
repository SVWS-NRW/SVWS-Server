package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die Fachrichtung eines Lehrers aus.
 * Es wird überprüft, ob die ID der Fachrichtung im Katalog existiert und somit zulässig ist.
 */
public final class ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<Long> _idFachrichtung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung(final @NotNull Supplier<Long> idFachrichtung, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;

		_validatoren.add(new ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung(idFachrichtung, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idFachrichtung = _idFachrichtung.get();

		if (LehrerFachrichtung.data().getSchluesselByIDOrNull(idFachrichtung) == null) {
			addFehler(0, "Lehrer Fachrichtung: Das Feld 'Fachrichtung' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
