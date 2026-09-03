import { JavaObject } from '../../../java/lang/JavaObject';
import { Laender } from '../../../asd/types/schule/Laender';
import { Orte } from '../../../asd/types/schule/Orte';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIop02IntKatalogOrtePlz extends Validator {

	private readonly plz: Supplier<string>;

	private readonly idLand: Supplier<number | null>;


	/**
	 * @param plz        die Postleitzahl
	 * @param idLand     die ID des Landes
	 * @param kontext    Kontext
	 */
	public constructor(plz: Supplier<string>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.plz = plz;
		this.idLand = idLand;
	}

	protected pruefe(): boolean {
		const laender: Laender | null = Laender.data().getWertByIDOrNull(this.idLand.get());
		if (JavaObject.equalsTranspiler(Laender.NW, (laender))) {
			let plzString: string | null = this.plz.get();
			for (let orteKatalogEintrag of Orte.data().getEintraegeBySchuljahr(this.kontext().getSchuljahr())) {
				if (JavaObject.equalsTranspiler(plzString, (orteKatalogEintrag.plz))) {
					return true;
				}
			}
			this.addFehler(0, "Der eingetragene Wert für das Feld 'PLZ' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIop02IntKatalogOrtePlz';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIop02IntKatalogOrtePlz', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIop02IntKatalogOrtePlz>('de.svws_nrw.asd.validate.intKataloge.ValidatorIop02IntKatalogOrtePlz');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIop02IntKatalogOrtePlz(obj: unknown): ValidatorIop02IntKatalogOrtePlz {
	return obj as ValidatorIop02IntKatalogOrtePlz;
}
