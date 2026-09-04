import type { ApiFile } from "@core/api/BaseApi";
import { ENMv2Daten } from "@core/core/data/enm/v2/ENMv2Daten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


export interface RouteStateDatenaustauschENM extends RouteStateInterface {
	listLehrer: List<LehrerListeEintrag>;
}

const defaultState = <RouteStateDatenaustauschENM>{
	listLehrer: new ArrayList(),
};


export class RouteDataSchuleDatenaustauschENM extends RouteData<RouteStateDatenaustauschENM> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const listLehrer = await api.server.getLehrer(api.schema);
		this.setPatchedState({ listLehrer });
	}

	get listLehrer() {
		return this._state.value.listLehrer;
	}

	exportLehrerENM = async (id: number): Promise<ENMv2Daten> => {
		return api.server.getLehrerENMv2Daten(api.schema, id);
	};

	exportGzipENM = async (): Promise<ApiFile> => {
		return api.server.getENMv2DatenGZip(api.schema);
	};

	importGzipENM = async (data: FormData): Promise<void> => {
		return api.server.importENMv2DatenGZip(data, api.schema);
	};

	importENM = async (file: File): Promise<void> => {
		const json = new TextDecoder().decode(await file.arrayBuffer());
		const data = ENMv2Daten.transpilerFromJSON(json);
		return api.server.importENMv2Daten(data, api.schema);
	};

}
