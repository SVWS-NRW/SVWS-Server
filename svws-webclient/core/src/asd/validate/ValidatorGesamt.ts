import { ArrayList } from '../../java/util/ArrayList';
import { ValidatorSssSchuleStammdatenSchulform } from '../../asd/validate/schule/ValidatorSssSchuleStammdatenSchulform';
import { ValidatorLpLehrerPersonaldaten } from '../../asd/validate/lehrer/ValidatorLpLehrerPersonaldaten';
import type { List } from '../../java/util/List';
import type { Supplier } from '../../java/util/function/Supplier';
import { Class } from '../../java/lang/Class';
import { StatistikGesamt } from '../../asd/data/statistik/StatistikGesamt';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';
import { ValidatorGlGesamtLehrerdaten } from '../../asd/validate/gesamt/ValidatorGlGesamtLehrerdaten';
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
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<StatistikGesamt>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this.validatoren.add(new ValidatorSssSchuleStammdatenSchulform({ get: () => daten.get().schule.schulform }, kontext));
		this.validatoren.add(new ValidatorGlGesamtLehrerdaten({ get: () => daten.get().lehrer }, kontext));
	}

	protected pruefe(): boolean {
		this._validatoren.clear();
		this._validatoren.addAll(this.validatoren);
		const gesamt: StatistikGesamt = this.daten.get();
		for (const lehrer of gesamt.lehrer) {
			this._validatoren.add(new ValidatorLsLehrerStammdaten({ get: () => lehrer.nachname }, { get: () => lehrer.vorname }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.geschlecht }, { get: () => lehrer.kuerzel }, this.kontext()));
			this._validatoren.add(new ValidatorLpLehrerPersonaldaten({ get: () => lehrer.id }, { get: () => gesamt.schule.idSchuljahresabschnitt }, { get: () => lehrer.rechtsverhaeltnis }, { get: () => lehrer.pflichtstundensoll }, { get: () => lehrer.einsatzstatus }, { get: () => lehrer.beschaeftigungsart }, { get: () => lehrer.geburtsdatum }, { get: () => lehrer.lehraemter }, { get: () => lehrer.mehrleistung }, { get: () => lehrer.minderleistung }, this.kontext()));
		}
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
