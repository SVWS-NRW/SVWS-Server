import { JavaObject } from '../../../java/lang/JavaObject';
import { Laender } from '../../../asd/types/schule/Laender';
import { Orte } from '../../../asd/types/schule/Orte';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIop10IntKatalogOrtePlz extends Validator {

	private readonly plz: Supplier<string>;

	private readonly idLand: Supplier<number | null>;


	/**
	 * @param plz         die Postleitzahl
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public constructor(plz: Supplier<string>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.plz = plz;
		this.idLand = idLand;
	}

	protected pruefe(): boolean {
		const laender: Laender | null = Laender.data().getWertByIDOrNull(this.idLand.get());
		if (!JavaObject.equalsTranspiler(Laender.NW, (laender))) {
			let plzString: string | null = this.plz.get();
			for (let ort of Orte.data().getWerte()) {
				for (let orteKatalogEintrag of ort.historie()) {
					if (JavaObject.equalsTranspiler(orteKatalogEintrag.plz, (plzString))) {
						this.addFehler(0, "Für Orte, die nicht in Nordrhein-Westfalen liegen, darf keine in Nordrhein-Westfalen liegende Postleitzahl verwendet werden. Bitte prüfen!");
						return false;
					}
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIop10IntKatalogOrtePlz';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIop10IntKatalogOrtePlz', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIop10IntKatalogOrtePlz>('de.svws_nrw.asd.validate.intKataloge.ValidatorIop10IntKatalogOrtePlz');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIop10IntKatalogOrtePlz(obj: unknown): ValidatorIop10IntKatalogOrtePlz {
	return obj as ValidatorIop10IntKatalogOrtePlz;
}
