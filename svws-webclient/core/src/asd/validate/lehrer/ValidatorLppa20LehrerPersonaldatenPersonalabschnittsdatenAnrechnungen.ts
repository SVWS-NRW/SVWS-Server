import { LehrerMinderleistungsarten } from '../../../asd/types/lehrer/LehrerMinderleistungsarten';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { JavaMath } from '../../../java/lang/JavaMath';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Minderleistungsstunden.
	 */
	private readonly _minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

	/**
	 * Das Pflichtstundensoll der Lehrkraft.
	 */
	private readonly _pflichtstundensoll: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param minderleistung       die Liste der Minderleistungsstunden
	 * @param pflichtstundensoll   das Pflichtstundensoll
	 * @param kontext              der Kontext des Validators
	 */
	public constructor(minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._minderleistung = minderleistung;
		this._pflichtstundensoll = pflichtstundensoll;
	}

	protected pruefe(): boolean {
		const liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._minderleistung.get();
		const soll: number | null = this._pflichtstundensoll.get();
		if ((liste === null) || (soll === null)) {
			return true;
		}
		const minder250: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_250");
		for (const eintrag of liste) {
			if (eintrag.idGrund === null) {
				continue;
			}
			const art: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art as unknown === minder250 as unknown) && (Math.abs(eintrag.anzahl - soll) > 0.001)) {
				this.addFehler(0, "Anrechnungsstunden des Lehrers: Bei Eintragung des Minderleistungsgrunds 'Abwesenheit wegen Beschäftigungsverbot gem. § 3 MuSchG' muss die Zahl der Ermäßigungsstunden dem Pflichtstundensoll der Lehrkraft entsprechen.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
