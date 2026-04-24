package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsv00SchuelerStammdatenVorname extends Validator {

	/** Der Schueler-Vorname */
	private final @NotNull Supplier<@AllowNull String> vornameSupplier;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param vornameSupplier   der Vorname des Schuelers
	 * @param kontext           der Kontext der Schule
	 */
	public ValidatorSsv00SchuelerStammdatenVorname(final @NotNull Supplier<@AllowNull String> vornameSupplier,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.vornameSupplier = vornameSupplier;
		_validatoren.add(new ValidatorSsv10SchuelerStammdatenVorname(getNotNullSupplier(vornameSupplier), kontext));

	}

	@Override
	protected boolean pruefe() {
		final String vorname = this.vornameSupplier.get();

		if (vorname == null || vorname.isEmpty()) {
			addFehler(0, "Rufname des Schülers: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
