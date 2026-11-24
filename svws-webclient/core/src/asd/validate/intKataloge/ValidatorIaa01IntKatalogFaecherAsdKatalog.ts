import { Fach } from '../../../asd/types/fach/Fach';
import { ValidatorIaa02IntKatalogFaecherAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIaa02IntKatalogFaecherAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIaa01IntKatalogFaecherAsdKatalog extends Validator {

	private readonly _kuerzelStatistik: Supplier<string>;


	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public constructor(kuerzelStatistik: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this._kuerzelStatistik = kuerzelStatistik;
		this._validatoren.add(new ValidatorIaa02IntKatalogFaecherAsdKatalog(kuerzelStatistik, kontext));
	}

	protected pruefe(): boolean {
		const fach: Fach | null = Fach.getBySchluesselOrDefault(this._kuerzelStatistik.get());
		if (Fach.VF as unknown === fach as unknown) {
			this.addFehler(0, "Fach: Das Feld 'Fach ASD-Kürzel' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIaa01IntKatalogFaecherAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIaa01IntKatalogFaecherAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIaa01IntKatalogFaecherAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIaa01IntKatalogFaecherAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIaa01IntKatalogFaecherAsdKatalog(obj: unknown): ValidatorIaa01IntKatalogFaecherAsdKatalog {
	return obj as ValidatorIaa01IntKatalogFaecherAsdKatalog;
}
