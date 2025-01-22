import type { KursListeManager } from "@core";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface KurseAuswahlProps extends RouteAuswahlListProps<KursListeManager> {
	setFilterNurSichtbar: (value: boolean) => Promise<void>;
	// ggf weitere Props
}
