import { KlassenStatistikGesamt } from '../../../asd/data/statistik/KlassenStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet } from '../../../asd/validate/klassen/ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listKlassenDaten   ein Supplier für die Klassendaten, die geprüft werden sollen
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(listKlassenDaten: Supplier<List<KlassenStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet(listKlassenDaten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet>('de.svws_nrw.asd.validate.klassen.ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet(obj: unknown): ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet {
	return obj as ValidatorKckpKlassenKombinationKlassenjahrgangParallelitaet;
}
