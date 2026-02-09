import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLp00LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLp00LehrerPersonaldatenLehramt';
import { ValidatorLplk13LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk13LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk11LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk11LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk14LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk14LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk18LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk18LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl11LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl11LehrerPersonaldatenLehramt';
import { ValidatorLplk16LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk16LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk12LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk12LehrerPersonaldatenLehramtKombination';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLplk20LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk20LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl01LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl01LehrerPersonaldatenLehramt';
import { ValidatorLplk10LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk10LehrerPersonaldatenLehramtKombination';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorLplk19LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk19LehrerPersonaldatenLehramtKombination';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLplk15LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk15LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk17LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk17LehrerPersonaldatenLehramtKombination';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLpl10LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl10LehrerPersonaldatenLehramt';

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
		this._validatoren.add(new ValidatorLp00LehrerPersonaldatenLehramt(lehraemter, lehrerId, kontext));
		this._validatoren.add(new ValidatorLpl01LehrerPersonaldatenLehramt(lehraemter, lehrerId, kontext));
		this._validatoren.add(new ValidatorLpl10LehrerPersonaldatenLehramt(lehraemter, kontext));
		this._validatoren.add(new ValidatorLpl11LehrerPersonaldatenLehramt(lehraemter, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLplk10LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk11LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk12LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk13LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk14LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk15LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk16LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk17LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk18LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk19LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		this._validatoren.add(new ValidatorLplk20LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
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
