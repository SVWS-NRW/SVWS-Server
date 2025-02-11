<template>
	<ui-card icon="i-ri-device-recover-line" title="Backup importieren" subtitle="Ein SQLite-Backup wird in ein neues Schema wiederhergestellt" :is-open @update:is-open="(open) => emit('opened', open)">
		<div class="input-wrapper">
			<div class="space-y-2">
				<div class="font-bold text-button">Quell-Datenbank: SQLite-Datei (.sqlite) hochladen</div>
				<input type="file" @change="onFileChanged" :disabled="loadingFunction().value" accept=".sqlite">
			</div>
			<svws-ui-spacing />
			<div class="font-bold text-button">Ziel-Datenbank (wird erstellt):</div>
			<svws-ui-text-input v-model.trim="schema" required placeholder="Schemaname" />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :valid="validatorUsername" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" type="password" />
		</div>
		<template #buttonFooterLeft>
			<svws-ui-button :disabled="(schema.length === 0) || (user.length === 0) || (password.length === 0) || (user === 'root') || loadingFunction().value" title="Schema anlegen" @click="actionFunction" :is-loading="loadingFunction().value" class="mt-4">
				<svws-ui-spinner v-if="loadingFunction().value" spinning />
				<span v-else class="icon i-ri-play-line" />
				Schema anlegen
			</svws-ui-button>
		</template>
	</ui-card>
</template>

<script setup lang="ts">

	import { ref, type ShallowRef } from "vue";
	import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";

	const props = defineProps<{
		importSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		validatorUsername: (username: string | null) => boolean;
		isOpen: boolean;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
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
