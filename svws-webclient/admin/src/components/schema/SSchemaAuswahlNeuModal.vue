<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>Neues Schema anlegen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model.trim="schemaname" required placeholder="Schemaname" :disabled="loading" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loading" />
				<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loading" type="password" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="add" :disabled="(schemaname.length === 0) || user.length === 0 || loading">
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

	import { ref } from "vue";
	import { BenutzerKennwort, SimpleOperationResponse, type List } from "@core";

	const props = defineProps<{
		addSchema: ((data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>);
	}>();

	const schemaname = ref<string>('');
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
		loading.value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		result = await props.addSchema(data, schemaname.value);
		logs.value = result.log;
		status.value = result.success;
		schemaname.value = '';
		user.value = '';
		password.value = '';
		loading.value = false;
		if (result.success)
			close();
	}

	function close() {
		showModal().value = false;
		logs.value = undefined;
		status.value = undefined;
		user.value = '';
		password.value = '';
	}

</script>
