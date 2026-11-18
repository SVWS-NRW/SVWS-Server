package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.fach.BilingualeSprache;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IAB01: Prüft, ob Bilinguale Sachfachsprache des internen Kataloges korrekt ist.
 */
public final class ValidatorIab01IntKatalogFaecherBilingualeSprache extends Validator {

	private final @NotNull Supplier<@AllowNull Long> idBilingualeSprache;

	/**
	 * @param idBilingualeSprache   ID
	 * @param kontext               Kontext
	 */
	public ValidatorIab01IntKatalogFaecherBilingualeSprache(
			final @NotNull Supplier<@AllowNull Long> idBilingualeSprache,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.idBilingualeSprache = idBilingualeSprache;

		_validatoren.add(new ValidatorIab02IntKatalogFaecherBilingualeSprache(getNotNullSupplierLong(idBilingualeSprache), kontext));
	}

	@Override
	protected boolean pruefe() {

		final BilingualeSprache bilingualeSprache = BilingualeSprache.data().getWertByIDOrNull(this.idBilingualeSprache.get());

		if (bilingualeSprache == null) {
			addFehler(0, "Bilinguale Sachfachsprache des Fachs: Das Feld 'Bilinguale Sachfachsprache' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
