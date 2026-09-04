import type { RouteParamsRawGeneric } from "vue-router";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeBetriebsartenGruppenprozesse } from "./RouteBetriebsartenGruppenprozesse";
import { routeBetriebsartenNeu } from "./RouteBetriebsartenNeu";
import { routeBetriebsartenDaten } from "./RouteBetriebsartenDaten";
import { api } from "~/router/Api";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { BetriebsartenListeManager } from "@ui/ui/manager/kataloge/BetriebsartenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeBetriebsartenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataBetriebsarten extends RouteDataAuswahl<BetriebsartenListeManager, RouteStateAuswahlInterface<BetriebsartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeBetriebsartenGruppenprozesse, hinzufuegen: routeBetriebsartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<BetriebsartenListeManager>>> {
		const betriebsarten = await api.server.getBetriebsarten(api.schema);
		const manager = new BetriebsartenListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, betriebsarten);
		return { manager };
	}

	async ladeDaten(auswahl: Betriebsart | null): Promise<Betriebsart | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Betriebsart>, id: number): Promise<boolean> {
		await api.server.patchBetriebsart(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteBetriebsarten(ids, api.schema);
	}

	add = async (partial: Partial<Betriebsart>): Promise<void> => {
		const betriebsart = await api.server.addBetriebsart(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(betriebsart.id);
	};

	protected deleteMessage(id: number, betriebsart: Betriebsart | null): string {
		return `Betriebsart ${betriebsart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Betriebsarten vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Betriebsarten zum Löschen ausgewählt.');
		}
		if (!this.manager.idsOfReferencedBetriebsarten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedBetriebsarten());
		}
		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedBetriebsarten(): string {
		let errorMessage = 'Die folgenden Betriebsarten sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsOfReferencedBetriebsarten) {
			const betriebsart = this.manager.liste.get(id);
			if (betriebsart) {
				errorMessage += `- ${betriebsart.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
