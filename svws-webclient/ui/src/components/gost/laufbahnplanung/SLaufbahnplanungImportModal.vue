<template>
	<svws-ui-modal :show @update:show="value => doShow(value)" size="small">
		<template #modalTitle>Laufbahnplanungsdaten importieren</template>
		<template #modalContent>
			<input type="file" accept=".lp" :multiple @change="importFile" :disabled="loading">
			<div v-if="failed" class="mt-4"> Fehler beim Upload </div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="doShow(false)">{{ failed === true ? 'Schließen':'Abbrechen' }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from 'vue';

	const props = defineProps<{
		show: boolean;
		importLaufbahnplanung: (data: FormData) => Promise<void>;
		multiple?: boolean;
	}>();

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	const failed = ref<boolean>(false);
	const loading = ref<boolean>(false);

	function doShow(value: boolean) {
		failed.value = false;
		emit('update:show', value);
	}

	async function importFile(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const formData = new FormData();
		for (let i = 0; i < target.files.length; i++)
			formData.append("data", target.files[i], "lpfile" + (i+1));
		try {
			await props.importLaufbahnplanung(formData);
			emit('update:show', false);
		} catch (e) {
			failed.value = true;
			throw e;
		}
	}

</script>
