import { Jahrgaenge } from '../../../asd/types/jahrgang/Jahrgaenge';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog extends Validator {

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
		if (!Jahrgaenge.data().isGueltig(this._idKatalog.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Jahrgang ASD-Kürzel: Der eingetragene Wert für das Feld 'Jahrgang ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.intKataloge.ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog(obj: unknown): ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog {
	return obj as ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog;
}
