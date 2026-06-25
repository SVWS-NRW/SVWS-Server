import { KlassenDaten } from '../../../asd/data/klassen/KlassenDaten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { Klassenart } from '../../../asd/types/klassen/Klassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKk02KlassenKlassenart extends Validator {

	private readonly _klassenDaten: Supplier<KlassenDaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten  ein Supplier für die Klassendaten
	 * @param kontext       der Kontext des Validators
	 */
	public constructor(klassenDaten: Supplier<KlassenDaten>, kontext: ValidatorKontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;
	}

	protected pruefe(): boolean {
		const daten: KlassenDaten | null = this._klassenDaten.get();
		if ((daten === null) || (daten.idKlassenart === null)) {
			return true;
		}
		const art: Klassenart | null = Klassenart.data().getWertByIDOrNull(daten.idKlassenart);
		if (art === null) {
			return true;
		}
		if (!Klassenart.data().isGueltig(daten.idKlassenart, this.kontext().getSchuljahr())) {
			this.addFehler(0, "Art der Klasse: Der eingetragene Wert für das Feld 'Klassenart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKk02KlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKk02KlassenKlassenart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKk02KlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKk02KlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKk02KlassenKlassenart(obj: unknown): ValidatorKk02KlassenKlassenart {
	return obj as ValidatorKk02KlassenKlassenart;
}
