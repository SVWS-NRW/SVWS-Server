<template>
	<div class="input-wrapper">
		<svws-ui-select v-model="migrationQuellinformationen().dbms" :items="items.keys()" :item-text="i => items.get(i) || ''" title="Datenbank-Typ" />
		<svws-ui-spacing />
		<template v-if="migrationQuellinformationen().dbms !== 'mdb'">
			<div class="font-bold text-button">Quell-Datenbank:</div>
			<svws-ui-text-input v-model.trim="migrationQuellinformationen().location" placeholder="Datenbank-Host (hostname:port) oder (ip:port)" />
			<svws-ui-text-input v-model.trim="migrationQuellinformationen().schema" placeholder="Datenbank-Schema (z.B. schild_nrw)" />
			<svws-ui-text-input v-model.trim="migrationQuellinformationen().user" placeholder="Name des Datenbankbenutzers" />
			<svws-ui-text-input v-model.trim="migrationQuellinformationen().password" placeholder="Passwort des Datenbankbenutzers" type="password" />
			<svws-ui-spacing />
			<svws-ui-checkbox v-model="migrationQuellinformationen().schildzentral">Migration aus einer Schild-2-Zentral-Instanz</svws-ui-checkbox>
			<template v-if="migrationQuellinformationen().schildzentral">
				<svws-ui-text-input v-model="migrationQuellinformationen().schulnummer" placeholder="zu migrierende Schulnummer" />
			</template>
		</template>
		<template v-else>
			<div class="font-bold text-button">Quell-Datenbank: Access-Datei (.mdb) hochladen</div>
			<input type="file" @change="onFileChanged" :disabled="loading().value" accept=".mdb">
		</template>
		<svws-ui-spacing />
		<div class="font-bold text-button mt-5">Ziel-Datenbank (wird erstellt):</div>
		<svws-ui-text-input v-model.trim="zielSchema" placeholder="Schema (wird erstellt, z.B. svwsdb)" />
		<svws-ui-text-input v-model.trim="zielUsername" placeholder="Name des Datenbankbenutzers" :valid="value => value !== 'root'" />
		<svws-ui-text-input v-model.trim="zielUserPassword" placeholder="Passwort des Datenbankbenutzers" type="password" />
		<svws-ui-spacing />
		<svws-ui-button type="primary" @click="migrate" :disabled="(zielSchema.length === 0) || (zielUsername.length === 0) || (zielUserPassword.length === 0) || (status().value != undefined) || loading().value || (zielUsername === 'root')">
			<svws-ui-spinner :spinning="loading().value" />
			Migrieren
		</svws-ui-button>
	</div>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { type ShallowRef, shallowRef } from "vue";
	import { type SchemaMigrationQuelle } from "../schema/SchemaMigrationQuelle";

	const props = defineProps<{
		migrateSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
		migrationQuellinformationen: () => SchemaMigrationQuelle;
		logs: () => ShallowRef<List<string | null> | undefined>;
		status: () => ShallowRef<boolean | undefined>;
		loading: () => ShallowRef<boolean>;
	}>();

	const items = new Map<string, string>();

	items.set('mysql', 'MySQL');
	items.set('mariadb', 'MariaDB');
	items.set('mssql', 'MSSQL');
	items.set('mdb', 'Access (MDB)');

	const zielSchema = shallowRef("");
	const zielUsername = shallowRef("");
	const zielUserPassword = shallowRef("");

	async function migrate() {
		props.loading().value = true;
		const formData = new FormData();
		if (file.value !== null) {
			formData.append("database", file.value);
			formData.append('databasePassword', props.migrationQuellinformationen().password);
		}
		formData.append('schema', zielSchema.value);
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
			props.logs().value = result.log;
			props.status().value = result.success;
		} catch (e) {
			console.log(e);
			props.status().value = false;
		}
		props.loading().value = false;
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
	}

</script>
