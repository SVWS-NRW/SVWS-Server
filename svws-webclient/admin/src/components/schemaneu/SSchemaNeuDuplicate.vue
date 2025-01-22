<template>
	<svws-ui-action-button :title="`Schema „${schema}“ duplizieren`" description="Dupliziert das aktuell ausgewählte Schema in ein neues Schema." icon="i-ri-file-copy-line" action-label="Duplizieren" :action-function
		:disabled="(user === 'root') || (schemaNeu.length === 0) || (user.length === 0) || (password.length === 0) || (schema === schemaNeu) || loadingFunction().value" :is-loading="loadingFunction().value" :is-active>
		<div class="input-wrapper">
			<svws-ui-text-input v-model.trim="schemaNeu" placeholder="Name des neuen Schemas" />
			<svws-ui-spacing />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :valid="validatorUsername" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" />
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { ref, type ShallowRef } from "vue";
	import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";

	const props = defineProps<{
		duplicateSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		validatorUsername: (username: string | null) => boolean;
		isActive: boolean;
		schema: string;
	}>();

	const schemaNeu = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');

	async function actionFunction() {
		props.loadingFunction().value = true;
		const formData = new FormData();
		formData.append('schemaUsername', user.value);
		formData.append('schemaUserPassword', password.value);
		const result = await props.duplicateSchema(formData, schemaNeu.value);
		props.logsFunction().value = result.log;
		props.statusFunction().value = result.success;
		props.loadingFunction().value = false;
		schemaNeu.value = '';
	}

</script>
