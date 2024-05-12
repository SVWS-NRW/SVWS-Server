<template>
	<svws-ui-action-button title="Backup" description="Daten werden aus einem Backup wiederhergestellt" icon="i-ri-device-recover-line">
		<div class="flex flex-col gap-2 text-left">
			<span class="font-bold text-button">SQLite-Datenbank hochladen</span>
			<input type="file" @change="import_file" :disabled="loading" accept=".sqlite">
			<svws-ui-spinner :spinning="loading" />
			<div class="font-bold text-sm">
				{{
					status === false
						? "Fehler beim Upload"
						: status === true
							? "Upload erfolgreich"
							: ""
				}}
			</div>
		</div>
	</svws-ui-action-button>
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
		formData.append("database", file);
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
