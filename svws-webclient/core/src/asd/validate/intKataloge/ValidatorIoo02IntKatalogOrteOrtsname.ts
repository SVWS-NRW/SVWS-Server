import { JavaObject } from '../../../java/lang/JavaObject';
import { Laender } from '../../../asd/types/schule/Laender';
import { Orte } from '../../../asd/types/schule/Orte';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIoo02IntKatalogOrteOrtsname extends Validator {

	private readonly ortsname: Supplier<string>;

	private readonly idLand: Supplier<number | null>;


	/**
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	Kontext
	 */
	public constructor(ortsname: Supplier<string>, idLand: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.ortsname = ortsname;
		this.idLand = idLand;
	}

	protected pruefe(): boolean {
		const laender: Laender | null = Laender.data().getWertByIDOrNull(this.idLand.get());
		if (JavaObject.equalsTranspiler(Laender.NW, (laender))) {
			let ortsnameString: string | null = this.ortsname.get();
			for (let orteKatalogEintrag of Orte.data().getEintraegeBySchuljahr(this.kontext().getSchuljahr())) {
				if (JavaObject.equalsTranspiler(ortsnameString, (orteKatalogEintrag.ort))) {
					return true;
				}
			}
			this.addFehler(0, "Der eingetragene Wert für das Feld 'Ortsname' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIoo02IntKatalogOrteOrtsname';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIoo02IntKatalogOrteOrtsname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIoo02IntKatalogOrteOrtsname>('de.svws_nrw.asd.validate.intKataloge.ValidatorIoo02IntKatalogOrteOrtsname');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIoo02IntKatalogOrteOrtsname(obj: unknown): ValidatorIoo02IntKatalogOrteOrtsname {
	return obj as ValidatorIoo02IntKatalogOrteOrtsname;
}
