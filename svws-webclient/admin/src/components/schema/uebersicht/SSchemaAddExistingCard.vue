<template>
	<svws-ui-action-button title="In Konfiguration aufnehmen" description="Das Schema wird mit dem angegebenen Benutzer und Kennwort in die Konfiguration der SVWS-Servers aufgenommen." icon="i-ri-share-forward-2-line" :action-function="add" :action-disabled="(props.schema === undefined) || user.length === 0 || (user === 'root')" :is-loading="loading().value" action-label="Hinzufügen">
		<div class="input-wrapper">
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loading().value" :valid="value => value !== 'root'" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loading().value" type="password" />
			<svws-ui-spacing />
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { type ShallowRef, ref } from "vue";
	import { BenutzerKennwort, SimpleOperationResponse, type List } from "@core";

	const props = defineProps<{
		addExisting: ((data: BenutzerKennwort, schema: string) => Promise<void>);
		schema: string;
		logs: () => ShallowRef<List<string | null> | undefined>;
		status: () => ShallowRef<boolean | undefined>;
		loading: () => ShallowRef<boolean>;
	}>();

	const user = ref<string>('');
	const password = ref<string>('');

	async function add() {
		props.loading().value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		result.success = true;
		await props.addExisting(data, props.schema);
		user.value = '';
		password.value = '';
		props.loading().value = false;
		if (result.success)
			clear();
	}

	function clear() {
		props.logs().value = undefined;
		props.status().value = undefined;
		props.loading().value = false;
		user.value = '';
		password.value = '';
	}

</script>
