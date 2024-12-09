<template>
	<svws-ui-modal :show @update:show="value => emit('update:show', value)" size="small">
		<template #modalTitle>Laufbahnplanungsdaten importieren</template>
		<template #modalContent>
			<input type="file" accept=".lp" :multiple="multiple" @change="import_file" :disabled="loading">
			<svws-ui-spinner :spinning="loading" />
			<br>{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('update:show', false)">{{ status === true ? 'Schließen':'Abbrechen' }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from 'vue';
	import type { SimpleOperationResponse } from '../../../../../core/src/core/data/SimpleOperationResponse';

	const props = defineProps<{
		show: boolean;
		importLaufbahnplanung: (data: FormData) => Promise<boolean|SimpleOperationResponse>;
		multiple?: boolean;
	}>();

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const formData = new FormData();
		for (let i = 0; i < target.files.length; i++)
			formData.append("data", target.files[i]);
		loading.value = true;
		const res = await props.importLaufbahnplanung(formData);
		status.value = typeof res === 'boolean' ? res : res.success;
		loading.value = false;
		if (status.value === true)
			emit('update:show', false);
	}

</script>
