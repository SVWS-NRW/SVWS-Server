import type { ENMLerngruppe, List, SimpleOperationResponse } from "@core";
import { UnsupportedOperationException } from "@core";
import { EnmLerngruppenAuswahlListeManager, ViewType } from "@ui";

import { api } from "~/router/Api";
import { routeNotenmodul } from "./RouteNotenmodul";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeNotenmodulLeistungenData } from "./RouteNotenmodulLeistungenData";


interface RouteStateNotenmodulLeistungen extends RouteStateAuswahlInterface<EnmLerngruppenAuswahlListeManager> {
}


export class RouteDataNotenmodulLeistungen extends RouteDataAuswahl<EnmLerngruppenAuswahlListeManager, RouteStateNotenmodulLeistungen> {

	public constructor() {
		super(<RouteStateNotenmodulLeistungen>{
			idSchuljahresabschnitt: -1,
			manager: undefined,
			view: routeNotenmodulLeistungenData,
			activeViewType: ViewType.DEFAULT,
			auswahl: [],
		}, { });
	}

	public async entferneDaten() {
		this.setPatchedState({
			idSchuljahresabschnitt: -1,
			manager: undefined,
		});
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateNotenmodulLeistungen>> {
		const manager = new EnmLerngruppenAuswahlListeManager(routeNotenmodul.data.manager, api.schuleStammdaten.idSchuljahresabschnitt,
			api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: ENMLerngruppe, state: Partial<RouteStateNotenmodulLeistungen>): Promise<ENMLerngruppe | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ENMLerngruppe>, id: number): Promise<void> {
		// nothing to do
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.");
	}

	protected deleteMessage(id: number, eintrag: any): string {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.");
	}

	get columnsVisible(): Map<string, boolean | null> {
		const config = JSON.parse(api.config.getValue("notenmodul.leistungen.table.columns"));
		if (config === null) {
			return routeNotenmodul.data.manager.spalten.mapSpaltenLeistungen;
		}
		return new Map<string, boolean | null>(config);
	}

	setColumnsVisible = async (value: Map<string, boolean | null>) => {
		await api.config.setValue('notenmodul.leistungen.table.columns', JSON.stringify([...value]));
	};

}
