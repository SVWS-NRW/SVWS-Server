import type { SchuelerEinwilligung } from "@core/core/data/schueler/SchuelerEinwilligung";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { api } from "~/router/Api";
import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";


interface RouteStateSchuelerEinwilligungen extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	einwilligungen: List<SchuelerEinwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
}

const defaultState = <RouteStateSchuelerEinwilligungen>{
	auswahl: undefined,
	einwilligungen: new ArrayList(),
	mapEinwilligungsarten: new Map(),
};

export class RouteDataSchuelerEinwilligungen extends RouteData<RouteStateSchuelerEinwilligungen> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu Vermerk-Daten abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	get einwilligungen(): List<SchuelerEinwilligung> {
		return this._state.value.einwilligungen;
	}

	get mapEinwilligungsarten(): Map<number, Einwilligungsart> {
		return this._state.value.mapEinwilligungsarten;
	}

	patch = async (data: Partial<SchuelerEinwilligung> | undefined, idEinwilligungsart: number) => {
		if (data === undefined) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		await api.server.patchSchuelerEinwilligung(data, api.schema, this.auswahl.id, idEinwilligungsart);
		for (const einwilligung of this.einwilligungen) {
			if (einwilligung.idEinwilligungsart === idEinwilligungsart) {
				Object.assign(einwilligung, data);
			}
		}
		this.commit();
		return true;
	};

	public async ladeDaten(auswahl: SchuelerListeEintrag | null | undefined) {
		if ((auswahl === null) || (auswahl === undefined)) {
			this.setPatchedDefaultState({});
		} else {
			const einwilligungen = await api.server.getSchuelerEinwilligungen(api.schema, auswahl.id);
			const einwilligungsArten = await api.server.getEinwilligungsarten(api.schema);
			const mapEinwilligungsarten = new Map();
			for (const ea of einwilligungsArten) {
				mapEinwilligungsarten.set(ea.id, ea);
			}
			this.setPatchedDefaultState({ auswahl, einwilligungen, mapEinwilligungsarten });
		}
	}

}

