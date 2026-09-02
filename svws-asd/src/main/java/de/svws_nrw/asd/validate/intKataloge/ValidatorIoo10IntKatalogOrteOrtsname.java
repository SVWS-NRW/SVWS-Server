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
 * Validator IOO10: Prüft, ob Ort korrekt ist.
 */
public final class ValidatorIoo10IntKatalogOrteOrtsname extends Validator {

	private final @NotNull Supplier<@AllowNull String> plz;
	private final @NotNull Supplier<@AllowNull String> ortsname;
	private final @NotNull Supplier<@AllowNull Long> idLand;

	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public ValidatorIoo10IntKatalogOrteOrtsname(
			final @NotNull Supplier<@AllowNull String> plz,
			final @NotNull Supplier<@AllowNull String> ortsname,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.plz = plz;
		this.ortsname = ortsname;
		this.idLand = idLand;
	}

	@Override
	protected boolean pruefe() {

		final Laender laender =
				Laender.data().getWertByIDOrNull(idLand.get());

		if (Laender.NW.equals(laender)) {

			String ortsnameString = ortsname.get();
			String plzString = plz.get();

			for (Orte ort : Orte.data().getWerte()) {
				for (OrteKatalogEintrag orteKatalogEintrag : ort.historie()) {
					if (orteKatalogEintrag.ort.equals(ortsnameString) && orteKatalogEintrag.plz.equals(plzString)) {
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
