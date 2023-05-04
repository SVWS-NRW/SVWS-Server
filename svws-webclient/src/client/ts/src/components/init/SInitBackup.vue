<template>
	<div class="modal--content">
		<div class="init-form-header mb-8 px-8 py-4 mt-6">
			<h1 class="leading-none text-center w-full">
				<span class="font-normal">Import Schild 2-Datenbank</span>
			</h1>
		</div>
		<svws-ui-content-card title="MDB-Datei auswählen">
			<div class="content-wrapper">
				<svws-ui-text-input v-model="password" type="password" placeholder="Passwort" />
				<input type="file" accept="" @change="import_file" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
				<br>{{
					status === false
						? "Fehler beim Upload"
						: status === true
							? "Upload erfolgreich"
							: ""
				}}
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { InitBackupProps } from "./SInitBackupProps";
	import {ref} from "vue";

	const props = defineProps<InitBackupProps>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const password = ref<string>("");
	const salt = ref<string>("");

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
		formData.append("password", password.value)
		status.value = await props.migrateBackup(formData);
		loading.value = false;
	}
</script>

<style lang="postcss" scoped>
.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
