import { ValidatorKk01KlassenKlassenart } from '../../../asd/validate/klassen/ValidatorKk01KlassenKlassenart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKk00KlassenKlassenart extends Validator {

	private readonly _idKlassenart: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart  KlassenartID
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
		this._validatoren.add(new ValidatorKk01KlassenKlassenart(this.getNotNullSupplierLong(idKlassenart), kontext));
	}

	protected pruefe(): boolean {
		const idKlassenart: number | null = this._idKlassenart.get();
		if (idKlassenart === null) {
			this.addFehler(0, "Art der Klasse: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKk00KlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKk00KlassenKlassenart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKk00KlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKk00KlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKk00KlassenKlassenart(obj: unknown): ValidatorKk00KlassenKlassenart {
	return obj as ValidatorKk00KlassenKlassenart;
}
