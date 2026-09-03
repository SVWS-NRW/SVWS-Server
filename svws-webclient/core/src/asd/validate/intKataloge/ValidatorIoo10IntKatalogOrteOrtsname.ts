import { JavaObject } from '../../../java/lang/JavaObject';
import { Laender } from '../../../asd/types/schule/Laender';
import { Orte } from '../../../asd/types/schule/Orte';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIoo10IntKatalogOrteOrtsname extends Validator {

	private readonly plz: Supplier<string | null>;

	private readonly ortsname: Supplier<string | null>;

	private readonly idLand: Supplier<number | null>;


	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public constructor(plz: Supplier<string | null>, ortsname: Supplier<string | null>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.plz = plz;
		this.ortsname = ortsname;
		this.idLand = idLand;
	}

	protected pruefe(): boolean {
		const laender: Laender | null = Laender.data().getWertByIDOrNull(this.idLand.get());
		if (JavaObject.equalsTranspiler(Laender.NW, (laender))) {
			let ortsnameString: string | null = this.ortsname.get();
			let plzString: string | null = this.plz.get();
			for (let ort of Orte.data().getWerte()) {
				for (let orteKatalogEintrag of ort.historie()) {
					if (JavaObject.equalsTranspiler(orteKatalogEintrag.ort, (ortsnameString)) && JavaObject.equalsTranspiler(orteKatalogEintrag.plz, (plzString))) {
						return true;
					}
				}
			}
			this.addFehler(0, "Da der eigetragene Ort in Nordrhein-Westfalen liegt, muss die dazugehörige Postleitzahl auch in Nordrhein-Westfalen liegen. Bitte prüfen!");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIoo10IntKatalogOrteOrtsname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIoo10IntKatalogOrteOrtsname', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIoo10IntKatalogOrteOrtsname>('de.svws_nrw.asd.validate.intKataloge.ValidatorIoo10IntKatalogOrteOrtsname');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIoo10IntKatalogOrteOrtsname(obj: unknown): ValidatorIoo10IntKatalogOrteOrtsname {
	return obj as ValidatorIoo10IntKatalogOrteOrtsname;
}
