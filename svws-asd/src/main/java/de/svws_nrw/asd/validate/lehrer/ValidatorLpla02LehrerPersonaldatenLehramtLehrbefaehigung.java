package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Lehrbefähigung im zeitlichen Raum der JSON-Datei liegt.
 */
public final class ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** IDLehrbefähigung */
	private final @NotNull Supplier<@NotNull Long> _idLehrbefaehigung;

	/** Lehrbefähigung */
	private final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> _lehrbefaehigung;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param idLehrbefaehigung   eine idLehrbefaehigung des Lehrers
	 * @param lehrerLehramt       das Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@NotNull Long> idLehrbefaehigung,
			final @NotNull Supplier<@AllowNull LehrerLehramt> lehrerLehramt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idLehrbefaehigung = idLehrbefaehigung;
		_lehrbefaehigung = () -> LehrerLehrbefaehigung.data().getWertByID(idLehrbefaehigung.get());

		_validatoren.add(new ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(_lehrbefaehigung, lehrerLehramt, kontext));
		_validatoren.add(new ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(_lehrbefaehigung, lehrerLehramt, kontext));
		_validatoren.add(new ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung(_lehrbefaehigung, lehrerLehramt, kontext));
	}

	@Override
	protected boolean pruefe() {

		if (!LehrerLehrbefaehigung.data().isGueltig(_idLehrbefaehigung.get(), kontext().getSchuljahr())) {
			addFehler(0, "Der eingetragene Wert für das Feld 'Lehrbefähigungen' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
		return false;
		}


		return true;
	}
}
