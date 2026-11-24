import { ValidatorIaa00IntKatalogFaecherAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIaa00IntKatalogFaecherAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIaaIntKatalogFaecherAsdKatalog extends Validator {


	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public constructor(kuerzelStatistik: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorIaa00IntKatalogFaecherAsdKatalog(kuerzelStatistik, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIaaIntKatalogFaecherAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIaaIntKatalogFaecherAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIaaIntKatalogFaecherAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIaaIntKatalogFaecherAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIaaIntKatalogFaecherAsdKatalog(obj: unknown): ValidatorIaaIntKatalogFaecherAsdKatalog {
	return obj as ValidatorIaaIntKatalogFaecherAsdKatalog;
}
