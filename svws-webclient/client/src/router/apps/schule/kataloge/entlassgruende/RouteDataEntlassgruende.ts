import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, SimpleOperationResponse, KatalogEntlassgrund } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType, EntlassgruendeListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeEntlassgruendeDaten } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeDaten";
import { routeEntlassgruendeGruppenprozesse } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeGruppenprozesse";
import { routeEntlassgruendeNeu } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new EntlassgruendeListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeEntlassgruendeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataEntlassgruende extends RouteDataAuswahl<EntlassgruendeListeManager, RouteStateAuswahlInterface<EntlassgruendeListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeEntlassgruendeGruppenprozesse, hinzufuegen: routeEntlassgruendeNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<EntlassgruendeListeManager>>> {
		const entlassgruende = await api.server.getEntlassgruende(api.schema);
		const manager = new EntlassgruendeListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, entlassgruende);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<KatalogEntlassgrund> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchEntlassgrund(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteEntlassgruende(ids, api.schema);
	}

	protected deleteMessage(id: number, entlassgrund: KatalogEntlassgrund | null): string {
		return `Entlassgrund ${entlassgrund?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addEntlassgrund = async (data: Partial<KatalogEntlassgrund>): Promise<void> => {
		const entlassgrund = await api.server.addEntlassgrund(data, api.schema);
		this.manager.liste.add(entlassgrund);
		this.commit();
		await this.gotoDefaultView(entlassgrund.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Entlassgründen vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde kein Entlassgrund zum Löschen ausgewählt.');
		}

		if (!this.manager.getIdsReferencedEntlassgruende().isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedEntlassgründe());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedEntlassgründe(): string {
		let errorMessage = 'Die folgenden Entlassgründen sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.getIdsReferencedEntlassgruende()) {
			const entlassgrund = this.manager.liste.get(id);
			if (entlassgrund) {
				errorMessage += `- ${entlassgrund.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}

