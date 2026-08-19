import { ValidatorKk02KlassenKlassenart } from '../../../asd/validate/klassen/ValidatorKk02KlassenKlassenart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { Klassenart } from '../../../asd/types/klassen/Klassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKk01KlassenKlassenart extends Validator {

	private readonly _idKlassenart: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart  KlassenartID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
		this._validatoren.add(new ValidatorKk02KlassenKlassenart(idKlassenart, kontext));
	}

	protected pruefe(): boolean {
		const idKlassenart: number | null = this._idKlassenart.get();
		if (Klassenart.data().getSchluesselByIDOrNull(idKlassenart) === null) {
			this.addFehler(0, "Art der Klasse: Das Feld 'Klassenart' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKk01KlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKk01KlassenKlassenart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKk01KlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKk01KlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKk01KlassenKlassenart(obj: unknown): ValidatorKk01KlassenKlassenart {
	return obj as ValidatorKk01KlassenKlassenart;
}
