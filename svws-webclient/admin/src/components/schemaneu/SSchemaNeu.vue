<template>
	<div class="page--flex">
		<header class="svws-ui-header max-w-[140rem]">
			<div class="svws-ui-header--title gap-x-8 lg:gap-x-16 w-full">
				<div class="svws-headline-wrapper flex-[2]">
					<h2 class="svws-headline">
						<span>Neues Schema anlegen</span>
					</h2>
					<span class="svws-subline" />
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="[]" :hidden="[]" :model-value="{ name: 'Dummy', text: 'Dummy' }">
			<div class="page--content !flex flex-col">
				<div class="flex flex-row gap-8 h-full w-full">
					<div class="flex-grow">
						<!-- Neues leeres Schema anlegen -->
						<button role="button" class="svws-ui-content-button" :class="{'svws-active': currentAction === 'neu'}" @click="clickNeuesSchema">
							<div class="svws-icon"><span class="icon i-ri-add-line" /></div>
							<div class="flex flex-col">
								<div class="svws-title">Neues Schema: Leer</div>
								<div class="svws-description">Es wird ein leeres neues Schema in der neuesten Revision erzeugt. Dieses kann im Anschluss initialisiert werden.</div>
							</div>
						</button>
						<s-schema-neu-leer v-if="currentAction === 'neu'" class="ml-4 mt-4 mb-4" :add-schema="addSchema" :logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
						<!-- Backup in neues Schema importieren -->
						<button role="button" class="svws-ui-content-button" :class="{'svws-active': currentAction === 'import'}" @click="clickImportSchema">
							<div class="svws-icon"><span class="icon i-ri-device-recover-line" /></div>
							<div class="flex flex-col">
								<div class="svws-title">Neues Schema: Importiere Backup</div>
								<div class="svws-description">Ein SQLite-Backup wird in ein neues Schema wiederhergestellt.</div>
							</div>
						</button>
						<s-schema-neu-import v-if="currentAction === 'import'" class="ml-4 mt-4 mb-4" :import-schema="importSchema" :logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
						<!-- In Neues Schema migrieren -->
						<button role="button" class="svws-ui-content-button" :class="{'svws-active': currentAction === 'migrate'}" @click="clickMigrateSchema">
							<div class="svws-icon"><span class="icon i-ri-database-2-line" /></div>
							<div class="flex flex-col">
								<div class="svws-title">Neues Schema: Migriere Schild2-Datenbank</div>
								<div class="svws-description">Eine Schild2-Datenbank wird in ein neues Schema migriert.</div>
							</div>
						</button>
						<s-schema-neu-migrate v-if="currentAction === 'migrate'" class="ml-4 mt-4 mb-4" :migrate-schema="migrateSchema" :migration-quellinformationen="migrationQuellinformationen"
							:logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
						<!-- Das ausgewählte Schema in ein neues Schema duplizieren -->
						<button role="button" class="svws-ui-content-button" :class="{'svws-active': currentAction === 'duplicate'}" @click="clickDuplicateSchema">
							<div class="svws-icon"><span class="icon i-ri-file-copy-line" /></div>
							<div class="flex flex-col">
								<div class="svws-title">Neues Schema: Dupliziere Auswahl</div>
								<div class="svws-description">Dupliziert das aktuell ausgewählte Schema in ein neues Schema.</div>
							</div>
						</button>
						<s-schema-neu-duplicate v-if="currentAction === 'duplicate'" class="ml-4 mt-4 mb-4" :duplicate-schema="duplicateSchema" :logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
					</div>
				</div>
				<div class="w-full flex-grow">
					<log-box :logs="logs" :status="status">
						<template #button>
							<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen </svws-ui-button>
						</template>
					</log-box>
				</div>
			</div>
		</svws-ui-router-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import { ref, shallowRef } from "vue";
	import type { SchemaNeuProps } from "./SSchemaNeuProps";
	import type { List } from "@core";

	const props = defineProps<SchemaNeuProps>();

	const loading = ref<boolean>(false);
	const logs = shallowRef<List<string|null> | undefined>(undefined);
	const status = shallowRef<boolean | undefined>(undefined);
	const currentAction = ref<'' | 'neu' | 'import' | 'migrate' | 'duplicate'>('');

	const logsFunction = () => logs;
	const loadingFunction = () => loading;
	const statusFunction = () => status;

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function clickNeuesSchema() {
		currentAction.value = (currentAction.value === 'neu') ? '' : 'neu';
		clearLog();
	}

	async function clickImportSchema() {
		currentAction.value = (currentAction.value === 'import') ? '' : 'import';
		clearLog();
	}

	async function clickMigrateSchema() {
		currentAction.value = (currentAction.value === 'migrate') ? '' : 'migrate';
		clearLog();
	}

	async function clickDuplicateSchema() {
		currentAction.value = (currentAction.value === 'duplicate') ? '' : 'duplicate';
		clearLog();
	}


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
