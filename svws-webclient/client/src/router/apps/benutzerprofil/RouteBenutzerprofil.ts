import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { BenutzerprofilAppProps } from "~/components/benutzerprofil/SBenutzerprofilAppProps";
import { routeApp, type RouteApp} from "~/router/apps/RouteApp";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteDataBenutzerprofil } from "~/router/apps/benutzerprofil/RouteDataBenutzerprofil";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";

const SBenutzerprofilApp = () => import("~/components/benutzerprofil/SBenutzerprofilApp.vue")

export class RouteBenutzerprofil extends RouteNode<RouteDataBenutzerprofil, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "benutzerprofil", "benutzerprofil", SBenutzerprofilApp, new RouteDataBenutzerprofil());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzerprofil";
		api.config.addElements([
			new ConfigElement("benutzerprofil.notifications.backticks", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (this.data.benutzerEMailDaten.id !== id)
			await this.data.ladeDaten();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } };
	}

	public getProps(to: RouteLocationNormalized): BenutzerprofilAppProps {
		return {
			benutzer: () => this.data.benutzer,
			mode: api.mode,
			patch: this.data.patch,
			benutzerEMailDaten: () => this.data.benutzerEMailDaten,
			patchBenutzerEMailDaten: this.data.patchBenutzerEMailDaten,
			patchPasswort: this.data.patchPasswort,
			aes: api.aes,
			backticks: () => this.data.backticks,
			setBackticks: this.data.setBackticks,
		};
	}

}

export const routeBenutzerprofil = new RouteBenutzerprofil();
