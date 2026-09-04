import type { RouteParamsRawGeneric } from "vue-router";
import { api } from "~/router/Api";
import { routeSchulenDaten } from "./RouteSchulenDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeSchulenGruppenprozesse } from "~/router/apps/schule/kataloge/schulen/RouteSchulenGruppenprozesse";
import { routeSchulenNeu } from "~/router/apps/schule/kataloge/schulen/RouteSchulenNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { SchulenListeManager } from "@ui/ui/manager/kataloge/SchulenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new SchulenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeSchulenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataSchulen extends RouteDataAuswahl<SchulenListeManager, RouteStateAuswahlInterface<SchulenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeSchulenGruppenprozesse, hinzufuegen: routeSchulenNeu });
	}


	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<SchulenListeManager>>> {
		const schulen = await api.server.getSchulen(api.schema);
		const katalogSchulen = await api.server.getKatalogSchulen(api.schema);
		const manager = new SchulenListeManager(
			schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle, schuleStateImpl.schulform, schulen, katalogSchulen);
		return { manager };
	}

	public ladeDaten(auswahl: Promise<SchulEintrag>): Promise<SchulEintrag> {
		return auswahl;
	}

	protected async doPatch(data: Partial<SchulEintrag>, id: number): Promise<boolean> {
		await api.server.patchSchuleAusKatalog(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchulenVonKatalog(ids, api.schema);
	}

	add = async (data: Partial<SchulEintrag>): Promise<void> => {
		const schule = await api.server.addSchuleZuKatalog(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		this.manager.liste.add(schule);
		this.commit();
		await this.gotoDefaultView(schule.id);
	};

	deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schulen vor.');
		}

		if (!this.manager.idsReferencedSchulen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedSchulen());
		}

		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedSchulen(): string {
		let errorMessage = 'Die folgenden Schulen sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsReferencedSchulen) {
			const schule = this.manager.liste.get(id);
			if (schule) {
				errorMessage += `- ${schule.name} \n`;
			}
		}
		return errorMessage;
	}

	protected deleteMessage(id: number, schule: SchulEintrag | null): string {
		return `Schule ${schule?.kurzbezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}

