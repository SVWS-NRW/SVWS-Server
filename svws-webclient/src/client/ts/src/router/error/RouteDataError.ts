import { reactive } from "vue";

interface RouteStateError {
	code: number | undefined;
	error: Error | undefined;
}

export class RouteDataError {

	private _state = reactive<RouteStateError>({
		code: undefined,
		error: undefined
	});

	public get code(): number | undefined {
		return this._state.code;
	}

	public set code(value : number | undefined) {
		this._state.code = value;
	}

	public get error(): Error | undefined {
		return this._state.error;
	}

	public set error(value : Error | undefined) {
		this._state.error = value;
	}

	public reset() {
		this._state.code = undefined;
		this._state.error = undefined;
	}

}