package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für die Fachrichtung eines Lehrers aus.
 * Es wird überprüft, ob die ID der Fachrichtung im Katalog existiert und somit zulässig ist.
 */
public final class ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/** Die Katalog-ID der Fachrichtung. */
	private final @NotNull Supplier<@AllowNull Long> _idFachrichtung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr        Schuljahr
	 * @param idLehramt        die Katalog-ID des Lehramtes
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> idLehramt,
			final @NotNull Supplier<@AllowNull Long> idFachrichtung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;

		_validatoren.add(new ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung(schuljahr, idLehramt, idFachrichtung, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idFachrichtung = _idFachrichtung.get();

		//Null Prüfung ist nötig, da es für diesen Fehler keinen 00er Prüfer gibt
		if (idFachrichtung != null) {
			if (LehrerFachrichtung.data().getSchluesselByIDOrNull(idFachrichtung) == null) {
				addFehler(0, "Lehrer Fachrichtung: Das Feld 'Fachrichtung' muss zulässig sein.");
				return false;
			}
		}
		return true;
	}

}
