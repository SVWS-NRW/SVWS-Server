package de.svws_nrw.asd.validate.lehrer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Sammel-Validator führt Statistikprüfungen auf die Personalabschnittsdaten
 * eines Lehrers aus. Er bündelt spezifische Einzelprüfungen (Pflichtstundensoll,
 * Beschäftigungsart, Rechtsverhältnis und Anrechnungen).
 */
public final class ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten extends Validator {

	/** Eine Liste von Einzel-Validatoren, die von diesem Sammel-Validator verwaltet werden. */
	private final @NotNull List<Validator> validatoren = new ArrayList<>();

	/** Das Geburtsdatum des Lehrers (aus den Stammdaten). */
	private final @NotNull Supplier<@AllowNull String> _geburtsdatum;

	/** Die ID des Schuljahresabschnittes, auf den sich die Personalabschnittsdaten beziehen. */
	private final @NotNull Supplier<Long> _idSchuljahresabschnitt;

	/** Die Staatsangehoerigkeit der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Long> _idStaatsangehoerigkeit;

	/** Das Rechtsverhältnis der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Long> _idRechtsverhaeltnis;


	/**
	 * Erstellt einen neuen Sammel-Validator für Personalabschnittsdaten.
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param idStaatsangehoerigkeit  die idStaatsangehoerigkeit des Lehrers
	 * @param idRechtsverhaeltnis     das Rechtsverhältnis
	 * @param pflichtstundensoll      das Pflichtstundensoll
	 * @param anrechnungen            die Liste der Anrechnungsstunden
	 * @param idEinsatzstatus         der Einsatz-Status
	 * @param idBeschaeftigungsart    die Beschäftigungsart
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param lehraemter              die Liste der Lehrämter der Lehrkraft
	 * @param mehrleistungen          die Liste der Mehrleistungen
	 * @param minderleistungen        die Liste der Minderleistungen
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull Supplier<@AllowNull Long> idRechtsverhaeltnis,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._geburtsdatum = geburtsdatum;
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		this._idRechtsverhaeltnis = idRechtsverhaeltnis;
		// Hinzufügen der fachspezifischen Validatoren
		validatoren.add(new ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));

		validatoren.add(new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(getNotNullSupplierLong(idBeschaeftigungsart),
				getNotNullSupplierLong(idEinsatzstatus), pflichtstundensoll, kontext));

		validatoren.add(new ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(idBeschaeftigungsart, pflichtstundensoll,
				idEinsatzstatus, mehrleistungen, minderleistungen, kontext));

		validatoren.add(new ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(idEinsatzstatus, kontext));

		validatoren.add(
				new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, idEinsatzstatus, idBeschaeftigungsart,
						kontext));

	}

	@Override
	protected boolean pruefe() {
		_validatoren.clear();
		_validatoren.addAll(validatoren);

		try {
			// Prüfung des Rechtsverhältnisses erfordert ein gültiges Geburtsdatum
			final @NotNull DateManager datum = DateManager.from(this._geburtsdatum.get());
			final @NotNull Supplier<@AllowNull DateManager> supplierGeburtsdatumNullable = () -> datum;
			final @NotNull Supplier<DateManager> supplierGeburtsdatum = this.getNotNullSupplierObject(supplierGeburtsdatumNullable);

			_validatoren.add(new ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(_idSchuljahresabschnitt, _idStaatsangehoerigkeit, _idRechtsverhaeltnis,
					supplierGeburtsdatum, this.kontext()));
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			// Falls kein gültiges Geburtsdatum vorliegt, wird die spezifische Altersprüfung
			// für das Rechtsverhältnis übersprungen. Die Validierung des Geburtsdatums
			// selbst erfolgt separat in den Lehrer-Stammdaten.
		}

		return true;
	}
}
