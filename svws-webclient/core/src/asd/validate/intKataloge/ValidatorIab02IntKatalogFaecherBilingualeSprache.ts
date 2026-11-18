import { BilingualeSprache } from '../../../asd/types/fach/BilingualeSprache';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIab02IntKatalogFaecherBilingualeSprache extends Validator {

	private readonly idBilingualeSprache: Supplier<number>;


	/**
	 * @param idBilingualeSprache   ID
	 * @param kontext	            Kontext
	 */
	public constructor(idBilingualeSprache: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this.idBilingualeSprache = idBilingualeSprache;
	}

	protected pruefe(): boolean {
		if (!BilingualeSprache.data().isGueltig(this.idBilingualeSprache.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Bilinguale Sachfachsprache des Fachs: Der eingetragene Wert für das Feld 'Bilinguale Sachfachsprache' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIab02IntKatalogFaecherBilingualeSprache';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIab02IntKatalogFaecherBilingualeSprache', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIab02IntKatalogFaecherBilingualeSprache>('de.svws_nrw.asd.validate.intKataloge.ValidatorIab02IntKatalogFaecherBilingualeSprache');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIab02IntKatalogFaecherBilingualeSprache(obj: unknown): ValidatorIab02IntKatalogFaecherBilingualeSprache {
	return obj as ValidatorIab02IntKatalogFaecherBilingualeSprache;
}
