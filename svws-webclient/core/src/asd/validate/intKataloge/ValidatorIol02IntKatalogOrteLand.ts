import { Laender } from '../../../asd/types/schule/Laender';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIol02IntKatalogOrteLand extends Validator {

	private readonly _idKatalog: Supplier<number>;


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
	}

	protected pruefe(): boolean {
		if (!Laender.data().isGueltig(this._idKatalog.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Der eingetragene Wert für das Feld 'Land' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIol02IntKatalogOrteLand';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIol02IntKatalogOrteLand', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIol02IntKatalogOrteLand>('de.svws_nrw.asd.validate.intKataloge.ValidatorIol02IntKatalogOrteLand');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIol02IntKatalogOrteLand(obj: unknown): ValidatorIol02IntKatalogOrteLand {
	return obj as ValidatorIol02IntKatalogOrteLand;
}
