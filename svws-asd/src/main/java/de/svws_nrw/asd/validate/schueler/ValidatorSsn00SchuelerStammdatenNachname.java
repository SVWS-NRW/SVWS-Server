package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator für das Feld Nachname bei Schülern (SSN00).
 * Prüft, ob der Nachname vorhanden ist (Pflichtfeld für fast alle Schulformen).
 */
public final class ValidatorSsn00SchuelerStammdatenNachname extends Validator {

	private final @NotNull Supplier<@AllowNull String> _nachname;

	/**
	 * Erstellt einen neuen Validator für den Nachnamen.
	 *
	 * @param nachname der Supplier für den Nachnamen
	 * @param kontext  der Validierungskontext
	 */
	public ValidatorSsn00SchuelerStammdatenNachname(@NotNull final Supplier<@AllowNull String> nachname, @NotNull final ValidatorKontext kontext) {
		super(kontext);
		this._nachname = nachname;
		_validatoren.add(new ValidatorSsn10SchuelerStammdatenNachname(nachname, kontext));

	}

	@Override
	protected boolean pruefe() {
		final String nachname = _nachname.get();
		if ((nachname == null) || nachname.isBlank()) {
			this.addFehler(0, "Nachname des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

}
