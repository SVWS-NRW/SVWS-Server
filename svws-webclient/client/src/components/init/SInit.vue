<template>
	<ui-login-layout size="lg" hide-header hide-hinweis>
		<template #main>
			<div class="w-full flex flex-col gap-2">
				<div class="w-full pb-2 mb-4 text-headline-md text-left border-b-1">
					<span>Initialisierung der Datenbank</span>
				</div>
				<ui-card icon="i-ri-archive-line" title="Schulkatalog" subtitle="Daten werden über die Auswahl der Schulnummer ausgwählt"
					:is-open="currentAction === 'init'" @update:is-open="(isOpen) => setCurrentAction('init', isOpen)">
					<div class="mt-2 w-full">
						<div class="flex gap-2">
							<svws-ui-select v-model="schule" title="Schule auswählen" autocomplete
								:items="listSchulkatalog" :item-text="i => i.KurzBez ? `${i.SchulNr}: ${i.KurzBez}` : `${i.SchulNr}: Schule ohne Name`"
								:item-filter="filterSchulenKatalogEintraege" required :disabled="isLoading" />
						</div>
						<div v-if="status !== undefined" class="font-bold text-sm text-ui-danger mt-2">
							{{ status === false ? "Fehler beim Initialisieren" : status === true ? "Initialisierung erfolgreich" : "" }}
						</div>
					</div>
					<template #buttonFooterLeft>
						<svws-ui-button :disabled="schule === undefined || isLoading" title="Löschen" @click="init" :is-loading class="mt-4">
							<svws-ui-spinner v-if="isLoading" spinning />
							<span v-else class="icon i-ri-play-line" />
							Ausführen
						</svws-ui-button>
					</template>
				</ui-card>

				<ui-card icon="i-ri-database-2-line" title="Schild 2-Datenbank migrieren" subtitle="Daten werden über die Auswahl einer existierenden Schild 2-Datenbank migriert."
					:is-open="currentAction === 'migrate'" @update:is-open="(isOpen) => setCurrentAction('migrate', isOpen)">
					<div class="flex flex-col gap-4 mt-2">
						<svws-ui-select :model-value="items.get(db)" :items="items.values()" @update:model-value="set" :item-text="i => i" title="Datenbank" />
						<div class="flex flex-col gap-6 text-left" v-if="db === 'mdb'">
							<div class="flex flex-col gap-2 px-2">
								<span class="font-bold text-button">Access-Datei (.mdb) hochladen</span>
								<input type="file" @change="onFileChanged" :disabled="isLoading" accept=".mdb">
							</div>
						</div>
						<div v-if="db !== 'mdb'" class="flex flex-col gap-3">
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
							{{ status === false ? "Fehler beim Upload" : status === true ? "Upload erfolgreich" : "" }}
						</div>
					</div>
					<template #buttonFooterLeft>
						<svws-ui-button :disabled="(db === 'mdb' && !file) || (user === 'root') || isLoading" title="Migration starten" @click="migrate" :is-loading class="mt-4">
							<svws-ui-spinner v-if="isLoading" spinning />
							<span v-else class="icon i-ri-play-line" />
							Migration starten
						</svws-ui-button>
					</template>
				</ui-card>
				<ui-card icon="i-ri-device-recover-line" title="Wiederherstellen" subtitle="Daten werden aus einem Backup wiederhergestellt."
					:is-open="currentAction === 'restore'" @update:is-open="(isOpen) => setCurrentAction('restore', isOpen)">
					<div class="flex flex-col gap-2 text-left">
						<span class="font-bold text-button">Quell-Datenbank: SQLite-Datenbank (.sqlite) hochladen</span>
						<input type="file" @change="onFileChanged" :disabled="isLoading" accept=".sqlite">
						<div class="font-bold text-sm">
							{{ status === false ? "Fehler beim Upload" : status === true ? "Upload erfolgreich" : "" }}
						</div>
					</div>
					<template #buttonFooterLeft>
						<svws-ui-button :disabled="!file || isLoading" title="Wiederherstellen" @click="restore" :is-loading class="mt-4">
							<svws-ui-spinner v-if="isLoading" spinning />
							<span v-else class="icon i-ri-play-line" />
							Wiederherstellen
						</svws-ui-button>
					</template>
				</ui-card>
				<div class="col-span-full">
					<log-box :logs :status>
						<template #button>
							<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen </svws-ui-button>
						</template>
					</log-box>
				</div>
			</div>
		</template>
	</ui-login-layout>
	<s-notifications />
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { InitProps } from "./SInitProps";
	import type { SchulenKatalogEintrag, List } from "@core";
	import { filterSchulenKatalogEintraege } from "~/utils/helfer";

	const props = defineProps<InitProps>();
	const schule = ref<SchulenKatalogEintrag>()

	const logs = ref<List<string|null> | undefined>(undefined);
	const status = ref<boolean | undefined>(undefined);
	const isLoading = ref<boolean>(false);

	const file = ref<File | null>(null);

	const db = ref<'mysql'|'mariadb'|'mssql'|'mdb'|undefined>(undefined);

	const currentAction = ref<string>('');
	const oldAction = ref({
		name: "",
		open: false,
	});

	function setCurrentAction(newAction: string, open: boolean) {
		if(newAction === oldAction.value.name && !open)
			return;
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value === "") ? false : true;
		if(open === true)
			currentAction.value= newAction;
		else
			currentAction.value = "";
	}

	function clearLog() {
		isLoading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	// Restore
	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files !== null) && (target.files.length > 0))
			file.value = target.files[0];
	}

	async function restore() {
		if (!file.value)
			return;
		isLoading.value = true;
		const formData = new FormData();
		formData.append("database", file.value);
		status.value = await props.importSQLite(formData);
		isLoading.value = false;
	}

	// Migrate
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

	async function set(item: string | null | undefined) {
		if (item === null || item === undefined)
			return;
		for (const [k,v] of items.entries()) {
			if ((v === item) && (k !== undefined)) {
				db.value = k;
				break;
			}
		}
	}

	async function migrate() : Promise<void> {
		isLoading.value = true;
		const formData = new FormData();
		if (file.value)
			formData.append("database", file.value);
		formData.append('username', user.value);
		formData.append('password', password.value);
		formData.append('databasePassword', password.value);
		formData.append('schema', schema.value);
		formData.append('location', location.value);
		status.value = await props.migrateDB(formData, currentAction.value === 'restore', db.value);
		isLoading.value = false;
	}

	async function init() {
		if (schule.value === undefined)
			return;
		isLoading.value = true;
		status.value = await props.initSchule(schule.value);
		isLoading.value = false;
	}
</script>

<style lang="postcss">

	@reference "../../../../ui/src/assets/styles/index.css"

	.svws-ui-content-button {
		@apply rounded-lg border-ui-neutral border p-4 text-balance flex gap-4 text-left;

		&.svws-not-active {
			@apply opacity-50 border-transparent order-1;

			.svws-icon {
				@apply opacity-25;
			}
		}

		&.svws-active {
			@apply border-transparent text-ui-brand bg-ui-brand/10 pointer-events-none;
		}

		&:not(.svws-active):hover,
		&:not(.svws-active):focus-visible {
			@apply outline-hidden bg-ui-75 opacity-100;

			.svws-icon {
				@apply opacity-100;
			}
		}

		&:focus {
			@apply outline-hidden;
		}

		&:not(.svws-active):focus-visible {
			@apply ring-3 ring-ui-brand/50 ring-offset-1;
		}

		.svws-title {
			@apply font-bold text-headline-md;
		}

		.svws-description {
			@apply opacity-50 leading-tight;
		}

		.svws-icon {
			@apply text-headline-xl w-16 text-center;
		}
	}

</style>
