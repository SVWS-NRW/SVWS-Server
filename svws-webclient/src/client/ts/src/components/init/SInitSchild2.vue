<template>
	<svws-ui-content-card title="Datenbank auswählen">
		<div class="flex items-start gap-3">
			<div class="flex flex-col gap-3">
				<!--TODO: SvwsUISelectInput (deprecated) durch SvwsUIMultiselect austauschen-->
				<svws-ui-select-input v-model="db" :options="[{index: 'mysql', label: 'MySQL'}, {index: 'mariadb', label: 'MariaDB'},{index: 'mssql', label: 'MSSQL'},{index: 'mdb', label: 'Access (MDB)'}]" @update:model-value="setDB" />
				<div class="flex flex-col gap-3" v-if="db && db !== 'mdb'">
					<svws-ui-checkbox v-model="schildzentral">mit Angabe einer Schulnummer bei Migration aus einer Schild-Zentral-Instanz</svws-ui-checkbox>
					<svws-ui-text-input v-if="schildzentral" v-model="schulnummer" placeholder="Schulnummer" />
					<svws-ui-text-input v-model="location" placeholder="Datenbank-Host" />
					<svws-ui-text-input v-model="schema" placeholder="Datenbank-Schema" />
					<svws-ui-text-input v-model="user" placeholder="Datenbankbenutzer" />
					<svws-ui-text-input v-model="password" placeholder="Passwort Datenbankbenutzer" />
					<svws-ui-button @click="migrate()">Migration starten</svws-ui-button>
				</div>
				<div class="flex flex-col gap-3" v-if="db === 'mdb'">
					<svws-ui-text-input v-model="password" placeholder="Datenbank-Passwort" />
					Access-Datei auswählen (Endung .mdb):
					<input type="file" @change="migrate" :disabled="loading">
				</div>
			</div>
			<svws-ui-spinner :spinning="loading" />
			<br>{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import {ref} from "vue";

	const props = defineProps<{
		migrateDB: (data: FormData) => Promise<boolean>;
		setDB: (db: string) => Promise<void>;
	}>();

	const db = ref('');
	const schildzentral = ref(false);
	const schulnummer = ref("");
	const location = ref("");
	const schema = ref("");
	const user = ref("");
	const password = ref("");
	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);

	async function migrate(event?: Event) {
		loading.value = true;
		const formData = new FormData();
		const target = event?.target as HTMLInputElement;
		if (target?.files?.length) {
			const file = target.files.item(0);
			if (file)
				formData.append("database", file);
		}
		formData.append('username', user.value);
		formData.append('password', password.value);
		formData.append('databasePassword', password.value);
		formData.append('schema', schema.value);
		formData.append('location', location.value);
		status.value = await props.migrateDB(formData);
		loading.value = false;
	}
</script>

<style lang="postcss" scoped>
.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
