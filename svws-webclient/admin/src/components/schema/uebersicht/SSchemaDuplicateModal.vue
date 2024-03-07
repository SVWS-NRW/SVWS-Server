<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Schema Duplizieren</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model.trim="schema" placeholder="Name des neuen Schemas" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" />
				<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="duplicate" :disabled="loading"> <svws-ui-spinner :spinning="loading" /> Duplizieren </svws-ui-button>
				<svws-ui-button type="secondary" @click="close" :disabled="loading"> Abbrechen </svws-ui-button>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="close"> Schließen </svws-ui-button>
			</template>
		</template>
		<template #modalLogs>
			<log-box :logs="logs" :status="status" />
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { List, SimpleOperationResponse } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		duplicateSchema:  (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
	}>();

	const schema = ref<string>("");
	const user = ref<string>('');
	const password = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();
	const status = ref<boolean>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const openModal = () => {
		showModal().value = true;
	}

	async function duplicate() {
		loading.value = true;
		const formData = new FormData();
		formData.append('schemaUsername', user.value);
		formData.append('schemaUserPassword', password.value);
		const result = await props.duplicateSchema(formData, schema.value);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
		schema.value = '';
	}

	function close() {
		showModal().value = false;
		logs.value = undefined;
		status.value = undefined;
	}
</script>
