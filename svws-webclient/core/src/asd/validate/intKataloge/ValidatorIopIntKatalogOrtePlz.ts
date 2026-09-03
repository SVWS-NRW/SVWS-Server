import { ValidatorIop00IntKatalogOrtePlz } from '../../../asd/validate/intKataloge/ValidatorIop00IntKatalogOrtePlz';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIopIntKatalogOrtePlz extends Validator {


	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext     Kontext
	 */
	public constructor(plz: Supplier<string | null>, ortsname: Supplier<string | null>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorIop00IntKatalogOrtePlz(plz, ortsname, idLand, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIopIntKatalogOrtePlz';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIopIntKatalogOrtePlz', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIopIntKatalogOrtePlz>('de.svws_nrw.asd.validate.intKataloge.ValidatorIopIntKatalogOrtePlz');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIopIntKatalogOrtePlz(obj: unknown): ValidatorIopIntKatalogOrtePlz {
	return obj as ValidatorIopIntKatalogOrtePlz;
}
