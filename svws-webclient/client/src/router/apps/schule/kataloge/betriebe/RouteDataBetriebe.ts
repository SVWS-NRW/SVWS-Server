import type { RouteParamsRawGeneric } from "vue-router";
import type { Betrieb, BetriebeAnsprechpartner, JavaSet, List, SimpleOperationResponse } from "@core";
import { ArrayList, BenutzerKompetenz } from "@core";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { BetriebeListeManager } from "../../../../../../../ui/src/ui/manager/kataloge/BetriebeListeManager";
import { routeBetriebeGruppenprozesse } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeGruppenprozesse";
import { routeBetriebeNeu } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeNeu";
import { routeBetriebeDaten } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new BetriebeListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList(), new ArrayList()),
	view: routeBetriebeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataBetriebe extends RouteDataAuswahl<BetriebeListeManager, RouteStateAuswahlInterface<BetriebeListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeBetriebeGruppenprozesse, hinzufuegen: routeBetriebeNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<BetriebeListeManager>>> {
		const [betriebe, betriebsarten, orte] = await Promise.all([
			api.server.getBetriebeNeu(api.schema),
			api.server.getBetriebsarten(api.schema),
			api.server.getOrte(api.schema),
		]);

		const manager = new BetriebeListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, betriebe, betriebsarten, orte);
		return { manager };
	}

	ladeDaten(auswahl: any): Promise<any> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Betrieb>, id: number): Promise<void> {
		await api.server.patchBetriebeNeu(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteBetriebeNeu(ids, api.schema);
	}

	add = async (data: Partial<Betrieb>): Promise<void> => {
		const result = await api.server.addBetriebNeu(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, betrieb: Betrieb | null): string {
		return `Betrieb ${betrieb?.name ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Betrieben vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde kein Betrieb zum Löschen ausgewählt.');
		}

		const idsOfReferencedBetriebe = this.manager.idsOfReferencedBetriebe;
		if (!idsOfReferencedBetriebe.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedBetriebe(idsOfReferencedBetriebe));
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedBetriebe(idsOfReferencedBetriebe: JavaSet<number>): string {
		let errorMessage = 'Die folgenden Betriebe sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of idsOfReferencedBetriebe) {
			const betrieb = this.manager.liste.get(id);
			if (betrieb) {
				errorMessage += `- ${betrieb.name} \n`;
			}
		}
		return errorMessage;
	}

	addAnsprechpartner = async (data: Partial<BetriebeAnsprechpartner>): Promise<void> => {
		const result = await api.server.addBetriebeAnsprechpartnerNeu(data, api.schema);
		this.manager.addAnsprechpartner(result);
		this.commit();
	};

	deleteAnsprechpartner = async (ids: List<number>): Promise<void> => {
		await api.server.deleteBetriebeAnsprechpartnerNeu(ids, api.schema);
		for (const id of ids) {
			this.manager.deleteAnsprechpartner(id);
		}
		this.commit();
	};

	patchAnsprechpartner = async (id: number, data: Partial<BetriebeAnsprechpartner>): Promise<void> => {
		await api.server.patchBetriebeAnsprechpartnerNeu(data, api.schema, id);
		this.manager.patchAnsprechpartner(id, data);
		this.commit();
	};


}
