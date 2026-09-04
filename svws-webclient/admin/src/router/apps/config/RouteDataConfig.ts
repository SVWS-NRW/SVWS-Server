import { api } from "@admin/router/Api";
import type { RouteNode } from "@admin/router/RouteNode";
import type { TLSCertificateInfo } from "@core/core/data/TLSCertificateInfo";
import { shallowRef } from "vue";
import { routeSchema } from "../schema/RouteSchema";
import { routeSchemaUebersicht } from "../schema/uebersicht/RouteSchemaUebersicht";


interface RouteStateConfig {
	view: RouteNode<any, any>;
}

export class RouteDataConfig {

	private static readonly _defaultState: RouteStateConfig = {
		view: routeSchemaUebersicht,
	};

	private readonly _state = shallowRef(RouteDataConfig._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateConfig>) {
		this._state.value = { ... RouteDataConfig._defaultState, ...patch };
	}

	private setPatchedState(patch: Partial<RouteStateConfig>) {
		this._state.value = { ... this._state.value, ...patch };
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public get view(): RouteNode<any, any> {
		return this._state.value.view;
	}

	public async setView(view: RouteNode<any, any>) {
		if (routeSchema.children.includes(view)) {
			this.setPatchedState({ view });
		} else {
			throw new Error("Diese für die Konfiguration gewählte Ansicht wird nicht unterstützt.");
		}
	}

	getCert = async () => {
		return await api.server.getConfigCertificateFile();
	};

	createCert = async (tlsCertificateInfo: TLSCertificateInfo, alias: string): Promise<boolean> => {
		api.status.start();
		try {
			await api.privileged.createConfigPrivateKeySelfSignedCertificate(tlsCertificateInfo, alias);
			return true;
		} finally {
			api.status.stop();
		}
	};

	uploadCert = async (formData: FormData, alias: string): Promise<boolean> => {
		api.status.start();
		try {
			await api.privileged.setConfigPrivateKeyCertificateBase64(formData, alias);
			return true;
		} finally {
			api.status.stop();
		}
	};

}
