import type { List } from "@core";
import { ArrayList, GostBlockungsdaten, GostBlockungsdatenManager, GostBlockungsergebnis, GostFach, GostFaecherManager, KursblockungAlgorithmusPermanent } from "@core";

class Berechnen {

	protected algo: KursblockungAlgorithmusPermanent;
	protected faecherManager: GostFaecherManager;
	protected datenmanager: GostBlockungsdatenManager;

	constructor(faecher: string[], blockungsdaten: string) {
		const list: List<GostFach> = new ArrayList();
		faecher.forEach(f=>list.add(GostFach.transpilerFromJSON(f)));
		this.faecherManager = new GostFaecherManager(list);
		this.datenmanager = new GostBlockungsdatenManager(GostBlockungsdaten.transpilerFromJSON(blockungsdaten), this.faecherManager)
		this.algo = new KursblockungAlgorithmusPermanent(this.datenmanager);
	}

	public next(val: number) {
		return this.algo.next(val);
	}

	public getBlockungsergebnisse() {
		const manager = this.algo.getBlockungsergebnisse();
		const arr = [];
		for (const m of manager)
			arr.push(GostBlockungsergebnis.transpilerToJSON(m.getErgebnis()));
		return arr;
	}
}

let berechnen: Berechnen | undefined = undefined;
let running: boolean = false;

onmessage = (e) => {
	if (e.data.name === 'init') {
		const {faecher, blockungsdaten} = e.data;
		berechnen = new Berechnen(faecher, blockungsdaten);
	}
	if (e.data.name === 'run' && berechnen !== undefined) {
		running = true;
		while (running)
			if (berechnen.next(100))
				postMessage(berechnen.getBlockungsergebnisse());
	}
	if (e.data.name === 'stop' && running === true) {
		console.log("stopping");
		running = false;
	}
};
