package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei dem Minderleistungsgrund 'langfristige Erkrankung' (ID 240)
 * die Stundenanzahl dem Pflichtstundensoll entspricht.
 */
public final class ValidatorLppa19LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Minderleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _minderleistung;

	/** Das Pflichtstundensoll der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Double> _pflichtstundensoll;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param minderleistung       die Liste der Minderleistungsstunden
	 * @param pflichtstundensoll   das Pflichtstundensoll
	 * @param kontext              der Kontext des Validators
	 */
	public ValidatorLppa19LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistung,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_minderleistung = minderleistung;
		_pflichtstundensoll = pflichtstundensoll;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = _minderleistung.get();
		final Double soll = _pflichtstundensoll.get();

		if ((liste == null) || (soll == null)) {
			return true;
		}

		final LehrerMinderleistungsarten minder240 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_240");
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			if (eintrag.idGrund == null) {
				continue;
			}
			final LehrerMinderleistungsarten art = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art == minder240) && (Math.abs(eintrag.anzahl - soll) > 0.001)) {
				addFehler(0,
						"Anrechnungsstunden des Lehrers: Bei Eintragung des Minderleistungsgrunds 'langfristige Erkrankung' muss die Zahl der Ermäßigungsstunden dem Pflichtstundensoll der Lehrkraft entsprechen.");
				return false;
			}
		}

		return true;
	}

}
