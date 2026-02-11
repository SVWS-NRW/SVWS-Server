import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { ValidatorLplk06LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk06LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk03LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk03LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk01LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk01LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk04LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk04LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl02LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl02LehrerPersonaldatenLehramt';
import { ValidatorLplk08LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk08LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl00LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl00LehrerPersonaldatenLehramt';
import { DateManager } from '../../../asd/validate/DateManager';
import { ValidatorLplk02LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk02LehrerPersonaldatenLehramtKombination';
import { ValidatorLpl01LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl01LehrerPersonaldatenLehramt';
import { ValidatorLpl03LehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLpl03LehrerPersonaldatenLehramt';
import { ValidatorLplk10LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk10LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk00LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk00LehrerPersonaldatenLehramtKombination';
import { ValidatorLplk09LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk09LehrerPersonaldatenLehramtKombination';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLplk05LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk05LehrerPersonaldatenLehramtKombination';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLplk07LehrerPersonaldatenLehramtKombination } from '../../../asd/validate/lehrer/ValidatorLplk07LehrerPersonaldatenLehramtKombination';

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
		if (this.lehrerPersonaldaten.lehraemter !== null && this.lehrerPersonaldaten.lehraemter.size() > 0) {
			this._validatoren.add(new ValidatorLplk00LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk01LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk02LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk03LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk04LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk05LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk06LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk07LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk08LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk09LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
			this._validatoren.add(new ValidatorLplk10LehrerPersonaldatenLehramtKombination(lehrerPersonaldaten, kontext));
		}
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

	public static readonly class = new Class<ValidatorLplLehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.lehrer.ValidatorLplLehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplLehrerPersonaldatenLehramt(obj: unknown): ValidatorLplLehrerPersonaldatenLehramt {
	return obj as ValidatorLplLehrerPersonaldatenLehramt;
}
