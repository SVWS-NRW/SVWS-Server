import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import type { List , Lernplattform , LehrerLernplattform, LehrerListeEintrag} from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";


interface RouteStateLehrerLernplattformen extends RouteStateInterface {
	auswahl: LehrerListeEintrag | undefined;
	lehrerLernplattformen: List<LehrerLernplattform>;
	mapLernplattformen: Map<number, Lernplattform>;
}

const defaultState = <RouteStateLehrerLernplattformen>{
	auswahl: undefined,
	lehrerLernplattformen: new ArrayList(),
	mapLernplattformen: new Map(),
};

export class RouteDataLehrerLernplattformen extends RouteData<RouteStateLehrerLernplattformen> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): LehrerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Lehrerauswahl nicht festgelegt, es können keine Informationen zu Lernplattformen abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get lehrerLernplattformen(): List<LehrerLernplattform> {
		return this._state.value.lehrerLernplattformen;
	}

	get mapLernplattformen(): Map<number, Lernplattform> {
		return this._state.value.mapLernplattformen;
	}

	patch = async (data : Partial<LehrerLernplattform> | undefined, idLernplattform: number) => {
		if (data === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		api.status.start();

		await api.server.patchLehrerLernplattform(data, api.schema, this.auswahl.id, idLernplattform);

		for (const lernplattform of this.lehrerLernplattformen)
			if (lernplattform.idLernplattform === idLernplattform)
				Object.assign(lernplattform, data);
		this.commit();
		api.status.stop();
	}

	public async ladeDaten(auswahl: LehrerListeEintrag | null | undefined) {
		if ((auswahl === null) || (auswahl === undefined)) {
			this.setPatchedDefaultState({});
		} else {
			const lehrerLernplattformen = await api.server.getLehrerLernplattformen(api.schema, auswahl.id);
			const lernplattformen = await api.server.getLernplattformen(api.schema);
			const mapLernplattformen = new Map();
			for (const lp of lernplattformen)
				mapLernplattformen.set(lp.id, lp);
			this.setPatchedDefaultState({ auswahl, lehrerLernplattformen, mapLernplattformen });
		}
	}

}

