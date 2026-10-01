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
public final class ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idStaatsangehoerigkeit;
	private static final @NotNull String FEHLERTEXT =
			"Der eingetragene Wert für das Feld 'Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   ID der Staatsangehörigkeit des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(
			final @NotNull Supplier<@NotNull Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
	}

	@Override
	protected boolean pruefe() {
		if (!Nationalitaeten.data().isGueltig(_idStaatsangehoerigkeit.get(), kontext().getSchuljahr())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
