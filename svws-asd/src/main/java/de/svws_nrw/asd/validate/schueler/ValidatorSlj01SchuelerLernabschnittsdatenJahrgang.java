package de.svws_nrw.asd.validate.schueler;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Feld 'Jahrgang' in den Lernabschnittsdaten
 * eines Schülers zulässig ist.
 */
public final class ValidatorSlj01SchuelerLernabschnittsdatenJahrgang extends Validator {

	/** Die ID des Jahrgangs des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _jahrgangID;

	/** Die Liste aller Jahrgänge der Schule */
	private final @NotNull Supplier<List<JahrgaengeStatistikGesamt>> _jahrgaenge;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param jahrgangID   die ID des Jahrgangs des Schülers
	 * @param jahrgaenge   die Liste aller Jahrgänge der Schule
	 * @param kontext      der Kontext des Validators
	 */
	public ValidatorSlj01SchuelerLernabschnittsdatenJahrgang(
			final @NotNull Supplier<@AllowNull Long> jahrgangID,
			final @NotNull Supplier<List<JahrgaengeStatistikGesamt>> jahrgaenge,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_jahrgangID = jahrgangID;
		_jahrgaenge = jahrgaenge;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long jahrgangID = _jahrgangID.get();
		final List<JahrgaengeStatistikGesamt> listJahrgaenge = _jahrgaenge.get();

		if ((jahrgangID == null) || (listJahrgaenge == null)) {
			return true;
		}

		for (final JahrgaengeStatistikGesamt jahrgang : listJahrgaenge) {
			if (jahrgang.id == jahrgangID) {
				return true;
			}
		}

		addFehler(0, "Jahrgang des Schülers: Das Feld 'Jahrgang' muss zulässig sein.");
		return false;
	}

}
