<template>
	<div class="input-wrapper">
		<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loading().value" :valid="value => value !== 'root'" />
		<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loading().value" type="password" />
		<svws-ui-spacing />
		<svws-ui-button type="primary" @click="add" :disabled="(props.schema === undefined) || user.length === 0 || loading().value || (user === 'root')">
			<svws-ui-spinner :spinning="loading().value" />
			Hinzufügen
		</svws-ui-button>
	</div>
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
