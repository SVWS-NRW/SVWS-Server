import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, Abteilung, SimpleOperationResponse, AbteilungKlassenzuordnung } from "@core";
import { ArrayList } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType, AbteilungenListeManager } from "@ui";
import { routeAbteilungenGruppenprozesse} from "~/router/apps/schule/schulbezogen/abteilungen/RouteAbteilungenGruppenprozesse";
import { routeAbteilungenDaten } from "~/router/apps/schule/schulbezogen/abteilungen/RouteAbteilungenDaten";
import { routeAbteilungenNeu } from "~/router/apps/schule/schulbezogen/abteilungen/RouteAbteilungenNeu";
import { api } from "~/router/Api";


const defaultState = {
	idSchuljahresabschnitt : -1,
	manager: new AbteilungenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList(), new ArrayList()),
	view: routeAbteilungenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataAbteilungen extends RouteDataAuswahl<AbteilungenListeManager, RouteStateAuswahlInterface<AbteilungenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeAbteilungenGruppenprozesse, hinzufuegen: routeAbteilungenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number) {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<AbteilungenListeManager>>> {
		const abteilungen = await api.server.getAbteilungenByIdJahresAbschnitt(api.schema, api.schuleStammdaten.idSchuljahresabschnitt)
		const lehrer = await api.server.getLehrer(api.schema);
		const klassen = await api.server.getKlassenFuerAbschnitt(api.schema, api.schuleStammdaten.idSchuljahresabschnitt);
		const manager = new AbteilungenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, abteilungen, lehrer, klassen);
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

	addAbteilung = async (data: Partial<Abteilung>) : Promise<number> => {
		const abteilung = await api.server.addAbteilung(data, api.schema, api.schuleStammdaten.idSchuljahresabschnitt);
		this.manager.liste.add(abteilung);
		this.commit();
		return abteilung.id;
	}

	addKlassenzuordnungen = async (data: List<Partial<AbteilungKlassenzuordnung>>, idAbteilung : number) : Promise<void> => {
		const result = await api.server.addAbteilungKlassenzuordnung(data, api.schema);
		await this.gotoDefaultView(idAbteilung);
		this.manager.addKlassenToAuswahl(result);
		this.commit();
	}

	deleteKlassenzuordnungen = async (ids: List<number>) : Promise<void> => {
		await api.server.deleteAbteilungKlassenzuordnung(ids, api.schema);
		this.manager.deleteKlassenzuordnungen(ids);
		this.commit();
	}

}
