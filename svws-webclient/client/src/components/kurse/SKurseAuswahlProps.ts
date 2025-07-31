import type { KursListeManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface KurseAuswahlProps extends RouteAuswahlListProps<KursListeManager> {
	setFilterNurSichtbar: (value: boolean) => Promise<void>;
	// ggf weitere Props
}
