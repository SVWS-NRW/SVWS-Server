package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<String> daten;
	private final @NotNull Supplier<String> rechtsverhaeltnis;
	private static final @NotNull Set<String> setRechtsverhaeltnis = Set.of("L", "N", "P", "W");
	private static final @NotNull Set<String> setStaatsangehoerigkeit = Set.of("DEU", "BEL", "BGR", "DNK", "EST", "FIN", "FRA", "HRV", "SVN", "GRC", "IRL", "ISL", "ITA", "LVA", "LIE", "LTU", "LUX", "MLT",
			"NLD", "NOR", "AUT", "POL", "PRT", "ROU", "SVK", "SWE", "CHE", "ESP", "CZE", "HUN", "GBR", "CYP");
	private static final @NotNull String FEHLERTEXT = "Zu dieser verbeamteten Lehrkraft ist die Staatsangehörigkeit '\" + LehrerStammdaten.staatsangehoerigkeitID + \"' angegeben. "
			+ "Dabei handelt es sich jedoch nicht um eine Staatsangehörigkeit eines Mitgliedsstaats der Europäischen Union (EU) oder des Europäischen Wirtschaftsraums (EWR). "
			+ "Die vorgenommene Eintragung kann nur in Ausnahmefällen korrekt sein. Für Lehrkräfte, die neben einer ausländischen Staatsangehörigkeit auch die deutsche Staatsangehörigkeit "
			+ "besitzen, erfassen Sie bitte die Staatsangehörigkeit 'deutsch'. ";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
    * @param daten              die StaatsangehoerigkeitID des Lehrers
	 * @param rechtsverhaeltnis  das Rechtsverhaeltnis des Lehrers
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<String> daten, final @NotNull Supplier<String> rechtsverhaeltnis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		this.rechtsverhaeltnis = rechtsverhaeltnis;
	}

	@Override
	protected boolean pruefe() {

		if (setRechtsverhaeltnis.contains(rechtsverhaeltnis.get()) && !setStaatsangehoerigkeit.contains(daten.get())) {
			this.addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
