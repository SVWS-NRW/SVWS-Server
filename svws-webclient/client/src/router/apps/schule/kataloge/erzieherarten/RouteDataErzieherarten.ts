import { api } from "~/router/Api";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeErzieherartenDaten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenDaten";
import { routeErzieherartenGruppenprozesse } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenGruppenprozesse";
import { routeErzieherartenNeu } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { ErzieherartListeManager } from "@ui/ui/manager/kataloge/ErzieherartListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new ErzieherartListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeErzieherartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataErzieherarten extends RouteDataAuswahl<ErzieherartListeManager, RouteStateAuswahlInterface<ErzieherartListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeErzieherartenGruppenprozesse, hinzufuegen: routeErzieherartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<ErzieherartListeManager>>> {
		const erzieherarten = await api.server.getErzieherArten(api.schema);
		const manager = new ErzieherartListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle, schuleStateImpl.schulform, erzieherarten);
		return { manager };
	}

	async ladeDaten(auswahl: Erzieherart | null): Promise<Erzieherart | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Erzieherart>, id: number): Promise<boolean> {
		await api.server.patchErzieherart(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteErzieherarten(ids, api.schema);
	}

	add = async (data: Partial<Erzieherart>): Promise<void> => {
		const res = await api.server.addErzieherart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	};

	protected deleteMessage(id: number, erzieherart: Erzieherart | null): string {
		return `Erzieherart ${erzieherart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Erzieherarten vor.');
		}

		const idsOfReferencedErzieherarten = this.manager.idsReferencedErzieherarten;
		if (!idsOfReferencedErzieherarten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedErzieherarten(idsOfReferencedErzieherarten));
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedErzieherarten(idsOfReferencedErzieherarten: JavaSet<number>): string {
		let errorMessage = 'Die folgenden Erzieherarten sind an anderer Stelle referenziert: \n\n';
		for (const id of idsOfReferencedErzieherarten) {
			const erzieherart = this.manager.liste.get(id);
			if (erzieherart) {
				errorMessage += `- ${erzieherart.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}
}
