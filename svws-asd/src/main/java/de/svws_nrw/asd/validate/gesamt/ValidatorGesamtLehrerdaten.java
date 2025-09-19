package de.svws_nrw.asd.validate.gesamt;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Lehrern einer Schule aus.
 */
public final class ValidatorGesamtLehrerdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listStammdaten      die Liste aller Lehrerstammdaten
	 * @param listPersonaldaten   die Liste aller Lehrerpersonaldaten
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGesamtLehrerdaten(final @NotNull List<LehrerStammdaten> listStammdaten, final @NotNull List<LehrerPersonaldaten> listPersonaldaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		// Variante 1: Validator für Duplikatprüfung
	}


	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
