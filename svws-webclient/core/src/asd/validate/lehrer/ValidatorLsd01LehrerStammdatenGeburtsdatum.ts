import { DateManager } from '../../../asd/validate/DateManager';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsd01LehrerStammdatenGeburtsdatum extends Validator {

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
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		let geburtsdatum: DateManager | null = null;
		try {
			geburtsdatum = DateManager.from(this.daten.geburtsdatum);
		} catch(e : any) {
			e.printStackTrace();
		}
		const finalGeburtsdatum: DateManager | null = geburtsdatum;
		const schuljahr: number = this.kontext().getSchuljahr();
		success = this.exec(1, { getAsBoolean: () => finalGeburtsdatum === null || !finalGeburtsdatum.istInJahren(schuljahr - 80, schuljahr - 18) }, "Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind die Werte " + (schuljahr - 80) + " bis " + (schuljahr - 18) + ".");
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsd01LehrerStammdatenGeburtsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsd01LehrerStammdatenGeburtsdatum', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLsd01LehrerStammdatenGeburtsdatum>('de.svws_nrw.asd.validate.lehrer.ValidatorLsd01LehrerStammdatenGeburtsdatum');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsd01LehrerStammdatenGeburtsdatum(obj: unknown): ValidatorLsd01LehrerStammdatenGeburtsdatum {
	return obj as ValidatorLsd01LehrerStammdatenGeburtsdatum;
}
