import type { FaecherListeManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface FaecherAuswahlProps extends RouteAuswahlListProps<FaecherListeManager> {
	setzeDefaultSortierungSekII: () => Promise<void>;
}
