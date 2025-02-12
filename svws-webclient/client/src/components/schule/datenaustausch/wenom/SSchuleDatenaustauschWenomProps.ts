import type { ENMDaten, ENMServerConfig, ENMServerConfigElement, JavaMap, OAuth2ClientSecret, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschWenomProps {
	mapInitialKennwoerter: () => JavaMap<number, string>;
	getEnmDaten: () => Promise<ENMDaten | null>;
	getCredentials: () => Promise<OAuth2ClientSecret | null>;
	setCredentials: (url: string, token: string) => Promise<OAuth2ClientSecret | null>;
	removeCredentials: () => Promise<void>;
	synchronize: () => Promise<SimpleOperationResponse>;
	download: () => Promise<SimpleOperationResponse>;
	upload: () => Promise<SimpleOperationResponse>;
	truncate: () => Promise<SimpleOperationResponse>;
	reset: () => Promise<SimpleOperationResponse>;
	check: () => Promise<SimpleOperationResponse>;
	setup: () => Promise<boolean|SimpleOperationResponse>;
	serverConfig: () => JavaMap<string, string>;
	setServerConfigElement: (config: ENMServerConfigElement) => Promise<SimpleOperationResponse>
}

