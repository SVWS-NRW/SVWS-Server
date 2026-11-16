import { LehrerMinderleistungsarten } from '../../../asd/types/lehrer/LehrerMinderleistungsarten';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Geschlecht } from '../../../asd/types/Geschlecht';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Minderleistungsstunden.
	 */
	private readonly _minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

	/**
	 * Das Geschlecht der Lehrkraft.
	 */
	private readonly _geschlecht: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param minderleistung   die Liste der Minderleistungsstunden
	 * @param geschlecht       das Geschlecht der Lehrkraft
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, geschlecht: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._minderleistung = minderleistung;
		this._geschlecht = geschlecht;
	}

	protected pruefe(): boolean {
		const liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._minderleistung.get();
		const geschlechtId: number | null = this._geschlecht.get();
		if ((liste === null) || (geschlechtId === null)) {
			return true;
		}
		const geschlecht: Geschlecht | null = Geschlecht.fromValue(geschlechtId);
		if ((geschlecht as unknown === Geschlecht.W as unknown) || (geschlecht as unknown === Geschlecht.D as unknown) || (geschlecht as unknown === Geschlecht.X as unknown)) {
			return true;
		}
		const minder250: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_250");
		const minder255: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_255");
		for (const eintrag of liste) {
			if (eintrag.idGrund === null) {
				continue;
			}
			const art: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art as unknown === minder250 as unknown) || (art as unknown === minder255 as unknown)) {
				this.addFehler(0, "Anrechnungsgründe des Lehrers: Die Minderleistungsgründe 'Abwesenheit wegen Beschäftigungsverbot gem. § 3 MuSchG' oder 'Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG' dürfen nur bei weiblichen Lehrkräften angegeben werden.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
