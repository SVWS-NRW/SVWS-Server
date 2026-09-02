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
 * Validator IOO01: Prüft, ob Ort korrekt ist.
 */
public final class ValidatorIoo01IntKatalogOrteOrtsname extends Validator {

	private final @NotNull Supplier<@NotNull String> ortsname;
	private final @NotNull Supplier<@AllowNull Long> idLand;

	/**
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public ValidatorIoo01IntKatalogOrteOrtsname(
			final @NotNull Supplier<@NotNull String> ortsname,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.idLand = idLand;
		this.ortsname = ortsname;

		_validatoren.add(new ValidatorIoo02IntKatalogOrteOrtsname(ortsname, idLand, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Laender laender =
				Laender.data().getWertByIDOrNull(idLand.get());

		if (Laender.NW.equals(laender)) {

			String ortsnameString = ortsname.get();

			for (Orte ort : Orte.data().getWerte()) {
				for (OrteKatalogEintrag orteKatalogEintrag : ort.historie()) {
					if (ortsnameString.equals(orteKatalogEintrag.ort)) {
						return true;
					}
				}
			}

			addFehler(0, "Das Feld 'Ortsname' muss zulässig sein.");
			return false;

		}

		return true;
	}

}
