import { ValidatorIjja00IntKatalogJahrgaengeJahrgangAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIjja00IntKatalogJahrgaengeJahrgangAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog extends Validator {


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public constructor(idKatalog: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorIjja00IntKatalogJahrgaengeJahrgangAsdKatalog(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog(obj: unknown): ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog {
	return obj as ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog;
}
