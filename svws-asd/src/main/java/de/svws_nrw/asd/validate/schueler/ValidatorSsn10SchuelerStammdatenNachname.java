package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator für das Feld Nachname bei Schülern (SSN10).
 * Prüft, ob der Nachname lediglich aus Leerzeichen besteht.
 */
public final class ValidatorSsn10SchuelerStammdatenNachname extends Validator {

	private final @NotNull Supplier<@AllowNull String> _nachname;

	/**
	 * Erstellt einen neuen Validator für den Nachnamen.
	 *
	 * @param nachname der Supplier für den Nachnamen
	 * @param kontext  der Validierungskontext
	 */
	public ValidatorSsn10SchuelerStammdatenNachname(@NotNull final Supplier<@AllowNull String> nachname, @NotNull final ValidatorKontext kontext) {
		super(kontext);
		this._nachname = nachname;
	}

	@Override
	protected boolean pruefe() {
		final String nachname = _nachname.get();
		if (nachname == null)
			return true;

		if (!nachname.isEmpty() && nachname.trim().isEmpty()) {
			this.addFehler(0, "Nachname des Schülers: Der Nachname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}

		return true;
	}

}
