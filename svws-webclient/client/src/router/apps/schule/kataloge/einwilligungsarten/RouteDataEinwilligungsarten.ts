import { api } from "~/router/Api";
import { routeEinwilligungsartenDaten } from "./RouteEinwilligungsartenDaten";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeEinwilligungsartenGruppenprozesse } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenGruppenprozesse";
import { routeEinwilligungsartenNeu } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenNeu";
import type { RouteParamsRawGeneric } from "vue-router";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new EinwilligungsartenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeEinwilligungsartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataEinwilligungsarten extends RouteDataAuswahl<EinwilligungsartenListeManager, RouteStateAuswahlInterface<EinwilligungsartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeEinwilligungsartenGruppenprozesse, hinzufuegen: routeEinwilligungsartenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<EinwilligungsartenListeManager>>> {
		const einwilligungsarten = await api.server.getEinwilligungsarten(api.schema);
		const manager = new EinwilligungsartenListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, einwilligungsarten);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	async ladeDaten(auswahl: Einwilligungsart): Promise<Einwilligungsart> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Einwilligungsart>, id: number): Promise<boolean> {
		await api.server.patchEinwilligungsart(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteEinwilligungsarten(ids, api.schema);
	}

	add = async (data: Partial<Einwilligungsart>): Promise<void> => {
		const res = await api.server.createEinwilligungsart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Einwilligungsarten vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Einwilligungsarten zum Löschen ausgewählt.');
		}

		if (!this.manager.getIdsReferencedEinwilligungsarten().isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedEinwilligungsarten());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedEinwilligungsarten(): string {
		let errorMessage = 'Die folgenden Einwilligungsarten sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.getIdsReferencedEinwilligungsarten()) {
			const einwilligungsart = this.manager.liste.get(id);
			if (einwilligungsart) {
				errorMessage += `- ${einwilligungsart.bezeichnung}: ${einwilligungsart.schluessel} \n`;
			}
		}
		return errorMessage;
	}

	protected deleteMessage(id: number, einwilligungsart: Einwilligungsart | null): string {
		return `Einwilligungsart ${einwilligungsart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
