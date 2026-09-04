import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface LehrerAuswahlProps extends RouteAuswahlListProps<LehrerListeManager> {
	setFilterNurSichtbar: (value: boolean) => Promise<void>;
	setFilterNurStatistikrelevant: (value: boolean) => Promise<void>;
}
