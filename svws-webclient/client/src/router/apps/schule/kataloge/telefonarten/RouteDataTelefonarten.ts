import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeTelefonartenGruppenprozesse } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenGruppenprozesse";
import { routeTelefonartenNeu } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenNeu";
import { routeTelefonartenDaten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenDaten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Telefonart } from "@core/core/data/schule/Telefonart";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { TelefonartenListeManager } from "@ui/ui/manager/kataloge/TelefonartenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";
import { api } from "~/router/Api";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeTelefonartenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataTelefonarten extends RouteDataAuswahl<TelefonartenListeManager, RouteStateAuswahlInterface<TelefonartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeTelefonartenGruppenprozesse, hinzufuegen: routeTelefonartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<TelefonartenListeManager>>> {
		const telefonarten = await api.server.getTelefonarten(api.schema);
		const manager = new TelefonartenListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, telefonarten);
		return { manager };
	}

	async ladeDaten(auswahl: Telefonart | null): Promise<Telefonart | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Telefonart>, id: number): Promise<boolean> {
		await api.server.patchTelefonart(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteTelefonarten(ids, api.schema);
	}

	add = async (partial: Partial<Telefonart>): Promise<void> => {
		const telefonart = await api.server.addTelefonart(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(telefonart.id);
	};

	protected deleteMessage(id: number, TelefonArt: Telefonart | null): string {
		return `Telefonart ${TelefonArt?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Telefonarten vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Telefonarten zum Löschen ausgewählt.');
		}
		if (!this.manager.idsOfReferencedTelefonarten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedTelefonarten());
		}
		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedTelefonarten(): string {
		let errorMessage = 'Die folgenden Telefonarten sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsOfReferencedTelefonarten) {
			const telefonart = this.manager.liste.get(id);
			if (telefonart) {
				errorMessage += `- ${telefonart.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}
}
