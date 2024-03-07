import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { BenutzerprofilAppProps } from "~/components/benutzerprofil/SBenutzerprofilAppProps";
import type { RouteApp} from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteDataBenutzerprofil } from "~/router/apps/benutzerprofil/RouteDataBenutzerprofil";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";

const SBenutzerprofilApp = () => import("~/components/benutzerprofil/SBenutzerprofilApp.vue")

export class RouteBenutzerprofil extends RouteNode<RouteDataBenutzerprofil, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "benutzerprofil", "/benutzerprofil", SBenutzerprofilApp, new RouteDataBenutzerprofil());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzerprofil";
		api.config.addElements([
			new ConfigElement("benutzerprofil.notifications.backticks", "user", "true"),
		]);
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getProps(to: RouteLocationNormalized): BenutzerprofilAppProps {
		return {
			benutzer: this.data.benutzer,
			patch: this.data.patch,
			patchPasswort: this.data.patchPasswort,
			backticks: () => this.data.backticks,
			setBackticks: this.data.setBackticks,
		};
	}

}

export const routeBenutzerprofil = new RouteBenutzerprofil();
