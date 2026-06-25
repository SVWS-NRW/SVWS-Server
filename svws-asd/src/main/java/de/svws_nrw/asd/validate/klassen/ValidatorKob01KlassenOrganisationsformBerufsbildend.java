package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Orgaform Berufsbildend bei den Stammdaten
 * einer Klasse aus.
 */
public final class ValidatorKob01KlassenOrganisationsformBerufsbildend extends Validator {


		/** Orgaform */
		private final @NotNull Supplier<@NotNull Long> _idOrgaform;
		private static final @NotNull String FEHLERTEXT = "Organisationsform der Klasse: Das Feld 'Organisationsform' muss zulässig sein.";

		/**
		 * Erstellt einen neuen Validator für die Existenzprüfung der Organisationsform im Katalog.
		 *
		 * @param idOrgaform   ID Orgaform
		 * @param kontext      der Kontext des Validators
		 */
		public ValidatorKob01KlassenOrganisationsformBerufsbildend(
				final @NotNull Supplier<@NotNull Long> idOrgaform,
				final @NotNull ValidatorKontext kontext) {
			super(kontext);
			_idOrgaform = idOrgaform;

			_validatoren.add(
					new ValidatorKob02KlassenOrganisationsformBerufsbildend(idOrgaform, kontext));

		}

		@Override
		protected boolean pruefe() {

			final @NotNull Long idOrgaform = _idOrgaform.get();
			final @AllowNull BerufskollegOrganisationsformen oForm = BerufskollegOrganisationsformen.data().getWertByIDOrNull(idOrgaform);

			if (oForm == null) {
				addFehler(0, FEHLERTEXT);
				return false;
			}


			return true;
		}
	}
