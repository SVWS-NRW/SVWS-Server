package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator SBE02: Prüft, ob der eingetragene Wert für das Feld 'Einschulungsart'
 * für das ausgewählte Schuljahr zeitlich gültig ist.
 */
public final class ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	private final @NotNull Supplier<SchuelerSchulbesuchsdaten> _schulbesuchsdaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param schulbesuchsdaten   ein Supplier für die Schulbesuchsdaten des Schülers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(
			final @NotNull Supplier<SchuelerSchulbesuchsdaten> schulbesuchsdaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_schulbesuchsdaten = schulbesuchsdaten;
	}

	@Override
	protected boolean pruefe() {
		final SchuelerSchulbesuchsdaten daten = _schulbesuchsdaten.get();

		if ((daten == null) || (daten.idEinschulungsartGrundschule == null) || (daten.idEinschulungsartGrundschule < 0)) {
			return true;
		}

		final Einschulungsart art = Einschulungsart.data().getWertByIDOrNull(daten.idEinschulungsartGrundschule);
		if (art == null) {
			return true;
		}

		// Zeitliche Gültigkeit prüfen
		if (art.daten(kontext().getSchuljahr()) == null) {
			addFehler(0,
					"Einschulungsart des Schülers: Der eingetragene Wert für das Feld 'Einschulungsart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
