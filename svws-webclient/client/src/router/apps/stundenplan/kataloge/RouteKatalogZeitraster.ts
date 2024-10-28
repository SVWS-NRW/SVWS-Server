import type { RouteParams, RouteLocationRaw, RouteLocationNormalized } from "vue-router";
import type { ZeitrasterAuswahlProps } from "~/components/stundenplan/kataloge/zeitraster/SZeitrasterAuswahlProps";
import type { RouteApp} from "../../RouteApp";
import type { LehrerListeEintrag} from "@core";
import { ArrayList, BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { RouteDataKatalogZeitraster } from "./RouteDataKatalogZeitraster";
import type { StundenplanZeitrasterPausenzeitProps } from "~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeitProps";
import { api } from "~/router/Api";
import { routeStundenplan } from "../RouteStundenplan";

const SZeitrasterAuswahl = () => import("~/components/stundenplan/kataloge/zeitraster/SZeitrasterAuswahl.vue")
const SStundenplanZeitrasterPausenzeit = () => import("~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeit.vue");

export class RouteKatalogZeitraster extends RouteNode<RouteDataKatalogZeitraster, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.zeitraster", "zeitraster", SStundenplanZeitrasterPausenzeit, new RouteDataKatalogZeitraster());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		super.setView("eintraege", SZeitrasterAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
	}

	public getAuswahlProps(to: RouteLocationNormalized): ZeitrasterAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanZeitrasterPausenzeitProps {
		return {
			schulform: api.schulform,
			stundenplanManager: () => this.data.stundenplanManager,
			listLehrer: new ArrayList<LehrerListeEintrag>(),
			patchPausenzeit: async () => {}, // this.data.patchPausenzeit,
			removePausenzeiten: async () => {}, // this.data.removePausenzeiten,
			importPausenzeiten: async () => {}, // this.data.importPausenzeiten,
			patchZeitraster: this.data.patchZeitraster,
			addZeitraster: this.data.addZeitraster,
			removeZeitraster: this.data.removeZeitraster,
			importZeitraster: undefined, // this.data.importZeitraster,
			selected: this.data.selected,
			setSelection: this.data.setSelection,
			setSettingsDefaults: routeStundenplan.data.setSettingsDefaults,
		};
	}

}

export const routeKatalogZeitraster = new RouteKatalogZeitraster();
