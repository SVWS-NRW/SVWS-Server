<template>
	<ui-card icon="i-ri-share-forward-2-line" title="In Konfiguration aufnehmen"
		subtitle="Das Schema wird mit dem angegebenen Benutzer und Kennwort in die Konfiguration der SVWS-Servers aufgenommen."
		:is-open="isOpen" @update:is-open="(isOpen) => emit('opened', isOpen)">
		<div class="input-wrapper mt-2">
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loadingFunction().value" :valid="value => value !== 'root'" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loadingFunction().value" type="password" />
			<svws-ui-spacing />
			<svws-ui-button :disabled="(props.schema === undefined) || user.length === 0 || (user === 'root') || loadingFunction().value" :is-loading="loadingFunction().value" title="Hinzufügen" @click="actionFunction">
				<svws-ui-spinner v-if="loadingFunction().value" spinning />
				<span v-else class="icon i-ri-play-line" />
				Hinzufügen
			</svws-ui-button>
		</div>
	</ui-card>
</template>

<script setup lang="ts">

	import { BenutzerKennwort } from "@core/core/data/BenutzerKennwort";
	import { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";
	import { type ShallowRef, ref } from "vue";

	const props = defineProps<{
		addExistingSchemaToConfig: ((data: BenutzerKennwort, schema: string) => Promise<void>);
		schema: string;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		isOpen: boolean;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
	}>();


	const user = ref<string>('');
	const password = ref<string>('');

	async function actionFunction() {
		props.loadingFunction().value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		const result = new SimpleOperationResponse();
		result.success = true;
		await props.addExistingSchemaToConfig(data, props.schema);
		user.value = '';
		password.value = '';
		props.loadingFunction().value = false;
		clear();
	}

	function clear() {
		props.logsFunction().value = undefined;
		props.statusFunction().value = undefined;
		props.loadingFunction().value = false;
		user.value = '';
		password.value = '';
	}

</script>
