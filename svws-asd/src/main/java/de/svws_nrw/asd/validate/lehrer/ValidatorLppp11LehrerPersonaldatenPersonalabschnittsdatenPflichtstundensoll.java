package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/** Das Pflichtstundensoll */
	private final @NotNull Supplier<@AllowNull Double> pflichtstundensoll;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@AllowNull String> einsatzstatus;

	/** Die Beschäftigungsart */
	private final @NotNull Supplier<@AllowNull String> beschaeftigungsart;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param einsatzstatus    		Der Einsatzstatus
	 * @param beschaeftigungsart    Die Beschäftigungsart
	 * @param kontext   			der Kontext des Validators
	 */
	public ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull String> einsatzstatus,
			final @NotNull Supplier<@AllowNull String> beschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.pflichtstundensoll = pflichtstundensoll;
		this.einsatzstatus = einsatzstatus;
		this.beschaeftigungsart = beschaeftigungsart;
	}


	@Override
	protected boolean pruefe() {
		final Double pflichtstundensoll = this.pflichtstundensoll.get();
		final LehrerEinsatzstatus einsatzstatus = LehrerEinsatzstatus.getBySchluessel(this.einsatzstatus.get());

		final String beschaeftigungsart = this.beschaeftigungsart.get();
		final @NotNull Set<String> setBeschaeftigungsart = Set.of("WV", "WT");
		final String fehlertext3 =
				"Ist bei einer Lehrkraft im Feld 'Pflichtstundensoll' der Wert = 0.00 eingetragen, so muss das Feld 'Einsatzstatus' den Schlüssel"
						+ " 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder die 'Beschäftigungsart' den Schlüssel 'Beamte auf"
						+ " Widerruf (LAA) in Vollzeit' bzw. 'Beamte auf Widerruf (LAA) in Teilzeit' aufweisen.";

		if (pflichtstundensoll == 0.0
				&& !LehrerEinsatzstatus.A.equals(einsatzstatus)
				&& !setBeschaeftigungsart.contains(beschaeftigungsart)) {
			this.addFehler(3, fehlertext3);
			return false;
		}

		return true;
	}

}
