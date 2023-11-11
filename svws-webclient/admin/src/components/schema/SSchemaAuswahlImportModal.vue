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
						<input type="file" @change="onFileChanged" :disabled="loading">
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
			<svws-ui-button type="secondary" @click="showModal().value = false" :disabled="loading"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="schema.length === 0 || user.length === 0 || loading"> Schema anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = defineProps<{
		importSchema:  (formData: FormData, schema: string) => Promise<void>;
	}>();

	const schema = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');
	const file = ref<File | null>(null);


	const loading = ref<boolean>(false);

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
		await props.importSchema(formData, schema.value);
		schema.value = '';
		user.value = '';
		password.value = '';
		showModal().value = false;
		loading.value = false;
	}
</script>