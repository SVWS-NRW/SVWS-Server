import type { ENMDaten, ENMServerConfig, ENMServerConfigElement, JavaMap, OAuth2ClientConnection, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschWenomProps {
	connected: boolean;
	connectionInfo: () => OAuth2ClientConnection | null;
	enmDaten: () => ENMDaten | null;
	mapEnmInitialKennwoerter: () => JavaMap<number, string>;
	connect: () => Promise<SimpleOperationResponse>;
	trustCertificate: (value: boolean) => Promise<void>;
	setCredentials: (url: string, token: string) => Promise<boolean>;
	removeCredentials: () => Promise<void>;
	synchronize: () => Promise<SimpleOperationResponse>;
	download: () => Promise<SimpleOperationResponse>;
	upload: () => Promise<SimpleOperationResponse>;
	truncate: () => Promise<SimpleOperationResponse>;
	reset: () => Promise<SimpleOperationResponse>;
	serverConfig: () => JavaMap<string, string>;
	setServerConfigElement: (config: ENMServerConfigElement) => Promise<SimpleOperationResponse>
}

