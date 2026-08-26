import {
	AppContext, BenutzerStateKey, AbschnittStateKey, AuskunftStateKey, ConfigStateKey, GostLaufbahnplanungStateKey, ReportingStateKey,
	SchuleStateKey, ServerStateKey, WiedervorlageStateKey, NotenmodulStateKey, OrteStateKey, GostKlausurplanungStateKey, NotificationsStateKey,
} from "@ui";

import { abschnittStateImpl } from "./AbschnittStateImpl";
import { auskunftStateImpl } from "./AuskunftStateImpl";
import { configStateImpl } from "./ConfigStateImpl";
import { gostLaufbahnplanungStateImpl } from "./GostLaufbahnplanungStateImpl";
import { gostKlausurplanungStateImpl } from "./GostKlausurplanungStateImpl";
import { reportingStateImpl } from "./ReportingStateImpl";
import { schuleStateImpl } from "./SchuleStateImpl";
import { serverStateImpl } from "./ServerStateImpl";
import { wiedervorlageStateImpl } from "./wiedervorlage/WiedervorlageStateImpl";
import { notenmodulStateImpl } from "./NotenmodulStateImpl";
import { benutzerStateImpl } from "./BenutzerStateImpl";
import { notificationStateImpl } from "~/states/NotificationsStateImpl";
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
	context.provide(GostKlausurplanungStateKey, gostKlausurplanungStateImpl);
	context.provide(NotenmodulStateKey, notenmodulStateImpl);
	context.provide(NotificationsStateKey, notificationStateImpl);
	context.provide(OrteStateKey, orteStateImpl);
}
