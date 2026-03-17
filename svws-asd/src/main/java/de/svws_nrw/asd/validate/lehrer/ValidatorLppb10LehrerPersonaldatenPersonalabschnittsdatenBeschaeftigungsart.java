package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschäftigungsart */
	private final @NotNull Supplier<Long> _idBeschaeftigungsart;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<Long> _idEinsatzstatus;
	private static final @NotNull Set<LehrerEinsatzstatus> setEinsatzstatus2 = Set.of(LehrerEinsatzstatus.A, LehrerEinsatzstatus.B);
	private static final @NotNull String FEHLERTEXT =
			"Bei einer unentgeltlich beschäftigten Lehrkraft (Feld 'Beschäftigungsart' = 'Unentgeltlich Beschäftigte') "
					+ "dürfen im Feld 'Einsatzstatus' nicht die Einträge 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder "
					+ "'nicht Stammschule, aber auch hier tätig' eingetragen sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idBeschaeftigungsart   die Beschäftigungsart
	 * @param idEinsatzstatus        der Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<Long> idBeschaeftigungsart,
			final @NotNull Supplier<Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		this._idEinsatzstatus = idEinsatzstatus;
	}


	@Override
	protected boolean pruefe() {
		final Long idBeschaeftigungsart = this._idBeschaeftigungsart.get();
		final Long idEinsatzstatus = this._idEinsatzstatus.get();

		// LPPB2 ex BI7
		if (setEinsatzstatus2.contains(LehrerEinsatzstatus.data().getWertByID(idEinsatzstatus))
				&& (LehrerBeschaeftigungsart.X == LehrerBeschaeftigungsart.data().getWertByID(idBeschaeftigungsart))) {
			this.addFehler(2, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
