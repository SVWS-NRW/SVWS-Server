package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Feld für den Anerkennungsgrund nicht leer ist.
 */
public final class ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {

	/** Die Katalog-ID des Anerkennungsgrunds. */
	private final @NotNull Supplier<@AllowNull Long> _idAnerkennungsgrund;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(
			final @NotNull Supplier<@AllowNull Long> idAnerkennungsgrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idAnerkennungsgrund = idAnerkennungsgrund;

		_validatoren.add(new ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(idAnerkennungsgrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idAnerkennungsgrund = _idAnerkennungsgrund.get();

		if (idAnerkennungsgrund == null) {
			addFehler(0, "Das Feld 'Anerkennungsgrund Lehramt' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
