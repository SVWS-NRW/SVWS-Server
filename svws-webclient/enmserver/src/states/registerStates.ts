import { AppContext } from "@ui/AppContext";
import { ActivityStateKey } from "./ActivityState";
import { AuthStateKey } from "./AuthState";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { activityStateImpl } from "./ActivityStateImpl";
import { authStateImpl } from "./AuthStateImpl";
import { auskunftStateImpl } from "./AuskunftStateImpl";

export function registerStates(): void {
	const context = AppContext.instance;

	context.provide(ActivityStateKey, activityStateImpl);
	context.provide(AuthStateKey, authStateImpl);
	context.provide(AuskunftStateKey, auskunftStateImpl);
}
