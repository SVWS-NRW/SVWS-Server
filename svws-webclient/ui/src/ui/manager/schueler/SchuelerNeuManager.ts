import type { KlasseListItem } from "../../../../../core/src/asd/data/klassen/KlasseListItem";
import type { EinschulungsartKatalogEintrag } from "../../../../../core/src/asd/data/schueler/EinschulungsartKatalogEintrag";
import type { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import type { JahrgangsDaten } from "../../../../../core/src/core/data/jahrgang/JahrgangsDaten";
import type { Kindergarten } from "../../../../../core/src/core/data/schule/Kindergarten";
import type { ReligionEintrag } from "../../../../../core/src/core/data/schule/ReligionEintrag";
import type { List } from "../../../../../core/src/java/util/List";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";


export class SchuelerNeuManager {

	private readonly _kindergaertenById: Map<number, Kindergarten>;
	private readonly _einschulungsartenById: Map<number, EinschulungsartKatalogEintrag>;
	private readonly _klassenByIdAbschnitt: Map<number, List<KlasseListItem>>;
	private readonly _jahrgaengeById: Map<number, JahrgangsDaten>;
	private readonly _religionenById: Map<number, ReligionEintrag>;
	private readonly _schuljahresabschnitte: List<Schuljahresabschnitt>;
	private readonly _aktuellerAbschnitt: Schuljahresabschnitt;
	private readonly _schuljahresabschnitteFilteredById: Map<number, Schuljahresabschnitt>;

	/**
	 * Erzeugt einen neuen SchuelerNeuManager
	 *
	 * @param kindergaertenById			kindergaertenById
	 * @param einschulungsartenById		einschulungsartenById
	 * @param jahrgaengeById			jahrgaengeById
	 * @param religionenById			religionenById
	 * @param schuljahresabschnitte		schuljahresabschnitte
	 * @param klassenByIdAbschnitt		klassenByIdAbschnitt
	 * @param aktuellerAbschnitt		aktuellerAbschnitt
	 */
	constructor(
		kindergaertenById: Map<number, Kindergarten>,
		einschulungsartenById: Map<number, EinschulungsartKatalogEintrag>,
		jahrgaengeById: Map<number, JahrgangsDaten>,
		religionenById: Map<number, ReligionEintrag>,
		schuljahresabschnitte: List<Schuljahresabschnitt>,
		klassenByIdAbschnitt: Map<number, List<KlasseListItem>>,
		aktuellerAbschnitt: Schuljahresabschnitt
	) {
		this._kindergaertenById = kindergaertenById;
		this._einschulungsartenById = einschulungsartenById;
		this._klassenByIdAbschnitt = klassenByIdAbschnitt;
		this._jahrgaengeById = jahrgaengeById;
		this._religionenById = religionenById;
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

	get einschulungsartenById(): Map<number, EinschulungsartKatalogEintrag> {
		return this._einschulungsartenById;
	}

	get klassenByIdAbschnitt(): Map<number, List<KlasseListItem>> {
		return this._klassenByIdAbschnitt;
	}

	get jahrgaengeById(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}

	get religionenById(): Map<number, ReligionEintrag> {
		return this._religionenById;
	}

	get aktuellerAbschnitt(): Schuljahresabschnitt {
		return this._aktuellerAbschnitt;
	}

	get schuljahresabschnitte(): List<Schuljahresabschnitt> {
		return this._schuljahresabschnitte;
	}

	get schuljahresabschnitteFilteredById(): Map<number, Schuljahresabschnitt> {
		return this._schuljahresabschnitteFilteredById;
	}

	getKlassenByIdFuerAbschnitt(idAbschnitt: number): Map<number, KlasseListItem> {
		const klassen = this.klassenByIdAbschnitt.get(idAbschnitt) ?? new ArrayList<KlasseListItem>();
		const result = new Map<number, KlasseListItem>();
		for (const klasse of klassen) {
			result.set(klasse.id, klasse);
		}
		return result;
	}
}
