import type { NotenmodulConfigManagerSichtbareSpalten } from "~/router/apps/notenmodul/NotenmodulConfigManagerSichtbareSpalten";
import type { NotenmodulConfigManagerSperrungen } from "~/router/apps/notenmodul/NotenmodulConfigManagerSperrungen";

export interface NotenmodulKonfigurationProps {
	istLokal: boolean;
	syncWithLocalConfig: () => Promise<void>;
	managerSperrungen: () => NotenmodulConfigManagerSperrungen;
	managerSichtbareSpalten: () => NotenmodulConfigManagerSichtbareSpalten;
}
