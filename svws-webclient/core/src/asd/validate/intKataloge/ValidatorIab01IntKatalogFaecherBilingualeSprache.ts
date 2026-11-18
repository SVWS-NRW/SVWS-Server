import { BilingualeSprache } from '../../../asd/types/fach/BilingualeSprache';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorIab02IntKatalogFaecherBilingualeSprache } from '../../../asd/validate/intKataloge/ValidatorIab02IntKatalogFaecherBilingualeSprache';

export class ValidatorIab01IntKatalogFaecherBilingualeSprache extends Validator {

	private readonly idBilingualeSprache: Supplier<number | null>;


	/**
	 * @param idBilingualeSprache   ID
	 * @param kontext               Kontext
	 */
	public constructor(idBilingualeSprache: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.idBilingualeSprache = idBilingualeSprache;
		this._validatoren.add(new ValidatorIab02IntKatalogFaecherBilingualeSprache(this.getNotNullSupplierLong(idBilingualeSprache), kontext));
	}

	protected pruefe(): boolean {
		const bilingualeSprache: BilingualeSprache | null = BilingualeSprache.data().getWertByIDOrNull(this.idBilingualeSprache.get());
		if (bilingualeSprache === null) {
			this.addFehler(0, "Bilinguale Sachfachsprache des Fachs: Das Feld 'Bilinguale Sachfachsprache' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIab01IntKatalogFaecherBilingualeSprache';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIab01IntKatalogFaecherBilingualeSprache', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIab01IntKatalogFaecherBilingualeSprache>('de.svws_nrw.asd.validate.intKataloge.ValidatorIab01IntKatalogFaecherBilingualeSprache');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIab01IntKatalogFaecherBilingualeSprache(obj: unknown): ValidatorIab01IntKatalogFaecherBilingualeSprache {
	return obj as ValidatorIab01IntKatalogFaecherBilingualeSprache;
}
