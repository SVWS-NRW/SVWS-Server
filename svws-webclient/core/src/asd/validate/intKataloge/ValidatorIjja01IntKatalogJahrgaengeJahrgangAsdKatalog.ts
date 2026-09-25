import { Jahrgaenge } from '../../../asd/types/jahrgang/Jahrgaenge';
import { ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog extends Validator {

	private readonly _idKatalog: Supplier<number>;


	/**
	 * @param idKatalog	IdKatalog
	 * @param kontext	Kontext
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
		this._validatoren.add(new ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		const jahrgang: Jahrgaenge | null = Jahrgaenge.data().getWertByIDOrNull(this._idKatalog.get());
		if (jahrgang === null) {
			this.addFehler(0, "Jahrgang ASD-Kürzel: Das Feld 'Jahrgang ASD-Kürzel' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog(obj: unknown): ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog {
	return obj as ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog;
}
