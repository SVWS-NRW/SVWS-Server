import type { List } from "@svws-nrw/svws-core";
import type { Config } from "~/components/Config";

export interface GostLaufbahnplanungProps {
	config: Config,
	laufbahnplanungsergebnisse: List<any>;
}
