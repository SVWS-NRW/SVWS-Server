package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Rechtsverhältnis im Kontext des Lehramts
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Das Rechtsverhältnis des Lehrers */
	private final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> _rechtsverhaeltnis;

	/** Die Liste der Lehramts-Katalog-IDs des Lehrers */
	private final @NotNull Supplier<List<Long>> _lehraemter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param rechtsverhaeltnis   das Rechtsverhältnis des Lehrers
	 * @param lehraemter          die Liste der Lehramts-Katalog-IDs des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> rechtsverhaeltnis,
			final @NotNull Supplier<List<Long>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_rechtsverhaeltnis = rechtsverhaeltnis;
		_lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {
		if (!LehrerRechtsverhaeltnis.W.equals(_rechtsverhaeltnis.get())) {
			return true;
		}

		final List<Long> lehraemter = _lehraemter.get();
		if (lehraemter != null) {
			for (final Long idKatalogLehramt : lehraemter) {
				if ((idKatalogLehramt != null) && LehrerLehramt.ID_98.equals(LehrerLehramt.data().getWertByIDOrNull(idKatalogLehramt))) {
					return true;
				}
			}
		}

		addFehler(0,
				"Rechtsverhältnis des Lehrers: Für das Rechtsverhältnis 'Beamte auf Widerruf (LAA)' muss das Lehramt 'Lehramtsanwärter/-in / Studienreferendar/-in' vorliegen.");
		return false;
	}

}
