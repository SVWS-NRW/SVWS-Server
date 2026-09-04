import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { api } from "~/router/Api";
import { routeHaltestellenDaten } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellenDaten";
import { routeHaltestellenGruppenprozesse } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellenGruppenprozesse";
import { routeHaltestellenNeu } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellenNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { HaltestellenListeManager } from "@ui/ui/manager/kataloge/HaltestellenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new HaltestellenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeHaltestellenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataHaltestellen extends RouteDataAuswahl<HaltestellenListeManager, RouteStateAuswahlInterface<HaltestellenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeHaltestellenGruppenprozesse, hinzufuegen: routeHaltestellenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<HaltestellenListeManager>>> {
		const haltestellen = await api.server.getHaltestellen(api.schema);
		const manager = new HaltestellenListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, haltestellen);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: Promise<Haltestelle>): Promise<Haltestelle> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Haltestelle>, id: number): Promise<boolean> {
		await api.server.patchHaltestelle(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteHaltestellen(ids, api.schema);
	}

	protected deleteMessage(id: number, haltestelle: Haltestelle | null): string {
		return `Haltestelle ${haltestelle?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	add = async (data: Partial<Haltestelle>): Promise<void> => {
		const result = await api.server.addHaltestelle(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Haltestellen vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Haltestellen zum Löschen ausgewählt.');
		}
		if (!this.manager.idsReferencedHaltestellen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedHaltestellen());
		}
		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedHaltestellen(): string {
		let errorMessage = 'Die folgenden Haltestellen sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsReferencedHaltestellen) {
			const haltestelle = this.manager.liste.get(id);
			if (haltestelle) {
				errorMessage += `- ${haltestelle.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}
}

