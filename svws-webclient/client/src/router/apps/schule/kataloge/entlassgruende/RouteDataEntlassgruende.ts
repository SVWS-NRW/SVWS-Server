import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { api } from "~/router/Api";
import { routeEntlassgruendeDaten } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeDaten";
import { routeEntlassgruendeGruppenprozesse } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeGruppenprozesse";
import { routeEntlassgruendeNeu } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { EntlassgruendeListeManager } from "@ui/ui/manager/kataloge/EntlassgruendeListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

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
		const manager = new EntlassgruendeListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, entlassgruende);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: Promise<KatalogEntlassgrund>): Promise<KatalogEntlassgrund> {
		return auswahl;
	}

	protected async doPatch(data: Partial<KatalogEntlassgrund>, id: number): Promise<boolean> {
		await api.server.patchEntlassgrund(data, api.schema, id);
		return true;
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

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Entlassgründen vor.');
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

