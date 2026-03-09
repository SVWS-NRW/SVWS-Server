import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { LehrerAnrechnungsgrund } from '../../../asd/types/lehrer/LehrerAnrechnungsgrund';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param anrechnungen       die Liste der Anrechnungsstunden
	 * @param lehraemter         die Liste der Lehrämter
	 * @param pflichtstundensoll das Pflichtstundensoll
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
		this._validatoren.add(new ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));
	}

	protected pruefe(): boolean {
		const liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this.anrechnungen.get();
		if (liste === null)
			return true;
		for (const eintrag of liste)
			if ((eintrag.idGrund === null) || (LehrerAnrechnungsgrund.data().getWertByIDOrNull(eintrag.idGrund) === null)) {
				this.addFehler(0, "Feld 'Anrechnungsgründe' muss besetzt sein.");
				return false;
			}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
