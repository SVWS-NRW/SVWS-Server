import type { ENMDaten, JavaMap, OAuth2ClientSecret, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschWenomProps {
	getEnmDaten: () => Promise<ENMDaten | null>;
	getEnmCredentials: () => Promise<JavaMap<number, string>>;
	getCredentials: () => Promise<OAuth2ClientSecret | null>;
	setCredentials: (url: string, token: string) => Promise<OAuth2ClientSecret | null>;
	removeCredentials: () => Promise<void>;
	synchronize: () => Promise<SimpleOperationResponse>;
	download: () => Promise<SimpleOperationResponse>;
	upload: () => Promise<SimpleOperationResponse>;
	truncate: () => Promise<SimpleOperationResponse>;
	reset: () => Promise<SimpleOperationResponse>;
	check: () => Promise<SimpleOperationResponse>;
}

