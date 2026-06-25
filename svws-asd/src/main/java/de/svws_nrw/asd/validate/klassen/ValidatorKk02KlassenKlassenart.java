package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KK02: Prüft, ob die angegebene Klassenart einer Klasse
 * im ausgewählten Schuljahr zeitlich gültig ist.
 */
public final class ValidatorKk02KlassenKlassenart extends Validator {

	private final @NotNull Supplier<KlassenDaten> _klassenDaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten  ein Supplier für die Klassendaten
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorKk02KlassenKlassenart(
			final @NotNull Supplier<KlassenDaten> klassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;
	}

	@Override
	protected boolean pruefe() {
		final KlassenDaten daten = _klassenDaten.get();

		if ((daten == null) || (daten.idKlassenart == null)) {
			return true;
		}

		final Klassenart art = Klassenart.data().getWertByIDOrNull(daten.idKlassenart);

		if (art == null) {
			return true;
		}

		// Nutzt die Manager-Methode: Prüft, ob exakt diese historische ID im Schuljahr gültig ist
		if (!Klassenart.data().isGueltig(daten.idKlassenart, kontext().getSchuljahr())) {
			addFehler(0, "Art der Klasse: Der eingetragene Wert für das Feld 'Klassenart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}
}
