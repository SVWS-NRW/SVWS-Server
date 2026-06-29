import { ServerMode } from "@core/core/types/ServerMode";
import type { ServerState } from "@ui/states/ServerState";

/**
 * Die Schnittstelle für den Zustand des Servers
 */
export class ServerStateImpl implements ServerState {

	private readonly state = {
		mode: ServerMode.STABLE,
	};

	public get mode(): ServerMode {
		return this.state.mode;
	}

	public get hasDev(): boolean {
		return ServerMode.DEV.checkServerMode(this.mode);
	}

	public get hasAlpha(): boolean {
		return ServerMode.ALPHA.checkServerMode(this.mode);
	}

	public get hasBeta(): boolean {
		return ServerMode.BETA.checkServerMode(this.mode);
	}

}

export const serverStateImpl = new ServerStateImpl();
