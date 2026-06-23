import { KlassenDaten } from '../../../asd/data/klassen/KlassenDaten';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorKs02KlassenSchulgliederung } from '../../../asd/validate/klassen/ValidatorKs02KlassenSchulgliederung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKs01KlassenSchulgliederung extends Validator {

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
		this._validatoren.add(new ValidatorKs02KlassenSchulgliederung(klassenDaten, kontext));
	}

	protected pruefe(): boolean {
		const daten: KlassenDaten | null = this._klassenDaten.get();
		if (daten === null) {
			return true;
		}
		const gliederung: Schulgliederung | null = Schulgliederung.data().getWertByIDOrNull(daten.idSchulgliederung);
		if (gliederung === null) {
			this.addFehler(0, "Schulgliederung der Klasse: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}
		if (!gliederung.hatSchulform(this.kontext().getSchuljahr(), this.kontext().getSchulform())) {
			this.addFehler(0, "Schulgliederung der Klasse: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKs01KlassenSchulgliederung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKs01KlassenSchulgliederung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKs01KlassenSchulgliederung>('de.svws_nrw.asd.validate.klassen.ValidatorKs01KlassenSchulgliederung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKs01KlassenSchulgliederung(obj: unknown): ValidatorKs01KlassenSchulgliederung {
	return obj as ValidatorKs01KlassenSchulgliederung;
}
