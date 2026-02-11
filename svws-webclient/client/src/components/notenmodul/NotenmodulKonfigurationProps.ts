import type { NotenmodulConfigManagerSperrungen } from "~/router/apps/notenmodul/NotenmodulConfigManagerSperrungen";
import type { NotenmodulConfigManagerSichtbareSpalten } from "~/router/apps/notenmodul/NotenmodulConfigManagerSichtbareSpalten";

export interface NotenmodulKonfigurationProps {
	istLokal: boolean;
	syncWithLocalConfig: () => Promise<void>;
	managerSperrungen: () => NotenmodulConfigManagerSperrungen;
	managerSichtbareSpalten: () => NotenmodulConfigManagerSichtbareSpalten;
}
