package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSlobSchuelerLernabschnittsdatenOrganisationsformBerufsbildend extends Validator {

	/** Die Organisationsform ID des Schuelers */
	private final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Validator für die Prüfung.
	 *
	 * @param idSchuljahresabschnitt    Schuljahresabschnitt ID
	 * @param idOrganisationsform       Organisationsform ID
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSlobSchuelerLernabschnittsdatenOrganisationsformBerufsbildend(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idOrganisationsform,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		this.idSchuljahresabschnitt = idSchuljahresabschnitt;


		final @NotNull Supplier<@NotNull Integer> schuljahr = () -> {
			final @AllowNull Schuljahresabschnitt sja =  kontext.getSchuljahresabschnittByID(getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja == null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		};
		_validatoren.add(new ValidatorSlob00SchuelerLernabschnittsdatenOrganisationsformBerufsbildend(schuljahr, idOrganisationsform, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Schuljahresabschnitt sja =  kontext().getSchuljahresabschnittByID(getNotNullSupplierLong(this.idSchuljahresabschnitt).get());
		if (sja == null) {
			addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}

		return true;
	}

}
