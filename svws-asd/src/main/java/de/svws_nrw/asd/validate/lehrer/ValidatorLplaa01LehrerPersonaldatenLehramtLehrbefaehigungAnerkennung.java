package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Feld für den Anerkennungsgrund einen zulässigen Katalogwert enthält.
 */
public final class ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {

	/** Die Katalog-ID des Anerkennungsgrunds. */
	private final @NotNull Supplier<@AllowNull Long> _idAnerkennungsgrund;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(
			final @NotNull Supplier<@AllowNull Long> idAnerkennungsgrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idAnerkennungsgrund = idAnerkennungsgrund;

		_validatoren.add(new ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(idAnerkennungsgrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idAnerkennungsgrund = _idAnerkennungsgrund.get();

		if (idAnerkennungsgrund == null) {
			return true;
		}

		if (LehrerLehramtAnerkennung.data().getWertByIDOrNull(idAnerkennungsgrund) == null) {
			addFehler(0, "Das Feld 'Anerkennungsgrund Lehramt' muss zulässig besetzt sein.");
			return false;
		}

		return true;
	}

}
