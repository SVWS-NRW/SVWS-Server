import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import { ValidatorLppa18LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa18LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa16LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa16LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { LehrerAnrechnungsgrund } from '../../../asd/types/lehrer/LehrerAnrechnungsgrund';
import { ValidatorLppa19LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa19LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/validate/lehrer/ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden';
import { Class } from '../../../java/lang/Class';
import { ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Das aktuelle Schuljahr
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly _anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;


	/**
	 * Erstellt einen neuen Validator für die zeitliche Gültigkeit der Anrechnungsgründe.
	 *
	 * @param schuljahr              Schuljahr
	 * @param anrechnungen           die Liste der Anrechnungsstunden
	 * @param mehrleistung           die Liste der Mehrleistungsstunden
	 * @param minderleistung         die Liste der Minderleistungsstunden
	 * @param lehraemter             die Liste der Lehrämter
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param idEinsatzstatus        die ID des Einsatzstatus
	 * @param geschlecht             das Geschlecht der Lehrkraft
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, mehrleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, pflichtstundensoll: Supplier<number | null>, idBeschaeftigungsart: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, geschlecht: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._anrechnungen = anrechnungen;
		this._validatoren.add(new ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, kontext));
		this._validatoren.add(new ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, lehraemter, pflichtstundensoll, kontext));
		this._validatoren.add(new ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden(mehrleistung, minderleistung, kontext));
		this._validatoren.add(new ValidatorLppa13LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(mehrleistung, kontext));
		this._validatoren.add(new ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(mehrleistung, idBeschaeftigungsart, idEinsatzstatus, kontext));
		this._validatoren.add(new ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, idBeschaeftigungsart, idEinsatzstatus, kontext));
		this._validatoren.add(new ValidatorLppa16LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, kontext));
		this._validatoren.add(new ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, geschlecht, kontext));
		this._validatoren.add(new ValidatorLppa18LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, pflichtstundensoll, kontext));
		this._validatoren.add(new ValidatorLppa19LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, pflichtstundensoll, kontext));
		this._validatoren.add(new ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(minderleistung, pflichtstundensoll, kontext));
		this._validatoren.add(new ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, minderleistung, kontext));
		this._validatoren.add(new ValidatorLppa22LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(anrechnungen, kontext));
	}

	protected pruefe(): boolean {
		const liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._anrechnungen.get();
		if (liste === null) {
			return true;
		}
		for (const eintrag of liste) {
			if (!LehrerAnrechnungsgrund.data().isGueltig(eintrag.idGrund, this._schuljahr.get())) {
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
