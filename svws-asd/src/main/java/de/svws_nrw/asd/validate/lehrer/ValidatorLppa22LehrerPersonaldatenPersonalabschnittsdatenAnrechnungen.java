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
 * Prüft, ob ein Anrechnungsgrund mehrfach eingetragen wurde.
 */
public final class ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _anrechnungen;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param anrechnungen   die Liste der Anrechnungsstunden
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_anrechnungen = anrechnungen;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = _anrechnungen.get();
		if (liste == null) {
			return true;
		}

		final Set<Long> vorhandeneGruende = new HashSet<>();
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			if (eintrag.idGrund == null) {
				continue;
			}
			if (!vorhandeneGruende.add(eintrag.idGrund)) {
				addFehler(0, "Anrechnungsgründe des Lehrers: Der Anrechnungsgrund kommt mehrfach vor. Bitte prüfen.");
				return false;
			}
		}

		return true;
	}

}
