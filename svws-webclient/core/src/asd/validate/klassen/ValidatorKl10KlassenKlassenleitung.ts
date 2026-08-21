import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKl10KlassenKlassenleitung extends Validator {

	private readonly _klassenLeitungen: Supplier<List<number>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenLeitungen   Klassenleitungen
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(klassenLeitungen: Supplier<List<number>>, kontext: ValidatorKontext) {
		super(kontext);
		this._klassenLeitungen = klassenLeitungen;
	}

	protected pruefe(): boolean {
		const klassenLeitungen: List<number> = this._klassenLeitungen.get();
		if (klassenLeitungen.size() === 0) {
			this.addFehler(0, "Leitung der Klasse: Zu jeder Klasse muss mindestens eine Klassenleitung vorliegen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKl10KlassenKlassenleitung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKl10KlassenKlassenleitung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKl10KlassenKlassenleitung>('de.svws_nrw.asd.validate.klassen.ValidatorKl10KlassenKlassenleitung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKl10KlassenKlassenleitung(obj: unknown): ValidatorKl10KlassenKlassenleitung {
	return obj as ValidatorKl10KlassenKlassenleitung;
}
