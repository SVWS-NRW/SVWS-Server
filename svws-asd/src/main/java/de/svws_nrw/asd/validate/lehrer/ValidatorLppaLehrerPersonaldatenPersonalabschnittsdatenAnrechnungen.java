package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Sammel-Validator für die Anrechnungsstunden der Lehrer-Personalabschnittsdaten (LPPA00 bis LPPA22).
 */
public final class ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Sammel-Validator für Anrechnungsdaten.
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param anrechnungen             die Liste der Anrechnungsstunden
	 * @param mehrleistung             die Liste der Mehrleistungsstunden
	 * @param minderleistung           die Liste der Minderleistungsstunden
	 * @param lehraemter               die Liste der Lehrämter
	 * @param pflichtstundensoll       das Pflichtstundensoll
	 * @param idBeschaeftigungsart     die ID der Beschäftigungsart
	 * @param idEinsatzstatus          die ID des Einsatzstatus
	 * @param geschlecht               das Geschlecht der Lehrkraft
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
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

		_idSchuljahresabschnitt = idSchuljahresabschnitt;

		final @NotNull Supplier<@NotNull Integer> schuljahr = () -> kontext.getSchuljahrBySchuljahresabschnitt(getNotNullSupplierLong(idSchuljahresabschnitt));

		_validatoren.add(new ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
				schuljahr, anrechnungen, mehrleistung, minderleistung, lehraemter,
				pflichtstundensoll, idBeschaeftigungsart, idEinsatzstatus, geschlecht, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Schuljahresabschnitt sja = kontext().getSchuljahresabschnittByID(getNotNullSupplierLong(_idSchuljahresabschnitt).get());
		if (sja == null) {
			addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}

		return true;
	}

}
