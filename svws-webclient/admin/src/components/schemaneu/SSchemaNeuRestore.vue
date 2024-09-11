<template>
	<svws-ui-action-button title="Backup importieren" description="Ein SQLite-Backup wird in ein neues Schema wiederhergestellt" icon="i-ri-device-recover-line" action-label="Schema anlegen" :action-function :action-disabled="(schema.length === 0) || (user.length === 0) || (password.length === 0) || (user === 'root')" :is-loading="loadingFunction().value" :is-active>
		<div class="input-wrapper">
			<div class="flex flex-col gap-2 mb-2">
				<div class="font-bold text-button">Quell-Datenbank: SQLite-Datei (.sqlite) hochladen</div>
				<input type="file" @change="onFileChanged" :disabled="loadingFunction().value" accept=".sqlite">
			</div>
			<svws-ui-spacing />
			<div class="font-bold text-button">Ziel-Datenbank (wird erstellt):</div>
			<svws-ui-text-input v-model.trim="schema" required placeholder="Schemaname" />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :valid="validatorUsername" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" type="password" />
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { ref, type ShallowRef } from "vue";
	import type { List, SimpleOperationResponse } from "@core";
	import type { InputDataType } from "@ui";

	const props = defineProps<{
		importSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		validatorUsername: (username: InputDataType) => boolean;
		isActive: boolean;
	}>();

	const schema = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');
	const file = ref<File | null>(null);

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files) {
			file.value = target.files[0];
		}
	}

	async function actionFunction() {
		if (file.value === null)
			return;
		props.loadingFunction().value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		formData.append('schemaUsername', user.value);
		formData.append('schemaUserPassword', password.value);
		const result = await props.importSchema(formData, schema.value);
		props.logsFunction().value = result.log;
		props.statusFunction().value = result.success;
		props.loadingFunction().value = false;
		schema.value = '';
		user.value = '';
		password.value = '';
	}

</script>
