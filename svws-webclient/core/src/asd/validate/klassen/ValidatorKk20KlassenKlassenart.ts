import { JavaObject } from '../../../java/lang/JavaObject';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Klassenart } from '../../../asd/types/klassen/Klassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKk20KlassenKlassenart extends Validator {

	private readonly _idKlassenart: Supplier<number | null>;

	private readonly _listIdKlassenartSchueler: Supplier<List<number> | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart                KlassenartID
	 * @param listIdKlassenartSchueler    Liste der IDs der Klassenart der Schüler dieser Klasse
	 * @param kontext                     der Kontext des Validators
	 */
	public constructor(idKlassenart: Supplier<number | null>, listIdKlassenartSchueler: Supplier<List<number> | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
		this._listIdKlassenartSchueler = listIdKlassenartSchueler;
	}

	protected pruefe(): boolean {
		const idKlassenart: number | null = this._idKlassenart.get();
		if (!JavaObject.equalsTranspiler(Klassenart.PK, (Klassenart.data().getWertByIDOrNull(idKlassenart)))) {
			let listIdKlassenartSchueler: List<number> | null = this._listIdKlassenartSchueler.get();
			if (listIdKlassenartSchueler !== null) {
				for (let idKlassenartSchueler of listIdKlassenartSchueler) {
					if (JavaObject.equalsTranspiler(Klassenart.PK, (Klassenart.data().getWertByIDOrNull(idKlassenartSchueler)))) {
						this.addFehler(0, "Klassenart der Klasse: In der vorliegenden Klasse ist mindestens ein(e) Schüler(in) mit der Klassenart 'Profilklasse' vorhanden. In den Daten zu dieser Klasse findet sich jedoch kein entsprechender Eintrag zur Profilklasse.");
						return false;
					}
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKk20KlassenKlassenart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKk20KlassenKlassenart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKk20KlassenKlassenart>('de.svws_nrw.asd.validate.klassen.ValidatorKk20KlassenKlassenart');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKk20KlassenKlassenart(obj: unknown): ValidatorKk20KlassenKlassenart {
	return obj as ValidatorKk20KlassenKlassenart;
}
