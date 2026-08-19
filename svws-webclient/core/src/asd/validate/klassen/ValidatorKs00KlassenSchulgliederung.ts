import { ValidatorKs01KlassenSchulgliederung } from '../../../asd/validate/klassen/ValidatorKs01KlassenSchulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKs00KlassenSchulgliederung extends Validator {

	private readonly _idSchulgliederung: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idSchulgliederung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;
		this._validatoren.add(new ValidatorKs01KlassenSchulgliederung(this.getNotNullSupplierLong(idSchulgliederung), kontext));
	}

	protected pruefe(): boolean {
		const idSchulgliederung: number | null = this._idSchulgliederung.get();
		if (idSchulgliederung === null) {
			this.addFehler(0, "Schulgliederung der Klasse: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKs00KlassenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKs00KlassenSchulgliederung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKs00KlassenSchulgliederung>('de.svws_nrw.asd.validate.klassen.ValidatorKs00KlassenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKs00KlassenSchulgliederung(obj: unknown): ValidatorKs00KlassenSchulgliederung {
	return obj as ValidatorKs00KlassenSchulgliederung;
}
