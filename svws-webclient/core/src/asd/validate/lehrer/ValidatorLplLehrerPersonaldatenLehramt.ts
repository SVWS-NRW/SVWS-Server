import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLpl01LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl01LehrerPersonaldatenLehramt';
import { ValidatorLpl03LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl03LehrerPersonaldatenLehramt';
import { Class } from '../../../java/lang/Class';
import { ValidatorLpl02LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl02LehrerPersonaldatenLehramt';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLpl00LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl00LehrerPersonaldatenLehramt';

export class ValidatorLplLehrerPersonaldatenLehramt extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly lehrerPersonaldaten: LehrerPersonaldaten;

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly geburtsdatum: DateManager;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param geburtsdatum          das Geburtsdatum des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehrerPersonaldaten: LehrerPersonaldaten, geburtsdatum: DateManager, kontext: ValidatorKontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
		this.geburtsdatum = geburtsdatum;
		this._validatoren.add(new ValidatorLpl00LehrerPersonaldatenLehramt(lehrerPersonaldaten, kontext));
		this._validatoren.add(new ValidatorLpl01LehrerPersonaldatenLehramt(lehrerPersonaldaten, kontext));
		this._validatoren.add(new ValidatorLpl02LehrerPersonaldatenLehramt(lehrerPersonaldaten, kontext));
		this._validatoren.add(new ValidatorLpl03LehrerPersonaldatenLehramt(lehrerPersonaldaten, geburtsdatum, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplLehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplLehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLplLehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.lehrer.ValidatorLplLehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplLehrerPersonaldatenLehramt(obj: unknown): ValidatorLplLehrerPersonaldatenLehramt {
	return obj as ValidatorLplLehrerPersonaldatenLehramt;
}
