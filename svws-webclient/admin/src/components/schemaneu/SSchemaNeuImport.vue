<template>
	<div class="input-wrapper">
		<div class="flex flex-row gap-16">
			<div class="flex flex-col gap-3 w-128 text-left">
				<div><b>Quell-Datenbank: </b> SQLite-Datei auswählen (Endung .sqlite)</div>
				<input type="file" @change="onFileChanged" :disabled="loading().value" accept=".sqlite">
			</div>
			<div class="flex flex-col gap-3 w-128 text-left">
				<div><b>Ziel-Datenbank (wird erstellt):</b></div>
				<svws-ui-text-input v-model.trim="schema" required placeholder="Schemaname" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :valid="value => value !== 'root'" />
				<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" type="password" />
			</div>
		</div>
		<svws-ui-spacing />
		<svws-ui-button type="primary" @click="doImport" :disabled="(schema.length === 0) || (user.length === 0) || (password.length === 0) || loading().value || (user === 'root')">
			<svws-ui-spinner :spinning="loading().value" />
			Schema anlegen
		</svws-ui-button>
	</div>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { ref, type ShallowRef } from "vue";

	const props = defineProps<{
		importSchema:  (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
		logs: () => ShallowRef<List<string | null> | undefined>;
		status: () => ShallowRef<boolean | undefined>;
		loading: () => ShallowRef<boolean>;
	}>();

	const schema = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');
	const file = ref<File | null>(null);

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target && target.files) {
			file.value = target.files[0];
		}
	}

	async function doImport(event: Event) {
		if (file.value === null)
			return;
		props.loading().value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		formData.append('schemaUsername', user.value);
		formData.append('schemaUserPassword', password.value);
		const result = await props.importSchema(formData, schema.value);
		props.logs().value = result.log;
		props.status().value = result.success;
		props.loading().value = false;
		schema.value = '';
		user.value = '';
		password.value = '';
	}

</script>
