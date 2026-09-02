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
 * Validator IOO02: Prüft, ob Ort korrekt ist.
 */
public final class ValidatorIoo02IntKatalogOrteOrtsname extends Validator {

	private final @NotNull Supplier<@NotNull String> ortsname;
	private final @NotNull Supplier<@AllowNull Long> idLand;


	/**
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	Kontext
	 */
	public ValidatorIoo02IntKatalogOrteOrtsname(
			final @NotNull Supplier<@NotNull String> ortsname,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.ortsname = ortsname;
		this.idLand = idLand;
	}

	@Override
	protected boolean pruefe() {

		final Laender laender =
				Laender.data().getWertByIDOrNull(idLand.get());

		if (Laender.NW.equals(laender)) {

			String ortsnameString = ortsname.get();

				for (OrteKatalogEintrag orteKatalogEintrag : Orte.data().getEintraegeBySchuljahr(kontext().getSchuljahr())) {
					if (ortsnameString.equals(orteKatalogEintrag.ort)) {
						return true;
					}
				}

			addFehler(0, "Der eingetragene Wert für das Feld 'Ortsname' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;

		}


		return true;
	}

}
