<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Schema migrieren</template>
		<template #modalContent>
			<svws-ui-select v-model="migrationQuellinformationen().dbms" :items="items.keys()" :item-text="i => items.get(i) || ''" class="pb-8" />
			<div class="flex flex-col items-start gap-3">
				<div v-if="migrationQuellinformationen().dbms !== 'mdb'" class="flex flex-col gap-4 pb-4">
					<svws-ui-checkbox v-model="migrationQuellinformationen().schildzentral">mit Angabe einer Schulnummer bei Migration aus einer Schild-Zentral-Instanz</svws-ui-checkbox>
					<template v-if="migrationQuellinformationen().schildzentral">
						<svws-ui-text-input v-model="migrationQuellinformationen().schulnummer" placeholder="Schulnummer" />
					</template>
				</div>
				<div class="flex flex-row gap-16">
					<div v-if="migrationQuellinformationen().dbms !== 'mdb'" class="flex flex-col gap-3 w-128 text-left">
						<div><b>Quell-Datenbank:</b></div>
						<svws-ui-text-input v-model.trim="migrationQuellinformationen().location" placeholder="Datenbank-Host (hostname:port) oder (ip:port)" />
						<svws-ui-text-input v-model.trim="migrationQuellinformationen().schema" placeholder="Datenbank-Schema (z.B. schild_nrw)" />
						<svws-ui-text-input v-model.trim="migrationQuellinformationen().user" placeholder="Name des Datenbankbenutzers" />
						<svws-ui-text-input v-model.trim="migrationQuellinformationen().password" placeholder="Passwort des Datenbankbenutzers" type="password" />
					</div>
					<div v-else class="flex flex-col gap-3 w-128 text-left">
						<div><b>Quell-Datenbank: </b> Access-Datei auswählen (Endung .mdb)</div>
						<input type="file" @change="onFileChanged" :disabled="loading" accept=".mdb">
					</div>
					<div class="flex flex-col gap-3 w-128 text-left" v-if="targetSchema === undefined">
						<div><b>Ziel-Datenbank (wird erstellt):</b></div>
						<svws-ui-text-input v-model.trim="zielSchema" placeholder="Schema (wird erstellt, z.B. svwsdb)" />
						<svws-ui-text-input v-model.trim="zielUsername" placeholder="Name des Datenbankbenutzers" />
						<svws-ui-text-input v-model.trim="zielUserPassword" placeholder="Passwort des Datenbankbenutzers" type="password" />
					</div>
				</div>
			</div>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="migrate" :disabled="loading"> <svws-ui-spinner :spinning="loading" /> Migrieren </svws-ui-button>
				<svws-ui-button type="secondary" @click="close" :disabled="loading"> Abbrechen </svws-ui-button>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="clear" title="Verwerfe das Ergebnis des letzten Migrationsversuchs"> Log verwerfen </svws-ui-button>
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
		clear();
	}

	function clear() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	function reset() {
		clear();
		zielSchema.value = '';
		zielUserPassword.value = '';
		zielUsername.value = '';
	}

	function close() {
		showModal().value = false;
		reset();
	}

</script>
