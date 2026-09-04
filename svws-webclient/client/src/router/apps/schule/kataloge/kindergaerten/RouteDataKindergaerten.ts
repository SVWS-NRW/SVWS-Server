import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { api } from "~/router/Api";
import { routeKindergaertenGruppenprozesse } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaertenGruppenprozesse";
import { routeKindergaertenNeu } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaertenNeu";
import { routeKindergaertenDaten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaertenDaten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { KindergaertenListeManager } from "@ui/ui/manager/kataloge/KindergaertenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new KindergaertenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeKindergaertenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataKindergaerten extends RouteDataAuswahl<KindergaertenListeManager, RouteStateAuswahlInterface<KindergaertenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKindergaertenGruppenprozesse, hinzufuegen: routeKindergaertenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<KindergaertenListeManager>>> {
		const kindergaerten = await api.server.getKindergaerten(api.schema);
		const manager = new KindergaertenListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id,
			abschnittStateImpl.alle, schuleStateImpl.schulform, kindergaerten);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	async ladeDaten(auswahl: Kindergarten): Promise<Kindergarten> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Kindergarten>, id: number): Promise<boolean> {
		await api.server.patchKindergarten(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKindergaerten(ids, api.schema);
	}

	add = async (data: Partial<Kindergarten>): Promise<void> => {
		const result = await api.server.addKindergarten(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Kindergärten vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Kindergärten zum Löschen ausgewählt.');
		}
		if (!this.manager.idsOfReferencedKindergaerten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedKindergaerten());
		}
		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedKindergaerten(): string {
		let errorMessage = 'Die folgenden Kindergärten sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsOfReferencedKindergaerten) {
			const kindergarten = this.manager.liste.get(id);
			if (kindergarten) {
				errorMessage += `- ${kindergarten.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

	protected deleteMessage(id: number, kindergarten: Kindergarten | null): string {
		return `Kindergarten ${kindergarten?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

}
