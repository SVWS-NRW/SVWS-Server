import type { ENMLerngruppe } from "@core";
import { UnsupportedOperationException, type List, type SimpleOperationResponse } from "@core";
import type { EnmLerngruppenAuswahlEintrag } from "@ui";
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
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.")
	}

	protected deleteMessage(id: number, eintrag: any): string {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.")
	}

	get columnsVisible(): Map<string, boolean|null> {
		return new Map<string, boolean|null>(JSON.parse(api.config.getValue("notenmodul.leistungen.table.columns")));
	}

	setColumnsVisible = async (value: Map<string, boolean|null>) => {
		await api.config.setValue('notenmodul.leistungen.table.columns', JSON.stringify([...value]));
	}

	get floskelEditorVisible(): boolean {
		return (api.config.getValue("notenmodul.leistungen.floskelEditorVisible") === 'true');
	}

	setFloskelEditorVisible = async (value: boolean) => {
		await api.config.setValue('notenmodul.leistungen.floskelEditorVisible', value ? 'true' : 'false');
	}

}
