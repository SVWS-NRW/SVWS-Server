package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<@NotNull String> _staatsangehoerigkeitSchluessel;
	private final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> _rechtsverhaeltnis;
	private static final @NotNull Set<LehrerRechtsverhaeltnis> setRechtsverhaeltnis =
			Set.of(LehrerRechtsverhaeltnis.L, LehrerRechtsverhaeltnis.N, LehrerRechtsverhaeltnis.P, LehrerRechtsverhaeltnis.W);
	private static final @NotNull Set<String> setStaatsangehoerigkeit =
			Set.of("DEU", "BEL", "BGR", "DNK", "EST", "FIN", "FRA", "HRV", "SVN", "GRC", "IRL", "ISL", "ITA", "LVA", "LIE", "LTU", "LUX", "MLT",
					"NLD", "NOR", "AUT", "POL", "PRT", "ROU", "SVK", "SWE", "CHE", "ESP", "CZE", "HUN", "GBR", "CYP");

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitSchluessel   der staatsangehoerigkeitSchluessel des Lehrers
	 * @param rechtsverhaeltnis                das Rechtsverhältnis des Lehrers
	 * @param kontext                          der Kontext des Validators
	 */
	public ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(final @NotNull Supplier<@NotNull String> staatsangehoerigkeitSchluessel,
			final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> rechtsverhaeltnis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._staatsangehoerigkeitSchluessel = staatsangehoerigkeitSchluessel;
		this._rechtsverhaeltnis = rechtsverhaeltnis;
	}

	@Override
	protected boolean pruefe() {

		if (this._rechtsverhaeltnis.get() == null) {
			return true;
		}

		final int schuljahr = kontext().getSchuljahr();
		final Schulform schulform = kontext().getSchulform();

		final Nationalitaeten nationalitaet =
				Nationalitaeten.data().getBySchuljahrAndSchulformAndSchluessel(schuljahr, schulform, this._staatsangehoerigkeitSchluessel.get());

		if ((nationalitaet != null)) {
			final NationalitaetenKatalogEintrag katalogEintrag = nationalitaet.daten(schuljahr);

			if (setRechtsverhaeltnis.contains(this._rechtsverhaeltnis.get()) && (katalogEintrag != null)
					&& !setStaatsangehoerigkeit
							.contains(
									katalogEintrag.iso3)) {
				addFehler(0, "Zu dieser verbeamteten Lehrkraft ist die Staatsangehörigkeit '" + this._staatsangehoerigkeitSchluessel.get() + "' angegeben. "
						+ "Dabei handelt es sich jedoch nicht um eine Staatsangehörigkeit eines Mitgliedsstaats der Europäischen Union (EU) oder des Europäischen Wirtschaftsraums (EWR). "
						+ "Die vorgenommene Eintragung kann nur in Ausnahmefällen korrekt sein. Für Lehrkräfte, die neben einer ausländischen Staatsangehörigkeit auch die deutsche Staatsangehörigkeit "
						+ "besitzen, erfassen Sie bitte die Staatsangehörigkeit 'deutsch'. ");
				return false;
			}
		}
		return true;
	}

}
