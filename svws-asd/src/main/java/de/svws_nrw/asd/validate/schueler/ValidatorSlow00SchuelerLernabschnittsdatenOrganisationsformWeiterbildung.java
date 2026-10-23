package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung extends Validator {

	/** Die Organisationsform ID des Schuelers */
	private final @NotNull Supplier<@AllowNull Long> idOrganisationsform;

	/**
	 * Erstellt einen neuen Validator für die Prüfung.
	 *
	 * @param schuljahr              Schuljahr
	 * @param idOrganisationsform    Organisationsform ID
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorSlow00SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> idOrganisationsform,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);
		this.idOrganisationsform = idOrganisationsform;

		_validatoren.add(new ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(schuljahr, getNotNullSupplierLong(idOrganisationsform), kontext));
	}

	@Override
	protected boolean pruefe() {

		final Long idOrganisationsformLong = this.idOrganisationsform.get();

		if (idOrganisationsformLong == null) {
			addFehler(0, "Organisationsform des Schülers: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
