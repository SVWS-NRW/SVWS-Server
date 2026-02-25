import { ValidatorLsd01LehrerStammdatenGeburtsdatum } from '../../../asd/validate/lehrer/ValidatorLsd01LehrerStammdatenGeburtsdatum';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsd00LehrerStammdatenGeburtsdatum extends Validator {

	/**
	 * Das Geburtsdatumm des Lehrers
	 */
	private readonly daten: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geburtsdatumm des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsd01LehrerStammdatenGeburtsdatum(this.getNotNullSupplier(daten), kontext));
	}

	protected pruefe(): boolean {
		const geburtsdatum: string | null = this.daten.get();
		if ((geburtsdatum === null) || (JavaString.isEmpty(geburtsdatum))) {
			this.addFehler(0, "Das Feld 'Geburtsdatum' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsd00LehrerStammdatenGeburtsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsd00LehrerStammdatenGeburtsdatum', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsd00LehrerStammdatenGeburtsdatum>('de.svws_nrw.asd.validate.lehrer.ValidatorLsd00LehrerStammdatenGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsd00LehrerStammdatenGeburtsdatum(obj: unknown): ValidatorLsd00LehrerStammdatenGeburtsdatum {
	return obj as ValidatorLsd00LehrerStammdatenGeburtsdatum;
}
