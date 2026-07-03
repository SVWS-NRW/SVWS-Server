import { AppContext } from "@ui/AppContext";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { GostLaufbahnplanungStateKey } from "@ui/states/GostLaufbahnplanungState";
import { ServerStateKey } from "@ui/states/ServerState";
import { serverStateImpl } from "./ServerStateImpl";
import { auskunftStateImpl } from "./AuskunftStateImpl";
import { gostLaufbahnplanungStateImpl } from "./GostLaufbahnplanungStateImpl";

export function registerStates(): void {
	const context = AppContext.instance;

	context.provide(ServerStateKey, serverStateImpl);
	context.provide(AuskunftStateKey, auskunftStateImpl);
	context.provide(GostLaufbahnplanungStateKey, gostLaufbahnplanungStateImpl);
}
