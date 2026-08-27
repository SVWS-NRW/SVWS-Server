import { ValidatorIol01IntKatalogOrteLand } from '../../../asd/validate/intKataloge/ValidatorIol01IntKatalogOrteLand';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIol00IntKatalogOrteLand extends Validator {

	private readonly _idKatalog: Supplier<number | null>;


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public constructor(idKatalog: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
		this._validatoren.add(new ValidatorIol01IntKatalogOrteLand(this.getNotNullSupplierLong(idKatalog), kontext));
	}

	protected pruefe(): boolean {
		if (null === this._idKatalog.get()) {
			this.addFehler(0, "Das Feld 'Land' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIol00IntKatalogOrteLand';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIol00IntKatalogOrteLand', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIol00IntKatalogOrteLand>('de.svws_nrw.asd.validate.intKataloge.ValidatorIol00IntKatalogOrteLand');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIol00IntKatalogOrteLand(obj: unknown): ValidatorIol00IntKatalogOrteLand {
	return obj as ValidatorIol00IntKatalogOrteLand;
}
