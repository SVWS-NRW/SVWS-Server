package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<String> _staatsangehoerigkeitID;
	private final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> _rechtsverhaeltnis;
	private static final @NotNull Set<LehrerRechtsverhaeltnis> setRechtsverhaeltnis =
			Set.of(LehrerRechtsverhaeltnis.L, LehrerRechtsverhaeltnis.N, LehrerRechtsverhaeltnis.P, LehrerRechtsverhaeltnis.W);
	private static final @NotNull Set<String> setStaatsangehoerigkeit =
			Set.of("DEU", "BEL", "BGR", "DNK", "EST", "FIN", "FRA", "HRV", "SVN", "GRC", "IRL", "ISL", "ITA", "LVA", "LIE", "LTU", "LUX", "MLT",
					"NLD", "NOR", "AUT", "POL", "PRT", "ROU", "SVK", "SWE", "CHE", "ESP", "CZE", "HUN", "GBR", "CYP");
	private static final @NotNull String FEHLERTEXT =
			"Zu dieser verbeamteten Lehrkraft ist die Staatsangehörigkeit '\" + LehrerStammdaten.staatsangehoerigkeitID + \"' angegeben. "
					+ "Dabei handelt es sich jedoch nicht um eine Staatsangehörigkeit eines Mitgliedsstaats der Europäischen Union (EU) oder des Europäischen Wirtschaftsraums (EWR). "
					+ "Die vorgenommene Eintragung kann nur in Ausnahmefällen korrekt sein. Für Lehrkräfte, die neben einer ausländischen Staatsangehörigkeit auch die deutsche Staatsangehörigkeit "
					+ "besitzen, erfassen Sie bitte die Staatsangehörigkeit 'deutsch'. ";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitID   die StaatsangehoerigkeitID des Lehrers
	 * @param rechtsverhaeltnis        das Rechtsverhältnis des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<String> staatsangehoerigkeitID,
			final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> rechtsverhaeltnis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_staatsangehoerigkeitID = staatsangehoerigkeitID;
		_rechtsverhaeltnis = rechtsverhaeltnis;
	}

	@Override
	protected boolean pruefe() {

		if (_rechtsverhaeltnis.get() == null) {
			return true;
		}

		if (setRechtsverhaeltnis.contains(_rechtsverhaeltnis.get())
				&& !setStaatsangehoerigkeit.contains(_staatsangehoerigkeitID.get())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}
		return true;
	}

}
