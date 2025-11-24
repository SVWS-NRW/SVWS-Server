import { ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Die Lehrer-Personalabschnittdaten
	 */
	private readonly daten: LehrerPersonalabschnittsdaten;

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly geburtsdatum: DateManager;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten          die Personalabschnittsdaten für den Validator
	 * @param geburtsdatum   das Geburtsdatum des Lehrers
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, geburtsdatum: DateManager, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this.geburtsdatum = geburtsdatum;
		this._validatoren.add(new ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
	}

	protected pruefe(): boolean {
		const rv: LehrerRechtsverhaeltnis | null = LehrerRechtsverhaeltnis.getBySchluessel(this.daten.rechtsverhaeltnis);
		if (rv === null) {
			this.addFehler(0, "Kein Wert im Feld 'rechtsverhaeltnis'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
