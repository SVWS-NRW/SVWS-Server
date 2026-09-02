import { ValidatorIoo10IntKatalogOrteOrtsname } from '../../../asd/validate/intKataloge/ValidatorIoo10IntKatalogOrteOrtsname';
import { ValidatorIoo01IntKatalogOrteOrtsname } from '../../../asd/validate/intKataloge/ValidatorIoo01IntKatalogOrteOrtsname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIoo00IntKatalogOrteOrtsname extends Validator {

	private readonly ortsname: Supplier<string | null>;


	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public constructor(plz: Supplier<string | null>, ortsname: Supplier<string | null>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.ortsname = ortsname;
		this._validatoren.add(new ValidatorIoo01IntKatalogOrteOrtsname(this.getNotNullSupplier(ortsname), idLand, kontext));
		this._validatoren.add(new ValidatorIoo10IntKatalogOrteOrtsname(plz, ortsname, idLand, kontext));
	}

	protected pruefe(): boolean {
		if (null === this.ortsname.get() || JavaString.isEmpty(this.ortsname.get())) {
			this.addFehler(0, "Das Feld 'Ortsname' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIoo00IntKatalogOrteOrtsname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIoo00IntKatalogOrteOrtsname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIoo00IntKatalogOrteOrtsname>('de.svws_nrw.asd.validate.intKataloge.ValidatorIoo00IntKatalogOrteOrtsname');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIoo00IntKatalogOrteOrtsname(obj: unknown): ValidatorIoo00IntKatalogOrteOrtsname {
	return obj as ValidatorIoo00IntKatalogOrteOrtsname;
}
