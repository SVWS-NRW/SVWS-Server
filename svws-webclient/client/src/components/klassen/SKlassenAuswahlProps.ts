import type { KlassenListeManager } from "@core";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface KlassenAuswahlProps extends RouteAuswahlListProps<KlassenListeManager> {
	setzeDefaultSortierung: () => Promise<void>;
}
