package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLplLehrerPersonaldatenLehramt extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonaldaten lehrerPersonaldaten;

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull DateManager geburtsdatum;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param geburtsdatum          das Geburtsdatum des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplLehrerPersonaldatenLehramt(final @NotNull LehrerPersonaldaten lehrerPersonaldaten, final @NotNull DateManager geburtsdatum, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
		this.geburtsdatum = geburtsdatum;
		_validatoren.add(new ValidatorLpl00LehrerPersonaldatenLehramt(lehrerPersonaldaten, kontext));
		_validatoren.add(new ValidatorLpl01LehrerPersonaldatenLehramt(lehrerPersonaldaten, kontext));
		_validatoren.add(new ValidatorLpl02LehrerPersonaldatenLehramt(lehrerPersonaldaten, kontext));
		_validatoren.add(new ValidatorLpl03LehrerPersonaldatenLehramt(lehrerPersonaldaten, geburtsdatum, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
