<template>
	<ui-card icon="i-ri-share-forward-2-line" title="In Konfiguration aufnehmen"
		subtitle="Das Schema wird mit dem angegebenen Benutzer und Kennwort in die Konfiguration der SVWS-Servers aufgenommen."
		:is-open="isOpen" @update:is-open="(isOpen) => emit('opened', isOpen)">
		<div class="input-wrapper mt-2">
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loading" :valid="value => value !== 'root'" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loading" type="password" />
			<svws-ui-spacing />
		</div>
		<template #buttonFooterLeft>
			<svws-ui-button :disabled="(props.schema === undefined) || user.length === 0 || (user === 'root') || loading" :is-loading="loading" title="Hinzufügen" @click="actionFunction">
				<svws-ui-spinner v-if="loading" spinning />
				<span v-else class="icon i-ri-play-line" />
				Hinzufügen
			</svws-ui-button>
		</template>
	</ui-card>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { List } from "@core/java/util/List";
	import { BenutzerKennwort } from "@core/core/data/BenutzerKennwort";
	import { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";

	const props = defineProps<{
		addExistingSchemaToConfig: ((data: BenutzerKennwort, schema: string) => Promise<void>);
		schema: string;
		setLogs: (value: List<string | null> | undefined) => void;
		setStatus: (value: boolean | undefined) => void;
		loading: boolean;
		setLoading: (value: boolean) => void;
		isOpen: boolean;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
	}>();

	const user = ref<string>('');
	const password = ref<string>('');

	async function actionFunction() {
		props.setLoading(true);
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		const result = new SimpleOperationResponse();
		result.success = true;
		await props.addExistingSchemaToConfig(data, props.schema);
		user.value = '';
		password.value = '';
		props.setLoading(false);
		clear();
	}

	function clear() {
		props.setLogs(undefined);
		props.setStatus(undefined);
		props.setLoading(false);
		user.value = '';
		password.value = '';
	}

</script>
