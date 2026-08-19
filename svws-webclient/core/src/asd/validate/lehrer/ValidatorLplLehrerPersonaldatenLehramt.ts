import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLplkLehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplkLehrerPersonaldatenLehramtKombination';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLpl00LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl00LehrerPersonaldatenLehramt';

export class ValidatorLplLehrerPersonaldatenLehramt extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter            die Lehrämter, die geprüft werden sollen
	 * @param lehrerId              die LehrerId
	 * @param geburtsdatum          das Geburtsdatum des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehraemter: Supplier<List<LehrerLehramtEintrag>>, lehrerId: Supplier<number>, geburtsdatum: Supplier<DateManager | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLpl00LehrerPersonaldatenLehramt(lehraemter, lehrerId, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLplkLehrerPersonaldatenLehramtKombination(lehraemter, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplLehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplLehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplLehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.lehrer.ValidatorLplLehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplLehrerPersonaldatenLehramt(obj: unknown): ValidatorLplLehrerPersonaldatenLehramt {
	return obj as ValidatorLplLehrerPersonaldatenLehramt;
}
