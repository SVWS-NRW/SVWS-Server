<template>
	<div class="page--content page--content--flex">
		<svws-ui-card v-for="gpu in gpus" :key="gpu.value.title" :title="gpu.value.title" :subtitle="gpu.value.subtitle" collapsible show-divider
			button-mode="text" button-container="footer" button-position="right" :on-save="() => onSave(gpu.value)" @open="onOpen(gpu.value)">
			<template #content>
				<pre>{{ gpu.value.daten }}</pre>
			</template>
			<template #footer>
				<span class="font-bold">Dateiname:</span>
				<svws-ui-text-input placeholder="Dateiname" :model-value="gpu.value.filename" type="text" headless />
			</template>
		</svws-ui-card>
	</div>
</template>

<script setup lang="ts">

	import { ref, onMounted } from 'vue';
	import type { SchuleDatenaustauschUntisExporteProps } from './SSchuleDatenaustauschUntisExporteProps';

	const props = defineProps<SchuleDatenaustauschUntisExporteProps>();

	type GPU = {
		title: string,
		subtitle: string,
		daten: string | null,
		filename : string,
		export: () => Promise<string>,
	};

	const klassenGPU003 = ref<GPU>({
		title: 'Klassenliste',
		subtitle: 'Export im Untis-Format GPU003.txt',
		daten: null,
		filename: 'GPU003.txt',
		export: async () => await props.exportUntisKlassenGPU003(),
	});

	const lehrerGPU004 = ref<GPU>({
		title: 'Lehrerliste',
		subtitle: 'Export im Untis-Format GPU004.txt',
		daten: null,
		filename: 'GPU004.txt',
		export: async () => await props.exportUntisLehrerGPU004(),
	});

	const faecherGPU006 = ref<GPU>({
		title: 'Fächer- und Kursliste',
		subtitle: 'Export im Untis-Format GPU006.txt',
		daten: null,
		filename: 'GPU006.txt',
		export: async () => await props.exportUntisFaecherGPU006(),
	});

	const schuelerGPU010 = ref<GPU>({
		title: 'Schülerliste',
		subtitle: 'Export im Untis-Format GPU010.txt',
		daten: null,
		filename: 'GPU010.txt',
		export: async () => await props.exportUntisSchuelerGPU010(),
	});

	const gpus = [ klassenGPU003, lehrerGPU004, faecherGPU006, schuelerGPU010 ];

	async function onOpen(gpu: GPU): Promise<void> {
		gpu.daten = await gpu.export();
	}

	function onSave(gpu: GPU): void {
		if (gpu.daten === null)
			return;
		const link = document.createElement("a");
		const blob = new Blob([gpu.daten], { type : 'plain/text' });
		link.href = URL.createObjectURL(blob);
		link.download = gpu.filename;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
