<template>
	<svws-ui-action-button title="Schild2-Datenbank migrieren" description="Eine Schild2-Datenbank wird in ein neues Schema migrier." icon="i-ri-database-2-line" action-label="Migrieren" :action-function :action-disabled="(zielSchema.length === 0) || (zielUsername.length === 0) || (zielUserPassword.length === 0) || (statusFunction().value != undefined) || (zielUsername === 'root')" :is-loading="loadingFunction().value" :is-active>
		<div class="input-wrapper">
			<svws-ui-select v-model="migrationQuellinformationen().dbms" :items="items.keys()" :item-text="i => items.get(i) || ''" title="Datenbank-Typ" />
			<svws-ui-spacing />
			<template v-if="migrationQuellinformationen().dbms !== 'mdb'">
				<div class="font-bold text-button">Quell-Datenbank:</div>
				<svws-ui-text-input v-model.trim="migrationQuellinformationen().location" placeholder="Datenbank-Host (hostname:port) oder (ip:port)" />
				<svws-ui-text-input v-model.trim="migrationQuellinformationen().schema" placeholder="Datenbank-Schema (z.B. schild_nrw)" />
				<svws-ui-text-input v-model.trim="migrationQuellinformationen().user" placeholder="Name des Datenbankbenutzers" :valid="validateUsername" />
				<svws-ui-text-input v-model.trim="migrationQuellinformationen().password" placeholder="Passwort des Datenbankbenutzers" type="password" />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="migrationQuellinformationen().schildzentral">Migration aus einer Schild-2-Zentral-Instanz</svws-ui-checkbox>
				<template v-if="migrationQuellinformationen().schildzentral">
					<svws-ui-text-input v-model="migrationQuellinformationen().schulnummer" placeholder="zu migrierende Schulnummer" />
				</template>
			</template>
			<template v-else>
				<div class="font-bold text-button">Quell-Datenbank: Access-Datei (.mdb) hochladen</div>
				<input type="file" @change="onFileChanged" :disabled="loadingFunction().value" accept=".mdb">
			</template>
			<svws-ui-spacing />
			<div class="font-bold text-button mt-2">Ziel-Datenbank (wird erstellt):</div>
			<svws-ui-text-input v-model.trim="zielSchema" placeholder="Schema (wird erstellt, z.B. svwsdb)" />
			<svws-ui-text-input v-model.trim="zielUsername" placeholder="Name des Datenbankbenutzers" :valid="value => value !== 'root'" />
			<svws-ui-text-input v-model.trim="zielUserPassword" placeholder="Passwort des Datenbankbenutzers" type="password" />
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { type ShallowRef, shallowRef } from "vue";
	import type { List, SimpleOperationResponse } from "@core";
	import type { InputDataType } from "@ui";
	import { type SchemaMigrationQuelle } from "../schema/SchemaMigrationQuelle";

	const props = defineProps<{
		migrateSchema:  (formData: FormData) => Promise<SimpleOperationResponse>;
		migrationQuellinformationen: () => SchemaMigrationQuelle;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		validatorUsername: (username: InputDataType) => boolean;
		isActive: boolean;
	}>();

	const items = new Map<string, string>();

	items.set('mysql', 'MySQL');
	items.set('mariadb', 'MariaDB');
	items.set('mssql', 'MSSQL');
	items.set('mdb', 'Access (MDB)');

	const zielSchema = shallowRef("");
	const zielUsername = shallowRef("");
	const zielUserPassword = shallowRef("");

	async function actionFunction() {
		props.loadingFunction().value = true;
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
			props.logsFunction().value = result.log;
			props.statusFunction().value = result.success;
		} catch (e) {
			console.log(e);
			props.statusFunction().value = false;
		}
		props.loadingFunction().value = false;
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
