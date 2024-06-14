<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-action-button title="Löschen" description="Ausgewählte Schemata löschen." icon="i-ri-delete-bin-line"
				:action-function="removeSchemata" action-label="Löschen" :is-loading="apiStatus.pending" :is-active="currentAction === 'delete'"
				:action-disabled="!checkDeletable[0]" @click="toggleDelete">
				<template v-if="checkDeletable[0]">
					<span>Die folgenden Schemata werden gelöscht:</span><br>
					<ul>
						<li v-for="schema in auswahlGruppe" :key="schema.name"> {{ schema.name }} </li>
					</ul>
				</template>
				<template v-else v-for="message in checkDeletable[1]" :key="message">
					<span class="text-error"> {{ message }} <br> </span>
				</template>
			</svws-ui-action-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SchemagruppeProps } from "./SSchemagruppeProps";
	import { type List, ArrayList } from "@core";

	const props = defineProps<SchemagruppeProps>();

	const currentAction = ref<'' | 'delete'>('delete');

	function toggleDelete() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
	}

	const checkDeletable = computed<Array<boolean, boolean, List<string>>>(() => {
		let log : List<string> = new ArrayList();
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
