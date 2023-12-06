<template>
	<svws-ui-modal :show="show" size="small">
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
			<svws-ui-button type="secondary" @click="show().value = false">{{ status === true ? 'Schließen':'Abbrechen' }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { Ref } from 'vue';
	import { ref } from 'vue';
	import type { SimpleOperationResponse } from '@core';

	const props = defineProps<{
		show: () => Ref<boolean>;
		importLaufbahnplanung: (data: FormData) => Promise<boolean|SimpleOperationResponse>;
		multiple?: boolean;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length)
			return;
		const formData = new FormData();
		for (let i = 0; i < target.files.length; i++)
			formData.append("data", target.files[i]);
		loading.value = true;
		const res = await props.importLaufbahnplanung(formData);
		status.value = typeof res === 'boolean' ? res : res.success;
		loading.value = false;
		if (status.value === true)
			props.show().value = false;
	}

</script>
