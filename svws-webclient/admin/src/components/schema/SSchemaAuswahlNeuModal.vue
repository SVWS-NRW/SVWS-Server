<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Neues Schema anlegen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="schema" required placeholder="Schemaname" :disabled="loading" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model="user" required placeholder="Benutzername" :disabled="loading" />
				<svws-ui-text-input v-model="password" required placeholder="Passwort" :disabled="loading" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="add" :disabled="schema.length === 0 || user.length === 0 || loading">
					<svws-ui-spinner :spinning="loading" />
					Schema anlegen
				</svws-ui-button>
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
	import { BenutzerKennwort } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addSchema:  (data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>;
	}>();

	const schema = ref<string>('');
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

	async function add() {
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		loading.value = true;
		const result = await props.addSchema(data, schema.value);
		logs.value = result.log;
		status.value = result.success;
		schema.value = '';
		user.value = '';
		password.value = '';
		loading.value = false;
		schema.value = '';
		user.value = '';
		password.value = '';
		if (result.success)
			close();
	}

	function close() {
		showModal().value = false;
		logs.value = undefined;
		status.value = undefined;
	}
</script>
