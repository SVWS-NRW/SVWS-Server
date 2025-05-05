import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, Abteilung, SimpleOperationResponse } from "@core";
import { ArrayList } from "@core";
import { AbteilungenListeManager } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType } from "@ui";
import { routeAbteilungenGruppenprozesse} from "~/router/apps/schule/abteilungen/RouteAbteilungenGruppenprozesse";
import { routeAbteilungenDaten } from "~/router/apps/schule/abteilungen/RouteAbteilungenDaten";
import { routeAbteilungenNeu } from "~/router/apps/schule/abteilungen/RouteAbteilungenNeu";
import { api } from "~/router/Api";


const defaultState = {
	idSchuljahresabschnitt : -1,
	manager: new AbteilungenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeAbteilungenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataAbteilungen extends RouteDataAuswahl<AbteilungenListeManager, RouteStateAuswahlInterface<AbteilungenListeManager>> {

	public constructor() {
		super(defaultState, routeAbteilungenGruppenprozesse, routeAbteilungenNeu);
	}

	public addID(param: RouteParamsRawGeneric, id: number) {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<AbteilungenListeManager>>> {
		const abteilungen = await api.server.getAbteilungenByIdJahresAbschnitt(api.schema, api.schuleStammdaten.idSchuljahresabschnitt)
		const lehrer = await api.server.getLehrer(api.schema);
		const manager = new AbteilungenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, abteilungen, lehrer);
		return { manager }
	}

	async ladeDaten(auswahl: Abteilung): Promise<Abteilung> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Abteilung>, id: number): Promise<void> {
		await api.server.patchAbteilung(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteAbteilungen(ids, api.schema);
	}

	protected deleteMessage(id: number, abteilung: Abteilung | null): string {
		return `Abteilung ${abteilung?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

}
