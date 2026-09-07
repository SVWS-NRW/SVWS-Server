import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import type { KursListeManager } from "~/states/kurse/KursListeManager";

export interface KurseAuswahlProps extends RouteAuswahlListProps<KursListeManager> {
	setFilterNurSichtbar: (value: boolean) => Promise<void>;
	// ggf weitere Props
}
