import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLsd01LehrerStammdatenGeburtsdatum } from '../../../asd/validate/lehrer/ValidatorLsd01LehrerStammdatenGeburtsdatum';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsd00LehrerStammdatenGeburtsdatum extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: LehrerStammdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerStammdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsd01LehrerStammdatenGeburtsdatum(daten, kontext));
	}

	protected pruefe(): boolean {
		let geburtsdatum: DateManager | null = null;
		let errorMsg: string = "";
		try {
			geburtsdatum = DateManager.from(this.daten.geburtsdatum);
		} catch(e : any) {
			errorMsg = e.getMessage();
		}
		const finalGeburtsdatum: DateManager | null = geburtsdatum;
		if (finalGeburtsdatum === null) {
			this.addFehler(0, "Das Geburtsdatum ist ungültig: " + errorMsg);
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
