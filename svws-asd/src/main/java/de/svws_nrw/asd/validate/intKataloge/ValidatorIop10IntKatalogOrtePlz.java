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
 * Validator IOP10: Prüft, ob PLZ korrekt ist.
 */
public final class ValidatorIop10IntKatalogOrtePlz extends Validator {

	private final @NotNull Supplier<@NotNull String> plz;
	private final @NotNull Supplier<@AllowNull Long> idLand;

	/**
	 * @param plz         die Postleitzahl
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public ValidatorIop10IntKatalogOrtePlz(
			final @NotNull Supplier<@NotNull String> plz,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.plz = plz;
		this.idLand = idLand;
	}

	@Override
	protected boolean pruefe() {

		final Laender laender =
				Laender.data().getWertByIDOrNull(idLand.get());

		if (!Laender.NW.equals(laender)) {

			String plzString = plz.get();

			for (Orte ort : Orte.data().getWerte()) {
				for (OrteKatalogEintrag orteKatalogEintrag : ort.historie()) {
					if (orteKatalogEintrag.plz.equals(plzString)) {
						addFehler(0, "Für Orte, die nicht in Nordrhein-Westfalen liegen, darf keine in Nordrhein-Westfalen liegende Postleitzahl verwendet werden. Bitte prüfen!");
						return false;
					}
				}
			}
		}

		return true;
	}

}
