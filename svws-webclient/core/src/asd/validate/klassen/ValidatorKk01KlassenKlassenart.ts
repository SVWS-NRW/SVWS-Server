import { KlassenDaten } from '../../../asd/data/klassen/KlassenDaten';
import { ValidatorKk02KlassenKlassenart } from '../../../asd/validate/klassen/ValidatorKk02KlassenKlassenart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { Klassenart } from '../../../asd/types/klassen/Klassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKk01KlassenKlassenart extends Validator {

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
		this._validatoren.add(new ValidatorKk02KlassenKlassenart(klassenDaten, kontext));
	}

	protected pruefe(): boolean {
		const daten: KlassenDaten | null = this._klassenDaten.get();
		if ((daten === null) || (daten.idKlassenart === null)) {
			return true;
		}
		const art: Klassenart | null = Klassenart.data().getWertByIDOrNull(daten.idKlassenart);
		if (art === null) {
			this.addFehler(0, "Art der Klasse: Das Feld 'Klassenart' muss zulässig sein.");
			return false;
		}
		if (!art.hatSchulform(this.kontext().getSchuljahr(), this.kontext().getSchulform())) {
			this.addFehler(0, "Art der Klasse: Das Feld 'Klassenart' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKk01KlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKk01KlassenKlassenart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKk01KlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKk01KlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKk01KlassenKlassenart(obj: unknown): ValidatorKk01KlassenKlassenart {
	return obj as ValidatorKk01KlassenKlassenart;
}
