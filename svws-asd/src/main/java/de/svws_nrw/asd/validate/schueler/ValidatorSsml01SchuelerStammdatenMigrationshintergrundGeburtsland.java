package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsland eines Schülers aus.
 */

public final class ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/** Das Geburtsland */
	private final @NotNull Supplier<@NotNull Long> _idGeburtsland;
	private static final @NotNull String FEHLERTEXT = "Geburtsland des Schülers: Das Feld muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param idGeburtsland             die ID des Geburtslands
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland(
			final @NotNull Supplier<@NotNull Long> idGeburtsland,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtsland = idGeburtsland;

		_validatoren.add(
				new ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland(idGeburtsland, kontext));

	}

	@Override
	protected boolean pruefe() {
		// Bestimme Geburtsland.
		final @NotNull Long idGeburtsland = _idGeburtsland.get();
		final @AllowNull Nationalitaeten nat = Nationalitaeten.data().getWertByIDOrNull(idGeburtsland);

		if (nat == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
