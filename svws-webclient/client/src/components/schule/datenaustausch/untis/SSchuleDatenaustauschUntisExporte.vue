<template>
	<div class="page--content page--content--flex-row gap-2 h-full w-full overflow-hidden">
		<!-- Auswahl des Untis-Exportes (linke Seite) -->
		<div class="h-full min-w-48 w-48 flex flex-col gap-2">
			<svws-ui-button v-for="gpu in gpus" :key="gpu.title" :type="(aktuell === gpu) ? 'primary' : 'secondary'" @click="onSelect(gpu)">
				<div class="flex flex-col gap-1">
					<p class="text-left font-bold ">{{ gpu.title }}</p>
					<p class="text-left font-normal">{{ gpu.subtitle }}</p>
				</div>
			</svws-ui-button>
		</div>

		<!-- Weitere Eingabemöglichkeiten für den zuvor gewählten Untis-Export (rechte Seite - spezielle Ansicht nach Auswahl) -->
		<div class="flex flex-col">
			<div v-if="gpusHabenSchueler.includes(aktuell)" class="max-w-196">
				<svws-ui-card :compact="true" :collapsible="false" title="Schema Schüler-IDs" content="Wählen Sie das Schema für das Erzeugen der Schüler-IDs in Untis:">
					<template #footer>
						<svws-ui-radio-group>
							<svws-ui-radio-option value="1" v-model="sidvariante" @update:model-value="setSIDVariante" name="radioInputSchueleridschema" label="SVWS ID-Schema (S-SVWSID)" />
							<svws-ui-radio-option value="2" v-model="sidvariante" @update:model-value="setSIDVariante" name="radioInputSchueleridschema" label="Untis ID-Schema kurz (Nachname-Vor-20081023)" />
							<svws-ui-radio-option value="3" v-model="sidvariante" @update:model-value="setSIDVariante" name="radioInputSchueleridschema" label="Untis ID-Schema lang (Nachname-Vorname-20081023)" />
						</svws-ui-radio-group>
					</template>
				</svws-ui-card>
			</div>
			<div v-if="gpusBrauchenGPU002.includes(aktuell)" class="max-w-196">
				<svws-ui-card :compact="true" :collapsible="false" title="Upload GPU002.txt" show-divider>
					<template #content>
						<div class="text-justify">
							Laden Sie jetzt die Datei <span class="font-bold">GPU002.txt</span> von Untis hier hoch, um die Klausuren anschließend zu exportieren.
							Die CSV-Datei muss als Textkodierung UTF-8 verwenden. Als Trennzeichen wird das Semikolon verwendet und für die textbegrenzung doppelte Anführungszeichen (").
						</div>
					</template>
					<template #footer>
						<input type="file" accept=".txt" @change="importGPU002">
					</template>
				</svws-ui-card>
			</div>
		</div>
		<div v-if="(daten !== null) && (!gpusBrauchenGPU002.includes(aktuell) || (gpu002 !== null))" class="w-full h-full overflow-hidden flex flex-col gap-4">
			<!-- Angabe des Dateinamen und Speichermöglichkeit -->
			<div class="flex flex-row gap-2 mt-2">
				<div class="grow max-w-128">
					<svws-ui-text-input placeholder="Dateiname" :model-value="filename" @change="(value) => filename = value ?? ''" type="text" required />
				</div>
				<svws-ui-button type="primary" :disabled="(daten === null) || (filename.trim() === '')" @click="() => onSave()">
					Speichern
				</svws-ui-button>
				<div class="grow" />
			</div>

			<!-- Visualisierung der zuvor vom SVWS-Server geladenen Daten -->
			<div class="h-full w-full overflow-scroll grow bg-ui border border-ui-secondary text-ui rounded-md text-base pt-2 pl-2 pr-6 pb-6">
				<pre>{{ daten }}</pre>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, shallowRef } from 'vue';
	import type { SchuleDatenaustauschUntisExporteProps } from './SSchuleDatenaustauschUntisExporteProps';

	const props = defineProps<SchuleDatenaustauschUntisExporteProps>();

	type GPU = {
		title: string,
		subtitle: string,
		export: (gpu002? : string) => Promise<string>,
	};

	const klassenGPU003 = <GPU>({
		title: 'Klassenliste',
		subtitle: 'GPU003.txt',
		export: async () => await props.exportUntisKlassenGPU003(),
	});

	const lehrerGPU004 = <GPU>({
		title: 'Lehrerliste',
		subtitle: 'GPU004.txt',
		export: async () => await props.exportUntisLehrerGPU004(),
	});

	const faecherGPU006 = <GPU>({
		title: 'Fächer- und Kursliste',
		subtitle: 'GPU006.txt',
		export: async () => await props.exportUntisFaecherGPU006(),
	});

	const schuelerGPU010 = <GPU>({
		title: 'Schülerliste',
		subtitle: 'GPU010.txt',
		export: async () => await props.exportUntisSchuelerGPU010(sidvariante.value),
	});

	const fachwahlenGPU017 = <GPU>({
		title: 'Fachwahlen',
		subtitle: 'GPU015.txt',
		export: async (gpu002 : string) => await props.exportUntisFachwahlenGPU015(sidvariante.value, gpu002),
	});

	const klausurenGPU017 = <GPU>({
		title: 'Klausuren',
		subtitle: 'GPU017.txt',
		export: async (gpu002 : string) => await props.exportUntisKlausurenGPU017(sidvariante.value, gpu002),
	});

	const schienenGPU019 = <GPU>({
		title: 'Schienen',
		subtitle: 'GPU019.txt',
		export: async (gpu002 : string) => await props.exportUntisSchienenGPU019(gpu002),
	});

	const gpus = [ klassenGPU003, lehrerGPU004, faecherGPU006, schuelerGPU010, fachwahlenGPU017, klausurenGPU017, schienenGPU019 ];

	const gpusBrauchenGPU002 = [ fachwahlenGPU017, klausurenGPU017, schienenGPU019 ];

	const gpusHabenSchueler = [ schuelerGPU010, fachwahlenGPU017, klausurenGPU017 ];
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
	const filename = ref<string>('');
	const gpu002 = shallowRef<string | null>(null);
	const daten = shallowRef<string | null>(null);

	async function onSelect(gpu : GPU): Promise<void> {
		aktuell.value = gpu;
		filename.value = gpu.subtitle;
		if (gpusBrauchenGPU002.includes(aktuell.value))
			daten.value = null;
		else
			daten.value = await gpu.export();
		gpu002.value = null;
	}

	async function importGPU002(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		gpu002.value = await file.text();
		daten.value = await aktuell.value.export(gpu002.value);
	}

	function onSave(): void {
		if ((daten.value === null) || (filename.value.trim() === ''))
			return;
		const link = document.createElement("a");
		const blob = new Blob([daten.value], { type : 'plain/text' });
		link.href = URL.createObjectURL(blob);
		link.download = filename.value;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
