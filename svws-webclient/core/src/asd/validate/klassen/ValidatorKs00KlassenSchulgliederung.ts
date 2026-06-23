import { KlassenDaten } from '../../../asd/data/klassen/KlassenDaten';
import { ValidatorKs01KlassenSchulgliederung } from '../../../asd/validate/klassen/ValidatorKs01KlassenSchulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKs00KlassenSchulgliederung extends Validator {

	private readonly _klassenDaten: Supplier<KlassenDaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten   ein Supplier für die Klassendaten
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(klassenDaten: Supplier<KlassenDaten>, kontext: ValidatorKontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;
		this._validatoren.add(new ValidatorKs01KlassenSchulgliederung(klassenDaten, kontext));
	}

	protected pruefe(): boolean {
		const daten: KlassenDaten | null = this._klassenDaten.get();
		if ((daten === null) || (daten.idSchulgliederung === -1)) {
			this.addFehler(0, "Schulgliederung der Klasse: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKs00KlassenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKs00KlassenSchulgliederung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKs00KlassenSchulgliederung>('de.svws_nrw.asd.validate.klassen.ValidatorKs00KlassenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKs00KlassenSchulgliederung(obj: unknown): ValidatorKs00KlassenSchulgliederung {
	return obj as ValidatorKs00KlassenSchulgliederung;
}
