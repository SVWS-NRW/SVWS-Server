import type { WenomAuswahlListeManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulAdministrationAuswahlProps extends RouteAuswahlListProps<WenomAuswahlListeManager> {
	manager: () => WenomAuswahlListeManager;
}