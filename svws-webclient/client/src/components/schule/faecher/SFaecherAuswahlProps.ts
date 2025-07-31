import type { FachListeManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface FaecherAuswahlProps extends RouteAuswahlListProps<FachListeManager> {
	setzeDefaultSortierungSekII: () => Promise<void>;
}
