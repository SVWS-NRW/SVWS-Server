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
public final class ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung extends Validator {

	/** Das aktuelle Schuljahr des Schülers */
	private final @NotNull Supplier<@NotNull Integer> schuljahr;

	/** Die Organisationsform ID des Schuelers */
	private final @NotNull Supplier<@NotNull Long> idOrganisationsform;

	/**
	 * Erstellt einen neuen Validator für die Prüfung.
	 *
	 * @param schuljahr              Schuljahr
	 * @param idOrganisationsform    Organisationsform ID
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorSlow02SchuelerLernabschnittsdatenOrganisationsformWeiterbildung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> idOrganisationsform,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		this.schuljahr = schuljahr;
		this.idOrganisationsform = idOrganisationsform;
	}


	@Override
	protected boolean pruefe() {

		if (!WeiterbildungskollegOrganisationsformen.data().isGueltig(this.idOrganisationsform.get(), this.schuljahr.get())) {
			addFehler(0, "Organisationsform des Schülers: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
