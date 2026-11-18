package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.fach.BilingualeSprache;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IAB02: Prüft, ob Bilinguale Sachfachsprache des internen Kataloges korrekt ist.
 */
public final class ValidatorIab02IntKatalogFaecherBilingualeSprache extends Validator {

	private final @NotNull Supplier<@NotNull Long> idBilingualeSprache;

	/**
	 * @param idBilingualeSprache   ID
	 * @param kontext	            Kontext
	 */
	public ValidatorIab02IntKatalogFaecherBilingualeSprache(
			final @NotNull Supplier<@NotNull Long> idBilingualeSprache,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.idBilingualeSprache = idBilingualeSprache;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
		if (!BilingualeSprache.data().isGueltig(this.idBilingualeSprache.get(), kontext().getSchuljahr())) {
			addFehler(0, "Bilinguale Sachfachsprache des Fachs: Der eingetragene Wert für das Feld 'Bilinguale Sachfachsprache' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
