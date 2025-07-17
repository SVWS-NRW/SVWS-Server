import type { EnmLerngruppenAuswahlEintrag} from "@ui";
import { EnmLerngruppenAuswahlListeManager, ViewType } from "@ui";
import { api } from "~/router/Api";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeNotenmodulTeilleistungenData } from "./RouteNotenmodulTeilleistungenData";
import { routeNotenmodul } from "./RouteNotenmodul";
import type { RouteParamsRawGeneric } from "vue-router";
import type { ENMLerngruppe, List, SimpleOperationResponse} from "@core";
import { UnsupportedOperationException } from "@core";


interface RouteStateNotenmodulTeilleistungen extends RouteStateAuswahlInterface<EnmLerngruppenAuswahlListeManager> {
}


export class RouteDataNotenmodulTeilleistungen extends RouteDataAuswahl<EnmLerngruppenAuswahlListeManager, RouteStateNotenmodulTeilleistungen> {

	public constructor() {
		super(<RouteStateNotenmodulTeilleistungen>{
			idSchuljahresabschnitt: -1,
			manager: undefined,
			view: routeNotenmodulTeilleistungenData,
			activeViewType: ViewType.DEFAULT,
		}, { });
	}

	public async entferneDaten() {
		this.setPatchedState({
			idSchuljahresabschnitt: -1,
			manager: undefined,
		});
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateNotenmodulTeilleistungen>> {
		const manager = new EnmLerngruppenAuswahlListeManager(routeNotenmodul.data.manager, api.schuleStammdaten.idSchuljahresabschnitt,
			api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: ENMLerngruppe, state: Partial<RouteStateNotenmodulTeilleistungen>): Promise<ENMLerngruppe | null> {
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
		return new Map<string, boolean|null>(JSON.parse(api.config.getValue("notenmodul.teilleistungen.table.columns")));
	}

	setColumnsVisible = async (value: Map<string, boolean|null>) => {
		await api.config.setValue('notenmodul.teilleistungen.table.columns', JSON.stringify([...value]));
	}

}
