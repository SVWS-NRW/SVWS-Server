import type { RouteParamsRawGeneric } from "vue-router";
import { api } from "~/router/Api";
import { routeOrteGruppenprozesse } from "~/router/apps/schule/kataloge/orte/RouteOrteGruppenprozesse";
import { routeOrteNeu } from "~/router/apps/schule/kataloge/orte/RouteOrteNeu";
import { routeOrteDaten } from "~/router/apps/schule/kataloge/orte/RouteOrteDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { OrteListeManager } from "@ui/ui/manager/kataloge/OrteListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new OrteListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeOrteDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataOrte extends RouteDataAuswahl<OrteListeManager, RouteStateAuswahlInterface<OrteListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeOrteGruppenprozesse, hinzufuegen: routeOrteNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<OrteListeManager>>> {
		// TODO refactor katalog to work with orteState
		const orte = await api.server.getOrte(api.schema);
		const manager = new OrteListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, orte);
		return { manager };
	}

	ladeDaten(auswahl: Promise<OrtKatalogEintrag>): Promise<OrtKatalogEintrag> {
		return auswahl;
	}

	protected async doPatch(data: Partial<OrtKatalogEintrag>, id: number): Promise<boolean> {
		await api.server.patchOrt(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteOrte(ids, api.schema);
	}

	add = async (data: Partial<OrtKatalogEintrag>): Promise<void> => {
		const result = await api.server.addOrt(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, ort: OrtKatalogEintrag | null): string {
		return `Ort ${ort?.ortsname ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Orten vor.');
		}

		const idsOfReferencedOrte = this.manager.idsReferencedOrte;
		if (!idsOfReferencedOrte.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedOrte(idsOfReferencedOrte));
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedOrte(idsOfReferencedOrte: JavaSet<number>): string {
		let errorMessage = 'Die folgenden Orte sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of idsOfReferencedOrte) {
			const ort = this.manager.liste.get(id);
			if (ort) {
				errorMessage += `- ${ort.plz} ${ort.ortsname} \n`;
			}
		}
		return errorMessage;
	}

}
