import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface LehrerAppProps extends RouteAuswahlProps<LehrerListeManager> {
	gotoDefaultView: (id?: number | null) => Promise<void>;
}
