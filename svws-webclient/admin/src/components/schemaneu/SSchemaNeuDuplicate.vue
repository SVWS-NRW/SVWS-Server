<template>
	<div class="input-wrapper">
		<svws-ui-text-input v-model.trim="schema" placeholder="Name des neuen Schemas" />
		<svws-ui-spacing />
		<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :valid="value => value !== 'root'" />
		<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" />
		<svws-ui-spacing />
		<svws-ui-button type="primary" @click="duplicate" :disabled="loading().value || (user === 'root') || (schema.length === 0) || (user.length === 0) || (password.length === 0)">
			<svws-ui-spinner :spinning="loading().value" />
			Duplizieren
		</svws-ui-button>
	</div>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { ref, type ShallowRef } from "vue";

	const props = defineProps<{
		duplicateSchema:  (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
		logs: () => ShallowRef<List<string | null> | undefined>;
		status: () => ShallowRef<boolean | undefined>;
		loading: () => ShallowRef<boolean>;
	}>();

	const schema = ref<string>("");
	const user = ref<string>('');
	const password = ref<string>('');

	async function duplicate() {
		props.loading().value = true;
		const formData = new FormData();
		formData.append('schemaUsername', user.value);
		formData.append('schemaUserPassword', password.value);
		const result = await props.duplicateSchema(formData, schema.value);
		props.logs().value = result.log;
		props.status().value = result.success;
		props.loading().value = false;
		schema.value = '';
	}

</script>
