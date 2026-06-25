import { KlassenDaten } from '../../../asd/data/klassen/KlassenDaten';
import { ValidatorKk01KlassenKlassenart } from '../../../asd/validate/klassen/ValidatorKk01KlassenKlassenart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKk00KlassenKlassenart extends Validator {

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
		this._validatoren.add(new ValidatorKk01KlassenKlassenart(klassenDaten, kontext));
	}

	protected pruefe(): boolean {
		const daten: KlassenDaten | null = this._klassenDaten.get();
		if ((daten === null) || (daten.idKlassenart === null)) {
			this.addFehler(0, "Art der Klasse: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKk00KlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKk00KlassenKlassenart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKk00KlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKk00KlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKk00KlassenKlassenart(obj: unknown): ValidatorKk00KlassenKlassenart {
	return obj as ValidatorKk00KlassenKlassenart;
}
