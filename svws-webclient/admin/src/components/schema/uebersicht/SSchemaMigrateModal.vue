<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Schema migrieren</template>
		<template #modalContent>
			<div class="flex items-start gap-3">
				<div class="flex flex-col gap-3">
					<svws-ui-select v-model="migrationQuellinformationen().dbms" :items="items.keys()" :item-text="i => items.get(i) || ''" />
					<div class="flex flex-col gap-3" v-if="migrationQuellinformationen().dbms !== 'mdb'">
						<svws-ui-checkbox v-model="migrationQuellinformationen().schildzentral">mit Angabe einer Schulnummer bei Migration aus einer Schild-Zentral-Instanz</svws-ui-checkbox>
						<svws-ui-text-input v-if="migrationQuellinformationen().schildzentral" v-model="migrationQuellinformationen().schulnummer" placeholder="Schulnummer" />
						<svws-ui-text-input v-model="migrationQuellinformationen().location" placeholder="Datenbank-Host" />
						<svws-ui-text-input v-model="migrationQuellinformationen().schema" placeholder="Datenbank-Schema" />
						<svws-ui-text-input v-model="migrationQuellinformationen().user" placeholder="Datenbankbenutzer" />
						<svws-ui-text-input v-model="migrationQuellinformationen().password" placeholder="Passwort Datenbankbenutzer" />
					</div>
					<div class="flex flex-col gap-3" v-else>
						<svws-ui-text-input v-model="migrationQuellinformationen().password" placeholder="Datenbank-Passwort" />
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
	import { shallowRef } from "vue";
	import type { SchemaMigrationQuelle } from "../SchemaMigrationQuelle";

	const props = defineProps<{
		migrateSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
		targetSchema?: string;
		migrationQuellinformationen: () => SchemaMigrationQuelle;
	}>();

	const items = new Map<string, string>();

	items.set('mysql', 'MySQL');
	items.set('mariadb', 'MariaDB');
	items.set('mssql', 'MSSQL');
	items.set('mdb', 'Access (MDB)');

	const zielSchema = shallowRef("");
	const zielUsername = shallowRef("");
	const zielUserPassword = shallowRef("");
	const loading = shallowRef<boolean>(false);
	const logs = shallowRef<List<string|null>>();
	const status = shallowRef<boolean>();

	async function migrate() {
		loading.value = true;
		const formData = new FormData();
		if (file.value !== null) {
			formData.append("database", file.value);
			formData.append('databasePassword', props.migrationQuellinformationen().password);
		}
		formData.append('schema', props.targetSchema || zielSchema.value);
		formData.append('db', props.migrationQuellinformationen().dbms);
		formData.append('srcUsername', props.migrationQuellinformationen().user);
		formData.append('srcPassword', props.migrationQuellinformationen().password);
		formData.append('srcSchema', props.migrationQuellinformationen().schema);
		formData.append('srcLocation', props.migrationQuellinformationen().location);
		formData.append('schulnummer', props.migrationQuellinformationen().schulnummer);
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
		zielSchema.value = '';
		zielUserPassword.value = '';
		zielUsername.value = '';
	}

	const file = shallowRef<File | null>(null);

	const _showModal = shallowRef<boolean>(false);
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
		zielSchema.value = '';
		zielUserPassword.value = '';
		zielUsername.value = '';
	}

</script>
