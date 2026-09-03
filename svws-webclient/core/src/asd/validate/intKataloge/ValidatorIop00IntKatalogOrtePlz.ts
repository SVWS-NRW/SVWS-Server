import { ValidatorIoo10IntKatalogOrteOrtsname } from '../../../asd/validate/intKataloge/ValidatorIoo10IntKatalogOrteOrtsname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorIop10IntKatalogOrtePlz } from '../../../asd/validate/intKataloge/ValidatorIop10IntKatalogOrtePlz';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorIop01IntKatalogOrtePlz } from '../../../asd/validate/intKataloge/ValidatorIop01IntKatalogOrtePlz';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIop00IntKatalogOrtePlz extends Validator {

	private readonly plz: Supplier<string | null>;


	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public constructor(plz: Supplier<string | null>, ortsname: Supplier<string | null>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.plz = plz;
		this._validatoren.add(new ValidatorIop01IntKatalogOrtePlz(this.getNotNullSupplier(plz), idLand, kontext));
		this._validatoren.add(new ValidatorIop10IntKatalogOrtePlz(this.getNotNullSupplier(plz), idLand, kontext));
		this._validatoren.add(new ValidatorIoo10IntKatalogOrteOrtsname(plz, ortsname, idLand, kontext));
	}

	protected pruefe(): boolean {
		if (null === this.plz.get() || JavaString.isEmpty(this.plz.get())) {
			this.addFehler(0, "Das Feld 'PLZ' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIop00IntKatalogOrtePlz';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIop00IntKatalogOrtePlz', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIop00IntKatalogOrtePlz>('de.svws_nrw.asd.validate.intKataloge.ValidatorIop00IntKatalogOrtePlz');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIop00IntKatalogOrtePlz(obj: unknown): ValidatorIop00IntKatalogOrtePlz {
	return obj as ValidatorIop00IntKatalogOrtePlz;
}
