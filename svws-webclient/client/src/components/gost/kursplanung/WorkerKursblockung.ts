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
		const arr = new Array<string>();
		for (const m of manager)
			arr.push(GostBlockungsergebnis.transpilerToJSON(m.getErgebnis()));
		return arr;
	}

	public getBlockungsbewertungen() {
		const manager = this.algo.getBlockungsergebnisse();
		const arr = [];
		for (const m of manager) {
			const bewertung = {
				wert1: m.getOfBewertung1Wert(),
				color1: `hsl(${Math.round((1 - (m.getOfBewertung1Farbcode()||0)) * 120)},100%,75%)`,
				wert2: m.getOfBewertung2Wert(),
				color2: `hsl(${Math.round((1 - (m.getOfBewertung2Farbcode()||0)) * 120)},100%,75%)`,
				wert3: m.getOfBewertung3Wert(),
				color3: `hsl(${Math.round((1 - (m.getOfBewertung3Farbcode()||0)) * 120)},100%,75%)`,
				wert4: m.getOfBewertung4Wert(),
				color4: `hsl(${Math.round((1 - (m.getOfBewertung4Farbcode()||0)) * 120)},100%,75%)`,
			};
			arr.push(bewertung);
		}
		return arr;
	}
}

let berechnen: Berechnen | undefined = undefined;

type Message = { cmd: string; faecher?: string[], blockungsdaten?: string }

onmessage = (e) => {
	const data: Message = e.data;
	switch (data.cmd) {
		case 'init': {
			const {faecher, blockungsdaten} = data;
			if (faecher !== undefined && blockungsdaten !== undefined)
				berechnen = new Berechnen(faecher, blockungsdaten);
			postMessage({cmd: 'init', result: berechnen !== undefined});
			break;
		}
	  case 'next': {
			if (berechnen !== undefined) {
				const result = berechnen.next(100);
				postMessage({cmd: 'next', result });
			}
			break;
		}
		case 'getErgebnisBewertungen': {
			 if (berechnen !== undefined) {
				 const result = berechnen.getBlockungsbewertungen();
				 postMessage({cmd: 'getErgebnisBewertungen', result});
			 }
			break;
		}
		case 'getErgebnisse': {
			 if (berechnen !== undefined) {
				 const result = berechnen.getBlockungsergebnisse();
				 postMessage({cmd: 'getErgebnisse', result});
			 }
			break;
		}
	}
};
