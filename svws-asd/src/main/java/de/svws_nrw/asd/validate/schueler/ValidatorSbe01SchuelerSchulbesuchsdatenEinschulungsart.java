package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBE01: Prüft, ob die angegebene Einschulungsart ein gültiger
 * und zulässiger Eintrag im Katalog ist.
 */
public final class ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	private final @NotNull Supplier<SchuelerSchulbesuchsdaten> _schulbesuchsdaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param schulbesuchsdaten   ein Supplier für die Schulbesuchsdaten des Schülers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart(
			final @NotNull Supplier<SchuelerSchulbesuchsdaten> schulbesuchsdaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_schulbesuchsdaten = schulbesuchsdaten;
		_validatoren.add(new ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(schulbesuchsdaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		final SchuelerSchulbesuchsdaten daten = _schulbesuchsdaten.get();

		if ((daten == null) || (daten.idEinschulungsartGrundschule == null) || (daten.idEinschulungsartGrundschule < 0)) {
			return true;
		}

		if (Einschulungsart.data().getWertByIDOrNull(daten.idEinschulungsartGrundschule) == null) {
			addFehler(0, "Einschulungsart des Schülers: Das Feld 'Einschulungsart' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
