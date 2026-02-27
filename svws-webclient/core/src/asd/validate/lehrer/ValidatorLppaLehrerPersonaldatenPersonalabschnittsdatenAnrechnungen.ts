import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import { ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {


	/**
	 * Erstellt einen neuen Sammel-Validator für Anrechnungsdaten.
	 *
	 * @param anrechnungen       die Liste der Anrechnungsstunden
	 * @param lehraemter         die Liste der Lehrämter
	 * @param pflichtstundensoll das Pflichtstundensoll
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, kontext));
		this._validatoren.add(new ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, kontext));
		this._validatoren.add(new ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, kontext));
		this._validatoren.add(new ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, kontext));
		this._validatoren.add(new ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppaLehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
