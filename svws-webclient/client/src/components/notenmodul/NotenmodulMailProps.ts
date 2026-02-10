import type { SimpleOperationResponse, JavaMap, ENMServerConfigElement, ENMServerConnection } from "@core";
import type { WenomAuswahlListeManager } from "@ui";

export interface NotenmodulMailProps {
	manager: () => WenomAuswahlListeManager;
	serverConfig: () => JavaMap<string, string>;
	setServerConfigElement: (config: ENMServerConfigElement) => Promise<SimpleOperationResponse>;
	updateServerConnection: (data: Partial<ENMServerConnection>) => Promise<void>;
}
