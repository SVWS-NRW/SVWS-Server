<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" size="medium">
		<template #modalTitle>Räume importieren</template>
		<template #modalContent>
			<div class="text-left">
				<p>Es werden nur Räume importiert, deren Kürzel noch nicht im Katalog vertreten sind.</p>
				<p v-if="file === null">Hierbei wird das JSON-Format verwendet:</p>
				<p v-else>Vorschau der ausgewählten Datei:</p>
			</div>
			<code-box :code status :backticks="false" class="overflow-y-auto overflow-x-hidden break-words" />
			<div class="mt-2" />
			<svws-ui-input-wrapper :grid="2">
				<label class="svws-ui-label" for="uv-raum-import-json">JSON-Datei</label>
				<input id="uv-raum-import-json" type="file" accept=".json" @change="onFileChanged" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" :disabled="file === null" @click="import_file"> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	import { useUvState } from "@ui/states/UvState";

	const state = useUvState();

	const code = ref<string>("");

	const show = ref<boolean>(false);

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const file = ref<File | null>(null);

	const openModal = () => {
		file.value = null;
		show.value = true;
		code.value = `[
  {
    "id": 8, // wird entfernt, daher optional
    "kuerzel": "T1",
    "beschreibung": "Turnhalle 1",
    "groesse": 30,
	"gueltigVon": "2023-01-01", // optional, sonst ab heute
	"gueltigBis": null // optional, null heißt unbegrenzt gültig
  },
  {
	"id": 9,
	"kuerzel": "T2",
	"beschreibung": "Turnhalle 2",
	"groesse": 25,
]`;
	};

	async function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files !== null && target.files.length > 0) {
			file.value = target.files[0];
			code.value = await file.value.text();
		}
		loading.value = false;
		status.value = undefined;
	}

	async function import_file() {
		if (file.value === null) {
			return;
		}
		status.value = undefined;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file.value);
		try {
			await state.setUvRaeumeImportJSON(formData);
			show.value = false;
		} catch (e) {
			console.log(e);
		} finally {
			loading.value = false;
			file.value = null;
		}
	}

</script>
