<template>
	<div class="page--content page--content--flex-row gap-2 h-full w-full overflow-hidden">
		<!-- Auswahl des Untis-Exportes (linke Seite) -->
		<div class="h-full min-w-48 w-48 flex flex-col gap-2">
			<template v-for="(gpu, index) in gpus" :key="gpu.title">
				<svws-ui-button v-if="isVisible(gpu)" :class="{'contentFocusField': index===0 }" :type="(aktuell === gpu) ? 'primary' : 'secondary'" @click="onSelect(gpu)">
					<div class="flex flex-col gap-1">
						<p class="text-left font-bold ">{{ gpu.title }}</p>
						<p class="text-left font-normal">{{ gpu.files.join(', ') }}</p>
					</div>
				</svws-ui-button>
			</template>
		</div>

		<!-- Weitere Eingabemöglichkeiten für den zuvor gewählten Untis-Export (rechte Seite - spezielle Ansicht nach Auswahl) -->
		<div class="flex flex-col">
			<div v-if="zeigeSchuelerVariantenAuswahl" class="max-w-196">
				<ui-card :compact="true" :collapsible="false" title="Schema Schüler-IDs" content="Wählen Sie das Schema für das Erzeugen der Schüler-IDs in Untis:">
					<template #footer>
						<svws-ui-radio-group>
							<svws-ui-radio-option value="1" v-model="sidvariante" @update:model-value="setSIDVariante" name="radioInputSchueleridschema" label="SVWS ID-Schema (S-SVWSID)" />
							<svws-ui-radio-option value="2" v-model="sidvariante" @update:model-value="setSIDVariante" name="radioInputSchueleridschema" label="Untis ID-Schema kurz (Nachname-Vor-20081023)" />
							<svws-ui-radio-option value="3" v-model="sidvariante" @update:model-value="setSIDVariante" name="radioInputSchueleridschema" label="Untis ID-Schema lang (Nachname-Vorname-20081023)" />
						</svws-ui-radio-group>
					</template>
				</ui-card>
			</div>
			<div v-if="zeigeGPU002Laden" class="max-w-196">
				<ui-card compact :collapsible="false" title="Upload GPU002.txt" show-divider>
					<div class="text-justify">
						Laden Sie jetzt die Datei <span class="font-bold">GPU002.txt</span> von Untis hier hoch, um die Klausuren anschließend zu exportieren.
						Die CSV-Datei muss als Textkodierung UTF-8 verwenden. Als Trennzeichen wird das Semikolon verwendet und für die textbegrenzung doppelte Anführungszeichen (").
					</div>
					<template #footer>
						<input type="file" accept=".txt" @change="importGPU002">
					</template>
				</ui-card>
			</div>
			<div v-if="aktuell === blockungGPUs" class="mt-8 max-w-196">
				<p>Wählen Sie die aktiven Blockungsergebnisse aus, welche beim Export berücksichtigt werden sollen: </p>
				<table class="svws-ui-table mt-4 mb-4" role="table" aria-label="Tabelle">
					<thead class="svws-ui-thead cursor-pointer mb-1" role="rowgroup" aria-label="Tabellenkopf">
						<tr class="svws-ui-tr" role="row" style="grid-template-columns: 2rem 5rem 5rem 1fr 1fr">
							<th class="svws-ui-td svws-align-center" role="columnheader" />
							<th class="svws-ui-td svws-align-center" role="columnheader">Abiturjahr</th>
							<th class="svws-ui-td svws-align-center" role="columnheader">Halbjahr</th>
							<th class="svws-ui-td svws-align-center" role="columnheader">aktive Blockung</th>
							<th class="svws-ui-td svws-align-center" role="columnheader">aktives Ergebnis</th>
						</tr>
					</thead>
					<tbody class="svws-ui-tbody" role="rowgroup" aria-label="Tabelleninhalt">
						<tr v-for="info of blockungsinfos" :key="info.abijahr" class="svws-ui-tr" role="row" style="grid-template-columns: 2rem 5rem 5rem 1fr 1fr">
							<td class="svws-ui-td svws-align-center" role="cell">
								<div v-if="(info.idBlockung === null) || (info.idErgebnis === null)" />
								<svws-ui-checkbox v-else :model-value="info.auswahl.value" @update:model-value="(value) => setBlockungsergebnis(info, value)" />
							</td>
							<td class="svws-ui-td svws-align-center" role="cell">{{ info.abijahr }}</td>
							<td class="svws-ui-td svws-align-center" role="cell">{{ info.halbjahr.kuerzel }}</td>
							<td class="svws-ui-td svws-align-center" role="cell">{{ (info.idBlockung === null) ? '–' : info.idBlockung }}</td>
							<td class="svws-ui-td svws-align-center" role="cell">{{ (info.idErgebnis === null) ? '–' : info.idErgebnis }}</td>
						</tr>
					</tbody>
				</table>
				<p><span class="font-bold">Hinweis:</span> Blockung und Blockungsergebnisse können unter Oberstufe / Kursplanung als aktiv markiert werden. Es ist nur der Export von aktiven Ergebnissen aktiver Blockungen möglich </p>
			</div>
		</div>
		<div v-if="zeigeDateiSpeichern" class="w-full h-full overflow-hidden flex flex-col gap-4">
			<template v-for="(value, index) of daten" :key="index">
				<!-- Angabe des Dateinamen und Speichermöglichkeit -->
				<div class="flex flex-row gap-2 mt-2">
					<div class="grow max-w-128">
						<svws-ui-text-input placeholder="Dateiname" :model-value="filenames[index]" @change="(value) => filenames[index] = value ?? ''" type="text" required />
					</div>
					<svws-ui-button type="primary" :disabled="(value === null) || (filenames[index].trim() === '')" @click="() => onSave(index)">
						Speichern
					</svws-ui-button>
					<div class="grow" />
				</div>

				<!-- Visualisierung der zuvor vom SVWS-Server geladenen Daten -->
				<div class="h-full w-full overflow-scroll grow bg-ui border border-ui-secondary text-ui rounded-md text-base pt-2 pl-2 pr-6 pb-6">
					<pre>{{ value }}</pre>
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Ref} from 'vue';
	import { computed, onMounted, ref, shallowRef } from 'vue';
	import type { SchuleDatenaustauschUntisExporteProps } from './SSchuleDatenaustauschUntisExporteProps';
	import { GostHalbjahr, Schulform } from '@core';

	const props = defineProps<SchuleDatenaustauschUntisExporteProps>();

	type GPU = {
		title: string,
		files: string[],
		export: (gpu002? : string, blockungsergebnisse? : number[]) => Promise<string[]>,
	};

	const klassenGPU003 = <GPU>({
		title: 'Klassenliste',
		files: [ 'GPU003.txt' ],
		export: async () => await props.exportUntisKlassenGPU003(),
	});

	const lehrerGPU004 = <GPU>({
		title: 'Lehrerliste',
		files: [ 'GPU004.txt' ],
		export: async () => await props.exportUntisLehrerGPU004(),
	});

	const faecherGPU006 = <GPU>({
		title: 'Fächer- und Kursliste',
		files: [ 'GPU006.txt' ],
		export: async () => await props.exportUntisFaecherGPU006(),
	});

	const schuelerGPU010 = <GPU>({
		title: 'Schülerliste',
		files: [ 'GPU010.txt' ],
		export: async () => await props.exportUntisSchuelerGPU010(sidvariante.value),
	});

	const fachwahlenGPU015 = <GPU>({
		title: 'Fachwahlen',
		files: [ 'GPU015.txt' ],
		export: async (gpu002 : string) => await props.exportUntisFachwahlenGPU015(sidvariante.value, gpu002),
	});

	const klausurenGPU017 = <GPU>({
		title: 'Klausuren',
		files: [ 'GPU017.txt' ],
		export: async (gpu002 : string) => await props.exportUntisKlausurenGPU017(sidvariante.value, gpu002),
	});

	const schienenGPU019 = <GPU>({
		title: 'Schienen',
		files: [ 'GPU019.txt' ],
		export: async (gpu002 : string) => await props.exportUntisSchienenGPU019(gpu002),
	});

	const blockungGPUs = <GPU>({
		title: 'Blockung',
		files: [ 'GPU002.txt', 'GPU015.txt', 'GPU019.txt' ],
		export: async (gpu002 : string, blockungsergebnisse : number[]) => await props.exportUntisBlockung(sidvariante.value, gpu002, blockungsergebnisse),
	});

	const sidvariante = ref<number>(1);
	async function setSIDVariante(value: number | string | boolean | object) {
		if (typeof value !== 'string')
			return;
		sidvariante.value = parseInt(value);
		if (gpusBrauchenGPU002.includes(aktuell.value)) {
			if (gpu002.value !== null)
				daten.value = await aktuell.value.export(gpu002.value);
		} else {
			daten.value = await aktuell.value.export();
		}
	}

	const aktuell = shallowRef<GPU>(klassenGPU003);
	const filenames = ref<string[]>([ ]);
	const daten = ref<string[] | null>(null);
	const gpu002 = shallowRef<string | null>(null);
	interface AbijahrgangsBlockungsinfos {
		abijahr : number,
		halbjahr : GostHalbjahr,
		idBlockung : number | null,
		idErgebnis : number | null,
		auswahl: Ref<boolean>,
	}
	const blockungsinfos = shallowRef<Array<AbijahrgangsBlockungsinfos>>([]);
	const blockungsergebnisse = computed<number[]>(() => blockungsinfos.value.map(info => info.auswahl.value ? info.idErgebnis : null).filter(id => id !== null));

	const gpus = [ klassenGPU003, lehrerGPU004, faecherGPU006, schuelerGPU010, fachwahlenGPU015, klausurenGPU017, schienenGPU019, blockungGPUs ];
	const gpusBrauchenGPU002 = [ blockungGPUs, fachwahlenGPU015, klausurenGPU017, schienenGPU019 ];
	const gpusHabenSchueler = [ schuelerGPU010, fachwahlenGPU015, klausurenGPU017, blockungGPUs ];
	const zeigeSchuelerVariantenAuswahl = computed<boolean>(() => gpusHabenSchueler.includes(aktuell.value));
	const zeigeGPU002Laden = computed<boolean>(() => gpusBrauchenGPU002.includes(aktuell.value));
	const zeigeDateiSpeichern = computed<boolean>(() => (daten.value !== null) && (!zeigeGPU002Laden.value || (gpu002.value !== null)));

	onMounted(() => onSelect(aktuell.value));

	function isVisible(gpu: GPU) {
		if (gpu === blockungGPUs)
			return Schulform.getListMitGymOb(props.schuljahresabschnitt().schuljahr).contains(props.schulform);
		return true;
	}

	async function updateAbiturjahrgaenge(gpu : GPU) {
		if (gpu === blockungGPUs) {
			const schuljahr = props.schuljahresabschnitt().schuljahr;
			const abschnitt = props.schuljahresabschnitt().abschnitt;
			const abijahrgaenge = [ schuljahr + 1, schuljahr + 2, schuljahr + 3 ];
			const blockungslisten = await props.ladeBlockungslisten(abijahrgaenge);
			const infos = new Array<AbijahrgangsBlockungsinfos>();
			for (let index = 0; index < abijahrgaenge.length; index++) {
				const abijahr = abijahrgaenge[index];
				const info = <AbijahrgangsBlockungsinfos> {
					abijahr: abijahr,
					halbjahr: GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abijahr, schuljahr, abschnitt),
					idBlockung: null,
					idErgebnis: null,
					auswahl: ref(false),
				}
				const blockungsliste = blockungslisten[index];
				for (const blockung of blockungsliste) {
					if (blockung.istAktiv) {
						info.idBlockung = blockung.id;
						info.idErgebnis = blockung.idAktivesErgebnis;
						info.auswahl.value = (blockung.idAktivesErgebnis !== null)
						break;
					}
				}
				infos.push(info);
			}
			blockungsinfos.value = infos;
			return;
		}
		blockungsinfos.value = [];
	}

	async function onSelect(gpu : GPU): Promise<void> {
		aktuell.value = gpu;
		// Initialisiere die Dateinamen für den Ouput
		filenames.value = new Array<string>(gpu.files.length);
		for (let i = 0; i < gpu.files.length; i++)
			filenames.value[i] = gpu.files[i];
		// Setze die Daten auf null, falls die GPU002 noch geladen werden muss
		if (gpusBrauchenGPU002.includes(aktuell.value))
			daten.value = null;
		else
			daten.value = await gpu.export();
		gpu002.value = null;
		await updateAbiturjahrgaenge(gpu);
	}

	async function importGPU002(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		gpu002.value = await file.text();
		if (aktuell.value !== blockungGPUs)
			daten.value = await aktuell.value.export(gpu002.value);
		if ((aktuell.value === blockungGPUs) && (blockungsergebnisse.value.length > 0))
			daten.value = await aktuell.value.export(gpu002.value, blockungsergebnisse.value);
	}

	async function setBlockungsergebnis(info: AbijahrgangsBlockungsinfos, value: boolean) {
		info.auswahl.value = value;
		daten.value = ((blockungsergebnisse.value.length === 0) || (gpu002.value === null))
			? null
			: await aktuell.value.export(gpu002.value, blockungsergebnisse.value);
	}

	function onSave(index : number): void {
		if ((daten.value === null) || (filenames.value[index].trim() === ''))
			return;
		const link = document.createElement("a");
		const blob = new Blob([ daten.value[index] ], { type : 'plain/text' });
		link.href = URL.createObjectURL(blob);
		link.download = filenames.value[index];
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
