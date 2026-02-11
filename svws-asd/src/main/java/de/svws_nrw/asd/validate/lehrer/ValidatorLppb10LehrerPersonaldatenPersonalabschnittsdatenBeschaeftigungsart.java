package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Beschäftigungsart */
	private final @NotNull Supplier<String> beschaeftigungsart;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<String> einsatzstatus;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param beschaeftigungsart     die Beschäftigungsart
	 * @param einsatzstatus     der Einsatzstatus
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<String> beschaeftigungsart,
			final @NotNull Supplier<String> einsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.beschaeftigungsart = beschaeftigungsart;
		this.einsatzstatus = einsatzstatus;
	}


	@Override
	protected boolean pruefe() {
		final String beschaeftigungsart = this.beschaeftigungsart.get();
		final String einsatzstatus = this.einsatzstatus.get();

		// LPPB2 ex BI7
		final Set<String> setEinsatzstatus2 = Set.of("A", "B");
		final String fehlertext2 = "Bei einer unentgeltlich beschäftigten Lehrkraft (Feld 'Beschäftigungsart' = 'Unentgeltlich Beschäftigte') "
				+ "dürfen im Feld 'Einsatzstatus' nicht die Einträge 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder "
				+ "'nicht Stammschule, aber auch hier tätig' eingetragen sein.";

		if (setEinsatzstatus2.contains(einsatzstatus) && "X".equals(beschaeftigungsart)) {
			this.addFehler(2, fehlertext2);
			return false;
		}

		return true;
	}

}
