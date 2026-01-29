import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLplk06LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk06LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk03LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk03LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk01LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk01LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk04LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk04LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl02LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl02LehrerPersonaldatenLehramt';
import { ValidatorLplk08LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk08LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl00LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl00LehrerPersonaldatenLehramt';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLplk02LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk02LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl01LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl01LehrerPersonaldatenLehramt';
import { ValidatorLpl03LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl03LehrerPersonaldatenLehramt';
import { ValidatorLplk10LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk10LehrerPersonaldatenLehramtKombination';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorLplk00LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk00LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk09LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk09LehrerPersonaldatenLehramtKombination';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLplk05LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk05LehrerPersonaldatenLehramtKombination';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLplk07LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk07LehrerPersonaldatenLehramtKombination';

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
		this._validatoren.add(new ValidatorLpl00LehrerPersonaldatenLehramt(lehraemter, lehrerId, kontext));
		this._validatoren.add(new ValidatorLpl01LehrerPersonaldatenLehramt(lehraemter, lehrerId, kontext));
		this._validatoren.add(new ValidatorLpl02LehrerPersonaldatenLehramt(lehraemter, kontext));
		this._validatoren.add(new ValidatorLpl03LehrerPersonaldatenLehramt(lehraemter, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLplk00LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk01LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk02LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk03LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk04LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk05LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk06LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk07LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk08LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk09LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk10LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
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
