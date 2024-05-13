<template>
	<svws-ui-action-button title="Schild 2-Datenbank" description="Daten werden über die Auswahl einer existierenden Schild 2-Datenbank importiert." icon="i-ri-database-2-line" :action-function="migrate" action-label="Migration starten" :is-loading="loading">
		<div class="flex flex-col gap-4">
			<svws-ui-select :model-value="items.get(db)" :items="items.values()" @update:model-value="set" :item-text="i => i" title="Datenbank" />
			<div class="flex flex-col gap-6 text-left" v-if="db === 'mdb'">
				<div class="flex flex-col gap-2 px-2">
					<span class="font-bold text-button">Access-Datei (.mdb) hochladen</span>
					<input type="file" @change="onFileChanged" :disabled="loading" accept=".mdb">
				</div>
			</div>
			<div class="flex flex-col gap-3" v-if="db !== 'mdb'">
				<div class="flex flex-col gap-2 mt-2 mb-6">
					<div class="flex flex-col text-left gap-0.5 pl-3">
						<span class="opacity-50">Bei Migration aus einer Schild-Zentral-Instanz:</span>
						<svws-ui-checkbox class="text-left" type="toggle" v-model="schildzentral">Schulnummer angeben</svws-ui-checkbox>
					</div>
					<svws-ui-text-input v-if="schildzentral" v-model="schulnummer" placeholder="Schulnummer" />
				</div>
				<svws-ui-text-input v-model.trim="location" placeholder="Datenbank-Host" />
				<svws-ui-text-input v-model.trim="schema" placeholder="Datenbank-Schema" />
				<svws-ui-text-input v-model.trim="user" placeholder="Datenbank-Benutzer" />
				<svws-ui-text-input v-model.trim="password" placeholder="Passwort Datenbankbenutzer" type="password" />
			</div>
			<div class="text-left font-bold text-sm -mb-5 mt-4">
				{{
					status === false
						? "Fehler beim Upload"
						: status === true
							? "Upload erfolgreich"
							: ""
				}}
			</div>
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import {ref, shallowRef} from "vue";

	const props = defineProps<{
		migrateDB: (data: FormData) => Promise<boolean>;
		setDB: (db: string) => Promise<void>;
		db?: 'mysql'|'mariadb'|'mssql'|'mdb';
	}>();

	const items = new Map<'mysql'|'mariadb'|'mssql'|'mdb'|undefined, string>();
	items.set('mysql', 'MySQL');
	items.set('mariadb', 'MariaDB');
	items.set('mssql', 'MSSQL');
	items.set('mdb', 'Access (MDB)');

	const schildzentral = ref(false);
	const schulnummer = ref("");
	const location = ref("");
	const schema = ref("");
	const user = ref("");
	const password = ref("");
	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function set(item: string | null | undefined) {
		if (item === null || item === undefined)
			return;
		for (const [k,v] of items.entries()) {
			if ((v === item) && (k !== undefined)) {
				await props.setDB(k);
				break;
			}
		}
	}

	const file = shallowRef<File | null>(null);

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target !== null) && (target.files !== null) && (target.files.length > 0))
			file.value = target.files[0];
	}

	async function migrate() : Promise<void> {
		loading.value = true;
		const formData = new FormData();
		if (file.value)
			formData.append("database", file.value);
		formData.append('username', user.value);
		formData.append('password', password.value);
		formData.append('databasePassword', password.value);
		formData.append('schema', schema.value);
		formData.append('location', location.value);
		status.value = await props.migrateDB(formData);
		loading.value = false;
	}

</script>
