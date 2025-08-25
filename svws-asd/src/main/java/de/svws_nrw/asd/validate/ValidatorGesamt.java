package de.svws_nrw.asd.validate;

import java.util.HashMap;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt;
import de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonaldaten;
import de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdaten;
import de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdaten;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf alle Daten einer Schule aus.
 */
public final class ValidatorGesamt extends Validator {

	private final @NotNull SchuleStatistikdatenGesamt daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorGesamt(final @NotNull SchuleStatistikdatenGesamt daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorSchuleStammdaten(kontext));

		final HashMap<Long, LehrerStammdaten> mapStammdaten = new HashMap<>();
		for (final LehrerStammdaten lehrerStammdaten : daten.lehrerStammdaten) {
			_validatoren.add(new ValidatorLehrerStammdaten(lehrerStammdaten, kontext));
			mapStammdaten.put(lehrerStammdaten.id, lehrerStammdaten);
		}

		for (final LehrerPersonaldaten lehrerPersonaldaten : daten.lehrerPersonaldaten) {
			final LehrerStammdaten stammdaten = mapStammdaten.get(lehrerPersonaldaten.id);
			if (stammdaten == null)
				continue;
			_validatoren.add(new ValidatorLehrerPersonaldaten(lehrerPersonaldaten, stammdaten, kontext));
		}
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
