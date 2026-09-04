import type { SchuelerSchulbesuchMerkmal } from "@core/asd/data/schueler/SchuelerSchulbesuchMerkmal";
import type { SchuelerSchulbesuchSchule } from "@core/asd/data/schueler/SchuelerSchulbesuchSchule";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import type { Merkmal } from "@core/core/data/schule/Merkmal";

export interface SchulbesuchLookups {
	schulenById: Map<number, SchulEintrag>;
	merkmaleById: Map<number, Merkmal>;
	entlassgruendeById: Map<number, KatalogEntlassgrund>;
	kindergaertenById: Map<number, Kindergarten>;
	jahrgaengeById: Map<number, JahrgangsDaten>;
	abschnitteById: Map<number, Schuljahresabschnitt>;
}

export class SchuelerSchulbesuchManager {

	private readonly _daten: SchuelerSchulbesuchsdaten;
	private readonly _idSchueler: number;
	private readonly _schuljahr: number;
	private readonly _idSchuljahresabschnitt: number;

	private readonly _schuljahresabschnitteById: Map<number, Schuljahresabschnitt> = new Map();
	private readonly _schulenById: Map<number, SchulEintrag> = new Map();
	private readonly _merkmaleById: Map<number, Merkmal> = new Map();
	private readonly _entlassgruendeById: Map<number, KatalogEntlassgrund> = new Map();
	private readonly _kindergaertenById: Map<number, Kindergarten> = new Map();
	private readonly _jahrgaengeById: Map<number, JahrgangsDaten> = new Map();

	public constructor(
		schulbesuchsdaten: SchuelerSchulbesuchsdaten,
		idSchueler: number,
		idSchuljahresabschnitt: number,
		lookups: SchulbesuchLookups) {
		this._daten = schulbesuchsdaten;
		this._idSchueler = idSchueler;
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		this._schulenById = lookups.schulenById;
		this._merkmaleById = lookups.merkmaleById;
		this._entlassgruendeById = lookups.entlassgruendeById;
		this._kindergaertenById = lookups.kindergaertenById;
		this._jahrgaengeById = lookups.jahrgaengeById;
		this._schuljahresabschnitteById = lookups.abschnitteById;
		this._schuljahr = this.getSchuljahr();
	}

	private getSchuljahr(): number {
		return this._schuljahresabschnitteById.get(this._idSchuljahresabschnitt)?.schuljahr ?? -1;
	}

	// --- Merkmale ---

	public addMerkmal(m: SchuelerSchulbesuchMerkmal) {
		this.daten.merkmale.add(m);
	}

	public patchMerkmalById(id: number, data: Partial<SchuelerSchulbesuchMerkmal>) {
		const merkmal = this.getMerkmalById(id);
		if (merkmal === undefined) {
			return;
		}
		Object.assign(merkmal, data);
	}

	private getMerkmalById(id: number): SchuelerSchulbesuchMerkmal | undefined {
		for (const merkmal of this.daten.merkmale) {
			if (merkmal.id === id) {
				return merkmal;
			}
		}
		return undefined;
	}

	public deleteMerkmal(id: number) {
		for (let i = 0; i < this.daten.merkmale.size(); i++) {
			if (this.daten.merkmale.get(i).id === id) {
				this.daten.merkmale.removeElementAt(i);
			}
		}
	}

	// --- Bisherige Schulen ---

	public addBisherigeSchule(s: SchuelerSchulbesuchSchule) {
		this.daten.bisherBesuchteSchulen.add(s);
	}

	public patchBisherigeSchuleById(id: number, data: Partial<SchuelerSchulbesuchSchule>) {
		const schule = this.getBisherigeSchuleById(id);
		if (schule === undefined) {
			return;
		}
		Object.assign(schule, data);
	}

	public deleteBisherigeSchuleById(id: number) {
		for (let i = 0; i < this.daten.bisherBesuchteSchulen.size(); i++) {
			if (this.daten.bisherBesuchteSchulen.get(i).id === id) {
				this.daten.bisherBesuchteSchulen.removeElementAt(i);
			}
		}
	}

	private getBisherigeSchuleById(id: number): SchuelerSchulbesuchSchule | undefined {
		for (const schule of this.daten.bisherBesuchteSchulen) {
			if (schule.id === id) {
				return schule;
			}
		}
		return undefined;
	}

	get daten(): SchuelerSchulbesuchsdaten {
		return this._daten;
	}

	get idSchueler(): number {
		return this._idSchueler;
	}

	get schuljahr(): number {
		return this._schuljahr;
	}

	get idSchuljahresabschnitt(): number {
		return this._idSchuljahresabschnitt;
	}

	get schuljahresabschnitteById(): Map<number, Schuljahresabschnitt> {
		return this._schuljahresabschnitteById;
	}

	get schulenById(): Map<number, SchulEintrag> {
		return this._schulenById;
	}

	get merkmaleById(): Map<number, Merkmal> {
		return this._merkmaleById;
	}

	get entlassgruendeById(): Map<number, KatalogEntlassgrund> {
		return this._entlassgruendeById;
	}

	get kindergaertenById(): Map<number, Kindergarten> {
		return this._kindergaertenById;
	}

	get jahrgaengeById(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}
}
