<template>
	<svws-ui-content-card title="SQLite-Datenbank auswählen">
		<div class="content-wrapper">
			<input type="file" @change="import_file" :disabled="loading">
			<svws-ui-spinner :spinning="loading" />
			{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import {ref} from "vue";

	const props = defineProps<{
		migrateDB: (data: FormData) => Promise<boolean>;
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
		status.value = await props.migrateDB(formData);
		loading.value = false;
	}
</script>

<style lang="postcss" scoped>
.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
