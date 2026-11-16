package de.svws_nrw.asd.validate.lehrer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob ein Minderleistungsgrund mehrfach eingetragen wurde.
 */
public final class ValidatorLppa16LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Minderleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _minderleistung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param minderleistung   die Liste der Minderleistungsstunden
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLppa16LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_minderleistung = minderleistung;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = _minderleistung.get();
		if (liste == null) {
			return true;
		}

		final Set<Long> vorhandeneGruende = new HashSet<>();
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			if (eintrag.idGrund == null) {
				continue;
			}
			if (!vorhandeneGruende.add(eintrag.idGrund)) {
				addFehler(0, "Anrechnungsgründe des Lehrers: Der Minderleistungsgrund kommt mehrfach vor. Bitte prüfen.");
				return false;
			}
		}

		return true;
	}

}
