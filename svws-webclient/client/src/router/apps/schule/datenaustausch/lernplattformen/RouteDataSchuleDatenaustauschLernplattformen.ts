import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { type Lernplattform, type List, ArrayList, LernplattformV1Export } from "@core";
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
			const lernplattformenExport = await api.external.getLernplattformenExport(api.schema, lernplattform.id, routeApp.data.idSchuljahresabschnitt);
			return new Blob([ LernplattformV1Export.transpilerToJSON(lernplattformenExport) ], { type: "application/json" });
		} else if (datenformat === 'GZIP') {
			return (await api.external.getLernplattformenExportAsGzip(api.schema, lernplattform.id, routeApp.data.idSchuljahresabschnitt)).data;
		}
		return null;
	}

	public async init() {
		this._state.value.lernplattformen = await api.server.getLernplattformen(api.schema);
	}

}
