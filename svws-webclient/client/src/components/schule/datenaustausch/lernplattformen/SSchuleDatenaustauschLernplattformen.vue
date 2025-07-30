<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<svws-ui-header>
			<span class="inline-block mr-3">Datenexport für Lernplattformen</span>
			<br>
			<span class="opacity-50 flex">
				<span class="i-ri-download-2-line icon-xl" />
			</span>
		</svws-ui-header>
		<div class="page page-flex-row">
			<svws-ui-input-wrapper :grid="4">
				<div class="flex flex-col gap-2">
					<ui-select title="Lernplattform" label="Lernplattform" :manager="selectManagerLernplattformen" v-model="lernplattform"
						:removable="false" />
					<ui-select title="Datenformat" label="Datenformat" :manager="selectManagerDatenformat" v-model="datenformat" :removable="false" />
					<svws-ui-button type="primary" size="big" :disabled="exportDisabled" @click="startExport">
						Starte Export
						<svws-ui-spinner :spinning="loading" />
					</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuleDatenaustauschLernplattformenProps } from "~/components/schule/datenaustausch/lernplattformen/SSchuleDatenaustauschLernplattformenProps";
	import type { Lernplattform } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { BaseSelectManager } from "@ui";

	const props = defineProps<SchuleDatenaustauschLernplattformenProps>();

	const loading = ref<boolean>(false);
	const lernplattform = ref<Lernplattform | undefined>(undefined);
	const datenformat = ref<string>('JSON');

	const selectManagerLernplattformen = new BaseSelectManager<Lernplattform>({
		removable: false, options: props.lernplattformen,
		selectionDisplayText: (lernplattform: Lernplattform) => lernplattform.id + " - " + lernplattform.bezeichnung,
		optionDisplayText: (lernplattform: Lernplattform) => lernplattform.id + " - " + lernplattform.bezeichnung,
	})
	const selectManagerDatenformat = new BaseSelectManager<string>({ removable: false, options: ['JSON', 'GZIP'], selected: 'JSON' });

	const hatKompetenzExport = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.IMPORT_EXPORT_LERNPLATTFORM));
	const exportDisabled = computed(() => (lernplattform.value === undefined) || !hatKompetenzExport.value || loading.value);

	async function startExport() {
		if (exportDisabled.value)
			return

		loading.value = true;
		const blob = await props.export(lernplattform.value!, datenformat.value);
		if (blob !== null) {
			const url = URL.createObjectURL(blob);
			let filename = `LernplattformExport-${lernplattform.value!.id}_${lernplattform.value!.bezeichnung}.json`;
			if (datenformat.value === 'GZIP')
				filename += '.gzip';
			const a = document.createElement("a");
			a.href = url;
			a.download = filename;
			a.click();
		}
		loading.value = false;
	}

</script>
