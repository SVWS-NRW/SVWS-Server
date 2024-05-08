<template>
	<div class="page--flex">
		<header class="svws-ui-header max-w-[140rem]">
			<div class="svws-ui-header--title gap-x-8 lg:gap-x-16 w-full">
				<div class="svws-headline-wrapper flex-[2]">
					<h2 class="svws-headline" />
					<span class="svws-subline">
						Neues Schema anlegen
					</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="[]" :hidden="[]" :model-value="{ name: 'Dummy', text: 'Dummy' }">
			<div class="page--content">
				<div class="flex flex-col gap-y-16 lg:gap-y-20">
					<svws-ui-content-card>
						<!-- Neues leeres Schema anlegen -->
						<s-schema-action-button title="Leeres Schema" description="Es wird ein leeres neues Schema in der neuesten Revision erzeugt. Dieses kann im Anschluss initialisiert werden." icon="i-ri-add-line">
							<s-schema-neu-leer :add-schema="addSchema" :logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
						</s-schema-action-button>
						<!-- Backup in neues Schema importieren -->
						<s-schema-action-button title="Backup importieren" description="Ein SQLite-Backup wird in ein neues Schema wiederhergestellt." icon="i-ri-device-recover-line">
							<s-schema-neu-import :import-schema="importSchema" :logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
						</s-schema-action-button>
						<!-- In Neues Schema migrieren -->
						<s-schema-action-button title="Schild2-Datenbank migrieren" description="Eine Schild2-Datenbank wird in ein neues Schema migriert." icon="i-ri-database-2-line">
							<s-schema-neu-migrate :migrate-schema="migrateSchema" :migration-quellinformationen="migrationQuellinformationen"
								:logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
						</s-schema-action-button>
						<!-- Das ausgewählte Schema in ein neues Schema duplizieren -->
						<s-schema-action-button title="Auswahl duplizieren" description="Dupliziert das aktuell ausgewählte Schema in ein neues Schema." icon="i-ri-file-copy-line">
							<s-schema-neu-duplicate :duplicate-schema="duplicateSchema" :logs="logsFunction" :loading="loadingFunction" :status="statusFunction" />
						</s-schema-action-button>
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
