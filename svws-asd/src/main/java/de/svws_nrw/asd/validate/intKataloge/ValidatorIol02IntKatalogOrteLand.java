package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IOL02: Prüft, ob Land korrekt ist.
 */
public final class ValidatorIol02IntKatalogOrteLand extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKatalog;

	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public ValidatorIol02IntKatalogOrteLand(
			final @NotNull Supplier<@NotNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
		if (!Laender.data().isGueltig(_idKatalog.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Der eingetragene Wert für das Feld 'Land' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
