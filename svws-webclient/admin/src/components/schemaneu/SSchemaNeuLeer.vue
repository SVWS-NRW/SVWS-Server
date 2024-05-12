<template>
	<svws-ui-action-button title="Leeres Schema" description="Es wird ein leeres neues Schema in der neuesten Revision erzeugt. Dieses kann im Anschluss initialisiert werden." icon="i-ri-add-line" :action-function="add" :action-disabled="(schemaname.length === 0) || (user.length === 0) || (password.length === 0) || (user === 'root')" :is-loading="loading().value" action-label="Schema anlegen">
		<div class="input-wrapper">
			<svws-ui-text-input v-model.trim="schemaname" required placeholder="Schemaname" :disabled="loading().value" />
			<svws-ui-spacing />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loading().value" :valid="value => value !== 'root'" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loading().value" type="password" />
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { type ShallowRef, ref } from "vue";
	import { BenutzerKennwort, SimpleOperationResponse, type List } from "@core";

	const props = defineProps<{
		addSchema: ((data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>);
		logs: () => ShallowRef<List<string | null> | undefined>;
		status: () => ShallowRef<boolean | undefined>;
		loading: () => ShallowRef<boolean>;
	}>();

	const schemaname = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');

	async function add() {
		props.loading().value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		result = await props.addSchema(data, schemaname.value);
		props.logs().value = result.log;
		props.status().value = result.success;
		schemaname.value = '';
		user.value = '';
		password.value = '';
		props.loading().value = false;
	}

</script>
