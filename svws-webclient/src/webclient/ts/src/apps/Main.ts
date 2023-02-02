import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
import { App } from "./BaseApp";
import { BaseList } from "./BaseList";
import { inject, InjectionKey, provide, reactive } from "vue";
import { UserConfigKeys } from "~/utils/userconfig/keys";

export class MainConfig {
	user_config: Map<keyof UserConfigKeys, UserConfigKeys[keyof UserConfigKeys]> = new Map();

	/** Das aktuelle Drag & Drop - Objekt */
	drag_and_drop_data: any;

	/**
	 * Der aktuell ausgewählte Abschnitt
	 *
	 * @returns {Schuljahresabschnitt}
	 */
	get akt_abschnitt(): Schuljahresabschnitt {
		return App.akt_abschnitt;
	}
	/**
	 * Setzt den aktuellen Schuljahresabschnitt
	 *
	 * @param {Schuljahresabschnitt} abschnitt
	 */
	set akt_abschnitt(abschnitt: Schuljahresabschnitt) {
		App.akt_abschnitt = abschnitt;
		const lists = BaseList.all
		for (const l of lists) {
			void l.update_list()
		}
	}
}

/**
 * Diese Klasse wird als App in vue geladen Sie steht als `this.$app` zur
 * Verfügung und sollte nur die nötigsten Informationen enthalten, die für alle
 * anderen Apps notwendig sind. Beispielsweise der aktuelle Abschnitt.
 */
export class Main {
	public config = reactive(new MainConfig());
}

export const mainApp = new Main();

function requireInjection<T>(key: InjectionKey<T>, defaultValue?: T) {
	const resolved = inject(key, defaultValue);
	if (!resolved) throw new Error(`${key} was not provided.`);
	return resolved;
}

export const mainInjectKey = Symbol("MainInjectKey") as InjectionKey<Main>;
export function provideMainApp() {
	provide(mainInjectKey, mainApp);
}
export function injectMainApp() {
	return requireInjection(mainInjectKey);
}
