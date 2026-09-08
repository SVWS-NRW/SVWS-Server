import { ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft } from '../../asd/validate/kurse/ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft';
import { HashMap } from '../../java/util/HashMap';
import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorSlSchuelerLernabschnittsdaten } from '../../asd/validate/schueler/ValidatorSlSchuelerLernabschnittsdaten';
import { ValidatorKoKlassenOrganisationsform } from '../../asd/validate/klassen/ValidatorKoKlassenOrganisationsform';
import { ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung } from '../../asd/validate/lehrer/ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung';
import { ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft } from '../../asd/validate/kurse/ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft';
import { ValidatorIolIntKatalogOrteLand } from '../../asd/validate/intKataloge/ValidatorIolIntKatalogOrteLand';
import { ValidatorUwUnterrichtsverteilungsdatenWochenstunden } from '../../asd/validate/kurse/ValidatorUwUnterrichtsverteilungsdatenWochenstunden';
import { ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet } from '../../asd/validate/klassen/ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet';
import { ValidatorKsKlassenSchulgliederung } from '../../asd/validate/klassen/ValidatorKsKlassenSchulgliederung';
import { ValidatorIopIntKatalogOrtePlz } from '../../asd/validate/intKataloge/ValidatorIopIntKatalogOrtePlz';
import type { List } from '../../java/util/List';
import type { Supplier } from '../../java/util/function/Supplier';
import { ValidatorIooIntKatalogOrteOrtsname } from '../../asd/validate/intKataloge/ValidatorIooIntKatalogOrteOrtsname';
import { ValidatorKlKlassenKlassenleitung } from '../../asd/validate/klassen/ValidatorKlKlassenKlassenleitung';
import { ValidatorSsSchuelerStammdaten } from '../../asd/validate/schueler/ValidatorSsSchuelerStammdaten';
import { ValidatorIkaIntKatalogKonfessionenAsdKatalog } from '../../asd/validate/intKataloge/ValidatorIkaIntKatalogKonfessionenAsdKatalog';
import { ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden } from '../../asd/validate/kurse/ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden';
import { ValidatorLpLehrerPersonaldaten } from '../../asd/validate/lehrer/ValidatorLpLehrerPersonaldaten';
import { ValidatorFehler } from '../../asd/validate/ValidatorFehler';
import { ValidatorGlGesamtLehrerdaten } from '../../asd/validate/gesamt/ValidatorGlGesamtLehrerdaten';
import { ValidatorKkKlassenKlassenart } from '../../asd/validate/klassen/ValidatorKkKlassenKlassenart';
import { ValidatorGsGesamtSchuelerdaten } from '../../asd/validate/gesamt/ValidatorGsGesamtSchuelerdaten';
import { LehrerLehramt } from '../../asd/types/lehrer/LehrerLehramt';
import { ValidatorSssSchuleStammdatenSchulform } from '../../asd/validate/schule/ValidatorSssSchuleStammdatenSchulform';
import { Class } from '../../java/lang/Class';
import { StatistikGesamt } from '../../asd/data/statistik/StatistikGesamt';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';
import type { JavaMap } from '../../java/util/JavaMap';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';
import { ValidatorUfUnterrichtsverteilungsdatenFach } from '../../asd/validate/kurse/ValidatorUfUnterrichtsverteilungsdatenFach';
import { ValidatorLsLehrerStammdaten } from '../../asd/validate/lehrer/ValidatorLsLehrerStammdaten';

export class ValidatorGesamt extends Validator {

	/**
	 * Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden.
	 */
	protected readonly validatoren: List<Validator> = new ArrayList<Validator>();

	/**
	 * Die Daten des Validators
	 */
	protected readonly daten: Supplier<StatistikGesamt>;

	/**
	 * Eine Map mit allen Sub-Validatoren, welche direkt auf Schülerdatensätzen arbeiten, zugeordnet zu ihrer ID
	 */
	protected readonly mapValidatorenSchueler: JavaMap<number, List<Validator>> = new HashMap<number, List<Validator>>();

	/**
	 * Eine Map mit allen Sub-Validatoren, welche direkt auf Lehrerdatensätzen arbeiten, zugeordnet zu ihrer ID
	 */
	protected readonly mapValidatorenLehrer: JavaMap<number, List<Validator>> = new HashMap<number, List<Validator>>();

	/**
	 * Eine Map mit allen Sub-Validatoren, welche direkt auf Klassendatensätzen arbeiten, zugeordnet zu ihrer ID
	 */
	protected readonly mapValidatorenKlassen: JavaMap<number, List<Validator>> = new HashMap<number, List<Validator>>();

	/**
	 * Eine Map mit allen Sub-Validatoren, welche direkt auf Kursdatensätzen arbeiten, zugeordnet zu ihrer ID
	 */
	protected readonly mapValidatorenKurse: JavaMap<number, List<Validator>> = new HashMap<number, List<Validator>>();


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<StatistikGesamt>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this.validatoren.add(new ValidatorSssSchuleStammdatenSchulform({ get: () => daten.get().schule.schulform }, kontext));
	}

	/**
	 * Fügt die Subvalidatoren für die Lehrerdatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Lehrerdatensätzen
	 */
	private addSubvalidatorenLehrer(gesamt: StatistikGesamt): void {
		this.mapValidatorenLehrer.clear();
		const allgemein: List<Validator> = new ArrayList<Validator>();
		allgemein.add(new ValidatorGlGesamtLehrerdaten({ get: () => gesamt.lehrer }, this.kontext()));
		this._validatoren.addAll(allgemein);
		this.mapValidatorenLehrer.put(-1, allgemein);
		for (const lehrer of gesamt.lehrer) {
			const list: List<Validator> = new ArrayList<Validator>();
			list.add(new ValidatorLsLehrerStammdaten({ get: () => lehrer.nachname }, { get: () => lehrer.vorname }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.geschlecht }, { get: () => lehrer.kuerzel }, { get: () => lehrer.idRechtsverhaeltnis }, this.kontext()));
			list.add(new ValidatorLpLehrerPersonaldaten({ get: () => lehrer.id }, { get: () => gesamt.schule.idSchuljahresabschnitt }, { get: () => lehrer.idStaatsangehoerigkeit }, { get: () => lehrer.idRechtsverhaeltnis }, { get: () => lehrer.pflichtstundensoll }, { get: () => lehrer.anrechnungen }, { get: () => lehrer.idEinsatzstatus }, { get: () => lehrer.idBeschaeftigungsart }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.lehraemter }, { get: () => lehrer.mehrleistung }, { get: () => lehrer.minderleistung }, this.kontext()));
			for (const lehraemter of lehrer.lehraemter) {
				for (const lehrbefaehigungen of lehraemter.lehrbefaehigungen) {
					list.add(new ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung({ get: () => lehrbefaehigungen.idLehrbefaehigung }, { get: () => LehrerLehramt.data().getWertByIDOrNull(lehraemter.idKatalogLehramt) }, this.kontext()));
				}
			}
			this._validatoren.addAll(list);
			this.mapValidatorenLehrer.put(lehrer.id, list);
		}
	}

	/**
	 * Fügt die Subvalidatoren für die Schülerdatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Schülerdatensätzen
	 */
	private addSubvalidatorenSchueler(gesamt: StatistikGesamt): void {
		this.mapValidatorenSchueler.clear();
		const allgemein: List<Validator> = new ArrayList<Validator>();
		allgemein.add(new ValidatorGsGesamtSchuelerdaten({ get: () => gesamt.schueler }, this.kontext()));
		this._validatoren.addAll(allgemein);
		this.mapValidatorenSchueler.put(-1, allgemein);
		for (const schueler of gesamt.schueler) {
			const list: List<Validator> = new ArrayList<Validator>();
			list.add(new ValidatorSsSchuelerStammdaten({ get: () => schueler.geschlecht }, { get: () => schueler.geburtsdatum }, { get: () => schueler.idGeburtsland }, { get: () => schueler.idGeburtslandMutter }, { get: () => schueler.idGeburtslandVater }, { get: () => schueler.hatMigrationshintergrund }, { get: () => schueler.idStaatsangehoerigkeit }, { get: () => schueler.idStaatsangehoerigkeit2 }, this.kontext()));
			for (const lernabschnitt of schueler.lernabschnitte) {
				list.add(new ValidatorSlSchuelerLernabschnittsdaten({ get: () => lernabschnitt.idKlassenart }, { get: () => lernabschnitt.idEpJahre }, this.kontext()));
			}
			this._validatoren.addAll(list);
			this.mapValidatorenSchueler.put(schueler.id, list);
		}
	}

	/**
	 * Fügt die Subvalidatoren für die Klassendatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Klassendatensätzen
	 */
	private addSubvalidatorenKlassen(gesamt: StatistikGesamt): void {
		this.mapValidatorenKlassen.clear();
		const allgemein: List<Validator> = new ArrayList<Validator>();
		allgemein.add(new ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet({ get: () => gesamt.klassen }, this.kontext()));
		this._validatoren.addAll(allgemein);
		this.mapValidatorenKlassen.put(-1, allgemein);
		for (const klasse of gesamt.klassen) {
			const list: List<Validator> = new ArrayList<Validator>();
			list.add(new ValidatorKkKlassenKlassenart({ get: () => null }, this.kontext()));
			list.add(new ValidatorKlKlassenKlassenleitung({ get: () => klasse.klassenLeitungen }, this.kontext()));
			list.add(new ValidatorKoKlassenOrganisationsform({ get: () => null }, { get: () => null }, { get: () => null }, this.kontext()));
			list.add(new ValidatorKsKlassenSchulgliederung({ get: () => null }, this.kontext()));
			this._validatoren.addAll(list);
			this.mapValidatorenKlassen.put(klasse.id, list);
		}
	}

	/**
	 * Fügt die Subvalidatoren für die Kursdatensätze hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit den Kursdatensätzen
	 */
	private addSubvalidatorenKurse(gesamt: StatistikGesamt): void {
		this.mapValidatorenKurse.clear();
		const allgemein: List<Validator> = new ArrayList<Validator>();
		this._validatoren.addAll(allgemein);
		this.mapValidatorenKurse.put(-1, allgemein);
		for (const kurs of gesamt.kurse) {
			const list: List<Validator> = new ArrayList<Validator>();
			list.add(new ValidatorUfUnterrichtsverteilungsdatenFach({ get: () => kurs.idFach }, this.kontext()));
			list.add(new ValidatorUllUnterrichtsverteilungsdatenLehrkraefteLehrkraft({ get: () => kurs.lehrer }, { get: () => gesamt.lehrer }, this.kontext()));
			list.add(new ValidatorUwUnterrichtsverteilungsdatenWochenstunden({ get: () => kurs.wochenstunden as number }, this.kontext()));
			list.add(new ValidatorUzlUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteLehrkraft({ get: () => kurs.weitereLehrer }, { get: () => gesamt.lehrer }, this.kontext()));
			list.add(new ValidatorUzwUnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden({ get: () => kurs.wochenstundenLehrer }, this.kontext()));
			this._validatoren.addAll(list);
			this.mapValidatorenKurse.put(kurs.id, list);
		}
	}

	/**
	 * Fügt die Subvalidatoren für den Katalog der Orte hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit dem Katalog der Orte
	 */
	private addSubvalidatorenKatalogOrte(gesamt: StatistikGesamt): void {
		for (const ort of gesamt.orte) {
			this._validatoren.add(new ValidatorIolIntKatalogOrteLand({ get: () => ort.idLand }, this.kontext()));
			this._validatoren.add(new ValidatorIooIntKatalogOrteOrtsname({ get: () => ort.plz }, { get: () => ort.ortsname }, { get: () => ort.idLand }, this.kontext()));
			this._validatoren.add(new ValidatorIopIntKatalogOrtePlz({ get: () => ort.plz }, { get: () => ort.ortsname }, { get: () => ort.idLand }, this.kontext()));
		}
	}

	/**
	 * Fügt die Subvalidatoren für den Katalog der Religionen hinzu.
	 *
	 * @param gesamt   die Statistikdaten mit dem Katalog der Religionen
	 */
	private addSubvalidatorenKatalogReligion(gesamt: StatistikGesamt): void {
		for (const religion of gesamt.religionen) {
			this._validatoren.add(new ValidatorIkaIntKatalogKonfessionenAsdKatalog({ get: () => religion.idKatalog }, this.kontext()));
		}
	}

	/**
	 * Gibt die Liste der Validatoren für den Schüler mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return die Liste der Validatoren
	 */
	public getBySchuelerID(id: number): List<Validator> {
		const result: List<Validator> | null = this.mapValidatorenSchueler.get(id);
		if (result === null) {
			return new ArrayList();
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
	public getByLehrerID(id: number): List<Validator> {
		const result: List<Validator> | null = this.mapValidatorenLehrer.get(id);
		if (result === null) {
			return new ArrayList();
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
	public getByKlassenID(id: number): List<Validator> {
		const result: List<Validator> | null = this.mapValidatorenKlassen.get(id);
		if (result === null) {
			return new ArrayList();
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
	public getByKursID(id: number): List<Validator> {
		const result: List<Validator> | null = this.mapValidatorenKurse.get(id);
		if (result === null) {
			return new ArrayList();
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
	public getFehlerBySchuelerID(id: number): List<ValidatorFehler> {
		return Validator.getFehlerOfListe(this.getBySchuelerID(id));
	}

	/**
	 * Gibt die Liste der Validator-Fehler für den Lehrer mit der übergebenen ID zurück.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return die Liste der Fehler
	 */
	public getFehlerByLehrerID(id: number): List<ValidatorFehler> {
		return Validator.getFehlerOfListe(this.getByLehrerID(id));
	}

	/**
	 * Gibt die Liste der Validator-Fehler für die Klasse mit der übergebenen ID zurück.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Liste der Fehler
	 */
	public getFehlerByKlassenID(id: number): List<ValidatorFehler> {
		return Validator.getFehlerOfListe(this.getByKlassenID(id));
	}

	/**
	 * Gibt die Liste der Validator-Fehler für die Kurse mit der übergebenen ID zurück.
	 *
	 * @param id   die ID der Kurse
	 *
	 * @return die Liste der Fehler
	 */
	public getFehlerByKursID(id: number): List<ValidatorFehler> {
		return Validator.getFehlerOfListe(this.getByKursID(id));
	}

	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Schüler zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public getFehlerSchueler(): List<ValidatorFehler> {
		return Validator.getFehlerOfListen(this.mapValidatorenSchueler.values());
	}

	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Lehrer zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public getFehlerLehrer(): List<ValidatorFehler> {
		return Validator.getFehlerOfListen(this.mapValidatorenLehrer.values());
	}

	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Klassen zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public getFehlerKlassen(): List<ValidatorFehler> {
		return Validator.getFehlerOfListen(this.mapValidatorenKlassen.values());
	}

	/**
	 * Gibt die Liste aller Validator-Fehler für den Bereich Kurse zurück.
	 *
	 * @return die Liste der Fehler
	 */
	public getFehlerKurse(): List<ValidatorFehler> {
		return Validator.getFehlerOfListen(this.mapValidatorenKurse.values());
	}

	protected pruefe(): boolean {
		this._validatoren.clear();
		this._validatoren.addAll(this.validatoren);
		const gesamt: StatistikGesamt = this.daten.get();
		this.addSubvalidatorenLehrer(gesamt);
		this.addSubvalidatorenSchueler(gesamt);
		this.addSubvalidatorenKlassen(gesamt);
		this.addSubvalidatorenKatalogReligion(gesamt);
		this.addSubvalidatorenKatalogOrte(gesamt);
		this.addSubvalidatorenKurse(gesamt);
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorGesamt', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGesamt>('de.svws_nrw.asd.validate.ValidatorGesamt');

}

export function cast_de_svws_nrw_asd_validate_ValidatorGesamt(obj: unknown): ValidatorGesamt {
	return obj as ValidatorGesamt;
}
