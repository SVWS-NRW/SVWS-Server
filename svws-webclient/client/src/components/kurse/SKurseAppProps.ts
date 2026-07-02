import type { KursListeManager } from "~/states/kurse/KursListeManager";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface KurseAppProps extends RouteAuswahlProps<KursListeManager> {
	// ggf weitere Props
}
