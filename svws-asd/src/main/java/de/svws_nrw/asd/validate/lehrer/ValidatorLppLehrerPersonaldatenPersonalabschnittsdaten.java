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
	private final @NotNull Supplier<@AllowNull String> geburtsdatum;

	/** Die ID des Schuljahresabschnittes, auf den sich die Personalabschnittsdaten beziehen. */
	private final @NotNull Supplier<Long> idSchuljahresabschnitt;

	/** Das Rechtsverhältnis der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull String> rechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Sammel-Validator für Personalabschnittsdaten.
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param rechtsverhaeltnis        das Rechtsverhältnis
	 * @param pflichtstundensoll      das Pflichtstundensoll
	 * @param anrechnungen            die Liste der Anrechnungsstunden
	 * @param einsatzstatus           der Einsatz-Status
	 * @param beschaeftigungsart      die Beschäftigungsart
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param lehraemter              die Liste der Lehrämter der Lehrkraft
	 * @param mehrleistungen          die Liste der Mehrleistungen
	 * @param minderleistungen        die Liste der Minderleistungen
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull String> rechtsverhaeltnis,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<@AllowNull String> einsatzstatus,
			final @NotNull Supplier<@AllowNull String> beschaeftigungsart,
			final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.geburtsdatum = geburtsdatum;
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.rechtsverhaeltnis = rechtsverhaeltnis;

		// Hinzufügen der fachspezifischen Validatoren
		validatoren.add(
				new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, einsatzstatus, beschaeftigungsart, kontext));

		validatoren.add(new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(getNotNullSupplier(beschaeftigungsart),
				getNotNullSupplier(einsatzstatus), pflichtstundensoll, kontext));

		validatoren.add(new ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(beschaeftigungsart, pflichtstundensoll,
				einsatzstatus, mehrleistungen, minderleistungen, kontext));

		validatoren.add(new ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));
	}

	@Override
	protected boolean pruefe() {
		_validatoren.clear();
		_validatoren.addAll(validatoren);

		try {
			// Prüfung des Rechtsverhältnisses erfordert ein gültiges Geburtsdatum
			final @NotNull DateManager datum = DateManager.from(this.geburtsdatum.get());
			final @NotNull Supplier<@AllowNull DateManager> supplierGeburtsdatumNullable = () -> datum;
			final @NotNull Supplier<DateManager> supplierGeburtsdatum = this.getNotNullObjectSupplier(supplierGeburtsdatumNullable);

			_validatoren.add(new ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnis,
					supplierGeburtsdatum, this.kontext()));
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			// Falls kein gültiges Geburtsdatum vorliegt, wird die spezifische Altersprüfung
			// für das Rechtsverhältnis übersprungen. Die Validierung des Geburtsdatums
			// selbst erfolgt separat in den Lehrer-Stammdaten.
		}

		return true;
	}
}
