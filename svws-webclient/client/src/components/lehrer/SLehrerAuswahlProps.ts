import type { LehrerListeManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface LehrerAuswahlProps extends RouteAuswahlListProps<LehrerListeManager> {
	setFilterNurSichtbar: (value: boolean) => Promise<void>;
	setFilterNurStatistikrelevant: (value: boolean) => Promise<void>;
}
