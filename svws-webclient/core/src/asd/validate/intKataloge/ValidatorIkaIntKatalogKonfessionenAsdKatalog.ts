import { ValidatorIka00IntKatalogKonfessionenAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIka00IntKatalogKonfessionenAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIkaIntKatalogKonfessionenAsdKatalog extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idKatalog   die Katalog-ID
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorIka00IntKatalogKonfessionenAsdKatalog(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIkaIntKatalogKonfessionenAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIkaIntKatalogKonfessionenAsdKatalog', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIkaIntKatalogKonfessionenAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIkaIntKatalogKonfessionenAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIkaIntKatalogKonfessionenAsdKatalog(obj: unknown): ValidatorIkaIntKatalogKonfessionenAsdKatalog {
	return obj as ValidatorIkaIntKatalogKonfessionenAsdKatalog;
}
