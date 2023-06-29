import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { AES } from "~/utils/crypto/aes";
import { AESAlgo } from "~/utils/crypto/aesAlgo";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import type { RouteSchule } from "~/router/apps/RouteSchule";
import { routeSchule } from "~/router/apps/RouteSchule";

import { routeSchuleDatenaustauschKurs42 } from "~/router/apps/datenaustausch/RouteDatenaustauschKurs42";
import { routeSchuleDatenaustauschLaufbahnplanung } from "~/router/apps/datenaustausch/RouteDatenaustauschLupo";
import { routeSchuleDatenaustauschENM } from "~/router/apps/datenaustausch/RouteDatenaustauschENM";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { SchuleDatenaustauschAuswahlProps } from "~/components/schule/datenaustausch/SSchuleDatenaustauschAuswahlProps";

const SSchuleDatenaustauschApp = () => import("~/components/schule/datenaustausch/SSchuleDatenaustauschApp.vue")
const SSchuleDatenaustauschAuswahl = () => import("~/components/schule/datenaustausch/SSchuleDatenaustauschAuswahl.vue")

interface RouteStateDatenaustausch {
	view: RouteNode<any, any>;
}

export class RouteDataSchuleDatenaustausch {

	private static _defaultState : RouteStateDatenaustausch = {
		view: routeSchuleDatenaustauschLaufbahnplanung
	}

	private _state = shallowRef<RouteStateDatenaustausch>(RouteDataSchuleDatenaustausch._defaultState);

	private setPatchedState(patch: Partial<RouteStateDatenaustausch>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchuleDatenaustausch.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für den Datenaustausch gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData) => {
		try {
			const res = await api.server.setGostLupoImportMDBFuerJahrgang(formData, api.schema);
			return res.success;
		} catch(e) {
			return false;
		}
	}

	setGostKurs42ImportZip = async (formData: FormData) => {
		try {
			const res = await api.server.importKurs42Blockung(formData, api.schema);
			return res.success;
		} catch(e) {
			return false;
		}
	}

	setImportENM = async (file: File, password: string, salt: string) => {
		const key = await AES.getKey256(password, salt);
		const aes = new AES(AESAlgo.CBC, key);
		const base64 = new TextDecoder().decode(await file.arrayBuffer());
		const encoded = await aes.decryptBase64(base64);
		console.log(new TextDecoder().decode(encoded));
		return true;
	}
}

export class RouteSchuleDatenaustausch extends RouteNode<RouteDataSchuleDatenaustausch, RouteSchule> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch", "/schule/datenaustausch", SSchuleDatenaustauschApp, new RouteDataSchuleDatenaustausch());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Datenaustausch";
		super.setView("liste", SSchuleDatenaustauschAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleDatenaustauschKurs42,
			routeSchuleDatenaustauschLaufbahnplanung,
			routeSchuleDatenaustauschENM,
		];
		super.defaultChild = routeSchuleDatenaustauschLaufbahnplanung;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return this.getRoute();
	}

	public async enter() {
		return this.setChild(this.defaultChild!);
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: {}};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuleDatenaustauschAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoSchule: routeSchule.gotoSchule,
			// Props für die Navigation
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
			childrenHidden: this.children_hidden().value,
		};
	}

	private getChild(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getChildData(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: AuswahlChildData) => {
		if (value.name === this.data.view?.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {} });
		await this.data.setView(node);
	}

}

export const routeSchuleDatenaustausch = new RouteSchuleDatenaustausch();

