import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKll10KlassenKlassenleitungslisteLehrkraft extends Validator {

	private readonly _klassenLeitungen: Supplier<List<number | null>>;

	/**
	 * Die Liste der Lehrer.
	 */
	private readonly _listLehrer: Supplier<List<number>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenLeitungen   Klassenleitungen
	 * @param listLehrer         die Liste der Lehrer
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(klassenLeitungen: Supplier<List<number | null>>, listLehrer: Supplier<List<number>>, kontext: ValidatorKontext) {
		super(kontext);
		this._klassenLeitungen = klassenLeitungen;
		this._listLehrer = listLehrer;
	}

	protected pruefe(): boolean {
		const klassenLeitungen: List<number | null> = this._klassenLeitungen.get();
		const listLehrer: List<number> = this._listLehrer.get();
		for (const klassenleitung of klassenLeitungen) {
			let gefunden: boolean = false;
			for (const idLehrer of listLehrer) {
				if (klassenleitung as unknown === idLehrer as unknown) {
					gefunden = true;
				}
			}
			if (!gefunden) {
				this.addFehler(0, "Leitung der Klasse: Die id des Eintrages muss einer gültigen Lehrer ID zuordenbar sein.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKll10KlassenKlassenleitungslisteLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKll10KlassenKlassenleitungslisteLehrkraft', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKll10KlassenKlassenleitungslisteLehrkraft>('de.svws_nrw.asd.validate.klassen.ValidatorKll10KlassenKlassenleitungslisteLehrkraft');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKll10KlassenKlassenleitungslisteLehrkraft(obj: unknown): ValidatorKll10KlassenKlassenleitungslisteLehrkraft {
	return obj as ValidatorKll10KlassenKlassenleitungslisteLehrkraft;
}
