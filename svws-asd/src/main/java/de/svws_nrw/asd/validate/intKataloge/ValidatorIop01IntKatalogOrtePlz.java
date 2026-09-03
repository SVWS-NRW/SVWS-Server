package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.OrteKatalogEintrag;
import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.asd.types.schule.Orte;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IOP01: Prüft, ob PLZ korrekt ist.
 */
public final class ValidatorIop01IntKatalogOrtePlz extends Validator {

	private final @NotNull Supplier<@NotNull String> plz;
	private final @NotNull Supplier<@AllowNull Long> idLand;

	/**
	 * @param plz        die Postleitzahl
	 * @param idLand     die ID des Landes
	 * @param kontext    Kontext
	 */
	public ValidatorIop01IntKatalogOrtePlz(
			final @NotNull Supplier<@NotNull String> plz,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.idLand = idLand;
		this.plz = plz;

		_validatoren.add(new ValidatorIop02IntKatalogOrtePlz(plz, idLand, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Laender laender =
				Laender.data().getWertByIDOrNull(idLand.get());

		if (Laender.NW.equals(laender)) {

			String plzString = plz.get();

			for (Orte ort : Orte.data().getWerte()) {
				for (OrteKatalogEintrag orteKatalogEintrag : ort.historie()) {
					if (plzString.equals(orteKatalogEintrag.plz)) {
						return true;
					}
				}
			}

			addFehler(0, "Das Feld 'PLZ' muss zulässig sein.");
			return false;

		}

		return true;
	}

}
