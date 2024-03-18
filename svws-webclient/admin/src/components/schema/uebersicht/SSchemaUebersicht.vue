<template>
	<div class="page--content !flex">
		<svws-ui-content-card v-if="eintrag !== undefined" :title="schuleInfo() === undefined ? 'Schema initialisieren' : 'Schema verwalten'" class="flex-[2]">
			<div class="flex flex-col gap-2 mb-4">
				<template v-if="schuleInfo() === undefined">
					<button role="button" class="svws-ui-content-button" :class="{'svws-not-active': currentAction && currentAction !== 'init', 'svws-active': currentAction === 'init'}" @click="currentAction = 'init'">
						<div class="svws-icon"><span class="icon i-ri-archive-line" /></div>
						<div class="flex flex-col">
							<div class="svws-title">Schulkatalog</div>
							<div class="svws-description">Daten werden über die Auswahl der Schulnummer initialisiert</div>
						</div>
					</button>
					<svws-ui-input-wrapper v-if="currentAction === 'init'" class="mt-4 mb-20">
						<svws-ui-select title="Schulen nach Schulnummer und Ort suchen" v-model="schule" :items="schulen()" :item-text="i=> `${i.SchulNr}: ${i.ABez1 ?? ''} ${i.ABez2 ?? ''} ${i.ABez3 ?? ''}`" autocomplete :item-filter="schulen_filter" />
						<div class="flex gap-1 flex-wrap justify-self-start">
							<svws-ui-button :disabled="schule == undefined" @click="init"><svws-ui-spinner :spinning="loading" /> Initialisieren</svws-ui-button>
							<svws-ui-button type="secondary" @click="currentAction = ''"> Abbrechen</svws-ui-button>
						</div>
					</svws-ui-input-wrapper>
				</template>
				<button role="button" class="svws-ui-content-button" :class="{'svws-not-active': currentAction && currentAction !== 'migrate', 'svws-active': currentAction === 'migrate'}" @click="currentAction = 'migrate'">
					<div class="svws-icon"><span class="icon i-ri-database-2-line" /></div>
					<div class="flex flex-col">
						<div class="svws-title">Schild2-Schema migrieren</div>
						<div class="svws-description">Daten werden über die Auswahl einer existierenden Schild 2-Datenbank importiert.</div>
					</div>
				</button>
				<s-schema-migrate-card v-if="eintrag !== undefined && currentAction === 'migrate'" :set-current-action="setCurrentAction" :migrate-schema="migrateSchema" :target-schema="eintrag.name" :migration-quellinformationen="migrationQuellinformationen" />
				<button role="button" class="svws-ui-content-button" :class="{'svws-not-active': currentAction && currentAction !== 'import', 'svws-active': currentAction === 'import'}" @click="currentAction = 'import'" title="Schema aus Backup wiederherstellen">
					<div class="svws-icon"><span class="icon i-ri-device-recover-line" /></div>
					<div class="flex flex-col">
						<div class="svws-title">Backup wiederherstellen</div>
						<div class="svws-description">Daten werden aus einem Backup wiederhergestellt</div>
					</div>
				</button>
				<s-schema-import-card v-if="eintrag !== undefined && currentAction === 'import'" :set-current-action="setCurrentAction" :restore-schema="restoreSchema" />
			</div>

			<div class="flex flex-wrap gap-2 mt-10">
				<svws-ui-button @click="getBackupSchema" title="SQLite-Schema als Backup erstellen" class="mr-3" :disabled="apiStatus.pending || !eintrag.isSVWS"> <span class="icon i-ri-save-3-line h-[1.5em] w-[1.5em] !-m-[0.3em] !-mr-[0.1em]" /> Backup erstellen </svws-ui-button>
				<template v-if="revisionUpToDate">
					<svws-ui-button type="secondary" @click="upgradeSchema" title="Schema auf aktuelle Revision hochsetzen"> <span class="icon i-ri-speed-line h-[1.5em] w-[1.5em] !-m-[0.3em] !-mr-[0.1em]" /> Revision auf {{ revision }} setzen </svws-ui-button>
					<div v-if="eintrag.isTainted" class="opacity-50"><span class="icon i-ri-error-warning-line inline relative -top-0.5 -mr-0.5" /> Achtung, auch nach dem Hochsetzen auf die aktuelle Revision bleibt das Schema „Tainted“.</div>
				</template>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Admin-Benutzer" class="flex-1">
			<svws-ui-table :columns="cols" :items="admins()" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchemaUebersichtProps } from "./SSchemaUebersichtProps";
	import type { DataTableColumn } from "@ui";
	import type { SchulenKatalogEintrag } from "@core";

	const props = defineProps<SchemaUebersichtProps>();

	const eintrag = computed(()=> props.data());
	const schule = ref<SchulenKatalogEintrag>()
	const loading = ref<boolean>(false);
	const currentAction = ref<string>("");
	const setCurrentAction = (action: string) => {
		currentAction.value = action;
	}

	const revisionUpToDate = computed<boolean>(()=> {
		const revServer = props.revision;
		if (eintrag.value === undefined || revServer === null || eintrag.value.revision < 0)
			return false;
		return revServer !== eintrag.value.revision;
	})

	const cols: DataTableColumn[] = [
		{ key: "anzeigename", label: "Name", span: 2 },
		{ key: "name", label: "Benutzername", span: 1, sortable: true },
	];

	async function getBackupSchema() {
		const { data, name } = await props.backupSchema();
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
