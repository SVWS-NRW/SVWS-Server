<template>
	<ui-card icon="i-ri-database-2-line" title="Schild2-Schema migrieren" subtitle="Daten werden über die Auswahl einer existierenden Schild 2-Datenbank importiert"
		:is-open @update:is-open="(open) => emit('opened', open)">
		<div class="space-y-2 mt-1">
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
					<div v-else class="flex flex-col gap-2 -mt-5">
						<div class="font-bold text-button">Quell-Datenbank: Access-Datei (.mdb) hochladen</div>
						<input type="file" @change="onFileChanged" :disabled="loading" accept=".mdb">
					</div>
					<svws-ui-input-wrapper v-if="targetSchema === undefined">
						<div class="text-button -mb-1 mt-2">Ziel-Datenbank (wird erstellt)</div>
						<svws-ui-text-input v-model.trim="zielSchema" placeholder="Schema (wird erstellt, z.B. svwsdb)" />
						<svws-ui-text-input v-model.trim="zielUsername" placeholder="Name des Datenbankbenutzers" :valid="value => value !== 'root'" />
						<svws-ui-text-input v-model.trim="zielUserPassword" placeholder="Passwort des Datenbankbenutzers" type="password" />
					</svws-ui-input-wrapper>
				</div>
			</div>
		</div>
		<template #buttonFooterLeft>
			<svws-ui-button :disabled="(migrationQuellinformationen().dbms === 'mdb' && !file) || (zielUsername === 'root') || loading" title="Migrieren" @click="actionFunction" :is-loading="loading" class="w-fit">
				<svws-ui-spinner v-if="loading" spinning />
				<span v-else class="icon i-ri-play-line" />
				Migrieren
			</svws-ui-button>
		</template>
	</ui-card>
</template>

<script setup lang="ts">

	import { shallowRef } from "vue";
	import type { SchemaMigrationQuelle } from "../SchemaMigrationQuelle";
	import type { List } from "@core/java/util/List";
	import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";

	const props = defineProps<{
		migrateSchema: (formData: FormData) => Promise<SimpleOperationResponse>;
		targetSchema?: string;
		migrationQuellinformationen: () => SchemaMigrationQuelle;
		setStatus: (loading: boolean, status?: boolean, logs?: List<string | null>) => void;
		loading: boolean;
		isOpen: boolean;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
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
		props.setStatus(true);
		const formData = new FormData();
		if (file.value !== null) {
			formData.append("database", file.value);
			formData.append('databasePassword', props.migrationQuellinformationen().password);
		}
		formData.append('schema', props.targetSchema ?? zielSchema.value);
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
			props.setStatus(false, result.success, result.log);
			if (result.success) {
				zielSchema.value = '';
				zielUserPassword.value = '';
				zielUsername.value = '';
			}
		} catch (e) {
			console.log(e);
			props.setStatus(false);
		}
	}

	const file = shallowRef<File | null>(null);

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files)
			file.value = target.files[0];
	}

</script>
