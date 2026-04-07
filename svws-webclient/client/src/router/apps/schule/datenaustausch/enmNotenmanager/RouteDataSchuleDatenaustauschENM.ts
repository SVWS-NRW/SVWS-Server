import type { ApiFile, List, LehrerListeEintrag } from "@core";
import { ENMv2Daten, ArrayList } from "@core";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { AES } from "~/utils/crypto/aes";
import { AESAlgo } from "~/utils/crypto/aesAlgo";


export interface RouteStateDatenaustauschENM extends RouteStateInterface {
	listLehrer: List<LehrerListeEintrag>;
}

const defaultState = <RouteStateDatenaustauschENM>{
	listLehrer: new ArrayList(),
};

export class RouteDataSchuleDatenaustauschENM extends RouteData<RouteStateDatenaustauschENM> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const listLehrer = await api.server.getLehrer(api.schema);
		this.setPatchedState({ listLehrer });
	}

	get listLehrer() {
		return this._state.value.listLehrer;
	}

	setImportENM = async (file: File, password: string, salt: string) => {
		const key = await AES.getKey256(password, salt);
		const aes = new AES(AESAlgo.CBC, key);
		const base64 = new TextDecoder().decode(await file.arrayBuffer());
		const encoded = await aes.decryptBase64(base64);
		console.log(new TextDecoder().decode(encoded));
		return true;
	};

	exportLehrerENM = async (id: number): Promise<ENMv2Daten> => {
		return api.server.getLehrerENMv2Daten(api.schema, id);
	};

	exportGzipENM = async (): Promise<ApiFile> => {
		return api.server.getENMv2DatenGZip(api.schema);
	};

	importGzipENM = async (data: FormData): Promise<void> => {
		return api.server.importENMv2DatenGZip(data, api.schema);
	};

	importENM = async (file: File): Promise<void> => {
		const json = new TextDecoder().decode(await file.arrayBuffer());
		const data = ENMv2Daten.transpilerFromJSON(json);
		return api.server.importENMv2Daten(data, api.schema);
	};

}

