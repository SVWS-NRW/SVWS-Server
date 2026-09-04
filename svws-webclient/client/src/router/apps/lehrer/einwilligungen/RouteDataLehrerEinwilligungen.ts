import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { api } from "~/router/Api";
import type { LehrerEinwilligung } from "@core/core/data/lehrer/LehrerEinwilligung";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

interface RouteStateLehrerEinwilligungen extends RouteStateInterface {
	auswahl: LehrerListeEintrag | undefined;
	einwilligungen: List<LehrerEinwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
}

const defaultState = <RouteStateLehrerEinwilligungen>{
	auswahl: undefined,
	einwilligungen: new ArrayList(),
	mapEinwilligungsarten: new Map(),
};

export class RouteDataLehrerEinwilligungen extends RouteData<RouteStateLehrerEinwilligungen> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): LehrerListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Lehrerauswahl nicht festgelegt, es können keine Informationen zu Lehrereinwilligungen abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	get einwilligungen(): List<LehrerEinwilligung> {
		return this._state.value.einwilligungen;
	}

	get mapEinwilligungsarten(): Map<number, Einwilligungsart> {
		return this._state.value.mapEinwilligungsarten;
	}

	patch = async (data: Partial<LehrerEinwilligung> | undefined, idEinwilligungsart: number) => {
		if (data === undefined) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		api.status.start();

		await api.server.patchLehrerEinwilligung(data, api.schema, this.auswahl.id, idEinwilligungsart);

		for (const einwilligung of this.einwilligungen) {
			if (einwilligung.idEinwilligungsart === idEinwilligungsart) {
				Object.assign(einwilligung, data);
			}
		}
		this.commit();
		api.status.stop();
	};

	public async ladeDaten(auswahl: LehrerListeEintrag | null | undefined) {
		if ((auswahl === null) || (auswahl === undefined)) {
			this.setPatchedDefaultState({});
		} else {
			const einwilligungen = await api.server.getLehrerEinwilligungen(api.schema, auswahl.id);
			const einwilligungsArten = await api.server.getEinwilligungsarten(api.schema);
			const mapEinwilligungsarten = new Map();
			for (const ea of einwilligungsArten) {
				mapEinwilligungsarten.set(ea.id, ea);
			}
			this.setPatchedDefaultState({ auswahl, einwilligungen, mapEinwilligungsarten });
		}
	}

}

