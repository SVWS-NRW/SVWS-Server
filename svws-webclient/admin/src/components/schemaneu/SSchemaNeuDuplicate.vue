<template>
	<ui-card icon="i-ri-file-copy-line" :title="`Schema „${schema}“ duplizieren`" subtitle="Dupliziert das aktuell ausgewählte Schema in ein neues Schema." :is-open @update:is-open="(open) => emit('opened', open)">
		<div class="input-wrapper mt-2">
			<svws-ui-text-input v-model.trim="schemaNeu" placeholder="Name des neuen Schemas" />
			<svws-ui-spacing />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :valid="validatorUsername" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" />
		</div>
		<template #buttonFooterLeft>
			<svws-ui-button :disabled="(user === 'root') || (schemaNeu.length === 0) || (user.length === 0) || (password.length === 0) || (schema === schemaNeu) || loading" title="Duplizieren" @click="actionFunction" :is-loading="loading" class="mt-4">
				<svws-ui-spinner v-if="loading" spinning />
				<span v-else class="icon i-ri-play-line" />
				Duplizieren
			</svws-ui-button>
		</template>
	</ui-card>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";

	const props = defineProps<{
		duplicateSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
		setLogs: (value: List<string | null> | undefined) => void;
		setStatus: (value: boolean | undefined) => void;
		loading: boolean;
		setLoading: (value: boolean) => void;
		validatorUsername: (username: string | null) => boolean;
		isOpen: boolean;
		schema: string;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
	}>();

	const schemaNeu = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');

	async function actionFunction() {
		props.setLoading(true);
		const formData = new FormData();
		formData.append('schemaUsername', user.value);
		formData.append('schemaUserPassword', password.value);
		const result = await props.duplicateSchema(formData, schemaNeu.value);
		props.setLogs(result.log);
		props.setStatus(result.success);
		props.setLoading(false);
		schemaNeu.value = '';
	}

</script>
