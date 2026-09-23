import { ValidatorKll10KlassenKlassenleitungslisteLehrkraft } from '../../../asd/validate/klassen/ValidatorKll10KlassenKlassenleitungslisteLehrkraft';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKllKlassenKlassenleitungslisteLehrkraft extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenLeitungen   Klassenleitungen
	 * @param listLehrer         Liste der Lehrer
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(klassenLeitungen: Supplier<List<number>>, listLehrer: Supplier<List<number>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKll10KlassenKlassenleitungslisteLehrkraft(klassenLeitungen, listLehrer, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKllKlassenKlassenleitungslisteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKllKlassenKlassenleitungslisteLehrkraft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKllKlassenKlassenleitungslisteLehrkraft>('de.svws_nrw.asd.validate.klassen.ValidatorKllKlassenKlassenleitungslisteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKllKlassenKlassenleitungslisteLehrkraft(obj: unknown): ValidatorKllKlassenKlassenleitungslisteLehrkraft {
	return obj as ValidatorKllKlassenKlassenleitungslisteLehrkraft;
}
