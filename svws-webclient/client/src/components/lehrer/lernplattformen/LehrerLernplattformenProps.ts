import type { LehrerLernplattform } from "@core/core/data/lehrer/LehrerLernplattform";
import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerLernplattformenProps {
	lehrerLernplattformen: () => List<LehrerLernplattform>;
	mapLernplattformen: Map<number, Lernplattform>;
	patch: (data: Partial<LehrerLernplattform>, idLernplattform: number) => Promise<boolean>;
	apiStatus: ApiStatus;
}
