import type { GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp} from "@core";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateDataGostFaecher extends RouteStateInterface {
	abiturjahr: number | undefined;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination> | undefined;
}

const defaultState = <RouteStateDataGostFaecher> {
	abiturjahr: undefined,
	mapFachkombinationen: undefined,
};

export class RouteDataGostFaecher extends RouteData<RouteStateDataGostFaecher> {

	public constructor() {
		super(defaultState);
	}

	get abiturjahr(): number {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Unerwarteter Fehler: Jahrgang nicht festgelegt, es können keine Informationen zu den Fächern abgerufen oder eingegeben werden.");
		return this._state.value.abiturjahr;
	}

	public get mapFachkombinationen() : Map<number, GostJahrgangFachkombination> {
		if (this._state.value.mapFachkombinationen === undefined)
			throw new Error("Zugriff auf die Fachkombinationen, bevor diese geladen wurden.");
		return this._state.value.mapFachkombinationen;
	}

	public async setEintrag(abiturjahr?: number) {
		if (abiturjahr === this._state.value.abiturjahr)
			return;
		let mapFachkombinationen = undefined;
		if (abiturjahr !== undefined) {
			const listFachkombinationen = await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, abiturjahr);
			mapFachkombinationen = new Map<number, GostJahrgangFachkombination>();
			for (const fk of listFachkombinationen)
				mapFachkombinationen.set(fk.id, fk);
		}
		this.setPatchedState({abiturjahr, mapFachkombinationen})
	}

	patchFachkombination = async (data: Partial<GostJahrgangFachkombination>, id : number) => {
		const mapFachkombinationen = this.mapFachkombinationen;
		const kombi = mapFachkombinationen.get(id);
		if (kombi === undefined)
			throw new Error("Änderungen an der Fachkombination mit der ID " + id + " nicht möglich, da eine solche Fachkombination nicht bekannt ist.");
		await api.server.patchGostFachkombination(data, api.schema, id);
		Object.assign(kombi, data);
		mapFachkombinationen.set(kombi.id, kombi);
		this.setPatchedState({mapFachkombinationen});
		return;
	}

	addFachkombination = async (typ: GostLaufbahnplanungFachkombinationTyp) => {
		if (this._state.value.abiturjahr === undefined)
			return undefined;
		const result = await api.server.addGostAbiturjahrgangFachkombination(api.schema, this.abiturjahr, typ.getValue());
		const mapFachkombinationen = this.mapFachkombinationen;
		if (result !== undefined)
			mapFachkombinationen.set(result.id, result);
		this.setPatchedState({mapFachkombinationen});
		return result;
	}

	removeFachkombination = async (id: number) => {
		const result = await api.server.deleteGostFachkombination(api.schema, id);
		const mapFachkombinationen = this.mapFachkombinationen;
		if (result !== undefined)
			mapFachkombinationen.delete(id);
		this.setPatchedState({mapFachkombinationen});
		return result;
	}

}
