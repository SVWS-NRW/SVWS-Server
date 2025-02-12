<template>
	<div class="page page-flex-col overflow-x-auto">
		<div v-if="auswahlGruppe.length > 0" class="flex flex-col gap-y-4">
			<div class="min-w-128 max-w-192">
				<ui-card icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Schemata löschen." :is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
					<div>
						<template v-if="checkDeletable[0]">
							<span>Die folgenden Schemata werden gelöscht:</span><br>
							<ul>
								<li v-for="schema in auswahlGruppe" :key="schema.name"> {{ schema.name }} </li>
							</ul>
						</template>
						<template v-else v-for="message in checkDeletable[1]" :key="message">
							<span class="text-ui-danger"> {{ message }} <br> </span>
						</template>
						<svws-ui-button :disabled="!checkDeletable[0] || apiStatus.pending" title="Löschen" @click="removeSchemata" :is-loading="apiStatus.pending" class="mt-4">
							<svws-ui-spinner v-if="apiStatus.pending" spinning />
							<span v-else class="icon i-ri-play-line" />
							Löschen
						</svws-ui-button>
					</div>
				</ui-card>
			</div>
		</div>
		<div v-else class="flex"><svws-ui-spinner spinning /><span>&nbsp;Laden des zuletzt ausgewählten Schemas …</span></div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SchemagruppeProps } from "./SSchemagruppeProps";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";

	const props = defineProps<SchemagruppeProps>();

	const currentAction = ref<'' | 'delete'>('delete');

	function setCurrentAction(newAction: "delete", open: boolean) {
		if(open === true)
			currentAction.value= newAction;
		else
			currentAction.value = "";
	}


	function toggleDelete() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	const checkDeletable = computed<[boolean, List<string>]>(() => {
		const log : List<string> = new ArrayList();
		let result : boolean = true;
		if (currentAction.value === 'delete') {
			for (const schema of props.auswahlGruppe) {
				if (schema.username === props.apiUsername) {
					result = false;
					log.add("Schema " + schema.name + " kann nicht gelöscht werden, da es dem aktuell angemeldeten Benutzer " + schema.username + " zugeordnet ist.");
				}
			}
		} else {
			result = false;
		}
		return [ result, log ];
	});

</script>
