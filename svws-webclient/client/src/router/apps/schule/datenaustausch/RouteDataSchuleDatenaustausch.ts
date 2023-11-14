import { shallowRef } from "vue";

import { AES } from "~/utils/crypto/aes";
import { AESAlgo } from "~/utils/crypto/aesAlgo";

import { api } from "~/router/Api";
import { type RouteNode } from "~/router/RouteNode";

import { routeSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";
import { routeSchuleDatenaustauschLaufbahnplanung } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustauschLupo";

import { OpenApiError, SimpleOperationResponse } from "@core";


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

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData, mode: 'none' | 'schueler' | 'all') : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.setGostLupoImportMDBFuerJahrgang(formData, api.schema, mode);
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response != null) && ((e.response.status === 400) || (e.response.status === 409))) {
				const json : string = await e.response.text();
				return SimpleOperationResponse.transpilerFromJSON(json);
			}
			const result = new SimpleOperationResponse();
			result.log.add("Fehler bei der Server-Anfrage. ");
			if (e instanceof Error)
				result.log.add("  " + e.message);
			return result;
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

