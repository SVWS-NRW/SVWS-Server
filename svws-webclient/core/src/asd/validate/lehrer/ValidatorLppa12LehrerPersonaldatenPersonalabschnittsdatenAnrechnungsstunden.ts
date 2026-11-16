import { LehrerMinderleistungsarten } from '../../../asd/types/lehrer/LehrerMinderleistungsarten';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerMehrleistungsarten } from '../../../asd/types/lehrer/LehrerMehrleistungsarten';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden extends Validator {

	/**
	 * Die Liste der Mehrleistungsstunden.
	 */
	private readonly _mehrleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

	/**
	 * Die Liste der Minderleistungsstunden.
	 */
	private readonly _minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param mehrleistung     die Liste der Mehrleistungsstunden
	 * @param minderleistung   die Liste der Minderleistungsstunden
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(mehrleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, kontext: ValidatorKontext) {
		super(kontext);
		this._mehrleistung = mehrleistung;
		this._minderleistung = minderleistung;
	}

	protected pruefe(): boolean {
		const listeMehrleistung: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._mehrleistung.get();
		const listeMinderleistung: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._minderleistung.get();
		if ((listeMehrleistung === null) || (listeMinderleistung === null)) {
			return true;
		}
		const mehrarbeit110: LehrerMehrleistungsarten | null = LehrerMehrleistungsarten.data().getWertByBezeichner("ID_110");
		let hatMehrarbeit110: boolean = false;
		for (const eintrag of listeMehrleistung) {
			if ((eintrag.idGrund !== null) && (LehrerMehrleistungsarten.data().getWertByIDOrNull(eintrag.idGrund) as unknown === mehrarbeit110 as unknown)) {
				hatMehrarbeit110 = true;
				break;
			}
		}
		if (!hatMehrarbeit110) {
			return true;
		}
		const minder230: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_230");
		const minder240: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_240");
		const minder250: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_250");
		for (const eintrag of listeMinderleistung) {
			if (eintrag.idGrund === null) {
				continue;
			}
			const art: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art as unknown === minder230 as unknown) || (art as unknown === minder240 as unknown) || (art as unknown === minder250 as unknown)) {
				this.addFehler(0, "Anrechnungsgrund des Lehrers: Bei einer Lehrkraft mit der Mehrleistung 'Mehrarbeit (angeordnet und regelmäßig)' dürfen nicht zusätzlich die Minderleistungen 'Beurlaubung (auch Elternzeit), Rückkehr im Laufe des Schuljahres', 'Langfristige Erkrankung' oder 'Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG' eingetragen sein.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden(obj: unknown): ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden {
	return obj as ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden;
}
