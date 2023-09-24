<template>
	<svws-ui-modal :show="show" size="small">
		<template #modalTitle>Laufbahnplanungsdaten importieren</template>
		<template #modalContent>
			<input type="file" accept=".lp" @change="import_file" :disabled="loading">
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

	import { type Ref, ref } from 'vue';

	const props = defineProps<{
		show: () => Ref<boolean>;
		importLaufbahnplanung: (data: FormData) => Promise<boolean>;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length)
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		status.value = await props.importLaufbahnplanung(formData);
		loading.value = false;
		if (status.value === true)
			props.show().value = false;
	}

</script>
