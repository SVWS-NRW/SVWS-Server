<template>
	<div class="page page-flex-col">
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
			<div class="page page-flex-row">
				<div class="min-w-fit flex flex-col gap-y-4 overflow-y-auto px-6">
					<!-- Neues leeres Schema anlegen -->
					<s-schema-neu-leer :add-schema :loading :set-status :validator-username :is-open="currentAction === 'neu'" @opened="(isOpen) => setCurrentAction('neu', isOpen)" />
					<!-- Backup in neues Schema importieren -->
					<s-schema-neu-restore :import-schema :loading :set-status :validator-username :is-open="currentAction === 'restore'" @opened="(isOpen) => setCurrentAction('restore', isOpen)" />
					<!-- In Neues Schema migrieren -->
					<s-schema-neu-migrate :migrate-schema :migration-quellinformationen :loading :set-status :validator-username :is-open="currentAction === 'migrate'" @opened="(isOpen) => setCurrentAction('migrate', isOpen)" />
					<!-- Das ausgewählte Schema in ein neues Schema duplizieren -->
					<s-schema-neu-duplicate v-if="schema !== undefined" :duplicate-schema :loading :set-status :is-open="currentAction === 'duplicate'" @opened="(isOpen) => setCurrentAction('duplicate', isOpen)" :validator-username :schema />
				</div>
				<div class="min-w-fit grow">
					<log-box :logs="logs" :status="status">
						<template #button>
							<svws-ui-button v-if="status !== undefined" type="transparent" @click="clear" title="Log verwerfen">Log verwerfen </svws-ui-button>
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

	const currentAction = ref<string>('');
	const oldAction = ref({
		name: "",
		open: false,
	});

	function setStatus(loadingV: boolean, statusV?: boolean, logsV?: List<string | null>) {
		logs.value = logsV;
		loading.value = loadingV;
		status.value = statusV;
	}

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

	function clear() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	const validatorUsername = (username: string | null) => {
		if ((username === null) || (username === 'root') || (username === props.apiUsername))
			return false;
		return true;
	}

// bg-ui-contrast-100 text-ui-contrast-0 rounded-xl max-h-96 w-full Bugfix Tailwind TODO
</script>
