import type { FachListeManager } from "@core";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface FaecherAuswahlProps extends RouteAuswahlListProps<FachListeManager> {
	setzeDefaultSortierungSekII: () => Promise<void>;
}
