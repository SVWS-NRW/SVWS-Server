import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Das Rechtsverhältnis des Lehrers
	 */
	private readonly _rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis | null>;

	/**
	 * Die Liste der Lehramts-Katalog-IDs des Lehrers
	 */
	private readonly _lehraemter: Supplier<List<number>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param rechtsverhaeltnis   das Rechtsverhältnis des Lehrers
	 * @param lehraemter          die Liste der Lehramts-Katalog-IDs des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis | null>, lehraemter: Supplier<List<number>>, kontext: ValidatorKontext) {
		super(kontext);
		this._rechtsverhaeltnis = rechtsverhaeltnis;
		this._lehraemter = lehraemter;
	}

	protected pruefe(): boolean {
		if (!JavaObject.equalsTranspiler(LehrerRechtsverhaeltnis.W, (this._rechtsverhaeltnis.get()))) {
			return true;
		}
		const lehraemter: List<number> | null = this._lehraemter.get();
		if (lehraemter !== null) {
			for (const idKatalogLehramt of lehraemter) {
				if ((idKatalogLehramt !== null) && JavaObject.equalsTranspiler(LehrerLehramt.ID_98, (LehrerLehramt.data().getWertByIDOrNull(idKatalogLehramt)))) {
					return true;
				}
			}
		}
		this.addFehler(0, "Rechtsverhältnis des Lehrers: Für das Rechtsverhältnis 'Beamte auf Widerruf (LAA)' muss das Lehramt 'Lehramtsanwärter/-in / Studienreferendar/-in' vorliegen.");
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
