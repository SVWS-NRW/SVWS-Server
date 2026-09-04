<template>
	<svws-ui-header>
		<span class="inline-block mr-3">Datenaustausch mit dem Externen Notenmodul</span>
		<br>
		<span class="opacity-50 flex">
			<span class="i-ri-download-2-line icon-xl" />
			<span class="i-ri-upload-2-line icon-xl" />
		</span>
	</svws-ui-header>
	<div class="svws-ui-page w-full">
		<div class="svws-ui-tab-content">
			<div class="page page-flex-row">
				<div>
					<svws-ui-input-wrapper>
						<div class="text-headline-md">Lehrkraftdaten exportieren</div>
						<div class="flex items-center">
							<ui-select :manager="lehrerSelect" v-model="lehrer" />
							<div class="h-full p-3">
								<svws-ui-button v-if="lehrer !== null" @click="lehrerENM" class="min-w-64 h-full">ENM-Daten für einzelne Lehrkraft herunterladen</svws-ui-button>
							</div>
						</div>
					</svws-ui-input-wrapper>
					<div class="my-4">
						<div class="text-headline-md">Konferenzdaten exportieren</div>
						<svws-ui-button @click="gzipENM">Alle ENM-Daten als GZIP herunterladen</svws-ui-button>
					</div>
					<div class="text-headline-md">Lehrkraftdaten und Konferenzdaten importieren</div>
					<div class="col-span-full">
						ENM-Daten hochladen, erlaubt sind GZIP und JSON
						<br><input class="contentFocusField" type="file" accept=".gz, .json" @change="importFileENM" :disabled="loading">
						<svws-ui-spinner :spinning="loading" /> {{ importStatus ? 'Import erfolgreich' : '' }}
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { SchuleDatenaustauschENMProps } from './SSchuleDatenaustauschENMProps';
	import { ENMv2Daten } from '@core/core/data/enm/v2/ENMv2Daten';
	import type { LehrerListeEintrag } from '@core/core/data/lehrer/LehrerListeEintrag';
	import { SelectManager } from '@ui/ui/controls/select/manager/SelectManager';

	const props = defineProps<SchuleDatenaustauschENMProps>();
	const loading = ref<boolean>(false);
	const importStatus = ref<boolean | undefined>(undefined);
	const lehrer = ref<LehrerListeEintrag | null>(null);

	const lehrerSelect = new SelectManager({
		options: props.listLehrer,
		optionDisplayText: v => `${v.nachname}, ${v.vorname}`,
		selectionDisplayText: v => `${v.nachname}, ${v.vorname}`,
	});

	async function lehrerENM() {
		if (lehrer.value === null) {
			return;
		}
		const json = await props.exportLehrerENM(lehrer.value.id);
		const blob = new Blob([ENMv2Daten.transpilerToJSON(json)], { type: "application/json" });
		const url = URL.createObjectURL(blob);
		let filename = `ENMExport-${lehrer.value.nachname}_${lehrer.value.vorname}.json`;
		const a = document.createElement("a");
		a.href = url;
		a.download = filename;
		a.click();
		URL.revokeObjectURL(a.href);
	}

	async function gzipENM() {
		loading.value = true;
		try {
			const { data, name } = await props.exportGzipENM();
			const link = document.createElement("a");
			link.href = URL.createObjectURL(data);
			link.download = name;
			link.target = "_blank";
			link.click();
			URL.revokeObjectURL(link.href);
		} finally {
			loading.value = false;
		}
	}

	async function importFileENM(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0)) {
			return;
		}
		const file = target.files.item(0);
		if (!file) {
			return;
		}
		importStatus.value = undefined;
		loading.value = true;
		if (file.type === 'application/x-gzip') {
			const formData = new FormData();
			formData.append("data", file);
			await props.importGzipENM(formData);
		} else {
			await props.importENM(file);
		}
		loading.value = false;
		importStatus.value = true;
	}

</script>
