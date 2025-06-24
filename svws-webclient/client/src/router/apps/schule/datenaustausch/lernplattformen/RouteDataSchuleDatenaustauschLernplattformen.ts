import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { type Lernplattform, type List, ArrayList } from "@core";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";


interface RouteStateDatenaustauschLernplattformen extends RouteStateInterface {
	lernplattformen: List<Lernplattform>;
}

const defaultState = <RouteStateDatenaustauschLernplattformen>{
	lernplattformen: new ArrayList(),
};

export class RouteDataSchuleDatenaustauschLernplattformen extends RouteData<RouteStateDatenaustauschLernplattformen> {

	public constructor() {
		super(defaultState);
	}

	get lernplattformen(): List<Lernplattform> {
		return this._state.value.lernplattformen;
	}

	export = async (lernplattform: Lernplattform, datenformat: string): Promise<Blob | null> => {
		if (datenformat === 'JSON') {
			// raw JSON holen, damit transpilierte Felder aus z.B. ArrayList nicht im resultierenden JSON enthalten sind
			const lernplattformenExport = await api.server.getJSON(`/db/${api.schema}/v1/lernplattformen/${lernplattform.id}/${routeApp.data.idSchuljahresabschnitt}`);
			return new Blob([lernplattformenExport], { type: "application/json" });
		} else if (datenformat === 'GZIP')
			return (await api.server.getLernplattformenExportAsGzip(api.schema, routeApp.data.idSchuljahresabschnitt, lernplattform.id)).data;

		return null;
	}

	public async init() {
		this._state.value.lernplattformen = await api.server.getLernplattformen(api.schema);
	}

}
