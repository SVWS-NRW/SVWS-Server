package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
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
	private final @NotNull Supplier<@NotNull Double> _pflichtstundensoll;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> _einsatzstatus;

	/** Die Beschäftigungsart */
	private final @NotNull Supplier<@AllowNull LehrerBeschaeftigungsart> _beschaeftigungsart;
	private static final @NotNull Set<LehrerBeschaeftigungsart> setBeschaeftigungsart = Set.of(LehrerBeschaeftigungsart.WV, LehrerBeschaeftigungsart.WT);
	private static final @NotNull String FEHLERTEXT =
			"Ist bei einer Lehrkraft im Feld 'Pflichtstundensoll' der Wert = 0.00 eingetragen, so muss das Feld 'Einsatzstatus' den Schlüssel"
					+ " 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder die 'Beschäftigungsart' den Schlüssel 'Beamte auf"
					+ " Widerruf (LAA) in Vollzeit' bzw. 'Beamte auf Widerruf (LAA) in Teilzeit' aufweisen.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus        der Einsatzstatus
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			final @NotNull Supplier<@NotNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull Supplier<@AllowNull LehrerBeschaeftigungsart> beschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_pflichtstundensoll = pflichtstundensoll;
		_einsatzstatus = einsatzstatus;
		_beschaeftigungsart = beschaeftigungsart;
	}


	@Override
	protected boolean pruefe() {
		final Double pflichtstundensoll = _pflichtstundensoll.get();
		final LehrerEinsatzstatus einsatzstatus = _einsatzstatus.get();
		final LehrerBeschaeftigungsart beschaeftigungsart = _beschaeftigungsart.get();

		if (pflichtstundensoll == 0.0 && !LehrerEinsatzstatus.A.equals(einsatzstatus)
				&& (beschaeftigungsart == null || !setBeschaeftigungsart.contains(beschaeftigungsart))) {
			addFehler(3, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
