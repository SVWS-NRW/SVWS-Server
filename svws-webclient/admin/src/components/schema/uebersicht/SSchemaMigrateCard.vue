<template>
	<svws-ui-content-card title="Schild-2 Schema migrieren">
		<svws-ui-select v-model="migrationQuellinformationen().dbms" :items="items.keys()" :item-text="i => items.get(i) || ''" title="Datenbank" class="mb-8" />
		<div class="flex flex-col items-start gap-3">
			<div v-if="migrationQuellinformationen().dbms !== 'mdb'" class="flex flex-col gap-2">
				<div class="flex flex-col gap-0.5 pl-3">
					<span class="opacity-50">Bei Migration aus einer Schild-Zentral-Instanz:</span>
					<svws-ui-checkbox class="text-left" type="toggle" v-model="migrationQuellinformationen().schildzentral">Schulnummer angeben</svws-ui-checkbox>
				</div>
			</div>
			<svws-ui-text-input v-if="migrationQuellinformationen().dbms !== 'mdb' && migrationQuellinformationen().schildzentral" v-model="migrationQuellinformationen().schulnummer" placeholder="Schulnummer" />
			<div class="flex flex-col gap-12 mt-5 w-full">
				<svws-ui-input-wrapper v-if="migrationQuellinformationen().dbms !== 'mdb'">
					<div v-if="targetSchema === undefined" class="text-button -mb-1 mt-2">Quell-Datenbank</div>
					<svws-ui-text-input v-model.trim="migrationQuellinformationen().location" placeholder="Datenbank-Host (hostname:port) oder (ip:port)" />
					<svws-ui-text-input v-model.trim="migrationQuellinformationen().schema" placeholder="Datenbank-Schema (z.B. schild_nrw)" />
					<svws-ui-text-input v-model.trim="migrationQuellinformationen().user" placeholder="Name des Datenbankbenutzers" />
					<svws-ui-text-input v-model.trim="migrationQuellinformationen().password" placeholder="Passwort des Datenbankbenutzers" type="password" />
				</svws-ui-input-wrapper>
				<div v-else class="flex flex-col gap-2 -mt-5 mb-5">
					<div class="font-bold text-button">Quell-Datenbank: Access-Datei (.mdb) hochladen</div>
					<input type="file" @change="onFileChanged" :disabled="loading" accept=".mdb">
				</div>
				<svws-ui-input-wrapper v-if="targetSchema === undefined">
					<div class="text-button -mb-1 mt-2">Ziel-Datenbank (wird erstellt)</div>
					<svws-ui-text-input v-model.trim="zielSchema" placeholder="Schema (wird erstellt, z.B. svwsdb)" />
					<svws-ui-text-input v-model.trim="zielUsername" placeholder="Name des Datenbankbenutzers" />
					<svws-ui-text-input v-model.trim="zielUserPassword" placeholder="Passwort des Datenbankbenutzers" type="password" />
				</svws-ui-input-wrapper>
			</div>
		</div>
		<div class="flex gap-1 items-start" :class="{'mt-12': status === undefined, 'mt-5 mb-12': status !== undefined}">
			<template v-if="status === undefined">
				<svws-ui-button @click="migrate" :disabled="loading"> <svws-ui-spinner :spinning="loading" /> Migrieren </svws-ui-button>
				<svws-ui-button type="secondary" @click="setCurrentAction('')" :disabled="loading"> Abbrechen </svws-ui-button>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="close"> Schließen </svws-ui-button>
			</template>
		</div>
		<log-box :logs="logs" :status="status">
			<template #button>
				<svws-ui-button v-if="status !== undefined" type="transparent" @click="clear" title="Verwerfe das Ergebnis des letzten Migrationsversuchs">Log verwerfen </svws-ui-button>
			</template>
		</log-box>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { shallowRef } from "vue";
	import type { SchemaMigrationQuelle } from "../SchemaMigrationQuelle";

	const props = defineProps<{
		migrateSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
		targetSchema?: string;
		migrationQuellinformationen: () => SchemaMigrationQuelle;
		setCurrentAction: (action: string) => void;
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
		props.setCurrentAction('');
		reset();
	}

</script>
