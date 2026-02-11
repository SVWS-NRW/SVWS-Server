<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" size="medium">
		<template #modalTitle>Pausenzeiten importieren</template>
		<template #modalContent>
			<div class="text-left">
				Es werden nur Pausenzeiten importiert, deren Start- und Endzeiten noch nicht im Katalog vertreten sind.
				<br>Hierbei wird das JSON-Format verwendet:
			</div>
			<code-box :code status :backticks="false" />
			<div class="mt-2" />
			<svws-ui-input-wrapper :grid="2">
				<input type="file" accept=".json" @change="onFileChanged" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="import_file"> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = defineProps<{
		setKatalogPausenzeitenImportJSON: (formData: FormData) => Promise<void>;
	}>();

	const code = `[
  {
    // Die ID wird entfernt, daher optional
    "id": 1,
    // Der Wochentag beginnend bei 1 mit Montag
    "wochentag": 1,
    // Die Uhrzeit in Minuten, hier 9:35
    "beginn": 575,
    // bis 10:00
    "ende": 600,
    "bezeichnung": "1. Vormittagspause",
    // Die ID der Klassen, für die die Pause gelten soll (optional, daher [] übergeben)
    "klassen": [1, 2]
  }
]`;

	const show = ref<boolean>(false);

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const file = ref<File | null>(null);

	const openModal = () => {
		show.value = true;
	};

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files) {
			file.value = target.files[0];
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
			await props.setKatalogPausenzeitenImportJSON(formData);
			show.value = false;
		} catch (e) {
			console.log(e);
		} finally {
			loading.value = false;
			file.value = null;
		}
	}

</script>
