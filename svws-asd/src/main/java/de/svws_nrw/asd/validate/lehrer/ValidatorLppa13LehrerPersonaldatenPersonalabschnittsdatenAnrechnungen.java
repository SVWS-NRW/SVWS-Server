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
 * Prüft, ob ein Mehrleistungsgrund mehrfach eingetragen wurde.
 */
public final class ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Mehrleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _mehrleistung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param mehrleistung   die Liste der Mehrleistungsstunden
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_mehrleistung = mehrleistung;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = _mehrleistung.get();
		if (liste == null) {
			return true;
		}

		final Set<Long> vorhandeneGruende = new HashSet<>();
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			if (eintrag.idGrund == null) {
				continue;
			}
			if (!vorhandeneGruende.add(eintrag.idGrund)) {
				addFehler(0, "Anrechnungsgründe des Lehrers: Der Mehrleistungsgrund kommt mehrfach vor. Bitte prüfen.");
				return false;
			}
		}

		return true;
	}

}
