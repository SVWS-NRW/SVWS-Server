package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten              die StaatsangehoerigkeitID des Lehrers
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		final int schuljahr = kontext().getSchuljahr();

		final Nationalitaeten staatsangehoerigkeitID = Nationalitaeten.getByDESTATIS(daten.get());

		if (staatsangehoerigkeitID == null) {
			return true;
		}

		for (NationalitaetenKatalogEintrag historie : staatsangehoerigkeitID.historie()) {

			int gueltigVon = (historie.gueltigVon == null) ? 0 : historie.gueltigVon;
			int gueltigBis = (historie.gueltigBis == null) ? 99999 : historie.gueltigBis;

			if (gueltigVon <= schuljahr && gueltigBis >= schuljahr) {
				return true;
			}
		}
		return false;

	}

}
