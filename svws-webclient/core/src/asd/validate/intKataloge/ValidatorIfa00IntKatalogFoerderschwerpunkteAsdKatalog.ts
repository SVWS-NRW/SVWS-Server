import { ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog extends Validator {

	private readonly _idKatalog: Supplier<number | null>;


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public constructor(idKatalog: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
		this._validatoren.add(new ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog(this.getNotNullSupplierLong(idKatalog), kontext));
	}

	protected pruefe(): boolean {
		if (null === this._idKatalog.get()) {
			this.addFehler(0, "Förderschwerpunkt des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog(obj: unknown): ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog {
	return obj as ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog;
}
