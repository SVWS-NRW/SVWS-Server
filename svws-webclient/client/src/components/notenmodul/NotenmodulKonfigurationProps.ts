import type { NotenmodulConfigManagerSperrungen } from "~/router/apps/notenmodul/NotenmodulConfigManagerSperrungen";
import type { NotenmodulConfigManagerSichtbareSpalten } from "~/router/apps/notenmodul/NotenmodulConfigManagerSichtbareSpalten";

export interface NotenmodulKonfigurationProps {
	managerSperrungen: () => NotenmodulConfigManagerSperrungen;
	managerSichtbareSpalten: () => NotenmodulConfigManagerSichtbareSpalten;
}
