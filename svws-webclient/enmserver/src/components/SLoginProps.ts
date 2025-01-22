export interface LoginProps {
	setHostname: (hostname: string) => void;
	login: (username: string, password: string) => Promise<void>;
	connectTo: (url: string) => Promise<void>;
	authenticated: boolean;
	hostname: string;
}
