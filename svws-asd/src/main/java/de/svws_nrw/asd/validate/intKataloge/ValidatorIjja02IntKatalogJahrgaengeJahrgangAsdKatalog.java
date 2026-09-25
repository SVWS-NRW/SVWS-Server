package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IJJA02: Prüft, ob Jahrgang des internen Kataloges korrekt ist.
 */
public final class ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKatalog;

	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog(
			final @NotNull Supplier<@NotNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
		if (!Jahrgaenge.data().isGueltig(_idKatalog.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Jahrgang ASD-Kürzel: Der eingetragene Wert für das Feld 'Jahrgang ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
