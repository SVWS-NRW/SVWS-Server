import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen } from '../../../asd/validate/lehrer/ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Anrechnungsstunden.
	 */
	private readonly _anrechnungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;


	/**
	 * Erstellt einen neuen Validator für die Pflichtfeldprüfung der Anrechnungsgründe.
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
		this._anrechnungen = anrechnungen;
		this._validatoren.add(new ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(schuljahr, anrechnungen, mehrleistung, minderleistung, lehraemter, pflichtstundensoll, idBeschaeftigungsart, idEinsatzstatus, geschlecht, kontext));
	}

	protected pruefe(): boolean {
		const liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._anrechnungen.get();
		let istGueltig: boolean = true;
		if (liste === null) {
			istGueltig = false;
		} else {
			for (const eintrag of liste) {
				if (eintrag.idGrund === null) {
					istGueltig = false;
					break;
				}
			}
		}
		if (!istGueltig) {
			this.addFehler(0, "Das Feld 'Anrechnungsgründe' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
