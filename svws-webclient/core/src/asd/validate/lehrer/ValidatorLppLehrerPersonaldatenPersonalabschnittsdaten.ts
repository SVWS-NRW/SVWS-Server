import { ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell } from '../../../asd/validate/lehrer/ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';

export class ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten        die Daten des Validators, hier die Personalabschnittsdaten des Lehrers
	 * @param stammdaten   die Stammdaten des Lehrers
	 * @param kontext      der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, stammdaten: LehrerStammdaten, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		this._validatoren.add(new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(daten, kontext));
		this._validatoren.add(new ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(daten, kontext));
		try {
			const geburtsdatum: DateManager = DateManager.from(stammdaten.geburtsdatum);
			this._validatoren.add(new ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		} catch(e : any) {
			// empty block
		}
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(obj: unknown): ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten {
	return obj as ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten;
}
