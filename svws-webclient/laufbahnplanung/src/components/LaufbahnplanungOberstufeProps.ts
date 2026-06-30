import type { Config } from "@ui/utils/Config";

export interface LaufbahnplanungOberstufeProps {
	config: () => Config;
	exitLaufbahnplanung: () => Promise<void>;
	dirty: () => boolean;
}
