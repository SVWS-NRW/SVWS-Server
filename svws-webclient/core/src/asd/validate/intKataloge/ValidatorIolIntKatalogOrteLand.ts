import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorIol00IntKatalogOrteLand } from '../../../asd/validate/intKataloge/ValidatorIol00IntKatalogOrteLand';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIolIntKatalogOrteLand extends Validator {


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public constructor(idKatalog: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorIol00IntKatalogOrteLand(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIolIntKatalogOrteLand';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIolIntKatalogOrteLand', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIolIntKatalogOrteLand>('de.svws_nrw.asd.validate.intKataloge.ValidatorIolIntKatalogOrteLand');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIolIntKatalogOrteLand(obj: unknown): ValidatorIolIntKatalogOrteLand {
	return obj as ValidatorIolIntKatalogOrteLand;
}
