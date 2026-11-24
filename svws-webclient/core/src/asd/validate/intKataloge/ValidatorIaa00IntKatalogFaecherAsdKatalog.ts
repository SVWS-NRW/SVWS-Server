import { ValidatorIaa01IntKatalogFaecherAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIaa01IntKatalogFaecherAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIaa00IntKatalogFaecherAsdKatalog extends Validator {

	private readonly _kuerzelStatistik: Supplier<string | null>;


	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public constructor(kuerzelStatistik: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._kuerzelStatistik = kuerzelStatistik;
		this._validatoren.add(new ValidatorIaa01IntKatalogFaecherAsdKatalog(this.getNotNullSupplier(kuerzelStatistik), kontext));
	}

	protected pruefe(): boolean {
		if (null === this._kuerzelStatistik.get() || JavaString.isEmpty(this._kuerzelStatistik.get())) {
			this.addFehler(0, "Fach: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIaa00IntKatalogFaecherAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIaa00IntKatalogFaecherAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIaa00IntKatalogFaecherAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIaa00IntKatalogFaecherAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIaa00IntKatalogFaecherAsdKatalog(obj: unknown): ValidatorIaa00IntKatalogFaecherAsdKatalog {
	return obj as ValidatorIaa00IntKatalogFaecherAsdKatalog;
}
