import { ValidatorIab01IntKatalogFaecherBilingualeSprache } from '../../../asd/validate/intKataloge/ValidatorIab01IntKatalogFaecherBilingualeSprache';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIabIntKatalogFaecherBilingualeSprache extends Validator {


	/**
	 * @param idBilingualeSprache   ID
	 * @param kontext	            Kontext
	 */
	public constructor(idBilingualeSprache: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorIab01IntKatalogFaecherBilingualeSprache(idBilingualeSprache, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIabIntKatalogFaecherBilingualeSprache';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIabIntKatalogFaecherBilingualeSprache', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIabIntKatalogFaecherBilingualeSprache>('de.svws_nrw.asd.validate.intKataloge.ValidatorIabIntKatalogFaecherBilingualeSprache');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIabIntKatalogFaecherBilingualeSprache(obj: unknown): ValidatorIabIntKatalogFaecherBilingualeSprache {
	return obj as ValidatorIabIntKatalogFaecherBilingualeSprache;
}
