package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung extends Validator {

	/** Die Organisationsform ID des Schuelers */
	private final @NotNull Supplier<@NotNull Long> idOrganisationsform;

	/**
	 * Erstellt einen neuen Validator für die Prüfung.
	 *
	 * @param schuljahr              Schuljahr
	 * @param idOrganisationsform    Organisationsform ID
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorSlow01SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> idOrganisationsform,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);
		this.idOrganisationsform = idOrganisationsform;

		_validatoren.add(new ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(schuljahr, idOrganisationsform, kontext));

	}

	@Override
	protected boolean pruefe() {
		final WeiterbildungskollegOrganisationsformen sa = WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(this.idOrganisationsform.get());

		if (sa == null) {
			this.addFehler(0, "Organisationsform des Schülers: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}

		return true;
	}

}

