import { AppContext } from "@ui/AppContext";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { auskunftStateImpl } from "./AuskunftStateImpl";


export function registerStates(): void {
	const context = AppContext.instance;

	context.provide(AuskunftStateKey, auskunftStateImpl);
}