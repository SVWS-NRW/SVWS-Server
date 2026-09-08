package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.KursStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.OrteStatistikGesamt;
import de.svws_nrw.asd.data.statistik.ReligionStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.gesamt.ValidatorGlGesamtLehrerdaten;
import de.svws_nrw.asd.validate.gesamt.ValidatorGsGesamtSchuelerdaten;
import de.svws_nrw.asd.validate.intKataloge.ValidatorIkaIntKatalogKonfessionenAsdKatalog;
import de.svws_nrw.asd.validate.intKataloge.ValidatorIolIntKatalogOrteLand;
import de.svws_nrw.asd.validate.intKataloge.ValidatorIooIntKatalogOrteOrtsname;
import de.svws_nrw.asd.validate.intKataloge.ValidatorIopIntKatalogOrtePlz;
import de.svws_nrw.asd.validate.klassen.ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet;
import de.svws_nrw.asd.validate.klassen.ValidatorKkKlassenKlassenart;
import de.svws_nrw.asd.validate.klassen.ValidatorKlKlassenKlassenleitung;
import de.svws_nrw.asd.validate.klassen.ValidatorKoKlassenOrganisationsform;
import de.svws_nrw.asd.validate.klassen.ValidatorKsKlassenSchulgliederung;
import de.svws_nrw.asd.validate.kurse.ValidatorUfUnterrichtsverteilungsdatenFach;
import de.svws_nrw.asd.validate.kurse.ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft;
import de.svws_nrw.asd.validate.kurse.ValidatorUwUnterrichtsverteilungsdatenWochenstunden;
import de.svws_nrw.asd.validate.kurse.ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft;
import de.svws_nrw.asd.validate.kurse.ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden;
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

	/** Eine Map mit allen Sub-Validatoren, welche direkt auf Schülerdatensätzen arbeiten, zugeordnet zu ihrer ID */
	protected final @NotNull Map<Long, List<Validator>> mapValidatorenSchueler = new HashMap<>();

	/** Eine Map mit allen Sub-Validatoren, welche direkt auf Lehrerdatensätzen arbeiten, zugeordnet zu ihrer ID */
	protected final @NotNull Map<Long, List<Validator>> mapValidatorenLehrer = new HashMap<>();

	/** Eine Map mit allen Sub-Validatoren, welche direkt auf Klassendatensätzen arbeiten, zugeordnet zu ihrer ID */
	protected final @NotNull Map<Long, List<Validator>> mapValidatorenKlassen = new HashMap<>();

	/** Eine Map mit allen Sub-Validatoren, welche direkt auf Kursdatensätzen arbeiten, zugeordnet zu ihrer ID */
	protected final @NotNull Map<Long, List<Validator>> mapValidatorenKurse = new HashMap<>();


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
	}


	/**
	 * Fügt die Subvalidatoren für die Lehrerdatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Lehrerdatensätzen
	 */
	private void addSubvalidatorenLehrer(final @NotNull StatistikGesamt gesamt) {
		// Leere zunächst die Map für die Lehrervalidatoren
		mapValidatorenLehrer.clear();

		// Ergänze die allgemeinen Validatoren unter der ID -1
		final @NotNull List<Validator> allgemein = new ArrayList<>();
		allgemein.add(new ValidatorGlGesamtLehrerdaten(() -> gesamt.lehrer, this.kontext()));
		_validatoren.addAll(allgemein);
		mapValidatorenLehrer.put(-1L, allgemein);

		// Durchwandere die Lehrerdatensätze und ergänze die Subvalidatoren für die Lehrer
		for (final LehrerStatistikGesamt lehrer : gesamt.lehrer) {
			// Erzeuge die Liste der Subvalidatoren für den Lehrer
			final @NotNull List<Validator> list = new ArrayList<>();
			list.add(new ValidatorLsLehrerStammdaten(
					() -> lehrer.nachname,
					() -> lehrer.vorname,
					() -> lehrer.geburtsdatum,
					() -> lehrer.geschlecht,
					() -> lehrer.kuerzel,
					() -> lehrer.idRechtsverhaeltnis,
					this.kontext()));
			list.add(new ValidatorLpLehrerPersonaldaten(
					() -> lehrer.id,
					() -> gesamt.schule.idSchuljahresabschnitt,
					() -> lehrer.idStaatsangehoerigkeit,
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
					list.add(new ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung(
							() -> lehrbefaehigungen.idLehrbefaehigung,
							() -> LehrerLehramt.data().getWertByIDOrNull(lehraemter.idKatalogLehramt),
							this.kontext()));
				}
			}

			// Füge diese in die allgemeine Liste für die Ausführung ein und in die Map für den Lehrer
			_validatoren.addAll(list);
			mapValidatorenLehrer.put(lehrer.id, list);
		}
	}

	/**
	 * Fügt die Subvalidatoren für die Schülerdatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Schülerdatensätzen
	 */
	private void addSubvalidatorenSchueler(final @NotNull StatistikGesamt gesamt) {
		// Leere zunächst die Map für die Schülervalidatoren
		mapValidatorenSchueler.clear();

		// Ergänze die allgemeinen Validatoren unter der ID -1
		final @NotNull List<Validator> allgemein = new ArrayList<>();
		allgemein.add(new ValidatorGsGesamtSchuelerdaten(() -> gesamt.schueler, this.kontext()));
		_validatoren.addAll(allgemein);
		mapValidatorenSchueler.put(-1L, allgemein);

		// Durchwandere die Schülerdatensätze und ergänze die Subvalidatoren für die Schüler
		for (final SchuelerStatistikGesamt schueler : gesamt.schueler) {
			// Erzeuge die Liste der Subvalidatoren für den Schüler
			final @NotNull List<Validator> list = new ArrayList<>();
			list.add(new ValidatorSsSchuelerStammdaten(
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
				list.add(new ValidatorSlSchuelerLernabschnittsdaten(
						() -> lernabschnitt.idKlassenart,
						() -> lernabschnitt.idEpJahre,
						this.kontext()));
			}

			// Füge diese in die allgemeine Liste für die Ausführung ein und in die Map für den Schüler
			_validatoren.addAll(list);
			mapValidatorenSchueler.put(schueler.id, list);
		}
	}


	/**
	 * Fügt die Subvalidatoren für die Klassendatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Klassendatensätzen
	 */
	private void addSubvalidatorenKlassen(final @NotNull StatistikGesamt gesamt) {
		// Leere zunächst die Map für die Klassenvalidatoren
		mapValidatorenKlassen.clear();

		// Ergänze die allgemeinen Validatoren unter der ID -1
		final @NotNull List<Validator> allgemein = new ArrayList<>();
		allgemein.add(new ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet(
				() -> gesamt.klassen,
				this.kontext()));
		_validatoren.addAll(allgemein);
		mapValidatorenKlassen.put(-1L, allgemein);

		// Durchwandere die Klassendatensätze und ergänze die Subvalidatoren für die Klassen
		for (final KlassenStatistikGesamt klasse : gesamt.klassen) {
			// Erzeuge die Liste der Subvalidatoren für die Klasse
			final @NotNull List<Validator> list = new ArrayList<>();

			list.add(new ValidatorKkKlassenKlassenart(
					() -> null, //hier muss die idKlassenart hin -> gibt es in den daten noch nicht
					this.kontext()));
			list.add(new ValidatorKlKlassenKlassenleitung(
					() -> klasse.klassenLeitungen,
					this.kontext()));
			list.add(new ValidatorKoKlassenOrganisationsform(
					() -> null, //hier muss die idallgemeinbildungsorganisationsform hin -> gibt es in den daten noch nicht
					() -> null, //hier muss die idweiterbildungsorganisationsform hin    -> gibt es in den daten noch nicht
					() -> null, //hier muss die idberufsbildungsorganisationsform hin    -> gibt es in den daten noch nicht
					this.kontext()));
			list.add(new ValidatorKsKlassenSchulgliederung(
					() -> null, //hier muss die idSchulgliederung hin -> gibt es in den daten noch nicht,
					this.kontext()));

			// Füge diese in die allgemeine Liste für die Ausführung ein und in die Map für die Klasse
			_validatoren.addAll(list);
			mapValidatorenKlassen.put(klasse.id, list);
		}
	}


	/**
	 * Fügt die Subvalidatoren für die Kursdatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Kursdatensätzen
	 */
	private void addSubvalidatorenKurse(final @NotNull StatistikGesamt gesamt) {
		// Leere zunächst die Map für die Kursvalidatoren
		mapValidatorenKurse.clear();

		// Ergänze die allgemeinen Validatoren unter der ID -1
		final @NotNull List<Validator> allgemein = new ArrayList<>();
		// hier ggf. allgemeine Kurs-Validierung
		_validatoren.addAll(allgemein);
		mapValidatorenKurse.put(-1L, allgemein);

		// Durchwandere die Kursdatensätze und ergänze die Subvalidatoren für die Kurse
		for (final KursStatistikGesamt kurs : gesamt.kurse) {
			// Erzeuge die Liste der Subvalidatoren für die Kurse
			final @NotNull List<Validator> list = new ArrayList<>();

			list.add(new ValidatorUfUnterrichtsverteilungsdatenFach(
					() -> kurs.idFach,
					this.kontext()));
			list.add(new ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft(
					() -> kurs.lehrer,
					() -> gesamt.lehrer,
					this.kontext()));
			list.add(new ValidatorUwUnterrichtsverteilungsdatenWochenstunden(
					() -> (double) kurs.wochenstunden,
					this.kontext()));
			list.add(new ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft(
					() -> kurs.weitereLehrer,
					() -> gesamt.lehrer,
					this.kontext()));
			list.add(new ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(
					() -> kurs.wochenstundenLehrer,
					this.kontext()));

			// Füge diese in die allgemeine Liste für die Ausführung ein und in die Map für die Kurse
			_validatoren.addAll(list);
			mapValidatorenKurse.put(kurs.id, list);
		}
	}


	/**
	 * Fügt die Subvalidatoren für den Katalog der Orte hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit dem Katalog der Orte
	 */
	private void addSubvalidatorenKatalogOrte(final @NotNull StatistikGesamt gesamt) {
		for (final OrteStatistikGesamt ort : gesamt.orte) {
			_validatoren.add(new ValidatorIolIntKatalogOrteLand(
					() -> ort.idLand,
					this.kontext()));
			_validatoren.add(new ValidatorIooIntKatalogOrteOrtsname(
					() -> ort.plz,
					() -> ort.ortsname,
					() -> ort.idLand,
					this.kontext()));
			_validatoren.add(new ValidatorIopIntKatalogOrtePlz(
					() -> ort.plz,
					() -> ort.ortsname,
					() -> ort.idLand,
					this.kontext()));
		}
	}


	/**
	 * Fügt die Subvalidatoren für den Katalog der Religionen hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit dem Katalog der Religionen
	 */
	private void addSubvalidatorenKatalogReligion(final @NotNull StatistikGesamt gesamt) {
		for (final ReligionStatistikGesamt religion : gesamt.religionen) {
			_validatoren.add(new ValidatorIkaIntKatalogKonfessionenAsdKatalog(
					() -> religion.idKatalog,
					this.kontext()));
		}
	}


	/**
	 * Gibt die Liste der Validatoren für den Schüler mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return die Liste der Validatoren
	 */
	public @NotNull List<Validator> getBySchuelerID(final long id) {
		final List<Validator> result = mapValidatorenSchueler.get(id);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}


	/**
	 * Gibt die Liste der Validatoren für den Lehrer mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return die Liste der Validatoren
	 */
	public @NotNull List<Validator> getByLehrerID(final long id) {
		final List<Validator> result = mapValidatorenLehrer.get(id);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}


	/**
	 * Gibt die Liste der Validatoren für die Klasse mit der übergebenen ID zurück.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Liste der Validatoren
	 */
	public @NotNull List<Validator> getByKlassenID(final long id) {
		final List<Validator> result = mapValidatorenKlassen.get(id);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}


	/**
	 * Gibt die Liste der Validatoren für die Kurse mit der übergebenen ID zurück.
	 *
	 * @param id   die ID der Kurse
	 *
	 * @return die Liste der Validatoren
	 */
	public @NotNull List<Validator> getByKursID(final long id) {
		final List<Validator> result = mapValidatorenKurse.get(id);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}


	/**
	 * Gibt die Liste der Validator-Fehler für den Schüler mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerBySchuelerID(final long id) {
		return Validator.getFehlerOfListe(this.getBySchuelerID(id));
	}


	/**
	 * Gibt die Liste der Validator-Fehler für den Lehrer mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerByLehrerID(final long id) {
		return Validator.getFehlerOfListe(this.getByLehrerID(id));
	}


	/**
	 * Gibt die Liste der Validator-Fehler für die Klasse mit der übergebenen ID zurück.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerByKlassenID(final long id) {
		return Validator.getFehlerOfListe(this.getByKlassenID(id));
	}


	/**
	 * Gibt die Liste der Validator-Fehler für die Kurse mit der übergebenen ID zurück.
	 *
	 * @param id   die ID der Kurse
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerByKursID(final long id) {
		return Validator.getFehlerOfListe(this.getByKursID(id));
	}


	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Schüler zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerSchueler() {
		return Validator.getFehlerOfListen(mapValidatorenSchueler.values());
	}


	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Lehrer zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerLehrer() {
		return Validator.getFehlerOfListen(mapValidatorenLehrer.values());
	}


	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Klassen zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerKlassen() {
		return Validator.getFehlerOfListen(mapValidatorenKlassen.values());
	}


	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Kurse zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public @NotNull List<ValidatorFehler> getFehlerKurse() {
		return Validator.getFehlerOfListen(mapValidatorenKurse.values());
	}


	@Override
	protected boolean pruefe() {
		_validatoren.clear();
		_validatoren.addAll(validatoren);

		// Erzeuge dynamisch Subvalidatoren anhand der übergebenen Daten zur späteren Ausführung in diesem Validator
		final @NotNull StatistikGesamt gesamt = daten.get();
		this.addSubvalidatorenLehrer(gesamt);
		this.addSubvalidatorenSchueler(gesamt);
		this.addSubvalidatorenKlassen(gesamt);
		this.addSubvalidatorenKatalogReligion(gesamt);
		this.addSubvalidatorenKatalogOrte(gesamt);
		this.addSubvalidatorenKurse(gesamt);

		return true;
	}

}
