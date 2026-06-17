import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { LehrerAnrechnungsgrund } from '../../../asd/types/lehrer/LehrerAnrechnungsgrund';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;


	/**
	 * Erstellt einen neuen Validator für die zeitliche Gültigkeit der Anrechnungsgründe.
	 *
	 * @param anrechnungen       die Liste der Anrechnungsstunden
	 * @param lehraemter         die Liste der Lehrämter
	 * @param pflichtstundensoll das Pflichtstundensoll
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
		this._validatoren.add(new ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, kontext));
		this._validatoren.add(new ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));
	}

	protected pruefe(): boolean {
		const liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this.anrechnungen.get();
		if (liste === null) {
			return true;
		}
		for (const eintrag of liste) {
			if (!LehrerAnrechnungsgrund.data().isGueltig(eintrag.idGrund, this.kontext().getSchuljahr())) {
				this.addFehler(0, "Der eingetragene Wert für das Feld 'Anrechnungsgründe' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
