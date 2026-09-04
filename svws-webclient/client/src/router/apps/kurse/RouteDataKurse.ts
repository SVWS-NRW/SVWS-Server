import { KursListeManager } from "~/states/kurse/KursListeManager";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { routeKurseNeu } from "./RouteKurseNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { KursLehrer } from "@core/asd/data/kurse/KursLehrer";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";

type RouteStateKurse = RouteStateAuswahlInterface<KursListeManager>;

const defaultState: RouteStateKurse = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeKursDaten,
};

export class RouteDataKurse extends RouteDataAuswahl<KursListeManager, RouteStateKurse> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKurseGruppenprozesse, hinzufuegen: routeKurseNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	get filterNurSichtbar(): boolean {
		return configStateImpl.config.getValue("kurse.auswahl.filterNurSichtbar") === 'true';
	}

	setFilterNurSichtbar = async (value: boolean) => {
		await configStateImpl.config.setValue('kurse.auswahl.filterNurSichtbar', value ? "true" : "false");
	};

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateKurse>> {
		const schuljahresabschnitt = abschnittStateImpl.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}
		// Lade die Kataloge und erstelle den Manager
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listFaecher = await api.server.getFaecher(api.schema);
		const manager = new KursListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, listKurse, listSchueler, listJahrgaenge, listLehrer, listFaecher);
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
			manager.setFilterNurSichtbar(this.filterNurSichtbar);
		} else {
			manager.useFilter(this._state.value.manager);
		}
		return { manager };
	}

	public async ladeDaten(auswahl: KursDaten | null): Promise<KursDaten | null> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		return auswahl;
	}

	protected async doPatch(data: Partial<KursDaten>, id: number): Promise<boolean> {
		await api.server.patchKurs(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKurse(ids, api.schema);
	}

	protected deleteMessage(id: number, kurs: KursDaten | null): string {
		return `Kurs ${kurs?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

	add = async (partialKurs: Partial<KursDaten>): Promise<void> => {
		const neuerKurs = await api.server.addKurs({ ...partialKurs, idSchuljahresabschnitt: abschnittStateImpl.auswahl.id }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(neuerKurs.id);
	};

	addKurLehrer = async (data: Partial<KursLehrer>, idKurs: number): Promise<void> => {
		api.status.start();
		const result = await api.server.addKursLehrer(data, api.schema, idKurs);
		this.manager.daten().weitereLehrer.add(result);
		this.commit();
		api.status.stop();
	};

	patchKursLehrer = async (data: Partial<KursLehrer>, idKurs: number, idLehrer: number): Promise<void> => {
		api.status.start();
		await api.server.patchKursLehrer(data, api.schema, idKurs, idLehrer);
		for (const k of this.manager.daten().weitereLehrer) {
			if (k.idLehrer === idLehrer) {
				Object.assign(k, data);
			}
		}
		this.commit();
		api.status.stop();
	};

	deleteKursLehrer = async (lehrerIds: List<number>, idKurs: number): Promise<void> => {
		await api.server.deleteKursLehrer(lehrerIds, api.schema, idKurs);
		const weitereLehrer = this.manager.daten().weitereLehrer;
		for (let i = weitereLehrer.size() - 1; i >= 0; i--) {
			if (lehrerIds.contains(weitereLehrer.get(i).idLehrer)) {
				weitereLehrer.removeElementAt(i);
			}
		}
		this.commit();
	};

	/* TODO
	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.manager.auswahl().id;
		await api.server.setKursSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
		await this.setEintrag(this.manager.liste.get(auswahl_id));
	}
	*/

}
