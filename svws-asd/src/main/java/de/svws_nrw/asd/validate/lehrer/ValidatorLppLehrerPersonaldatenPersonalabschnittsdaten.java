package de.svws_nrw.asd.validate.lehrer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Personalabschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten extends Validator {

	/** Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden. */
	protected final @NotNull List<Validator> validatoren = new ArrayList<>();

	/** das Geburtsdatum des Lehrers */
	protected final @NotNull Supplier<@AllowNull String> geburtsdatum;

	/** Die ID des Schuljahresabschnittes */
	protected final @NotNull Supplier<Long> idSchuljahresabschnitt;

	/** Das Rechtsverhältnis */
	protected final @NotNull Supplier<@AllowNull String> rechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param rechtsverhaeltnis       das Rechtsverhältnis
	 * @param pflichtstundensoll      der Pflichtstundensoll
	 * @param einsatzstatus           der Einsatz-Status
	 * @param beschaeftigungsart      die Beschäftigungsart
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param mehrleistungen          die Liste mit den Einträgen zu Mehrleistungen
	 * @param minderleistungen        die Liste mit den Einträgen zu Minderleistungen
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull String> rechtsverhaeltnis,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull String> einsatzstatus,
			final @NotNull Supplier<@AllowNull String> beschaeftigungsart,
			final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.geburtsdatum = geburtsdatum;
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.rechtsverhaeltnis = rechtsverhaeltnis;
		validatoren.add(
				new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, einsatzstatus, beschaeftigungsart, kontext));
		validatoren.add(
				new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(getNotNullSupplier(beschaeftigungsart),
						getNotNullSupplier(einsatzstatus), pflichtstundensoll, kontext));
		validatoren.add(new ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(beschaeftigungsart, pflichtstundensoll,
				einsatzstatus, mehrleistungen, minderleistungen, kontext));
		// Die nachfolgenden Prüfungen sind nur durchführbar, wenn bei den Stammdaten ein Geburtsdatum gesetzt ist...

	}

	@Override
	protected boolean pruefe() {
		_validatoren.clear();
		_validatoren.addAll(validatoren);
		try {
			final @NotNull DateManager datum = DateManager.from(this.geburtsdatum.get());
			final @NotNull Supplier<@AllowNull DateManager> supplierGeburtsdatumNullable = () -> datum;
			final @NotNull Supplier<DateManager> supplierGeburtsdatum = this.getNotNullObjectSupplier(supplierGeburtsdatumNullable);
			_validatoren.add(new ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnis,
					supplierGeburtsdatum, this.kontext()));
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			// Ist kein gültiges Geburtsdatum gesetzt, so werden die Prüfungen übersprungen.
			// Die eigentliche Validierung des Geburtsdatums erfolgt bei den Lehrer-Stammdaten
		}

		return true;
	}

}
