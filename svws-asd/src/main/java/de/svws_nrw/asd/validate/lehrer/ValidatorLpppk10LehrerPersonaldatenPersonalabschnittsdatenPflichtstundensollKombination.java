package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll in Kombination
 * mit dem Einsatzstatus und der Beschäftigungsart der Personalabschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination extends Validator {

	/** Das Pflichtstundensoll des Lehrers */
	private final @NotNull Supplier<@AllowNull Double> _pflichtstundensoll;

	/** Die Katalog-ID des Einsatzstatus des Lehrers */
	private final @NotNull Supplier<@AllowNull Long> _idEinsatzstatus;

	/** Die Katalog-ID der Beschäftigungsart des Lehrers */
	private final @NotNull Supplier<@AllowNull Long> _idBeschaeftigungsart;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll des Lehrers
	 * @param idEinsatzstatus        die Katalog-ID des Einsatzstatus des Lehrers
	 * @param idBeschaeftigungsart   die Katalog-ID der Beschäftigungsart des Lehrers
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_pflichtstundensoll = pflichtstundensoll;
		_idEinsatzstatus = idEinsatzstatus;
		_idBeschaeftigungsart = idBeschaeftigungsart;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Double pflichtstundensoll = _pflichtstundensoll.get();
		if ((pflichtstundensoll == null) || (pflichtstundensoll != 0.0)) {
			return true;
		}

		final @AllowNull Long idEinsatzstatus = _idEinsatzstatus.get();
		final @AllowNull LehrerEinsatzstatus einsatzstatus = (idEinsatzstatus == null)
				? null
				: LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus);

		final @AllowNull Long idBeschaeftigungsart = _idBeschaeftigungsart.get();
		final @AllowNull LehrerBeschaeftigungsart beschaeftigungsart = (idBeschaeftigungsart == null)
				? null
				: LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart);

		if ((einsatzstatus != LehrerEinsatzstatus.A)
				&& (beschaeftigungsart != LehrerBeschaeftigungsart.WT)
				&& (beschaeftigungsart != LehrerBeschaeftigungsart.WV)) {
			addFehler(0,
					"Pflichtstundensoll des Lehrers: Bei einer Lehrkraft mit 'Pflichtstundensoll' = 0.00 muss der Einsatzstatus 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder die Beschäftigungsart 'Beamte auf Widerruf (LAA) in Vollzeit' bzw. 'Beamte auf Widerruf (LAA) in Teilzeit' lauten.");
			return false;
		}

		return true;
	}

}
