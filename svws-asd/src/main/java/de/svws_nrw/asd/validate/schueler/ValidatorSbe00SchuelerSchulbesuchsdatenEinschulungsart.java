package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBE00: Prüft, ob für den Schüler eine Einschulungsart angegeben wurde.
 */
public final class ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	private final @NotNull Supplier<SchuelerSchulbesuchsdaten> _schulbesuchsdaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param schulbesuchsdaten ein Supplier für die Schulbesuchsdaten
	 * @param kontext           der Kontext des Validators
	 */
	public ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(
			final @NotNull Supplier<SchuelerSchulbesuchsdaten> schulbesuchsdaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._schulbesuchsdaten = schulbesuchsdaten;
		_validatoren.add(new ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart(schulbesuchsdaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		final SchuelerSchulbesuchsdaten daten = _schulbesuchsdaten.get();

		if ((daten == null) || (daten.idEinschulungsartGrundschule == null) || (daten.idEinschulungsartGrundschule < 0)) {
			addFehler(0, "Einschulungsart des Schülers: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
