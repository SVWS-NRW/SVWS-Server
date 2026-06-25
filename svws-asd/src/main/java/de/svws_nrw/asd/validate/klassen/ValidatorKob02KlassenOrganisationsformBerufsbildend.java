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
public final class ValidatorKob02KlassenOrganisationsformBerufsbildend extends Validator {


		/** Orgaform */
		private final @NotNull Supplier<@AllowNull Long> _idOrgaform;
		private static final @NotNull String FEHLERTEXT = "Organisationsform der Klasse: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

		/**
		 * Erstellt einen neuen Validator für die Existenzprüfung der Organisationsform im Katalog.
		 *
		 * @param idOrgaform   ID Orgaform
		 * @param kontext      der Kontext des Validators
		 */
		public ValidatorKob02KlassenOrganisationsformBerufsbildend(
				final @NotNull Supplier<@NotNull Long> idOrgaform,
				final @NotNull ValidatorKontext kontext) {
			super(kontext);
			_idOrgaform = idOrgaform;

		}

		@Override
		protected boolean pruefe() {

			if (!BerufskollegOrganisationsformen.data().isGueltig(_idOrgaform.get(), kontext().getSchuljahr())) {
				addFehler(0, FEHLERTEXT);
				return false;
			}


			return true;
		}
	}

