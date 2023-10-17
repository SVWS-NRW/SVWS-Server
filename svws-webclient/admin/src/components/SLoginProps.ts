
export interface LoginProps {
	authenticated: boolean;
	hostname: string;
	setHostname: (hostname: string) => void;
	login: (username: string, password: string) => Promise<void>;
	connectTo: (url: string) => Promise<boolean>;
	schemaPrevious: string | null;
}
