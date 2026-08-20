import { KlassenStatistikGesamt } from '../../../asd/data/statistik/KlassenStatistikGesamt';
import type { JavaSet } from '../../../java/util/JavaSet';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { HashSet } from '../../../java/util/HashSet';

export class ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet extends Validator {

	private readonly listKlassenDaten: Supplier<List<KlassenStatistikGesamt>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param listKlassenStatistikGesamt   die Liste aller Klassendaten
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(listKlassenStatistikGesamt: Supplier<List<KlassenStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this.listKlassenDaten = listKlassenStatistikGesamt;
	}

	protected pruefe(): boolean {
		const klassendatenList: List<KlassenStatistikGesamt> | null = this.listKlassenDaten.get();
		if (klassendatenList.isEmpty()) {
			return true;
		}
		const ids: JavaSet<string> = new HashSet<string>();
		for (const klassendaten of klassendatenList) {
			if (klassendaten.idJahrgang === null) {
				return true;
			}
			const idJahrgangText: string | null = klassendaten.idJahrgang.toString();
			const parallelitaet: string | null = klassendaten.parallelitaet;
			const unique: boolean = ids.add(idJahrgangText + parallelitaet);
			if (!unique) {
				this.addFehler(0, "Die Kombination von Klassenjahrgang und Parallelitaet existiert bereits.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.klassen.ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet>('de.svws_nrw.asd.validate.klassen.ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet(obj: unknown): ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet {
	return obj as ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet;
}
