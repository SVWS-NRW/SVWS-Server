package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Orgaform Berufsbildend bei den Stammdaten
 * einer Klasse aus.
 */
public final class ValidatorKob00KlassenOrganisationsformBerufsbildend extends Validator {


		/** Orgaform */
		private final @NotNull Supplier<@AllowNull Long> _idOrgaform;
		private static final @NotNull String FEHLERTEXT = "Organisationsform der Klasse: Kein Wert vorhanden.";

		/**
		 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
		 *
		 * @param idOrgaform  die Organisationsform
		 * @param kontext     der Kontext des Validators
		 */
		public ValidatorKob00KlassenOrganisationsformBerufsbildend(
				final @NotNull Supplier<@AllowNull Long> idOrgaform,
				final @NotNull ValidatorKontext kontext) {
			super(kontext);
			_idOrgaform = idOrgaform;

			_validatoren.add(
					new ValidatorKob01KlassenOrganisationsformBerufsbildend(getNotNullSupplierLong(idOrgaform), kontext));
		}


		@Override
		protected boolean pruefe() {
			final @AllowNull Long idOrgaform = _idOrgaform.get();

				if (idOrgaform == null) {
					addFehler(0, FEHLERTEXT);
					return false;
				}
			return true;
		}
	}
