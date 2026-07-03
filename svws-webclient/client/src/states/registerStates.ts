import { AppContext } from "../../../ui/src/AppContext";
import { AbschnittStateKey } from "../../../ui/src/states/AbschnittState";
import { AuskunftStateKey } from "../../../ui/src/states/AuskunftState";
import { ConfigStateKey } from "../../../ui/src/states/ConfigState";
import { GostLaufbahnplanungStateKey } from "../../../ui/src/states/GostLaufbahnplanungState";
import { ReportingStateKey } from "../../../ui/src/states/ReportingState";
import { SchuleStateKey } from "../../../ui/src/states/SchuleState";
import { ServerStateKey } from "../../../ui/src/states/ServerState";
import { WiedervorlageStateKey } from "../../../ui/src/states/WiedervorlageState";
import { configStateImpl } from "./ConfigStateImpl";
import { abschnittStateImpl } from "./AbschnittStateImpl";
import { auskunftStateImpl } from "./AuskunftStateImpl";
import { gostLaufbahnplanungStateImpl } from "./GostLaufbahnplanungStateImpl";
import { reportingStateImpl } from "./ReportingStateImpl";
import { schuleStateImpl } from "./SchuleStateImpl";
import { serverStateImpl } from "./ServerStateImpl";
import { wiedervorlageStateImpl } from "./WiedervorlageStateImpl";


export function registerStates(): void {
	const context = AppContext.instance;

	context.provide(ConfigStateKey, configStateImpl);
	context.provide(AbschnittStateKey, abschnittStateImpl);
	context.provide(SchuleStateKey, schuleStateImpl);
	context.provide(ServerStateKey, serverStateImpl);
	context.provide(ReportingStateKey, reportingStateImpl);
	context.provide(WiedervorlageStateKey, wiedervorlageStateImpl);
	context.provide(AuskunftStateKey, auskunftStateImpl);
	context.provide(GostLaufbahnplanungStateKey, gostLaufbahnplanungStateImpl);
}
