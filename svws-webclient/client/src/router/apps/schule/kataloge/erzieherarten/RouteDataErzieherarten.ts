import { BenutzerKompetenz, type JavaSet, type List, type SimpleOperationResponse, type Erzieherart } from "@core";
import { ArrayList } from "@core";
import { api } from "~/router/Api";
import { ViewType, ErzieherartListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeErzieherartenDaten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenDaten";
import { routeErzieherartenGruppenprozesse } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenGruppenprozesse";
import { routeErzieherartenNeu } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenNeu";

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
		const manager = new ErzieherartListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, erzieherarten);
		return { manager };
	}

	async ladeDaten(auswahl: Erzieherart | null): Promise<Erzieherart | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Erzieherart>, id: number): Promise<void> {
		await api.server.patchErzieherart(data, api.schema, id);
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

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Erzieherarten vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde keine Erzieherart zum Löschen ausgewählt.');
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
