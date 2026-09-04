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
import { AppContext } from "@ui/AppContext";
import { AbschnittStateKey } from "@ui/states/AbschnittState";
import { AuskunftStateKey } from "@ui/states/AuskunftState";
import { BenutzerStateKey } from "@ui/states/BenutzerState";
import { ConfigStateKey } from "@ui/states/ConfigState";
import { GostKlausurplanungStateKey } from "@ui/states/GostKlausurplanungState";
import { GostLaufbahnplanungStateKey } from "@ui/states/GostLaufbahnplanungState";
import { OrteStateKey } from "@ui/states/kataloge/OrteState";
import { NotenmodulStateKey } from "@ui/states/NotenmodulState";
import { NotificationsStateKey } from "@ui/states/NotificationsState";
import { ReportingStateKey } from "@ui/states/ReportingState";
import { SchuleStateKey } from "@ui/states/SchuleState";
import { ServerStateKey } from "@ui/states/ServerState";
import { WiedervorlageStateKey } from "@ui/states/WiedervorlageState";


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
