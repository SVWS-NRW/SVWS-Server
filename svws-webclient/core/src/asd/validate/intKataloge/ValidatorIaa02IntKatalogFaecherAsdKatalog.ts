import { Fach } from '../../../asd/types/fach/Fach';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { FachKatalogEintrag } from '../../../asd/data/fach/FachKatalogEintrag';

export class ValidatorIaa02IntKatalogFaecherAsdKatalog extends Validator {

	private readonly _kuerzelStatistik: Supplier<string>;


	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public constructor(kuerzelStatistik: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this._kuerzelStatistik = kuerzelStatistik;
	}

	protected pruefe(): boolean {
		const fachKatalogEintrag: FachKatalogEintrag | null = Fach.data().getEintragBySchuljahrUndSchluessel(this.kontext().getSchuljahr(), this._kuerzelStatistik.get());
		if ((fachKatalogEintrag === null)) {
			this.addFehler(0, "Fach: Der eingetragene Wert für das Feld 'Fach ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIaa02IntKatalogFaecherAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIaa02IntKatalogFaecherAsdKatalog', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIaa02IntKatalogFaecherAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIaa02IntKatalogFaecherAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIaa02IntKatalogFaecherAsdKatalog(obj: unknown): ValidatorIaa02IntKatalogFaecherAsdKatalog {
	return obj as ValidatorIaa02IntKatalogFaecherAsdKatalog;
}
