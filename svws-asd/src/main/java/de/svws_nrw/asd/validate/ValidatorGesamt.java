package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.gesamt.ValidatorGlGesamtLehrerdaten;
import de.svws_nrw.asd.validate.gesamt.ValidatorGsGesamtSchuelerdaten;
import de.svws_nrw.asd.validate.klassen.ValidatorKkKlassenKlassenart;
import de.svws_nrw.asd.validate.klassen.ValidatorKoKlassenOrganisationsform;
import de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten;
import de.svws_nrw.asd.validate.lehrer.ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung;
import de.svws_nrw.asd.validate.lehrer.ValidatorLsLehrerStammdaten;
import de.svws_nrw.asd.validate.schueler.ValidatorSlSchuelerLernabschnittsdaten;
import de.svws_nrw.asd.validate.schueler.ValidatorSsSchuelerStammdaten;
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

		validatoren.add(new ValidatorGsGesamtSchuelerdaten(() -> daten.get().schueler, kontext));
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
					() -> lehrer.idStaatsangehoerigkeit,
					() -> lehrer.idRechtsverhaeltnis,
					this.kontext()));
			_validatoren.add(new ValidatorLpLehrerPersonaldaten(
					() -> lehrer.id,
					() -> gesamt.schule.idSchuljahresabschnitt,
					() -> lehrer.idRechtsverhaeltnis,
					() -> lehrer.pflichtstundensoll,
					() -> lehrer.anrechnungen,
					() -> lehrer.idEinsatzstatus,
					() -> lehrer.idBeschaeftigungsart,
					() -> lehrer.geburtsdatum,
					() -> lehrer.lehraemter,
					() -> lehrer.mehrleistung,
					() -> lehrer.minderleistung,
					this.kontext()));

			for (final LehrerLehramtEintrag lehraemter : lehrer.lehraemter) {
				for (final LehrerLehrbefaehigungEintrag lehrbefaehigungen : lehraemter.lehrbefaehigungen) {
							_validatoren.add(new ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung(
									() -> lehrbefaehigungen.idLehrbefaehigung,
									() -> LehrerLehramt.data().getWertByIDOrNull(lehraemter.idKatalogLehramt),
									this.kontext()));
				}
			}
		}

		for (final SchuelerStatistikGesamt schueler : gesamt.schueler) {
			_validatoren.add(new ValidatorSsSchuelerStammdaten(
					() -> schueler.geschlecht,
					() -> schueler.geburtsdatum,
					() -> schueler.idGeburtsland,
					() -> schueler.idGeburtslandMutter,
					() -> schueler.idGeburtslandVater,
					() -> schueler.hatMigrationshintergrund,
					() -> schueler.idStaatsangehoerigkeit,
					() -> schueler.idStaatsangehoerigkeit2,
					this.kontext()));
			for (final SchuelerLernabschnittStatistikGesamt lernabschnitt : schueler.lernabschnitte) {
				_validatoren.add(new ValidatorSlSchuelerLernabschnittsdaten(
						() -> lernabschnitt.idKlassenart,
						() -> (long) lernabschnitt.epJahre,
						this.kontext()));
			}
		}

		for (final KlassenStatistikGesamt klassen : gesamt.klassen) {
			_validatoren.add(new ValidatorKkKlassenKlassenart(
					() -> null, //hier muss die idKlassenart hin -> gibt es in den daten noch nicht
					this.kontext()));
			_validatoren.add(new ValidatorKoKlassenOrganisationsform(
					() -> null, //hier muss die idallgemeinbildungsorganisationsform hin -> gibt es in den daten noch nicht
					() -> null, //hier muss die idweiterbildungsorganisationsform hin    -> gibt es in den daten noch nicht
					() -> null, //hier muss die idberufsbildungsorganisationsform hin    -> gibt es in den daten noch nicht
					this.kontext()));
		}

		return true;
	}

}
