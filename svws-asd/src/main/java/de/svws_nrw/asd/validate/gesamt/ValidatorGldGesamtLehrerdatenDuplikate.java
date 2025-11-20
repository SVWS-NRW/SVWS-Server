package de.svws_nrw.asd.validate.gesamt;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Lehrern einer Schule aus und überprüft dort, ob Duplikate in Bezug
 * auf Namen, Vornamen, Geschlecht und Geburtsdatum vorkommen.
 */
public final class ValidatorGldGesamtLehrerdatenDuplikate extends Validator {

	private final @NotNull List<LehrerStammdaten> listStammdaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listStammdaten    		die Liste aller Lehrerstammdaten
	 * @param kontext             		der Kontext des Validators
	 */
	public ValidatorGldGesamtLehrerdatenDuplikate(final @NotNull List<LehrerStammdaten> listStammdaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.listStammdaten = listStammdaten;
		_validatoren.add(new ValidatorGld00GesamtLehrerdatenDuplikate(listStammdaten, kontext));
		_validatoren.add(new ValidatorGld02GesamtLehrerdatenDuplikate(listStammdaten, kontext));
	}


	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}
}
