import { AppContext } from "@ui/AppContext";
import { AuskunftStateKey } from "@ui/states/AuskunftState";

import { ActivityStateKey } from "./ActivityState";
import { activityStateImpl } from "./ActivityStateImpl";
import { auskunftStateImpl } from "./AuskunftStateImpl";
import { AuthStateKey } from "./AuthState";
import { authStateImpl } from "./AuthStateImpl";

export function registerStates(): void {
	const context = AppContext.instance;

	context.provide(ActivityStateKey, activityStateImpl);
	context.provide(AuthStateKey, authStateImpl);
	context.provide(AuskunftStateKey, auskunftStateImpl);
}
