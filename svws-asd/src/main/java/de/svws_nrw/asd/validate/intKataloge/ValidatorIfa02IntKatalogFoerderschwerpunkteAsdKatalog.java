package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IFA02: Prüft, ob Förderschwerpunkt des Schülers korrekt ist.
 */
public final class ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKatalog;

	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog(
			final @NotNull Supplier<@NotNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
		if (!Foerderschwerpunkt.data().isGueltig(_idKatalog.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Foerderschwerpunkt des Schülers: Der eingetragene Wert für das Feld 'Förderschwerpunkt ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
