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
						<svws-ui-button :disabled="!checkDeletable[0] || apiStatus.pending" @click="removeSchemata" :is-loading="apiStatus.pending" class="mt-4">
							<svws-ui-spinner v-if="apiStatus.pending" spinning />
							<span v-else class="icon i-ri-play-line" />
							Löschen
						</svws-ui-button>
					</div>
				</ui-card>
				<ui-card icon="i-ri-save-3-line" title="Backup" subtitle="Ausgewählte Schemata als Backup sichern." :is-open="currentAction === 'backup'" @update:is-open="(isOpen) => setCurrentAction('backup', isOpen)">
					<div>
						Die folgenden Schemata werden heruntergeladen:<br>
						<ul>
							<li v-for="schema, i in auswahlGruppe" :key="schema.name" class="flex justify-between w-full h-10">
								<span v-if="backupFiles().length === 0">{{ schema.name }}</span>
								<svws-ui-button v-else @click="getBackupFile(i)" :disabled="backupFiles()[i] === undefined">
									<svws-ui-spinner :spinning="backupFiles()[i] === undefined" /> {{ schema.name }} {{ (backupFiles()[i] !== undefined) ? "Herunterladen" : "Erstellen ..." }}
								</svws-ui-button>
							</li>
						</ul>
						<svws-ui-button :disabled="apiStatus.pending" @click="backupSchemata" :is-loading="apiStatus.pending" class="mt-4">
							<svws-ui-spinner v-if="apiStatus.pending" spinning />
							<span v-else class="icon i-ri-play-line" />
							Backup erstellen
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

	type Action = 'delete' | 'backup';

	const props = defineProps<SchemagruppeProps>();

	const currentAction = ref<Action | undefined>(undefined);
	const oldAction = ref<{ name: Action | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});

	function setCurrentAction(newAction: Action, open: boolean) {
		if (newAction === oldAction.value.name && !open) {
			return;
		}
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value !== undefined);
		if (open === true) {
			currentAction.value = newAction;
		} else {
			currentAction.value = undefined;
		}
	}

	const checkDeletable = computed<[boolean, List<string>]>(() => {
		const log: List<string> = new ArrayList();
		let result = true;
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
		return [result, log];
	});

	function getBackupFile(i: number) {
		const { data, name } = props.backupFiles()[i];
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
