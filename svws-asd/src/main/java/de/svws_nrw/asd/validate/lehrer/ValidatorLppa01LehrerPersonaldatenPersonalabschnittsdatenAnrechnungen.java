package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob der angegebene Anrechnungsgrund im Katalog der Anrechnungsgründe existiert.
 */
public final class ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _anrechnungen;

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param schuljahr              Schuljahr
	 * @param anrechnungen           die Liste der Anrechnungsstunden
	 * @param mehrleistung           die Liste der Mehrleistungsstunden
	 * @param minderleistung         die Liste der Minderleistungsstunden
	 * @param lehraemter             die Liste der Lehrämter
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param idEinsatzstatus        die ID des Einsatzstatus
	 * @param geschlecht             das Geschlecht der Lehrkraft
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistung,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistung,
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Integer> geschlecht,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_anrechnungen = anrechnungen;

		_validatoren.add(new ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
				schuljahr, anrechnungen, mehrleistung, minderleistung, lehraemter,
				pflichtstundensoll, idBeschaeftigungsart, idEinsatzstatus, geschlecht, kontext));
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = _anrechnungen.get();

		if (liste == null) {
			return true;
		}

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			// Prüfung, ob die ID im Katalog vorhanden ist.
			// Hinweis: Die reine Pflichtfeldprüfung (idGrund == null) erfolgt bereits in LPPA00.
			if ((eintrag.idGrund == null) || (LehrerAnrechnungsgrund.data().getWertByIDOrNull(eintrag.idGrund) == null)) {
				addFehler(0, "Feld 'Anrechnungsgründe' muss besetzt sein.");
				return false;
			}
		}

		return true;
	}

}
