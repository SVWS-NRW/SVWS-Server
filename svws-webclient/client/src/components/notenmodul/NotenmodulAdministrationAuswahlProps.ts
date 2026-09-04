import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulAdministrationAuswahlProps extends RouteAuswahlListProps<WenomAuswahlListeManager> {
	manager: () => WenomAuswahlListeManager;
}