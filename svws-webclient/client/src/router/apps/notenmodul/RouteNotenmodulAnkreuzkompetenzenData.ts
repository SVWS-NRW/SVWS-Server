import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { useNotenmodulState } from "@ui/states/NotenmodulState";

import type { NotenmodulAnkreuzkompetenzenProps } from "~/components/notenmodul/NotenmodulAnkreuzkompetenzenProps";
import { RouteNode } from "~/router/RouteNode";

import type { RouteNotenmodulAnkreuzkompetenzen } from "./RouteNotenmodulAnkreuzkompetenzen";

const NotenmodulAnkreuzkompetenzen = () => import("~/components/notenmodul/NotenmodulAnkreuzkompetenzen.vue");

export class RouteNotenmodulAnkreuzkompetenzenData extends RouteNode<any, RouteNotenmodulAnkreuzkompetenzen> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.ankreuzkompetenzen.daten", "daten", NotenmodulAnkreuzkompetenzen);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ankreuzkompetenzen";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulAnkreuzkompetenzenProps {
		const notenmodulState = useNotenmodulState();
		return {
			enmManager: () => notenmodulState.manager,
			auswahl: () => notenmodulState.auswahlKlassen,
			patchLeistung: notenmodulState.patchLeistung,
			patchBemerkungen: notenmodulState.patchBemerkungen,
			patchAnkreuzkompetenz: notenmodulState.patchAnkreuzkompetenz,
		};
	}

}

export const routeNotenmodulAnkreuzkompetenzenData = new RouteNotenmodulAnkreuzkompetenzenData();
