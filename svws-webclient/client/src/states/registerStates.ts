import {
	AppContext, BenutzerStateKey, AbschnittStateKey, AuskunftStateKey, ConfigStateKey, GostLaufbahnplanungStateKey, ReportingStateKey,
	SchuleStateKey, ServerStateKey, WiedervorlageStateKey, NotenmodulStateKey, OrteStateKey,
} from "@ui";

import { abschnittStateImpl } from "./AbschnittStateImpl";
import { auskunftStateImpl } from "./AuskunftStateImpl";
import { configStateImpl } from "./ConfigStateImpl";
import { gostLaufbahnplanungStateImpl } from "./GostLaufbahnplanungStateImpl";
import { reportingStateImpl } from "./ReportingStateImpl";
import { schuleStateImpl } from "./SchuleStateImpl";
import { serverStateImpl } from "./ServerStateImpl";
import { wiedervorlageStateImpl } from "./WiedervorlageStateImpl";
import { notenmodulStateImpl } from "./NotenmodulStateImpl";
import { benutzerStateImpl } from "./BenutzerStateImpl";
import { orteStateImpl } from "./kataloge/OrteStateImpl";


export function registerStates(): void {
	const context = AppContext.instance;

	context.provide(BenutzerStateKey, benutzerStateImpl);
	context.provide(ConfigStateKey, configStateImpl);
	context.provide(AbschnittStateKey, abschnittStateImpl);
	context.provide(SchuleStateKey, schuleStateImpl);
	context.provide(ServerStateKey, serverStateImpl);
	context.provide(ReportingStateKey, reportingStateImpl);
	context.provide(WiedervorlageStateKey, wiedervorlageStateImpl);
	context.provide(AuskunftStateKey, auskunftStateImpl);
	context.provide(GostLaufbahnplanungStateKey, gostLaufbahnplanungStateImpl);
	context.provide(NotenmodulStateKey, notenmodulStateImpl);
	context.provide(OrteStateKey, orteStateImpl);
}
