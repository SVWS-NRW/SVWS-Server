<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>Pausenzeiten importieren</template>
		<template #modalContent>
			Es werden nur Pausenzeiten importiert, deren Start- und Endzeiten noch nicht im Katalog vertreten sind.
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

	const show = ref<boolean>(false);

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const file = ref<File | null>(null);

	const openModal = () => {
		show.value = true;
	}

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files)
			file.value = target.files[0];
		loading.value = false;
		status.value = undefined;
	}

	async function import_file() {
		if (!file.value)
			return;
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
