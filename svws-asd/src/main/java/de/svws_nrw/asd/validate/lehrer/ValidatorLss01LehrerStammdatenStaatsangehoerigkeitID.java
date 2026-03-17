package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten                 die StaatsangehoerigkeitID des Lehrers
	 * @param idRechtsverhaeltnis   das Rechtsverhaeltnis des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<String> daten, final @NotNull Supplier<Long> idRechtsverhaeltnis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(daten, kontext));
		_validatoren.add(new ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(daten, idRechtsverhaeltnis, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Nationalitaeten staatsangehoerigkeitID = Nationalitaeten.getByDESTATIS(daten.get());

		if (staatsangehoerigkeitID == null) {
			this.addFehler(0, "Das Feld 'Staatsangehörigkeit' muss zulässig sein. ");
			return false;
		}

		return true;
	}

}
