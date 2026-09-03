import { JavaObject } from '../../../java/lang/JavaObject';
import { Laender } from '../../../asd/types/schule/Laender';
import { Orte } from '../../../asd/types/schule/Orte';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorIop02IntKatalogOrtePlz } from '../../../asd/validate/intKataloge/ValidatorIop02IntKatalogOrtePlz';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIop01IntKatalogOrtePlz extends Validator {

	private readonly plz: Supplier<string>;

	private readonly idLand: Supplier<number | null>;


	/**
	 * @param plz        die Postleitzahl
	 * @param idLand     die ID des Landes
	 * @param kontext    Kontext
	 */
	public constructor(plz: Supplier<string>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.idLand = idLand;
		this.plz = plz;
		this._validatoren.add(new ValidatorIop02IntKatalogOrtePlz(plz, idLand, kontext));
	}

	protected pruefe(): boolean {
		const laender: Laender | null = Laender.data().getWertByIDOrNull(this.idLand.get());
		if (JavaObject.equalsTranspiler(Laender.NW, (laender))) {
			let plzString: string | null = this.plz.get();
			for (let ort of Orte.data().getWerte()) {
				for (let orteKatalogEintrag of ort.historie()) {
					if (JavaObject.equalsTranspiler(plzString, (orteKatalogEintrag.plz))) {
						return true;
					}
				}
			}
			this.addFehler(0, "Das Feld 'PLZ' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIop01IntKatalogOrtePlz';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIop01IntKatalogOrtePlz', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIop01IntKatalogOrtePlz>('de.svws_nrw.asd.validate.intKataloge.ValidatorIop01IntKatalogOrtePlz');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIop01IntKatalogOrtePlz(obj: unknown): ValidatorIop01IntKatalogOrtePlz {
	return obj as ValidatorIop01IntKatalogOrtePlz;
}
