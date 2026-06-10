import type { KlassenListeManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface KlassenAuswahlProps extends RouteAuswahlListProps<KlassenListeManager> {
	setzeDefaultSortierung: () => Promise<void>;
}
