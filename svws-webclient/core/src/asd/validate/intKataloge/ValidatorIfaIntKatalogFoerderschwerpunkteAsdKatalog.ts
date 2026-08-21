import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog extends Validator {


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public constructor(idKatalog: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog(obj: unknown): ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog {
	return obj as ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog;
}
