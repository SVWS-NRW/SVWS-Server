package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der Anerkennungsgrund im aktuellen Schuljahr historisch gültig ist.
 */
public final class ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {

	/** Die Katalog-ID des Anerkennungsgrunds. */
	private final @NotNull Supplier<@AllowNull Long> _idAnerkennungsgrund;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(
			final @NotNull Supplier<@AllowNull Long> idAnerkennungsgrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idAnerkennungsgrund = idAnerkennungsgrund;
	}

	@Override
	protected boolean pruefe() {
		final Long idAnerkennungsgrund = _idAnerkennungsgrund.get();

		if (idAnerkennungsgrund == null) {
			return true;
		}

		final int schuljahr = kontext().getSchuljahr();

		if (!LehrerLehramtAnerkennung.data().isGueltig(idAnerkennungsgrund, schuljahr)) {
			addFehler(0, "Der eingetragene Wert für das Feld 'Anerkennung Lehramt' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
