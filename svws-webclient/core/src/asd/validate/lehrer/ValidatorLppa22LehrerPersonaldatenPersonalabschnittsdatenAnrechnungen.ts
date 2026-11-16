import type { JavaSet } from '../../../java/util/JavaSet';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { HashSet } from '../../../java/util/HashSet';

export class ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly _anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param anrechnungen   die Liste der Anrechnungsstunden
	 * @param kontext        der Kontext des Validators
	 */
	public constructor(anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, kontext: ValidatorKontext) {
		super(kontext);
		this._anrechnungen = anrechnungen;
	}

	protected pruefe(): boolean {
		const liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._anrechnungen.get();
		if (liste === null) {
			return true;
		}
		const vorhandeneGruende: JavaSet<number> | null = new HashSet<number>();
		for (const eintrag of liste) {
			if (eintrag.idGrund === null) {
				continue;
			}
			if (!vorhandeneGruende.add(eintrag.idGrund)) {
				this.addFehler(0, "Anrechnungsgründe des Lehrers: Der Anrechnungsgrund kommt mehrfach vor. Bitte prüfen.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
