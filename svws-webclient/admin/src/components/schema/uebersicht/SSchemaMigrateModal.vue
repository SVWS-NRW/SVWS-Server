<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Schema migrieren</template>
		<template #modalContent>
			<div class="flex items-start gap-3">
				<div class="flex flex-col gap-3">
					<svws-ui-select v-model="db" :items="items.keys()" :item-text="i => items.get(i) || ''" />
					<div class="flex flex-col gap-3" v-if="db !== 'mdb'">
						<svws-ui-checkbox v-model="schildzentral">mit Angabe einer Schulnummer bei Migration aus einer Schild-Zentral-Instanz</svws-ui-checkbox>
						<svws-ui-text-input v-if="schildzentral" v-model="schulnummer" placeholder="Schulnummer" />
						<svws-ui-text-input v-model="location" placeholder="Datenbank-Host" />
						<svws-ui-text-input v-model="schema" placeholder="Datenbank-Schema" />
						<svws-ui-text-input v-model="user" placeholder="Datenbankbenutzer" />
						<svws-ui-text-input v-model="password" placeholder="Passwort Datenbankbenutzer" />
					</div>
					<div class="flex flex-col gap-3" v-else>
						<svws-ui-text-input v-model="password" placeholder="Datenbank-Passwort" />
						Access-Datei auswählen (Endung .mdb):
						<input type="file" @change="onFileChanged" :disabled="loading" accept=".mdb">
					</div>
					<div class="flex flex-col gap-3" v-if="targetSchema === undefined">
						<svws-ui-text-input v-model="zielSchema" placeholder="Schema, das erstellt werden soll" />
						<svws-ui-text-input v-model="zielUsername" placeholder="Benutzername für das neue Schema" />
						<svws-ui-text-input v-model="zielUserPassword" placeholder="Benutzerpasswort für das neue Schema" />
					</div>
				</div>
				<svws-ui-spinner :spinning="loading" />
			</div>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="close" :disabled="loading"> Abbrechen </svws-ui-button>
				<svws-ui-button type="secondary" @click="migrate" :disabled="loading"> Migrieren </svws-ui-button>
			</template>
			<template v-else>
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
		migrateSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
		targetSchema?: string;
	}>();

	const items = new Map<string, string>();

	items.set('mysql', 'MySQL');
	items.set('mariadb', 'MariaDB');
	items.set('mssql', 'MSSQL');
	items.set('mdb', 'Access (MDB)');

	const db = ref<string>('mdb');
	const schildzentral = ref(false);
	const schulnummer = ref("");
	const location = ref("");
	const schema = ref("");
	const user = ref("");
	const password = ref("");
	// eslint-disable-next-line vue/no-setup-props-destructure
	const zielSchema = ref("");
	const zielUsername = ref("");
	const zielUserPassword = ref("");
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();
	const status = ref<boolean>();

	async function migrate() {
		loading.value = true;
		const formData = new FormData();
		if (file.value !== null) {
			formData.append("database", file.value);
			formData.append('databasePassword', password.value);
		}
		formData.append('schema', props.targetSchema || zielSchema.value);
		formData.append('db', db.value);
		formData.append('srcUsername', user.value);
		formData.append('srcPassword', password.value);
		formData.append('srcSchema', schema.value);
		formData.append('srcLocation', location.value);
		formData.append('schulnummer', schulnummer.value);
		formData.append('schemaUsername', zielUsername.value);
		formData.append('schemaUserPassword', zielUserPassword.value);
		try {
			const result = await props.migrateSchema(formData);
			logs.value = result.log;
			status.value = result.success;
		} catch (e) {
			console.log(e);
			status.value = false;
		}
		loading.value = false;
		schema.value = '';
		user.value = '';
		password.value = '';
		location.value = '';
		schulnummer.value = '';
		zielSchema.value = '';
		zielUserPassword.value = '';
		zielUsername.value = '';
	}

	const file = ref<File | null>(null);

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

	function close() {
		showModal().value = false;
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
		schema.value = '';
		user.value = '';
		password.value = '';
		location.value = '';
		schulnummer.value = '';
		zielSchema.value = '';
		zielUserPassword.value = '';
		zielUsername.value = '';
	}
</script>
