package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private final @NotNull Supplier<@NotNull String> _staatsangehoerigkeitSchluessel;
	private final int schuljahr;
	private static final @NotNull String FEHLERTEXT =
			"Der eingetragene Wert für das Feld 'Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitSchluessel              der staatsangehoerigkeitSchluessel des Lehrers
	 * @param schuljahr
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<@NotNull String> staatsangehoerigkeitSchluessel,
			final @NotNull Supplier<Integer> schuljahr, final @NotNull ValidatorKontext kontext) {

		super(kontext);

		this._staatsangehoerigkeitSchluessel = staatsangehoerigkeitSchluessel;
		this.schuljahr = schuljahr.get();
	}

	@Override
	protected boolean pruefe() {
		final Schulform schulform = kontext().getSchulform();

		if (Nationalitaeten.data().getBySchuljahrAndSchulformAndSchluessel(this.schuljahr, schulform, this._staatsangehoerigkeitSchluessel.get()) == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
