import type { SchuelerBetrieb } from "../../../../../core/src/asd/data/schueler/SchuelerBetrieb";
import type { LehrerListeEintrag } from "../../../../../core/src/core/data/lehrer/LehrerListeEintrag";
import type { Beschaeftigungsart } from "../../../../../core/src/core/data/schule/Beschaeftigungsart";
import type { Betrieb } from "../../../../../core/src/core/data/schule/Betrieb";
import type { BetriebeAnsprechpartner } from "../../../../../core/src/core/data/schule/BetriebeAnsprechpartner";
import type { List } from "../../../../../core/src/java/util/List";
import { SchuelerListeEintrag } from "../../../../../core/src/core/data/schueler/SchuelerListeEintrag";
import { StateManager } from "../../StateManager";

interface SchuelerBetriebeState {
	auswahl: SchuelerListeEintrag;
	schuelerBetriebeById: Map<number, SchuelerBetrieb>;
}

const createDefaultState = (): SchuelerBetriebeState => ({
	auswahl: new SchuelerListeEintrag(),
	schuelerBetriebeById: new Map(),
});


export class SchuelerBetriebeManager extends StateManager<SchuelerBetriebeState> {

	private readonly _idSchueler: number;
	private readonly _ansprechpartnerById: Map<number, BetriebeAnsprechpartner>;
	private readonly _lehrerById: Map<number, LehrerListeEintrag>;
	private readonly _betriebeById: Map<number, Betrieb>;
	private readonly _beschaeftigungsartenById: Map<number, Beschaeftigungsart>;

	constructor(
		idSchueler: number,
		schuelerBetriebe: List<SchuelerBetrieb>,
		ansprechpartner: List<BetriebeAnsprechpartner>,
		lehrer: List<LehrerListeEintrag>,
		betriebeById: Map<number, Betrieb>,
		beschaeftigungsartenById: Map<number, Beschaeftigungsart>) {
		super(createDefaultState());
		this._idSchueler = idSchueler;
		this._state.value.schuelerBetriebeById = this.mapSchuelerBetriebe(schuelerBetriebe);
		this._ansprechpartnerById = this.mapAnsprechpartner(ansprechpartner);
		this._lehrerById = this.mapLehrer(lehrer);
		this._betriebeById = betriebeById;
		this._beschaeftigungsartenById = beschaeftigungsartenById;
	}

	private mapAnsprechpartner(ansprechpartnerList: List<BetriebeAnsprechpartner>): Map<number, BetriebeAnsprechpartner> {
		const ansprechpartnerById = new Map<number, BetriebeAnsprechpartner>();
		for (const ansprechpartner of ansprechpartnerList) {
			ansprechpartnerById.set(ansprechpartner.id, ansprechpartner);
		}
		return ansprechpartnerById;
	}

	private mapSchuelerBetriebe(schuelerBetriebe: List<SchuelerBetrieb>): Map<number, SchuelerBetrieb> {
		const schuelerBetriebeById = new Map<number, SchuelerBetrieb>();
		for (const schuelerBetrieb of schuelerBetriebe) {
			schuelerBetriebeById.set(schuelerBetrieb.id, schuelerBetrieb);
		}
		return schuelerBetriebeById;
	}

	private mapLehrer(lehrerListe: List<LehrerListeEintrag>): Map<number, LehrerListeEintrag> {
		const lehrerById = new Map<number, LehrerListeEintrag>();
		for (const lehrer of lehrerListe) {
			lehrerById.set(lehrer.id, lehrer);
		}
		return lehrerById;
	}

	get betriebeById(): Map<number, Betrieb> {
		return this._betriebeById;
	}

	get lehrerById(): Map<number, LehrerListeEintrag> {
		return this._lehrerById;
	}

	get beschaeftigungsartenById(): Map<number, Beschaeftigungsart> {
		return this._beschaeftigungsartenById;
	}

	get schuelerBetriebeById(): Map<number, SchuelerBetrieb> {
		return this._state.value.schuelerBetriebeById;
	}

	get ansprechpartnerById(): Map<number, BetriebeAnsprechpartner> {
		return this._ansprechpartnerById;
	}

	get idSchueler(): number {
		return this._idSchueler;
	}
}
