<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Schema importieren</template>
		<template #modalContent>
			<div class="flex flex-col items-start gap-3">
				<div class="flex flex-row gap-16">
					<div class="flex flex-col gap-3 w-128 text-left">
						<div><b>Quell-Datenbank: </b> SQLite-Datei auswählen (Endung .sqlite)</div>
						<input type="file" @change="onFileChanged" :disabled="loading" accept=".sqlite">
					</div>
					<div class="flex flex-col gap-3 w-128 text-left">
						<div><b>Ziel-Datenbank (wird erstellt):</b></div>
						<svws-ui-text-input v-model="schema" required placeholder="Schemaname" />
						<svws-ui-spacing />
						<svws-ui-text-input v-model="user" required placeholder="Benutzername" />
						<svws-ui-text-input v-model="password" required placeholder="Passwort" type="password" />
					</div>
				</div>
				<svws-ui-spacing />
			</div>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="add" :disabled="schema.length === 0 || user.length === 0 || loading">
					<svws-ui-spinner :spinning="loading" />
					Schema anlegen
				</svws-ui-button>
				<svws-ui-button type="secondary" @click="close" :disabled="loading"> Abbrechen </svws-ui-button>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="clear" title="Verwerfe das Ergebnis des letzten Importversuchs"> Log verwerfen </svws-ui-button>
				<svws-ui-button type="secondary" @click="close"> Schließen </svws-ui-button>
			</template>
		</template>
		<template #modalLogs>
			<log-box :logs="logs" :status="status" />
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		importSchema:  (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
	}>();

	const schema = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');
	const file = ref<File | null>(null);


	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();
	const status = ref<boolean>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const openModal = () => {
		showModal().value = true;
	}

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target && target.files) {
			file.value = target.files[0];
		}
		clear();
	}

	async function add(event: Event) {
		if (file.value === null)
			return;
		loading.value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		formData.append('schemaUsername', user.value);
		formData.append('schemaUserPassword', password.value);
		const result = await props.importSchema(formData, schema.value);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
		schema.value = '';
		user.value = '';
		password.value = '';
	}

	function clear() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	function reset() {
		clear();
		schema.value = '';
		user.value = '';
		password.value = '';
	}

	function close() {
		showModal().value = false;
		reset();
	}

</script>
