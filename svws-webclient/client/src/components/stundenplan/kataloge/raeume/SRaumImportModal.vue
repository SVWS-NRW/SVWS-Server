<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Räume importieren</template>
		<template #modalContent>
			Es werden nur Räume importiert, deren Kürzel noch nicht im Katalog vertreten sind.
			<svws-ui-input-wrapper :grid="2">
				<input type="file" accept=".json" @change="onFileChanged" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="import_file"> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = defineProps<{
		setKatalogRaeumeImportJSON: (formData: FormData) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const file = ref<File | null>(null);

	const openModal = () => {
		showModal().value = true;
	}

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files) {
			file.value = target.files[0];
		}
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
			await props.setKatalogRaeumeImportJSON(formData);
			showModal().value = false;
		} catch (e) {
			console.log(e);
		} finally {
			loading.value = false;
			file.value = null;
		}
	}

</script>
