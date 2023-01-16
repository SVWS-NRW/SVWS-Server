import { Ref, shallowRef } from "vue";

interface Container<T, U> {
	daten_manager : T;
	ergebnis_manager : U
}

export class ManagerContainer<T, U> {
	_container : Ref<Container<T, U>>;

	constructor(m : T, e : U) {
		this._container = shallowRef({ daten_manager: m, ergebnis_manager: e });
	}

	commit() {
		this._container.value = this._container.value;
	}

}


/**
 *
 *
 */