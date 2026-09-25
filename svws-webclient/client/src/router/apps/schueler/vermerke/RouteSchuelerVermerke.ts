import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ConfigElement } from "@ui/utils/Config";

import type { SchuelerVermerkeProps } from "~/components/schueler/vermerke/SSchuelerVermerkeProps";
import { api } from "~/router/Api";
import { type RouteSchueler, routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerVermerke } from "~/router/apps/schueler/vermerke/RouteDataSchuelerVermerke";
import { routeError } from "~/router/error/RouteError";
import { RouteNode } from "~/router/RouteNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";


const SSchuelerVermerke = () => import("~/components/schueler/vermerke/SSchuelerVermerke.vue");

export class RouteSchuelerVermerke extends RouteNode<RouteDataSchuelerVermerke, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN], "schueler.vermerke", "vermerke", SSchuelerVermerke, new RouteDataSchuelerVermerke());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerke";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
		configStateImpl.config.addElements([
			new ConfigElement("schueler.vermerke.filterNurSichtbare", "user", "true"),
		]);
	}

	protected checkHidden(to_params?: RouteParams) {
		try {
			const { id } = (to_params !== undefined) ? RouteNode.getIntParams(to_params, ["id"]) : { id: undefined };
			if (id === undefined) {
				throw new DeveloperNotificationException("Fehler: Die Parameter der Route sind nicht gültig gesetzt.");
			}
			const schuelerAuswahlState = useSchuelerAuswahlState();
			return schuelerAuswahlState.manager.hasDaten() ? false : routeSchueler.getRouteDefaultChild({ id });
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			if (this.parent === undefined) {
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			}
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined) {
				await this.data.ladeDaten(null);
			} else {
				const schuelerAuswahlState = useSchuelerAuswahlState();
				await this.data.ladeDaten(schuelerAuswahlState.manager.liste.get(id));
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerVermerkeProps {
		return {
			schuelerVermerke: () => this.data.schuelerVermerke,
			patch: this.data.patch,
			add: this.data.add,
			remove: this.data.remove,
			apiStatus: api.status,
			filterNurSichtbare: this.data.filterNurSichtbare,
			setFilterNurSichtbare: this.data.setFilterNurSichtbare,
		};
	}

}

export const routeSchuelerVermerke = new RouteSchuelerVermerke();

