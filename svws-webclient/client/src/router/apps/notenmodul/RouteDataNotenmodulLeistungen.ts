import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeNotenmodulLeistungenData } from "./RouteNotenmodulLeistungenData";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";
import type { ENMv2Lerngruppe } from "@core/core/data/enm/v2/ENMv2Lerngruppe";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { UnsupportedOperationException } from "@core/java/lang/UnsupportedOperationException";
import type { List } from "@core/java/util/List";
import { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";


interface RouteStateNotenmodulLeistungen extends RouteStateAuswahlInterface<EnmLerngruppenAuswahlListeManager> {
}


export class RouteDataNotenmodulLeistungen extends RouteDataAuswahl<EnmLerngruppenAuswahlListeManager, RouteStateNotenmodulLeistungen> {

	public constructor() {
		super(<RouteStateNotenmodulLeistungen>{
			idSchuljahresabschnitt: -1,
			manager: undefined,
			view: routeNotenmodulLeistungenData,
			activeViewType: ViewType.DEFAULT,
			auswahl: [],
		}, { });
	}

	public async entferneDaten() {
		this.setPatchedState({
			idSchuljahresabschnitt: -1,
			manager: undefined,
		});
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateNotenmodulLeistungen>> {
		const manager = new EnmLerngruppenAuswahlListeManager(notenmodulStateImpl.manager, schuleStateImpl.abschnitt.id,
			schuleStateImpl.abschnitt.id, abschnittStateImpl.alle, schuleStateImpl.schulform);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: ENMv2Lerngruppe, state: Partial<RouteStateNotenmodulLeistungen>): Promise<ENMv2Lerngruppe | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ENMv2Lerngruppe>, id: number): Promise<boolean> {
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.");
	}

	protected deleteMessage(id: number, eintrag: any): string {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.");
	}

	get columnsVisible(): Map<string, boolean | null> {
		const config = JSON.parse(configStateImpl.config.getValue("notenmodul.leistungen.table.columns"));
		if (config === null) {
			return notenmodulStateImpl.manager.spalten.mapSpaltenLeistungen;
		}
		return new Map<string, boolean | null>(config);
	}

	setColumnsVisible = async (value: Map<string, boolean | null>) => {
		await configStateImpl.config.setValue('notenmodul.leistungen.table.columns', JSON.stringify([...value]));
	};

}
