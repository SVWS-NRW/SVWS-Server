import type { RouteParamsRawGeneric } from "vue-router";
import { api } from "~/router/Api";
import { routeBetriebeGruppenprozesse } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeGruppenprozesse";
import { routeBetriebeNeu } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeNeu";
import { routeBetriebeDaten } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Betrieb } from "@core/core/data/schule/Betrieb";
import type { BetriebeAnsprechpartner } from "@core/core/data/schule/BetriebeAnsprechpartner";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { BetriebeListeManager } from "@ui/ui/manager/kataloge/BetriebeListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new BetriebeListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList(), new ArrayList()),
	view: routeBetriebeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataBetriebe extends RouteDataAuswahl<BetriebeListeManager, RouteStateAuswahlInterface<BetriebeListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeBetriebeGruppenprozesse, hinzufuegen: routeBetriebeNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<BetriebeListeManager>>> {
		// TODO refactor katalog to work with orteState, ...
		const [betriebe, betriebsarten, orte] = await Promise.all([
			api.server.getBetriebe(api.schema),
			api.server.getBetriebsarten(api.schema),
			api.server.getOrte(api.schema),
		]);

		const manager = new BetriebeListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, betriebe, betriebsarten, orte);
		return { manager };
	}

	ladeDaten(auswahl: Promise<Betrieb>): Promise<Betrieb> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Betrieb>, id: number): Promise<boolean> {
		await api.server.patchBetrieb(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteBetriebe(ids, api.schema);
	}

	add = async (data: Partial<Betrieb>): Promise<void> => {
		const result = await api.server.addBetrieb(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, betrieb: Betrieb | null): string {
		return `Betrieb ${betrieb?.name ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Betrieben vor.');
		}

		const idsOfReferencedBetriebe = this.manager.idsOfReferencedBetriebe;
		if (!idsOfReferencedBetriebe.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedBetriebe(idsOfReferencedBetriebe));
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedBetriebe(idsOfReferencedBetriebe: JavaSet<number>): string {
		let errorMessage = 'Die folgenden Betriebe sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of idsOfReferencedBetriebe) {
			const betrieb = this.manager.liste.get(id);
			if (betrieb) {
				errorMessage += `- ${betrieb.name} \n`;
			}
		}
		return errorMessage;
	}

	addAnsprechpartner = async (data: Partial<BetriebeAnsprechpartner>): Promise<void> => {
		const result = await api.server.addBetriebAnsprechpartner(data, api.schema);
		this.manager.addAnsprechpartner(result);
		this.commit();
	};

	deleteAnsprechpartner = async (ids: List<number>): Promise<void> => {
		await api.server.deleteBetriebAnsprechpartner(ids, api.schema);
		for (const id of ids) {
			this.manager.deleteAnsprechpartner(id);
		}
		this.commit();
	};

	patchAnsprechpartner = async (data: Partial<BetriebeAnsprechpartner>): Promise<boolean> => {
		if (data.id === undefined) {
			return false;
		}
		const { id, ...partial } = data;
		await api.server.patchBetriebAnsprechpartner(partial, api.schema, data.id);
		this.manager.patchAnsprechpartner(data);
		this.commit();
		return true;
	};

}
