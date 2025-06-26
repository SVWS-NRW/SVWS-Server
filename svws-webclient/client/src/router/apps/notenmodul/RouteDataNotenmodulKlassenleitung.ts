import type { SimpleOperationResponse} from "@core";
import { UnsupportedOperationException, type ENMKlasse, type List } from "@core";
import { EnmKlassenleitungAuswahlListeManager, ViewType } from "@ui";
import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeNotenmodulKlassenleitungData } from "./RouteNotenmodulKlassenleitungData";
import { routeNotenmodul } from "./RouteNotenmodul";
import type { RouteParamsRawGeneric } from "vue-router";


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
		const manager = new EnmKlassenleitungAuswahlListeManager(routeNotenmodul.data.manager, api.schuleStammdaten.idSchuljahresabschnitt,
			api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: ENMKlasse, state: Partial<RouteStateNotenmodulKlassenleitung>): Promise<ENMKlasse | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ENMKlasse>, id: number): Promise<void> {
		// nothing to do
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.")
	}

	protected deleteMessage(id: number, eintrag: any): string {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.")
	}

	get columnsVisible(): Map<string, boolean|null> {
		return new Map<string, boolean|null>(JSON.parse(api.config.getValue("notenmodul.klassenleitung.table.columns")));
	}

	setColumnsVisible = async (value: Map<string, boolean|null>) => {
		await api.config.setValue('notenmodul.klassenleitung.table.columns', JSON.stringify([...value]));
	}

	get floskelEditorVisible(): boolean {
		return (api.config.getValue("notenmodul.klassenleitung.floskelEditorVisible") === 'true');
	}

	setFloskelEditorVisible = async (value: boolean) => {
		await api.config.setValue('notenmodul.klassenleitung.floskelEditorVisible', value ? 'true' : 'false');
	}

}
