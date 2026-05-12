package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/** Das Pflichtstundensoll */
	private final @NotNull Supplier<@NotNull Double> pflichtstundensoll;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param kontext   			der Kontext des Validators
	 */
	public ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(final @NotNull Supplier<@NotNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.pflichtstundensoll = pflichtstundensoll;
	}


	@Override
	protected boolean pruefe() {
		final Double pflichtstundensollTemp = pflichtstundensoll.get();

		if (pflichtstundensollTemp != null && (pflichtstundensollTemp < 0.0 || pflichtstundensollTemp > 41.0)) {
			addFehler(1,
					"Unzulässiger Wert im Feld 'pflichtstundensoll'. Zulässig sind im Stundenmodell Werte im Bereich von 0,00 bis 41,00 Wochenstunden. "
							+ "Im Minutenmodell zwischen 0,00 und 1845,00 Minuten.");
			return false;
		}

		return true;
	}

}
