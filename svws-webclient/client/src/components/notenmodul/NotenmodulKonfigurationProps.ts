import type { SimpleOperationResponse, JavaMap, ENMServerConfigElement, ENMServerConnection } from "@core";
import type { WenomAuswahlListeManager } from "@ui";

export interface NotenmodulKonfigurationProps {
	manager: () => WenomAuswahlListeManager;
	connected: boolean;
	connect: (id: number) => Promise<void>;
	trustCertificate: (value: boolean) => Promise<void>;
	serverConfig: () => JavaMap<string, string>;
	setServerConfigElement: (config: ENMServerConfigElement) => Promise<SimpleOperationResponse>;
	updateServerConnection: (data: Partial<ENMServerConnection>) => Promise<void>;
}
