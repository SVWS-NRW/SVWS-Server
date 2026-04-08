import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { NutzereinstellungenAppProps } from "~/components/benutzerprofil/einstellungen/SNutzereinstellungenAppProps";
import type { RouteApp } from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteDataBenutzerprofilNutzereinstellungen } from "~/router/apps/benutzerprofil/nutzereinstellungen/RouteDataBenutzerprofilNutzereinstellungen";
import { api } from "~/router/Api";
import { RouteBenutzerprofilMenuGroup } from "~/router/apps/benutzerprofil/RouteBenutzerprofilMenuGroup";

const SBenutzerprofilApp = () => import("~/components/benutzerprofil/einstellungen/SNutzereinstellungenApp.vue");

export class RouteBenutzerprofilNutzereinstellungen extends RouteNode<RouteDataBenutzerprofilNutzereinstellungen, RouteApp> {

	public constructor() {
		super(
			Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"benutzerprofil.nutzereinstellungen",
			"benutzerprofil/nutzereinstellungen",
			SBenutzerprofilApp,
			new RouteDataBenutzerprofilNutzereinstellungen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nutzereinstellungen";
		super.menugroup = RouteBenutzerprofilMenuGroup.EINSTELLUNGEN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		const { id } = RouteNode.getIntParams(to_params, ["id"]);
		if (this.data.benutzerEMailDaten.id !== id) {
			await this.data.ladeDaten();
		}
	}

	public getProps(to: RouteLocationNormalized): NutzereinstellungenAppProps {
		return {
			benutzer: () => this.data.benutzer,
			benutzertyp: api.benutzertyp,
			mode: api.mode,
			patch: this.data.patch,
			benutzerEMailDaten: () => this.data.benutzerEMailDaten,
			patchBenutzerEMailDaten: this.data.patchBenutzerEMailDaten,
			patchPasswort: this.data.patchPasswort,
			patchPasswortWenom: this.data.patchPasswortWenom,
			resetPasswordWenom: this.data.passwordResetWenom,
			aes: api.aes,
		};
	}

}

export const routeBenutzerprofilNutzereinstellungen = new RouteBenutzerprofilNutzereinstellungen();
