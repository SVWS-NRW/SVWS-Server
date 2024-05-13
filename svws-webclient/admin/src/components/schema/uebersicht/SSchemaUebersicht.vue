<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<template v-if="eintrag !== undefined">
				<svws-ui-content-card v-if="(eintrag !== undefined) && (!eintrag.isInConfig)">
					<s-schema-uebersicht-add-existing :schema="eintrag.name" :add-existing-schema-to-config :logs-function :loading-function :status-function :is-active="currentAction === 'config'" @click="clickConfig" />
				</svws-ui-content-card>
				<svws-ui-content-card v-if="eintrag.isSVWS || revisionNotUpToDate" title="Sicherung">
					<svws-ui-action-button v-if="eintrag.isSVWS" title="Backup" description="Daten aus dem Schema werden in ein SQLite-Backup übertragen" icon="i-ri-save-3-line" :action-function="getBackupSchema" action-label="Backup starten" :is-loading="loading" :is-active="currentAction === 'backup'" @click="clickBackup" />
					<svws-ui-action-button v-if="revisionNotUpToDate" title="Aktualisieren" :description="`Setzt das Schema auf die aktuelle Revision ${ revision } hoch`" icon="i-ri-speed-line" :action-function="upgradeSchema" action-label="Aktualisierung starten" :is-loading="loading" :is-active="currentAction === 'upgrade'" @click="clickUpgrade">
						<div v-if="eintrag.isTainted" class="text-error flex">
							<span class="icon icon-error i-ri-error-warning-line inline relative mt-0.5 mr-1" />
							Achtung, auch nach dem Hochsetzen bleibt das Schema „Tainted“.
						</div>
					</svws-ui-action-button>
				</svws-ui-content-card>
				<svws-ui-content-card v-if="zeigeInitialisierungMitSchulkatalog || ((eintrag !== undefined) && (eintrag.isInConfig))" title="Initialisieren / Wiederherstellen">
					<svws-ui-action-button v-if="zeigeInitialisierungMitSchulkatalog" title="Schulkatalog" description="Daten werden über die Auswahl der Schulnummer initialisiert" icon="i-ri-archive-line" :action-function="init" action-label="Initialisieren" :is-loading="loading" :action-disabled="schule === undefined" :is-active="currentAction === 'init'" @click="clickInit">
						<svws-ui-input-wrapper>
							<svws-ui-select title="Schulen nach Schulnummer und Ort suchen" v-model="schule" :items="schulen()" :item-text="i=> `${i.SchulNr}: ${i.ABez1 ?? ''} ${i.ABez2 ?? ''} ${i.ABez3 ?? ''}`" autocomplete :item-filter="schulen_filter" />
						</svws-ui-input-wrapper>
					</svws-ui-action-button>
					<s-schema-uebersicht-restore v-if="(eintrag !== undefined) && (eintrag.isInConfig)" :restore-schema :logs-function :loading-function :status-function :is-active="currentAction === 'restore'" @click="clickRestore" />
					<s-schema-uebersicht-migrate v-if="(eintrag !== undefined) && (eintrag.isInConfig)" :migrate-schema :target-schema="eintrag.name" :migration-quellinformationen :logs-function :loading-function :status-function :is-active="currentAction === 'migrate'" @click="clickMigrate" />
				</svws-ui-content-card>
			</template>
		</div>
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Admin-Benutzer">
				<svws-ui-table :columns="cols" :items="admins()" />
			</svws-ui-content-card>
		</div>
		<div class="col-span-full">
			<log-box :logs :status>
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
	const currentAction = ref<'config' | 'init' | 'restore' | 'migrate' | 'upgrade' | 'backup' | ''>("");

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

	function clickConfig() {
		currentAction.value = (currentAction.value === 'config') ? '' : 'config';
		clearLog();
	}

	function clickInit() {
		currentAction.value = (currentAction.value === 'init') ? '' : 'init';
		clearLog();
	}

	function clickRestore() {
		currentAction.value = (currentAction.value === 'restore') ? '' : 'restore';
		clearLog();
	}

	function clickMigrate() {
		currentAction.value = (currentAction.value === 'migrate') ? '' : 'migrate';
		clearLog();
	}

	function clickUpgrade() {
		currentAction.value = (currentAction.value === 'upgrade') ? '' : 'upgrade';
		clearLog();
	}

	function clickBackup() {
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
