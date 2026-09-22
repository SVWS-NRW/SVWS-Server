import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";


export class SchuelerNeuManager {

	private readonly _kindergaertenById: Map<number, Kindergarten>;
	private readonly _klassenByIdAbschnitt: Map<number, List<KlassenListeEintrag>>;
	private readonly _jahrgaengeById: Map<number, JahrgangsDaten>;
	private readonly _schuljahresabschnitte: Iterable<Schuljahresabschnitt>;
	private readonly _aktuellerAbschnitt: Schuljahresabschnitt;
	private readonly _schuljahresabschnitteFilteredById: Map<number, Schuljahresabschnitt>;

	/**
	 * Erzeugt einen neuen SchuelerNeuManager
	 *
	 * @param kindergaertenById			kindergaertenById
	 * @param jahrgaengeById			jahrgaengeById
	 * @param schuljahresabschnitte		schuljahresabschnitte
	 * @param klassenByIdAbschnitt		klassenByIdAbschnitt
	 * @param aktuellerAbschnitt		aktuellerAbschnitt
	 */
	constructor(
		kindergaertenById: Map<number, Kindergarten>,
		jahrgaengeById: Map<number, JahrgangsDaten>,
		schuljahresabschnitte: Iterable<Schuljahresabschnitt>,
		klassenByIdAbschnitt: Map<number, List<KlassenListeEintrag>>,
		aktuellerAbschnitt: Schuljahresabschnitt
	) {
		this._kindergaertenById = kindergaertenById;
		this._klassenByIdAbschnitt = klassenByIdAbschnitt;
		this._jahrgaengeById = jahrgaengeById;
		this._schuljahresabschnitte = schuljahresabschnitte;
		this._aktuellerAbschnitt = aktuellerAbschnitt;
		this._schuljahresabschnitteFilteredById = this.filterSchuljahresabschnitte();
	}

	private filterSchuljahresabschnitte() {
		const result = new Map<number, Schuljahresabschnitt>();
		result.set(this.aktuellerAbschnitt.id, this.aktuellerAbschnitt);

		if (this.aktuellerAbschnitt.idFolgeAbschnitt === null) {
			return result;
		}

		for (const abschnitt of this._schuljahresabschnitte) {
			if (abschnitt.id === this.aktuellerAbschnitt.idFolgeAbschnitt) {
				result.set(abschnitt.id, abschnitt);
				break;
			}
		}
		return result;
	}

	get kindergaertenById(): Map<number, Kindergarten> {
		return this._kindergaertenById;
	}

	get klassenByIdAbschnitt(): Map<number, List<KlassenListeEintrag>> {
		return this._klassenByIdAbschnitt;
	}

	get jahrgaengeById(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}

	get aktuellerAbschnitt(): Schuljahresabschnitt {
		return this._aktuellerAbschnitt;
	}

	get schuljahresabschnitteFilteredById(): Map<number, Schuljahresabschnitt> {
		return this._schuljahresabschnitteFilteredById;
	}

	getKlassenByIdFuerAbschnitt(idAbschnitt: number): Map<number, KlassenListeEintrag> {
		const klassen = this.klassenByIdAbschnitt.get(idAbschnitt) ?? new ArrayList<KlassenListeEintrag>();
		const result = new Map<number, KlassenListeEintrag>();
		for (const klasse of klassen) {
			result.set(klasse.id, klasse);
		}
		return result;
	}
}
