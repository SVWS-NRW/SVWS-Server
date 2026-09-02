import { JavaObject } from '../../../java/lang/JavaObject';
import { Laender } from '../../../asd/types/schule/Laender';
import { Orte } from '../../../asd/types/schule/Orte';
import { ValidatorIoo02IntKatalogOrteOrtsname } from '../../../asd/validate/intKataloge/ValidatorIoo02IntKatalogOrteOrtsname';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIoo01IntKatalogOrteOrtsname extends Validator {

	private readonly ortsname: Supplier<string>;

	private readonly idLand: Supplier<number | null>;


	/**
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public constructor(ortsname: Supplier<string>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.idLand = idLand;
		this.ortsname = ortsname;
		this._validatoren.add(new ValidatorIoo02IntKatalogOrteOrtsname(ortsname, idLand, kontext));
	}

	protected pruefe(): boolean {
		const laender: Laender | null = Laender.data().getWertByIDOrNull(this.idLand.get());
		if (JavaObject.equalsTranspiler(Laender.NW, (laender))) {
			let ortsnameString: string | null = this.ortsname.get();
			for (let ort of Orte.data().getWerte()) {
				for (let orteKatalogEintrag of ort.historie()) {
					if (JavaObject.equalsTranspiler(ortsnameString, (orteKatalogEintrag.ort))) {
						return true;
					}
				}
			}
			this.addFehler(0, "Das Feld 'Ortsname' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIoo01IntKatalogOrteOrtsname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIoo01IntKatalogOrteOrtsname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIoo01IntKatalogOrteOrtsname>('de.svws_nrw.asd.validate.intKataloge.ValidatorIoo01IntKatalogOrteOrtsname');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIoo01IntKatalogOrteOrtsname(obj: unknown): ValidatorIoo01IntKatalogOrteOrtsname {
	return obj as ValidatorIoo01IntKatalogOrteOrtsname;
}
