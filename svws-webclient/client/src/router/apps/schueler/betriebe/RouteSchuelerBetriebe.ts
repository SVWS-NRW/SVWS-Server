import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerBetriebe } from "~/router/apps/schueler/betriebe/RouteDataSchuelerBetriebe";
import { routeError } from "~/router/error/RouteError";
import type { SchuelerBetriebeProps } from "~/components/schueler/betriebe/SchuelerBetriebeProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuelerBetriebe = () => import("~/components/schueler/betriebe/SchuelerBetriebe.vue");

export class RouteSchuelerBetriebe extends RouteNode<RouteDataSchuelerBetriebe, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN],
			"schueler.betriebe", "betriebe", SchuelerBetriebe, new RouteDataSchuelerBetriebe());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betriebe";
		super.setCheckpoint = true;
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id !== undefined) {
				await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(_: RouteLocationNormalized): SchuelerBetriebeProps {
		return {
			manager: () => this.data.manager,
			add: this.data.add,
			patch: this.data.patch,
			deleteBetriebe: this.data.delete,
			goToBetrieb: routeSchuelerBetriebe.data.goToBetrieb,
		};
	}

}

export const routeSchuelerBetriebe = new RouteSchuelerBetriebe();

