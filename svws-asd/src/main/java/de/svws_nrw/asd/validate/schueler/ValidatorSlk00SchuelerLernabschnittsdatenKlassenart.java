package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Klassenart eines Schülers aus.
 */
public final class ValidatorSlk00SchuelerLernabschnittsdatenKlassenart extends Validator {

	/** Geburtsland */
	private final @NotNull Supplier<@AllowNull Long> _idKlassenart;
	private static final @NotNull String FEHLERTEXT = "Klassenart des Schülers: Das Feld darf nicht leer sein";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKlassenart  die Klassenart ID
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorSlk00SchuelerLernabschnittsdatenKlassenart(
			final @NotNull Supplier<@AllowNull Long> idKlassenart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idKlassenart = idKlassenart;

		_validatoren.add(
				new ValidatorSlk01SchuelerLernabschnittsdatenKlassenart(getNotNullSupplierLong(idKlassenart), kontext));
	}


	@Override
	protected boolean pruefe() {
		// Bestimme Klassenart.
		final @AllowNull Long idKlassenart = _idKlassenart.get();

			if (idKlassenart == null) {
				addFehler(0, FEHLERTEXT);
				return false;
			}
		return true;
	}
}
