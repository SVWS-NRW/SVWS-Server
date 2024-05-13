<template>
	<svws-ui-action-button title="In Konfiguration aufnehmen" description="Das Schema wird mit dem angegebenen Benutzer und Kennwort in die Konfiguration der SVWS-Servers aufgenommen." icon="i-ri-share-forward-2-line" :action-function :action-disabled="(props.schema === undefined) || user.length === 0 || (user === 'root')" :is-loading="loadingFunction().value" action-label="Hinzufügen" :is-active>
		<div class="input-wrapper">
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loadingFunction().value" :valid="value => value !== 'root'" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loadingFunction().value" type="password" />
			<svws-ui-spacing />
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { type ShallowRef, ref } from "vue";
	import { BenutzerKennwort, SimpleOperationResponse, type List } from "@core";

	const props = defineProps<{
		addExistingSchemaToConfig: ((data: BenutzerKennwort, schema: string) => Promise<void>);
		schema: string;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		isActive: boolean;
	}>();

	const user = ref<string>('');
	const password = ref<string>('');

	async function actionFunction() {
		props.loadingFunction().value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		result.success = true;
		await props.addExistingSchemaToConfig(data, props.schema);
		user.value = '';
		password.value = '';
		props.loadingFunction().value = false;
		if (result.success)
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
