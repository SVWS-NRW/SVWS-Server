import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenAuswahlProps extends RouteAuswahlListProps<KlassenListeManager> {
	setzeDefaultSortierung: () => Promise<void>;
}
