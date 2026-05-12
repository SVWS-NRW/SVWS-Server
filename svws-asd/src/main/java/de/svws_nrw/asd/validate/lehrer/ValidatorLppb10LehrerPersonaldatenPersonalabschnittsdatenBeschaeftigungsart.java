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
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschäftigungsart */
	private final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> _beschaeftigungsart;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> _einsatzstatus;
	private static final @NotNull Set<LehrerEinsatzstatus> setEinsatzstatus2 = Set.of(LehrerEinsatzstatus.A, LehrerEinsatzstatus.B);
	private static final @NotNull String FEHLERTEXT =
			"Bei einer unentgeltlich beschäftigten Lehrkraft (Feld 'Beschäftigungsart' = 'Unentgeltlich Beschäftigte') "
					+ "dürfen im Feld 'Einsatzstatus' nicht die Einträge 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder "
					+ "'nicht Stammschule, aber auch hier tätig' eingetragen sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param einsatzstatus        der Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> beschaeftigungsart,
			final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_beschaeftigungsart = beschaeftigungsart;
		_einsatzstatus = einsatzstatus;
	}


	@Override
	protected boolean pruefe() {
		final LehrerBeschaeftigungsart beschaeftigungsart = _beschaeftigungsart.get();
		final LehrerEinsatzstatus einsatzstatus = _einsatzstatus.get();

		if ((einsatzstatus == null || setEinsatzstatus2.contains(einsatzstatus))
				&& LehrerBeschaeftigungsart.X == beschaeftigungsart) {
			addFehler(2, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
