import type { AnkreuzkompetenzKonfiguration } from "@core/core/data/kataloge/AnkreuzkompetenzKonfiguration";
import type { AnkreuzkompetenzenListeManager } from "@ui/ui/manager/kataloge/AnkreuzkompetenzenListeManager";

import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface AnkreuzkompetenzenAuswahlProps extends RouteAuswahlListProps<AnkreuzkompetenzenListeManager> {
	patchKonfiguration: (ankreuzkompetenzKonfiguration: AnkreuzkompetenzKonfiguration) => Promise<void>;
}
