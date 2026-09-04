import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerEinwilligungen } from "~/router/apps/schueler/einwilligungen/RouteDataSchuelerEinwilligungen";
import type { SchuelerEinwilligungenProps } from "~/components/schueler/einwilligungen/SchuelerEinwilligungenProps";
import { api } from "~/router/Api";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuelerEinwilligungen = () => import("~/components/schueler/einwilligungen/SchuelerEinwilligungen.vue");

export class RouteSchuelerEinwilligungen extends RouteNode<RouteDataSchuelerEinwilligungen, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN], "schueler.einwilligungen", "einwilligungen", SchuelerEinwilligungen, new RouteDataSchuelerEinwilligungen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungen";
	}

	public async update(_: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			if (this.parent === undefined) {
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			}
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined) {
				await this.data.ladeDaten(null);
			} else {
				await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(_: RouteLocationNormalized): SchuelerEinwilligungenProps {
		return {
			einwilligungen: () => this.data.einwilligungen,
			mapEinwilligungsarten: this.data.mapEinwilligungsarten,
			patch: this.data.patch,
			apiStatus: api.status,
		};
	}
}

export const routeSchuelerEinwilligungen = new RouteSchuelerEinwilligungen();

