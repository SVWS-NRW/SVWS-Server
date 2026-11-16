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
 * Prüft, ob der gewählte Anrechnungsgrund im Kontext des aktuellen Schuljahres gültig ist.
 */
public final class ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Das aktuelle Schuljahr */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _anrechnungen;

	/**
	 * Erstellt einen neuen Validator für die zeitliche Gültigkeit der Anrechnungsgründe.
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
	public ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
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
		_schuljahr = schuljahr;
		_anrechnungen = anrechnungen;

		_validatoren.add(new ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, kontext));
		_validatoren.add(new ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));
		_validatoren.add(new ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden(mehrleistung, minderleistung, kontext));
		_validatoren.add(new ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(mehrleistung, kontext));
		_validatoren
				.add(new ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(mehrleistung, idBeschaeftigungsart, idEinsatzstatus, kontext));
		_validatoren
				.add(new ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, idBeschaeftigungsart, idEinsatzstatus, kontext));
		_validatoren.add(new ValidatorLppa16LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, kontext));
		_validatoren.add(new ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, geschlecht, kontext));
		_validatoren.add(new ValidatorLppa18LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, pflichtstundensoll, kontext));
		_validatoren.add(new ValidatorLppa19LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, pflichtstundensoll, kontext));
		_validatoren.add(new ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, pflichtstundensoll, kontext));
		_validatoren.add(new ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, minderleistung, kontext));
		_validatoren.add(new ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, kontext));
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = _anrechnungen.get();
		if (liste == null) {
			return true;
		}

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			if (!LehrerAnrechnungsgrund.data().isGueltig(eintrag.idGrund, _schuljahr.get())) {
				addFehler(0, "Der eingetragene Wert für das Feld 'Anrechnungsgründe' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
				return false;
			}
		}
		return true;
	}

}
