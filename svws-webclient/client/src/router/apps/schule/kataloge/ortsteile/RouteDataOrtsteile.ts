import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { JavaSet, List, OrtsteilKatalogEintrag, SimpleOperationResponse } from "@core";
import { ArrayList, BenutzerKompetenz } from "@core";
import { OrtsteileListeManager, ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeOrtsteileGruppenprozesse } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileGruppenprozesse";
import { routeOrtsteileNeu } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileNeu";
import { routeOrtsteileDaten } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileDaten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new OrtsteileListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeOrtsteileDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataOrtsteile extends RouteDataAuswahl<OrtsteileListeManager, RouteStateAuswahlInterface<OrtsteileListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeOrtsteileGruppenprozesse, hinzufuegen: routeOrtsteileNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<OrtsteileListeManager>>> {
		// TODO refactor katalog to work with orteState
		const [ortsteile, orte] = await Promise.all([
			api.server.getOrtsteile(api.schema),
			api.server.getOrte(api.schema),
		]);
		const manager = new OrtsteileListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id,
			abschnittStateImpl.alle, schuleStateImpl.schulform, ortsteile, orte);
		return { manager };
	}

	ladeDaten(auswahl: any): Promise<any> {
		return auswahl;
	}

	protected async doPatch(data: Partial<OrtsteilKatalogEintrag>, id: number): Promise<boolean> {
		await api.server.patchOrtsteil(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteOrtsteile(ids, api.schema);
	}

	add = async (data: Partial<OrtsteilKatalogEintrag>): Promise<void> => {
		const result = await api.server.addOrtsteil(data, api.schema);
		// zum Anzeigen in der Auswahlliste nach dem Add
		result.plzOrt = data.plzOrt ?? '-';
		result.bezeichnungOrt = data.bezeichnungOrt ?? '-';
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, ortsteil: OrtsteilKatalogEintrag | null): string {
		return `Ortsteil ${ortsteil?.ortsteil ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Ortsteilen vor.');
		}

		const idsOfReferencedOrtsteile = this.manager.idsOfReferencedOrtsteile;
		if (!idsOfReferencedOrtsteile.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedOrtsteile(idsOfReferencedOrtsteile));
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedOrtsteile(idsOfReferencedOrtsteile: JavaSet<number>): string {
		let errorMessage = 'Die folgenden Ortsteile sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of idsOfReferencedOrtsteile) {
			const ortsteil = this.manager.liste.get(id);
			if (ortsteil) {
				errorMessage += `- ${ortsteil.ortsteil} \n`;
			}
		}
		return errorMessage;
	}

}
