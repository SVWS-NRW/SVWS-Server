import type { SimpleOperationResponse, ENMv2Klasse, List } from "@core";
import { UnsupportedOperationException } from "@core";
import { EnmKlassenleitungAuswahlListeManager, ViewType } from "@ui";
import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeNotenmodulKlassenleitungData } from "./RouteNotenmodulKlassenleitungData";
import { routeNotenmodul } from "./RouteNotenmodul";
import type { RouteParamsRawGeneric } from "vue-router";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";


interface RouteStateNotenmodulKlassenleitung extends RouteStateAuswahlInterface<EnmKlassenleitungAuswahlListeManager> {
}


export class RouteDataNotenmodulKlassenleitung extends RouteDataAuswahl<EnmKlassenleitungAuswahlListeManager, RouteStateNotenmodulKlassenleitung> {

	public constructor() {
		super(<RouteStateNotenmodulKlassenleitung>{
			idSchuljahresabschnitt: -1,
			manager: undefined,
			view: routeNotenmodulKlassenleitungData,
			activeViewType: ViewType.DEFAULT,
		}, { });
	}

	public async entferneDaten() {
		this.setPatchedState({
			idSchuljahresabschnitt: -1,
			manager: undefined,
		});
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateNotenmodulKlassenleitung>> {
		const manager = new EnmKlassenleitungAuswahlListeManager(routeNotenmodul.data.manager, schuleStateImpl.abschnitt.id,
			schuleStateImpl.abschnitt.id, abschnittStateImpl.alle, schuleStateImpl.schulform);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: ENMv2Klasse, state: Partial<RouteStateNotenmodulKlassenleitung>): Promise<ENMv2Klasse | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ENMv2Klasse>, id: number): Promise<boolean> {
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.");
	}

	protected deleteMessage(id: number, eintrag: any): string {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.");
	}

	get columnsVisible(): Map<string, boolean | null> {
		const config = JSON.parse(configStateImpl.config.getValue("notenmodul.klassenleitung.table.columns"));
		if (config === null) {
			return routeNotenmodul.data.manager.spalten.mapSpaltenKlassenleitung;
		}
		return new Map<string, boolean | null>(config);
	}

	setColumnsVisible = async (value: Map<string, boolean | null>) => {
		await configStateImpl.config.setValue('notenmodul.klassenleitung.table.columns', JSON.stringify([...value]));
	};

}
