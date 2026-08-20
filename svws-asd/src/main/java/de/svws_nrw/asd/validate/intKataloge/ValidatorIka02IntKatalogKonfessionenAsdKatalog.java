package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für eine KatalogID des CoreType Religion aus.
 * Es wird überprüft, ob der eingetragene Wert für das ausgewählte Schuljahr gültig ist.
 */
public final class ValidatorIka02IntKatalogKonfessionenAsdKatalog extends Validator {

	/** Die Katalog-ID. */
	private final @NotNull Supplier<Long> _idKatalog;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idKatalog   die Katalog-ID der Fachrichtung
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorIka02IntKatalogKonfessionenAsdKatalog(final @NotNull Supplier<Long> idKatalog, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;
	}

	@Override
	protected boolean pruefe() {

		// Prüft auf fehlende Werte, inaktive Schuljahre und falsche Historien-IDs
		if (!Religion.data().isGueltig(_idKatalog.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Konfession des Schülers: Der eingetragene Wert für das Feld 'Konfession ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
