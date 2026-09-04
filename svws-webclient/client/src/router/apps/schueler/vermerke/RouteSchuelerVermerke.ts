import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerVermerke } from "~/router/apps/schueler/vermerke/RouteDataSchuelerVermerke";
import type { SchuelerVermerkeProps } from "~/components/schueler/vermerke/SSchuelerVermerkeProps";
import { api } from "~/router/Api";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ConfigElement } from "@ui/utils/Config";


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
			return routeSchueler.data.manager.hasDaten() ? false : routeSchueler.getRouteDefaultChild({ id });
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
				await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerVermerkeProps {
		return {
			schuelerVermerke: () => this.data.schuelerVermerke,
			mapVermerkArten: this.data.mapVermerkArten,
			patch: this.data.patch,
			add: this.data.add,
			remove: this.data.remove,
			apiStatus: api.status,
			autofocus: routeSchueler.data.autofocus,
			filterNurSichtbare: this.data.filterNurSichtbare,
			setFilterNurSichtbare: this.data.setFilterNurSichtbare,
		};
	}

}

export const routeSchuelerVermerke = new RouteSchuelerVermerke();

