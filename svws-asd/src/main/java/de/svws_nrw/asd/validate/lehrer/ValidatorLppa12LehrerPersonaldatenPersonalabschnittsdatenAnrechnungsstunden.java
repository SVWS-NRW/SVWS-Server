package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei der Mehrleistung 'Mehrarbeit (angeordnet und regelmäßig)' (ID 110)
 * nicht gleichzeitig unzulässige Minderleistungen (ID 230, 240, 250) eingetragen sind.
 */
public final class ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden extends Validator {

	/** Die Liste der Mehrleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _mehrleistung;

	/** Die Liste der Minderleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _minderleistung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param mehrleistung     die Liste der Mehrleistungsstunden
	 * @param minderleistung   die Liste der Minderleistungsstunden
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistung,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_mehrleistung = mehrleistung;
		_minderleistung = minderleistung;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeMehrleistung = _mehrleistung.get();
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeMinderleistung = _minderleistung.get();

		if ((listeMehrleistung == null) || (listeMinderleistung == null)) {
			return true;
		}

		final LehrerMehrleistungsarten mehrarbeit110 = LehrerMehrleistungsarten.data().getWertByBezeichner("ID_110");
		boolean hatMehrarbeit110 = false;
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : listeMehrleistung) {
			if ((eintrag.idGrund != null) && (LehrerMehrleistungsarten.data().getWertByIDOrNull(eintrag.idGrund) == mehrarbeit110)) {
				hatMehrarbeit110 = true;
				break;
			}
		}

		if (!hatMehrarbeit110) {
			return true;
		}

		final LehrerMinderleistungsarten minder230 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_230");
		final LehrerMinderleistungsarten minder240 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_240");
		final LehrerMinderleistungsarten minder250 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_250");

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : listeMinderleistung) {
			if (eintrag.idGrund == null) {
				continue;
			}
			final LehrerMinderleistungsarten art = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art == minder230) || (art == minder240) || (art == minder250)) {
				addFehler(0,
						"Anrechnungsgrund des Lehrers: Bei einer Lehrkraft mit der Mehrleistung 'Mehrarbeit (angeordnet und regelmäßig)' dürfen nicht zusätzlich die Minderleistungen 'Beurlaubung (auch Elternzeit), Rückkehr im Laufe des Schuljahres', 'Langfristige Erkrankung' oder 'Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG' eingetragen sein.");
				return false;
			}
		}

		return true;
	}

}
