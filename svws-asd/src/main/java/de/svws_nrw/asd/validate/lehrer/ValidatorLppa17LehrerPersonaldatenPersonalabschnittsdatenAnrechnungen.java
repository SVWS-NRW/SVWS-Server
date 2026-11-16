package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Minderleistungsgründe nach § 3 MuSchG (ID 250, ID 255)
 * nicht bei männlichen Lehrkräften eingetragen sind.
 */
public final class ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Minderleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _minderleistung;

	/** Das Geschlecht der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Integer> _geschlecht;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param minderleistung   die Liste der Minderleistungsstunden
	 * @param geschlecht       das Geschlecht der Lehrkraft
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistung,
			final @NotNull Supplier<@AllowNull Integer> geschlecht,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_minderleistung = minderleistung;
		_geschlecht = geschlecht;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = _minderleistung.get();
		final Integer geschlechtId = _geschlecht.get();

		if ((liste == null) || (geschlechtId == null)) {
			return true;
		}

		final Geschlecht geschlecht = Geschlecht.fromValue(geschlechtId);
		if ((geschlecht == Geschlecht.W) || (geschlecht == Geschlecht.D) || (geschlecht == Geschlecht.X)) {
			return true;
		}

		final LehrerMinderleistungsarten minder250 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_250");
		final LehrerMinderleistungsarten minder255 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_255");

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			if (eintrag.idGrund == null) {
				continue;
			}
			final LehrerMinderleistungsarten art = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art == minder250) || (art == minder255)) {
				addFehler(0,
						"Anrechnungsgründe des Lehrers: Die Minderleistungsgründe 'Abwesenheit wegen Beschäftigungsverbot gem. § 3 MuSchG' oder 'Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG' dürfen nur bei weiblichen Lehrkräften angegeben werden.");
				return false;
			}
		}

		return true;
	}

}

