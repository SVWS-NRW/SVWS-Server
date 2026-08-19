import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorKs02KlassenSchulgliederung } from '../../../asd/validate/klassen/ValidatorKs02KlassenSchulgliederung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKs01KlassenSchulgliederung extends Validator {

	private readonly _idSchulgliederung: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idSchulgliederung: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;
		this._validatoren.add(new ValidatorKs02KlassenSchulgliederung(idSchulgliederung, kontext));
	}

	protected pruefe(): boolean {
		const idSchulgliederung: number | null = this._idSchulgliederung.get();
		if (Schulgliederung.data().getSchluesselByIDOrNull(idSchulgliederung) === null) {
			this.addFehler(0, "Schulgliederung der Klasse: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKs01KlassenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKs01KlassenSchulgliederung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKs01KlassenSchulgliederung>('de.svws_nrw.asd.validate.klassen.ValidatorKs01KlassenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKs01KlassenSchulgliederung(obj: unknown): ValidatorKs01KlassenSchulgliederung {
	return obj as ValidatorKs01KlassenSchulgliederung;
}
