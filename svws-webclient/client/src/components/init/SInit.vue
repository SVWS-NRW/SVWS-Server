<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="init-wrapper">
				<div class="init-container">
					<div class="init-form modal modal--md">
						<div class="modal--titlebar">
							<div class="modal--title inline-flex items-center gap-1">
								<span>Initialisierung der Datenbank</span>
							</div>
							<svws-ui-button type="icon" class="invisible" />
						</div>
						<div class="modal--content-wrapper">
							<div class="modal--content overflow-y-auto">
								<div class="flex flex-col">
									<svws-ui-action-button title="Schulkatalog" description="Daten werden über die Auswahl der Schulnummer ausgwählt"
										icon="i-ri-archive-line" :action-function="init" :is-loading :action-disabled="schule === undefined"
										:is-active="source === 'init'" @click="clickInit">
										<div class="flex gap-2">
											<svws-ui-select v-model="schule" title="Schule auswählen" autocomplete
												:items="listSchulkatalog" :item-text="i => i.KurzBez ? `${i.SchulNr}: ${i.KurzBez}` : `${i.SchulNr}: Schule ohne Name`"
												:item-filter required :disabled="isLoading" />
										</div>
										<div class="font-bold text-sm text-error mt-2">
											{{ status === false ? "Fehler beim Initialisieren" : status === true ? "Initialisierung erfolgreich" : "" }}
										</div>
									</svws-ui-action-button>
									<svws-ui-action-button title="Schild 2-Datenbank migrieren" description="Daten werden über die Auswahl einer existierenden Schild 2-Datenbank migriert."
										icon="i-ri-database-2-line" :action-function="migrate" action-label="Migration starten" :is-loading :action-disabled="(db === 'mdb' && !file) || (user === 'root')"
										:is-active="source === 'migrate'" @click="clickMigrate">
										<div class="flex flex-col gap-4">
											<svws-ui-select :model-value="items.get(db)" :items="items.values()" @update:model-value="set" :item-text="i => i" title="Datenbank" />
											<div class="flex flex-col gap-6 text-left" v-if="db === 'mdb'">
												<div class="flex flex-col gap-2 px-2">
													<span class="font-bold text-button">Access-Datei (.mdb) hochladen</span>
													<input type="file" @change="onFileChanged" :disabled="isLoading" accept=".mdb">
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
												{{ status === false ? "Fehler beim Upload" : status === true ? "Upload erfolgreich" : "" }}
											</div>
										</div>
									</svws-ui-action-button>
									<svws-ui-action-button title="Wiederherstellen" description="Daten werden aus einem Backup wiederhergestellt" :action-function="restore"
										action-label="Wiederherstellen" icon="i-ri-device-recover-line" :action-disabled="!file || isLoading" :is-loading
										:is-active="source === 'restore'" @click="clickRestore">
										<div class="flex flex-col gap-2 text-left">
											<span class="font-bold text-button">Quell-Datenbank: SQLite-Datenbank (.sqlite) hochladen</span>
											<input type="file" @change="onFileChanged" :disabled="isLoading" accept=".sqlite">
											<div class="font-bold text-sm">
												{{ status === false ? "Fehler beim Upload" : status === true ? "Upload erfolgreich" : "" }}
											</div>
										</div>
									</svws-ui-action-button>
									<div class="col-span-full">
										<log-box :logs :status>
											<template #button>
												<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen </svws-ui-button>
											</template>
										</log-box>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
	<s-notifications :backticks="() => true" />
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { InitProps } from "./SInitProps";
	import type { SchulenKatalogEintrag, List } from "@core";

	const props = defineProps<InitProps>();
	const schule = ref<SchulenKatalogEintrag>()

	const logs = ref<List<string|null> | undefined>(undefined);
	const status = ref<boolean | undefined>(undefined);
	const isLoading = ref<boolean>(false);

	const file = ref<File | null>(null);

	function clearLog() {
		isLoading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function clickInit() {
		await props.setSource('init');
		clearLog();
	}

	async function clickRestore() {
		await props.setSource('restore');
		clearLog();
	}

	async function clickMigrate() {
		await props.setSource('migrate');
		clearLog();
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
				await props.setDB(k);
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
		status.value = await props.migrateDB(formData);
		isLoading.value = false;
	}

	// Init
	const itemFilter = (items: Iterable<SchulenKatalogEintrag>, search: string) => {
		const list = [];
		for (const i of items)
			if (i.SchulNr.includes(search.toLocaleLowerCase())
				|| i.KurzBez?.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
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

	.init-wrapper {
		@apply flex h-full flex-col justify-between;
	}

	.init-container {
		@apply bg-cover bg-top rounded-2xl h-full flex flex-col justify-center items-center px-4;
		background-image: url("/images/placeholder-background-blurred.jpg");
	}

	.svws-ui-content-button {
		@apply rounded-lg border-light border p-4 text-balance flex gap-4 text-left;

		&.svws-not-active {
			@apply opacity-50 border-transparent order-1;

			.svws-icon {
				@apply opacity-25;
			}
		}

		&.svws-active {
			@apply border-transparent text-primary bg-primary/10 pointer-events-none;
		}

		&:not(.svws-active):hover,
		&:not(.svws-active):focus-visible {
			@apply outline-none bg-black/10 border-black/10 opacity-100;

			.svws-icon {
				@apply opacity-100;
			}
		}

		&:focus {
			@apply outline-none;
		}

		&:not(.svws-active):focus-visible {
			@apply ring ring-primary/50 ring-offset-1;
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
