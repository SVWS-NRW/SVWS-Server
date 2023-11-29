<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Schema importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-model="schema" required placeholder="Schemaname" />
					<div class="flex gap-3">
						SQLite-Datei auswählen:
						<input type="file" accept=".sqlite" @change="onFileChanged" :disabled="loading">
					</div>
					<template v-if="loading">
						<div class="flex">
							<svws-ui-spinner :spinning="true" /> Das Schema wird importiert…
						</div>
					</template>
					<svws-ui-spacing />
					<svws-ui-text-input v-model="user" required placeholder="Benutzername" />
					<svws-ui-text-input v-model="password" required placeholder="Passwort" />
				</svws-ui-input-wrapper>
			</div>
		</template>
		<template #modalActions>
			<template v-if="status !== true">
				<svws-ui-button type="secondary" @click="close" :disabled="loading"> Abbrechen </svws-ui-button>
				<svws-ui-button type="secondary" @click="add" :disabled="schema.length === 0 || user.length === 0 || loading"> Schema anlegen </svws-ui-button>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="close"> Schließen </svws-ui-button>
			</template>
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
		schema.value = '';
		user.value = '';
		password.value = '';
		showModal().value = false;
		loading.value = false;
	}

	function close() {
		showModal().value = false;
		logs.value = undefined;
		status.value = undefined;
	}
</script>
