import { shallowRef } from "vue";

import { ArrayList } from "@core";
import type { GostStatistikFachwahl, List } from "@core";

import { api } from "~/router/Api";

interface RouteStateDataGostFachwahlen {
	fachwahlen: List<GostStatistikFachwahl>;
}

export class RouteDataGostFachwahlen  {

	private static _defaultState: RouteStateDataGostFachwahlen = {
		fachwahlen: new ArrayList<GostStatistikFachwahl>(),
	}

	private _state = shallowRef(RouteDataGostFachwahlen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostFachwahlen>) {
		this._state.value = Object.assign({ ... RouteDataGostFachwahlen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostFachwahlen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public get fachwahlen(): List<GostStatistikFachwahl> {
		return this._state.value.fachwahlen;
	}

	public async setEintrag(abiturjahr?: number) {
		const fachwahlen = await api.server.getGostAbiturjahrgangFachwahlstatistik(api.schema, abiturjahr || -1);
		this.setPatchedState({fachwahlen})
	}

}

