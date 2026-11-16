package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei den Minderleistungsgründen ID 230, 240 oder 250
 * keine zusätzlichen Anrechnungsgründe eingetragen sind.
 */
public final class ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _anrechnungen;

	/** Die Liste der Minderleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _minderleistung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param anrechnungen     die Liste der Anrechnungsstunden
	 * @param minderleistung   die Liste der Minderleistungsstunden
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_anrechnungen = anrechnungen;
		_minderleistung = minderleistung;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeAnrechnungen = _anrechnungen.get();
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeMinderleistung = _minderleistung.get();

		if ((listeAnrechnungen == null) || (listeMinderleistung == null)) {
			return true;
		}

		final LehrerMinderleistungsarten minder230 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_230");
		final LehrerMinderleistungsarten minder240 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_240");
		final LehrerMinderleistungsarten minder250 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_250");

		boolean hatVollabwesenheit = false;
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : listeMinderleistung) {
			if (eintrag.idGrund == null) {
				continue;
			}
			final LehrerMinderleistungsarten art = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art == minder230) || (art == minder240) || (art == minder250)) {
				hatVollabwesenheit = true;
				break;
			}
		}

		if (!hatVollabwesenheit) {
			return true;
		}

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden anrechnung : listeAnrechnungen) {
			if ((anrechnung.idGrund != null) || (Math.abs(anrechnung.anzahl) > 0.001)) {
				addFehler(0,
						"Anrechnungsgründe des Lehrers: Bei Eintragung der Minderleistungsgründe 'Beurlaubung, Rückkehr im Laufe des Schuljahres', 'langfristige Erkrankung' und 'Abwesenheit wegen Beschäftigungsverbot gem. § 3 MuSchG' dürfen keine Anrechnungsgründe eingetragen werden.");
				return false;
			}
		}

		return true;
	}

}
