package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.validate.gesamt.ValidatorGlGesamtLehrerdaten;
import de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten;
import de.svws_nrw.asd.validate.lehrer.ValidatorLsLehrerStammdaten;
import de.svws_nrw.asd.validate.schule.ValidatorSssSchuleStammdatenSchulform;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf alle Daten einer Schule aus.
 */
public final class ValidatorGesamt extends Validator {

	/** Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden. */
	protected final @NotNull List<Validator> validatoren = new ArrayList<>();

	/** Die Daten des Validators */
	protected final @NotNull Supplier<StatistikGesamt> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorGesamt(final @NotNull Supplier<StatistikGesamt> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;

		validatoren.add(new ValidatorSssSchuleStammdatenSchulform(() -> daten.get().schule.schulform, kontext));

		validatoren.add(new ValidatorGlGesamtLehrerdaten(() -> daten.get().lehrer, kontext));
	}

	@Override
	protected boolean pruefe() {
		_validatoren.clear();
		_validatoren.addAll(validatoren);

		final @NotNull StatistikGesamt gesamt = daten.get();
		for (final LehrerStatistikGesamt lehrer : gesamt.lehrer) {
			_validatoren.add(new ValidatorLsLehrerStammdaten(() -> lehrer.nachname,
					() -> lehrer.vorname,
					() -> lehrer.geburtsdatum,
					() -> lehrer.geschlecht,
					() -> lehrer.kuerzel,
					this.kontext()));
			_validatoren.add(new ValidatorLpLehrerPersonaldaten(
					() -> lehrer.id,
					() -> gesamt.schule.idSchuljahresabschnitt,
					() -> lehrer.rechtsverhaeltnis,
					() -> lehrer.pflichtstundensoll,
					() -> lehrer.einsatzstatus,
					() -> lehrer.beschaeftigungsart,
					() -> lehrer.geburtsdatum,
					() -> lehrer.lehraemter,
					() -> lehrer.mehrleistung,
					() -> lehrer.minderleistung,
					this.kontext()));
		}
		return true;
	}

}
