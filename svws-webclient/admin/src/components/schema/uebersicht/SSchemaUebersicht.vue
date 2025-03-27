<template>
	<div class="page page-flex-row">
		<div class="h-full min-w-128 flex flex-col gap-y-8 overflow-y-auto px-6">
			<template v-if="eintrag !== undefined">
				<div v-if="(eintrag !== undefined) && (!eintrag.isInConfig)" class="flex flex-col gap-4">
					<s-schema-uebersicht-add-existing :schema="eintrag.name" :add-existing-schema-to-config :loading :set-status :is-open="currentAction === 'config'" @opened="(isOpen) => setCurrentAction('config', isOpen)" />
				</div>
				<div v-if="eintrag.isSVWS || revisionNotUpToDate" class="flex flex-col gap-4">
					<div class="text-headline-md">Sicherung</div>
					<div class="space-y-2">
						<ui-card v-if="eintrag.isSVWS" icon="i-ri-save-3-line" title="Backup" subtitle="Daten aus dem Schema werden in ein SQLite-Backup übertragen" :is-open="currentAction === 'backup'" @update:is-open="(isOpen) => setCurrentAction('backup', isOpen)">
							<svws-ui-button :disabled="loading" title="Backup starten" @click="getBackupSchema" :is-loading="loading">
								<svws-ui-spinner v-if="loading" spinning />
								<span v-else class="icon i-ri-play-line" />
								Backup starten
							</svws-ui-button>
						</ui-card>
						<ui-card v-if="revisionNotUpToDate" icon="i-ri-speed-line" title="Aktualisieren" :subtitle="`Setzt das Schema auf die aktuelle Revision ${revision} hoch`" :is-open="currentAction === 'upgrade'" @update:is-open="(isOpen) => setCurrentAction('upgrade', isOpen)">
							<div class="flex flex-col gap-2">
								<div v-if="eintrag.isTainted" class="text-ui-danger flex items-center">
									<span class="icon icon-ui-danger i-ri-error-warning-line inline relative mt-0.5 mr-1" />
									Achtung, auch nach dem Hochsetzen bleibt das Schema „Tainted“.
								</div>
							</div>
							<template #buttonFooterLeft>
								<svws-ui-button :disabled="loading" title="Aktualisierung starten" @click="upgradeSchema" :is-loading="loading" class="w-fit mt-4">
									<svws-ui-spinner v-if="loading" spinning />
									<span v-else class="icon i-ri-play-line" />
									Aktualisierung starten
								</svws-ui-button>
							</template>
						</ui-card>
					</div>
				</div>
				<div v-if="zeigeInitialisierungMitSchulkatalog || zeigeNeuesSchemaAnlegen || ((eintrag !== undefined) && (eintrag.isInConfig))" class="flex flex-col gap-4">
					<div class="text-headline-md">Initialisieren / Wiederherstellen</div>
					<div class="space-y-2">
						<ui-card v-if="zeigeNeuesSchemaAnlegen" icon="i-ri-archive-line" title="Neues Schema" subtitle="Erstellt ein neues leeres Schema, welches im Anschluss initialisiert werden kann" :is-open="currentAction === 'empty'" @update:is-open="(isOpen) => setCurrentAction('empty', isOpen)">
							<svws-ui-button :disabled="loading" title="Schema Anlegen" @click="createEmptySchema" :is-loading="loading">
								<svws-ui-spinner v-if="loading" spinning />
								<span v-else class="icon i-ri-play-line" />
								Schema Anlegen
							</svws-ui-button>
						</ui-card>
						<ui-card v-if="zeigeInitialisierungMitSchulkatalog" icon="i-ri-archive-line" title="Initialisieren aus Schulkatalog" subtitle="Daten werden über die Auswahl der Schulnummer initialisiert" :is-open="currentAction === 'init'" @update:is-open="(isOpen) => setCurrentAction('init', isOpen)">
							<div class="w-full">
								<svws-ui-input-wrapper class="mt-2">
									<svws-ui-select title="Schulen nach Schulnummer und Ort suchen" v-model="schule" :items="schulen()" :item-text="i=> `${i.SchulNr}: ${i.ABez1 ?? ''} ${i.ABez2 ?? ''} ${i.ABez3 ?? ''}`" autocomplete :item-filter="schulen_filter" />
								</svws-ui-input-wrapper>
							</div>
							<template #buttonFooterLeft>
								<svws-ui-button :disabled="schule === undefined || loading" title="Initialisieren" @click="init" :is-loading="loading">
									<svws-ui-spinner v-if="loading" spinning />
									<span v-else class="icon i-ri-play-line" />
									Initialisieren
								</svws-ui-button>
							</template>
						</ui-card>
						<s-schema-uebersicht-restore v-if="(eintrag !== undefined) && (eintrag.isInConfig)" :restore-schema :loading :set-status :is-open="currentAction === 'restore'" @opened="(isOpen) => setCurrentAction('restore', isOpen)" />
						<s-schema-uebersicht-migrate v-if="(eintrag !== undefined) && (eintrag.isInConfig)" :migrate-schema :target-schema="eintrag.name" :migration-quellinformationen :loading :set-status :is-open="currentAction === 'migrate'" @opened="isOpen => setCurrentAction('migrate', isOpen)" />
					</div>
				</div>
			</template>
		</div>
		<div class="min-w-96 flex flex-col gap-y-8 overflow-y-auto px-6">
			<div class="flex flex-col gap-4">
				<div class="text-headline-md">Admin-Benutzer</div>
				<svws-ui-table :columns="cols" :items="admins()" scroll />
			</div>
		</div>
		<div class="grow min-w-fit">
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clear" title="Log verwerfen">Log verwerfen </svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";
	import type { SchemaUebersichtProps } from "./SSchemaUebersichtProps";
	import type { SchulenKatalogEintrag } from "@core/core/data/schule/SchulenKatalogEintrag";
	import type { List } from "@core/java/util/List";
	import type { DataTableColumn } from "@ui/types";

	const props = defineProps<SchemaUebersichtProps>();

	const eintrag = computed(() => props.data());
	watch(eintrag, async(newEintrag, oldEintrag) => {
		if ((newEintrag === undefined) && (oldEintrag === undefined))
			return;
		if ((newEintrag !== undefined) && (oldEintrag !== undefined) && (newEintrag.name === oldEintrag.name))
			return;
		clear();
	});

	const schule = ref<SchulenKatalogEintrag>()
	const loading = ref<boolean>(false);
	const logs = shallowRef<List<string|null> | undefined>(undefined);
	const status = shallowRef<boolean | undefined>(undefined);
	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});

	function setStatus(loadingV: boolean, statusV?: boolean, logsV?: List<string | null>) {
		logs.value = logsV;
		loading.value = loadingV;
		status.value = statusV;
	}

	function clear() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	const revisionNotUpToDate = computed<boolean>(() => {
		const revServer = props.revision;
		if (eintrag.value === undefined || revServer === null || eintrag.value.revision < 0)
			return false;
		return revServer !== eintrag.value.revision;
	})

	const zeigeNeuesSchemaAnlegen = computed<boolean>(() => (eintrag.value !== undefined) && (eintrag.value.isInConfig) && !eintrag.value.isSVWS);

	const zeigeInitialisierungMitSchulkatalog = computed<boolean>(() =>
		(eintrag.value !== undefined) && (eintrag.value.isInConfig) && eintrag.value.isSVWS && (props.schuleInfo() === undefined) && !revisionNotUpToDate.value);

	const cols: DataTableColumn[] = [
		{ key: "anzeigename", label: "Name", span: 2 },
		{ key: "name", label: "Benutzername", span: 1, sortable: true },
	];

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
		const nrmatch = /\d+/.exec(search);
		const nr = nrmatch ? nrmatch[0] : undefined;
		//Teilmatch Name
		const ort = search.replace(/\d+\s*/, "").trim();
		if ((nr === undefined) && (ort.length === 0)) {
			return items;
		}
		if (nr === undefined) {
			return items.filter((item: SchulenKatalogEintrag) => {
				if (item.Ort !== null) {
					return item.Ort
						.toLocaleLowerCase("de-DE")
						.startsWith(ort.toLocaleLowerCase("de-DE"));
				}
				return false;
			});
		} else if (ort.length === 0) {
			return items.filter(item => {
				if (item.SchulNr.length > 0) {
					return item.SchulNr.includes(nr);
				}
				return false;
			});
		} else {
			return items.filter(item => {
				if ((item.SchulNr.length > 0) && (item.Ort !== null)) {
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
