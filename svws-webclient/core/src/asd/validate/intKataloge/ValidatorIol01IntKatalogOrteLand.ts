import { Laender } from '../../../asd/types/schule/Laender';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorIol02IntKatalogOrteLand } from '../../../asd/validate/intKataloge/ValidatorIol02IntKatalogOrteLand';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIol01IntKatalogOrteLand extends Validator {

	private readonly _idKatalog: Supplier<number>;


	/**
	 * @param idKatalog	IdKatalog
	 * @param kontext	Kontext
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
		this._validatoren.add(new ValidatorIol02IntKatalogOrteLand(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		const laender: Laender | null = Laender.data().getWertByIDOrNull(this._idKatalog.get());
		if (laender === null) {
			this.addFehler(0, "Das Feld 'Land' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIol01IntKatalogOrteLand';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIol01IntKatalogOrteLand', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIol01IntKatalogOrteLand>('de.svws_nrw.asd.validate.intKataloge.ValidatorIol01IntKatalogOrteLand');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIol01IntKatalogOrteLand(obj: unknown): ValidatorIol01IntKatalogOrteLand {
	return obj as ValidatorIol01IntKatalogOrteLand;
}
