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
		<svws-ui-tab-bar :tab-manager="() => new TabManager([], { name: 'Dummy', text: 'Dummy' }, async (a) => {})">
			<div class="page--content">
				<div class="flex flex-col gap-y-16 lg:gap-y-20">
					<svws-ui-content-card>
						<!-- Neues leeres Schema anlegen -->
						<s-schema-neu-leer :add-schema :logs-function :loading-function :status-function :validator-username :is-active="currentAction === 'neu'" @click="clickNeu" />
						<!-- Backup in neues Schema importieren -->
						<s-schema-neu-restore :import-schema :logs-function :loading-function :status-function :validator-username :is-active="currentAction === 'restore'" @click="clickRestore" />
						<!-- In Neues Schema migrieren -->
						<s-schema-neu-migrate :migrate-schema :migration-quellinformationen :logs-function :loading-function :status-function :validator-username :is-active="currentAction === 'migrate'" @click="clickMigrate" />
						<!-- Das ausgewählte Schema in ein neues Schema duplizieren -->
						<s-schema-neu-duplicate v-if="schema !== undefined" :duplicate-schema :logs-function :loading-function :status-function :is-active="currentAction === 'duplicate'" :validator-username @click="clickDuplicate" :schema />
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
		</svws-ui-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import { ref, shallowRef } from "vue";
	import type { SchemaNeuProps } from "./SSchemaNeuProps";
	import type { List } from "@core/java/util/List";
	import { TabManager } from "@ui/ui/nav/TabManager";

	const props = defineProps<SchemaNeuProps>();

	const loading = ref<boolean>(false);
	const logs = shallowRef<List<string|null> | undefined>(undefined);
	const status = shallowRef<boolean | undefined>(undefined);
	const currentAction = ref<'' | 'neu' | 'restore' | 'migrate' | 'duplicate'>('');

	const logsFunction = () => logs;
	const loadingFunction = () => loading;
	const statusFunction = () => status;

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function clickNeu() {
		if (loading.value)
			return;
		currentAction.value = (currentAction.value === 'neu') ? '' : 'neu';
		clearLog();
	}

	async function clickRestore() {
		if (loading.value)
			return;
		currentAction.value = (currentAction.value === 'restore') ? '' : 'restore';
		clearLog();
	}

	async function clickMigrate() {
		if (loading.value)
			return;
		currentAction.value = (currentAction.value === 'migrate') ? '' : 'migrate';
		clearLog();
	}

	async function clickDuplicate() {
		if (loading.value)
			return;
		currentAction.value = (currentAction.value === 'duplicate') ? '' : 'duplicate';
		clearLog();
	}

	const validatorUsername = (username: string | null) => {
		if ((username === null) || (username === 'root') || (username === props.apiUsername))
			return false;
		return true;
	}

</script>
