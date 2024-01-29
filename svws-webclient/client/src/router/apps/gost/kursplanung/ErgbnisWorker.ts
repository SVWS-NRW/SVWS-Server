import type { GostBlockungsdatenManager, GostFaecherManager} from "@core";
import { GostBlockungsdaten, GostFach } from "@core";
import { shallowRef } from "vue";

type Message = { cmd: string; faecher?: string[], blockungsdaten?: string, result?: boolean | string[] | Bewertung[]};
type Bewertung = {
	wert1: number;
	color1: string;
	wert2: number;
	color2: string;
	wert3: number;
	color3: string;
	wert4: number;
	color4: string;
};


export class ErgebnisWorker {
	public liste = shallowRef<Bewertung[]>([]);
	public worker = new Worker(new URL('./berechne_lokal_worker.ts', import.meta.url), {type: 'module'});
	public running = shallowRef<boolean|undefined>();

	public constructor(faecherManager: GostFaecherManager, datenmanager: GostBlockungsdatenManager) {
		const faecher = [];
		for (const f of faecherManager.faecher())
			faecher.push(GostFach.transpilerToJSON(f));
		const blockungsdaten = GostBlockungsdaten.transpilerToJSON(datenmanager.daten());
		this.worker.postMessage({cmd: "init", faecher, blockungsdaten});
		this.worker.addEventListener("message", this.messageHandler);
	}

	protected messageHandler = (e: MessageEvent<any>) => {
		const data: Message = e.data;
		switch (data.cmd) {
			case 'init':
				if (data.result !== undefined && typeof data.result === 'boolean')
					this.running.value = data.result ? false : undefined;
				break;
			case 'next':
				if (this.running.value === true)
					this.worker.postMessage({cmd: 'next'});
				if (data.result === true)
					this.worker.postMessage({cmd: 'getErgbnisBewertungen'});
				break;
			case 'getErgbnisBewertungen':
				if (Array.isArray(data.result))
					this.liste.value = data.result as Bewertung[];
				break;
			case 'getErgebnisse':
				if (Array.isArray(data.result))
					console.log(data.result);
				break;
			default:
				break;
		}
	}

	public next(val = 100) {
		this.worker.postMessage({cmd: 'next', val});
	}

	public terminate() {
		this.worker.removeEventListener('message', this.messageHandler);
		this.worker.terminate();
	}
}