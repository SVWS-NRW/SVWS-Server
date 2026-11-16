import { LehrerMinderleistungsarten } from '../../../asd/types/lehrer/LehrerMinderleistungsarten';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { JavaMath } from '../../../java/lang/JavaMath';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly _anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

	/**
	 * Die Liste der Minderleistungsstunden.
	 */
	private readonly _minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param anrechnungen     die Liste der Anrechnungsstunden
	 * @param minderleistung   die Liste der Minderleistungsstunden
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, kontext: ValidatorKontext) {
		super(kontext);
		this._anrechnungen = anrechnungen;
		this._minderleistung = minderleistung;
	}

	protected pruefe(): boolean {
		const listeAnrechnungen: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._anrechnungen.get();
		const listeMinderleistung: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._minderleistung.get();
		if ((listeAnrechnungen === null) || (listeMinderleistung === null)) {
			return true;
		}
		const minder230: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_230");
		const minder240: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_240");
		const minder250: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_250");
		let hatVollabwesenheit: boolean = false;
		for (const eintrag of listeMinderleistung) {
			if (eintrag.idGrund === null) {
				continue;
			}
			const art: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art as unknown === minder230 as unknown) || (art as unknown === minder240 as unknown) || (art as unknown === minder250 as unknown)) {
				hatVollabwesenheit = true;
				break;
			}
		}
		if (!hatVollabwesenheit) {
			return true;
		}
		for (const anrechnung of listeAnrechnungen) {
			if ((anrechnung.idGrund !== null) || (Math.abs(anrechnung.anzahl) > 0.001)) {
				this.addFehler(0, "Anrechnungsgründe des Lehrers: Bei Eintragung der Minderleistungsgründe 'Beurlaubung, Rückkehr im Laufe des Schuljahres', 'langfristige Erkrankung' und 'Abwesenheit wegen Beschäftigungsverbot gem. § 3 MuSchG' dürfen keine Anrechnungsgründe eingetragen werden.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
