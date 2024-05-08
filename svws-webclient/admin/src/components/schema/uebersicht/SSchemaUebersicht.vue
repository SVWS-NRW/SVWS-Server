<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<template v-if="eintrag !== undefined">
				<svws-ui-content-card v-if="(eintrag !== undefined) && (!eintrag.isInConfig)">
					<s-schema-action-button title="In Konfiguration aufnehmen" description="Das Schema wird mit dem angegebenen Benutzer und Kennwort in die Konfiguration der SVWS-Servers aufgenommen." icon="i-ri-share-forward-2-line">
						<s-schema-add-existing-card :schema="eintrag.name" :add-existing="addExistingSchemaToConfig"
							:logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
					</s-schema-action-button>
				</svws-ui-content-card>
				<svws-ui-content-card v-if="eintrag.isSVWS || revisionNotUpToDate" title="Sicherung">
					<s-schema-action-button v-if="eintrag.isSVWS" title="Backup" description="Daten aus dem Schema werden in ein SQLite-Backup übertragen" icon="i-ri-save-3-line">
						<svws-ui-button @click="getBackupSchema" :disabled="loading"><svws-ui-spinner v-if="loading" :spinning="loading" /> Backup starten </svws-ui-button>
					</s-schema-action-button>
					<s-schema-action-button v-if="revisionNotUpToDate" title="Aktualisieren" :description="`Setzt das Schema auf die aktuelle Revision ${ revision } hoch.`" icon="i-ri-speed-line">
						<div v-if="eintrag.isTainted" class="mb-2 text-error flex">
							<span class="icon icon-error i-ri-error-warning-line inline relative mt-0.5 mr-1" />
							Achtung, auch nach dem Hochsetzen bleibt das Schema „Tainted“.
						</div>
						<svws-ui-button @click="upgradeSchema" :disabled="loading"><svws-ui-spinner v-if="loading" :spinning="loading" /> Aktualisierung starten </svws-ui-button>
					</s-schema-action-button>
				</svws-ui-content-card>
				<svws-ui-content-card v-if="zeigeInitialisierungMitSchulkatalog || ((eintrag !== undefined) && (eintrag.isInConfig))" title="Initialisieren / Wiederherstellen">
					<s-schema-action-button v-if="zeigeInitialisierungMitSchulkatalog" title="Schulkatalog" description="Daten werden über die Auswahl der Schulnummer initialisiert" icon="i-ri-archive-line">
						<svws-ui-input-wrapper>
							<svws-ui-select title="Schulen nach Schulnummer und Ort suchen" v-model="schule" :items="schulen()" :item-text="i=> `${i.SchulNr}: ${i.ABez1 ?? ''} ${i.ABez2 ?? ''} ${i.ABez3 ?? ''}`" autocomplete :item-filter="schulen_filter" />
							<div class="flex gap-1 flex-wrap justify-self-start">
								<svws-ui-button :disabled="schule == undefined" @click="init"><svws-ui-spinner :spinning="loading" /> Initialisieren</svws-ui-button>
							</div>
						</svws-ui-input-wrapper>
					</s-schema-action-button>
					<s-schema-action-button v-if="(eintrag !== undefined) && (eintrag.isInConfig)" title="Backup wiederherstellen" description="Daten werden aus einem Backup wiederhergestellt" icon="i-ri-device-recover-line">
						<s-schema-import-card :restore-schema="restoreSchema"
							:logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
					</s-schema-action-button>
					<s-schema-action-button v-if="(eintrag !== undefined) && (eintrag.isInConfig)" title="Schild2-Schema migrieren" description="Daten werden über die Auswahl einer existierenden Schild 2-Datenbank importiert." icon="i-ri-database-2-line">
						<s-schema-migrate-card :migrate-schema="migrateSchema"
							:target-schema="eintrag.name" :migration-quellinformationen="migrationQuellinformationen"
							:logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
					</s-schema-action-button>
				</svws-ui-content-card>
			</template>
		</div>
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Admin-Benutzer">
				<svws-ui-table :columns="cols" :items="admins()" />
			</svws-ui-content-card>
		</div>
		<div class="col-span-full">
			<log-box :logs="logs" :status="status">
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen </svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";
	import type { SchemaUebersichtProps } from "./SSchemaUebersichtProps";
	import type { DataTableColumn } from "@ui";
	import type { List, SchulenKatalogEintrag } from "@core";

	const props = defineProps<SchemaUebersichtProps>();

	const eintrag = computed(() => props.data());
	watch(eintrag, async(newEintrag, oldEintrag) => {
		if ((newEintrag === undefined) && (oldEintrag === undefined))
			return;
		if ((newEintrag !== undefined) && (oldEintrag !== undefined) && (newEintrag.name === oldEintrag.name))
			return;
		clearLog();
	});

	const schule = ref<SchulenKatalogEintrag>()
	const loading = ref<boolean>(false);
	const logs = shallowRef<List<string|null> | undefined>(undefined);
	const status = shallowRef<boolean | undefined>(undefined);
	const currentAction = ref<string>("");

	const logsFunction = () => logs;
	const loadingFunction = () => loading;
	const statusFunction = () => status;

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	const revisionNotUpToDate = computed<boolean>(()=> {
		const revServer = props.revision;
		if (eintrag.value === undefined || revServer === null || eintrag.value.revision < 0)
			return false;
		return revServer !== eintrag.value.revision;
	})

	const zeigeInitialisierungMitSchulkatalog = computed<boolean>(() =>
		(eintrag.value !== undefined) && (eintrag.value.isInConfig) && eintrag.value.isSVWS && (props.schuleInfo() === undefined) && !revisionNotUpToDate.value);

	const cols: DataTableColumn[] = [
		{ key: "anzeigename", label: "Name", span: 2 },
		{ key: "name", label: "Benutzername", span: 1, sortable: true },
	];

	async function clickInConfigAufnehmen() {
		currentAction.value = (currentAction.value === 'inConfigAufnehmen') ? '' : 'inConfigAufnehmen';
		clearLog();
	}

	async function clickInitialisierungMitSchulkatalog() {
		currentAction.value = (currentAction.value === 'init') ? '' : 'init';
		clearLog();
	}

	async function clickImportSQLiteBackup() {
		currentAction.value = (currentAction.value === 'import') ? '' : 'import';
		clearLog();
	}

	async function clickMigrate() {
		currentAction.value = (currentAction.value === 'migrate') ? '' : 'migrate';
		clearLog();
	}

	async function clickBackup() {
		currentAction.value = (currentAction.value === 'backup') ? '' : 'backup';
		clearLog();
	}

	async function getBackupSchema() {
		loading.value = true;
		const { data, name } = await props.backupSchema();
		loading.value = false;
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function init() {
		if (schule.value === undefined)
			return;
		loading.value = true;
		await props.initSchema(Number(schule.value.SchulNr));
		loading.value = false;
	}

	/** Der Filter für Schulen */
	const schulen_filter = (items: SchulenKatalogEintrag[], search: string): SchulenKatalogEintrag[] => {
		// Teilmatch Schulnummer
		const nrmatch = search.match(/\d+/);
		const nr = nrmatch ? nrmatch[0] : undefined;
		//Teilmatch Name
		const ort = search.replace(/\d+\s*/, "").trim();
		if (!nr && !ort) {
			return items;
		}
		if (!nr) {
			return items.filter((item: SchulenKatalogEintrag) => {
				if (item && item.Ort !== null) {
					return item.Ort
						.toLocaleLowerCase("de-DE")
						.startsWith(ort.toLocaleLowerCase("de-DE"));
				}
				return false;
			});
		} else if (!ort) {
			return items.filter(item => {
				if (item.SchulNr) {
					return item.SchulNr.includes(nr);
				}
				return false;
			});
		} else {
			return items.filter(item => {
				if (item.SchulNr && item.Ort) {
					return (
						item.SchulNr.includes(nr) &&
						item.Ort
							.toLocaleLowerCase("de-DE")
							.startsWith(ort.toLocaleLowerCase("de-DE"))
					);
				}
				return false;
			});
		}
	};

</script>

<style lang="postcss" scoped>
	.page--content {
		grid-template-columns: 2fr 1fr;
	}

	.svws-ui-content-button {
		@apply rounded-lg border-light border p-4 text-balance flex gap-1 text-left;

		.icon {
			@apply block h-[1.2em] w-[1.2em];
		}

		&.svws-not-active {
			@apply opacity-50 border-transparent order-1;

			.svws-icon {
				@apply opacity-25;
			}
		}

		&.svws-active {
			@apply border-transparent text-primary bg-primary/10;
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
